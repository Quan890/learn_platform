<template>
  <div class="course-detail-container">
    <!-- 课程头部信息 -->
    <div class="course-header" v-if="course.id">
      <div class="course-header-content">
        <div class="course-header-info">
          <div class="course-type-section">
            <el-tag type="primary" size="large">{{ course.type }}</el-tag>
          </div>
          
          <h2 class="course-title">{{ course.title }}</h2>
          
          <!-- 课程介绍摘要 -->
          <p class="course-description" v-if="course.description">
            {{ course.description }}
          </p>
        
        <div class="course-meta">
            <div class="course-author">
              <el-icon><User /></el-icon>
              <span>{{ course.teacherName }}</span>
            </div>
            <div class="course-rating">
              <el-rate :model-value="course.rating" disabled show-score :score-template="`${parseFloat(course.rating || 0).toFixed(1)}`"></el-rate>
            </div>
            <div class="course-students">
              <el-icon><UserFilled /></el-icon>
              <span>{{ course.studentCount }} 人学习</span>
            </div>
          </div>
        </div>
        
        <div class="course-header-actions">
          <el-button type="success" size="large" :icon="Star">收藏课程</el-button>
        </div>
      </div>
    </div>
    
    <!-- 课程内容区域 -->
    <div class="course-content">
      <!-- 课程标签页 -->
      <div class="course-tabs">
        <el-tabs v-model:active-name="activeTab" @tab-click="handleTabClick">
          <el-tab-pane label="课程详情" name="detail"></el-tab-pane>
          <el-tab-pane label="课程目录" name="directory"></el-tab-pane>
          <el-tab-pane label="学员评价" name="comments"></el-tab-pane>
          <el-tab-pane label="课程资料" name="materials"></el-tab-pane>
        </el-tabs>
      </div>
      
      <!-- 标签页内容 -->
      <div class="tab-content">
        <!-- 课程详情模块 -->
        <div v-if="activeTab === 'detail'" class="detail-section">
          <h3 class="section-title">课程详情</h3>
          
          <!-- 加载状态 -->
          <el-skeleton :rows="10" animated v-if="detailLoading" />
          
          <!-- 错误状态 -->
          <div v-else-if="detailError" class="module-error">
            <el-alert
              title="加载课程详情失败"
              description="请检查网络连接或稍后重试"
              type="error"
              show-icon
              :closable="false"
            ></el-alert>
            <el-button type="primary" @click="fetchCourseDetail" style="margin-top: 20px;">
              重试
            </el-button>
          </div>
          
          <!-- 内容区域 -->
          <div v-else-if="course.id" class="detail-content">
            <!-- 课程介绍 -->
            <div class="detail-card">
              <h4>课程介绍</h4>
              <div class="detail-text" v-if="course.description">
                {{ course.description }}
              </div>
              <el-empty description="暂无课程介绍" v-else />
            </div>
            
            <!-- 学习目标 -->
            <div class="detail-card">
              <h4>学习目标</h4>
              <ul class="goals-list" v-if="course.learningObjectives && course.learningObjectives.length > 0">
                <li v-for="(goal, index) in course.learningObjectives" :key="index">
                  <el-icon><CircleCheck /></el-icon>
                  <span>{{ typeof goal === 'object' && goal.content ? goal.content : goal }}</span>
                </li>
              </ul>
              <el-empty description="暂无学习目标" v-else />
            </div>
            
            <!-- 适合人群 -->
            <div class="detail-card">
              <h4>适合人群</h4>
              <ul class="audience-list" v-if="course.targetAudiences && course.targetAudiences.length > 0">
                <li v-for="(audience, index) in course.targetAudiences" :key="index">
                  <el-icon><UserFilled /></el-icon>
                  <span>{{ typeof audience === 'object' && audience.content ? audience.content : audience }}</span>
                </li>
              </ul>
              <el-empty description="暂无适合人群信息" v-else />
            </div>
          </div>
          
          <!-- 空状态 -->
          <el-empty description="暂无课程详情数据" v-else />
        </div>
        
        <!-- 课程目录模块 -->
        <div v-if="activeTab === 'directory'" class="directory-section">
          <h3 class="section-title">课程目录</h3>
          
          <!-- 加载状态 -->
          <el-skeleton :rows="8" animated v-if="directoryLoading" />
          
          <!-- 错误状态 -->
          <div v-else-if="directoryError" class="module-error">
            <el-alert
              title="加载课程目录失败"
              description="请检查网络连接或稍后重试"
              type="error"
              show-icon
              :closable="false"
            ></el-alert>
            <el-button type="primary" @click="fetchCourseDirectory" style="margin-top: 20px;">
              重试
            </el-button>
          </div>
          
          <!-- 内容区域 -->
          <div v-else>
            <ul class="chapter-list" v-if="course.chapters && course.chapters.length > 0">
              <li v-for="chapter in course.chapters" :key="chapter.id" class="chapter-item">
                <div class="chapter-title" @click="toggleChapter(chapter.id)">
                  <el-icon :class="{'rotate-arrow': expandedChapters.includes(chapter.id)}"><ArrowDown /></el-icon>
                  <span>{{ chapter.title }}</span>
                  <span class="video-count">{{ chapter.videoResources ? chapter.videoResources.length : 0 }} 课时</span>
                </div>
                
                <ul class="video-items-list" :class="{'show-videos': expandedChapters.includes(chapter.id)}">
                  <li 
                    v-for="video in chapter.videoResources" 
                    :key="video.id" 
                    class="video-item" 
                    :class="{ 'active': currentVideoId === video.id }"
                    @click="playVideo(video)"
                  >
                    <el-icon><VideoPlay /></el-icon>
                    <span class="video-name">{{ video.resourceName || video.name || video.title || chapter.title }}</span>
                    <span class="video-duration">{{ video.duration }}</span>
                  </li>
                  <li v-if="!chapter.videoResources || chapter.videoResources.length === 0" class="empty-videos">
                    <el-empty description="暂无课时" size="small" />
                  </li>
                </ul>
              </li>
            </ul>
            <el-empty description="暂无课程目录数据" v-else />
          </div>
        </div>
        
        <!-- 学员评价模块 -->
        <div v-if="activeTab === 'comments'" class="comments-section">
          <h3 class="section-title">学员评价</h3>
          
          <!-- 评论输入框 -->
          <div class="comment-input-section">
            <div class="comment-input-header">
              <el-avatar :size="40">U</el-avatar>
              <div class="comment-input-title">写下您的评价</div>
            </div>
            
            <div class="comment-input-content">
              <el-rate v-model="newComment.rating" class="comment-rating" show-score score-template="{value}"></el-rate>
              <el-input
                v-model="newComment.content"
                type="textarea"
                placeholder="请输入您的评价内容..."
                :rows="4"
                resize="none"
                maxlength="500"
                show-word-limit
                class="comment-textarea"
                :error="commentError"
                @input="clearCommentError"
              ></el-input>
              <div class="comment-input-footer">
                <div class="comment-error" v-if="commentError">{{ commentError }}</div>
                <el-button
                  type="primary"
                  @click="submitComment"
                  :loading="submittingComment"
                  class="submit-comment-btn"
                >
                  提交评价
                </el-button>
              </div>
            </div>
          </div>
          
          <!-- 加载状态 -->
          <el-skeleton :rows="5" animated v-if="commentsLoading" style="margin-top: 20px;" />
          
          <!-- 错误状态 -->
          <div v-else-if="commentsError" class="module-error">
            <el-alert
              title="加载学员评价失败"
              description="请检查网络连接或稍后重试"
              type="error"
              show-icon
              :closable="false"
            ></el-alert>
            <el-button type="primary" @click="fetchCourseComments" style="margin-top: 20px;">
              重试
            </el-button>
          </div>
          
          <!-- 内容区域 -->
          <div v-else>
            <div class="comments-list" v-if="course.comments && course.comments.length > 0">
              <div v-for="(comment, index) in course.comments" :key="comment.id || index" class="comment-item">
                <div class="comment-header">
                  <div class="comment-user">
                    <el-avatar :size="40">{{ (comment.userName || comment.user || 'U').charAt(0) }}</el-avatar>
                    <div class="user-info">
                      <div class="user-name">{{ comment.userName || comment.user }}</div>
                      <div class="comment-date">{{ formatDate(comment.createTime || comment.date) }}</div>
                    </div>
                  </div>
                  <el-rate 
                    :model-value="comment.rating" 
                    disabled 
                    show-score 
                    :score-template="`${parseFloat(comment.rating || 0).toFixed(1)}`"
                  ></el-rate>
                </div>
                <div class="comment-content">
                  {{ comment.content }}
                </div>
                <div class="comment-footer">
                  <span class="comment-like">
                    <el-icon><StarFilled /></el-icon>
                    <span>{{ comment.likeCount || 0 }}</span>
                  </span>
                </div>
              </div>
            </div>
            <el-empty description="暂无学员评价" v-else style="margin-top: 20px;" />
          </div>
        </div>
        
        <!-- 课程资料模块 -->
        <div v-if="activeTab === 'materials'" class="materials-section">
          <h3 class="section-title">课程资料</h3>
          
          <!-- 加载状态 -->
          <el-skeleton :rows="8" animated v-if="materialsLoading" />
          
          <!-- 错误状态 -->
          <div v-else-if="materialsError" class="module-error">
            <el-alert
              title="加载课程资料失败"
              description="请检查网络连接或稍后重试"
              type="error"
              show-icon
              :closable="false"
            ></el-alert>
            <el-button type="primary" @click="fetchCourseMaterials" style="margin-top: 20px;">
              重试
            </el-button>
          </div>
          
          <!-- 内容区域 -->
          <div v-else>
            <ul class="chapter-list" v-if="course.materials && course.materials.length > 0">
              <li v-for="chapter in course.materials" :key="chapter.id" class="chapter-item">
                <div class="chapter-title" @click="toggleMaterialChapter(chapter.id)">
                  <el-icon :class="{'rotate-arrow': expandedMaterialChapters.includes(chapter.id)}"><ArrowDown /></el-icon>
                  <span>{{ chapter.title }}</span>
                  <span class="video-count">{{ chapter.materialResources ? chapter.materialResources.length : 0 }} 个文件</span>
                </div>
                
                <ul class="video-items-list" :class="{'show-videos': expandedMaterialChapters.includes(chapter.id)}">
                  <li v-for="material in chapter.materialResources" :key="material.id" class="video-item">
                    <el-icon><Document /></el-icon>
                    <span class="video-name">{{ material.resourceName }}</span>
                    <div class="file-meta">
                      <el-tag type="info" size="small">资料</el-tag>
                      <a 
                        :href="material.resourceUrl" 
                        target="_blank" 
                        rel="noopener noreferrer" 
                        class="download-link"
                      >
                        <el-button type="primary" size="mini" :icon="Download">下载</el-button>
                      </a>
                    </div>
                  </li>
                  <li v-if="!chapter.materialResources || chapter.materialResources.length === 0" class="empty-videos">
                    <el-empty description="暂无资料" size="small" />
                  </li>
                </ul>
              </li>
            </ul>
            <el-empty description="暂无课程资料" v-else />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  User,
  UserFilled,
  VideoPlay,
  Star,
  StarFilled,
  ArrowDown,
  CircleCheck,
  Download,
  Document
} from '@element-plus/icons-vue'
import { courseApi, commentApi, videoApi, materialApi } from '../api/index'
import { ElMessage } from 'element-plus'

