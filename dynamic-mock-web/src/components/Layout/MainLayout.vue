<template>
  <el-container class="main-layout">
    <el-aside width="200px" class="sidebar">
      <Sidebar />
    </el-aside>
    <el-container>
      <el-header height="60px" class="header">
        <Header />
      </el-header>
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { onMounted } from 'vue'
import Header from './Header.vue'
import Sidebar from './Sidebar.vue'
import { useAuthStore } from '@/stores/auth'
import { useProjectStore } from '@/stores/project'

const authStore = useAuthStore()
const projectStore = useProjectStore()

onMounted(() => {
  authStore.initUserInfo()
  projectStore.loadProjects()
})
</script>

<style scoped>
.main-layout {
  height: 100%;
}

.sidebar {
  background-color: #304156;
  overflow-x: hidden;
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
