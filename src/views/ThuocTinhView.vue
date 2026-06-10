<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <span class="breadcrumb-text">Quản lý sản phẩm / <strong style="color: #f97316;">Quản lý thuộc tính hệ thống</strong></span>
    </div>

    <div class="tabs-container">
      <button 
        v-for="tab in tabs" 
        :key="tab.key" 
        :class="['tab-item', activeTab === tab.key ? 'tab-active' : '']"
        @click="chuyenTab(tab.key)"
      >
        {{ tab.icon }} {{ tab.label }}
      </button>
    </div>

    <div class="filter-toggle-bar">
      <button class="btn-toggle-filter" @click="isFilterVisible = !isFilterVisible">
        {{ isFilterVisible ? '🙈 Ẩn bộ lọc' : '🔍 Hiện bộ lọc' }}
      </button>
    </div>

    <div class="filter-section-container" v-if="isFilterVisible">
      <div class="filter-grid">
        <div class="filter-item" style="flex: 2; min-width: 200px;">
          <label>Tìm kiếm tên hoặc mã</label>
          <input 
            type="text" 
            v-model="filter.keyword" 
            :placeholder="'Tìm kiếm ' + currentTabLabel.toLowerCase() + '...'" 
            @keyup.enter="phatSinhTimKiem"
          />
        </div>
        <div class="filter-item" style="max-width: 200px;">
          <label>Trạng thái</label>
          <select v-model="filter.trangThai" @change="phatSinhTimKiem">
            <option value="">Tất cả</option>
            <option value="1">Đang hoạt động</option>
            <option value="0">Ngừng hoạt động</option>
          </select>
        </div>
        <div class="filter-action-buttons">
          <button class="btn-search-trigger" @click="phatSinhTimKiem">Tìm kiếm</button>
          <button class="btn-reset-trigger" @click="resetFilter">Làm mới</button>
        </div>
      </div>
    </div>

    <div class="data-table-container">
      <div class="table-header-wrapper">
        <h3 class="table-title">Danh sách {{ currentTabLabel.toLowerCase() }}</h3>
        <div class="table-actions-right">
          <button class="btn-export-excel" @click="xuatFileExcelConfirm">Xuất Excel</button>
          <button class="btn-add-new" @click="moModalThemMoi">➕ Thêm {{ currentTabLabel }}</button>
        </div>
      </div>

      <table class="custom-data-table">
        <thead>
          <tr>
            <th style="width: 60px; text-align: center;">STT</th>
            <th>Mã {{ currentTabLabel.toLowerCase() }}</th>
            <th>Tên {{ currentTabLabel.toLowerCase() }}</th>
            <th style="width: 200px; text-align: center;">Trạng thái</th>
            <th style="width: 180px; text-align: center;">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in danhSachThuocTinh" :key="item.id">
            <td style="text-align: center;">{{ (filter.page * filter.size) + index + 1 }}</td>
            <td><span class="code-text-flat">{{ item[currentMapping.ma] }}</span></td>
            <td><strong>{{ item[currentMapping.ten] }}</strong></td>
            
            <td style="text-align: center;">
              <span :class="['badge-status-text', item.trangThai === 1 ? 'status-active' : 'status-stopped']">
                {{ item.trangThai === 1 ? 'Hoạt động' : 'Ngừng hoạt động' }}
              </span>
            </td>
            
            <td style="text-align: center;">
              <div class="action-buttons-cell">
                <button class="btn-action-edit" @click="moModalCapNhat(item)">✏️ Sửa</button>
                
                <div class="status-toggle-container" @click="toggleXoaMemCucBo(item)" title="Gạt công tắc để thay đổi trạng thái nhanh">
                  <span :class="['toggle-track', item.trangThai === 1 ? 'track-active' : 'track-inactive']">
                    <span class="toggle-handle"></span>
                  </span>
                </div>
              </div>
            </td>
          </tr>
          <tr v-if="danhSachThuocTinh.length === 0">
            <td colspan="5" class="empty-table-row">Không tìm thấy thuộc tính dữ liệu nào khớp bộ lọc!</td>
          </tr>
        </tbody>
      </table>

      <div class="custom-pagination-wrapper">
        <button 
          class="page-arrow-btn" 
          :disabled="filter.page === 0" 
          @click="thayDoiTrang(filter.page - 1)"
        >
          &lt;
        </button>
        <span class="page-text-indicator">
          Trang {{ filter.page + 1 }} / {{ totalPages }}
        </span>
        <button 
          class="page-arrow-btn" 
          :disabled="filter.page >= totalPages - 1 || totalPages <= 1" 
          @click="thayDoiTrang(filter.page + 1)"
        >
          &gt;
        </button>
      </div>
    </div>

    <div class="custom-modal-overlay" v-if="isModalOpen">
      <div class="custom-modal-box">
        <div class="modal-box-header">
          <h3>{{ isEditMode ? '✏️ Cập nhật ' : '➕ Thêm mới ' }} {{ currentTabLabel.toLowerCase() }}</h3>
          <button class="btn-close-x" @click="isModalOpen = false">&times;</button>
        </div>
        <div class="modal-box-body">
          <div class="form-group-item">
            <label>Mã {{ currentTabLabel.toLowerCase() }} (Tự động sinh) <span class="required-star">*</span></label>
            <input 
              type="text" 
              v-model="formModel.ma" 
              disabled
              placeholder="Hệ thống đang tự tính toán mã..."
              class="disabled-input"
            />
          </div>
          <div class="form-group-item">
            <label>Tên {{ currentTabLabel.toLowerCase() }} <span class="required-star">*</span></label>
            <input 
              type="text" 
              v-model="formModel.ten" 
              :placeholder="'Nhập tên ' + currentTabLabel.toLowerCase() + '...'"
            />
          </div>
        </div>
        <div class="modal-box-footer">
          <button class="btn-modal-cancel" @click="isModalOpen = false">Hủy bỏ</button>
          <button class="btn-modal-submit" @click="submitLuuThuocTinhConfirm">💾 Lưu dữ liệu</button>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import MainLayout from '../layouts/MainLayout.vue';

