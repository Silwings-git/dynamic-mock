<template>
  <div class="task-log-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>任务日志</span>
          <el-button
            type="danger"
            :disabled="!tableData.length"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="处理器">
          <el-select
            v-model="searchForm.handlerId"
            placeholder="请选择处理器"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="handler in handlerList"
              :key="handler.handlerId"
              :label="handler.name"
              :value="handler.handlerId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="任务代码">
          <el-input
            v-model="searchForm.taskCode"
            placeholder="请输入任务代码"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="任务名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入任务名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="logId" label="日志ID" width="180" />
        <el-table-column prop="taskCode" label="任务代码" width="180" />
        <el-table-column prop="name" label="任务名称" />
        <el-table-column prop="handlerName" label="处理器名称" />
        <el-table-column prop="projectName" label="项目名称" width="120" />
        <el-table-column prop="executeTime" label="执行时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.executeTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewDetail(row)">
              查看详情
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

    <!-- 批量删除对话框 -->
    <el-dialog
      v-model="batchDeleteVisible"
      title="批量删除任务日志"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="batchDeleteForm" label-width="100px">
        <el-form-item label="删除范围">
          <el-select v-model="batchDeleteForm.scope" style="width: 100%">
            <el-option value="all" label="所有日志">
              <el-tooltip
                content="删除所有已授权处理器的日志"
                placement="right"
                effect="light"
              >
                <span>所有日志</span>
              </el-tooltip>
            </el-option>
            <el-option value="project" label="当前项目的日志">
              <el-tooltip
                content="删除当前项目下所有处理器的日志"
                placement="right"
                effect="light"
              >
                <span>当前项目的日志</span>
              </el-tooltip>
            </el-option>
            <el-option value="handler" label="当前处理器的日志" :disabled="!searchForm.handlerId">
              <el-tooltip
                content="仅删除当前选择处理器的日志"
                placement="right"
                effect="light"
              >
                <span>当前处理器的日志</span>
              </el-tooltip>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="删除方式">
          <el-select v-model="batchDeleteForm.deleteType" style="width: 100%">
            <el-option-group label="按时间删除">
              <el-option value="1" label="1个月前的日志">
                <el-tooltip
                  content="删除1个月之前的任务日志"
                  placement="right"
                  effect="light"
                >
                  <span>1个月前的日志</span>
                </el-tooltip>
              </el-option>
              <el-option value="2" label="3个月前的日志">
                <el-tooltip
                  content="删除3个月之前的任务日志"
                  placement="right"
                  effect="light"
                >
                  <span>3个月前的日志</span>
                </el-tooltip>
              </el-option>
              <el-option value="3" label="6个月前的日志">
                <el-tooltip
                  content="删除6个月之前的任务日志"
                  placement="right"
                  effect="light"
                >
                  <span>6个月前的日志</span>
                </el-tooltip>
              </el-option>
              <el-option value="4" label="1年前的日志">
                <el-tooltip
                  content="删除1年之前的任务日志"
                  placement="right"
                  effect="light"
                >
                  <span>1年前的日志</span>
                </el-tooltip>
              </el-option>
            </el-option-group>
            <el-option-group label="按数量保留">
              <el-option value="5" label="仅保留最新1000条">
                <el-tooltip
                  content="删除最新1000条之前的日志"
                  placement="right"
                  effect="light"
                >
                  <span>仅保留最新1000条</span>
                </el-tooltip>
              </el-option>
              <el-option value="6" label="仅保留最新1万条">
                <el-tooltip
                  content="删除最新1万条之前的日志"
                  placement="right"
                  effect="light"
                >
                  <span>仅保留最新1万条</span>
                </el-tooltip>
              </el-option>
              <el-option value="7" label="仅保留最新3万条">
                <el-tooltip
                  content="删除最新3万条之前的日志"
                  placement="right"
                  effect="light"
                >
                  <span>仅保留最新3万条</span>
                </el-tooltip>
              </el-option>
              <el-option value="8" label="仅保留最新10万条">
                <el-tooltip
                  content="删除最新10万条之前的日志"
                  placement="right"
                  effect="light"
                >
                  <span>仅保留最新10万条</span>
                </el-tooltip>
              </el-option>
            </el-option-group>
            <el-option-group label="全部删除">
              <el-option value="100" label="删除所有日志">
                <el-tooltip
                  content="删除所有任务日志（危险操作）"
                  placement="right"
                  effect="light"
                >
                  <span style="color: #f56c6c">删除所有日志</span>
                </el-tooltip>
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-alert
          title="操作提示"
          type="warning"
          :closable="false"
          style="margin-top: 20px"
        >
          <div>
            <strong>删除范围：</strong>
            <span v-if="batchDeleteForm.scope === 'all'">所有已授权处理器的日志</span>
            <span v-else-if="batchDeleteForm.scope === 'project'">当前项目下所有处理器的日志</span>
            <span v-else-if="batchDeleteForm.scope === 'handler'">当前选择的处理器的日志</span>
          </div>
          <div style="margin-top: 8px">
            <strong>删除方式：</strong>{{ getDeleteTypeText(batchDeleteForm.deleteType) }}
          </div>
        </el-alert>
      </el-form>
      <template #footer>
        <el-button @click="batchDeleteVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmBatchDelete">确定删除</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="任务日志详情" width="800px">
      <div v-if="currentLog">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="日志ID">
            {{ currentLog.logId }}
          </el-descriptions-item>
          <el-descriptions-item label="任务代码">
            {{ currentLog.taskCode }}
          </el-descriptions-item>
          <el-descriptions-item label="任务名称">
            {{ currentLog.name }}
          </el-descriptions-item>
          <el-descriptions-item label="处理器名称">
            {{ currentLog.handlerName }}
          </el-descriptions-item>
          <el-descriptions-item label="项目名称">
            {{ currentLog.projectName }}
          </el-descriptions-item>
          <el-descriptions-item label="执行时间">
            {{ formatTime(currentLog.executeTime) }}
          </el-descriptions-item>
        </el-descriptions>

        <el-divider />

        <h4>请求信息</h4>
        <el-input
          :model-value="formatJson(currentLog.requestInfo)"
          type="textarea"
          :rows="8"
          readonly
        />

        <h4 style="margin-top: 20px">响应信息</h4>
        <el-input
          :model-value="formatJson(currentLog.responseInfo)"
          type="textarea"
          :rows="8"
          readonly
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryTaskLogs, deleteTaskLog } from '@/api/mockTask'
import { queryOwnHandlers } from '@/api/mockHandler'
import { useProjectStore } from '@/stores/project'

