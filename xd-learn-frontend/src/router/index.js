import { createRouter, createWebHistory } from 'vue-router'
import { isLoggedIn } from '../utils/auth'

// 定义路由组件
const Home = () => import('../views/Home.vue')
const Login = () => import('../views/Login.vue')
const Register = () => import('../views/Register.vue')
const CourseDetail = () => import('../views/CourseDetail.vue')
const Assistant = () => import('../views/Assistant.vue')
const Profile = () => import('../views/Profile.vue')
const CourseEdit = () => import('../views/CourseEdit.vue')

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home,
      meta: { requiresAuth: false }
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: { requiresAuth: false }
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
      meta: { requiresAuth: false }
    },
    {
      path: '/course/:id',
      name: 'CourseDetail',
      component: CourseDetail,
      meta: { requiresAuth: false }
    },
    {
      path: '/assistant',
      name: 'Assistant',
      component: Assistant,
      meta: { requiresAuth: false }
    },
    {
      path: '/profile',
      name: 'Profile',
      component: Profile,
      meta: { requiresAuth: true }
    },
    {
      path: '/profile/courses',
      name: 'MyCourses',
      component: Profile,
      meta: { requiresAuth: true }
    },
    {
      path: '/course/create',
      name: 'CourseCreate',
      component: CourseEdit,
      meta: { requiresAuth: true }
    },
    {
      path: '/course/:id/edit',
      name: 'CourseEdit',
      component: CourseEdit,
      meta: { requiresAuth: true }
    }
  ]
})

// 导航守卫
router.beforeEach((to, from, next) => {
  // 判断路由是否需要认证
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)
  
  if (requiresAuth && !isLoggedIn()) {
    // 需要认证但未登录，跳转到登录页
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else {
    // 不需要认证或已登录，继续访问
    next()
  }
})

export default router