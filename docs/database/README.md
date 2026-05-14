# 数据库设计

> 完整建表语句：`sql/init.sql`
>
> 本文档以当前 SQL 为准，描述 OfferPilot 的正式数据模型边界。

## 总览

当前初始化脚本共包含 32 张表，按业务域拆分如下：

| 模块 | 表名 | 说明 |
|------|------|------|
| 用户 | `user` | 用户账号与安全字段 |
| 认证 | `login_device` | 登录设备 |
| 认证 | `login_log` | 登录日志 |
| 分类 | `category` | 题库/知识库分类 |
| 问答 | `chat_session` | 问答会话 |
| 问答 | `chat_message` | 问答消息 |
| 知识库 | `knowledge_doc` | 文档元信息 |
| 知识库 | `knowledge_chunk` | 文档切片 |
| 卡片强化 | `knowledge_card_task` | 卡片任务 |
| 卡片强化 | `knowledge_card` | 卡片内容 |
| 卡片强化 | `knowledge_card_log` | 卡片学习日志 |
| 卡片强化 | `daily_card_task` | 每日卡片任务 |
| 卡片强化 | `daily_memory_snapshot` | 记忆快照 |
| 题库 | `question` | 结构化题目 |
| 面试 | `interview_session` | 面试会话 |
| 面试 | `interview_record` | 每题记录 |
| 面试 | `voice_record` | 语音记录 |
| 错题 | `wrong_question` | 错题与掌握状态 |
| 复习 | `review_log` | 复习评分日志 |
| 学习计划 | `study_plan` | 计划主表 |
| 学习计划 | `study_plan_task` | 每日任务 |
| 简历 | `resume_file` | 简历文件与解析结果 |
| 简历 | `resume_project` | 简历拆分出的项目 |
| 投递 | `job_application` | 岗位投递主表 |
| 投递 | `job_application_event` | 投递事件流 |
| AI 治理 | `ai_call_log` | AI 调用日志 |
| AI 治理 | `system_config` | 系统配置与提示词覆盖 |
| 社区 | `community_question` | 社区问题 |
| 社区 | `community_answer` | 社区回答 |
| 社区 | `community_vote` | 社区投票 |
| 统计 | `user_stats` | 用户统计 |
| 通知 | `notification` | 通知 |

## 核心业务模型

### 用户与认证

#### `user`

- 主键：`id`
- 核心字段：`username`、`password`、`nickname`、`avatar`、`email`
- 权限字段：`role`、`status`
- 安全字段：`totp_secret`、`totp_enabled`、`recovery_codes`
- 行为字段：`last_login_time`

#### `login_device`

- 用于记录设备 ID、设备名称、IP、最后活跃时间、是否当前设备
- 配合 JWT、设备撤销和登录审计使用

#### `login_log`

- 记录登录时间、设备、IP、城市、是否成功、失败原因等
- 同时服务个人登录历史和管理员全局日志页面

### 题库、错题与复习

#### `question`

| 字段 | 说明 |
|------|------|
| `title` | 题目标题 |
| `category_id` | 分类 |
| `type` | 题型 |
| `difficulty` | 难度 |
| `frequency` | 高频程度 |
| `job_direction` | 适用岗位方向 |
| `applicable_scope` | 适用范围 |
| `tags` | 标签字符串 |
| `standard_answer` | 标准答案 |
| `interview_answer` | 面试版答案 |
| `follow_up_suggestions` | 追问建议 |
| `common_mistakes` | 常见错误 |
| `score_standard` | 评分标准 |

#### `wrong_question`

- 关联用户和题目
- 包含 `mastery_level`、`ease_factor`、`interval_days`、`next_review_date`
- 同一用户同一题目唯一
- 作为复习与错题导出的主数据源

#### `review_log`

- 记录每次复习评分、响应时间、复习前后间隔和 EF 变化
- 支撑复习统计与趋势分析

### 知识库与问答

#### `knowledge_doc`

- 文档归属：系统资料或用户资料
- 关键字段：`library_scope`、`business_type`、`file_type`、`status`
- 处理状态：`parse_status`、`index_status`
- 存储信息：`file_url`、`summary`、`content_text`

#### `knowledge_chunk`

- 关联 `knowledge_doc`
- 保存切片内容、顺序、向量索引键、检索分数等
- 作为 RAG 检索命中片段的结构化来源

#### `chat_session` / `chat_message`

- 维护问答会话和消息流
- 区分用户消息、AI 消息和引用信息
- 支撑普通问答与 RAG 问答的历史回看

### 模拟面试

