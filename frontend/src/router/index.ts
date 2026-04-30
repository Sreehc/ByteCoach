import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '@/layouts/AppLayout.vue'
import AdminPage from '@/pages/admin/AdminPage.vue'
import LoginPage from '@/pages/auth/LoginPage.vue'
import RegisterPage from '@/pages/auth/RegisterPage.vue'
import ChatPage from '@/pages/chat/ChatPage.vue'
import DashboardPage from '@/pages/dashboard/DashboardPage.vue'
import InterviewPage from '@/pages/interview/InterviewPage.vue'
import KnowledgePage from '@/pages/knowledge/KnowledgePage.vue'
import NotFoundPage from '@/pages/NotFoundPage.vue'
import PlanPage from '@/pages/plan/PlanPage.vue'
import WrongPage from '@/pages/wrong/WrongPage.vue'
import { useAuthStore } from '@/stores/auth'

const pageMeta = {
  dashboard: {
    kicker: 'Learning Overview',
    title: 'Your Java interview cockpit',
    subtitle: '把最近的学习行为、面试结果、错题和计划压缩成一块可操作的主看板。'
  },
  chat: {
    kicker: 'Guided Q&A',
    title: 'Ask with context, not just chat',
    subtitle: '普通问答和知识问答放在同一入口，方便后续把问题直接带回复习链路。'
  },
  knowledge: {
    kicker: 'Knowledge Base',
    title: 'A built-in library for interview recall',
    subtitle: '阶段 2 继续用内置资料库，先保证检索和引用体验稳定。'
  },
  interview: {
    kicker: 'Mock Interview',
    title: 'Simulate the pressure before the real round',
    subtitle: '每场控制在 3-5 题，通过评分、点评和追问把问题沉淀到后续复习。'
  },
  wrong: {
    kicker: 'Wrong Book',
    title: 'Turn weak answers into review assets',
    subtitle: '错题不只是归档，而是驱动下一步复习与计划生成的基础资产。'
  },
  plan: {
    kicker: 'Study Plan',
    title: 'Convert weak points into daily execution',
    subtitle: '计划模块把薄弱点和错题拆成可完成的节奏，而不是停留在泛泛建议。'
  },
  admin: {
    kicker: 'Admin Entry',
    title: 'Operate content without splitting the product',
    subtitle: '后台仍然嵌在同一个应用里，先服务题库、知识库和分类管理。'
  }
} as const

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: AppLayout,
      meta: { requiresAuth: true },
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: DashboardPage,
          meta: pageMeta.dashboard
        },
        {
          path: 'chat',
          name: 'chat',
          component: ChatPage,
          meta: pageMeta.chat
        },
        {
          path: 'knowledge',
          name: 'knowledge',
          component: KnowledgePage,
          meta: pageMeta.knowledge
        },
        {
          path: 'interview',
          name: 'interview',
          component: InterviewPage,
          meta: pageMeta.interview
        },
        {
          path: 'wrong',
          name: 'wrong',
          component: WrongPage,
          meta: pageMeta.wrong
        },
        {
          path: 'plan',
          name: 'plan',
          component: PlanPage,
          meta: pageMeta.plan
        },
        {
          path: 'admin',
          name: 'admin',
          component: AdminPage,
          meta: pageMeta.admin
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: LoginPage,
      meta: { guestOnly: true }
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterPage,
      meta: { guestOnly: true }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: NotFoundPage
    }
  ]
})

let restored = false

router.beforeEach(async (to) => {
  const authStore = useAuthStore()

  if (!restored && authStore.token) {
    restored = true
    await authStore.restoreProfile()
  }

  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    return { path: '/login', query: { redirect: to.fullPath } }
  }

  if (to.meta.guestOnly && authStore.isLoggedIn) {
    return '/dashboard'
  }

  return true
})

export default router
