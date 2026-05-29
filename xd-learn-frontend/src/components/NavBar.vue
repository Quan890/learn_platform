<template>
  <nav class="nav-bar">
    <div class="nav-container">
      <div class="nav-left">
        <h1 class="site-title">木又智能学习平台</h1>
      </div>
      
      <div class="nav-center">
        <div class="custom-menu">
          <a
            v-for="item in menuItems"
            :key="item.path"
            :href="item.path"
            class="menu-item"
            :class="{ active: activeIndex === item.path }"
            @click.prevent="handleMenuClick(item.path)"
          >
            {{ item.label }}
          </a>
        </div>
      </div>
      
      <div class="nav-right">
        <!-- 登录状态显示 -->
        <transition name="fade" mode="out-in">
          <template v-if="loggedIn">
            <div class="user-info">
              <el-dropdown>
                <span class="el-dropdown-link user-name">
                  <el-avatar :size="32" class="user-avatar">
                    {{ (currentUser?.name || '用户').charAt(0) }}
                  </el-avatar>
                  <span class="user-name-text">{{ currentUser?.name || '用户' }}</span>
                  <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="navigateToProfile">个人中心</el-dropdown-item>
                    <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>
          
          <!-- 未登录状态显示 -->
          <template v-else>
            <el-button type="primary" :icon="User" @click="handleLogin">登录</el-button>
          </template>
        </transition>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, computed, watch, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, SwitchButton, ArrowDown } from '@element-plus/icons-vue'
import { logout, isLoggedIn, getCurrentUser, getToken } from '../utils/auth'
import { userApi } from '../api/index'
import { ElNotification } from 'element-plus'

const router = useRouter()
const route = useRoute()
const activeIndex = ref('/')
// 添加token ref用于跟踪token变化，使computed属性能够响应
const token = ref(localStorage.getItem('token'))
const loading = ref(false)
const loginStatusChecked = ref(false)

// 定义菜单列表
const menuItems = [
  { path: '/', label: '首页' },
  { path: '/assistant', label: '智能助手' },
  { path: '/profile', label: '个人中心' }
]

// 计算属性：判断用户是否已登录，显式依赖token ref
const loggedIn = computed(() => {
  // 显式使用token.value，创建依赖关系
  token.value // 这行确保computed属性依赖token ref
  return isLoggedIn()
})

// 实时用户信息ref
const currentUser = ref({ name: '用户' })

// 从后端获取实时用户信息
const fetchUserInfo = async () => {
  if (!isLoggedIn()) {
    currentUser.value = { name: '用户' }
    return
  }
  
  try {
    loading.value = true
    const response = await userApi.getUserInfo()
    
    if (response && response.code === 200 && response.data) {
      currentUser.value = {
        ...response.data,
        // 确保name字段存在，用于显示
        name: response.data.username || response.data.nickname || '用户'
      }
    }
  } catch (err) {
    console.error('获取用户信息失败:', err)
    // 保留本地token中的用户信息作为备份
    const localUser = getCurrentUser()
    currentUser.value = localUser || { name: '用户' }
  } finally {
    loading.value = false
  }
}

// 监听登录状态变化，刷新用户信息
watch(
  loggedIn,
  (isLoggedIn) => {
    if (isLoggedIn) {
      fetchUserInfo()
    } else {
      currentUser.value = { name: '用户' }
    }
  },
  { immediate: true }
)

// 定期刷新用户信息，确保实时性
let userInfoRefreshInterval = null

const setupUserInfoRefresh = () => {
  userInfoRefreshInterval = setInterval(() => {
    if (isLoggedIn()) {
      fetchUserInfo()
    }
  }, 60000) // 每分钟刷新一次
}

const clearUserInfoRefresh = () => {
  if (userInfoRefreshInterval) {
    clearInterval(userInfoRefreshInterval)
    userInfoRefreshInterval = null
  }
}

