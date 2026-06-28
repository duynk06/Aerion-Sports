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
  
    if (
      loaiHoaDon !== null &&
      loaiHoaDon !== undefined
    ) {
      params.append(
        "loaiHoaDon",
        loaiHoaDon
      )
    }
  
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
      throw new Error(await response.text())
    }
  
    return await response.json()
  }
    export const getHoaDonById = async (id) => {
    const response = await fetch(
      `${baseUrl}/${id}`
    )
  
    if (!response.ok) {
      throw new Error(
        response.status +
        ': ' +
        await response.text()
      )
    }
  
    return await response.json()
  }
  // HoaDonService.js — thêm hàm
  export const chuyenTrangThaiHoaDon = async (id, trangThaiMoi, ghiChu = '') => {
    const params = new URLSearchParams({ trangThaiMoi });
    if (ghiChu) params.append('ghiChu', ghiChu);
  
    const response = await fetch(
        `http://localhost:8080/hoa-don/${id}/chuyen-trang-thai?${params}`,
        { method: 'PUT' }
    );
    if (!response.ok) {
        const msg = await response.text();
        throw new Error(msg);
    }
    return await response.json();
  }