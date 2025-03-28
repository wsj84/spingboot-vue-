<script setup>
import { ref, onMounted, computed, nextTick } from 'vue'
import { Document, Menu as IconMenu, Location, Setting, User, Film, List, ChatDotRound, MessageBox } from '@element-plus/icons-vue'
import { ElMenu, ElMenuItem, ElContainer, ElHeader, ElMain, ElAside, ElRow, ElCol, ElInput, ElSubMenu, ElButton,ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { getCurrentUser } from '@/api/loginInfo.js'
const handleSelect = () => {
    // TODO: 根据需求添加选中处理逻辑
}

const handleOpen = () => {
    console.log(key, keyPath)
}

const handleClose = () => {
    try {
        // 调用退出接口
        if (1) {
            // 清除本地存储的token和管理员token
            localStorage.removeItem('token')
            localStorage.removeItem('userId')
            localStorage.removeItem('admin-token')
            localStorage.removeItem('username')
            localStorage.removeItem('isVip');
            localStorage.removeItem('isActive');
            // 清除用户信息
            userInfo.value = null
            isAdmin.value = false
            // 提示退出成功
            ElMessage.success('退出成功')
            // 跳转到登录页
            router.push('/index')
        } else {
            ElMessage.error(response.msg || '退出失败')
        }
    } catch (error) {
        console.error('退出失败:', error)
        ElMessage.error('退出失败，请重试')
    }
}

const input = ref('')

// 修改用户角色检测
const isAdmin = ref(false)
const userInfo = ref(null)

// 修改检查用户角色的函数
const checkUserRole = async () => {
    const token = localStorage.getItem('token')
    if (token) {
        try {
            const response = await getCurrentUser(token)
            if (response.code === 1 && response.data) {
                userInfo.value = response.data
                console.log('用户信息:', userInfo.value)
                isAdmin.value = response.data.role === 'admin'
            }
        } catch (error) {
            console.error('获取用户信息失败:', error)
            userInfo.value = null
            isAdmin.value = false
        }
    }
}

// VIP 购买按钮点击处理
const handleVipPurchase = () => {
    router.push('/vip')
}

const router = useRouter()
const route = useRoute()

// 计算当前激活的菜单项
const activeMenu = computed(() => {
    return route.path
})

onMounted(() => {
    checkUserRole()
})

</script>

<template>
    <div class="common-layout">
        <el-container class="container">
            <!-- 头部 -->
            <el-header class="layout-header">
                <el-menu class="el-menu-demo" mode="horizontal" :ellipsis="false" @select="handleSelect" router>
                    <el-menu-item index="/index">
                        <h1>动漫网站</h1>
                    </el-menu-item>
                    <!-- 添加 VIP 购买按钮 -->
                    <el-menu-item v-if="userInfo && !userInfo.isVip" index="/vip">
                        <el-button type="warning" size="small">开通VIP</el-button>
                    </el-menu-item>
                    <el-menu-item v-if="userInfo" index="/index" @click="handleClose">
                        退出登录
                    </el-menu-item>
                    <el-menu-item v-else index="/Login">
                        前往登录
                    </el-menu-item>
                </el-menu>
            </el-header>

            <el-container class="body-container">
                <!-- 左侧菜单栏 -->
                <el-aside width="220px" class="layout-aside">
                    <div class="menu-header">
                        <div class="user-info">
                            <div class="user-name">{{ userInfo?.username || '未登录' }}</div>
                            <!-- 修改用户身份显示 -->
                            <template v-if="userInfo">
                                <div class="user-role" v-if="isAdmin">管理员</div>
                                <div class="user-role" v-else :class="{ 'vip-user': userInfo.isVip }">
                                    {{ userInfo.isVip ? 'VIP会员' : '普通用户' }}
                                </div>
                            </template>
                            <div class="user-role" v-else>游客</div>
                        </div>
                    </div>

                    <el-menu class="el-menu-vertical" :default-active="activeMenu" router :unique-opened="true"
                        active-text-color="#409EFF" background-color="#ffffff">
                        <!-- 所有用户共有的菜单项 -->
                        <el-menu-item index="/index" class="menu-item">
                            <el-icon>
                                <Location />
                            </el-icon>
                            <span>首页</span>
                        </el-menu-item>

                        <el-menu-item index="/ranking" class="menu-item">
                            <el-icon><Medal /></el-icon>
                            <span>排行榜</span>
                        </el-menu-item>

                        <el-menu-item index="/community" class="menu-item">
                            <el-icon><Promotion /></el-icon>
                            <span>社区论坛</span>
                        </el-menu-item>

                        <el-menu-item index="/history" class="menu-item">
                            <el-icon>
                                <IconMenu />
                            </el-icon>
                            <span>观看历史</span>
                        </el-menu-item>

                        <el-menu-item index="/MyInfo" class="menu-item">
                            <el-icon>
                                <User />
                            </el-icon>
                            <span>我的</span>
                        </el-menu-item>

                        <!-- 管理员专属菜单项 -->
                        <template v-if="isAdmin">
                            <el-divider class="menu-divider">管理功能</el-divider>

                            <el-menu-item index="/users" class="menu-item">
                                <el-icon>
                                    <User />
                                </el-icon>
                                <span>管理用户</span>
                            </el-menu-item>

                            <el-menu-item index="/animes" class="menu-item">
                                <el-icon>
                                    <Film />
                                </el-icon>
                                <span>管理动漫</span>
                            </el-menu-item>

                            <el-menu-item index="/categories" class="menu-item">
                                <el-icon>
                                    <List />
                                </el-icon>
                                <span>管理分类</span>
                            </el-menu-item>

                            <el-menu-item index="/comments" class="menu-item">
                                <el-icon>
                                    <ChatDotRound />
                                </el-icon>
                                <span>管理评论</span>
                            </el-menu-item>

                            <el-menu-item index="/danmus" class="menu-item">
                                <el-icon>
                                    <MessageBox />
                                </el-icon>
                                <span>管理弹幕</span>
                            </el-menu-item>

                            <el-menu-item index="/report" class="menu-item">
                                <el-icon>
                                    <MessageBox />
                                </el-icon>
                                <span>查看举报</span>
                            </el-menu-item>

                            <el-menu-item index="/posts" class="menu-item">
                                <el-icon>
                                    <MessageBox />
                                </el-icon>
                                <span>查看帖子详情</span>
                            </el-menu-item>
                        </template>
                    </el-menu>
                </el-aside>

                <!-- 中间内容区 -->
                <el-main class="layout-main">
                    <router-view />
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<style scoped>
.common-layout {
    min-height: 100vh;
    background-color: #f8f9fa; /* 更柔和的背景色 */
}

/* 整个容器铺满屏幕 */
.container {
    height: 100vh;
    display: flex;
    flex-direction: column;
}

/* 头部样式 */
.layout-header {
    background-color: #fff;
    border-bottom: 1px solid #eaecef;
    padding: 0 24px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    position: relative;
    z-index: 1000;
    height: 64px; /* 适当增加高度 */
    line-height: 64px;
}

/* 剩余内容区撑满屏幕 */
.body-container {
    flex: 1;
    display: flex;
}

/* 左侧菜单栏样式 */
.layout-aside {
    background-color: #fff;
    border-right: 1px solid #eaecef;
    line-height: normal;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    position: relative;
    z-index: 999;
    transition: all 0.3s ease;
}

/* 菜单头部用户信息 */
.menu-header {
    padding: 24px 20px;
    display: flex;
    align-items: center;
    border-bottom: 1px solid #f0f2f5;
    background-color: #f8f9fb; /* 轻微的背景色区分 */
}

.user-avatar {
    margin-right: 14px;
}

.user-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.user-name {
    font-size: 16px;
    font-weight: 600; /* 加粗字体 */
    color: #2c3e50; /* 更深的颜色 */
    margin-bottom: 4px;
}

.user-role {
    font-size: 13px;
    color: #5e6d82;
    font-weight: 400;
}

/* VIP 用户身份样式 */
.user-role.vip-user {
    color: #ff9800; /* 更亮的金色 */
    font-weight: 600;
}

/* 菜单分隔线 */
.menu-divider {
    margin: 18px 0;
    padding: 0 20px;
    color: #5e6d82;
    font-size: 12px;
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 1.2px;
}

/* 菜单项样式 */
.menu-item {
    height: 54px; /* 增加高度 */
    line-height: 54px;
    padding: 0 22px;
    margin: 6px 10px;
    border-radius: 6px; /* 增加圆角 */
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    font-weight: 500;
}

.el-menu-vertical {
    border-right: none;
    padding: 8px 0;
}

/* 清除 el-col 的内边距和外边距 */
.layout-aside .el-col {
    padding: 0;
    margin: 0;
}

/* 让菜单宽度充满父容器 */
.layout-aside .el-menu {
    width: 100%;
}

/* 中间内容区样式 */
.layout-main {
    background-color: #fff;
    padding: 24px;
    flex: 1;
    overflow: auto;
    border-radius: 8px; /* 增加圆角 */
    margin: 16px; /* 增加边距 */
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.03); /* 轻微阴影 */
}