const route = useRoute()
const activeTab = ref('detail')
const expandedChapters = ref([]) // 默认展开第一个章节
const expandedMaterialChapters = ref([]) // 默认展开第一个资料章节
const currentVideoId = ref('') // 当前播放的视频ID

// 日期格式化函数
const formatDate = (dateString) => {
  if (!dateString) return ''
  
  const date = new Date(dateString)
  if (isNaN(date.getTime())) return dateString
  
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

// 课程数据，初始为空对象
const course = ref({
  id: '',
  title: '',
  description: '', // 课程介绍
  teacherName: '', // 讲师名称
  author: '', // 兼容旧字段
  type: '',
  rating: 0,
  studentCount: 0, // 学习人数
  students: 0, // 兼容旧字段
  price: 0,
  detail: '', // 兼容旧字段
  learningObjectives: [], // 学习目标
  goals: [], // 兼容旧字段
  targetAudiences: [], // 适合人群
  audience: [], // 兼容旧字段
  chapters: [],
  comments: [],
  materials: []
})

// 播放视频函数
const playVideo = (video) => {
  // 更新当前播放的视频ID
  currentVideoId.value = video.id
  // 这里可以添加视频播放逻辑，比如使用videoResource.resourceUrl加载视频
  console.log('播放视频:', video.resourceName || video.name, 'URL:', video.resourceUrl)
  // 可以在这里调用视频播放器API或跳转到视频播放页面
  ElMessage.success(`开始播放: ${video.resourceName || video.name}`)
}

// 新评论数据
const newComment = ref({
  rating: 5, // 默认5星评分
  content: ''
})

// 评论错误信息
const commentError = ref('')

// 提交评论的加载状态
const submittingComment = ref(false)

// 各模块的加载和错误状态
const detailLoading = ref(false)
const detailError = ref(false)
const directoryLoading = ref(false)
const directoryError = ref(false)
const commentsLoading = ref(false)
const commentsError = ref(false)
const materialsLoading = ref(false)
const materialsError = ref(false)

// 缓存状态，避免重复请求
const cachedModules = ref({
  detail: false,
  directory: false,
  comments: false,
  materials: false
})

// 清除评论错误
const clearCommentError = () => {
  commentError.value = ''
}

// 获取课程详情
const fetchCourseDetail = async () => {
  detailLoading.value = true
  detailError.value = false
  
  try {
    const courseId = route.params.id
    const response = await courseApi.getCourseDetail(courseId)
    
    if (response.data) {
      // 更新课程基础信息，兼容新旧字段
      course.value = {
        ...course.value,
        id: response.data.id,
        title: response.data.title,
        description: response.data.description || response.data.detail || '',
        teacherName: response.data.teacherName || response.data.author || '',
        author: response.data.teacherName || response.data.author || '', // 兼容旧字段
        type: response.data.type,
        rating: response.data.rating,
        studentCount: response.data.studentCount || response.data.students || 0,
        students: response.data.studentCount || response.data.students || 0, // 兼容旧字段
        price: response.data.price,
        detail: response.data.description || response.data.detail || '', // 兼容旧字段
        learningObjectives: response.data.learningObjectives || response.data.goals || [],
        goals: response.data.learningObjectives || response.data.goals || [], // 兼容旧字段
        targetAudiences: response.data.targetAudiences || response.data.audience || [],
        audience: response.data.targetAudiences || response.data.audience || [] // 兼容旧字段
      }
    }
  } catch (error) {
    console.error('获取课程详情失败:', error)
    detailError.value = true
    ElMessage.error('获取课程详情失败，请稍后重试')
  } finally {
    detailLoading.value = false
  }
}

// 获取课程目录
const fetchCourseDirectory = async () => {
  directoryLoading.value = true
  directoryError.value = false
  
  try {
    const courseId = route.params.id
    const response = await courseApi.getCourseChapters(courseId)
    
    if (response.data) {
      // 获取章节列表并按sortOrder升序排序
      const chapters = response.data || []
      const sortedChapters = chapters.sort((a, b) => {
        return (a.sortOrder || 0) - (b.sortOrder || 0)
      })
      
      // 对每个章节的视频资源按sortOrder升序排序
      sortedChapters.forEach(chapter => {
        if (chapter.videoResources && Array.isArray(chapter.videoResources)) {
          chapter.videoResources.sort((a, b) => {
            return (a.sortOrder || 0) - (b.sortOrder || 0)
          })
        } else {
          // 兼容旧格式
          chapter.videoResources = chapter.videos || []
        }
      })
      
      course.value.chapters = sortedChapters
      
      // 默认展开第一个章节（如果有）
      if (course.value.chapters.length > 0) {
        expandedChapters.value = [course.value.chapters[0].id]
      }
    }
  } catch (error) {
    console.error('获取课程目录失败:', error)
    directoryError.value = true
    ElMessage.error('获取课程目录失败，请稍后重试')
  } finally {
    directoryLoading.value = false
  }
}

// 获取学员评价
const fetchCourseComments = async () => {
  commentsLoading.value = true
  commentsError.value = false
  
  try {
    const courseId = route.params.id
    const response = await commentApi.getCourseComments(courseId, { page: 1, pageSize: 10 })
    
    if (response.data) {
      course.value.comments = response.data.data || response.data.list || response.data || []
      console.log('评论数据已更新:', course.value.comments)
    }
  } catch (error) {
    console.error('获取学员评价失败:', error)
    commentsError.value = true
    ElMessage.error('获取学员评价失败，请稍后重试')
  } finally {
    commentsLoading.value = false
  }
}

// 获取课程资料
const fetchCourseMaterials = async () => {
  materialsLoading.value = true
  materialsError.value = false
  
  try {
    const courseId = route.params.id
    const response = await materialApi.getCourseMaterials(courseId)
    
    if (response.data) {
      // 获取资料列表，支持新格式（response.data.materials）和旧格式（response.data）
      const materialsData = response.data.materials || response.data || []
      
      // 按类型分组，构建章节结构
      const groupedByType = {}
      materialsData.forEach(item => {
        const type = item.type || '其他'
        if (!groupedByType[type]) {
          groupedByType[type] = []
        }
        // 转换字段名，兼容后端返回的字段
        groupedByType[type].push({
          id: item.id,
          name: item.name,
          resourceName: item.name,
          resourceUrl: item.url,
          size: item.size,
          downloadCount: item.downloadCount,
          type: item.type,
          uploadDate: item.uploadDate
        })
      })
      
      // 构建前端期望的章节结构
      const chapters = Object.keys(groupedByType).map((typeName, index) => ({
        id: `material-chapter-${index}`,
        title: typeName,
        sortOrder: index,
        materialResources: groupedByType[typeName]
      }))
      
      course.value.materials = chapters
      
      // 默认展开第一个资料章节（如果有）
      if (course.value.materials.length > 0) {
        expandedMaterialChapters.value = [course.value.materials[0].id]
      }
    }
  } catch (error) {
    console.error('获取课程资料失败:', error)
    materialsError.value = true
    ElMessage.error('获取课程资料失败，请稍后重试')
  } finally {
    materialsLoading.value = false
  }
}

// 提交评论
const submitComment = async () => {
  // 验证输入
  if (!newComment.value.content.trim()) {
    commentError.value = '请输入评论内容'
    return
  }
  
  try {
    submittingComment.value = true
    commentError.value = ''
    
    // 调用后端评论接口，使用正确的API端点
    const response = await commentApi.addComment(course.value.id, {
      rating: newComment.value.rating,
      content: newComment.value.content.trim()
    })
    
    // 如果API调用成功，将返回的评论添加到评论列表中
    if (response.data) {
      course.value.comments.unshift(response.data)
      ElMessage.success('评论提交成功')
      
      // 重置评论输入
      newComment.value = {
        rating: 5,
        content: ''
      }
    }
  } catch (error) {
    console.error('提交评论失败:', error)
    commentError.value = '提交评论失败，请稍后重试'
  } finally {
    submittingComment.value = false
  }
}

// 切换章节展开状态
const toggleChapter = (chapterId) => {
  const index = expandedChapters.value.indexOf(chapterId)
  if (index > -1) {
    expandedChapters.value.splice(index, 1)
  } else {
    expandedChapters.value.push(chapterId)
  }
}

// 切换资料章节展开状态
const toggleMaterialChapter = (chapterId) => {
  const index = expandedMaterialChapters.value.indexOf(chapterId)
  if (index > -1) {
    expandedMaterialChapters.value.splice(index, 1)
  } else {
    expandedMaterialChapters.value.push(chapterId)
  }
}

// 处理标签页切换
const handleTabClick = (tab) => {
  activeTab.value = tab.props.name
  
  // 根据当前标签页获取对应数据
  fetchTabData(tab.props.name)
}

// 根据标签页获取对应数据
const fetchTabData = async (tabName) => {
  switch (tabName) {
    case 'detail':
      await fetchCourseDetail()
      break
    case 'directory':
      await fetchCourseDirectory()
      break
    case 'comments':
      await fetchCourseComments()
      break
    case 'materials':
      await fetchCourseMaterials()
      break
  }
}

// 组件挂载时获取默认标签页数据
onMounted(async () => {
  // 先获取课程详情基础信息
  await fetchCourseDetail()
  
  // 获取当前标签页数据
  await fetchTabData(activeTab.value)
})
</script>

<style scoped>
.course-detail-container {
  max-width: 1280px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.course-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  padding: 30px;
}

.course-type-section {
  margin-bottom: 15px;
}

.course-type-section .el-tag {
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
  font-weight: bold;
  font-size: 14px;
}

.course-header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.course-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #fff;
}

