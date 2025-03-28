<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { getPostsApi, likePostApi, unlikePostApi } from '@/api/post'

const router = useRouter()
const posts = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalPosts = ref(0)
const categories = ref(['全部', '讨论', '求助', '分享', '公告'])
const activeCategory = ref('全部')
const searchQuery = ref('')
const isLoggedIn = computed(() => !!localStorage.getItem('token'))
const currentUserId = computed(() => localStorage.getItem('userId'))

// 加载帖子列表
const loadPosts = async () => {
  loading.value = true
  try {
    const category = activeCategory.value === '全部' ? '' : activeCategory.value
    const result = await getPostsApi(
      currentPage.value, 
      pageSize.value, 
      category, 
      searchQuery.value.trim() // 添加搜索关键词参数
    )
    if (result.code === 1) {
      posts.value = result.data.rows
      totalPosts.value = result.data.total
      
      // 如果是搜索结果，显示提示
      if (searchQuery.value.trim()) {
        ElMessage.success(`找到 ${result.data.total} 条相关帖子`)
      }
    } else {
      ElMessage.error(result.msg || '获取帖子失败')
    }
  } catch (error) {
    console.error('加载帖子失败:', error)
    ElMessage.error('加载帖子失败')
  } finally {
    loading.value = false
  }
}

// 处理分类切换
const handleCategoryChange = (category) => {
  activeCategory.value = category
  currentPage.value = 1
  loadPosts()
}

// 处理分页
const handleCurrentChange = (page) => {
  currentPage.value = page
  loadPosts()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadPosts()
}

// 跳转到帖子详情
const goToPostDetail = (postId) => {
  router.push(`/community/post/${postId}`)
}

// 创建新帖子
const createNewPost = () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  router.push('/community/create')
}

