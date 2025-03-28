import request from '@/utils/request'

// 添加收藏
export const addFavorite = (data) => request.post('/favorites', data)
/* 查询收藏 */
export const getFavorites = (userId, currentPage, pageSize) => request.get(`/favorites/user?userId=${parseInt(userId)}&page=${currentPage}&pageSize=${pageSize}`)
// 删除收藏
export const deleteFavorite = (id) => request.delete(`/favorites/${id}`)

// 检查是否已收藏
export const checkFavorite = (animeId, userId) => request.get(`/favorites/check?animeId=${animeId}&userId=${userId}`)