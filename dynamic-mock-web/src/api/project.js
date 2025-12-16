import request from '@/utils/request'

/**
 * 保存项目
 */
export function saveProject(data) {
  return request({
    url: '/dynamic-mock/project/save',
    method: 'post',
    data
  })
}

/**
 * 分页查询项目列表
 */
export function queryProjects(data) {
  return request({
    url: '/dynamic-mock/project/query',
    method: 'post',
    data
  })
}

/**
 * 查询当前用户的全部项目
 */
export function queryOwnProjects() {
  return request({
    url: '/dynamic-mock/project/queryOwnAll',
    method: 'post',
    data: {}
  })
}

/**
 * 删除项目
 */
export function deleteProject(data) {
  return request({
    url: '/dynamic-mock/project/del',
    method: 'post',
    data
  })
}
