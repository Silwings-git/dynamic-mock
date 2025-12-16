import request from '@/utils/request'

/**
 * 保存Mock处理器
 */
export function saveMockHandler(data) {
  return request({
    url: '/dynamic-mock/mock/handler/save',
    method: 'post',
    data
  })
}

/**
 * 查询Mock处理器详情
 */
export function findMockHandler(data) {
  return request({
    url: '/dynamic-mock/mock/handler/find',
    method: 'post',
    data
  })
}

/**
 * 分页查询Mock处理器列表
 */
export function queryMockHandlers(data) {
  return request({
    url: '/dynamic-mock/mock/handler/query',
    method: 'post',
    data
  })
}

/**
 * 删除Mock处理器
 */
export function deleteMockHandler(data) {
  return request({
    url: '/dynamic-mock/mock/handler/del',
    method: 'post',
    data
  })
}

/**
 * 启用/停用Mock处理器
 */
export function updateHandlerEnableStatus(data) {
  return request({
    url: '/dynamic-mock/mock/handler/enableStatus',
    method: 'post',
    data
  })
}

/**
 * 查询当前用户的全部Handler
 */
export function queryOwnHandlers(data) {
  return request({
    url: '/dynamic-mock/mock/handler/queryOwn',
    method: 'post',
    data
  })
}

/**
 * 修改响应信息
 */
export function updateMockHandlerResponse(handlerId, data) {
  return request({
    url: `/dynamic-mock/mock/handler/response/update/${handlerId}`,
    method: 'post',
    data
  })
}

/**
 * 修改响应开关
 */
export function updateResponseEnableStatus(data) {
  return request({
    url: '/dynamic-mock/mock/handler/response/status',
    method: 'post',
    data
  })
}

/**
 * 修改任务开关
 */
export function updateTaskEnableStatus(data) {
  return request({
    url: '/dynamic-mock/mock/handler/task/status',
    method: 'post',
    data
  })
}

/**
 * 查询快照列表
 */
export function querySnapshots(data) {
  return request({
    url: '/dynamic-mock/snapshot/handler/query',
    method: 'post',
    data
  })
}

/**
 * 查询快照详情
 */
export function findSnapshot(data) {
  return request({
    url: '/dynamic-mock/snapshot/handler/find',
    method: 'post',
    data
  })
}

/**
 * 恢复快照
 */
export function recoverSnapshot(data) {
  return request({
    url: '/dynamic-mock/snapshot/handler/recover',
    method: 'post',
    data
  })
}
