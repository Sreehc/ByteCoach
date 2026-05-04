# 系统架构设计

## 整体架构

ByteCoach 采用前后端分离架构，通过 Nginx 反向代理将前端静态资源和后端 API 统一在一个域名下。

```
┌─────────────────────────────────────────────────────┐
│                    Nginx (:80)                       │
│  ┌──────────────┐    ┌──────────────────────────┐   │
│  │  静态资源     │    │  /api/* → backend:8080   │   │
│  │  (Vue SPA)   │    │  (反向代理)               │   │
│  └──────────────┘    └──────────────────────────┘   │
└─────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────┐
│              Spring Boot Backend (:8080)             │
│                                                     │
│  ┌──────────────────────────────────────────────┐   │
│  │  Security Layer (JWT + RBAC)                 │   │
│  │  ├─ JwtAuthenticationFilter                  │   │
│  │  ├─ RateLimitInterceptor                     │   │
│  │  └─ RequestLoggingInterceptor                │   │
│  └──────────────────────────────────────────────┘   │
│                      │                              │
│  ┌──────────────────────────────────────────────┐   │
│  │  Controller Layer                            │   │
│  │  auth / admin / chat / interview / plan /    │   │
│  │  community / knowledge / question / ...      │   │
│  └──────────────────────────────────────────────┘   │
│                      │                              │
│  ┌──────────────────────────────────────────────┐   │
│  │  Service Layer                               │   │
│  │  业务逻辑 + LLM 调用 + 定时任务              │   │
│  └──────────────────────────────────────────────┘   │
│                      │                              │
│  ┌────────────┐  ┌────────────┐  ┌──────────────┐  │
│  │   MySQL    │  │   Redis    │  │  LLM API     │  │
│  │  (持久化)  │  │  (缓存/    │  │  (OpenAI     │  │
│  │            │  │   向量搜索) │  │   兼容)      │  │
│  └────────────┘  └────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────┘
```

## 技术选型

| 层级 | 技术 | 版本 | 选型理由 |
|------|------|------|----------|
| 后端框架 | Spring Boot | 3.3.5 | Java 生态主流，开箱即用 |
| 安全框架 | Spring Security | 6.x | 与 Spring Boot 深度集成 |
| ORM | MyBatis-Plus | 3.5.7 | 简化 CRUD，保留 SQL 灵活性 |
| 数据库 | MySQL | 8.0 | 成熟稳定，社区生态丰富 |
| 缓存 | Redis Stack | 7.x | 含 Search/JSON 模块，支持向量搜索 |
| AI/LLM | OpenAI 兼容 API | — | 支持任意 OpenAI 格式提供商 |
| 前端框架 | Vue 3 | 3.5 | Composition API + TypeScript 友好 |
| UI 组件 | Element Plus | 2.8 | 企业级 UI 组件库 |
| 样式 | Tailwind CSS | 3.4 | 原子化 CSS，开发效率高 |
| 构建工具 | Vite | 5.4 | 极速 HMR，ESBuild 预构建 |
| 图表 | ECharts | 5.5 | 功能丰富的可视化库 |
| Excel | EasyExcel | 3.3 | 阿里开源，内存友好 |
| 部署 | Docker + Nginx | — | 标准容器化方案 |

## 后端模块结构

```
com.bytecoach
├── adaptive/          # 自适应推荐（能力画像、智能推荐）
├── admin/             # 管理后台（用户管理、内容审核、系统概览、数据导出导入）
├── ai/                # AI 基础设施（LLM 网关、Embedding 网关、Prompt 模板）
├── analytics/         # 数据分析（趋势、效率、时段）
├── auth/              # 认证（登录/注册、设备管理、登录日志、两步验证、验证码）
├── category/          # 分类管理
├── chat/              # 智能问答（普通问答 + RAG）
├── common/            # 公共模块（Result、异常、配置、拦截器）
├── community/         # 学习社区（提问、回答、投票、排行榜）
├── dashboard/         # 学习概览
├── data/              # 数据管理（导出/导入）
├── interview/         # 模拟面试（文字 + 语音）
├── knowledge/         # 知识库（文档管理、切分、向量化）
├── notification/      # 通知系统
├── plan/              # 学习计划
├── question/          # 题库管理
├── security/          # 安全基础设施（JWT、CORS、SecurityConfig）
├── user/              # 用户管理
└── wrong/             # 错题本 + 间隔复习（SM-2）
```

每个模块遵循统一的分层结构：

```
module/
├── controller/    # REST 接口
├── dto/           # 请求/响应对象
├── entity/        # 数据库实体（MyBatis-Plus）
├── mapper/        # 数据访问层
├── service/       # 业务接口
│   └── impl/      # 业务实现
├── vo/            # 视图对象
└── scheduler/     # 定时任务（如有）
```

## 前端结构

```
frontend/src/
├── api/            # API 调用封装（axios）
├── components/     # 全局公共组件（NavRail、AppShellHeader 等）
├── composables/    # 组合式函数（useFormRules、useTheme）
├── directives/     # 自定义指令（lazy loading）
├── layouts/        # 布局组件（AppLayout）
├── pages/          # 页面组件（按功能模块分目录）
├── router/         # 路由配置
├── stores/         # Pinia 状态管理
├── styles/         # 全局样式
├── types/          # TypeScript 类型定义
└── utils/          # 工具函数（http、storage、device）
```

## 请求处理流程

```
前端发起请求
    │
    ▼
axios 拦截器（自动附加 Authorization + X-Device-Id）
    │
    ▼
Nginx 反向代理（/api/* → backend:8080）
    │
    ▼
JwtAuthenticationFilter（解析 Token、校验黑名单、检查设备撤销）
    │
    ▼
RateLimitInterceptor（Redis 滑动窗口限流）
    │
    ▼
Controller → Service → Mapper → MySQL/Redis
    │
    ▼
统一响应格式：{ code, message, data }
```

## 安全架构

### 认证流程

```
1. 用户提交用户名 + 密码
2. 后端校验账号锁定状态（Redis）
3. 后端校验图形验证码（Redis，连续失败 3 次后触发）
4. Spring Security AuthenticationManager 验证凭据
5. 检查用户是否启用 2FA
   ├─ 未启用 → 签发 JWT，返回 token
   └─ 已启用 → 返回 requires2fa + tempToken（Redis，5 分钟 TTL）
6. 前端跳转 2FA 验证页，用户输入 TOTP 码或恢复码
7. 验证通过 → 签发正式 JWT
```

### 权限模型

| 角色 | 权限范围 |
|------|----------|
| USER | 个人学习功能、社区互动、个人设置 |
| ADMIN | 所有 USER 权限 + 管理后台（用户管理、内容审核、系统概览、数据导出导入） |

### 安全防护

- **JWT Token**：HMAC-SHA256 签名，24 小时有效期
- **Token 黑名单**：登出时将 Token 加入 Redis 黑名单
- **设备指纹**：浏览器指纹 + Redis 设备撤销检查
- **登录锁定**：5 分钟内失败 5 次锁定 30 分钟
- **图形验证码**：连续失败 3 次后触发
- **两步验证**：TOTP（SHA1，6 位，30 秒窗口）+ 8 个恢复码
- **异地检测**：IP 地理位置解析，城市变化发送通知
- **限流**：Redis 滑动窗口，每 IP 每分钟 60 次
- **CORS**：可配置的跨域白名单
- **XSS**：Vue 模板自动转义 + DOMPurify（富文本场景）
