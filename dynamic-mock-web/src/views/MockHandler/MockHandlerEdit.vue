<template>
  <div class="mock-handler-edit-container">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑Mock处理器' : '新增Mock处理器' }}</span>
          <div>
            <el-button type="info" plain @click="showDslDoc">
              <el-icon><Document /></el-icon>
              DSL文档
            </el-button>
            <el-button type="primary" :loading="saveLoading" @click="handleSave">
              <el-icon><Check /></el-icon>
              {{ formData.enableStatus === 1 ? '保存并启用' : '保存' }}
            </el-button>
            <el-button @click="goBack">
              <el-icon><Back /></el-icon>
              返回
            </el-button>
          </div>
        </div>
      </template>

      <!-- 编辑模式切换 -->
      <el-radio-group v-model="editMode" style="margin-bottom: 20px">
        <el-radio-button value="form">表单模式</el-radio-button>
        <el-radio-button value="browse">浏览模式</el-radio-button>
        <el-radio-button value="json">JSON模式（高级）</el-radio-button>
      </el-radio-group>

      <!-- 表单模式 -->
      <div v-show="editMode === 'form'">
        <el-form :model="formData" label-width="120px">
          <!-- 基础信息 -->
          <el-divider content-position="left">基础信息</el-divider>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="处理器名称" required>
                <el-input
                  v-model="formData.name"
                  placeholder="请输入处理器名称"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属项目" required>
                <el-select
                  v-model="formData.projectId"
                  placeholder="请选择项目"
                  style="width: 100%"
                  disabled
                >
                  <el-option
                    v-for="project in projectList"
                    :key="project.projectId"
                    :label="project.projectName"
                    :value="project.projectId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="请求URI" required>
                <el-input
                  v-model="formData.requestUri"
                  placeholder="请输入请求URI，如：/api/user/info"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="HTTP方法" required>
                <el-select
                  v-model="formData.httpMethods"
                  multiple
                  placeholder="请选择HTTP方法"
                  style="width: 100%"
                >
                  <el-option label="GET" value="GET" />
                  <el-option label="POST" value="POST" />
                  <el-option label="PUT" value="PUT" />
                  <el-option label="DELETE" value="DELETE" />
                  <el-option label="PATCH" value="PATCH" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="标签">
                <el-input
                  v-model="formData.label"
                  placeholder="请输入标签（可选）"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="延迟时间(ms)">
                <el-input-number
                  v-model="formData.delayTime"
                  :min="0"
                  :step="100"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="启用状态">
            <el-switch
              v-model="formData.enableStatus"
              :active-value="1"
              :inactive-value="2"
              active-text="已启用"
              inactive-text="已停用"
            />
          </el-form-item>

          <!-- Handler级别的校验配置 -->
          <el-divider content-position="left">Handler校验配置</el-divider>
          <el-form-item label="校验配置">
            <div style="width: 100%; border: 1px solid #dcdfe6; border-radius: 4px; padding: 10px">
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
                    {{ getCheckInfoSummary(handlerCheckInfoData) }}
                  </el-tag>
                </span>
              </div>

              <el-collapse-transition>
                <div v-show="handlerCheckExpanded">
                  <el-alert
                    title="Handler级别的校验配置说明：此配置会在所有响应前执行校验，用于验证所有请求的通用条件"
                    type="info"
                    :closable="false"
                    style="margin-bottom: 15px"
                  />

                  <!-- 错误响应列表 -->
                  <div style="margin-bottom: 20px">
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px">
                      <span style="font-weight: 500; color: #303133">错误响应列表</span>
                      <el-button type="primary" size="small" @click="addHandlerErrorResponse()">
                        <el-icon><Plus /></el-icon>
                        添加错误响应
                      </el-button>
                    </div>
                    <div v-if="!handlerCheckInfoData.errResList || handlerCheckInfoData.errResList.length === 0">
                      <el-empty description="暂无错误响应" :image-size="60" />
                    </div>
                    <div
                      v-for="(errRes, errIdx) in handlerCheckInfoData.errResList"
                      :key="errIdx"
                      style="margin-bottom: 10px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #fafafa"
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
                            {{ errRes.errResCode || '未命名' }}
                          </el-tag>
                          <el-tag type="info" size="small">
                            HTTP {{ errRes.status || 400 }}
                          </el-tag>
                        </div>
                        <el-button
                          type="danger"
                          size="small"
                          @click.stop="removeHandlerErrorResponse(errIdx)"
                        >
                          删除
                        </el-button>
                      </div>

                      <el-collapse-transition>
                        <div v-show="errRes._expanded">
                          <el-input
                            v-model="errRes.errResCode"
                            placeholder="错误响应编码，如：ERR_001"
                            style="margin-bottom: 10px"
                            size="small"
                          >
                            <template #prepend>编码</template>
                          </el-input>

                          <el-input-number
                            v-model="errRes.status"
                            :min="100"
                            :max="599"
                            placeholder="HTTP状态码"
                            style="width: 100%; margin-bottom: 10px"
                            size="small"
                          >
                            <template #prepend>状态码</template>
                          </el-input-number>

                          <div style="margin-bottom: 10px">
                            <div style="font-size: 12px; color: #606266; margin-bottom: 4px">响应头</div>
                            <el-table :data="getErrResHeadersList(errRes)" border size="small">
                              <el-table-column label="Header名" width="150">
                                <template #default="{ row }">
                                  <el-input v-model="row.key" placeholder="如：Content-Type" size="small" />
                                </template>
                              </el-table-column>
                              <el-table-column label="Header值">
                                <template #default="{ row }">
                                  <el-input v-model="row.value" placeholder="如：application/json" size="small" />
                                </template>
                              </el-table-column>
                              <el-table-column label="操作" width="80" align="center">
                                <template #default="{ $index: headerIndex }">
                                  <el-button
                                    type="danger"
                                    link
                                    size="small"
                                    @click="removeErrResHeader(errRes, headerIndex)"
                                  >
                                    删除
                                  </el-button>
                                </template>
                              </el-table-column>
                            </el-table>
                            <el-button size="small" style="margin-top: 5px" @click="addErrResHeader(errRes)">
                              <el-icon><Plus /></el-icon>
                              添加Header
                            </el-button>
                          </div>

                          <div>
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px">
                              <div style="font-size: 12px; color: #606266">响应体JSON</div>
                              <div>
                                <el-button
                                  type="primary"
                                  link
                                  size="small"
                                  @click="formatJson('handlerErrRes', errRes, 'bodyJson')"
                                >
                                  <el-icon><MagicStick /></el-icon>
                                  格式化
                                </el-button>
                                <el-button
                                  type="primary"
                                  link
                                  size="small"
                                  @click="openFullscreenEdit('handlerErrRes', errRes, 'bodyJson', 'Handler错误响应 - 响应体JSON')"
                                >
                                  <el-icon><FullScreen /></el-icon>
                                  全屏编辑
                                </el-button>
                              </div>
                            </div>
                            <el-input
                              v-model="errRes.bodyJson"
                              type="textarea"
                              :rows="4"
                              placeholder="响应体JSON"
                              size="small"
                              style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                            />
                          </div>
                        </div>
                      </el-collapse-transition>
                    </div>
                  </div>

                  <!-- 校验项列表 -->
                  <div>
                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px">
                      <span style="font-weight: 500; color: #303133">校验项列表</span>
                      <el-button type="primary" size="small" @click="addHandlerCheckItem()">
                        <el-icon><Plus /></el-icon>
                        添加校验项
                      </el-button>
                    </div>
                    <div v-if="!handlerCheckInfoData.checkItemList || handlerCheckInfoData.checkItemList.length === 0">
                      <el-empty description="暂无校验项" :image-size="60" />
                    </div>
                    <div
                      v-for="(checkItem, checkIdx) in handlerCheckInfoData.checkItemList"
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
                        <el-button
                          type="danger"
                          size="small"
                          @click.stop="removeHandlerCheckItem(checkIdx)"
                        >
                          删除
                        </el-button>
                      </div>

                      <el-collapse-transition>
                        <div v-show="checkItem._expanded">
                          <div style="margin-bottom: 10px">
                            <div style="font-size: 12px; color: #606266; margin-bottom: 4px">校验表达式</div>
                            <el-input
                              v-model="checkItem.checkExpression"
                              type="textarea"
                              :rows="3"
                              placeholder="如：$.userId != null && $.userId.length > 0"
                              size="small"
                              style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace"
                            />
                          </div>
                          <div style="margin-bottom: 10px">
                            <div style="font-size: 12px; color: #606266; margin-bottom: 4px">失败时返回的错误响应</div>
                            <el-select
                              v-model="checkItem.errResCode"
                              placeholder="选择错误响应编码"
                              style="width: 100%"
                              size="small"
                            >
                              <el-option
                                v-for="errRes in handlerCheckInfoData.errResList"
                                :key="errRes.errResCode"
                                :label="`${errRes.errResCode} (HTTP ${errRes.status})`"
                                :value="errRes.errResCode"
                              />
                            </el-select>
                          </div>
                          <div>
                            <div style="font-size: 12px; color: #606266; margin-bottom: 4px">
                              错误消息填充参数
                              <el-button type="primary" link size="small" @click="addErrMsgParam(checkItem)">
                                添加
                              </el-button>
                            </div>
                            <el-tag
                              v-for="(param, paramIdx) in checkItem.errMsgFillParam"
                              :key="paramIdx"
                              closable
                              @close="removeErrMsgParam(checkItem, paramIdx)"
                              style="margin-right: 5px; margin-bottom: 5px"
                              size="small"
                            >
                              {{ param }}
                            </el-tag>
                            <el-input
                              v-if="checkItem._addingParam"
                              v-model="checkItem._newParam"
                              size="small"
                              style="width: 200px; margin-top: 5px"
                              placeholder="输入参数名"
                              @keyup.enter="confirmAddErrMsgParam(checkItem)"
                              @blur="cancelAddErrMsgParam(checkItem)"
                            />
                          </div>
                        </div>
                      </el-collapse-transition>
                    </div>
                  </div>
                </div>
              </el-collapse-transition>
            </div>
          </el-form-item>

          <!-- 自定义参数空间 -->
          <el-form-item label="自定义参数">
            <div style="width: 100%">
              <el-table :data="customizeSpaceList" border style="margin-bottom: 10px">
                <el-table-column label="参数名" width="200">
                  <template #default="{ row }">
                    <el-input v-model="row.key" placeholder="参数名" />
                  </template>
                </el-table-column>
                <el-table-column label="参数值">
                  <template #default="{ row }">
                    <el-input
                      v-model="row.value"
                      type="textarea"
                      :rows="2"
                      placeholder="参数值（支持JSON）"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100" align="center">
                  <template #default="{ $index }">
                    <el-button
                      type="danger"
                      link
                      @click="removeCustomizeParam($index)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-button @click="addCustomizeParam">
                <el-icon><Plus /></el-icon>
                添加参数
              </el-button>
            </div>
          </el-form-item>

          <!-- 响应集 -->
          <el-divider content-position="left">
            <span>响应集</span>
            <el-button
              type="primary"
              size="small"
              style="margin-left: 10px"
              @click="addResponse"
            >
              <el-icon><Plus /></el-icon>
              添加响应
            </el-button>
          </el-divider>

          <div v-if="formData.responses.length === 0" style="text-align: center; color: #909399; padding: 20px">
            暂无响应配置，请点击"添加响应"按钮添加
          </div>

          <el-card
            v-for="(response, index) in formData.responses"
            :key="index"
            style="margin-bottom: 10px"
            shadow="hover"
          >
            <template #header>
              <div style="display: flex; justify-content: space-between; align-items: center">
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
                    {{ response.name || `响应 ${index + 1}` }}
                  </span>
                  <el-tag size="small" style="margin-left: 10px" type="info">
                    HTTP {{ response.response?.status || 200 }}
                  </el-tag>
                </div>
                <div @click.stop style="display: flex; gap: 8px">
                  <el-button
                    size="small"
                    :disabled="index === 0"
                    @click="moveResponseUp(index)"
                    title="上移"
                  >
                    <el-icon><Top /></el-icon>
                  </el-button>
                  <el-button
                    size="small"
                    :disabled="index === formData.responses.length - 1"
                    @click="moveResponseDown(index)"
                    title="下移"
                  >
                    <el-icon><Bottom /></el-icon>
                  </el-button>
                  <el-popconfirm
                    :title="`确定要删除响应【${response.name}】吗？`"
                    confirm-button-text="确定"
                    cancel-button-text="取消"
                    @confirm="removeResponse(index)"
                  >
                    <template #reference>
                      <el-button
                        type="danger"
                        size="small"
                      >
                        删除
                      </el-button>
                    </template>
                  </el-popconfirm>
                </div>
              </div>
            </template>

            <el-collapse-transition>
              <div v-show="activeResponseIndex === index">
                <el-form label-width="120px">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="响应名称">
                        <el-input v-model="response.name" placeholder="请输入响应名称" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="启用状态">
                        <el-switch
                          v-model="response.enableStatus"
                          :active-value="1"
                          :inactive-value="2"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="HTTP状态码">
                        <el-input-number
                          v-model="response.response.status"
                          :min="100"
                          :max="599"
                          style="width: 100%"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="延迟时间(ms)">
                        <el-input-number
                          v-model="response.delayTime"
                          :min="0"
                          :step="100"
                          style="width: 100%"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="校验配置">
                    <div style="width: 100%; border: 1px solid #dcdfe6; border-radius: 4px; padding: 10px">
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
                            {{ getCheckInfoSummary(response.checkInfoData) }}
                          </el-tag>
                        </span>
                      </div>

                      <el-collapse-transition>
                        <div v-show="response._checkExpanded">
                          <el-alert
                            title="校验配置说明：校验在期望表达式之前执行，用于验证请求是否满足特定条件"
                            type="info"
                            :closable="false"
                            style="margin-bottom: 15px"
                          />

                          <!-- 错误响应列表 -->
                          <div style="margin-bottom: 20px">
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px">
                              <span style="font-weight: 500; color: #303133">错误响应列表</span>
                              <el-button type="primary" size="small" @click="addErrorResponse(response)">
                                <el-icon><Plus /></el-icon>
                                添加错误响应
                              </el-button>
                            </div>
                            <div v-if="!response.checkInfoData.errResList || response.checkInfoData.errResList.length === 0">
                              <el-empty description="暂无错误响应" :image-size="60" />
                            </div>
                            <div
                              v-for="(errRes, errIdx) in response.checkInfoData.errResList"
                              :key="errIdx"
                              style="margin-bottom: 10px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #fafafa"
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
                                    {{ errRes.errResCode || '未命名' }}
                                  </el-tag>
                                  <el-tag type="info" size="small">
                                    HTTP {{ errRes.status || 400 }}
                                  </el-tag>
                                </div>
                                <el-button
                                  type="danger"
                                  size="small"
                                  @click.stop="removeErrorResponse(response, errIdx)"
                                >
                                  删除
                                </el-button>
                              </div>

                              <el-collapse-transition>
                                <div v-show="errRes._expanded">
                                  <el-input
                                    v-model="errRes.errResCode"
                                    placeholder="错误响应编码，如：ERR_001"
                                    style="margin-bottom: 10px"
                                    size="small"
                                  >
                                    <template #prepend>编码</template>
                                  </el-input>

                                  <el-input-number
                                    v-model="errRes.status"
                                    :min="100"
                                    :max="599"
                                    placeholder="HTTP状态码"
                                    style="width: 100%; margin-bottom: 10px"
                                    size="small"
                                  >
                                    <template #prepend>状态码</template>
                                  </el-input-number>

                                  <div style="margin-bottom: 10px">
                                    <div style="font-size: 12px; color: #606266; margin-bottom: 4px">响应头</div>
                                    <el-table :data="getErrResHeadersList(errRes)" border size="small">
                                      <el-table-column label="Header名" width="150">
                                        <template #default="{ row }">
                                          <el-input v-model="row.key" placeholder="如：Content-Type" size="small" />
                                        </template>
                                      </el-table-column>
                                      <el-table-column label="Header值">
                                        <template #default="{ row }">
                                          <el-input v-model="row.value" placeholder="如：application/json" size="small" />
                                        </template>
                                      </el-table-column>
                                      <el-table-column label="操作" width="80" align="center">
                                        <template #default="{ $index: headerIndex }">
                                          <el-button
                                            type="danger"
                                            link
                                            size="small"
                                            @click="removeErrResHeader(errRes, headerIndex)"
                                          >
                                            删除
                                          </el-button>
                                        </template>
                                      </el-table-column>
                                    </el-table>
                                    <el-button size="small" style="margin-top: 5px" @click="addErrResHeader(errRes)">
                                      <el-icon><Plus /></el-icon>
                                      添加Header
                                    </el-button>
                                  </div>

                                  <div>
                                    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px">
                                      <div style="font-size: 12px; color: #606266">响应体JSON</div>
                                      <div>
                                        <el-button
                                          type="primary"
                                          link
                                          size="small"
                                          @click="formatJson('responseErrRes', errRes, 'bodyJson')"
                                        >
                                          <el-icon><MagicStick /></el-icon>
                                          格式化
                                        </el-button>
                                        <el-button
                                          type="primary"
                                          link
                                          size="small"
                                          @click="openFullscreenEdit('responseErrRes', errRes, 'bodyJson', '响应错误响应 - 响应体JSON')"
                                        >
                                          <el-icon><FullScreen /></el-icon>
                                          全屏编辑
                                        </el-button>
                                      </div>
                                    </div>
                                    <el-input
                                      v-model="errRes.bodyJson"
                                      type="textarea"
                                      :rows="4"
                                      placeholder="响应体JSON"
                                      size="small"
                                      style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                                    />
                                  </div>
                                </div>
                              </el-collapse-transition>
                            </div>
                          </div>

                          <!-- 校验项列表 -->
                          <div>
                            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px">
                              <span style="font-weight: 500; color: #303133">校验项列表</span>
                              <el-button type="primary" size="small" @click="addCheckItem(response)">
                                <el-icon><Plus /></el-icon>
                                添加校验项
                              </el-button>
                            </div>
                            <div v-if="!response.checkInfoData.checkItemList || response.checkInfoData.checkItemList.length === 0">
                              <el-empty description="暂无校验项" :image-size="60" />
                            </div>
                            <div
                              v-for="(checkItem, checkIdx) in response.checkInfoData.checkItemList"
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
                                <el-button
                                  type="danger"
                                  size="small"
                                  @click.stop="removeCheckItem(response, checkIdx)"
                                >
                                  删除
                                </el-button>
                              </div>

                              <el-collapse-transition>
                                <div v-show="checkItem._expanded">
                                  <div style="margin-bottom: 10px">
                                    <div style="font-size: 12px; color: #606266; margin-bottom: 4px">校验表达式</div>
                                    <el-input
                                      v-model="checkItem.checkExpression"
                                      type="textarea"
                                      :rows="3"
                                      placeholder="如：$.userId != null && $.userId.length > 0"
                                      size="small"
                                      style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace"
                                    />
                                  </div>
                                  <div style="margin-bottom: 10px">
                                    <div style="font-size: 12px; color: #606266; margin-bottom: 4px">失败时返回的错误响应</div>
                                    <el-select
                                      v-model="checkItem.errResCode"
                                      placeholder="选择错误响应编码"
                                      style="width: 100%"
                                      size="small"
                                    >
                                      <el-option
                                        v-for="errRes in response.checkInfoData.errResList"
                                        :key="errRes.errResCode"
                                        :label="`${errRes.errResCode} (HTTP ${errRes.status})`"
                                        :value="errRes.errResCode"
                                      />
                                    </el-select>
                                  </div>
                                  <div>
                                    <div style="font-size: 12px; color: #606266; margin-bottom: 4px">
                                      错误消息填充参数
                                      <el-button type="primary" link size="small" @click="addErrMsgParam(checkItem)">
                                        添加
                                      </el-button>
                                    </div>
                                    <el-tag
                                      v-for="(param, paramIdx) in checkItem.errMsgFillParam"
                                      :key="paramIdx"
                                      closable
                                      @close="removeErrMsgParam(checkItem, paramIdx)"
                                      style="margin-right: 5px; margin-bottom: 5px"
                                      size="small"
                                    >
                                      {{ param }}
                                    </el-tag>
                                    <el-input
                                      v-if="checkItem._addingParam"
                                      v-model="checkItem._newParam"
                                      size="small"
                                      style="width: 200px; margin-top: 5px"
                                      placeholder="输入参数名"
                                      @keyup.enter="confirmAddErrMsgParam(checkItem)"
                                      @blur="cancelAddErrMsgParam(checkItem)"
                                    />
                                  </div>
                                </div>
                              </el-collapse-transition>
                            </div>
                          </div>
                        </div>
                      </el-collapse-transition>
                    </div>
                  </el-form-item>

                  <el-form-item label="期望表达式">
                    <div style="width: 100%; border: 1px solid #dcdfe6; border-radius: 4px; padding: 10px">
                      <div
                        style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; cursor: pointer"
                        @click="response._supportExpanded = !response._supportExpanded"
                      >
                        <span style="font-weight: 500; color: #606266">
                          <span style="margin-right: 5px">
                            {{ response._supportExpanded ? '▼' : '▶' }}
                          </span>
                          期望表达式配置
                          <el-tag size="small" type="info" style="margin-left: 10px">
                            {{ (response.support || []).length }} 个表达式
                          </el-tag>
                        </span>
                        <el-button
                          type="primary"
                          size="small"
                          @click.stop="addResponseSupport(response)"
                        >
                          <el-icon><Plus /></el-icon>
                          添加表达式
                        </el-button>
                      </div>

                      <el-collapse-transition>
                        <div v-show="response._supportExpanded">
                          <el-alert
                            v-if="(response.support || []).length === 0"
                            title="暂无期望表达式，点击【添加表达式】按钮添加"
                            type="info"
                            :closable="false"
                            show-icon
                            style="margin-bottom: 10px"
                          />
                          <div v-else>
                            <div
                              v-for="(expr, index) in response.support"
                              :key="index"
                              style="margin-bottom: 15px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #fafafa"
                            >
                              <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
                                <span style="font-weight: 500; color: #606266">
                                  表达式 #{{ index + 1 }}
                                </span>
                                <el-button
                                  type="danger"
                                  size="small"
                                  @click="removeResponseSupport(response, index)"
                                >
                                  删除
                                </el-button>
                              </div>
                              <el-input
                                v-model="response.support[index]"
                                type="textarea"
                                :rows="4"
                                placeholder="请输入完整的表达式，例如：&#10;$.userId == '123'&#10;$.timestamp > 1000000000&#10;$.status in ['active', 'pending']&#10;$.data.name != null && $.data.name.length > 0"
                                style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 13px"
                              />
                            </div>
                          </div>
                          <div style="margin-top: 15px; border-top: 1px solid #e4e7ed; padding-top: 10px">
                            <div
                              style="display: flex; align-items: center; cursor: pointer; user-select: none"
                              @click="response._syntaxExpanded = !response._syntaxExpanded"
                            >
                              <span style="margin-right: 5px; color: #909399">
                                {{ response._syntaxExpanded ? '▼' : '▶' }}
                              </span>
                              <span style="font-size: 12px; font-weight: 500; color: #606266">
                                期望表达式语法说明
                              </span>
                            </div>
                            <el-collapse-transition>
                              <div v-show="response._syntaxExpanded" style="font-size: 12px; color: #909399; margin-top: 8px; margin-left: 15px">
                                <div>• <strong>JSON路径</strong>：$.userId、$.data.name、$.items[0].price</div>
                                <div>• <strong>比较运算</strong>：== (等于)、!= (不等于)、> (大于)、< (小于)、>= (大于等于)、<= (小于等于)</div>
                                <div>• <strong>逻辑运算</strong>：&& (与)、|| (或)、! (非)</div>
                                <div>• <strong>集合运算</strong>：in (包含)、not in (不包含)</div>
                                <div>• <strong>空值判断</strong>：== null、!= null</div>
                              </div>
                            </el-collapse-transition>
                          </div>
                        </div>
                      </el-collapse-transition>
                    </div>
                  </el-form-item>

                  <el-form-item label="响应头">
                    <el-table :data="getHeadersList(response.response)" border size="small">
                      <el-table-column label="Header名" width="200">
                        <template #default="{ row }">
                          <el-input v-model="row.key" placeholder="如：Content-Type" size="small" />
                        </template>
                      </el-table-column>
                      <el-table-column label="Header值">
                        <template #default="{ row }">
                          <el-input v-model="row.value" placeholder="如：application/json" size="small" />
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="100" align="center">
                        <template #default="{ $index: headerIndex }">
                          <el-button
                            type="danger"
                            link
                            size="small"
                            @click="removeHeader(response.response, headerIndex)"
                          >
                            删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                    <el-button size="small" style="margin-top: 5px" @click="addHeader(response.response)">
                      <el-icon><Plus /></el-icon>
                      添加Header
                    </el-button>
                  </el-form-item>

                  <el-form-item label="响应体">
                    <div style="width: 100%">
                      <div style="display: flex; justify-content: flex-end; margin-bottom: 5px">
                        <el-button
                          type="primary"
                          link
                          size="small"
                          @click="formatJson('response', response, 'bodyJson')"
                        >
                          <el-icon><MagicStick /></el-icon>
                          格式化
                        </el-button>
                        <el-button
                          type="primary"
                          link
                          size="small"
                          @click="openFullscreenEdit('response', response, 'bodyJson', `响应【${response.name}】 - 响应体JSON`)"
                        >
                          <el-icon><FullScreen /></el-icon>
                          全屏编辑
                        </el-button>
                      </div>
                      <el-input
                        v-model="response.bodyJson"
                        type="textarea"
                        :rows="10"
                        placeholder="请输入JSON格式的响应体"
                        style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                      />
                    </div>
                  </el-form-item>
                </el-form>
              </div>
            </el-collapse-transition>
          </el-card>

          <!-- 任务集 -->
          <el-divider content-position="left">
            <span>任务集</span>
            <el-button
              type="primary"
              size="small"
              style="margin-left: 10px"
              @click="addTask"
            >
              <el-icon><Plus /></el-icon>
              添加任务
            </el-button>
          </el-divider>

          <div v-if="formData.tasks.length === 0" style="text-align: center; color: #909399; padding: 20px">
            暂无任务配置，请点击"添加任务"按钮添加
          </div>

          <el-card
            v-for="(task, index) in formData.tasks"
            :key="index"
            style="margin-bottom: 10px"
            shadow="hover"
          >
            <template #header>
              <div style="display: flex; justify-content: space-between; align-items: center">
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
                    {{ task.name || `任务 ${index + 1}` }}
                  </span>
                  <el-tag size="small" style="margin-left: 10px" :type="task.async ? 'warning' : 'info'">
                    {{ task.async ? '异步' : '同步' }}
                  </el-tag>
                </div>
                <div @click.stop>
                  <el-popconfirm
                    :title="`确定要删除任务【${task.name}】吗？`"
                    confirm-button-text="确定"
                    cancel-button-text="取消"
                    @confirm="removeTask(index)"
                  >
                    <template #reference>
                      <el-button
                        type="danger"
                        size="small"
                      >
                        删除
                      </el-button>
                    </template>
                  </el-popconfirm>
                </div>
              </div>
            </template>

            <el-collapse-transition>
              <div v-show="activeTaskIndex === index">
                <el-form label-width="120px">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="任务名称">
                        <el-input v-model="task.name" placeholder="请输入任务名称" />
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="启用状态">
                        <el-switch
                          v-model="task.enableStatus"
                          :active-value="1"
                          :inactive-value="2"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="执行方式">
                        <el-radio-group v-model="task.async">
                          <el-radio :label="false">同步</el-radio>
                          <el-radio :label="true">异步</el-radio>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="执行次数">
                        <el-input-number
                          v-model="task.numberOfExecute"
                          :min="1"
                          style="width: 100%"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="Cron表达式">
                    <el-input v-model="task.cron" placeholder="如：0 0 0 * * ?（每天零点执行）" />
                  </el-form-item>

                  <el-form-item label="配置说明">
                    <el-alert
                      title="任务配置说明"
                      type="info"
                      :closable="false"
                    >
                      <p>同步任务：在响应前执行；异步任务：在响应后执行</p>
                      <p>Cron表达式格式：秒 分 时 日 月 周</p>
                    </el-alert>
                  </el-form-item>

                  <el-form-item label="期望表达式">
                    <div style="width: 100%; border: 1px solid #dcdfe6; border-radius: 4px; padding: 10px">
                      <div
                        style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; cursor: pointer"
                        @click="task._supportExpanded = !task._supportExpanded"
                      >
                        <span style="font-weight: 500; color: #606266">
                          <span style="margin-right: 5px">
                            {{ task._supportExpanded ? '▼' : '▶' }}
                          </span>
                          期望表达式配置
                          <el-tag size="small" type="info" style="margin-left: 10px">
                            {{ (task.support || []).length }} 个表达式
                          </el-tag>
                        </span>
                        <el-button
                          type="primary"
                          size="small"
                          @click.stop="addTaskSupport(task)"
                        >
                          <el-icon><Plus /></el-icon>
                          添加表达式
                        </el-button>
                      </div>

                      <el-collapse-transition>
                        <div v-show="task._supportExpanded">
                          <el-alert
                            v-if="(task.support || []).length === 0"
                            title="暂无期望表达式，点击【添加表达式】按钮添加"
                            type="info"
                            :closable="false"
                            show-icon
                            style="margin-bottom: 10px"
                          />
                          <div v-else>
                            <div
                              v-for="(expr, index) in task.support"
                              :key="index"
                              style="margin-bottom: 15px; border: 1px solid #e4e7ed; border-radius: 4px; padding: 10px; background-color: #fafafa"
                            >
                              <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px">
                                <span style="font-weight: 500; color: #606266">
                                  表达式 #{{ index + 1 }}
                                </span>
                                <el-button
                                  type="danger"
                                  size="small"
                                  @click="removeTaskSupport(task, index)"
                                >
                                  删除
                                </el-button>
                              </div>
                              <el-input
                                v-model="task.support[index]"
                                type="textarea"
                                :rows="4"
                                placeholder="请输入完整的表达式，例如：&#10;$.userId == '123'&#10;$.timestamp > 1000000000&#10;$.status in ['active', 'pending']&#10;$.data.name != null && $.data.name.length > 0"
                                style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 13px"
                              />
                            </div>
                          </div>
                          <div style="margin-top: 15px; border-top: 1px solid #e4e7ed; padding-top: 10px">
                            <div
                              style="display: flex; align-items: center; cursor: pointer; user-select: none"
                              @click="task._syntaxExpanded = !task._syntaxExpanded"
                            >
                              <span style="margin-right: 5px; color: #909399">
                                {{ task._syntaxExpanded ? '▼' : '▶' }}
                              </span>
                              <span style="font-size: 12px; font-weight: 500; color: #606266">
                                期望表达式语法说明
                              </span>
                            </div>
                            <el-collapse-transition>
                              <div v-show="task._syntaxExpanded" style="font-size: 12px; color: #909399; margin-top: 8px; margin-left: 15px">
                                <div>• <strong>JSON路径</strong>：$.userId、$.data.name、$.items[0].price</div>
                                <div>• <strong>比较运算</strong>：== (等于)、!= (不等于)、> (大于)、< (小于)、>= (大于等于)、<= (小于等于)</div>
                                <div>• <strong>逻辑运算</strong>：&& (与)、|| (或)、! (非)</div>
                                <div>• <strong>集合运算</strong>：in (包含)、not in (不包含)</div>
                                <div>• <strong>空值判断</strong>：== null、!= null</div>
                              </div>
                            </el-collapse-transition>
                          </div>
                        </div>
                      </el-collapse-transition>
                    </div>
                  </el-form-item>

                  <el-form-item label="请求URL">
                    <el-input
                      v-model="task.request.requestUrl"
                      placeholder="如：http://localhost:8080/api/callback"
                    />
                  </el-form-item>

                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="HTTP方法">
                        <el-select v-model="task.request.httpMethod" style="width: 100%">
                          <el-option label="GET" value="GET" />
                          <el-option label="POST" value="POST" />
                          <el-option label="PUT" value="PUT" />
                          <el-option label="DELETE" value="DELETE" />
                          <el-option label="PATCH" value="PATCH" />
                        </el-select>
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <el-form-item label="请求头">
                    <el-table :data="getTaskHeadersList(task.request)" border size="small">
                      <el-table-column label="Header名" width="200">
                        <template #default="{ row }">
                          <el-input v-model="row.key" placeholder="如：Content-Type" size="small" />
                        </template>
                      </el-table-column>
                      <el-table-column label="Header值">
                        <template #default="{ row }">
                          <el-input v-model="row.value" placeholder="如：application/json" size="small" />
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="100" align="center">
                        <template #default="{ $index: headerIndex }">
                          <el-button
                            type="danger"
                            link
                            size="small"
                            @click="removeTaskHeader(task.request, headerIndex)"
                          >
                            删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                    <el-button size="small" style="margin-top: 5px" @click="addTaskHeader(task.request)">
                      <el-icon><Plus /></el-icon>
                      添加Header
                    </el-button>
                  </el-form-item>

                  <el-form-item label="路径参数">
                    <el-table :data="getTaskUriVariablesList(task.request)" border size="small">
                      <el-table-column label="参数名" width="200">
                        <template #default="{ row }">
                          <el-input v-model="row.key" placeholder="如：userId" size="small" />
                        </template>
                      </el-table-column>
                      <el-table-column label="参数值">
                        <template #default="{ row }">
                          <el-input v-model="row.value" placeholder="如：12345" size="small" />
                        </template>
                      </el-table-column>
                      <el-table-column label="操作" width="100" align="center">
                        <template #default="{ $index: varIndex }">
                          <el-button
                            type="danger"
                            link
                            size="small"
                            @click="removeTaskUriVariable(task.request, varIndex)"
                          >
                            删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                    <el-button size="small" style="margin-top: 5px" @click="addTaskUriVariable(task.request)">
                      <el-icon><Plus /></el-icon>
                      添加路径参数
                    </el-button>
                  </el-form-item>

                  <el-form-item label="请求体">
                    <div style="width: 100%">
                      <div style="display: flex; justify-content: flex-end; margin-bottom: 5px">
                        <el-button
                          type="primary"
                          link
                          size="small"
                          @click="formatJson('task', task, 'requestBodyJson')"
                        >
                          <el-icon><MagicStick /></el-icon>
                          格式化
                        </el-button>
                        <el-button
                          type="primary"
                          link
                          size="small"
                          @click="openFullscreenEdit('task', task, 'requestBodyJson', `任务【${task.name}】 - 请求体JSON`)"
                        >
                          <el-icon><FullScreen /></el-icon>
                          全屏编辑
                        </el-button>
                      </div>
                      <el-input
                        v-model="task.requestBodyJson"
                        type="textarea"
                        :rows="8"
                        placeholder="请输入JSON格式的请求体"
                        style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                      />
                    </div>
                  </el-form-item>
                </el-form>
              </div>
            </el-collapse-transition>
          </el-card>

          <!-- 插件信息集 -->
          <el-divider content-position="left">
            <span>插件信息集</span>
            <el-button
              type="primary"
              size="small"
              style="margin-left: 10px"
              @click="addPlugin"
            >
              <el-icon><Plus /></el-icon>
              添加插件
            </el-button>
          </el-divider>

          <div v-if="formData.pluginInfos.length === 0" style="text-align: center; color: #909399; padding: 20px">
            暂无插件配置，请点击"添加插件"按钮添加
          </div>

          <el-card
            v-for="(plugin, index) in formData.pluginInfos"
            :key="index"
            style="margin-bottom: 10px"
            shadow="hover"
          >
            <template #header>
              <div style="display: flex; justify-content: space-between; align-items: center">
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
                    {{ getSelectedPluginInfo(plugin)?.pluginName || plugin.pluginCode || `插件 ${index + 1}` }}
                  </span>
                  <el-tag v-if="plugin.pluginCode" size="small" type="info" style="margin-left: 8px">
                    {{ plugin.pluginCode }}
                  </el-tag>
                </div>
                <div @click.stop>
                  <el-popconfirm
                    :title="`确定要删除插件【${getSelectedPluginInfo(plugin)?.pluginName || plugin.pluginCode || '未命名插件'}】吗？`"
                    confirm-button-text="确定"
                    cancel-button-text="取消"
                    @confirm="removePlugin(index)"
                  >
                    <template #reference>
                      <el-button type="danger" size="small">
                        删除
                      </el-button>
                    </template>
                  </el-popconfirm>
                </div>
              </div>
            </template>

            <el-collapse-transition>
              <div v-show="activePluginIndex === index">
                <el-form label-width="120px">
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-form-item label="插件编码" required>
                        <el-select
                          v-model="plugin.pluginCode"
                          placeholder="请选择插件"
                          style="width: 100%"
                          @change="handlePluginChange(plugin)"
                        >
                          <el-option
                            v-for="item in pluginList"
                            :key="item.pluginCode"
                            :label="`${item.pluginName} (${item.pluginCode})`"
                            :value="item.pluginCode"
                          >
                            <div style="display: flex; flex-direction: column">
                              <span style="font-weight: 500">{{ item.pluginName }}</span>
                              <span style="font-size: 12px; color: #909399">{{ item.description }}</span>
                            </div>
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="12">
                      <el-form-item label="启用状态">
                        <el-switch
                          v-model="plugin.enableStatus"
                          :active-value="1"
                          :inactive-value="2"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>

                  <!-- 显示插件详细信息 -->
                  <el-form-item label="插件信息" v-if="getSelectedPluginInfo(plugin)">
                    <div style="width: 100%">
                      <el-alert
                        :title="getSelectedPluginInfo(plugin).pluginName"
                        type="info"
                        :closable="false"
                        style="margin-bottom: 15px"
                      >
                        <div style="font-size: 13px">
                          <div><strong>插件编码：</strong>{{ getSelectedPluginInfo(plugin).pluginCode }}</div>
                          <div><strong>版本：</strong>{{ getSelectedPluginInfo(plugin).version }}</div>
                          <div><strong>插件描述：</strong>{{ getSelectedPluginInfo(plugin).description }}</div>
                        </div>
                      </el-alert>

                      <!-- 插件参数规则说明 -->
                      <div v-if="getSelectedPluginInfo(plugin).metadata?.pluginParamRuleList?.length > 0">
                        <div style="font-weight: 500; color: #303133; margin-bottom: 10px">
                          <el-icon><InfoFilled /></el-icon>
                          插件参数规则说明
                        </div>
                        <el-table
                          :data="getSelectedPluginInfo(plugin).metadata.pluginParamRuleList"
                          border
                          size="small"
                        >
                          <el-table-column prop="fieldName" label="参数名" width="150" />
                          <el-table-column prop="fieldType" label="类型" width="120">
                            <template #default="{ row }">
                              <el-tag size="small" type="info">{{ row.fieldType }}</el-tag>
                            </template>
                          </el-table-column>
                          <el-table-column prop="required" label="是否必须" width="100" align="center">
                            <template #default="{ row }">
                              <el-tag :type="row.required ? 'danger' : 'info'" size="small">
                                {{ row.required ? '必须' : '可选' }}
                              </el-tag>
                            </template>
                          </el-table-column>
                          <el-table-column prop="description" label="参数说明" />
                        </el-table>
                      </div>
                    </div>
                  </el-form-item>

                  <el-form-item label="插件参数">
                    <div style="width: 100%">
                      <div style="display: flex; justify-content: flex-end; margin-bottom: 5px">
                        <el-button
                          type="primary"
                          link
                          size="small"
                          @click="formatJson('plugin', plugin, 'pluginParamJson')"
                        >
                          <el-icon><MagicStick /></el-icon>
                          格式化
                        </el-button>
                        <el-button
                          type="primary"
                          link
                          size="small"
                          @click="openFullscreenEdit('plugin', plugin, 'pluginParamJson', `插件【${plugin.pluginCode}】 - 参数JSON`)"
                        >
                          <el-icon><FullScreen /></el-icon>
                          全屏编辑
                        </el-button>
                      </div>
                      <el-input
                        v-model="plugin.pluginParamJson"
                        type="textarea"
                        :rows="8"
                        placeholder='请输入插件参数JSON，如：{"key": "value"}'
                        style="font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 12px"
                      />
                      <div style="font-size: 12px; color: #909399; margin-top: 5px">
                        请根据上方参数规则说明配置插件参数（JSON格式）
                      </div>
                    </div>
                  </el-form-item>
                </el-form>
              </div>
            </el-collapse-transition>
          </el-card>
        </el-form>

        <!-- 底部操作按钮 -->
        <div style="margin-top: 20px; text-align: center; padding: 20px 0; border-top: 1px solid #ebeef5">
          <el-button type="primary" size="large" :loading="saveLoading" @click="handleSave">
            <el-icon><Check /></el-icon>
            {{ formData.enableStatus === 1 ? '保存并启用' : '保存' }}
          </el-button>
          <el-button size="large" @click="goBack">
            <el-icon><Back /></el-icon>
            返回
          </el-button>
        </div>
      </div>

      <!-- 浏览模式 -->
      <div v-show="editMode === 'browse'">
        <!-- 浏览模式提示 -->
        <el-alert
          title="浏览模式 - 可双击编辑"
          type="info"
          :closable="false"
          show-icon
          style="margin-bottom: 20px"
        >
          <template #default>
            双击任意高亮字段（显示为蓝色）可快速切换到表单模式并定位到该字段进行编辑
          </template>
        </el-alert>

        <!-- 基础信息 -->
        <el-divider content-position="left">基础信息</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="处理器名称">
            <span style="cursor: pointer" @dblclick="handleBrowseDoubleClick('basic', null, 'name')">{{ formData.name }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="所属项目">
            {{ projectList.find(p => p.projectId === formData.projectId)?.projectName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="HTTP方法">
            <el-tag v-for="method in formData.httpMethods" :key="method" size="small" style="margin-right: 5px">
              {{ method }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="请求URI">
            <span style="cursor: pointer" @dblclick="handleBrowseDoubleClick('basic', null, 'requestUri')">{{ formData.requestUri }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="标签">{{ formData.label || '-' }}</el-descriptions-item>
          <el-descriptions-item label="延迟时间">{{ formData.delayTime || 0 }} ms</el-descriptions-item>
          <el-descriptions-item label="启用状态">
            <el-tag :type="formData.enableStatus === 1 ? 'success' : 'info'" size="small">
              {{ formData.enableStatus === 1 ? '已启用' : '已停用' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>

        <!-- Handler校验配置 -->
        <el-divider content-position="left">Handler校验配置</el-divider>
        <div v-if="handlerCheckInfoData && (handlerCheckInfoData.errResList?.length > 0 || handlerCheckInfoData.checkItemList?.length > 0)">
          <!-- 错误响应列表 -->
          <div v-if="handlerCheckInfoData.errResList?.length > 0" style="margin-bottom: 15px">
            <h4 style="font-size: 14px; color: #606266; margin-bottom: 10px">错误响应列表 ({{ handlerCheckInfoData.errResList.length }})</h4>
            <el-card v-for="(errRes, idx) in handlerCheckInfoData.errResList" :key="idx" size="small" style="margin-bottom: 8px">
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="错误码">{{ errRes.errResCode }}</el-descriptions-item>
                <el-descriptions-item label="HTTP状态">{{ errRes.status }}</el-descriptions-item>
                <el-descriptions-item label="响应体" :span="2">
                  <pre style="margin: 0; max-height: 150px; overflow: auto; font-size: 12px">{{ errRes.bodyJson }}</pre>
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </div>
          <!-- 校验项列表 -->
          <div v-if="handlerCheckInfoData.checkItemList?.length > 0">
            <h4 style="font-size: 14px; color: #606266; margin-bottom: 10px">校验项列表 ({{ handlerCheckInfoData.checkItemList.length }})</h4>
            <el-card v-for="(item, idx) in handlerCheckInfoData.checkItemList" :key="idx" size="small" style="margin-bottom: 8px">
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
        <div v-if="customizeSpaceList.length > 0">
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
        <el-divider content-position="left">响应集 ({{ formData.responses.length }})</el-divider>
        <div v-if="formData.responses.length > 0">
          <el-card v-for="(response, idx) in formData.responses" :key="idx" style="margin-bottom: 10px" size="small">
            <template #header>
              <div style="display: flex; align-items: center; cursor: pointer" @dblclick="handleBrowseDoubleClick('response', idx, 'name')">
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
                  <pre style="margin: 0; font-size: 12px; cursor: pointer" @dblclick="handleBrowseDoubleClick('response', idx, 'support', i)">{{ expr }}</pre>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="响应体">
                <pre style="margin: 0; max-height: 200px; overflow: auto; font-size: 12px; cursor: pointer" @dblclick="handleBrowseDoubleClick('response', idx, 'bodyJson')">{{ response.bodyJson }}</pre>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>
        <div v-else style="color: #909399; padding: 10px; text-align: center; background: #f5f7fa; border-radius: 4px">
          暂无响应配置
        </div>

        <!-- 任务集 -->
        <el-divider content-position="left">任务集 ({{ formData.tasks.length }})</el-divider>
        <div v-if="formData.tasks.length > 0">
          <el-card v-for="(task, idx) in formData.tasks" :key="idx" style="margin-bottom: 10px" size="small">
            <template #header>
              <div style="display: flex; align-items: center; cursor: pointer" @dblclick="handleBrowseDoubleClick('task', idx, 'name')">
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
                  <pre style="margin: 0; font-size: 12px; cursor: pointer" @dblclick="handleBrowseDoubleClick('task', idx, 'support', i)">{{ expr }}</pre>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="请求体" :span="2" v-if="task.requestBodyJson">
                <pre style="margin: 0; max-height: 200px; overflow: auto; font-size: 12px; cursor: pointer" @dblclick="handleBrowseDoubleClick('task', idx, 'requestBodyJson')">{{ task.requestBodyJson }}</pre>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>
        <div v-else style="color: #909399; padding: 10px; text-align: center; background: #f5f7fa; border-radius: 4px">
          暂无任务配置
        </div>

        <!-- 插件信息集 -->
        <el-divider content-position="left">插件信息集 ({{ formData.pluginInfos.length }})</el-divider>
        <div v-if="formData.pluginInfos.length > 0">
          <el-card v-for="(plugin, idx) in formData.pluginInfos" :key="idx" style="margin-bottom: 10px" size="small">
            <template #header>
              <div style="display: flex; align-items: center; cursor: pointer" @dblclick="handleBrowseDoubleClick('plugin', idx, 'pluginCode')">
                <el-tag :type="plugin.enableStatus === 1 ? 'success' : 'info'" size="small">
                  {{ plugin.enableStatus === 1 ? '已启用' : '已停用' }}
                </el-tag>
                <span style="margin-left: 8px; font-weight: bold">{{ plugin.pluginCode }}</span>
              </div>
            </template>
            <el-descriptions :column="1" border size="small">
              <el-descriptions-item label="插件参数">
                <pre style="margin: 0; max-height: 200px; overflow: auto; font-size: 12px; cursor: pointer" @dblclick="handleBrowseDoubleClick('plugin', idx, 'pluginParamJson')">{{ plugin.pluginParamJson }}</pre>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>
        <div v-else style="color: #909399; padding: 10px; text-align: center; background: #f5f7fa; border-radius: 4px">
          暂无插件配置
        </div>
      </div>

      <!-- JSON模式 -->
      <div v-show="editMode === 'json'">
        <el-alert
          title="JSON编辑模式"
          type="warning"
          :closable="false"
          style="margin-bottom: 20px"
        >
          <p>此模式适合高级用户，可以直接编辑完整的JSON配置。</p>
          <p>切换回表单模式时，JSON的修改会同步到表单中。</p>
        </el-alert>

        <el-input
          v-model="jsonContent"
          type="textarea"
          :rows="30"
          placeholder="请输入完整的Mock处理器JSON配置"
          @blur="syncJsonToForm"
        />
        <div v-if="jsonError" class="error-message">
          {{ jsonError }}
        </div>

        <!-- 底部操作按钮 -->
        <div style="margin-top: 20px; text-align: center; padding: 20px 0; border-top: 1px solid #ebeef5">
          <el-button type="primary" size="large" :loading="saveLoading" @click="handleSave">
            <el-icon><Check /></el-icon>
            {{ formData.enableStatus === 1 ? '保存并启用' : '保存' }}
          </el-button>
          <el-button size="large" @click="goBack">
            <el-icon><Back /></el-icon>
            返回
          </el-button>
        </div>
      </div>

      <!-- 浮动保存按钮 -->
      <transition name="fade">
        <div v-show="showFloatButton" class="float-buttons">
          <!-- 返回浏览模式按钮 -->
          <el-tooltip v-if="showBackToBrowse" content="返回浏览模式" placement="left" effect="dark" :show-after="300">
            <div class="float-button-wrapper">
              <el-button
                type="success"
                size="large"
                circle
                @click="backToBrowseMode"
              >
                <el-icon><View /></el-icon>
              </el-button>
            </div>
          </el-tooltip>
          <el-tooltip content="文档中心" placement="left" effect="dark" :show-after="300">
            <div class="float-button-wrapper">
              <el-button
                type="info"
                size="large"
                circle
                @click="showDslDoc"
              >
                <el-icon><Document /></el-icon>
              </el-button>
            </div>
          </el-tooltip>
          <el-tooltip :content="formData.enableStatus === 1 ? '保存并启用' : '保存'" placement="left" effect="dark" :show-after="300">
            <div class="float-button-wrapper">
              <el-button
                type="primary"
                size="large"
                circle
                :loading="saveLoading"
                @click="handleSave"
              >
                <el-icon><Check /></el-icon>
              </el-button>
            </div>
          </el-tooltip>
        </div>
      </transition>
    </el-card>

    <!-- DSL文档抽屉 -->
    <el-drawer
      v-model="dslDocVisible"
      title="文档中心"
      direction="rtl"
      size="650px"
    >
      <el-tabs v-model="activeDocTab" class="doc-tabs">
        <!-- DSL语法文档 -->
        <el-tab-pane label="DSL语法" name="dsl">
          <div v-loading="dslDocLoading" class="dsl-doc-container">
            <el-input
              v-model="dslDocSearchKeyword"
              placeholder="搜索DSL语法..."
              clearable
              style="margin-bottom: 20px"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>

            <div v-if="!dslDocLoading && dslDocData">
              <el-collapse v-model="activeDocSections" accordion>
                <el-collapse-item
                  v-for="(section, sectionIndex) in filteredDslDocData"
                  :key="sectionIndex"
                  :name="sectionIndex"
                >
                  <template #title>
                    <div class="section-title">
                      <el-icon><Folder /></el-icon>
                      <span>{{ section.title }}</span>
                    </div>
                  </template>

                  <div class="section-content">
                    <p v-if="section.description" class="section-description">
                      {{ section.description }}
                    </p>

                    <div
                      v-for="(subSection, subIndex) in section.subSections"
                      :key="subIndex"
                      class="sub-section"
                    >
                      <h4 class="sub-section-title">{{ subSection.title }}</h4>

                      <ul v-if="subSection.content && subSection.content.length > 0" class="content-list">
                        <li v-for="(item, itemIndex) in subSection.content" :key="itemIndex">
                          {{ item }}
                        </li>
                      </ul>

                      <div v-if="subSection.examples && subSection.examples.length > 0" class="examples">
                        <div class="examples-header">
                          <el-icon><Edit /></el-icon>
                          <span>示例：</span>
                        </div>
                        <div
                          v-for="(example, exampleIndex) in subSection.examples"
                          :key="exampleIndex"
                          class="example-item"
                        >
                          <code>{{ example }}</code>
                        </div>
                      </div>
                    </div>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </div>

            <el-empty v-if="!dslDocLoading && filteredDslDocData.length === 0" description="未找到匹配的文档" />
          </div>
        </el-tab-pane>

        <!-- 函数文档 -->
        <el-tab-pane label="函数文档" name="function">
          <div class="function-doc-container">
            <el-input
              v-model="functionSearchKeyword"
              placeholder="搜索函数名称、描述..."
              clearable
              style="margin-bottom: 20px"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>

            <!-- 加载中 -->
            <div v-if="functionDocLoading" v-loading="true" style="min-height: 200px" />

            <!-- 有数据 -->
            <div v-else-if="filteredFunctionList.length > 0" class="function-list">
              <el-collapse v-model="activeFunctionNames" accordion>
                <el-collapse-item
                  v-for="func in filteredFunctionList"
                  :key="func.functionName"
                  :name="func.functionName"
                >
                  <template #title>
                    <div class="function-title">
                      <el-tag type="primary" size="small">{{ func.functionName }}</el-tag>
                      <el-tag v-if="func.functionReturnType" type="success" size="small" style="margin-left: 8px">
                        {{ func.functionReturnType }}
                      </el-tag>
                      <span class="function-params">
                        ({{ func.minArgsNumber }}~{{ func.maxArgsNumber === 2147483647 ? '∞' : func.maxArgsNumber }} 参数)
                      </span>
                    </div>
                  </template>

                  <div class="function-detail-content">
                    <div v-if="func.description" class="function-description">
                      <div class="detail-label">
                        <el-icon><Document /></el-icon>
                        <span>说明</span>
                      </div>
                      <div class="detail-value">{{ func.description }}</div>
                    </div>

                    <div v-if="func.example" class="function-example">
                      <div class="detail-label">
                        <el-icon><Edit /></el-icon>
                        <span>示例</span>
                        <el-button
                          type="primary"
                          text
                          size="small"
                          @click="copyFunctionExample(func.example)"
                        >
                          复制
                        </el-button>
                      </div>
                      <div class="example-code">
                        <pre>{{ func.example }}</pre>
                      </div>
                    </div>
                  </div>
                </el-collapse-item>
              </el-collapse>
            </div>

            <!-- 无数据 -->
            <el-empty v-else description="未找到匹配的函数" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-drawer>

    <!-- 全屏编辑对话框 -->
    <el-dialog
      v-model="fullscreenEditVisible"
      :title="fullscreenEditTitle"
      fullscreen
      :close-on-click-modal="false"
    >
      <div style="display: flex; flex-direction: column; height: calc(100vh - 150px)">
        <div style="margin-bottom: 10px; display: flex; justify-content: flex-end">
          <el-button
            type="primary"
            @click="formatFullscreenJson"
          >
            <el-icon><MagicStick /></el-icon>
            格式化
          </el-button>
        </div>
        <el-input
          v-model="fullscreenEditContent"
          type="textarea"
          :rows="30"
          placeholder="请输入JSON格式的内容"
          style="flex: 1; font-family: 'Monaco', 'Menlo', 'Consolas', monospace; font-size: 14px"
        />
      </div>
      <template #footer>
        <el-button @click="fullscreenEditVisible = false">取消</el-button>
        <el-button type="primary" @click="saveFullscreenEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { findMockHandler, saveMockHandler, updateHandlerEnableStatus } from '@/api/mockHandler'
import { queryPlugins } from '@/api/plugin'
import { getDslDocument } from '@/api/document'
import { queryFunctions } from '@/api/function'
import { useProjectStore } from '@/stores/project'

const router = useRouter()
const route = useRoute()
const projectStore = useProjectStore()

const loading = ref(false)
const saveLoading = ref(false)
// 根据是否是编辑模式设置初始视图模式：编辑时默认浏览模式，新增时默认表单模式
const editMode = ref(route.params.id ? 'browse' : 'form')
const jsonError = ref('')
const jsonContent = ref('')
const activeResponseIndex = ref(-1)
const activeTaskIndex = ref(-1)
const activePluginIndex = ref(-1)
const handlerCheckExpanded = ref(false)
const showFloatButton = ref(false)
const showBackToBrowse = ref(false) // 是否显示返回浏览模式按钮

// DSL文档相关
const dslDocVisible = ref(false)
const dslDocLoading = ref(false)
const dslDocData = ref(null)
const dslDocSearchKeyword = ref('')
const activeDocSections = ref(0)
const activeDocTab = ref('dsl')

// 函数文档相关
const functionDocLoading = ref(false)
const functionList = ref([])
const functionSearchKeyword = ref('')
const activeFunctionNames = ref('')

// 全屏编辑相关
const fullscreenEditVisible = ref(false)
const fullscreenEditContent = ref('')
const fullscreenEditTitle = ref('')
const fullscreenEditContext = ref(null) // 存储编辑上下文 { type, obj, field }

// 浏览模式双击聚焦相关
const focusTarget = ref(null) // 存储要聚焦的目标 { section, index, field }

const handlerCheckInfoData = reactive({
  errResList: [],
  checkItemList: []
})

const formData = reactive({
  handlerId: null,
  name: '',
  projectId: null,
  requestUri: '',
  httpMethods: [],
  label: '',
  delayTime: 0,
  enableStatus: 1,
  checkInfo: null,
  customizeSpace: {},
  responses: [],
  tasks: [],
  pluginInfos: []
})

const customizeSpaceList = ref([])

const isEdit = computed(() => !!route.params.id)
const projectList = computed(() => projectStore.projectList)
const pluginList = ref([])

// 监听表单数据变化，同步到JSON
watch(
  () => editMode.value,
  (newMode) => {
    if (newMode === 'json') {
      syncFormToJson()
    }
    // 如果用户手动切换到浏览模式或JSON模式，隐藏返回浏览模式按钮
    if (newMode === 'browse' || newMode === 'json') {
      showBackToBrowse.value = false
    }
  }
)

onMounted(async () => {
  await projectStore.loadProjects()
  await loadPlugins()

  if (isEdit.value) {
    await loadData()
  } else {
    // 新增模式，初始化默认值
    formData.projectId = projectStore.currentProjectId
    addResponse()
  }

  // 添加滚动监听 - 监听 el-main 容器
  const mainContent = document.querySelector('.main-content')
  if (mainContent) {
    mainContent.addEventListener('scroll', handleScroll)
  }
})

onUnmounted(() => {
  // 移除滚动监听
  const mainContent = document.querySelector('.main-content')
  if (mainContent) {
    mainContent.removeEventListener('scroll', handleScroll)
  }
})

// 处理滚动事件
function handleScroll(event) {
  // 当滚动超过300px时显示浮动按钮
  const scrollTop = event.target.scrollTop || 0
  showFloatButton.value = scrollTop > 300
}

async function loadPlugins() {
  try {
    const data = await queryPlugins()
    pluginList.value = data || []
  } catch (error) {
    console.error('Load plugins error:', error)
    ElMessage.error('加载插件列表失败')
  }
}

async function loadData() {
  try {
    loading.value = true
    const data = await findMockHandler({
      handlerId: route.params.id
    })

    // 填充表单数据
    Object.assign(formData, {
      handlerId: data.handlerId,
      name: data.name,
      projectId: data.projectId,
      requestUri: data.requestUri,
      httpMethods: data.httpMethods || [],
      label: data.label || '',
      delayTime: data.delayTime || 0,
      enableStatus: data.enableStatus ?? 1,
      checkInfo: data.checkInfo || null,
      customizeSpace: data.customizeSpace || {},
      responses: processResponsesFromServer(data.responses || []),
      tasks: processTasksFromServer(data.tasks || []),
      pluginInfos: processPluginsFromServer(data.pluginInfos || [])
    })

    // Handler级别的checkInfo
    Object.assign(handlerCheckInfoData, processCheckInfo(data.checkInfo))

    // 转换 customizeSpace 为列表
    customizeSpaceList.value = Object.entries(formData.customizeSpace).map(([key, value]) => ({
      key,
      value: typeof value === 'object' ? JSON.stringify(value) : String(value)
    }))

    // 填充JSON内容
    syncFormToJson()
  } catch (error) {
    console.error('Load handler error:', error)
    ElMessage.error('加载处理器数据失败')
  } finally {
    loading.value = false
  }
}

function processResponsesFromServer(responses) {
  return responses.map(resp => ({
    ...resp,
    response: resp.response || { status: 200, headers: {}, body: null },
    bodyJson: resp.response?.body ? JSON.stringify(resp.response.body, null, 2) : '',
    checkInfoData: processCheckInfo(resp.checkInfo),
    support: resp.support || [],
    _supportExpanded: false,
    _syntaxExpanded: false,
    _checkExpanded: false,
    delayTime: resp.delayTime || 0
  }))
}

function processCheckInfo(checkInfo) {
  if (!checkInfo) {
    return { errResList: [], checkItemList: [] }
  }
  return {
    errResList: (checkInfo.errResList || []).map(err => ({
      ...err,
      bodyJson: err.body ? JSON.stringify(err.body, null, 2) : '',
      _expanded: false
    })),
    checkItemList: (checkInfo.checkItemList || []).map(item => ({
      ...item,
      errMsgFillParam: item.errMsgFillParam || [],
      _addingParam: false,
      _newParam: '',
      _expanded: false
    }))
  }
}

function getCheckInfoSummary(checkInfoData) {
  if (!checkInfoData) return '未配置'
  const errCount = (checkInfoData.errResList || []).length
  const checkCount = (checkInfoData.checkItemList || []).length
  if (errCount === 0 && checkCount === 0) return '未配置'
  return `${errCount} 个错误响应, ${checkCount} 个校验项`
}

function processTasksFromServer(tasks) {
  return tasks.map(task => ({
    ...task,
    request: task.request || { requestUrl: '', httpMethod: 'POST', headers: {}, body: null, uriVariables: {} },
    requestBodyJson: task.request?.body ? JSON.stringify(task.request.body, null, 2) : '',
    support: task.support || [],
    _supportExpanded: false,
    _syntaxExpanded: false
  }))
}

function processPluginsFromServer(plugins) {
  return plugins.map(plugin => ({
    ...plugin,
    pluginParamJson: plugin.pluginParam ? JSON.stringify(plugin.pluginParam, null, 2) : ''
  }))
}

function syncFormToJson() {
  try {
    // 转换 customizeSpace
    const customizeSpace = {}
    customizeSpaceList.value.forEach(item => {
      if (item.key) {
        try {
          customizeSpace[item.key] = JSON.parse(item.value)
        } catch {
          customizeSpace[item.key] = item.value
        }
      }
    })

    // 转换 responses
    const responses = formData.responses.map(resp => {
      const response = { ...resp }

      // 解析 bodyJson
      if (response.bodyJson) {
        try {
          response.response.body = JSON.parse(response.bodyJson)
        } catch {
          response.response.body = response.bodyJson
        }
      }

      // 转换 checkInfoData 为 checkInfo
      let checkInfo = null
      if (response.checkInfoData && (response.checkInfoData.errResList?.length > 0 || response.checkInfoData.checkItemList?.length > 0)) {
        checkInfo = {
          errResList: (response.checkInfoData.errResList || []).map(errRes => {
            // 转换 headers list 回 map
            if (errRes._headersList) {
              errRes.headers = {}
              errRes._headersList.forEach(header => {
                if (header.key) {
                  errRes.headers[header.key] = [header.value]
                }
              })
            }

            let body = null
            if (errRes.bodyJson) {
              try {
                body = JSON.parse(errRes.bodyJson)
              } catch {
                body = errRes.bodyJson
              }
            }
            return {
              errResCode: errRes.errResCode,
              status: errRes.status,
              headers: errRes.headers || {},
              body: body
            }
          }),
          checkItemList: (response.checkInfoData.checkItemList || []).map(item => ({
            checkExpression: item.checkExpression,
            errResCode: item.errResCode,
            errMsgFillParam: item.errMsgFillParam || []
          }))
        }
      }
      response.checkInfo = checkInfo

      // 清理 UI 专用字段
      delete response.bodyJson
      delete response.checkInfoData
      delete response._supportExpanded
      delete response._syntaxExpanded
      delete response._checkExpanded
      delete response._headersList
      return response
    })

    // 转换 tasks
    const tasks = formData.tasks.map(task => {
      const t = { ...task }
      if (t.requestBodyJson) {
        try {
          t.request.body = JSON.parse(t.requestBodyJson)
        } catch {
          t.request.body = t.requestBodyJson
        }
      }
      delete t.requestBodyJson
      delete t._supportExpanded
      delete t._syntaxExpanded
      return t
    })

    // 转换 plugins
    const pluginInfos = formData.pluginInfos.map(plugin => {
      const p = { ...plugin }
      if (p.pluginParamJson) {
        try {
          p.pluginParam = JSON.parse(p.pluginParamJson)
        } catch {
          p.pluginParam = {}
        }
      }
      delete p.pluginParamJson
      return p
    })

    // 解析 handler checkInfo
    let checkInfo = null
    if (handlerCheckInfoData && (handlerCheckInfoData.errResList?.length > 0 || handlerCheckInfoData.checkItemList?.length > 0)) {
      checkInfo = {
        errResList: (handlerCheckInfoData.errResList || []).map(errRes => {
          // 转换 headers list 回 map
          if (errRes._headersList) {
            errRes.headers = {}
            errRes._headersList.forEach(header => {
              if (header.key) {
                errRes.headers[header.key] = [header.value]
              }
            })
          }

          let body = null
          if (errRes.bodyJson) {
            try {
              body = JSON.parse(errRes.bodyJson)
            } catch {
              body = errRes.bodyJson
            }
          }
          return {
            errResCode: errRes.errResCode,
            status: errRes.status,
            headers: errRes.headers || {},
            body: body
          }
        }),
        checkItemList: (handlerCheckInfoData.checkItemList || []).map(item => ({
          checkExpression: item.checkExpression,
          errResCode: item.errResCode,
          errMsgFillParam: item.errMsgFillParam || []
        }))
      }
    }

    const data = {
      handlerId: formData.handlerId,
      name: formData.name,
      projectId: formData.projectId,
      requestUri: formData.requestUri,
      httpMethods: formData.httpMethods,
      label: formData.label,
      delayTime: formData.delayTime,
      enableStatus: formData.enableStatus,
      checkInfo,
      customizeSpace,
      responses,
      tasks,
      pluginInfos
    }

    jsonContent.value = JSON.stringify(data, null, 2)
    jsonError.value = ''
  } catch (error) {
    jsonError.value = `转换错误: ${error.message}`
  }
}

function syncJsonToForm() {
  try {
    const data = JSON.parse(jsonContent.value)

    Object.assign(formData, {
      handlerId: data.handlerId,
      name: data.name || '',
      projectId: data.projectId,
      requestUri: data.requestUri || '',
      httpMethods: data.httpMethods || [],
      label: data.label || '',
      delayTime: data.delayTime || 0,
      enableStatus: data.enableStatus ?? 1,
      checkInfo: data.checkInfo || null,
      customizeSpace: data.customizeSpace || {},
      responses: processResponsesFromServer(data.responses || []),
      tasks: processTasksFromServer(data.tasks || []),
      pluginInfos: processPluginsFromServer(data.pluginInfos || [])
    })

    // Handler级别的checkInfo
    Object.assign(handlerCheckInfoData, processCheckInfo(data.checkInfo))

    // 转换 customizeSpace 为列表
    customizeSpaceList.value = Object.entries(formData.customizeSpace).map(([key, value]) => ({
      key,
      value: typeof value === 'object' ? JSON.stringify(value) : String(value)
    }))

    jsonError.value = ''
  } catch (error) {
    jsonError.value = `JSON格式错误: ${error.message}`
  }
}

// CustomizeSpace 操作
function addCustomizeParam() {
  customizeSpaceList.value.push({ key: '', value: '' })
}

function removeCustomizeParam(index) {
  customizeSpaceList.value.splice(index, 1)
}

// Response 操作
function addResponse() {
  formData.responses.push({
    responseId: null,
    name: `响应 ${formData.responses.length + 1}`,
    enableStatus: 1,
    support: [],
    _supportExpanded: false,
    _syntaxExpanded: false,
    _checkExpanded: false,
    checkInfoData: { errResList: [], checkItemList: [] },
    delayTime: 0,
    response: {
      status: 200,
      headers: {},
      body: {}
    },
    bodyJson: JSON.stringify({ code: 200, msg: 'success', data: {} }, null, 2)
  })
  activeResponseIndex.value = formData.responses.length - 1
}

function removeResponse(index) {
  formData.responses.splice(index, 1)
  if (activeResponseIndex.value >= formData.responses.length) {
    activeResponseIndex.value = formData.responses.length - 1
  }
  ElMessage.success('删除成功')
}

function moveResponseUp(index) {
  if (index === 0) return
  const temp = formData.responses[index]
  formData.responses[index] = formData.responses[index - 1]
  formData.responses[index - 1] = temp
  // 如果当前展开的是被移动的项，更新展开索引
  if (activeResponseIndex.value === index) {
    activeResponseIndex.value = index - 1
  } else if (activeResponseIndex.value === index - 1) {
    activeResponseIndex.value = index
  }
}

function moveResponseDown(index) {
  if (index === formData.responses.length - 1) return
  const temp = formData.responses[index]
  formData.responses[index] = formData.responses[index + 1]
  formData.responses[index + 1] = temp
  // 如果当前展开的是被移动的项，更新展开索引
  if (activeResponseIndex.value === index) {
    activeResponseIndex.value = index + 1
  } else if (activeResponseIndex.value === index + 1) {
    activeResponseIndex.value = index
  }
}

function toggleResponse(index) {
  activeResponseIndex.value = activeResponseIndex.value === index ? -1 : index
}

function addResponseSupport(response) {
  if (!response.support) {
    response.support = []
  }
  response.support.push('')
  response._supportExpanded = true
}

function removeResponseSupport(response, index) {
  if (response.support) {
    response.support.splice(index, 1)
  }
}

// 校验配置操作
function addErrorResponse(response) {
  if (!response.checkInfoData) {
    response.checkInfoData = { errResList: [], checkItemList: [] }
  }
  if (!response.checkInfoData.errResList) {
    response.checkInfoData.errResList = []
  }
  response.checkInfoData.errResList.push({
    errResCode: `ERR_${String(response.checkInfoData.errResList.length + 1).padStart(3, '0')}`,
    status: 400,
    headers: {},
    body: {},
    bodyJson: JSON.stringify({ code: 400, msg: '请求校验失败' }, null, 2),
    _expanded: true
  })
}

function removeErrorResponse(response, index) {
  if (response.checkInfoData && response.checkInfoData.errResList) {
    response.checkInfoData.errResList.splice(index, 1)
  }
}

// Handler级别校验配置操作
function addHandlerErrorResponse() {
  if (!handlerCheckInfoData.errResList) {
    handlerCheckInfoData.errResList = []
  }
  handlerCheckInfoData.errResList.push({
    errResCode: `ERR_${String(handlerCheckInfoData.errResList.length + 1).padStart(3, '0')}`,
    status: 400,
    headers: {},
    body: {},
    bodyJson: JSON.stringify({ code: 400, msg: '请求校验失败' }, null, 2),
    _expanded: true
  })
}

function removeHandlerErrorResponse(index) {
  if (handlerCheckInfoData.errResList) {
    handlerCheckInfoData.errResList.splice(index, 1)
  }
}

function addHandlerCheckItem() {
  if (!handlerCheckInfoData.checkItemList) {
    handlerCheckInfoData.checkItemList = []
  }
  handlerCheckInfoData.checkItemList.push({
    checkExpression: '',
    errResCode: '',
    errMsgFillParam: [],
    _addingParam: false,
    _newParam: '',
    _expanded: true
  })
}

function removeHandlerCheckItem(index) {
  if (handlerCheckInfoData.checkItemList) {
    handlerCheckInfoData.checkItemList.splice(index, 1)
  }
}

function getErrResHeadersList(errRes) {
  if (!errRes.headers) {
    errRes.headers = {}
  }

  const list = []
  for (const [key, values] of Object.entries(errRes.headers)) {
    list.push({
      key,
      value: Array.isArray(values) ? values.join(', ') : values
    })
  }

  errRes._headersList = list
  return list
}

function addErrResHeader(errRes) {
  if (!errRes._headersList) {
    errRes._headersList = []
  }
  errRes._headersList.push({ key: '', value: '' })
}

function removeErrResHeader(errRes, index) {
  if (errRes._headersList) {
    const header = errRes._headersList[index]
    if (header.key && errRes.headers[header.key]) {
      delete errRes.headers[header.key]
    }
    errRes._headersList.splice(index, 1)
  }
}

function addCheckItem(response) {
  if (!response.checkInfoData) {
    response.checkInfoData = { errResList: [], checkItemList: [] }
  }
  if (!response.checkInfoData.checkItemList) {
    response.checkInfoData.checkItemList = []
  }
  response.checkInfoData.checkItemList.push({
    checkExpression: '',
    errResCode: '',
    errMsgFillParam: [],
    _addingParam: false,
    _newParam: '',
    _expanded: true
  })
}

function removeCheckItem(response, index) {
  if (response.checkInfoData && response.checkInfoData.checkItemList) {
    response.checkInfoData.checkItemList.splice(index, 1)
  }
}

function addErrMsgParam(checkItem) {
  checkItem._addingParam = true
  checkItem._newParam = ''
}

function confirmAddErrMsgParam(checkItem) {
  if (checkItem._newParam && checkItem._newParam.trim()) {
    if (!checkItem.errMsgFillParam) {
      checkItem.errMsgFillParam = []
    }
    checkItem.errMsgFillParam.push(checkItem._newParam.trim())
  }
  checkItem._addingParam = false
  checkItem._newParam = ''
}

function cancelAddErrMsgParam(checkItem) {
  setTimeout(() => {
    checkItem._addingParam = false
    checkItem._newParam = ''
  }, 200)
}

function removeErrMsgParam(checkItem, index) {
  if (checkItem.errMsgFillParam) {
    checkItem.errMsgFillParam.splice(index, 1)
  }
}

function getHeadersList(response) {
  if (!response.headers) {
    response.headers = {}
  }

  const list = []
  for (const [key, values] of Object.entries(response.headers)) {
    list.push({
      key,
      value: Array.isArray(values) ? values.join(', ') : values
    })
  }

  // 确保响应式
  response._headersList = list
  return list
}

function addHeader(response) {
  if (!response._headersList) {
    response._headersList = []
  }
  response._headersList.push({ key: '', value: '' })
}

function removeHeader(response, index) {
  if (response._headersList) {
    const header = response._headersList[index]
    if (header.key && response.headers[header.key]) {
      delete response.headers[header.key]
    }
    response._headersList.splice(index, 1)
  }
}

// Task 操作
function addTask() {
  formData.tasks.push({
    taskId: null,
    name: `任务 ${formData.tasks.length + 1}`,
    enableStatus: 1,
    support: [],
    _supportExpanded: false,
    _syntaxExpanded: false,
    async: false,
    cron: '0 0 0 * * ?',
    numberOfExecute: 1,
    request: {
      requestUrl: '',
      httpMethod: 'POST',
      headers: {},
      body: null,
      uriVariables: {}
    },
    requestBodyJson: ''
  })
  activeTaskIndex.value = formData.tasks.length - 1
}

function removeTask(index) {
  formData.tasks.splice(index, 1)
  if (activeTaskIndex.value >= formData.tasks.length) {
    activeTaskIndex.value = formData.tasks.length - 1
  }
  ElMessage.success('删除成功')
}

function toggleTask(index) {
  activeTaskIndex.value = activeTaskIndex.value === index ? -1 : index
}

function addTaskSupport(task) {
  if (!task.support) {
    task.support = []
  }
  task.support.push('')
  task._supportExpanded = true
}

function removeTaskSupport(task, index) {
  if (task.support) {
    task.support.splice(index, 1)
  }
}

function getTaskHeadersList(request) {
  if (!request.headers) {
    request.headers = {}
  }

  const list = []
  for (const [key, values] of Object.entries(request.headers)) {
    list.push({
      key,
      value: Array.isArray(values) ? values.join(', ') : values
    })
  }

  request._headersList = list
  return list
}

function addTaskHeader(request) {
  if (!request._headersList) {
    request._headersList = []
  }
  request._headersList.push({ key: '', value: '' })
}

function removeTaskHeader(request, index) {
  if (request._headersList) {
    const header = request._headersList[index]
    if (header.key && request.headers[header.key]) {
      delete request.headers[header.key]
    }
    request._headersList.splice(index, 1)
  }
}

function getTaskUriVariablesList(request) {
  if (!request.uriVariables) {
    request.uriVariables = {}
  }

  const list = []
  for (const [key, values] of Object.entries(request.uriVariables)) {
    list.push({
      key,
      value: Array.isArray(values) ? values.join(', ') : values
    })
  }

  request._uriVariablesList = list
  return list
}

function addTaskUriVariable(request) {
  if (!request._uriVariablesList) {
    request._uriVariablesList = []
  }
  request._uriVariablesList.push({ key: '', value: '' })
}

function removeTaskUriVariable(request, index) {
  if (request._uriVariablesList) {
    const variable = request._uriVariablesList[index]
    if (variable.key && request.uriVariables[variable.key]) {
      delete request.uriVariables[variable.key]
    }
    request._uriVariablesList.splice(index, 1)
  }
}

// Plugin 操作
function addPlugin() {
  formData.pluginInfos.push({
    pluginCode: '',
    enableStatus: 1,
    pluginParam: {},
    pluginParamJson: ''
  })
  activePluginIndex.value = formData.pluginInfos.length - 1
}

function removePlugin(index) {
  formData.pluginInfos.splice(index, 1)
  if (activePluginIndex.value >= formData.pluginInfos.length) {
    activePluginIndex.value = formData.pluginInfos.length - 1
  }
  ElMessage.success('删除成功')
}

function togglePlugin(index) {
  activePluginIndex.value = activePluginIndex.value === index ? -1 : index
}

function getSelectedPluginInfo(plugin) {
  if (!plugin.pluginCode) return null
  return pluginList.value.find(p => p.pluginCode === plugin.pluginCode)
}

function handlePluginChange(plugin) {
  const selectedPlugin = getSelectedPluginInfo(plugin)
  if (selectedPlugin && selectedPlugin.metadata && selectedPlugin.metadata.pluginParamRuleList) {
    // 可以根据插件元数据初始化参数结构
    // 暂时保持用户输入的参数
  }
}

// JSON 格式化函数
function formatJson(type, obj, field) {
  try {
    const jsonStr = obj[field]
    if (!jsonStr || !jsonStr.trim()) {
      ElMessage.warning('JSON 内容为空')
      return
    }
    // 尝试解析并格式化
    const parsed = JSON.parse(jsonStr)
    obj[field] = JSON.stringify(parsed, null, 2)
    ElMessage.success('格式化成功')
  } catch (error) {
    ElMessage.error('JSON 格式错误，无法格式化：' + error.message)
  }
}

// 打开全屏编辑
function openFullscreenEdit(type, obj, field, title) {
  fullscreenEditContext.value = { type, obj, field }
  fullscreenEditContent.value = obj[field] || ''
  fullscreenEditTitle.value = title
  fullscreenEditVisible.value = true
}

// 保存全屏编辑
function saveFullscreenEdit() {
  const context = fullscreenEditContext.value
  if (context && context.obj && context.field) {
    context.obj[context.field] = fullscreenEditContent.value
    ElMessage.success('保存成功')
    fullscreenEditVisible.value = false
  }
}

// 全屏编辑中的格式化
function formatFullscreenJson() {
  try {
    const jsonStr = fullscreenEditContent.value
    if (!jsonStr || !jsonStr.trim()) {
      ElMessage.warning('JSON 内容为空')
      return
    }
    // 尝试解析并格式化
    const parsed = JSON.parse(jsonStr)
    fullscreenEditContent.value = JSON.stringify(parsed, null, 2)
    ElMessage.success('格式化成功')
  } catch (error) {
    ElMessage.error('JSON 格式错误，无法格式化：' + error.message)
  }
}

// 浏览模式双击切换到表单模式并聚焦
function handleBrowseDoubleClick(section, index = null, field = null, exprIndex = null) {
  focusTarget.value = { section, index, field, exprIndex }
  editMode.value = 'form'
  showBackToBrowse.value = true // 显示返回浏览模式按钮

  // 如果是响应/任务/插件，需要展开对应的项
  if (section === 'response' && index !== null) {
    activeResponseIndex.value = index
  } else if (section === 'task' && index !== null) {
    activeTaskIndex.value = index
  } else if (section === 'plugin' && index !== null) {
    activePluginIndex.value = index
  }

  // 等待DOM更新后尝试聚焦
  nextTick(() => {
    tryFocusElement(section, index, field, exprIndex)
  })
}

// 返回浏览模式
function backToBrowseMode() {
  editMode.value = 'browse'
  showBackToBrowse.value = false
}

// 尝试聚焦到对应的元素
function tryFocusElement(section, index, field, exprIndex = null) {
  let selector = null

  // 根据不同的section和field构建选择器
  switch (section) {
    case 'basic':
      // 基础信息字段
      if (field === 'name') {
        selector = 'input[placeholder="请输入处理器名称"]'
      } else if (field === 'requestUri') {
        selector = 'input[placeholder="请输入请求URI，如：/api/user/info"]'
      }
      break
    case 'response':
      // 响应相关
      if (index !== null) {
        if (field === 'name') {
          // 聚焦到响应名称输入框
          setTimeout(() => {
            const nameInputs = document.querySelectorAll('input[placeholder="请输入响应名称"]')
            if (nameInputs[index]) {
              nameInputs[index].focus()
              nameInputs[index].select()
            }
          }, 100)
          return
        } else if (field === 'bodyJson') {
          // 聚焦到响应体JSON输入框 - 需要在展开的响应卡片中查找
          setTimeout(() => {
            // 先展开对应的响应卡片（已在handleBrowseDoubleClick中处理）
            // 查找所有响应体textarea，取对应索引的
            const bodyTextareas = document.querySelectorAll('textarea[placeholder="请输入JSON格式的响应体"]')
            if (bodyTextareas[index]) {
              bodyTextareas[index].focus()
              bodyTextareas[index].select()
            }
          }, 200)
          return
        } else if (field === 'support' && exprIndex !== null) {
          // 聚焦到响应的期望表达式输入框
          setTimeout(() => {
            // 需要展开期望表达式配置区域
            const response = formData.responses[index]
            if (response) {
              response._supportExpanded = true
              // 等待DOM更新后再查找元素
              nextTick(() => {
                // 查找所有期望表达式的textarea
                // 由于期望表达式在每个响应卡片内部，需要更精确的定位
                // 这里使用一个变通方法：通过placeholder来查找
                const allSupportTextareas = document.querySelectorAll('textarea[placeholder*="请输入完整的表达式"]')
                // 计算当前响应的期望表达式textarea的起始位置
                let startIndex = 0
                for (let i = 0; i < index; i++) {
                  startIndex += (formData.responses[i].support || []).length
                }
                const targetTextarea = allSupportTextareas[startIndex + exprIndex]
                if (targetTextarea) {
                  targetTextarea.focus()
                  targetTextarea.select()
                }
              })
            }
          }, 200)
          return
        }
      }
      break
    case 'task':
      // 任务相关
      if (index !== null) {
        if (field === 'name') {
          setTimeout(() => {
            const nameInputs = document.querySelectorAll('input[placeholder="请输入任务名称"]')
            if (nameInputs[index]) {
              nameInputs[index].focus()
              nameInputs[index].select()
            }
          }, 100)
          return
        } else if (field === 'requestBodyJson') {
          // 聚焦到任务请求体JSON输入框
          setTimeout(() => {
            const bodyTextareas = document.querySelectorAll('textarea[placeholder="请输入JSON格式的请求体"]')
            if (bodyTextareas[index]) {
              bodyTextareas[index].focus()
              bodyTextareas[index].select()
            }
          }, 200)
          return
        } else if (field === 'support' && exprIndex !== null) {
          // 聚焦到任务的期望表达式输入框
          setTimeout(() => {
            // 需要展开期望表达式配置区域
            const task = formData.tasks[index]
            if (task) {
              task._supportExpanded = true
              // 等待DOM更新后再查找元素
              nextTick(() => {
                // 查找所有期望表达式的textarea（需要排除响应的）
                const allSupportTextareas = document.querySelectorAll('textarea[placeholder*="请输入完整的表达式"]')
                // 计算当前任务的期望表达式textarea的起始位置
                // 先计算所有响应的期望表达式总数
                let startIndex = formData.responses.reduce((sum, resp) => sum + (resp.support || []).length, 0)
                // 再加上前面任务的期望表达式总数
                for (let i = 0; i < index; i++) {
                  startIndex += (formData.tasks[i].support || []).length
                }
                const targetTextarea = allSupportTextareas[startIndex + exprIndex]
                if (targetTextarea) {
                  targetTextarea.focus()
                  targetTextarea.select()
                }
              })
            }
          }, 200)
          return
        }
      }
      break
    case 'plugin':
      // 插件相关
      if (index !== null) {
        if (field === 'pluginCode') {
          // 聚焦到插件编码选择框
          setTimeout(() => {
            // 插件选择框较难通过索引定位，暂时聚焦到插件卡片的第一个输入元素
            const pluginSelects = document.querySelectorAll('.el-select input[placeholder="请选择插件"]')
            if (pluginSelects[index]) {
              pluginSelects[index].focus()
            }
          }, 200)
          return
        } else if (field === 'pluginParamJson') {
          // 聚焦到插件参数JSON输入框
          setTimeout(() => {
            const paramTextareas = document.querySelectorAll('textarea[placeholder*="请输入插件参数JSON"]')
            if (paramTextareas[index]) {
              paramTextareas[index].focus()
              paramTextareas[index].select()
            }
          }, 200)
          return
        }
      }
      break
  }

  // 尝试通过选择器聚焦
  if (selector) {
    const element = document.querySelector(selector)
    if (element) {
      element.focus()
      // 如果是输入框，选中所有文本
      if (element.select) {
        element.select()
      }
    }
  }
}

async function handleSave() {
  // 验证Handler级别必填字段
  if (!formData.name) {
    ElMessage.warning('请输入处理器名称')
    editMode.value = 'form'
    return
  }
  if (!formData.projectId) {
    ElMessage.warning('请选择所属项目')
    editMode.value = 'form'
    return
  }
  if (!formData.requestUri) {
    ElMessage.warning('请输入请求URI')
    editMode.value = 'form'
    return
  }
  if (!formData.httpMethods || formData.httpMethods.length === 0) {
    ElMessage.warning('请选择至少一个HTTP方法')
    editMode.value = 'form'
    return
  }
  if (formData.delayTime === null || formData.delayTime === undefined) {
    ElMessage.warning('请输入延迟时间')
    editMode.value = 'form'
    return
  }

  // 验证Response必填字段
  for (let i = 0; i < formData.responses.length; i++) {
    const response = formData.responses[i]
    if (!response.name) {
      ElMessage.warning(`响应 #${i + 1} 缺少名称`)
      editMode.value = 'form'
      activeResponseIndex.value = i
      return
    }
    if (response.delayTime === null || response.delayTime === undefined) {
      ElMessage.warning(`响应【${response.name}】缺少延迟时间`)
      editMode.value = 'form'
      activeResponseIndex.value = i
      return
    }
    if (!response.response) {
      ElMessage.warning(`响应【${response.name}】缺少响应内容`)
      editMode.value = 'form'
      activeResponseIndex.value = i
      return
    }
  }

  // 验证Task必填字段
  for (let i = 0; i < formData.tasks.length; i++) {
    const task = formData.tasks[i]
    if (!task.name) {
      ElMessage.warning(`任务 #${i + 1} 缺少名称`)
      editMode.value = 'form'
      activeTaskIndex.value = i
      return
    }
    if (task.async === null || task.async === undefined) {
      ElMessage.warning(`任务【${task.name}】缺少执行方式`)
      editMode.value = 'form'
      activeTaskIndex.value = i
      return
    }
    if (!task.cron) {
      ElMessage.warning(`任务【${task.name}】缺少Cron表达式`)
      editMode.value = 'form'
      activeTaskIndex.value = i
      return
    }
    if (!task.numberOfExecute || task.numberOfExecute < 1) {
      ElMessage.warning(`任务【${task.name}】的执行次数必须大于0`)
      editMode.value = 'form'
      activeTaskIndex.value = i
      return
    }
    if (!task.request) {
      ElMessage.warning(`任务【${task.name}】缺少请求信息`)
      editMode.value = 'form'
      activeTaskIndex.value = i
      return
    }
  }

  // 验证Plugin必填字段
  for (let i = 0; i < formData.pluginInfos.length; i++) {
    const plugin = formData.pluginInfos[i]
    if (!plugin.pluginCode) {
      ElMessage.warning(`插件 #${i + 1} 缺少插件编码`)
      editMode.value = 'form'
      activePluginIndex.value = i
      return
    }
  }

  // 记住保存前的启用状态
  const wasEnabled = formData.enableStatus === 1

  try {
    saveLoading.value = true

    // 如果在JSON模式，先同步到表单
    if (editMode.value === 'json') {
      syncJsonToForm()
      if (jsonError.value) {
        ElMessage.warning('JSON配置格式不正确，请检查')
        return
      }
    }

    // 构建保存数据
    const customizeSpace = {}
    customizeSpaceList.value.forEach(item => {
      if (item.key) {
        try {
          customizeSpace[item.key] = JSON.parse(item.value)
        } catch {
          customizeSpace[item.key] = item.value
        }
      }
    })

    // 处理 responses
    const responses = formData.responses.map(resp => {
      // 转换 headers list 回 map
      if (resp._headersList) {
        resp.response.headers = {}
        resp._headersList.forEach(header => {
          if (header.key) {
            resp.response.headers[header.key] = [header.value]
          }
        })
      }

      // 解析 bodyJson
      if (resp.bodyJson) {
        try {
          resp.response.body = JSON.parse(resp.bodyJson)
        } catch {
          resp.response.body = resp.bodyJson
        }
      }

      // 转换 checkInfoData 为 checkInfo
      let checkInfo = null
      if (resp.checkInfoData && (resp.checkInfoData.errResList.length > 0 || resp.checkInfoData.checkItemList.length > 0)) {
        checkInfo = {
          errResList: resp.checkInfoData.errResList.map(errRes => {
            // 转换 headers list 回 map
            if (errRes._headersList) {
              errRes.headers = {}
              errRes._headersList.forEach(header => {
                if (header.key) {
                  errRes.headers[header.key] = [header.value]
                }
              })
            }

            let body = null
            if (errRes.bodyJson) {
              try {
                body = JSON.parse(errRes.bodyJson)
              } catch {
                body = errRes.bodyJson
              }
            }
            return {
              errResCode: errRes.errResCode,
              status: errRes.status,
              headers: errRes.headers || {},
              body: body
            }
          }),
          checkItemList: resp.checkInfoData.checkItemList.map(item => ({
            checkExpression: item.checkExpression,
            errResCode: item.errResCode,
            errMsgFillParam: item.errMsgFillParam || []
          }))
        }
      }

      return {
        responseId: resp.responseId,
        name: resp.name,
        enableStatus: resp.enableStatus,
        support: resp.support || [],
        delayTime: resp.delayTime || 0,
        checkInfo: checkInfo,
        response: resp.response
      }
    })

    // 处理 tasks
    const tasks = formData.tasks.map(task => {
      // 转换 headers list 回 map
      if (task.request._headersList) {
        task.request.headers = {}
        task.request._headersList.forEach(header => {
          if (header.key) {
            task.request.headers[header.key] = [header.value]
          }
        })
      }

      // 转换 uriVariables list 回 map
      if (task.request._uriVariablesList) {
        task.request.uriVariables = {}
        task.request._uriVariablesList.forEach(variable => {
          if (variable.key) {
            task.request.uriVariables[variable.key] = [variable.value]
          }
        })
      }

      // 解析 requestBodyJson
      if (task.requestBodyJson) {
        try {
          task.request.body = JSON.parse(task.requestBodyJson)
        } catch {
          task.request.body = task.requestBodyJson
        }
      }

      return {
        taskId: task.taskId,
        name: task.name,
        enableStatus: task.enableStatus,
        support: task.support || [],
        async: task.async,
        cron: task.cron,
        numberOfExecute: task.numberOfExecute,
        request: task.request
      }
    })

    // 处理 plugins
    const pluginInfos = formData.pluginInfos.map(plugin => {
      let pluginParam = {}
      if (plugin.pluginParamJson) {
        try {
          pluginParam = JSON.parse(plugin.pluginParamJson)
        } catch (e) {
          console.warn('Failed to parse pluginParamJson:', e)
        }
      }

      return {
        pluginCode: plugin.pluginCode,
        enableStatus: plugin.enableStatus,
        pluginParam: pluginParam
      }
    })

    // 解析 Handler级别的checkInfo
    let handlerCheckInfo = null
    if (handlerCheckInfoData && (handlerCheckInfoData.errResList?.length > 0 || handlerCheckInfoData.checkItemList?.length > 0)) {
      handlerCheckInfo = {
        errResList: (handlerCheckInfoData.errResList || []).map(errRes => {
          // 转换 headers list 回 map
          if (errRes._headersList) {
            errRes.headers = {}
            errRes._headersList.forEach(header => {
              if (header.key) {
                errRes.headers[header.key] = [header.value]
              }
            })
          }

          let body = null
          if (errRes.bodyJson) {
            try {
              body = JSON.parse(errRes.bodyJson)
            } catch {
              body = errRes.bodyJson
            }
          }
          return {
            errResCode: errRes.errResCode,
            status: errRes.status,
            headers: errRes.headers || {},
            body: body
          }
        }),
        checkItemList: (handlerCheckInfoData.checkItemList || []).map(item => ({
          checkExpression: item.checkExpression,
          errResCode: item.errResCode,
          errMsgFillParam: item.errMsgFillParam || []
        }))
      }
    }

    const saveData = {
      handlerId: formData.handlerId,
      name: formData.name,
      projectId: formData.projectId,
      requestUri: formData.requestUri,
      httpMethods: formData.httpMethods,
      label: formData.label,
      delayTime: formData.delayTime,
      enableStatus: formData.enableStatus,
      checkInfo: handlerCheckInfo,
      customizeSpace,
      responses,
      tasks,
      pluginInfos
    }

    // 保存处理器
    const result = await saveMockHandler(saveData)

    // 获取保存后的handlerId（用于新增时）
    const savedHandlerId = result?.handlerId || formData.handlerId

    // 如果保存前是启用状态，自动触发启用接口
    if (wasEnabled && savedHandlerId) {
      try {
        await updateHandlerEnableStatus({
          handlerId: savedHandlerId,
          enableStatus: 1
        })
        ElMessage.success('保存成功，处理器已自动启用')
      } catch (enableError) {
        console.error('Enable handler error:', enableError)
        ElMessage.warning('保存成功，但启用失败，请手动启用处理器')
      }
    } else {
      ElMessage.success('保存成功')
    }

    router.push('/mock-handler/list')
  } catch (error) {
    console.error('Save handler error:', error)
    ElMessage.error('保存失败')
  } finally {
    saveLoading.value = false
  }
}

// DSL文档相关函数
async function showDslDoc() {
  dslDocVisible.value = true
  if (!dslDocData.value) {
    await loadDslDocument()
  }
  // 如果是函数文档tab且函数列表为空，则加载函数列表
  if (activeDocTab.value === 'function' && functionList.value.length === 0) {
    await loadFunctionList()
  }
}

// 监听tab切换，懒加载函数列表
watch(activeDocTab, async (newTab) => {
  if (newTab === 'function' && functionList.value.length === 0) {
    await loadFunctionList()
  }
})

async function loadDslDocument() {
  try {
    dslDocLoading.value = true
    const result = await getDslDocument()
    console.log('DSL document result:', result)
    // axios 拦截器已经解包了 Result，直接使用 result.sections
    dslDocData.value = result.sections || result.data?.sections || []
    console.log('DSL sections loaded:', dslDocData.value.length, 'sections')
  } catch (error) {
    console.error('Load DSL document error:', error)
    ElMessage.error('加载文档失败')
  } finally {
    dslDocLoading.value = false
  }
}

async function loadFunctionList() {
  try {
    functionDocLoading.value = true
    // 传递空对象作为请求体，满足后端 @RequestBody 要求
    const result = await queryFunctions({})
    console.log('Function list result:', result)
    // 后端返回的字段名是 list，而不是 pageData
    functionList.value = result.list || result.pageData || []
    console.log('Function list loaded:', functionList.value.length, 'functions')
  } catch (error) {
    console.error('Load function list error:', error)
    ElMessage.error('加载函数列表失败')
  } finally {
    functionDocLoading.value = false
  }
}

async function copyFunctionExample(example) {
  try {
    await navigator.clipboard.writeText(example)
    ElMessage.success('示例代码已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败，请手动复制')
  }
}

// 过滤函数列表
const filteredFunctionList = computed(() => {
  if (!functionList.value) return []
  if (!functionSearchKeyword.value) return functionList.value

  const keyword = functionSearchKeyword.value.toLowerCase()

  return functionList.value.filter(func => {
    return (
      func.functionName?.toLowerCase().includes(keyword) ||
      func.description?.toLowerCase().includes(keyword) ||
      func.example?.toLowerCase().includes(keyword)
    )
  })
})

// 过滤文档内容
const filteredDslDocData = computed(() => {
  if (!dslDocData.value) return []
  if (!dslDocSearchKeyword.value) return dslDocData.value

  const keyword = dslDocSearchKeyword.value.toLowerCase()

  return dslDocData.value
    .map(section => {
      // 检查章节标题或描述是否匹配
      const sectionMatches =
        section.title?.toLowerCase().includes(keyword) ||
        section.description?.toLowerCase().includes(keyword)

      // 过滤子章节
      const filteredSubSections = section.subSections?.filter(subSection => {
        // 检查子章节标题是否匹配
        if (subSection.title?.toLowerCase().includes(keyword)) return true

        // 检查内容列表是否匹配
        if (subSection.content?.some(item => item.toLowerCase().includes(keyword))) return true

        // 检查示例是否匹配
        if (subSection.examples?.some(example => example.toLowerCase().includes(keyword))) return true

        return false
      }) || []

      // 如果章节匹配或有匹配的子章节，保留该章节
      if (sectionMatches || filteredSubSections.length > 0) {
        return {
          ...section,
          subSections: sectionMatches ? section.subSections : filteredSubSections
        }
      }

      return null
    })
    .filter(section => section !== null)
})

function goBack() {
  router.back()
}
</script>

<style scoped>
.mock-handler-edit-container {
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

/* 减少响应/任务/插件卡片的内边距 */
:deep(.el-card__body) {
  padding: 15px;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 5px;
}

:deep(.el-textarea__inner) {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', 'Consolas', monospace;
  font-size: 13px;
  line-height: 1.6;
}

/* 浮动按钮组 */
.float-buttons {
  position: fixed;
  right: 40px;
  bottom: 40px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.float-button-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 56px;
  height: 56px;
}

.float-buttons .el-button {
  width: 56px !important;
  height: 56px !important;
  padding: 0 !important;
  font-size: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.float-buttons .el-button:hover {
  transform: scale(1.1);
  transition: transform 0.2s ease;
}

/* Fade 动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* DSL文档抽屉样式 */
.doc-tabs {
  height: calc(100vh - 100px);
}

.doc-tabs :deep(.el-tabs__content) {
  height: calc(100% - 55px);
  overflow-y: auto;
}

.dsl-doc-container,
.function-doc-container {
  padding: 10px 0;
  height: 100%;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.section-content {
  padding: 10px 0;
}

.section-description {
  margin: 0 0 15px 0;
  padding: 12px;
  background-color: #f0f9ff;
  border-left: 3px solid #409eff;
  border-radius: 4px;
  color: #606266;
  line-height: 1.6;
}

.sub-section {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #fafafa;
  border-radius: 6px;
}

.sub-section-title {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
  padding-bottom: 8px;
  border-bottom: 2px solid #e4e7ed;
}

.content-list {
  margin: 0;
  padding-left: 20px;
  list-style-type: none;
}

.content-list li {
  position: relative;
  margin-bottom: 8px;
  padding-left: 12px;
  color: #606266;
  line-height: 1.8;
}

.content-list li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #409eff;
  font-weight: bold;
}

.examples {
  margin-top: 15px;
  padding: 12px;
  background-color: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.examples-header {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 10px;
  font-size: 13px;
  font-weight: 600;
  color: #67c23a;
}

.example-item {
  margin-bottom: 8px;
  padding: 10px;
  background-color: #282c34;
  border-radius: 4px;
  overflow-x: auto;
}

.example-item:last-child {
  margin-bottom: 0;
}

.example-item code {
  display: block;
  color: #abb2bf;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-all;
}

/* 浏览模式可编辑元素样式 */
[style*="cursor: pointer"][style*="@dblclick"],
span[style*="cursor: pointer"],
pre[style*="cursor: pointer"],
div[style*="cursor: pointer"] {
  transition: all 0.2s ease;
  border-radius: 2px;
  padding: 2px 4px;
  margin: -2px -4px;
}

[style*="cursor: pointer"]:hover {
  background-color: #e6f7ff;
  color: #1890ff;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}

/* 函数文档样式 */
.function-list {
  margin-top: 10px;
}

.function-title {
  display: flex;
  align-items: center;
  flex: 1;
}

.function-params {
  margin-left: 8px;
  font-size: 13px;
  color: #909399;
}

.function-detail-content {
  padding: 15px 0;
}

.function-description,
.function-example {
  margin-bottom: 20px;
}

.function-description:last-child,
.function-example:last-child {
  margin-bottom: 0;
}

.detail-label {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 10px;
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
}

.detail-label .el-icon {
  font-size: 16px;
}

.detail-label span {
  flex: 1;
}

.detail-value {
  padding: 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  color: #606266;
  line-height: 1.8;
  white-space: pre-wrap;
  word-break: break-word;
}

.example-code {
  background-color: #282c34;
  border-radius: 4px;
  padding: 15px;
  overflow-x: auto;
}

.example-code pre {
  margin: 0;
  color: #abb2bf;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-all;
}
</style>
