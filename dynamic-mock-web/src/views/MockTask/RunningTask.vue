<template>
  <div class="running-task-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>运行中任务</span>
          <div>
            <el-button
              type="warning"
              :disabled="!tableData.length"
              @click="handleBatchCancel"
            >
              <el-icon><Close /></el-icon>
              批量取消
            </el-button>
            <el-button @click="loadData">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </template>

      <!-- 筛选区域 -->
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
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column type="expand">
          <template #default="{ row }">
            <div style="padding: 20px; background-color: #fafafa">
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="任务代码">
                  {{ row.taskCode }}
                </el-descriptions-item>
                <el-descriptions-item label="任务名称">
                  {{ row.taskName }}
                </el-descriptions-item>
                <el-descriptions-item label="处理器">
                  {{ getHandlerName(row.handlerId) || row.handlerId }}
                </el-descriptions-item>
                <el-descriptions-item label="剩余执行次数">
                  {{ row.remainingTimes }}
                </el-descriptions-item>
                <el-descriptions-item label="注册时间" :span="2">
                  {{ formatTime(row.registrationTime) }}
                </el-descriptions-item>
              </el-descriptions>

              <!-- 任务详细信息 -->
              <div v-if="row.taskInfo" style="margin-top: 20px">
                <el-divider content-position="left">任务详细配置</el-divider>
                <el-descriptions :column="2" border size="small">
                  <el-descriptions-item label="处理器ID">
                    {{ row.taskInfo.handlerId || '-' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="任务名称">
                    {{ row.taskInfo.name || '-' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="执行方式" v-if="row.taskInfo.async !== undefined">
                    <el-tag :type="row.taskInfo.async ? 'warning' : 'info'" size="small">
                      {{ row.taskInfo.async ? '异步' : '同步' }}
                    </el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="Cron表达式">
                    {{ row.taskInfo.cron || '-' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="执行次数">
                    {{ row.taskInfo.numberOfExecute || '-' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="启用状态" v-if="row.taskInfo.enableStatus !== undefined">
                    <el-tag :type="row.taskInfo.enableStatus === 1 ? 'success' : 'info'" size="small">
                      {{ row.taskInfo.enableStatus === 1 ? '已启用' : '已停用' }}
                    </el-tag>
                  </el-descriptions-item>
                </el-descriptions>

                <!-- 注册信息 -->
                <div v-if="row.taskInfo.registrationInfo" style="margin-top: 15px">
                  <h4 style="color: #606266; margin-bottom: 10px">注册信息</h4>
                  <el-descriptions :column="2" border size="small">
                    <el-descriptions-item label="任务编码">
                      {{ row.taskInfo.registrationInfo.taskCode || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="注册时间戳">
                      {{ row.taskInfo.registrationInfo.registrationTime || '-' }}
                    </el-descriptions-item>
                  </el-descriptions>
                </div>

                <!-- 期望表达式 -->
                <div v-if="row.taskInfo.support && row.taskInfo.support.length > 0" style="margin-top: 15px">
                  <h4 style="color: #606266; margin-bottom: 10px">期望表达式 ({{ row.taskInfo.support.length }})</h4>
                  <div
                    v-for="(expr, index) in row.taskInfo.support"
                    :key="index"
                    style="margin-bottom: 10px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #fff"
                  >
                    <div style="font-weight: 500; color: #606266; margin-bottom: 5px">表达式 #{{ index + 1 }}</div>
                    <pre style="margin: 0; font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px; white-space: pre-wrap">{{ expr }}</pre>
                  </div>
                </div>

                <!-- 请求信息 -->
                <div v-if="row.taskInfo.requestUrl || row.taskInfo.httpMethod" style="margin-top: 15px">
                  <h4 style="color: #606266; margin-bottom: 10px">请求信息</h4>
                  <el-descriptions :column="2" border size="small" style="margin-bottom: 10px">
                    <el-descriptions-item label="请求URL" :span="2">
                      {{ row.taskInfo.requestUrl || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="HTTP方法">
                      <el-tag size="small">{{ row.taskInfo.httpMethod || '-' }}</el-tag>
                    </el-descriptions-item>
                  </el-descriptions>

                  <!-- 请求头 -->
                  <div v-if="hasHeaders(row.taskInfo.headers)" style="margin-bottom: 10px">
                    <div style="font-size: 13px; font-weight: 500; color: #606266; margin-bottom: 8px">请求头</div>
                    <el-table :data="getHeadersList(row.taskInfo.headers)" border size="small">
                      <el-table-column prop="key" label="Header名" width="200" />
                      <el-table-column prop="value" label="Header值" />
                    </el-table>
                  </div>

                  <!-- 路径参数 -->
                  <div v-if="hasHeaders(row.taskInfo.uriVariables)" style="margin-bottom: 10px">
                    <div style="font-size: 13px; font-weight: 500; color: #606266; margin-bottom: 8px">路径参数</div>
                    <el-table :data="getHeadersList(row.taskInfo.uriVariables)" border size="small">
                      <el-table-column prop="key" label="参数名" width="200" />
                      <el-table-column prop="value" label="参数值" />
                    </el-table>
                  </div>

                  <!-- 请求体 -->
                  <div v-if="row.taskInfo.body && Object.keys(row.taskInfo.body).length > 0">
                    <div style="font-size: 13px; font-weight: 500; color: #606266; margin-bottom: 8px">请求体</div>
                    <el-input
                      :model-value="formatJson(row.taskInfo.body)"
                      type="textarea"
                      :rows="8"
                      readonly
                      style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                    />
                  </div>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="taskCode" label="任务代码" width="200" />
        <el-table-column prop="taskName" label="任务名称" width="200" />
        <el-table-column label="处理器" width="200">
          <template #default="{ row }">
            {{ getHandlerName(row.handlerId) || row.handlerId }}
          </template>
        </el-table-column>
        <el-table-column prop="remainingTimes" label="剩余执行次数" width="140" />
        <el-table-column prop="registrationTime" label="注册时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.registrationTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              type="danger"
              link
              @click.stop="handleCancel(row)"
            >
              取消任务
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-empty
        v-if="!loading && !tableData.length"
        description="暂无运行中的任务"
      />
    </el-card>

    <!-- 批量取消对话框 -->
    <el-dialog
      v-model="batchCancelVisible"
      title="批量取消任务"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="batchCancelForm" label-width="100px">
        <el-form-item label="取消范围">
          <el-select v-model="batchCancelForm.scope" style="width: 100%">
            <el-option value="all" label="所有任务">
              <el-tooltip
                content="取消所有已授权处理器的运行中任务"
                placement="right"
                effect="light"
              >
                <span>所有任务</span>
              </el-tooltip>
            </el-option>
            <el-option value="project" label="当前项目的所有任务">
              <el-tooltip
                content="取消当前项目下所有处理器的运行中任务"
                placement="right"
                effect="light"
              >
                <span>当前项目的所有任务</span>
              </el-tooltip>
            </el-option>
            <el-option value="handler" label="当前处理器的任务" :disabled="!searchForm.handlerId">
              <el-tooltip
                content="仅取消当前选择处理器的运行中任务"
                placement="right"
                effect="light"
              >
                <span>当前处理器的任务</span>
              </el-tooltip>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="执行方式">
          <el-select v-model="batchCancelForm.interrupt" style="width: 100%">
            <el-option :value="true" label="强制中断">
              <el-tooltip
                content="立即中断正在执行的任务"
                placement="right"
                effect="light"
              >
                <span>强制中断</span>
              </el-tooltip>
            </el-option>
            <el-option :value="false" label="等待完成">
              <el-tooltip
                content="等待当前正在执行的任务完成后取消后续任务"
                placement="right"
                effect="light"
              >
                <span>等待完成</span>
              </el-tooltip>
            </el-option>
          </el-select>
        </el-form-item>
        <el-alert
          title="操作提示"
          type="warning"
          :closable="false"
          style="margin-top: 20px"
        >
          <div v-if="batchCancelForm.scope === 'all'">
            将取消<strong>所有已授权处理器</strong>的运行中任务
          </div>
          <div v-else-if="batchCancelForm.scope === 'project'">
            将取消<strong>当前项目</strong>下所有处理器的运行中任务
          </div>
          <div v-else-if="batchCancelForm.scope === 'handler'">
            将取消<strong>当前选择的处理器</strong>的运行中任务
          </div>
          <div style="margin-top: 8px">
            <span v-if="batchCancelForm.interrupt">
              执行方式：<strong>强制中断</strong>（立即中断正在执行的任务）
            </span>
            <span v-else>
              执行方式：<strong>等待完成</strong>（等待正在执行的任务完成后取消后续任务）
            </span>
          </div>
        </el-alert>
      </el-form>
      <template #footer>
        <el-button @click="batchCancelVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmBatchCancel">确定取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  queryRunningTasks,
  unregisterTask,
  batchUnregisterTasks
} from '@/api/mockTask'
import { queryOwnHandlers } from '@/api/mockHandler'
import { useProjectStore } from '@/stores/project'

const projectStore = useProjectStore()

const loading = ref(false)
const tableData = ref([])
const handlerList = ref([])
const batchCancelVisible = ref(false)

const searchForm = reactive({
  handlerId: null
})

const batchCancelForm = reactive({
  scope: 'project', // all: 所有任务, project: 当前项目所有任务, handler: 当前处理器任务
  interrupt: true // true: 强制中断, false: 等待完成
})

// 监听项目切换
watch(
  () => projectStore.currentProjectId,
  (newProjectId) => {
    if (newProjectId) {
      searchForm.handlerId = null
      loadHandlers()
      loadData()
    }
  }
)

onMounted(() => {
  loadHandlers()
  loadData()
})

async function loadData() {
  try {
    loading.value = true
    const data = await queryRunningTasks({
      projectId: projectStore.currentProjectId,
      handlerId: searchForm.handlerId
    })
    tableData.value = data || []

    // 确保加载了处理器列表，以便显示处理器名称
    if (!handlerList.value || handlerList.value.length === 0) {
      await loadHandlers()
    }
  } catch (error) {
    console.error('Load running tasks error:', error)
  } finally {
    loading.value = false
  }
}

async function loadHandlers() {
  if (!projectStore.currentProjectId) {
    handlerList.value = []
    return
  }

  try {
    const data = await queryOwnHandlers({
      projectId: projectStore.currentProjectId
    })
    const handlers = data.projectHandlerMap?.[projectStore.currentProjectId] || []
    handlerList.value = handlers
  } catch (error) {
    console.error('Load handlers error:', error)
  }
}

function handleSearch() {
  loadData()
}

function handleReset() {
  searchForm.handlerId = null
  handleSearch()
}

function getHandlerName(handlerId) {
  if (!handlerId) return '-'
  const handler = handlerList.value.find(h => h.handlerId === handlerId)
  return handler ? handler.name : null
}

async function handleCancel(row) {
  try {
    await ElMessageBox.confirm(
      `确定要取消任务"${row.taskName}"吗？`,
      '警告',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )

    await unregisterTask({
      handlerId: row.handlerId,
      taskCode: row.taskCode,
      interrupt: true
    })

    ElMessage.success('任务已取消')
    loadData()
  } catch (error) {
    // 取消操作
  }
}

async function handleBatchCancel() {
  // 重置表单为默认值
  batchCancelForm.scope = searchForm.handlerId ? 'handler' : 'project'
  batchCancelForm.interrupt = true
  // 显示对话框
  batchCancelVisible.value = true
}

async function confirmBatchCancel() {
  try {
    // 根据选择的范围构建参数
    let projectId = null
    let handlerId = null
    let scopeText = ''

    switch (batchCancelForm.scope) {
      case 'all':
        projectId = null
        handlerId = null
        scopeText = '所有已授权处理器'
        break
      case 'project':
        projectId = projectStore.currentProjectId
        handlerId = null
        scopeText = '当前项目'
        break
      case 'handler':
        projectId = projectStore.currentProjectId
        handlerId = searchForm.handlerId
        const handlerName = getHandlerName(handlerId) || handlerId
        scopeText = `处理器【${handlerName}】`
        break
    }

    const interruptText = batchCancelForm.interrupt ? '强制中断' : '等待完成'
    const confirmMessage = `确定要取消${scopeText}的所有运行中任务吗？\n执行方式：${interruptText}`

    await ElMessageBox.confirm(
      confirmMessage,
      '确认批量取消',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )

    await batchUnregisterTasks({
      projectId: projectId,
      handlerId: handlerId,
      interrupt: batchCancelForm.interrupt
    })

    ElMessage.success('批量取消成功')
    batchCancelVisible.value = false
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Batch cancel error:', error)
    }
  }
}

function formatTime(timestamp) {
  if (!timestamp) return '-'
  // 如果是字符串或Date对象，尝试转换
  const time = typeof timestamp === 'string' || timestamp instanceof Date
    ? new Date(timestamp)
    : new Date(timestamp)

  // 检查是否是有效日期
  if (isNaN(time.getTime())) return '-'

  return time.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

function hasHeaders(headers) {
  return headers && Object.keys(headers).length > 0
}

function getHeadersList(headers) {
  if (!headers) return []
  return Object.entries(headers).map(([key, value]) => ({
    key,
    value: Array.isArray(value) ? value.join(', ') : value
  }))
}

function formatJson(data) {
  if (!data) return ''
  try {
    return JSON.stringify(data, null, 2)
  } catch (error) {
    return String(data)
  }
}
</script>

<style scoped>
.running-task-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header > div {
  display: flex;
  gap: 10px;
}

.search-form {
  margin-bottom: 20px;
}
</style>
