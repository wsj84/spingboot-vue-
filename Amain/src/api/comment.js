import request from "@/utils/request"

// 获取评论列表
export const queryCommentsApi = (page, pageSize, animeId, userId) => 
  request.get(`/comments/query?page=${page}&pageSize=${pageSize}&animeId=${animeId}&userId=${userId}`)

// 添加评论
export const addCommentApi = (data) => request.post('/comments', data)

// 删除评论
export const deleteCommentApi = (id) => request.delete(`/comments/${id}`)

// 搜索评论
export const searchCommentsApi = (keyword, status, page, pageSize) =>
  request.get(`/comments/search?keyword=${keyword}&status=${status}&page=${page}&pageSize=${pageSize}`)

// 获取用户评论
export const getUserCommentsApi = (userId, page, pageSize) => 
  request.get(`/comments/query?userId=${userId}&page=${page}&pageSize=${pageSize}`)