.course-description {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 20px;
  max-width: 700px;
}

.course-meta {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.course-meta > div {
  display: flex;
  align-items: center;
  gap: 5px;
}

.course-header-actions {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.course-price {
  font-size: 24px;
  font-weight: bold;
  color: #ffd700;
}

.course-price.free {
  color: #67c23a;
}

.course-header-actions .el-button {
  margin-bottom: 10px;
  width: 200px;
}

.course-header-actions .el-button:first-of-type {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.course-content {
  padding: 30px;
}

.course-tabs {
  margin-bottom: 30px;
}

.course-tabs .el-tabs__header {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.course-tabs .el-tabs__nav {
  border-bottom: 2px solid #e4e7ed;
}

.section-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
  color: var(--text-color-primary);
}

/* 课程目录样式 */
.chapter-list {
  list-style: none;
  padding: 0;
}

.chapter-item {
  margin-bottom: 10px;
  background: #f8f9fa;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.chapter-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.chapter-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px 24px;
  cursor: pointer;
  font-weight: 600;
  font-size: 17px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  transition: all 0.3s ease;
  position: relative;
}

.chapter-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: rgba(255, 255, 255, 0.9);
}

.chapter-title:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a408a 100%);
  padding-left: 28px;
}

.chapter-title:hover::before {
  width: 8px;
}

