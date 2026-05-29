import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  // 基础路径配置
  base: '/',
  
  // 插件配置
  plugins: [vue()],
  
  // 解析配置
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    },
    extensions: ['.vue', '.js', '.json', '.ts']
  },
  
  // 开发服务器配置
  server: {
    port: 3000,
    host: '127.0.0.1',
    open: true,
    // 修复SPA路由问题
    historyApiFallback: true,
    // 增加代理配置（处理API请求）
    proxy: {
      // 处理 /api 开头的请求，保留 /api 前缀
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // 不重写路径，保留 /api 前缀
        rewrite: (path) => path
      }
    }
  },
  
  // 构建配置
  build: {
    // 生成生产环境的构建文件
    outDir: 'dist',
    // 静态资源存放目录
    assetsDir: 'assets',
    // 启用CSS代码分割
    cssCodeSplit: true,
    // 生成sourcemap
    sourcemap: false,
    // 最小化配置
    minify: 'esbuild',
    // 产物命名规则
    rollupOptions: {
      output: {
        entryFileNames: 'assets/[name]-[hash].js',
        chunkFileNames: 'assets/[name]-[hash].js',
        assetFileNames: 'assets/[name]-[hash].[ext]'
      }
    }
  },
  
  // 依赖优化配置
  optimizeDeps: {
    include: ['vue', 'vue-router', 'vuex', 'element-plus'],
    exclude: []
  },
  
  // CSS配置
  css: {
    // 启用CSS模块化
    modules: {
      localsConvention: 'camelCase'
    },
    // 全局CSS变量
    preprocessorOptions: {
      scss: {
        additionalData: '@import "@/assets/styles/variables.scss";'
      }
    }
  }
})