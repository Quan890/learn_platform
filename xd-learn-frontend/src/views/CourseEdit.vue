<template>
  <div class="course-edit-container">
    <div class="course-edit-header">
      <h1>{{ isEdit ? '编辑课程' : '创建课程' }}</h1>
      <el-button type="info" @click="$router.back()">返回</el-button>
    </div>
    
    <el-card shadow="hover" class="course-edit-card">
      <el-form
        ref="courseFormRef"
        :model="courseForm"
        :rules="courseRules"
        label-width="120px"
        class="course-form"
      >
        <el-form-item label="课程标题" prop="title">
          <el-input v-model="courseForm.title" placeholder="请输入课程标题" maxlength="100" show-word-limit></el-input>
        </el-form-item>
        
        <el-form-item label="课程描述" prop="description">
          <el-input
            v-model="courseForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入课程描述"
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
        
        <el-form-item label="课程封面">
          <el-upload
            action="#"
            :auto-upload="false"
            :on-change="handleCoverChange"
            accept="image/*"
            :show-file-list="true"
            class="cover-upload"
          >
            <el-button type="primary">
              <el-icon><Upload /></el-icon>
              选择封面
            </el-button>
            <template #file-list>
              <div class="cover-preview" v-if="courseForm.cover">
                <div class="cover-preview-placeholder" :style="{ backgroundImage: `linear-gradient(135deg, #667eea 0%, #764ba2 100%)` }">
                  <div class="cover-preview-title">{{ courseForm.title || '课程封面' }}</div>
                </div>
                <el-button type="text" @click="courseForm.cover = ''">删除</el-button>
              </div>
            </template>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="课程分类" prop="category">
          <el-select v-model="courseForm.category" placeholder="请选择课程分类">
            <el-option label="编程" value="programming"></el-option>
            <el-option label="设计" value="design"></el-option>
            <el-option label="营销" value="marketing"></el-option>
            <el-option label="语言" value="language"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程价格" prop="price">
          <el-input-number
            v-model="courseForm.price"
            :min="0"
            :precision="2"
            placeholder="请输入课程价格"
          ></el-input-number>
        </el-form-item>
        
        <el-form-item label="课程状态" prop="status">
          <el-select v-model="courseForm.status" placeholder="请选择课程状态">
            <el-option label="草稿" value="draft"></el-option>
            <el-option label="发布" value="published"></el-option>
            <el-option label="下架" value="offline"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="saving"
            @click="handleSave"
            size="large"
          >
            {{ isEdit ? '保存修改' : '创建课程' }}
          </el-button>
          <el-button
            type="danger"
            :loading="deleting"
            @click="handleDelete"
            size="large"
            v-if="isEdit"
          >
            删除课程
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 课程资料上传 -->
      <div class="course-materials">
        <h3>课程资料</h3>
        <el-upload
          :action="uploadUrl"
          :headers="uploadHeaders"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          :before-upload="beforeUpload"
          :file-list="uploadFileList"
          multiple
          accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.zip,.rar"
          class="material-upload"
        >
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            上传资料
          </el-button>
          <template #tip>
            <div class="upload-tip">
              支持上传 PDF、Word、Excel、PPT、压缩包等格式文件，单个文件不超过 50MB
            </div>
          </template>
        </el-upload>
        
        <!-- 资料列表 -->
        <div class="materials-list" v-if="materials.length > 0">
          <h4>已上传资料</h4>
          <el-table
            :data="materials"
            style="width: 100%"
            border
            size="small"
          >
            <el-table-column prop="name" label="资料名称"></el-table-column>
            <el-table-column prop="type" label="文件类型"></el-table-column>
            <el-table-column prop="size" label="文件大小" width="100">
              <template #default="scope">
                {{ formatFileSize(scope.row.size) }}
              </template>
            </el-table-column>
            <el-table-column prop="uploadTime" label="上传时间" width="180">
              <template #default="scope">
                {{ formatDate(scope.row.uploadTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  @click="downloadMaterial(scope.row)"
                >
                  <el-icon><Download /></el-icon>
                  下载
                </el-button>
                <el-button
                  type="danger"
                  size="small"
                  @click="deleteMaterial(scope.row)"
                >
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Upload, Download, Delete } from '@element-plus/icons-vue'
import { getToken } from '../utils/auth'

const router = useRouter()
const route = useRoute()
const courseFormRef = ref()
const saving = ref(false)
const deleting = ref(false)

// 判断是否为编辑模式
const isEdit = computed(() => {
  return !!route.params.id
})

// 课程表单
const courseForm = reactive({
  title: '',
  description: '',
  cover: '',
  category: '',
  price: 0,
  status: 'draft'
})

// 课程表单验证规则
const courseRules = {
  title: [
    { required: true, message: '请输入课程标题', trigger: 'blur' },
    { min: 5, max: 100, message: '课程标题长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入课程描述', trigger: 'blur' },
    { min: 20, max: 500, message: '课程描述长度在 20 到 500 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择课程分类', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择课程状态', trigger: 'change' }
  ]
}

// 资料上传
const uploadUrl = '/api/materials/upload' // 模拟地址
const uploadHeaders = {
  Authorization: `Bearer ${getToken()}`
}
const uploadFileList = ref([])

// 模拟资料数据
const materials = ref([
  {
    id: 1,
    name: 'Vue 3 学习笔记.pdf',
    type: 'pdf',
    size: 2048576,
    uploadTime: new Date(Date.now() - 86400000).toISOString()
  },
  {
    id: 2,
    name: 'Vue 3 实战代码.zip',
    type: 'zip',
    size: 5242880,
    uploadTime: new Date(Date.now() - 172800000).toISOString()
  }
])

// 模拟课程数据
const mockCourses = [
  {
    id: 1,
    title: 'Vue 3 从入门到精通',
    description: '全面学习Vue 3框架，包括组合式API、响应式系统、组件开发等核心内容',
    cover: 'https://via.placeholder.com/300x200?text=Vue+3+Course',
    teacher: '张老师',
    studentCount: 1234,
    rating: 4.8,
    price: 99,
    category: 'programming',
    status: 'published',
    createdAt: new Date(Date.now() - 86400000 * 7).toISOString()
  },
  {
    id: 2,
    title: 'JavaScript 高级编程',
    description: '深入学习JavaScript的高级特性，包括闭包、原型链、异步编程等',
    cover: 'https://via.placeholder.com/300x200?text=JavaScript+Course',
    teacher: '李老师',
    studentCount: 2345,
    rating: 4.9,
    price: 129,
    category: 'programming',
    status: 'draft',
    createdAt: new Date(Date.now() - 86400000 * 3).toISOString()
  }
]

// 初始化课程数据
const initCourseData = () => {
  if (isEdit.value) {
    // 编辑模式，加载课程数据
    const courseId = parseInt(route.params.id)
    const course = mockCourses.find(c => c.id === courseId)
    if (course) {
      Object.assign(courseForm, course)
    } else {
      ElMessage.error('课程不存在')
      router.push('/profile/courses')
    }
  }
}

// 保存课程
const handleSave = async () => {
  try {
    await courseFormRef.value.validate()
    saving.value = true
    
    // 模拟API延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(isEdit.value ? '课程修改保存成功' : '课程创建成功')
    router.push('/profile/courses')
  } catch (error) {
    if (error.message !== 'Validation failed') {
      ElMessage.error('操作失败: ' + (error.message || '未知错误'))
    }
  } finally {
    saving.value = false
  }
}

// 删除课程
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm('确定要删除该课程吗？删除后不可恢复', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    deleting.value = true
    
    // 模拟API延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('课程删除成功')
    router.push('/profile/courses')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + (error.message || '未知错误'))
    }
  } finally {
    deleting.value = false
  }
}

