<template>
  <div class="home">
    <div class="text-center py-16 lg:py-24 bg-gradient-to-b from-transparent to-gray-50/50 rounded-3xl mb-12">
      <h1 class="text-4xl md:text-5xl lg:text-6xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-gray-900 to-gray-600 mb-6 tracking-tight">探索观点 & 见解</h1>
      <p class="text-lg text-gray-500 mb-10 max-w-2xl mx-auto">发现来自不同领域作者的故事、思考与专业知识。</p>
      
      <div class="max-w-xl mx-auto relative group">
        <div class="absolute -inset-1 bg-gradient-to-r from-blue-600 to-indigo-600 rounded-lg blur opacity-25 group-hover:opacity-50 transition duration-1000 group-hover:duration-200"></div>
        <div class="relative">
          <input 
            v-model="keyword" 
            placeholder="搜索文章..." 
            class="w-full px-6 py-4 rounded-lg border-0 shadow-lg ring-1 ring-gray-900/5 placeholder:text-gray-400 focus:ring-2 focus:ring-blue-500 text-lg transition-all"
            @keyup.enter="fetchArticles"
          >
          <button 
            @click="fetchArticles"
            class="absolute right-2 top-2 bottom-2 px-6 bg-gray-900 text-white rounded-md font-medium hover:bg-black transition-colors"
          >
            搜索
          </button>
        </div>
      </div>
    </div>
    
    <div v-loading="loading" class="min-h-[400px]">
      <div v-if="articles.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
        <div 
          v-for="article in articles" 
          :key="article.id" 
          class="group bg-white rounded-2xl border border-gray-100 overflow-hidden hover:shadow-2xl transition-all duration-300 cursor-pointer flex flex-col"
          @click="goDetail(article.id)"
        >
          <!-- Optional Image Placeholder -->
          <div class="h-48 bg-gray-100 flex items-center justify-center relative overflow-hidden">
             <div class="absolute inset-0 bg-gradient-to-t from-black/50 to-transparent opacity-60 z-10"></div>
             <!-- Ideally we have images, for now use a pattern or gradient -->
             <div class="absolute inset-0 bg-blue-50 group-hover:scale-105 transition-transform duration-500" style="background-image: radial-gradient(#cbd5e1 1px, transparent 1px); background-size: 20px 20px;"></div>
             <span class="z-20 text-white font-bold text-xl tracking-wider opacity-80 select-none">ARTICLE</span>
          </div>

          <div class="p-6 flex-grow flex flex-col">
             <div class="flex flex-wrap gap-2 mb-4">
                <span v-if="article.categoryName" class="px-3 py-1 text-xs font-bold text-blue-600 bg-blue-50 rounded-full uppercase tracking-wider">{{ article.categoryName }}</span>
                <span v-for="tag in article.tags" :key="tag.id" class="px-2 py-1 text-xs font-medium text-gray-500 bg-gray-100 rounded-md">#{{ tag.name }}</span>
             </div>

             <h3 class="text-xl font-bold text-gray-900 mb-3 group-hover:text-blue-600 transition-colors line-clamp-2">{{ article.title }}</h3>
             
             <p class="text-gray-500 text-sm leading-relaxed mb-6 line-clamp-3 flax-grow">
               {{ article.content ? article.content.substring(0, 120) + '...' : '暂无预览。' }}
             </p>

             <div class="mt-auto pt-4 border-t border-gray-50 flex items-center justify-between text-xs text-gray-400 font-medium uppercase tracking-wide">
               <span class="flex items-center gap-1"><el-icon><Calendar /></el-icon> {{ formatTime(article.createTime) }}</span>
               <span class="flex items-center gap-1"><el-icon><View /></el-icon> {{ article.viewCount }}</span>
             </div>
          </div>
        </div>
      </div>
      
      <div v-else class="flex flex-col items-center justify-center py-20 text-center">
        <el-icon class="text-6xl text-gray-200 mb-4"><Document /></el-icon>
        <p class="text-gray-500 text-lg">没有找到相关文章。</p>
      </div>
    </div>

    <div class="mt-16 flex justify-center">
       <el-pagination
        v-if="total > 0"
        background
        layout="prev, pager, next"
        :total="total"
        v-model:current-page="currentPage"
        @current-change="fetchArticles"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticlePage } from '@/api'
import { useRouter } from 'vue-router'

const router = useRouter()
const articles = ref([])
const total = ref(0)
const currentPage = ref(1)
const keyword = ref('')
const loading = ref(false)

const fetchArticles = async () => {
  loading.value = true
  try {
    const res = await getArticlePage({
      current: currentPage.value,
      size: 9, // changed to 9 for grid 3x3
      keyword: keyword.value
    })
    articles.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const goDetail = (id) => {
  router.push(`/article/${id}`)
}

const formatTime = (time) => {
  return time ? time.split('T')[0] : ''
}

onMounted(() => {
  fetchArticles()
})
</script>

<style scoped>
/* Scoped styles removed in favor of Tailwind classes, keeping empty if needed for specific overrides */
</style>
