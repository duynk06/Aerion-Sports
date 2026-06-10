// LichSuThanhToanService.js

const API =
  "http://localhost:8080/lich-su-thanh-toan"

export const getLichSuThanhToan = async (idHoaDon) => {

  const response = await fetch(
    `${API}/hoa-don/${idHoaDon}`
  )

  if (!response.ok) {
    throw new Error(await response.text())
  }

  return await response.json()
}