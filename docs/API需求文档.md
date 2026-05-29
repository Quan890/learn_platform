# 小滴智能学习平台 - 前端API接口需求文档

## 1. 文档说明

### 1.1 文档目的
本文档旨在明确前端应用所需的后端API接口规范，指导后端开发团队实现符合前端需求的接口，确保前后端协作顺畅，提高开发效率。

### 1.2 适用范围
本文档适用于小滴智能学习平台的后端开发团队，以及参与该项目的其他技术人员。

### 1.3 术语定义
- **JWT**: JSON Web Token，用于用户认证和授权
- **API**: Application Programming Interface，应用程序编程接口
- **HTTP**: HyperText Transfer Protocol，超文本传输协议
- **RESTful**: 一种软件架构风格，用于设计网络应用程序

## 2. 接口设计规范

### 2.1 接口风格
采用RESTful API设计风格，使用HTTP方法表示操作类型：
- GET: 获取资源
- POST: 创建资源
- PUT: 更新资源
- DELETE: 删除资源

### 2.2 统一响应格式
所有API接口返回的数据格式必须统一，包含以下字段：
```json
{
  "code": 200,          // 状态码，200表示成功，其他表示错误
  "message": "success",  // 响应消息，成功或错误描述
  "data": {}             // 响应数据，根据接口返回不同的数据结构
}
```

### 2.3 状态码规范
| 状态码 | 含义 | 说明 |
|--------|------|------|
| 200    | 成功 | 请求成功 |
| 400    | 错误请求 | 请求参数错误或格式不正确 |
| 401    | 未授权 | 缺少认证信息或认证失败 |
| 403    | 禁止访问 | 没有权限访问该资源 |
| 404    | 资源不存在 | 请求的资源不存在 |
| 500    | 服务器错误 | 服务器内部错误 |

### 2.4 错误处理
- 所有错误响应必须包含明确的错误消息
- 业务错误应返回具体的错误码和描述
- 系统错误应返回通用的错误消息，避免泄露敏感信息

## 3. 认证相关接口

### 3.1 登录接口

#### 3.1.1 功能描述
用户登录，获取访问令牌和刷新令牌

#### 3.1.2 请求信息
- **请求方法**: POST
- **URL路径**: /auth/login
- **请求头**: 
  - Content-Type: application/json

#### 3.1.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码，长度6-20位 |

#### 3.1.4 响应数据
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "username": "user1",
      "nickname": "用户1",
      "email": "user1@example.com",
      "avatar": "https://example.com/avatar1.jpg"
    }
  }
}
```

#### 3.1.5 错误码
| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 401 | 用户名或密码错误 | 用户名或密码不正确 |
| 400 | 用户名不能为空 | 缺少用户名参数 |
| 400 | 密码不能为空 | 缺少密码参数 |

### 3.2 注册接口

#### 3.2.1 功能描述
用户注册新账号

#### 3.2.2 请求信息
- **请求方法**: POST
- **URL路径**: /auth/register
- **请求头**: 
  - Content-Type: application/json

#### 3.2.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| username | String | 是 | 用户名，长度3-20位 |
| password | String | 是 | 密码，长度6-20位 |
| email | String | 是 | 邮箱地址，格式正确 |
| nickname | String | 否 | 用户昵称 |

#### 3.2.4 响应数据
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "username": "user1",
    "nickname": "用户1",
    "email": "user1@example.com"
  }
}
```

#### 3.2.5 错误码
| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 400 | 用户名已存在 | 该用户名已被注册 |
| 400 | 邮箱已存在 | 该邮箱已被注册 |
| 400 | 邮箱格式不正确 | 邮箱格式不符合要求 |

### 3.3 刷新令牌接口

#### 3.3.1 功能描述
使用刷新令牌获取新的访问令牌

#### 3.3.2 请求信息
- **请求方法**: POST
- **URL路径**: /auth/refresh
- **请求头**: 
  - Content-Type: application/json

#### 3.3.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| refreshToken | String | 是 | 刷新令牌 |

