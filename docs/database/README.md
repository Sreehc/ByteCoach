# 数据库设计

> 完整建表语句：`sql/init.sql`
> 初始化数据：`sql/initdata.sql`
>
> 字符集：utf8mb4，排序规则：utf8mb4_unicode_ci

## 表清单

共 27 张表，按功能模块分组：

| 模块 | 表名 | 说明 |
|------|------|------|
| 用户 | user | 用户账号 |
| 认证 | login_device | 登录设备 |
| 认证 | login_log | 登录日志 |
| 分类 | category | 分类体系 |
| 题库 | question | 题目 |
| 面试 | interview_session | 面试会话 |
| 面试 | interview_record | 面试记录（每题） |
| 面试 | voice_record | 语音记录 |
| 错题 | wrong_question | 错题（含 SM-2 字段） |
| 复习 | review_log | 复习记录 |
| 计划 | study_plan | 学习计划 |
| 计划 | study_plan_task | 计划任务 |
| 简历 | resume_file | 简历文件 |
| 简历 | resume_project | 简历项目 |
| 投递 | job_application | 岗位投递主记录 |
| 投递 | job_application_event | 投递事件流 |
| AI 治理 | ai_call_log | AI 调用日志 |
| AI 治理 | system_config | 系统配置与提示词覆盖 |
| 问答 | chat_session | 聊天会话 |
| 问答 | chat_message | 聊天消息 |
| 知识 | knowledge_doc | 知识文档 |
| 知识 | knowledge_chunk | 知识分片 |
| 社区 | community_question | 社区问题 |
| 社区 | community_answer | 社区回答 |
| 社区 | community_vote | 社区投票 |
| 统计 | user_stats | 用户统计 |
| 通知 | notification | 通知 |

---

## 核心表结构

### user（用户表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| username | VARCHAR(64) UNIQUE | 用户名 |
| password | VARCHAR(255) | BCrypt 加密密码 |
| nickname | VARCHAR(64) | 昵称 |
| avatar | VARCHAR(255) | 头像 URL |
| email | VARCHAR(128) | 邮箱 |
| role | VARCHAR(32) | 角色：USER / ADMIN |
| status | TINYINT | 状态：1=正常, 0=封禁 |
| totp_secret | VARCHAR(64) | TOTP 密钥（AES 加密） |
| totp_enabled | TINYINT(1) | 是否启用两步验证 |
| recovery_codes | TEXT | 恢复码 JSON（AES 加密） |
| last_login_time | DATETIME | 最后登录时间 |

### question（题目表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| title | VARCHAR(255) | 题目标题 |
| category_id | BIGINT | 分类 ID |
| difficulty | VARCHAR(32) | 难度：easy / medium / hard |
| tags | VARCHAR(255) | 标签（逗号分隔） |
| standard_answer | TEXT | 标准答案 |
| score_standard | TEXT | 评分标准 |

### wrong_question（错题表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| user_id | BIGINT | 用户 ID |
| question_id | BIGINT | 题目 ID |
| source_type | VARCHAR(32) | 来源：interview / chat |
| mastery_level | VARCHAR(32) | 掌握程度：not_started / reviewing / mastered |
| ease_factor | DECIMAL(4,2) | SM-2 EF 值（初始 2.50，最低 1.30） |
| interval_days | INT | 当前复习间隔（天） |
| next_review_date | DATE | 下次复习日期 |
| streak | INT | 连续成功次数 |

UNIQUE KEY (user_id, question_id) — 每个用户每道题只有一条错题记录。

### review_log（复习记录表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| user_id | BIGINT | 用户 ID |
| wrong_question_id | BIGINT | 错题 ID |
| rating | INT | 评分 1-4（1=重来 2=困难 3=良好 4=简单） |
| response_time_ms | INT | 复习耗时（毫秒） |
| ease_factor_before | DECIMAL(4,2) | 复习前 EF |
| interval_before | INT | 复习前间隔 |
| ease_factor_after | DECIMAL(4,2) | 复习后 EF |
| interval_after | INT | 复习后间隔 |

