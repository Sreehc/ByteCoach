# 卡片记忆主线重构推进计划

> 目标：将 ByteCoach 从“Java 面试综合训练平台”调整为“面向 Java 后端面试的智能记忆卡片系统”。
> 核心体验参考扇贝单词：用户每天进入产品后，优先完成今日卡片任务，通过持续复习建立长期记忆。

---

## 一、产品定位调整

### 新定位

ByteCoach 是一个面向 Java 后端面试准备的智能记忆卡片系统。它将知识库、问答、模拟面试和错题沉淀转化为可持续复习的卡片，帮助用户每天稳定推进知识掌握。

### 主闭环

```
导入资料 / 完成诊断 / 提出问题
        ↓
AI 生成知识卡片
        ↓
每日卡片学习
        ↓
间隔复习调度
        ↓
掌握度提升与薄弱点反馈
        ↓
继续生成或调整卡片
```

### 主线变化

| 维度 | 当前形态 | 调整后形态 |
|------|----------|------------|
| 首页主任务 | 面试、错题、趋势混合 | 今日卡片任务 |
| 卡片定位 | 知识库附属能力 | 产品核心主线 |
| 面试定位 | 核心训练入口 | 薄弱点诊断与卡片生成入口 |
| 知识库定位 | RAG 问答资料池 | 卡片来源与知识素材库 |
| 数据看板 | 面试次数、平均分、错题数 | 今日完成率、连续天数、掌握卡片数、复习负债 |
| 用户心智 | AI 面试学习平台 | 每天刷 Java 面试卡片 |

---

## 二、推进方向

### 方向一：产品主线与信息架构重构

**状态：已完成**

> 已在 commit `c951677` 中落地。范围包括首页主 CTA 与指标重构、导航顺序调整、页面核心命名统一、`/api/dashboard/overview` 记忆导向字段补齐，以及 `README.md` / `docs/prd/overview.md` 重写。

### 目标

让用户进入产品后明确感知：每天最重要的动作是完成今日卡片，而不是在多个学习模块之间选择。

### 需要修改的内容

#### 前端

- [x] `frontend/src/router/index.ts`
  - 调整页面 meta 文案，将“面试练习、资料问答”弱化为辅助能力。
  - 首页标题改为围绕“今日记忆任务”“卡片复习进度”。
  - 卡片页标题改为“今日卡片”或“记忆工作台”。

- [x] `frontend/src/layouts/AppLayout.vue`
  - 调整导航顺序：首页、今日卡片、知识库、复习中心、问答、面试诊断、数据分析、管理后台。
  - 将卡片入口设为高优先级导航项。
  - 面试入口文案调整为“面试诊断”。

- [x] `frontend/src/pages/dashboard/DashboardPage.vue`
  - 首页主 CTA 改为“开始今日卡片”。
  - 首屏展示今日待学、今日待复习、连续学习天数、今日完成率。
  - 将面试推荐从主任务降级为辅助建议。

- [x] `frontend/src/pages/dashboard/DashboardMetrics.vue`
  - 指标从面试导向改为记忆导向。
  - 建议核心指标：今日卡片、今日完成率、连续天数、已掌握卡片、复习负债。

- [x] `frontend/src/components/NavRail.vue` / `frontend/src/components/MobileNavBar.vue`
  - 导航入口顺序和命名改为“今日卡片主线”。
  - 移动端底部导航保留 5 项，并将今日卡片前置。

- [x] `frontend/src/pages/cards/CardsPage.vue` / `frontend/src/pages/review/ReviewPage.vue` / `frontend/src/pages/interview/InterviewPage.vue`
  - 页面内核心标题同步切换为“今日卡片 / 复习中心 / 面试诊断”。

#### 后端

- [x] `backend/src/main/java/com/bytecoach/dashboard`
  - 扩展 `/api/dashboard/overview`。
  - 新增 `todayLearnCards`、`todayReviewCards`、`todayCompletedCards`、`todayCardCompletionRate`、`masteredCardCount`、`reviewDebtCount` 聚合字段。

#### 文档

- [x] `README.md`
  - 重写项目简介与核心功能顺序。
  - 将“智能记忆卡片、间隔复习、资料转卡片”放在最前。

- [x] `docs/prd/overview.md`
  - 重写项目定位、核心价值和主闭环。
  - 调整功能模块优先级，将卡片系统作为一期核心。

