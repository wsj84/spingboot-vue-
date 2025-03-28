<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { queryAnimeApi } from '@/api/anime';
import { queryAll } from '@/api/category'; // 添加这个导入

const router = useRouter();
const animes = ref([]);

// 分页数据
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)

const searchKeyword = ref('')
const selectedCategory = ref('')
const categories = ref([])
const featuredAnimes = ref([]);
// 获取动漫列表数据
const getAnimeList = async () => {
  try {
    const result = await queryAnimeApi(
      currentPage.value, 
      pageSize.value, 
      searchKeyword.value, 
      selectedCategory.value
    )
    if(result.code === 1) {
      animes.value = result.data.rows
      total.value = result.data.total
      // 提取前5部动漫用于轮播图展示
      if (currentPage.value === 1 && !searchKeyword.value && !selectedCategory.value) {
        // 只在首页且没有搜索和筛选时获取热门轮播
        featuredAnimes.value = result.data.rows.slice(0, 5)
      }
      
      console.log('获取动漫列表成功:', animes.value)


    }
  } catch(error) {
    console.error('获取动漫列表失败:', error)
  }
}

// 加载分类列表
const loadCategories = async () => {
  try {
    const result = await queryAll()
    if (result.code === 1) {
      categories.value = result.data
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getAnimeList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getAnimeList()
}

const viewAnime = (id) => {
    // 修改导航方式，使用路径而不是命名路由
    router.push(`/AnimeDetail/${id}`);
    
};

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1 // 重置页码
  getAnimeList()
}

// 处理分类选择
const handleCategoryChange = () => {
  currentPage.value = 1 // 重置页码
  getAnimeList()
}

onMounted(() => {
  loadCategories()
  getAnimeList()
})
</script>

<template>
  <div class="anime-container">
    <div class="header">
      <h1>欢迎来到动漫平台</h1>
      <div class="filter-controls">
        <!-- 搜索框 -->
        <el-input
          v-model="searchKeyword"
          placeholder="搜索动漫..."
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <i class="el-icon-search"></i>
          </template>
          <template #append>
            <el-button @click="handleSearch">搜索</el-button>
          </template>
        </el-input>

        <!-- 分类选择 -->
        <el-select
          v-model="selectedCategory"
          placeholder="选择分类"
          clearable
          @change="handleCategoryChange"
          class="category-select"
        >
          <el-option
            v-for="category in categories"
            :key="category.id"
            :label="category.name"
            :value="category.id"
          />
        </el-select>
      </div>
    </div>

    <!-- 新增：热门动漫轮播图 -->
    <div class="featured-carousel-section">
      <h2 class="section-title">热门推荐</h2>
      <el-carousel :interval="4000" type="card" height="320px" indicator-position="outside">
        <el-carousel-item v-for="anime in featuredAnimes" :key="anime.id" @click="viewAnime(anime.id)">
          <div class="carousel-card">
            <div class="carousel-image-container">
              <img :src="anime.thumbnailUrl" alt="封面" class="carousel-image">
              <div class="vip-badge" v-if="anime.isVipOnly">VIP</div>
            </div>
            <div class="carousel-content">
              <h3 class="carousel-title">{{ anime.title }}</h3>
              <p class="carousel-desc">{{ anime.description?.substring(0, 60) || '暂无简介' }}...</p>
              <div class="carousel-stats">
                <span class="stat-item">
                  <i class="el-icon-view"></i> {{ anime.viewCount || 0 }}
                </span>
                <span class="stat-item">
                  <i class="el-icon-star-on"></i> {{ anime.rating?.toFixed(1) || '暂无' }}
                </span>
              </div>
              <el-button size="small" type="primary" @click.stop="viewAnime(anime.id)">
                立即观看
              </el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <h2 class="section-title">全部动漫</h2>
    <!-- 显示无结果提示 -->
    <div v-if="animes.length === 0" class="no-results">
      <i class="el-icon-warning-outline"></i>
      <p>没有找到相关动漫</p>
    </div>

    <div class="anime-grid">
      <div v-for="anime in animes" :key="anime.id" class="anime-item" @click="viewAnime(anime.id)">
        <div class="cover-container">
          <img :src="anime.thumbnailUrl" alt="封面" class="anime-cover">
          <!-- 添加 VIP 标识 -->
          <div class="vip-badge" v-if="anime.isVipOnly">VIP</div>
        </div>
        <div class="anime-title">{{ anime.title }}</div>
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
.anime-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px 24px;
  background-color: #f9fafc;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 15px;
}

.header h1 {
  font-size: 28px;
  color: #303133;
  margin: 0;
  background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-weight: 700;
  letter-spacing: -0.5px;
}

