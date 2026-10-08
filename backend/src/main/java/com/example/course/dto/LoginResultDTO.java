package com.example.course.dto;

import lombok.Data;

/**
 * 登录成功后的响应数据
 */
@Data
public class LoginResultDTO {

    /** JWT 令牌 */
    private String token;

    /** 登录用户名 */
    private String username;

    /** 真实姓名 */
    private String realName;

    /** 角色编码：ADMIN / TEACHER / STUDENT */
    private String role;

    /** 角色中文名：管理员 / 教师 / 学生 */
    private String roleName;

    /** 学生主键（学生登录时返回，供选课/成绩使用） */
    private Long studentId;

    /** 教师主键（教师登录时返回，供课程/成绩使用） */
    private Long teacherId;
}
