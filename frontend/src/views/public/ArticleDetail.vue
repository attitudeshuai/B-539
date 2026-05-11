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

    <el-card style="margin-top: 20px;">
      <template #header>
        <span>发表评论</span>
      </template>
      <el-form :model="commentForm" label-width="80px">
        <el-form-item label="昵称">
          <el-input v-model="commentForm.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="commentForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="评论内容">
          <el-input v-model="commentForm.content" type="textarea" :rows="4" placeholder="请输入评论内容"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitComment">提交评论</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 20px;">
      <template #header>
        <span>评论列表 ({{ comments.length }})</span>
      </template>
      <el-empty v-if="comments.length === 0" description="暂无评论"></el-empty>
      <div v-else>
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-header">
            <span class="comment-nickname">{{ comment.nickname }}</span>
            <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
          </div>
          <div class="comment-content">{{ comment.content }}</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail, addComment, getArticleComments } from '@/api'
import { ElMessage } from 'element-plus'
import { marked } from 'marked'

const route = useRoute()
const article = ref(null)
const comments = ref([])
const loading = ref(false)

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
  const id = route.params.id
  const res = await getArticleComments(id)
  comments.value = res
}

const submitComment = async () => {
  if (!commentForm.value.nickname || !commentForm.value.email || !commentForm.value.content) {
    ElMessage.warning('请填写完整信息')
    return
  }
  const id = route.params.id
  await addComment({
    articleId: id,
    nickname: commentForm.value.nickname,
    email: commentForm.value.email,
    content: commentForm.value.content
  })
  ElMessage.success('评论提交成功，等待审核')
  commentForm.value.nickname = ''
  commentForm.value.email = ''
  commentForm.value.content = ''
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
  margin-bottom: 8px;
}
.comment-nickname {
  font-weight: bold;
  color: #409eff;
}
.comment-time {
  margin-left: 15px;
  color: #999;
  font-size: 12px;
}
.comment-content {
  color: #666;
  line-height: 1.6;
}
</style>
