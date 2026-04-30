import { request } from '@/utils/http'
import type { DashboardOverview } from '@/types/api'

export const fetchDashboardOverviewApi = () => {
  return request<DashboardOverview>({ url: '/dashboard/overview', method: 'get' })
}

