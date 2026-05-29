<template>
  <div class="home-container">
    <div class="home-header">
      <h2 class="home-title">欢迎来到木又智能学习平台</h2>
      <p class="home-subtitle">发现优质课程，提升自我价值</p>
    </div>
    
    <div class="search-section">
      <!-- 课程类型下拉列表 -->
      <el-select
        v-model="localSelectedTypeId"
        placeholder="课程类型"
        style="width: 180px; margin-right: 10px;"
        clearable
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
            style="margin-right: 10px;"
          ></el-button>
        </el-tooltip>
      </div>
      <el-input
        v-model="localSearchQuery"
        placeholder="搜索课程名称或关键词"
        clearable
        prefix-icon="Search"
        style="width: 400px; margin-right: 10px;"
      ></el-input>
      <!-- 添加明确的查询按钮 -->
      <el-button
        type="primary"
        :icon="Search"
        @click="handleSearch"
        :loading="searchLoading"
      >查询</el-button>
    </div>
    
    <!-- 课程列表，添加loading和error状态 -->
    <div class="course-grid" v-if="!coursesError">
      <!-- 加载中状态 -->
      <div v-if="coursesLoading" class="loading-container">
        <el-skeleton :rows="9" animated :loading="coursesLoading">
          <template #template>
            <div class="course-skeleton">
              <el-skeleton-item variant="image" style="width: 100%; height: 150px; border-radius: 8px;"></el-skeleton-item>
              <div style="padding: 10px 0;">
                <el-skeleton-item variant="text" style="width: 80%; margin-bottom: 10px;"></el-skeleton-item>
                <el-skeleton-item variant="text" style="width: 60%;"></el-skeleton-item>
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>
      <!-- 课程卡片 -->
      <CourseCard
        v-else
        v-for="course in courses"
        :key="course.id"
        :course="course"
      ></CourseCard>
    </div>
    <!-- 课程列表加载错误 -->
    <div v-else class="error-container">
      <el-alert
        title="加载课程失败"
        description="请检查网络连接或稍后重试"
        type="error"
        show-icon
        :closable="false"
      ></el-alert>
      <el-button type="primary" @click="handleSearch" style="margin-top: 20px;">
        重试
      </el-button>
    </div>
    
    <!-- 分页控件 -->
    <div class="pagination-section" v-if="!coursesError && !coursesLoading">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[9, 18, 27]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      ></el-pagination>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import CourseCard from '../components/CourseCard.vue'