### study_plan（学习计划表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| user_id | BIGINT | 用户 ID |
| title | VARCHAR(128) | 计划标题 |
| duration_days | INT | 计划周期：7 / 14 / 30 |
| focus_direction | VARCHAR(64) | 重点方向 |
| target_role | VARCHAR(128) | 目标岗位 |
| tech_stack | VARCHAR(255) | 技术范围 |
| weak_points | VARCHAR(255) | 弱项标签（逗号分隔） |
| review_suggestion | VARCHAR(500) | 复盘建议 |
| status | VARCHAR(32) | 状态：active / completed / archived |
| start_date | DATE | 开始日期 |
| end_date | DATE | 结束日期 |
| current_day | INT | 当前执行天数 |
| progress_rate | DECIMAL(5,2) | 完成进度百分比 |

### study_plan_task（学习计划任务表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| plan_id | BIGINT | 所属计划 ID |
| user_id | BIGINT | 用户 ID |
| day_index | INT | 第几天任务 |
| task_date | DATE | 任务日期 |
| module | VARCHAR(32) | 模块：question / chat / review / interview |
| title | VARCHAR(128) | 任务标题 |
| description | VARCHAR(500) | 任务说明 |
| action_path | VARCHAR(128) | 前端跳转路径 |
| estimated_minutes | INT | 预估时长 |
| priority | VARCHAR(16) | 优先级：high / medium / low |
| status | VARCHAR(32) | 状态：pending / completed |
| completed_at | DATETIME | 完成时间 |

### resume_file（简历文件表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| user_id | BIGINT | 用户 ID |
| title | VARCHAR(128) | 简历标题 |
| file_url | VARCHAR(255) | 存储地址 |
| file_type | VARCHAR(16) | 文件类型：pdf / doc / docx |
| parse_status | VARCHAR(32) | 解析状态：pending / parsed / failed |
| raw_text | LONGTEXT | 提取后的全文 |
| summary | VARCHAR(500) | 解析摘要 |
| skills | VARCHAR(255) | 技能标签（逗号分隔） |
| education | VARCHAR(500) | 教育背景摘要 |
| self_intro | TEXT | 推荐自我介绍 |
| interview_resume_text | TEXT | 面试版简历提纲 |

### resume_project（简历项目表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| resume_file_id | BIGINT | 所属简历 ID |
| user_id | BIGINT | 用户 ID |
| project_name | VARCHAR(128) | 项目名称 |
| role_name | VARCHAR(128) | 角色 / 岗位描述 |
| tech_stack | VARCHAR(255) | 项目技术栈 |
| responsibility | TEXT | 主要职责 |
| achievement | TEXT | 结果与成果 |
| project_summary | VARCHAR(500) | 项目摘要 |
| follow_up_questions_json | JSON | 项目追问列表 |
| risk_hints | VARCHAR(500) | 风险提示（逗号分隔） |

### job_application（岗位投递表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| user_id | BIGINT | 用户 ID |
| resume_file_id | BIGINT | 绑定简历 ID |
| company | VARCHAR(128) | 公司名称 |
| job_title | VARCHAR(128) | 岗位名称 |
| city | VARCHAR(64) | 城市 |
| source | VARCHAR(64) | 渠道来源 |
| jd_text | TEXT | JD 原文 |
| status | VARCHAR(32) | 状态：saved / applied / written / interview / offer / rejected |
| match_score | DECIMAL(5,2) | 简历与 JD 匹配度 |
| jd_keywords | VARCHAR(500) | 识别出的 JD 关键词（逗号分隔） |
| missing_keywords | VARCHAR(500) | 待补关键词（逗号分隔） |
| analysis_summary | VARCHAR(1000) | JD 分析摘要 |
| apply_date | DATE | 实际投递日期 |
| next_step_date | DATE | 下一节点日期 |

### job_application_event（投递事件表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| application_id | BIGINT | 所属投递 ID |
| user_id | BIGINT | 用户 ID |
| event_type | VARCHAR(32) | 事件类型：status_change / interview / review / note / analysis |
| title | VARCHAR(255) | 事件标题 |
| content | TEXT | 事件内容 |
| event_time | DATETIME | 事件发生时间 |
| result | VARCHAR(255) | 结果摘要 |

