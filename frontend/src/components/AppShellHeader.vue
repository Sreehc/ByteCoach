<template>
  <header class="paper-panel flex flex-col gap-4 p-5 lg:flex-row lg:items-center lg:justify-between">
    <div>
      <p class="section-kicker">{{ kicker }}</p>
      <h2 class="page-title mt-3">{{ title }}</h2>
      <p class="page-subtitle mt-3">{{ subtitle }}</p>
    </div>

    <div class="flex flex-col gap-3 self-start sm:flex-row sm:items-center">
      <div class="surface-card flex items-center gap-3 px-3 py-2">
        <div class="flex size-10 items-center justify-center rounded-full bg-accent text-sm font-semibold text-white">
          {{ initials }}
        </div>
        <div>
          <div class="text-sm font-semibold">{{ name }}</div>
          <div class="text-xs uppercase tracking-[0.24em] text-slate-500">{{ role }}</div>
        </div>
      </div>

      <button
        type="button"
        class="hard-button-secondary"
        @click="$emit('logout')"
      >
        退出登录
      </button>
    </div>
  </header>
</template>

<script setup lang="ts">
defineEmits<{
  logout: []
}>()

defineProps<{
  kicker: string
  title: string
  subtitle: string
  name: string
  role: string
  initials: string
}>()
</script>
