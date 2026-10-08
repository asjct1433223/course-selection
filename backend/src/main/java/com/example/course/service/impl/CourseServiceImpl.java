package com.example.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.course.entity.Course;
import com.example.course.entity.Sc;
import com.example.course.entity.Teacher;
import com.example.course.mapper.CourseMapper;
import com.example.course.mapper.ScMapper;
import com.example.course.mapper.TeacherMapper;
import com.example.course.service.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程管理服务实现（含选课/退课核心业务）
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Resource
    private ScMapper scMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Override
    public IPage<Course> pageWithTeacher(Integer current, Integer size, String keyword) {
        String kw = StringUtils.hasText(keyword) ? keyword.trim() : null;
        Page<Course> page = new Page<>(current == null ? 1 : current, size == null ? 10 : size);
        return baseMapper.selectPageWithTeacher(page, kw);
    }

    @Override
    public List<Course> listWithTeacher() {
        return baseMapper.selectListWithTeacher();
    }

    @Override
    public Course getWithTeacherById(Long id) {
        return baseMapper.selectByIdWithTeacher(id);
    }

    @Override
    public boolean addCourse(Course course) {
        if (course == null || !StringUtils.hasText(course.getCourseNo())
                || !StringUtils.hasText(course.getName())) {
            throw new RuntimeException("课程编号和课程名称不能为空");
        }
        Long count = baseMapper.selectCount(new LambdaQueryWrapper<Course>()
                .eq(Course::getCourseNo, course.getCourseNo().trim()));
        if (count != null && count > 0) {
            throw new RuntimeException("课程编号已存在");
        }
        checkTeacherExists(course.getTeacherId());

        course.setCourseNo(course.getCourseNo().trim());
        if (course.getSelectedNum() == null) {
            course.setSelectedNum(0);
        }
        if (course.getCapacity() == null) {
            course.setCapacity(60);
        }
        return save(course);
    }

    @Override
    public boolean updateCourse(Course course) {
        if (course == null || course.getId() == null) {
            throw new RuntimeException("课程ID不能为空");
        }
        if (StringUtils.hasText(course.getCourseNo())) {
            Long count = baseMapper.selectCount(new LambdaQueryWrapper<Course>()
                    .eq(Course::getCourseNo, course.getCourseNo().trim())
                    .ne(Course::getId, course.getId()));
            if (count != null && count > 0) {
                throw new RuntimeException("课程编号已存在");
            }
        }
        checkTeacherExists(course.getTeacherId());
        return updateById(course);
    }

    @Override
    public boolean deleteCourse(Long id) {
        Course course = getById(id);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        if (course.getSelectedNum() != null && course.getSelectedNum() > 0) {
            throw new RuntimeException("该课程已有学生选课，不能删除");
        }
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean selectCourse(Long studentId, Long courseId) {
        // 1. 检查课程是否存在
        Course course = baseMapper.selectById(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }
        // 2. 检查课容量
        if (course.getSelectedNum() != null && course.getSelectedNum() >= course.getCapacity()) {
            throw new RuntimeException("课程已满，无法选课");
        }
        // 3. 检查是否重复选课
        if (scMapper.countByStudentAndCourse(studentId, courseId) > 0) {
            throw new RuntimeException("已选过该课程，不能重复选课");
        }
        // 4. 更新已选人数
        int selected = course.getSelectedNum() == null ? 0 : course.getSelectedNum();
        course.setSelectedNum(selected + 1);
        baseMapper.updateById(course);
        // 5. 插入选课记录
        Sc sc = new Sc();
        sc.setStudentId(studentId);
        sc.setCourseId(courseId);
        return scMapper.insert(sc) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dropCourse(Long studentId, Long courseId) {
        // 1. 检查是否已选
        if (scMapper.countByStudentAndCourse(studentId, courseId) == 0) {
            throw new RuntimeException("未选该课程，无法退课");
        }
        // 2. 更新课程已选人数（不能小于0）
        Course course = baseMapper.selectById(courseId);
        if (course != null) {
            int selected = course.getSelectedNum() == null ? 0 : course.getSelectedNum();
            course.setSelectedNum(Math.max(0, selected - 1));
            baseMapper.updateById(course);
        }
        // 3. 删除选课记录
        LambdaQueryWrapper<Sc> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Sc::getStudentId, studentId).eq(Sc::getCourseId, courseId);
        return scMapper.delete(wrapper) > 0;
    }

    /** 校验授课教师是否存在 */
    private void checkTeacherExists(Long teacherId) {
        if (teacherId == null) {
            throw new RuntimeException("请选择授课教师");
        }
        Teacher teacher = teacherMapper.selectById(teacherId);
        if (teacher == null) {
            throw new RuntimeException("授课教师不存在");
        }
    }
}
