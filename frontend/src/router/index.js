// 路由接口文件 axios封装,响应速度快,服务器压力小

import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '../store/user.js'

//routes数组中的每一个元素对应一条路由
const routes=[
    //1 登录路由
    {
        path: '/login',   //只要访问/login地址,则加载Login.vue组件   localhost:5173/login
        name: 'Login',//路由名称 代指当前路由
        //懒加载     访问谁加载谁  优化首屏速度有
        component:()=>import('../views/Login.vue'),
        meta:{title:'登录'}//html的head中的meta包含title也就是网页的名字
    },
    //嵌套路由
    {
        path: '/',//空请求
        component: ()=>import('../components/AppLayout.vue'),//布局组件
        redirect: '/dashboard',//重定向到首页
        children:[//存放后续所有路由
            {
                path: 'dashboard',
                name: 'Dashboard',
                component:()=>import('../views/Dashboard.vue'),
                meta:{title:'首页'}
            },
            {
                path: 'student',
                name: 'Student',
                component:()=>import('../views/StudentList.vue'),
                meta:{title:'学生管理',roles:['ADMIN']}//管理员可以进行学生管理
            },
            {
                path: 'teacher',
                name: 'Teacher',
                component:()=>import('../views/TeacherList.vue'),
                meta:{title:'教师管理',roles:['ADMIN']}//仅管理员
            },
            {
                path: 'course',
                name: 'Course',
                component:()=>import('../views/CourseList.vue'),
                meta:{title:'课程管理'}//所有登录用户可见
            },
            {
                path: 'selection',
                name: 'Selection',
                component:()=>import('../views/CourseSelection.vue'),
                meta:{title:'选课中心',roles:['STUDENT']}//仅学生
            },
            {
                path: 'grade',
                name: 'Grade',
                component:()=>import('../views/GradeManage.vue'),
                meta:{title:'成绩管理'}//所有登录用户可见
            }
        ]
    }

]
// 路由实例 router
const router=createRouter({
    history:createWebHashHistory(),//Hash 模式,兼容性好 无需后端配置;地址栏中有#号 就是hash模式
    routes //路由数组,后续我们会继续添加
})
// 路由守卫
export function setupAuthGuard(router){
    router.beforeEach((to,from,next)=>{
        const userStore=useUserStore()
        //1 谁都可以访问登陆页面
        if(to.path==='/login'){
            next()
            return
        }
        //2 未登录的访问-->重定向到登陆页面
        if(!userStore.token){
            next('/login')
            return
        }
        //3 角色检查-->管理员/教师/学生  管理员==ADMIN  教师==TEACHER   学生==STUDENT
        if(to.meta.roles&&!to.meta.roles.includes(userStore.role)){
            //无权限-->跳转首页
            next('/dashboard')
            return
        }
        //4 放行
        next()
    })   
}
export default router //本身是个对象
