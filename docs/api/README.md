# 接口文档

> 项目已集成 Knife4j（Swagger），启动后端后访问 `http://localhost:8080/doc.html` 查看交互式接口文档。
>
> 本文档提供当前代码真实路由的速查表，便于前后端协作和联调。

## 通用说明

### 请求格式

- `Content-Type: application/json`，文件上传接口除外
- 认证头：`Authorization: Bearer <jwt_token>`
- 设备标识：`X-Device-Id: <device_id>`，用于设备管理链路

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 常用错误码

| code | 含义 |
|------|------|
| 200 | 成功 |
| 400 | 参数错误或业务校验失败 |
| 401 | 未认证 |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 422 | 参数校验失败 |
| 423 | 账号锁定 |
| 429 | 请求过频 |
| 500 | 服务端异常 |

### 分页参数

| 参数 | 默认值 | 说明 |
|------|--------|------|
| `pageNum` | `1` | 页码 |
| `pageSize` | `20` | 每页条数 |

---

## 认证与账户 `/api/auth`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/auth/register` | 注册并返回登录态 | 公开 |
| POST | `/api/auth/login` | 用户名密码登录 | 公开 |
| POST | `/api/auth/logout` | 登出并拉黑当前 Token | 需认证 |
| GET | `/api/auth/ping` | 健康检查 | 公开 |
| GET | `/api/auth/captcha` | 获取图形验证码 | 公开 |
| POST | `/api/auth/email/send-verification-code` | 发送邮箱验证码 | 需认证 |
| POST | `/api/auth/email/verify` | 验证邮箱验证码 | 需认证 |
| POST | `/api/auth/password/forgot` | 发送找回密码验证码 | 公开 |
| POST | `/api/auth/password/reset` | 通过邮箱验证码重置密码 | 公开 |
| GET | `/api/auth/oauth/providers` | 查询第三方登录入口状态 | 公开 |
| GET | `/api/auth/oauth/github/callback` | GitHub 回调占位路径 | 公开 |
| GET | `/api/auth/login-logs` | 当前用户登录日志 | 需认证 |

## 两步验证 `/api/auth/2fa`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/auth/2fa/status` | 查询 2FA 状态 | 需认证 |
| POST | `/api/auth/2fa/setup` | 初始化 2FA | 需认证 |
| POST | `/api/auth/2fa/enable` | 启用 2FA | 需认证 |
| POST | `/api/auth/2fa/disable` | 关闭 2FA | 需认证 |
| POST | `/api/auth/2fa/verify` | 2FA 登录校验 | 公开 |

## 设备管理 `/api/auth/devices`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/auth/devices` | 设备列表 | 需认证 |
| POST | `/api/auth/devices/{id}/revoke` | 撤销指定设备 | 需认证 |
| POST | `/api/auth/devices/revoke-all` | 撤销其它设备 | 需认证 |

## 用户 `/api/user`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/user/me` | 当前用户信息 | 需认证 |
| POST | `/api/user/avatar` | 上传头像 | 需认证 |

## 首页概览 `/api/dashboard`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/dashboard/overview` | 首页训练概览 | 需认证 |

## 题库 `/api/question`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/question/list` | 分页查询题库 | 需认证 |
| GET | `/api/question/{id}` | 题目详情 | 需认证 |

## 分类 `/api/category`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/category/list` | 分类列表 | 需认证 |

## 知识库 `/api/knowledge`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/knowledge/list` | 系统/通用知识文档列表 | 需认证 |
| POST | `/api/knowledge/search` | 知识检索 | 需认证 |
| POST | `/api/knowledge/upload` | 上传个人知识文档 | 需认证 |
| GET | `/api/knowledge/my` | 我的文档列表 | 需认证 |
| DELETE | `/api/knowledge/{docId}` | 删除我的文档 | 需认证 |

## 问答 `/api/chat`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/chat/send` | 发送问答消息 | 需认证 |
| POST | `/api/chat/stream` | 流式问答 | 需认证 |
| GET | `/api/chat/sessions` | 会话列表 | 需认证 |
| GET | `/api/chat/messages/{sessionId}` | 会话消息 | 需认证 |
| DELETE | `/api/chat/session/{sessionId}` | 删除会话 | 需认证 |

