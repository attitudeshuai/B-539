<template>
  <div>
    <h2>{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
    <el-form :model="form" label-width="100px" style="max-width: 800px;">
      <el-form-item label="标题">
        <el-input v-model="form.title" placeholder="请输入文章标题" />
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="form.categoryId" placeholder="请选择分类">
          <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="form.tagIds" multiple placeholder="请选择标签">
          <el-option v-for="item in tags" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="内容">
        <el-input type="textarea" v-model="form.content" :rows="15" placeholder="Markdown 内容..." />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
          <el-radio :label="0">草稿</el-radio>
          <el-radio :label="1">发布</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit" :loading="loading">保存</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getArticleDetail, saveArticle, updateArticle, getCategoryList, getTagList } from '@/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const isEdit = ref(false)
const loading = ref(false)
const categories = ref([])
const tags = ref([])

const form = ref({
  id: null,
  title: '',
  content: '',
  categoryId: null,
  tagIds: [],
  status: 1
})

const fetchData = async () => {
  const [catRes, tagRes] = await Promise.all([getCategoryList(), getTagList()])
  categories.value = catRes
  tags.value = tagRes

  if (route.params.id) {
    isEdit.value = true
    const res = await getArticleDetail(route.params.id)
    form.value = {
      id: res.id,
      title: res.title,
      content: res.content,
      categoryId: res.categoryId,
      tagIds: res.tags.map(t => t.id),
      status: res.status
    }
  }
}

const submit = async () => {
  if (!form.value.title || !form.value.content) {
    ElMessage.warning('标题和内容不能为空')
    return
  }
  loading.value = true
  try {
    if (isEdit.value) {
      await updateArticle(form.value)
    } else {
      await saveArticle(form.value)
    }
    ElMessage.success('保存成功')
    router.push('/admin/article')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})
</script>
