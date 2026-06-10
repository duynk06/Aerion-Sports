const baseUrl = '/api/dot-giam-gia'

// Parse response body for both JSON and plain-text error payloads.
const parseResponseBody = async (response) => {
  const contentType = response.headers.get('content-type') || ''

  if (contentType.includes('application/json')) {
    return await response.json()
  }

  const text = await response.text()
  return text ? text : null
}

// Normalize API errors so the UI can show a readable message.
const getErrorMessage = async (response) => {
  const body = await parseResponseBody(response)

  if (typeof body === 'string') {
    return body
  }

  if (body && typeof body === 'object') {
    return body.error || body.message || JSON.stringify(body)
  }

  return `Request failed with status ${response.status}`
}

// GET /api/dot-giam-gia
// Lấy danh sách đợt giảm giá có phân trang và bộ lọc.
export const fetchDotGiamGiaPage = async ({
  keyword = '',
  trangThai = '',
  tuNgay = '',
  denNgay = '',
  page = 0,
  size = 10,
} = {}) => {
  const params = new URLSearchParams()

  if (keyword) {
    params.append('keyword', keyword)
  }

  if (trangThai !== '' && trangThai !== null && trangThai !== undefined) {
    params.append('trangThai', trangThai)
  }

  if (tuNgay) {
    params.append('tuNgay', tuNgay)
  }

  if (denNgay) {
    params.append('denNgay', denNgay)
  }

  params.append('page', page)
  params.append('size', size)

  const response = await fetch(`${baseUrl}?${params.toString()}`)

  if (!response.ok) {
    throw new Error(await getErrorMessage(response))
  }

  return await response.json()
}

// GET /api/dot-giam-gia/{id}
// Lấy chi tiết một đợt giảm giá để hiển thị/chỉnh sửa.
export const fetchDotGiamGiaById = async (id) => {
  const response = await fetch(`${baseUrl}/${id}`)

  if (!response.ok) {
    throw new Error(await getErrorMessage(response))
  }

  return await response.json()
}

// PUT /api/dot-giam-gia/{id}
// Cập nhật thông tin đợt giảm giá theo dữ liệu form.
export const updateDotGiamGia = async (id, payload) => {
  const response = await fetch(`${baseUrl}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response))
  }

  return await response.json()
}

// POST /api/dot-giam-gia
// Tạo mới một đợt giảm giá kèm danh sách sản phẩm áp dụng.
export const createDotGiamGia = async (payload) => {
  const response = await fetch(baseUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response))
  }

  return await response.json()
}

// GET /api/dot-giam-gia/san-pham
// Lấy danh sách sản phẩm chi tiết để chọn áp dụng giảm giá.
export const fetchDotGiamGiaProducts = async (keyword = '') => {
  const params = new URLSearchParams()

  if (keyword) {
    params.append('keyword', keyword)
  }

  const query = params.toString()
  const response = await fetch(`${baseUrl}/san-pham${query ? `?${query}` : ''}`)

  if (!response.ok) {
    throw new Error(await getErrorMessage(response))
  }

  return await response.json()
}

// DELETE /api/dot-giam-gia/{id}
// Xóa mềm đợt giảm giá bằng cách chuyển trạng thái sang đã hủy.
export const deleteDotGiamGia = async (id) => {
  const response = await fetch(`${baseUrl}/${id}`, {
    method: 'DELETE',
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response))
  }

  return await parseResponseBody(response)
}
