# 接口文档

> 项目已集成 Knife4j（Swagger），启动后端后访问 `http://localhost:8080/doc.html` 查看自动生成的交互式接口文档。
>
> 本文档提供接口速查表和调用流程说明，便于前后端协作。

## 通用说明

### 请求格式

- Content-Type: `application/json`（文件上传除外）
- 认证: `Authorization: Bearer <jwt_token>`
- 设备标识: `X-Device-Id: <device_id>`（可选，用于设备管理）

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

### 错误码

| code | 含义 | 说明 |
|------|------|------|
| 200 | 成功 | |
| 400 | 请求参数错误 | 表单校验失败、业务规则校验 |
| 401 | 未认证 | Token 缺失或过期 |
| 403 | 无权限 | 角色权限不足 |
| 404 | 资源不存在 | |
| 422 | 校验失败 | @Valid 参数校验 |
| 423 | 账号锁定 | 连续登录失败锁定 |
| 429 | 请求过频 | 触发限流 |
| 500 | 服务器错误 | |

### 分页参数

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| pageNum | int | 1 | 页码 |
| pageSize | int | 20 | 每页条数 |

分页响应：

```json
{
  "records": [],
  "total": 100,
  "pageNum": 1,
  "pageSize": 20,
  "totalPages": 5
}
```

---

## 接口速查表

### 认证 `/api/auth`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/auth/register` | 注册 | 公开 |
| POST | `/api/auth/login` | 登录 | 公开 |
| POST | `/api/auth/logout` | 登出 | 需认证 |
| GET | `/api/auth/ping` | 健康检查 | 公开 |
| GET | `/api/auth/captcha` | 获取图形验证码 | 公开 |
| GET | `/api/auth/devices` | 已登录设备列表 | 需认证 |
| POST | `/api/auth/devices/{id}/revoke` | 撤销指定设备 | 需认证 |
| POST | `/api/auth/devices/revoke-all` | 撤销其他设备 | 需认证 |
| GET | `/api/auth/login-logs` | 个人登录日志 | 需认证 |
| GET | `/api/auth/2fa/status` | 两步验证状态 | 需认证 |
| POST | `/api/auth/2fa/setup` | 初始化 2FA | 需认证 |
| POST | `/api/auth/2fa/enable` | 启用 2FA | 需认证 |
| POST | `/api/auth/2fa/disable` | 关闭 2FA | 需认证 |
| POST | `/api/auth/2fa/verify` | 2FA 登录验证 | 公开 |

### 用户 `/api/user`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/user/me` | 当前用户信息 | 需认证 |
| POST | `/api/user/avatar` | 上传头像 | 需认证 |

### 智能问答 `/api/chat`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/chat/sessions` | 创建会话 | 需认证 |
| GET | `/api/chat/sessions` | 会话列表 | 需认证 |
| GET | `/api/chat/sessions/{id}/messages` | 会话消息 | 需认证 |
| POST | `/api/chat/send` | 发送消息 | 需认证 |
| DELETE | `/api/chat/sessions/{id}` | 删除会话 | 需认证 |

### 模拟面试 `/api/interview`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/interview/start` | 开始面试 | 需认证 |
| POST | `/api/interview/answer` | 提交答案 | 需认证 |
| GET | `/api/interview/history` | 面试历史 | 需认证 |
| GET | `/api/interview/{id}` | 面试详情 | 需认证 |
| POST | `/api/interview/voice/start` | 开始语音面试 | 需认证 |
| POST | `/api/interview/voice/submit` | 提交语音答案 | 需认证 |

### 错题本 `/api/wrong`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/wrong` | 错题列表 | 需认证 |
| PUT | `/api/wrong/{id}/mastery` | 更新掌握程度 | 需认证 |
| DELETE | `/api/wrong/{id}` | 移除错题 | 需认证 |

### 间隔复习 `/api/review`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/review/today` | 今日待复习 | 需认证 |
| POST | `/api/review/rate` | 提交复习评分 | 需认证 |
| GET | `/api/review/stats` | 复习统计 | 需认证 |

### 学习计划 `/api/plan`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/plan/generate` | 生成计划 | 需认证 |
| GET | `/api/plan/current` | 当前计划 | 需认证 |
| PUT | `/api/plan/tasks/{id}/status` | 更新任务状态 | 需认证 |
| POST | `/api/plan/adjust` | 调整计划 | 需认证 |

### 知识库 `/api/knowledge`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/knowledge/docs` | 文档列表 | 需认证 |
| GET | `/api/knowledge/search` | 向量搜索 | 需认证 |
| GET | `/api/knowledge/docs/{id}` | 文档详情 | 需认证 |

### 题库 `/api/question`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/question` | 题目列表（分页） | 需认证 |
| GET | `/api/question/{id}` | 题目详情 | 需认证 |

### 分类 `/api/category`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/category` | 分类列表 | 需认证 |

### 社区 `/api/community`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/community/questions` | 问题列表 | 需认证 |
| POST | `/api/community/questions` | 发起提问 | 需认证 |
| PUT | `/api/community/questions` | 编辑问题 | 需认证 |
| DELETE | `/api/community/questions/{id}` | 删除问题 | 需认证 |
| GET | `/api/community/questions/{id}` | 问题详情 | 需认证 |
| POST | `/api/community/answers` | 提交回答 | 需认证 |
| DELETE | `/api/community/answers/{id}` | 删除回答 | 需认证 |
| POST | `/api/community/answers/{id}/accept` | 采纳回答 | 需认证 |
| POST | `/api/community/vote` | 投票 | 需认证 |
| GET | `/api/community/leaderboard` | 排行榜 | 需认证 |

