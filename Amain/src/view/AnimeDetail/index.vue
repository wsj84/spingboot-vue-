<template>
    <div class="anime-player-page">
        <div class="main-content">
            <div class="title-area">
                <el-button type="default" size="small" @click="goBack" icon="el-icon-arrow-left"
                    class="back-btn">返回</el-button>
                <h1>{{ anime.title }}</h1>
            </div>

            <div class="video-container">
                <!-- VIP检查弹窗 -->
                <el-dialog v-model="showVipDialog" title="VIP专属内容" width="400px" :close-on-click-modal="false"
                    :close-on-press-escape="false" :show-close="false">
                    <div class="vip-dialog-content">
                        <i class="el-icon-warning-outline vip-icon"></i>
                        <h3>此内容需要VIP会员</h3>
                        <p>开通VIP会员即可观看全部内容</p>
                    </div>
                    <template #footer>
                        <span class="dialog-footer">
                            <el-button @click="goToHome">返回首页</el-button>
                            <el-button type="warning" @click="goToVip">开通VIP</el-button>
                        </span>
                    </template>
                </el-dialog>


                <div class="video-container">
                    <!-- 挂在视频播放器组件 -->
                    <div id="video" v-if="canWatch">
                        <NPlayer  :video-url="videoUrl" :anime-id="parseInt(route.params.id)"
                            />
                    </div>
                    <div v-else class="video-placeholder">
                        <i class="el-icon-lock"></i>
                        <p>VIP专属内容</p>
                    </div>
                </div>
            </div>

            <div class="content-section">
                <div class="video-info">
                    <div class="interact-area">
                        <div class="action-buttons">
                            <button class="action-btn" @click="handleFavorite">
                                <i :class="isFavorited ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
                                {{ isFavorited ? '已收藏' : '收藏' }}
                            </button>
                            <button class="action-btn"><i class="el-icon-share"></i> 分享</button>
                        </div>
                        <p class="description">{{ anime.description }}</p>
                    </div>
                </div>

                <!-- 评论区 -->
                <div class="comments-section">
                    <div class="comment-form">
                        <h3>发表评论</h3>
                        <div class="comment-input-area">
                            <el-input type="textarea" v-model="commentContent" placeholder="说点什么吧..." :rows="3"
                                class="comment-input"></el-input>
                            <el-button type="primary" @click="submitComment" class="comment-submit-btn">发表评论</el-button>
                        </div>
                    </div>

                    <div class="comments-list">
                        <el-tabs v-model="activeTab">
                            <el-tab-pane label="全部评论" name="all">
                                <h3>全部评论 ({{ comments.length }})</h3>
                                <div v-if="comments.length === 0" class="no-comments">
                                    暂无评论，快来发表第一条评论吧！
                                </div>
                                <div v-else class="comment-items">
                                    <div v-for="comment in comments" :key="comment.id" class="comment-item"
                                        :class="{ 'my-comment': isCurrentUser(comment.userId) }">
                                        <div class="comment-avatar">
                                            <el-avatar :size="40">{{ comment.user.username.charAt(0) }}</el-avatar>
                                        </div>
                                        <div class="comment-content">
                                            <div class="comment-header">
                                                <span class="comment-username">
                                                    {{ comment.user.username }}
                                                    <el-tag v-if="isCurrentUser(comment.userId)" size="small"
                                                        type="info">我的评论</el-tag>
                                                </span>
                                                <span class="comment-time">{{ formatDateTime(comment.createdAt)
                                                    }}</span>
                                            </div>
                                            <div class="comment-text">{{ comment.content }}</div>
                                            <div class="comment-actions">
                                                <!-- 只在不是自己的评论时显示举报按钮 -->
                                                <button v-if="!isCurrentUser(comment.userId)"
                                                    class="comment-action-btn report-btn"
                                                    @click="handleReport(comment)">
                                                    <i class="el-icon-warning"></i> 举报
                                                </button>
                                                <button v-if="isCurrentUser(comment.userId)" class="comment-action-btn"
                                                    @click="deleteComment(comment.id)">
                                                    <i class="el-icon-delete"></i> 删除
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="我的评论" name="my">
                                <h3>我的评论 ({{ myComments.length }})</h3>
                                <div v-if="myComments.length === 0" class="no-comments">
                                    您还没有发表过评论！
                                </div>
                                <div v-else class="comment-items">
                                    <div v-for="comment in myComments" :key="comment.id" class="comment-item">
                                        <div class="comment-avatar">
                                            <el-avatar :size="40">{{ comment.user.username.charAt(0) }}</el-avatar>
                                        </div>
                                        <div class="comment-content">
                                            <div class="comment-header">
                                                <span class="comment-username">{{ comment.user.username }}</span>
                                                <span class="comment-time">{{ formatDateTime(comment.createdAt)
                                                }}</span>
                                            </div>
                                            <div class="comment-text">{{ comment.content }}</div>
                                            <div class="comment-actions">
                                                <button class="comment-action-btn" @click="deleteComment(comment.id)">
                                                    <i class="el-icon-delete"></i> 删除
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </el-tab-pane>
                        </el-tabs>
                    </div>
                </div>
            </div>
        </div>

        <div class="sidebar">
            <div class="tabs">
                <div class="tab active">简介</div>
            </div>

            <div class="anime-info">
                <div class="info-item">
                    <h3>作品信息</h3>
                    <p class="description">{{ anime.description }}</p>
                </div>

                <div class="recommended">
                    <h3>相关推荐</h3>
                    <div class="recommended-items">
                        <div v-for="item in recommendedAnimes" :key="item.id" class="rec-item"
                            @click="goToAnime(item.id)">
                            <div class="rec-thumbnail">
                                <img :src="item.thumbnailUrl || 'https://via.placeholder.com/150'" :alt="item.title">
                                <!-- 添加VIP标识 -->
                                <div class="vip-badge" v-if="item.isVipOnly">VIP</div>
                            </div>
                            <div class="rec-info">
                                <div class="rec-title">{{ item.title }}</div>
                                <div class="rec-views">{{ item.viewCount || 0 }}次观看</div>
                            </div>
                        </div>
                        <div v-if="recommendedAnimes.length === 0" class="no-recommendations">
                            暂无相关推荐
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { queryAnimebyid, incrementViewCountApi } from '@/api/anime';
import { addHistory } from '@/api/history';
import { addFavorite, deleteFavorite, checkFavorite } from '@/api/favorite';
import { queryCommentsApi, addCommentApi, deleteCommentApi, getUserCommentsApi } from '@/api/comment';
import { queryUserbyid } from '@/api/user';
import { getRandomAnimesByCategoryApi } from '@/api/anime';
import { addReportApi } from '@/api/report'
import NPlayer from '../NPlayer.vue'

