package com.example.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 选课记录实体（sc 表），学生与课程的多对多中间表
 */
@Data
@TableName("sc")
public class Sc {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 学生ID（外键 → student.id） */
    private Long studentId;

    /** 课程ID（外键 → course.id） */
    private Long courseId;

    /** 成绩（可为空） */
    private Double score;

    /** 选课时间 */
    private LocalDateTime createTime;

    /** 学生姓名（非数据库字段，联表查询） */
    @TableField(exist = false)
    private String studentName;

    /** 学号（非数据库字段，联表查询） */
    @TableField(exist = false)
    private String studentNo;

    /** 课程名称（非数据库字段，联表查询） */
    @TableField(exist = false)
    private String courseName;
}
