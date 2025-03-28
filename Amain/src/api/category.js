import request from "@/utils/request"

export const queryAll = () => request.get('/categories/all')
// 获取分类列表
export const queryCategoriesApi = (page, pageSize) => 
  request.get(`/categories/query?page=${page}&pageSize=${pageSize}`)

// 添加分类
export const addCategoryApi = (data) => request.post('/categories', data)

// 删除分类
export const deleteCategoryApi = (id) => request.delete(`/categories/${id}`)

// 修改分类
export const updateCategoryApi = (data) => request.put('/categories', data)