// 监听路由变化，更新当前活动菜单和token ref
watch(
  [() => route.path, () => route.query],
  ([newPath, newQuery]) => {
    activeIndex.value = newPath
    // 更新token ref，触发computed属性重新计算
    token.value = localStorage.getItem('token')
    
    // 处理登录成功的情况
    if (newQuery.loginSuccess === 'true') {
      // 更新token ref
      token.value = localStorage.getItem('token')
      // 移除查询参数，避免重复处理
      router.replace({
        path: newPath,
        query: Object.fromEntries(
          Object.entries(newQuery).filter(([key]) => key !== 'loginSuccess')
        )
      })
    }
  }
)

// 监听token变化（例如从其他标签页登录/登出）
const handleStorageChange = () => {
  const newToken = localStorage.getItem('token')
  if (token.value !== newToken) {
    token.value = newToken
    // 刷新用户信息
    fetchUserInfo()
  }
}

window.addEventListener('storage', handleStorageChange)

// 定期检查登录状态（可选，用于处理token过期情况）
let statusCheckInterval = null

const setupStatusCheckInterval = () => {
  statusCheckInterval = setInterval(() => {
    // 定期更新token ref，处理token过期情况
    token.value = localStorage.getItem('token')
  }, 60000) // 每分钟检查一次
}

const clearStatusCheckInterval = () => {
  if (statusCheckInterval) {
    clearInterval(statusCheckInterval)
    statusCheckInterval = null
  }
}

onMounted(() => {
  activeIndex.value = route.path
  // 初始化token ref
  token.value = localStorage.getItem('token')
  setupStatusCheckInterval()
  // 设置用户信息刷新定时器
  setupUserInfoRefresh()
  // 初始获取用户信息
  fetchUserInfo()
})

onUnmounted(() => {
  clearStatusCheckInterval()
  // 清除用户信息刷新定时器
  clearUserInfoRefresh()
  // 移除storage事件监听器
  window.removeEventListener('storage', handleStorageChange)
})

const handleMenuClick = (path) => {
  router.push(path)
}

const navigateToProfile = () => {
  router.push('/profile')
}

const handleLogin = () => {
  // 记录当前路由作为登录后的跳转目标
  const redirectPath = route.path !== '/login' ? route.path : '/'
  router.push({ path: '/login', query: { redirect: redirectPath } })
}

const handleLogout = () => {
  // 执行登出操作
  logout()
  
  // 验证token是否已被成功移除
  const remainingToken = localStorage.getItem('token')
  if (!remainingToken) {
    console.log('token已成功清除')
  } else {
    console.error('token清除失败，尝试强制清除')
    // 强制清除，确保token被移除
    localStorage.removeItem('token')
  }
  
  // 重置当前用户信息
  currentUser.value = { name: '用户' }
  
  // 更新token ref，触发computed属性重新计算
  token.value = localStorage.getItem('token')
  
  // 跳转到首页
  router.push('/')
}
</script>

<style scoped>
.nav-bar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.nav-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}

.nav-left {
  flex: 1;
}

.site-title {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  margin: 0;
}

.nav-center {
  flex: 2;
  display: flex;
  justify-content: center;
}

.nav-right {
  flex: 1;
  display: flex;
  justify-content: flex-end;
}

/* 自定义菜单样式 */
.custom-menu {
  display: flex;
  gap: 20px;
  align-items: center;
}

.menu-item {
  color: #fff;
  text-decoration: none;
  font-size: 16px;
  padding: 10px 15px;
  border-radius: 6px;
  transition: all 0.3s ease;
  font-weight: normal;
}

.menu-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
  color: #ffd700;
  text-decoration: none;
}

.menu-item.active {
  color: #ffd700;
  font-weight: bold;
  background-color: rgba(255, 255, 255, 0.1);
}

/* 用户信息样式 */
.user-info {
  display: flex;
  align-items: center;
}

.user-name {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 20px;
  transition: all 0.3s ease;
  color: #fff;
}

.user-name:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.user-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.user-name-text {
  font-weight: 500;
  color: #fff;
}

/* 过渡动画效果 */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(5px);
}

/* 响应式设计 */
@media (max-width: 992px) {
  .custom-menu {
    display: none;
  }
  
  .nav-right {
    display: flex;
    gap: 10px;
  }
  
  .user-name-text {
    display: none;
  }
}
</style>
