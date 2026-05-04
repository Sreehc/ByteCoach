# 部署指南

## 快速部署（Docker Compose）

### 前提条件

- Docker 20.10+
- Docker Compose 2.0+

### 步骤

```bash
# 1. 克隆项目
git clone <repo-url> && cd ByteCoach

# 2. 创建环境变量文件
cp .env.example .env

# 3. 编辑 .env，填写必填项
#    - MYSQL_ROOT_PASSWORD
#    - BYTECOACH_DB_PASSWORD
#    - BYTECOACH_JWT_SECRET（至少 32 字符）
#    - BYTECOACH_CORS_ORIGINS（你的域名）
#    - BYTECOACH_LLM_API_KEY

# 4. 一键启动
docker compose up -d

# 5. 查看状态
docker compose ps

# 6. 访问
#    前端：http://localhost（或 BYTECOACH_HTTP_PORT 指定的端口）
#    后端 API：http://localhost/api/
#    API 文档：http://localhost:8080/doc.html（需直接访问后端端口）
```

### 服务说明

| 服务 | 镜像 | 端口 | 说明 |
|------|------|------|------|
| mysql | mysql:8.0 | 3306（内部） | 数据库，自动执行 init.sql 初始化 |
| redis | redis/redis-stack-server | 6379（内部） | 缓存 + 向量搜索，含 Search/JSON 模块 |
| backend | 自构建 | 8080（内部） | Spring Boot 应用 |
| frontend | 自构建 | 80（对外） | Nginx 静态资源 + API 反向代理 |

### 数据持久化

- `mysql_data` — MySQL 数据文件
- `redis_data` — Redis AOF 持久化文件

### 健康检查

所有服务均配置了健康检查：

- MySQL：`mysqladmin ping`
- Redis：`redis-cli ping`
- Backend：`GET /actuator/health`
- Frontend：`wget http://localhost:80/`

Backend 依赖 MySQL 和 Redis 健康后才启动，Frontend 依赖 Backend 健康后才启动。

---

## 开发环境

### 启动基础设施

```bash
# 仅启动 MySQL + Redis
docker compose up -d mysql redis
```

### 启动后端

```bash
cd backend
# 需要 JDK 17+，Maven 3.8+
mvn spring-boot:run
```

默认连接 `localhost:3306`（MySQL）和 `localhost:6379`（Redis），使用 `dev` profile。

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

访问 `http://localhost:5173`，API 请求通过 Vite 代理转发到后端。

---

## 环境变量参考

### 必填项

| 变量 | 说明 | 示例 |
|------|------|------|
| `MYSQL_ROOT_PASSWORD` | MySQL root 密码 | `strong_root_pw` |
| `BYTECOACH_DB_PASSWORD` | 业务数据库密码 | `strong_db_pw` |
| `BYTECOACH_JWT_SECRET` | JWT 签名密钥（≥32 字符） | `openssl rand -base32 32` |
| `BYTECOACH_CORS_ORIGINS` | CORS 允许的域名 | `https://your-domain.com` |
| `BYTECOACH_LLM_API_KEY` | LLM API Key | `sk-xxx` |

### 数据库

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_DB_NAME` | `bytecoach` | 数据库名 |
| `BYTECOACH_DB_USERNAME` | `bytecoach` | 数据库用户 |

### Redis

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_REDIS_PASSWORD` | 空 | Redis 密码 |

### LLM / AI

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_LLM_ENABLED` | `true` | LLM 功能开关 |
| `BYTECOACH_LLM_BASE_URL` | `https://api.openai.com/v1` | API 地址 |
| `BYTECOACH_LLM_MODEL` | `gpt-4.1-mini` | 模型名称 |
| `BYTECOACH_LLM_TIMEOUT_SECONDS` | `30` | 请求超时 |
| `BYTECOACH_EMBEDDING_ENABLED` | `false` | 向量搜索开关 |
| `BYTECOACH_EMBEDDING_MODEL` | `text-embedding-3-small` | Embedding 模型 |
| `BYTECOACH_EMBEDDING_DIMENSIONS` | `1536` | 向量维度 |

### 端口

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_HTTP_PORT` | `80` | 前端对外端口 |

### 业务参数

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `BYTECOACH_WRONG_THRESHOLD` | `60` | 低于此分自动加入错题本 |
| `BYTECOACH_AI_QUOTA` | `100` | 每用户每日 LLM 调用上限 |
| `BYTECOACH_RATE_LIMIT` | `60` | 每 IP 每分钟最大请求数 |

> 完整的环境变量列表请参考 `.env.example` 和 `CONFIG_GUIDE.md`。

---

## 运维操作

### 查看日志

```bash
docker compose logs -f backend
docker compose logs -f frontend
```

### 重启服务

```bash
docker compose restart backend
```

### 数据库备份

```bash
docker compose exec mysql mysqldump -u root -p bytecoach > backup_$(date +%Y%m%d).sql
```

### 数据库恢复

```bash
docker compose exec -T mysql mysql -u root -p bytecoach < backup_20260501.sql
```

### 更新部署

```bash
git pull
docker compose up -d --build
```

---

## 常见问题

**Q: Backend 启动失败，提示 BYTECOACH_DB_PASSWORD is required**
A: 检查 `.env` 文件是否正确设置了 `BYTECOACH_DB_PASSWORD`。

**Q: 前端访问 API 返回 502**
A: Backend 可能还没启动完成。Backend 有 30 秒的启动等待期（start_period），检查 `docker compose logs backend`。

**Q: Redis 连接失败**
A: 确认使用的是 `redis/redis-stack-server` 镜像（不是普通 redis），它包含 Search 和 JSON 模块。

**Q: LLM 功能不可用**
A: 检查 `BYTECOACH_LLM_API_KEY` 是否正确设置。如果不配置 API Key，面试评分和计划生成功能将不可用，其余功能正常。
