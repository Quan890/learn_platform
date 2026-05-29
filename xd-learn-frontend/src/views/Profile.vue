<template>
  <div class="profile-container">
    <h2 class="profile-title">个人中心</h2>
    
    <div class="profile-content">
      <div class="profile-info">
        <div class="profile-header">
          <el-skeleton :loading="loading" animated>
            <template #template>
              <el-avatar :size="120" class="profile-avatar"></el-avatar>
            </template>
            <el-avatar :size="120" class="profile-avatar">
              {{ userInfo.username.charAt(0).toUpperCase() || 'U' }}
            </el-avatar>
          </el-skeleton>
        </div>
        
        <div class="profile-details">
          <div class="user-info-grid">
            <el-skeleton :loading="loading" animated>
              <template #template>
                <div class="info-item">
                  <div class="info-label"></div>
                  <div class="info-value skeleton-line"></div>
                </div>
              </template>
              <div class="info-item">
                <div class="info-label">用户名</div>
                <div class="info-value">{{ userInfo.username || '-' }}</div>
              </div>
            </el-skeleton>
            
            <el-skeleton :loading="loading" animated>
              <template #template>
                <div class="info-item">
                  <div class="info-label"></div>
                  <div class="info-value skeleton-line"></div>
                </div>
              </template>
              <div class="info-item">
                <div class="info-label">昵称</div>
                <div class="info-value">{{ userInfo.nickname || '-' }}</div>
              </div>
            </el-skeleton>
            
            <el-skeleton :loading="loading" animated>
              <template #template>
                <div class="info-item">
                  <div class="info-label"></div>
                  <div class="info-value skeleton-line"></div>
                </div>
              </template>
              <div class="info-item">
                <div class="info-label">邮箱</div>
                <div class="info-value">{{ userInfo.email || '-' }}</div>
              </div>
            </el-skeleton>
            
            <el-skeleton :loading="loading" animated>
              <template #template>
                <div class="info-item">
                  <div class="info-label"></div>
                  <div class="info-value skeleton-line"></div>
                </div>
              </template>
              <div class="info-item">
                <div class="info-label">注册时间</div>
                <div class="info-value">{{ userInfo.registerDate || '-' }}</div>
              </div>
            </el-skeleton>
          </div>
        </div>
        
        <div class="profile-actions">
          <el-button 
            type="primary" 
            :icon="Edit" 
            @click="openEditProfileForm"
            :loading="loading"
          >
            编辑资料
          </el-button>
        </div>
        
        <!-- 错误提示 -->
        <el-alert
          v-if="error"
          :title="error"
          type="error"
          show-icon
          closable
          @close="error = ''"
          class="mt-4"
        ></el-alert>
      </div>
      
      <div class="profile-stats">
        <h3 class="section-title">学习统计</h3>
        
        <div class="stats-grid">
          <div class="stat-card">
            <el-card shadow="hover" class="stat-item">
              <template #header>
                <div class="card-header">
                  <span>已学课程</span>
                </div>
              </template>
              <div class="stat-value">{{ userInfo.courseCount }}</div>
            </el-card>
          </div>
          
          <div class="stat-card">
            <el-card shadow="hover" class="stat-item">
              <template #header>
                <div class="card-header">
                  <span>学习时长</span>
                </div>
              </template>
              <div class="stat-value">{{ userInfo.learningHours }} 小时</div>
            </el-card>
          </div>
          
          <div class="stat-card">
            <el-card shadow="hover" class="stat-item" @click="goToFavorites">
              <template #header>
                <div class="card-header">
                  <span>收藏课程</span>
                </div>
              </template>
              <div class="stat-value">{{ userInfo.favoriteCount }}</div>
            </el-card>
          </div>
          
          <div class="stat-card">
            <el-card shadow="hover" class="stat-item">
              <template #header>
                <div class="card-header">
                  <span>获得成就</span>
                </div>
              </template>
              <div class="stat-value">{{ userInfo.achievementCount }}</div>
            </el-card>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 课程管理区域 -->
    <div class="profile-courses-management">
      <div class="section-header">
        <h3 class="section-title">课程管理</h3>
        <el-button 
          type="primary" 
          :icon="Plus" 
          @click="openCreateForm"
        >
          创建课程
        </el-button>
      </div>
      
      <!-- 我的课程列表（根据后端Course实体调整） -->
      <div class="my-courses">
        <el-table :data="myCourses" stripe style="width: 100%">
          <el-table-column prop="id" label="课程ID" width="80"></el-table-column>
          <el-table-column prop="title" label="课程名称" width="200"></el-table-column>
          <el-table-column prop="teacherName" label="教师" width="100"></el-table-column>
          <el-table-column prop="price" label="价格" width="100">
            <template #default="scope">
              <span>¥{{ scope.row.price }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="type.name" label="分类" width="120"></el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="150"></el-table-column>
          <el-table-column prop="updatedAt" label="更新时间" width="150"></el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button 
                type="primary" 
                :icon="Edit" 
                size="small" 
                @click="editCourse(scope.row)"
              >
                编辑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    
    <!-- 编辑用户资料表单对话框 -->
    <el-dialog
      title="编辑资料"
      v-model="showEditProfileForm"
      width="500px"
    >
      <el-form
        :model="editProfileForm"
        :rules="editProfileRules"
        ref="editProfileFormRef"
        label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="editProfileForm.username" placeholder="请输入用户名" disabled></el-input>
        </el-form-item>
        
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="editProfileForm.nickname" placeholder="请输入昵称"></el-input>
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editProfileForm.email" placeholder="请输入邮箱" type="email"></el-input>
        </el-form-item>
        
        <el-form-item label="新密码" prop="password" v-if="showPasswordField">
          <el-input v-model="editProfileForm.password" placeholder="请输入新密码" type="password" show-password></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="showPasswordField">修改密码</el-checkbox>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditProfileForm = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="submitEditProfileForm"
            :loading="isSubmittingProfile"
          >
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 创建/编辑课程表单对话框（根据后端Course实体调整） -->
    <el-dialog
      :title="isEditing ? '编辑课程' : '创建课程'"
      v-model="showCourseForm"
      width="600px"
    >
      <el-form
        :model="courseForm"
        :rules="courseRules"
        ref="courseFormRef"
        label-width="100px"
      >
        <el-form-item label="课程名称" prop="title">
          <el-input v-model="courseForm.title" placeholder="请输入课程名称"></el-input>
        </el-form-item>
        
        <el-form-item label="课程描述" prop="description">
          <el-input
            v-model="courseForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入课程描述"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="教师姓名" prop="teacherName">
          <el-input v-model="courseForm.teacherName" placeholder="请输入教师姓名"></el-input>
        </el-form-item>
        
        <el-form-item label="课程价格" prop="price">
          <el-input 
            v-model="courseForm.price" 
            type="number" 
            placeholder="请输入课程价格"
            :min="0"
            :precision="2"
          ></el-input>
        </el-form-item>
        
        <el-form-item label="课程分类" prop="typeId">
          <el-select v-model="courseForm.typeId" placeholder="请选择课程分类">
            <el-option
              v-for="type in courseTypes"
              :key="type.id"
              :label="type.name"
              :value="type.id"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="封面图片" prop="coverImage">
          <el-upload
            class="avatar-uploader"
            action="#"
            :show-file-list="false"
            :before-upload="handleImageUpload"
            :http-request="uploadImage"
          >
            <img v-if="courseForm.coverImage" :src="courseForm.coverImage" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCourseForm = false">取消</el-button>
          <el-button 
            type="primary" 
            @click="submitCourseForm"
            :loading="isSubmitting"
          >
            {{ isEditing ? '保存' : '创建' }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { Edit, Refresh, Plus } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElNotification } from 'element-plus'
import { userApi } from '../api/index'

const router = useRouter()

// 用户信息数据
const userInfo = ref({
  username: '',
  nickname: '',
  email: '',
  registerDate: '',
  courseCount: 0,
  learningHours: 0,
  favoriteCount: 0,
  achievementCount: 0,
  avatar: ''
})

// 加载状态
const loading = ref(false)
// 错误状态
const error = ref('')

// 模拟最近学习的课程
const recentCourses = ref([
  {
    id: 1,
    title: 'Vue3 从入门到精通',
    author: '张老师',
    progress: 65,
    status: '学习中'
  },
  {
    id: 2,
    title: 'React 高级实战',
    author: '李老师',
    progress: 30,
    status: '学习中'
  },
  {
    id: 3,
    title: 'Node.js 全栈开发',
    author: '王老师',
    progress: 100,
    status: '已完成'
  }
])

// 模拟课程分类列表（对应后端CourseType实体）
const courseTypes = ref([
  { id: 1, name: '前端开发', description: '前端开发相关课程' },
  { id: 2, name: '后端开发', description: '后端开发相关课程' },
  { id: 3, name: '移动端开发', description: '移动端开发相关课程' },
  { id: 4, name: '数据库', description: '数据库相关课程' },
  { id: 5, name: '人工智能', description: '人工智能相关课程' },
  { id: 6, name: 'DevOps', description: 'DevOps相关课程' },
  { id: 7, name: '数据分析', description: '数据分析相关课程' }
])

// 模拟我的课程列表（根据后端Course实体调整）
const myCourses = ref([
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
    id: 2,
    title: 'React 高级实战',
    description: '深入理解React原理，构建复杂应用',
    coverImage: '',
    teacherName: '李老师',
    price: 299,
    typeId: 1,
    type: { id: 1, name: '前端开发' },
    createdAt: '2024-02-01 14:30:00',
    updatedAt: '2024-02-01 14:30:00'
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
  }
])

