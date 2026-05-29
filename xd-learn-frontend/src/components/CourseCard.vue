<template>
  <el-card class="course-card" @click="handleCardClick" shadow="hover">
    <template #header>
      <div class="course-title-container">
        <h3 class="course-title">{{ course.title }}</h3>
        <el-tag type="primary" size="small">{{ course.type?.name || course.type }}</el-tag>
      </div>
    </template>
    
    <div class="course-meta">
      <el-avatar :size="28" class="author-avatar">{{ course.teacherName?.charAt(0) || course.author?.charAt(0) }}</el-avatar>
      <span class="course-teacher">{{ course.teacherName || course.author }}</span>
      <el-rate :model-value="course.rating" disabled show-score :score-template="`${parseFloat(course.rating || 0).toFixed(1)}`" class="course-rating"></el-rate>
      <span class="course-price" :class="{ 'free': course.price === 0 }">
        {{ course.price === 0 ? '免费' : `¥${course.price}` }}
      </span>
    </div>
    
    <div class="course-stats">
      <span class="stat-item">
        <el-icon><UserFilled /></el-icon>
        <span>{{ course.students || course.studentCount || course.enrollmentCount || course.participantCount || 0 }} 人学习</span>
      </span>
      <span class="stat-item">
        <el-icon><Clock /></el-icon>
        <span>创建于 {{ formatDate(course.createdAt || course.createTime || course.creationTime) }}</span>
      </span>
    </div>
  </el-card>
</template>

<script setup>
import { VideoPlay, UserFilled, Clock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  course: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const handleCardClick = () => {
  router.push(`/course/${props.course.id}`)
}

// 格式化日期
const formatDate = (dateValue) => {
  if (!dateValue) return ''
  
  let date
  // 处理不同类型的时间数据
  if (typeof dateValue === 'number') {
    // 处理时间戳（毫秒或秒）
    if (dateValue.toString().length === 10) {
      // 秒级时间戳转换为毫秒级
      date = new Date(dateValue * 1000)
    } else {
      // 毫秒级时间戳直接使用
      date = new Date(dateValue)
    }
  } else if (typeof dateValue === 'string') {
    // 处理字符串格式的时间
    date = new Date(dateValue)
  } else {
    date = dateValue
  }
  
  // 检查日期是否有效
  if (isNaN(date.getTime())) return ''
  
  // 格式化日期为"年月日"格式（例如：2023-10-25）
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  
  return `${year}-${month}-${day}`
}
</script>

<style scoped>
.course-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  cursor: pointer;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.course-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.course-title-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
}

.course-title {
  font-size: 18px;
  font-weight: bold;
  margin: 0;
  color: var(--text-color-primary);
  line-height: 1.4;
  width: 100%;
  word-break: break-word;
}

.course-title-container .el-tag {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 12px;
  font-weight: normal;
  padding: 2px 10px;
  border-radius: 12px;
}

.course-description {
  font-size: 14px;
  color: var(--text-color-regular);
  margin: 12px 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.course-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.author-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.course-teacher {
  font-size: 13px;
  color: var(--text-color-secondary);
  font-weight: 500;
}

.course-rating {
  display: flex;
  align-items: center;
}

.course-price {
  font-size: 18px;
  font-weight: bold;
  color: var(--danger-color);
  margin-left: auto;
}

.course-price.free {
  color: var(--success-color);
}

.course-stats {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: var(--text-color-secondary);
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--border-color-light);
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.stat-item .el-icon {
  font-size: 14px;
}
</style>
