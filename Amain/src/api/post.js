import request from '@/utils/request'

// 获取帖子列表
export const getPostsApi = (page = 1, pageSize = 10, category = '', keyword = '') => {
    return request({
      url: '/posts',
      method: 'get',
      params: {
        page,
        pageSize,
        category,
        keyword
      }
    });
  };
// 获取帖子详情
export const getPostDetailApi = (postId) => 
  request.get(`/posts/${postId}`)

// 创建帖子
export const createPostApi = (data) => 
  request.post('/posts', data)

// 更新帖子
export const updatePostApi = (data) => 
  request.put('/posts', data)

// 删除帖子
export const deletePostApi = (postId) => 
  request.delete(`/posts/${postId}`)

// 点赞帖子
export const likePostApi = (postId, userId) => 
  request.post(`/posts/${postId}/like?userId=${userId}`)

// 取消点赞
export const unlikePostApi = (postId, userId) => 
  request.delete(`/posts/${postId}/like?userId=${userId}`)

// 获取帖子评论
export const getPostCommentsApi = (postId, page = 1, pageSize = 20) => 
    request.get(`/post-comments?postId=${postId}&page=${page}&pageSize=${pageSize}`)

// 添加评论
export const addCommentApi = (postId, data) => 
  request.post(`/post-comments`, {
    postId: postId,
    userId: data.userId,
    content: data.content
  })

// 修改回复评论的 API 函数，添加 parentId 参数
export const replyCommentApi = (commentId, data) => {
    return request.post(`/post-comments`, {
      postId: data.postId,
      userId: data.userId,
      content: data.content,
      parentId: commentId, // 添加父评论ID
      replyToUsername: data.replyToUsername // 可以保留被回复用户名
    })
  }
// 删除评论
export const deleteCommentApi = (commentId) => 
  request.delete(`/post-comments/${commentId}`)

// 点赞评论
export const likeCommentApi = (commentId, userId) => 
  request.post(`/post-comments/${commentId}/like?userId=${userId}`)

// 取消评论点赞
export const unlikeCommentApi = (commentId, userId) => 
  request.delete(`/post-comments/${commentId}/like?userId=${userId}`)

// 获取用户点赞的评论列表
export const getCommentLikeStatusApi = (userId) => 
    request.get(`/post-comments/liked?userId=${userId}`)