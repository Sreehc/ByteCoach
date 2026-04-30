export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

export interface UserInfo {
  id: number
  username: string
  nickname: string
  avatar?: string
  role: string
}

export interface LoginResponse {
  token: string
  userInfo: UserInfo
}

export interface RecentInterviewItem {
  sessionId: number
  direction: string
  totalScore: number
  status: string
  finishedAt: string
}

export interface WeakPointItem {
  categoryName: string
  wrongCount: number
  score: number
}

export interface DashboardOverview {
  learningCount: number
  averageScore: number
  wrongCount: number
  planCompletionRate: number
  recentInterviews: RecentInterviewItem[]
  weakPoints: WeakPointItem[]
  firstVisit: boolean
}
