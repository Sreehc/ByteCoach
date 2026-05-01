import { request } from '@/utils/http'
import type { WrongQuestionItem } from '@/types/api'

export interface WrongMasteryPayload {
  masteryLevel: 'not_started' | 'reviewing' | 'mastered'
}

export const fetchWrongListApi = () => {
  return request<WrongQuestionItem[]>({ url: '/wrong/list', method: 'get' })
}

export const fetchWrongDetailApi = (id: number) => {
  return request<WrongQuestionItem>({ url: `/wrong/${id}`, method: 'get' })
}

export const updateMasteryApi = (id: number, payload: WrongMasteryPayload) => {
  return request<null>({ url: `/wrong/mastery/${id}`, method: 'put', data: payload })
}

export const deleteWrongApi = (id: number) => {
  return request<null>({ url: `/wrong/delete/${id}`, method: 'delete' })
}
