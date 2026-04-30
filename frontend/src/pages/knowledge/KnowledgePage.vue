<template>
  <div class="space-y-6">
    <section class="grid gap-4 lg:grid-cols-[1.1fr_0.9fr]">
      <div class="paper-panel p-6">
        <p class="section-kicker">Knowledge Library</p>
        <h3 class="mt-4 font-display text-3xl text-ink">一期只做内置资料库</h3>
        <p class="mt-4 text-sm leading-7 text-slate-600">
          用户上传、在线切分、实时向量化全部后置。当前阶段只保留文档列表、分类查看、检索测试和管理端导入入口。
        </p>
      </div>
      <div class="paper-panel p-6">
        <p class="section-kicker">RAG Boundary</p>
        <ul class="mt-4 space-y-3 text-sm leading-7 text-slate-600">
          <li>文档元数据落 `knowledge_doc`</li>
          <li>分片内容落 `knowledge_chunk`</li>
          <li>`Qdrant` 和 rerank 只保留扩展位</li>
        </ul>
      </div>
    </section>

    <section class="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
      <article v-for="doc in docs" :key="doc.id" class="metric-card">
        <div class="flex items-center justify-between">
          <h4 class="font-semibold">{{ doc.title }}</h4>
          <span class="rounded-full bg-ink px-3 py-1 text-xs uppercase tracking-[0.22em] text-white">{{ doc.status }}</span>
        </div>
        <p class="mt-3 text-sm leading-6 text-slate-500">{{ doc.summary }}</p>
      </article>
    </section>
  </div>
</template>

<script setup lang="ts">
const docs = [
  { id: 1, title: 'Spring 核心笔记', status: 'vectorized', summary: 'IOC / AOP / MVC 快速回顾。' },
  { id: 2, title: 'JVM 面试手册', status: 'parsed', summary: '类加载、GC、内存区域。' },
  { id: 3, title: 'MySQL 高频题', status: 'draft', summary: '索引、事务、锁和执行计划。' }
]
</script>

