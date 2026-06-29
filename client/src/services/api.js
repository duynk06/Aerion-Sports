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

// Resolve đường dẫn media tương đối sang URL đầy đủ
export const resolveMediaUrl = (path) => {
  if (!path) return ''
  if (/^(https?:)?\/\//.test(path) || path.startsWith('data:')) return path
  return `${API_BASE_URL}${path.startsWith('/') ? path : `/${path}`}`
}
