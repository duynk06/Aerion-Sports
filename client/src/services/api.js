import axios from 'axios'

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const getProductsPage = (params = {}) => {
  return apiClient.get('/san-pham/search', {
    params: {
      ...params,
      _ts: Date.now(),
    },
  })
}

export const getProductDetail = (id) => {
  return apiClient.get(`/san-pham/detail/${id}`, {
    params: {
      _ts: Date.now(),
    },
  })
}

export const resolveMediaUrl = (path) => {
  if (!path) return ''
  if (/^(https?:)?\/\//.test(path) || path.startsWith('data:')) return path
  return `${API_BASE_URL}${path.startsWith('/') ? path : `/${path}`}`
}
