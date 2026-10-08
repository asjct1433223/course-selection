package com.example.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 课程实体（course 表）
 */
@Data
@TableName("course")
public class Course {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 课程编号 */
    private String courseNo;

    /** 课程名称 */
    private String name;

    /** 学分 */
    private Double credit;

    /** 授课教师ID（外键 → teacher.id） */
    private Long teacherId;

    /** 课容量 */
    private Integer capacity;

    /** 已选人数 */
    private Integer selectedNum;

    /** 学期 */
    private String semester;

    /** 总学时 */
    private Integer classHours;

    /** 课程简介 */
    private String description;

    /** 授课教师姓名（非数据库字段，联表查询时承载） */
    @TableField(exist = false)
    private String teacherName;
}
