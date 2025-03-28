import { createRouter, createWebHistory } from 'vue-router'
import { getCurrentUser } from '../api/loginInfo'
import Layout from '/src/view/Layout/index.vue'
import Login from '/src/view/Login/index.vue'
import IndexView from '/src/view/IndexView/index.vue'
import MyInfo from '/src/view/MyInfo/index.vue'
import history from '/src/view/History/index.vue'
import AnimeDetail from '/src/view/AnimeDetail/index.vue'
import Register from '/src/view/Register/index.vue'
import vip from "/src/view/vip/vip.vue"
import ReportManagement from "/src/view/admin/ReportManagement/index.vue"
import Ranking from "../view/Ranking/index.vue"
// 导入管理员专用组件
import UserManagement from '../view/admin/UserManagement/index.vue'
import AnimeManagement from '../view/admin/AnimeManagement/index.vue'
import CategoryManagement from '../view/admin/CategoryManagement/index.vue'
import CommentManagement from '../view/admin/CommentManagement/index.vue'
import DanmuManagement from '../view/admin/DanmuManagement/index.vue'
import PostManagement from '../view/admin/PostManagenment/index.vue'
const routes = [
  {
    path: '/',
    name: 'Home',
    component: Layout,
    redirect: '/index', // 修正拼写错误
    children: [
        {path: '/index', name: 'index', component: IndexView, meta: { keepAlive: false }},
        {path: '/MyInfo', name: 'MyInfo', component: MyInfo, meta: { keepAlive: false }},
        {path: '/history', name: 'history', component: history, meta: { keepAlive: false }},
        {path: '/AnimeDetail/:id', name: 'AnimeDetail', component: AnimeDetail},
        {path: '/vip', name: 'vip', component: vip, meta: { keepAlive: false }},
        {path: '/ranking', name: 'Ranking', component: Ranking, meta: { keepAlive: false }},
        
        // 社区相关路由
        {path: '/community', name: 'Community', component: () => import('@/view/Community/index.vue'), meta: { keepAlive: false }},
        {path: '/community/post/:id', name: 'PostDetail', component: () => import('@/view/Community/PostDetail.vue'), meta: { keepAlive: false }},
        {path: '/community/create', name: 'CreatePost', component: () => import('@/view/Community/CreatePost.vue'), meta: { requiresAuth: true, keepAlive: false }},
        {path: '/community/edit/:id', name: 'EditPost', component: () => import('@/view/Community/CreatePost.vue'), meta: { requiresAuth: true, keepAlive: false }},
        
        // 管理员专用路由
        {path: '/users', name: 'UserManagement', component: UserManagement, meta: { requiresAdmin: true }},
        {path: '/animes', name: 'AnimeManagement', component: AnimeManagement, meta: { requiresAdmin: true }},
        {path: '/categories', name: 'CategoryManagement', component: CategoryManagement, meta: { requiresAdmin: true }},
        {path: '/comments', name: 'CommentManagement', component: CommentManagement, meta: { requiresAdmin: true }},
        {path: '/danmus', name: 'DanmuManagement', component: DanmuManagement, meta: { requiresAdmin: true }},
        {path: '/report', name: 'ReportManagement', component: ReportManagement, meta: { requiresAdmin: true }},
        {path: '/posts', name: 'PostManagement', component: PostManagement, meta: { requiresAdmin: true }}
    ]
  },
  {path: '/login', name: 'login', component: Login},
  {path: '/register', name: 'Register', component: Register},

]
 
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 修改路由守卫
router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token');
  const publicPages = ['/login', '/register', '/index'];
  const requiresAuth = !publicPages.includes(to.path);

  // 如果需要认证且没有token
  if (requiresAuth && !token) {
    next('/login');
    return;
  }

  // 验证用户权限
  if (token) {
    try {
      const response = await getCurrentUser(token);
      if (response.code === 1 && response.data) {
        // 如果路由需要管理员权限
        if (to.meta.requiresAdmin && response.data.role !== 'admin') {
          next('/');
          return;
        }
      } else {
        // token无效
        localStorage.removeItem('token');
        next('/login');
        return;
      }
    } catch (error) {
      console.error('验证用户权限失败:', error);
      localStorage.removeItem('token');
      next('/login');
      return;
    }
  }
  
  next();
});

export default router