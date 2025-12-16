<template>
  <div class="function-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>函数文档</span>
          <el-input
            v-model="searchKeyword"
            placeholder="搜索函数名称"
            style="width: 300px"
            clearable
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </template>

      <!-- 筛选区域 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="返回类型">
          <el-select
            v-model="searchForm.functionReturnType"
            placeholder="请选择返回类型"
            clearable
            style="width: 200px"
            @change="loadData"
          >
            <el-option label="全部" value="" />
            <el-option label="String" value="STRING" />
            <el-option label="Boolean" value="BOOLEAN" />
            <el-option label="Number" value="NUMBER" />
            <el-option label="Object" value="OBJECT" />
            <el-option label="List" value="LIST" />
          </el-select>
        </el-form-item>
      </el-form>

      <!-- 函数列表 -->
      <el-row :gutter="20" v-loading="loading">
        <el-col
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
          v-for="func in filteredFunctions"
          :key="func.functionName"
          style="margin-bottom: 20px"
        >
          <el-card
            class="function-card"
            shadow="hover"
            @click="showFunctionDetail(func)"
          >
            <div class="function-header">
              <h3>{{ func.functionName }}</h3>
              <el-tag v-if="func.functionReturnType" size="small">
                {{ func.functionReturnType }}
              </el-tag>
            </div>
            <div class="function-info">
              <p>
                <span class="label">参数数量：</span>
                <span>{{ func.minArgsNumber }} ~ {{ func.maxArgsNumber === 2147483647 ? '∞' : func.maxArgsNumber }}</span>
              </p>
              <div v-if="func.description" class="description">
                <el-icon><Document /></el-icon>
                <span>{{ truncateDescription(func.description) }}</span>
              </div>
            </div>
            <div class="view-more">
              <el-icon><ArrowRight /></el-icon>
              <span>点击查看详情</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty
        v-if="!loading && filteredFunctions.length === 0"
        description="暂无函数数据"
      />
    </el-card>

    <!-- 函数详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      :title="currentFunction?.functionName"
      width="700px"
    >
      <div v-if="currentFunction" class="function-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="函数名称">
            <el-tag type="primary">{{ currentFunction.functionName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="返回类型">
            <el-tag v-if="currentFunction.functionReturnType" size="small" type="success">
              {{ currentFunction.functionReturnType }}
            </el-tag>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="最小参数数量">
            {{ currentFunction.minArgsNumber }}
          </el-descriptions-item>
          <el-descriptions-item label="最大参数数量">
            {{ currentFunction.maxArgsNumber === 2147483647 ? '不限' : currentFunction.maxArgsNumber }}
          </el-descriptions-item>
        </el-descriptions>

        <el-divider />

        <div v-if="currentFunction.description" class="detail-section">
          <div class="section-header">
            <el-icon><Document /></el-icon>
            <h4>函数说明</h4>
          </div>
          <div class="description-content">
            {{ currentFunction.description }}
          </div>
        </div>

        <div v-if="currentFunction.example" class="detail-section">
          <div class="section-header">
            <el-icon><Edit /></el-icon>
            <h4>使用示例</h4>
            <el-button
              type="primary"
              size="small"
              :icon="CopyDocument"
              @click="copyExample"
            >
              复制示例
            </el-button>
          </div>
          <div class="example-content">
            <pre>{{ currentFunction.example }}</pre>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { CopyDocument, Document, Edit, ArrowRight } from '@element-plus/icons-vue'
import { queryFunctions } from '@/api/function'

const loading = ref(false)
const functionList = ref([])
const searchKeyword = ref('')
const detailVisible = ref(false)
const currentFunction = ref(null)

const searchForm = reactive({
  functionReturnType: ''
})

const filteredFunctions = computed(() => {
  let list = functionList.value

  // 按关键词搜索
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    list = list.filter(
      (func) =>
        func.functionName.toLowerCase().includes(keyword) ||
        (func.description && func.description.toLowerCase().includes(keyword))
    )
  }

  return list
})

onMounted(() => {
  loadData()
})

async function loadData() {
  try {
    loading.value = true
    const data = await queryFunctions({
      functionReturnType: searchForm.functionReturnType || null
    })
    functionList.value = data.list || []
  } catch (error) {
    console.error('Load functions error:', error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  // 搜索已在计算属性中处理
}

function showFunctionDetail(func) {
  currentFunction.value = func
  detailVisible.value = true
}

function truncateDescription(description) {
  if (!description) return ''
  const firstLine = description.split('\n')[0]
  return firstLine.length > 80 ? firstLine.substring(0, 80) + '...' : firstLine
}

async function copyExample() {
  if (!currentFunction.value?.example) return

  try {
    await navigator.clipboard.writeText(currentFunction.value.example)
    ElMessage.success('示例代码已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败，请手动复制')
  }
}
</script>

<style scoped>
.function-list-container {
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

.function-card {
  height: 100%;
  cursor: pointer;
  transition: all 0.3s;
}

.function-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(64, 158, 255, 0.2);
}

.function-card:hover .view-more {
  color: #409eff;
}

.function-card:hover .view-more .el-icon {
  transform: translateX(4px);
}

.function-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.function-header h3 {
  margin: 0;
  font-size: 18px;
  color: #409eff;
}

.function-info {
  margin-bottom: 10px;
}

.function-info p {
  margin: 8px 0;
  font-size: 14px;
  color: #606266;
}

.function-info .label {
  font-weight: bold;
  color: #909399;
}

.function-info .description {
  margin-top: 10px;
  padding: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
  color: #606266;
  font-size: 13px;
  line-height: 1.6;
  display: flex;
  align-items: flex-start;
  gap: 6px;
}

.function-info .description .el-icon {
  margin-top: 2px;
  color: #909399;
  flex-shrink: 0;
}

.function-info .description span {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.view-more {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  font-size: 13px;
  transition: all 0.3s;
}

.view-more .el-icon {
  transition: transform 0.3s;
}

.function-detail {
  padding: 10px 0;
}

.detail-section {
  margin-top: 20px;
}

.section-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.section-header .el-icon {
  color: #409eff;
  font-size: 18px;
}

.section-header h4 {
  margin: 0;
  flex: 1;
  font-size: 16px;
  color: #303133;
}

.description-content {
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
  word-break: break-word;
}

.example-content {
  background-color: #282c34;
  border-radius: 4px;
  padding: 15px;
  overflow-x: auto;
}

.example-content pre {
  margin: 0;
  color: #abb2bf;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-all;
}
</style>
