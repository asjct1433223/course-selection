package com.example.course.service;

import com.example.course.dto.LoginDTO;
import com.example.course.dto.LoginResultDTO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /** 用户登录：校验用户名密码，返回 JWT 与用户信息 */
    LoginResultDTO login(LoginDTO loginDTO);
}
