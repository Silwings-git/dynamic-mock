import request from '@/utils/request'

/**
 * 查询运行中的任务列表
 */
export function queryRunningTasks(data) {
  return request({
    url: '/dynamic-mock/mock/task/running/query',
    method: 'post',
    data
  })
}

/**
 * 取消任务
 */
export function unregisterTask(data) {
  return request({
    url: '/dynamic-mock/mock/task/running/unregister',
    method: 'post',
    data
  })
}

/**
 * 批量取消任务
 */
export function batchUnregisterTasks(data) {
  return request({
    url: '/dynamic-mock/mock/task/running/unregister/batch',
    method: 'post',
    data
  })
}

/**
 * 查询任务日志
 */
export function queryTaskLogs(data) {
  return request({
    url: '/dynamic-mock/mock/task/log/query',
    method: 'post',
    data
  })
}

/**
 * 删除任务日志
 */
export function deleteTaskLog(data) {
  return request({
    url: '/dynamic-mock/mock/task/log/del',
    method: 'post',
    data
  })
}
