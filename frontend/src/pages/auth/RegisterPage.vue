<template>
  <div class="flex min-h-screen items-center justify-center px-4 py-8">
    <section class="paper-panel w-full max-w-2xl p-8 md:p-10">
      <p class="section-kicker">Create Account</p>
      <h1 class="mt-4 font-display text-4xl text-ink">创建一个用于演示和联调的账号</h1>
      <p class="mt-4 max-w-xl text-sm leading-7 text-slate-600">
        注册页目前只接最小字段：`username`、`password`、`nickname`。角色、头像、来源等信息在后端按默认值处理。
      </p>

      <el-form ref="formRef" :model="form" :rules="rules" class="mt-8" label-position="top" @submit.prevent>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="ByteCoach" size="large" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="demo" size="large" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="123456" size="large" />
        </el-form-item>
        <div class="flex flex-col gap-3 pt-4 md:flex-row">
          <el-button :loading="loading" type="primary" size="large" class="!bg-ink !border-ink md:min-w-40" @click="handleRegister">
            注册
          </el-button>
          <RouterLink to="/login" class="inline-flex items-center text-sm text-ember">已有账号，返回登录</RouterLink>
        </div>
      </el-form>
    </section>
  </div>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  nickname: 'ByteCoach',
  username: 'demo',
  password: '123456'
})

const rules: FormRules<typeof form> = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleRegister = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await authStore.register(form)
    ElMessage.success('注册成功，请登录')
    await router.push('/login')
  } finally {
    loading.value = false
  }
}
</script>

