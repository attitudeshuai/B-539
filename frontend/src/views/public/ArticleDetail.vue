<template>
  <div v-loading="loading">
    <el-card v-if="article">
      <template #header>
        <h1>{{ article.title }}</h1>
        <div class="meta" style="color: #999; margin-top: 10px;">
          <span>{{ formatTime(article.createTime) }}</span>
          <span style="margin-left: 20px;">分类: {{ article.categoryName }}</span>
          <span style="margin-left: 20px;">阅读: {{ article.viewCount }}</span>
          <span style="margin-left: 20px;">评论: {{ article.commentCount }}</span>
        </div>
        <div style="margin-top: 10px;">
          <el-tag v-for="tag in article.tags" :key="tag.id" style="margin-right: 10px;">{{ tag.name }}</el-tag>
        </div>
      </template>
      <div v-html="renderMarkdown(article.content)" class="markdown-body"></div>
    </el-card>

    <el-card v-if="article" style="margin-top: 20px;">
      <template #header>
        <h3>发表评论</h3>
      </template>
      <el-form :model="commentForm" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="commentForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="commentForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="评论">
          <el-input v-model="commentForm.content" type="textarea" :rows="4" placeholder="请输入评论内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmitComment" :loading="submitting">提交评论</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="article" style="margin-top: 20px;">
      <template #header>
        <h3>评论列表 ({{ comments.length }})</h3>
      </template>
      <el-empty v-if="comments.length === 0" description="暂无评论" />
      <div v-for="comment in comments" :key="comment.id" class="comment-item">
        <div class="comment-header">
          <strong>{{ comment.nickname }}</strong>
          <span style="color: #999; margin-left: 10px;">{{ formatTime(comment.createTime) }}</span>
        </div>
        <div class="comment-content">{{ comment.content }}</div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail, submitComment, getArticleComments } from '@/api'
import { marked } from 'marked'
import { ElMessage } from 'element-plus'

const route = useRoute()
const article = ref(null)
const comments = ref([])
const loading = ref(false)
const submitting = ref(false)
const commentForm = ref({
  nickname: '',
  email: '',
  content: ''
})

const formatTime = (time) => {
  return time ? time.replace('T', ' ') : ''
}

const renderMarkdown = (text) => {
  if (!text) return ''
  return marked.parse(text)
}

const loadComments = async () => {
  try {
    const id = route.params.id
    comments.value = await getArticleComments(id)
  } catch (e) {
    console.error(e)
  }
}

const handleSubmitComment = async () => {
  if (!commentForm.value.nickname || !commentForm.value.email || !commentForm.value.content) {
    ElMessage.warning('请填写完整的评论信息')
    return
  }
  submitting.value = true
  try {
    await submitComment({
      articleId: route.params.id,
      nickname: commentForm.value.nickname,
      email: commentForm.value.email,
      content: commentForm.value.content
    })
    ElMessage.success('评论提交成功，等待审核')
    commentForm.value = { nickname: '', email: '', content: '' }
  } catch (e) {
    ElMessage.error(e.message || '评论提交失败')
  } finally {
    submitting.value = false
  }
}

onMounted(async () => {
  loading.value = true
  try {
    const id = route.params.id
    article.value = await getArticleDetail(id)
    await loadComments()
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
.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}
.comment-item:last-child {
  border-bottom: none;
}
.comment-header {
  margin-bottom: 5px;
}
.comment-content {
  color: #333;
  line-height: 1.6;
}
</style>
