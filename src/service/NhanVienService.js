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
      throw new Error(`Lỗi ${response.status}: ${errorText || 'Không thể tải danh sách nhân viên'}`);
    }

    const data = await response.json();
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error("Lỗi tại fetchAllNhanVien:", error);
    throw error;
  }
};

/**
 * 2. Cập nhật thông tin tổng thể của nhân viên
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
    console.error(`Lỗi tại updateNhanVien (ID: ${id}):`, error);
    throw error;
  }
};

/**
 * 3. Thay đổi trạng thái hoạt động nhanh của nhân viên
 */
export const changeStatusNhanVien = async (id, trangThai) => {
  try {
    // URL khớp chuẩn cấu trúc: /nhan-vien/doi-trang-thai/{id}?trangThai={value}
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
    console.error(`Lỗi tại changeStatusNhanVien (ID: ${id}):`, error);
    throw error;
  }
};

/**
 * 4. Thêm địa chỉ mới từ Modal
 */
export const addAddressByNhanVienId = async (currentNv, addressPayload) => {
  try {
    if (!currentNv) throw new Error("Thông tin nhân viên không hợp lệ!");

    const updatedNv = JSON.parse(JSON.stringify(currentNv));
    const nhanVienId = updatedNv.id || updatedNv.idNhanVien;

    if (!Array.isArray(updatedNv.addresses)) {
      updatedNv.addresses = [];
    }

    const newAddr = {
      ...addressPayload,
      id: addressPayload.id || 'ADDR_' + Date.now()
    };

    if (newAddr.isDefault) {
      updatedNv.addresses.forEach(addr => addr.isDefault = false);
      updatedNv.addresses.unshift(newAddr);
    } else {
      updatedNv.addresses.push(newAddr);
    }

    // Đồng bộ trường text địa chỉ tổng của nhân viên
    if (newAddr.isDefault) {
      updatedNv.diaChi = newAddr.chiTiet || `Số ${newAddr.chiTietCuThe || ''}, Phường ${newAddr.phuongXa || ''}, ${newAddr.tinhThanh || ''}`;
    }

    // Đồng bộ cấu trúc vai trò phù hợp Jackson Map
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
 * 5. Cập nhật thông tin chi tiết địa chỉ hoặc thay đổi địa chỉ mặc định từ Modal
 */
export const updateAddress = async (currentNv, addressId, addressPayload) => {
  try {
    if (!currentNv || !Array.isArray(currentNv.addresses)) {
      throw new Error("Dữ liệu nhân viên hoặc danh sách địa chỉ bị trống!");
    }

    const updatedNv = JSON.parse(JSON.stringify(currentNv));
    const nhanVienId = updatedNv.id || updatedNv.idNhanVien;

    updatedNv.addresses = updatedNv.addresses.map(addr => {
      if (addr.id === addressId) {
        const mergedAddr = { ...addr, ...addressPayload };
        if (mergedAddr.isDefault) {
          updatedNv.diaChi = mergedAddr.chiTiet || `Số ${mergedAddr.chiTietCuThe || ''}, Phường ${mergedAddr.phuongXa || ''}, ${mergedAddr.tinhThanh || ''}`;
        }
        return mergedAddr;
      }
      
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
 * 6. Xóa vĩnh viễn địa chỉ khỏi nhân viên từ Modal
 */
export const deleteAddress = async (currentNv, addressId) => {
  try {
    if (!currentNv || !Array.isArray(currentNv.addresses)) {
      throw new Error("Không tìm thấy thông tin địa chỉ hợp lệ để xóa!");
    }

    const updatedNv = JSON.parse(JSON.stringify(currentNv));
    const nhanVienId = updatedNv.id || updatedNv.idNhanVien;

    const wasDefault = updatedNv.addresses.find(addr => addr.id === addressId)?.isDefault;
    updatedNv.addresses = updatedNv.addresses.filter(addr => addr.id !== addressId);

    if (wasDefault && updatedNv.addresses.length > 0) {
      updatedNv.addresses[0].isDefault = true;
      const primaryAddr = updatedNv.addresses[0];
      updatedNv.diaChi = primaryAddr.chiTiet || `Số ${primaryAddr.chiTietCuThe || ''}, Phường ${primaryAddr.phuongXa || ''}, ${primaryAddr.tinhThanh || ''}`;
    } else if (updatedNv.addresses.length === 0) {
      updatedNv.diaChi = ''; 
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