const tabs = [
  { key: 'thuong-hieu', label: 'Thương hiệu', icon: '🏷️', api: 'thuong-hieu', ma: 'maThuongHieu', ten: 'tenThuongHieu', prefix: 'TH' },
  { key: 'xuat-xu', label: 'Xuất xứ hãng', icon: '🌍', api: 'xuat-xu', ma: 'maXuatXu', ten: 'tenXuatXu', prefix: 'XX' },
  { key: 'mau-sac', label: 'Màu sắc', icon: '🎨', api: 'mau-sac', ma: 'maMauSac', ten: 'tenMauSac', prefix: 'MS' },
  { key: 'trong-luong', label: 'Trọng lượng', icon: '⚖️', api: 'trong-luong', ma: 'maTrongLuong', ten: 'tenTrongLuong', prefix: 'TL' },
  { key: 'do-cung', label: 'Độ cứng', icon: '🦴', api: 'do-cung', ma: 'maDoCung', ten: 'tenDoCung', prefix: 'DC' },
  { key: 'diem-can-bang', label: 'Điểm cân bằng', icon: '🎯', api: 'diem-can-bang', ma: 'maDiemCanBang', ten: 'tenDiemCanBang', prefix: 'CB' },
  { key: 'chu-vi-can', label: 'Chu vi cán', icon: '🏸', api: 'chu-vi-can-vot', ma: 'maChuViCanVot', ten: 'tenChuViCanVot', prefix: 'CV' },
  { key: 'chat-lieu-than', label: 'Chất liệu thân', icon: '🧪', api: 'chat-lieu-than-vot', ma: 'maChatLieuThanVot', ten: 'tenChatLieuThanVot', prefix: 'CLT' },
  { key: 'chat-lieu-khung', label: 'Chất liệu khung', icon: '🛡️', api: 'chat-lieu-khung-vot', ma: 'maChatLieuKhungVot', ten: 'tenChatLieuKhungVot', prefix: 'CLK' },
  { key: 'danh-muc', label: 'Danh mục', icon: '📦', api: 'danh-muc', ma: 'maDanhMuc', ten: 'tenDanhMuc', prefix: 'DM' }
];

