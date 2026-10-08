<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>课程管理</span>
          <el-button v-if="userStore.role === 'ADMIN'" type="primary" @click="handleAdd">新增课程</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索课程名称或编号" style="width:240px"
          clearable @clear="loadData" @keyup.enter="loadData" />
        <el-button type="primary" @click="loadData" style="margin-left:8px">搜索</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="margin-top:16px">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="courseNo" label="课程编号" width="110" />
        <el-table-column prop="name" label="课程名称" min-width="160" />
        <el-table-column prop="credit" label="学分" width="70" />
        <el-table-column prop="teacherName" label="授课教师" width="100" />
        <el-table-column prop="semester" label="学期" width="110" />
        <el-table-column prop="classHours" label="学时" width="70" />
        <el-table-column label="选课情况" width="100">
          <template #default="{ row }">
            <el-progress :percentage="Math.round(row.selectedNum / row.capacity * 100)"
              :color="row.selectedNum >= row.capacity ? '#f56c6c' : '#409EFF'" />
          </template>
        </el-table-column>
        <el-table-column v-if="userStore.role === 'ADMIN'" label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-if="userStore.role === 'ADMIN'" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="userStore.role === 'ADMIN'" size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination style="margin-top:16px;justify-content:flex-end"
        v-model:current-page="current" v-model:page-size="size"
        :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="550px">
      <el-form :model="form" ref="formRef" :rules="rules" label-width="90px">
        <el-form-item label="课程编号" prop="courseNo">
          <el-input v-model="form.courseNo" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="学分" prop="credit">
          <el-input-number v-model="form.credit" :min="0.5" :max="10" :step="0.5" />
        </el-form-item>
        <el-form-item label="授课教师" prop="teacherId">
          <el-select v-model="form.teacherId" placeholder="请选择授课教师" style="width:100%">
            <el-option v-for="t in teacherOptions" :key="t.id"
              :label="t.name + '（' + t.teacherNo + '）'" :value="t.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="课容量" prop="capacity">
          <el-input-number v-model="form.capacity" :min="1" :max="200" />
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input v-model="form.semester" placeholder="如: 2025-2026-2" />
        </el-form-item>
        <el-form-item label="学时">
          <el-input-number v-model="form.classHours" :min="1" :max="200" />
        </el-form-item>
        <el-form-item label="课程简介">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { useUserStore } from '../store/user';
import { getCoursePage, getCourseList, saveCourse, updateCourse, deleteCourse } from '../api/course';
import { getTeacherList } from '../api/teacher';

const userStore=useUserStore()
// 授课教师下拉数据
const teacherOptions=ref([])
// 表格与分页
const tableData=ref([])
const loading=ref(false)
const current=ref(1)
const size=ref(10)
const total=ref(0)
const keyword=ref('')
// 弹窗
const dialogVisible=ref(false)
const dialogTitle=ref('新增课程')
const isEdit=ref(false)
const formRef=ref(null)
const emptyForm=()=>({
  id:null, courseNo:'', name:'', credit:2, teacherId:null,
  capacity:60, selectedNum:0, semester:'', classHours:48, description:''
})
const form=reactive(emptyForm())
const rules={
  courseNo:[{required:true,message:'请输入课程编号',trigger:'blur'}],
  name:[{required:true,message:'请输入课程名称',trigger:'blur'}],
  credit:[{required:true,message:'请输入学分',trigger:'change'}],
  teacherId:[{required:true,message:'请选择授课教师',trigger:'change'}],
  capacity:[{required:true,message:'请输入课容量',trigger:'change'}],
  semester:[{required:true,message:'请输入学期',trigger:'blur'}]
}

// 分页加载课程（含教师姓名）
async function loadData(){
  loading.value=true
  try{
    const res=await getCoursePage({current:current.value,size:size.value,keyword:keyword.value})
    tableData.value=res.data.records||[]
    total.value=res.data.total||0
  }finally{
    loading.value=false
  }
}

// 新增
function handleAdd(){
  Object.assign(form,emptyForm())
  isEdit.value=false
  dialogTitle.value='新增课程'
  dialogVisible.value=true
}

// 编辑
function handleEdit(row){
  Object.assign(form,emptyForm(),row)
  isEdit.value=true
  dialogTitle.value='编辑课程'
  dialogVisible.value=true
}

// 提交（新增/编辑）
async function handleSubmit(){
  const valid=await formRef.value.validate().catch(()=>false)
  if(!valid)return
  try{
    if(isEdit.value){
      await updateCourse(form)
      ElMessage.success('更新成功')
    }else{
      await saveCourse(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value=false
    loadData()
  }catch(e){/* 错误已统一提示 */}
}

// 删除课程
async function handleDelete(row){
  try{
    await ElMessageBox.confirm(`确定删除课程"${row.name}"吗？`,'警告',{type:'warning'})
  }catch(e){return}
  try{
    await deleteCourse(row.id)
    ElMessage.success('删除成功')
    loadData()
  }catch(e){/* 错误已统一提示 */}
}

onMounted(async ()=>{
  loadData()
  // 管理员弹窗需要教师下拉
  try{
    const res=await getTeacherList()
    teacherOptions.value=res.data||[]
  }catch(e){/* 教师接口对管理员/教师开放，学生访问被拒时静默处理 */}
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-bar { display: flex; align-items: center; }
</style>
