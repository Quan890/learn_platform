<template>
  <div class="favorite-container">
    <h2 class="favorite-title">我的收藏课程</h2>
    
    <!-- 收藏课程列表 - 卡片式布局 -->
    <div class="favorite-content">
      <div class="empty-state" v-if="favoriteCourses.length === 0">
        <el-empty description="您还没有收藏任何课程">
          <el-button type="primary" @click="$router.push('/')">去浏览课程</el-button>
        </el-empty>
      </div>
      
      <div class="favorite-courses-grid" v-else>
        <div 
          v-for="course in favoriteCourses" 
          :key="course.id"
          class="course-card"
        >
          <div class="course-card-inner">
            <!-- 课程封面 -->
            <div class="course-cover">
              <div class="cover-placeholder" :style="{ backgroundImage: `linear-gradient(135deg, #${Math.floor(Math.random()*16777215).toString(16)} 0%, #${Math.floor(Math.random()*16777215).toString(16)} 100%)` }">
                <span class="course-type">{{ course.type?.name || course.type }}</span>
              </div>
            </div>
            
            <!-- 课程信息 -->
            <div class="course-info">
              <h3 class="course-title">{{ course.title }}</h3>
              <div class="course-meta">
                <span class="course-teacher">{{ course.teacherName || course.author }}</span>
                <span class="course-price">{{ course.price === 0 ? '免费' : `¥${course.price}` }}</span>
              </div>
            </div>
            
            <!-- 操作按钮 -->
            <div class="course-actions">
              <el-button 
                type="primary" 
                :icon="VideoPlay" 
                size="small" 
                @click="viewCourse(course)"
                class="action-btn"
              >
                查看
              </el-button>
              <el-button 
                type="danger" 
                :icon="Delete" 
                size="small" 
                @click="removeFavorite(course)"
                class="action-btn"
              >
                取消收藏
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { VideoPlay, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

// 模拟收藏课程数据
const favoriteCourses = ref([
  {
    id: 1,
    title: 'Vue3 从入门到精通',
    description: '全面学习Vue3框架，掌握Composition API和最新特性',
    coverImage: '',
    teacherName: '张老师',
    price: 199,
    typeId: 1,
    type: { id: 1, name: '前端开发' },
    createdAt: '2024-01-01 10:00:00',
    updatedAt: '2024-01-01 10:00:00'
  },
  {
    id: 3,
    title: 'Node.js 全栈开发',
    description: '从后端到前端，构建完整的Web应用',
    coverImage: '',
    teacherName: '王老师',
    price: 399,
    typeId: 2,
    type: { id: 2, name: '后端开发' },
    createdAt: '2024-03-01 09:15:00',
    updatedAt: '2024-03-01 09:15:00'
  },
  {
    id: 5,
    title: 'Java 基础教程',
    description: 'Java编程基础，面向对象编程思想',
    coverImage: '',
    teacherName: '刘老师',
    price: 99,
    typeId: 2,
    type: { id: 2, name: '后端开发' },
    createdAt: '2024-04-01 14:00:00',
    updatedAt: '2024-04-01 14:00:00'
  }
])

// 方法：查看课程详情
const viewCourse = (course) => {
  router.push(`/course/${course.id}`)
}

// 防重复点击状态管理
const isProcessing = ref(false)

// 方法：取消收藏
const removeFavorite = async (course) => {
  // 防重复点击检查
  if (isProcessing.value) {
    return
  }
  
  try {
    isProcessing.value = true
    
    // 1. 显示确认对话框
    await ElMessageBox.confirm('确定要取消收藏这门课程吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 找到课程索引
    const index = favoriteCourses.value.findIndex(item => item.id === course.id)
    if (index !== -1) {
      // 2. 向后端发送取消收藏请求，并显示加载状态
      const loadingMessage = ElMessage({
        message: '取消收藏中...',
        type: 'info',
        duration: 0,
        showClose: false
      })
      
      try {
        // 3. 向后端发送取消收藏请求
        await cancelFavoriteAPI(course.id)
        
        // 4. 收到成功响应后，更新UI
        // 添加移除动画效果
        const cardElement = document.querySelectorAll('.course-card')[index]
        if (cardElement) {
          cardElement.style.animation = 'slideOutRight 0.5s ease forwards'
          // 等待动画完成后再移除课程
          await new Promise(resolve => setTimeout(resolve, 500))
        }
        
        // 从收藏列表中移除课程
        favoriteCourses.value.splice(index, 1)
        
        // 关闭加载提示
        loadingMessage.close()
        
        // 5. 显示成功提示
        ElMessage.success({
          message: '取消收藏成功',
          duration: 2000,
          showClose: true
        })
        
        // 6. 如果收藏列表为空，可以添加一个空状态提示
        if (favoriteCourses.value.length === 0) {
          // 这里可以添加空状态处理逻辑
          console.log('收藏列表已空')
        }
      } catch (apiError) {
        // 关闭加载提示
        loadingMessage.close()
        
        // 7. 若请求失败，显示错误提示并保留课程在列表中
        ElMessage.error({
          message: '取消收藏失败，请稍后重试',
          duration: 2000,
          showClose: true
        })
        console.error('取消收藏失败:', apiError)
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      // 处理其他异常情况
      ElMessage.error({
        message: '操作失败，请稍后重试',
        duration: 2000,
        showClose: true
      })
      console.error('操作失败:', error)
    } else {
      // 用户取消操作
      ElMessage.info('已取消操作')
    }
  } finally {
    // 重置处理状态
    isProcessing.value = false
  }
}

// 模拟API调用：取消收藏课程
const cancelFavoriteAPI = async (courseId) => {
  // 模拟向后端发送取消收藏请求
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      // 模拟API成功响应
      const success = Math.random() > 0.1; // 90%成功率
      if (success) {
        resolve({ success: true, message: '取消收藏成功' });
      } else {
        reject(new Error('网络请求失败'));
      }
    }, 300); // 模拟网络延迟
  });
};

