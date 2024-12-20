import axios from "axios";

// 基本url配置
export default axios.create({
    baseURL: 'http://localhost:5050',
    timeout: 2000,
})

// 相关api 在baseURL后追加
export const api = {
    // 登录
    login: '/login',
    // 注册
    register: '/register',
    // 更新
    update: '/update',
    // 改变状态
    changeStutes: '/changeStutes',
    // 更新服务器信息
    UpdateServerMySQL: '/UpdateServerMySQL',
    // 获取服务器信息
    GetServerMySQL: '/GetServerMySQL'
}