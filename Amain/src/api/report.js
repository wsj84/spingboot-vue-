import request from '@/utils/request'

// 获取举报列表
export const getReportsApi = (page, pageSize) => 
  request.get(`/reports/query?page=${page}&pageSize=${pageSize}`)

// 处理举报 - 封禁用户
export const banUserApi = (userId) =>
  request.post(`/reports/ban/${userId}`)

// 忽略举报
export const ignoreReportApi = (reportId) =>
  request.delete(`/reports/${reportId}`) 

// 添加举报
export const addReportApi = (data) => 
  request.post('/reports', data)
