const baseUrl = '/api/dot-giam-gia'

// 🌟 Helper tự động gắn Bearer token vào mọi request
const getAuthHeaders = (extra = {}) => {
  const token = localStorage.getItem('token')
  return {
    ...extra,
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  }
}

const parseResponseBody = async (response) => {
  const contentType = response.headers.get('content-type') || ''
  if (contentType.includes('application/json')) return await response.json()
  const text = await response.text()
  return text ? text : null
}

const getErrorMessage = async (response) => {
  const body = await parseResponseBody(response)
  if (typeof body === 'string') return body
  if (body && typeof body === 'object') return body.error || body.message || JSON.stringify(body)
  return `Request failed with status ${response.status}`
}

export const fetchDotGiamGiaPage = async ({
  keyword = '', trangThai = '', tuNgay = '', denNgay = '', page = 0, size = 10,
} = {}) => {
  const params = new URLSearchParams()
  if (keyword) params.append('keyword', keyword)
  if (trangThai !== '' && trangThai !== null && trangThai !== undefined) params.append('trangThai', trangThai)
  if (tuNgay) params.append('tuNgay', tuNgay)
  if (denNgay) params.append('denNgay', denNgay)
  params.append('page', page)
  params.append('size', size)

  const response = await fetch(`${baseUrl}?${params.toString()}`, {
    headers: getAuthHeaders(),
  })
  if (!response.ok) throw new Error(await getErrorMessage(response))
  return await response.json()
}

export const fetchDotGiamGiaById = async (id) => {
  const response = await fetch(`${baseUrl}/${id}`, {
    headers: getAuthHeaders(),
  })
  if (!response.ok) throw new Error(await getErrorMessage(response))
  return await response.json()
}

export const updateDotGiamGia = async (id, payload) => {
  const response = await fetch(`${baseUrl}/${id}`, {
    method: 'PUT',
    headers: getAuthHeaders({ 'Content-Type': 'application/json' }),
    body: JSON.stringify(payload),
  })
  if (!response.ok) throw new Error(await getErrorMessage(response))
  return await response.json()
}

export const updateDotGiamGiaTrangThai = async (id, trangThai) => {
  const params = new URLSearchParams()
  params.append('trangThai', trangThai)

  const response = await fetch(`${baseUrl}/${id}/trang-thai?${params.toString()}`, {
    method: 'PUT',
    headers: getAuthHeaders(),
  })
  if (!response.ok) throw new Error(await getErrorMessage(response))
  return await response.json()
}

export const createDotGiamGia = async (payload) => {
  const response = await fetch(baseUrl, {
    method: 'POST',
    headers: getAuthHeaders({ 'Content-Type': 'application/json' }),
    body: JSON.stringify(payload),
  })
  if (!response.ok) throw new Error(await getErrorMessage(response))
  return await response.json()
}

export const fetchDotGiamGiaProducts = async (keyword = '') => {
  const params = new URLSearchParams()
  if (keyword) params.append('keyword', keyword)
  const query = params.toString()

  const response = await fetch(`${baseUrl}/san-pham${query ? `?${query}` : ''}`, {
    headers: getAuthHeaders(),
  })
  if (!response.ok) throw new Error(await getErrorMessage(response))
  return await response.json()
}

export const deleteDotGiamGia = async (id) => {
  const response = await fetch(`${baseUrl}/${id}`, {
    method: 'DELETE',
    headers: getAuthHeaders(),
  })
  if (!response.ok) throw new Error(await getErrorMessage(response))
  return await parseResponseBody(response)
}