#### 3.3.4 响应数据
```json
{
  "code": 200,
  "message": "刷新成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

#### 3.3.5 错误码
| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 401 | 刷新令牌无效 | 刷新令牌已过期或无效 |

### 3.4 退出登录接口

#### 3.4.1 功能描述
用户退出登录，清除令牌

#### 3.4.2 请求信息
- **请求方法**: POST
- **URL路径**: /auth/logout
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 3.4.3 请求参数
无

#### 3.4.4 响应数据
```json
{
  "code": 200,
  "message": "退出成功",
  "data": null
}
```

## 4. 课程相关接口

### 4.1 获取课程列表

#### 4.1.1 功能描述
获取课程列表，支持分页和筛选

#### 4.1.2 请求信息
- **请求方法**: GET
- **URL路径**: /courses
- **请求头**: 
  - Authorization: Bearer {accessToken} (可选)

#### 4.1.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |
| keyword | String | 否 | 搜索关键词，支持课程名称和描述 |
| type | String | 否 | 课程类型 |
| sortBy | String | 否 | 排序字段，如rating、students、price |
| sortOrder | String | 否 | 排序方向，asc或desc，默认asc |

#### 4.1.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "title": "Vue3从入门到精通",
        "description": "全面学习Vue3框架，掌握Composition API",
        "author": "张老师",
        "type": "前端开发",
        "rating": 4.8,
        "students": 1234,
        "price": 199,
        "cover": "https://example.com/cover1.jpg",
        "createTime": "2024-01-01T00:00:00Z"
      }
    ],
    "total": 100,
    "page": 1,
    "pageSize": 10
  }
}
```

### 4.2 搜索课程

#### 4.2.1 功能描述
根据关键词搜索课程

#### 4.2.2 请求信息
- **请求方法**: GET
- **URL路径**: /courses/search
- **请求头**: 
  - Authorization: Bearer {accessToken} (可选)

#### 4.2.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| keyword | String | 是 | 搜索关键词 |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

#### 4.2.4 响应数据
同4.1.4 获取课程列表

### 4.3 获取课程详情

#### 4.3.1 功能描述
获取指定课程的详细信息

#### 4.3.2 请求信息
- **请求方法**: GET
- **URL路径**: /courses/{id}
- **请求头**: 
  - Authorization: Bearer {accessToken} (可选)

#### 4.3.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | Integer | 是 | 课程ID，通过URL路径传递 |

#### 4.3.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "Vue3从入门到精通",
    "description": "全面学习Vue3框架，掌握Composition API",
    "detail": "详细的课程介绍...",
    "author": "张老师",
    "type": "前端开发",
    "rating": 4.8,
    "students": 1234,
    "price": 199,
    "cover": "https://example.com/cover1.jpg",
    "goals": ["掌握Vue3核心概念", "学会Composition API"],
    "audience": ["前端开发者", "Vue初学者"],
    "chapters": [
      {
        "id": 1,
        "title": "第一章：Vue3基础",
        "videos": [
          {
            "id": 1,
            "name": "1.1 Vue3简介",
            "duration": "15:30",
            "playCount": 1000
          }
        ]
      }
    ],
    "createTime": "2024-01-01T00:00:00Z",
    "updateTime": "2024-01-02T00:00:00Z"
  }
}
```

### 4.4 收藏课程

#### 4.4.1 功能描述
收藏指定课程

#### 4.4.2 请求信息
- **请求方法**: POST
- **URL路径**: /courses/{id}/collect
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 4.4.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | Integer | 是 | 课程ID，通过URL路径传递 |

#### 4.4.4 响应数据
```json
{
  "code": 200,
  "message": "收藏成功",
  "data": null
}
```

### 4.5 取消收藏课程

#### 4.5.1 功能描述
取消收藏指定课程

#### 4.5.2 请求信息
- **请求方法**: DELETE
- **URL路径**: /courses/{id}/collect
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 4.5.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | Integer | 是 | 课程ID，通过URL路径传递 |

#### 4.5.4 响应数据
```json
{
  "code": 200,
  "message": "取消收藏成功",
  "data": null
}
```

### 4.6 检查是否收藏

#### 4.6.1 功能描述
检查当前用户是否收藏了指定课程

#### 4.6.2 请求信息
- **请求方法**: GET
- **URL路径**: /courses/{id}/check-collect
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 4.6.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | Integer | 是 | 课程ID，通过URL路径传递 |

#### 4.6.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "isCollected": true
  }
}
```

## 5. 评论相关接口

### 5.1 获取课程评论

#### 5.1.1 功能描述
获取指定课程的评论列表

#### 5.1.2 请求信息
- **请求方法**: GET
- **URL路径**: /courses/{courseId}/comments
- **请求头**: 
  - Authorization: Bearer {accessToken} (可选)

#### 5.1.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| courseId | Integer | 是 | 课程ID，通过URL路径传递 |
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

