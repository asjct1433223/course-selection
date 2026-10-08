package com.example.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 教师实体（teacher 表）
 */
@Data
@TableName("teacher")
public class Teacher {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联 sys_user.id */
    private Long userId;

    /** 工号 */
    private String teacherNo;

    /** 姓名 */
    private String name;

    /** 性别 */
    private String gender;

    /** 职称：教授/副教授/讲师/助教 */
    private String title;

    /** 所属院系 */
    private String department;

    /** 联系电话 */
    private String phone;

    /** 登录账号（非数据库字段） */
    @TableField(exist = false)
    private String username;

    /** 登录密码（非数据库字段，仅新增教师时使用） */
    @TableField(exist = false)
    private String password;
}
