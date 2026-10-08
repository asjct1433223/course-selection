<template>
  <div>
    <h3>欢迎使用学生选课管理系统</h3>
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
import { onMounted, reactive } from 'vue';
import { useUserStore } from '../store/user';
import { getStudentList } from '../api/student';
import { getCourseList } from '../api/course';
import { getTeacherList } from '../api/teacher';

 // 教师数量,学生数量,课程数量,角色管理员ADMIN 教师TEACHER 学生STUDENT(useUserStore()中有)
 const userStore=useUserStore()
 const roleMap={ADMIN:'管理员',TEACHER:'教师',STUDENT:'学生'}
 //数量初始化
 const stats=reactive({
  studentCount: 0,
  courseCount: 0,
  teacherCount: 0
 })
 
 //同时请求3种数据,只要当前页面加载,就像后端发请求
 onMounted(async()=>{
  try {
    const [sRes,cRes,tRes] =await Promise.all([
      getStudentList(),getCourseList(),getTeacherList()
    ])
    stats.studentCount=sRes.data?.length||0
    stats.courseCount=cRes.data?.length||0
    stats.teacherCount=tRes.data?.length||0
  } catch (e) { /* 错误已在拦截器统一提示 */ }
 })
</script>

<style scoped>
.stat-card { text-align: center; padding: 20px 0; }
.stat-num { font-size: 48px; font-weight: bold; color: #409EFF; }
.stat-label { font-size: 16px; color: #909399; margin-top: 8px; }
.intro { line-height: 2; }
.intro ul { padding-left: 20px; }
</style>
