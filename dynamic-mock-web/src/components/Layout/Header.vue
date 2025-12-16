<template>
  <div class="header-container">
    <div class="header-left">
      <h2 class="logo">Dynamic-Mock</h2>
    </div>

    <div class="header-right">
      <!-- 项目选择器 -->
      <el-select
        v-model="currentProjectId"
        placeholder="选择项目"
        @change="handleProjectChange"
        class="project-selector"
      >
        <el-option
          v-for="project in projectList"
          :key="project.projectId"
          :label="project.projectName"
          :value="project.projectId"
        />
      </el-select>

      <!-- 用户下拉菜单 -->
      <el-dropdown @command="handleCommand">
        <span class="user-dropdown">
          <el-icon><User /></el-icon>
          <span>{{ user?.username || '用户' }}</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="changePassword">
              <el-icon><Lock /></el-icon>
              修改密码
            </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { useProjectStore } from '@/stores/project'

const router = useRouter()
const authStore = useAuthStore()
const projectStore = useProjectStore()

const user = computed(() => authStore.user)
const projectList = computed(() => projectStore.projectList)
const currentProjectId = computed({
  get: () => projectStore.currentProjectId,
  set: (val) => projectStore.switchProject(val)
})

function handleProjectChange(projectId) {
  projectStore.switchProject(projectId)
  const project = projectStore.projectList.find(p => p.projectId === projectId)
  ElMessage.success(`已切换至项目：${project?.projectName || projectId}`)
}

async function handleCommand(command) {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('确定要退出登录吗?', '提示', {
        type: 'warning'
      })
      await authStore.logout()
      router.push('/login')
      ElMessage.success('已退出登录')
    } catch (error) {
      // 取消操作
    }
  } else if (command === 'changePassword') {
    router.push('/user/change-password')
  }
}
</script>

<style scoped>
.header-container {
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left .logo {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.project-selector {
  width: 200px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: #f5f7fa;
}
</style>
