package com.example.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.course.entity.Course;

import java.util.List;

/**
 * 课程服务接口（含选课/退课业务）
 */
public interface CourseService extends IService<Course> {

    /** 分页查询课程（含授课教师姓名） */
    IPage<Course> pageWithTeacher(Integer current, Integer size, String keyword);

    /** 查询所有课程（含授课教师姓名） */
    List<Course> listWithTeacher();

    /** 按ID查询课程（含授课教师姓名） */
    Course getWithTeacherById(Long id);

    /** 新增课程 */
    boolean addCourse(Course course);

    /** 修改课程 */
    boolean updateCourse(Course course);

    /** 删除课程（已有学生选课的课程不允许删除） */
    boolean deleteCourse(Long id);

    /** 学生选课 */
    boolean selectCourse(Long studentId, Long courseId);

    /** 学生退课 */
    boolean dropCourse(Long studentId, Long courseId);
}
