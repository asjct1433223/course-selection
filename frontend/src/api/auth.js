import request from "./request";

// 验证模块  发送登录请求到后端  借助axios,被封装在request.js,这3件事是每个请求必须的
export function login(data){
    return request.post('/auth/login',data) //指的是@RestController和@RequestMapping AuthController类
}