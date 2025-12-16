import request from '@/utils/request'

/**
 * 登录
 */
export function login(data) {
  return request({
    url: '/dynamic-mock/auth/login',
    method: 'post',
    data
  })
}

/**
 * 登出
 */
export function logout() {
  return request({
    url: '/dynamic-mock/auth/logout',
    method: 'post'
  })
}
