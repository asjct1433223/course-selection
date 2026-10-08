// 用户状态管理（Pinia + localStorage 持久化）

import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useUserStore = defineStore('user', () => {
    // 登录后由后端返回并持久化到 localStorage，刷新不丢失
    const token = ref(localStorage.getItem('token') || '')      // JWT，放入请求头鉴权
    const username = ref(localStorage.getItem('username') || '')// 登录账号
    const realName = ref(localStorage.getItem('realName') || '')// 真实姓名
    const role = ref(localStorage.getItem('role') || '')        // ADMIN/TEACHER/STUDENT
    const roleName = ref(localStorage.getItem('roleName') || '')// 角色中文名
    const studentId = ref(localStorage.getItem('studentId') || '') // 学生登录时返回
    const teacherId = ref(localStorage.getItem('teacherId') || '') // 教师登录时返回

    // 计算属性：是否已登录
    const isLoggedIn = computed(() => !!token.value)

    // 登录成功后的存储方法
    function setLogin(data) {
        token.value = data.token
        username.value = data.username
        realName.value = data.realName
        role.value = data.role
        roleName.value = data.roleName
        studentId.value = data.studentId || ''
        teacherId.value = data.teacherId || ''

        localStorage.setItem('token', data.token)
        localStorage.setItem('username', data.username)
        localStorage.setItem('realName', data.realName)
        localStorage.setItem('role', data.role)
        localStorage.setItem('roleName', data.roleName)
        localStorage.setItem('studentId', data.studentId || '')
        localStorage.setItem('teacherId', data.teacherId || '')
    }

    // 退出登录：清空状态与本地缓存
    function logout() {
        token.value = ''
        username.value = ''
        realName.value = ''
        role.value = ''
        roleName.value = ''
        studentId.value = ''
        teacherId.value = ''
        localStorage.clear()
    }

    return { token, username, realName, role, roleName, studentId, teacherId, isLoggedIn, setLogin, logout }
})