#### 5.1.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "userId": 1,
        "userName": "用户1",
        "userAvatar": "https://example.com/avatar1.jpg",
        "rating": 5,
        "content": "课程非常好，老师讲解清晰",
        "createTime": "2024-01-01T00:00:00Z"
      }
    ],
    "total": 50,
    "page": 1,
    "pageSize": 10
  }
}
```

### 5.2 发表评论

#### 5.2.1 功能描述
为指定课程发表评论

#### 5.2.2 请求信息
- **请求方法**: POST
- **URL路径**: /courses/{courseId}/comments
- **请求头**: 
  - Authorization: Bearer {accessToken}
  - Content-Type: application/json

#### 5.2.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| courseId | Integer | 是 | 课程ID，通过URL路径传递 |
| rating | Integer | 是 | 评分，1-5分 |
| content | String | 是 | 评论内容，长度10-500字 |

#### 5.2.4 响应数据
```json
{
  "code": 200,
  "message": "评论成功",
  "data": {
    "id": 1,
    "userId": 1,
    "userName": "用户1",
    "userAvatar": "https://example.com/avatar1.jpg",
    "rating": 5,
    "content": "课程非常好，老师讲解清晰",
    "createTime": "2024-01-01T00:00:00Z"
  }
}
```

## 6. 视频相关接口

### 6.1 获取视频播放地址

#### 6.1.1 功能描述
获取视频的播放地址

#### 6.1.2 请求信息
- **请求方法**: GET
- **URL路径**: /videos/{videoId}/url
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 6.1.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| videoId | Integer | 是 | 视频ID，通过URL路径传递 |

#### 6.1.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "url": "https://example.com/video1.mp4",
    "duration": 930, // 视频时长，秒
    "quality": "1080p"
  }
}
```

### 6.2 获取视频列表

#### 6.2.1 功能描述
获取指定课程的视频列表

#### 6.2.2 请求信息
- **请求方法**: GET
- **URL路径**: /courses/{courseId}/videos
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 6.2.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| courseId | Integer | 是 | 课程ID，通过URL路径传递 |

#### 6.2.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "1.1 Vue3简介",
      "chapterId": 1,
      "chapterName": "第一章：Vue3基础",
      "duration": 930, // 视频时长，秒
      "playCount": 1000,
      "order": 1 // 视频顺序
    }
  ]
}
```

## 7. 资料相关接口

### 7.1 获取课程资料

#### 7.1.1 功能描述
获取指定课程的资料列表

#### 7.1.2 请求信息
- **请求方法**: GET
- **URL路径**: /courses/{courseId}/materials
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 7.1.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| courseId | Integer | 是 | 课程ID，通过URL路径传递 |

#### 7.1.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "Vue3课程代码",
      "type": "文档",
      "size": 12345678, // 文件大小，字节
      "uploadDate": "2024-01-01T00:00:00Z",
      "downloadCount": 500
    }
  ]
}
```

### 7.2 下载资料

#### 7.2.1 功能描述
下载指定资料文件

#### 7.2.2 请求信息
- **请求方法**: GET
- **URL路径**: /materials/{materialId}/download
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 7.2.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| materialId | Integer | 是 | 资料ID，通过URL路径传递 |

#### 7.2.4 响应数据
- **响应头**: 
  - Content-Type: application/octet-stream
  - Content-Disposition: attachment; filename="filename.ext"
- **响应体**: 文件二进制数据

### 7.3 上传资料

#### 7.3.1 功能描述
上传课程资料文件

#### 7.3.2 请求信息
- **请求方法**: POST
- **URL路径**: /materials/upload
- **请求头**: 
  - Authorization: Bearer {accessToken}
  - Content-Type: multipart/form-data

#### 7.3.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| file | File | 是 | 资料文件，支持doc、pdf、zip等格式，大小不超过100MB |
| courseId | Integer | 是 | 课程ID |
| name | String | 否 | 资料名称，默认使用文件名 |
| type | String | 否 | 资料类型，如文档、代码、视频 |

#### 7.3.4 响应数据
```json
{
  "code": 200,
  "message": "上传成功",
  "data": {
    "id": 1,
    "name": "Vue3课程代码",
    "type": "文档",
    "size": 12345678,
    "uploadDate": "2024-01-01T00:00:00Z"
  }
}
```

## 8. 智能助手接口

### 8.1 智能问答

#### 8.1.1 功能描述
向智能助手提问，获取回答

#### 8.1.2 请求信息
- **请求方法**: POST
- **URL路径**: /assistant/ask
- **请求头**: 
  - Authorization: Bearer {accessToken}
  - Content-Type: application/json

#### 8.1.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| question | String | 是 | 问题内容 |
| courseId | Integer | 否 | 关联课程ID，可选 |

#### 8.1.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "answer": "这是智能助手的回答内容...",
    "relatedCourses": [
      {
        "id": 1,
        "title": "Vue3从入门到精通"
      }
    ]
  }
}
```

## 9. 个人中心接口

### 9.1 获取用户信息

#### 9.1.1 功能描述
获取当前登录用户的信息

#### 9.1.2 请求信息
- **请求方法**: GET
- **URL路径**: /user/info
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 9.1.3 请求参数
无

#### 9.1.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "user1",
    "nickname": "用户1",
    "email": "user1@example.com",
    "avatar": "https://example.com/avatar1.jpg",
    "registerTime": "2024-01-01T00:00:00Z",
    "lastLoginTime": "2024-01-02T00:00:00Z"
  }
}
```

