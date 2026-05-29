<template>
  <div class="home">
    <!-- 导航栏 -->
    <header class="header">
      <div class="container">
        <div class="logo">
          <h1>XD-Learn</h1>
        </div>
        <nav class="nav">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/courses" class="nav-item">课程</router-link>
          <router-link to="/ai-assistant" class="nav-item">智能助手</router-link>
          <router-link to="/personal" class="nav-item">个人中心</router-link>
        </nav>
        <div class="user-info">
          <el-button type="primary" plain v-if="!isAuthenticated">登录</el-button>
          <el-dropdown v-else>
            <span class="user-dropdown">
              {{ user?.username }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- 轮播图 -->
    <el-carousel :interval="5000" height="400px" class="carousel">
      <el-carousel-item v-for="item in carouselItems" :key="item.id">
        <div class="carousel-item" :style="{ backgroundImage: `url(${item.image})` }">
          <div class="carousel-content">
            <h2>{{ item.title }}</h2>
            <p>{{ item.description }}</p>
            <el-button type="primary" size="large">开始学习</el-button>
          </div>
        </div>
      </el-carousel-item>
    </el-carousel>

    <!-- 课程推荐 -->
    <section class="courses-section">
      <div class="container">
        <h2 class="section-title">热门课程推荐</h2>
        <div class="course-list">
          <el-card v-for="course in courses" :key="course.id" class="course-card">
            <template #header>
              <div class="card-header">
                <h3>{{ course.title }}</h3>
              </div>
            </template>
            <div class="card-body">
              <div class="course-image">
                <img :src="course.coverImage" :alt="course.title">
              </div>
              <div class="course-info">
                <p class="course-description">{{ course.description }}</p>
                <div class="course-meta">
                  <span class="meta-item">{{ course.category }}</span>
                  <span class="meta-item">{{ course.studentCount }}人学习</span>
                </div>
                <el-button type="primary" size="small" @click="goToCourseDetail(course.id)">查看详情</el-button>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="container">
        <p>&copy; 2024 QL-Learn 学习平台. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script>
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ArrowDown } from '@element-plus/icons-vue'

export default {
  name: 'HomeView',
  components: {
    ArrowDown
  },
  data() {
    return {
      carouselItems: [
        {
          id: 1,
          title: '欢迎来到QL-Learn',
          description: '专业的在线学习平台，提供优质课程',
          image: 'https://picsum.photos/id/1015/1200/400'
        },
        {
          id: 2,
          title: '智能学习助手',
          description: 'AI驱动的学习助手，随时解答你的问题',
          image: 'https://picsum.photos/id/1016/1200/400'
        },
        {
          id: 3,
          title: '海量课程资源',
          description: '涵盖多个领域的优质课程，满足你的学习需求',
          image: 'https://picsum.photos/id/1019/1200/400'
        }
      ],
      courses: [
        {
          id: 1,
          title: 'Vue 3 实战开发',
          description: '全面学习Vue 3框架，掌握现代化前端开发技术',
          coverImage: 'https://picsum.photos/id/1025/400/225',
          category: '前端开发',
          studentCount: 1234
        },
        {
          id: 2,
          title: 'React 高级应用',
          description: '深入学习React框架，构建复杂的单页应用',
          coverImage: 'https://picsum.photos/id/1026/400/225',
          category: '前端开发',
          studentCount: 892
        },
        {
          id: 3,
          title: 'Node.js 后端开发',
          description: '学习Node.js，构建高性能的Web应用',
          coverImage: 'https://picsum.photos/id/1027/400/225',
          category: '后端开发',
          studentCount: 654
        },
        {
          id: 4,
          title: 'Python 数据分析',
          description: '掌握Python数据分析技能，从数据中获取洞见',
          coverImage: 'https://picsum.photos/id/1028/400/225',
          category: '数据分析',
          studentCount: 987
        }
      ]
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    },
    isAuthenticated() {
      return this.$store.getters.isAuthenticated
    }
  },
  methods: {
    goToCourseDetail(id) {
      this.$router.push(`/course/${id}`)
    },
    handleLogout() {
      this.$store.dispatch('logout')
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>
.home {
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
  transition: background-color 0.3s;
}

.nav-item:hover {
  background-color: #f5f7fa;
}

.user-dropdown {
  cursor: pointer;
  padding: 8px 16px;
}

/* 轮播图 */
.carousel {
  margin-bottom: 40px;
}

.carousel-item {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.carousel-content {
  text-align: center;
  color: #fff;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.carousel-content h2 {
  font-size: 36px;
  margin-bottom: 16px;
}

.carousel-content p {
  font-size: 18px;
  margin-bottom: 24px;
}

/* 课程推荐 */
.courses-section {
  padding: 40px 0;
  background-color: #f5f7fa;
}

.courses-section .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-title {
  font-size: 28px;
  text-align: center;
  margin-bottom: 32px;
  color: #333;
}

.course-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
}

.course-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.course-card .card-header {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.course-card .card-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.course-image {
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.course-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.course-image img:hover {
  transform: scale(1.05);
}

.course-info {
  padding: 16px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.course-description {
  margin-bottom: 16px;
  color: #666;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.course-meta {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
  color: #999;
  font-size: 14px;
}

.course-card .el-button {
  margin-top: auto;
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