const route = useRoute();
const router = useRouter();
const videoUrl = ref('');
const comments = ref([]);
const commentContent = ref('');
const isFavorited = ref(false);
const favoriteId = ref(null);
const activeTab = ref('all');
const myComments = ref([]);
const recommendedAnimes = ref([]);
const showVipDialog = ref(false);
const canWatch = ref(false);

// 初始化anime对象
const anime = ref({
    id: 0,
    title: '',
    description: '',
    videoUrl: '',
    duration: 0,
    thumbnailUrl: '',
    releaseDate: '',
});



// 获取动漫详情数据
const getAnimeDetail = async () => {
    try {
        const animeId = parseInt(route.params.id);
        const result = await queryAnimebyid(animeId);
        if (result.code === 1) {
            anime.value = result.data;
            videoUrl.value = result.data.videoUrl; // 设置视频URL
            console.log('Video URL:', videoUrl.value);
            checkWatchPermission(); // 添加权限检查
            // 获取动漫详情后加载推荐
            await loadRecommendedAnimes();
            // 增加观看次数
            await incrementViewCount(animeId);
        } else {
            ElMessage.error('获取动漫详情失败');
        }
    } catch (error) {
        console.error('获取动漫详情失败:', error);
        ElMessage.error('获取动漫详情失败');
    }
};

// 添加增加观看次数的方法
const incrementViewCount = async (animeId) => {
    try {
        await incrementViewCountApi(animeId);
    } catch (error) {
        console.error('增加观看次数失败:', error);
    }
};

// 添加观看记录
const addWatchHistory = async () => {
    try {
        const userId = localStorage.getItem('userId'); // 确保登录时保存了用户ID
        if (!userId) {
            ElMessage.warning('请先登录');
            router.push('/login');
            return;
        }

        await addHistory({
            animeId: parseInt(route.params.id),
            userId: parseInt(userId)
        });
    } catch (error) {
        console.error('添加观看记录失败:', error);
    }
};
const goBack = () => {
    router.back();
};