### 通知 `/api/notification`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/notification` | 通知列表 | 需认证 |
| GET | `/api/notification/unread-count` | 未读数 | 需认证 |
| POST | `/api/notification/read` | 标记已读 | 需认证 |
| POST | `/api/notification/read-all` | 全部已读 | 需认证 |

### 数据分析 `/api/analytics`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/analytics/trend` | 趋势分析 | 需认证 |
| GET | `/api/analytics/efficiency` | 效率分析 | 需认证 |
| GET | `/api/analytics/insights` | 学习洞察 | 需认证 |

### 自适应推荐 `/api/recommend`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/recommend/profile` | 能力画像 | 需认证 |
| GET | `/api/recommend/interview` | 推荐面试 | 需认证 |
| GET | `/api/recommend/questions` | 推荐题目 | 需认证 |

### Dashboard `/api/dashboard`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/dashboard/overview` | 学习概览 | 需认证 |

### 个人数据导出 `/api/export`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/export/my-data` | 导出个人数据（Excel） | 需认证 |

### 管理后台 `/api/admin`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/admin/users` | 用户列表 | 管理员 |
| PUT | `/api/admin/users/{id}` | 编辑用户 | 管理员 |
| POST | `/api/admin/users/{id}/ban` | 封禁用户 | 管理员 |
| POST | `/api/admin/users/{id}/unban` | 解封用户 | 管理员 |
| GET | `/api/admin/users/{id}/detail` | 用户详情 | 管理员 |
| GET | `/api/admin/community/pending` | 待审核列表 | 管理员 |
| POST | `/api/admin/community/{id}/approve` | 审核通过 | 管理员 |
| POST | `/api/admin/community/{id}/reject` | 审核拒绝 | 管理员 |
| GET | `/api/admin/overview` | 系统概览 | 管理员 |
| GET | `/api/admin/overview/trend` | 趋势数据 | 管理员 |
| GET | `/api/admin/login-logs` | 全局登录日志 | 管理员 |
| GET | `/api/admin/export/questions` | 导出题库 | 管理员 |
| GET | `/api/admin/export/users` | 导出用户 | 管理员 |
| POST | `/api/admin/import/questions` | 批量导入题目 | 管理员 |
| POST | `/api/admin/category/add` | 新增分类 | 管理员 |
| PUT | `/api/admin/category/update` | 编辑分类 | 管理员 |
| DELETE | `/api/admin/category/delete/{id}` | 删除分类 | 管理员 |
| POST | `/api/admin/question/add` | 新增题目 | 管理员 |
| PUT | `/api/admin/question/update` | 编辑题目 | 管理员 |
| DELETE | `/api/admin/question/delete/{id}` | 删除题目 | 管理员 |
| POST | `/api/admin/knowledge/import-seed` | 导入内置资料 | 管理员 |
| POST | `/api/admin/knowledge/{id}/rechunk` | 重新切分 | 管理员 |
| POST | `/api/admin/knowledge/{id}/reindex` | 重建索引 | 管理员 |

---

## 核心流程

### 登录流程

```
POST /api/auth/login
├─ 检查账号锁定（Redis login:lock:{userId}）
├─ 检查图形验证码（Redis captcha:{key}，连续失败 3 次后）
├─ AuthenticationManager 验证凭据
├─ 写入 login_log（成功/失败）
├─ 失败计数（Redis login:fail:{userId}，5 次锁定）
├─ 检查 2FA
│  ├─ 未启用 → 签发 JWT + 记录设备 → 返回 token
│  └─ 已启用 → 生成 tempToken（Redis 2fa:temp:{uuid}，5 分钟）
│              → 返回 requires2fa: true + tempToken
└─ 前端根据 requires2fa 决定跳转
   └─ POST /api/auth/2fa/verify（tempToken + code）→ 返回正式 JWT
```

### 面试流程

```
POST /api/interview/start
├─ 选择方向、题数、模式（text/voice）
├─ 从题库随机选题
├─ 创建 interview_session
└─ 返回第一题

POST /api/interview/answer
├─ 用户提交答案
├─ LLM 评分（如果启用）
├─ 创建 interview_record
├─ 如果分数 < 60 → 自动加入错题本（wrong_question）
├─ 更新 session 进度
└─ 返回评分 + 标准答案 + 是否有下一题

GET /api/interview/{id}
└─ 返回完整的面试详情（所有题目和答案）
```

### 间隔复习流程

```
SM-2 算法参数：
- ease_factor (EF): 初始 2.50，最低 1.30
- interval_days: 当前复习间隔（天）
- streak: 连续成功次数（rating >= 3）

POST /api/review/rate
├─ 用户评分 1-4（1=重来 2=困难 3=良好 4=简单）
├─ 记录 review_log（EF 变化、间隔变化）
├─ 重新计算 EF 和 interval_days
│  ├─ rating < 3 → streak 归零，interval 重置为 1
│  └─ rating >= 3 → streak++，interval 按 SM-2 公式增长
├─ 更新 next_review_date
└─ 如果全部掌握 → mastery_level = 'mastered'
```
