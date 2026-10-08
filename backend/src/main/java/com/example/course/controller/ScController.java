package com.example.course.controller;

import com.example.course.common.Result;
import com.example.course.entity.Sc;
import com.example.course.service.ScService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 选课记录接口（成绩管理）
 */
@RestController
@RequestMapping("/api/sc")
public class ScController {

    @Resource
    private ScService scService;

    /** 查询某学生的选课记录 */
    @GetMapping("/student/{studentId}")
    public Result<List<Sc>> getByStudentId(@PathVariable Long studentId) {
        return Result.ok(scService.getByStudentId(studentId));
    }

    /** 查询某课程的选课学生 */
    @GetMapping("/course/{courseId}")
    public Result<List<Sc>> getByCourseId(@PathVariable Long courseId) {
        return Result.ok(scService.getByCourseId(courseId));
    }

    /** 查询所有选课记录 */
    @GetMapping("/list")
    public Result<List<Sc>> list() {
        return Result.ok(scService.listAll());
    }

    /** 录入/修改成绩 */
    @PutMapping("/score")
    public Result<?> updateScore(@RequestParam Long scId, @RequestParam Double score) {
        scService.updateScore(scId, score);
        return Result.ok("成绩录入成功");
    }
}