// 页面加载时获取收藏课程数据
onMounted(() => {
  // 这里可以添加从后端获取收藏课程的逻辑
  console.log('获取收藏课程数据');
  // 示例：fetchFavoriteCourses();
});

// 模拟API调用：获取收藏课程列表
const fetchFavoriteCourses = async () => {
  // 模拟从后端获取收藏课程数据
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(favoriteCourses.value); // 返回模拟数据
    }, 500);
  });
};
</script>

<style scoped>
/* 容器样式 */
.favorite-container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 20px;
}

.favorite-title {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 30px;
  text-align: center;
  color: #333;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 内容区域 */
.favorite-content {
  background: linear-gradient(135deg, #f8f9ff 0%, #eef2ff 100%);
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.favorite-content:hover {
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

/* 空状态 */
.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
}

/* 课程网格布局 */
.favorite-courses-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
  margin-top: 20px;
}

/* 课程卡片 */
.course-card {
  perspective: 1000px;
}

.course-card-inner {
  background: white;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s ease;
  transform-style: preserve-3d;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.course-card-inner:hover {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 12px 36px rgba(0, 0, 0, 0.15);
}

/* 课程封面 */
.course-cover {
  position: relative;
  width: 100%;
  padding-top: 60%;
  overflow: hidden;
}

.cover-placeholder {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: flex-start;
  justify-content: flex-start;
  padding: 12px;
}

.course-type {
  background: rgba(255, 255, 255, 0.9);
  color: #333;
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

/* 课程信息 */
.course-info {
  padding: 20px;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.course-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.3s ease;
}

.course-card-inner:hover .course-title {
  color: #667eea;
}

.course-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  font-size: 14px;
}

.course-teacher {
  color: #666;
  font-weight: 500;
}

.course-price {
  color: #ff6b6b;
  font-weight: 700;
  font-size: 16px;
}

.course-price:contains('免费') {
  color: #4ecdc4;
}

/* 操作按钮 */
.course-actions {
  display: flex;
  gap: 8px;
  padding: 0 20px 20px;
}

.action-btn {
  flex: 1;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 按钮样式 */
.el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.el-button--primary:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a408a 100%);
}

.el-button--danger {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border: none;
}

.el-button--danger:hover {
  background: linear-gradient(135deg, #ff5252 0%, #e53935 100%);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .favorite-courses-grid {
    grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .favorite-container {
    padding: 15px;
  }
  
  .favorite-content {
    padding: 20px;
  }
  
  .favorite-courses-grid {
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 16px;
  }
  
  .favorite-title {
    font-size: 24px;
    margin-bottom: 20px;
  }
  
  .course-info {
    padding: 16px;
  }
  
  .course-title {
    font-size: 16px;
  }
  
  .course-actions {
    padding: 0 16px 16px;
  }
}

@media (max-width: 480px) {
  .favorite-courses-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .course-card-inner {
    max-width: 100%;
  }
}

/* 平滑滚动 */
.favorite-content {
  scroll-behavior: smooth;
}

/* 加载动画 */
.course-card {
  animation: fadeInUp 0.6s ease forwards;
  opacity: 0;
}

.course-card:nth-child(1) { animation-delay: 0.1s; }
.course-card:nth-child(2) { animation-delay: 0.2s; }
.course-card:nth-child(3) { animation-delay: 0.3s; }
.course-card:nth-child(4) { animation-delay: 0.4s; }
.course-card:nth-child(5) { animation-delay: 0.5s; }
.course-card:nth-child(6) { animation-delay: 0.6s; }
.course-card:nth-child(7) { animation-delay: 0.7s; }
.course-card:nth-child(8) { animation-delay: 0.8s; }
.course-card:nth-child(9) { animation-delay: 0.9s; }

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideOutRight {
  from {
    opacity: 1;
    transform: translateX(0);
  }
  to {
    opacity: 0;
    transform: translateX(100%);
    height: 0;
    margin: 0;
    padding: 0;
    overflow: hidden;
  }
}
</style>