<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getReportsApi, ignoreReportApi } from '@/api/report'
import { queryUserbyid, updateUserApi } from '../../../api/user'
import { deleteCommentApi } from '../../../api/comment'
import '@/assets/styles/admin-table.css'
// 表格数据
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const isActive = false
const reslt = ref(
    {
        id: null,
        isActive: isActive
    }
)


// 加载举报列表
const loadReports = async () => {
    try {
        const result = await getReportsApi(currentPage.value, pageSize.value);
        console.log('举报列表:', result);
        if (result.code === 1) {
            // 为每条举报数据加载用户信息
            const reportsWithUserInfo = await Promise.all(
                result.data.rows.map(async (report) => {
                    // 获取举报人信息
                    const reporterResult = await queryUserbyid(report.userId);
                    // 获取被举报人信息
                    const reportedUserResult = await queryUserbyid(report.reportedUserId);

                    return {
                        ...report,
                        reporterName: reporterResult.code === 1 ? reporterResult.data.username : '未知用户',
                        reportedUserName: reportedUserResult.code === 1 ? reportedUserResult.data.username : '未知用户'
                    };
                })
            );

            tableData.value = reportsWithUserInfo;
            totalItems.value = result.data.total;
        }
    } catch (error) {
        console.error('加载举报列表失败:', error);
        ElMessage.error('加载数据失败');
    }
}
const handleReport = async (row) => {
    try {
        await ElMessageBox.confirm(
            '请选择处理方式',
            '举报处理',
            {
                confirmButtonText: '封禁用户',
                cancelButtonText: '删除评论',
                type: 'warning',
                showClose: true,
                distinguishCancelAndClose: true,
                cancelButtonClass: 'handle-btn-delete'
            }
        )
        // 传入完整的 row 对象
        await handleBanUser(row)
    } catch (action) {
        if (action === 'cancel') {
            // 传入完整的 row 对象
            await handleDeleteComment(row)
        }
    }
}


// 封禁用户
const handleBanUser = async (row) => {
    reslt.value.id = row.reportedUserId
    console.log(reslt.value)
    try {
        const result = await updateUserApi(reslt.value)
        if (result.code == 1) {
            ElMessage.success('已封禁该用户')
            // 处理完成后删除举报记录
            await handleIgnore(row.id) // 传入完整的 row 对象
            // 重新加载列表
            await loadReports()
            return;
        }
    } catch (error) {
        ElMessage.error('操作失败')
        console.error('封禁用户失败:', error)
    }
}

// 删除评论
const handleDeleteComment = async (row) => {
    try {
        const result = await deleteCommentApi(row.commentId)
        if (result.code === 1) {
            ElMessage.success('已删除该评论')
            // 处理完成后删除举报记录
            await handleIgnore(row.id) // 传入完整的 row 对象
            // 重新加载列表
            await loadReports()
        }
    } catch (error) {
        ElMessage.error('操作失败')
        console.error('删除评论失败:', error)
    }
}


// 忽略/删除举报
const handleIgnore = async (reportId) => {
    if (!reportId) {
        console.error('举报ID未定义')
        ElMessage.error('操作失败: 举报ID无效')
        return
    }
    
    try {
        const result = await ignoreReportApi(parseInt(reportId))
        if (result.code === 1) {
            console.log('举报记录已删除')
            await loadReports() // 重新加载列表
        }
    } catch (error) {
        console.error('删除举报记录失败:', error)
        ElMessage.error('删除举报记录失败')
    }
}

// 添加分页处理方法
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  loadReports()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadReports()
}

// 在 mounted 钩子中添加
onMounted(() => {
    loadReports()
});
</script>

<template>
    <div class="admin-container">
        <div class="admin-header">
            <h2>举报管理</h2>
        </div>

        <div class="admin-content">
            <el-table :data="tableData" border stripe class="admin-table">
                <el-table-column prop="id" label="ID" width="80" />
                <el-table-column label="举报人" min-width="120">
                    <template #default="{ row }">
                        <el-tag size="small">{{ row.reporterName }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="被举报人" min-width="120">
                    <template #default="{ row }">
                        <el-tag type="danger" size="small">{{ row.reportedUserName }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="reason" label="举报原因" min-width="200" show-overflow-tooltip />
                <el-table-column prop="reportedAt" label="举报时间" min-width="160" />
                <el-table-column label="操作" fixed="right" width="220">
                    <template #default="{ row }">
                        <el-button type="primary" size="small" @click="handleReport(row)">处理</el-button>
                        <el-button type="info" size="small" @click="handleIgnore(row.id)">忽略</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <div class="admin-pagination">
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
    </div>
</template>

<style scoped>
.admin-pagination {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}
</style>
