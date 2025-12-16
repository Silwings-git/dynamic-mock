import request from '@/utils/request'

/**
 * 上传文本文件
 */
export function uploadTextFile(file) {
  const formData = new FormData()
  formData.append('file', file)

  return request({
    url: '/dynamic-mock/file/text/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 下载文件
 */
export function downloadTextFile(data) {
  return request({
    url: '/dynamic-mock/file/text/download',
    method: 'post',
    data,
    responseType: 'blob'
  })
}
