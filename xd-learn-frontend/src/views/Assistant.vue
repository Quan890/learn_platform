<template>
  <div class="assistant-container">
    <div class="assistant-header">
      <h2 class="assistant-title">智能助手</h2>
      <p class="assistant-subtitle">有任何学习问题，随时问我</p>
      <div class="header-actions">
        <el-button 
          type="text" 
          @click="clearChat"
          size="small"
          :disabled="messages.length === 0"
        >
          <el-icon><Delete /></el-icon>
          清空对话
        </el-button>
      </div>
    </div>
    
    <div class="assistant-content">
      <div class="chat-container">
          <div class="chat-messages" ref="chatMessagesRef">
            <div 
              v-for="message in messages" 
              :key="message.id"
              class="message"
              :class="{
                'system-message': message.sender === 'system',
                'assistant-message': message.sender === 'assistant',
                'user-message': message.sender === 'user'
              }"
            >
            <div class="message-avatar">
              <el-avatar 
                :size="40" 
                :class="message.sender === 'user' ? 'user-avatar' : 'assistant-avatar'"
              >
                {{ message.sender === 'user' ? 'U' : 'AI' }}
              </el-avatar>
            </div>
            <div class="message-content">
              <div 
                class="message-text"
                v-if="message.sender === 'assistant'"
              >
                <div v-if="message.content" v-html="renderMarkdown(message.content)"></div>
                <div v-else-if="isTyping" class="typing-indicator">
                  <span class="typing-dot"></span>
                  <span class="typing-dot"></span>
                  <span class="typing-dot"></span>
                </div>
              </div>
              <div 
                class="message-text"
                v-else
              >{{ message.content }}</div>
              <div class="message-time">{{ message.time }}</div>
            </div>
          </div>
        </div>
        
        <div class="chat-input-area">
          <el-input
            v-model="inputMessage"
            placeholder="请输入您的问题..."
            type="textarea"
            :rows="3"
            resize="none"
            @keyup.enter="sendMessage"
            class="chat-textarea"
            :disabled="isTyping"
          />
          <div class="input-actions">
            <div class="input-stats" v-if="messages.length > 0">
              <span>{{ messages.length }} 条消息</span>
            </div>
            <div class="send-button-container">
              <el-button 
                type="primary" 
                @click="sendMessage" 
                :disabled="!inputMessage.trim() || isTyping"
                size="large"
                class="send-button"
              >
                发送
              </el-button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 错误提示 -->
      <div v-if="error" class="error-message">
        <el-alert
          :title="error"
          type="error"
          show-icon
          closable
          @close="error = ''"
        ></el-alert>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, watch, onMounted, onBeforeUnmount, computed } from 'vue'
import { Delete } from '@element-plus/icons-vue'
import { assistantApi } from '../api/index'
import { ElMessage } from 'element-plus'
import { marked } from 'marked'

// 状态管理
const inputMessage = ref('')
const messages = ref([])
const messageId = ref(1)
const chatMessagesRef = ref(null)
const isTyping = ref(false)
const error = ref('')
let streamController = null

// 获取当前时间
const getCurrentTime = () => {
  const now = new Date()
  return now.toLocaleTimeString('zh-CN', { 
    year: 'numeric', 
    month: '2-digit', 
    day: '2-digit',
    hour12: false 
  })
}

// 滚动到底部函数
const scrollToBottom = () => {
  nextTick(() => {
    if (chatMessagesRef.value) {
      // 平滑滚动到底部
      chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
    }
  })
}

// 监听messages数组变化，自动滚动到底部
watch(
  () => messages.value.length,
  (newLength, oldLength) => {
    if (newLength > oldLength) {
      scrollToBottom()
    }
  }
)

// 监听最后一条消息的内容变化，确保AI回复完成后isTyping状态正确重置
watch(
  () => {
    // 获取最后一条AI消息的内容
    const lastAiMessage = messages.value.filter(msg => msg.sender === 'assistant').pop()
    return lastAiMessage?.content || ''
  },
  (newContent, oldContent) => {
    // 当AI消息内容发生变化且非空时，确保isTyping被重置为false
    if (newContent && newContent !== oldContent) {
      // 使用nextTick确保DOM更新完成后再重置状态
      nextTick(() => {
        isTyping.value = false
      })
    }
  }
)

