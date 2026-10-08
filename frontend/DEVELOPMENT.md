# 学生选课管理系统 — 从 0 到 1 开发文档

## 目录

1. [项目概述](#1-项目概述)
2. [环境准备](#2-环境准备)
3. [项目初始化](#3-项目初始化)
4. [目录结构设计](#4-目录结构设计)
5. [核心基础设施搭建](#5-核心基础设施搭建)
   - 5.1 [Vite 配置](#51-vite-配置)
   - 5.2 [应用入口 main.js](#52-应用入口-mainjs)
   - 5.3 [根组件 App.vue](#53-根组件-appvue)
   - 5.4 [Axios 请求封装](#54-axios-请求封装)
   - 5.5 [Pinia 用户状态](#55-pinia-用户状态)
   - 5.6 [Vue Router 路由与守卫](#56-vue-router-路由与守卫)
   - 5.7 [主布局组件 AppLayout](#57-主布局组件-applayout)
6. [功能模块开发](#6-功能模块开发)
   - 6.1 [登录模块](#61-登录模块)
   - 6.2 [首页仪表盘](#62-首页仪表盘)
   - 6.3 [学生管理](#63-学生管理)
   - 6.4 [教师管理](#64-教师管理)
   - 6.5 [课程管理](#65-课程管理)
   - 6.6 [选课中心](#66-选课中心)
   - 6.7 [成绩管理](#67-成绩管理)
7. [构建与部署](#7-构建与部署)
8. [常见问题与改进方向](#8-常见问题与改进方向)

---

## 1. 项目概述

### 1.1 项目简介

学生选课管理系统是一个面向高校的教学管理前端应用，支持三种角色的用户：

| 角色 | 权限 |
|------|------|
| **管理员 (ADMIN)** | 管理学生信息、教师信息、课程信息 |
| **教师 (TEACHER)** | 查看课程、录入和管理学生成绩 |
| **学生 (STUDENT)** | 浏览课程、选课/退课、查看成绩 |

### 1.2 技术栈选型

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue 3 | ^3.4.29 | 前端框架（Composition API） |
| Vite | ^5.3.1 | 构建工具与开发服务器 |
| Element Plus | ^2.7.5 | UI 组件库 |
| Pinia | ^2.1.7 | 状态管理 |
| Vue Router | ^4.3.3 | 前端路由 |
| Axios | ^1.7.2 | HTTP 客户端 |
| @element-plus/icons-vue | ^2.3.1 | Element Plus 图标 |
| @vitejs/plugin-vue | ^5.0.5 | Vite Vue 插件 |

### 1.3 后端接口约定

- 后端技术栈：Spring Boot 2.7 + MyBatis-Plus + JWT
- 后端地址：`http://localhost:8080`
- API 前缀：`/api`
- 统一响应格式：`{ code: 200, message: "success", data: ... }`
- 认证方式：JWT Token，请求头 `Authorization: Bearer <token>`

---

## 2. 环境准备

### 2.1 基础环境

```bash
# Node.js >= 16（推荐 18+）
node -v

# npm >= 8
npm -v
```

如果未安装 Node.js，请前往 [https://nodejs.org](https://nodejs.org) 下载 LTS 版本安装。

### 2.2 包管理器（可选）

项目使用 npm，你也可以使用 pnpm 或 yarn：

```bash
# pnpm
npm install -g pnpm

# yarn
npm install -g yarn
```

### 2.3 后端服务确认

在启动前端之前，确保后端 Spring Boot 项目已在 `localhost:8080` 运行：

```bash
curl http://localhost:8080/api/auth/login
# 如果后端运行正常，应返回 JSON 响应（即使是错误信息）
```

---

## 3. 项目初始化

### 3.1 创建项目

```bash
# 使用 Vite 脚手架创建 Vue 3 项目
npm create vite@latest course-selection-frontend -- --template vue

# 进入项目目录
cd course-selection-frontend
```

### 3.2 安装依赖

```bash
# 生产依赖
npm install vue@^3.4.29 vue-router@^4.3.3 pinia@^2.1.7
npm install axios@^1.7.2 element-plus@^2.7.5 @element-plus/icons-vue@^2.3.1

# 开发依赖
npm install -D vite@^5.3.1 @vitejs/plugin-vue@^5.0.5
```

### 3.3 安装完成后的 package.json

```json
{
  "name": "course-selection-frontend",
  "version": "1.0.0",
  "private": true,
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "axios": "^1.7.2",
    "element-plus": "^2.7.5",
    "pinia": "^2.1.7",
    "vue": "^3.4.29",
    "vue-router": "^4.3.3",
    "@element-plus/icons-vue": "^2.3.1"
  },
  "devDependencies": {
    "@vitejs/plugin-vue": "^5.0.5",
    "vite": "^5.3.1"
  }
}
```

### 3.4 初始化入口 HTML

编辑 `index.html`：

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>学生选课管理系统</title>
</head>
<body>
  <div id="app"></div>
  <script type="module" src="/src/main.js"></script>
</body>
</html>
```

### 3.5 启动开发服务器验证

```bash
npm run dev
```

浏览器访问 `http://localhost:5173`，看到 Vite + Vue 默认页面即表示初始化成功。

---

## 4. 目录结构设计

在 `src/` 下创建以下目录结构：

```bash
mkdir -p src/api
mkdir -p src/router
mkdir -p src/store
mkdir -p src/components
mkdir -p src/views
```

最终目录结构：

```
course-selection-frontend/
├── index.html
├── package.json
├── vite.config.js
└── src/
    ├── main.js                  # 应用入口
    ├── App.vue                  # 根组件
    ├── api/                     # API 请求层
    │   ├── request.js           # Axios 实例 + 拦截器
    │   ├── auth.js              # 认证接口
    │   ├── student.js           # 学生接口
    │   ├── teacher.js           # 教师接口
    │   ├── course.js            # 课程接口
    │   └── sc.js                # 选课/成绩接口
    ├── router/
    │   └── index.js             # 路由配置 + 导航守卫
    ├── store/
    │   └── user.js              # 用户状态
    ├── components/
    │   └── AppLayout.vue        # 主布局
    └── views/
        ├── Login.vue            # 登录页
        ├── Dashboard.vue        # 仪表盘
        ├── StudentList.vue      # 学生管理
        ├── TeacherList.vue      # 教师管理
        ├── CourseList.vue       # 课程管理
        ├── CourseSelection.vue  # 选课中心
        └── GradeManage.vue      # 成绩管理
```

---

## 5. 核心基础设施搭建

> **说明**：基础设施是应用的地基，应按以下顺序依次搭建，每步完成后都可以运行 `npm run dev` 验证。

### 5.1 Vite 配置

**文件：`vite.config.js`**

```javascript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端地址
        changeOrigin: true
      }
    }
  }
})
```

**关键点解释：**

- `plugins: [vue()]` — 启用 Vue SFC（.vue 单文件组件）编译
- `server.proxy` — 开发环境代理，将前端 `/api/*` 请求转发到后端，**解决跨域**
- `changeOrigin: true` — 修改请求头中的 Host 为目标地址

### 5.2 应用入口 main.js

**文件：`src/main.js`**

```javascript
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

import App from './App.vue'
import router from './router'
import { setupAuthGuard } from './router'

const app = createApp(App)

// 1. 注册 Pinia 状态管理
app.use(createPinia())

// 2. 注册路由
app.use(router)

// 3. 注册 Element Plus 组件库（中文语言包）
app.use(ElementPlus, { locale: zhCn })

// 4. 全局注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 5. 设置路由守卫（JWT 鉴权）
setupAuthGuard(router)

// 6. 挂载应用
app.mount('#app')
```

**插件加载顺序：**

```
createApp → Pinia → Router → ElementPlus(中文) → 图标注册 → 路由守卫 → mount
```

### 5.3 根组件 App.vue

**文件：`src/App.vue`**

```vue
<template>
  <router-view />
</template>

<script setup>
</script>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}
</style>
```

**核心逻辑：** `<router-view />` 是 Vue Router 的出口，根据当前 URL 动态渲染对应组件。

### 5.4 Axios 请求封装

这是整个前后端通信的核心层，封装了 JWT 鉴权和统一错误处理。

**文件：`src/api/request.js`**

```javascript
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'

// 创建 Axios 实例
const request = axios.create({
  baseURL: '/api',     // 所有请求以 /api 开头
  timeout: 10000       // 10 秒超时
})

// ========== 请求拦截器 ==========
// 每次请求自动从 Pinia Store 中读取 Token 并注入请求头
request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  error => Promise.reject(error)
)

// ========== 响应拦截器 ==========
request.interceptors.response.use(
  response => {
    const res = response.data
    // 后端统一返回 { code, message, data }
    // code !== 200 表示业务错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message))
    }
    return res  // 返回整个响应体，在组件中取 res.data
  },
  error => {
    // HTTP 401 —— Token 过期或无效
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      window.location.hash = '#/login'
      ElMessage.error('登录已过期，请重新登录')
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
```

**数据流图：**

```
组件调用 API 函数
  → request.js 请求拦截器（注入 JWT）
  → HTTP 请求 → 后端处理
  → request.js 响应拦截器（统一错误处理）
  → 组件获取 res.data
```

### 5.5 Pinia 用户状态

**文件：`src/store/user.js`**

```javascript
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  // ===== 状态 =====
  // 使用 ref 定义，并同步初始化 localStorage 中的持久化值
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const realName = ref(localStorage.getItem('realName') || '')
  const role = ref(localStorage.getItem('role') || '')

  // ===== 计算属性 =====
  const isLoggedIn = computed(() => !!token.value)

  // ===== 方法 =====
  function setLogin(data) {
    token.value = data.token
    username.value = data.username
    realName.value = data.realName
    role.value = data.role
    // 同步到 localStorage，保证刷新后登录态不丢失
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    localStorage.setItem('realName', data.realName)
    localStorage.setItem('role', data.role)
  }

  function logout() {
    token.value = ''
    username.value = ''
    realName.value = ''
    role.value = ''
    localStorage.clear()
  }

  return { token, username, realName, role, isLoggedIn, setLogin, logout }
})
```

**设计要点：**

- 采用 Composition API 的 `defineStore` 写法（比 Options API 更灵活）
- Token 等关键信息同时存储在 **Pinia 内存**和 **localStorage** 中
- `isLoggedIn` 作为派生状态，不需要手动维护
- `logout` 调用 `localStorage.clear()` 一次清除所有持久化数据

### 5.6 Vue Router 路由与守卫

这是应用的导航骨架，同时负责权限控制。

**文件：`src/router/index.js`**

```javascript
import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '../store/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('../components/AppLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'student',
        name: 'Student',
        component: () => import('../views/StudentList.vue'),
        meta: { title: '学生管理', roles: ['ADMIN'] }
      },
      {
        path: 'teacher',
        name: 'Teacher',
        component: () => import('../views/TeacherList.vue'),
        meta: { title: '教师管理', roles: ['ADMIN'] }
      },
      {
        path: 'course',
        name: 'Course',
        component: () => import('../views/CourseList.vue'),
        meta: { title: '课程管理' }
      },
      {
        path: 'selection',
        name: 'Selection',
        component: () => import('../views/CourseSelection.vue'),
        meta: { title: '选课中心', roles: ['STUDENT'] }
      },
      {
        path: 'grade',
        name: 'Grade',
        component: () => import('../views/GradeManage.vue'),
        meta: { title: '成绩管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),  // Hash 模式，兼容性好无需服务端配置
  routes
})

// ===== 路由守卫 =====
export function setupAuthGuard(router) {
  router.beforeEach((to, from, next) => {
    const userStore = useUserStore()

    // 1. 访问登录页 → 直接放行
    if (to.path === '/login') {
      next()
      return
    }

    // 2. 未登录 → 重定向到登录页
    if (!userStore.token) {
      next('/login')
      return
    }

    // 3. 角色权限检查 —— 路由 meta.roles 定义了允许访问的角色列表
    if (to.meta.roles && !to.meta.roles.includes(userStore.role)) {
      next('/dashboard')  // 无权限 → 跳转首页
      return
    }

    // 4. 放行
    next()
  })
}

export default router
```

**路由嵌套关系：**

```
/login                        → Login.vue（独立页面，无布局）
/                             → AppLayout.vue（主布局）
  ├── /dashboard              →   Dashboard.vue
  ├── /student                →   StudentList.vue    [ADMIN]
  ├── /teacher                →   TeacherList.vue    [ADMIN]
  ├── /course                 →   CourseList.vue
  ├── /selection              →   CourseSelection.vue [STUDENT]
  └── /grade                  →   GradeManage.vue
```

**选用 Hash 模式的原因：**

- Hash 模式 URL 形如 `/#/login`，不依赖服务端配置
- 如果是 History 模式，Nginx/Spring Boot 需要配置 fallback 到 index.html

**路由懒加载：**

所有页面组件使用 `() => import(...)` 动态导入，Vite 会将其拆分为独立 chunk，按需加载，减小首屏体积。

### 5.7 主布局组件 AppLayout

**文件：`src/components/AppLayout.vue`**

这是登录后所有页面的外层容器，包含侧边栏导航、顶部信息栏和内容区域。

```vue
<template>
  <el-container class="layout">
    <!-- ===== 左侧边栏 ===== -->
    <el-aside width="220px" class="aside">
      <div class="logo">
        <el-icon><School /></el-icon>
        <span>选课管理系统</span>
      </div>

      <el-menu
        :default-active="route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>系统首页</span>
        </el-menu-item>

        <!-- 管理员专属菜单 -->
        <el-menu-item v-if="userStore.role === 'ADMIN'" index="/student">
          <el-icon><User /></el-icon>
          <span>学生管理</span>
        </el-menu-item>
        <el-menu-item v-if="userStore.role === 'ADMIN'" index="/teacher">
          <el-icon><UserFilled /></el-icon>
          <span>教师管理</span>
        </el-menu-item>

        <!-- 通用菜单 -->
        <el-menu-item index="/course">
          <el-icon><Reading /></el-icon>
          <span>课程管理</span>
        </el-menu-item>

        <!-- 学生专属菜单 -->
        <el-menu-item v-if="userStore.role === 'STUDENT'" index="/selection">
          <el-icon><Plus /></el-icon>
          <span>选课中心</span>
        </el-menu-item>

        <el-menu-item index="/grade">
          <el-icon><Trophy /></el-icon>
          <span>成绩管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- ===== 顶部栏 ===== -->
      <el-header class="header">
        <div class="header-right">
          <el-tag :type="roleTagType">{{ roleName }}</el-tag>
          <span class="username">{{ userStore.realName }}</span>
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>

      <!-- ===== 内容区域 ===== -->
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>
```

**布局结构：**

```
┌──────────────────────────────────────────┐
│  aside (220px)   │  header (60px)        │
│  ┌────────────┐  │  ┌──────────────────┐ │
│  │ Logo       │  │  │ 角色标签 用户名 退出│ │
│  ├────────────┤  │  └──────────────────┘ │
│  │ 菜单项1    │  ├────────────────────────┤
│  │ 菜单项2    │  │  main (内容区)         │
│  │ 菜单项3    │  │                        │
│  │ ...        │  │  <router-view />       │
│  └────────────┘  │                        │
└──────────────────────────────────────────┘
```

---

## 6. 功能模块开发

### 6.1 登录模块

#### 6.1.1 API 接口

**文件：`src/api/auth.js`**

```javascript
import request from './request'

export function login(data) {
  return request.post('/auth/login', data)
}
```

#### 6.1.2 登录页面

**文件：`src/views/Login.vue`**

完整登录流程如下：

```vue
<template>
  <div class="login-container">
    <div class="login-card">
      <h2>学生选课管理系统</h2>
      <p class="subtitle">软件学院教学演示项目</p>

      <el-form :model="form" :rules="rules" ref="formRef" size="large">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"
            prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password"
            placeholder="请输入密码" prefix-icon="Lock"
            show-password @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%"
            :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 测试账号提示 -->
      <div class="tips">
        <p>测试账号：</p>
        <p>管理员 admin / 123456</p>
        <p>教师 t001 / 123456</p>
        <p>学生 s2024001 / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import { login } from '../api/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  // 1. 表单校验
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  // 2. 调用登录接口
  loading.value = true
  try {
    const res = await login(form)
    // 3. 存储用户信息（Pinia + localStorage）
    userStore.setLogin(res.data)
    ElMessage.success('登录成功')
    // 4. 跳转首页
    router.push('/dashboard')
  } catch (e) {
    // 错误已在 Axios 响应拦截器中统一处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
}
.login-card h2 {
  text-align: center;
  margin-bottom: 4px;
  color: #303133;
}
.subtitle {
  text-align: center;
  color: #909399;
  margin-bottom: 30px;
  font-size: 14px;
}
.tips {
  margin-top: 16px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
  font-size: 12px;
  color: #909399;
  line-height: 1.8;
}
.tips p { margin: 0; }
</style>
```

**登录流程时序：**

```
[用户]                    [前端]                         [后端]
  │  输入用户名密码          │                             │
  │  点击登录 ─────────────→│                             │
  │                         │  表单校验（rules）             │
  │                         │  POST /api/auth/login        │
  │                         │  ──────────────────────────→ │
  │                         │                              │ 验证凭证
  │                         │                              │ 生成 JWT
  │                         │  { code:200, data:{          │
  │                         │    token, username,          │
  │                         │    realName, role }}         │
  │                         │  ←────────────────────────── │
  │                         │  存入 Pinia + localStorage    │
  │                         │  跳转 /dashboard             │
  │  看到首页 ←──────────────│                             │
```

### 6.2 首页仪表盘

#### 6.2.1 文件：`src/views/Dashboard.vue`

```vue
<template>
  <div>
    <h3>欢迎使用学生选课管理系统</h3>

    <!-- 统计卡片 -->
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-num">{{ stats.studentCount }}</div>
          <div class="stat-label">学生总数</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-num">{{ stats.courseCount }}</div>
          <div class="stat-label">课程总数</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-num">{{ stats.teacherCount }}</div>
          <div class="stat-label">教师总数</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统介绍 -->
    <el-card style="margin-top:20px">
      <template #header>系统说明</template>
      <div class="intro">
        <p><strong>当前角色：</strong>{{ roleMap[userStore.role] || '未知' }}</p>
        <p><strong>系统功能：</strong></p>
        <ul>
          <li>管理员：管理学生信息、教师信息、课程信息</li>
          <li>教师：查看课程信息、录入和管理学生成绩</li>
          <li>学生：浏览课程、选课/退课、查看自己的成绩</li>
        </ul>
        <p><strong>技术栈：</strong>Spring Boot 2.7 + Vue 3 + Element Plus + MyBatis-Plus + JWT</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { useUserStore } from '../store/user'
import { getStudentList } from '../api/student'
import { getCourseList } from '../api/course'
import { getTeacherList } from '../api/teacher'

const userStore = useUserStore()
const roleMap = { ADMIN: '管理员', TEACHER: '教师', STUDENT: '学生' }

const stats = reactive({
  studentCount: 0,
  courseCount: 0,
  teacherCount: 0
})

onMounted(async () => {
  try {
    // 并行请求三个接口，提升加载速度
    const [sRes, cRes, tRes] = await Promise.all([
      getStudentList(), getCourseList(), getTeacherList()
    ])
    stats.studentCount = sRes.data?.length || 0
    stats.courseCount = cRes.data?.length || 0
    stats.teacherCount = tRes.data?.length || 0
  } catch (e) { /* ignore */ }
})
</script>
```

**性能要点：** 使用 `Promise.all` 并行请求三个统计接口，先到先得，不互相等待。

### 6.3 学生管理

#### 6.3.1 API 接口

**文件：`src/api/student.js`**

```javascript
import request from './request'

export function getStudentPage(params) {
  return request.get('/student/page', { params })
}

export function getStudentList() {
  return request.get('/student/list')
}

export function getStudentById(id) {
  return request.get(`/student/${id}`)
}

export function saveStudent(data) {
  return request.post('/student', data)
}

export function updateStudent(data) {
  return request.put('/student', data)
}

export function deleteStudent(id) {
  return request.delete(`/student/${id}`)
}
```

#### 6.3.2 学生管理页面

**文件：`src/views/StudentList.vue`**

这是典型的 **CRUD 页面模式**，其他管理页面复用相同的模式：

```
┌──────────────────────────────────────────────────┐
│  学生管理                        [新增学生] 按钮   │
├──────────────────────────────────────────────────┤
│  [搜索框___] [搜索]                                │
├──────────────────────────────────────────────────┤
│  表格: ID | 学号 | 姓名 | 性别 | 班级 | ... | 操作  │
│        每行操作: [编辑] [删除]                       │
├──────────────────────────────────────────────────┤
│  分页组件: 共 N 条  < 1 2 3 ... >                  │
└──────────────────────────────────────────────────┘

点击 [新增] / [编辑] → 弹出对话框:
┌──────────────────────────┐
│ 新增学生 / 编辑学生        │
│ ┌──────────────────────┐ │
│ │ 学号: [________]     │ │
│ │ 姓名: [________]     │ │
│ │ 性别: [男 ▼]         │ │
│ │ 班级: [________]     │ │
│ │ ...                  │ │
│ └──────────────────────┘ │
│      [取消]  [确定]       │
└──────────────────────────┘
```

**核心 CRUD 流程（以学生管理为例）：**

```javascript
// 1. 分页加载
async function loadData() {
  loading.value = true
  try {
    const res = await getStudentPage({
      current: current.value,
      size: size.value,
      keyword: keyword.value
    })
    tableData.value = res.data.records   // 当前页数据
    total.value = res.data.total         // 总记录数
  } finally {
    loading.value = false
  }
}

// 2. 新增
function handleAdd() {
  resetForm()              // 清空表单
  isEdit.value = false     // 标记为新增模式
  dialogVisible.value = true
}

// 3. 编辑 —— 将选中行数据填入表单
function handleEdit(row) {
  Object.assign(form, row) // 浅拷贝行数据到表单
  isEdit.value = true
  dialogVisible.value = true
}

// 4. 提交（新增/编辑共用一个方法）
async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isEdit.value) {
      await updateStudent(form)   // PUT
    } else {
      await saveStudent(form)     // POST
    }
    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    loadData()  // 刷新列表
  } catch (e) { /* ignore */ }
}

// 5. 删除
async function handleDelete(row) {
  // 先弹出确认框
  await ElMessageBox.confirm(
    `确定删除学生"${row.name}"吗？`,
    '警告',
    { type: 'warning' }
  )
  try {
    await deleteStudent(row.id)  // DELETE
    ElMessage.success('删除成功')
    loadData()  // 刷新列表
  } catch (e) { /* ignore */ }
}

onMounted(loadData)
```

### 6.4 教师管理

#### 6.4.1 API 接口

**文件：`src/api/teacher.js`**

```javascript
import request from './request'

export function getTeacherPage(params) {
  return request.get('/teacher/page', { params })
}
export function getTeacherList() {
  return request.get('/teacher/list')
}
export function getTeacherById(id) {
  return request.get(`/teacher/${id}`)
}
export function saveTeacher(data) {
  return request.post('/teacher', data)
}
export function updateTeacher(data) {
  return request.put('/teacher', data)
}
export function deleteTeacher(id) {
  return request.delete(`/teacher/${id}`)
}
```

#### 6.4.2 页面组件

**文件：`src/views/TeacherList.vue`**

开发模式与学生管理完全相同，区别仅在于：

| 维度 | 学生管理 | 教师管理 |
|------|----------|----------|
| API 模块 | `../api/student` | `../api/teacher` |
| 字段 | 学号、姓名、班级、年级 | 工号、姓名、职称、院系 |
| 权限 | 仅 ADMIN | 仅 ADMIN |

### 6.5 课程管理

#### 6.5.1 API 接口

**文件：`src/api/course.js`**

```javascript
import request from './request'

export function getCoursePage(params) {
  return request.get('/course/page', { params })
}
export function getCourseList() {
  return request.get('/course/list')
}
export function getCourseById(id) {
  return request.get(`/course/${id}`)
}
export function saveCourse(data) {
  return request.post('/course', data)
}
export function updateCourse(data) {
  return request.put('/course', data)
}
export function deleteCourse(id) {
  return request.delete(`/course/${id}`)
}

// 选课相关
export function selectCourse(studentId, courseId) {
  return request.post('/course/select', null, {
    params: { studentId, courseId }
  })
}
export function dropCourse(studentId, courseId) {
  return request.post('/course/drop', null, {
    params: { studentId, courseId }
  })
}
```

#### 6.5.2 页面组件

**文件：`src/views/CourseList.vue`**

课程管理比学生/教师管理多了一个特点：

- **管理员**：看到 CRUD 按钮（新增、编辑、删除）
- **学生**：看到"选课"按钮
- **教师**：只读查看

通过 `v-if="userStore.role === 'ADMIN'"` 实现按钮级别的权限控制。

**权限控制代码：**

```vue
<el-table-column label="操作" width="200" fixed="right">
  <template #default="{ row }">
    <!-- 管理员：管理按钮 -->
    <el-button v-if="userStore.role === 'ADMIN'"
      size="small" @click="handleEdit(row)">编辑</el-button>
    <el-button v-if="userStore.role === 'ADMIN'"
      size="small" type="danger" @click="handleDelete(row)">删除</el-button>

    <!-- 学生：选课按钮 -->
    <el-button v-if="userStore.role === 'STUDENT'"
      size="small" type="success" @click="handleSelect(row)">选课</el-button>
  </template>
</el-table-column>
```

### 6.6 选课中心

#### 6.6.1 API 接口

**文件：`src/api/sc.js`**

```javascript
import request from './request'

// 查询某学生的选课记录
export function getScByStudent(studentId) {
  return request.get(`/sc/student/${studentId}`)
}

// 查询某课程的选课学生
export function getScByCourse(courseId) {
  return request.get(`/sc/course/${courseId}`)
}

// 录入/更新成绩
export function updateScore(scId, score) {
  return request.put('/sc/score', null, {
    params: { scId, score }
  })
}

// 获取所有选课记录
export function getScList() {
  return request.get('/sc/list')
}
```

#### 6.5.2 页面组件

**文件：`src/views/CourseSelection.vue`**

选课中心使用 **Tab 切换** 实现两个视图：

```
Tab: [可选课程] [我的课程]

可选课程 Tab:
  ┌──────────┐ ┌──────────┐ ┌──────────┐
  │ 课程卡片1  │ │ 课程卡片2  │ │ 课程卡片3  │
  │ 编号/学分  │ │ 编号/学分  │ │ 编号/学分  │
  │ 教师/学期  │ │ 教师/学期  │ │ 教师/学期  │
  │ 进度条     │ │ 进度条     │ │ 进度条     │
  │ [立即选课] │ │ [立即选课] │ │ [已满]     │
  └──────────┘ └──────────┘ └──────────┘

我的课程 Tab:
  表格: 课程名称 | 学生 | 学号 | 成绩 | [退课]
```

**核心业务逻辑：**

```javascript
async function loadData() {
  const [cRes, scRes] = await Promise.all([
    getCourseList(),        // 所有课程
    getScByStudent(studentId) // 我的选课
  ])
  allCourses.value = cRes.data || []
  myCourses.value = scRes.data || []

  // 从所有课程中排除已选课程
  const selectedIds = myCourses.value.map(s => s.courseId)
  availableCourses.value = allCourses.value.filter(
    c => !selectedIds.includes(c.id)
  )
}

async function handleSelect(course) {
  await selectCourse(studentId, course.id)
  ElMessage.success(`成功选修"${course.name}"`)
  loadData()  // 刷新可选/已选列表
}

async function handleDrop(row) {
  await ElMessageBox.confirm(
    `确定退选课程"${row.courseName}"吗？`, '提示',
    { type: 'warning' }
  )
  await dropCourse(studentId, row.courseId)
  ElMessage.success('退课成功')
  loadData()
}
```

**课程卡片悬停效果：**

```css
.course-card { transition: all 0.3s; }
.course-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}
```

### 6.7 成绩管理

**文件：`src/views/GradeManage.vue`**

成绩管理页面根据角色展示不同视图：

| 角色 | 视图 | API |
|------|------|-----|
| **学生** | 自己的成绩单（只读） | `GET /sc/student/{studentId}` |
| **教师** | 下拉选课程 → 该课程的学生列表 → 逐行录入成绩 | `GET /sc/course/{courseId}` → `PUT /sc/score` |
| **管理员** | 所有选课记录（只读） | `GET /sc/list` |

**教师录入成绩的核心逻辑（行内编辑模式）：**

```javascript
const editingRow = ref(null)   // 当前正在编辑的行 ID
const editScore = ref(0)       // 编辑中的分数值

// 进入编辑模式
function startEdit(row) {
  editingRow.value = row.id
  editScore.value = row.score || 0
}

// 保存成绩
async function saveScore(row) {
  await updateScore(row.id, editScore.value)
  ElMessage.success('成绩录入成功')
  row.score = editScore.value   // 乐观更新表格
  editingRow.value = null       // 退出编辑模式
}
```

**行内编辑 UI：**

```
正常状态:  [85] [录入]      ← 点击"录入"进入编辑
编辑状态:  [__] [保存] [取消]  ← 输入分数后保存或取消
```

---

## 7. 构建与部署

### 7.1 本地构建

```bash
# 构建生产版本
npm run build

# 输出到 dist/ 目录
ls dist/
# index.html  assets/  ...
```

### 7.2 本地预览

```bash
npm run preview
# 默认在 http://localhost:4173 预览构建产物
```

### 7.3 部署到 Nginx

```nginx
server {
    listen       80;
    server_name  course-selection.example.com;

    # 前端静态资源
    location / {
        root   /usr/share/nginx/html/course-selection;
        index  index.html;
        try_files $uri $uri/ /index.html;  # SPA fallback
    }

    # API 反向代理
    location /api/ {
        proxy_pass http://localhost:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

### 7.4 部署到 Spring Boot 静态资源目录

直接将 `dist/` 内容复制到 Spring Boot 项目的 `src/main/resources/static/` 目录下，由 Spring Boot 统一提供服务，无需 Nginx。

---

## 8. 常见问题与改进方向

### 8.1 常见问题

| 问题 | 原因 | 解决 |
|------|------|------|
| 页面空白 | 路由未配置或组件路径错误 | 检查 `router/index.js` 中的 component 路径 |
| 接口 401 | Token 过期 | 重新登录，Axios 拦截器会自动跳转 |
| 跨域错误 | Vite 代理未配置 | 检查 `vite.config.js` 中的 proxy 设置 |
| 刷新后登录态丢失 | localStorage 未同步 | 检查 `store/user.js` 中 `setLogin` 是否同时写入了 localStorage |
| 角色菜单不显示 | Pinia Store 中 role 为空 | 检查登录后 `setLogin` 是否正确赋值 |

### 8.2 已知问题

1. **[CourseSelection.vue](src/views/CourseSelection.vue#L68) 和 [GradeManage.vue](src/views/GradeManage.vue#L68)** 中 `studentId` 硬编码为 `1`，应从 `useUserStore` 中获取当前登录学生 ID
2. **[CourseList.vue](src/views/CourseList.vue#L153)** 中选课功能 `selectCourse(null, row.id)` 传了 `null` 作为 `studentId`，同样是硬编码问题

### 8.3 扩展改进建议

| 改进项 | 说明 | 优先级 |
|--------|------|--------|
| **TypeScript 迁移** | 当前全部为 JavaScript，引入 TS 提升类型安全 | 中 |
| **环境变量** | 将 API 地址、代理目标等配置抽到 `.env` 文件 | 高 |
| **Composable 抽取** | 学生/教师/课程的 CRUD 逻辑高度相似，抽取 `useCrud` 组合式函数减少重复代码 | 中 |
| **错误边界** | 添加全局 `onErrorCaptured` 处理未捕获的 Vue 异常 | 低 |
| **加载骨架** | 用 `<el-skeleton>` 替代 `v-loading` 提升加载体验 | 低 |
| **单元测试** | 引入 Vitest + Vue Test Utils 覆盖核心逻辑 | 低 |
| **E2E 测试** | 引入 Playwright 或 Cypress 覆盖关键用户流程 | 低 |
| **代码规范** | 配置 ESLint + Prettier + Husky 保证代码风格一致 | 中 |
| **国际化** | 虽然当前是中文系统，但可预留 i18n 扩展能力 | 低 |

---

## 附录：开发命令速查

```bash
# 安装依赖
npm install

# 启动开发服务器（热更新）
npm run dev

# 生产构建
npm run build

# 预览构建产物
npm run preview

# 查看依赖树
npm ls --depth=0

# 升级依赖
npm update
```
