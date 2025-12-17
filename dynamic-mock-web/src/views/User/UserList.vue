<template>
  <div class="user-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增用户
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户名">
          <el-input
            v-model="searchForm.username"
            placeholder="请输入用户名"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="用户账号">
          <el-input
            v-model="searchForm.userAccount"
            placeholder="请输入用户账号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="角色">
          <el-select
            v-model="searchForm.role"
            placeholder="请选择角色"
            clearable
            style="width: 160px"
          >
            <el-option label="全部" value="" />
            <el-option label="管理员" :value="1" />
            <el-option label="普通用户" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="userId" label="用户ID" width="180" />
        <el-table-column prop="username" label="用户名" />
        <el-table-column prop="userAccount" label="用户账号" />
        <el-table-column prop="role" label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="row.role === 1 ? 'danger' : 'success'">
              {{ row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permissionList" label="项目权限">
          <template #default="{ row }">
            <el-tag
              v-for="projectId in row.permissionList"
              :key="projectId"
              size="small"
              style="margin-right: 5px"
            >
              {{ getProjectName(projectId) }}
            </el-tag>
            <span v-if="!row.permissionList || row.permissionList.length === 0">-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
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
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="100px"
        autocomplete="off"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            autocomplete="off"
          />
        </el-form-item>

        <el-form-item label="用户账号" prop="userAccount">
          <el-input
            v-model="form.userAccount"
            placeholder="请输入用户账号"
            :disabled="!!form.userId"
            autocomplete="off"
          />
        </el-form-item>

        <el-form-item
          label="密码"
          :prop="form.userId ? '' : 'password'"
          :required="!form.userId"
        >
          <el-input
            v-model="form.password"
            type="password"
            :placeholder="form.userId ? '不修改请留空' : '请输入密码'"
            show-password
            autocomplete="new-password"
          />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role">
            <el-radio :label="0">普通用户</el-radio>
            <el-radio :label="1">管理员</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="项目权限" prop="permissionList">
          <el-select
            v-model="form.permissionList"
            multiple
            placeholder="请选择项目"
            style="width: 100%"
          >
            <el-option
              v-for="project in projectList"
              :key="project.projectId"
              :label="project.projectName"
              :value="project.projectId"
            />
          </el-select>
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
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryUsers, saveUser, deleteUser } from '@/api/user'
import { useProjectStore } from '@/stores/project'

const projectStore = useProjectStore()

const loading = ref(false)
const saveLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const searchForm = reactive({
  username: '',
  userAccount: '',
  role: ''
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

const form = reactive({
  userId: null,
  username: '',
  userAccount: '',
  password: '',
  role: 0,
  permissionList: []
})

const formRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  userAccount: [{ required: true, message: '请输入用户账号', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
  ],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const dialogTitle = computed(() =>
  form.userId ? '编辑用户' : '新增用户'
)

const projectList = computed(() => projectStore.projectList)

onMounted(() => {
  loadData()
  projectStore.loadProjects()
})

async function loadData() {
  try {
    loading.value = true
    const data = await queryUsers({
      username: searchForm.username,
      userAccount: searchForm.userAccount,
      role: searchForm.role,
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize
    })
    tableData.value = data.list || []
    pagination.total = data.total || 0
  } catch (error) {
    console.error('Load users error:', error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  pagination.pageNum = 1
  loadData()
}

function handleReset() {
  searchForm.username = ''
  searchForm.userAccount = ''
  searchForm.role = ''
  handleSearch()
}

function handleAdd() {
  // 先清空表单
  form.userId = null
  form.username = ''
  form.userAccount = ''
  form.password = ''
  form.role = 0
  form.permissionList = []

  dialogVisible.value = true

  // 在对话框打开后重置表单验证状态
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

function handleEdit(row) {
  form.userId = row.userId
  form.username = row.username
  form.userAccount = row.userAccount
  form.password = ''
  form.role = row.role
  form.permissionList = row.permissionList || []
  dialogVisible.value = true
}

async function handleSave() {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    saveLoading.value = true

    const data = {
      userId: form.userId,
      username: form.username,
      userAccount: form.userAccount,
      role: form.role,
      permissionList: form.permissionList
    }

    // 新增用户或修改密码时才传password
    if (!form.userId || form.password) {
      data.password = form.password
    }

    await saveUser(data)

    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('Save user error:', error)
  } finally {
    saveLoading.value = false
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户"${row.username}"吗？`,
      '警告',
      {
        type: 'warning'
      }
    )

    await deleteUser({ userId: row.userId })
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    // 取消操作
  }
}

function handleDialogClose() {
  // 重置表单验证
  formRef.value?.resetFields()

  // 清空表单数据，防止浏览器自动填充
  nextTick(() => {
    form.userId = null
    form.username = ''
    form.userAccount = ''
    form.password = ''
    form.role = 0
    form.permissionList = []
  })
}

function getProjectName(projectId) {
  const project = projectList.value.find((p) => p.projectId === projectId)
  return project ? project.projectName : projectId
}
</script>

<style scoped>
.user-list-container {
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
