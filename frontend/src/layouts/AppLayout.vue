<template>
  <div class="min-h-screen px-4 py-4 md:px-6 md:py-6">
    <div class="mx-auto grid max-w-[1480px] gap-4 lg:grid-cols-[300px_minmax(0,1fr)]">
      <NavRail class="min-h-[280px]" />

      <main class="space-y-4">
        <AppShellHeader
          :kicker="headerMeta.kicker"
          :title="headerMeta.title"
          :subtitle="headerMeta.subtitle"
          :name="displayName"
          :role="authStore.user?.role ?? 'USER'"
          :initials="initials"
          @logout="handleLogout"
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
import { useRoute } from 'vue-router'
import AppShellHeader from '@/components/AppShellHeader.vue'
import NavRail from '@/components/NavRail.vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
const route = useRoute()

const displayName = computed(() => authStore.user?.nickname || 'Visitor')
const initials = computed(() => displayName.value.slice(0, 1).toUpperCase())
const headerMeta = computed(() => {
  const meta = route.meta as { kicker?: string; title?: string; subtitle?: string }
  return {
    kicker: meta.kicker ?? 'ByteCoach',
    title: meta.title ?? 'Interview Studio',
    subtitle: meta.subtitle ?? '围绕学习闭环组织功能、状态和下一步动作。'
  }
})

const handleLogout = async () => {
  await authStore.logout()
}
</script>
