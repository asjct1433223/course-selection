<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>成绩管理</span>
          <el-select v-if="userStore.role === 'TEACHER'" v-model="selectedCourseId"
            placeholder="选择课程" @change="loadScByCourse" style="width:240px">
            <el-option v-for="c in courses" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </div>
      </template>

      <!-- 管理员：所有选课记录 -->
      <!-- 教师：按课程查看选课学生 -->
      <!-- 学生：自己的成绩单 -->
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column v-if="userStore.role !== 'STUDENT'" prop="studentNo" label="学号" width="120" />
        <el-table-column v-if="userStore.role !== 'STUDENT'" prop="studentName" label="学生姓名" width="100" />
        <el-table-column prop="courseName" label="课程名称" min-width="180" />
        <el-table-column label="成绩" width="120">
          <template #default="{ row }">
            <template v-if="editingRow === row.id">
              <el-input-number v-model="editScore" :min="0" :max="100" :precision="1"
                size="small" style="width:90px" />
              <el-button size="small" type="primary" @click="saveScore(row)" style="margin-left:4px">保存</el-button>
              <el-button size="small" @click="editingRow = null">取消</el-button>
            </template>
            <template v-else>
              <span v-if="row.score !== null" :style="{ color: row.score >= 60 ? '#67c23a' : '#f56c6c', fontWeight:'bold' }">
                {{ row.score }}
              </span>
              <el-tag v-else type="info" size="small">未录入</el-tag>
              <el-button v-if="canEdit" size="small" @click="startEdit(row)" style="margin-left:8px">
                录入
              </el-button>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.score === null" type="warning" size="small">待录入</el-tag>
            <el-tag v-else-if="row.score >= 60" type="success" size="small">通过</el-tag>
            <el-tag v-else type="danger" size="small">不及格</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { useUserStore } from '../store/user';
import { getCourseList } from '../api/course';
import { getScByStudent, getScByCourse, getScList, updateScore } from '../api/sc';

const userStore=useUserStore()
// 教师选择的课程
const selectedCourseId=ref(null)
const courses=ref([])
// 表格数据与加载动画
const tableData=ref([])
const loading=ref(false)
// 行内编辑状态
const editingRow=ref(null)
const editScore=ref(0)
// 教师可录入成绩
const canEdit=computed(()=>userStore.role==='TEACHER')

// 学生：自己的成绩单
async function loadMyGrade(){
  if(!userStore.studentId)return
  const res=await getScByStudent(userStore.studentId)
  tableData.value=res.data||[]
}

// 教师：某课程的选课学生
async function loadScByCourse(){
  if(!selectedCourseId.value)return
  loading.value=true
  try{
    const res=await getScByCourse(selectedCourseId.value)
    tableData.value=res.data||[]
  }finally{
    loading.value=false
  }
}

// 管理员：所有选课记录
async function loadAll(){
  const res=await getScList()
  tableData.value=res.data||[]
}

// 教师加载自己的授课课程
async function loadMyCourses(){
  if(!userStore.teacherId)return
  const cRes=await getCourseList()
  const all=cRes.data||[]
  courses.value=all.filter(c=>String(c.teacherId)===String(userStore.teacherId))
  if(courses.value.length>0){
    selectedCourseId.value=courses.value[0].id
    await loadScByCourse()
  }
}

// 进入编辑模式
function startEdit(row){
  editingRow.value=row.id
  editScore.value=row.score||0
}

// 保存成绩
async function saveScore(row){
  await updateScore(row.id,editScore.value)
  ElMessage.success('成绩录入成功')
  row.score=editScore.value
  editingRow.value=null
}

onMounted(async ()=>{
  try{
    if(userStore.role==='TEACHER'){
      await loadMyCourses()
    }else if(userStore.role==='STUDENT'){
      await loadMyGrade()
    }else{
      await loadAll()
    }
  }catch(e){/* 错误已统一提示 */}
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