.filter-controls {
  display: flex;
  gap: 15px;
  align-items: center;
}

.search-input {
  width: 300px;
  border-radius: 8px;
}

.category-select {
  width: 150px;
  border-radius: 8px;
}

.no-results {
  text-align: center;
  padding: 60px 0;
  color: #909399;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.no-results i {
  font-size: 60px;
  margin-bottom: 15px;
  color: #dcdfe6;
}

.anime-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 22px;
}

.anime-item {
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 10px;
  background-color: white;
  overflow: hidden;
}

.anime-item:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.cover-container {
  position: relative;
  width: 100%;
  height: 280px;
  border-radius: 10px 10px 0 0;
  overflow: hidden;
  box-shadow: 0 4px 12px 0 rgba(0, 0, 0, 0.1);
}

/* 添加 VIP 标识样式 */
.vip-badge {
  position: absolute;
  top: 10px;
  right: 10px;
  background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
  color: white;
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: bold;
  z-index: 1;
  box-shadow: 0 3px 8px rgba(230, 126, 34, 0.4);
  letter-spacing: 0.5px;
}

.anime-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.anime-item:hover .anime-cover {
  transform: scale(1.1);
}

.episode-badge {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 20px;
  backdrop-filter: blur(5px);
}

.anime-title {
  font-size: 15px;
  margin: 12px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.3;
  font-weight: 500;
}

.pagination-wrapper {
  margin-top: 40px;
  margin-bottom: 20px;
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
    gap: 15px;
  }
  
  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .filter-controls {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .anime-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }
}

/* 新增：轮播图相关样式 */
.section-title {
  font-size: 24px;
  margin: 35px 0 20px;
  color: #303133;
  border-left: 4px solid #4b6cb7;
  padding-left: 12px;
  font-weight: 600;
  display: flex;
  align-items: center;
}

.section-title::after {
  content: "";
  flex-grow: 1;
  height: 1px;
  background: linear-gradient(to right, #ebeef5 0%, transparent 100%);
  margin-left: 15px;
}

.featured-carousel-section {
  margin: 30px 0 40px;
}

.carousel-card {
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  background: white;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: all 0.4s ease;
  border: 1px solid rgba(235, 238, 245, 0.8);
}

.carousel-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
}

.carousel-image-container {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.carousel-card:hover .carousel-image {
  transform: scale(1.08);
}

.carousel-content {
  padding: 18px;
  flex: 1;
  display: flex;
  flex-direction: column;
  background: linear-gradient(to bottom, #fff, #f9fafc);
}

.carousel-title {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #303133;
  letter-spacing: -0.3px;
  position: relative;
}

.carousel-title::after {
  content: "";
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, #4b6cb7, #182848);
  border-radius: 3px;
  transition: width 0.3s;
}

.carousel-card:hover .carousel-title::after {
  width: 70px;
}

.carousel-desc {
  font-size: 14px;
  color: #606266;
  margin: 0 0 15px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.5;
}

.carousel-stats {
  display: flex;
  gap: 20px;
  font-size: 13px;
  color: #909399;
  margin-bottom: 15px;
  margin-top: auto;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 6px;
  background-color: #f5f7fa;
  padding: 4px 10px;
  border-radius: 20px;
  transition: all 0.3s;
}

.stat-item:hover {
  background-color: #e6f1fc;
  color: #409EFF;
}

.all-anime-section {
  margin-top: 30px;
}

/* 修改 Element Plus 轮播图样式 */
:deep(.el-carousel__item--card) {
  border-radius: 12px;
}

:deep(.el-carousel__item:not(.is-active)) {
  filter: brightness(0.7);
}

:deep(.el-carousel__indicators) {
  margin-top: 10px;
}

:deep(.el-carousel__button) {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  opacity: 0.7;
  transition: all 0.3s;
}

:deep(.el-carousel__indicator--active .el-carousel__button) {
  background-color: #4b6cb7;
  opacity: 1;
}

/* 修改按钮样式 */
:deep(.el-button--primary) {
  background: linear-gradient(135deg, #4b6cb7 0%, #182848 100%);
  border: none;
  border-radius: 20px;
  padding: 8px 16px;
  font-weight: 500;
  transition: all 0.3s;
  box-shadow: 0 4px 10px rgba(75, 108, 183, 0.3);
}

:deep(.el-button--primary:hover) {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(75, 108, 183, 0.4);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .carousel-card {
    flex-direction: column;
  }
  
  .carousel-image-container {
    height: 150px;
  }
  
  .carousel-desc {
    -webkit-line-clamp: 1;
    line-clamp: 1;
  }
}
</style>