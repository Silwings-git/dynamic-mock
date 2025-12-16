<template>
  <div class="mock-handler-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Mock处理器管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增处理器
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="处理器名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入处理器名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="HTTP方法">
          <el-select
            v-model="searchForm.httpMethod"
            placeholder="请选择"
            clearable
            style="width: 150px"
          >
            <el-option label="GET" value="GET" />
            <el-option label="POST" value="POST" />
            <el-option label="PUT" value="PUT" />
            <el-option label="DELETE" value="DELETE" />
          </el-select>
        </el-form-item>
        <el-form-item label="请求URI">
          <el-input
            v-model="searchForm.requestUri"
            placeholder="请输入请求URI"
            clearable
          />
        </el-form-item>
        <el-form-item label="启用状态">
          <el-select
            v-model="searchForm.enableStatus"
            placeholder="请选择"
            clearable
            style="width: 150px"
          >
            <el-option label="全部" value="" />
            <el-option label="已启用" value="1" />
            <el-option label="已停用" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="name" label="处理器名称" width="180" />
        <el-table-column prop="projectName" label="项目名称" width="150" />
        <el-table-column prop="httpMethods" label="HTTP方法" width="120">
          <template #default="{ row }">
            <el-tag
              v-for="method in row.httpMethods"
              :key="method"
              size="small"
              style="margin-right: 5px"
            >
              {{ method }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="requestUri" label="请求URI" />
        <el-table-column prop="label" label="标签" width="120" />
        <el-table-column prop="enableStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              :model-value="row.enableStatus === 1"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">
              详情
            </el-button>
            <el-button type="primary" link @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="info" link @click="handleSnapshot(row)">
              快照
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.pageNo"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        class="pagination"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  queryMockHandlers,
  deleteMockHandler,
  updateHandlerEnableStatus
} from '@/api/mockHandler'
import { useProjectStore } from '@/stores/project'

const router = useRouter()
const projectStore = useProjectStore()

const loading = ref(false)
const tableData = ref([])

const searchForm = reactive({
  projectId: null,
  name: '',
  httpMethod: '',
  requestUri: '',
  enableStatus: ''
})

const pagination = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
})

// 监听项目切换
watch(
  () => projectStore.currentProjectId,
  (newProjectId) => {
    if (newProjectId) {
      searchForm.projectId = newProjectId
      pagination.pageNo = 1
      loadData()
    }
  }
)

onMounted(() => {
  searchForm.projectId = projectStore.currentProjectId
  loadData()
})

async function loadData() {
  try {
    loading.value = true
    const data = await queryMockHandlers({
      projectId: searchForm.projectId,
      name: searchForm.name,
      httpMethod: searchForm.httpMethod,
      requestUri: searchForm.requestUri,
      enableStatus: searchForm.enableStatus,
      pageNo: pagination.pageNo,
      pageSize: pagination.pageSize
    })
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('Load mock handlers error:', error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.pageNo = 1
  loadData()
}

function handleReset() {
  searchForm.name = ''
  searchForm.httpMethod = ''
  searchForm.requestUri = ''
  searchForm.enableStatus = ''
  handleSearch()
}

function handleAdd() {
  router.push('/mock-handler/edit')
}

function handleDetail(row) {
  router.push(`/mock-handler/detail/${row.handlerId}`)
}

function handleEdit(row) {
  router.push(`/mock-handler/edit/${row.handlerId}`)
}

function handleSnapshot(row) {
  router.push(`/mock-handler/snapshot/${row.handlerId}`)
}

async function handleStatusChange(row) {
  try {
    const newStatus = row.enableStatus === 1 ? 2 : 1
    await updateHandlerEnableStatus({
      handlerId: row.handlerId,
      enableStatus: newStatus
    })
    ElMessage.success(newStatus === 1 ? '已启用' : '已停用')
    loadData()
  } catch (error) {
    console.error('Update status error:', error)
    ElMessage.error('状态更新失败')
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除处理器"${row.name}"吗？`,
      '警告',
      {
        type: 'warning'
      }
    )

    await deleteMockHandler({ handlerId: row.handlerId })
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    // 取消操作
  }
}
</script>

<style scoped>
.mock-handler-list-container {
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
