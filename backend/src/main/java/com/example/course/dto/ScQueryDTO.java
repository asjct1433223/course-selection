package com.example.course.dto;

import lombok.Data;

/**
 * 选课记录查询条件（可按学生/课程过滤）
 */
@Data
public class ScQueryDTO {

    /** 学生ID（可选） */
    private Long studentId;

    /** 课程ID（可选） */
    private Long courseId;
}
