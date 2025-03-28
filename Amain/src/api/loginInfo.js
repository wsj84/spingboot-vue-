import request from "@/utils/request"

// 登录接口
export const loginInfo = (data) => request.post('/login', data)

// 获取当前登录用户信息
export const getCurrentUser = (token) => request.get(`/users/token?token=${token}`)

//添加用户
export const addUserApi = (data) => request.post('/register', data)
