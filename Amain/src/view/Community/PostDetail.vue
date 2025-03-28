<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  getPostDetailApi, 
  getPostCommentsApi, 
  addCommentApi, 
  replyCommentApi,
  deleteCommentApi,
  likePostApi,
  unlikePostApi,
  likeCommentApi,
  unlikeCommentApi,
  deletePostApi,
  getCommentLikeStatusApi // 添加这一行
} from '@/api/post'

const route = useRoute()
const router = useRouter()
const postId = computed(() => route.params.id)
const postDetail = ref(null)
const comments = ref([])
const loading = ref(false)
const commentContent = ref('')
const replyContent = ref('')
const replyingTo = ref(null)
const currentPage = ref(1)
const pageSize = ref(20)
const totalComments = ref(0)
const isLoggedIn = computed(() => !!localStorage.getItem('token'))
const currentUserId = computed(() => localStorage.getItem('userId'))
const commentLoading = ref(false) // 添加这一行
// 加载帖子详情
const loadPostDetail = async () => {
  loading.value = true
  try {
    const result = await getPostDetailApi(postId.value)
    if (result.code === 1) {
      postDetail.value = result.data
      
      // 判断当前用户是否已点赞
      if (postDetail.value.likes && isLoggedIn.value) {
        postDetail.value.isLiked = postDetail.value.likes.some(
          like => like.userId.toString() === currentUserId.value
        )
      }
    } else {
      ElMessage.error(result.msg || '获取帖子详情失败')
    }
  } catch (error) {
    console.error('加载帖子详情失败:', error)
    ElMessage.error('加载帖子详情失败')
  } finally {
    loading.value = false
  }
}

// 加载评论
const loadComments = async () => {
    commentLoading.value = true
  try {
    const result = await getPostCommentsApi(postId.value, currentPage.value, pageSize.value)
    
    if (result && result.code === 1) {
          // 处理评论数据
         const commentData = result.data.rows || []
        // 构建嵌套结构
        const mainComments = [];
         const replyMap = new Map(); // 用于存储回复
      
      // 第一次遍历，识别主评论和回复
      commentData.forEach(comment => {
        // 处理点赞状态
        comment.isLiked = false;
        comment.likeCount = comment.likeCount || 0;
        // 添加临时用户信息
        comment.user = {
            id: comment.userId,
            username: comment.username || `用户${comment.userId}` // 使用已有username字段或创建临时名称
        };
        
        // 初始化回复数组
        if (!comment.replies) {
          comment.replies = [];
        }
        
        // 区分主评论和回复
        if (!comment.parentId) {
          mainComments.push(comment);
        } else {
          // 将回复放入 Map 中
          if (!replyMap.has(comment.parentId)) {
            replyMap.set(comment.parentId, []);
          }
          replyMap.get(comment.parentId).push(comment);
        }
      });
      
      // 第二次遍历，将回复添加到主评论中
      mainComments.forEach(comment => {
        if (replyMap.has(comment.id)) {
          comment.replies = replyMap.get(comment.id);
        }
      });
      
      // 设置点赞状态
      if (isLoggedIn.value) {
        try {
          const likedResult = await getCommentLikeStatusApi(currentUserId.value);
          if (likedResult && likedResult.code === 1) {
            const likedCommentIds = new Set(likedResult.data || []);
            
            // 为所有评论（包括主评论和回复）设置点赞状态
            const setLikeStatus = (comment) => {
              comment.isLiked = likedCommentIds.has(comment.id);
              if (comment.replies && comment.replies.length > 0) {
                comment.replies.forEach(reply => {
                  reply.isLiked = likedCommentIds.has(reply.id);
                });
              }
            };
            
            mainComments.forEach(setLikeStatus);
          }
        } catch (e) {
          console.error('获取评论点赞状态失败:', e);
        }
      }
      
      // 更新评论列表
      comments.value = mainComments
      totalComments.value = result.data.total || 0
    } else {
      ElMessage.error(result?.msg || '获取评论失败')
    }
  } catch (error) {
    console.error('加载评论失败:', error)
    ElMessage.error('评论加载失败')
  } finally {
    commentLoading.value = false
  }
  console.log('加载的评论数据:', comments.value)
}

