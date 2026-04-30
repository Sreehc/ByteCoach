import { request } from '@/utils/http'
import type { ApiResponse, LoginResponse, UserInfo } from '@/types/api'

export interface LoginPayload {
  username: string
  password: string
}

export interface RegisterPayload extends LoginPayload {
  nickname: string
}

export const loginApi = (payload: LoginPayload) => {
  return request<LoginResponse>({ url: '/auth/login', method: 'post', data: payload })
}

export const registerApi = (payload: RegisterPayload) => {
  return request<null>({ url: '/auth/register', method: 'post', data: payload })
}

export const fetchCurrentUserApi = () => {
  return request<UserInfo>({ url: '/user/me', method: 'get' })
}

export const logoutApi = () => {
  return request<null>({ url: '/auth/logout', method: 'post' })
}
