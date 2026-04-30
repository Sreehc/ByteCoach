<template>
  <div class="flex min-h-screen items-center justify-center px-4 py-8">
    <div class="grid w-full max-w-6xl gap-5 lg:grid-cols-[1.1fr_0.9fr]">
      <section class="paper-panel relative overflow-hidden p-8 md:p-12">
        <div class="absolute -left-16 top-10 h-48 w-48 rounded-full bg-ember/20 blur-3xl"></div>
        <div class="absolute bottom-0 right-0 h-64 w-64 rounded-full bg-pine/20 blur-3xl"></div>
        <p class="section-kicker relative">ByteCoach</p>
        <h1 class="page-title relative mt-5 max-w-xl">
          把 Java 面试准备收敛成一条可复用、可讲解、可演示的学习闭环。
        </h1>
        <p class="page-subtitle relative mt-5">
          阶段 1 先完成登录、问答、面试、错题、学习计划和 Dashboard 骨架。UI 保持轻量，但结构已经按后续联调节奏切好。
        </p>

        <div class="relative mt-12 grid gap-4 md:grid-cols-3">
          <article class="metric-card">
            <p class="metric-label">Core Loop</p>
            <p class="metric-value">6</p>
            <p class="mt-2 text-sm text-slate-500">登录、问答、面试、错题、计划、看板</p>
          </article>
          <article class="metric-card">
            <p class="metric-label">AI Shape</p>
            <p class="metric-value">1</p>
            <p class="mt-2 text-sm text-slate-500">单供应商 OpenAI-compatible gateway</p>
          </article>
          <article class="metric-card">
            <p class="metric-label">Admin</p>
            <p class="metric-value">/admin</p>
            <p class="mt-2 text-sm text-slate-500">同应用内嵌，不独立拆前端</p>
          </article>
        </div>
      </section>

      <section class="paper-panel p-8 md:p-10">
        <p class="section-kicker">Sign In</p>
        <h2 class="mt-4 font-display text-3xl text-ink">进入 ByteCoach</h2>
        <el-form ref="formRef" :model="form" :rules="rules" class="mt-8" label-position="top" @submit.prevent>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="demo" size="large" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password" show-password placeholder="123456" size="large" />
          </el-form-item>
          <el-button :loading="loading" type="primary" size="large" class="mt-4 w-full !bg-ink !border-ink" @click="handleLogin">
            登录
          </el-button>
        </el-form>
        <div class="mt-6 flex items-center justify-between text-sm text-slate-500">
          <span>默认对接 `/api/auth/login`</span>
          <RouterLink class="text-ember" to="/register">创建账号</RouterLink>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: 'demo',
  password: '123456'
})

const rules: FormRules<typeof form> = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await authStore.login(form)
    ElMessage.success('登录成功')
    await router.push((route.query.redirect as string) || '/dashboard')
  } finally {
    loading.value = false
  }
}
</script>

