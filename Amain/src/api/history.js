import request from '@/utils/request'

// 添加观看历史
export const addHistory = (data) => request.post('/history', data)

// 获取用户观看历史
export const getHistory = (userId, currentPage, pageSize) => request.get(`/history/user?userId=${parseInt(userId)}&page=${currentPage}&pageSize=${pageSize}`)

// 删除观看历史
export const deleteHistory = (id) => request.delete(`/history/${id}`)