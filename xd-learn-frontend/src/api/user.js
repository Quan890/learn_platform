// 导入HTTP请求工具
import request from '@/utils/request'

/**
 * 用户登录接口
 * @param {Object} data - 登录信息
 * @param {String} data.username - 用户名
 * @param {String} data.password - 密码
 * @returns {Promise} 登录结果Promise
 */
export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户注册接口
 * @param {Object} data - 注册信息
 * @param {String} data.username - 用户名
 * @param {String} data.password - 密码
 * @param {String} data.email - 邮箱
 * @returns {Promise} 注册结果Promise
 */
export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

/**
 * 获取用户信息接口
 * @returns {Promise} 用户信息Promise
 */
export function getUserInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

/**
 * 退出登录接口
 * @returns {Promise} 退出结果Promise
 */
export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

/**
 * 更新用户资料接口
 * @param {Object} data - 用户资料
 * @returns {Promise} 更新结果Promise
 */
export function updateProfile(data) {
  return request({
    url: '/user/profile',
    method: 'put',
    data
  })
}