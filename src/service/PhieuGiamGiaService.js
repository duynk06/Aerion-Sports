const baseUrl = "http://localhost:8080";

export const fetchAllPhieuGiamGia = async () => {
  try {
    const response = await fetch(`${baseUrl}/phieu-giam-gia/hien-thi`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Lỗi ${response.status}: ${errorText || 'Không thể tải dữ liệu'}`);
    }

    const data = await response.json();
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error("Lỗi khi gọi API fetchAllPhieuGiamGia:", error);
    throw error;
  }
};

export const createPhieuGiamGia = async (phieuData) => {
  console.log("DATA GUI DI:", phieuData)

  const response = await fetch(`${baseUrl}/phieu-giam-gia`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(phieuData)
  })

  console.log("STATUS:", response.status)

  const data = await response.json()
  console.log("RESPONSE:", data)

  return data
}

export const updatePhieuGiamGia = async (id, phieuData) => {
  const response = await fetch(`${baseUrl}/phieu-giam-gia/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(phieuData)
  })

  if (!response.ok) {
    const errorText = await response.text()
    throw new Error(`HTTP ${response.status}: ${errorText}`)
  }

  return await response.json()
}

export const deletePhieuGiamGia = async (id) => {
  const response = await fetch(`${baseUrl}/phieu-giam-gia/${id}`, {
    method: 'DELETE'
  })

  if (!response.ok) {
    const text = await response.text()
    throw new Error(`Lỗi ${response.status}: ${text}`)
  }

  return true
}
