# API 接口文档

> 小滴智能学习平台（XD Learn Platform）后端 API 接口说明

---

## 目录

- [概述](#概述)
- [统一响应格式](#统一响应格式)
- [认证机制](#认证机制)
- [错误码说明](#错误码说明)
- [接口列表](#接口列表)
  - [认证接口](#1-认证接口)
  - [课程接口](#2-课程接口)
  - [评论接口](#3-评论接口)
  - [视频接口](#4-视频接口)
  - [收藏接口](#5-收藏接口)
  - [课程分类接口](#6-课程分类接口)
  - [用户接口](#7-用户接口)
  - [AI 助手接口](#8-ai-助手接口)
- [数据库表结构](#数据库表结构)

---

## 概述

- **基础路径**：`/api`
- **数据格式**：JSON（`Content-Type: application/json`）
- **字符编码**：UTF-8
- **认证方式**：Bearer Token（JWT）
- **AI 助手流式响应**：Server-Sent Events（SSE）

---

## 统一响应格式

所有接口返回统一的 JSON 结构：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | Integer | 状态码，200 表示成功 |
| `message` | String | 提示信息 |
| `data` | Object | 响应数据，具体结构因接口而异 |

---

## 认证机制

### 获取 Token

通过登录接口获取 `accessToken` 和 `refreshToken`。

### 使用 Token

在需要认证的接口请求头中添加：

```
Authorization: Bearer <accessToken>
```

### Token 有效期

| Token | 有效期 |
|-------|--------|
| accessToken | 24 小时 |
| refreshToken | 7 天 |

### 公开接口（无需认证）

以下接口无需携带 Token：

- `GET /api/courses` — 课程列表
- `GET /api/courses/{id}/detail` — 课程详情
- `GET /api/courses/{id}/chapters` — 课程章节
- `GET /api/courses/{id}/materials` — 课程资料
- `GET /api/courses/{id}/videos` — 课程视频列表
- `GET /api/courses/{id}/comments` — 课程评论列表
- `GET /api/videos/{id}/url` — 视频播放地址
- `GET /api/course-types` — 课程分类列表
- `POST /api/auth/login` — 用户登录

---

## 错误码说明

| 状态码 | 含义 | 说明 |
|--------|------|------|
| 200 | 成功 | 请求正常处理 |
| 400 | 请求错误 | 参数校验失败 |
| 401 | 未认证 | Token 无效或已过期 |
| 403 | 无权限 | 无权访问该资源 |
| 404 | 未找到 | 资源不存在 |
| 500 | 服务器错误 | 服务端内部异常 |

错误响应示例：

```json
{
  "code": 401,
  "message": "Token已过期",
  "data": {
    "error_code": "TOKEN_EXPIRED",
    "error_message": "Token已过期",
    "timestamp": "2025-01-01T12:00:00",
    "path": "/api/user/info"
  }
}
```

---

## 接口列表

### 1. 认证接口

> 路径前缀：`/api/auth`

#### 1.1 用户登录

用户通过用户名和密码登录，成功后返回 JWT Token。

- **URL**：`POST /api/auth/login`
- **认证**：不需要

**请求参数（Body）：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `username` | String | 是 | 用户名，2-20 个字符 |
| `password` | String | 是 | 密码，6-20 个字符 |

**请求示例：**

```json
{
  "username": "zhangsan",
  "password": "123456"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "userInfo": {
      "id": 1,
      "username": "zhangsan",
      "nickname": "张三",
      "email": "zhangsan@example.com",
      "avatar": "https://example.com/avatar.jpg",
      "registerTime": "2025-01-01T12:00:00",
      "lastLoginTime": "2025-06-15T10:30:00"
    }
  }
}
```

**失败响应：**

```json
{
  "code": 401,
  "message": "用户名或密码错误",
  "data": null
}
```

---

### 2. 课程接口

> 路径前缀：`/api/courses`

#### 2.1 获取课程列表

分页获取课程列表，支持按关键词和分类筛选。

- **URL**：`GET /api/courses`
- **认证**：不需要

**查询参数：**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `keyword` | String | 否 | — | 搜索关键词（按课程标题模糊匹配） |
| `typeId` | Long | 否 | — | 课程分类 ID |
| `page` | Integer | 否 | 1 | 页码 |
| `pageSize` | Integer | 否 | 9 | 每页数量 |

**请求示例：**

```
GET /api/courses?keyword=Java&page=1&pageSize=9
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "title": "Java 零基础入门到精通",
        "description": "从零开始学习 Java 编程...",
        "author": "张教授",
        "type": "编程语言",
        "rating": 4.8,
        "students": 1234,
        "price": 99.00,
        "cover": "https://example.com/cover1.jpg",
        "createTime": "2025-01-01T00:00:00"
      }
    ],
    "total": 10,
    "page": 1,
    "pageSize": 9
  }
}
```

---

#### 2.2 获取课程详情

获取单门课程的完整信息，包括学习目标、目标受众、章节和视频列表。

- **URL**：`GET /api/courses/{id}/detail`
- **认证**：不需要

**路径参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 课程 ID |

**请求示例：**

```
GET /api/courses/1/detail
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Java 零基础入门到精通",
    "description": "从零开始学习 Java 编程...",
    "author": "张教授",
    "type": "编程语言",
    "rating": 4.8,
    "students": 1234,
    "price": 99.00,
    "cover": "https://example.com/cover1.jpg",
    "createTime": "2025-01-01T00:00:00",
    "goals": [
      "掌握 Java 基础语法",
      "理解面向对象编程思想",
      "能够独立开发 Java 应用"
    ],
    "audience": [
      "零基础编程爱好者",
      "想转行做 Java 开发的人"
    ],
    "chapters": [
      {
        "id": 1,
        "title": "第一章：Java 概述",
        "videos": [
          {
            "id": 1,
            "title": "1.1 什么是 Java",
            "duration": "15:30",
            "playCount": 500
          }
        ]
      }
    ]
  }
}
```

---

#### 2.3 获取课程资料

获取指定课程的学习资料列表。

- **URL**：`GET /api/courses/{courseId}/materials`
- **认证**：不需要

**路径参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `courseId` | Long | 课程 ID |

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "Java 基础课件.pdf",
      "type": "document",
      "size": "2.5MB",
      "uploadDate": "2025-01-15",
      "downloadCount": 120,
      "url": "https://example.com/files/java-basics.pdf"
    }
  ]
}
```

---

#### 2.4 获取课程章节

获取指定课程的章节及章节内的视频列表。

- **URL**：`GET /api/courses/{courseId}/chapters`
- **认证**：不需要

**路径参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `courseId` | Long | 课程 ID |

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "title": "第一章：Java 概述",
      "videos": [
        {
          "id": 1,
          "title": "1.1 什么是 Java",
          "duration": "15:30",
          "playCount": 500
        },
        {
          "id": 2,
          "title": "1.2 搭建开发环境",
          "duration": "20:00",
          "playCount": 480
        }
      ]
    }
  ]
}
```

---

### 3. 评论接口

> 路径前缀：`/api/courses`

#### 3.1 获取课程评论列表

分页获取指定课程的评论列表。

- **URL**：`GET /api/courses/{courseId}/comments`
- **认证**：不需要

**路径参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `courseId` | Long | 课程 ID |

**查询参数：**

| 参数 | 类型 | 必填 | 默认值 | 说明 |
|------|------|------|--------|------|
| `page` | Integer | 否 | 1 | 页码 |
| `pageSize` | Integer | 否 | 10 | 每页数量 |

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "data": [
      {
        "id": 1,
        "content": "课程讲得很好，受益匪浅！",
        "userId": 1,
        "userName": "张三",
        "userAvatar": "https://example.com/avatar.jpg",
        "courseId": 1,
        "createTime": "2025-06-10T14:30:00",
        "rating": 5
      }
    ],
    "total": 23,
    "pageNum": 1,
    "pageSize": 10,
    "totalPages": 3,
    "hasNextPage": true,
    "hasPreviousPage": false
  }
}
```

---

#### 3.2 发表评论

为指定课程发表评论。需要登录。

- **URL**：`POST /api/courses/{courseId}/comments`
- **认证**：需要（Bearer Token）

**路径参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `courseId` | Long | 课程 ID |

**请求参数（Body）：**

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `rating` | Integer | 是 | 评分，1-5 分 |
| `content` | String | 是 | 评论内容，10-500 个字符 |

**请求示例：**

```json
{
  "rating": 5,
  "content": "课程内容非常丰富，老师讲解清晰易懂，强烈推荐！"
}
```

**成功响应：**

```json
{
  "code": 200,
  "message": "评论发表成功",
  "data": {
    "id": 24,
    "content": "课程内容非常丰富，老师讲解清晰易懂，强烈推荐！",
    "userId": 1,
    "userName": "张三",
    "userAvatar": "https://example.com/avatar.jpg",
    "courseId": 1,
    "createTime": "2025-06-15T10:30:00",
    "rating": 5
  }
}
```

---

### 4. 视频接口

> 路径前缀：`/api`

#### 4.1 获取视频播放地址

获取指定视频的播放地址和详细信息。

- **URL**：`GET /api/videos/{videoId}/url`
- **认证**：不需要

**路径参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `videoId` | Long | 视频 ID |

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "url": "https://example.com/videos/java-intro.mp4",
    "duration": "15:30",
    "quality": "1080p"
  }
}
```

---

#### 4.2 获取课程视频列表

获取指定课程下所有视频的列表。

- **URL**：`GET /api/courses/{courseId}/videos`
- **认证**：不需要

**路径参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `courseId` | Long | 课程 ID |

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "1.1 什么是 Java",
      "chapterId": 1,
      "chapterName": "第一章：Java 概述",
      "duration": "15:30",
      "playCount": 500,
      "order": 1
    },
    {
      "id": 2,
      "name": "1.2 搭建开发环境",
      "chapterId": 1,
      "chapterName": "第一章：Java 概述",
      "duration": "20:00",
      "playCount": 480,
      "order": 2
    }
  ]
}
```

---

### 5. 收藏接口

> 路径前缀：`/courses`
>
> ⚠️ 注意：收藏接口路径未使用 `/api` 前缀，与其他接口不一致。且通过 `userId` 查询参数传递用户身份，而非 JWT Token。

#### 5.1 收藏课程

收藏指定课程。

- **URL**：`POST /courses/{id}/collect`
- **认证**：不需要（通过 userId 参数标识用户）

**路径参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| `id` | Long | 课程 ID |

**查询参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `userId` | Long | 是 | 用户 ID |

**请求示例：**

```
POST /courses/1/collect?userId=1
```

**成功响应：**

```json
{
  "code": 200,
  "message": "收藏成功",
  "data": null
}
```

---

#### 5.2 取消收藏

取消收藏指定课程。

- **URL**：`DELETE /courses/{id}/collect`
- **认证**：不需要（通过 userId 参数标识用户）

**路径参数和查询参数：** 同 5.1

**成功响应：**

```json
{
  "code": 200,
  "message": "取消收藏成功",
  "data": null
}
```

---

#### 5.3 检查收藏状态

检查当前用户是否已收藏指定课程。

- **URL**：`GET /courses/{id}/check-collect`
- **认证**：不需要（通过 userId 参数标识用户）

**路径参数和查询参数：** 同 5.1

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "isCollected": true
  }
}
```

---

### 6. 课程分类接口

> 路径前缀：`/api/course-types`

#### 6.1 获取所有课程分类

获取全部课程分类列表。

- **URL**：`GET /api/course-types`
- **认证**：不需要

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "编程语言",
      "description": "各种编程语言的学习课程"
    },
    {
      "id": 2,
      "name": "前端开发",
      "description": "Web 前端开发相关课程"
    }
  ]
}
```

---

### 7. 用户接口

> 路径前缀：`/api/user`

#### 7.1 获取当前用户信息

获取当前登录用户的详细信息。

- **URL**：`GET /api/user/info`
- **认证**：需要（Bearer Token）

**请求头：**

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**成功响应：**

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "nickname": "张三",
    "avatar": "https://example.com/avatar.jpg",
    "email": "zhangsan@example.com",
    "registerTime": "2025-01-01T12:00:00",
    "lastLoginTime": "2025-06-15T10:30:00"
  }
}
```

**失败响应（未登录或 Token 过期）：**

```json
{
  "code": 401,
  "message": "Token已过期",
  "data": null
}
```

---

### 8. AI 助手接口

> 路径前缀：`/api/assistant`
>
> AI 助手基于阿里云 DashScope（通义千问 Qwen-plus 模型），支持多轮对话和编程学习问答。

#### 8.1 AI 助手流式问答

向 AI 助手提问，以 SSE（Server-Sent Events）流式方式返回回答。

- **URL**：`POST /api/assistant/ask/stream`
- **认证**：需要（Bearer Token）
- **Content-Type**：`text/plain`
- **响应格式**：SSE（`text/event-stream`）

**请求参数（Body）：**

直接发送纯文本问题（不是 JSON 格式）。

**请求示例：**

```bash
curl -X POST http://localhost:8080/api/assistant/ask/stream \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -H "Content-Type: text/plain" \
  -d "什么是 Java 中的多态？"
```

**响应格式：**

```
data: 什么是
data:  Java
data:  中的
data:  多态
data: ？
data: 
data: [DONE]
```

- 每条 `data:` 行包含 AI 回答的一个文本片段
- 流以 `data: [DONE]` 结束
- AI 助手角色为"木又编程学习助手"，专注于编程学习问答
- 支持多轮对话，服务器会为每个用户维护上下文记忆（内存存储，重启后清空）

**前端对接示例（JavaScript）：**

```javascript
const response = await fetch('/api/assistant/ask/stream', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'text/plain'
  },
  body: '什么是 Java 中的多态？'
});

const reader = response.body.getReader();
const decoder = new TextDecoder();

while (true) {
  const { done, value } = await reader.read();
  if (done) break;

  const text = decoder.decode(value);
  const lines = text.split('\n');

  for (const line of lines) {
    if (line.startsWith('data: ')) {
      const content = line.slice(6);
      if (content === '[DONE]') break;
      // 将 content 拼接到 UI 显示
      appendToUI(content);
    }
  }
}
```

---

## 数据库表结构

### user — 用户表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `username` | VARCHAR(50) | 用户名，唯一 |
| `nickname` | VARCHAR(50) | 昵称 |
| `password` | VARCHAR(100) | 密码（MD5 + 盐值加密） |
| `email` | VARCHAR(100) | 邮箱，唯一 |
| `avatar` | VARCHAR(255) | 头像 URL |
| `created_time` | DATETIME | 注册时间 |
| `updated_time` | DATETIME | 更新时间 |

### course — 课程表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `title` | VARCHAR(100) | 课程标题 |
| `description` | TEXT | 课程描述 |
| `cover_image` | VARCHAR(255) | 封面图 URL |
| `teacher_name` | VARCHAR(50) | 讲师姓名 |
| `price` | DECIMAL(10,2) | 价格 |
| `rating` | DECIMAL(2,1) | 评分（1.0-5.0） |
| `student_count` | INT | 学习人数 |
| `type_id` | BIGINT | 分类 ID（FK → course_type） |
| `creator_id` | BIGINT | 创建者 ID（FK → user） |

### course_type — 课程分类表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `name` | VARCHAR(50) | 分类名称 |
| `description` | VARCHAR(200) | 分类描述 |

### course_chapter — 章节表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `title` | VARCHAR(100) | 章节标题 |
| `description` | TEXT | 章节描述 |
| `course_id` | BIGINT | 所属课程 ID（FK → course） |
| `sort_order` | INT | 排序序号 |

### course_resource — 课程资源表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `resource_name` | VARCHAR(100) | 资源名称 |
| `resource_url` | VARCHAR(255) | 资源 URL |
| `course_id` | BIGINT | 所属课程 ID（FK → course） |
| `chapter_id` | BIGINT | 所属章节 ID（FK → course_chapter） |
| `sort_order` | INT | 排序序号 |
| `type_id` | BIGINT | 资源类型 ID（FK → resource_type） |

### resource_type — 资源类型表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `name` | VARCHAR(50) | 类型名称（video / document / code 等） |

### comment — 评论表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `content` | TEXT | 评论内容 |
| `user_id` | BIGINT | 评论用户 ID（FK → user） |
| `course_id` | BIGINT | 所属课程 ID（FK → course） |
| `created_time` | DATETIME | 评论时间 |

### favorite — 收藏表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `user_id` | BIGINT | 用户 ID（FK → user） |
| `course_id` | BIGINT | 课程 ID（FK → course） |
| | | UNIQUE(`user_id`, `course_id`) |

### learning_objective — 学习目标表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `content` | VARCHAR(200) | 目标内容 |
| `course_id` | BIGINT | 所属课程 ID（FK → course） |

### target_audience — 目标受众表

| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | BIGINT | 主键，自增 |
| `content` | VARCHAR(200) | 受众描述 |
| `course_id` | BIGINT | 所属课程 ID（FK → course） |

---

## 接口总览

| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| `POST` | `/api/auth/login` | ❌ | 用户登录 |
| `GET` | `/api/courses` | ❌ | 课程列表（分页、筛选） |
| `GET` | `/api/courses/{id}/detail` | ❌ | 课程详情 |
| `GET` | `/api/courses/{courseId}/materials` | ❌ | 课程资料 |
| `GET` | `/api/courses/{courseId}/chapters` | ❌ | 课程章节 |
| `GET` | `/api/courses/{courseId}/comments` | ❌ | 评论列表（分页） |
| `POST` | `/api/courses/{courseId}/comments` | ✅ | 发表评论 |
| `GET` | `/api/videos/{videoId}/url` | ❌ | 视频播放地址 |
| `GET` | `/api/courses/{courseId}/videos` | ❌ | 课程视频列表 |
| `POST` | `/courses/{id}/collect` | ❌ | 收藏课程 |
| `DELETE` | `/courses/{id}/collect` | ❌ | 取消收藏 |
| `GET` | `/courses/{id}/check-collect` | ❌ | 检查收藏状态 |
| `GET` | `/api/course-types` | ❌ | 课程分类列表 |
| `GET` | `/api/user/info` | ✅ | 当前用户信息 |
| `POST` | `/api/assistant/ask/stream` | ✅ | AI 助手流式问答 |
