# ByteCoach

ByteCoach 是一个面向 Java 后端学习与面试准备的智能学习系统。一期目标是跑通 `登录 -> 问答/面试 -> 错题 -> 学习计划 -> Dashboard` 的最小可展示闭环，并保留后续 `RAG`、管理端和 AI 能力扩展位。

## 技术栈

- 后端：`Spring Boot 3`、`Java 17`、`MyBatis-Plus`、`MySQL`、`Redis`、`Spring Security`、`JWT`、`Knife4j/Swagger`
- 前端：`Vue 3`、`Vite`、`TypeScript`、`Pinia`、`Vue Router`、`Axios`、`Element Plus`、`Tailwind CSS`
- AI：单一 `OpenAI-compatible` 网关抽象，阶段 1 接真实 `LLM`，不强制接入 `Qdrant`

## 目录结构

```text
ByteCoach
├── backend/   Spring Boot 单体后端
├── frontend/  Vue 3 前端
├── docs/      需求、架构、接口、数据库文档
├── sql/       初始化脚本
└── deploy/    本地依赖与部署样例
```

## 一期冻结范围

- 保留：登录注册、Dashboard、Chat、Knowledge、Interview、Wrong、Plan、Question/Category 管理入口
- 不做：用户上传资料、长期记忆、多 Agent、代码沙箱、复杂追问状态机、复杂画像系统、完整 RAG 优化平台
- 后台形态：同一个前端应用内嵌 `/admin` 路由，不拆独立 `admin-web`

## 环境要求

- `JDK 17+`
- `Maven 3.9+`
- `Node.js 20+`
- `MySQL 8.x`
- `Redis 7.x`

## 快速启动

### 1. 初始化数据库

执行 [sql/init.sql](/Users/cheers/Desktop/workspace/ByteCoach/sql/init.sql)。

### 2. 启动本地依赖

如果本地没有 `MySQL/Redis`，可以使用 [deploy/docker-compose.yml](/Users/cheers/Desktop/workspace/ByteCoach/deploy/docker-compose.yml)：

```bash
cd deploy
docker compose up -d
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

默认端口：`8080`

Swagger / Knife4j：

- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- [http://localhost:8080/doc.html](http://localhost:8080/doc.html)

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

默认端口：`5173`

初始化数据默认账号：

- 用户名：`demo`
- 密码：`123456`

## 环境变量

### 后端

使用 [backend/src/main/resources/application-dev.yml](/Users/cheers/Desktop/workspace/ByteCoach/backend/src/main/resources/application-dev.yml) 中的默认配置，或通过环境变量覆盖：

- `BYTECOACH_DB_HOST`
- `BYTECOACH_DB_PORT`
- `BYTECOACH_DB_NAME`
- `BYTECOACH_DB_USERNAME`
- `BYTECOACH_DB_PASSWORD`
- `BYTECOACH_REDIS_HOST`
- `BYTECOACH_REDIS_PORT`
- `BYTECOACH_REDIS_PASSWORD`
- `BYTECOACH_JWT_SECRET`
- `BYTECOACH_LLM_ENABLED`
- `BYTECOACH_LLM_BASE_URL`
- `BYTECOACH_LLM_API_KEY`
- `BYTECOACH_LLM_MODEL`

### 前端

使用 [frontend/.env.development](/Users/cheers/Desktop/workspace/ByteCoach/frontend/.env.development)：

- `VITE_API_BASE_URL`
- `VITE_APP_TITLE`

## 当前实现状态

- 已完成：monorepo 结构、后端公共能力、`JWT` 鉴权骨架、`LLM gateway` 接口层、前端路由与登录态骨架、`/admin` 路由壳、初始化 SQL、部署样例
- 当前接口大多为占位实现，便于后续按既定顺序接入数据库和真实业务逻辑

## 推荐开发顺序

1. `Auth/User`
2. `Category/Question/Knowledge`
3. `Dashboard`
4. `Chat`
5. `Interview`
6. `Wrong`
7. `Plan`
8. `/admin` 最小管理闭环

## 相关文档

- [项目规划与功能评审](/Users/cheers/Desktop/workspace/ByteCoach/docs/01-项目规划与功能评审.md)
- [MVP 范围与迭代规划](/Users/cheers/Desktop/workspace/ByteCoach/docs/02-MVP范围与迭代规划.md)
- [系统架构设计](/Users/cheers/Desktop/workspace/ByteCoach/docs/03-系统架构设计.md)
- [数据库设计](/Users/cheers/Desktop/workspace/ByteCoach/docs/04-数据库设计.md)
- [接口设计](/Users/cheers/Desktop/workspace/ByteCoach/docs/05-接口设计.md)
