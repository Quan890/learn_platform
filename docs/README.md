# 部署说明

> 小滴智能学习平台（XD Learn Platform）部署指南

---

## 目录

- [环境要求](#环境要求)
- [项目结构](#项目结构)
- [后端部署](#后端部署)
- [前端部署](#前端部署)
- [生产环境部署](#生产环境部署)
- [常见问题](#常见问题)

---

## 环境要求

| 组件 | 版本要求 | 说明 |
|------|---------|------|
| JDK | 21+ | Spring Boot 4.0 要求最低 JDK 21 |
| Maven | 3.9+ | 用于后端构建 |
| Node.js | 16+ | 前端运行环境 |
| npm | 8+ | 前端包管理器 |
| MySQL | 8.0+ | 数据库 |

---

## 项目结构

```
xd_learn_platform/
├── docs/                       # 项目文档
│   ├── README.md               # 项目说明
│   ├── API文档.md              # API 接口文档
│   ├── 部署说明.md              # 本文档
│   └── xd_learn_platform.sql   # 数据库初始化脚本
├── xd-learn-backend/           # 后端（Spring Boot）
│   ├── pom.xml
│   └── src/
├── xd-learn-frontend/          # 前端（Vue 3）
│   ├── package.json
│   └── src/
└── .gitignore
```

---

## 后端部署

### 1. 安装依赖

确保已安装 JDK 21 和 Maven 3.9+：

```bash
java -version   # 应显示 21.x
mvn -version    # 应显示 3.9.x
```

### 2. 创建数据库

启动 MySQL 服务，然后执行初始化脚本：

```bash
mysql -u root -p < docs/xd_learn_platform.sql
```

该脚本会自动创建数据库 `xd_learn_platform`、所有表结构以及测试数据（含 10 门课程、20 个用户等）。

### 3. 配置应用

进入后端目录，复制示例配置文件并修改：

```bash
cd xd-learn-backend
cp src/main/resources/application-example.yaml src/main/resources/application.yaml
```

编辑 `application.yaml`，修改以下关键配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/xd_learn_platform?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8mb4
    username: root          # 改为你的 MySQL 用户名
    password: 123456        # 改为你的 MySQL 密码

jwt:
  secret: your-secret-key   # 建议改为一个足够复杂的随机字符串
  expiration: 86400         # Token 有效期（秒），默认 24 小时

ai:
  dashscope:
    api-key: your-api-key   # 阿里云 DashScope API Key（如需 AI 助手功能）
```

### 4. 构建与运行

**开发模式（直接运行）：**

```bash
cd xd-learn-backend
./mvnw spring-boot:run
```

**打包后运行：**

```bash
cd xd-learn-backend
./mvnw clean package -DskipTests
java -jar target/xd-learn-backend-0.0.1-SNAPSHOT.jar
```

后端服务默认启动在 `http://localhost:8080`。

### 5. 验证后端

```bash
curl http://localhost:8080/api/courses
```

返回 JSON 数据即表示后端启动成功：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [...],
    "total": 10,
    "page": 1,
    "pageSize": 9
  }
}
```

---

## 前端部署

### 1. 安装依赖

```bash
cd xd-learn-frontend
npm install
```

### 2. 开发模式运行

```bash
npm run dev
```

前端开发服务器默认启动在 `http://localhost:3000`，API 请求会自动代理到后端 `http://localhost:8080`。

### 3. 访问应用

浏览器打开 `http://localhost:3000`，使用以下测试账号登录：

| 用户名 | 密码 |
|--------|------|
| `zhangsan` | `123456` |
| `lisi` | `123456` |
| `wangwu` | `123456` |

（密码为 MD5 加密存储，以上为数据库中的测试账号）

---

## 生产环境部署

### 前端构建

```bash
cd xd-learn-frontend
npm run build
```

构建产物输出到 `dist/` 目录，将该目录部署到 Nginx 或其他 Web 服务器。

### Nginx 配置示例

```nginx
server {
    listen       80;
    server_name  your-domain.com;

    # 前端静态文件
    location / {
        root   /var/www/xd-learn/dist;
        index  index.html;
        try_files $uri $uri/ /index.html;   # Vue Router History 模式支持
    }

    # API 反向代理
    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # SSE（AI 助手流式响应）支持
        proxy_buffering off;
        proxy_cache off;
        proxy_read_timeout 300s;
    }

    # 收藏接口（未使用 /api 前缀）
    location /courses/ {
        proxy_pass http://localhost:8080/courses/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### 后端生产配置建议

1. **更换 JWT 密钥**：使用高强度随机字符串，不要使用默认值
2. **数据库密码**：使用强密码，不要用 `123456`
3. **关闭调试日志**：将日志级别调整为 `INFO` 或 `WARN`
4. **配置跨域**：在 `WebMvcConfig.java` 中限制 `allowedOrigins` 为实际域名
5. **DashScope API Key**：确保已配置有效的阿里云 API Key

---

## 常见问题

### Q: 后端启动报 `Communications link failure`

检查 MySQL 服务是否已启动，以及 `application.yaml` 中的数据库连接信息是否正确：

```bash
mysql -u root -p -e "SELECT 1"
```

### Q: 前端请求 404

1. 确认后端已启动在 8080 端口
2. 检查 `vite.config.js` 中的代理配置是否正确
3. 确认请求路径以 `/api` 开头

### Q: AI 助手无响应

1. 检查 `application.yaml` 中的 DashScope API Key 是否有效
2. 确认网络可以访问阿里云 DashScope 服务
3. 查看后端日志是否有异常堆栈

### Q: Token 过期后前端报 401

前端会在收到 401 时尝试使用 refreshToken 刷新。如果 refreshToken 也过期（7 天有效期），需要重新登录。注意后端目前未实现 `/api/auth/refresh` 接口。

### Q: 数据库字符集问题

确保 MySQL 使用 `utf8mb4` 字符集：

```sql
ALTER DATABASE xd_learn_platform CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
