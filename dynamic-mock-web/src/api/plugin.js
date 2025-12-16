import request from '@/utils/request'

/**
 * 查询插件列表
 */
export function queryPlugins() {
  return request({
    url: '/dynamic-mock/plugin/list',
    method: 'post',
    data: {}
  })
}