### 验收标准

- [x] 用户打开首页后，3 秒内能理解今天要完成什么。
- [x] 首页第一按钮指向卡片学习，而不是面试或数据分析。
- [x] 导航结构清楚表达“卡片是主线，其他模块是来源或辅助”。

---

### 方向二：卡片系统升级为核心工作台

**状态：已完成（v1 兼容演进）**

> 本次按“方向二 v1：卡片工作台兼容演进”落地。没有直接切换到 `memory_*` 通用模型，而是基于现有 `knowledge_card_task / knowledge_card / knowledge_card_log` 扩展出 deck、今日任务、复习队列和工作台接口。
> 偏差说明：面试来源本次不接入 deck，低分面试题继续先进入错题本，由错题 deck 自动沉淀；面试专属 deck 规则顺延到方向五。

### 目标

将现有卡片页从“单文档生成任务”升级为类似扇贝单词的每日记忆工作台，支持牌组、今日任务、复习队列、连续打卡和掌握度追踪。

### 需要修改的内容

#### 前端

- [x] `frontend/src/pages/cards/CardsPage.vue`
  - 首屏改为“今日任务”而不是“选择资料生成卡片”。
  - 展示今日待学、今日待复习、预计用时、连续天数。
  - 支持学习流：正面问题、背面答案、评分按钮、下一张卡。
  - 支持任务完成页：今日完成、连续天数、明日预计复习量。

- 新增或拆分组件
  - [x] `TodayCardsPanel.vue`：今日任务总览。
  - [x] `CardStudySession.vue`：卡片学习会话。
  - [x] `CardDeckList.vue`：牌组列表。
  - [x] `CardProgressSummary.vue`：卡片进度摘要。

#### 后端

- [x] `backend/src/main/java/com/bytecoach/cards`
  - 补齐牌组、卡片、复习记录、每日任务相关能力。
  - 当前 `KnowledgeCard` 任务模型保留，v1 中 `knowledge_card_task` 直接承担 deck 容器。

#### 数据库

v1 采用兼容演进，实际落地为：

- [x] 扩展 `knowledge_card_task`
  - 新增 `source_type`、`deck_title`、`is_current`、`estimated_minutes`、`last_studied_at`。

- [x] 扩展 `knowledge_card`
  - 新增 `source_ref_id`、`source_ref_type`、`explanation`，继续沿用 `next_review_at`。

- [x] 新增 `daily_card_task`
  - 记录当天计划量、完成量、连续天数快照、预计用时和明日复习量。

- [x] 继续沿用 `knowledge_card_log`
  - 作为评分记录，不新增 `card_review_log`。

#### API

新增接口：

- [x] `GET /api/cards/today`
  - 获取今日卡片任务。

- [x] `POST /api/cards/{id}/review`
  - 提交卡片评分，并返回下一次复习时间。

- [x] `GET /api/cards/decks`
  - 获取牌组列表和进度。

- [x] `POST /api/cards/decks/{id}/activate`
  - 手动切换当前 deck，同一用户同时最多一个当前 deck。

- [x] `POST /api/cards/generate`
  - 从知识库文档生成 deck，并可激活为当前 deck。

- [x] `GET /api/cards/stats`
  - 获取卡片学习统计。

保留旧接口 `/api/cards/task`、`/api/cards/active`、`/api/cards/task/{id}`、`/api/cards/task/{id}/start`、`/api/cards/task/{id}/rate`、`/api/cards/task/{id}/restart` 作为兼容层。

#### Deck 规则

- [x] 知识库来源：一文档一 deck，主动生成后可直接成为当前 deck。
- [x] 错题来源：每个用户一个系统只读 deck，标题固定为“错题复习”。
- [x] 错题自动沉淀：新增、更新、删除错题后同步 deck 卡片，不抢占当前 deck。
- [x] 今日任务：只推进一个当前 deck，不做多 deck 统一调度。
- [x] 面试来源：方向二不直接接入，留到方向五设计专属 deck 规则。

### 验收标准

- [x] 用户无需先进入知识库，也能在卡片页直接开始今日任务。
- [x] 完成一张卡片后，系统根据评分自动安排下次复习。
- [x] 用户能看到连续学习天数和今日完成状态。
- [x] 用户能手动切换当前 deck。
- [x] 错题自动进入卡片工作台，但不会打断当前正在推进的 deck。

