<template>
  <div class="vip-container">
    <div class="vip-header">
      <div class="vip-title">
        <i class="el-icon-crown crown-icon"></i>
        <h1>开通VIP会员</h1>
      </div>
      <p class="subtitle">享受无限观影体验</p>
    </div>

    <div class="price-cards">
      <div class="price-card" :class="{ active: selectedPlan === 'month' }" @click="selectedPlan = 'month'">
        <h3>月度会员</h3>
        <div class="price">
          <span class="currency">￥</span>
          <span class="amount">19</span>
          <span class="period">/月</span>
        </div>
        <div class="features">
          <p><i class="el-icon-check"></i> 无限观看VIP动漫</p>
          <p><i class="el-icon-check"></i> 1080P高清画质</p>
          <p><i class="el-icon-check"></i> 新番抢先看</p>
        </div>
      </div>

      <div class="price-card" :class="{ active: selectedPlan === 'year' }" @click="selectedPlan = 'year'">
        <div class="best-value">年付优惠</div>
        <h3>年度会员</h3>
        <div class="price">
          <span class="currency">￥</span>
          <span class="amount">188</span>
          <span class="period">/年</span>
        </div>
        <div class="features">
          <p><i class="el-icon-check"></i> 无限观看VIP动漫</p>
          <p><i class="el-icon-check"></i> 1080P高清画质</p>
          <p><i class="el-icon-check"></i> 新番抢先看</p>
          <p><i class="el-icon-check"></i> 专属客服服务</p>
        </div>
      </div>
    </div>

    <div class="payment-section">
      <h2>选择支付方式</h2>
      <div class="payment-methods">
        <div class="payment-method" :class="{ active: payMethod === 'alipay' }" @click="payMethod = 'alipay'">
          <img src="/images/alipay.png" alt="支付宝">
          <span>支付宝</span>
        </div>
        <div class="payment-method" :class="{ active: payMethod === 'wechat' }" @click="payMethod = 'wechat'">
          <img src="/images/wechat.png" alt="微信">
          <span>微信支付</span>
        </div>
      </div>

      <el-button type="primary" class="pay-button" @click="handlePayment" :loading="loading">
        立即开通 {{ selectedPlan === 'month' ? '￥19/月' : '￥188/年' }}
      </el-button>
    </div>

    <el-dialog v-model="showQRCode" title="扫码支付" width="300px" center>
      <div class="qrcode-container">
        <div class="qrcode">
          <!-- 这里放二维码图片 -->
          <img :src="qrCodeUrl" alt="支付二维码">
        </div>
        <p>请使用{{ payMethod === 'alipay' ? '支付宝' : '微信' }}扫码支付</p>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { updateUserApi } from '@/api/user'

const router = useRouter()
const selectedPlan = ref('month')
const payMethod = ref('alipay')
const loading = ref(false)
const showQRCode = ref(false)
const qrCodeUrl = ref('')

const handlePayment = async () => {
  loading.value = true
  try {
    // 模拟支付过程
    showQRCode.value = true
    qrCodeUrl.value = payMethod.value === 'alipay' ? '/images/alipay-qr.png' : '/images/wechat-qr.png'
    
    // 模拟支付成功
    setTimeout(async () => {
      const userId = localStorage.getItem('userId')
      // 调用更新用户API
      const result = await updateUserApi({
        id: userId,
        isVip: true
      })
      
      if (result.code === 1) {
        // 更新本地存储
        localStorage.setItem('isVip', 'true')
        ElMessage.success('VIP开通成功！')
        router.push('/')
      }
      
      showQRCode.value = false
      loading.value = false
    }, 2000)
    
  } catch (error) {
    console.error('支付失败:', error)
    ElMessage.error('支付失败，请重试')
    loading.value = false
  }
}
</script>

<style scoped>
.vip-container {
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
}

.vip-header {
  text-align: center;
  margin-bottom: 40px;
}

.vip-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.crown-icon {
  font-size: 32px;
  color: #e6a23c;
}

.subtitle {
  color: #909399;
  margin-top: 10px;
}

.price-cards {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-bottom: 40px;
}

.price-card {
  position: relative;
  width: 300px;
  padding: 30px;
  border-radius: 12px;
  background: white;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s ease;
}

.price-card:hover {
  transform: translateY(-5px);
}

.price-card.active {
  border: 2px solid #e6a23c;
}

.best-value {
  position: absolute;
  top: -12px;
  right: -12px;
  background: #e6a23c;
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.price {
  margin: 20px 0;
  text-align: center;
}

.currency {
  font-size: 24px;
  color: #e6a23c;
}

.amount {
  font-size: 48px;
  font-weight: bold;
  color: #e6a23c;
}

.period {
  color: #909399;
}

.features {
  margin-top: 20px;
}

.features p {
  margin: 10px 0;
  color: #606266;
}

.features i {
  color: #67c23a;
  margin-right: 8px;
}

.payment-section {
  text-align: center;
}

.payment-methods {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin: 20px 0;
}

.payment-method {
  padding: 15px 30px;
  border: 2px solid #dcdfe6;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-method.active {
  border-color: #e6a23c;
  background: #fdf6ec;
}

.payment-method img {
  width: 40px;
  height: 40px;
  margin-right: 10px;
}

.pay-button {
  width: 300px;
  height: 50px;
  font-size: 18px;
  margin-top: 30px;
}

.qrcode-container {
  text-align: center;
}

.qrcode {
  width: 200px;
  height: 200px;
  margin: 0 auto 20px;
  border: 1px solid #dcdfe6;
  padding: 10px;
}

.qrcode img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style>