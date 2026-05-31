const baseUrl = "http://localhost:8080/hoa-don"

export const fetchAllHoaDon = async () => {
    const response = await fetch(`${baseUrl}/hien-thi`)

    if (!response.ok) {
        throw new Error(response.status + ": " + await response.text())
    }

    return await response.json()
}
export const searchHoaDon = async (keyword) => {
    const response = await fetch(
      `http://localhost:8080/hoa-don/search?keyword=${keyword}`
    )
  
    if (!response.ok) {
      throw new Error(await response.text())
    }
  
    return await response.json()
  }
export const filterHoaDon = async (
    keyword,
    loaiHoaDon,
    trangThai,
    tuNgay,
    denNgay,
    page,
    size
  ) => {
  
    const params = new URLSearchParams()
  
    if (keyword)
      params.append("keyword", keyword)
  
    if (loaiHoaDon)
      params.append("loaiHoaDon", loaiHoaDon)
  
    if (
      trangThai !== '' &&
      trangThai !== null &&
      trangThai !== undefined
    ) {
      params.append(
        "trangThai",
        trangThai
      )
    }
  
    if (tuNgay)
      params.append("tuNgay", tuNgay)
  
    if (denNgay)
      params.append("denNgay", denNgay)
  
    params.append("page", page)
    params.append("size", size)
  
    const response = await fetch(
      `${baseUrl}/filter?${params}`
    )
  
    if (!response.ok) {
      throw new Error(
        await response.text()
      )
    }
  
    return await response.json()
  }