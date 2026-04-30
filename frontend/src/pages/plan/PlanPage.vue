<template>
  <div class="grid gap-4 xl:grid-cols-[0.85fr_1.15fr]">
    <section class="paper-panel p-6">
      <p class="section-kicker">Plan Generator</p>
      <h3 class="mt-4 font-display text-3xl text-ink">围绕错题和薄弱点拆成每日任务</h3>
      <p class="mt-4 text-sm leading-7 text-slate-600">
        阶段 1 只做一次性生成和任务状态更新，不做动态调整和个性化增强。
      </p>
      <div class="mt-6 grid gap-3">
        <div class="rounded-3xl border border-black/5 bg-white/70 p-4">
          <div class="text-xs uppercase tracking-[0.24em] text-slate-500">Direction</div>
          <div class="mt-2 font-semibold">Java Backend</div>
        </div>
        <div class="rounded-3xl border border-black/5 bg-white/70 p-4">
          <div class="text-xs uppercase tracking-[0.24em] text-slate-500">Cycle</div>
          <div class="mt-2 font-semibold">7 Days / 60 mins daily</div>
        </div>
      </div>
    </section>

    <section class="paper-panel p-6">
      <p class="section-kicker">Task Timeline</p>
      <div class="mt-6 space-y-3">
        <div v-for="task in tasks" :key="task.id" class="rounded-3xl border border-black/5 bg-white/70 p-4">
          <div class="flex items-center justify-between">
            <div>
              <div class="font-semibold">{{ task.content }}</div>
              <div class="mt-1 text-sm text-slate-500">{{ task.date }}</div>
            </div>
            <span class="rounded-full px-3 py-1 text-xs uppercase tracking-[0.24em]" :class="task.status === 'done' ? 'bg-pine text-white' : 'bg-sand text-ember'">
              {{ task.status }}
            </span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
const tasks = [
  { id: 1, date: 'Day 1', content: '复习 Spring AOP 错题并完成一次知识问答', status: 'todo' },
  { id: 2, date: 'Day 2', content: '完成 1 场 JVM 模拟面试', status: 'todo' },
  { id: 3, date: 'Day 3', content: '复盘 MySQL 事务与锁问题', status: 'done' }
]
</script>

