# 部署指南

## 当前生产架构

- 前端：独立 Docker 镜像，容器内由 Nginx 提供静态文件
- 后端：独立 Docker 镜像，Spring Boot 监听宿主机 `127.0.0.1:8080`
- Redis：使用项目专用 `redis/redis-stack-server` 容器，提供 `RediSearch + RedisJSON`
- MySQL：使用宿主机上的宝塔 MySQL，不在 Docker 内重建
- 宝塔 / Nginx：只负责域名、HTTPS 和反向代理

当前线上路由：

- `/` -> `127.0.0.1:8092`
- `/api/` -> `127.0.0.1:8080`

> 不要把 ByteCoach 指向一套缺少 `RediSearch` / `RedisJSON` 的普通 Redis。知识检索和向量索引实现依赖 Redis Stack 能力。

## 方式一：服务器本地构建部署

### 前提条件

- Docker 20.10+
- Docker Compose 2.0+

### 步骤

```bash
git clone <repo-url> && cd ByteCoach
cp deploy/.env.example deploy/.env

# 编辑 deploy/.env，至少填写：
# - BYTECOACH_DB_PASSWORD
# - BYTECOACH_JWT_SECRET
# - BYTECOACH_CORS_ORIGINS
# - 如果启用 LLM，再填写 BYTECOACH_LLM_API_KEY

docker compose --env-file deploy/.env -f deploy/docker-compose.prod.yml up -d
docker compose --env-file deploy/.env -f deploy/docker-compose.prod.yml ps
```

### 服务说明

| 服务 | 镜像 | 端口 | 说明 |
|------|------|------|------|
| redis | redis/redis-stack-server | 6380 -> 6379 | 项目专用 Redis Stack，含 Search/JSON 模块 |
| backend | 自构建 | 127.0.0.1:8080 | Spring Boot 应用，直接访问宿主机 MySQL |
| frontend | 自构建 | 127.0.0.1:8092 | Nginx 静态资源 |

### 数据持久化

- `bytecoach_redis_data` — Redis AOF / 数据文件

### 健康检查

- Redis：`redis-cli ping`
- Backend：`GET /actuator/health`
- Frontend：`wget http://127.0.0.1:80/`

## 方式二：GitHub Actions + GHCR 自动发布

这套方式用于后续自动化发布：

1. `git push origin main`
2. GitHub Actions 构建前后端镜像
3. 推送到 `ghcr.io`
4. Actions 通过 SSH 登录服务器
5. 服务器执行 `deploy/redeploy.sh`
6. 自动拉取新镜像并执行 `docker compose up -d`

对应文件：

- `deploy/docker-compose.release.yml`
- `deploy/redeploy.sh`
- `.github/workflows/deploy.yml`

### GitHub Secrets

在仓库 `Settings -> Secrets and variables -> Actions` 中添加：

| Secret | 说明 |
|------|------|
| `DEPLOY_HOST` | `103.85.227.161` |
| `DEPLOY_PORT` | `22` |
| `DEPLOY_USER` | `root` |
| `DEPLOY_SSH_KEY` | 你的部署私钥内容 |
| `GHCR_USERNAME` | `Sreehc` |
| `GHCR_TOKEN` | GitHub PAT，至少包含 `read:packages` |

### 服务器要求

服务器需要保留：

- `/srv/bytecoach/deploy/.env`

这个文件保存线上真实环境变量，不由 GitHub Actions 覆盖。

首次启用自动发布时，确保服务器已有：

```bash
chmod +x /srv/bytecoach/deploy/redeploy.sh
```

### 服务器发布脚本行为

`deploy/redeploy.sh` 会：

1. 登录 `ghcr.io`
2. 拉取指定 tag 的前后端镜像
3. 用 `deploy/docker-compose.release.yml` 重建 `redis / backend / frontend`
4. 清理无用 dangling 镜像

### 回滚

如果某次发布有问题，可以在服务器执行：

```bash
cd /srv/bytecoach/deploy
IMAGE_TAG=sha-「旧提交 SHA」 GHCR_USERNAME="「GitHub 用户名」" GHCR_TOKEN="「PAT」" ./redeploy.sh
```

也可以在 GitHub Actions 里手工重跑旧提交对应的 workflow。

## Redis Stack 验证

容器启动后，确认模块可用：

```bash
docker compose --env-file deploy/.env -f deploy/docker-compose.prod.yml exec redis redis-cli ping
docker compose --env-file deploy/.env -f deploy/docker-compose.prod.yml exec redis redis-cli MODULE LIST
```

`MODULE LIST` 至少应看到 `search` 和 `ReJSON`。

## 环境变量参考

### 必填项

| 变量 | 说明 |
|------|------|
| `BYTECOACH_DB_PASSWORD` | 业务数据库密码 |
| `BYTECOACH_JWT_SECRET` | JWT 签名密钥，至少 32 字符 |
| `BYTECOACH_CORS_ORIGINS` | CORS 允许的域名 |
| `BYTECOACH_LLM_API_KEY` | 启用 LLM 时才需要 |

### 数据库

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_DB_HOST` | `127.0.0.1` | 宿主机 MySQL 地址 |
| `BYTECOACH_DB_PORT` | `3306` | 宿主机 MySQL 端口 |
| `BYTECOACH_DB_NAME` | `bytecoach` | 数据库名 |
| `BYTECOACH_DB_USERNAME` | `bytecoach` | 数据库用户 |

### Redis

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_REDIS_HOST` | `127.0.0.1` | 后端连接 Redis Stack 的地址 |
| `BYTECOACH_REDIS_PORT` | `6380` | Redis Stack 映射到宿主机的端口 |
| `BYTECOACH_REDIS_PASSWORD` | 空 | Redis Stack 密码 |

### LLM / Embedding

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_LLM_ENABLED` | `false` | LLM 功能开关 |
| `BYTECOACH_LLM_BASE_URL` | `https://api.openai.com/v1` | API 地址 |
| `BYTECOACH_LLM_MODEL` | `gpt-4.1-mini` | 模型名称 |
| `BYTECOACH_EMBEDDING_ENABLED` | `false` | 向量搜索开关 |

### 镜像覆盖

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_BACKEND_IMAGE` | `ghcr.io/sreehc/bytecoach-backend:latest` | 后端镜像 |
| `BYTECOACH_FRONTEND_IMAGE` | `ghcr.io/sreehc/bytecoach-frontend:latest` | 前端镜像 |

## 常见问题

**Q: 推送到 GitHub 后为什么没有自动发布？**  
A: 检查 `.github/workflows/deploy.yml` 是否存在，仓库 Secrets 是否完整，以及 workflow 是否运行在 `main` 分支推送事件上。

**Q: 前端访问 API 返回 502**  
A: 先检查宝塔 / Nginx 是否仍把 `/api/` 转发到 `127.0.0.1:8080`，再检查 Backend 是否已经通过健康检查。

**Q: Redis 连接失败**  
A: 先执行 `docker exec bytecoach-redis redis-cli MODULE LIST`，确认 ByteCoach 使用的是项目专用 `redis/redis-stack-server` 容器，而不是宿主机上的普通 Redis。

**Q: LLM 功能不可用**  
A: 检查 `BYTECOACH_LLM_API_KEY` 是否正确设置。如果不配置 API Key，面试评分和计划生成功能将不可用，其余功能正常。