// 处理SSE流式响应
const handleStreamResponse = async (response, aiMessageId) => {
  let reader = null
  try {
    reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let buffer = ''
    
    // 更新AI消息的时间
    const aiMessage = messages.value.find(msg => msg.id === aiMessageId)
    if (aiMessage) {
      aiMessage.time = getCurrentTime()
    }
    
    while (true) {
      const { done, value } = await reader.read()
      
      // 解码接收到的字节
      if (value) {
        buffer += decoder.decode(value, { stream: true })
      }
      
      // 处理SSE事件
      let eventEndIndex
      while ((eventEndIndex = buffer.indexOf('\n\n')) !== -1) {
        const eventData = buffer.slice(0, eventEndIndex)
        buffer = buffer.slice(eventEndIndex + 2)
        
        // 解析SSE事件
        if (eventData.startsWith('data:')) {
          const chunk = eventData.slice(5).trim()
          if (chunk) {
            // 更新AI消息内容
            const aiMessage = messages.value.find(msg => msg.id === aiMessageId)
            if (aiMessage) {
              // 检查是否是错误消息
              if (chunk.startsWith('Error:')) {
                aiMessage.content = chunk
                scrollToBottom()
                // 收到错误消息后，立即跳出循环
                break
              }
              
              // 处理正常消息
              aiMessage.content += chunk
              // 滚动到底部，确保用户能看到最新内容
              scrollToBottom()
            }
          }
        }
      }
      
      if (done) break
    }
    
    // 处理缓冲区中剩余的事件
    if (buffer.trim()) {
      const aiMessage = messages.value.find(msg => msg.id === aiMessageId)
      if (aiMessage) {
        // 检查是否是错误消息
        if (buffer.startsWith('data: Error:')) {
          const errorMsg = buffer.slice(11).trim()
          aiMessage.content = errorMsg
        } else if (buffer.startsWith('data:')) {
          const remainingChunk = buffer.slice(5).trim()
          if (remainingChunk) {
            aiMessage.content += remainingChunk
          }
        }
        scrollToBottom()
      }
    }
  } catch (err) {
    console.error('SSE流式响应处理失败:', err)
    error.value = 'SSE流式响应处理失败，请稍后重试'
  } finally {
    // 无论成功还是失败，都确保释放资源并清除输入状态
    if (reader) {
      reader.releaseLock()
    }
    // 强制清除输入状态，确保打字指示器消失
    isTyping.value = false
  }
}

// 配置marked选项，参考行业优秀实践，提升AI回答的结构化程度和可读性
marked.setOptions({
  gfm: true,      // 启用GitHub风格Markdown，包括表格支持
  breaks: true,      // 换行符转换为<br>
  headerIds: false,  // 不生成header id
  mangle: false,     // 不混淆链接
  pedantic: false,   // 不使用严格的Markdown语法
  sanitize: false,   // 不清理HTML
  smartLists: true,  // 优化列表格式
  smartypants: false, // 不启用智能标点
  xhtml: false,      // 不使用XHTML语法
  highlight: function(code, lang) {
    // 代码高亮处理，保持代码原样
    return code;
  }
})

