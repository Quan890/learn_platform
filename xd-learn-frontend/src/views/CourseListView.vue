<template>
  <div class="course-list">
    <!-- 导航栏 -->
    <NavBar />

    <!-- 搜索和筛选区域 -->
    <section class="filter-section">
      <div class="container">
        <div class="filter-form">
          <div class="search-box">
            <el-input
              v-model="searchKeyword"
              placeholder="输入课程名称或关键词搜索"
              clearable
              class="search-input"
            >
              <template #append>
                <el-button type="primary" @click="searchCourses"><el-icon><search /></el-icon></el-button>
              </template>
            </el-input>
          </div>

          <div class="filter-options">
            <div style="display: flex; align-items: center;">
              <el-select 
                v-model="filters.category" 
                placeholder="课程分类" 
                clearable 
                class="filter-select"
                :loading="courseTypesLoading"
                :disabled="courseTypesLoading"
              >
                <el-option 
                  v-for="type in courseTypes" 
                  :key="type.id" 
                  :label="type.name" 
                  :value="type.id"
                ></el-option>
              </el-select>
              <!-- 课程类型加载错误提示 -->
              <div v-if="courseTypesError" class="type-error-indicator">
                <el-tooltip content="点击重试" placement="top">
                  <el-button 
                    type="text" 
                    :icon="Refresh" 
                    size="small" 
                    @click="refreshCourseTypes"
                    style="margin-left: 5px;"
                  ></el-button>
                </el-tooltip>
              </div>
            </div>

            <el-select v-model="filters.level" placeholder="难度级别" clearable class="filter-select">
              <el-option label="入门级" value="beginner"></el-option>
              <el-option label="中级" value="intermediate"></el-option>
              <el-option label="高级" value="advanced"></el-option>
            </el-select>

            <el-button type="primary" plain @click="resetFilters">重置筛选</el-button>
          </div>
        </div>
      </div>
    </section>

    <!-- 课程列表 -->
    <section class="courses-section">
      <div class="container">
        <el-skeleton :rows="4" animated v-if="loading">
          <template #template>
            <div style="display: flex; flex-wrap: wrap; gap: 20px;">
              <div v-for="i in 4" :key="i" style="width: calc(25% - 15px);">
                <el-skeleton-item variant="image" style="width: 100%; height: 180px;"></el-skeleton-item>
                <div style="padding: 16px;">
                  <el-skeleton-item variant="text" style="width: 80%;"></el-skeleton-item>
                  <el-skeleton-item variant="text" style="width: 60%;"></el-skeleton-item>
                  <el-skeleton-item variant="text" style="width: 40%;"></el-skeleton-item>
                </div>
              </div>
            </div>
          </template>
        </el-skeleton>

        <div v-else-if="courses.length > 0" class="course-grid">
          <CourseCard v-for="course in courses" :key="course.id" :course="course" :show-level="true" @go-to-course-detail="viewCourse" />
        </div>

        <div v-else class="empty-state">
          <el-empty description="暂无课程数据"></el-empty>
        </div>

        <!-- 分页 -->
        <div class="pagination" v-if="total > 0">
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[8, 16, 24, 32]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          ></el-pagination>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <Footer />
  </div>
</template>

<script>
import { Search, User, Star, Refresh } from '@element-plus/icons-vue'
import { getCourseList, getCourseTypes } from '@/api/course'
import NavBar from '../components/NavBar.vue'
import Footer from '../components/Footer.vue'
import CourseCard from '../components/CourseCard.vue'

