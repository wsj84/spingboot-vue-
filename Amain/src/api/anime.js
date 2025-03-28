import request from "@/utils/request"
//根据动漫观看次数查询
export const queryAnimeAllRanking = () => request.get('/animes/all')
// 获取动漫列表
export const queryAnimeApi = (page, pageSize, keyword = '', categoryId = '') => 
  request.get(`/animes/query?page=${page}&pageSize=${pageSize}&keyword=${keyword}&categoryId=${categoryId}`)

// 添加动漫
export const addAnimeApi = (data) => request.post('/animes', data)

// 删除动漫
export const deleteAnimeApi = (id) => request.delete(`/animes/${id}`)

// 修改动漫
export const updateAnimeApi = (data) => request.put('/animes', data)
//根据id查找
export const queryAnimebyid = (id) => request.get(`/animes/${id}`)

// 根据分类获取随机动漫
export const getRandomAnimesByCategoryApi = (categoryId, limit, excludeId) => 
  request.get(`/animes/random?categoryId=${categoryId}&limit=${limit}&excludeId=${excludeId}`)

// 增加观看次数
export const incrementViewCountApi = (animeId) => 
  request.put(`/animes/incrementView/${animeId}`)

// 获取动漫观看次数排行榜
export const getAnimeRankingApi = (currentPage = 1, pageSize = 10) => 
  request.get(`/animes/ranking?page=${currentPage}&pageSize=${pageSize}`)