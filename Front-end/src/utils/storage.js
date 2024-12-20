// 获取会话存储值 语法： getSession('user', {})
export const getSession = (key, type) => {
    let item = JSON.parse(sessionStorage.getItem(key))
    if ((String(item) === 'null')) {
        item = type
    }
    return item
}

// 设置会话存储值
export const setSession = (key, item) => {
    // item 需要存入的数据
    return sessionStorage.setItem(key, JSON.stringify(item))
}

// 获取永久存储值
export const getLocal = (key, type) => {
    let item = JSON.parse(localStorage.getItem(key))

    if ((String(item) === 'null')) {
        item = type
    }
    return item
}

// 设置永久存储值
export const setLocal = (key, item) => {
    return localStorage.setItem(key, JSON.stringify(item))
}

// 全部删除
export const removeALLItem = () => {
    localStorage.clear()
    sessionStorage.clear()
}

// 创建Cookie
export const setCookie = (name, value, day) => {
    let date = new Date()
    date.setTime(date.getTime() + day)
    document.cookie = name + '=' + value + ';expires=' + date + '; path=/;'
}

// 获取Cookie
export const getCookie = (name) => {
    let reg = RegExp('(^| )' + name + '=([^;]*)(;|$)')
    let arr = document.cookie.match(reg)
    if (arr) {
        return arr[2]
    } else {
        return null
    }
}

// 删除Cookie
export const removeCookie = (name) => {
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
}