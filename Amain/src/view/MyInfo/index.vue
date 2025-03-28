<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getFavorites, deleteFavorite } from '@/api/favorite';
import { updateUserApi } from '@/api/user';
import { ElMessage } from 'element-plus';
import { getCurrentUser } from '../../api/loginInfo';

const router = useRouter();
const user = ref({});
const currentPage = ref(1)
const pageSize = ref(3)
const total = ref(0)
const getuser = async () => {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            ElMessage.warning('请先登录');
            router.push('/login');
            return;
        }
        const response = await getCurrentUser(token);
        if (response.code === 1) {
            user.value = response.data;
            // 修改为分页获取收藏
            const favoritesResponse = await getFavorites(
                response.data.id,
                currentPage.value,
                pageSize.value
            );
            if (favoritesResponse.code === 1) {
                animes.value = favoritesResponse.data.rows;
                console.log('获取收藏成功:', animes.value);
                total.value = favoritesResponse.data.total;
            }
        }
    } catch (error) {
        console.error('获取用户信息失败:', error);
        ElMessage.error('获取用户信息失败');
    }
};
// 添加分页处理方法
const handleCurrentChange = (val) => {
    currentPage.value = val;
    getuser();
};

const handleSizeChange = (val) => {
    pageSize.value = val;
    currentPage.value = 1;
    getuser();
};
onMounted(() => {
    getuser();
});


const viewAnime = (id) => {
    router.push(`/AnimeDetail/${id}`);
};

const animes = ref([]);

const editDialogVisible = ref(false)
const editForm = ref({
    username: '',
    email: '',
    password: '',
    confirmPassword: ''
})

const rules = {
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
    ],
    email: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
    ],
    password: [
        { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
    ],
    confirmPassword: [
        {
            validator: (rule, value, callback) => {
                if (value === '') {
                    callback();
                } else if (value !== editForm.value.password) {
                    callback(new Error('两次输入的密码不一致'));
                } else {
                    callback();
                }
            }, trigger: 'blur'
        }
    ]
}

const edit = () => {
    editForm.value = {
        username: user.value.username,
        email: user.value.email,
        password: '',
        confirmPassword: ''
    }
    editDialogVisible.value = true
}

const handleEditSubmit = async () => {
    try {
        // 如果没有输入密码，则不发送密码字段
        const submitData = {
            id: user.value.id,
            username: editForm.value.username,
            email: editForm.value.email,
            isActive: user.value.isActive
        }
        if (editForm.value.password) {
            submitData.password = editForm.value.password
        }

        const response = await updateUserApi(submitData)
        if (response.code === 1) {
            ElMessage.success('更新成功')
            editDialogVisible.value = false
            getuser() // 重新获取用户信息
        } else {
            ElMessage.error(response.msg || '更新失败')
        }
    } catch (error) {
        console.error('更新用户信息失败:', error)
        ElMessage.error('更新失败')
    }
}

const removeFavorite = (id) => {
    if (confirm('确定要取消收藏吗？')) {
        // TODO: 调用后端API取消收藏
        deleteFavorite(id)
            .then(response => {
                if (response.code === 1) {
                    getuser();
                    ElMessage.success('取消收藏成功');
                } else {
                    ElMessage.error(response.msg || '取消收藏失败');
                }
            })
            .catch(error => {
                console.error('取消收藏失败:', error);
                ElMessage.error('取消收藏失败，请重试');
            });

    }
};
</script>

