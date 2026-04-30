<template>
  <div class="grid gap-4 xl:grid-cols-[320px_minmax(0,1fr)]">
    <section class="paper-panel p-5">
      <p class="section-kicker">Sessions</p>
      <div class="mt-5 space-y-3">
        <button
          v-for="session in sessions"
          :key="session.id"
          class="surface-card surface-card-hover w-full px-4 py-4 text-left"
        >
          <div class="flex items-center justify-between">
            <span class="font-semibold">{{ session.title }}</span>
            <span class="text-xs uppercase tracking-[0.24em] text-slate-500">{{ session.mode }}</span>
          </div>
          <p class="mt-2 text-sm leading-6 text-slate-500">{{ session.preview }}</p>
        </button>
      </div>
    </section>

    <section class="paper-panel flex min-h-[520px] flex-col p-5">
      <p class="section-kicker">Chat</p>
      <div class="mt-5 flex-1 space-y-4">
        <div
          v-for="message in messages"
          :key="message.id"
          class="p-4"
          style="border-radius: var(--radius-md);"
          :class="message.role === 'assistant' ? 'surface-card' : 'bg-accent text-white shadow-[0_14px_30px_rgba(47,79,157,0.14)]'"
        >
          <div class="text-xs uppercase tracking-[0.24em]" :class="message.role === 'assistant' ? 'text-slate-500' : 'text-white/60'">
            {{ message.role }}
          </div>
          <div class="mt-3 text-sm leading-7">{{ message.content }}</div>
          <div v-if="message.references" class="surface-muted mt-4 px-3 py-3 text-xs text-slate-600">
            引用：{{ message.references }}
          </div>
        </div>
      </div>

      <div class="mt-4 grid gap-3 md:grid-cols-[140px_minmax(0,1fr)_120px]">
        <el-select model-value="rag" size="large">
          <el-option label="知识问答" value="rag" />
          <el-option label="普通问答" value="chat" />
        </el-select>
        <el-input type="textarea" :rows="3" placeholder="输入问题，后端默认发往 /api/chat/send" />
        <el-button type="primary" size="large" class="action-button transition active:translate-y-px">发送</el-button>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
const sessions = [
  { id: 1, title: 'Spring AOP 复习', mode: 'rag', preview: '引用知识片段返回结构已经固定。' },
  { id: 2, title: 'JVM 高频问答', mode: 'chat', preview: '通用问答和知识问答合并到一个模块。' }
]

const messages = [
  { id: 1, role: 'user', content: 'Spring AOP 的底层实现原理是什么？' },
  { id: 2, role: 'assistant', content: '常见实现方式是 JDK 动态代理和 CGLIB。', references: 'Spring 核心笔记 / chunk-101' }
]
</script>
