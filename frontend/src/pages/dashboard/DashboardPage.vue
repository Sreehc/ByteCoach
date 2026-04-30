<template>
  <div class="space-y-6">
    <div class="grid gap-4 xl:grid-cols-4 md:grid-cols-2">
      <article v-for="metric in metrics" :key="metric.label" class="metric-card">
        <p class="metric-label">{{ metric.label }}</p>
        <p class="metric-value">{{ metric.value }}</p>
        <p class="mt-2 text-sm text-slate-500">{{ metric.desc }}</p>
      </article>
    </div>

    <div class="grid gap-4 xl:grid-cols-[1.2fr_0.8fr]">
      <section class="paper-panel p-6">
        <p class="section-kicker">Core Flow</p>
        <div class="mt-4 grid gap-3 md:grid-cols-5">
          <div v-for="step in steps" :key="step.title" class="rounded-3xl border border-black/5 bg-white/70 p-4">
            <div class="font-display text-2xl text-ember">{{ step.index }}</div>
            <div class="mt-2 font-semibold">{{ step.title }}</div>
            <div class="mt-2 text-sm leading-6 text-slate-500">{{ step.desc }}</div>
          </div>
        </div>
      </section>

      <section class="paper-panel p-6">
        <p class="section-kicker">Weakness Top N</p>
        <div class="mt-6 space-y-4">
          <div v-for="topic in weakTopics" :key="topic.name">
            <div class="flex items-center justify-between text-sm">
              <span class="font-semibold">{{ topic.name }}</span>
              <span class="text-slate-500">{{ topic.value }}%</span>
            </div>
            <div class="mt-2 h-2 rounded-full bg-slate-200">
              <div class="h-2 rounded-full bg-ember" :style="{ width: `${topic.value}%` }"></div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
const metrics = [
  { label: '学习总次数', value: '18', desc: '问答、面试、复习行为都归到统一闭环里' },
  { label: '平均面试分', value: '78', desc: '后续由 `interview_record` 聚合' },
  { label: '错题数量', value: '12', desc: '低分题自动沉淀，不做手动录入优先' },
  { label: '计划完成率', value: '64%', desc: '由 `study_plan_task` 的完成状态计算' }
]

const steps = [
  { index: '01', title: 'Login', desc: '先完成鉴权和状态恢复。' },
  { index: '02', title: 'Ask', desc: '支持普通问答和知识问答。' },
  { index: '03', title: 'Interview', desc: '每场 3-5 题，单次追问。' },
  { index: '04', title: 'Wrong', desc: '低分自动入错题本。' },
  { index: '05', title: 'Plan', desc: '围绕薄弱点生成任务。' }
]

const weakTopics = [
  { name: 'Spring', value: 84 },
  { name: 'JVM', value: 71 },
  { name: 'MySQL', value: 63 }
]
</script>