// 课程表单相关数据
const showCourseForm = ref(false)
const isEditing = ref(false)
const courseFormRef = ref(null)
const isSubmitting = ref(false)

// 课程表单数据（根据后端Course实体调整）
const courseForm = reactive({
  id: null,
  title: '',
  description: '',
  coverImage: '',
  teacherName: '',
  price: 0,
  typeId: null
})

// 课程表单验证规则（根据后端Course实体调整）
const courseRules = reactive({
  title: [
    { required: true, message: '请输入课程名称', trigger: 'blur' },
    { min: 2, max: 50, message: '课程名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入课程描述', trigger: 'blur' },
    { min: 10, max: 500, message: '课程描述长度在 10 到 500 个字符', trigger: 'blur' }
  ],
  teacherName: [
    { required: true, message: '请输入教师姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '教师姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入课程价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '课程价格必须大于等于0', trigger: 'blur' }
  ],
  typeId: [
    { required: true, message: '请选择课程类型', trigger: 'change' }
  ]
})

// 编辑用户资料表单相关数据
const showEditProfileForm = ref(false)
const editProfileFormRef = ref(null)
const isSubmittingProfile = ref(false)
const showPasswordField = ref(false)

// 编辑用户资料表单数据
const editProfileForm = reactive({
  username: '',
  nickname: '',
  email: '',
  password: ''
})

