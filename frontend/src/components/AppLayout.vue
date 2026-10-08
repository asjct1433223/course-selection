<template>
  <el-container class="layout">
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

        <el-menu-item v-if="userStore.role === 'ADMIN'" index="/student">
          <el-icon><User /></el-icon>
          <span>学生管理</span>
        </el-menu-item>

        <el-menu-item v-if="userStore.role === 'ADMIN'" index="/teacher">
          <el-icon><UserFilled /></el-icon>
          <span>教师管理</span>
        </el-menu-item>

        <el-menu-item index="/course">
          <el-icon><Reading /></el-icon>
          <span>课程管理</span>
        </el-menu-item>

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
      <el-header class="header">
        <div class="header-right">
          <el-tag :type="roleTagType">{{ userStore.roleName }}</el-tag>
          <span class="username">{{ userStore.realName }}</span>
          <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useUserStore } from '../store/user';
const route=useRoute()
const router=useRouter()
const userStore=useUserStore()
// 角色标签颜色
const roleTagType=computed(()=>{
  const map={ADMIN:'danger',TEACHER:'warning',STUDENT:'success'}
  return map[userStore.role]||'info'
})
// 退出登录
function handleLogout(){
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100vh; }
.aside { background-color: #304156; overflow-y: auto; }
.logo {
  height: 60px; display: flex; align-items: center; justify-content: center;
  color: #fff; font-size: 18px; font-weight: bold; gap: 8px;
}
.header {
  background: #fff; display: flex; align-items: center; justify-content: flex-end;
  border-bottom: 1px solid #e6e6e6; padding: 0 20px;
}
.header-right { display: flex; align-items: center; gap: 12px; }
.username { font-weight: 500; }
.main { background: #f0f2f5; padding: 20px; }
</style>
