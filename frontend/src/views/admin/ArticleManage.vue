<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-button type="primary" @click="$router.push('/admin/article/edit')">写文章</el-button>
    </div>

    <el-table :data="articles" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="categoryName" label="分类" width="150" />
      <el-table-column label="标签" width="200">
        <template #default="scope">
          <el-tag v-for="tag in scope.row.tags" :key="tag.id" size="small" style="margin-right: 5px;">{{ tag.name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '已发布' : '草稿' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="scope">
          {{ scope.row.createTime ? scope.row.createTime.replace('T', ' ') : '' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="$router.push('/admin/article/edit/' + scope.row.id)">编辑</el-button>
          <el-popconfirm title="确定要删除这条记录吗?" @confirm="handleDelete(scope.row.id)">
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
import { getArticlePage, deleteArticle } from '@/api'
import { ElMessage } from 'element-plus'

const articles = ref([])
const total = ref(0)
const currentPage = ref(1)
const loading = ref(false)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getArticlePage({ current: currentPage.value, size: 10 })
    articles.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const handleDelete = async (id) => {
  await deleteArticle(id)
  ElMessage.success('删除成功')
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>
