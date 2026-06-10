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
 * 2. Cập nhật thông tin nhân viên (Gồm thông tin cá nhân + mảng địa chỉ lồng bên trong)
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
 * 3. Thay đổi trạng thái hoạt động nhanh của nhân viên
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
 * 4. Thêm địa chỉ mới trực tiếp cho nhân viên từ Modal
 * (Tận dụng hàm cập nhật tổng để đồng bộ mảng địa chỉ xuống SQL)
 */
export const addAddressByNhanVienId = async (nhanVienId, addressPayload) => {
  try {
    // Tải thông tin hiện tại của toàn bộ nhân viên để tìm đúng đối tượng
    const allNhanVien = await fetchAllNhanVien();
    const currentNv = allNhanVien.find(item => (item.id || item.idNhanVien) === nhanVienId);
    
    if (!currentNv) {
      throw new Error("Không tìm thấy thông tin nhân viên để thêm địa chỉ!");
    }

    // Khởi tạo mảng địa chỉ nếu chưa có và thêm địa chỉ mới vào
    if (!Array.isArray(currentNv.addresses)) {
      currentNv.addresses = [];
    }

    // Tạo ID giả định tạm thời cho địa chỉ mới nếu Backend cần, hoặc để tự tăng dưới SQL
    const newAddr = {
      ...addressPayload,
      id: addressPayload.id || Date.now() // tạo tạm id số nếu cần
    };

    // Nếu địa chỉ mới được đặt làm mặc định, hủy mặc định các địa chỉ cũ
    if (newAddr.isDefault) {
      currentNv.addresses.forEach(addr => addr.isDefault = false);
    }

    currentNv.addresses.push(newAddr);

    // Chuẩn hóa cấu trúc vai trò tương thích với Spring Boot Jackson Map giống giao diện view
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

    // Gọi hàm update để đẩy dữ liệu mới lên cơ sở dữ liệu
    return await updateNhanVien(nhanVienId, updatedPayload);
  } catch (error) {
    console.error(`Lỗi tại addAddressByNhanVienId (ID NV: ${nhanVienId}):`, error);
    throw error;
  }
};

/**
 * 5. Đặt địa chỉ làm mặc định hoặc cập nhật một địa chỉ từ Modal
 */
export const updateAddress = async (addressId, addressPayload) => {
  try {
    const allNhanVien = await fetchAllNhanVien();
    let targetNv = null;

    // Tìm xem địa chỉ cần sửa đang thuộc về nhân viên nào
    for (const nv of allNhanVien) {
      if (Array.isArray(nv.addresses) && nv.addresses.some(addr => addr.id === addressId)) {
        targetNv = nv;
        break;
      }
    }

    if (!targetNv) {
      throw new Error("Không tìm thấy nhân viên sở hữu địa chỉ này!");
    }

    // Cập nhật trạng thái hoặc thông tin của địa chỉ trong mảng
    targetNv.addresses = targetNv.addresses.map(addr => {
      if (addr.id === addressId) {
        return { ...addr, ...addressPayload };
      }
      // Nếu địa chỉ hiện tại được đặt làm mặc định, các địa chỉ khác sẽ hủy mặc định
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
 * 6. Xóa vĩnh viễn một địa chỉ khỏi nhân viên từ Modal
 */
export const deleteAddress = async (addressId) => {
  try {
    const allNhanVien = await fetchAllNhanVien();
    let targetNv = null;

    // Tìm nhân viên đang chứa địa chỉ muốn xóa
    for (const nv of allNhanVien) {
      if (Array.isArray(nv.addresses) && nv.addresses.some(addr => addr.id === addressId)) {
        targetNv = nv;
        break;
      }
    }

    if (!targetNv) {
      throw new Error("Không tìm thấy thông tin nhân viên chứa địa chỉ cần xóa!");
    }

    // Lọc bỏ địa chỉ cần xóa ra khỏi mảng
    const wasDefault = targetNv.addresses.find(addr => addr.id === addressId)?.isDefault;
    targetNv.addresses = targetNv.addresses.filter(addr => addr.id !== addressId);

    // Nếu xóa đúng địa chỉ mặc định, tự động chuyển quyền mặc định sang địa chỉ đầu tiên còn lại
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