## 模拟面试 `/api/interview`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/interview/start` | 开始面试 | 需认证 |
| GET | `/api/interview/current/{sessionId}` | 当前面试状态 | 需认证 |
| POST | `/api/interview/answer` | 提交文字答案 | 需认证 |
| GET | `/api/interview/detail/{sessionId}` | 面试详情 | 需认证 |
| GET | `/api/interview/history` | 面试历史 | 需认证 |
| GET | `/api/interview/trend` | 面试趋势 | 需认证 |
| POST | `/api/interview/{sessionId}/cards/generate` | 从面试生成卡片 | 需认证 |
| POST | `/api/interview/{sessionId}/cards/activate` | 激活面试生成的卡片 deck | 需认证 |
| GET | `/api/interview/voice/status` | 语音能力状态 | 需认证 |
| POST | `/api/interview/voice/start` | 开始语音面试 | 需认证 |
| POST | `/api/interview/voice/submit` | 提交语音答案 | 需认证 |

## 学习计划 `/api/plan`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/plan/generate` | 生成学习计划 | 需认证 |
| GET | `/api/plan/current` | 当前计划 | 需认证 |
| POST | `/api/plan/task/{taskId}/status` | 更新任务状态 | 需认证 |
| POST | `/api/plan/{planId}/refresh` | 刷新计划 | 需认证 |

## 简历助手 `/api/resume`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/resume/upload` | 上传并解析简历 | 需认证 |
| GET | `/api/resume/list` | 简历列表 | 需认证 |
| GET | `/api/resume/latest` | 最近一份简历 | 需认证 |
| GET | `/api/resume/{resumeId}` | 简历详情 | 需认证 |
| GET | `/api/resume/{resumeId}/project-questions` | 项目追问 | 需认证 |
| GET | `/api/resume/{resumeId}/intro` | 自我介绍建议 | 需认证 |
| GET | `/api/resume/{resumeId}/interview-resume` | 面试简历提纲 | 需认证 |

## 投递管理 `/api/applications`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/applications` | 创建投递记录 | 需认证 |
| GET | `/api/applications/board` | 看板列表 | 需认证 |
| GET | `/api/applications/{applicationId}` | 投递详情 | 需认证 |
| PUT | `/api/applications/{applicationId}/status` | 更新投递状态 | 需认证 |
| POST | `/api/applications/{applicationId}/events` | 新增投递事件 | 需认证 |
| POST | `/api/applications/{applicationId}/analysis` | 重新生成 JD 分析 | 需认证 |

## 卡片强化 `/api/cards`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/cards/task` | 创建卡片任务 | 需认证 |
| POST | `/api/cards/generate` | 生成卡片 | 需认证 |
| GET | `/api/cards/today` | 今日卡片任务 | 需认证 |
| GET | `/api/cards/decks` | deck 列表 | 需认证 |
| POST | `/api/cards/decks/{id}/activate` | 激活 deck | 需认证 |
| GET | `/api/cards/stats` | 卡片统计 | 需认证 |
| POST | `/api/cards/{id}/review` | 卡片评分 | 需认证 |
| GET | `/api/cards/active` | 当前激活 deck | 需认证 |
| GET | `/api/cards/task/{id}` | 任务详情 | 需认证 |
| POST | `/api/cards/task/{id}/start` | 开始任务 | 需认证 |
| POST | `/api/cards/task/{id}/rate` | 任务内评分 | 需认证 |
| POST | `/api/cards/task/{id}/restart` | 重启任务 | 需认证 |

## 错题本 `/api/wrong`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/wrong/list` | 错题列表 | 需认证 |
| GET | `/api/wrong/{id}` | 错题详情 | 需认证 |
| PUT | `/api/wrong/mastery/{id}` | 更新掌握状态 | 需认证 |
| DELETE | `/api/wrong/delete/{id}` | 删除错题 | 需认证 |
| GET | `/api/wrong/export` | 导出错题 Markdown | 需认证 |

## 复习中心 `/api/review`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/review/today` | 今日待复习 | 需认证 |
| POST | `/api/review/{id}/rate` | 提交复习评分 | 需认证 |
| GET | `/api/review/stats` | 复习统计 | 需认证 |

## 数据分析 `/api/analytics`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/analytics/trend` | 趋势分析 | 需认证 |
| GET | `/api/analytics/efficiency` | 效率分析 | 需认证 |
| GET | `/api/analytics/insights` | 学习洞察 | 需认证 |