// 优化Markdown渲染，参考行业优秀实践，提升AI回答的结构化程度和可读性
const renderMarkdown = (content) => {
  if (!content) return ''
  
  // 1. 基础内容清理与标准化
  let processedContent = content
  
  // 移除各种多余前缀和特殊标记
  processedContent = processedContent
    // 移除行首的data:前缀
    .replace(/^\s*data:/gm, '')
    // 移除行首的sqldata:前缀
    .replace(/^\s*sqldata:/gm, '')
    // 移除行首的ata:前缀
    .replace(/^\s*ata:/gm, '')
    // 移除重复的等号分隔线
    .replace(/^\s*=+\s*$/gm, '')
    // 移除重复的减号分隔线
    .replace(/^\s*-+\s*$/gm, '')
    // 移除特殊标记如---##
    .replace(/^\s*---#+\s*/gm, '')
    // 移除特殊标记如---##
    .replace(/\s*---#+\s*/g, '\n\n')
    // 移除特殊标记如data:
    .replace(/\s*data:\s*/g, '')
    // 移除特殊标记如data:和:data
    .replace(/\s*:data\s*/g, '')
    // 移除特殊标记如：
    .replace(/\s*\*\*\*\s*/g, '\n\n')
    // 确保思考过程提示语句独占一行，处理任意数量的等号
    .replace(/(.*?)(=+思考过程=+)(.*?)/gs, '$1\n\n$2\n\n$3')
    // 确保完整回复提示语句独占一行，处理任意数量的等号
    .replace(/(.*?)(=+完整回复=+)(.*?)/gs, '$1\n\n$2\n\n$3')
    // 处理开头的思考过程标记
    .replace(/^(=+思考过程=+)(.*?)/s, '$1\n\n$2')
    // 处理开头的完整回复标记
    .replace(/^(=+完整回复=+)(.*?)/s, '$1\n\n$2')
    // 处理结尾的思考过程标记
    .replace(/(.*?)(=+思考过程=+)$/s, '$1\n\n$2')
    // 处理结尾的完整回复标记
    .replace(/(.*?)(=+完整回复=+)$/s, '$1\n\n$2')
  
  // 2. 清理多余的空格和换行
  processedContent = processedContent
    // 合并连续的换行符，最多保留2个
    .replace(/\n{3,}/g, '\n\n')
    // 移除行尾的多余空格
    .replace(/\s+$/gm, '')
    // 移除行首的多余空格
    .replace(/^\s+/gm, '')
    // 移除整个文本前后的空白
    .trim()
  
  // 3. 修复Markdown格式
  processedContent = processedContent
    // 确保标题格式正确（#后面必须有空格）
    .replace(/^(#{1,6})([^\s#])/gm, '$1 $2')
    // 确保列表项格式正确（-/*/+后面必须有空格）
    .replace(/^(\s*)([-*+])([^\s])/gm, '$1$2 $3')
  
  // 4. 优化代码块显示
  processedContent = processedContent
    // 确保代码块有正确的格式（前后空行）
    .replace(/(^|\n)(```\w*\n[\s\S]*?)```(\n|$)/g, '$1\n$2\n```\n$4')
    // 为没有指定语言的代码块添加默认语言
    .replace(/```\n([\s\S]*?)```/g, '```text\n$1\n```')
  
  // 5. 增强内容的逻辑性和连贯性
  processedContent = processedContent
    // 确保段落之间有正确的换行
    .replace(/\n([^\n#\*\-\d\s\|>```])/g, '\n\n$1')
    // 确保代码块前后有正确的换行
    .replace(/(```\w*\n[\s\S]*?```)([^\n])/g, '$1\n\n$2')
    .replace(/([^\n])(```\w*\n[\s\S]*?```)/g, '$1\n\n$2')
  
  // 6. 优化表格显示
  processedContent = processedContent
    // 为表格添加前后换行
    .replace(/(^|\n)(\|.*?\|)(\n|$)/g, '$1\n$2\n$3')
  
  // 7. 确保回答开头有适当的格式
  if (processedContent && !processedContent.startsWith('#') && !processedContent.startsWith('>') && 
      !processedContent.startsWith('-') && !processedContent.startsWith('*') && 
      !processedContent.startsWith('+') && !processedContent.startsWith('1.') && 
      !processedContent.startsWith('```')) {
    processedContent = '\n' + processedContent
  }
  
  // 8. 使用marked渲染Markdown
  return marked(processedContent)
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim()) return
  if (isTyping.value) return
  
  // 清空之前的错误
  error.value = ''
  
  // 添加用户消息
  const userMessage = {
    id: messageId.value++,
    content: inputMessage.value.trim(),
    sender: 'user',
    time: getCurrentTime()
  }
  messages.value.push(userMessage)
  
  // 清空输入框
  inputMessage.value = ''
  
  // 滚动到底部（用户消息发送后）
  scrollToBottom()
  
  // 设置AI正在回复的状态
  isTyping.value = true
  
  // 预创建AI消息，用于流式更新
  const aiMessageId = messageId.value++
  const aiMessage = {
    id: aiMessageId,
    content: '',
    sender: 'assistant',
    time: ''
  }
  messages.value.push(aiMessage)
  
  // 设置超时定时器，确保isTyping状态一定会被清除
  const timeoutId = setTimeout(() => {
    isTyping.value = false
    console.warn('SSE流处理超时，已自动清除输入状态')
    
    // 更新AI消息为超时状态
    const aiMessage = messages.value.find(msg => msg.id === aiMessageId)
    if (aiMessage && aiMessage.content === '') {
      aiMessage.content = '抱歉，请求超时，请稍后重试。'
      aiMessage.time = getCurrentTime()
    }
  }, 30000) // 30秒超时
  
  try {
    // 使用fetch API直接调用，支持SSE流式响应
    const response = await fetch('/api/assistant/ask/stream', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify(userMessage.content)
    })
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }
    
    if (!response.body) {
      throw new Error('Response body is not a readable stream')
    }
    
    // 更新AI消息的时间
    aiMessage.time = getCurrentTime()
    
    // 处理流式响应
    await handleStreamResponse(response, aiMessageId)
  } catch (err) {
    console.error('发送消息失败:', err)
    
    // 更新AI消息为错误状态
    const aiMessage = messages.value.find(msg => msg.id === aiMessageId)
    if (aiMessage) {
      aiMessage.content = '抱歉，我暂时无法回答您的问题，请稍后重试。'
      aiMessage.time = getCurrentTime()
    }
    
    error.value = '发送消息失败，请稍后重试'
  } finally {
    // 无论成功还是失败，都确保清除超时定时器和输入状态
    clearTimeout(timeoutId)
    isTyping.value = false
  }
}

// 清空对话
const clearChat = () => {
  messages.value = []
  messageId.value = 1
  inputMessage.value = ''
  error.value = ''
}

// 组件挂载时，添加系统欢迎消息
onMounted(() => {
  messages.value.push({
    id: messageId.value++,
    content: '您好！我是您的智能学习助手。有任何学习问题，随时问我。',
    sender: 'system',
    time: getCurrentTime()
  })
})

// 组件卸载前，清理资源
onBeforeUnmount(() => {
  if (streamController) {
    streamController.abort()
  }
})
</script>

<style scoped>
.assistant-container {
  max-width: 1067px; /* 增加1/3宽度 */
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: calc(800px * 4/5); /* 增加1/5高度 */
  position: relative;
}

.assistant-header {
  background: linear-gradient(135deg, #409eff 0%, #764ba2 100%);
  color: #fff;
  padding: 15px 20px;
  text-align: center;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  position: relative;
}

.header-actions {
  position: absolute;
  top: 15px;
  right: 20px;
}

.header-actions .el-button {
  color: rgba(255, 255, 255, 0.9);
  font-size: 13px;
  padding: 4px 10px;
  transition: all 0.2s ease;
}

.header-actions .el-button:hover {
  color: #fff;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

.header-actions .el-button:disabled {
  color: rgba(255, 255, 255, 0.5);
  background: transparent;
  cursor: not-allowed;
}

.assistant-title {
  font-size: 22px;
  font-weight: bold;
  margin: 0;
  color: #fff;
  line-height: 1.2;
}

.assistant-subtitle {
  font-size: 13px;
  opacity: 0.9;
  line-height: 1.2;
  margin: 0;
}

.assistant-content {
  padding: 15px;
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  gap: 12px;
}

.chat-container {
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
  border-radius: 12px;
  padding: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  flex: 1;
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding-right: 5px;
  scroll-behavior: smooth;
  min-height: 0;
}

/* 滚动条样式 - 保持可见但不突兀 */
.chat-messages::-webkit-scrollbar {
  width: 8px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.chat-input-area {
  background: #fff;
  border-radius: 12px;
  padding: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex-shrink: 0;
  border: 1px solid #e9ecef;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.input-stats {
  font-size: 12px;
  color: #909399;
}

.send-button-container {
  display: flex;
  justify-content: flex-end;
}

.send-button {
  min-width: 100px;
  font-size: 14px;
  background: linear-gradient(135deg, #409eff 0%, #764ba2 100%);
  border: none;
  transition: all 0.2s ease;
}

.send-button:hover {
  background: linear-gradient(135deg, #3789e0 0%, #673ab7 100%);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
  transform: translateY(-1px);
}

.send-button:disabled {
  background: #c0c4cc;
  box-shadow: none;
  transform: none;
  cursor: not-allowed;
}

.assistant-avatar {
  background: linear-gradient(135deg, #409eff 0%, #764ba2 100%);
  color: #fff;
  font-weight: bold;
}

.user-avatar {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: #fff;
  font-weight: bold;
}

.message {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  flex-direction: row;
  animation: messageSlideIn 0.3s ease-out;
}

@keyframes messageSlideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-message {
  flex-direction: row-reverse;
}

.user-message .message-avatar {
  order: 2;
}

.user-message .message-content {
  order: 1;
  align-items: flex-end;
}

.message-content {
  max-width: 80%;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-text {
  background: #fff;
  padding: 12px 16px;
  border-radius: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.08);
  line-height: 1.6;
  word-break: break-word;
  font-size: 14px;
  position: relative;
  transition: all 0.2s ease;
}

.message-text:hover {
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.12);
}

.system-message .message-text {
  background: linear-gradient(135deg, #e6a23c 0%, #f1c40f 100%);
  color: #fff;
  text-align: center;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 500;
}

.assistant-message .message-text {
  background: #f0f9ff;
  border: 1px solid #e0f2fe;
  border-radius: 12px 12px 12px 4px;
  padding-left: 40px;
}

.user-message .message-text {
  background: #ecfdf5;
  border: 1px solid #d1fae5;
  color: #065f46;
  border-radius: 12px 12px 4px 12px;
}

.message-time {
  font-size: 11px;
  color: #909399;
  align-self: flex-start;
  margin-left: 4px;
}

.user-message .message-time {
  align-self: flex-end;
  margin-right: 4px;
  margin-left: 0;
}

.chat-textarea {
  resize: none;
  font-size: 14px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  transition: all 0.2s ease;
  min-height: 80px;
}

.chat-textarea:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.chat-textarea:disabled {
  background-color: #f5f7fa;
  cursor: not-allowed;
}

/* 加载状态指示 */
.typing-indicator {
  display: flex;
  gap: 6px;
  align-items: center;
  padding: 12px 16px;
  background: #f0f9ff;
  border: 1px solid #e0f2fe;
  border-radius: 12px 12px 12px 4px;
  min-height: 20px;
}

.typing-dot {
  width: 8px;
  height: 8px;
  background: #409eff;
  border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out;
}

.typing-dot:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.5;
  }
  30% {
    transform: translateY(-10px);
    opacity: 1;
  }
}

/* 错误提示 */
.error-message {
  margin-top: auto;
  flex-shrink: 0;
}

.error-message .el-alert {
  margin: 0;
  border-radius: 8px;
}

/* Markdown 样式 */
.message-text h1,
.message-text h2,
.message-text h3,
.message-text h4,
.message-text h5,
.message-text h6 {
  margin: 12px 0 8px 0;
  font-weight: 600;
  line-height: 1.4;
  color: #303133;
}

.message-text h1 {
  font-size: 20px;
  border-bottom: 1px solid #e8e8e8;
  padding-bottom: 6px;
}

.message-text h2 {
  font-size: 18px;
  border-bottom: 1px solid #e8e8e8;
  padding-bottom: 4px;
}

.message-text h3 {
  font-size: 16px;
}

.message-text h4,
.message-text h5,
.message-text h6 {
  font-size: 14px;
}

.message-text p {
  margin: 8px 0;
  line-height: 1.6;
  color: #606266;
}

.message-text ul,
.message-text ol {
  margin: 8px 0;
  padding-left: 24px;
  line-height: 1.6;
  color: #606266;
}

.message-text li {
  margin: 4px 0;
}

.message-text li ul,
.message-text li ol {
  margin: 4px 0;
}

.message-text code {
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 2px 6px;
  font-size: 13px;
  font-family: 'Courier New', Courier, monospace;
  color: #e74c3c;
  white-space: nowrap;
}

.message-text pre {
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 12px;
  margin: 8px 0;
  overflow-x: auto;
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  line-height: 1.5;
}

.message-text pre code {
  background: transparent;
  border: none;
  padding: 0;
  color: #303133;
  white-space: pre;
}

.message-text blockquote {
  border-left: 4px solid #409eff;
  margin: 8px 0;
  padding: 8px 12px;
  background-color: #ecf5ff;
  color: #606266;
  font-style: italic;
}

.message-text strong {
  font-weight: 600;
  color: #303133;
}

.message-text em {
  font-style: italic;
}

.message-text hr {
  border: none;
  border-top: 1px solid #e8e8e8;
  margin: 16px 0;
}

.message-text table {
  border-collapse: collapse;
  width: 100%;
  margin: 8px 0;
}

.message-text th,
.message-text td {
  border: 1px solid #e8e8e8;
  padding: 8px 12px;
  text-align: left;
  font-size: 13px;
  line-height: 1.5;
}

.message-text th {
  background-color: #f5f7fa;
  font-weight: 600;
  color: #303133;
}

.message-text td {
  color: #606266;
}

.message-text a {
  color: #409eff;
  text-decoration: none;
  transition: color 0.2s ease;
}

.message-text a:hover {
  color: #66b1ff;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .assistant-content {
    padding: 10px;
  }
  
  .chat-container {
    padding: 10px;
  }
  
  .message-content {
    max-width: 85%;
  }
  
  .chat-input-area {
    padding: 10px;
  }
  
  .assistant-header {
    padding: 15px;
  }
  
  .header-actions {
    position: relative;
    top: auto;
    right: auto;
    margin-top: 10px;
  }
  
  .assistant-title {
    font-size: 20px;
  }
}
</style>