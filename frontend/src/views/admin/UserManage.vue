<template>
  <div>
    <h2>用户管理</h2>
    <el-table :data="tableData" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="nickname" label="昵称" />
      <el-table-column label="角色" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.role === 1 ? 'danger' : 'success'">
            {{ scope.row.role === 1 ? '管理员' : '用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间">
        <template #default="scope">
           {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-popconfirm title="确定删除用户?" @confirm="handleDelete(scope.row.id)" v-if="scope.row.username !== 'admin'">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

     <div style="margin-top: 20px; text-align: right;">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        v-model:current-page="currentPage"
        @current-change="fetchData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserPage, deleteUser } from '@/api'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const total = ref(0)
const currentPage = ref(1)
const loading = ref(false)

const formatTime = (time) => {
  return time ? time.replace('T', ' ') : ''
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getUserPage({ current: currentPage.value, size: 10 })
    tableData.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const handleDelete = async (id) => {
  await deleteUser(id)
  ElMessage.success('删除成功')
  fetchData()
}

onMounted(fetchData)
</script>