#### `interview_session`

| 字段 | 说明 |
|------|------|
| `user_id` | 所属用户 |
| `mode` | `text` / `voice` |
| `direction` | 岗位方向 |
| `question_count` | 题量 |
| `status` | 会话状态 |
| `score` | 总分 |
| `summary` | 总结摘要 |

#### `interview_record`

- 按题保存题目、用户答案、AI 评分、点评、追问、标准答案
- 支撑详情页、趋势分析和面试治理列表

#### `voice_record`

- 记录语音文件地址、转写结果和处理状态
- 供语音面试链路回放和审计

### 学习计划

#### `study_plan`

| 字段 | 说明 |
|------|------|
| `title` | 计划标题 |
| `duration_days` | 周期，当前支持 7 / 14 / 30 |
| `focus_direction` | 重点方向 |
| `target_role` | 目标岗位 |
| `tech_stack` | 技术范围 |
| `weak_points` | 弱项标签 |
| `review_suggestion` | 复盘建议摘要 |
| `status` | `active / completed / archived` |
| `current_day` | 当前执行天数 |
| `progress_rate` | 进度百分比 |

#### `study_plan_task`

- 按天拆分任务
- 关键字段：`day_index`、`task_date`、`module`、`action_path`、`estimated_minutes`、`priority`、`status`
- 当前模块值主要覆盖 `question / chat / review / interview`

### 简历助手

#### `resume_file`

| 字段 | 说明 |
|------|------|
| `title` | 简历标题 |
| `file_url` | 文件存储地址 |
| `file_type` | `pdf / doc / docx` |
| `parse_status` | `pending / parsed / failed` |
| `raw_text` | 提取全文 |
| `summary` | 简历摘要 |
| `skills` | 技术栈标签 |
| `education` | 教育信息摘要 |
| `self_intro` | 推荐自我介绍 |
| `interview_resume_text` | 面试简历提纲 |

#### `resume_project`

- 关联 `resume_file`
- 拆分项目名称、角色、技术栈、职责、成果、摘要
- `follow_up_questions_json` 保存项目追问数组
- `risk_hints` 保存项目风险提示

### 投递管理

#### `job_application`

| 字段 | 说明 |
|------|------|
| `resume_file_id` | 绑定的简历 |
| `company` | 公司 |
| `job_title` | 岗位名称 |
| `city` | 城市 |
| `source` | 渠道 |
| `jd_text` | JD 原文 |
| `status` | `saved / applied / written / interview / offer / rejected` |
| `match_score` | JD 匹配度 |
| `jd_keywords` | 识别出的关键词 |
| `missing_keywords` | 待补关键词 |
| `analysis_summary` | JD 分析摘要 |
| `apply_date` | 投递日期 |
| `next_step_date` | 下一节点日期 |

#### `job_application_event`

- 关联 `job_application`
- 事件类型：`status_change / interview / review / note / analysis`
- 保存标题、内容、事件时间和结果摘要
- 用于详情页时间线和状态流转留痕

### AI 治理

#### `ai_call_log`

| 字段 | 说明 |
|------|------|
| `provider` | 网关或服务商标识 |
| `model` | 模型名 |
| `call_type` | `chat / stream / embedding` |
| `scene` | 业务场景 |
| `input_tokens` | 输入 token 估算值 |
| `output_tokens` | 输出 token 估算值 |
| `latency_ms` | 调用耗时 |
| `success` | 是否成功 |
| `error_message` | 错误摘要 |

#### `system_config`

- 配置分组：`llm / embedding / prompt`
- 保存可在线覆盖的运行配置和提示词模板
- 包含 `config_key`、`config_value`、`value_type`、`description`、`enabled`

## 关系说明

### 主线关系

```text
question -> wrong_question -> review_log
knowledge_doc -> knowledge_chunk -> chat_message
interview_session -> interview_record -> wrong_question / knowledge_card
study_plan -> study_plan_task
resume_file -> resume_project -> job_application -> job_application_event
ai_call_log / system_config -> admin governance
```

### 归属关系

- 大部分业务表都以 `user_id` 作为数据归属边界
- 管理后台接口在读取这些表时会额外按管理员权限保护
- 简历、投递、问答和面试数据互相可形成训练闭环，但当前仍保持各域独立存储

## 设计原则

- 结构化训练内容和非结构化知识语料分开建模
- 面试、简历、投递分别使用独立主表，避免把求职链路挤进单一训练模型
- AI 治理信息单独落表，避免和业务主表强耦合
- 当前文档只描述已经存在的表和字段，不扩展未来规划字段
