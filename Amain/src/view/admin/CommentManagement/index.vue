<template>
    <div class="admin-container">
      <div class="admin-header">
        <h1>评论管理</h1>
       
      </div>
  
      <el-table :data="tableData" style="width: 100%" border stripe>
        <el-table-column type="index" label="序号" width="70" />
        <el-table-column prop="content" label="评论内容" min-width="250" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="评论时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户" width="120">
          <template #default="scope">
            <el-tooltip effect="dark" :content="'用户ID: ' + scope.row.userId" placement="top">
              <span>{{ scope.row.userName || '加载中...' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="animeTitle" label="动漫" width="150">
          <template #default="scope">
            <el-tooltip effect="dark" :content="'动漫ID: ' + scope.row.animeId" placement="top">
              <span>{{ scope.row.animeTitle || '加载中...' }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250">
          <template #default="scope">
<!--             <el-button size="small" @click="handleView(scope.row)">查看</el-button> -->
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalPage"
          :page-sizes="[10, 20, 30, 50]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

   <!-- 评论详情对话框 -->
    <!--  <el-dialog v-model="dialogVisible" title="评论详情" width="600px">
        <div class="comment-detail">
          <div class="comment-header">
            <el-avatar :size="40">{{ currentComment.username?.charAt(0) }}</el-avatar>
            <div class="comment-info">
              <div class="comment-user">{{ currentComment.username }}</div>
              <div class="comment-meta">
                {{ currentComment.createTime }} · {{ currentComment.animeTitle }}
              </div>
            </div>
          </div>
          <div class="comment-body">
            {{ currentComment.content }}
          </div>
          <div class="comment-actions">
            <span class="comment-likes">
              <el-icon><Star /></el-icon> {{ currentComment.likeCount }} 点赞
            </span>
            <el-tag :type="tagType">{{ currentComment.status }}</el-tag>
          </div>
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">关闭</el-button>
            <el-button 
              :type="currentComment.status === '已审核' ? 'danger' : 'success'"
              @click="handleApproveInDialog"
            >
              {{ currentComment.status === '已审核' ? '屏蔽评论' : '通过审核' }}
            </el-button>
            <el-button type="danger" @click="handleDeleteInDialog">删除评论</el-button>
          </span>
        </template>
      </el-dialog> -->
    </div> 
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { 
    queryCommentsApi, 
    deleteCommentApi, 
    searchCommentsApi 
  } from '@/api/comment';
  import { queryAnimebyid  } from '../../../api/anime';
  import { queryUserbyid  } from '../../../api/user';
  import '@/assets/styles/admin-table.css'
  // 分页数据
  const currentPage = ref(1);
  const pageSize = ref(10);
  const totalPage = ref(0);
  const animename = ref('');
  // 筛选和搜索
  const filterStatus = ref('');
  const searchKeyword = ref('');
  
  // 表格数据
  const tableData = ref([]);
  
  // 格式化日期时间
  const formatDateTime = (dateTime) => {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    return date.toLocaleString();
  };
  
  // 加载单条评论的用户和动漫信息
  const loadCommentDetails = async (comment) => {
    try {
      // 加载用户信息
      const userResult = await queryUserbyid(comment.userId);
      if (userResult.code === 1) {
        comment.userName = userResult.data.username;
      }
      
      // 加载动漫信息
      const animeResult = await queryAnimebyid(comment.animeId);
      if (animeResult.code === 1) {
        comment.animeTitle = animeResult.data.title;
      }
    } catch (error) {
      console.error('加载评论详情失败:', error);
    }
  };
  
  // 查询所有评论
  const queryAll = async () => {
    try {
      const result = await queryCommentsApi(currentPage.value, pageSize.value, "" , "");
      if (result.code === 1) {
        tableData.value = result.data.rows;
        totalPage.value = result.data.total;
        console.log(tableData.value);
        // 为每条评论加载用户和动漫信息
        for (const comment of tableData.value) {
          await loadCommentDetails(comment);
        }
      } else {
        ElMessage.error('获取评论列表失败');
      }
    } catch (error) {
      ElMessage.error('系统错误');
    }
  };
  
  // 搜索评论
  const handleSearch = async () => {
    try {
      const result = await searchCommentsApi(
        searchKeyword.value,
        filterStatus.value,
        currentPage.value,
        pageSize.value
      );
      if (result.code === 1) {
        tableData.value = result.data.rows;
        totalPage.value = result.data.total;
        
        // 为搜索结果加载用户和动漫信息
        for (const comment of tableData.value) {
          await loadCommentDetails(comment);
        }
      }
    } catch (error) {
      ElMessage.error('搜索失败');
    }
  };
  
  // 删除评论
  const handleDelete = async (row) => {
    try {
      await ElMessageBox.confirm('确定要删除该评论吗？此操作不可恢复！', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      });
      
      const result = await deleteCommentApi(row.id);
      if (result.code === 1) {
        ElMessage.success('删除成功');
        queryAll();
      } else {
        ElMessage.error('删除失败');
      }
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error('操作失败');
      }
    }
  };
  
  // 分页处理
  const handleSizeChange = (val) => {
    pageSize.value = val;
    queryAll();
  };
  
  const handleCurrentChange = (val) => {
    currentPage.value = val;
    queryAll();
  };
  
  // 初始化加载
  onMounted(() => {
    queryAll();
  });
  /* 
  // 评论详情对话框
  const dialogVisible = ref(false);
  const currentComment = ref({});
  const tagType = computed(() => {
    if (currentComment.value.status === '已审核') return 'success';
    if (currentComment.value.status === '待审核') return 'warning';
    return 'danger';
  }); */
  
 /*  // 查看评论详情
  const handleView = (row) => {
    currentComment.value = { ...row };
    dialogVisible.value = true;
  };
  
  // 在对话框中审核或屏蔽评论
  const handleApproveInDialog = () => {
    const action = currentComment.value.status === '已审核' ? '屏蔽' : '通过审核';
    ElMessageBox.confirm(
      `确定要${action}该评论吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
      .then(() => {
        // 这里应该调用API
        currentComment.value.status = currentComment.value.status === '已审核' ? '已屏蔽' : '已审核';
        // 同步更新表格数据
        const index = tableData.value.findIndex(item => item.id === currentComment.value.id);
        if (index !== -1) {
          tableData.value[index].status = currentComment.value.status;
        }
        ElMessage({
          type: 'success',
          message: `${action}成功`,
        });
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消操作',
        });
      });
  };
  
  // 删除评论
  const handleDeleteInDialog = (row) => {
    ElMessageBox.confirm(
      '确定要删除该评论吗？此操作不可恢复！',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
      .then(() => {
        // 这里应该调用删除API
        ElMessage({
          type: 'success',
          message: '删除成功',
        });
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '已取消删除',
        });
      });
  }; */
  </script>
  
  <style scoped>
  .admin-container {
    padding: 20px;
  }
  
  .admin-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    align-items: center;
  }
  
  h1 {
    font-size: 24px;
    color: #303133;
    margin: 0;
  }
  
  .header-actions {
    display: flex;
    align-items: center;
  }
  
  .search-input {
    margin-right: 10px;
  }
  
  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
  
  .comment-detail {
    padding: 20px;
  }
  
  .comment-info {
    margin-left: 10px;
  }
  
  .comment-user {
    font-size: 16px;
    font-weight: bold;
  }
  
  .comment-meta {
    font-size: 12px;
    color: #909399;
  }
  </style>