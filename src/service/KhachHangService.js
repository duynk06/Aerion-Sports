const baseUrl = "http://localhost:8080"

// ====================
// LẤY DANH SÁCH KHÁCH HÀNG
// ====================
export const fetchAllKhachHang = async () => {
  try {
    const response = await fetch(
      `${baseUrl}/public/khach-hang/hien-thi`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(
        `Lỗi ${response.status}: ${errorText || 'Không thể tải dữ liệu'}`
      )
    }

    return await response.json()
  } catch (error) {
    console.error('Lỗi fetchAllKhachHang:', error)
    throw error
  }
}

// ====================
// THÊM KHÁCH HÀNG
// ====================
export const addKhachHang = async (khachHangData) => {
  try {
    const response = await fetch(
      `${baseUrl}/public/khach-hang/add`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        }
      },
      body, JSON.stringify(khachHangData)
    )

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(
        `Lỗi ${response.status}: ${errorText || 'Không thể thêm khách hàng'}`
      )
    }

    // 🌟 SỬA LỖI: Phòng trường hợp Backend trả về chuỗi trống hoặc không có body dữ liệu
    const text = await response.text()
    return text ? JSON.parse(text) : { success: true }
  } catch (error) {
    console.error('Lỗi addKhachHang:', error)
    throw error
  }
}

// ====================
// LẤY CHI TIẾT KHÁCH HÀNG
// ====================
export const fetchKhachHangById = async (id) => {
  try {
    const response = await fetch(
      `${baseUrl}/public/khach-hang/${id}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )

    if (!response.ok) {
      throw new Error(
        `Lỗi ${response.status}: Không thể lấy chi tiết khách hàng`
      )
    }

    return await response.json()
  } catch (error) {
    console.error('Lỗi fetchKhachHangById:', error)
    throw error
  }
}

// ====================
// CẬP NHẬT KHÁCH HÀNG
// ====================
export const updateKhachHang = async (id, khachHangData) => {
  try {
    const response = await fetch(
      `${baseUrl}/public/khach-hang/update/${id}`,
      {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(khachHangData)
      }
    )

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(
        `Lỗi ${response.status}: ${errorText || 'Không thể cập nhật'}`
      )
    }

    // 🌟 SỬA LỖI: Tránh lỗi "Unexpected end of JSON input" nếu Backend phản hồi trống
    const text = await response.text()
    return text ? JSON.parse(text) : { success: true }
  } catch (error) {
    console.error('Lỗi updateKhachHang:', error)
    throw error
  }
}

// ====================
// XÓA KHÁCH HÀNG
// ====================
export const deleteKhachHang = async (id) => {
  try {
    const response = await fetch(
      `${baseUrl}/public/khach-hang/delete/${id}`,
      {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(
        `Lỗi ${response.status}: ${errorText || 'Không thể xóa'}`
      )
    }

    const text = await response.text()
    return text || 'Xóa thành công'
  } catch (error) {
    console.error('Lỗi deleteKhachHang:', error)
    throw error
  }
}