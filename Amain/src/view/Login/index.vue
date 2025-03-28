<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { loginInfo, getCurrentUser } from '../../api/loginInfo';

const router = useRouter();
const loginForm = ref({
  username: '',
  password: ''
});

const handleLogin = async () => {
  try {
    // 调用登录接口
    const result = await loginInfo({
      username: loginForm.value.username,
      password: loginForm.value.password
    });
    
    if (result.code === 1) {
      console.log('登录成功:', result.data);
      // 保存token
      localStorage.setItem('token', result.data.token);
      const s =   localStorage.getItem('token')
      // 获取用户信息
      const userInfo = await getCurrentUser(s);
      console.log('用户信息:', userInfo);
      if(userInfo.data.isActive === false){
        ElMessage.error('账号被封禁，请联系管理员激活账号');
        localStorage.removeItem('token');
        return;
      }
      localStorage.setItem('userId', userInfo.data.id);
      localStorage.setItem('username', userInfo.data.username);
      localStorage.setItem('isVip', userInfo.data.isVip);
      localStorage.setItem('isActive', userInfo.data.isActive);
      if (userInfo.data.role === 'admin') {
        localStorage.setItem('admin-token', 'true');
      }
      
      ElMessage.success('登录成功');
      setTimeout(() => {
        router.push('/');
      }, 300);
    } else {
      ElMessage.error(result.message || '登录失败，请检查用户名和密码');
    }
  } catch (error) {
    console.error('登录失败:', error);
    ElMessage.error('登录失败，请稍后重试');
  }
};

const handleRegister = () => {
  router.push('/register');
};
</script>

<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <h2>用户登录</h2>
        <p>欢迎回来</p>
      </div>
      <el-form :model="loginForm" class="login-form">
        <el-form-item>
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="submit-btn">登录</el-button>
        </el-form-item>
        <div class="register-link">
          没有账号？<el-link type="primary" @click="handleRegister">立即注册</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-container {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
  animation: fadeIn 0.5s ease;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  color: #333;
  font-size: 28px;
  margin-bottom: 8px;
}

.login-header p {
  color: #666;
  font-size: 16px;
}

.login-form {
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

.register-link {
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

@media (max-width: 480px) {
  .login-container {
    width: 90%;
    padding: 20px;
    margin: 20px;
  }
  
  .login-header h2 {
    font-size: 24px;
  }
}
</style>