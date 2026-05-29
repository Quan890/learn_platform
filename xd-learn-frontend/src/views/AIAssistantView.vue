 <template>
  <div class="ai-assistant">
    <!-- 导航栏 -->
    <header class="header">
      <div class="container">
        <div class="logo">
          <h1>XD-Learn</h1>
        </div>
        <nav class="nav">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/courses" class="nav-item">课程</router-link>
          <router-link to="/ai-assistant" class="nav-item active">智能助手</router-link>
          <router-link to="/personal" class="nav-item">个人中心</router-link>
        </nav>
        <div class="user-info">
          <el-dropdown>
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

    <!-- 主要内容 -->
    <div class="container">
      <div class="page-title">
        <h2>智能学习助手</h2>
      </div>

      <el-card class="assistant-card">
        <div class="chat-container">
          <!-- 聊天消息区域 -->
          <div class="chat-messages" ref="chatMessages">
            <!-- 欢迎消息 -->
            <div class="welcome-message">
              <el-avatar :size="60" :src="aiAvatar"></el-avatar>
              <h3>欢迎使用智能学习助手</h3>
              <p>我是您的AI学习伙伴，可以帮助您解答学习相关的问题</p>
              <div class="quick-questions">
                <el-button size="small" type="primary" plain @click="sendQuickQuestion('如何学习JavaScript？')">
                  如何学习JavaScript？
                </el-button>
                <el-button size="small" type="primary" plain @click="sendQuickQuestion('推荐几本编程书籍')">
                  推荐几本编程书籍
                </el-button>
                <el-button size="small" type="primary" plain @click="sendQuickQuestion('什么是人工智能？')">
                  什么是人工智能？
                </el-button>
              </div>
            </div>

            <!-- 对话消息 -->
            <div 
              v-for="message in messages" 
              :key="message.id" 
              :class="['message-item', message.sender]"
            >
              <!-- 用户消息 -->
              <div v-if="message.sender === 'user'" class="message user-message">
                <div class="message-avatar">
                  <el-avatar :size="40" :src="userAvatar"></el-avatar>
                </div>
                <div class="message-content">
                  <div class="message-bubble user-bubble">
                    <div v-html="message.content"></div>
                  </div>
                  <div class="message-time">{{ message.time }}</div>
                </div>
              </div>

              <!-- AI消息 -->
              <div v-else class="message ai-message">
                <div class="message-avatar">
                  <el-avatar :size="40" :src="aiAvatar"></el-avatar>
                </div>
                <div class="message-content">
                  <div class="message-bubble ai-bubble">
                    <!-- 打字指示器 -->
                    <div v-if="message.isTyping" class="typing-indicator">
                      <span></span>
                      <span></span>
                      <span></span>
                    </div>
                    <!-- AI回复内容 -->
                    <div v-else class="ai-reply" v-html="message.content"></div>
                  </div>
                  <div class="message-time">{{ message.time }}</div>
                </div>
              </div>
            </div>
          </div>

          <!-- 输入区域 -->
          <div class="chat-input">
            <el-input 
              v-model="inputMessage" 
              placeholder="请输入您的问题..." 
              @keyup.enter="sendMessage"
              :disabled="sending"
              resize="none"
              :rows="2"
              type="textarea"
            >
              <template #prepend>
                <el-button 
                  type="default" 
                  @click="clearChat" 
                  :disabled="messages.length === 0 || sending"
                  title="清空对话"
                >
                  <el-icon><delete /></el-icon>
                </el-button>
              </template>
              <template #append>
                <el-button 
                  :loading="sending" 
                  @click="sendMessage" 
                  type="primary"
                  :disabled="!inputMessage.trim()"
                >
                  <el-icon><send /></el-icon> 发送
                </el-button>
              </template>
            </el-input>
            <div class="input-tip">
              <span>支持Markdown格式，使用Ctrl+Enter换行</span>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="container">
        <p>&copy; 2024 XD-Learn 学习平台. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script>
import { ArrowDown, Delete, send } from '@element-plus/icons-vue'
import { askAI } from '@/api/ai'

