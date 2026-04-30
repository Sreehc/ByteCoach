<template>
  <div class="grid gap-4 xl:grid-cols-[0.95fr_1.05fr]">
    <section class="paper-panel p-6">
      <p class="section-kicker">Interview Setup</p>
      <h3 class="mt-4 font-display text-3xl text-ink">每场 3-5 题，一轮追问</h3>
      <div class="mt-6 grid gap-3">
        <div class="rounded-3xl border border-black/5 bg-white/70 p-4">
          <div class="text-xs uppercase tracking-[0.24em] text-slate-500">Direction</div>
          <div class="mt-2 font-semibold">Spring</div>
        </div>
        <div class="rounded-3xl border border-black/5 bg-white/70 p-4">
          <div class="text-xs uppercase tracking-[0.24em] text-slate-500">Current Question</div>
          <div class="mt-2 font-semibold">解释 Spring AOP 的底层实现原理</div>
        </div>
      </div>
    </section>

    <section class="paper-panel p-6">
      <p class="section-kicker">Scoring Response</p>
      <div class="mt-5 rounded-[30px] bg-ink p-6 text-white">
        <div class="text-xs uppercase tracking-[0.24em] text-white/60">AI Feedback</div>
        <div class="mt-3 font-display text-5xl">78</div>
        <p class="mt-4 text-sm leading-7 text-white/80">
          点评、标准答案和单次追问都已经在接口基线上固定。后续联调时，只需要把这个展示壳接到 `/api/interview/answer`。
        </p>
      </div>
      <div class="mt-4 grid gap-3 md:grid-cols-2">
        <div class="rounded-3xl border border-black/5 bg-white/70 p-4">
          <div class="font-semibold">标准答案</div>
          <p class="mt-2 text-sm leading-6 text-slate-500">包含 JDK 动态代理、CGLIB 及使用边界。</p>
        </div>
        <div class="rounded-3xl border border-black/5 bg-white/70 p-4">
          <div class="font-semibold">自动错题</div>
          <p class="mt-2 text-sm leading-6 text-slate-500">低分题直接沉淀到 `wrong_question`。</p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
</script>

