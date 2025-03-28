<template>
  <div class="admin-container">
    <div class="admin-header">
      <h1>动漫管理</h1>
      <el-button type="primary" @click="handleAdd">添加动漫</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" border stripe>
      <el-table-column type="index" label="序号" width="70" />
      <el-table-column label="封面" min-width="120">
        <template #default="scope">
          <img :src="scope.row.thumbnailUrl" alt="封面"
            style="width: 100px; height: 140px; object-fit: cover; border-radius: 4px;" />
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="180" />
      <el-table-column prop="categoryId" label="分类" min-width="120">
        <template #default="scope">
          {{ getCategoryName(scope.row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column prop="viewCount" label="观看次数" min-width="100" />
      <el-table-column prop="isVipOnly" label="VIP专属" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.isVipOnly ? 'warning' : 'info'">
            {{ scope.row.isVipOnly ? 'VIP专属' : '免费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="releaseDate" label="发布日期" min-width="120" />
      <el-table-column label="操作" min-width="220">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" background
        layout="total, sizes, prev, pager, next, jumper" :total="totalPage" :page-sizes="[10, 20, 30, 50, 75, 100]"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

    <!-- 动漫编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '添加动漫' : '编辑动漫'" width="600px">
      <el-form ref="animeForm" :model="form" label-width="100px" :rules="rules">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" />
        </el-form-item>

        <el-form-item label="视频" prop="videoUrl">
          <el-upload class="avatar-uploader" action="/api/upload" :headers="{ 'X-Session-Id': uploadSessionId }"
            :show-file-list="true" :on-success="handleVideoSuccess" :before-upload="beforeVideoUpload"
            accept=".mp4,.webm,.ogg">
            <video v-if="form.videoUrl" :src="form.videoUrl" class="video-preview" controls preload="auto"></video>
            <el-icon v-else class="video-uploader-icon">
              <Plus />
            </el-icon>
            <div class="upload-tip" v-if="!form.videoUrl">点击上传视频</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="封面" prop="thumbnailUrl">
          <el-upload class="avatar-uploader" action="/api/upload" :show-file-list="true"
            :headers="{ 'X-Session-Id': uploadSessionId }" :on-success="handleThumbnailSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="form.thumbnailUrl" :src="form.thumbnailUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="发布日期" prop="releaseDate">
          <el-date-picker v-model="form.releaseDate" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="VIP专属">
          <el-switch v-model="form.isVipOnly" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="连载中" value="连载中" />
            <el-option label="已完结" value="已完结" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancel">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { countdownEmits, ElMessage, ElMessageBox } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import '@/assets/styles/admin-table.css'
import { queryAll } from '../../../api/category';
import { queryAnimeApi, addAnimeApi, updateAnimeApi, deleteAnimeApi } from '../../../api/anime';
import axios from 'axios';
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)
// 分类数据
const categories = ref([]);
const uploadSessionId = ref(localStorage.getItem('upload-session-id') || '');
// 获取分类数据
const fetchCategories = async () => {
  try {
    const response = await queryAll();
    categories.value = response.data;
  } catch (error) {
    ElMessage.error('获取分类列表失败');
  }
};

// 根据分类ID获取分类名称
const getCategoryName = (categoryId) => {
  const category = categories.value.find(c => c.id === categoryId);
  return category ? category.name : '未知分类';
};

// 页面加载时获取分类数据
onMounted(() => {
  if (!uploadSessionId.value) {
    uploadSessionId.value = 'session-' + Date.now();
    localStorage.setItem('upload-session-id', uploadSessionId.value);
  }
  SeachAll();
  fetchCategories();
});
const SeachAll = async () => {
  // 这里添加调用后端API的代码
  /* 示例:
  const result = await queryAllCategoriesApi(currentPage.value, pageSize.value)
  tableData.value = result.data.rows
  totalPage.value = result.data.total
  */
  const result = await queryAnimeApi(currentPage.value, pageSize.value)
  tableData.value = result.data.rows
  totalPage.value = result.data.total
  console.log(tableData.value);
};
// 表格数据
const tableData = ref([]);

// 表单数据
const dialogVisible = ref(false);
const dialogType = ref('add'); // 'add' 或 'edit'
const form = ref({
  id: '',
  title: '',
  description: '',
  videoUrl: '',
  thumbnailUrl: '',
  categoryId: '',
  releaseDate: '',
  viewCount: 0,
  isVipOnly: false,
  createdAt: null
});

// 添加表单引用
const animeForm = ref(null);

// 添加表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入动漫标题', trigger: 'blur' },
    { min: 2, max: 50, message: '标题长度必须在2到50个字符之间', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    { max: 500, message: '描述长度不能超过500个字符', trigger: 'blur' }
  ],
  videoUrl: [
    { required: true, message: '请输入视频地址', trigger: 'blur' }
  ],
  thumbnailUrl: [
    { required: true, message: '请上传封面图片', trigger: 'change' }
  ],
  releaseDate: [
    { required: true, message: '请选择发布日期', trigger: 'change' }
  ]
};

// 添加动漫
const handleAdd = () => {
  dialogType.value = 'add';
  form.value = {
    id: '',
    title: '',
    description: '',
    videoUrl: '',
    thumbnailUrl: '',
    categoryId: '',
    releaseDate: '',
    viewCount: 0,
    isVipOnly: false,
    createdAt: null
  };
  dialogVisible.value = true;
};

// 编辑动漫
const handleEdit = (row) => {
  dialogType.value = 'edit';
  form.value = { ...row };
  dialogVisible.value = true;
  console.log(form.value);
};

// 删除动漫
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除动漫 ${row.title} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      // 这里应该调用删除API
      const result = await deleteAnimeApi(row.id)
      if (result.code !== 1) {
        ElMessage.error('删除失败');
        return;
      }
      ElMessage.success('删除成功');
      SeachAll();
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '出错了请联系管理员',
      });
    });
};

