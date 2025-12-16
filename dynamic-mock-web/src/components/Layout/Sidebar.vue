<template>
  <div class="sidebar-container">
    <el-scrollbar>
      <el-menu
        :default-active="activeMenu"
        :router="true"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/mock-handler/list">
          <el-icon><Document /></el-icon>
          <span>Mock处理器</span>
        </el-menu-item>

        <el-menu-item index="/mock-task/running">
          <el-icon><Timer /></el-icon>
          <span>运行中任务</span>
        </el-menu-item>

        <el-menu-item index="/mock-task/log">
          <el-icon><Tickets /></el-icon>
          <span>任务日志</span>
        </el-menu-item>

        <el-menu-item index="/function/list">
          <el-icon><Reading /></el-icon>
          <span>函数文档</span>
        </el-menu-item>

        <el-menu-item index="/plugin/list">
          <el-icon><Grid /></el-icon>
          <span>插件管理</span>
        </el-menu-item>

        <template v-if="isAdmin">
          <el-divider style="margin: 10px 0; border-color: #5a6c7d" />

          <el-menu-item index="/project/list">
            <el-icon><Folder /></el-icon>
            <span>项目管理</span>
          </el-menu-item>

          <el-menu-item index="/user/list">
            <el-icon><UserFilled /></el-icon>
            <span>用户管理</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const authStore = useAuthStore()

const activeMenu = computed(() => route.path)
const isAdmin = computed(() => authStore.isAdmin)
</script>

<style scoped>
.sidebar-container {
  height: 100%;
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-scrollbar__view) {
  height: 100%;
}
</style>