export default {
  name: 'AIAssistantView',
  components: {
    ArrowDown,
    Delete,
    Send
  },
  data() {
    return {
      // 用户信息
      user: {},
      
      // 聊天状态
      messages: [],
      inputMessage: '',
      sending: false,
      context: [], // 对话上下文
      
      // 头像设置
      userAvatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      aiAvatar: 'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png'
    }
  },
  computed: {
    isAuthenticated() {
      return this.$store.getters.isAuthenticated
    }
  },
  created() {
    this.user = this.$store.state.user
    if (this.user.avatar) {
      this.userAvatar = this.user.avatar
    }
  },
  updated() {
    this.scrollToBottom()
  },
  methods: {
    // 导航栏方法
    handleLogout() {
      this.$store.dispatch('logout')
      this.$router.push('/')
    },

    // 发送消息
    sendMessage() {
      if (!this.inputMessage.trim() || this.sending) return

      // 获取当前时间
      const currentTime = this.formatTime(new Date())

      // 添加用户消息
      const userMessage = {
        id: Date.now(),
        sender: 'user',
        content: this.inputMessage.trim(),
        time: currentTime,
        isTyping: false
      }
      this.messages.push(userMessage)

      // 更新上下文
      this.context.push({
        role: 'user',
        content: this.inputMessage.trim()
      })

      // 清空输入框
      const question = this.inputMessage
      this.inputMessage = ''
      this.sending = true

      // 添加AI回复占位符
      const aiMessage = {
        id: Date.now() + 1,
        sender: 'ai',
        content: '',
        time: '',
        isTyping: true
      }
      this.messages.push(aiMessage)

      // 调用AI API
      this.callAIAPI(question)
    },

    // 发送快速问题
    sendQuickQuestion(question) {
      if (this.sending) return
      this.inputMessage = question
      this.sendMessage()
    },

    // 调用AI API
    callAIAPI(question) {
      askAI({
        question: question,
        context: this.context
      })
        .then(response => {
          const aiMessage = this.messages[this.messages.length - 1]
          aiMessage.isTyping = false
          aiMessage.time = this.formatTime(new Date())
          
          // 更新上下文
          this.context.push({
            role: 'assistant',
            content: response.data.content
          })

          // 限制上下文长度（最多保留最近10轮对话）
          if (this.context.length > 20) {
            this.context = this.context.slice(-20)
          }

          // 打字机效果显示回答
          this.typeWriter(aiMessage, response.data.content)
        })
        .catch(error => {
          console.error('调用AI API失败:', error)
          const aiMessage = this.messages[this.messages.length - 1]
          aiMessage.isTyping = false
          aiMessage.time = this.formatTime(new Date())
          
          // 检查是否为未登录错误
          const isUnauthorized = error.response?.status === 401 || 
                                error.response?.data?.message?.includes('未登录') ||
                                error.response?.data?.message?.includes('login')
          
          if (isUnauthorized) {
            aiMessage.content = '请先登录后再提问。'
            this.$message.error('请先登录')
          } else {
            aiMessage.content = '抱歉，我暂时无法回答您的问题。请稍后重试或检查网络连接。'
            this.$message.error('AI回复失败，请稍后重试')
          }
          
          this.sending = false
        })
    },

    // 打字机效果
    typeWriter(message, text) {
      let i = 0
      const speed = 30 // 打字速度（毫秒）
      
      const type = () => {
        if (i < text.length) {
          message.content += text.charAt(i)
          i++
          setTimeout(type, speed)
        } else {
          this.sending = false
        }
      }
      
      type()
    },

    // 清空聊天
    clearChat() {
      this.$confirm('确定要清空当前对话吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      .then(() => {
        this.messages = []
        this.context = []
        this.$message.success('对话已清空')
      })
      .catch(() => {
        // 取消清空
      })
    },

    // 滚动到底部
    scrollToBottom() {
      this.$nextTick(() => {
        const chatMessages = this.$refs.chatMessages
        if (chatMessages) {
          chatMessages.scrollTop = chatMessages.scrollHeight
        }
      })
    },

    // 格式化时间
    formatTime(date) {
      const hours = date.getHours().toString().padStart(2, '0')
      const minutes = date.getMinutes().toString().padStart(2, '0')
      return `${hours}:${minutes}`
    },

    // 模拟API调用（用于演示）
    simulateAIAPI(question) {
      return new Promise((resolve) => {
        // 模拟网络延迟
        setTimeout(() => {
          // 模拟AI回答
          let response = ''
          if (question.includes('JavaScript')) {
            response = 'JavaScript是一种广泛使用的脚本语言，主要用于网页交互。它可以在浏览器中直接运行，为网页添加动态效果、处理用户输入和与服务器通信。学习JavaScript可以从基础语法开始，然后学习DOM操作、事件处理、异步编程等进阶内容。'
          } else if (question.includes('书籍')) {
            response = '以下是几本推荐的编程书籍：<br>1. 《JavaScript高级程序设计》 - Nicholas C. Zakas<br>2. 《深入理解计算机系统》 - Randal E. Bryant<br>3. 《算法导论》 - Thomas H. Cormen<br>4. 《代码大全》 - Steve McConnell<br>5. 《设计模式》 - Erich Gamma'
          } else if (question.includes('人工智能')) {
            response = '人工智能（AI）是计算机科学的一个分支，旨在创建能够执行通常需要人类智能的任务的系统。这些任务包括学习、推理、解决问题、感知和语言理解。AI的主要领域包括机器学习、深度学习、自然语言处理、计算机视觉和机器人技术。'
          } else {
            response = `感谢您的提问！关于"${question}"，我会尽力为您解答。<br><br>我可以帮助您：<br>- 解释编程概念<br>- 提供学习建议<br>- 解答技术问题<br>- 推荐学习资源<br><br>请随时提出更具体的问题，我会为您提供详细的帮助。`
          }
          
          resolve({
            data: {
              content: response
            }
          })
        }, 1000)
      })
    }
  }
}
</script>

