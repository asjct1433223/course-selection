package com.example.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 学生实体（student 表）
 */
@Data
@TableName("student")
public class Student {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联 sys_user.id */
    private Long userId;

    /** 学号 */
    private String studentNo;

    /** 姓名 */
    private String name;

    /** 性别 */
    private String gender;

    /** 班级 */
    private String className;

    /** 年级（入学年份） */
    private String grade;

    /** 联系电话 */
    private String phone;

    /** 登录账号（非数据库字段，联表查询或新增时承载数据） */
    @TableField(exist = false)
    private String username;

    /** 登录密码（非数据库字段，仅新增学生时使用） */
    @TableField(exist = false)
    private String password;
}
