<template>
  <div class="space-y-4">
    <div class="grid gap-4 xl:grid-cols-3">
      <article v-for="item in wrongItems" :key="item.id" class="metric-card">
        <div class="flex items-center justify-between">
          <span class="font-semibold">{{ item.title }}</span>
          <span class="rounded-full bg-sand px-3 py-1 text-xs uppercase tracking-[0.24em] text-ember">
            {{ item.mastery }}
          </span>
        </div>
        <p class="mt-3 text-sm leading-6 text-slate-600">{{ item.reason }}</p>
        <div class="mt-4 text-xs uppercase tracking-[0.22em] text-slate-400">再次练习入口已预留</div>
      </article>
    </div>
  </div>
</template>

<script setup lang="ts">
const wrongItems = [
  { id: 1, title: 'Spring AOP', mastery: 'reviewing', reason: '回答时遗漏了代理实现和适用边界。' },
  { id: 2, title: 'JVM GC', mastery: 'not_started', reason: '没有区分 CMS 与 G1 的关键差异。' },
  { id: 3, title: 'MySQL 索引', mastery: 'mastered', reason: '索引失效场景描述还不稳定。' }
]
</script>

