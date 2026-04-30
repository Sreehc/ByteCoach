<template>
  <div class="min-h-screen px-4 py-4 md:px-6 md:py-6">
    <div class="mx-auto grid max-w-[1480px] gap-4 lg:grid-cols-[300px_minmax(0,1fr)]">
      <NavRail class="min-h-[280px]" />

      <main class="space-y-4">
        <AppShellHeader
          kicker="Stage 1 Skeleton"
          title="A deliberate shell for the ByteCoach MVP"
          subtitle="页面优先承载流程感和模块边界：从问答、面试到错题与计划，都放在同一条学习动线上。"
          :name="displayName"
          :role="authStore.user?.role ?? 'USER'"
          :initials="initials"
        />

        <section class="paper-panel overflow-hidden p-4 md:p-6">
          <RouterView />
        </section>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import AppShellHeader from '@/components/AppShellHeader.vue'
import NavRail from '@/components/NavRail.vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const displayName = computed(() => authStore.user?.nickname || 'Visitor')
const initials = computed(() => displayName.value.slice(0, 1).toUpperCase())
</script>