const projectStore = useProjectStore()

const loading = ref(false)
const tableData = ref([])
const handlerList = ref([])
const detailVisible = ref(false)
const currentLog = ref(null)
const batchDeleteVisible = ref(false)

const searchForm = reactive({
  projectId: null,
  handlerId: null,
  taskCode: '',
  name: ''
})

const batchDeleteForm = reactive({
  scope: 'project', // all: 所有日志, project: 当前项目日志, handler: 当前处理器日志
  deleteType: '2' // 默认删除3个月前的日志
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const projectList = computed(() => projectStore.projectList)

// 监听项目切换
watch(
  () => projectStore.currentProjectId,
  (newProjectId) => {
    if (newProjectId) {
      searchForm.projectId = newProjectId
      pagination.pageNum = 1
      loadData()
    }
  }
)

onMounted(() => {
  searchForm.projectId = projectStore.currentProjectId
  loadData()
  loadHandlers()
})

watch(
  () => searchForm.projectId,
  () => {
    searchForm.handlerId = null
    loadHandlers()
  }
)

async function loadData() {
  try {
    loading.value = true
    const data = await queryTaskLogs({
      projectId: searchForm.projectId,
      handlerId: searchForm.handlerId,
      taskCode: searchForm.taskCode,
      name: searchForm.name,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('Load task logs error:', error)
  } finally {
    loading.value = false
  }
}

async function loadHandlers() {
  if (!searchForm.projectId) {
    handlerList.value = []
    return
  }

  try {
    const data = await queryOwnHandlers({
      projectId: searchForm.projectId
    })
    const handlers = data.projectHandlerMap?.[searchForm.projectId] || []
    handlerList.value = handlers
  } catch (error) {
    console.error('Load handlers error:', error)
  }
}

function handleSearch() {
  pagination.pageNum = 1
  loadData()
}

function handleReset() {
  searchForm.projectId = projectStore.currentProjectId
  searchForm.handlerId = null
  searchForm.taskCode = ''
  searchForm.name = ''
  handleSearch()
}

function handleViewDetail(row) {
  currentLog.value = row
  detailVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm('确定要删除该日志吗？', '警告', {
      type: 'warning'
    })

    await deleteTaskLog({
      projectId: searchForm.projectId,
      handlerId: searchForm.handlerId,
      logId: row.logId,
      deleteType: '100' // 传入有效的deleteType，但由于指定了logId，实际只会删除这一条
    })

    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    // 取消操作
  }
}

async function handleBatchDelete() {
  // 重置表单为默认值
  batchDeleteForm.scope = searchForm.handlerId ? 'handler' : 'project'
  batchDeleteForm.deleteType = '2' // 默认3个月前
  // 显示对话框
  batchDeleteVisible.value = true
}

async function confirmBatchDelete() {
  try {
    // 根据选择的范围构建参数
    let projectId = null
    let handlerId = null
    let scopeText = ''

    switch (batchDeleteForm.scope) {
      case 'all':
        projectId = null
        handlerId = null
        scopeText = '所有已授权处理器'
        break
      case 'project':
        projectId = searchForm.projectId
        handlerId = null
        scopeText = '当前项目'
        break
      case 'handler':
        projectId = searchForm.projectId
        handlerId = searchForm.handlerId
        scopeText = '当前处理器'
        break
    }

    const deleteTypeText = getDeleteTypeText(batchDeleteForm.deleteType)
    const confirmMessage = `确定要删除${scopeText}的日志吗？\n删除方式：${deleteTypeText}\n\n此操作不可恢复，请谨慎操作！`

    await ElMessageBox.confirm(
      confirmMessage,
      '确认批量删除',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        dangerouslyUseHTMLString: false
      }
    )

    await deleteTaskLog({
      projectId: projectId,
      handlerId: handlerId,
      logId: null,
      deleteType: batchDeleteForm.deleteType
    })

    ElMessage.success('批量删除成功')
    batchDeleteVisible.value = false
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Batch delete error:', error)
    }
  }
}

function getDeleteTypeText(deleteType) {
  const deleteTypeMap = {
    '1': '删除1个月之前的日志',
    '2': '删除3个月之前的日志',
    '3': '删除6个月之前的日志',
    '4': '删除1年之前的日志',
    '5': '保留最新1000条，删除其余日志',
    '6': '保留最新1万条，删除其余日志',
    '7': '保留最新3万条，删除其余日志',
    '8': '保留最新10万条，删除其余日志',
    '100': '删除所有日志（危险操作）'
  }
  return deleteTypeMap[deleteType] || '未知删除方式'
}

function formatTime(timestamp) {
  if (!timestamp) return '-'
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

function formatJson(data) {
  if (!data) return ''
  try {
    if (typeof data === 'string') {
      return JSON.stringify(JSON.parse(data), null, 2)
    }
    return JSON.stringify(data, null, 2)
  } catch (error) {
    return data
  }
}
</script>

<style scoped>
.task-log-container {
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