// 加载全部评论
const loadComments = async () => {
    try {
        const animeId = route.params.id;
        const result = await queryCommentsApi(1, 100, animeId, "");
        console.log('qurryresult:', result);
        if (result.code === 1) {
            // 为每个评论加载用户信息
            const commentsWithUser = await Promise.all(result.data.rows.map(async (comment) => {
                const userResult = await queryUserbyid(comment.userId);
                return {
                    ...comment,
                    user: userResult.code === 1 ? userResult.data : { username: '未知用户' }
                };
            }));
            comments.value = commentsWithUser;
        }
    } catch (error) {
        console.error('加载评论失败:', error);
        ElMessage.error('加载评论失败');
    }
};

// 提交评论
const submitComment = async () => {
    if (commentContent.value.trim() === '') {
        ElMessage.warning('评论内容不能为空');
        return;
    }

    const userId = localStorage.getItem('userId');
    if (!userId) {
        ElMessage.warning('请先登录');
        router.push('/login');
        return;
    }

    try {
        const response = await addCommentApi({
            content: commentContent.value,
            animeId: parseInt(route.params.id),
            userId: parseInt(userId)
        });

        if (response.code === 1) {
            ElMessage.success('评论成功');
            commentContent.value = '';
            // 重新加载评论
            await loadComments();
            await loadMyComments();
        } else {
            ElMessage.error('评论失败');
        }
    } catch (error) {
        console.error('评论失败:', error);
        ElMessage.error('评论失败');
    }
};

// 删除评论
const deleteComment = async (commentId) => {
    try {
        await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        });

        const result = await deleteCommentApi(commentId);
        if (result.code === 1) {
            ElMessage.success('删除成功');
            // 重新加载评论
            await loadComments();
            await loadMyComments();
        } else {
            ElMessage.error('删除失败');
        }
    } catch (error) {
        if (error !== 'cancel') {
            console.error('删除评论失败:', error);
            ElMessage.error('删除失败');
        }
    }
};

// 添加举报处理方法
const handleReport = async (comment) => {
    try {
        const userId = localStorage.getItem('userId');
        if (!userId) {
            ElMessage.warning('请先登录');
            router.push('/login');
            return;
        }

        await ElMessageBox.prompt('请输入举报原因', '举报评论', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputPlaceholder: '请详细描述举报原因',
            inputValidator: (value) => {
                if (!value) {
                    return '举报原因不能为空';
                }
                return true;
            }
        }).then(async ({ value: reason }) => {
            console.log('commentid:', comment.id);
            const report = {
                reason,
                userId: parseInt(userId),
                reportedUserId: comment.userId,
                commentId: comment.id
            };

            const result = await addReportApi(report);
            if (result.code === 1) {
                ElMessage.success('举报成功,管理员会尽快处理');
            } else {
                ElMessage.error(result.msg || '举报失败');
            }
        });
    } catch (error) {
        if (error !== 'cancel') {
            console.error('举报失败:', error);
            ElMessage.error('举报失败');
        }
    }
};

// 检查是否已收藏
const checkFavoriteStatus = async () => {
    try {
        const userId = localStorage.getItem('userId');
        if (!userId) return;

        const animeId = route.params.id;
        const response = await checkFavorite(animeId, userId);
        if (response.code === 1) {
            isFavorited.value = response.data.isFavorite;
            favoriteId.value = response.data.id;
        }
    } catch (error) {
        console.error('检查收藏状态失败:', error);
    }
};

// 处理收藏/取消收藏
const handleFavorite = async () => {
    try {
        const userId = localStorage.getItem('userId');
        if (!userId) {
            ElMessage.warning('请先登录');
            router.push('/login');
            return;
        }

        if (isFavorited.value) {
            // 取消收藏
            const response = await deleteFavorite(favoriteId.value);
            if (response.code === 1) {
                isFavorited.value = false;
                favoriteId.value = null;
                ElMessage.success('取消收藏成功');
            }
        } else {
            // 添加收藏
            const response = await addFavorite({
                animeId: parseInt(route.params.id),
                userId: parseInt(userId)
            });
            if (response.code === 1) {
                ElMessage.success('收藏成功');
            }
            checkFavoriteStatus(); // 添加检查收藏状态
        }
    } catch (error) {
        console.error('收藏操作失败:', error);
        ElMessage.error('操作失败，请重试');
    }
};


// 格式化日期时间
const formatDateTime = (dateTime) => {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    return date.toLocaleString();
};

// 判断是否是当前用户的评论
const isCurrentUser = (commentUserId) => {
    const currentUserId = parseInt(localStorage.getItem('userId'));
    return currentUserId === commentUserId;
};

