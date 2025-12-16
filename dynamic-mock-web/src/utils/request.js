import axios from 'axios'
import { ElMessage } from 'element-plus'
import { removeUserInfo } from './auth'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/',
  timeout: 30000,
  withCredentials: true, // 支持跨域携带cookie
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // Cookie会自动携带，无需手动添加认证信息
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data

    // 后端有两种响应格式:
    // 1. Result<T>: { code, errMsg, data }
    // 2. PageResult<T>: { code, errMsg, pageData, total }
    // 注意：code是字符串类型，成功为"200"
    if (res.code !== '200') {
      ElMessage.error(res.errMsg || '请求失败')

      // 401: 未登录或认证过期
      if (res.code === '401') {
        ElMessage.error('登录已过期，请重新登录')
        removeUserInfo()
        router.push('/login')
      }

      // 403: 权限不足
      if (res.code === '403') {
        ElMessage.error('权限不足')
      }

      return Promise.reject(new Error(res.errMsg || '请求失败'))
    }

    // 判断是否为分页结果（PageResult有pageData字段）
    if (res.pageData !== undefined) {
      return {
        list: res.pageData,
        total: res.total
      }
    }

    // 普通结果（Result有data字段）
    return res.data
  },
  (error) => {
    console.error('Response error:', error)

    if (error.response) {
      switch (error.response.status) {
        case 401:
          ElMessage.error('登录已过期，请重新登录')
          removeUserInfo()
          router.push('/login')
          break
        case 403:
          ElMessage.error('权限不足')
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误')
          break
        default:
          ElMessage.error(error.message || '请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查网络连接')
    } else {
      ElMessage.error(error.message || '请求失败')
    }

    return Promise.reject(error)
  }
)

export default service
