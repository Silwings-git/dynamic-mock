<template>
  <div class="snapshot-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>快照管理</span>
          <el-button @click="goBack">
            <el-icon><Back /></el-icon>
            返回
          </el-button>
        </div>
      </template>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="snapshotVersion" label="快照版本" width="200" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row)">
              查看
            </el-button>
            <el-button type="success" link @click="handleRecover(row)">
              恢复
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

    <!-- 快照详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="快照详情"
      width="90%"
      top="5vh"
      :close-on-click-modal="false"
    >
      <div v-if="currentSnapshot && handlerData" style="max-height: 75vh; overflow-y: auto; padding: 0 10px">
        <!-- 视图模式切换 -->
        <el-radio-group v-model="viewMode" style="margin-bottom: 20px">
          <el-radio-button value="graphic">图形模式</el-radio-button>
          <el-radio-button value="json">JSON模式</el-radio-button>
        </el-radio-group>

        <!-- 图形模式 -->
        <div v-show="viewMode === 'graphic'">
          <!-- 快照信息 -->
          <el-alert
            :title="`快照版本：${currentSnapshot.snapshotVersion}`"
            type="info"
            :closable="false"
            style="margin-bottom: 20px"
          >
            <div>
              <strong>创建时间：</strong>{{ formatTime(currentSnapshot.createTime) }}
            </div>
            <div v-if="currentSnapshot.remark">
              <strong>备注：</strong>{{ currentSnapshot.remark }}
            </div>
          </el-alert>

          <!-- 基础信息 -->
          <el-divider content-position="left">基础信息</el-divider>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="处理器名称">
              {{ handlerData.name }}
            </el-descriptions-item>
            <el-descriptions-item label="项目名称">
              {{ handlerData.projectName }}
            </el-descriptions-item>
            <el-descriptions-item label="HTTP方法">
              <el-tag
                v-for="method in handlerData.httpMethods"
                :key="method"
                size="small"
                style="margin-right: 5px"
              >
                {{ method }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="请求URI">
              {{ handlerData.requestUri }}
            </el-descriptions-item>
            <el-descriptions-item label="标签">
              {{ handlerData.label || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="延迟时间">
              {{ handlerData.delayTime || 0 }} ms
            </el-descriptions-item>
            <el-descriptions-item label="启用状态">
              <el-tag :type="handlerData.enableStatus === 1 ? 'success' : 'info'">
                {{ handlerData.enableStatus === 1 ? '已启用' : '已停用' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>

          <!-- Handler级别校验配置 -->
          <el-divider content-position="left">Handler校验配置</el-divider>
          <div v-if="hasHandlerCheckInfo" style="border: 1px solid #dcdfe6; border-radius: 4px; padding: 15px; background-color: #fafafa">
            <div
              style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; cursor: pointer"
              @click="handlerCheckExpanded = !handlerCheckExpanded"
            >
              <span style="font-weight: 500; color: #606266">
                <span style="margin-right: 5px">
                  {{ handlerCheckExpanded ? '▼' : '▶' }}
                </span>
                Handler级别校验配置
                <el-tag size="small" type="info" style="margin-left: 10px">
                  {{ getCheckInfoSummary(handlerData.checkInfo) }}
                </el-tag>
              </span>
            </div>

            <el-collapse-transition>
              <div v-show="handlerCheckExpanded">
                <!-- 错误响应列表 -->
                <div v-if="handlerData.checkInfo?.errResList?.length > 0" style="margin-bottom: 20px">
                  <div style="font-weight: 500; color: #303133; margin-bottom: 10px">错误响应列表</div>
                  <div
                    v-for="(errRes, errIdx) in handlerData.checkInfo.errResList"
                    :key="errIdx"
                    style="margin-bottom: 10px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #fff"
                  >
                    <div style="display: flex; align-items: center; margin-bottom: 5px">
                      <el-tag type="danger" size="small" style="margin-right: 8px">
                        {{ errRes.errResCode }}
                      </el-tag>
                      <el-tag type="info" size="small">
                        HTTP {{ errRes.status }}
                      </el-tag>
                    </div>
                    <div v-if="hasHeaders(errRes.headers)" style="margin-bottom: 5px">
                      <span style="font-size: 12px; color: #606266">响应头：</span>
                      <el-tag
                        v-for="(value, key) in errRes.headers"
                        :key="key"
                        size="small"
                        style="margin-right: 5px"
                      >
                        {{ key }}: {{ formatHeaderValue(value) }}
                      </el-tag>
                    </div>
                  </div>
                </div>

                <!-- 校验项列表 -->
                <div v-if="handlerData.checkInfo?.checkItemList?.length > 0">
                  <div style="font-weight: 500; color: #303133; margin-bottom: 10px">校验项列表</div>
                  <div
                    v-for="(checkItem, checkIdx) in handlerData.checkInfo.checkItemList"
                    :key="checkIdx"
                    style="margin-bottom: 10px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #fff9e6"
                  >
                    <div style="margin-bottom: 5px">
                      <span style="font-weight: 500; color: #303133">校验项 #{{ checkIdx + 1 }}</span>
                      <el-tag v-if="checkItem.errResCode" type="warning" size="small" style="margin-left: 10px">
                        → {{ checkItem.errResCode }}
                      </el-tag>
                    </div>
                    <div style="font-size: 12px; color: #606266; margin-bottom: 3px">表达式：</div>
                    <pre style="margin: 0 0 5px 0; font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 11px; background-color: #f5f7fa; padding: 5px; border-radius: 3px">{{ checkItem.checkExpression }}</pre>
                  </div>
                </div>
              </div>
            </el-collapse-transition>
          </div>
          <div v-else style="text-align: center; color: #909399; padding: 20px; border: 1px solid #dcdfe6; border-radius: 4px">
            未配置Handler级别校验
          </div>

          <!-- 自定义参数空间 -->
          <el-divider content-position="left">自定义参数空间</el-divider>
          <div v-if="hasCustomizeSpace">
            <el-table :data="customizeSpaceList" border>
              <el-table-column prop="key" label="参数名" width="200" />
              <el-table-column prop="value" label="参数值">
                <template #default="{ row }">
                  <div style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px; white-space: pre-wrap">
                    {{ row.value }}
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else style="text-align: center; color: #909399; padding: 20px">
            暂无自定义参数
          </div>

          <!-- 响应集 -->
          <el-divider content-position="left">响应集 ({{ handlerData.responses?.length || 0 }})</el-divider>
          <div v-if="handlerData.responses && handlerData.responses.length > 0">
            <el-card
              v-for="(response, index) in handlerData.responses"
              :key="index"
              style="margin-bottom: 15px"
              shadow="hover"
            >
              <template #header>
                <div style="display: flex; justify-content: space-between; align-items: center">
                  <div>
                    <el-tag :type="response.enableStatus === 1 ? 'success' : 'info'" size="small">
                      {{ response.enableStatus === 1 ? '已启用' : '已停用' }}
                    </el-tag>
                    <span style="margin-left: 10px; font-weight: bold">
                      {{ response.name }}
                    </span>
                    <el-tag size="small" style="margin-left: 10px" type="info">
                      HTTP {{ response.response?.status || 200 }}
                    </el-tag>
                    <el-tag size="small" style="margin-left: 10px" type="warning">
                      延迟 {{ response.delayTime || 0 }}ms
                    </el-tag>
                  </div>
                  <el-button
                    :type="activeResponseIndex === index ? 'primary' : 'default'"
                    size="small"
                    @click="toggleResponse(index)"
                  >
                    {{ activeResponseIndex === index ? '收起' : '展开' }}
                  </el-button>
                </div>
              </template>

              <el-collapse-transition>
                <div v-show="activeResponseIndex === index">
                  <!-- 校验配置 -->
                  <div v-if="hasResponseCheckInfo(response)" style="margin-bottom: 20px">
                    <h4 style="color: #606266; margin-bottom: 10px">校验配置</h4>
                    <div style="border: 1px solid #dcdfe6; border-radius: 4px; padding: 10px; background-color: #fafafa">
                      <div style="font-weight: 500; color: #606266; margin-bottom: 10px">
                        {{ getCheckInfoSummary(response.checkInfo) }}
                      </div>
                      <!-- 简化显示校验项 -->
                      <div v-if="response.checkInfo?.checkItemList?.length > 0">
                        <div
                          v-for="(checkItem, checkIdx) in response.checkInfo.checkItemList"
                          :key="checkIdx"
                          style="margin-bottom: 8px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 8px; background-color: #fff9e6"
                        >
                          <div style="margin-bottom: 5px">
                            <span style="font-weight: 500; color: #303133">校验项 #{{ checkIdx + 1 }}</span>
                            <el-tag v-if="checkItem.errResCode" type="warning" size="small" style="margin-left: 8px">
                              → {{ checkItem.errResCode }}
                            </el-tag>
                          </div>
                          <pre style="margin: 0; font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 11px; background-color: #f5f7fa; padding: 5px; border-radius: 3px">{{ checkItem.checkExpression }}</pre>
                        </div>
                      </div>
                    </div>
                  </div>

                  <!-- 期望表达式 -->
                  <div v-if="response.support && response.support.length > 0" style="margin-bottom: 20px">
                    <h4 style="color: #606266; margin-bottom: 10px">期望表达式 ({{ response.support.length }})</h4>
                    <div
                      v-for="(expr, exprIdx) in response.support"
                      :key="exprIdx"
                      style="margin-bottom: 10px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #f5f7fa"
                    >
                      <div style="font-weight: 500; color: #606266; margin-bottom: 5px">表达式 #{{ exprIdx + 1 }}</div>
                      <pre style="margin: 0; font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px; white-space: pre-wrap">{{ expr }}</pre>
                    </div>
                  </div>

                  <!-- 响应头 -->
                  <div v-if="hasHeaders(response.response?.headers)" style="margin-bottom: 20px">
                    <h4 style="color: #606266; margin-bottom: 10px">响应头</h4>
                    <el-table :data="getHeadersList(response.response.headers)" border size="small">
                      <el-table-column prop="key" label="Header名" width="200" />
                      <el-table-column prop="value" label="Header值" />
                    </el-table>
                  </div>

                  <!-- 响应体 -->
                  <div>
                    <h4 style="color: #606266; margin-bottom: 10px">响应体</h4>
                    <el-input
                      :model-value="formatJson(response.response?.body)"
                      type="textarea"
                      :rows="10"
                      readonly
                      style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                    />
                  </div>
                </div>
              </el-collapse-transition>
            </el-card>
          </div>
          <div v-else style="text-align: center; color: #909399; padding: 20px">
            暂无响应配置
          </div>

          <!-- 任务集 -->
          <el-divider content-position="left">任务集 ({{ handlerData.tasks?.length || 0 }})</el-divider>
          <div v-if="handlerData.tasks && handlerData.tasks.length > 0">
            <el-card
              v-for="(task, index) in handlerData.tasks"
              :key="index"
              style="margin-bottom: 15px"
              shadow="hover"
            >
              <template #header>
                <div style="display: flex; justify-content: space-between; align-items: center">
                  <div>
                    <el-tag :type="task.enableStatus === 1 ? 'success' : 'info'" size="small">
                      {{ task.enableStatus === 1 ? '已启用' : '已停用' }}
                    </el-tag>
                    <span style="margin-left: 10px; font-weight: bold">
                      {{ task.name }}
                    </span>
                    <el-tag size="small" style="margin-left: 10px" :type="task.async ? 'warning' : 'info'">
                      {{ task.async ? '异步' : '同步' }}
                    </el-tag>
                  </div>
                  <el-button
                    :type="activeTaskIndex === index ? 'primary' : 'default'"
                    size="small"
                    @click="toggleTask(index)"
                  >
                    {{ activeTaskIndex === index ? '收起' : '展开' }}
                  </el-button>
                </div>
              </template>

              <el-collapse-transition>
                <div v-show="activeTaskIndex === index">
                  <el-descriptions :column="2" border size="small" style="margin-bottom: 20px">
                    <el-descriptions-item label="任务名称">
                      {{ task.name }}
                    </el-descriptions-item>
                    <el-descriptions-item label="执行方式">
                      <el-tag :type="task.async ? 'warning' : 'info'" size="small">
                        {{ task.async ? '异步' : '同步' }}
                      </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="Cron表达式">
                      {{ task.cron }}
                    </el-descriptions-item>
                    <el-descriptions-item label="执行次数">
                      {{ task.numberOfExecute }}
                    </el-descriptions-item>
                  </el-descriptions>

                  <!-- 期望表达式 -->
                  <div v-if="task.support && task.support.length > 0" style="margin-bottom: 20px">
                    <h4 style="color: #606266; margin-bottom: 10px">期望表达式 ({{ task.support.length }})</h4>
                    <div
                      v-for="(expr, exprIdx) in task.support"
                      :key="exprIdx"
                      style="margin-bottom: 10px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #f5f7fa"
                    >
                      <div style="font-weight: 500; color: #606266; margin-bottom: 5px">表达式 #{{ exprIdx + 1 }}</div>
                      <pre style="margin: 0; font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px; white-space: pre-wrap">{{ expr }}</pre>
                    </div>
                  </div>

                  <!-- 请求信息 -->
                  <h4 style="color: #606266; margin-bottom: 10px">请求信息</h4>
                  <el-descriptions :column="2" border size="small" style="margin-bottom: 15px">
                    <el-descriptions-item label="请求URL" :span="2">
                      {{ task.request?.requestUrl || '-' }}
                    </el-descriptions-item>
                    <el-descriptions-item label="HTTP方法">
                      <el-tag size="small">{{ task.request?.httpMethod || 'POST' }}</el-tag>
                    </el-descriptions-item>
                  </el-descriptions>

                  <!-- 请求头 -->
                  <div v-if="hasHeaders(task.request?.headers)" style="margin-bottom: 15px">
                    <h4 style="color: #606266; margin-bottom: 10px">请求头</h4>
                    <el-table :data="getHeadersList(task.request.headers)" border size="small">
                      <el-table-column prop="key" label="Header名" width="200" />
                      <el-table-column prop="value" label="Header值" />
                    </el-table>
                  </div>

                  <!-- 请求体 -->
                  <div v-if="task.request?.body">
                    <h4 style="color: #606266; margin-bottom: 10px">请求体</h4>
                    <el-input
                      :model-value="formatJson(task.request.body)"
                      type="textarea"
                      :rows="8"
                      readonly
                      style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                    />
                  </div>
                </div>
              </el-collapse-transition>
            </el-card>
          </div>
          <div v-else style="text-align: center; color: #909399; padding: 20px">
            暂无任务配置
          </div>

          <!-- 插件信息集 -->
          <el-divider content-position="left">插件信息集 ({{ handlerData.pluginInfos?.length || 0 }})</el-divider>
          <div v-if="handlerData.pluginInfos && handlerData.pluginInfos.length > 0">
            <el-card
              v-for="(plugin, index) in handlerData.pluginInfos"
              :key="index"
              style="margin-bottom: 15px"
              shadow="hover"
            >
              <template #header>
                <div style="display: flex; justify-content: space-between; align-items: center">
                  <div>
                    <el-tag :type="plugin.enableStatus === 1 ? 'success' : 'info'" size="small">
                      {{ plugin.enableStatus === 1 ? '已启用' : '已停用' }}
                    </el-tag>
                    <span style="margin-left: 10px; font-weight: bold">
                      {{ plugin.pluginCode }}
                    </span>
                  </div>
                  <el-button
                    :type="activePluginIndex === index ? 'primary' : 'default'"
                    size="small"
                    @click="togglePlugin(index)"
                  >
                    {{ activePluginIndex === index ? '收起' : '展开' }}
                  </el-button>
                </div>
              </template>

              <el-collapse-transition>
                <div v-show="activePluginIndex === index">
                  <h4 style="color: #606266; margin-bottom: 10px">插件参数</h4>
                  <el-input
                    :model-value="formatJson(plugin.pluginParam)"
                    type="textarea"
                    :rows="8"
                    readonly
                    style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                  />
                </div>
              </el-collapse-transition>
            </el-card>
          </div>
          <div v-else style="text-align: center; color: #909399; padding: 20px">
            暂无插件配置
          </div>
        </div>

        <!-- JSON模式 -->
        <div v-show="viewMode === 'json'">
          <el-input
            :model-value="formatJson(handlerData)"
            type="textarea"
            :rows="25"
            readonly
            style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
          />
        </div>
      </div>
      <div v-else style="text-align: center; color: #909399; padding: 40px">
        暂无数据
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  querySnapshots,
  findSnapshot,
  recoverSnapshot
} from '@/api/mockHandler'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const currentSnapshot = ref(null)
const handlerId = ref(null)
const viewMode = ref('graphic')
const activeResponseIndex = ref(-1)
const activeTaskIndex = ref(-1)
const activePluginIndex = ref(-1)
const handlerCheckExpanded = ref(false)

const pagination = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
})

const handlerData = computed(() => {
  return currentSnapshot.value?.mockHandlerInfo || null
})

const hasHandlerCheckInfo = computed(() => {
  return handlerData.value?.checkInfo &&
    (handlerData.value.checkInfo.errResList?.length > 0 ||
     handlerData.value.checkInfo.checkItemList?.length > 0)
})

const hasCustomizeSpace = computed(() => {
  return handlerData.value?.customizeSpace &&
    Object.keys(handlerData.value.customizeSpace).length > 0
})

const customizeSpaceList = computed(() => {
  if (!hasCustomizeSpace.value) return []
  return Object.entries(handlerData.value.customizeSpace).map(([key, value]) => ({
    key,
    value: typeof value === 'object' ? JSON.stringify(value, null, 2) : String(value)
  }))
})

onMounted(() => {
  handlerId.value = route.params.handlerId
  loadData()
})

async function loadData() {
  try {
    loading.value = true
    const data = await querySnapshots({
      handlerId: handlerId.value,
      pageNo: pagination.pageNo,
      pageSize: pagination.pageSize
    })
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('Load snapshots error:', error)
  } finally {
    loading.value = false
  }
}

async function handleView(row) {
  try {
    const data = await findSnapshot({
      snapshotVersion: row.snapshotVersion
    })
    currentSnapshot.value = data
    detailVisible.value = true

    // 重置展开状态
    activeResponseIndex.value = -1
    activeTaskIndex.value = -1
    activePluginIndex.value = -1
    handlerCheckExpanded.value = false
  } catch (error) {
    console.error('Load snapshot detail error:', error)
    ElMessage.error('加载快照详情失败')
  }
}

async function handleRecover(row) {
  try {
    await ElMessageBox.confirm(
      '确定要恢复到该快照版本吗？恢复后当前配置将被覆盖。',
      '警告',
      {
        type: 'warning'
      }
    )

    await recoverSnapshot({
      snapshotVersion: row.snapshotVersion
    })

    ElMessage.success('快照恢复成功')
    router.push(`/mock-handler/detail/${handlerId.value}`)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Recover snapshot error:', error)
      ElMessage.error('快照恢复失败')
    }
  }
}

function goBack() {
  router.back()
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
    return JSON.stringify(data, null, 2)
  } catch (error) {
    return data
  }
}