// 加载我的评论
const loadMyComments = async () => {
    try {
        const animeId = route.params.id;
        const userId = localStorage.getItem('userId');
        const username = localStorage.getItem('username');
        if (!userId) return;

        const result = await queryCommentsApi(1, 100, animeId, userId);

        if (result.code === 1) {
            // 为每条评论添加用户信息
            myComments.value = result.data.rows.map(comment => ({
                ...comment,
                user: {
                    id: userId,
                    username: username
                }
            }));
            console.log('我的评论:', myComments.value);
        }
    } catch (error) {
        console.error('加载我的评论失败:', error);
    }
};

// 加载推荐动漫
const loadRecommendedAnimes = async () => {
    try {
        if (!anime.value.categoryId) return;

        const result = await getRandomAnimesByCategoryApi(
            anime.value.categoryId,
            3,  // 限制显示3个推荐
            anime.value.id // 排除当前动漫
        );

        if (result.code === 1) {
            recommendedAnimes.value = result.data;
            console.log('推荐动漫:', recommendedAnimes.value);
        }
    } catch (error) {
        console.error('加载推荐动漫失败:', error);
    }
};

// 跳转到推荐动漫
const goToAnime = async (animeId) => {
    router.push(`/AnimeDetail/${animeId}`);
    // 等待路由更新完成
    await router.isReady();
    // 重新加载数据
    await getAnimeDetail();
    await loadComments();
    await checkFavoriteStatus();
};

// 监听路由参数变化
watch(
    () => route.params.id,
    async (newId, oldId) => {
        if (newId && newId !== oldId) {
            await getAnimeDetail();
            await loadComments();
            await checkFavoriteStatus();
        }
    }
);


// 检查观看权限
const checkWatchPermission = () => {

    const isVip = localStorage.getItem('isVip') === 'true';
    if (anime.value.isVipOnly && !isVip) {
        showVipDialog.value = true;
        canWatch.value = false;
    } else {
        showVipDialog.value = false;
        canWatch.value = true;
    }
};

// 跳转到VIP购买页面
const goToVip = () => {
    router.push('/vip');
};

// 返回首页
const goToHome = () => {
    router.push('/');
};

onMounted(() => {
    getAnimeDetail();
    addWatchHistory();
    checkFavoriteStatus(); // 添加检查收藏状态
    loadComments(); // 加载当前动漫的评论
    loadMyComments();

});

</script>

<style scoped>
.anime-player-page {
    display: flex;
    background-color: #f0f2f5;
    min-height: 100vh;
    padding: 20px;
    gap: 20px;
}

.main-content {
    flex: 1;
}

