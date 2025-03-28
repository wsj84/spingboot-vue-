<template>
  <div class="admin-container">
    <div class="admin-header">
      <h1>用户管理</h1>
      <el-button type="primary" @click="handleAdd">添加用户</el-button>
    </div>

    <el-table :data="tableData" border stripe>
      <el-table-column type="index" label="序号" width="70" />
      <el-table-column prop="username" label="用户名" min-width="150" />
      <el-table-column prop="email" label="邮箱" min-width="200" />
      <el-table-column prop="role" label="角色" min-width="120">
        <template #default="scope">
          <el-tag :type="scope.row.role === 'admin' ? 'danger' : 'info'">
            {{ scope.row.role === 'admin' ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 添加 VIP 状态列 -->
      <el-table-column prop="isVip" label="VIP" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.isVip ? 'warning' : 'info'">
            {{ scope.row.isVip ? 'VIP会员' : '普通会员' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="active" label="状态" min-width="120">
        <template #default="scope">
          <el-tag
            :type="scope.row.isActive === true || scope.row.isActive === 'true' || scope.row.isActive === '1' ? 'success' : 'danger'">
            {{ scope.row.isActive === true || scope.row.isActive === 'true' || scope.row.isActive === '1' ? '激活' : '禁用'
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" min-width="180" />
      <el-table-column label="操作" min-width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" background
        layout="total, sizes, prev, pager, next, jumper" :total="totlePage" :page-size="[10, 20, 30, 50, 75, 100]"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </div>

    <!-- 用户编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogType === 'add' ? '添加用户' : '编辑用户'" width="500px">
      <el-form ref="userForm" :model="form" label-width="80px" :rules="rules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" />
        </el-form-item>

        <el-form-item label="密码" v-if="dialogType === 'add'" prop="password">
          <el-input v-model="form.password" type="password" show-password />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>

        <el-form-item label="角色">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="管理员" value="admin" />
            <el-option label="普通用户" value="user" />
          </el-select>
        </el-form-item>

        <el-form-item label="VIP状态">
          <el-switch v-model="form.isVip" />
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="form.isActive" placeholder="请选择状态">
            <el-option label="激活" value="true" />
            <el-option label="禁用" value="false" />
          </el-select>
        </el-form-item>

        <el-form-item label="创建时间" v-if="dialogType === 'edit'">
          <el-date-picker v-model="form.createdAt" type="datetime" placeholder="选择日期时间" :disabled="true"
            format="YYYY-MM-DD HH:mm:ss" />
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
import { queryAllApi, addUserApi, updateUserApi, deleteUserApi } from '@/api/user';
import '@/assets/styles/admin-table.css'

const currentPage = ref(1)
const pageSize = ref(10)
const totlePage = ref();
const queryAll = async () => {
  const result = await queryAllApi(currentPage.value,pageSize.value)
  tableData.value = result.data.rows
  totlePage.value = result.data.total
  console.log("result用户数据:", result.data.rows);
  console.log("1table用户数据:", tableData.value);
}

// 表格数据
let tableData = ref([]);



// 新增表单验证规则
const userForm = ref(null);
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度必须在3到20位之间', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9]+$/, message: '用户名只能包含数字和字母', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value && value.includes('@')) {
          callback();
        } else {
          callback(new Error('邮箱格式不正确, 必须包含@符号'));
        }
      }, trigger: 'blur'
    }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
};

// 表单数据
const dialogVisible = ref(false);
const dialogType = ref('add'); // 'add' 或 'edit'
const form = ref({
  id: '',
  username: '',
  password: '',
  email: '',
  role: '用户',
  isVip: false,  // 添加 VIP 字段
  isActive: 'true',
  createdAt: ''
});

// 添加用户
const handleAdd = () => {
  dialogType.value = 'add';
  form.value = {
    id: '',
    username: '',
    password: '',
    email: '',
    role: '用户',
    isVip: false,  // 添加 VIP 字段初始值
    isActive: 'true',
    createdAt: ''
  };
  dialogVisible.value = true;
};

// 编辑用户
const handleEdit = (row) => {
  dialogType.value = 'edit';
  form.value = { ...row };
  // 不在编辑界面显示密码
  form.value.password = '';
  dialogVisible.value = true;
};

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除用户 ${row.username} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async () => {
      // 这里应该调用删除API
      const result = await deleteUserApi(row.id)
      if (result.code !== 1) {
        ElMessage({
          type: 'error',
          message: '删除失败，请联系管理员',
        });
        return;
      }
      ElMessage({
        type: 'success',
        message: '删除成功',
      });
      queryAll()
    })

};
const handleSubmit = () => {
  userForm.value.validate(async (valid) => {
    if (!valid) return;
    if (dialogType.value === 'add') {
      const result = await addUserApi(form.value)
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
      const result = await updateUserApi(form.value)
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
const handleSizeChange = (val) => {
  queryAll()
  console.log(`每页 ${val} 条`);
  // 这里应该重新获取数据
};

const handleCurrentChange = (val) => {
  queryAll()
  console.log(`当前页: ${val}`);
  // 这里应该重新获取数据
};

// 在 mounted 钩子中添加
onMounted(() => {
  queryAll()
});
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
}

/* 表头样式 */
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

/* 表格内按钮组样式 */
:deep(.el-table .cell .el-button+.el-button) {
  margin-left: 8px;
}

/* 为 VIP 标签添加特殊样式 */
:deep(.el-tag.el-tag--warning) {
  background-color: #fdf6ec;
  border-color: #faecd8;
  color: #e6a23c;
}

/* 仅保留VIP标签特殊样式 */
:deep(.el-tag.el-tag--warning) {
  background-color: #fdf6ec;
  border-color: #faecd8;
  color: #e6a23c;
}
</style>