export default {
  name: 'CourseListView',
  components: {
    Search,
    User,
    Star,
    Refresh,
    NavBar,
    Footer,
    CourseCard
  },
  data() {
    return {
      courses: [],
      total: 0,
      currentPage: 1,
      pageSize: 8,
      searchKeyword: '',
      filters: {
        category: '',
        level: ''
      },
      loading: false,
      // 课程类型相关数据
      courseTypes: [{ id: '', name: '全部' }],
      courseTypesLoading: false,
      courseTypesError: false
    }
  },
  computed: {
  },
  mounted() {
    this.fetchCourseTypes()
    this.loadCourses()
  },
  methods: {
    // 从API获取课程类型数据
    fetchCourseTypes() {
      this.courseTypesLoading = true
      this.courseTypesError = false
      
      getCourseTypes()
        .then(response => {
          // 假设API返回的数据格式为 [{ id: 1, name: '前端开发' }, ...]
          const types = response.data || []
          // 添加"全部"选项作为第一个选项
          this.courseTypes = [{ id: '', name: '全部' }, ...types]
        })
        .catch(error => {
          console.error('获取课程类型失败:', error)
          this.courseTypesError = true
          this.$message.error('获取课程类型失败，请稍后重试')
          // 保留默认的"全部"选项
          this.courseTypes = [{ id: '', name: '全部' }]
        })
        .finally(() => {
          this.courseTypesLoading = false
        })
    },
    
    // 重新获取课程类型数据
    refreshCourseTypes() {
      this.fetchCourseTypes()
    },
    
    loadCourses() {
      this.loading = true
      const params = {
        page: this.currentPage,
        pageSize: this.pageSize,
        keyword: this.searchKeyword,
        category: this.filters.category,
        level: this.filters.level
      }
      
      console.log('发送API请求，查询参数:', params)
      
      getCourseList(params)
        .then(response => {
          // 假设后端返回的数据格式为 { list: [], total: 0 }
          this.courses = response.data.list || response.data || []
          this.total = response.data.total || 0
          // 查看第一个课程的完整数据结构，特别是时间字段
          if (this.courses.length > 0) {
            console.log('课程数据结构:', this.courses[0])
          }
        })
        .catch(error => {
          console.error('加载课程失败:', error)
          this.$message.error('加载课程失败，请稍后重试')
          this.courses = []
          this.total = 0
        })
        .finally(() => {
          this.loading = false
        })
    },
    
    searchCourses() {
      this.currentPage = 1
      this.loadCourses()
    },
    
    resetFilters() {
      this.filters = {
        category: '',
        level: ''
      }
      this.searchKeyword = ''
      this.currentPage = 1
      this.loadCourses()
    },
    
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.loadCourses()
    },
    
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadCourses()
    },
    
    viewCourse(courseId) {
      this.$router.push(`/course/${courseId}`)
    }
  }
}
</script>

<style scoped>
.course-list {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
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

.nav-item:hover,
.nav-item.active {
  background-color: #409eff;
  color: #fff;
}

.user-dropdown {
  cursor: pointer;
  padding: 8px 16px;
}

/* 搜索和筛选区域 */
.filter-section {
  background-color: #f5f7fa;
  padding: 20px 0;
  border-bottom: 1px solid #e4e7ed;
}

.filter-section .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.filter-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.search-box {
  width: 100%;
}

.search-input {
  max-width: 600px;
}

.filter-options {
  display: flex;
  gap: 20px;
  align-items: center;
}

.filter-select {
  width: 180px;
}

/* 课程列表区域 */
.courses-section {
  padding: 40px 0;
  flex: 1;
}

.courses-section .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

.course-card {
  height: 100%;
  display: flex;
  flex-direction: column;
  transition: transform 0.3s, box-shadow 0.3s;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.course-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.course-title {
  margin: 0;
  font-size: 18px;
  color: #333;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.course-body {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.course-image {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.course-card:hover .cover-image {
  transform: scale(1.05);
}

.course-stats {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  padding: 8px 12px;
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.course-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.course-description {
  margin-bottom: 12px;
  color: #666;
  line-height: 1.5;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.course-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 16px;
  color: #999;
  font-size: 12px;
}

.course-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.course-price {
  font-size: 18px;
  font-weight: 600;
  color: #f56c6c;
}

.course-price span[style*="免费"] {
  color: #67c23a;
}

/* 空状态 */
.empty-state {
  padding: 60px 0;
  text-align: center;
}

/* 分页 */
.pagination {
  margin-top: 40px;
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