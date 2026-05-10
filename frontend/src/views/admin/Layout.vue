<template>
  <el-container style="height: 100vh;">
    <el-aside width="200px" style="background-color: #304156;">
      <el-menu
        router
        :default-active="$route.path"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        style="border: none;"
      >
        <div style="height: 50px; line-height: 50px; color: #fff; text-align: center; font-weight: bold;">
          博客管理后台
        </div>
        <el-menu-item index="/admin/article">
          <el-icon><Document /></el-icon>
          <span>文章管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/category">
          <el-icon><Folder /></el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/tag">
          <el-icon><Collection /></el-icon>
          <span>标签管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/user" v-if="role == 1">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/comment">
          <el-icon><ChatDotSquare /></el-icon>
          <span>评论管理</span>
        </el-menu-item>
        <el-menu-item index="/" @click="logout">
          <el-icon><SwitchButton /></el-icon>
          <span>退出后台</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background-color: #fff; display: flex; align-items: center; justify-content: flex-end; border-bottom: 1px solid #e6e6e6;">
        <span>欢迎, {{ username }}</span>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref(localStorage.getItem('username') || 'Admin')
const role = ref(localStorage.getItem('role'))

const logout = () => {
  localStorage.clear()
  router.push('/login')
}
</script>
