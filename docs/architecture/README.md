# 系统架构设计

## 文档说明

本文档描述 OfferPilot 当前代码已经落地的系统结构、核心依赖与关键数据流，不记录未来规划态架构。

## 整体架构

OfferPilot 采用前后端分离架构，通过 Nginx 统一前端静态资源和后端 API。

```text
浏览器 / 移动端 Web
        |
        v
Nginx / Frontend Container
  - Vue 3 SPA
  - /api/* 反向代理到 Spring Boot
        |
        v
Spring Boot Backend
  - Security / JWT / RBAC
  - Controller / Service / Mapper
  - AI Gateway / Prompt / Governance
        |
        +--> MySQL 8.0
        +--> Redis Stack
        +--> Local File Storage
        +--> OpenAI Compatible LLM / Embedding API
```

## 技术选型

| 层级 | 技术 | 作用 |
|------|------|------|
| 前端 | Vue 3 + TypeScript + Vite | 页面渲染、路由、状态管理 |
| UI | Element Plus + Tailwind CSS | 表单、表格、工作台布局 |
| 后端 | Spring Boot 3.3 | API、业务编排、鉴权 |
| 权限 | Spring Security + JWT | 登录态、角色控制、管理员接口保护 |
| ORM | MyBatis-Plus | CRUD 与分页 |
| 数据库 | MySQL 8.0 | 持久化业务数据 |
| 缓存 / 检索 | Redis Stack | 缓存、限流、向量检索 |
| AI | OpenAI 兼容 API | 问答、面试评分、摘要与 Embedding |
| 文档解析 | Apache Tika | PDF / Word / TXT 等文本抽取 |
| 部署 | Docker + Nginx | 容器化部署与反向代理 |

## 后端模块结构

```text
com.offerpilot
├── admin/         管理后台、AI 日志、系统配置、面试治理
├── ai/            LLM / Embedding 网关、Prompt 模板、调用日志
├── analytics/     趋势、效率、洞察
├── application/   投递看板、JD 分析、事件流
├── auth/          登录、注册、邮箱验证、找回密码、设备、2FA
├── cards/         卡片 deck、今日任务、评分
├── category/      分类管理
├── chat/          普通问答、RAG、会话消息
├── community/     社区问答与投票
├── common/        通用配置、异常、存储、工具
├── dashboard/     首页训练概览
├── data/          导出与批量导入
├── interview/     文字/语音面试、历史、详情、趋势
├── knowledge/     知识文档、切分、索引、检索
├── notification/  通知
├── plan/          学习计划与每日任务
├── question/      题库
├── resume/        简历文件、项目解析、项目追问
├── security/      JWT、限流、鉴权配置
├── user/          用户资料
└── wrong/         错题本与 SM-2 复习
```

模块内部遵循统一结构：

```text
module/
├── controller/
├── dto/
├── entity/
├── mapper/
├── service/
│   └── impl/
└── vo/
```

## 前端信息架构

当前主路由围绕求职训练工作台组织：

```text
/dashboard        首页工作台
/question         题库训练
/knowledge        知识库
/chat             问答
/interview        模拟面试
/study-plan       学习计划
/resume           简历助手
/applications     投递管理
/analytics        数据分析
/admin            管理后台
/cards            卡片强化（辅助）
/review           复习中心（辅助）
/community        社区
/settings         账户设置
```

前端目录结构：

```text
frontend/src/
├── api/          API 封装
├── components/   全局组件
├── layouts/      AppLayout 等布局
├── pages/        按模块划分页面
├── router/       路由配置
├── stores/       Pinia 状态
├── types/        TypeScript 类型
└── utils/        网络与工具函数
```

## 关键数据流

### 1. 知识库与 RAG

```text
上传/导入文档
  -> Tika 解析文本
  -> 切分 knowledge_chunk
  -> Embedding 写入 Redis 向量索引
  -> Chat 检索引用片段
  -> LLM 生成带引用回答
  -> ai_call_log 记录调用摘要
```

### 2. 模拟面试

```text
用户选择模式与题量
  -> 创建 interview_session
  -> 逐题提交答案
  -> LLM 评分 / 点评 / 追问
  -> 写入 interview_record
  -> 低分题进入错题或卡片强化链路
```

### 3. 学习计划

```text
用户生成计划
  -> 汇总弱项、面试记录、训练摘要
  -> 生成 study_plan 与 study_plan_task
  -> 用户逐日打卡
  -> 更新计划进度与任务状态
```

### 4. 简历与投递

```text
上传简历
  -> 文本解析与 resume_file / resume_project 入库
  -> 生成项目追问、自我介绍、面试简历提纲
  -> 创建 job_application
  -> 计算 JD 关键词与匹配摘要
  -> 记录状态流转和事件流
```

### 5. 后台治理

```text
管理员访问 /api/admin/**
  -> ROLE_ADMIN 鉴权
  -> 查看用户/内容/题库/文档/登录日志
  -> 查看 ai_call_log 摘要与明细
  -> 维护 system_config 覆盖项
  -> 查看面试治理摘要与会话列表
```

## 存储与外部依赖

### 文件存储

- 后端通过统一存储抽象处理头像、知识文档、简历和语音音频
- 当前实际实现为本地文件存储
- 文件公开访问通过 `/api/files/public/**` 提供
- MinIO 配置仍为预留项，当前正式部署口径不以 MinIO 为前提

### Redis Stack

- 承担缓存、限流、验证码和设备辅助状态
- 提供向量检索能力，知识库检索依赖 RediSearch / RedisJSON

### AI 网关

- Chat、面试评分、部分摘要能力通过 OpenAI 兼容网关实现
- Embedding 用于知识库切分内容的向量化
- 管理后台可以查看 AI 调用日志与系统配置覆盖值

## 安全边界

- 认证方式：JWT
- 角色模型：`USER` / `ADMIN`
- `/api/admin/**` 由管理员角色保护
- 支持设备撤销、登录日志、图形验证码、邮箱验证和两步验证
- 限流由 Redis 滑动窗口实现
