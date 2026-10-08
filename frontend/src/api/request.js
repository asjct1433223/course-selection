
//导包可能有问题,import
import axios from "axios"
import { useUserStore } from "../store/user"
import { ElMessage } from "element-plus"

// 创建axios实例  /api为基础路径
const request=axios.create({
    baseURL: '/api',
    timeout: 10000 //10秒超时

})
// 访问后端时403,没有携带token,可知每次必须带token, 将其写入用户状态管理
// 请求拦截器:请求之前做的事
request.interceptors.request.use(
    config=>{
        const userStore=useUserStore()//
        if(userStore.token)config.headers.Authorization=`Bearer ${userStore.token}`//反引号
        return config   //有token则添加到请求头
    },
    error=>Promise.reject(error) //如果拦截器异常,则抱token 无法获取 
)
//响应拦截器
request.interceptors.response.use(
    response=>{
        const res=response.data //后端统一返回code,meassge,data
        if(res.code!==200){   //=== 值判断和类型判断   数值类型200 字符类型200    
            ElMessage.error(res.message|| '请求失败')
            return Promise.reject(new Error(res.message))
        }
        return res//响应体
    },
    error=>{
        //401 --token过期或者无效
        if(error.response?.status===401){
            const userStore=useUserStore()
            userStore.logout()
            window.location.hash='#/login'
            ElMessage.error('登陆过期,请重新登录')
        }else{
            ElMessage.error(error.message || '网络错误')
        }
        return Promise.reject(error)
    })
export default request