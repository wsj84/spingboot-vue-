<template>
  <div class="admin-container">
    <div class="admin-header">
      <h1>弹幕管理</h1>
      <div class="header-actions">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索用户名或动漫名称"
          size="small"
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
      </div>
    </div>

    <el-table :data="filteredTableData" border stripe>
      <el-table-column type="index" label="序号" width="70" />
      <el-table-column prop="animeName" label="动漫" min-width="120" />
      <el-table-column prop="username" label="发送用户" width="120" />
      <el-table-column prop="content" label="弹幕内容" min-width="200" show-overflow-tooltip />
      <el-table-column label="发送时间点" width="100">
        <template #default="{ row }">
          {{ formatTime(row.time) }}
        </template>
      </el-table-column>
      <el-table-column prop="color" label="颜色" width="80">
        <template #default="{ row }">
          <div class="color-preview" :style="{ backgroundColor: row.color }">
            <span class="color-text">{{ row.color }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" fixed="right" width="100">
        <template #default="{ row }">
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="totalItems"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDanmakuAll, deleteDanmaku } from '@/api/danmaku'
import { queryUserbyid } from '@/api/user'
import { queryAnimebyid } from '@/api/anime'
import '@/assets/styles/admin-table.css'
const searchKeyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const tableData = ref([])

// 格式化时间点
const formatTime = (seconds) => {
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = Math.floor(seconds % 60)
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
}

// 搜索过滤
const filteredTableData = computed(() => {
  if (!searchKeyword.value) return tableData.value
  const keyword = searchKeyword.value.toLowerCase()
  return tableData.value.filter(item => 
    item.username.toLowerCase().includes(keyword) || 
    item.animeName.toLowerCase().includes(keyword)
  )
})

// 处理搜索
const handleSearch = () => {
  // 实际项目中可以调用API进行搜索
  console.log('搜索关键词:', searchKeyword.value)
}

// 处理删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条弹幕吗？此操作不可恢复！',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    const result = await deleteDanmaku(row.id)
    if (result.code === 1) {
      ElMessage.success('删除成功')
      await loadDanmuList()
    } else {
      ElMessage.error(result.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除弹幕失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 加载弹幕列表
const loadDanmuList = async () => {
  try {
    const response = await getDanmakuAll(currentPage.value, pageSize.value)
    if (response.code === 1) {
      totalItems.value = response.data.total
      // 为每条弹幕数据加载用户和动漫信息
      const danmakusWithDetails = await Promise.all(
        response.data.rows.map(async (danmaku) => {
          // 获取用户信息
          const userResult = await queryUserbyid(danmaku.userId)
          // 获取动漫信息
          const animeResult = await queryAnimebyid(danmaku.animeId)

          return {
            ...danmaku,
            username: userResult.code === 1 ? userResult.data.username : '未知用户',
            animeName: animeResult.code === 1 ? animeResult.data.title : '未知动漫'
          }
        })
      )
      tableData.value = danmakusWithDetails
    }
  } catch (error) {
    console.error('加载弹幕列表失败:', error)
    ElMessage.error('加载数据失败')
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  loadDanmuList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadDanmuList()
}

// 组件挂载时加载数据
onMounted(() => {
  loadDanmuList()
})
</script>

<style scoped>
/* 仅保留颜色预览样式 */
.color-preview {
  width: 100%;
  height: 24px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  text-shadow: 0 0 2px rgba(0, 0, 0, 0.5);
}

.color-text {
  font-size: 12px;
}
</style>