.chapter-title .el-icon {
  transition: transform 0.3s ease;
  font-size: 18px;
  margin-right: 12px;
}

.rotate-arrow {
  transform: rotate(180deg);
}

.video-count {
  font-size: 14px;
  opacity: 0.9;
  font-weight: 400;
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 12px;
  border-radius: 12px;
}

.video-items-list {
  list-style: none;
  padding: 0;
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.4s ease;
}

.video-items-list.show-videos {
  max-height: 1000px; /* 足够大的值，确保所有视频都能显示 */
}

.video-item {
  display: flex;
  align-items: center;
  padding: 15px 24px 15px 60px;
  border-bottom: 1px solid #e9ecef;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  background: #fff;
}

.video-item::before {
  content: '';
  position: absolute;
  left: 36px;
  top: 50%;
  transform: translateY(-50%);
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #667eea;
  opacity: 0;
  transition: all 0.2s ease;
}

.video-item:hover {
  background-color: rgba(102, 126, 234, 0.08);
  padding-left: 65px;
}

.video-item:hover::before {
  opacity: 1;
  left: 32px;
}

.video-item:last-child {
  border-bottom: none;
}

.video-item.active {
  background-color: rgba(102, 126, 234, 0.15);
  border-left: 4px solid #667eea;
  padding-left: 56px;
}