// 提交评论
const submitComment = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  if (!commentContent.value.trim()) {
    ElMessage.warning('评论内容不能为空')
    return
  }
  
  try {
    const result = await addCommentApi(postId.value, { content: commentContent.value,userId: parseInt(currentUserId.value) })
    if (result.code === 1) {
      ElMessage.success('评论成功')
      commentContent.value = ''
      await loadComments() // 重新加载评论
      
      // 更新帖子的评论数
      if (postDetail.value) {
        postDetail.value.commentCount = (postDetail.value.commentCount || 0) + 1
      }
    } else {
      ElMessage.error(result.msg || '评论失败')
    }
  } catch (error) {
    console.error('提交评论失败:', error)
    ElMessage.error('评论失败')
  }
  // 在 loadComments 函数末尾添加
    console.log('加载的评论数据:', comments.value)
}

// 回复评论
const openReplyForm = (comment) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  replyingTo.value = comment
  replyContent.value = ''
}

const submitReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('回复内容不能为空')
    return
  }
  
  try {
    const result = await replyCommentApi(replyingTo.value.id, {
         content: replyContent.value ,
         postId: postId.value,  // 必须添加帖子ID
         userId: parseInt(currentUserId.value), // 必须添加用户ID
         replyToUsername: replyingTo.value.user?.username // 可选
        })
    if (result.code === 1) {
      ElMessage.success('回复成功')
      replyContent.value = ''
      replyingTo.value = null
      await loadComments() // 重新加载评论
    } else {
      ElMessage.error(result.msg || '回复失败')
    }
  } catch (error) {
    console.error('提交回复失败:', error)
    ElMessage.error('回复失败')
  }
}