const beforeAvatarUpload = (file) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/webp';
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJpgOrPng) {
    ElMessage.error('上传头像图片只能是 JPG 或 PNG,webp 格式!');
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!');
  }
  return isJpgOrPng && isLt2M;
};

const beforeVideoUpload = (file) => {
  const isValidFormat = ['video/mp4', 'video/webm', 'video/ogg'].includes(file.type);
  const isLt500M = file.size / 1024 / 1024 < 500;

  if (!isValidFormat) {
    ElMessage.error('只能上传MP4/WebM/Ogg格式的视频!');
    return false;
  }
  if (!isLt500M) {
    ElMessage.error('视频大小不能超过 500MB!');
    return false;
  }
  return true;
};

// 提交表单
const handleSubmit = async () => {
  animeForm.value.validate(async (valid) => {
    if (!valid) return;

    // 获取会话ID
    const sessionId = localStorage.getItem('upload-session-id');

    // 确认上传的文件
    if (sessionId) {
      try {
        // 收集所有需要确认的文件ID
        const fileIds = {};
        
        // 安全地提取文件ID
        if (form.value.thumbnailUrl && typeof form.value.thumbnailUrl === 'string') {
          const thumbnailParts = form.value.thumbnailUrl.split('/');
          fileIds.thumbnailUrl = thumbnailParts[thumbnailParts.length - 1];
        }
        
        if (form.value.videoUrl && typeof form.value.videoUrl === 'string') {
          const videoParts = form.value.videoUrl.split('/');
          fileIds.videoUrl = videoParts[videoParts.length - 1];
        }
        
        console.log('确认的文件IDs:', fileIds);
        
        // 只有当有文件ID时才调用确认API
        if (Object.keys(fileIds).length > 0) {
          const confirmResult = await axios.post('/api/confirm-upload',
            { fileIds },
            { headers: { 'X-Session-Id': sessionId } }
          );
          console.log('确认文件结果:', confirmResult);
          if (confirmResult.data.code !== 1) {
            ElMessage.error('文件确认失败');
            return;
          }
        }
      } catch (error) {
        console.error('确认文件失败:', error);
        ElMessage.error('文件确认失败: ' + (error.message || '未知错误'));
        return;
      }
    }

    // 继续原有的表单提交逻辑
    if (dialogType.value === 'add') {
      const result = await addAnimeApi(form.value);
      if (result.code !== 1) {
        ElMessage.error('添加失败');
        return;
      }
      ElMessage.success('添加成功');
    } else {
      const result = await updateAnimeApi(form.value);
      if (result.code !== 1) {
        ElMessage.error('编辑失败');
        return;
      }
      ElMessage.success('编辑成功');
    }

    // 清除会话ID
    localStorage.removeItem('upload-session-id');

    dialogVisible.value = false;
    SeachAll();
  });
};


