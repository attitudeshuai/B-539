<template>
  <div class="login-container">
    <el-card class="login-card shadow-lg" :body-style="{ padding: '30px' }">
      <template #header>
        <div class="card-header">
          <h2>欢迎回来</h2>
          <p class="subtitle">登录你的账户</p>
        </div>
      </template>
      <el-form :model="form" label-position="top">
        <el-form-item label="用户名">
          <el-input v-model="form.username" size="large" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="form.password" size="large" placeholder="请输入密码" show-password @keyup.enter="handleLogin"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" @click="handleLogin" :loading="loading" class="w-100">登录</el-button>
        </el-form-item>
        <div class="text-center mt-3">
           <router-link to="/register">创建一个账号</router-link>
           <div style="margin-top: 5px;">
             <router-link to="/" style="font-size: 14px; color: #999;">返回首页</router-link>
           </div>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = ref({
  username: '',
  password: ''
})
const loading = ref(false)

const handleLogin = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await login(form.value)
    localStorage.setItem('token', res.token)
    localStorage.setItem('username', res.username)
    localStorage.setItem('role', res.role) // Store role
    ElMessage.success('登录成功')
    router.push('/admin/article')
  } catch (e) {
    // handled
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
  border-radius: 12px;
  border: none;
}
.card-header {
  text-align: center;
}
.card-header h2 {
  margin: 0;
  color: #333;
}
.subtitle {
  color: #666;
  font-size: 14px;
  margin-top: 5px;
}
.w-100 {
  width: 100%;
}
.text-center {
  text-align: center;
}
.mt-3 {
  margin-top: 15px;
}
</style>