// 编辑用户资料表单验证规则
const editProfileRules = reactive({
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' }
  ],
  nickname: [
    { min: 0, max: 20, message: '昵称长度不能超过20个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: false, message: '密码不能为空', trigger: 'blur', validator: (rule, value, callback) => {
      // 只有在修改密码选项被勾选时才验证密码
      if (showPasswordField.value) {
        if (!value) {
          callback(new Error('新密码不能为空'))
        } else if (value.length < 6 || value.length > 20) {
          callback(new Error('新密码长度在 6 到 20 个字符'))
        } else if (!/^(?=.*[a-zA-Z])(?=.*\d)/.test(value)) {
          callback(new Error('新密码必须包含字母和数字'))
        } else if (/\s/.test(value)) {
          callback(new Error('新密码不能包含空格'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }}
  ]
})

// 打开创建课程表单
const showCreateForm = ref(false)

// 方法：显示创建课程表单
const openCreateForm = () => {
  isEditing.value = false
  resetForm()
  showCourseForm.value = true
}

// 方法：编辑课程（根据后端Course实体调整）
const editCourse = (row) => {
  isEditing.value = true
  // 复制课程数据到表单
  courseForm.id = row.id
  courseForm.title = row.title
  courseForm.description = row.description
  courseForm.coverImage = row.coverImage || ''
  courseForm.teacherName = row.teacherName
  courseForm.price = row.price
  courseForm.typeId = row.typeId
  showCourseForm.value = true
}

// 方法：重置表单（根据后端Course实体调整）
const resetForm = () => {
  courseForm.id = null
  courseForm.title = ''
  courseForm.description = ''
  courseForm.coverImage = ''
  courseForm.teacherName = ''
  courseForm.price = 0
  courseForm.typeId = null
  if (courseFormRef.value) {
    courseFormRef.value.resetFields()
  }
}

// 方法：提交课程表单（根据后端Course实体调整）
const submitCourseForm = async () => {
  if (!courseFormRef.value) return
  
  try {
    await courseFormRef.value.validate()
    isSubmitting.value = true
    
    // 模拟异步提交
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 查找对应的课程类型对象
    const courseType = courseTypes.value.find(type => type.id === courseForm.typeId)
    
    if (isEditing.value) {
      // 编辑现有课程
      const index = myCourses.value.findIndex(course => course.id === courseForm.id)
      if (index !== -1) {
        myCourses.value[index] = {
          ...courseForm,
          type: courseType,
          updatedAt: new Date().toLocaleString()
        }
      }
    } else {
      // 创建新课程
      const newCourse = {
        ...courseForm,
        id: Date.now(), // 模拟生成唯一ID
        type: courseType,
        createdAt: new Date().toLocaleString(),
        updatedAt: new Date().toLocaleString()
      }
      myCourses.value.unshift(newCourse)
    }
    
    showCourseForm.value = false
    isSubmitting.value = false
  } catch (error) {
    console.error('表单验证失败:', error)
    isSubmitting.value = false
  }
}

// 方法：处理图片上传
const handleImageUpload = (file) => {
  // 模拟图片上传，返回一个模拟的图片URL
  return new Promise(resolve => {
    // 模拟上传成功，返回一个URL
    resolve({
      url: URL.createObjectURL(file.raw)
    })
  })
}

// 方法：自定义图片上传处理
const uploadImage = (options) => {
  handleImageUpload(options.file)
    .then(response => {
      courseForm.coverImage = response.url
      options.onSuccess(response)
    })
    .catch(error => {
      options.onError(error)
    })
}

// 方法：打开编辑用户资料表单
const openEditProfileForm = () => {
  if (loading.value) {
    ElMessage.info('正在加载用户信息，请稍候...')
    return
  }
  
  // 确保表单数据与最新用户信息同步
  updateEditFormFromUserInfo()
  editProfileForm.password = ''
  showPasswordField.value = false
  showEditProfileForm.value = true
}

// 监听用户信息变化，确保编辑表单数据始终与最新用户信息同步
watch(
  () => [userInfo.value.username, userInfo.value.nickname, userInfo.value.email],
  ([newUsername, newNickname, newEmail]) => {
    // 只有在表单未打开时才更新，避免覆盖用户正在编辑的内容
    if (!showEditProfileForm.value) {
      editProfileForm.username = newUsername || ''
      editProfileForm.nickname = newNickname || ''
      editProfileForm.email = newEmail || ''
    }
  },
  { deep: true }
)

// 方法：提交编辑用户资料表单
const submitEditProfileForm = async () => {
  if (!editProfileFormRef.value) return
  
  try {
    // 1. 表单验证
    await editProfileFormRef.value.validate()
    isSubmittingProfile.value = true
    
    // 2. 准备提交数据
    const updateData = {
      username: editProfileForm.username,
      nickname: editProfileForm.nickname,
      email: editProfileForm.email,
      // 只有在修改密码选项被勾选且密码不为空时才包含新密码，使用后端要求的参数名 "newPassword"
      ...(showPasswordField.value && editProfileForm.password ? { newPassword: editProfileForm.password } : {})
    }
    
    // 3. 调用后端API更新用户资料
    const response = await userApi.updateProfile(updateData)
    
    // 4. 处理响应
    if (response && response.code === 200) {
      // 5. 更新本地用户信息
      await fetchUserInfo() // 重新获取最新用户信息，确保数据一致性
      
      // 6. 关闭表单
      showEditProfileForm.value = false
      
      // 7. 显示成功提示
      ElMessage.success(response.message || '用户资料更新成功')
      
      // 8. 重置密码字段和显示状态
      editProfileForm.password = ''
      showPasswordField.value = false
    } else {
      // 处理API返回的错误
      throw new Error(response?.message || '用户资料更新失败')
    }
  } catch (error) {
    console.error('更新用户资料失败:', error)
    
    // 9. 处理不同类型的错误
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      // 延迟跳转到登录页
      setTimeout(() => {
        router.push('/login')
      }, 1500)
    } else if (error.response?.status === 400) {
      // 后端返回的验证错误
      const errorMessage = error.response.data?.message || '输入数据不符合要求'
      // 提取更具体的密码相关错误信息
      if (errorMessage.toLowerCase().includes('password') || errorMessage.includes('密码')) {
        ElMessage.error(errorMessage)
      } else {
        ElMessage.error(errorMessage)
      }
    } else if (error.response?.status === 403) {
      ElMessage.error('您没有权限执行此操作')
    } else if (error.response?.status === 500) {
      ElMessage.error('服务器内部错误，请稍后重试')
    } else {
      // 网络错误或其他未知错误
      ElMessage.error(error.message || '网络连接失败，请检查网络设置')
    }
  } finally {
    // 10. 无论成功失败，都清除提交状态
    isSubmittingProfile.value = false
  }
}

// 方法：格式化日期时间
const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return '-'
  
  try {
    const date = new Date(dateTimeString)
    if (isNaN(date.getTime())) return '-'
    
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  } catch (error) {
    console.error('日期格式化失败:', error)
    return '-'
  }
}

