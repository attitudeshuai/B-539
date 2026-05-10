<template>
  <div v-loading="loading">
    <el-card v-if="article">
      <template #header>
        <h1>{{ article.title }}</h1>
        <div class="meta" style="color: #999; margin-top: 10px;">
          <span>{{ formatTime(article.createTime) }}</span>
          <span style="margin-left: 20px;">分类: {{ article.categoryName }}</span>
          <span style="margin-left: 20px;">阅读: {{ article.viewCount }}</span>
          <span style="margin-left: 20px;">评论: {{ article.commentCount || 0 }}</span>
        </div>
        <div style="margin-top: 10px;">
          <el-tag v-for="tag in article.tags" :key="tag.id" style="margin-right: 10px;">{{ tag.name }}</el-tag>
        </div>
      </template>
      <div v-html="renderMarkdown(article.content)" class="markdown-body"></div>
    </el-card>

    <el-card style="margin-top: 20px;">
      <template #header>
        <span style="font-size: 18px; font-weight: bold;">评论 ({{ comments.length }})</span>
      </template>

      <div v-if="comments.length > 0" style="margin-bottom: 24px;">
        <div v-for="comment in comments" :key="comment.id" style="padding: 16px 0; border-bottom: 1px solid #f0f0f0;">
          <div style="display: flex; align-items: center; margin-bottom: 8px;">
            <el-avatar :size="32" style="background-color: #409EFF; margin-right: 10px;">
              {{ comment.nickname.charAt(0).toUpperCase() }}
            </el-avatar>
            <span style="font-weight: bold; margin-right: 12px;">{{ comment.nickname }}</span>
            <span style="color: #999; font-size: 13px;">{{ formatTime(comment.createTime) }}</span>
          </div>
          <div style="padding-left: 42px; color: #333; line-height: 1.6;">{{ comment.content }}</div>
        </div>
      </div>
      <div v-else style="text-align: center; color: #999; padding: 20px 0;">暂无评论，来抢沙发吧！</div>

      <el-divider />
      <h3 style="margin-bottom: 16px;">发表评论</h3>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="60px">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱（不会公开显示）" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="4" placeholder="请输入评论内容" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">提交评论</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail, getCommentList, submitComment } from '@/api'
import { marked } from 'marked'
import { ElMessage } from 'element-plus'

const route = useRoute()
const article = ref(null)
const comments = ref([])
const loading = ref(false)
const submitting = ref(false)
const formRef = ref(null)

const form = ref({
  nickname: '',
  email: '',
  content: ''
})

const rules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  content: [{ required: true, message: '请输入评论内容', trigger: 'blur' }]
}

const formatTime = (time) => {
  return time ? time.replace('T', ' ') : ''
}

const renderMarkdown = (text) => {
  if (!text) return ''
  return marked.parse(text)
}

const fetchComments = async () => {
  try {
    const id = route.params.id
    comments.value = await getCommentList(id)
  } catch (e) {
    // ignore
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    submitting.value = true
    try {
      await submitComment({
        articleId: Number(route.params.id),
        nickname: form.value.nickname,
        email: form.value.email,
        content: form.value.content
      })
      ElMessage.success('评论提交成功，等待审核')
      form.value.content = ''
    } catch (e) {
      // error handled by interceptor
    } finally {
      submitting.value = false
    }
  })
}

onMounted(async () => {
  loading.value = true
  try {
    const id = route.params.id
    article.value = await getArticleDetail(id)
    await fetchComments()
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
