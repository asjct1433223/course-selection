package com.example.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.course.entity.Sc;

import java.util.List;

/**
 * 选课记录服务接口（成绩管理）
 */
public interface ScService extends IService<Sc> {

    /** 查询某学生的选课记录 */
    List<Sc> getByStudentId(Long studentId);

    /** 查询某课程的选课学生 */
    List<Sc> getByCourseId(Long courseId);

    /** 查询所有选课记录（管理员） */
    List<Sc> listAll();

    /** 录入/修改成绩（0-100分） */
    boolean updateScore(Long scId, Double score);
}