// 方法：获取用户信息
const fetchUserInfo = async () => {
  try {
    loading.value = true
    error.value = ''
    
    const response = await userApi.getUserInfo()
    
    if (response && response.code === 200 && response.data) {
      const userData = response.data
      
      // 映射后端字段到前端预期字段，确保兼容不同命名约定
      const mappedUserInfo = {
        ...userInfo.value,
        ...userData,
        // 确保用户名字段正确映射（处理不同命名约定）
        username: userData.username || userData.userName || userData.name || '',
        // 确保昵称字段正确映射
        nickname: userData.nickname || userData.displayName || userData.username || '',
        // 确保邮箱字段正确映射
        email: userData.email || userData.Email || '',
        // 确保数字类型字段正确转换
        courseCount: Number(userData.courseCount) || 0,
        learningHours: Number(userData.learningHours) || 0,
        favoriteCount: Number(userData.favoriteCount) || 0,
        achievementCount: Number(userData.achievementCount) || 0,
        // 处理注册时间，确保兼容不同字段名
        registerDate: formatDateTime(userData.registerTime || userData.registerDate)
      }
      
      // 更新用户信息
      userInfo.value = mappedUserInfo
      
      // 立即更新编辑表单，确保数据同步
      updateEditFormFromUserInfo()
    } else {
      throw new Error('无效的响应格式')
    }
  } catch (err) {
    console.error('获取用户信息失败:', err)
    error.value = '获取用户信息失败，请稍后重试'
    
    // 显示错误通知
    ElNotification.error({
      title: '获取失败',
      message: err.message || '网络错误或认证失效',
      duration: 5000
    })
    
    // 如果是认证错误，跳转到登录页
    if (err.response && err.response.status === 401) {
      setTimeout(() => {
        router.push('/login')
      }, 1500)
    }
  } finally {
    loading.value = false
  }
}