const activeTab = ref('thuong-hieu');
const isFilterVisible = ref(true);
const danhSachThuocTinh = ref([]);
const totalPages = ref(1);
const isModalOpen = ref(false);
const isEditMode = ref(false);

const filter = ref({ page: 0, size: 5, keyword: '', trangThai: '' });
const formModel = ref({ id: null, ma: '', ten: '', trangThai: 1 });

const currentMapping = computed(() => tabs.find(t => t.key === activeTab.value));
const currentTabLabel = computed(() => currentMapping.value.label);

// 🤖 THUẬT TOÁN TỰ SINH MÃ THUỘC TÍNH: Quét danh sách hiện tại tìm số lớn nhất để tịnh tiến tăng dần mẫu TH001, MS002
const taoTuDongMaThuocTinh = () => {
  const prefix = currentMapping.value.prefix;
  if (danhSachThuocTinh.value.length === 0) {
    return `${prefix}001`;
  }
  
  const danhSachSo = danhSachThuocTinh.value.map(item => {
    const maGoc = item[currentMapping.value.ma] || '';
    const phanSo = maGoc.replace(/[^\d]/g, ''); // Giữ lại phần ký tự số
    return phanSo ? parseInt(phanSo, 10) : 0;
  });

  const soLonNhat = Math.max(...danhSachSo, 0);
  const soTiepTheo = soLonNhat + 1;
  return prefix + String(soTiepTheo).padStart(3, '0');
};

const loadData = async () => {
  try {
    const paramsPayload = { page: filter.value.page, size: filter.value.size };
    if (filter.value.trangThai !== '') paramsPayload.trangThai = filter.value.trangThai;
    if (filter.value.keyword.trim() !== '') paramsPayload.keyword = filter.value.keyword;

    const res = await axios.get(`http://localhost:8080/api/${currentMapping.value.api}/search`, { params: paramsPayload });

    if (res.data && res.data.content !== undefined) {
      danhSachThuocTinh.value = res.data.content;
      totalPages.value = res.data.totalPages || 1;
    } else {
      danhSachThuocTinh.value = Array.isArray(res.data) ? res.data : [];
      totalPages.value = 1;
    }
  } catch (e) {
    alert("Lỗi nạp dữ liệu từ Server!");
  }
};

const chuyenTab = (key) => { activeTab.value = key; filter.value.page = 0; filter.value.keyword = ''; filter.value.trangThai = ''; loadData(); };
const phatSinhTimKiem = () => { filter.value.page = 0; loadData(); };
const thayDoiTrang = (p) => { filter.value.page = p; loadData(); };
const resetFilter = () => { filter.value = { page: 0, size: 5, keyword: '', trangThai: '' }; loadData(); };

