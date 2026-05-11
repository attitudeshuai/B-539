<template>
  <div>
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>评论管理</span>
          <el-radio-group v-model="statusFilter" @change="loadData">
            <el-radio-button :label="null">全部</el-radio-button>
            <el-radio-button :label="0">待审核</el-radio-button>
            <el-radio-button :label="1">已通过</el-radio-button>
            <el-radio-button :label="2">已拒绝</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="articleTitle" label="文章"></el-table-column>
        <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="180">
          <template #default="{ row }">{{ formatTime(row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="success" size="small" @click="approve(row.id)">通过</el-button>
            <el-button v-if="row.status === 0" type="danger" size="small" @click="reject(row.id)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 20px;"
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCommentPage, approveComment, rejectComment } from '@/api'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const statusFilter = ref(null)

const formatTime = (time) => {
  return time ? time.replace('T', ' ') : ''
}

const getStatusType = (status) => {
  if (status === 0) return 'warning'
  if (status === 1) return 'success'
  return 'danger'
}

const getStatusText = (status) => {
  if (status === 0) return '待审核'
  if (status === 1) return '已通过'
  return '已拒绝'
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: currentPage.value,
      size: pageSize.value
    }
    if (statusFilter.value !== null) {
      params.status = statusFilter.value
    }
    const res = await getCommentPage(params)
    tableData.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const approve = async (id) => {
  await ElMessageBox.confirm('确定通过该评论？', '提示', { type: 'warning' })
  await approveComment(id)
  ElMessage.success('审核通过')
  loadData()
}

const reject = async (id) => {
  await ElMessageBox.confirm('确定拒绝该评论？', '提示', { type: 'warning' })
  await rejectComment(id)
  ElMessage.success('已拒绝')
  loadData()
}

onMounted(() => {
  loadData()
})
</script>
