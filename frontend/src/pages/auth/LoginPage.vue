<template>
  <div class="flex min-h-screen items-center justify-center px-4 py-8">
    <div class="grid w-full max-w-6xl gap-5 lg:grid-cols-[1.1fr_0.9fr]">
      <section class="paper-panel relative overflow-hidden p-8 md:p-12">
        <div class="absolute -left-16 top-10 h-48 w-48 rounded-full bg-ember/20 blur-3xl"></div>
        <div class="absolute bottom-0 right-0 h-64 w-64 rounded-full bg-pine/20 blur-3xl"></div>
        <p class="section-kicker relative">ByteCoach</p>
        <h1 class="page-title relative mt-5 max-w-xl">
          进入一个围绕 Java 面试准备设计的学习驾驶舱，而不是普通聊天页。
        </h1>
        <p class="page-subtitle relative mt-5">
          登录之后，首页会把学习概览、最近面试、错题数量、薄弱点和计划完成率放进同一个工作台，帮助你决定下一步该做什么。
        </p>

        <div class="relative mt-12 grid gap-4 md:grid-cols-3">
          <article class="metric-card">
            <p class="metric-label">Core Loop</p>
            <p class="metric-value">6</p>
            <p class="mt-2 text-sm text-slate-500">登录、问答、面试、错题、计划、Dashboard</p>
          </article>
          <article class="metric-card">
            <p class="metric-label">Dashboard</p>
            <p class="metric-value">Live</p>
            <p class="mt-2 text-sm text-slate-500">学习概览、面试结果、薄弱点与快捷入口统一呈现</p>
          </article>
          <article class="metric-card">
            <p class="metric-label">Auth</p>
            <p class="metric-value">JWT</p>
            <p class="mt-2 text-sm text-slate-500">注册、登录、恢复登录态、退出登录形成完整闭环</p>
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
          <span>登录后直接进入 Dashboard 总览</span>
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
  } catch {
    // Message is handled by the request interceptor.
  } finally {
    loading.value = false
  }
}
</script>
