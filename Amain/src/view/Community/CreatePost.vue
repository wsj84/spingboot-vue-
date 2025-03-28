<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createPostApi, getPostDetailApi, updatePostApi } from '@/api/post'

const route = useRoute()
const router = useRouter()
const postId = computed(() => route.params.id)
const isEditing = computed(() => !!postId.value)
const title = ref('')
const content = ref('')
const category = ref('讨论')
const loading = ref(false)
const categories = ['讨论', '求助', '分享']
// 如果是管理员，添加公告分类
const isAdmin = computed(() => {
    const isAdmin1 = localStorage.getItem('admin-token')
    if(isAdmin1){
        return isAdmin1
    }
})
// 获取当前用户ID
const userId = computed(() => {
  return localStorage.getItem('userId') || null
})

// 如果是管理员，添加公告分类
if (isAdmin.value) {
  categories.push('公告')
}

// 加载帖子详情（编辑模式）
const loadPostDetail = async () => {
  if (!isEditing.value) return
  
  loading.value = true
  try {
    const result = await getPostDetailApi(postId.value)
    if (result.code === 1) {
      const post = result.data
      title.value = post.title
      content.value = post.content
      category.value = post.category
    } else {
      ElMessage.error(result.msg || '获取帖子详情失败')
      router.push('/community')
    }
  } catch (error) {
    console.error('加载帖子详情失败:', error)
    ElMessage.error('加载帖子详情失败')
    router.push('/community')
  } finally {
    loading.value = false
  }
}

// 保存帖子
const savePost = async () => {
  if (!title.value.trim()) {
    ElMessage.warning('标题不能为空')
    return
  }
  
  if (!content.value.trim()) {
    ElMessage.warning('内容不能为空')
    return
  }

    // 检查用户是否已登录
    if (!userId.value) {
    ElMessage.error('请先登录')
    router.push('/login')
    return
  }

  
  loading.value = true
  try {
    const postData = {
      title: title.value,
      content: content.value,
      category: category.value,
      userId: parseInt(userId.value)
    }
    
    let result
    if (isEditing.value) {
        postData.id = parseInt(postId.value)
      result = await updatePostApi(postData)
    } else {
      result = await createPostApi(postData)
    }
    
    if (result.code === 1) {
      ElMessage.success(isEditing.value ? '更新成功' : '发布成功')
      console.log('帖子ID:', result);
      
      // 如果是新建帖子，跳转到帖子详情页
      if (!isEditing.value && result.data.id) {
        router.push(`/community/post/${result.data.id}`)
      } else {
        router.push('/community')
      }
    } else {
      ElMessage.error(result.msg || (isEditing.value ? '更新失败' : '发布失败'))
    }
  } catch (error) {
    console.error(isEditing.value ? '更新帖子失败:' : '发布帖子失败:', error)
    ElMessage.error(isEditing.value ? '更新失败' : '发布失败')
  } finally {
    loading.value = false
  }
}

// 取消
const cancel = () => {
  ElMessage.info('已取消')
  if (isEditing.value) {
    router.push(`/community/post/${postId.value}`)
  } else {
    router.push('/community')
  }
}

onMounted(() => {
  if (isEditing.value) {
    loadPostDetail()
  }
})
</script>

<template>
  <div class="create-post-container" v-loading="loading">
    <div class="back-button" @click="cancel">
      <i class="el-icon-arrow-left"></i> 
      {{ isEditing ? '返回帖子' : '返回社区' }}
    </div>
    
    <h1>{{ isEditing ? '编辑帖子' : '发布新帖子' }}</h1>
    
    <div class="form-container">
      <div class="form-group">
        <label for="title">标题</label>
        <el-input 
          id="title"
          v-model="title" 
          placeholder="请输入帖子标题"
          maxlength="100"
          show-word-limit
        ></el-input>
      </div>
      
      <div class="form-group">
        <label for="category">分类</label>
        <el-select v-model="category" placeholder="选择分类">
          <el-option 
            v-for="item in categories" 
            :key="item" 
            :label="item" 
            :value="item"
          ></el-option>
        </el-select>
      </div>
      
      <div class="form-group">
        <label for="content">内容</label>
        <el-input 
          id="content"
          v-model="content" 
          type="textarea"
          placeholder="请输入帖子内容"
          :rows="10"
          maxlength="5000"
          show-word-limit
        ></el-input>
      </div>
      
      <div class="form-actions">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="savePost">
          {{ isEditing ? '更新' : '发布' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.create-post-container {
  max-width: 800px;
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

h1 {
  font-size: 2rem;
  color: #2c3e50;
  margin-bottom: 30px;
  font-weight: 700;
}

.form-container {
  background-color: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.06);
}

.form-group {
  margin-bottom: 25px;
}

.form-group label {
  display: block;
  margin-bottom: 10px;
  font-weight: 600;
  color: #2c3e50;
  font-size: 1.05rem;
}

.form-group :deep(.el-input__inner),
.form-group :deep(.el-textarea__inner) {
  border-radius: 8px;
  border-color: #dcdfe6;
  padding: 12px;
  font-size: 1.05rem;
}

.form-group :deep(.el-select) {
  width: 100%;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 35px;
  gap: 15px;
}

.form-actions :deep(.el-button) {
  padding: 12px 24px;
  font-size: 1rem;
  border-radius: 8px;
}

.form-actions :deep(.el-button--primary) {
  background-color: #3498db;
  border-color: #3498db;
}

.form-actions :deep(.el-button--primary:hover) {
  background-color: #2980b9;
  border-color: #2980b9;
}

@media (max-width: 768px) {
  .form-container {
    padding: 20px 15px;
  }
  
  h1 {
    font-size: 1.7rem;
  }
}
</style>
