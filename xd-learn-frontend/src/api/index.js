import request from '../utils/request'

// 认证相关API
export const authApi = {
  // 登录
  login: (data) => request.post('/auth/login', data),
  // 注册
  register: (data) => request.post('/auth/register', data),
  // 刷新token
  refreshToken: (data) => request.post('/auth/refresh', data),
  // 退出登录
  logout: () => request.post('/auth/logout')
}

// 课程相关API
export const courseApi = {
  // 获取课程列表（统一的首页数据和搜索接口）
  getCourseList: (params) => request.get('/courses', { params }),
  // 搜索课程（统一到getCourseList，保留此方法用于向后兼容）
  searchCourses: (params) => request.get('/courses', { params }),
  // 获取课程详情
  getCourseDetail: (id) => request.get(`/courses/${id}/detail`),
  // 获取课程目录
  getCourseChapters: (id) => request.get(`/courses/${id}/chapters`),
  // 收藏课程
  collectCourse: (id) => request.post(`/courses/${id}/collect`),
  // 取消收藏课程
  cancelCollectCourse: (id) => request.delete(`/courses/${id}/collect`),
  // 检查是否收藏
  checkCollect: (id) => request.get(`/courses/${id}/check-collect`),
  // 获取课程类型列表
  getCourseTypes: () => request.get('/course-types')
}

// 评论相关API
export const commentApi = {
  // 获取课程评论
  getCourseComments: (courseId, params) => request.get(`/courses/${courseId}/comments`, { params }),
  // 发表评论
  addComment: (courseId, data) => request.post(`/courses/${courseId}/comments`, data)
}

// 视频相关API
export const videoApi = {
  // 获取视频播放地址
  getVideoUrl: (videoId) => request.get(`/videos/${videoId}/url`),
  // 获取视频列表 - 已迁移到courseApi.getCourseChapters
  getVideoList: (courseId) => request.get(`/courses/${courseId}/videos`)
}

// 资料相关API
export const materialApi = {
  // 获取课程资料
  getCourseMaterials: (courseId) => request.get(`/courses/${courseId}/materials`),
  // 下载资料
  downloadMaterial: (materialId) => request.get(`/materials/${materialId}/download`, { responseType: 'blob' }),
  // 上传资料
  uploadMaterial: (data) => request.post('/materials/upload', data, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 智能助手API
export const assistantApi = {
  // 智能问答
  askQuestion: (data) => request.post('/assistant/ask', data)
}

// 个人中心API
export const userApi = {
  // 获取用户信息
  getUserInfo: () => request.get('/user/info'),
  // 更新用户资料
  updateProfile: (data) => request.put('/user/profile', data),
  // 创建课程
  createCourse: (data) => request.post('/user/courses', data),
  // 获取用户收藏列表
  getCollectionList: (params) => request.get('/user/collections', { params }),
  // 取消收藏
  cancelCollection: (id) => request.delete(`/user/collections/${id}`)
}