import request from '@/utils/request'

// 根据动漫ID获取弹幕
export const getDanmakuByAnimeId = (animeId) => 
  request.get(`/danmaku/query/${animeId}`)

// 添加弹幕
export const addDanmaku = (data) => 
  request.post('/danmaku/send', data)

// 删除弹幕
export const deleteDanmaku = (danmakuId) => 
  request.delete(`/danmaku/${danmakuId}`)
//查询根据需求弹幕
export const getDanmakuByAnimeIdAndTime = (page,pageSize,userId,animeId) => 
  request.get(`/danmaku/find?page=${page}&pageSize=${pageSize}&userId=${userId}&animeId=${animeId}`)
//查询所有弹幕
export const getDanmakuAll = (page,pageSize) => 
  request.get(`/danmaku/find?page=${page}&pageSize=${pageSize}`)
