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

<!-- 单文件组件 
 script 在引入时执行一次
 script setup 每次组件实例被创建时执行,被编译在同一个作用域内的setup()渲染函数:
                性能更好,代码更简洁
-->
<script setup>
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/user';
import { reactive, ref } from 'vue';
import { login } from '../api/auth';
import { ElMessage } from 'element-plus';

const router=useRouter()//路由 跳转,替换页面等
const userStore=useUserStore()//用户状态数据
const formRef=ref(null)//初始数据,后续浏览器本地会存储数据时,就不是null
const loading=ref(false)//加载动画
const form=reactive({//响应式引用类型数据
  username:'',password:''
})
//表单校验
const rules={//                                    校验时机:失去焦点时校验
  username:[{required:true,message:'请输入用户名',trigger:'blur'}],
  password:[{required:true,message:'请输入密码',trigger:'blur'}]
}
// 登陆函数 handleLogin
async function handleLogin() { //不写参数  参数写到login(data)
  //表单校验失败返回false,不抛异常
  const vaild=await formRef.value.validate().catch(()=>false)
  if(!vaild)return//结束函数
  //调用动画
  loading.value=true
  try {
    const res=await login(form)//form表单中的用户名和密码
    userStore.setLogin(res.data)
    ElMessage.success('登陆成功')
    //首页跳转
    router.push('/dashboard')
  } catch (error) {}finally{
    loading.value=false
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