const baseUrl = '/api/dot-giam-gia'

export const fetchDotGiamGiaPage = async ({
  keyword = '',
  trangThai = '',
  tuNgay = '',
  denNgay = '',
  page = 0,
  size = 10
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
    throw new Error(await response.text())
  }

  return await response.json()
}

export const deleteDotGiamGia = async (id) => {
  const response = await fetch(`${baseUrl}/${id}`, {
    method: 'DELETE'
  })

  if (!response.ok) {
    throw new Error(await response.text())
  }

  return await response.json()
}
