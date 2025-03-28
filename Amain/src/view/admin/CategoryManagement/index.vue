<template>
  <div class="admin-container">
    <div class="admin-header">
      <h1>分类管理</h1>
      <el-button type="primary" @click="handleAdd">添加分类</el-button>
    </div>

    <el-table :data="tableData" style="width: 100%" border stripe>
      <el-table-column type="index" label="序号" width="70" />
      <el-table-column prop="name" label="分类名称" min-width="150" />
      <el-table-column prop="description" label="分类描述" min-width="200" />
      <el-table-column label="操作" min-width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
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
        :page-sizes="[10, 20, 30, 50, 75, 100]"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 分类编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '添加分类' : '编辑分类'" width="500px">
      <el-form ref="categoryForm" :model="form" label-width="100px" :rules="rules">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { queryCategoriesApi,addCategoryApi,updateCategoryApi,deleteCategoryApi } from '../../../api/category';
import '@/assets/styles/admin-table.css'
// 分页数据
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(0)

// 表格数据
const tableData = ref([]);

// 表单验证规则
const categoryForm = ref(null);
const rules = {
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { min: 2, max: 20, message: '分类名称长度必须在2到20个字符之间', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入分类描述', trigger: 'blur' },
    { max: 200, message: '描述长度不能超过200个字符', trigger: 'blur' }
  ]
};

// 表单数据
const dialogVisible = ref(false);
const dialogType = ref('add');
const form = ref({
  id: '',
  name: '',
  description: '',
  status: '启用'
});

// 查询所有分类
const queryAll = async () => {
  // 这里添加调用后端API的代码
  /* 示例:
  const result = await queryAllCategoriesApi(currentPage.value, pageSize.value)
  tableData.value = result.data.rows
  totalPage.value = result.data.total
  */
  const result = await queryCategoriesApi(currentPage.value, pageSize.value)
  tableData.value = result.data.rows
  totalPage.value = result.data.total
};

// 添加分类
const handleAdd = () => {
  dialogType.value = 'add';
  form.value = {
    id: '',
    name: '',
    description: '',
  };
  dialogVisible.value = true;
};

// 编辑分类
const handleEdit = (row) => {
  dialogType.value = 'edit';
  form.value = { ...row };
  dialogVisible.value = true;
};

// 删除分类
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除分类 ${row.name} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      // 这里添加删除API调用
      /* 示例:
      const result = await deleteCategoryApi(row.id)
      if (result.code !== 1) {
        ElMessage.error('删除失败');
        return;
      }
      */
      const result = await deleteCategoryApi(row.id)
      if (result.code !== 1) {
        ElMessage.error('删除失败');
        return;
      }
      ElMessage.success('删除成功');
      queryAll();
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '出错了请联系管理员',
      });
    });
};

// 提交表单
const handleSubmit = () => {
  categoryForm.value.validate(async (valid) => {
    if (!valid) return;
    if (dialogType.value === 'add') {
      const result = await addCategoryApi(form.value)
      if (result.code !== 1) {
        ElMessage({
          type: 'error',
          message: '添加失败',
        });
        return;
      }
      ElMessage({
        type: 'success',
        message: '添加成功',
      });
    } else {
      const result = await updateCategoryApi(form.value)
      if (result.code !== 1) {
        ElMessage({
          type: 'error',
          message: '编辑失败',
        });
        return;
      }
      ElMessage({
        type: 'success',
        message: '编辑成功',
      });
    }
    queryAll()
    dialogVisible.value = false;
  })
};

// 分页处理
const handleSizeChange = () => {
  queryAll();
};

const handleCurrentChange = () => {
  queryAll();
};

// 初始化加载
onMounted(() => {
  queryAll();
});
</script>

<style scoped>
/* 移除所有重复的表格样式，不需要保留任何特殊样式 */
</style>
