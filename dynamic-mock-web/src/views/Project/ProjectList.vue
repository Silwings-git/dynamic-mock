<template>
  <div class="project-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>项目管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增项目
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="项目名称">
          <el-input
            v-model="searchForm.projectName"
            placeholder="请输入项目名称"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="projectId" label="项目ID" width="180" />
        <el-table-column prop="projectName" label="项目名称" />
        <el-table-column prop="baseUri" label="Base URI" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        class="pagination"
      />
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="Base URI" prop="baseUri">
          <el-input
            v-model="form.baseUri"
            placeholder="请输入Base URI（可选），如：/api/v1"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="handleSave">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryProjects, saveProject, deleteProject } from '@/api/project'
import { useProjectStore } from '@/stores/project'

const projectStore = useProjectStore()

const loading = ref(false)
const saveLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  projectName: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  projectId: null,
  projectName: '',
  baseUri: ''
})

const formRules = {
  projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }]
}

const dialogTitle = computed(() =>
  form.projectId ? '编辑项目' : '新增项目'
)

onMounted(() => {
  loadData()
})

async function loadData() {
  try {
    loading.value = true
    const data = await queryProjects({
      projectName: searchForm.projectName,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('Load projects error:', error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.pageNum = 1
  loadData()
}

function handleReset() {
  searchForm.projectName = ''
  handleSearch()
}

function handleAdd() {
  form.projectId = null
  form.projectName = ''
  form.baseUri = ''
  dialogVisible.value = true
}

function handleEdit(row) {
  form.projectId = row.projectId
  form.projectName = row.projectName
  form.baseUri = row.baseUri
  dialogVisible.value = true
}

async function handleSave() {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    saveLoading.value = true

    await saveProject({
      projectId: form.projectId,
      projectName: form.projectName,
      baseUri: form.baseUri
    })

    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
    projectStore.loadProjects()
  } catch (error) {
    console.error('Save project error:', error)
  } finally {
    saveLoading.value = false
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除项目"${row.projectName}"吗？`,
      '警告',
      {
        type: 'warning'
      }
    )

    await deleteProject({ projectId: row.projectId })
    ElMessage.success('删除成功')
    loadData()
    projectStore.loadProjects()
  } catch (error) {
    // 取消操作
  }
}

function handleDialogClose() {
  formRef.value?.resetFields()
}
</script>

<style scoped>
.project-list-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