.video-item.active::before {
  opacity: 1;
  left: 32px;
  background: #fff;
  box-shadow: 0 0 0 3px #667eea;
}

.video-item .el-icon {
  margin-right: 12px;
  color: #667eea;
  font-size: 16px;
  transition: all 0.2s ease;
}

.video-item.active .el-icon {
  color: #667eea;
  transform: scale(1.2);
}

.video-name {
  flex: 1;
  font-size: 15px;
  font-weight: 500;
  color: var(--text-color-primary);
  transition: all 0.2s ease;
}

.video-item.active .video-name {
  color: #667eea;
  font-weight: 600;
}

.video-duration {
  font-size: 13px;
  color: var(--text-color-secondary);
  margin-left: 15px;
  padding: 2px 8px;
  background: #e9ecef;
  border-radius: 10px;
}

.video-item.active .video-duration {
  background: #667eea;
  color: #fff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chapter-title {
    padding: 15px 16px;
    font-size: 15px;
  }
  
  .chapter-title::before {
    width: 3px;
  }
  
  .chapter-title:hover::before {
    width: 6px;
  }
  
  .video-count {
    font-size: 12px;
    padding: 3px 8px;
  }
  
  .video-item {
    padding: 12px 16px 12px 45px;
    font-size: 14px;
  }
  
  .video-item::before {
    left: 24px;
  }
  
  .video-item:hover {
    padding-left: 50px;
  }
  
  .video-item:hover::before {
    left: 20px;
  }
  
  .video-item.active {
    padding-left: 41px;
  }
  
  .video-item.active::before {
    left: 20px;
  }
  
  .video-name {
    font-size: 14px;
  }
  
  .video-duration {
    font-size: 12px;
    margin-left: 10px;
  }
}

