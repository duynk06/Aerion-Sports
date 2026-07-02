import myAxios from '../api/axios';
// ⚡ LƯU Ý: Nếu file này không nằm trực tiếp trong src/views/ (ví dụ nằm trong src/service/),
// hãy kiểm tra lại số cấp "../" cho đúng vị trí thực tế của file api/axios.js trong dự án.
// An toàn nhất có thể đổi thành: import myAxios from '@/api/axios';

const baseUrl = '/nhan-vien';

/**
 * 1. Lấy toàn bộ danh sách nhân viên từ SQL Server
 */
export const fetchAllNhanVien = async () => {
  try {
    const response = await myAxios.get(`${baseUrl}/hien-thi`);
    const data = response.data;
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error("Lỗi khi gọi API fetchAllNhanVien:", error);
    throw new Error(error?.response?.data?.message || error?.response?.data || 'Không thể tải dữ liệu nhân viên');
  }
};

/**
 * Kiểm tra trùng SĐT / Email khi thêm mới
 */
export const checkDuplicate = async (sdt, email) => {
  try {
    const response = await myAxios.get(`${baseUrl}/check-duplicate`, {
      params: { sdt, email }
    });
    return response.data;
  } catch (error) {
    console.error("Lỗi kiểm tra trùng:", error);
    return false;
  }
};

/**
 * Kiểm tra trùng SĐT / Email khi cập nhật (loại trừ chính bản ghi đang sửa)
 */
export const checkDuplicateUpdate = async (sdt, email, id) => {
  try {
    const response = await myAxios.get(`${baseUrl}/check-duplicate-update`, {
      params: { sdt, email, id }
    });
    return response.data;
  } catch (error) {
    console.error("Lỗi kiểm tra trùng (update):", error);
    return false;
  }
};

/**
 * 2. Gọi API thêm nhân viên để kích hoạt luồng tự cấp mật khẩu và gửi Email ngầm
 */
export const addNhanVien = async (nhanVienData) => {
  try {
    const response = await myAxios.post(`${baseUrl}/add`, nhanVienData);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi gọi API addNhanVien:", error);
    throw new Error(error?.response?.data?.message || error?.response?.data || 'Không thể thêm mới nhân viên');
  }
};

/**
 * 3. Cập nhật thông tin nhân viên (Gồm thông tin cá nhân + mảng địa chỉ lồng bên trong)
 */
export const updateNhanVien = async (id, nhanVienData) => {
  try {
    const response = await myAxios.put(`${baseUrl}/update/${id}`, nhanVienData);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi gọi API updateNhanVien (ID: ${id}):`, error);
    throw new Error(error?.response?.data?.message || error?.response?.data || 'Không thể cập nhật nhân viên');
  }
};

/**
 * 4. Thay đổi trạng thái hoạt động nhanh của nhân viên
 */
export const changeStatusNhanVien = async (id, trangThai) => {
  try {
    await myAxios.put(`${baseUrl}/doi-trang-thai/${id}`, null, {
      params: { trangThai }
    });
    return true;
  } catch (error) {
    console.error(`Lỗi khi gọi API changeStatusNhanVien (ID: ${id}):`, error);
    throw new Error(error?.response?.data?.message || error?.response?.data || 'Không thể đổi trạng thái');
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