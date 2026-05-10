<template>
  <div class="min-h-screen bg-gray-50 flex flex-col">
    <!-- Sticky Header -->
    <header class="bg-white/80 backdrop-blur-md border-b border-gray-100 shadow-sm sticky top-0 z-50 transition-all duration-300">
      <div class="container mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <!-- Logo -->
        <div class="flex items-center gap-2 cursor-pointer group" @click="$router.push('/')">
           <div class="w-8 h-8 rounded-lg bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white font-bold text-lg shadow-md group-hover:scale-110 transition-transform">AG</div>
           <span class="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-gray-800 to-gray-600 tracking-tight">Blog</span>
        </div>

        <!-- Navigation -->
        <div class="flex items-center gap-4">
           <template v-if="!isLogin">
             <button @click="$router.push('/login')" class="px-5 py-2 rounded-full text-sm font-medium text-gray-600 hover:text-blue-600 hover:bg-blue-50 transition-colors">登录</button>
             <button @click="$router.push('/register')" class="px-5 py-2 rounded-full text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 shadow-md hover:shadow-lg transition-all transform hover:-translate-y-0.5">注册</button>
           </template>
           
           <div v-else class="flex items-center gap-3">
              <span class="text-sm font-medium text-gray-500 hidden sm:block">你好, {{ username }}</span>
              <el-dropdown trigger="click" @command="handleCommand">
                <div class="relative cursor-pointer ring-2 ring-transparent hover:ring-blue-100 rounded-full transition-all">
                   <el-avatar :size="36" class="shadow-sm border border-gray-200" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                </div>
                <template #dropdown>
                  <el-dropdown-menu class="w-48 p-2">
                    <el-dropdown-item command="admin" v-if="role === 1 || role === '1'" class="rounded-md mb-1"><el-icon class="mr-2"><Monitor /></el-icon> 后台管理</el-dropdown-item>
                    <el-dropdown-item command="profile" class="rounded-md mb-1"><el-icon class="mr-2"><User /></el-icon> 个人资料</el-dropdown-item>
                    <div class="h-px bg-gray-100 my-1"></div>
                    <el-dropdown-item command="logout" class="rounded-md text-red-500 hover:bg-red-50"><el-icon class="mr-2"><SwitchButton /></el-icon> 退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
           </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="flex-grow container mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
    
    <!-- Footer -->
    <footer class="bg-white border-t border-gray-100 py-8 mt-12">
      <div class="container mx-auto px-4 text-center text-gray-400 text-sm">
        <p>&copy; 2026 Blog System. All rights reserved.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isLogin = ref(false)
const username = ref('')
const role = ref(0) // 0 user, 1 admin

onMounted(() => {
  const token = localStorage.getItem('token')
  if (token) {
    isLogin.value = true
    username.value = localStorage.getItem('username')
    role.value = localStorage.getItem('role')
  }
})

const handleCommand = (cmd) => {
  if (cmd === 'logout') {
    localStorage.clear()
    isLogin.value = false
    router.push('/login')
  } else if (cmd === 'admin') {
    router.push('/admin/article')
  }
}
</script>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
