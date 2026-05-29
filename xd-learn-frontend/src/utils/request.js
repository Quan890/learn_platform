import axios from 'axios'
import { getToken, setToken, removeToken, getRefreshToken, setRefreshToken, removeRefreshToken } from './auth'

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 添加token到请求头
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 统一处理响应
    if (res.code === 200) {
      return res
    } else {
      // 错误处理
      console.error('请求错误:', res.message || '未知错误')
      return Promise.reject(new Error(res.message || '未知错误'))
    }
  },
  async error => {
    const originalRequest = error.config
    
    // 处理token过期
    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true
      
      try {
        // 尝试刷新token
        const refreshToken = getRefreshToken()
        if (!refreshToken) {
          // 没有刷新token，跳转到登录页
          removeToken()
          removeRefreshToken()
          window.location.href = '/login'
          return Promise.reject(error)
        }
        
        // 调用刷新token接口
        const res = await axios.post(
          `${import.meta.env.VITE_API_BASE_URL || '/api'}/auth/refresh`,
          { refreshToken },
          { timeout: 5000 }
        )
        
        if (res.data.code === 200) {
          // 更新token
          setToken(res.data.data.accessToken)
          setRefreshToken(res.data.data.refreshToken)
          
          // 重新发起请求
          service.defaults.headers.common.Authorization = `Bearer ${res.data.data.accessToken}`
          originalRequest.headers.Authorization = `Bearer ${res.data.data.accessToken}`
          return service(originalRequest)
        } else {
          // 刷新token失败，跳转到登录页
          removeToken()
          removeRefreshToken()
          window.location.href = '/login'
          return Promise.reject(error)
        }
      } catch (refreshError) {
        // 刷新token出错，跳转到登录页
        removeToken()
        removeRefreshToken()
        window.location.href = '/login'
        return Promise.reject(refreshError)
      }
    }
    
    // 其他错误处理
    console.error('请求错误:', error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default service