/* 自动将第一个菜单项推到左侧 */
.el-menu--horizontal>.el-menu-item:nth-child(1) {
    margin-right: auto;
}

.tac {
    text-align: center;
}

.menu-search {
    flex-grow: 1;
    display: flex;
    justify-content: center;
    align-items: center;
}

.menu-search .el-input {
    width: 700px;
    height: 40px; /* 稍微增加高度 */
}

:deep(.el-menu--horizontal > .el-menu-item.is-active) {
    background: transparent !important;
}

:deep(.el-menu-item.is-active) {
    background-color: #e6f1ff !important; /* 更亮的蓝色背景 */
    color: #3a8ee6 !important; /* 更亮的蓝色文字 */
    border-radius: 6px;
    font-weight: 600;
}

:deep(.el-menu-item:hover) {
    background-color: #f2f6fc !important; /* 更淡的悬停背景 */
    border-radius: 6px;
}

/* VIP 购买按钮样式 */
.el-menu-demo .el-button--warning {
    background-color: #fff8e6;
    border-color: #ff9800;
    color: #ff9800;
    border-radius: 20px; /* 圆角按钮 */
    padding: 8px 16px;
    font-weight: 600;
    transition: all 0.3s ease;
}

.el-menu-demo .el-button--warning:hover {
    background-color: #ff9800;
    border-color: #ff9800;
    color: #fff;
    transform: translateY(-2px); /* 微微上浮效果 */
    box-shadow: 0 4px 8px rgba(255, 152, 0, 0.3); /* 增加阴影 */
}