## 社区 `/api/community`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/community/questions` | 问题列表 | 需认证 |
| GET | `/api/community/questions/{id}` | 问题详情 | 需认证 |
| POST | `/api/community/questions` | 发起提问 | 需认证 |
| PUT | `/api/community/questions` | 编辑问题 | 需认证 |
| DELETE | `/api/community/questions/{id}` | 删除问题 | 需认证 |
| POST | `/api/community/vote` | 投票 | 需认证 |
| GET | `/api/community/leaderboard` | 排行榜 | 需认证 |
| POST | `/api/community/answers` | 提交回答 | 需认证 |
| DELETE | `/api/community/answers/{id}` | 删除回答 | 需认证 |
| POST | `/api/community/answers/{answerId}/accept` | 采纳回答 | 需认证 |

## 通知 `/api/notification`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/notification` | 通知列表 | 需认证 |
| GET | `/api/notification/unread-count` | 未读数量 | 需认证 |
| POST | `/api/notification/read` | 标记已读 | 需认证 |
| POST | `/api/notification/read-all` | 全部已读 | 需认证 |

## 导出 `/api/export`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/export/my-data` | 导出个人数据 | 需认证 |

## 自适应推荐 `/api/recommend`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/recommend/profile` | 能力画像 | 需认证 |
| GET | `/api/recommend/questions` | 推荐题目 | 需认证 |
| GET | `/api/recommend/interview` | 推荐面试 | 需认证 |

## 文件公开访问 `/api/files`

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/files/public/**` | 公开文件访问 | 公开 |

## 管理后台 `/api/admin`

### 用户与概览

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/admin/overview` | 系统概览 | 管理员 |
| GET | `/api/admin/overview/trend` | 概览趋势 | 管理员 |
| GET | `/api/admin/users` | 用户列表 | 管理员 |
| PUT | `/api/admin/users/{id}` | 更新用户 | 管理员 |
| POST | `/api/admin/users/{id}/ban` | 封禁用户 | 管理员 |
| POST | `/api/admin/users/{id}/unban` | 解封用户 | 管理员 |
| GET | `/api/admin/users/{id}/detail` | 用户详情 | 管理员 |
| GET | `/api/admin/login-logs` | 全局登录日志 | 管理员 |

### 内容治理

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| GET | `/api/admin/community/pending` | 待审核内容 | 管理员 |
| POST | `/api/admin/community/{id}/approve` | 审核通过 | 管理员 |
| POST | `/api/admin/community/{id}/reject` | 审核拒绝 | 管理员 |
| POST | `/api/admin/category/add` | 新增分类 | 管理员 |
| PUT | `/api/admin/category/update` | 更新分类 | 管理员 |
| DELETE | `/api/admin/category/delete/{id}` | 删除分类 | 管理员 |
| POST | `/api/admin/question/add` | 新增题目 | 管理员 |
| PUT | `/api/admin/question/update` | 更新题目 | 管理员 |
| DELETE | `/api/admin/question/delete/{id}` | 删除题目 | 管理员 |
| POST | `/api/admin/import/questions` | 批量导入题目 | 管理员 |
| GET | `/api/admin/export/questions` | 导出题库 | 管理员 |
| GET | `/api/admin/export/users` | 导出用户 | 管理员 |

### 文档与 AI 治理

| 方法 | 路径 | 说明 | 鉴权 |
|------|------|------|------|
| POST | `/api/admin/knowledge/import` | 导入内置资料 | 管理员 |
| POST | `/api/admin/knowledge/rechunk/{docId}` | 重新切分文档 | 管理员 |
| POST | `/api/admin/knowledge/reindex/{docId}` | 重建文档索引 | 管理员 |
| GET | `/api/admin/ai-logs` | AI 调用日志分页 | 管理员 |
| GET | `/api/admin/ai-logs/summary` | AI 调用日志摘要 | 管理员 |
| GET | `/api/admin/system-config` | 系统配置列表 | 管理员 |
| PUT | `/api/admin/system-config/{configKey}` | 更新单个系统配置 | 管理员 |
| GET | `/api/admin/interviews` | 面试治理列表 | 管理员 |
| GET | `/api/admin/interviews/summary` | 面试治理摘要 | 管理员 |
