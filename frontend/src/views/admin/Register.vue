<template>
  <div class="login-container">
    <el-card class="login-card shadow-lg" :body-style="{ padding: '30px' }">
      <template #header>
        <div class="card-header">
          <h2>注册</h2>
        </div>
      </template>
      <el-form :model="form" label-position="top">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" class="w-100">注册</el-button>
        </el-form-item>
        <div class="text-center mt-3">
           <router-link to="/login">已有账号? 去登录</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = ref({
  username: '',
  password: '',
  nickname: ''
})
const loading = ref(false)

const handleRegister = async () => {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请填写完整信息')
    return
  }
  loading.value = true
  try {
    await register(form.value)
    ElMessage.success('注册成功')
    router.push('/login')
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
