import request from '@/utils/request'

/**
 * 保存用户
 */
export function saveUser(data) {
  return request({
    url: '/dynamic-mock/user/save',
    method: 'post',
    data
  })
}

/**
 * 删除用户
 */
export function deleteUser(data) {
  return request({
    url: '/dynamic-mock/user/del',
    method: 'post',
    data
  })
}

/**
 * 修改密码
 */
export function changePassword(data) {
  return request({
    url: '/dynamic-mock/user/changePassword',
    method: 'post',
    data
  })
}

/**
 * 分页查询用户列表
 */
export function queryUsers(data) {
  return request({
    url: '/dynamic-mock/user/query',
    method: 'post',
    data
  })
}
