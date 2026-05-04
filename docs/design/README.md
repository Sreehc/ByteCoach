# UI/UX 设计

## 设计系统

### 设计风格

ByteCoach 采用简洁、专业的设计风格，目标是在学习场景下减少视觉干扰，让用户专注于内容。

### 配色方案

基于 CSS 变量的主题系统，支持亮色/暗色模式：

| 用途 | 亮色 | 暗色 | 变量名 |
|------|------|------|--------|
| 主色调 | #2F4F9D（深蓝） | #5B7FCC | `--accent` |
| 文字主色 | #1a1a2e | #e2e8f0 | `--ink` |
| 背景 | #f8f9fc | #0f172a | `--bg` |
| 卡片背景 | #ffffff | #1e293b | `--surface` |
| 边框 | #e2e8f0 | #334155 | `--border` |

### 字体

- 正文：系统字体栈（-apple-system, BlinkMacSystemFont, "Segoe UI", ...）
- 数字/代码：JetBrains Mono, monospace

### 圆角

- 小组件：6px（`--radius-sm`）
- 卡片：12px（`--radius-md`）
- 大面板：16px（`--radius-lg`）

---

## 布局结构

### 主布局

```
┌──────────────────────────────────────────────────┐
│                  AppShellHeader                   │
│  [Logo]  [页面标题]           [搜索] [通知] [头像] │
├──────────┬───────────────────────────────────────┤
│          │                                       │
│  NavRail │           页面内容区                   │
│  (侧边栏) │                                       │
│          │                                       │
│  首页    │                                       │
│  问答    │                                       │
│  知识库  │                                       │
│  面试    │                                       │
│  错题本  │                                       │
│  复习    │                                       │
│  社区    │                                       │
│  计划    │                                       │
│  ────── │                                       │
│  管理后台│                                       │
│          │                                       │
├──────────┴───────────────────────────────────────┤
│              MobileNavBar（移动端底部导航）         │
└──────────────────────────────────────────────────┘
```

- **桌面端**：左侧 NavRail 固定宽度 240px，右侧内容区自适应
- **移动端**：NavRail 隐藏，底部 MobileNavBar 显示 5 个主要入口

### 页面结构

每个页面遵循统一的结构：

```html
<div class="space-y-6">
  <!-- 页面头部 -->
  <section class="paper-panel p-6">
    <p class="section-kicker">模块名</p>
    <h2 class="page-title">页面标题</h2>
    <p class="page-subtitle">页面描述</p>
  </section>

  <!-- 内容区 -->
  <section class="paper-panel p-6">
    <!-- 具体内容 -->
  </section>
</div>
```

---

## 组件规范

### paper-panel

白色圆角卡片，带微妙的阴影：

```css
.paper-panel {
  background: var(--surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border);
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}
```

### metric-card

指标卡片，用于 Dashboard 和数据展示：

```html
<article class="metric-card">
  <p class="metric-label">指标名称</p>
  <p class="metric-value">123</p>
</article>
```

### section-kicker

页面/区块的标签文字，小号大写蓝色：

```css
.section-kicker {
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.24em;
  text-transform: uppercase;
  color: var(--accent);
}
```

### action-button

主操作按钮，深蓝色圆角：

```css
.action-button {
  background: var(--accent);
  color: white;
  border-radius: var(--radius-md);
  font-weight: 600;
}
```

### accent-link

强调链接，蓝色下划线：

```css
.accent-link {
  color: var(--accent);
  font-weight: 600;
  text-decoration: underline;
}
```

---

## 页面清单

### 公开页面

| 页面 | 路径 | 说明 |
|------|------|------|
| 登录 | /login | 左右分栏，左侧功能介绍，右侧登录表单 |
| 注册 | /register | 同登录页布局 |
| 2FA 验证 | /verify-2fa | 居中卡片，验证码/恢复码输入 |

### 学习页面

| 页面 | 路径 | 说明 |
|------|------|------|
| Dashboard | /dashboard | 指标卡片 + 最近面试 + 薄弱点 + 引导卡片 |
| 问答 | /chat | 左侧会话列表 + 右侧聊天区 |
| 知识库 | /knowledge | 分类筛选 + 文档卡片列表 + 搜索 |
| 面试 | /interview | 面试配置 → 答题界面 → 结果展示 |
| 面试历史 | /interview/history | 面试记录列表 |
| 面试详情 | /interview/detail/:id | 逐题复盘 |
| 错题本 | /wrong | 错题卡片列表 + 掌握程度 |
| 复习 | /review | 今日待复习 + 评分交互 |
| 学习计划 | /plan | 计划详情 + 任务列表 |
| 数据分析 | /analytics | 趋势图 + 效率图 + 时段分析 |
| 社区 | /community | 问题列表 + 筛选排序 |
| 排行榜 | /community/leaderboard | 排名列表 + 等级 |

### 设置页面

| 页面 | 路径 | 说明 |
|------|------|------|
| 账户设置 | /settings | Tab 切换：设备管理 / 登录历史 / 两步验证 / 数据导出 |

### 管理后台

| 页面 | 路径 | 说明 |
|------|------|------|
| 管理后台 | /admin | Tab 切换：系统概览 / 用户管理 / 内容审核 / 分类 / 题库 / 文档 / 登录日志 |

---

## 交互规范

### 表格

- 使用 Element Plus 的 `el-table`
- 斑马纹（stripe）
- 表头背景色：`var(--el-bg-color-page)`

### 弹窗

- 编辑操作使用 `el-dialog`
- 删除/封禁等危险操作使用 `el-popconfirm` 二次确认

### 分页

- 使用 Element Plus 的 `el-pagination`
- 布局：`prev, pager, next`

### 加载状态

- 表格：`v-loading` 指令
- 按钮：`:loading` 属性
- 页面级：SkeletonBlock 组件

### 空状态

- 使用 EmptyState 组件
- 图标 + 标题 + 描述文字

### 消息提示

- 成功：`ElMessage.success()`
- 错误：`ElMessage.error()`
- 警告：`ElMessage.warning()`

---

## 响应式断点

| 断点 | 宽度 | 布局变化 |
|------|------|----------|
| 移动端 | < 768px | NavRail 隐藏，底部导航，单列布局 |
| 平板 | 768px - 1024px | NavRail 显示，内容区自适应 |
| 桌面 | > 1024px | 完整布局，双列/三列网格 |

---

## 图标

使用 SVG 内联图标，不依赖图标库。常见图标：

- 设备：显示器 + 键盘轮廓
- 安全：锁/盾牌
- 下载：箭头向下 + 横线
- 搜索：放大镜
- 通知：铃铛

---

## 暗色模式

通过 `useTheme` composable 管理：

- 存储在 localStorage
- 跟随系统偏好（`prefers-color-scheme`）
- 切换时在 `<html>` 上添加/移除 `dark` class
- 所有颜色通过 CSS 变量自动适配
