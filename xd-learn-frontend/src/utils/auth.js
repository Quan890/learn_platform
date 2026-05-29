// 认证工具函数

/**
 * 获取token
 * @returns {string|null} - token值或null
 */
export const getToken = () => {
  return localStorage.getItem('token')
}

/**
 * 设置token
 * @param {string} token - 要存储的token
 * @returns {void}
 */
export const setToken = (token) => {
  localStorage.setItem('token', token)
}

/**
 * 移除token
 * @returns {void}
 */
export const removeToken = () => {
  localStorage.removeItem('token')
  sessionStorage.removeItem('token')
}

/**
 * 获取refresh token
 * @returns {string|null} - refresh token值或null
 */
export const getRefreshToken = () => {
  return localStorage.getItem('refreshToken')
}

/**
 * 设置refresh token
 * @param {string} refreshToken - 要存储的refresh token
 * @returns {void}
 */
export const setRefreshToken = (refreshToken) => {
  localStorage.setItem('refreshToken', refreshToken)
}

/**
 * 移除refresh token
 * @returns {void}
 */
export const removeRefreshToken = () => {
  localStorage.removeItem('refreshToken')
  sessionStorage.removeItem('refreshToken')
}

/**
 * 从token中提取用户信息
 * @param {string} token - JWT令牌
 * @returns {object|null} - 用户信息对象或null
 */
export const parseToken = (token) => {
  try {
    if (!token) return null
    const parts = token.split('.')
    if (parts.length !== 3) return null
    const payload = parts[1]
    if (!payload) return null
    // 处理URL安全的Base64编码
    const base64 = payload.replace(/-/g, '+').replace(/_/g, '/')
    const decoded = atob(base64)
    return JSON.parse(decoded)
  } catch (error) {
    console.error('解析token失败:', error)
    return null
  }
}

/**
 * 检查token是否过期
 * @param {string} token - JWT令牌
 * @returns {boolean} - 是否已过期
 */
export const isTokenExpired = (token) => {
  const payload = parseToken(token)
  if (!payload || !payload.exp) return true
  
  // 将exp转换为毫秒并与当前时间比较
  const now = Date.now() / 1000
  return payload.exp < now
}

/**
 * 获取当前登录用户信息
 * @returns {object|null} - 用户信息对象或null
 */
export const getCurrentUser = () => {
  const token = getToken()
  const user = parseToken(token)
  return user
}

/**
 * 检查用户是否已登录
 * @returns {boolean} - 是否已登录
 */
export const isLoggedIn = () => {
  const token = getToken()
  if (!token) return false
  
  // 检查token是否过期
  return !isTokenExpired(token)
}

/**
 * 退出登录 - 清除所有认证信息
 * @returns {boolean} - 是否成功清除所有认证信息
 */
export const logout = () => {
  let isSuccess = true
  
  try {
    // 清除所有token相关信息
    removeToken()
    localStorage.removeItem('refreshToken')
    sessionStorage.removeItem('refreshToken')
    
    // 清除相关cookie（如果使用cookie存储token）
    // 注意：HTTP-only cookie无法通过JavaScript直接删除
    // 这里删除可访问的token相关cookie
    document.cookie.split(';').forEach(cookie => {
      const [name] = cookie.split('=')
      if (name && (name.trim().toLowerCase().includes('token') || name.trim().toLowerCase().includes('auth'))) {
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`
      }
    })
    
    console.log('所有认证信息已清除')
  } catch (error) {
    console.error('清除认证信息失败:', error)
    isSuccess = false
  }
  
  return isSuccess
}