// ⚡ ĐÃ TỐI ƯU CỤC BỘ: Thay đổi trạng thái xóa mềm siêu nhanh, lật công tắc đổi màu ngay lập tức
const toggleXoaMemCucBo = async (item) => {
  const productId = item.id;
  const indexGoc = danhSachThuocTinh.value.findIndex(x => x.id === productId);
  if (indexGoc === -1) return;

  const trangThaiCu = danhSachThuocTinh.value[indexGoc].trangThai;
  const trangThaiMoi = trangThaiCu === 1 ? 0 : 1;
  
  if (!confirm(trangThaiMoi === 1 ? "Bạn có chắc chắn muốn KÍCH HOẠT hoạt động lại thuộc tính này?" : "Bạn có chắc chắn muốn TẠM NGỪNG hoạt động thuộc tính này?")) {
    return;
  }

  // Sửa đổi giá trị trên mảng gốc -> Công tắc gạt và chữ đổi màu lập tức (0.01 giây)
  danhSachThuocTinh.value[indexGoc].trangThai = trangThaiMoi;

  try {
    await axios.put(`http://localhost:8080/api/${currentMapping.value.api}/${productId}/trang-thai`, null, {
      params: { trangThai: trangThaiMoi }
    });
  } catch (e) {
    // Hoàn tác nếu lỗi
    danhSachThuocTinh.value[indexGoc].trangThai = trangThaiCu;
    alert("Không thể kết nối máy chủ để đổi trạng thái!");
  }
};

const moModalThemMoi = () => {
  isEditMode.value = false;
  const maMoi = taoTuDongMaThuocTinh(); // Gọi hàm sinh mã tự động tăng
  formModel.value = { id: null, ma: maMoi, ten: '', trangThai: 1 };
  isModalOpen.value = true;
};

const moModalCapNhat = (item) => {
  isEditMode.value = true;
  formModel.value = {
    id: item.id,
    ma: item[currentMapping.value.ma],
    ten: item[currentMapping.value.ten],
    trangThai: item.trangThai
  };
  isModalOpen.value = true;
};

// ❓ ĐÃ THÊM XÁC NHẬN KHI LƯU: Hỏi ý kiến Admin rõ ràng trước khi bấm ghi dữ liệu
const submitLuuThuocTinhConfirm = async () => {
  if (!formModel.value.ten.trim()) {
    return alert("Vui lòng điền tên thuộc tính dữ liệu!");
  }

  const thongBaoHoi = isEditMode.value 
    ? `Hệ thống ghi nhận hành động cập nhật.\nBạn có chắc chắn muốn SỬA thông tin thuộc tính [ ${formModel.value.ma} ] không?`
    : `Hệ thống ghi nhận hành động tạo mới.\nBạn có chắc chắn muốn THÊM thuộc tính mã [ ${formModel.value.ma} ] vào hệ thống không?`;

  if (!confirm(thongBaoHoi)) return;

  const payload = { id: formModel.value.id, trangThai: formModel.value.trangThai };
  payload[currentMapping.value.ma] = formModel.value.ma;
  payload[currentMapping.value.ten] = formModel.value.ten;

  try {
    let url = `http://localhost:8080/api/${currentMapping.value.api}/${formModel.value.id}`;
    let method = 'put';

    if (activeTab.value === 'chu-vi-can') {
      url = isEditMode.value 
        ? `http://localhost:8080/api/chu-vi-can-vot/update/${formModel.value.id}`
        : `http://localhost:8080/api/chu-vi-can-vot/add`;
      method = isEditMode.value ? 'put' : 'post';
    } else if (!isEditMode.value) {
      url = `http://localhost:8080/api/${currentMapping.value.api}`;
      method = 'post';
    }

    await axios[method](url, payload);
    alert("Lưu dữ liệu thuộc tính thành công! 🎉");
    isModalOpen.value = false;
    loadData();
  } catch (error) {
    alert("Thao tác thất bại! Kiểm tra lại kết nối hoặc dữ liệu trùng.");
  }
};

// ❓ ĐÃ THÊM XÁC NHẬN KHI XUẤT EXCEL
const xuatFileExcelConfirm = () => {
  if (danhSachThuocTinh.value.length === 0) return alert("Không có dữ liệu để xuất!");
  
  if (!confirm(`📊 Bạn có chắc chắn muốn xuất toàn bộ danh sách [ ${currentTabLabel.value.toLowerCase()} ] hiện tại ra file Excel không?`)) {
    return;
  }

  let csvContent = "\uFEFF";
  csvContent += `STT,Mã ${currentTabLabel.value},Tên ${currentTabLabel.value},Trạng thái\n`;
  danhSachThuocTinh.value.forEach((item, index) => {
    csvContent += `${index + 1},${item[currentMapping.value.ma]},${item[currentMapping.value.ten]},${item.trangThai === 1 ? "Hoạt động" : "Ngừng"}\n`;
  });
  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.setAttribute("download", `Danh_sach_${currentMapping.value.api}.csv`);
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
};