// 视频上传成功回调
const handleVideoSuccess = (response) => {
  console.log("视频上传响应:", response);
  if (response.code === 1) {
    // 处理不同的响应格式
    if (typeof response.data === 'string') {
      form.value.videoUrl = response.data;
    } else if (response.data && response.data.fileUrl) {
      form.value.videoUrl = response.data.fileUrl;
      
      // 保存会话ID
      if (response.data.sessionId) {
        localStorage.setItem('upload-session-id', response.data.sessionId);
      }
    }
    
    ElMessage.success('视频上传成功');
  } else {
    ElMessage.error('视频上传失败');
  }
};


// 缩略图上传成功回调
const handleThumbnailSuccess = (response) => {
  console.log("缩略图上传响应:", response);
  if (response.code === 1) {
    // 处理不同的响应格式
    if (typeof response.data === 'string') {
      form.value.thumbnailUrl = response.data;
    } else if (response.data && response.data.fileUrl) {
      form.value.thumbnailUrl = response.data.fileUrl;
      
      // 保存会话ID
      if (response.data.sessionId) {
        localStorage.setItem('upload-session-id', response.data.sessionId);
      }
    }
    
    ElMessage.success('封面上传成功');
  } else {
    ElMessage.error('封面上传失败');
  }
};

// 取消对话框时清理临时文件
const handleCancel = async () => {
  console.log('取消对话框');
  const sessionId = localStorage.getItem('upload-session-id');
  if (sessionId) {
    try {
      await axios.post('/api/cancel-upload', null, {
        headers: { 'X-Session-Id': sessionId }
      });
      localStorage.removeItem('upload-session-id');
    } catch (error) {
      console.error('取消上传失败:', error);
    }
  }
  dialogVisible.value = false;
};

// 分页处理
const handleSizeChange = (val) => {
  SeachAll();
  console.log(`每页 ${val} 条`);
  // 这里应该重新获取数据
};

const handleCurrentChange = (val) => {
  SeachAll();
  console.log(`当前页: ${val}`);
  // 这里应该重新获取数据
};
</script>

<style scoped>
/* 仅保留特殊样式 */
.avatar-uploader {
  width: 178px;
  height: 178px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.video-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 360px;
  height: 200px;
}

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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

/* 表格美化样式 */
:deep(.el-table) {
  width: 100% !important;
  margin: 0 auto;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

:deep(.el-table .cell) {
  white-space: nowrap;
  padding: 0 10px;
  line-height: 28px;
  width: 100%;
  /* 添加宽度100% */
}

:deep(.el-table thead) {
  background-color: #f5f7fa;
}

:deep(.el-table th.el-table__cell) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: bold;
  text-align: center;
  height: 50px;
  font-size: 14px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table__header-wrapper) {
  display: table !important;
}

:deep(.el-table th) {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: bold;
  text-align: center;
}

:deep(.el-table td) {
  text-align: center;
}

:deep(.el-table--striped .el-table__body tr.el-table__row--striped td) {
  background-color: #fafafa;
}

:deep(.el-table__body tr:hover > td) {
  background-color: #f0f9eb;
}

:deep(.el-tag) {
  display: inline-block;
  min-width: 60px;
  text-align: center;
  padding: 0 8px;
  line-height: 24px;
}

:deep(.el-table .cell .el-button+.el-button) {
  margin-left: 8px;
}

:deep(.el-table__row) {
  width: 100%;
  /* 添加行宽度100% */
}

/* 添加封面图片样式 */
:deep(.el-table .cover-cell) {
  padding: 10px;
  text-align: center;
}

:deep(.el-table img) {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

:deep(.el-table img:hover) {
  transform: scale(1.05);
}

/* 添加视频上传组件样式 */
.video-preview {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.video-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.upload-tip {
  position: absolute;
  bottom: 10px;
  left: 0;
  right: 0;
  text-align: center;
  color: #8c939d;
}

/* 修改上传组件内部样式 */
:deep(.el-upload) {
  width: 100%;
  height: 100%;
}
</style>
