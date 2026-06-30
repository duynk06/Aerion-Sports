import axios from 'axios'

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
export const PROVINCES_API_BASE_URL = 'https://provinces.open-api.vn/api'

// Axios client dùng chung cho các API nội bộ của backend
export const apiClient = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: {
    'Content-Type': 'application/json',
  },
})

apiClient.interceptors.request.use((config) => {
  const token = typeof window !== 'undefined'
    ? window.localStorage.getItem('aerion_client_token')
    : null
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// API sản phẩm
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

// API địa chỉ hành chính từ provinces.open-api.vn
export const getProvinces = () => {
  return axios.get(`${PROVINCES_API_BASE_URL}/p/`)
}

export const getDistrictsByProvinceCode = (provinceCode) => {
  return axios.get(`${PROVINCES_API_BASE_URL}/p/${provinceCode}?depth=2`)
}

export const getWardsByDistrictCode = (districtCode) => {
  return axios.get(`${PROVINCES_API_BASE_URL}/d/${districtCode}?depth=2`)
}

// API phiếu giảm giá
export const getVoucherList = () => {
  return axios.get(`${API_BASE_URL}/phieu-giam-gia/hien-thi`)
}

// Tạo khách hàng vãng lai khi checkout
export const createGuestCustomer = (payload) => {
  return axios.post(`${API_BASE_URL}/public/khach-hang/add`, payload)
}

export const createOnlineOrder = (payload) => {
  return axios.post(`${API_BASE_URL}/public/online-orders/checkout`, payload)
}

export const getOnlineOrderByCode = (maHoaDon) => {
  return axios.get(`${API_BASE_URL}/public/online-orders/${encodeURIComponent(maHoaDon)}`)
}

export const getMyOnlineOrders = () => {
  const token = typeof window !== 'undefined'
    ? window.localStorage.getItem('aerion_client_token')
    : null

  return axios.get(`${API_BASE_URL}/public/online-orders/me`, {
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  })
}

export const loginOnlineCustomer = (payload) => {
  return axios.post(`${API_BASE_URL}/public/client-auth/login`, payload)
}

export const registerOnlineCustomer = (payload) => {
  return axios.post(`${API_BASE_URL}/public/client-auth/register`, payload)
}

export const getOnlineCustomerProfile = () => {
  const token = typeof window !== 'undefined'
    ? window.localStorage.getItem('aerion_client_token')
    : null

  return axios.get(`${API_BASE_URL}/public/client-auth/me`, {
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  })
}

export const updateOnlineCustomerProfile = (payload) => {
  const token = typeof window !== 'undefined'
    ? window.localStorage.getItem('aerion_client_token')
    : null

  return axios.put(`${API_BASE_URL}/public/client-auth/me`, payload, {
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  })
}

// Resolve đường dẫn media tương đối sang URL đầy đủ
export const resolveMediaUrl = (path) => {
  if (!path) return ''
  if (/^(https?:)?\/\//.test(path) || path.startsWith('data:')) return path
  return `${API_BASE_URL}${path.startsWith('/') ? path : `/${path}`}`
}
