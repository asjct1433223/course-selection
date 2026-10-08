package com.example.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.course.common.Result;
import com.example.course.entity.Teacher;
import com.example.course.service.TeacherService;
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
 * 教师管理接口
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;

    /**
     * 分页查询教师
     */
    @GetMapping("/page")
    public Result<IPage<Teacher>> page(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String keyword) {
        return Result.ok(teacherService.pageWithUser(current, size, keyword));
    }

    /**
     * 查询所有教师
     */
    @GetMapping("/list")
    public Result<List<Teacher>> list() {
        return Result.ok(teacherService.listWithUser());
    }

    /**
     * 按ID查询教师
     */
    @GetMapping("/{id}")
    public Result<Teacher> getById(@PathVariable Long id) {
        return Result.ok(teacherService.getWithUserById(id));
    }

    /**
     * 新增教师（同时创建登录账号）
     */
    @PostMapping
    public Result<?> save(@RequestBody Teacher teacher) {
        teacherService.addTeacher(teacher);
        return Result.ok();
    }

    /**
     * 修改教师
     */
    @PutMapping
    public Result<?> update(@RequestBody Teacher teacher) {
        teacherService.updateTeacher(teacher);
        return Result.ok();
    }

    /**
     * 删除教师
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return Result.ok();
    }
}