.file-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
  font-size: 12px;
}

.download-link {
  text-decoration: none;
}

.file-size {
  color: var(--text-color-secondary);
}

.file-date {
  color: var(--text-color-secondary);
}

/* 课程资料特定样式 */
.materials-section .video-item {
  background: #fff;
  border-bottom: 1px solid #e9ecef;
  transition: all 0.2s ease;
}

.materials-section .video-item:hover {
  background-color: rgba(102, 126, 234, 0.08);
}

.materials-section .video-name {
  flex: 1;
  font-size: 15px;
  font-weight: 500;
  color: var(--text-color-primary);
  transition: all 0.2s ease;
}

.materials-section .video-item:hover .video-name {
  color: #667eea;
}

/* 课程详情样式 */
.detail-section h4 {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 15px 0;
  color: var(--text-color-primary);
}

.detail-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.detail-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.detail-text {
  line-height: 1.7;
  color: var(--text-color-regular);
  margin-bottom: 0;
  white-space: pre-wrap;
  word-break: break-word;
}

.goals-list, .audience-list {
  list-style: none;
  padding-left: 0;
  margin: 0;
}

.goals-list li, .audience-list li {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  margin-bottom: 12px;
  color: var(--text-color-regular);
  line-height: 1.5;
  padding: 8px 0;
  border-bottom: 1px solid #e9ecef;
}

