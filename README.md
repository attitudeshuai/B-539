# 博客系统 (Spring Boot + Vue 3)

一个基于 Spring Boot 3 和 Vue 3 构建的完整个人博客系统。

## 🛠 技术栈

### 后端 (Backend)
- **框架**: Spring Boot 3.2.11
- **语言**: Java 21
- **ORM**: MyBatis-Plus
- **数据库**: MySQL 8.x
- **认证**: JWT (JSON Web Token)

### 前端 (Frontend)
- **框架**: Vue 3.5.13
- **构建工具**: Vite 6.0.5
- **UI 组件库**: Element Plus
- **HTTP 客户端**: Axios
- **路由**: Vue Router

## 📂 项目结构

```
blog-system/
├── docker-compose.yml   # Docker 编排文件
├── backend/             # Spring Boot 后端
│   ├── Dockerfile
│   └── src/...
├── frontend/            # Vue 3 Frontend
│   ├── Dockerfile
│   ├── nginx.conf       # Docker 用的 Nginx 代理配置
│   └── src/...
└── db/
    └── init.sql         # 数据库初始化脚本
```

## 🚀 如何运行 (Docker - 推荐)

1. 确保已安装并运行 Docker Desktop。
2. 进入 `blog-system` 目录。
3. 运行以下命令：
   ```bash
   docker compose up -d --build
   ```
4. 等待容器启动 (MySQL 初始化可能需要几秒钟)。

## 🔗 访问地址

- **前端 (公开页 & 管理后台)**: http://localhost:3000
- **后端 API**: http://localhost:8080/api

## 🧪 默认账号

- **管理员用户**:
  - 用户名: `admin`
  - 密码: `123456`

## 💻 本地开发运行 (Development)

### 前置条件
- JDK 21+
- Node.js 20+
- MySQL 8.0+

### 数据库
1. 创建数据库 `blog_db` 并执行 `db/init.sql`。
2. 确保 MySQL 运行在 3306 端口 (用户: root, 密码: root)。

### 后端
```bash
cd backend
mvn spring-boot:run
```
后端运行在 `http://localhost:8080`。

### 前端
```bash
cd frontend
npm install
npm run dev
```
前端运行在 `http://localhost:5173`。

## 📜 功能特性
- **公开页**: 文章列表、详情、搜索。
- **后台管理**: 登录 (JWT)、文章 CRUD、分类 CRUD、标签 CRUD。
- **安全**: 基于 Token 的管理路由认证。
