// 导入HTTP请求工具
import request from '@/utils/request'

/**
 * 获取课程列表接口
 * @param {Object} params - 查询参数
 * @param {String} params.keyword - 关键字搜索
 * @param {Number} params.typeId - 课程类型ID
 * @returns {Promise} 课程列表Promise
 */
export function getCourseList(params) {
  return request({
    url: '/courses',
    method: 'get',
    params
  })
}

/**
 * 搜索课程接口 (公开访问)
 * @param {Object} params - 查询参数
 * @param {String} params.keyword - 关键字搜索
 * @param {Number} params.typeId - 课程类型ID
 * @returns {Promise} 课程列表Promise
 */
export function searchCourses(params) {
  return request({
    url: '/courses/search',
    method: 'get',
    params
  })
}

/**
 * 获取课程类型列表接口
 * @returns {Promise} 课程类型列表Promise
 */
export function getCourseTypes() {
  return request({
    url: '/course-types',
    method: 'get'
  })
}

/**
 * 获取课程详情接口
 * @param {Number} id - 课程ID
 * @returns {Promise} 课程详情Promise
 */
export function getCourseDetail(id) {
  return request({
    url: `/courses/${id}/detail`,
    method: 'get'
  })
}

/**
 * 获取课程目录接口
 * @param {Number} id - 课程ID
 * @returns {Promise} 课程目录Promise
 */
export function getCourseChapters(id) {
  return request({
    url: `/courses/${id}/chapters`,
    method: 'get'
  })
}

/**
 * 创建课程接口
 * @param {Object} data - 课程信息
 * @returns {Promise} 创建结果Promise
 */
export function createCourse(data) {
  return request({
    url: '/courses',
    method: 'post',
    data
  })
}

/**
 * 更新课程接口
 * @param {Number} id - 课程ID
 * @param {Object} data - 课程信息
 * @returns {Promise} 更新结果Promise
 */
export function updateCourse(id, data) {
  return request({
    url: `/courses/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除课程接口
 * @param {Number} id - 课程ID
 * @returns {Promise} 删除结果Promise
 */
export function deleteCourse(id) {
  return request({
    url: `/courses/${id}`,
    method: 'delete'
  })
}

/**
 * 收藏课程接口
 * @param {Number} courseId - 课程ID
 * @returns {Promise} 收藏结果Promise
 */
export function favoriteCourse(courseId) {
  return request({
    url: '/favorites',
    method: 'post',
    data: { courseId }
  })
}

/**
 * 取消收藏接口
 * @param {Number} courseId - 课程ID
 * @returns {Promise} 取消收藏结果Promise
 */
export function unfavoriteCourse(courseId) {
  return request({
    url: `/favorites/${courseId}`,
    method: 'delete'
  })
}

/**
 * 获取收藏列表接口
 * @returns {Promise} 收藏列表Promise
 */
export function getFavorites() {
  return request({
    url: '/favorites',
    method: 'get'
  })
}

/**
 * 添加评论接口
 * @param {Object} data - 评论信息
 * @returns {Promise} 添加评论结果Promise
 */
export function addComment(data) {
  return request({
    url: '/comments',
    method: 'post',
    data
  })
}

/**
 * 获取课程评论接口
 * @param {Number} courseId - 课程ID
 * @returns {Promise} 课程评论Promise
 */
export function getComments(courseId) {
  return request({
    url: `/comments/course/${courseId}`,
    method: 'get'
  })
}

/**
 * 切换收藏状态接口
 * @param {Number} courseId - 课程ID
 * @returns {Promise} 切换收藏状态结果Promise
 */
export function toggleFavorite(courseId) {
  return request({
    url: `/courses/${courseId}/favorite`,
    method: 'post'
  })
}

/**
 * 提交评论接口
 * @param {Object} data - 评论信息
 * @returns {Promise} 提交评论结果Promise
 */
export function submitComment(data) {
  return request({
    url: '/comments',
    method: 'post',
    data
  })
}

/**
 * 下载资源接口
 * @param {Number} resourceId - 资源ID
 * @returns {Promise} 下载资源Promise
 */
export function downloadResource(resourceId) {
  return request({
    url: `/resources/${resourceId}/download`,
    method: 'get',
    responseType: 'blob'
  })
}

/**
 * 获取我的课程接口
 * @returns {Promise} 我的课程Promise
 */
export function getMyCourses() {
  return request({
    url: '/courses/my',
    method: 'get'
  })
}

/**
 * 移除收藏接口
 * @param {Number} courseId - 课程ID
 * @returns {Promise} 移除收藏结果Promise
 */
export function removeFavorite(courseId) {
  return request({
    url: `/favorites/${courseId}`,
    method: 'delete'
  })
}

/**
 * 上传资源接口
 * @param {Number} courseId - 课程ID
 * @param {Object} data - 资源数据
 * @returns {Promise} 上传资源结果Promise
 */
export function uploadResource(courseId, data) {
  return request({
    url: `/courses/${courseId}/resources`,
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}