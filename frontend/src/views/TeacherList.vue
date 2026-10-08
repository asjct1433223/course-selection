<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>教师管理</span>
          <el-button type="primary" @click="handleAdd">新增教师</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="teacherNo" label="工号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="title" label="职称" width="100" />
        <el-table-column prop="department" label="所属院系" width="200" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination style="margin-top:16px;justify-content:flex-end"
        v-model:current-page="current" v-model:page-size="size"
        :total="total" layout="total, prev, pager, next" @current-change="loadData" />
    </el-card>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" ref="formRef" :rules="rules" label-width="80px">
        <el-form-item label="工号" prop="teacherNo">
          <el-input v-model="form.teacherNo" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="form.gender" style="width:100%">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="职称">
          <el-select v-model="form.title" style="width:100%">
            <el-option label="教授" value="教授" />
            <el-option label="副教授" value="副教授" />
            <el-option label="讲师" value="讲师" />
            <el-option label="助教" value="助教" />
          </el-select>
        </el-form-item>
        <el-form-item label="院系" prop="department">
          <el-input v-model="form.department" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="登录账号" prop="username">
          <el-input v-model="form.username" placeholder="用于登录的用户名" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="登录密码" prop="password">
          <el-input v-model="form.password" show-password placeholder="不填则默认为 123456" />
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
import { getTeacherPage, saveTeacher, updateTeacher, deleteTeacher } from '../api/teacher';

// 表格数据
const tableData=ref([])
// 加载动画
const loading=ref(false)
// 分页参数
const current=ref(1)
const size=ref(10)
const total=ref(0)
// 弹窗状态
const dialogVisible=ref(false)
const dialogTitle=ref('新增教师')
const isEdit=ref(false)
const formRef=ref(null)
// 表单默认值
const emptyForm=()=>({
  id:null, teacherNo:'', name:'', gender:'男', title:'讲师',
  department:'', phone:'', username:'', password:''
})
const form=reactive(emptyForm())
// 表单校验规则
const rules={
  teacherNo:[{required:true,message:'请输入工号',trigger:'blur'}],
  name:[{required:true,message:'请输入姓名',trigger:'blur'}],
  department:[{required:true,message:'请输入所属院系',trigger:'blur'}],
  phone:[{required:true,message:'请输入联系电话',trigger:'blur'}],
  username:[{required:true,message:'请输入登录账号',trigger:'blur'}]
}

// 分页加载教师列表
async function loadData(){
  loading.value=true
  try{
    const res=await getTeacherPage({current:current.value,size:size.value,keyword:''})
    tableData.value=res.data.records||[]
    total.value=res.data.total||0
  }finally{
    loading.value=false
  }
}

// 打开新增对话框
function handleAdd(){
  Object.assign(form,emptyForm())
  isEdit.value=false
  dialogTitle.value='新增教师'
  dialogVisible.value=true
}

// 打开编辑对话框
function handleEdit(row){
  Object.assign(form,emptyForm(),row)
  isEdit.value=true
  dialogTitle.value='编辑教师'
  dialogVisible.value=true
}

// 提交（新增/编辑）
async function handleSubmit(){
  const valid=await formRef.value.validate().catch(()=>false)
  if(!valid)return
  try{
    if(isEdit.value){
      await updateTeacher(form)
      ElMessage.success('更新成功')
    }else{
      await saveTeacher(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value=false
    loadData()
  }catch(e){/* 错误已统一提示 */}
}

// 删除教师
async function handleDelete(row){
  try{
    await ElMessageBox.confirm(`确定删除教师"${row.name}"吗？`,'警告',{type:'warning'})
  }catch(e){return}
  try{
    await deleteTeacher(row.id)
    ElMessage.success('删除成功')
    loadData()
  }catch(e){/* 错误已统一提示 */}
}

onMounted(loadData)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