// 处理封面上传
const handleCoverChange = (file) => {
  // 读取文件并显示预览
  const reader = new FileReader()
  reader.onload = (e) => {
    courseForm.cover = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

// 资料上传前校验
const beforeUpload = (file) => {
  const isLt50M = file.size / 1024 / 1024 < 50
  if (!isLt50M) {
    ElMessage.error('单个文件大小不能超过 50MB')
    return false
  }
  return true
}

// 资料上传成功
const handleUploadSuccess = (response) => {
  ElMessage.success('文件上传成功')
  // 清空文件列表
  uploadFileList.value = []
}

// 资料上传失败
const handleUploadError = (error) => {
  ElMessage.error('文件上传失败: ' + (error.message || '未知错误'))
}

// 下载资料
const downloadMaterial = async (material) => {
  try {
    // 模拟API延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(`${material.name} 下载开始`)
    
    // 实际项目中，这里会调用真实的下载API
    // 模拟下载成功
    setTimeout(() => {
      ElMessage.success(`${material.name} 下载完成`)
    }, 2000)
  } catch (error) {
    ElMessage.error('下载失败: ' + (error.message || '未知错误'))
  }
}

// 删除资料
const deleteMaterial = async (material) => {
  try {
    await ElMessageBox.confirm('确定要删除该资料吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟API延迟
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 从列表中移除
    const index = materials.value.findIndex(m => m.id === material.id)
    if (index > -1) {
      materials.value.splice(index, 1)
    }
    
    ElMessage.success('资料删除成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + (error.message || '未知错误'))
    }
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 格式化文件大小
const formatFileSize = (size) => {
  if (!size) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  let index = 0
  let fileSize = size
  while (fileSize >= 1024 && index < units.length - 1) {
    fileSize /= 1024
    index++
  }
  return `${fileSize.toFixed(2)} ${units[index]}`
}

// 初始加载
onMounted(() => {
  initCourseData()
})
</script>

<style scoped>
.course-edit-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.course-edit-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.course-edit-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.course-edit-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.course-form {
  max-width: 800px;
  margin-bottom: 30px;
}

.course-form .el-form-item {
  margin-bottom: 20px;
}

.cover-upload {
  display: flex;
  align-items: center;
  gap: 20px;
}

.cover-preview {
  display: flex;
  align-items: center;
  gap: 10px;
}

.cover-preview-placeholder {
  width: 200px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #ebeef5;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.cover-preview-title {
  color: white;
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  padding: 12px;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border-radius: 6px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  max-width: 80%;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.4;
}

.course-materials {
  margin-top: 30px;
  border-top: 1px solid #ebeef5;
  padding-top: 20px;
}

.course-materials h3 {
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.course-materials h4 {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: 500;
  color: #606266;
}

.material-upload {
  margin-bottom: 20px;
}

.upload-tip {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

.materials-list {
  margin-top: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .course-edit-container {
    padding: 10px;
  }
  
  .course-edit-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }
  
  .course-edit-card {
    padding: 15px;
  }
  
  .course-form {
    max-width: 100%;
  }
  
  .course-form .el-form-item__label {
    width: 80px;
    font-size: 14px;
  }
  
  .cover-upload {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .cover-preview-placeholder {
    width: 150px;
    height: 90px;
  }
}
</style>