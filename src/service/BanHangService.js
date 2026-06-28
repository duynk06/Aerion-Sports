import axios from 'axios';

const baseUrl = "http://localhost:8080/ban-hang"

// BanHangService.js — sửa taoHoaDonCho để throw đúng message
export const taoHoaDonCho = async () => {
    const response = await fetch(`${baseUrl}/tao-hoa-don`, {
        method: "POST"
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText); // ✅ Throw message từ backend
    }

    return await response.json();
}
export const getSanPham = async (
    keyword = "",
    idMauSac = null,
    idTrongLuong = null,
    giaMin = null,
    giaMax = null,
    trangThai = null,
    page = 0,
    size = 5
) => {

    const params = new URLSearchParams()

    if(keyword) params.append("keyword", keyword)
    if(idMauSac) params.append("idMauSac", idMauSac)
    if(idTrongLuong) params.append("idTrongLuong", idTrongLuong)
    if(giaMin) params.append("giaMin", giaMin)
    if(giaMax) params.append("giaMax", giaMax)
    if(trangThai != null) params.append("trangThai", trangThai)

    params.append("page", page)
    params.append("size", size)

    const response = await fetch(
        `${baseUrl}/san-pham?${params}`
    )

    if(!response.ok){
        throw new Error(await response.text())
    }

    return await response.json()
}
const apiUrl = "http://localhost:8080"

export const getMauSac = async () => {
    const response = await fetch(
        `${apiUrl}/api/mau-sac/all`
    )

    if(!response.ok){
        throw new Error(await response.text())
    }

    return await response.json()
}

export const getTrongLuong = async () => {
    const response = await fetch(
        `${apiUrl}/api/trong-luong/all`
    )

    if(!response.ok){
        throw new Error(await response.text())
    }

    return await response.json()
}
export const getKhoangGia = async () => {

    const response = await fetch(
        `${baseUrl}/khoang-gia`
    )

    if(!response.ok){
        throw new Error(await response.text())
    }

    return await response.json()
}
export const getKhachHangPos = async (
    keyword = "",
    page = 0,
    size = 5
) => {
    const params = new URLSearchParams()

    if (keyword) params.append("keyword", keyword)
    params.append("page", page)
    params.append("size", size)

    const response = await fetch(
        `${baseUrl}/khach-hang?${params}`
    )

    if (!response.ok) {
        throw new Error(await response.text())
    }

    return await response.json()
}
// Thêm hàm này vào file BanHangService.js của bạn

export const updateKhachHangHoaDon = async (idHoaDon, idKhachHang) => {
    // Thay url tương đối bằng url tuyệt đối trỏ tới Backend
    const url = `http://localhost:8080/ban-hang/${idHoaDon}/khach-hang`; 
    
    const response = await axios.put(url, null, {
      params: { 
        idKhachHang: idKhachHang 
      }
    });
    return response.data;
  }
  export const themChiTietHoaDon = async (payload) => {
    const response = await fetch(`${baseUrl}/them-san-pham`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
    });

    if (!response.ok) {
        throw new Error(await response.text());
    }

    return await response.json();
}

// BanHangService.js — sửa lại toàn bộ hàm capNhatSoLuong
export const capNhatSoLuong = async (idChiTiet, soLuongMoi) => {
    const response = await fetch(
        `${baseUrl}/chi-tiet/${idChiTiet}/so-luong?soLuong=${soLuongMoi}`,
        {
            method: "PUT"
        }
    );

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
    }

    return await response.json();
}
  
// BanHangService.js — thêm 2 hàm
export const getHoaDonCho = async () => {
    const response = await fetch(`${baseUrl}/hoa-don-cho`);
    if (!response.ok) throw new Error(await response.text());
    return await response.json();
}

export const huyHoaDon = async (idHoaDon) => {
    const response = await fetch(
        `${baseUrl}/hoa-don/${idHoaDon}`,
        { method: "DELETE" }
    );
    if (!response.ok) {
        throw new Error(await response.text());
    }
}

// BanHangService.js — thêm hàm
export const xoaChiTietHoaDon = async (idChiTiet) => {
    const response = await fetch(
        `${baseUrl}/chi-tiet/${idChiTiet}`,
        { method: "DELETE" }
    );
    if (!response.ok) {
        throw new Error(await response.text());
    }
}

export const thanhToanHoaDon = async (payload) => {
    const response = await fetch(`${baseUrl}/thanh-toan`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
    if (!response.ok) {
        throw new Error(await response.text());
    }
    return await response.json();
}

export const capNhatLoaiHoaDon = async (idHoaDon, loaiHoaDon) => {
    const response = await fetch(
        `${baseUrl}/${idHoaDon}/loai-hoa-don?loaiHoaDon=${loaiHoaDon}`,
        { method: "PUT" }
    );
    if (!response.ok) throw new Error(await response.text());
    return await response.json();
}

export const capNhatPhiVanChuyen = async (idHoaDon, phiVanChuyen) => {
    const response = await fetch(
        `${baseUrl}/${idHoaDon}/phi-van-chuyen?phiVanChuyen=${phiVanChuyen}`,
        { method: "PUT" }
    );
    if (!response.ok) throw new Error(await response.text());
    return await response.json();
}

// Lấy danh sách địa chỉ của khách hàng
export const getDiaChiKhachHang = async (idKhachHang) => {
    const response = await fetch(
        `http://localhost:8080/ban-hang/khach-hang/${idKhachHang}/dia-chi`
    );
    if (!response.ok) throw new Error(await response.text());
    return await response.json();
}

// BanHangService.js — thêm 3 hàm
export const getPhieuGiamGiaTotNhat = async (idHoaDon) => {
    const response = await fetch(`${baseUrl}/${idHoaDon}/phieu-giam-gia-tot-nhat`);
    if (!response.ok) return null;
    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

export const apDungPhieuGiamGia = async (idHoaDon, idPhieu) => {
    const response = await fetch(
        `${baseUrl}/${idHoaDon}/ap-dung-phieu?idPhieu=${idPhieu}`,
        { method: "PUT" }
    );
    if (!response.ok) throw new Error(await response.text());
    return await response.json();
}

export const boPhieuGiamGia = async (idHoaDon) => {
    const response = await fetch(
        `${baseUrl}/${idHoaDon}/bo-phieu`,
        { method: "PUT" }
    );
    if (!response.ok) throw new Error(await response.text());
    return await response.json();
}


// BanHangService.js
export const kiemTraGiaSanPham = async (idHoaDon) => {
    const response = await fetch(`${baseUrl}/hoa-don/${idHoaDon}/kiem-tra-gia`);
    if (!response.ok) return [];
    return await response.json();
}