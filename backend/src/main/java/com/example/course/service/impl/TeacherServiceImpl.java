package com.example.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.course.entity.Teacher;
import com.example.course.entity.User;
import com.example.course.mapper.CourseMapper;
import com.example.course.mapper.TeacherMapper;
import com.example.course.mapper.UserMapper;
import com.example.course.service.TeacherService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 教师管理服务实现
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public IPage<Teacher> pageWithUser(Integer current, Integer size, String keyword) {
        String kw = StringUtils.hasText(keyword) ? keyword.trim() : null;
        Page<Teacher> page = new Page<>(current == null ? 1 : current, size == null ? 10 : size);
        return baseMapper.selectPageWithUser(page, kw);
    }

    @Override
    public List<Teacher> listWithUser() {
        return baseMapper.selectListWithUser();
    }

    @Override
    public Teacher getWithUserById(Long id) {
        return baseMapper.selectByIdWithUser(id);
    }

    @Override
    public Teacher getByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTeacher(Teacher teacher) {
        if (teacher == null || !StringUtils.hasText(teacher.getTeacherNo())
                || !StringUtils.hasText(teacher.getName())) {
            throw new RuntimeException("工号和姓名不能为空");
        }
        // 工号唯一校验
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Teacher>()
                .eq(Teacher::getTeacherNo, teacher.getTeacherNo().trim()));
        if (count != null && count > 0) {
            throw new RuntimeException("工号已存在");
        }
        // 登录账号校验与创建
        if (!StringUtils.hasText(teacher.getUsername())) {
            throw new RuntimeException("请填写登录账号");
        }
        if (userMapper.selectByUsername(teacher.getUsername().trim()) != null) {
            throw new RuntimeException("登录账号已存在");
        }
        User user = new User();
        user.setUsername(teacher.getUsername().trim());
        String rawPassword = StringUtils.hasText(teacher.getPassword()) ? teacher.getPassword() : "123456";
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRealName(teacher.getName());
        user.setRole("TEACHER");
        user.setPhone(teacher.getPhone());
        user.setEnabled(1);
        userMapper.insert(user);

        teacher.setUserId(user.getId());
        teacher.setTeacherNo(teacher.getTeacherNo().trim());
        return save(teacher);
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        if (teacher == null || teacher.getId() == null) {
            throw new RuntimeException("教师ID不能为空");
        }
        if (StringUtils.hasText(teacher.getTeacherNo())) {
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<Teacher>()
                    .eq(Teacher::getTeacherNo, teacher.getTeacherNo().trim())
                    .ne(Teacher::getId, teacher.getId()));
            if (count != null && count > 0) {
                throw new RuntimeException("工号已存在");
            }
        }
        return updateById(teacher);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeacher(Long id) {
        Teacher teacher = getById(id);
        if (teacher == null) {
            throw new RuntimeException("教师不存在");
        }
        // 有关联课程的教师不允许删除
        Long courseCount = courseMapper.countByTeacherId(id);
        if (courseCount != null && courseCount > 0) {
            throw new RuntimeException("该教师已关联课程，不能删除");
        }
        removeById(id);
        if (teacher.getUserId() != null) {
            userMapper.deleteById(teacher.getUserId());
        }
        return true;
    }
}