---

### 方向三：知识库转为卡片来源

**状态：已完成**

> 本次方向三延续方向二的兼容演进策略：继续采用“一文档一 deck”，同一文档已生成时默认进入或激活现有 deck，不做覆盖重生成、不做追加生成，也不引入手动 deck CRUD。

### 目标

知识库不再只是 RAG 问答的资料池，而是“导入资料 -> 自动生成卡片 -> 加入每日记忆”的内容生产入口。

### 需要修改的内容

#### 前端

- [x] `frontend/src/pages/knowledge/KnowledgePage.vue`
  - 每份文档突出“生成卡片”按钮。
  - 展示文档已生成卡片数量、所属牌组、最近生成时间。
  - 支持选择生成类型：概念卡、问答卡、场景题卡、易混淆点卡。

- [x] `frontend/src/pages/cards/CardsPage.vue`
  - 支持从知识库跳转后直接进入“生成卡片确认页”。
  - 允许用户选择生成数量、难度、复习天数和卡片类型。
  - 若当前文档已有 deck，则展示已有 deck 摘要并提供进入/激活入口，而不是重复生成。

#### 后端

- [x] `backend/src/main/java/com/bytecoach/knowledge/service`
  - 文档解析和分片能力继续复用。
  - 为卡片生成提供文档 chunk 和来源引用，并在文档列表中返回 deck 统计。

- [x] `backend/src/main/java/com/bytecoach/cards/service`
  - 新增文档到卡片的生成服务。
  - 生成后保存来源文档 ID、chunk ID、引用片段，方便用户追溯。

- [x] `backend/src/main/java/com/bytecoach/ai/service`
  - 增加卡片生成 Prompt 模板。
  - 输出结构化 JSON，避免直接解析自然语言。

#### Prompt 方向

卡片生成需要从“回答问题”转为“提炼可记忆知识点”。建议输出字段：

- `front`：卡片正面问题。
- `back`：卡片背面答案。
- `explanation`：辅助理解说明。
- `tags`：知识标签。
- `difficulty`：难度。
- `cardType`：概念卡、问答卡、场景卡、对比卡。
- `sourceQuote`：来源片段。

### 验收标准

- [x] 上传一份资料后，用户能一键生成一组可学习卡片。
- [x] 卡片答案可以追溯到原文来源。
- [x] 卡片生成质量足够用于直接学习，而不是需要用户大量手工修订。

---

### 方向四：统一复习算法与记忆状态

**状态：已规划 / 待实现**

> 本次方向四按兼容演进执行：统一调度服务、统一复习中心接口和统一分析口径，但不合并 `wrong_question`、`knowledge_card` 及其日志表。
> 面试卡片本次按“面试低分题进入错题本后的复习项”口径归类到复习中心 `interview_card` 过滤；真正独立的面试 deck / 面试卡片体系仍留到方向五。

### 目标

将错题复习和知识卡片复习统一为一套记忆调度机制，避免产品中出现两套学习系统。

### 需要修改的内容

#### 后端

- `backend/src/main/java/com/bytecoach/wrong/service/impl/SpacedRepetitionServiceImpl.java`
  - 抽象为通用复习调度服务。
  - 支持不同内容类型：知识卡片、错题卡片、面试薄弱点卡片。

- `backend/src/main/java/com/bytecoach/cards/service`
  - 调用统一复习调度服务计算下一次复习日期。
  - 评分体系建议沿用 4 档：重来、困难、良好、轻松。

- `backend/src/main/java/com/bytecoach/wrong`
  - 错题可转化为特殊类型卡片。
  - 保留错题本概念，但底层进入统一记忆池。

#### 前端

- `frontend/src/pages/review/ReviewPage.vue`
  - 从“错题复习”升级为“复习中心”。
  - 支持按内容类型筛选：全部、知识卡片、错题卡片、面试卡片。

- `frontend/src/pages/cards/CardsPage.vue`
  - 卡片评分与复习中心保持一致。
  - 今日任务中同时包含新学卡片和到期复习卡片。

### 验收标准

- 卡片和错题都使用同一套评分与下次复习逻辑。
- 用户看到的是统一的“今日复习负债”，而不是多个模块各算各的。
- 复习记录可以统一进入数据分析。