// 处理帖子点赞
const handleLikePost = async (post, event) => {
  event.stopPropagation() // 阻止事件冒泡
  
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录')
    return
  }
  
  try {
    if (post.isLiked) {
      await unlikePostApi(post.id)
      post.likeCount--
    } else {
      await likePostApi(post.id)
      post.likeCount++
    }
    post.isLiked = !post.isLiked
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
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

// 判断是否是自己的帖子
const isMyPost = (userId) => {
  return userId.toString() === currentUserId.value
}

// 搜索帖子
const searchPosts = () => {
  currentPage.value = 1
  loadPosts()
}

// 清除搜索
const clearSearch = () => {
  searchQuery.value = ''
  currentPage.value = 1
  loadPosts()
}

onMounted(() => {
  loadPosts()
})
</script>

<template>
  <div class="community-container">
    <div class="community-header">
      <h1>社区论坛</h1>
      <p>分享你的想法，与其他人交流讨论</p>
    </div>
    
    <div class="top-actions">
      <div class="search-box">
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索帖子..." 
          clearable
          @keyup.enter="searchPosts"
          @clear="clearSearch"
        >
          <template #append>
            <el-button @click="searchPosts">
              <i class="el-icon-search"></i>
            </el-button>
          </template>
        </el-input>
      </div>
      
      <el-button type="primary" @click="createNewPost">
        <i class="el-icon-plus"></i> 发布新帖子
      </el-button>
    </div>
    
    <!-- 添加搜索结果提示 -->
    <div v-if="searchQuery" class="search-result-info">
      <el-tag effect="plain" size="large" closable @close="clearSearch">
        搜索结果: "{{ searchQuery }}"
      </el-tag>
    </div>
    
    <div class="categories">
      <div 
        v-for="category in categories" 
        :key="category"
        class="category-tag"
        :class="{ 'active': activeCategory === category }"
        @click="handleCategoryChange(category)"
      >
        {{ category }}
      </div>
    </div>
    
    <div class="posts-container" v-loading="loading">
      <div v-if="posts.length === 0" class="no-posts">
        暂无帖子，快来成为第一个发帖的人吧！
      </div>
      
      <div v-else class="post-list">
        <div 
          v-for="post in posts" 
          :key="post.id" 
          class="post-item"
          @click="goToPostDetail(post.id)"
        >
          <div class="post-avatar">
            <el-avatar>
                {{ post.user?.username?.charAt(0) || '?' }}
            </el-avatar>
          </div>
          
          <div class="post-content">
            <div class="post-header">
              <h3 class="post-title">{{ post.title }}</h3>
              <el-tag size="small" :type="post.category === '公告' ? 'danger' : 'info'">
                {{ post.category }}
              </el-tag>
            </div>
            
            <div class="post-brief">{{ post.content.substring(0, 150) }}{{ post.content.length > 150 ? '...' : '' }}</div>
            
            <div class="post-meta">
              <span class="post-author">{{ post.user.username }}</span>
              <span class="post-time">{{ formatDate(post.createdAt) }}</span>
              <span v-if="isMyPost(post.userId)" class="post-owner-tag">我的帖子</span>
            </div>
          </div>
          
          <div class="post-stats">
            <div class="stat-item" @click.stop="handleLikePost(post, $event)">
              <i :class="['el-icon-star-on', { 'liked': post.isLiked }]"></i>
              <span>{{ post.likeCount }}</span>
            </div>
            <div class="stat-item">
              <i class="el-icon-chat-dot-square"></i>
              <span>{{ post.commentCount }}</span>
            </div>
            <div class="stat-item">
              <i class="el-icon-view"></i>
              <span>{{ post.viewCount }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          :total="totalPosts"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.community-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9fafc;
  min-height: 100vh;
}

.community-header {
  text-align: center;
  margin-bottom: 35px;
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  padding: 30px 20px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  color: white;
}

.community-header h1 {
  font-size: 2.5rem;
  margin-bottom: 10px;
  font-weight: 700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.community-header p {
  font-size: 1.1rem;
  opacity: 0.9;
}

.top-actions {
  display: flex;
  justify-content: space-between;
  margin-bottom: 25px;
  align-items: center;
}

.search-box {
  width: 350px;
}

.search-box :deep(.el-input__inner) {
  border-radius: 8px 0 0 8px;
  height: 42px;
}

.search-box :deep(.el-input-group__append button) {
  height: 42px;
  border-radius: 0 8px 8px 0;
  background: #3498db;
  color: white;
  border-color: #3498db;
}

.search-result-info {
  margin-bottom: 20px;
}

.categories {
  display: flex;
  gap: 12px;
  margin-bottom: 25px;
  flex-wrap: wrap;
}

.category-tag {
  padding: 8px 18px;
  border-radius: 20px;
  background-color: #f0f2f5;
  color: #606266;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 500;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.category-tag:hover {
  background-color: #e6f7ff;
  transform: translateY(-2px);
}

.category-tag.active {
  background: linear-gradient(to right, #3498db, #2980b9);
  color: white;
  box-shadow: 0 4px 10px rgba(52, 152, 219, 0.3);
}

.post-list {
  margin-bottom: 30px;
}

.post-item {
  display: flex;
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid transparent;
}

.post-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.12);
  border-color: rgba(52, 152, 219, 0.3);
}

.post-avatar {
  margin-right: 20px;
}

.post-avatar :deep(.el-avatar) {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.1);
  background: linear-gradient(135deg, #3498db, #2c3e50);
  color: white;
  font-weight: bold;
  font-size: 16px;
}

.post-content {
  flex: 1;
  overflow: hidden;
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.post-title {
  margin: 0;
  margin-right: 12px;
  font-size: 1.3rem;
  color: #2c3e50;
  font-weight: 600;
}

.post-brief {
  color: #5d6778;
  margin-bottom: 15px;
  font-size: 1rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.6;
}

.post-meta {
  font-size: 0.9rem;
  color: #8392a5;
  display: flex;
  align-items: center;
}

.post-author {
  font-weight: 600;
  margin-right: 12px;
  color: #3498db;
}

.post-time {
  margin-right: 12px;
}

.post-owner-tag {
  color: #e74c3c;
  font-size: 0.85rem;
  font-weight: 500;
  background-color: rgba(231, 76, 60, 0.1);
  padding: 2px 8px;
  border-radius: 4px;
}

.post-stats {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 12px;
  min-width: 90px;
  padding-left: 15px;
  border-left: 1px solid #ebeef5;
}

.stat-item {
  display: flex;
  align-items: center;
  color: #8392a5;
  font-size: 0.95rem;
  transition: all 0.2s;
}

.stat-item i {
  margin-right: 8px;
  font-size: 1.1rem;
}

.stat-item:hover {
  color: #3498db;
}

.liked {
  color: #e74c3c !important;
}

.no-posts {
  text-align: center;
  padding: 60px 0;
  color: #8392a5;
  font-size: 1.2rem;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

@media (max-width: 768px) {
  .top-actions {
    flex-direction: column;
    gap: 15px;
  }
  
  .search-box {
    width: 100%;
  }
  
  .post-item {
    flex-direction: column;
    padding: 15px;
  }
  
  .post-avatar {
    margin-bottom: 15px;
    margin-right: 0;
  }
  
  .post-stats {
    flex-direction: row;
    margin-top: 20px;
    justify-content: space-around;
    padding-left: 0;
    border-left: none;
    border-top: 1px solid #ebeef5;
    padding-top: 15px;
  }

  .community-header {
    padding: 20px 15px;
  }

  .community-header h1 {
    font-size: 2rem;
  }
}
</style>
