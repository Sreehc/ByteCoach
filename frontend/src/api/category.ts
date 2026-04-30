import { request } from '@/utils/http'
import type { CategoryItem } from '@/types/api'

export interface CategoryPayload {
  id?: number
  name: string
  type: CategoryItem['type']
  sortOrder?: number
  status?: number
}

export const fetchCategoriesApi = (params?: { type?: CategoryItem['type'] }) => {
  return request<CategoryItem[]>({ url: '/category/list', method: 'get', params })
}

export const addCategoryApi = (payload: CategoryPayload) => {
  return request<CategoryItem>({ url: '/admin/category/add', method: 'post', data: payload })
}

export const updateCategoryApi = (payload: CategoryPayload) => {
  return request<CategoryItem>({ url: '/admin/category/update', method: 'put', data: payload })
}

export const deleteCategoryApi = (id: number) => {
  return request<null>({ url: `/admin/category/delete/${id}`, method: 'delete' })
}
