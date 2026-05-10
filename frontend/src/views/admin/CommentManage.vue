<template>
  <div>
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-size: 18px; font-weight: bold;">评论管理</span>
          <el-select v-model="filterStatus" placeholder="筛选状态" clearable style="width: 150px;" @change="fetchComments">
            <el-option label="待审核" :value="0" />
            <el-option label="通过" :value="1" />
            <el-option label="拒绝" :value="2" />
          </el-select>
        </div>
      </template>

      <el-table :data="comments" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="articleId" label="文章ID" width="80" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="content" label="内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="row.status === 1" type="success">通过</el-tag>
            <el-tag v-else-if="row.status === 2" type="danger">拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ip" label="IP" width="130" />
        <el-table-column prop="createTime" label="提交时间" width="170">
          <template #default="{ row }">
            {{ row.createTime ? row.createTime.replace('T', ' ') : '' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="success" size="small" @click="handleApprove(row.id)">通过</el-button>
            <el-button v-if="row.status === 0" type="warning" size="small" @click="handleReject(row.id)">拒绝</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 20px; display: flex; justify-content: flex-end;">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          v-model:current-page="currentPage"
          @current-change="fetchComments"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCommentPage, approveComment, rejectComment, deleteComment } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const comments = ref([])
const total = ref(0)
const currentPage = ref(1)
const filterStatus = ref(null)
const loading = ref(false)

const fetchComments = async () => {
  loading.value = true
  try {
    const params = {
      current: currentPage.value,
      size: 10
    }
    if (filterStatus.value !== null && filterStatus.value !== '') {
      params.status = filterStatus.value
    }
    const res = await getCommentPage(params)
    comments.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const handleApprove = async (id) => {
  await approveComment(id)
  ElMessage.success('已通过')
  fetchComments()
}

const handleReject = async (id) => {
  await rejectComment(id)
  ElMessage.success('已拒绝')
  fetchComments()
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定要删除该评论吗？', '提示', { type: 'warning' })
  await deleteComment(id)
  ElMessage.success('删除成功')
  fetchComments()
}

onMounted(() => {
  fetchComments()
})
</script>
