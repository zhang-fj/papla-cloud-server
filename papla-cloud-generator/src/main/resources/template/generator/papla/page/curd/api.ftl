import request from '@/utils/request'

export function page(data) {
  return request({
    url: '${modulePath}/${className}/page',
    method: 'post',
    data
  })
}

export function save(data) {
  return request({
    url: '${modulePath}/${className}/save',
    method: 'post',
    data
  })
}

export function batchSave(data) {
  return request({
    url: '${modulePath}/${className}/batchSave',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '${modulePath}/${className}/del',
    method: 'delete',
    data: ids
  })
}

export function load(params) {
  return request({
    url: '${modulePath}/${className}/load',
    method: 'get',
    params: params
  })
}

export function download(data) {
  return request({
    url: '${modulePath}/${className}/download',
    method: 'post',
    data
  })
}

export default { page, save, del, load, batchSave, download }
