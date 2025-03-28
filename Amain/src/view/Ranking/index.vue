<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAnimeRankingApi } from '@/api/anime'
import { useRouter } from 'vue-router'

const router = useRouter()
const rankingList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const loading = ref(false)

// 加载排行榜数据
const loadRanking = async () => {
  loading.value = true
  try {
    const result = await getAnimeRankingApi(currentPage.value, pageSize.value)
    if (result.code === 1) {
        console.log('排行榜数据:', result.data); // 添加调试日志
      rankingList.value = result.data.rows
      totalItems.value = result.data.total
    } else {
      ElMessage.error(result.msg || '获取排行榜数据失败')
    }
  } catch (error) {
    console.error('加载排行榜失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  loadRanking()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadRanking()
}

// 格式化观看次数
const formatViews = (views) => {
  if (views >= 10000) {
    return (views / 10000).toFixed(1) + '万'
  }
  return views.toString()
}

// 跳转到动漫详情页
const goToAnimeDetail = (animeId) => {
  router.push(`/AnimeDetail/${animeId}`)
}

onMounted(() => {
  loadRanking()
})
</script>

<template>
  <div class="ranking-container">
    <div class="ranking-header">
      <h1>热门动漫排行榜</h1>
      <p class="subtitle">根据观看次数排名</p>
    </div>

    <div class="ranking-content" v-loading="loading">
      <ul class="ranking-list">
        <li v-for="(anime, index) in rankingList" :key="anime.id" 
            class="ranking-item" 
            @click="goToAnimeDetail(anime.id)">
          <div class="rank-number" :class="{
            'top1': index === 0,
            'top2': index === 1,
            'top3': index === 2
          }">{{ index + 1 + (currentPage - 1) * pageSize }}</div>
          
          <div class="anime-cover">
            <img :src="anime.thumbnailUrl" :alt="anime.title">
          </div>
          
          <div class="anime-info">
            <h3 class="anime-title">{{ anime.title }}</h3>
            <div class="anime-meta">
              <span class="anime-category">{{ anime.category }}</span>
              <span class="anime-year">{{ anime.releaseDate }}</span>
            </div>
            <div class="anime-desc">{{ anime.description }}</div>
          </div>
          
          <div class="anime-stats">
            <div class="view-count">
              <i class="el-icon-view"></i>
              <span>{{ formatViews(anime.viewCount) }}</span>
            </div>
            <div class="score" v-if="anime.score">
              <i class="el-icon-star-on"></i>
              <span>{{ anime.score.toFixed(1) }}</span>
            </div>
          </div>
        </li>
      </ul>

      <div class="ranking-pagination">
        <el-pagination 
          v-model:current-page="currentPage" 
          v-model:page-size="pageSize" 
          :total="totalItems"
          :page-sizes="[10, 20, 50]" 
          layout="total, sizes, prev, pager, next" 
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange" 
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.ranking-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 25px;
  background-color: #f9fafc;
  min-height: 100vh;
}

.ranking-header {
  text-align: center;
  margin-bottom: 40px;
  background: linear-gradient(135deg, #2c3e50 0%, #3498db 100%);
  padding: 40px 20px;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  color: white;
}

.ranking-header h1 {
  font-size: 2.8rem;
  color: white;
  margin-bottom: 15px;
  font-weight: 800;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.subtitle {
  color: rgba(255, 255, 255, 0.9);
  font-size: 1.2rem;
  font-weight: 500;
}

.ranking-list {
  list-style: none;
  padding: 0;
}

.ranking-item {
  display: flex;
  margin-bottom: 25px;
  padding: 20px;
  border-radius: 12px;
  background-color: white;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.4s ease;
  cursor: pointer;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.ranking-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.12);
  border-color: rgba(52, 152, 219, 0.3);
}

.ranking-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 6px;
  height: 100%;
  background: #3498db;
  opacity: 0.7;
}

.rank-number {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background-color: #f0f2f5;
  color: #2c3e50;
  font-weight: 800;
  font-size: 1.3rem;
  margin-right: 20px;
  flex-shrink: 0;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.top1 {
  background: linear-gradient(135deg, #f6b93b, #e67e22);
  color: white;
  box-shadow: 0 5px 15px rgba(230, 126, 34, 0.4);
  transform: scale(1.1);
}

.top2 {
  background: linear-gradient(135deg, #bdc3c7, #95a5a6);
  color: white;
  box-shadow: 0 5px 15px rgba(189, 195, 199, 0.4);
  transform: scale(1.05);
}

.top3 {
  background: linear-gradient(135deg, #cd6133, #cc8e35);
  color: white;
  box-shadow: 0 5px 15px rgba(205, 97, 51, 0.4);
  transform: scale(1.02);
}

.anime-cover {
  width: 130px;
  height: 190px;
  margin-right: 25px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
  transition: all 0.4s ease;
}

.ranking-item:hover .anime-cover {
  transform: scale(1.05);
}

.anime-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.ranking-item:hover .anime-cover img {
  transform: scale(1.1);
}

.anime-info {
  flex: 1;
  overflow: hidden;
}

.anime-title {
  font-size: 1.5rem;
  margin: 0 0 12px;
  color: #2c3e50;
  font-weight: 700;
}

.anime-meta {
  display: flex;
  margin-bottom: 15px;
}

.anime-category, .anime-year {
  font-size: 0.95rem;
  color: #7f8c8d;
  margin-right: 20px;
  display: flex;
  align-items: center;
}

.anime-category::before, .anime-year::before {
  content: '';
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background-color: #3498db;
  margin-right: 8px;
}

.anime-desc {
  font-size: 1rem;
  color: #7f8c8d;
  line-height: 1.6;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  line-clamp: 3;
  -webkit-box-orient: vertical;
  margin-bottom: 15px;
}

.anime-stats {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-end;
  min-width: 120px;
  margin-left: 15px;
}

.view-count, .score {
  display: flex;
  align-items: center;
  margin: 10px 0;
  padding: 6px 12px;
  border-radius: 20px;
  background-color: rgba(52, 152, 219, 0.1);
  transition: all 0.3s ease;
}

.view-count {
  color: #e74c3c;
  font-weight: 700;
  font-size: 1.1rem;
}

.score {
  color: #f39c12;
  font-weight: 700;
  font-size: 1.1rem;
  background-color: rgba(243, 156, 18, 0.1);
}

.ranking-item:hover .view-count, .ranking-item:hover .score {
  transform: scale(1.05);
}

.ranking-pagination {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

@media (max-width: 768px) {
  .ranking-item {
    flex-direction: column;
    padding: 15px;
  }
  
  .anime-cover {
    margin-right: 0;
    margin-bottom: 20px;
    width: 100%;
    height: 200px;
  }
  
  .anime-info {
    text-align: center;
    margin-bottom: 20px;
  }

  .anime-meta {
    justify-content: center;
  }
  
  .anime-stats {
    flex-direction: row;
    justify-content: center;
    width: 100%;
    margin-left: 0;
    gap: 15px;
  }

  .rank-number {
    position: absolute;
    top: 10px;
    left: 10px;
    z-index: 2;
    margin-right: 0;
  }

  .ranking-header {
    padding: 25px 15px;
  }

  .ranking-header h1 {
    font-size: 2.2rem;
  }
}
</style>
