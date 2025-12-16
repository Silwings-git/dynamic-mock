import request from '@/utils/request'

/**
 * 获取DSL文档
 */
export function getDslDocument() {
  return request({
    url: '/dynamic-mock/doc/dsl',
    method: 'post'
  })
}
