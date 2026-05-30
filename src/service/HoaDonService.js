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
        `${baseUrl}/search?keyword=${encodeURIComponent(keyword)}`
    )

    if (!response.ok) {
        throw new Error(response.status + ": " + await response.text())
    }

    return await response.json()
}