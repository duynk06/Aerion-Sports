const baseUrl = "http://localhost:8080";

/**
 * 1. Lấy toàn bộ danh sách nhân viên từ SQL Server
 */
export const fetchAllNhanVien = async () => {
  try {
    const response = await fetch(`${baseUrl}/nhan-vien/hien-thi`, {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Lỗi ${response.status}: ${errorText || 'Không thể tải dữ liệu nhân viên'}`);
    }

    const data = await response.json();
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error("Lỗi khi gọi API fetchAllNhanVien:", error);
    throw error;
  }
};

/**
 * 2. Cập nhật thông tin nhân viên (Gồm thông tin cá nhân + mảng địa chỉ lồng bên trong)
 */
export const updateNhanVien = async (id, nhanVienData) => {
  try {
    const response = await fetch(`${baseUrl}/nhan-vien/update/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(nhanVienData)
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Lỗi ${response.status}: ${errorText || 'Không thể cập nhật nhân viên'}`);
    }

    return await response.json();
  } catch (error) {
    console.error(`Lỗi khi gọi API updateNhanVien (ID: ${id}):`, error);
    throw error;
  }
};

/**
 * 3. Thay đổi trạng thái hoạt động nhanh của nhân viên
 */
export const changeStatusNhanVien = async (id, trangThai) => {
  try {
    const response = await fetch(`${baseUrl}/nhan-vien/doi-trang-thai/${id}?trangThai=${trangThai}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' }
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Lỗi ${response.status}: ${errorText || 'Không thể đổi trạng thái'}`);
    }

    return true;
  } catch (error) {
    console.error(`Lỗi khi gọi API changeStatusNhanVien (ID: ${id}):`, error);
    throw error;
  }
};

/**
 * 4. Thêm địa chỉ mới trực tiếp cho nhân viên từ Modal
 * 💡 TỐI ƯU: Truyền thẳng object nhân viên hiện tại (currentNv) từ giao diện vào để tránh fetch lại toàn bộ DB
 */
export const addAddressByNhanVienId = async (currentNv, addressPayload) => {
  try {
    if (!currentNv) {
      throw new Error("Thông tin nhân viên không hợp lệ!");
    }

    // Nhân bản sâu (Deep clone) tránh làm biến đổi trực tiếp trạng thái trên UI khi chưa lưu thành công
    const updatedNv = JSON.parse(JSON.stringify(currentNv));
    const nhanVienId = updatedNv.id || updatedNv.idNhanVien;

    if (!Array.isArray(updatedNv.addresses)) {
      updatedNv.addresses = [];
    }

    const newAddr = {
      ...addressPayload,
      id: addressPayload.id || 'ADDR_' + Date.now()
    };

    // Nếu đặt địa chỉ mới làm mặc định, tắt mặc định của các địa chỉ cũ
    if (newAddr.isDefault) {
      updatedNv.addresses.forEach(addr => addr.isDefault = false);
      updatedNv.addresses.unshift(newAddr); // Đẩy địa chỉ mặc định lên đầu mảng
    } else {
      updatedNv.addresses.push(newAddr);
    }

    // Đồng bộ trường văn bản diaChi chính theo địa chỉ mặc định mới
    updatedNv.diaChi = newAddr.isDefault ? (newAddr.chiTiet || `Số ${newAddr.chiTietCuThe || ''}, Phường ${newAddr.phuongXa || ''}, ${newAddr.tinhThanh || ''}`) : updatedNv.diaChi;

    // Chuẩn hóa cấu trúc vai trò tương thích với Spring Boot
    let roleId = 3;
    if (updatedNv.vaiTro && typeof updatedNv.vaiTro === 'object') {
      roleId = updatedNv.vaiTro.id;
    } else if (updatedNv.vaiTro) {
      roleId = updatedNv.vaiTro;
    }

    const payload = {
      ...updatedNv,
      vaiTro: { id: Number(roleId) }
    };

    return await updateNhanVien(nhanVienId, payload);
  } catch (error) {
    console.error(`Lỗi tại addAddressByNhanVienId:`, error);
    throw error;
  }
};

/**
 * 5. Cập nhật thông tin hoặc Đặt địa chỉ làm mặc định từ Modal
 * 💡 ĐÃ SỬA LỖI: Sửa tận gốc logic map trạng thái isDefault không làm mất thông tin cũ
 */
export const updateAddress = async (currentNv, addressId, addressPayload) => {
  try {
    if (!currentNv || !Array.isArray(currentNv.addresses)) {
      throw new Error("Dữ liệu nhân viên hoặc danh sách địa chỉ bị trống!");
    }

    const updatedNv = JSON.parse(JSON.stringify(currentNv));
    const nhanVienId = updatedNv.id || updatedNv.idNhanVien;

    // Sửa lỗi: Duyệt mảng chuẩn xác, chỉ đổi trạng thái isDefault khi addressPayload có yêu cầu đặt mặc định
    updatedNv.addresses = updatedNv.addresses.map(addr => {
      if (addr.id === addressId) {
        const mergedAddr = { ...addr, ...addressPayload };
        // Nếu địa chỉ này được đặt làm mặc định, đồng bộ text ra trường diaChi của Nhân viên
        if (mergedAddr.isDefault) {
          updatedNv.diaChi = mergedAddr.chiTiet || `Số ${mergedAddr.chiTietCuThe || ''}, Phường ${mergedAddr.phuongXa || ''}, ${mergedAddr.tinhThanh || ''}`;
        }
        return mergedAddr;
      }
      
      // Nếu địa chỉ đang sửa được set làm mặc định, các địa chỉ khác tự động hủy mặc định
      if (addressPayload.isDefault === true) {
        return { ...addr, isDefault: false };
      }
      return addr;
    });

    let roleId = 3;
    if (updatedNv.vaiTro && typeof updatedNv.vaiTro === 'object') {
      roleId = updatedNv.vaiTro.id;
    } else if (updatedNv.vaiTro) {
      roleId = updatedNv.vaiTro;
    }

    const payload = {
      ...updatedNv,
      vaiTro: { id: Number(roleId) }
    };

    return await updateNhanVien(nhanVienId, payload);
  } catch (error) {
    console.error(`Lỗi tại updateAddress (ID Địa chỉ: ${addressId}):`, error);
    throw error;
  }
};

/**
 * 6. Xóa vĩnh viễn một địa chỉ khỏi mảng lưu trữ của nhân viên
 */
export const deleteAddress = async (currentNv, addressId) => {
  try {
    if (!currentNv || !Array.isArray(currentNv.addresses)) {
      throw new Error("Không tìm thấy thông tin địa chỉ hợp lệ để xóa!");
    }

    const updatedNv = JSON.parse(JSON.stringify(currentNv));
    const nhanVienId = updatedNv.id || updatedNv.idNhanVien;

    const wasDefault = updatedNv.addresses.find(addr => addr.id === addressId)?.isDefault;
    
    // Lọc bỏ phần tử ra khỏi mảng địa chỉ
    updatedNv.addresses = updatedNv.addresses.filter(addr => addr.id !== addressId);

    // Nếu xóa đúng dòng đang mặc định, tự động gán quyền mặc định cho dòng đầu tiên còn lại
    if (wasDefault && updatedNv.addresses.length > 0) {
      updatedNv.addresses[0].isDefault = true;
      const primaryAddr = updatedNv.addresses[0];
      updatedNv.diaChi = primaryAddr.chiTiet || `Số ${primaryAddr.chiTietCuThe || ''}, Phường ${primaryAddr.phuongXa || ''}, ${primaryAddr.tinhThanh || ''}`;
    } else if (updatedNv.addresses.length === 0) {
      updatedNv.diaChi = ''; // Nếu xóa sạch địa chỉ, xóa trắng văn bản diaChi tổng
    }

    let roleId = 3;
    if (updatedNv.vaiTro && typeof updatedNv.vaiTro === 'object') {
      roleId = updatedNv.vaiTro.id;
    } else if (updatedNv.vaiTro) {
      roleId = updatedNv.vaiTro;
    }

    const payload = {
      ...updatedNv,
      vaiTro: { id: Number(roleId) }
    };

    return await updateNhanVien(nhanVienId, payload);
  } catch (error) {
    console.error(`Lỗi tại deleteAddress (ID Địa chỉ: ${addressId}):`, error);
    throw error;
  }
};