<template>
    <div class="my-info">
        <el-card class="user-card" shadow="hover">
            <div class="header">
                <el-avatar size="large" :src="user.avatar"></el-avatar>
                <div class="user-info">
                    <h2>{{ user.username }}</h2>
                    <p>{{ user.email }}</p>
                </div>
            </div>
            <el-divider></el-divider>
            <el-descriptions title="个人信息" :column="1">
                <el-descriptions-item label="用户ID">{{ user.id }}</el-descriptions-item>
                <el-descriptions-item label="角色">{{ user.role }}</el-descriptions-item>
                <el-descriptions-item label="账号状态">{{ user.isActive ? '激活' : '未激活' }}</el-descriptions-item>
                <el-descriptions-item label="注册时间">{{ user.createdAt }}</el-descriptions-item>
            </el-descriptions>
            <div class="actions">
                <el-button type="primary" @click="edit">编辑信息</el-button>
            </div>
        </el-card>

        <el-card class="favorites-card" shadow="hover">
            <template #header>
                <div class="favorites-header">
                    <h2 class="section-title">我的收藏</h2>
                    <el-tag type="success" effect="dark">共 {{ total }} 部作品</el-tag>
                </div>
            </template>
            <div class="favorites-container">
                <div class="favorites-grid">
                    <el-card v-for="anime in animes" :key="anime.id" shadow="hover" class="favorite-item">
                        <div class="favorite-content">
                            <div class="image-wrapper">
                                <img :src="anime.thumbnailUrl" :alt="anime.title">
                                <!-- 添加VIP标识 -->
                                <div class="vip-badge" v-if="anime.isVipOnly">VIP</div>
                            </div>
                            <div class="favorite-info">
                                <h3 class="anime-title">{{ anime.title }}</h3>
                                <p class="description">{{ anime.description }}</p>
                                <div class="favorite-meta">
                                    <span class="favorite-time">收藏于: {{ anime.favoritedAt }}</span>
                                    <div class="favorite-actions">
                                        <el-button type="primary" size="small" plain @click="viewAnime(anime.animeId)">
                                            查看详情
                                        </el-button>
                                        <el-button type="danger" size="small" plain @click="removeFavorite(anime.id)">
                                            取消收藏
                                        </el-button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </el-card>
                </div>
                <div class="pagination-wrapper">
                    <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize"
                        :page-sizes="[3, 8, 12, 16, 24]" :total="total" layout="total, sizes, prev, pager, next, jumper"
                        @size-change="handleSizeChange" @current-change="handleCurrentChange" background />
                </div>
            </div>
        </el-card>
    </div>

    <el-dialog v-model="editDialogVisible" title="编辑个人信息" width="500px">
        <el-form :model="editForm" :rules="rules" label-width="80px">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="editForm.username" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="editForm.email" />
            </el-form-item>
            <el-form-item label="新密码" prop="password">
                <el-input v-model="editForm.password" type="password" placeholder="不修改密码请留空" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="editForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleEditSubmit">确定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<style scoped>
.pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 30px;
    padding: 20px 0;
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.el-pagination {
    padding: 0;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
    .favorites-grid {
        grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
        gap: 15px;
    }

    .favorite-info {
        padding: 12px;
    }

    .anime-title {
        font-size: 16px;
    }
}

/* 当没有数据时的样式 */
.no-data {
    text-align: center;
    color: #909399;
    padding: 30px 0;
}

.my-info {
    max-width: 1200px;
    margin: 20px auto;
    padding: 0 20px;
}

.user-card {
    margin-bottom: 20px;
}

.header {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.user-info {
    margin-left: 20px;
}

.user-info h2 {
    margin: 0;
    font-size: 24px;
}

.favorites-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.favorites-container {
    padding: 20px;
}

.favorites-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 25px;
    margin-bottom: 30px;
}

.favorite-item {
    border-radius: 8px;
    overflow: hidden;
    transition: all 0.3s ease;
    border: none;
    background-color: white;
}

.favorite-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.image-wrapper {
    position: relative;
    width: 100%;
    padding-top: 56.25%;
    /* 16:9 宽高比 */
    overflow: hidden;
}

.image-wrapper img {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s ease;
}

.favorite-item:hover .image-wrapper img {
    transform: scale(1.05);
}

.vip-badge {
    position: absolute;
    top: 10px;
    right: 10px;
    background-color: #e6a23c;
    color: white;
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 12px;
    font-weight: bold;
    z-index: 1;
}

.favorite-info {
    padding: 16px;
}

.anime-title {
    font-size: 18px;
    font-weight: 600;
    color: #2c3e50;
    margin: 0 0 12px 0;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.description {
    font-size: 14px;
    color: #5c6b7f;
    margin: 0 0 16px 0;
    display: -webkit-box;
    line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    line-height: 1.5;
}

.favorite-meta {
    border-top: 1px solid #eee;
    padding-top: 16px;
}

.favorite-time {
    display: block;
    font-size: 13px;
    color: #8c9bab;
    margin-bottom: 12px;
}

.favorite-actions {
    display: flex;
    gap: 12px;
    justify-content: flex-end;
}

.favorite-actions .el-button {
    padding: 8px 16px;
    font-weight: 500;
}

.section-title {
    font-size: 24px;
    color: #2c3e50;
    margin: 0;
    font-weight: 600;
}

.favorites-card {
    background-color: #f9f9f9;
    border-radius: 8px;
}

.avatar-preview {
    display: none;
}
</style>