/* 菜单项悬停效果 */
:deep(.el-menu-item:hover) {
    background-color: #f2f6fc !important;
    color: #3a8ee6 !important;
    transform: translateX(5px);
}

/* 选中菜单项样式 */
:deep(.el-menu-item.is-active) {
    background-color: #e6f1ff !important;
    color: #3a8ee6 !important;
    font-weight: 600;
    position: relative;
    box-shadow: 0 2px 8px rgba(58, 142, 230, 0.1); /* 轻微阴影 */
}

/* 选中菜单项左边框效果 */
:deep(.el-menu-item.is-active::before) {
    content: '';
    position: absolute;
    left: 0;
    top: 0;
    width: 4px;
    height: 100%;
    background-color: #3a8ee6;
    border-radius: 0 4px 4px 0;
}

/* 头部菜单项样式 */
:deep(.el-menu--horizontal > .el-menu-item) {
    height: 64px;
    line-height: 64px;
    transition: all 0.3s ease;
    font-weight: 500;
}

:deep(.el-menu--horizontal > .el-menu-item:hover) {
    background-color: #f2f6fc !important;
    color: #3a8ee6 !important;
}

:deep(.el-menu--horizontal > .el-menu-item.is-active) {
    border-bottom: 3px solid #3a8ee6;
    color: #3a8ee6 !important;
    font-weight: 600;
}

/* 菜单图标样式优化 */
:deep(.el-menu-item .el-icon) {
    margin-right: 12px;
    font-size: 18px;
    transition: all 0.3s ease;
    color: #5e6d82; /* 统一图标颜色 */
}

:deep(.el-menu-item:hover .el-icon) {
    transform: scale(1.1);
    color: #3a8ee6; /* 悬停时图标颜色 */
}

:deep(.el-menu-item.is-active .el-icon) {
    color: #3a8ee6; /* 激活时图标颜色 */
}
</style>