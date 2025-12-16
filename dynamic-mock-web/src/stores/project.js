import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { queryOwnProjects } from '@/api/project'

export const useProjectStore = defineStore('project', () => {
  const projectList = ref([])
  const currentProjectId = ref(null)

  // 当前项目
  const currentProject = computed(() => {
    if (!currentProjectId.value) return null
    return projectList.value.find((p) => p.projectId === currentProjectId.value)
  })

  /**
   * 加载项目列表
   */
  async function loadProjects() {
    try {
      const data = await queryOwnProjects()
      projectList.value = data || []

      // 从 localStorage 恢复当前项目
      const savedProjectId = localStorage.getItem('current_project_id')
      if (savedProjectId) {
        // 尝试在项目列表中查找保存的项目ID（需要处理类型转换）
        const savedId = isNaN(savedProjectId) ? savedProjectId : parseInt(savedProjectId)
        const project = projectList.value.find(p => p.projectId === savedId || String(p.projectId) === savedProjectId)

        if (project) {
          // 找到了，使用正确的类型
          currentProjectId.value = project.projectId
        } else if (projectList.value.length > 0) {
          // 没找到，使用第一个项目
          currentProjectId.value = projectList.value[0].projectId
          localStorage.setItem('current_project_id', projectList.value[0].projectId)
        }
      } else if (projectList.value.length > 0) {
        // 没有保存的项目ID，使用第一个
        currentProjectId.value = projectList.value[0].projectId
        localStorage.setItem('current_project_id', projectList.value[0].projectId)
      }

      return data
    } catch (error) {
      console.error('Load projects error:', error)
      throw error
    }
  }

  /**
   * 切换项目
   */
  function switchProject(projectId) {
    currentProjectId.value = projectId
    // 确保存储的是正确的类型
    localStorage.setItem('current_project_id', String(projectId))
  }

  return {
    projectList,
    currentProjectId,
    currentProject,
    loadProjects,
    switchProject
  }
})
