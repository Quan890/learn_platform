<template>
  <div class="login-container">
    <div class="login-form-wrapper">
      <h2 class="login-title">登录</h2>
      
      <el-form
        :model="loginForm"
        :rules="loginRules"
        ref="loginFormRef"
        label-position="top"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User"></el-input>
        </el-form-item>
        
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
          ></el-input>
        </el-form-item>
        
        <el-form-item>
          <div class="login-actions">
            <el-button type="primary" @click="handleLogin" :icon="CircleCheck">登录</el-button>
            <el-button type="success" @click="handleRegister" :icon="Plus">注册</el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { CircleCheck, Plus, User, Lock } from '@element-plus/icons-vue'
import { ElLoading, ElMessage } from 'element-plus'
import { authApi } from '../api/index'
import { setToken, setRefreshToken } from '../utils/auth'

const router = useRouter()
const loginFormRef = ref()
const loginForm = ref({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  try {
    // 表单验证
    await loginFormRef.value.validate()
    
    // 显示加载状态
    const loadingInstance = ElLoading.service({
      lock: true,
      text: '登录中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    
    try {
      // 调用后端登录接口
      const response = await authApi.login({
        username: loginForm.value.username,
        password: loginForm.value.password
      })
      
      // 关闭加载状态
      loadingInstance.close()
      
      // 登录成功，存储JWT令牌和refresh token
      if (response.data && response.data.accessToken) {
        // 存储token和refresh token
        setToken(response.data.accessToken)
        if (response.data.refreshToken) {
          setRefreshToken(response.data.refreshToken)
        }
        
        ElMessage.success(response.message || '登录成功')
        
        // 跳转到原页面或首页
        const redirectPath = router.currentRoute.value.query.redirect || '/'
        // 添加查询参数以触发登录状态更新
        router.replace({
          path: redirectPath,
          query: { ...router.currentRoute.value.query, loginSuccess: 'true' }
        })
      } else {
        // 响应格式不正确
        ElMessage.error('登录失败，服务器响应格式错误')
      }
    } catch (error) {
      // 网络错误或业务错误
      loadingInstance.close()
      console.error('登录请求失败:', error)
      
      // 根据错误类型显示不同的错误信息
      if (error.response) {
        // 服务器返回了错误状态码
        const status = error.response.status
        if (status === 401) {
          ElMessage.error('用户名或密码错误')
        } else if (status === 500) {
          ElMessage.error('服务器内部错误，请稍后重试')
        } else {
          ElMessage.error(`登录失败，错误码: ${status}`)
        }
      } else if (error.request) {
        // 请求已发送但没有收到响应
        ElMessage.error('网络错误，无法连接到服务器')
      } else {
        // 请求配置错误
        ElMessage.error(error.message || '登录失败，请稍后重试')
      }
    }
  } catch (error) {
    // 表单验证失败
    console.error('表单验证失败:', error)
  }
}

const handleRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  margin-top: -60px; /* 消除与导航栏的间隙 */
  padding-top: 60px; /* 确保内容不被导航栏遮挡 */
}

.login-form-wrapper {
  background: #fff;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  width: 100%;
  max-width: 400px;
}

.login-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 30px;
  text-align: center;
  color: var(--text-color-primary);
}

.login-actions {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 25px;
  width: 100%;
}

.login-actions .el-button {
  width: 100%;
  margin: 0;
  padding: 12px 20px;
  font-size: 16px;
  font-weight: 500;
}

.login-actions .el-button:first-of-type {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}
</style>