// 点赞帖子
const handleLikePost = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    if (postDetail.value.isLiked) {
      await unlikePostApi(postId.value, currentUserId.value)
      postDetail.value.likeCount--
    } else {
      await likePostApi(postId.value, currentUserId.value)
      postDetail.value.likeCount++
    }
    postDetail.value.isLiked = !postDetail.value.isLiked
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 点赞评论
const handleLikeComment = async (comment) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    if (comment.isLiked) {
      await unlikeCommentApi(comment.id, currentUserId.value)
      // 需要明确地更新评论对象
      comment.likeCount = Math.max((comment.likeCount || 0) - 1, 0)
      comment.isLiked = false
        // 添加视觉反馈
        ElMessage.success('已取消点赞')
    } else {
      await likeCommentApi(comment.id, currentUserId.value)
      // 需要明确地更新评论对象
      comment.likeCount = (comment.likeCount || 0) + 1
      comment.isLiked = true
        // 添加视觉反馈
        ElMessage.success('点赞成功')
    }
    
    // 添加调试信息
    console.log('评论点赞状态已更新:', comment.id, comment.isLiked, comment.likeCount)
  } catch (error) {
    console.error('评论点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 删除评论
const handleDeleteComment = async (comment) => {
  try {
    await ElMessageBox.confirm('确定要删除此评论吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const result = await deleteCommentApi(comment.id)
    if (result.code === 1) {
      ElMessage.success('删除成功')
      await loadComments() // 重新加载评论
      
      // 更新帖子的评论数
      if (postDetail.value) {
        postDetail.value.commentCount = Math.max((postDetail.value.commentCount || 0) - 1, 0)
      }
    } else {
      ElMessage.error(result.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评论失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 删除帖子
const handleDeletePost = async () => {
  try {
    await ElMessageBox.confirm('确定要删除此帖子吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const result = await deletePostApi(postId.value)
    if (result.code === 1) {
      ElMessage.success('删除成功')
      router.push('/community')
    } else {
      ElMessage.error(result.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除帖子失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 编辑帖子
const handleEditPost = () => {
  router.push(`/community/edit/${postId.value}`)
}

// 返回社区主页
const goBack = () => {
  router.push('/community')
}

// 格式化日期
const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', { 
    year: 'numeric', 
    month: '2-digit', 
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 判断是否是自己的帖子或评论
const isMyContent = (userId) => {
  return userId.toString() === currentUserId.value
}

// 处理分页
const handleCurrentChange = (page) => {
  currentPage.value = page
  loadComments()
}

onMounted(() => {
  loadPostDetail()
  loadComments()
})
</script>

<template>
  <div class="post-detail-container">
    <div class="back-button" @click="goBack">
      <i class="el-icon-arrow-left"></i> 返回社区
    </div>
    
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>
    
    <div v-else-if="!postDetail" class="not-found">
      <h2>帖子不存在或已被删除</h2>
      <el-button type="primary" @click="goBack">返回社区</el-button>
    </div>
    
    <template v-else>
      <div class="post-header">
        <div class="post-title-section">
          <h1>{{ postDetail.title }}</h1>
          <el-tag size="medium" :type="postDetail.category === '公告' ? 'danger' : 'info'">
            {{ postDetail.category }}
          </el-tag>
        </div>
        
        <div class="post-meta">
          <div class="author-info">
            <el-avatar :src="postDetail.user?.avatarUrl">
              {{ postDetail.user?.username.charAt(0) }}
            </el-avatar>
            <div>
              <div class="author-name">{{ postDetail.user?.username }}</div>
              <div class="post-time">{{ formatDate(postDetail.createdAt) }}</div>
            </div>
          </div>
          
          <div class="post-actions" v-if="isMyContent(postDetail.userId)">
            <el-button size="small" @click="handleEditPost">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDeletePost">删除</el-button>
          </div>
        </div>
      </div>
      
      <div class="post-content">
        {{ postDetail.content }}
      </div>
      
      <div class="post-stats">
        <div class="stat-item" @click="handleLikePost">
          <el-icon :class="{ 'liked': postDetail.isLiked }">
            <Star />
          </el-icon>
          <span>{{ postDetail.likeCount || 0 }} 点赞</span>
        </div>
        <div class="stat-item">
          <i class="el-icon-chat-dot-square"></i>
          <span>{{ postDetail.commentCount || 0 }} 评论</span>
        </div>
        <div class="stat-item">
          <i class="el-icon-view"></i>
          <span>{{ postDetail.viewCount || 0 }} 阅读</span>
        </div>
      </div>
      
      <div class="comments-section">
        <h2>评论 ({{ totalComments }})</h2>
        
        <div class="comment-form">
          <el-input
            v-model="commentContent"
            type="textarea"
            :rows="3"
            placeholder="写下你的评论..."
            :disabled="!isLoggedIn"
          ></el-input>
          <div class="form-actions">
            <el-button type="primary" @click="submitComment" :disabled="!isLoggedIn">
              发布评论
            </el-button>
          </div>
        </div>
        
        <div v-if="comments.length === 0" class="no-comments">
          暂无评论，快来发表第一条评论吧！
        </div>
        
        <div v-else class="comment-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-avatar">
              <el-avatar :src="comment.user?.avatarUrl">
                {{ comment.user?.username.charAt(0) }}
              </el-avatar>
            </div>
            
            <div class="comment-body">
              <div class="comment-header">
                <span class="comment-username">{{ comment.user?.username }}</span>
                <span class="comment-time">{{ formatDate(comment.createdAt) }}</span>
              </div>
              
              <div class="comment-text">{{ comment.content }}</div>
              
              <div class="comment-actions">
                <el-button 
                    size="small" 
                type="text" 
                @click.stop="handleLikeComment(comment)"
                >
                <el-icon :class="{ 'liked': comment.isLiked }">
                  <Star />
                </el-icon>
                    {{ comment.likeCount || 0 }}
                </el-button>
                <span class="action-btn" @click="openReplyForm(comment)">
                  <i class="el-icon-chat-line-square"></i> 回复
                </span>
                <span v-if="isMyContent(comment.userId)" class="action-btn delete" @click="handleDeleteComment(comment)">
                  <i class="el-icon-delete"></i> 删除
                </span>
              </div>
              
              <!-- 回复表单 -->
              <div v-if="replyingTo && replyingTo.id === comment.id" class="reply-form">
                <el-input
                  v-model="replyContent"
                  type="textarea"
                  :rows="2"
                  :placeholder="`回复 ${comment.user?.username}...`"
                ></el-input>
                <div class="form-actions">
                  <el-button size="small" @click="replyingTo = null">取消</el-button>
                  <el-button size="small" type="primary" @click="submitReply">回复</el-button>
                </div>
              </div>
              
              <!-- 评论的回复 -->
              <div v-if="comment.replies && comment.replies.length > 0" class="replies-list">
                <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                  <div class="reply-avatar">
                    <el-avatar :src="reply.user?.avatarUrl" :size="30">
                      {{ reply.user?.username.charAt(0) }}
                    </el-avatar>
                  </div>
                  
                  <div class="reply-body">
                    <div class="reply-header">
                      <span class="reply-username">{{ reply.user?.username }}</span>
                      <span class="reply-time">{{ formatDate(reply.createdAt) }}</span>
                    </div>
                    
                    <div class="reply-text">{{ reply.content }}</div>
                    
                    <div class="reply-actions">
                      <span class="action-btn" @click="handleLikeComment(reply)">
                        <el-icon :class="{ 'liked': reply.isLiked }">
                          <Star />
                        </el-icon>
                        {{ reply.likeCount || 0 }}
                      </span>
                      <span v-if="isMyContent(reply.userId)" class="action-btn delete" @click="handleDeleteComment(reply)">
                        <i class="el-icon-delete"></i> 删除
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <div class="pagination-container" v-if="totalComments > pageSize">
          <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            layout="prev, pager, next"
            :total="totalComments"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.post-detail-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9fafc;
  min-height: 100vh;
}

.back-button {
  display: inline-flex;
  align-items: center;
  color: #3498db;
  cursor: pointer;
  margin-bottom: 25px;
  font-size: 1.05rem;
  font-weight: 500;
  transition: all 0.2s;
}

.back-button:hover {
  color: #2980b9;
  transform: translateX(-5px);
}

.back-button i {
  margin-right: 6px;
  transition: all 0.2s;
}

.back-button:hover i {
  transform: translateX(-3px);
}

.post-header {
  margin-bottom: 30px;
  background: white;
  border-radius: 12px;
  padding: 25px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
}

.post-title-section {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.post-title-section h1 {
  margin: 0;
  margin-right: 15px;
  font-size: 1.9rem;
  color: #2c3e50;
  font-weight: 700;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 1px solid #eef2f7;
}

.author-info {
  display: flex;
  align-items: center;
}

.author-info .el-avatar {
  margin-right: 15px;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
  background: linear-gradient(135deg, #3498db, #2c3e50);
  color: white;
}

.author-name {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1.05rem;
}

.post-time {
  color: #8392a5;
  font-size: 0.9rem;
  margin-top: 3px;
}

.post-content {
  font-size: 1.1rem;
  line-height: 1.75;
  color: #3c4858;
  margin: 0 0 30px;
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
  white-space: pre-wrap;
}
/* 确保图标和文本垂直对齐 */
.el-icon {
  vertical-align: middle;
  margin-right: 4px;
}

/* 帖子状态项的图标样式 */
.stat-item .el-icon {
  font-size: 1.25rem;
  margin-right: 8px;
}

/* 评论和回复操作按钮图标样式 */
.action-btn .el-icon, 
.el-button .el-icon {
  margin-right: 5px;
}

.post-stats {
  display: flex;
  padding: 15px 25px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
  margin-bottom: 30px;
}

.stat-item {
  display: flex;
  align-items: center;
  margin-right: 30px;
  cursor: pointer;
  color: #8392a5;
  transition: all 0.3s;
  padding: 8px 15px;
  border-radius: 8px;
}

.stat-item:hover {
  background-color: #f5f7fa;
  color: #3498db;
}

.stat-item i {
  margin-right: 8px;
  font-size: 1.25rem;
}

.liked {
  color: #409EFF; /* Element Plus 主色调蓝色 */
}
.stat-item:hover .liked {
  color: #c0392b;
}

.comments-section {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
}

.comments-section h2 {
  font-size: 1.6rem;
  color: #2c3e50;
  margin-bottom: 25px;
  font-weight: 700;
}

.comment-form {
  margin-bottom: 35px;
}

.comment-form :deep(.el-textarea__inner) {
  border-radius: 8px;
  border-color: #dcdfe6;
  padding: 12px;
  font-size: 1.05rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}

.no-comments {
  text-align: center;
  padding: 40px 20px;
  color: #8392a5;
  background-color: #f9fafc;
  border-radius: 8px;
  font-size: 1.1rem;
}

.comment-list {
  margin-bottom: 30px;
}

.comment-item {
  display: flex;
  margin-bottom: 30px;
  background: #f9fafc;
  padding: 20px;
  border-radius: 12px;
  transition: all 0.3s;
}

.comment-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.comment-avatar {
  margin-right: 20px;
}

.comment-avatar :deep(.el-avatar) {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
  background: linear-gradient(135deg, #3498db, #2c3e50);
  color: white;
}

.comment-body {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.comment-username {
  font-weight: 600;
  margin-right: 12px;
  color: #2c3e50;
}

.comment-time {
  color: #8392a5;
  font-size: 0.9rem;
}

.comment-text {
  margin-bottom: 15px;
  font-size: 1.05rem;
  line-height: 1.6;
  color: #3c4858;
  white-space: pre-wrap;
}

.comment-actions {
  display: flex;
  gap: 20px;
}

.action-btn {
  font-size: 0.95rem;
  color: #8392a5;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: all 0.2s;
}

.action-btn i {
  margin-right: 5px;
}

.action-btn:hover {
  color: #3498db;
}

.action-btn.delete:hover {
  color: #e74c3c;
}

.reply-form {
  margin: 15px 0;
  padding: 15px 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.replies-list {
  margin-top: 20px;
  padding: 10px 0 0 20px;
  border-left: 2px solid #e6e9ed;
}

.reply-item {
  display: flex;
  margin-bottom: 20px;
  background: white;
  padding: 15px;
  border-radius: 8px;
  transition: all 0.3s;
}

.reply-item:hover {
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.06);
}

.reply-avatar {
  margin-right: 15px;
}

.reply-body {
  flex: 1;
}

.reply-header {
  display: flex;
  align-items: center;
  margin-bottom: 6px;
}

.reply-username {
  font-weight: 600;
  margin-right: 10px;
  color: #2c3e50;
  font-size: 0.95rem;
}

.reply-time {
  color: #8392a5;
  font-size: 0.85rem;
}

.reply-text {
  margin-bottom: 10px;
  font-size: 1rem;
  line-height: 1.5;
  color: #3c4858;
}

.reply-actions {
  display: flex;
  gap: 15px;
}

.loading-container {
  padding: 30px;
  background: white;
  border-radius: 12px;
}

.not-found {
  text-align: center;
  padding: 60px 30px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
}

.not-found h2 {
  margin-bottom: 20px;
  color: #3c4858;
  font-size: 1.8rem;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

@media (max-width: 768px) {
  .post-meta {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .post-actions {
    margin-top: 15px;
  }
  
  .post-stats {
    flex-wrap: wrap;
    padding: 10px 15px;
  }
  
  .stat-item {
    margin-bottom: 5px;
    margin-right: 10px;
  }

  .comments-section {
    padding: 20px 15px;
  }

  .comment-item, .reply-item {
    padding: 15px 10px;
  }
}
</style>
