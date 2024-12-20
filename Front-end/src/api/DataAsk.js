import request from '@/utils/request.js'

export const usePost = (url, data) => {
    return request({
        method: 'POST',
        data,
        url,
    })
}

export const useGet = (url, params) => {
    return request({
        method: 'GET',
        params,
        url
    })
}