# 部署指南

## 文档说明

本文档描述 OfferPilot 当前仓库内已经具备的部署方式、运行依赖和关键环境变量。内容以现有 `deploy/` 目录和后端配置文件为准。

## 当前生产架构

- 前端：独立 Docker 镜像，容器内由 Nginx 提供静态文件
- 后端：独立 Docker 镜像，Spring Boot 提供 API
- Redis：使用 `redis/redis-stack-server`，提供缓存和向量检索能力
- MySQL：业务主数据库
- 反向代理：Nginx 统一代理前端和 `/api/*`

典型路由：

- `/` -> 前端容器
- `/api/` -> 后端容器

> 不要把 OfferPilot 接到缺少 `RediSearch` / `RedisJSON` 的普通 Redis 上。知识库检索依赖 Redis Stack 能力。

## 部署方式一：服务器本地构建

### 前提条件

- Docker 20.10+
- Docker Compose 2.0+

### 步骤

```bash
git clone <repo-url> && cd OfferPilot
cp deploy/.env.example deploy/.env

# 至少填写以下变量
# OFFERPILOT_DB_PASSWORD
# OFFERPILOT_JWT_SECRET
# OFFERPILOT_CORS_ORIGINS
# 如果启用 LLM，再填写 OFFERPILOT_LLM_API_KEY

docker compose --env-file deploy/.env -f deploy/docker-compose.prod.yml up -d
docker compose --env-file deploy/.env -f deploy/docker-compose.prod.yml ps
```

### 服务说明

| 服务 | 说明 |
|------|------|
| `redis` | Redis Stack，负责缓存、限流、向量检索 |
| `backend` | Spring Boot 应用 |
| `frontend` | Nginx 托管的 Vue 静态资源 |

### 健康检查

- Redis：`redis-cli ping`
- Backend：`GET /actuator/health`
- Frontend：访问首页并确认 API 可连通

## 部署方式二：GitHub Actions + GHCR 自动发布

仓库已提供自动发布所需文件：

- `deploy/docker-compose.release.yml`
- `deploy/redeploy.sh`
- `.github/workflows/deploy.yml`

典型流程：

1. 推送代码到 `main`
2. GitHub Actions 构建前后端镜像
3. 推送到 GHCR
4. 服务器执行 `deploy/redeploy.sh`
5. 重建 `redis / backend / frontend`

### 服务器需要保留

- `/srv/offerpilot/deploy/.env`

该文件用于保存线上真实环境变量，不由 Actions 覆盖。

## 关键依赖

### MySQL

- 保存用户、训练、简历、投递、治理等全部业务数据

### Redis Stack

- 保存缓存、验证码、限流、会话辅助状态
- 承担知识库检索相关的向量索引能力

### LLM / Embedding

- LLM 主要用于问答、面试评分、摘要类能力
- Embedding 主要用于知识库向量检索
- 两者都可以通过环境变量独立开关和配置

### 文件存储

- 当前正式实现为统一存储抽象 + 本地文件存储
- 主要承载头像、知识文档、简历和面试语音音频
- 公开访问基路径由 `OFFERPILOT_STORAGE_PUBLIC_BASE_URL` 控制
- MinIO 配置仍为预留项，当前部署默认不依赖它

## 环境变量参考

### 必填项

| 变量 | 说明 |
|------|------|
| `OFFERPILOT_DB_PASSWORD` | 数据库密码 |
| `OFFERPILOT_JWT_SECRET` | JWT 密钥，至少 32 字符 |
| `OFFERPILOT_CORS_ORIGINS` | 前端域名白名单 |
| `OFFERPILOT_LLM_API_KEY` | 启用 LLM 时必填 |

### 数据库

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `OFFERPILOT_DB_HOST` | `127.0.0.1` | MySQL 地址 |
| `OFFERPILOT_DB_PORT` | `3306` | MySQL 端口 |
| `OFFERPILOT_DB_NAME` | `offerpilot` | 数据库名 |
| `OFFERPILOT_DB_USERNAME` | `offerpilot` | 数据库用户 |

