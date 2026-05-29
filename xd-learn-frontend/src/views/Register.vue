<template>
  <div class="register-container">
    <div class="register-form-wrapper">
      <h2 class="register-title">注册</h2>
      
      <el-form
        :model="registerForm"
        :rules="registerRules"
        ref="registerFormRef"
        label-position="top"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名" prefix-icon="User"></el-input>
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱" prefix-icon="Message"></el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item>
          <div class="register-actions">
            <el-button type="primary" @click="handleRegister" :icon="CircleCheck">注册</el-button>
            <el-button type="success" @click="handleLogin" :icon="User">登录</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { CircleCheck, User, Message, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const registerFormRef = ref()
const registerForm = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur' 
    }
  ]
}

import { ElLoading, ElMessage } from 'element-plus'

const handleRegister = async () => {
  try {
    // 表单验证
    await registerFormRef.value.validate()
    
    // 显示加载状态
    const loadingInstance = ElLoading.service({
      lock: true,
      text: '注册中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    // 模拟API延迟
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 关闭加载状态
    loadingInstance.close()
    
    // 注册成功
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } catch (error) {
    // 表单验证失败或其他错误
    console.error('注册失败:', error)
    if (error.message) {
      ElMessage.error(error.message)
    }
  }
}

const handleLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin-top: -60px; /* 消除与导航栏的间隙 */
  padding-top: 60px; /* 确保内容不被导航栏遮挡 */
}

.register-form-wrapper {
  background: #fff;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 400px;
}

.register-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
  color: var(--text-color-primary);
}

.register-actions {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 25px;
  width: 100%;
}

.register-actions .el-button {
  width: 100%;
  margin: 0;
  padding: 12px 20px;
  font-size: 16px;
  font-weight: 500;
}

.register-actions .el-button:first-of-type {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}
</style>
