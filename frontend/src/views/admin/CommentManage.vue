<template>
  <div>
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>评论管理</span>
          <el-select v-model="filterStatus" placeholder="全部状态" style="width: 150px; margin-right: 10px;" @change="loadComments">
            <el-option :label="'全部'" :value="null" />
            <el-option :label="'待审核'" :value="0" />
            <el-option :label="'已通过'" :value="1" />
            <el-option :label="'已拒绝'" :value="2" />
          </el-select>
        </div>
      </template>
      <el-table :data="comments" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="articleTitle" label="文章" min-width="200" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="content" label="评论内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="row.status === 2" type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="180">
          <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="success" size="small" @click="handleApprove(row.id)">通过</el-button>
            <el-button v-if="row.status === 0" type="danger" size="small" @click="handleReject(row.id)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 20px; justify-content: flex-end; display: flex;"
        :current-page="page"
        :page-size="size"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCommentPage, approveComment, rejectComment } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const comments = ref([])
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const total = ref(0)
const filterStatus = ref(null)

const formatTime = (time) => {
  return time ? time.replace('T', ' ') : ''
}

const loadComments = async () => {
  loading.value = true
  try {
    const params = { current: page.value, size: size.value }
    if (filterStatus.value !== null) {
      params.status = filterStatus.value
    }
    const res = await getCommentPage(params)
    comments.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const handlePageChange = (p) => {
  page.value = p
  loadComments()
}

const handleApprove = async (id) => {
  try {
    await ElMessageBox.confirm('确定要通过这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'success'
    })
    await approveComment(id)
    ElMessage.success('审核通过')
    loadComments()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(e.message || '操作失败')
    }
  }
}

const handleReject = async (id) => {
  try {
    await ElMessageBox.confirm('确定要拒绝这条评论吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await rejectComment(id)
    ElMessage.success('已拒绝')
    loadComments()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(e.message || '操作失败')
    }
  }
}

onMounted(() => {
  loadComments()
})
</script>
