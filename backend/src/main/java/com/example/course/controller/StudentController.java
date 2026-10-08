package com.example.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.course.common.Result;
import com.example.course.entity.Student;
import com.example.course.service.StudentService;
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
 * 学生管理接口
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    /**
     * 分页查询学生
     */
    @GetMapping("/page")
    public Result<IPage<Student>> page(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String keyword) {
        return Result.ok(studentService.pageWithUser(current, size, keyword));
    }

    /**
     * 查询所有学生
     */
    @GetMapping("/list")
    public Result<List<Student>> list() {
        return Result.ok(studentService.listWithUser());
    }

    /**
     * 按ID查询学生
     */
    @GetMapping("/{id}")
    public Result<Student> getById(@PathVariable Long id) {
        return Result.ok(studentService.getWithUserById(id));
    }

    /**
     * 新增学生（同时创建登录账号）
     */
    @PostMapping
    public Result<?> save(@RequestBody Student student) {
        studentService.addStudent(student);
        return Result.ok();
    }

    /**
     * 修改学生
     */
    @PutMapping
    public Result<?> update(@RequestBody Student student) {
        studentService.updateStudent(student);
        return Result.ok();
    }

    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return Result.ok();
    }
}