### ai_call_log（AI 调用日志表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| user_id | BIGINT | 触发用户 ID |
| provider | VARCHAR(64) | 模型服务商 / 网关标识 |
| model | VARCHAR(128) | 模型名称 |
| call_type | VARCHAR(32) | 调用类型：chat / stream / embedding |
| scene | VARCHAR(128) | 业务场景，例如 `chat.answer.rag` |
| input_tokens | INT | 估算输入 token |
| output_tokens | INT | 估算输出 token |
| latency_ms | BIGINT | 调用耗时（毫秒） |
| success | TINYINT | 是否成功：1 / 0 |
| error_message | VARCHAR(1000) | 失败原因摘要 |

### system_config（系统配置表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| config_group | VARCHAR(64) | 配置分组：llm / embedding / prompt |
| config_key | VARCHAR(128) UNIQUE | 配置键 |
| config_value | TEXT | 配置值 |
| value_type | VARCHAR(32) | 值类型：text / number / boolean / textarea |
| description | VARCHAR(500) | 配置说明 |
| enabled | TINYINT | 是否启用：1 / 0 |

### login_device（登录设备表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| user_id | BIGINT | 用户 ID |
| device_fingerprint | VARCHAR(128) | 浏览器/设备指纹 |
| device_name | VARCHAR(128) | 设备名（如 Chrome on Windows） |
| ip | VARCHAR(64) | IP 地址 |
| city | VARCHAR(64) | IP 归属城市 |
| status | TINYINT | 1=活跃, 0=已撤销 |

### login_log（登录日志表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 雪花 ID |
| user_id | BIGINT | 用户 ID |
| ip | VARCHAR(64) | IP 地址 |
| city | VARCHAR(64) | IP 归属城市 |
| device | VARCHAR(128) | 设备名称 |
| status | TINYINT | 1=成功, 0=失败 |
| fail_reason | VARCHAR(128) | 失败原因 |

### community_question / community_answer（社区表）

| 字段 | 类型 | 说明 |
|------|------|------|
| status | VARCHAR(32) | 问题/回答状态：pending / approved / rejected |

发布后默认 `pending`，管理员审核通过后变为 `approved` 才对外可见。

---

## 索引策略

### 常用查询索引

- `wrong_question(user_id, next_review_date)` — 每日复习查询
- `notification(user_id, is_read)` — 未读通知查询
- `notification(user_id, create_time DESC)` — 通知列表排序
- `login_log(user_id)` + `login_log(create_time)` — 登录日志查询
- `community_question(status)` — 审核队列查询

### 唯一约束

- `user(username)` — 用户名唯一
- `wrong_question(user_id, question_id)` — 每用户每题唯一错题
- `community_vote(user_id, target_type, target_id)` — 每用户对每个目标只能投票一次

---

## Redis 使用

| Key 模式 | 用途 | TTL |
|----------|------|-----|
| `jwt:blacklist:{token}` | 登出 Token 黑名单 | Token 剩余有效期 |
| `device:token:{userId}:{fingerprint}` | 设备撤销黑名单 | 7 天 |
| `login:lock:{userId}` | 登录锁定 | 30 分钟 |
| `login:fail:{userId}` | 连续失败计数 | 5 分钟 |
| `captcha:{key}` | 图形验证码 | 5 分钟 |
| `2fa:setup:{userId}` | 2FA 设置临时密钥 | 10 分钟 |
| `2fa:temp:{token}` | 2FA 登录临时 Token | 5 分钟 |
| `offerpilot_chunks:{vectorId}` | 知识库向量 | 持久 |
| `{各种}:cache:*` | 业务缓存 | 按配置 |

---

## SM-2 算法说明

错题表中的 SM-2 字段用于间隔复习调度：

- **ease_factor (EF)**：难度系数，初始 2.50，最低 1.30。复习评分越高，EF 越大，间隔增长越快。
- **interval_days**：当前复习间隔（天）。首次复习后为 1 天，之后按公式增长。
- **next_review_date**：下次复习日期 = 上次复习日期 + interval_days。
- **streak**：连续成功次数（rating >= 3）。rating < 3 时归零。

计算公式：
- 首次：interval = 1
- 第二次：interval = 6
- 之后：interval = interval × EF
- EF 调整：EF = EF + (0.1 - (4-rating) × (0.08 + (4-rating) × 0.02))
