const baseUrl = 'http://localhost:8080/lich-su-hoa-don'

export const getLichSuHoaDon = async (idHoaDon) => {

  const response = await fetch(
    `${baseUrl}/hoa-don/${idHoaDon}`
  )

  if (!response.ok) {
    throw new Error(
      await response.text()
    )
  }

  return await response.json()
}