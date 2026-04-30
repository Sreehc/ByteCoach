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
          component: DashboardPage
        },
        {
          path: 'chat',
          name: 'chat',
          component: ChatPage
        },
        {
          path: 'knowledge',
          name: 'knowledge',
          component: KnowledgePage
        },
        {
          path: 'interview',
          name: 'interview',
          component: InterviewPage
        },
        {
          path: 'wrong',
          name: 'wrong',
          component: WrongPage
        },
        {
          path: 'plan',
          name: 'plan',
          component: PlanPage
        },
        {
          path: 'admin',
          name: 'admin',
          component: AdminPage
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