function getCheckInfoSummary(checkInfo) {
  if (!checkInfo) return '未配置'
  const errCount = (checkInfo.errResList || []).length
  const checkCount = (checkInfo.checkItemList || []).length
  if (errCount === 0 && checkCount === 0) return '未配置'
  return `${errCount} 个错误响应, ${checkCount} 个校验项`
}

function hasResponseCheckInfo(response) {
  return response.checkInfo &&
    (response.checkInfo.errResList?.length > 0 ||
     response.checkInfo.checkItemList?.length > 0)
}

function hasHeaders(headers) {
  return headers && Object.keys(headers).length > 0
}

function formatHeaderValue(value) {
  return Array.isArray(value) ? value.join(', ') : value
}

function getHeadersList(headers) {
  if (!headers) return []
  return Object.entries(headers).map(([key, value]) => ({
    key,
    value: formatHeaderValue(value)
  }))
}

function toggleResponse(index) {
  activeResponseIndex.value = activeResponseIndex.value === index ? -1 : index
}

function toggleTask(index) {
  activeTaskIndex.value = activeTaskIndex.value === index ? -1 : index
}

function togglePlugin(index) {
  activePluginIndex.value = activePluginIndex.value === index ? -1 : index
}
</script>

<style scoped>
.snapshot-list-container {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

h4 {
  margin: 15px 0 10px 0;
  color: #606266;
  font-size: 14px;
}

:deep(.el-textarea__inner) {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', monospace;
  font-size: 12px;
  line-height: 1.6;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}
</style>
