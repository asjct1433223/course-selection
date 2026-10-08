<template>
  <div>
    <el-card>
      <template #header>
        <span>选课中心 — 浏览并选择你感兴趣的课程</span>
      </template>

      <!-- 可选课程 -->
      <el-tabs v-model="activeTab">
        <el-tab-pane label="可选课程" name="available">
          <el-row :gutter="16">
            <el-col :span="8" v-for="course in availableCourses" :key="course.id" style="margin-bottom:16px">
              <el-card shadow="hover" class="course-card">
                <div class="course-title">{{ course.name }}</div>
                <div class="course-info">
                  <p><strong>编号：</strong>{{ course.courseNo }}</p>
                  <p><strong>学分：</strong>{{ course.credit }}</p>
                  <p><strong>授课教师：</strong>{{ course.teacherName }}</p>
                  <p><strong>学期：</strong>{{ course.semester }}</p>
                  <p><strong>学时：</strong>{{ course.classHours }}</p>
                  <p><strong>课容量：</strong>{{ course.selectedNum }} / {{ course.capacity }}</p>
                  <el-progress :percentage="Math.round(course.selectedNum / course.capacity * 100)"
                    :color="course.selectedNum >= course.capacity ? '#f56c6c' : '#67c23a'" />
                </div>
                <el-button type="success" style="width:100%;margin-top:12px"
                  :disabled="course.selectedNum >= course.capacity"
                  @click="handleSelect(course)">立即选课</el-button>
              </el-card>
            </el-col>
          </el-row>
          <el-empty v-if="availableCourses.length === 0" description="暂无可选课程" />
        </el-tab-pane>

        <!-- 我的课程 -->
        <el-tab-pane label="我的课程" name="my">
          <el-table :data="myCourses" border stripe>
            <el-table-column prop="courseName" label="课程名称" min-width="180" />
            <el-table-column prop="studentName" label="学生" width="100" />
            <el-table-column prop="studentNo" label="学号" width="120" />
            <el-table-column label="成绩" width="100">
              <template #default="{ row }">
                <span v-if="row.score !== null">{{ row.score }}</span>
                <el-tag v-else type="info" size="small">暂无</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button size="small" type="danger" @click="handleDrop(row)">退课</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useUserStore } from '../store/user';
import { getCourseList, selectCourse, dropCourse } from '../api/course';
import { getScByStudent } from '../api/sc';

const userStore=useUserStore()
const activeTab=ref('available')
// 全部课程、我的选课、可选课程
const allCourses=ref([])
const myCourses=ref([])
const availableCourses=ref([])

// 加载课程与我的选课
async function loadData(){
  if(!userStore.studentId){
    ElMessage.warning('当前账号未关联学生信息，无法选课')
    return
  }
  const [cRes,scRes]=await Promise.all([
    getCourseList(),
    getScByStudent(userStore.studentId)
  ])
  allCourses.value=cRes.data||[]
  myCourses.value=scRes.data||[]
  // 可选课程 = 全部课程 - 已选课程
  const selectedIds=myCourses.value.map(s=>s.courseId)
  availableCourses.value=allCourses.value.filter(c=>!selectedIds.includes(c.id))
}

// 选课
async function handleSelect(course){
  try{
    await ElMessageBox.confirm(`确定选择课程"${course.name}"吗？`,'选课确认',{type:'info'})
  }catch(e){return}
  try{
    await selectCourse(userStore.studentId,course.id)
    ElMessage.success(`成功选修"${course.name}"`)
    loadData()
  }catch(e){/* 错误已统一提示 */}
}

// 退课
async function handleDrop(row){
  try{
    await ElMessageBox.confirm(`确定退选课程"${row.courseName}"吗？`,'提示',{type:'warning'})
  }catch(e){return}
  try{
    await dropCourse(userStore.studentId,row.courseId)
    ElMessage.success('退课成功')
    loadData()
  }catch(e){/* 错误已统一提示 */}
}

onMounted(loadData)
</script>

<style scoped>
.course-card { transition: all 0.3s; }
.course-card:hover { transform: translateY(-2px); box-shadow: 0 4px 16px rgba(0,0,0,0.12); }
.course-title { font-size: 16px; font-weight: bold; margin-bottom: 8px; color: #303133; }
.course-info p { margin: 4px 0; font-size: 13px; color: #606266; }
</style>