### Redis

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `OFFERPILOT_REDIS_HOST` | `127.0.0.1` | Redis 地址 |
| `OFFERPILOT_REDIS_PORT` | `6380` | 宿主机映射端口 |
| `OFFERPILOT_REDIS_PASSWORD` | 空 | Redis 密码 |

### LLM

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `OFFERPILOT_LLM_ENABLED` | `true` 或 `false` | LLM 开关 |
| `OFFERPILOT_LLM_BASE_URL` | `https://api.openai.com/v1` | Chat 网关地址 |
| `OFFERPILOT_LLM_API_KEY` | 空 | Chat 网关密钥 |
| `OFFERPILOT_LLM_MODEL` | `gpt-4.1-mini` | Chat 模型 |
| `OFFERPILOT_LLM_TIMEOUT_SECONDS` | `30` | 超时秒数 |

### Embedding

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `OFFERPILOT_EMBEDDING_ENABLED` | `false` | Embedding 开关 |
| `OFFERPILOT_EMBEDDING_BASE_URL` | 继承 LLM 地址 | Embedding 网关地址 |
| `OFFERPILOT_EMBEDDING_API_KEY` | 继承 LLM 密钥 | Embedding 网关密钥 |
| `OFFERPILOT_EMBEDDING_MODEL` | `text-embedding-3-small` | Embedding 模型 |
| `OFFERPILOT_EMBEDDING_DIMENSIONS` | `1536` | 向量维度 |
| `OFFERPILOT_VECTOR_THRESHOLD` | `0.3` | 相似度阈值 |

### 存储

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `OFFERPILOT_STORAGE_PROVIDER` | `local` | 当前存储实现 |
| `OFFERPILOT_STORAGE_LOCAL_ROOT` | 本地目录 | 本地存储根目录 |
| `OFFERPILOT_STORAGE_PUBLIC_BASE_URL` | `/api/files/public` | 公共访问基路径 |
| `OFFERPILOT_STORAGE_AVATAR_MAX_BYTES` | `2097152` | 头像大小限制 |
| `OFFERPILOT_STORAGE_KNOWLEDGE_MAX_BYTES` | `20971520` | 知识文档大小限制 |
| `OFFERPILOT_STORAGE_INTERVIEW_AUDIO_MAX_BYTES` | `15728640` | 面试音频大小限制 |
| `OFFERPILOT_STORAGE_RESUME_MAX_BYTES` | `10485760` | 简历文件大小限制 |

### HTTP 与镜像

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `OFFERPILOT_HTTP_BIND_HOST` | `127.0.0.1` 或 `0.0.0.0` | 前端绑定地址 |
| `OFFERPILOT_HTTP_PORT` | `8092` 或 `80` | 前端端口 |
| `OFFERPILOT_BACKEND_IMAGE` | `ghcr.io/sreehc/offerpilot-backend:latest` | 后端镜像 |
| `OFFERPILOT_FRONTEND_IMAGE` | `ghcr.io/sreehc/offerpilot-frontend:latest` | 前端镜像 |

## 常见问题

**Q: 为什么必须使用 Redis Stack？**  
A: 知识库检索依赖向量索引能力，普通 Redis 无法提供完整支持。

**Q: 不配置 LLM API Key 会怎样？**  
A: 依赖模型的问答、面试评分和部分摘要能力会不可用，其余页面可继续访问。

**Q: 文件存储现在是对象存储吗？**  
A: 当前正式实现是本地文件存储，接口层已经统一抽象，但部署默认不要求 MinIO。

**Q: 前端访问 API 返回 502 怎么排查？**  
A: 先检查 Nginx 的 `/api/` 转发，再检查后端健康检查和数据库、Redis 连接状态。
