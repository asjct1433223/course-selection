package com.example.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体（sys_user 表），负责统一登录认证
 */
@Data
@TableName("sys_user")
public class User {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 登录用户名 */
    private String username;

    /** 密码（BCrypt 加密） */
    private String password;

    /** 真实姓名 */
    private String realName;

    /** 角色：ADMIN / TEACHER / STUDENT */
    private String role;

    /** 手机号 */
    private String phone;

    /** 邮箱 */
    private String email;

    /** 是否启用：1=是 0=否 */
    private Integer enabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
