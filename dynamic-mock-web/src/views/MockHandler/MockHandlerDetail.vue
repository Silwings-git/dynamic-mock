<template>
  <div class="mock-handler-detail-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>Mock处理器详情</span>
          <div>
            <el-button type="primary" @click="handleEdit">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button @click="goBack">
              <el-icon><Back /></el-icon>
              返回
            </el-button>
          </div>
        </div>
      </template>

      <!-- 视图模式切换 -->
      <el-radio-group v-model="viewMode" style="margin-bottom: 20px">
        <el-radio-button value="graphic">表单模式</el-radio-button>
        <el-radio-button value="browse">浏览模式</el-radio-button>
        <el-radio-button value="json">JSON模式</el-radio-button>
      </el-radio-group>

      <!-- 表单模式 -->
      <div v-show="viewMode === 'graphic'" v-if="handlerData">
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
                  <div
                    style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; cursor: pointer"
                    @click="errRes._expanded = !errRes._expanded"
                  >
                    <div style="flex: 1; display: flex; align-items: center">
                      <span style="margin-right: 8px; color: #909399">
                        {{ errRes._expanded ? '▼' : '▶' }}
                      </span>
                      <el-tag type="danger" size="small" style="margin-right: 8px">
                        {{ errRes.errResCode }}
                      </el-tag>
                      <el-tag type="info" size="small">
                        HTTP {{ errRes.status }}
                      </el-tag>
                    </div>
                  </div>

                  <el-collapse-transition>
                    <div v-show="errRes._expanded">
                      <el-descriptions :column="1" border size="small">
                        <el-descriptions-item label="错误响应编码">
                          {{ errRes.errResCode }}
                        </el-descriptions-item>
                        <el-descriptions-item label="HTTP状态码">
                          {{ errRes.status }}
                        </el-descriptions-item>
                        <el-descriptions-item label="响应头">
                          <div v-if="hasHeaders(errRes.headers)">
                            <el-tag
                              v-for="(value, key) in errRes.headers"
                              :key="key"
                              size="small"
                              style="margin-right: 5px; margin-bottom: 5px"
                            >
                              {{ key }}: {{ formatHeaderValue(value) }}
                            </el-tag>
                          </div>
                          <span v-else style="color: #909399">无</span>
                        </el-descriptions-item>
                        <el-descriptions-item label="响应体">
                          <el-input
                            :model-value="formatJson(errRes.body)"
                            type="textarea"
                            :rows="4"
                            readonly
                            style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                          />
                        </el-descriptions-item>
                      </el-descriptions>
                    </div>
                  </el-collapse-transition>
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
                  <div
                    style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; cursor: pointer"
                    @click="checkItem._expanded = !checkItem._expanded"
                  >
                    <div style="flex: 1; display: flex; align-items: center">
                      <span style="margin-right: 8px; color: #909399">
                        {{ checkItem._expanded ? '▼' : '▶' }}
                      </span>
                      <span style="font-weight: 500; color: #303133">校验项 #{{ checkIdx + 1 }}</span>
                      <el-tag v-if="checkItem.errResCode" type="warning" size="small" style="margin-left: 10px">
                        → {{ checkItem.errResCode }}
                      </el-tag>
                    </div>
                  </div>

                  <el-collapse-transition>
                    <div v-show="checkItem._expanded">
                      <el-descriptions :column="1" border size="small">
                        <el-descriptions-item label="校验表达式">
                          <pre style="margin: 0; font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px">{{ checkItem.checkExpression }}</pre>
                        </el-descriptions-item>
                        <el-descriptions-item label="失败时返回的错误响应">
                          {{ checkItem.errResCode || '-' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="错误消息填充参数">
                          <el-tag
                            v-for="(param, paramIdx) in checkItem.errMsgFillParam"
                            :key="paramIdx"
                            size="small"
                            style="margin-right: 5px"
                          >
                            {{ param }}
                          </el-tag>
                          <span v-if="!checkItem.errMsgFillParam || checkItem.errMsgFillParam.length === 0" style="color: #909399">无</span>
                        </el-descriptions-item>
                      </el-descriptions>
                    </div>
                  </el-collapse-transition>
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
              <div
                style="flex: 1; display: flex; align-items: center; cursor: pointer"
                @click="toggleResponse(index)"
              >
                <span style="margin-right: 8px; color: #909399; font-size: 14px">
                  {{ activeResponseIndex === index ? '▼' : '▶' }}
                </span>
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
            </template>

            <el-collapse-transition>
              <div v-show="activeResponseIndex === index">
                <!-- 校验配置 -->
                <div v-if="hasResponseCheckInfo(response)" style="margin-bottom: 20px">
                  <h4 style="color: #606266; margin-bottom: 10px">校验配置</h4>
                  <div style="border: 1px solid #dcdfe6; border-radius: 4px; padding: 10px; background-color: #fafafa">
                    <div
                      style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; cursor: pointer"
                      @click="response._checkExpanded = !response._checkExpanded"
                    >
                      <span style="font-weight: 500; color: #606266">
                        <span style="margin-right: 5px">
                          {{ response._checkExpanded ? '▼' : '▶' }}
                        </span>
                        校验配置
                        <el-tag size="small" type="info" style="margin-left: 10px">
                          {{ getCheckInfoSummary(response.checkInfo) }}
                        </el-tag>
                      </span>
                    </div>

                    <el-collapse-transition>
                      <div v-show="response._checkExpanded">
                        <!-- 错误响应列表 -->
                        <div v-if="response.checkInfo?.errResList?.length > 0" style="margin-bottom: 15px">
                          <div style="font-weight: 500; color: #303133; margin-bottom: 8px">错误响应列表</div>
                          <div
                            v-for="(errRes, errIdx) in response.checkInfo.errResList"
                            :key="errIdx"
                            style="margin-bottom: 8px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 8px; background-color: #fff"
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
                        <div v-if="response.checkInfo?.checkItemList?.length > 0">
                          <div style="font-weight: 500; color: #303133; margin-bottom: 8px">校验项列表</div>
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
                            <div style="font-size: 12px; color: #606266; margin-bottom: 3px">表达式：</div>
                            <pre style="margin: 0 0 5px 0; font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 11px; background-color: #f5f7fa; padding: 5px; border-radius: 3px">{{ checkItem.checkExpression }}</pre>
                          </div>
                        </div>
                      </div>
                    </el-collapse-transition>
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
              <div
                style="flex: 1; display: flex; align-items: center; cursor: pointer"
                @click="toggleTask(index)"
              >
                <span style="margin-right: 8px; color: #909399; font-size: 14px">
                  {{ activeTaskIndex === index ? '▼' : '▶' }}
                </span>
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
            </template>

            <el-collapse-transition>
              <div v-show="activeTaskIndex === index">
                <!-- 基本信息 -->
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
                  <el-descriptions-item label="启用状态">
                    <el-tag :type="task.enableStatus === 1 ? 'success' : 'info'" size="small">
                      {{ task.enableStatus === 1 ? '已启用' : '已停用' }}
                    </el-tag>
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

                <!-- 路径参数 -->
                <div v-if="hasUriVariables(task.request?.uriVariables)" style="margin-bottom: 15px">
                  <h4 style="color: #606266; margin-bottom: 10px">路径参数</h4>
                  <el-table :data="getUriVariablesList(task.request.uriVariables)" border size="small">
                    <el-table-column prop="key" label="参数名" width="200" />
                    <el-table-column prop="value" label="参数值" />
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
              <div
                style="flex: 1; display: flex; align-items: center; cursor: pointer"
                @click="togglePlugin(index)"
              >
                <span style="margin-right: 8px; color: #909399; font-size: 14px">
                  {{ activePluginIndex === index ? '▼' : '▶' }}
                </span>
                <el-tag :type="plugin.enableStatus === 1 ? 'success' : 'info'" size="small">
                  {{ plugin.enableStatus === 1 ? '已启用' : '已停用' }}
                </el-tag>
                <span style="margin-left: 10px; font-weight: bold">
                  {{ plugin.pluginCode }}
                </span>
              </div>
            </template>

            <el-collapse-transition>
              <div v-show="activePluginIndex === index">
                <el-descriptions :column="1" border size="small" style="margin-bottom: 15px">
                  <el-descriptions-item label="插件编码">
                    {{ plugin.pluginCode }}
                  </el-descriptions-item>
                  <el-descriptions-item label="启用状态">
                    <el-tag :type="plugin.enableStatus === 1 ? 'success' : 'info'" size="small">
                      {{ plugin.enableStatus === 1 ? '已启用' : '已停用' }}
                    </el-tag>
                  </el-descriptions-item>
                </el-descriptions>

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

      <!-- 浏览模式 -->
      <div v-show="viewMode === 'browse'" v-if="handlerData">
        <!-- 浏览模式提示 -->
        <el-alert
          title="浏览模式 - 只读视图"
          type="warning"
          :closable="false"
          show-icon
          style="margin-bottom: 20px"
        >
          <template #default>
            当前为只读浏览模式，如需编辑请点击右上角"编辑"按钮
          </template>
        </el-alert>

        <!-- 基础信息 -->
        <el-divider content-position="left">基础信息</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="处理器名称">{{ handlerData.name }}</el-descriptions-item>
          <el-descriptions-item label="项目名称">{{ handlerData.projectName }}</el-descriptions-item>
          <el-descriptions-item label="HTTP方法">
            <el-tag v-for="method in handlerData.httpMethods" :key="method" size="small" style="margin-right: 5px">
              {{ method }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="请求URI">{{ handlerData.requestUri }}</el-descriptions-item>
          <el-descriptions-item label="标签">{{ handlerData.label || '-' }}</el-descriptions-item>
          <el-descriptions-item label="延迟时间">{{ handlerData.delayTime || 0 }} ms</el-descriptions-item>
          <el-descriptions-item label="启用状态">
            <el-tag :type="handlerData.enableStatus === 1 ? 'success' : 'info'" size="small">
              {{ handlerData.enableStatus === 1 ? '已启用' : '已停用' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <!-- Handler校验配置 -->
        <el-divider content-position="left">Handler校验配置</el-divider>
        <div v-if="hasHandlerCheckInfo">
          <!-- 错误响应列表 -->
          <div v-if="handlerData.checkInfo?.errResList?.length > 0" style="margin-bottom: 15px">
            <h4 style="font-size: 14px; color: #606266; margin-bottom: 10px">错误响应列表 ({{ handlerData.checkInfo.errResList.length }})</h4>
            <el-card v-for="(errRes, idx) in handlerData.checkInfo.errResList" :key="idx" size="small" style="margin-bottom: 8px">
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="错误码">{{ errRes.errResCode }}</el-descriptions-item>
                <el-descriptions-item label="HTTP状态">{{ errRes.status }}</el-descriptions-item>
                <el-descriptions-item label="响应体" :span="2">
                  <pre style="margin: 0; max-height: 150px; overflow: auto; font-size: 12px">{{ formatJson(errRes.body) }}</pre>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </div>
          <!-- 校验项列表 -->
          <div v-if="handlerData.checkInfo?.checkItemList?.length > 0">
            <h4 style="font-size: 14px; color: #606266; margin-bottom: 10px">校验项列表 ({{ handlerData.checkInfo.checkItemList.length }})</h4>
            <el-card v-for="(item, idx) in handlerData.checkInfo.checkItemList" :key="idx" size="small" style="margin-bottom: 8px">
              <el-descriptions :column="1" border size="small">
                <el-descriptions-item label="校验表达式">
                  <pre style="margin: 0; font-size: 12px">{{ item.checkExpression }}</pre>
                </el-descriptions-item>
                <el-descriptions-item label="错误响应码">{{ item.errResCode }}</el-descriptions-item>
              </el-descriptions>
            </el-card>
          </div>
        </div>
        <div v-else style="color: #909399; padding: 10px; text-align: center; background: #f5f7fa; border-radius: 4px">
          未配置Handler校验
        </div>

        <!-- 自定义参数空间 -->
        <el-divider content-position="left">自定义参数空间</el-divider>
        <div v-if="hasCustomizeSpace">
          <el-table :data="customizeSpaceList" border size="small">
            <el-table-column prop="key" label="参数名" width="200" />
            <el-table-column prop="value" label="参数值">
              <template #default="{ row }">
                <pre style="margin: 0; font-size: 12px; max-height: 100px; overflow: auto">{{ row.value }}</pre>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else style="color: #909399; padding: 10px; text-align: center; background: #f5f7fa; border-radius: 4px">
          暂无自定义参数
        </div>

        <!-- 响应集 -->
        <el-divider content-position="left">响应集 ({{ handlerData.responses?.length || 0 }})</el-divider>
        <div v-if="handlerData.responses && handlerData.responses.length > 0">
          <el-card v-for="(response, idx) in handlerData.responses" :key="idx" style="margin-bottom: 10px" size="small">
            <template #header>
              <div style="display: flex; align-items: center">
                <el-tag :type="response.enableStatus === 1 ? 'success' : 'info'" size="small">
                  {{ response.enableStatus === 1 ? '已启用' : '已停用' }}
                </el-tag>
                <span style="margin-left: 8px; font-weight: bold">{{ response.name }}</span>
                <el-tag size="small" style="margin-left: 8px" type="info">HTTP {{ response.response?.status || 200 }}</el-tag>
                <el-tag size="small" style="margin-left: 8px" type="warning">延迟 {{ response.delayTime || 0 }}ms</el-tag>
              </div>
            </template>
            <el-descriptions :column="1" border size="small" style="margin-bottom: 10px">
              <el-descriptions-item label="期望表达式" v-if="response.support?.length > 0">
                <div v-for="(expr, i) in response.support" :key="i" style="margin-bottom: 5px">
                  <pre style="margin: 0; font-size: 12px">{{ expr }}</pre>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="响应体">
                <pre style="margin: 0; max-height: 200px; overflow: auto; font-size: 12px">{{ formatJson(response.response?.body) }}</pre>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>
        <div v-else style="color: #909399; padding: 10px; text-align: center; background: #f5f7fa; border-radius: 4px">
          暂无响应配置
        </div>

        <!-- 任务集 -->
        <el-divider content-position="left">任务集 ({{ handlerData.tasks?.length || 0 }})</el-divider>
        <div v-if="handlerData.tasks && handlerData.tasks.length > 0">
          <el-card v-for="(task, idx) in handlerData.tasks" :key="idx" style="margin-bottom: 10px" size="small">
            <template #header>
              <div style="display: flex; align-items: center">
                <el-tag :type="task.enableStatus === 1 ? 'success' : 'info'" size="small">
                  {{ task.enableStatus === 1 ? '已启用' : '已停用' }}
                </el-tag>
                <span style="margin-left: 8px; font-weight: bold">{{ task.name }}</span>
                <el-tag size="small" style="margin-left: 8px" :type="task.async ? 'warning' : 'info'">
                  {{ task.async ? '异步' : '同步' }}
                </el-tag>
              </div>
            </template>
            <el-descriptions :column="2" border size="small" style="margin-bottom: 10px">
              <el-descriptions-item label="Cron表达式">{{ task.cron }}</el-descriptions-item>
              <el-descriptions-item label="执行次数">{{ task.numberOfExecute }}</el-descriptions-item>
              <el-descriptions-item label="请求URL" :span="2">{{ task.request?.requestUrl }}</el-descriptions-item>
              <el-descriptions-item label="HTTP方法">{{ task.request?.httpMethod }}</el-descriptions-item>
              <el-descriptions-item label="期望表达式" :span="2" v-if="task.support?.length > 0">
                <div v-for="(expr, i) in task.support" :key="i" style="margin-bottom: 5px">
                  <pre style="margin: 0; font-size: 12px">{{ expr }}</pre>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="请求体" :span="2" v-if="task.request?.body">
                <pre style="margin: 0; max-height: 200px; overflow: auto; font-size: 12px">{{ formatJson(task.request.body) }}</pre>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>
        <div v-else style="color: #909399; padding: 10px; text-align: center; background: #f5f7fa; border-radius: 4px">
          暂无任务配置
        </div>

        <!-- 插件信息集 -->
        <el-divider content-position="left">插件信息集 ({{ handlerData.pluginInfos?.length || 0 }})</el-divider>
        <div v-if="handlerData.pluginInfos && handlerData.pluginInfos.length > 0">
          <el-card v-for="(plugin, idx) in handlerData.pluginInfos" :key="idx" style="margin-bottom: 10px" size="small">
            <template #header>
              <div style="display: flex; align-items: center">
                <el-tag :type="plugin.enableStatus === 1 ? 'success' : 'info'" size="small">
                  {{ plugin.enableStatus === 1 ? '已启用' : '已停用' }}
                </el-tag>
                <span style="margin-left: 8px; font-weight: bold">{{ plugin.pluginCode }}</span>
              </div>
            </template>
            <el-descriptions :column="1" border size="small">
              <el-descriptions-item label="插件参数">
                <pre style="margin: 0; max-height: 200px; overflow: auto; font-size: 12px">{{ formatJson(plugin.pluginParam) }}</pre>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>
        <div v-else style="color: #909399; padding: 10px; text-align: center; background: #f5f7fa; border-radius: 4px">
          暂无插件配置
        </div>
      </div>

      <!-- JSON模式 -->
      <div v-show="viewMode === 'json'" v-if="handlerData">
        <el-input
          :model-value="formatJson(handlerData)"
          type="textarea"
          :rows="30"
          readonly
          style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { findMockHandler } from '@/api/mockHandler'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const handlerData = ref(null)
const handlerId = ref(null)
const viewMode = ref('browse')
const activeResponseIndex = ref(-1)
const activeTaskIndex = ref(-1)
const activePluginIndex = ref(-1)
const handlerCheckExpanded = ref(false)

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
  handlerId.value = route.params.id
  loadData()
})

async function loadData() {
  try {
    loading.value = true
    const data = await findMockHandler({
      handlerId: handlerId.value
    })

    // 处理数据，添加展开状态
    if (data.checkInfo) {
      if (data.checkInfo.errResList) {
        data.checkInfo.errResList.forEach(err => {
          err._expanded = false
        })
      }
      if (data.checkInfo.checkItemList) {
        data.checkInfo.checkItemList.forEach(item => {
          item._expanded = false
        })
      }
    }

    if (data.responses) {
      data.responses.forEach(resp => {
        resp._checkExpanded = false
        if (resp.checkInfo) {
          if (resp.checkInfo.errResList) {
            resp.checkInfo.errResList.forEach(err => {
              err._expanded = false
            })
          }
          if (resp.checkInfo.checkItemList) {
            resp.checkInfo.checkItemList.forEach(item => {
              item._expanded = false
            })
          }
        }
      })
    }

    handlerData.value = data
  } catch (error) {
    console.error('Load handler detail error:', error)
  } finally {
    loading.value = false
  }
}

function handleEdit() {
  router.push(`/mock-handler/edit/${handlerId.value}`)
}

function goBack() {
  router.back()
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

function hasUriVariables(uriVariables) {
  return uriVariables && Object.keys(uriVariables).length > 0
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

function getUriVariablesList(uriVariables) {
  if (!uriVariables) return []
  return Object.entries(uriVariables).map(([key, value]) => ({
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
.mock-handler-detail-container {
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
