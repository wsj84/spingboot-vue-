<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { addUserApi } from '../../api/loginInfo.js';
const router = useRouter();

const registerFormRef = ref(null);

// 表单数据
const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
});

// 表单验证规则
const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: ['blur', 'change'] },
    { min: 3, max: 20, message: '用户名长度应在 3 到 20 个字符之间', trigger: ['blur', 'change'] }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
});

// 注册处理逻辑
const submitRegister = async () => {
  await registerFormRef.value.validate(async (valid, fields) => {
    if (!valid) {
      console.log('验证失败', fields);
      return;
    }
    if(form.password !== form.confirmPassword) {
      ElMessage.error('密码不一致');
      return;
    }
    // 此处可添加调用API的逻辑
    const result =await addUserApi(form);
    console.log('注册结果:', result);
    if(result.code !== 1) {
      ElMessage.error(result.msg || '注册失败，请稍后重试');
      return;
    }
    ElMessage.success('注册成功');
    router.push('/login');
  });
};
</script>

<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-header">
        <h2>用户注册</h2>
        <p>欢迎加入我们</p>
      </div>
      <el-form 
        ref="registerFormRef"
        :model="form"
        :rules="rules"
        :validate-on-rule-change="false"
        status-icon
        scroll-to-error
        @submit.prevent="submitRegister" 
        class="register-form"
      >
        <el-form-item 
          prop="username"
          :error="form.username === '' ? '用户名不能为空' : ''"
        >
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item 
          prop="email"
          :error="form.email === '' ? '邮箱不能为空' : ''"
        >
          <el-input
            v-model="form.email"
            placeholder="请输入邮箱"
            prefix-icon="Message"
          />
        </el-form-item>
        <el-form-item 
          prop="password"
          :error="form.password === '' ? '密码不能为空' : ''"
        >
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item 
          prop="confirmPassword"
          :error="form.confirmPassword === '' ? '请确认密码' : form.confirmPassword !== form.password ? '两次密码不一致' : ''"
        >
          <el-input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitRegister" class="submit-btn">注册</el-button>
        </el-form-item>
        <div class="login-link">
          已有账号？<el-link type="primary" @click="router.push('/login')">立即登录</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-container {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
  animation: fadeIn 0.5s ease;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h2 {
  color: #333;
  font-size: 28px;
  margin-bottom: 8px;
}

.register-header p {
  color: #666;
  font-size: 16px;
}

.register-form {
  margin-top: 20px;
}

:deep(.el-input) {
  margin-bottom: 20px;
}

:deep(.el-input__wrapper) {
  border-radius: 8px;
  padding: 12px;
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409eff;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  border-radius: 8px;
  margin-top: 10px;
  background: linear-gradient(135deg, #409eff 0%, #36cfc9 100%);
  border: none;
  transition: transform 0.2s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
}

.login-link {
  text-align: center;
  margin-top: 15px;
  font-size: 14px;
  color: #666;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 480px) {
  .register-container {
    width: 90%;
    padding: 20px;
    margin: 20px;
  }
  
  .register-header h2 {
    font-size: 24px;
  }
}

:deep(.el-form-item__error) {
  position: absolute;
  top: -20px;
  left: 0;
  font-size: 12px;
  color: #f56c6c;
}
</style>