const baseUrl = "http://localhost:8080";

// Hàm lấy token từ nhiều vị trí lưu trữ
const getAuthHeaders = () => {
    const token = localStorage.getItem('token') || localStorage.getItem('accessToken') || sessionStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
    };
};

// --- LẤY DANH SÁCH KHÁCH HÀNG ---
export const fetchAllKhachHang = async () => {
    const response = await fetch(`${baseUrl}/public/khach-hang/hien-thi`, {
        method: 'GET',
        headers: getAuthHeaders()
    });
    if (!response.ok) throw new Error(`Lỗi ${response.status}: ${await response.text()}`);
    return await response.json();
};

// --- THÊM KHÁCH HÀNG ---
export const addKhachHang = async (khachHangData) => {
    // THÊM ĐOẠN LOG NÀY VÀO ĐÂY
    console.log("--- DEBUG DATA TRƯỚC KHI GỬI API ---");
    console.log("Dữ liệu đầy đủ:", khachHangData);
    console.log("Giá trị nguoi_nhan:", khachHangData.nguoi_nhan); 
    
    // Kiểm tra nhanh trước khi gửi
    if (khachHangData.nguoi_nhan === undefined || khachHangData.nguoi_nhan === null || khachHangData.nguoi_nhan === "") {
        console.error("LỖI: Trường 'nguoi_nhan' đang bị trống!");
    }

    const response = await fetch(`${baseUrl}/public/khach-hang/add`, {
        method: 'POST',
        headers: getAuthHeaders(),
        body: JSON.stringify(khachHangData)
    });
    
    if (!response.ok) {
        const errorText = await response.text();
        console.error("Lỗi từ server trả về:", errorText);
        throw new Error(`Lỗi ${response.status}: ${errorText}`);
    }
    return await response.json();
};

// --- LẤY CHI TIẾT KHÁCH HÀNG ---
export const fetchKhachHangById = async (id) => {
    const response = await fetch(`${baseUrl}/public/khach-hang/${id}`, {
        method: 'GET',
        headers: getAuthHeaders()
    });
    if (!response.ok) throw new Error(`Lỗi ${response.status}`);
    return await response.json();
};

// --- CẬP NHẬT KHÁCH HÀNG ---
export const updateKhachHang = async (id, khachHangData) => {
    const response = await fetch(`${baseUrl}/public/khach-hang/update/${id}`, {
        method: 'PUT',
        headers: getAuthHeaders(),
        body: JSON.stringify(khachHangData)
    });
    if (!response.ok) throw new Error(`Lỗi ${response.status}: ${await response.text()}`);
    return await response.json();
};

// --- XÓA KHÁCH HÀNG ---
export const deleteKhachHang = async (id) => {
    const response = await fetch(`${baseUrl}/public/khach-hang/delete/${id}`, {
        method: 'DELETE',
        headers: getAuthHeaders()
    });
    if (!response.ok) throw new Error(`Lỗi ${response.status}`);
    return 'Xóa thành công';
};