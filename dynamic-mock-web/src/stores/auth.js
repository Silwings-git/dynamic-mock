import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login as loginApi, logout as logoutApi } from '@/api/auth'
import { setUserInfo, clearAuth, getUserInfo } from '@/utils/auth'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(getUserInfo())
  const isAdmin = ref(false)

  /**
   * 登录
   */
  async function login(loginForm) {
    try {
      const data = await loginApi(loginForm)
      user.value = data
      // role为1表示管理员，0表示普通用户
      isAdmin.value = data.role === 1

      // 保存用户信息（认证信息在cookie中，由后端自动处理）
      setUserInfo(data)

      return data
    } catch (error) {
      throw error
    }
  }

  /**
   * 登出
   */
  async function logout() {
    try {
      await logoutApi()
    } finally {
      user.value = null
      isAdmin.value = false
      clearAuth()
    }
  }

  /**
   * 初始化用户信息
   */
  function initUserInfo() {
    const userInfo = getUserInfo()
    if (userInfo) {
      user.value = userInfo
      isAdmin.value = userInfo.role === 1
    }
  }

  return {
    user,
    isAdmin,
    login,
    logout,
    initUserInfo
  }
})
