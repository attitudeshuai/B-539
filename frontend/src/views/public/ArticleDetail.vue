<template>
  <div v-loading="loading">
    <el-card v-if="article">
      <template #header>
        <h1>{{ article.title }}</h1>
        <div class="meta" style="color: #999; margin-top: 10px;">
          <span>{{ formatTime(article.createTime) }}</span>
          <span style="margin-left: 20px;">分类: {{ article.categoryName }}</span>
          <span style="margin-left: 20px;">阅读: {{ article.viewCount }}</span>
        </div>
        <div style="margin-top: 10px;">
          <el-tag v-for="tag in article.tags" :key="tag.id" style="margin-right: 10px;">{{ tag.name }}</el-tag>
        </div>
      </template>
      <div v-html="renderMarkdown(article.content)" class="markdown-body"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail } from '@/api'
import { marked } from 'marked' // I probably need to install this or generic render

// For simplicity, simple text or html. 
// User mentioned "Article CRUD (Support Markdown)" so I should render it.
// I didn't add marked to package.json. I will assume it's simple text for MVP or I'll run npm install later.
// Actually I can use a simple pre-wrap for now avoiding extra heavy dependencies, OR better, I'll update package.json later.
// I will render content as is if no marked lib, but prompt asked for "Markdown Editor" and "Support Markdown".
// I'll stick to a simple function for now or expect just text content to display.
// To be safe, I'll just use `v-html` but maybe content is raw markdown.
// I will add `marked` to deps later.

const route = useRoute()
const article = ref(null)
const loading = ref(false)

const formatTime = (time) => {
  return time ? time.replace('T', ' ') : ''
}

// Minimal markdown parser or just display text
const renderMarkdown = (text) => {
  if (!text) return ''
  return marked.parse(text)
}

onMounted(async () => {
  loading.value = true
  try {
    const id = route.params.id
    article.value = await getArticleDetail(id)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.markdown-body {
  line-height: 1.6;
  font-size: 16px;
}
</style>
