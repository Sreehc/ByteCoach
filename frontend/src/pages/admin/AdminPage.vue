<template>
  <div class="space-y-6">
    <section class="grid gap-4 lg:grid-cols-3">
      <article v-for="module in modules" :key="module.name" class="metric-card">
        <div class="metric-label">{{ module.scope }}</div>
        <div class="mt-3 font-display text-3xl">{{ module.name }}</div>
        <p class="mt-3 text-sm leading-6 text-slate-500">{{ module.desc }}</p>
      </article>
    </section>

    <section class="paper-panel p-6">
      <p class="section-kicker">Admin Boundary</p>
      <p class="mt-4 max-w-3xl text-sm leading-7 text-slate-600">
        后台不是独立领域模块，当前只作为 `Question / Knowledge / Category` 的管理入口。阶段 1 只搭路由和壳，不进入完整增删改表单实现。
      </p>
    </section>
  </div>
</template>

<script setup lang="ts">
const modules = [
  { name: 'Question', scope: '/api/admin/question/*', desc: '题库管理接口和后续表格入口。' },
  { name: 'Knowledge', scope: '/api/admin/knowledge/*', desc: '文档导入、重切分、重建索引。' },
  { name: 'Category', scope: '/api/admin/category/*', desc: '分类维护和排序。' }
]
</script>