import { courseApi } from '../api/index'
import { Search, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 课程数据
const courses = ref([])
const total = ref(0) // 总记录数，用于分页
const coursesLoading = ref(false)
const coursesError = ref(false)
const searchLoading = ref(false)

// 本地搜索状态，不会立即触发API请求
const localSearchQuery = ref('')
const localSelectedTypeId = ref('')

// 实际用于API请求的搜索参数
const searchQuery = ref('')
const selectedTypeId = ref('')

const currentPage = ref(1)
const pageSize = ref(9)

// 课程类型数据
const courseTypes = ref([{ id: '', name: '全部' }])
const courseTypesLoading = ref(false)
const courseTypesError = ref(false)

// 从API获取课程类型数据
const fetchCourseTypes = async () => {
  courseTypesLoading.value = true
  courseTypesError.value = false
  
  try {
    const response = await courseApi.getCourseTypes()
    // 假设API返回的数据格式为 [{ id: 1, name: '前端开发' }, ...]
    const types = response.data || []
    // 添加"全部"选项作为第一个选项
    courseTypes.value = [{ id: '', name: '全部' }, ...types]
  } catch (error) {
    console.error('获取课程类型失败:', error)
    courseTypesError.value = true
    ElMessage.error('获取课程类型失败，请稍后重试')
    // 保留默认的"全部"选项
    courseTypes.value = [{ id: '', name: '全部' }]
  } finally {
    courseTypesLoading.value = false
  }
}

// 在组件挂载时获取课程类型数据
onMounted(() => {
  fetchCourseTypes()
})

// 重新获取课程类型数据
const refreshCourseTypes = () => {
  fetchCourseTypes()
}

// 从API获取课程数据 - 仅在调用时使用当前搜索参数
const fetchCourses = async () => {
  coursesLoading.value = true
  coursesError.value = false
  searchLoading.value = true
  try {
    // 构建查询参数，包含分页信息
    const params = {
      keyword: searchQuery.value,
      typeId: selectedTypeId.value || undefined, // 使用typeId作为参数名称，传递id值
      page: currentPage.value, // 当前页码
      pageSize: pageSize.value // 每页记录数
    }
    
    console.log('发送API请求，查询参数:', params)
    
    // 使用searchCourses endpoint which is publicly accessible
    const response = await courseApi.searchCourses(params)
    if (response.code === 200 && response.data) {
      // 假设后端返回的数据格式为 { list: [], total: 0 }
      courses.value = response.data.list || []
      total.value = response.data.total || 0 // 保存总记录数用于分页
    }
  } catch (error) {
    console.error('获取课程列表失败:', error)
    coursesError.value = true
    ElMessage.error('加载课程失败，请稍后重试')
    // 使用模拟数据作为备选
    courses.value = [
      {
        id: 1,
        title: 'Vue3 从入门到精通',
        description: '全面学习Vue3框架，掌握Composition API和最新特性',
        author: '张老师',
        type: '前端开发',
        rating: 4.8,
        price: 199
      },
      {
        id: 2,
        title: 'React 高级实战',
        description: '深入理解React原理，构建复杂应用',
        author: '李老师',
        type: '前端开发',
        rating: 4.9,
        price: 299
      },
      {
        id: 3,
        title: 'Node.js 全栈开发',
        description: '从后端到前端，构建完整的Web应用',
        author: '王老师',
        type: '后端开发',
        rating: 4.7,
        price: 399
      },
      {
        id: 4,
        title: 'Python 数据分析',
        description: '掌握Python数据分析库，进行数据可视化',
        author: '赵老师',
        type: '数据分析',
        rating: 4.6,
        price: 149
      },
      {
        id: 5,
        title: 'Java 基础教程',
        description: 'Java编程基础，面向对象编程思想',
        author: '刘老师',
        type: '后端开发',
        rating: 4.5,
        price: 99
      },
      {
        id: 6,
        title: 'MySQL 数据库设计',
        description: '数据库设计原则，SQL优化技巧',
        author: '陈老师',
        type: '数据库',
        rating: 4.4,
        price: 89
      },
      {
        id: 7,
        title: 'Docker 容器化技术',
        description: '容器化部署，微服务架构实践',
        author: '杨老师',
        type: 'DevOps',
        rating: 4.3,
        price: 129
      },
      {
        id: 8,
        title: '人工智能入门',
        description: '机器学习基础，深度学习入门',
        author: '黄老师',
        type: '人工智能',
        rating: 4.2,
        price: 159
      },
      {
        id: 9,
        title: 'HTML5 新特性',
        description: 'HTML5语义化标签，Canvas绘图',
        author: '周老师',
        type: '前端开发',
        rating: 4.1,
        price: 69
      },
      {
        id: 10,
        title: 'CSS3 高级特效',
        description: 'CSS动画，响应式设计，Flexbox与Grid',
        author: '吴老师',
        type: '前端开发',
        rating: 4.0,
        price: 79
      },
      {
        id: 11,
        title: 'JavaScript 高级编程',
        description: '闭包，原型链，异步编程',
        author: '郑老师',
        type: '前端开发',
        rating: 4.9,
        price: 169
      },
      {
        id: 12,
        title: 'TypeScript 实战',
        description: 'TypeScript语法，类型系统，项目实践',
        author: '冯老师',
        type: '前端开发',
        rating: 4.8,
        price: 139
      }
    ]
  } finally {
    coursesLoading.value = false
    searchLoading.value = false
  }
}

// 组件挂载时获取课程数据 - 使用默认参数（无搜索条件）
onMounted(() => {
  fetchCourses()
})

// 处理查询按钮点击 - 仅在此时更新搜索参数并调用API
const handleSearch = () => {
  console.log('查询按钮被点击')
  // 更新实际搜索参数
  searchQuery.value = localSearchQuery.value
  selectedTypeId.value = localSelectedTypeId.value
  // 重置页码
  currentPage.value = 1
  // 调用API获取数据
  fetchCourses()
}

// 分页变化处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchCourses()
}

// 页码变化处理
const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchCourses()
}
</script>

<style scoped>
.home-container {
  max-width: 1280px;
  margin: 0 auto;
}

.home-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 40px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 12px;
}

.home-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 10px;
}

.home-subtitle {
  font-size: 18px;
  opacity: 0.9;
}

.search-section {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  align-items: center;
}

.error-text {
  color: var(--el-color-danger);
  font-size: 14px;
  text-align: center;
}

.loading-container {
  grid-column: 1 / -1;
}

.course-skeleton {
  background: #fff;
  padding: 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.error-container {
  grid-column: 1 / -1;
  text-align: center;
  padding: 40px 0;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.pagination-section {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

.search-section .el-button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.search-section .el-button:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a408a 100%);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .search-section {
    flex-direction: column;
    gap: 10px;
    align-items: stretch;
  }
  
  .search-section .el-select,
  .search-section .el-input,
  .search-section .el-button {
    width: 100% !important;
    margin-right: 0 !important;
  }
  
  .course-grid {
    grid-template-columns: 1fr;
  }
}
</style>
