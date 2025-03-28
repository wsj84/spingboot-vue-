import request from "@/utils/request"

export const queryAllApi = (page, pageSize) => request.get(`/users?page=${page}&pageSize=${pageSize}`)
/* 添加用户 */
export const addUserApi = (data) => request.post('/users', data)
/* 删除用户 */
export const deleteUserApi = (id) => request.delete(`/users/${id}`)
/* 修改用户信息 */
export const updateUserApi = (data) => request.put(`/users`, data)
/* 根据id查找用户 */
export const queryUserbyid = (id) => request.get(`/users/${id}`)