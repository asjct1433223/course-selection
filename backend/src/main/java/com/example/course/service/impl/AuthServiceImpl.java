package com.example.course.service.impl;

import com.example.course.common.JwtUtils;
import com.example.course.dto.LoginDTO;
import com.example.course.dto.LoginResultDTO;
import com.example.course.entity.Student;
import com.example.course.entity.Teacher;
import com.example.course.entity.User;
import com.example.course.mapper.StudentMapper;
import com.example.course.mapper.TeacherMapper;
import com.example.course.mapper.UserMapper;
import com.example.course.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtUtils jwtUtils;

    /** 角色编码 → 中文名 */
    private static final Map<String, String> ROLE_NAME_MAP = new HashMap<>();

    static {
        ROLE_NAME_MAP.put("ADMIN", "管理员");
        ROLE_NAME_MAP.put("TEACHER", "教师");
        ROLE_NAME_MAP.put("STUDENT", "学生");
    }

    @Override
    public LoginResultDTO login(LoginDTO loginDTO) {
        if (loginDTO == null || !StringUtils.hasText(loginDTO.getUsername())
                || !StringUtils.hasText(loginDTO.getPassword())) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        // 1. 查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername().trim());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        // 2. 校验账号状态
        if (user.getEnabled() != null && user.getEnabled() == 0) {
            throw new RuntimeException("账号已被禁用，请联系管理员");
        }

        // 3. 生成 JWT
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole());

        // 4. 组装响应
        LoginResultDTO result = new LoginResultDTO();
        result.setToken(token);
        result.setUsername(user.getUsername());
        result.setRealName(user.getRealName());
        result.setRole(user.getRole());
        result.setRoleName(ROLE_NAME_MAP.getOrDefault(user.getRole(), user.getRole()));

        // 5. 学生/教师角色额外返回关联表主键（前端选课、成绩、课程下拉使用）
        if ("STUDENT".equals(user.getRole())) {
            Student student = studentMapper.selectByUserId(user.getId());
            if (student != null) {
                result.setStudentId(student.getId());
            }
        } else if ("TEACHER".equals(user.getRole())) {
            Teacher teacher = teacherMapper.selectByUserId(user.getId());
            if (teacher != null) {
                result.setTeacherId(teacher.getId());
            }
        }
        return result;
    }
}