---

### 方向五：面试降级为诊断与卡片生成入口

**状态：已完成（v1 兼容演进）**

> 本次方向五延续方向二到方向四的兼容演进方案：不新建独立 `memory_*` 模型，而是在现有 `knowledge_card_task / knowledge_card / knowledge_card_log` 上新增 `interview_auto` 来源，并将面试低分题沉淀为用户级单一只读 deck。
> 偏差说明：本次采用“全用户一组面试诊断卡片 deck”，不是按场次或方向拆分多个 interview deck；自动生成不会抢占当前 deck，只有用户手动点击“加入今日卡片”时才切换；错题 deck 与 interview deck 允许重复语义内容并存，不做跨来源去重融合。

### 目标

面试不再是产品主线，而是帮助用户发现薄弱点、生成高价值卡片的诊断工具。

### 需要修改的内容

#### 前端

- [x] `frontend/src/pages/interview/InterviewPage.vue`
  - 页面文案从“开始模拟面试”调整为“做一次面试诊断”。
  - 说明诊断结果会生成薄弱点卡片。

- [x] `frontend/src/pages/interview/InterviewDetailPage.vue`
  - 增加“生成复习卡片”或“加入今日卡片”按钮。
  - 对低分题展示推荐卡片内容。

- [x] `frontend/src/pages/interview/InterviewHistoryPage.vue`
  - 标记每场面试是否已生成卡片。
  - 支持从历史面试补生成卡片。

#### 后端

- [x] `backend/src/main/java/com/bytecoach/interview/service/impl/InterviewServiceImpl.java`
  - 低分题不只进入错题本，也生成面试卡片。
  - 卡片内容包括问题、用户答案问题点、标准答案、追问点。

- [x] `backend/src/main/java/com/bytecoach/cards/service`
  - 新增面试记录到卡片的生成逻辑。
  - 新增 `POST /api/interview/{sessionId}/cards/generate` 与 `POST /api/interview/{sessionId}/cards/activate`。

#### 实际落地口径

- [x] 面试完成后，低分题继续进入错题本，同时自动同步到 `interview_auto` deck。
- [x] `interview_auto` deck 为用户级单一只读 deck，标题固定为“面试诊断卡片”。
- [x] 历史补生成与自动生成都按 `interview_record.id` 去重，不重复插卡。
- [x] 详情页和历史页可直接触发“补生成 / 加入今日卡片”。
- [x] 统一复习中心中的 `interview_card` 过滤升级为可识别真实 `interview_record` 来源卡片。

### 验收标准

- [x] 用户完成一场面试后，能得到可复习的卡片集合。
- [x] 面试入口不再抢占首页主任务。
- [x] 面试结果能直接沉淀到卡片主线。

---

### 方向六：数据看板改为记忆成长看板

### 目标

数据分析从“面试表现统计”转向“记忆进度与掌握度增长”，帮助用户知道自己是否坚持、是否掌握、是否有复习负债。

### 需要修改的内容

#### 前端

- `frontend/src/pages/dashboard/DashboardPage.vue`
  - 首屏展示今日卡片任务。
  - 增加连续学习天数和每日完成状态。

- `frontend/src/pages/dashboard/DashboardWeakPoints.vue`
  - 从弱点分类展示调整为分类掌握度。
  - 展示每个分类的待复习量和掌握卡片数。

- `frontend/src/pages/analytics/AnalyticsPage.vue`
  - 增加记忆曲线、复习负债趋势、分类掌握度、评分分布。
  - 面试平均分作为辅助指标保留。

#### 后端

- `backend/src/main/java/com/bytecoach/dashboard`
  - 聚合今日卡片、复习负债、连续天数、掌握卡片数。

- `backend/src/main/java/com/bytecoach/analytics`
  - 新增卡片完成率、复习完成率、遗忘率、EF 趋势、分类掌握度。

### 验收标准

- 用户能清楚看到今天是否完成、最近是否稳定坚持。
- 用户能看到哪些分类掌握弱、哪些卡片逾期。
- 数据看板能反向驱动用户继续完成卡片。

---

## 三、阶段计划

### 阶段 1：主线感知调整

### 目标

不大改底层数据结构，先让用户感知产品主线已切换到卡片记忆。

