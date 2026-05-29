<template>
  <div class="app-container">
    <!-- 导航栏 -->
    <nav-bar />
    
    <!-- 主内容区域 -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import NavBar from './components/NavBar.vue'
</script>

<style>
/* 全局样式重置与基础设置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 行业顶级UI设计 - 全局样式变量 */
:root {
  /* 主色调 - 蓝色系 */
  --primary-color: #4f46e5;
  --primary-light: #6366f1;
  --primary-dark: #4338ca;
  --primary-hover: #4c1d95;
  
  /* 辅助色调 - 渐变色彩 */
  --accent-color-1: #ec4899;
  --accent-color-2: #8b5cf6;
  --accent-color-3: #06b6d4;
  
  /* 功能色彩 */
  --success-color: #10b981;
  --warning-color: #f59e0b;
  --danger-color: #ef4444;
  --info-color: #6b7280;
  
  /* 文本色彩 - 层级分明 */
  --text-primary: #1f2937;
  --text-secondary: #4b5563;
  --text-tertiary: #9ca3af;
  --text-placeholder: #d1d5db;
  
  /* 背景色彩 - 现代渐变 */
  --background-color: #f9fafb;
  --background-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  --card-background: #ffffff;
  --card-background-hover: #f9fafb;
  
  /* 边框色彩 */
  --border-color: #e5e7eb;
  --border-light: #f3f4f6;
  --border-dark: #d1d5db;
  
  /* 阴影效果 - 多层次 */
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  --shadow-hover: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
  
  /* 过渡动画 */
  --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  --transition-fast: all 0.15s cubic-bezier(0.4, 0, 0.2, 1);
  
  /* 间距系统 */
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;
  --spacing-2xl: 40px;
  
  /* 圆角系统 */
  --radius-sm: 6px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --radius-xl: 16px;
  --radius-full: 9999px;
}

/* 基础样式设置 */
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background-color: var(--background-color);
  color: var(--text-primary);
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  padding: var(--spacing-lg) 0;
}

/* 滚动条样式优化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: var(--border-light);
  border-radius: var(--radius-full);
}

::-webkit-scrollbar-thumb {
  background: var(--border-dark);
  border-radius: var(--radius-full);
  transition: var(--transition);
}

::-webkit-scrollbar-thumb:hover {
  background: var(--text-tertiary);
}
</style>