// 辅助函数：从用户信息更新编辑表单数据
const updateEditFormFromUserInfo = () => {
  editProfileForm.username = userInfo.value.username || ''
  editProfileForm.nickname = userInfo.value.nickname || ''
  editProfileForm.email = userInfo.value.email || ''
}

// 组件挂载时获取用户信息
onMounted(() => {
  fetchUserInfo()
})

// 方法：跳转到收藏课程页面
const goToFavorites = () => {
  router.push('/favorites')
}
</script>

<style scoped>
.profile-container {
  max-width: 1280px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.profile-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  text-align: center;
  color: var(--text-color-primary);
}

.profile-content {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 20px;
  margin-bottom: 20px;
  align-items: start;
}

.profile-info {
  background: #f5f7fa;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.profile-header {
  margin-bottom: 20px;
}

.profile-avatar {
  margin: 0 auto 15px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.user-info-grid {
  display: flex;
  flex-direction: column;
  gap: 15px;
  width: 100%;
  margin-top: 20px;
}

.info-item {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  width: 100%;
  text-align: left;
}

.info-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}

.info-label {
  font-size: 14px;
  color: var(--text-color-secondary);
  margin-bottom: 8px;
  font-weight: 500;
  display: block;
  line-height: 1.2;
}

.info-value {
  font-size: 16px;
  color: var(--text-color-primary);
  font-weight: bold;
  display: block;
  word-break: break-word;
  line-height: 1.5;
}