### 9.2 创建课程

#### 9.2.1 功能描述
创建新的课程

#### 9.2.2 请求信息
- **请求方法**: POST
- **URL路径**: /user/courses
- **请求头**: 
  - Authorization: Bearer {accessToken}
  - Content-Type: application/json

#### 9.2.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| title | String | 是 | 课程标题，长度10-100字 |
| description | String | 是 | 课程描述，长度50-500字 |
| detail | String | 是 | 课程详细内容，支持HTML |
| type | String | 是 | 课程类型 |
| price | Number | 是 | 课程价格，0表示免费 |
| cover | String | 否 | 课程封面图片URL |
| goals | Array | 否 | 学习目标，数组类型 |
| audience | Array | 否 | 适合人群，数组类型 |

#### 9.2.4 响应数据
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": 1,
    "title": "Vue3从入门到精通",
    "description": "全面学习Vue3框架",
    "author": "用户1",
    "type": "前端开发",
    "price": 199,
    "createTime": "2024-01-01T00:00:00Z"
  }
}
```

### 9.3 获取用户收藏列表

#### 9.3.1 功能描述
获取当前用户收藏的课程列表

#### 9.3.2 请求信息
- **请求方法**: GET
- **URL路径**: /user/collections
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 9.3.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| page | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页数量，默认10 |

#### 9.3.4 响应数据
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "courseId": 1,
        "courseTitle": "Vue3从入门到精通",
        "courseCover": "https://example.com/cover1.jpg",
        "coursePrice": 199,
        "collectTime": "2024-01-01T00:00:00Z"
      }
    ],
    "total": 5,
    "page": 1,
    "pageSize": 10
  }
}
```

### 9.4 取消收藏

#### 9.4.1 功能描述
取消收藏指定课程

#### 9.4.2 请求信息
- **请求方法**: DELETE
- **URL路径**: /user/collections/{id}
- **请求头**: 
  - Authorization: Bearer {accessToken}

#### 9.4.3 请求参数
| 字段名 | 数据类型 | 必填 | 说明 |
|--------|----------|------|------|
| id | Integer | 是 | 收藏记录ID，通过URL路径传递 |

#### 9.4.4 响应数据
```json
{
  "code": 200,
  "message": "取消收藏成功",
  "data": null
}
```

## 10. 性能要求

### 10.1 响应时间
- 普通查询接口响应时间不超过500ms
- 复杂查询接口响应时间不超过2000ms
- 文件上传下载接口响应时间不超过10s

### 10.2 并发处理
- 支持至少1000并发请求
- 数据库查询优化，避免全表扫描
- 合理使用缓存，减少数据库压力

### 10.3 可靠性
- 接口可用性不低于99.9%
- 数据一致性保障
- 完善的错误日志和监控机制

## 11. 安全要求

### 11.1 认证与授权
- 所有需要登录的接口必须验证JWT令牌
- 令牌必须设置合理的过期时间
- 敏感操作必须进行二次验证

### 11.2 数据加密
- 密码必须使用bcrypt等安全算法加密存储
- 敏感数据传输必须使用HTTPS
- JWT令牌必须使用安全的签名算法

### 11.3 输入验证
- 所有接口必须进行严格的输入验证
- 防止SQL注入、XSS攻击等安全漏洞
- 限制请求频率，防止恶意攻击

## 12. 测试要求

### 12.1 单元测试
- 所有接口必须编写单元测试
- 单元测试覆盖率不低于80%
- 测试用例包括正常情况和异常情况

### 12.2 集成测试
- 前后端集成测试
- 接口之间的集成测试
- 数据一致性测试

### 12.3 性能测试
- 并发测试
- 响应时间测试
- 压力测试

## 13. 文档维护

- 接口文档必须与实际实现保持一致
- 接口变更必须及时更新文档
- 文档版本管理，记录变更历史

## 14. 附录

### 14.1 数据格式示例
- 日期时间格式：ISO 8601，如2024-01-01T00:00:00Z
- 文件大小：字节数
- 价格：保留两位小数

### 14.2 接口调用示例
```javascript
// 使用axios调用登录接口
import axios from 'axios';

const login = async (username, password) => {
  try {
    const response = await axios.post('/auth/login', {
      username,
      password
    });
    return response.data;
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
};
```

## 15. 联系方式

- 前端负责人：XXX
- 后端负责人：XXX
- 项目管理：XXX

---

**文档版本**: 1.0
**创建日期**: 2025-12-19
**最后更新**: 2025-12-19
**文档审核**: XXX