### 范围

- 首页首屏改为今日卡片任务。
- 导航顺序调整。
- 卡片页文案与入口强化。
- 面试文案降级为诊断。
- README 和 PRD 定位更新。

### 预计产出

- 用户进入产品后，默认动作变为“开始今日卡片”。
- 产品介绍从“AI 面试系统”改为“Java 面试卡片记忆系统”。

### 风险

- 如果后端卡片统计接口尚不完整，首页可能需要先做兼容展示。

---

### 阶段 2：卡片核心模型重构

### 目标

建立长期可扩展的卡片、牌组、复习状态和复习记录模型。

### 范围

- 新增牌组与通用卡片模型。
- 新增今日任务接口。
- 新增评分提交接口。
- 卡片页支持完整学习会话。
- 复用或抽象 SM-2 调度算法。

### 预计产出

- 用户可以像背单词一样每天刷卡。
- 每张卡片都有用户维度的复习状态。
- 系统可以稳定计算今日待学和待复习。

### 风险

- 现有 `KnowledgeCard` 任务模型与新模型可能存在迁移成本。
- 需要避免一次性重构过大，可以先兼容旧表，再逐步迁移。

---

### 阶段 3：内容来源打通

### 目标

让知识库、面试、错题都能稳定生成卡片。

### 范围

- 知识库文档生成卡片。
- 面试低分题生成卡片。
- 错题转卡片。
- 卡片保留来源引用。

### 预计产出

- 用户不需要手动创建大量卡片。
- 所有学习行为都能沉淀到卡片池。

### 风险

- AI 生成卡片质量是关键，需要专门评估 Prompt 和结构化输出稳定性。

---

### 阶段 4：记忆成长数据化

### 目标

用数据反馈用户的坚持、掌握和薄弱点。

### 范围

- 首页卡片指标。
- 数据分析页新增记忆曲线。
- 分类掌握度。
- 复习负债趋势。

### 预计产出

- 用户能看到持续刷卡带来的进度变化。
- 产品具备复访驱动力。

### 风险

- 指标过多会增加认知负担，首页只保留最关键的 3-5 个指标。

---

## 四、优先级建议

| 优先级 | 事项 | 原因 |
|--------|------|------|
| P0 | 首页主任务改为今日卡片 | 直接改变用户心智 |
| P0 | 卡片页升级为今日学习工作台 | 承载新的产品主线 |
| P0 | 通用卡片复习状态模型 | 支撑长期记忆系统 |
| P1 | 知识库生成卡片 | 建立主要内容来源 |
| P1 | 错题与卡片统一调度 | 避免两套复习体系 |
| P1 | 面试结果生成卡片 | 保留面试价值但降级为诊断 |
| P2 | 记忆成长数据分析 | 提升复访和成就感 |
| P2 | 牌组管理增强 | 支持更复杂内容组织 |
| P3 | 社区与排行榜联动卡片 | 用户量起来后再做 |

---

## 五、关键产品指标

### 激活指标

- 新用户首日开始卡片学习比例。
- 新用户首日完成至少 10 张卡片比例。
- 从知识库生成第一组卡片的比例。

### 留存指标

- 次日继续学习比例。
- 7 日内完成 3 天卡片任务比例。
- 连续学习天数分布。

### 学习效果指标

- 卡片复习正确率。
- 卡片从新学到掌握的平均天数。
- 逾期复习卡片比例。
- 分类掌握度提升。

### 内容质量指标

- AI 生成卡片被用户保留比例。
- 用户手动编辑卡片比例。
- 用户删除生成卡片比例。
- 来源文档到卡片的转化率。

---

## 六、当前不建议优先做的内容

- 不优先强化社区，因为卡片主线尚未验证，社区冷启动成本高。
- 不优先扩大面试功能，因为面试已从主线降级为诊断入口。
- 不优先做复杂排行榜，避免用户早期被竞争压力干扰。
- 不优先做过多卡片类型，先保证问答卡、概念卡、场景卡质量。
- 不优先做多端同步之外的复杂社交能力，先验证个人学习闭环。

---

## 七、一句话执行原则

每次迭代都检查一个问题：这个改动是否让用户更容易每天完成卡片，并把知识长期记住。
如果答案是否定的，就不应该进入当前阶段的优先级。
