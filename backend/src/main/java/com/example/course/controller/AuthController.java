package com.example.course.controller;

import com.example.course.common.Result;
import com.example.course.dto.LoginDTO;
import com.example.course.dto.LoginResultDTO;
import com.example.course.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    /** 用户登录 */
    @PostMapping("/login")
    public Result<LoginResultDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return Result.ok("登录成功", authService.login(loginDTO));
    }
}
