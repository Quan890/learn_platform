<template>
  <div class="course-detail">
    <!-- 导航栏 -->
    <NavBar />

    <!-- 课程详情内容 -->
    <div class="container">
      <el-row :gutter="20" class="course-content">
        <!-- 主要内容区域 -->
        <el-col :span="16">
          <el-skeleton animated v-if="loading">
            <template #template>
              <div>
                <el-skeleton-item variant="text" style="width: 80%; height: 40px;"></el-skeleton-item>
                <el-skeleton-item variant="text" style="width: 100%; height: 100px;"></el-skeleton-item>
                <el-skeleton-item variant="image" style="width: 100%; height: 400px; margin: 20px 0;"></el-skeleton-item>
                <el-skeleton-item variant="text" style="width: 30%; height: 24px;"></el-skeleton-item>
                <el-skeleton-item variant="image" style="width: 100%; height: 200px; margin: 10px 0;"></el-skeleton-item>
              </div>
            </template>
          </el-skeleton>

          <div v-else>
            <!-- 课程基本信息 -->
            <el-card class="course-info-card">
              <div class="course-header">
                <h1 class="course-title">{{ course.title }}</h1>
                <div class="course-meta">
                  <el-tag size="small" :type="getLevelType(course.level)">{{ course.levelName }}</el-tag>
                  <span class="meta-item">{{ course.categoryName }}</span>
                  <span class="meta-item">{{ course.studentCount }}人学习</span>
                  <span class="meta-item">
                    <el-rate :value="course.rating" disabled :max="5" show-score :score-template="`${parseFloat(course.rating || 0).toFixed(1)}`" text-color="#ff9900"></el-rate>
                  </span>
                </div>
              </div>

              <div class="course-description">
                <h3>课程介绍</h3>
                <div v-html="course.description"></div>
              </div>

              <!-- 讲师信息 -->
              <div class="teacher-info">
                <h3>讲师信息</h3>
                <div class="teacher-card">
                  <div class="teacher-avatar">
                    <img :src="course.teacherAvatar || 'https://picsum.photos/id/1005/80/80'" :alt="course.teacherName">
                  </div>
                  <div class="teacher-details">
                    <h4>{{ course.teacherName }}</h4>
                    <p>{{ course.teacherTitle }}</p>
                    <p class="teacher-bio">{{ course.teacherBio }}</p>
                  </div>
                </div>
              </div>
            </el-card>

            <!-- 视频播放器 -->
            <el-card class="video-card">
              <div class="video-header">
                <h3>课程视频</h3>
              </div>
              <div class="video-player">
                <video
                  controls
                  :src="currentVideoUrl"
                  class="video-element"
                  poster="https://picsum.photos/id/1018/1280/720"
                >
                  您的浏览器不支持视频播放。
                </video>
              </div>
              <div class="video-list">
                <h4>课程章节</h4>
                <el-collapse v-model="activeVideoIndex">
                  <el-collapse-item 
                    v-for="(section, index) in course.sections" 
                    :key="section.id"
                    :title="`第${index + 1}章: ${section.title}`"
                  >
                    <div 
                      v-for="(video, videoIndex) in section.videos" 
                      :key="video.id"
                      class="video-item"
                      :class="{ active: currentVideoId === video.id }"
                      @click="playVideo(video)"
                    >
                      <el-icon class="video-icon"><video-camera /></el-icon>
                      <span class="video-name">{{ video.title }}</span>
                      <span class="video-duration">{{ formatDuration(video.duration) }}</span>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </div>
            </el-card>

            <!-- 课程资料 -->
            <el-card class="resources-card">
              <div class="resources-header">
                <h3>课程资料</h3>
                <span class="resource-count">{{ course.resources?.length || 0 }}份资料</span>
              </div>
              <el-table :data="course.resources || []" style="width: 100%" stripe>
                <el-table-column prop="name" label="资料名称" min-width="300">
                  <template #default="scope">
                    <div class="resource-name">
                      <el-icon :class="getResourceIcon(scope.row.type)">
                        <component :is="getResourceIcon(scope.row.type)"></component>
                      </el-icon>
                      {{ scope.row.name }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="type" label="类型">
                  <template #default="scope">
                    <el-tag size="small">{{ getResourceTypeLabel(scope.row.type) }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="size" label="大小">
                  <template #default="scope">
                    {{ formatFileSize(scope.row.size) }}
                  </template>
                </el-table-column>
                <el-table-column prop="uploadTime" label="上传时间" width="180">
                  <template #default="scope">
                    {{ formatDate(scope.row.uploadTime) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100">
                  <template #default="scope">
                    <el-button 
                      type="primary" 
                      size="small" 
                      @click="downloadResource(scope.row)"
                    >
                      <el-icon><download /></el-icon> 下载
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-col>

        <!-- 侧边栏 -->
        <el-col :span="8">
          <!-- 课程封面 -->
          <el-card class="course-cover-card">
            <div class="cover-image">
              <img :src="course.coverImage" :alt="course.title">
            </div>
            <div class="course-price-section">
              <span class="course-price">
                <span v-if="course.price === 0">免费</span>
                <span v-else>
                  <span class="price-symbol">¥</span>
                  <span class="price-value">{{ course.price }}</span>
                </span>
              </span>
              <el-button type="primary" size="large" block>
                <el-icon><video-play /></el-icon> 立即学习
              </el-button>
              <el-button 
                type="default" 
                size="large" 
                block 
                :loading="favoriteLoading"
                @click="toggleFavorite"
              >
                <el-icon :class="isFavorite ? 'el-icon-star-on' : 'el-icon-star-off'"></el-icon>
                {{ isFavorite ? '取消收藏' : '收藏课程' }}
              </el-button>
            </div>
          </el-card>

          <!-- 评论区 -->
          <el-card class="comments-card">
            <div class="comments-header">
              <h3>评论区</h3>
              <span class="comment-count">{{ comments.length }}条评论</span>
            </div>

            <!-- 评论表单 -->
            <div class="comment-form" v-if="isAuthenticated">
              <el-input 
                type="textarea" 
                v-model="newComment" 
                placeholder="请输入您的评论..."
                :rows="3"
                clearable
              ></el-input>
              <el-button 
                type="primary" 
                block 
                :disabled="!newComment.trim()"
                @click="submitComment"
              >
                发表评论
              </el-button>
            </div>

            <!-- 评论列表 -->
            <div class="comments-list">
              <el-skeleton :rows="3" animated v-if="commentsLoading">
                <template #template>
                  <div>
                    <div style="display: flex; gap: 10px; align-items: center; margin-bottom: 10px;">
                      <el-skeleton-item variant="circle" style="width: 40px; height: 40px;"></el-skeleton-item>
                      <el-skeleton-item variant="text" style="width: 100px;"></el-skeleton-item>
                    </div>
                    <el-skeleton-item variant="text" style="width: 100%;"></el-skeleton-item>
                    <el-skeleton-item variant="text" style="width: 80%;"></el-skeleton-item>
                  </div>
                </template>
              </el-skeleton>

              <div v-else-if="comments.length > 0">
                <div v-for="comment in comments" :key="comment.id" class="comment-item">
                  <div class="comment-header">
                    <div class="comment-avatar">
                      <img :src="comment.user.avatar || 'https://picsum.photos/id/1005/40/40'" :alt="comment.user.username">
                    </div>
                    <div class="comment-info">
                      <span class="comment-username">{{ comment.user.username }}</span>
                      <span class="comment-time">{{ formatDate(comment.createdTime) }}</span>
                    </div>
                  </div>
                  <div class="comment-content">{{ comment.content }}</div>
                </div>
              </div>

              <div v-else class="empty-comments">
                <el-empty description="暂无评论，快来发表第一条评论吧！"></el-empty>
              </div>

              <!-- 评论分页 -->
              <div class="comment-pagination" v-if="comments.length > 0">
                <el-pagination
                  v-model:current-page="commentCurrentPage"
                  v-model:page-size="commentPageSize"
                  :page-sizes="[5, 10, 20]"
                  layout="total, sizes, prev, pager, next"
                  :total="totalComments"
                  @size-change="handleCommentSizeChange"
                  @current-change="handleCommentCurrentChange"
                  small
                ></el-pagination>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 页脚 -->
    <Footer />
  </div>
</template>

<script>
import { Search, User, Star, VideoCamera, Download, VideoPlay } from '@element-plus/icons-vue'
import { getCourseDetail, toggleFavorite, submitComment, getComments, downloadResource } from '@/api/course'
import NavBar from '@/components/NavBar.vue'
import Footer from '@/components/Footer.vue'

export default {
  name: 'CourseDetailView',
  components: {
    NavBar,
    Footer,
    Search,
    User,
    Star,
    VideoCamera,
    Download,
    VideoPlay
  },
  data() {
    return {
      courseId: null,
      course: {
        title: '',
        description: '',
        coverImage: '',
        teacherName: '',
        teacherTitle: '',
        teacherBio: '',
        teacherAvatar: '',
        price: 0,
        level: 'beginner',
        levelName: '入门级',
        categoryName: '',
        studentCount: 0,
        rating: 0,
        sections: [],
        resources: []
      },
      comments: [],
      newComment: '',
      isFavorite: false,
      favoriteLoading: false,
      currentVideoId: '',
      currentVideoUrl: '',
      activeVideoIndex: 0,
      loading: true,
      commentsLoading: false,
      commentCurrentPage: 1,
      commentPageSize: 10,
      totalComments: 0
    }
  },
  computed: {
  },
  created() {
    this.courseId = this.$route.params.id
    this.loadCourseDetail()
  },
  methods: {
    loadCourseDetail() {
      this.loading = true
      getCourseDetail(this.courseId)
        .then(response => {
          this.course = response
          // 检查是否收藏
          this.isFavorite = response.isFavorite || false
          // 设置默认播放视频
          if (response.sections && response.sections.length > 0 && response.sections[0].videos.length > 0) {
            this.playVideo(response.sections[0].videos[0])
          }
          // 加载评论
          this.loadComments()
        })
        .catch(error => {
          console.error('加载课程详情失败:', error)
          this.$message.error('加载课程详情失败，请稍后重试')
        })
        .finally(() => {
          this.loading = false
        })
    },

    loadComments() {
      this.commentsLoading = true
      getComments(this.courseId, {
        page: this.commentCurrentPage,
        pageSize: this.commentPageSize
      })
        .then(response => {
          this.comments = response.data || []
          this.totalComments = response.total || 0
        })
        .catch(error => {
          console.error('加载评论失败:', error)
        })
        .finally(() => {
          this.commentsLoading = false
        })
    },

    toggleFavorite() {
      this.favoriteLoading = true
      toggleFavorite(this.courseId)
        .then(() => {
          this.isFavorite = !this.isFavorite
          this.$message.success(this.isFavorite ? '收藏成功' : '取消收藏成功')
        })
        .catch(error => {
          console.error('收藏操作失败:', error)
          this.$message.error('操作失败，请稍后重试')
        })
        .finally(() => {
          this.favoriteLoading = false
        })
    },

    submitComment() {
      if (!this.newComment.trim()) {
        this.$message.warning('请输入评论内容')
        return
      }

      submitComment({
        courseId: this.courseId,
        content: this.newComment.trim()
      })
        .then(() => {
          this.$message.success('评论发表成功')
          this.newComment = ''
          this.commentCurrentPage = 1
          this.loadComments()
        })
        .catch(error => {
          console.error('发表评论失败:', error)
          this.$message.error('评论发表失败，请稍后重试')
        })
    },

    playVideo(video) {
      this.currentVideoId = video.id
      this.currentVideoUrl = video.url
    },

    downloadResource(resource) {
      downloadResource(resource.id)
        .then(response => {
          // 创建下载链接
          const url = window.URL.createObjectURL(new Blob([response]))
          const link = document.createElement('a')
          link.href = url
          link.setAttribute('download', resource.name)
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          this.$message.success('下载开始')
        })
        .catch(error => {
          console.error('下载资源失败:', error)
          this.$message.error('下载失败，请稍后重试')
        })
    },

    handleCommentSizeChange(val) {
      this.commentPageSize = val
      this.commentCurrentPage = 1
      this.loadComments()
    },

    handleCommentCurrentChange(val) {
      this.commentCurrentPage = val
      this.loadComments()
    },

    handleLogout() {
      this.$store.dispatch('logout')
      this.$router.push('/')
    },

    getLevelType(level) {
      switch (level) {
        case 'beginner':
          return 'success'
        case 'intermediate':
          return 'warning'
        case 'advanced':
          return 'danger'
        default:
          return ''
      }
    },

    getResourceIcon(type) {
      switch (type) {
        case 'pdf':
          return 'el-icon-document'
        case 'doc':
        case 'docx':
          return 'el-icon-document'
        case 'ppt':
        case 'pptx':
          return 'el-icon-document'
        case 'xls':
        case 'xlsx':
          return 'el-icon-document'
        case 'zip':
        case 'rar':
          return 'el-icon-folder-opened'
        default:
          return 'el-icon-document'
      }
    },

    getResourceTypeLabel(type) {
      const typeMap = {
        'pdf': 'PDF文档',
        'doc': 'Word文档',
        'docx': 'Word文档',
        'ppt': 'PPT演示',
        'pptx': 'PPT演示',
        'xls': 'Excel表格',
        'xlsx': 'Excel表格',
        'zip': '压缩文件',
        'rar': '压缩文件',
        'mp4': '视频文件',
        'mp3': '音频文件'
      }
      return typeMap[type] || '其他文件'
    },

    formatDuration(seconds) {
      const minutes = Math.floor(seconds / 60)
      const remainingSeconds = seconds % 60
      return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
    },

    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    },

    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.course-detail {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

/* 导航栏 */
.header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header .container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  color: #409eff;
}

.nav {
  display: flex;
  gap: 30px;
}

.nav-item {
  text-decoration: none;
  color: #333;
  font-size: 16px;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s;
}

.nav-item:hover {
  background-color: #f5f7fa;
}

.user-dropdown {
  cursor: pointer;
  padding: 8px 16px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.course-content {
  margin-top: 20px;
  margin-bottom: 40px;
  flex: 1;
}

/* 课程信息卡片 */
.course-info-card {
  margin-bottom: 20px;
}

.course-header {
  margin-bottom: 24px;
}

.course-title {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
}

.course-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}

.meta-item {
  color: #666;
  font-size: 14px;
}

.course-description {
  margin-bottom: 24px;
}

.course-description h3 {
  font-size: 20px;
  margin-bottom: 12px;
  color: #333;
}

.course-description div {
  color: #666;
  line-height: 1.8;
}

/* 讲师信息 */
.teacher-info {
  margin-bottom: 24px;
}

.teacher-info h3 {
  font-size: 20px;
  margin-bottom: 16px;
  color: #333;
}

.teacher-card {
  display: flex;
  gap: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.teacher-avatar img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.teacher-details h4 {
  margin: 0 0 4px 0;
  font-size: 18px;
  color: #333;
}

.teacher-details p {
  margin: 0 0 8px 0;
  color: #666;
}

.teacher-bio {
  font-size: 14px;
  line-height: 1.5;
}

/* 视频卡片 */
.video-card {
  margin-bottom: 20px;
}

.video-header {
  margin-bottom: 16px;
}

.video-header h3 {
  font-size: 20px;
  color: #333;
}

.video-player {
  background-color: #000;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 20px;
}

.video-element {
  width: 100%;
  height: auto;
  max-height: 500px;
  display: block;
}

.video-list h4 {
  font-size: 16px;
  margin-bottom: 12px;
  color: #333;
}

.video-item {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  border-radius: 4px;
  transition: background-color 0.3s;
  margin-bottom: 4px;
}

.video-item:hover {
  background-color: #f5f7fa;
}

.video-item.active {
  background-color: #ecf5ff;
  border-left: 3px solid #409eff;
}

.video-icon {
  margin-right: 12px;
  color: #409eff;
}

.video-name {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.video-duration {
  font-size: 12px;
  color: #999;
}

/* 资源卡片 */
.resources-card {
  margin-bottom: 20px;
}

.resources-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.resources-header h3 {
  font-size: 20px;
  color: #333;
}

.resource-count {
  color: #666;
  font-size: 14px;
}

.resource-name {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 侧边栏 */
.course-cover-card {
  margin-bottom: 20px;
}

.cover-image img {
  width: 100%;
  height: auto;
  border-radius: 4px 4px 0 0;
}

.course-price-section {
  padding: 20px;
}

.course-price {
  display: block;
  font-size: 32px;
  font-weight: 600;
  color: #f56c6c;
  margin-bottom: 16px;
  text-align: center;
}

.price-symbol {
  font-size: 20px;
  vertical-align: top;
}

.price-value {
  font-size: 32px;
}

/* 评论区 */
.comments-card {
  margin-bottom: 20px;
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.comments-header h3 {
  font-size: 20px;
  color: #333;
}

.comment-count {
  color: #666;
  font-size: 14px;
}

.comment-form {
  margin-bottom: 20px;
}

.comments-list {
  max-height: 600px;
  overflow-y: auto;
}

.comment-item {
  padding: 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.comment-avatar img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 12px;
}

.comment-username {
  font-weight: 500;
  color: #333;
  margin-right: 12px;
}

.comment-time {
  font-size: 12px;
  color: #999;
}

.comment-content {
  color: #666;
  line-height: 1.6;
  font-size: 14px;
}

.empty-comments {
  padding: 40px 0;
}

.comment-pagination {
  margin-top: 20px;
  text-align: center;
}

/* 页脚 */
.footer {
  background-color: #333;
  color: #fff;
  text-align: center;
  padding: 20px 0;
  margin-top: auto;
}

.footer .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}
</style>