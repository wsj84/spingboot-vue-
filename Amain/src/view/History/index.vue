<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { getHistory } from '@/api/history';
import { ElMessage } from 'element-plus';
const router = useRouter();
const historyList = ref([]);

const viewAnime = (id) => {
  router.push(`/AnimeDetail/${id}`);
};

// 添加分页相关数据
const currentPage = ref(1);
const pageSize = ref(8);
const total = ref(100);

// 获取观看历史
const getWatchHistory = async () => {
  try {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      ElMessage.warning('请先登录');
      router.push('/login');
      return;
    }

    const response = await getHistory(parseInt(userId), currentPage.value, pageSize.value);
    if (response.code === 1) {
      historyList.value = response.data.rows;
      total.value = response.data.total;
      console.log('获取观看历史成功:', historyList.value);
    }
  } catch (error) {
    console.error('获取观看历史失败:', error);
    ElMessage.error('获取观看历史失败');
  }
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  getWatchHistory();
};

const handleSizeChange = (val) => {
  pageSize.value = val;
  getWatchHistory();
};

const sortOption = ref('recent');

onMounted(() => {
  getWatchHistory();
});
</script>

<template>  
  <div class="anime-container">
    <div class="header">
      <h1>观看历史</h1>
    </div>

    <div class="anime-grid">
      <div v-for="item in historyList" 
           :key="item.id" 
           class="anime-item" 
           @click="viewAnime(item.animeId)">
        <div class="cover-container">
          <img :src="item.thumbnailUrl" alt="封面" class="anime-cover">
          <span class="watch-time">{{ new Date(item.watchedAt).toLocaleDateString() }}</span>
          <!-- 添加VIP标识 -->
          <div class="vip-badge" v-if="item.isVipOnly">VIP</div>
        </div>
        <div class="anime-title">{{ item.title }}</div>
      </div>
    </div>

    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        background
        layout="prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<style scoped>
.watch-time {
  position: absolute;
  top: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-bottom-left-radius: 4px;
}

.empty-text {
  text-align: center;
  color: #909399;
  margin-top: 40px;
  font-size: 14px;
}
.anime-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 10px 15px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h1 {
  font-size: 22px;
  color: #333;
  margin: 0;
}

.filter-controls {
  display: flex;
  gap: 10px;
}

.anime-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 15px;
}

.anime-item {
  cursor: pointer;
  transition: transform 0.2s;
}

.anime-item:hover {
  transform: translateY(-3px);
}

.cover-container {
  position: relative;
  width: 100%;
  border-radius: 4px;
  overflow: hidden;
  aspect-ratio: 3/4;
}

.anime-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.episode-badge {
  position: absolute;
  bottom: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-top-left-radius: 4px;
}

.anime-title {
  font-size: 14px;
  margin-top: 6px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.2;
}

.pagination-wrapper {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

/* 响应式布局 */
@media (max-width: 1200px) {
  .anime-grid {
    grid-template-columns: repeat(5, 1fr);
  }
}

@media (max-width: 992px) {
  .anime-grid {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 768px) {
  .anime-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .filter-controls {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .anime-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
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
</style>
