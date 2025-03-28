<template>
    <div class="post-management-container">
      <h1 class="page-title">社区帖子管理</h1>
      
      <!-- 搜索和筛选 -->
      <div class="search-filter">
        <el-input
          v-model="searchQuery"
          placeholder="搜索帖子标题或内容"
          clearable
          @clear="resetSearch"
          style="width: 300px"
        >
          <template #append>
            <el-button @click="handleSearch">
              <i class="el-icon-search"></i>
            </el-button>
          </template>
        </el-input>
        
        <el-select v-model="categoryFilter" placeholder="帖子分类" clearable @change="handleFilter">
          <el-option
            v-for="category in categories"
            :key="category"
            :label="category"
            :value="category"
          ></el-option>
        </el-select>
      </div>
      
      <!-- 帖子列表 -->
      <el-table
        v-loading="loading"
        :data="posts"
        border
        style="width: 100%"
        @row-click="viewPostDetail"
      >
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="200"></el-table-column>
        <el-table-column prop="user.username" label="作者" width="120"></el-table-column>
        <el-table-column prop="category" label="分类" width="100">
          <template #default="{row}">
            <el-tag :type="row.category === '公告' ? 'danger' : 'info'">
              {{ row.category }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{row}">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞" width="80"></el-table-column>
        <el-table-column prop="commentCount" label="评论" width="80"></el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="80"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{row}">
            <el-button type="primary" size="small" @click.stop="viewPostDetail(row)">
              查看
            </el-button>
            <el-popconfirm
              title="确定删除该帖子吗？"
              @confirm="deletePost(row.id)"
              confirm-button-text="确定"
              cancel-button-text="取消"
            >
              <template #reference>
                <el-button type="danger" size="small" @click.stop>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next, total"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        ></el-pagination>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { getPostsApi, deletePostApi } from '@/api/post'
  
  const router = useRouter()
  const loading = ref(false)
  const posts = ref([])
  const total = ref(0)
  const currentPage = ref(1)
  const pageSize = ref(10)
  const searchQuery = ref('')
  const categoryFilter = ref('')
  const categories = ref(['全部', '公告', '讨论', '建议', '求助', '分享'])
  
  // 加载帖子列表
  const loadPosts = async () => {
    loading.value = true
    try {
      const category = categoryFilter.value === '全部' ? '' : categoryFilter.value
      const result = await getPostsApi(
        currentPage.value,
        pageSize.value,
        category,
        searchQuery.value.trim()
      )
      
      if (result.code === 1) {
        posts.value = result.data.rows || []
        total.value = result.data.total || 0
      } else {
        ElMessage.error(result.msg || '获取帖子列表失败')
      }
    } catch (error) {
      console.error('加载帖子失败:', error)
      ElMessage.error('加载帖子失败')
    } finally {
      loading.value = false
    }
  }
  
  // 查看帖子详情
  const viewPostDetail = (row) => {
    router.push(`/community/post/${row.id}`)
  }
  
  // 删除帖子
  const deletePost = async (postId) => {
    try {
      const result = await deletePostApi(postId)
      if (result.code === 1) {
        ElMessage.success('删除成功')
        loadPosts() // 重新加载列表
      } else {
        ElMessage.error(result.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除帖子失败:', error)
      ElMessage.error('删除失败')
    }
  }
  
  // 处理分页
  const handlePageChange = (page) => {
    currentPage.value = page
    loadPosts()
  }
  
  // 处理搜索
  const handleSearch = () => {
    currentPage.value = 1 // 重置到第一页
    loadPosts()
  }
  
  // 重置搜索
  const resetSearch = () => {
    searchQuery.value = ''
    handleSearch()
  }
  
  // 处理筛选
  const handleFilter = () => {
    currentPage.value = 1 // 重置到第一页
    loadPosts()
  }
  
  // 格式化日期
  const formatDate = (dateStr) => {
    if (!dateStr) return ''
    const date = new Date(dateStr)
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  }
  
  onMounted(() => {
    loadPosts()
  })
  </script>
  
  <style scoped>
  .post-management-container {
    padding: 25px;
    background-color: #f9fafc;
    min-height: 100vh;
  }
  
  .page-title {
    margin-bottom: 30px;
    font-size: 28px;
    font-weight: 700;
    color: #2c3e50;
    border-left: 4px solid #3498db;
    padding-left: 15px;
  }
  
  .search-filter {
    display: flex;
    gap: 20px;
    margin-bottom: 25px;
    background: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }
  
  .search-filter :deep(.el-input__inner),
  .search-filter :deep(.el-select__input) {
    border-radius: 8px;
    height: 42px;
  }
  
  .search-filter :deep(.el-input-group__append button) {
    height: 42px;
    border-radius: 0 8px 8px 0;
    background: #3498db;
    color: white;
    border-color: #3498db;
  }
  
  :deep(.el-table) {
    border-radius: 10px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }
  
  :deep(.el-table__header) th {
    background-color: #f0f2f5;
    color: #606266;
    font-weight: 600;
  }
  
  :deep(.el-table__row td) {
    padding: 12px 0;
  }
  
  :deep(.el-tag--danger) {
    background-color: rgba(245, 108, 108, 0.1);
    border-color: rgba(245, 108, 108, 0.2);
    color: #f56c6c;
  }
  
  :deep(.el-tag--info) {
    background-color: rgba(144, 147, 153, 0.1);
    border-color: rgba(144, 147, 153, 0.2);
    color: #909399;
  }
  
  .pagination {
    margin-top: 30px;
    display: flex;
    justify-content: center;
  }
  
  :deep(.el-pagination .btn-prev),
  :deep(.el-pagination .btn-next),
  :deep(.el-pagination .number) {
    background-color: white;
    color: #606266;
    border-radius: 4px;
    margin: 0 5px;
    min-width: 32px;
  }
  
  :deep(.el-pagination .number.active) {
    background-color: #3498db;
    color: white;
  }
  
  /* 表格行鼠标悬停效果 */
  :deep(.el-table__row) {
    cursor: pointer;
    transition: all 0.3s;
  }
  
  :deep(.el-table__row:hover) {
    background-color: #ecf5ff !important;
    transform: translateY(-2px);
  }
  
  :deep(.el-button--primary) {
    background-color: #3498db;
    border-color: #3498db;
  }
  
  :deep(.el-button--danger) {
    background-color: #e74c3c;
    border-color: #e74c3c;
  }
  
  :deep(.el-button--primary:hover) {
    background-color: #2980b9;
    border-color: #2980b9;
  }
  
  :deep(.el-button--danger:hover) {
    background-color: #c0392b;
    border-color: #c0392b;
  }
  
  @media (max-width: 768px) {
    .search-filter {
      flex-direction: column;
      padding: 15px;
    }
    
    .page-title {
      font-size: 24px;
    }
    
    .post-management-container {
      padding: 15px;
    }
  }
  </style>