.profile-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
  justify-content: center;
  width: 100%;
}

.profile-stats {
  background: #f5f7fa;
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
  color: var(--text-color-primary);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.stat-item {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  color: #667eea;
}

.stat-item:hover .stat-value {
  color: #764ba2;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #667eea;
  margin-top: 10px;
}

.profile-courses {
  background: #f5f7fa;
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.recent-courses {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
}

.recent-course-card {
  height: 100%;
}

.course-item {
  display: flex;
  gap: 15px;
}

.course-image-placeholder {
  width: 100px;
  height: 80px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 8px;
}

.course-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.course-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  color: var(--text-color-primary);
}

.course-progress {
  margin-bottom: 10px;
}

.course-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: var(--text-color-secondary);
}

.course-status {
  color: #67c23a;
  font-weight: bold;
}

/* 课程管理区域样式 */
.profile-courses-management {
  background: #f5f7fa;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  margin-top: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.my-courses {
  background: #fff;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow-x: auto;
}

.el-table {
  border-collapse: collapse;
}

.el-table__cell {
  padding: 12px 8px;
  font-size: 14px;
  line-height: 1.5;
}

.el-table__column--description .el-table__cell {
  max-width: 300px;
  white-space: normal;
  word-break: break-word;
  line-height: 1.6;
}

.el-table__column--title .el-table__cell {
  font-weight: 500;
  color: var(--text-color-primary);
}

.el-table__header-wrapper th {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e9f0 100%);
  font-weight: 600;
  color: var(--text-color-primary);
}

/* 图片上传组件样式 */
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 8px;
  object-fit: cover;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
  background-color: #f0f2f5;
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 表单样式 */
.el-form-item__label {
  font-weight: 500;
  color: var(--text-color-primary);
}

.el-dialog__header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  border-radius: 8px 8px 0 0;
}

.el-dialog__title {
  color: #fff;
}

/* 按钮样式 */
.el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.el-button--primary:hover {
  background: linear-gradient(135deg, #5a6fd8 0%, #6a408a 100%);
}

/* 骨架屏样式 */
.skeleton-line {
  height: 16px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: loading 1.5s infinite;
  border-radius: 4px;
}

@keyframes loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}

/* 间距样式 */
.mt-4 {
  margin-top: 16px;
}
</style>
