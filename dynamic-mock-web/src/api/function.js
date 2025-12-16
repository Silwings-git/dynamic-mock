import request from '@/utils/request'

/**
 * 查询全部函数信息
 */
export function queryFunctions(data) {
  return request({
    url: '/dynamic-mock/mock/handler/function/query',
    method: 'post',
    data
  })
}
