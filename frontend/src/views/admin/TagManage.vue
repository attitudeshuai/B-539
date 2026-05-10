<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-button type="primary" @click="handleCreate">新建标签</el-button>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="form.id ? '编辑标签' : '新建标签'" v-model="dialogVisible">
      <el-form :model="form">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTagList, saveTag, updateTag, deleteTag } from '@/api'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const form = ref({ id: null, name: '' })

const fetch = async () => {
  tableData.value = await getTagList()
}

const handleCreate = () => {
  form.value = { id: null, name: '' }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  await deleteTag(id)
  ElMessage.success('删除成功')
  fetch()
}

const submit = async () => {
  if (!form.value.name) return
  if (form.value.id) {
    await updateTag(form.value)
  } else {
    await saveTag(form.value)
  }
  ElMessage.success('成功')
  dialogVisible.value = false
  fetch()
}

onMounted(fetch)
</script>
