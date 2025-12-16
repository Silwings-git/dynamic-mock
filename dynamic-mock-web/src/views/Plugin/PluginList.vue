<template>
  <div class="plugin-list-container">
    <el-card>
      <template #header>
        <span>插件管理</span>
      </template>

      <!-- 插件列表 -->
      <el-row :gutter="20" v-loading="loading">
        <el-col
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
          v-for="plugin in pluginList"
          :key="plugin.pluginName"
          style="margin-bottom: 20px"
        >
          <el-card class="plugin-card" shadow="hover">
            <div class="plugin-header">
              <el-icon :size="40" color="#409eff">
                <Grid />
              </el-icon>
            </div>
            <h3 class="plugin-name">{{ plugin.pluginName }}</h3>
            <div class="plugin-info">
              <p>
                <span class="label">版本：</span>
                <el-tag size="small" type="success">{{ plugin.version }}</el-tag>
              </p>
              <p v-if="plugin.description" class="description">
                {{ plugin.description }}
              </p>
            </div>
            <el-button
              type="primary"
              link
              @click="showPluginDetail(plugin)"
            >
              查看详情
            </el-button>
          </el-card>
        </el-col>
      </el-row>

      <el-empty
        v-if="!loading && pluginList.length === 0"
        description="暂无插件数据"
      />
    </el-card>

    <!-- 插件详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      :title="currentPlugin?.pluginName"
      width="800px"
    >
      <div v-if="currentPlugin">
        <!-- 基础信息 -->
        <el-descriptions :column="2" border>
          <el-descriptions-item label="插件编码">
            {{ currentPlugin.pluginCode }}
          </el-descriptions-item>
          <el-descriptions-item label="插件名称">
            {{ currentPlugin.pluginName }}
          </el-descriptions-item>
          <el-descriptions-item label="版本">
            <el-tag size="small" type="success">{{ currentPlugin.version }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">
            {{ currentPlugin.description || '-' }}
          </el-descriptions-item>
        </el-descriptions>

        <!-- 插件参数规则 -->
        <el-divider content-position="left">插件参数规则</el-divider>
        <div v-if="hasPluginParamRules">
          <el-table :data="currentPlugin.metadata.pluginParamRuleList" border>
            <el-table-column prop="fieldName" label="字段名" width="200" />
            <el-table-column prop="fieldType" label="字段类型" width="120">
              <template #default="{ row }">
                <el-tag size="small" type="info">{{ row.fieldType }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="required" label="是否必须" width="100">
              <template #default="{ row }">
                <el-tag :type="row.required ? 'danger' : 'info'" size="small">
                  {{ row.required ? '必须' : '可选' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="描述" />
          </el-table>
        </div>
        <div v-else style="text-align: center; color: #909399; padding: 20px">
          暂无参数规则
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { queryPlugins } from '@/api/plugin'

const loading = ref(false)
const pluginList = ref([])
const detailVisible = ref(false)
const currentPlugin = ref(null)

const hasPluginParamRules = computed(() => {
  return currentPlugin.value?.metadata?.pluginParamRuleList &&
    currentPlugin.value.metadata.pluginParamRuleList.length > 0
})

onMounted(() => {
  loadData()
})

async function loadData() {
  try {
    loading.value = true
    const data = await queryPlugins()
    pluginList.value = data || []
  } catch (error) {
    console.error('Load plugins error:', error)
  } finally {
    loading.value = false
  }
}

function showPluginDetail(plugin) {
  currentPlugin.value = plugin
  detailVisible.value = true
}
</script>

<style scoped>
.plugin-list-container {
  height: 100%;
}

.plugin-card {
  height: 100%;
  text-align: center;
  transition: all 0.3s;
}

.plugin-card:hover {
  transform: translateY(-4px);
}

.plugin-header {
  margin-bottom: 15px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.plugin-name {
  margin: 10px 0;
  font-size: 18px;
  color: #303133;
  font-weight: bold;
}

.plugin-info {
  margin-top: 15px;
  text-align: left;
}

.plugin-info p {
  margin: 8px 0;
  font-size: 14px;
  color: #606266;
}

.plugin-info .label {
  font-weight: bold;
  color: #909399;
}

.plugin-info .description {
  margin-top: 10px;
  color: #909399;
  font-size: 13px;
  line-height: 1.6;
  word-break: break-word;
}
</style>
