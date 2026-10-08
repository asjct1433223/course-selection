package com.example.course.config;

import com.example.course.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            // Knife4j & H2
            .antMatchers("/doc.html", "/webjars/**", "/v2/api-docs/**", "/swagger-resources/**", "/h2-console/**").permitAll()
            // 公开接口
            .antMatchers("/api/auth/login").permitAll()
            // 统计类列表接口：所有登录用户可访问（首页统计卡片需要）
            .antMatchers(HttpMethod.GET, "/api/student/list", "/api/teacher/list", "/api/course/list").authenticated()
            // 管理员接口
            .antMatchers("/api/admin/**").hasAuthority("ADMIN")
            // 教师接口
            .antMatchers("/api/teacher/**").hasAnyAuthority("TEACHER", "ADMIN")
            // 其他需认证
            .anyRequest().authenticated()
            .and()
            .exceptionHandling()
            // 未登录/Token失效 → 401，前端据此跳转登录页
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(401);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
            })
            // 已登录但无权限 → 403
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"没有访问权限\"}");
            })
            .and()
            .headers().frameOptions().disable() // H2控制台需要
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
