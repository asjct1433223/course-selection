package com.example.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.course.common.Result;
import com.example.course.entity.Course;
import com.example.course.service.CourseService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 课程管理接口（含选课/退课）
 */
@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Resource
    private CourseService courseService;

    /** 分页查询课程（含授课教师姓名） */
    @GetMapping("/page")
    public Result<IPage<Course>> page(@RequestParam(defaultValue = "1") Integer current,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String keyword) {
        return Result.ok(courseService.pageWithTeacher(current, size, keyword));
    }

    /** 查询所有课程 */
    @GetMapping("/list")
    public Result<List<Course>> list() {
        return Result.ok(courseService.listWithTeacher());
    }

    /** 按ID查询课程 */
    @GetMapping("/{id}")
    public Result<Course> getById(@PathVariable Long id) {
        return Result.ok(courseService.getWithTeacherById(id));
    }

    /** 新增课程 */
    @PostMapping
    public Result<?> save(@RequestBody Course course) {
        courseService.addCourse(course);
        return Result.ok();
    }

    /** 修改课程 */
    @PutMapping
    public Result<?> update(@RequestBody Course course) {
        courseService.updateCourse(course);
        return Result.ok();
    }

    /** 删除课程 */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return Result.ok();
    }

    /** 学生选课 */
    @PostMapping("/select")
    public Result<?> select(@RequestParam Long studentId, @RequestParam Long courseId) {
        courseService.selectCourse(studentId, courseId);
        return Result.ok("选课成功");
    }

    /** 学生退课 */
    @PostMapping("/drop")
    public Result<?> drop(@RequestParam Long studentId, @RequestParam Long courseId) {
        courseService.dropCourse(studentId, courseId);
        return Result.ok("退课成功");
    }
}
