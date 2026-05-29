<template>
  <div class="personal-center">
    <!-- 导航栏 -->
    <NavBar />

    <!-- 主要内容 -->
    <div class="container">
      <div class="page-title">
        <h2>个人中心</h2>
      </div>

      <el-tabs v-model="activeTab" type="card" class="personal-tabs">
        <!-- 我的课程标签页 -->
        <el-tab-pane label="我的课程" name="courses">
          <div class="courses-tab">
            <div class="tab-header">
              <h3>我的课程</h3>
              <el-button type="primary" @click="showCreateCourseDialog = true" class="create-btn">
                <el-icon><plus /></el-icon> 创建课程
              </el-button>
            </div>

            <!-- 课程列表 -->
            <el-table 
              :data="coursesData" 
              style="width: 100%"
              stripe
              v-loading="coursesLoading"
              element-loading-text="加载中..."
            >
              <el-table-column prop="title" label="课程名称" min-width="200">
                <template #default="scope">
                  <div class="course-title-cell">
                    <img :src="scope.row.coverImage" class="course-cover-small" />
                    <span>{{ scope.row.title }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="categoryName" label="分类" width="120" />
              <el-table-column prop="levelName" label="难度" width="100">
                <template #default="scope">
                  <el-tag size="small" :type="getLevelType(scope.row.level)">{{ scope.row.levelName }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="price" label="价格" width="100">
                <template #default="scope">
                  <span v-if="scope.row.price === 0">免费</span>
                  <span v-else>¥{{ scope.row.price }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="studentCount" label="学习人数" width="100" />
              <el-table-column prop="createdTime" label="创建时间" width="180">
                <template #default="scope">
                  {{ formatDate(scope.row.createdTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="scope">
                  <el-button size="small" @click="editCourse(scope.row)">
                    <el-icon><edit /></el-icon> 编辑
                  </el-button>
                  <el-button size="small" @click="uploadResources(scope.row)">
                    <el-icon><upload /></el-icon> 上传资料
                  </el-button>
                  <el-button size="small" type="danger" @click="deleteCourse(scope.row)">
                    <el-icon><delete /></el-icon> 删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination" v-if="totalCourses > 0">
              <el-pagination
                v-model:current-page="coursesCurrentPage"
                v-model:page-size="coursesPageSize"
                :page-sizes="[5, 10, 20]"
                layout="total, sizes, prev, pager, next"
                :total="totalCourses"
                @size-change="handleCoursesSizeChange"
                @current-change="handleCoursesCurrentChange"
              ></el-pagination>
            </div>
          </div>
        </el-tab-pane>

        <!-- 我的收藏标签页 -->
        <el-tab-pane label="我的收藏" name="favorites">
          <div class="favorites-tab">
            <div class="tab-header">
              <h3>我的收藏</h3>
            </div>

            <el-skeleton :rows="6" animated v-if="favoritesLoading">
              <template #template>
                <div class="skeleton-course">
                  <el-skeleton-item variant="image" style="width: 100%; height: 150px;"></el-skeleton-item>
                  <div style="padding: 10px;">
                    <el-skeleton-item variant="text" style="width: 80%; height: 24px;"></el-skeleton-item>
                    <el-skeleton-item variant="text" style="width: 60%; height: 16px;"></el-skeleton-item>
                    <el-skeleton-item variant="text" style="width: 40%; height: 16px;"></el-skeleton-item>
                    <div style="margin-top: 10px;">
                      <el-skeleton-item variant="button" style="width: 100px; height: 32px;"></el-skeleton-item>
                    </div>
                  </div>
                </div>
              </template>
            </el-skeleton>

            <div v-else-if="favorites.length > 0">
              <el-row :gutter="20">
                <el-col :xs="24" :sm="12" :md="8" :lg="6" 
                  v-for="favorite in favorites" :key="favorite.id"
                  class="favorite-col"
                >
                  <el-card class="favorite-card">
                    <div class="card-cover">
                      <img :src="favorite.course.coverImage" :alt="favorite.course.title" />
                      <el-button 
                        type="text" 
                        size="small" 
                        class="remove-favorite"
                        @click.stop="removeFavorite(favorite.id)"
                      >
                        <el-icon class="star-icon filled"><star /></el-icon>
                      </el-button>
                    </div>
                    <div class="card-content">
                      <h4 class="card-title">{{ favorite.course.title }}</h4>
                      <div class="card-meta">
                        <span class="meta-item">{{ favorite.course.teacherName }}</span>
                        <span class="meta-item">
                        <el-rate :value="favorite.course.rating" disabled :max="5" show-score :score-template="`${parseFloat(favorite.course.rating || 0).toFixed(1)}`" text-color="#ff9900" size="small"></el-rate>
                      </span>
                      </div>
                      <div class="card-price">
                        <span v-if="favorite.course.price === 0">免费</span>
                        <span v-else>¥{{ favorite.course.price }}</span>
                      </div>
                      <el-button 
                        type="primary" 
                        block 
                        size="small"
                        @click="viewCourse(favorite.course.id)"
                      >
                        查看详情
                      </el-button>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>

            <div v-else class="empty-favorites">
              <el-empty description="暂无收藏课程" />
            </div>
          </div>
        </el-tab-pane>

        <!-- 我的资料标签页 -->
        <el-tab-pane label="我的资料" name="profile">
          <div class="profile-tab">
            <div class="tab-header">
              <h3>个人资料</h3>
            </div>

            <el-card>
              <el-form :model="userProfile" label-width="120px" class="profile-form">
                <el-form-item label="用户名">
                  <el-input v-model="userProfile.username" disabled />
                </el-form-item>
                <el-form-item label="邮箱">
                  <el-input v-model="userProfile.email" />
                </el-form-item>
                <el-form-item label="手机号">
                  <el-input v-model="userProfile.phone" />
                </el-form-item>
                <el-form-item label="个人简介">
                  <el-input v-model="userProfile.bio" type="textarea" :rows="4" />
                </el-form-item>
                <el-form-item label="头像">
                  <el-upload
                    class="avatar-uploader"
                    action="/api/upload/avatar"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload"
                  >
                    <img v-if="userProfile.avatar" :src="userProfile.avatar" class="avatar" />
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="updateProfile">保存修改</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="container">
        <p>&copy; 2024 XD-Learn 学习平台. 保留所有权利.</p>
      </div>
    </footer>

    <!-- 创建/编辑课程对话框 -->
    <el-dialog 
      v-model="showCreateCourseDialog" 
      :title="isEditing ? '编辑课程' : '创建课程'" 
      width="700px"
      destroy-on-close
    >
      <el-form 
        :model="courseForm" 
        label-width="100px"
        :rules="courseFormRules"
        ref="courseFormRef"
        class="course-form"
      >
        <el-form-item label="课程标题" prop="title">
          <el-input v-model="courseForm.title" placeholder="请输入课程标题" maxlength="50" show-word-limit />
        </el-form-item>
        
        <el-form-item label="课程分类" prop="categoryId">
          <el-select v-model="courseForm.categoryId" placeholder="请选择课程分类">
            <el-option 
              v-for="category in categories" 
              :key="category.id" 
              :label="category.name" 
              :value="category.id" 
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程难度" prop="level">
          <el-select v-model="courseForm.level" placeholder="请选择课程难度">
            <el-option label="入门级" value="beginner" />
            <el-option label="进阶级" value="intermediate" />
            <el-option label="高级" value="advanced" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="讲师姓名" prop="teacherName">
          <el-input v-model="courseForm.teacherName" placeholder="请输入讲师姓名" />
        </el-form-item>
        
        <el-form-item label="课程价格" prop="price">
          <el-input-number 
            v-model="courseForm.price" 
            :precision="2" 
            :step="0.1"
            :min="0"
            placeholder="设置课程价格（0表示免费）"
          />
        </el-form-item>
        
        <el-form-item label="课程封面" prop="coverImage">
          <el-upload
            class="cover-uploader"
            action="/api/upload/cover"
            :show-file-list="false"
            :on-success="handleCoverUploadSuccess"
            :before-upload="beforeCoverUpload"
          >
            <img v-if="courseForm.coverImage" :src="courseForm.coverImage" class="cover-image" />
            <div v-else class="cover-uploader-icon">
              <el-icon><plus /></el-icon>
              <div class="upload-text">上传封面</div>
            </div>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="课程描述" prop="description">
          <el-input 
            v-model="courseForm.description" 
            type="textarea" 
            placeholder="请输入课程详细描述" 
            :rows="5"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateCourseDialog = false">取消</el-button>
          <el-button type="primary" @click="submitCourseForm">
            {{ isEditing ? '保存修改' : '创建课程' }}
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 上传课程资料对话框 -->
    <el-dialog 
      v-model="showUploadResourcesDialog" 
      title="上传课程资料" 
      width="500px"
    >
      <el-card class="resources-dialog-content">
        <h4>{{ currentCourse?.title }}</h4>
        <div class="upload-section">
          <el-upload
            class="resource-uploader"
            action="/api/courses/resources/upload"
            :headers="{ 'Authorization': `Bearer ${getToken()}` }"
            :data="{ courseId: currentCourse?.id }"
            :multiple="true"
            :show-file-list="true"
            :on-success="handleResourceUploadSuccess"
            :on-error="handleResourceUploadError"
            accept=".pdf,.doc,.docx,.ppt,.pptx,.xls,.xlsx,.zip,.rar,.mp4,.mp3"
          >
            <el-button type="primary">
              <el-icon><upload /></el-icon> 点击上传
            </el-button>
            <div class="upload-tip">
              支持PDF、Word、PPT、Excel、压缩文件和音视频文件，单个文件不超过200MB
            </div>
          </el-upload>
        </div>

        <div class="existing-resources" v-if="currentCourse?.resources && currentCourse?.resources.length > 0">
          <h5>已上传资料：</h5>
          <el-table :data="currentCourse.resources" style="width: 100%; margin-top: 10px;" size="small">
            <el-table-column prop="name" label="资料名称" />
            <el-table-column prop="type" label="类型" width="100">
              <template #default="scope">
                <el-tag size="small">{{ getResourceTypeLabel(scope.row.type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="size" label="大小" width="100">
              <template #default="scope">
                {{ formatFileSize(scope.row.size) }}
              </template>
            </el-table-column>
            <el-table-column prop="uploadTime" label="上传时间" width="180">
              <template #default="scope">
                {{ formatDate(scope.row.uploadTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showUploadResourcesDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 页脚 -->
    <Footer />
  </div>
</template>

<script>
import { ArrowDown, Plus, Edit, Upload, Delete, Star, VideoPlay } from '@element-plus/icons-vue'
import { 
  getMyCourses, createCourse, updateCourse, deleteCourse, 
  getFavorites, removeFavorite, uploadResource, 
  getCourseDetail
} from '@/api/course'
import { updateProfile } from '@/api/user'
import NavBar from '@/components/NavBar.vue'
import Footer from '@/components/Footer.vue'
import CourseCard from '@/components/CourseCard.vue'

export default {
  name: 'PersonalCenterView',
  components: {
    NavBar,
    Footer,
    CourseCard,
    Plus,
    Edit,
    Upload,
    Delete,
    Star,
    VideoPlay
  },
  data() {
    return {
      // 通用状态
      user: {},
      activeTab: 'courses',

      // 我的课程状态
      coursesData: [],
      coursesLoading: false,
      coursesCurrentPage: 1,
      coursesPageSize: 10,
      totalCourses: 0,
      categories: [],

      // 创建/编辑课程状态
      showCreateCourseDialog: false,
      isEditing: false,
      courseForm: {
        title: '',
        categoryId: null,
        level: 'beginner',
        teacherName: '',
        price: 0,
        coverImage: '',
        description: ''
      },
      courseFormRules: {
        title: [
          { required: true, message: '请输入课程标题', trigger: 'blur' },
          { min: 5, max: 50, message: '长度在 5 到 50 个字符', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请选择课程分类', trigger: 'change' }
        ],
        level: [
          { required: true, message: '请选择课程难度', trigger: 'change' }
        ],
        teacherName: [
          { required: true, message: '请输入讲师姓名', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入课程描述', trigger: 'blur' },
          { min: 20, max: 2000, message: '长度在 20 到 2000 个字符', trigger: 'blur' }
        ]
      },

      // 我的收藏状态
      favorites: [],
      favoritesLoading: false,

      // 个人资料状态
      userProfile: {
        username: '',
        email: '',
        phone: '',
        bio: '',
        avatar: ''
      },

      // 上传资料状态
      showUploadResourcesDialog: false,
      currentCourse: null,
      uploadedResources: []
    }
  },
  computed: {
    isAuthenticated() {
      return this.$store.getters.isAuthenticated
    }
  },
  created() {
    this.user = this.$store.state.user
    this.loadMyCourses()
    this.loadFavorites()
    this.loadUserProfile()
  },
  methods: {
    // 导航栏方法
    handleLogout() {
      this.$store.dispatch('logout')
      this.$router.push('/')
    },

    // 我的课程方法
    loadMyCourses() {
      this.coursesLoading = true
      getMyCourses({
        page: this.coursesCurrentPage,
        pageSize: this.coursesPageSize
      })
        .then(response => {
          this.coursesData = response.data
          this.totalCourses = response.total
        })
        .catch(error => {
          console.error('加载我的课程失败:', error)
          this.$message.error('加载课程失败，请稍后重试')
        })
        .finally(() => {
          this.coursesLoading = false
        })
    },

    handleCoursesSizeChange(val) {
      this.coursesPageSize = val
      this.coursesCurrentPage = 1
      this.loadMyCourses()
    },

    handleCoursesCurrentChange(val) {
      this.coursesCurrentPage = val
      this.loadMyCourses()
    },

    // 课程创建/编辑方法
    openCreateCourseDialog() {
      this.isEditing = false
      this.resetCourseForm()
      this.showCreateCourseDialog = true
    },

    openEditCourseDialog(course) {
      this.isEditing = true
      this.courseForm = { ...course }
      this.showCreateCourseDialog = true
    },

    resetCourseForm() {
      this.courseForm = {
        title: '',
        categoryId: null,
        level: 'beginner',
        teacherName: '',
        price: 0,
        coverImage: '',
        description: ''
      }
    },

    submitCourseForm() {
      this.$refs.courseFormRef.validate((valid) => {
        if (valid) {
          const apiCall = this.isEditing 
            ? updateCourse(this.courseForm.id, this.courseForm)
            : createCourse(this.courseForm)

          apiCall
            .then(() => {
              this.$message.success(this.isEditing ? '课程更新成功' : '课程创建成功')
              this.showCreateCourseDialog = false
              this.loadMyCourses()
            })
            .catch(error => {
              console.error('操作课程失败:', error)
              this.$message.error('操作失败，请稍后重试')
            })
        } else {
          return false
        }
      })
    },

    handleCoverUploadSuccess(res, file) {
      if (res.code === 200) {
        this.courseForm.coverImage = res.data.url
        this.$message.success('封面上传成功')
      } else {
        this.$message.error('封面上传失败')
      }
    },

    beforeCoverUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('上传图片只能是 JPG/PNG/GIF 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
      }
      return isImage && isLt2M
    },

    editCourse(course) {
      this.openEditCourseDialog(course)
    },

    deleteCourse(course) {
      this.$confirm(`确定要删除课程「${course.title}」吗？此操作不可恢复。`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(() => {
        return deleteCourse(course.id)
      })
      .then(() => {
        this.$message.success('课程删除成功')
        this.loadMyCourses()
      })
      .catch(error => {
        if (error !== 'cancel') {
          console.error('删除课程失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      })
    },

    // 我的收藏方法
    loadFavorites() {
      this.favoritesLoading = true
      getFavorites()
        .then(response => {
          this.favorites = response
        })
        .catch(error => {
          console.error('加载收藏失败:', error)
          this.$message.error('加载收藏失败，请稍后重试')
        })
        .finally(() => {
          this.favoritesLoading = false
        })
    },

    removeFavorite(favoriteId) {
      this.$confirm('确定要取消收藏该课程吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(() => {
        return removeFavorite(favoriteId)
      })
      .then(() => {
        this.$message.success('取消收藏成功')
        this.loadFavorites()
      })
      .catch(error => {
        if (error !== 'cancel') {
          console.error('取消收藏失败:', error)
          this.$message.error('取消收藏失败，请稍后重试')
        }
      })
    },

    viewCourse(courseId) {
      this.$router.push(`/course/${courseId}`)
    },

    // 个人资料方法
    loadUserProfile() {
      this.userProfile = {
        username: this.user.username || '',
        email: this.user.email || '',
        phone: this.user.phone || '',
        bio: this.user.bio || '',
        avatar: this.user.avatar || ''
      }
    },

    updateProfile() {
      updateProfile(this.userProfile)
        .then(response => {
          this.$message.success('资料更新成功')
          // 更新Vuex中的用户信息
          this.$store.commit('SET_USER', response)
          this.user = response
        })
        .catch(error => {
          console.error('更新资料失败:', error)
          this.$message.error('更新资料失败，请稍后重试')
        })
    },

    handleAvatarSuccess(res, file) {
      if (res.code === 200) {
        this.userProfile.avatar = res.data.url
        this.$message.success('头像上传成功')
      } else {
        this.$message.error('头像上传失败')
      }
    },

    beforeAvatarUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isImage) {
        this.$message.error('上传头像只能是图片格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像大小不能超过 2MB!')
      }
      return isImage && isLt2M
    },

    // 上传资料方法
    uploadResources(course) {
      this.currentCourse = course
      this.loadCourseResources(course.id)
      this.showUploadResourcesDialog = true
    },

    loadCourseResources(courseId) {
      getCourseDetail(courseId)
        .then(response => {
          this.currentCourse = response
        })
        .catch(error => {
          console.error('加载课程资料失败:', error)
          this.$message.error('加载课程资料失败，请稍后重试')
        })
    },

    handleResourceUploadSuccess(response) {
      if (response.code === 200) {
        this.$message.success('资料上传成功')
        this.loadCourseResources(this.currentCourse.id)
      } else {
        this.$message.error('资料上传失败')
      }
    },

    handleResourceUploadError() {
      this.$message.error('资料上传失败，请稍后重试')
    },

    // 辅助方法
    getToken() {
      return this.$store.state.token
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

    formatDate(dateString) {
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
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

    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes'
      const k = 1024
      const sizes = ['Bytes', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    }
  }
}
</script>

<style scoped>
.personal-center {
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

.nav-item:hover, .nav-item.active {
  background-color: #ecf5ff;
  color: #409eff;
}

.user-dropdown {
  cursor: pointer;
  padding: 8px 16px;
}

/* 主要内容 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  flex: 1;
}

.page-title {
  padding: 20px 0;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 20px;
}

.page-title h2 {
  margin: 0;
  color: #333;
}

.personal-tabs {
  background-color: #fff;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

/* 我的课程样式 */
.courses-tab {
  padding: 20px;
}

.tab-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.tab-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.create-btn {
  display: flex;
  align-items: center;
  gap: 5px;
}

.course-title-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.course-cover-small {
  width: 60px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 创建/编辑课程对话框 */
.course-form {
  margin: 0;
}

.cover-uploader {
  display: block;
  width: 100%;
  max-width: 300px;
}

.cover-image {
  width: 100%;
  height: auto;
  border-radius: 4px;
  object-fit: cover;
  max-height: 200px;
}

.cover-uploader-icon {
  width: 100%;
  height: 150px;
  background-color: #f5f7fa;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.cover-uploader-icon:hover {
  border-color: #409eff;
  background-color: #ecf5ff;
}

.upload-text {
  margin-top: 8px;
  font-size: 14px;
  color: #909399;
}

/* 我的收藏样式 */
.favorites-tab {
  padding: 20px;
}

.favorites-tab .tab-header {
  margin-bottom: 20px;
}

.favorite-col {
  margin-bottom: 20px;
}

.favorite-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
}

.favorite-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.card-cover {
  position: relative;
  width: 100%;
  height: 150px;
  overflow: hidden;
}

.card-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.favorite-card:hover .card-cover img {
  transform: scale(1.05);
}

.remove-favorite {
  position: absolute;
  top: 8px;
  right: 8px;
  background-color: rgba(0, 0, 0, 0.5);
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.star-icon.filled {
  color: #ffd700;
}

.card-content {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-title {
  font-size: 16px;
  margin: 0 0 10px 0;
  color: #333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-meta {
  margin-bottom: 10px;
}

.meta-item {
  display: block;
  margin-bottom: 5px;
  font-size: 14px;
  color: #606266;
}

.card-price {
  font-size: 18px;
  font-weight: 600;
  color: #f56c6c;
  margin-bottom: 15px;
}

.empty-favorites {
  padding: 60px 0;
  text-align: center;
}

/* 个人资料样式 */
.profile-tab {
  padding: 20px;
}

.profile-form {
  margin: 0;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}

.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}

/* 上传资料对话框 */
.resources-dialog-content {
  margin: 0;
}

.upload-section {
  margin-top: 20px;
}

.resource-uploader {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  background-color: #f5f7fa;
}

.upload-tip {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

.existing-resources {
  margin-top: 20px;
}

.existing-resources h5 {
  margin: 0 0 10px 0;
  color: #333;
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

/* 骨架屏样式 */
.skeleton-course {
  margin-bottom: 20px;
  border-radius: 4px;
  overflow: hidden;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header .container {
    flex-direction: column;
    height: auto;
    padding: 10px 20px;
  }

  .nav {
    margin-top: 10px;
    gap: 10px;
  }

  .tab-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .course-title-cell {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }

  .favorite-col {
    margin-bottom: 20px;
  }
}
</style>