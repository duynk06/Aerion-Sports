const baseUrl = "http://localhost:8080/chi-tiet-hoa-don"

export const getChiTietHoaDon = async (idHoaDon) => {

    const response = await fetch(
        `${baseUrl}/chi-tiet/${idHoaDon}`
    )

    if (!response.ok) {
        throw new Error(
            response.status + ": " + await response.text()
        )
    }

    return await response.json()
}