.title-area {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

.title-area h1 {
    margin: 0;
    margin-left: 15px;
    font-size: 24px;
    color: #333;
}


.video-info {
    padding: 15px;
    background-color: white;
    border-radius: 4px;
    margin-top: 15px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.interact-area {
    margin-bottom: 15px;
}

.action-buttons {
    display: flex;
    margin-bottom: 10px;
}

.action-btn {
    display: flex;
    align-items: center;
    background-color: #f4f4f5;
    border: 1px solid #dcdfe6;
    color: #606266;
    padding: 6px 12px;
    margin-right: 10px;
    border-radius: 4px;
    cursor: pointer;
}

.action-btn:hover {
    background-color: #ecf5ff;
    color: #409EFF;
}

.description {
    color: #606266;
    font-size: 14px;
    line-height: 1.5;
}

.sidebar {
    width: 300px;
    background-color: white;
    border-left: 1px solid #e4e7ed;
    overflow-y: auto;
    box-shadow: -2px 0 8px rgba(0, 0, 0, 0.05);
}

.tabs {
    display: flex;
    border-bottom: 1px solid #e4e7ed;
}

.tab {
    flex: 1;
    text-align: center;
    padding: 12px 0;
    cursor: pointer;
    color: #606266;
}

.tab.active {
    color: #409EFF;
    border-bottom: 2px solid #409EFF;
}

.anime-info {
    padding: 15px;
}

.info-item {
    margin-bottom: 20px;
}

.info-item h3 {
    margin-top: 0;
    margin-bottom: 10px;
    font-size: 16px;
    color: #303133;
}

.recommended {
    padding: 15px;
}

.recommended h3 {
    margin-top: 0;
    margin-bottom: 10px;
    font-size: 16px;
    color: #303133;
}

.recommended-items {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.rec-item {
    display: flex;
    cursor: pointer;
    border-radius: 4px;
    padding: 8px;
    transition: background-color 0.3s;
}

.rec-item:hover {
    background-color: #f5f7fa;
}

.rec-thumbnail {
    position: relative;
    /* 添加相对定位 */
    width: 120px;
    height: 70px;
    overflow: hidden;
    border-radius: 4px;
    margin-right: 10px;
}

.vip-badge {
    position: absolute;
    top: 4px;
    right: 4px;
    background-color: #e6a23c;
    color: white;
    padding: 1px 4px;
    border-radius: 2px;
    font-size: 10px;
    font-weight: bold;
    z-index: 1;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.rec-thumbnail img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.rec-info {
    flex: 1;
}

.rec-title {
    font-size: 14px;
    margin-bottom: 4px;
    color: #303133;
}

.rec-views {
    font-size: 12px;
    color: #909399;
}

/* 评论区样式 */
.comments-section {
    margin-top: 30px;
    border-top: 1px solid #e4e7ed;
    padding-top: 20px;
}

.comment-form {
    margin-bottom: 20px;
}

.comment-form h3 {
    margin-top: 0;
    margin-bottom: 10px;
    font-size: 16px;
    color: #303133;
}

.comment-input-area {
    display: flex;
    flex-direction: column;
}

.comment-input {
    margin-bottom: 10px;
}

.comment-submit-btn {
    align-self: flex-end;
}

.comments-list h3 {
    margin-top: 0;
    margin-bottom: 15px;
    font-size: 16px;
    color: #303133;
    padding-bottom: 10px;
    border-bottom: 1px solid #e4e7ed;
}

.no-comments {
    color: #909399;
    text-align: center;
    padding: 20px 0;
}

.comment-items {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.comment-item {
    display: flex;
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;
}

.comment-avatar {
    margin-right: 12px;
}

.comment-content {
    flex: 1;
}

.comment-header {
    margin-bottom: 5px;
    display: flex;
    align-items: center;
}

.comment-username {
    font-weight: 500;
    color: #303133;
    margin-right: 10px;
}

.comment-time {
    font-size: 12px;
    color: #909399;
}

.comment-text {
    margin-bottom: 8px;
    line-height: 1.5;
    color: #606266;
    word-break: break-word;
}

.comment-actions {
    display: flex;
    gap: 15px;
}

.comment-action-btn {
    background: none;
    border: none;
    color: #909399;
    font-size: 13px;
    cursor: pointer;
    padding: 0;
    display: flex;
    align-items: center;
}

.comment-action-btn i {
    margin-right: 3px;
}

.comment-action-btn:hover {
    color: #409EFF;
}

@media (max-width: 992px) {
    .anime-player-page {
        flex-direction: column;
    }

    .sidebar {
        width: 100%;
        border-left: none;
        border-top: 1px solid #e4e7ed;
    }
}

.action-btn .el-icon-star-on {
    color: #f7ba2a;
}

.el-tabs {
    margin-top: 20px;
}

.no-recommendations {
    color: #909399;
    text-align: center;
    padding: 20px 0;
    font-size: 14px;
}

.video-placeholder {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.8);
    color: #e6a23c;
}

.video-placeholder i {
    font-size: 48px;
    margin-bottom: 16px;
}

.video-placeholder p {
    font-size: 18px;
    color: #fff;
}

.vip-dialog-content {
    text-align: center;
    padding: 20px 0;
}

.vip-icon {
    font-size: 48px;
    color: #e6a23c;
    margin-bottom: 16px;
}

.vip-dialog-content h3 {
    font-size: 18px;
    color: #303133;
    margin-bottom: 8px;
}

.vip-dialog-content p {
    color: #606266;
    font-size: 14px;
}

.dialog-footer {
    text-align: center;
    width: 100%;
}

.dialog-footer .el-button {
    margin: 0 10px;
}

.player-wrapper {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
}

.video-container {
    position: relative;
    width: 100%;
    background: #000;
    border-radius: 4px;
    overflow: hidden;
    aspect-ratio: 16/9;
}

/* 添加举报按钮样式 */
.comment-action-btn.report-btn {
    color: #f56c6c;
}

.comment-action-btn.report-btn:hover {
    color: #ff4949;
}

/* 添加自己评论的样式 */
.comment-item.my-comment {
    background-color: #f8f9fa;
    border-radius: 8px;
    padding: 12px;
}

.comment-username .el-tag {
    margin-left: 8px;
    font-size: 12px;
}
</style>