onMounted(loadData);
</script>

<style scoped>
.breadcrumb-container { display: flex; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #333; }

.tabs-container { display: flex; gap: 5px; background: #f8fafc; padding: 6px; border-radius: 8px; border: 1px solid #e2e8f0; margin-bottom: 15px; overflow-x: auto; }
.tab-item { display: flex; align-items: center; gap: 6px; background: transparent; border: none; padding: 8px 16px; font-size: 13px; font-weight: 600; color: #475569; border-radius: 6px; cursor: pointer; white-space: nowrap; transition: all 0.2s; }
.tab-item:hover { background: #f1f5f9; color: #f97316; }
.tab-item.tab-active { background: #f97316; color: white; box-shadow: 0 2px 4px rgba(249, 115, 22, 0.2); }

.filter-toggle-bar { display: flex; margin-bottom: 10px; justify-content: flex-start; }
.btn-toggle-filter { background: #f3f4f6; border: 1px solid #d1d5db; color: #374151; padding: 6px 14px; font-size: 12px; font-weight: 600; border-radius: 6px; cursor: pointer; transition: 0.2s; }
.btn-toggle-filter:hover { background: #e5e7eb; }

.filter-section-container { background: #fff; border: 1px solid #fed7aa; border-radius: 6px; padding: 15px; margin-bottom: 20px; }
.filter-grid { display: flex; gap: 15px; align-items: flex-end; flex-wrap: wrap; }
.filter-item { display: flex; flex-direction: column; gap: 6px; text-align: left; }
.filter-item label { font-size: 12px; font-weight: 600; color: #4a5568; }
.filter-item input, .filter-item select { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; width: 100%; box-sizing: border-box; outline: none; }
.filter-action-buttons { display: flex; gap: 8px; }
.btn-search-trigger { background: #f97316; color: white; border: none; padding: 8px 16px; border-radius: 4px; font-size: 13px; font-weight: 600; cursor: pointer; }
.btn-reset-trigger { background: #f3f4f6; color: #4b5563; border: 1px solid #d1d5db; padding: 8px 14px; border-radius: 4px; font-size: 13px; cursor: pointer; }

.data-table-container { background-color: #fff; border: 1px solid #fed7aa; border-radius: 4px; padding: 20px; }
.table-header-wrapper { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #f3f4f6; }
.table-title { font-size: 16px; font-weight: 600; color: #1e293b; margin: 0; text-transform: capitalize; }
.table-actions-right { display: flex; gap: 8px; }

.btn-export-excel { background-color: #16a34a; color: white; border: none; padding: 8px 16px; border-radius: 6px; font-size: 13px; font-weight: 600; cursor: pointer; }
.btn-export-excel:hover { background-color: #15803d; }
.btn-add-new { background-color: #1e293b; color: #fff; border: none; padding: 8px 16px; border-radius: 6px; font-size: 13px; font-weight: 600; cursor: pointer; }
.btn-add-new:hover { background-color: #0f172a; }

.custom-data-table { width: 100%; border-collapse: collapse; text-align: left; }
.custom-data-table th { background-color: #f97316; color: #fff; padding: 10px 12px; font-size: 13px; font-weight: 600; border: 1px solid #fed7aa; }
.custom-data-table td { padding: 12px; border-bottom: 1px solid #fed7aa; font-size: 13px; color: #334155; }
.code-text-flat { font-weight: 600; color: #c2410c; background-color: #fff7ed; padding: 2px 6px; border-radius: 4px; font-size: 12px; }

/* 🌟 STYLE TEXT BADGE TĨNH CHO CỘT TRẠNG THÁI */
.badge-status-text { padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; display: inline-block; }
.badge-status-text.status-active { background-color: #f0fdf4; color: #16a34a; border: 1px solid #bbf7d0; }
.badge-status-text.status-stopped { background-color: #fef2f2; color: #ef4444; border: 1px solid #fecaca; }

/* 🌟 STYLE TOGGLE SWITCH ĐỔI TRẠNG THÁI NHANH NẰM BÊN CẠNH SỬA */
.status-toggle-container { display: inline-flex; align-items: center; cursor: pointer; user-select: none; }
.toggle-track { position: relative; display: inline-block; width: 36px; height: 18px; border-radius: 999px; transition: background-color 0.2s ease; }
.track-active { background-color: #22c55e; }
.track-inactive { background-color: #cbd5e1; }
.toggle-handle { position: absolute; top: 3px; left: 3px; width: 12px; height: 12px; background-color: #fff; border-radius: 50%; transition: transform 0.2s ease; }
.track-active .toggle-handle { transform: translateX(18px); }

.action-buttons-cell { display: flex; gap: 12px; justify-content: center; align-items: center; }
.btn-action-edit { background: #fff7ed; border: 1px solid #f97316; color: #f97316; padding: 4px 10px; border-radius: 4px; font-size: 12px; font-weight: 600; cursor: pointer; }
.empty-table-row { text-align: center; padding: 30px !important; color: #94a3b8; }

.custom-pagination-wrapper { display: flex; align-items: center; gap: 10px; margin-top: 15px; justify-content: flex-start; }
.page-arrow-btn { width: 32px; height: 32px; background: #fff; border: 1px solid #e5e7eb; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 14px; color: #4b5563; cursor: pointer; transition: 0.2s; }
.page-arrow-btn:hover:not(:disabled) { background: #f3f4f6; border-color: #cbd5e1; }
.page-arrow-btn:disabled { background: #f9fafb; color: #d1d5db; cursor: not-allowed; border-color: #f3f4f6; }
.page-text-indicator { font-size: 13px; font-weight: 500; color: #374151; padding: 0 4px; }

.custom-modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.4); display: flex; justify-content: center; align-items: center; z-index: 9999; }
.custom-modal-box { background: white; border-radius: 6px; width: 450px; max-width: 95%; border-top: 5px solid #f97316; display: flex; flex-direction: column; }
.modal-box-header { padding: 15px; border-bottom: 1px solid #e5e7eb; display: flex; justify-content: space-between; align-items: center; }
.modal-box-header h3 { margin: 0; font-size: 16px; color: #c2410c; }
.btn-close-x { background: none; border: none; font-size: 24px; cursor: pointer; color: #94a3b8; }
.modal-box-body { padding: 20px; display: flex; flex-direction: column; gap: 15px; }
.form-group-item { display: flex; flex-direction: column; gap: 6px; text-align: left; }
.form-group-item label { font-size: 13px; font-weight: 600; color: #374151; }
.form-group-item input { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; width: 100%; box-sizing: border-box; outline: none; }
.disabled-input { background: #f3f4f6; cursor: not-allowed; color: #9ca3af; font-weight: bold; }
.modal-box-footer { padding: 15px; border-top: 1px solid #e5e7eb; display: flex; justify-content: flex-end; gap: 10px; }
.btn-modal-cancel { background: #f3f4f6; border: 1px solid #d1d5db; padding: 8px 16px; border-radius: 4px; cursor: pointer; font-size: 13px; }
.btn-modal-submit { background: #f97316; color: white; border: none; padding: 8px 20px; border-radius: 4px; cursor: pointer; font-weight: 600; font-size: 13px; }
.required-star { color: #ef4444; font-weight: bold; }
</style>