.goals-list li:last-child, .audience-list li:last-child {
  margin-bottom: 0;
  border-bottom: none;
}

.goals-list .el-icon, .audience-list .el-icon {
  color: #67c23a;
  margin-top: 2px;
  font-size: 16px;
}

/* 课程介绍摘要样式优化 */
.course-description {
  font-size: 16px;
  opacity: 0.95;
  margin: 15px 0 20px 0;
  line-height: 1.6;
  color: #fff;
  max-width: 800px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 学员评价样式 */
.comment-input-section {
  padding: 24px;
  background: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 24px;
  transition: all 0.3s ease;
}

.comment-input-section:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.comment-input-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 18px;
}

.comment-input-title {
  font-weight: bold;
  font-size: 16px;
  color: var(--text-color-primary);
}

.comment-input-content {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.comment-rating {
  align-self: flex-start;
}

.comment-textarea {
  border-radius: 8px;
  resize: none;
  border: 1px solid #e9ecef;
  transition: all 0.2s ease;
}

.comment-textarea:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.comment-input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.comment-error {
  color: #f56c6c;
  font-size: 12px;
}

.submit-comment-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  padding: 10px 24px;
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.submit-comment-btn:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a408a 100%);
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.comment-item {
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid #e9ecef;
  transition: all 0.3s ease;
}

.comment-item:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 16px;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 12px;
}

.comment-user .el-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  font-weight: bold;
  font-size: 16px;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.user-name {
  font-weight: 600;
  color: var(--text-color-primary);
  font-size: 15px;
}

.comment-date {
  font-size: 12px;
  color: var(--text-color-secondary);
}

.comment-content {
  color: var(--text-color-regular);
  line-height: 1.7;
  margin-bottom: 16px;
  font-size: 14px;
  word-break: break-word;
  padding: 12px 0;
  border-bottom: 1px solid #f0f2f5;
}

.comment-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 12px;
}

.comment-like {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #909399;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
  padding: 4px 12px;
  border-radius: 16px;
  background: #f5f7fa;
}

.comment-like:hover {
  background: #ecf5ff;
  color: #667eea;
}

.comment-like .el-icon {
  color: #f7ba2a;
  font-size: 14px;
  transition: all 0.2s ease;
}

.comment-like:hover .el-icon {
  transform: scale(1.1);
}

/* 响应式设计 */
@media (max-width: 992px) {
  .course-header-content {
    flex-direction: column;
    gap: 20px;
  }
  
  .course-header-actions {
    flex-direction: row;
    justify-content: flex-start;
  }
}
</style>