<style scoped>
.ai-assistant {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
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

.assistant-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: hidden;
}

/* 聊天容器 */
.chat-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 250px);
  min-height: 500px;
}

/* 聊天消息区域 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background-color: #fafafa;
}

.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 欢迎消息 */
.welcome-message {
  text-align: center;
  padding: 40px 20px;
  margin-bottom: 20px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.welcome-message h3 {
  margin: 15px 0 10px 0;
  color: #333;
}

.welcome-message p {
  color: #666;
  margin-bottom: 20px;
}

.quick-questions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
}

/* 消息项 */
.message-item {
  margin-bottom: 20px;
  display: flex;
  align-items: flex-start;
}

/* 用户消息 */
.user-message {
  justify-content: flex-end;
}

.user-message .message-content {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-right: 10px;
}

/* AI消息 */
.ai-message {
  justify-content: flex-start;
}

.ai-message .message-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-left: 10px;
}

/* 头像 */
.message-avatar {
  margin: 0 10px;
  flex-shrink: 0;
}

/* 消息内容 */
.message-bubble {
  max-width: 70%;
  padding: 12px 16px;
  border-radius: 18px;
  word-wrap: break-word;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.user-bubble {
  background-color: #409eff;
  color: white;
  border-bottom-right-radius: 6px;
}

.ai-bubble {
  background-color: white;
  color: #333;
  border-bottom-left-radius: 6px;
}

/* 消息时间 */
.message-time {
  font-size: 12px;
  color: #909399;
  margin: 5px 10px;
}

/* 打字指示器 */
.typing-indicator {
  display: flex;
  align-items: center;
  padding: 10px 0;
}

.typing-indicator span {
  height: 8px;
  width: 8px;
  margin: 0 2px;
  background-color: #909399;
  border-radius: 50%;
  display: block;
  opacity: 0.4;
}

.typing-indicator span:nth-of-type(1) {
  animation: typing 1s infinite;
}

.typing-indicator span:nth-of-type(2) {
  animation: typing 1s infinite 0.2s;
}

.typing-indicator span:nth-of-type(3) {
  animation: typing 1s infinite 0.4s;
}

@keyframes typing {
  0% {
    opacity: 0.4;
    transform: translateY(0);
  }
  50% {
    opacity: 1;
    transform: translateY(-10px);
  }
  100% {
    opacity: 0.4;
    transform: translateY(0);
  }
}

/* AI回复内容 */
.ai-reply {
  line-height: 1.6;
}

.ai-reply p {
  margin: 8px 0;
}

.ai-reply ul, .ai-reply ol {
  padding-left: 20px;
  margin: 8px 0;
}

.ai-reply li {
  margin: 4px 0;
}

/* 输入区域 */
.chat-input {
  padding: 20px;
  background-color: white;
  border-top: 1px solid #e4e7ed;
}

.input-tip {
  text-align: center;
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-container {
    height: calc(100vh - 300px);
  }

  .message-bubble {
    max-width: 85%;
  }

  .quick-questions {
    flex-direction: column;
    align-items: center;
  }

  .quick-questions .el-button {
    width: 200px;
  }
}
</style>