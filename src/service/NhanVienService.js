const baseUrl = "http://localhost:8080";

/**
 * 1. Lấy toàn bộ danh sách nhân viên từ SQL Server
 */
export const fetchAllNhanVien = async () => {
  try {
    const response = await fetch(`${baseUrl}/nhan-vien/hien-thi`, {
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
    console.error("Lỗi khi gọi API fetchAllNhanVien:", error);
    throw error;
  }
};

/**
 * 2. ⚡ THÊM MỚI: Gọi API thêm nhân viên để kích hoạt luồng tự cấp mật khẩu và gửi Email ngầm
 */
export const createNhanVien = async (nhanVienData) => {
  try {
    const response = await fetch(`${baseUrl}/nhan-vien/create`, {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(nhanVienData)
});

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Lỗi ${response.status}: ${errorText || 'Không thể thêm mới nhân viên'}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Lỗi khi gọi API createNhanVien:", error);
    throw error;
  }
};

/**
 * 3. Cập nhật thông tin nhân viên (Gồm thông tin cá nhân + mảng địa chỉ lồng bên trong)
 */
export const updateNhanVien = async (id, nhanVienData) => {
  try {
    const response = await fetch(`${baseUrl}/nhan-vien/update/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
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
 * 4. Thay đổi trạng thái hoạt động nhanh của nhân viên
 */
export const changeStatusNhanVien = async (id, trangThai) => {
  try {
    const response = await fetch(`${baseUrl}/nhan-vien/doi-trang-thai/${id}?trangThai=${trangThai}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      }
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
 * 5. Thêm địa chỉ mới trực tiếp cho nhân viên từ Modal
 * (Tận dụng hàm cập nhật tổng để đồng bộ mảng địa chỉ xuống SQL)
 */
export const addAddressByNhanVienId = async (nhanVienId, addressPayload) => {
  try {
    const allNhanVien = await fetchAllNhanVien();
    const currentNv = allNhanVien.find(item => (item.id || item.idNhanVien) === nhanVienId);
    
    if (!currentNv) {
      throw new Error("Không tìm thấy thông tin nhân viên để thêm địa chỉ!");
    }

    if (!Array.isArray(currentNv.addresses)) {
      currentNv.addresses = [];
    }

    const newAddr = {
      ...addressPayload,
      id: addressPayload.id || Date.now() 
    };

    if (newAddr.isDefault) {
      currentNv.addresses.forEach(addr => addr.isDefault = false);
    }

    currentNv.addresses.push(newAddr);

    let roleId = 3;
    if (currentNv.vaiTro && typeof currentNv.vaiTro === 'object') {
      roleId = currentNv.vaiTro.id;
    } else if (currentNv.vaiTro) {
      roleId = currentNv.vaiTro;
    }

    const updatedPayload = {
      ...currentNv,
      vaiTro: { id: Number(roleId) }
    };

    return await updateNhanVien(nhanVienId, updatedPayload);
  } catch (error) {
    console.error(`Lỗi tại addAddressByNhanVienId (ID NV: ${nhanVienId}):`, error);
    throw error;
  }
};

/**
 * 6. Đặt địa chỉ làm mặc định hoặc cập nhật một địa chỉ từ Modal
 */
export const updateAddress = async (addressId, addressPayload) => {
  try {
    const allNhanVien = await fetchAllNhanVien();
    let targetNv = null;

    for (const nv of allNhanVien) {
      if (Array.isArray(nv.addresses) && nv.addresses.some(addr => addr.id === addressId)) {
        targetNv = nv;
        break;
      }
    }

    if (!targetNv) {
      throw new Error("Không tìm thấy nhân viên sở hữu địa chỉ này!");
    }

    targetNv.addresses = targetNv.addresses.map(addr => {
      if (addr.id === addressId) {
        return { ...addr, ...addressPayload };
      }
      if (addressPayload.isDefault) {
        return { ...addr, isDefault: false };
      }
      return addr;
    });

    let roleId = 3;
    if (targetNv.vaiTro && typeof targetNv.vaiTro === 'object') {
      roleId = targetNv.vaiTro.id;
    } else if (targetNv.vaiTro) {
      roleId = targetNv.vaiTro;
    }

    const updatedPayload = {
      ...targetNv,
      vaiTro: { id: Number(roleId) }
    };

    return await updateNhanVien(targetNv.id || targetNv.idNhanVien, updatedPayload);
  } catch (error) {
    console.error(`Lỗi tại updateAddress (ID Địa chỉ: ${addressId}):`, error);
    throw error;
  }
};

/**
 * 7. Xóa vĩnh viễn một địa chỉ khỏi nhân viên từ Modal
 */
export const deleteAddress = async (addressId) => {
  try {
    const allNhanVien = await fetchAllNhanVien();
    let targetNv = null;

    for (const nv of allNhanVien) {
      if (Array.isArray(nv.addresses) && nv.addresses.some(addr => addr.id === addressId)) {
        targetNv = nv;
        break;
      }
    }

    if (!targetNv) {
      throw new Error("Không tìm thấy thông tin nhân viên chứa địa chỉ cần xóa!");
    }

    const wasDefault = targetNv.addresses.find(addr => addr.id === addressId)?.isDefault;
    targetNv.addresses = targetNv.addresses.filter(addr => addr.id !== addressId);

    if (wasDefault && targetNv.addresses.length > 0) {
      targetNv.addresses[0].isDefault = true;
    }

    let roleId = 3;
    if (targetNv.vaiTro && typeof targetNv.vaiTro === 'object') {
      roleId = targetNv.vaiTro.id;
    } else if (targetNv.vaiTro) {
      roleId = targetNv.vaiTro;
    }

    const updatedPayload = {
      ...targetNv,
      vaiTro: { id: Number(roleId) }
    };

    return await updateNhanVien(targetNv.id || targetNv.idNhanVien, updatedPayload);
  } catch (error) {
    console.error(`Lỗi tại deleteAddress (ID Địa chỉ: ${addressId}):`, error);
    throw error;
  }
};