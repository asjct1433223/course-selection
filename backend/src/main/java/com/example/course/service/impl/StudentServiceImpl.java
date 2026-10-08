package com.example.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.course.entity.Sc;
import com.example.course.entity.Student;
import com.example.course.entity.User;
import com.example.course.mapper.ScMapper;
import com.example.course.mapper.StudentMapper;
import com.example.course.mapper.UserMapper;
import com.example.course.service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 学生管理服务实现
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ScMapper scMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public IPage<Student> pageWithUser(Integer current, Integer size, String keyword) {
        String kw = StringUtils.hasText(keyword) ? keyword.trim() : null;
        Page<Student> page = new Page<>(current == null ? 1 : current, size == null ? 10 : size);
        return baseMapper.selectPageWithUser(page, kw);
    }

    @Override
    public List<Student> listWithUser() {
        return baseMapper.selectListWithUser();
    }

    @Override
    public Student getWithUserById(Long id) {
        return baseMapper.selectByIdWithUser(id);
    }

    @Override
    public Student getByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addStudent(Student student) {
        if (student == null || !StringUtils.hasText(student.getStudentNo())
                || !StringUtils.hasText(student.getName())) {
            throw new RuntimeException("学号和姓名不能为空");
        }
        // 学号唯一校验
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Student>()
                .eq(Student::getStudentNo, student.getStudentNo().trim()));
        if (count != null && count > 0) {
            throw new RuntimeException("学号已存在");
        }
        // 登录账号校验与创建
        if (!StringUtils.hasText(student.getUsername())) {
            throw new RuntimeException("请填写登录账号");
        }
        if (userMapper.selectByUsername(student.getUsername().trim()) != null) {
            throw new RuntimeException("登录账号已存在");
        }
        User user = new User();
        user.setUsername(student.getUsername().trim());
        String rawPassword = StringUtils.hasText(student.getPassword()) ? student.getPassword() : "123456";
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRealName(student.getName());
        user.setRole("STUDENT");
        user.setPhone(student.getPhone());
        user.setEnabled(1);
        userMapper.insert(user);

        // 保存学生并关联用户
        student.setUserId(user.getId());
        student.setStudentNo(student.getStudentNo().trim());
        return save(student);
    }

    @Override
    public boolean updateStudent(Student student) {
        if (student == null || student.getId() == null) {
            throw new RuntimeException("学生ID不能为空");
        }
        // 学号唯一校验（排除自己）
        if (StringUtils.hasText(student.getStudentNo())) {
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<Student>()
                    .eq(Student::getStudentNo, student.getStudentNo().trim())
                    .ne(Student::getId, student.getId()));
            if (count != null && count > 0) {
                throw new RuntimeException("学号已存在");
            }
        }
        return updateById(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStudent(Long id) {
        Student student = getById(id);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        // 级联删除选课记录
        scMapper.delete(new LambdaQueryWrapper<Sc>()
                .eq(Sc::getStudentId, id));
        removeById(id);
        // 删除登录账号
        if (student.getUserId() != null) {
            userMapper.deleteById(student.getUserId());
        }
        return true;
    }
}
