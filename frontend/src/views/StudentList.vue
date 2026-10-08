<template>
  <div>
    <el-card>
      <template #header>
        <div class="card-header">
          <span>学生管理</span>
          <el-button type="primary" @click="handleAdd">新增学生</el-button>
        </div>
      </template>

      <div class="search-bar">
        <el-input v-model="keyword" placeholder="搜索姓名或学号" style="width:240px"
          clearable @clear="loadData" @keyup.enter="loadData" />
        <el-button type="primary" @click="loadData" style="margin-left:8px">搜索</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="margin-top:16px">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="studentNo" label="学号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="60" />
        <el-table-column prop="className" label="班级" width="160" />
        <el-table-column prop="grade" label="年级" width="80" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="username" label="登录账号" width="120" />
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" ref="formRef" :rules="rules" label-width="80px">
        <el-form-item label="学号" prop="studentNo">
          <el-input v-model="form.studentNo" :disabled="isEdit" />
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
        <el-form-item label="班级" prop="className">
          <el-input v-model="form.className" />
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input v-model="form.grade" />
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
import { getStudentPage, saveStudent, updateStudent, deleteStudent } from '../api/student';

// 数据存放于表格
const tableData=ref([])
// 加载动画
const loading=ref(false)
// 默认第一页
const current=ref(1)
// 默认10条
const size=ref(10)
// 总数
const total=ref(0)
// 模糊查询 默认为空
const keyword=ref('')
// 弹窗控制
const dialogVisible=ref(false)
// 弹窗标题
const dialogTitle=ref('新增学生')
// 标记新增/编辑
const isEdit=ref(false)
// 表单引用（用于校验）
const formRef=ref(null)
// 表单默认值
const emptyForm=()=>({
  id:null, studentNo:'', name:'', gender:'男',
  className:'', grade:'', phone:'', username:'', password:''
})
const form=reactive(emptyForm())
// 表单校验规则
const rules={
  studentNo:[{required:true,message:'请输入学号',trigger:'blur'}],
  name:[{required:true,message:'请输入姓名',trigger:'blur'}],
  className:[{required:true,message:'请输入班级',trigger:'blur'}],
  grade:[{required:true,message:'请输入年级',trigger:'blur'}],
  phone:[{required:true,message:'请输入联系电话',trigger:'blur'}],
  username:[{required:true,message:'请输入登录账号',trigger:'blur'}]
}

// 分页加载学生列表
async function loadData(){
  loading.value=true
  try{
    const res=await getStudentPage({current:current.value,size:size.value,keyword:keyword.value})
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
  dialogTitle.value='新增学生'
  dialogVisible.value=true
}

// 打开编辑对话框
function handleEdit(row){
  Object.assign(form,emptyForm(),row)
  isEdit.value=true
  dialogTitle.value='编辑学生'
  dialogVisible.value=true
}

// 提交（新增/编辑）
async function handleSubmit(){
  const valid=await formRef.value.validate().catch(()=>false)
  if(!valid)return
  try{
    if(isEdit.value){
      await updateStudent(form)
      ElMessage.success('更新成功')
    }else{
      await saveStudent(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value=false
    loadData()
  }catch(e){/* 错误已统一提示 */}
}

// 删除学生
async function handleDelete(row){
  try{
    await ElMessageBox.confirm(`确定删除学生"${row.name}"吗？删除后其选课记录一并移除。`,'警告',{type:'warning'})
  }catch(e){return}
  try{
    await deleteStudent(row.id)
    ElMessage.success('删除成功')
    loadData()
  }catch(e){/* 错误已统一提示 */}
}

onMounted(loadData)
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.search-bar { display: flex; align-items: center; }
</style>
