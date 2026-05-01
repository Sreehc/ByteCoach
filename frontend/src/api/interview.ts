import { request } from '@/utils/http'
import type { InterviewAnswerResult, InterviewCurrentQuestion, InterviewDetail } from '@/types/api'

export interface InterviewStartPayload {
  direction: string
  questionCount?: number
}

export interface InterviewAnswerPayload {
  sessionId: number
  questionId: number
  answer: string
}

export const startInterviewApi = (payload: InterviewStartPayload) => {
  return request<InterviewCurrentQuestion>({ url: '/interview/start', method: 'post', data: payload })
}

export const currentQuestionApi = (sessionId: number) => {
  return request<InterviewCurrentQuestion>({ url: `/interview/current/${sessionId}`, method: 'get' })
}

export const submitAnswerApi = (payload: InterviewAnswerPayload) => {
  return request<InterviewAnswerResult>({ url: '/interview/answer', method: 'post', data: payload })
}

export const interviewDetailApi = (sessionId: number) => {
  return request<InterviewDetail>({ url: `/interview/detail/${sessionId}`, method: 'get' })
}
