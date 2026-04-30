<template>
  <aside class="paper-panel flex h-full flex-col justify-between p-5">
    <div class="space-y-8">
      <div>
        <div class="section-kicker">ByteCoach</div>
        <h1 class="mt-3 font-display text-3xl leading-none text-ink">Interview Studio</h1>
        <p class="mt-3 text-sm leading-6 text-slate-500">
          聚焦 Java 后端面试准备的最小学习闭环。
        </p>
      </div>

      <nav class="space-y-2">
        <RouterLink
          v-for="item in items"
          :key="item.path"
          :to="item.path"
          class="group flex items-center justify-between rounded-2xl px-4 py-3 text-sm transition-all duration-200"
          :class="isActive(item.path) ? 'bg-ink text-white' : 'text-slate-600 hover:bg-white/70'"
        >
          <span>{{ item.label }}</span>
          <span class="font-display text-base opacity-60">{{ item.index }}</span>
        </RouterLink>
      </nav>
    </div>

    <div class="rounded-3xl bg-ink px-4 py-5 text-white">
      <p class="text-xs uppercase tracking-[0.28em] text-white/60">MVP Freeze</p>
      <p class="mt-3 text-sm leading-6 text-white/80">
        登录、问答、面试、错题、计划、Dashboard 六段主链路优先。
      </p>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'

const route = useRoute()

const items = [
  { path: '/dashboard', label: 'Dashboard', index: '01' },
  { path: '/chat', label: 'Chat', index: '02' },
  { path: '/knowledge', label: 'Knowledge', index: '03' },
  { path: '/interview', label: 'Interview', index: '04' },
  { path: '/wrong', label: 'Wrong Book', index: '05' },
  { path: '/plan', label: 'Study Plan', index: '06' },
  { path: '/admin', label: 'Admin', index: '07' }
]

const isActive = (path: string) => route.path === path
</script>

