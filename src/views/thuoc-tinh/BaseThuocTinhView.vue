<template>
  <div class="breadcrumb-container">
    <span class="breadcrumb-text">Quản lý sản phẩm / Thuộc tính / <strong style="color: #f97316;">{{ title }}</strong></span>
  </div>

  <div class="filter-container">
    <div class="section-title">Bộ lọc tìm kiếm nhanh</div>
    <div class="filter-grid">
      <div class="filter-item">
        <label>Tìm kiếm thuộc tính</label>
        <input
          type="text"
          v-model="searchKeyword"
          placeholder="Nhập mã hoặc tên thuộc tính để lọc nhanh..."
          @input="phatSinhTimKiem"
        />
      </div>
      <div class="filter-item" style="max-width: 200px;">
        <label>Trạng thái hoạt động</label>
        <select v-model="statusFilter" @change="phatSinhTimKiem">
          <option value="">-- Tất cả trạng thái --</option>
          <option value="1">Đang hoạt động</option>
          <option value="0">Ngừng hoạt động</option>
        </select>
      </div>
      <div class="filter-item filter-actions">
        <button class="btn-filter-clear" @click="lamMoiBoLoc">Xóa bộ lọc</button>
      </div>
    </div>
  </div>

  <div class="data-table-container">
    <div class="table-header-row">
      <div class="table-summary-title">
        Danh sách dữ liệu hiện tại trên hệ thống
      </div>
      <div class="header-actions">
        <button class="btn-action-excel" @click="exportToExcel">
          Xuất Excel thuộc tính
        </button>
        <button class="btn-add-new-orange" @click="moModalThemMoi">
          Thêm {{ title }} mới
        </button>
      </div>
    </div>

    <div class="table-responsive" style="margin-top: 15px;">
      <table class="custom-data-table">
        <thead>
          <tr>
            <th style="width: 60px; text-align: center;">STT</th>
            <th>Mã thuộc tính</th>
            <th>Tên {{ title }}</th>
            <th style="width: 160px; text-align: center;">Trạng thái</th>
            <th style="width: 160px; text-align: center;">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in paginatedDataList" :key="item.id">
            <td style="text-align: center;">{{ (currentPage * pageSize) + index + 1 }}</td>
            <td class="product-code-text">{{ layMaThuocTinhAnToan(item) }}</td>
            <td style="font-weight: 600;">{{ item[propName] }}</td>
            <td style="text-align: center;">
              <span :class="['badge-status-text', item.trangThai === 1 ? 'status-active' : 'status-stopped']">
                {{ item.trangThai === 1 ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
              </span>
            </td>
            <td style="text-align: center;">
              <div class="action-buttons-flex-group">
                <button class="icon-btn-circle edit-pencil" @click="moModalChinhSua(item)" title="Chỉnh sửa tên thuộc tính">
                  <i class="fa-regular fa-pen-to-square"></i>
                </button>

                <div class="status-toggle-container" @click="toggleTrangThaiNhanh(item)" title="Thay đổi trạng thái">
                  <span :class="['toggle-track', item.trangThai === 1 ? 'track-active' : 'track-inactive']">
                    <span class="toggle-handle"></span>
                  </span>
                </div>
              </div>
            </td>
          </tr>
          <tr v-if="filteredDataList.length === 0">
            <td colspan="5" class="empty-table-row">Không tìm thấy dữ liệu thuộc tính nào khớp với bộ lọc!</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="custom-pagination-container">
      <div class="pagination-left-summary">
        Tổng số: <strong>{{ filteredDataList.length }}</strong> thuộc tính {{ title.toLowerCase() }} trên hệ thống
      </div>
      <div class="pagination-right-controls">
        <button class="page-arrow-btn" :disabled="currentPage === 0" @click="chuyenTrang(currentPage - 1)">&lt;</button>
        <span class="page-text-indicator">Trang {{ currentPage + 1 }} / {{ totalPages }}</span>
        <button class="page-arrow-btn" :disabled="currentPage >= totalPages - 1" @click="chuyenTrang(currentPage + 1)">&gt;</button>
      </div>
    </div>

  </div>

  <div class="custom-modal-overlay" v-if="isModalOpen">
    <div class="custom-modal-box">
      <div class="modal-box-header">
        <h3>{{ isEditMode ? 'Cập nhật' : 'Thêm mới' }} {{ title }}</h3>
        <button class="btn-close-x" @click="isModalOpen = false">&times;</button>
      </div>
      <div class="modal-box-body">
        <div class="form-group-item">
          <label>Mã {{ title.toLowerCase() }} (Khóa cố định) <span class="required-star"> *</span></label>
          <input type="text" v-model="generatedCode" disabled class="disabled-input" />
        </div>
        
        <div class="form-group-item">
          <label>Tên {{ title }} <span class="required-star"> *</span></label>
          <input type="text" v-model="formValue" :placeholder="'Nhập tên ' + title.toLowerCase() + '...'" @keyup.enter="handleHanhDongLuu"/>
        </div>
      </div>
      <div class="modal-box-footer">
        <button class="btn-modal-cancel" @click="isModalOpen = false">Hủy bỏ</button>
        <button class="btn-modal-submit" @click="handleHanhDongLuu">
          {{ isEditMode ? 'Cập nhật ngay' : 'Lưu dữ liệu' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, defineProps } from 'vue';
import axios from 'axios';
import * as XLSX from 'xlsx';

const props = defineProps({
  title: String,      
  apiPath: String,     
  propName: String     
});

const dataList = ref([]);
const isModalOpen = ref(false);
const formValue = ref('');
const generatedCode = ref(''); 

const isEditMode = ref(false);
const editingItemId = ref(null);
const editingItemTrangThai = ref(1);

const searchKeyword = ref('');
const statusFilter = ref('');

const currentPage = ref(0);
const pageSize = ref(5);

// 🌟 ĐÃ SỬA: Chuyển đổi gọi sang API /search kèm size lớn bốc data phẳng từ .content về an toàn
const loadData = async () => {
  try {
    const res = await axios.get(`http://localhost:8080/api/${props.apiPath}/search`, {
      params: { page: 0, size: 9999 }
    });
    dataList.value = res.data?.content || res.data || [];
  } catch (e) {
    console.error(`Lỗi nạp dữ liệu thuộc tính ${props.title}:`, e);
  }
};

const layMaThuocTinhAnToan = (item) => {
  if (!item) return '';
  return item.ma || item.maMauSac || item.maTrongLuong || item.maDoCung || item.maDiemCanBang || item.maChuViCanVot || item.maChatLieuThanVot || item.maChatLieuKhungVot || ('TT-' + item.id);
};

const filteredDataList = computed(() => {
  return dataList.value.filter(item => {
    const maText = layMaThuocTinhAnToan(item).toLowerCase();
    const tenText = String(item[props.propName] || '').toLowerCase();
    const key = searchKeyword.value.trim().toLowerCase();

    const matchKeyword = !key || maText.includes(key) || tenText.includes(key);
    const matchStatus = statusFilter.value === '' || String(item.trangThai) === String(statusFilter.value);

    return matchKeyword && matchStatus;
  });
});

const paginatedDataList = computed(() => {
  const start = currentPage.value * pageSize.value;
  const end = start + pageSize.value;
  return filteredDataList.value.slice(start, end);
});

const totalPages = computed(() => {
  const pages = Math.ceil(filteredDataList.value.length / pageSize.value);
  return pages > 0 ? pages : 1;
});

const chuyenTrang = (page) => {
  if (page >= 0 && page < totalPages.value) {
    currentPage.value = page;
  }
};

const phatSinhTimKiem = () => {
  currentPage.value = 0;
};

const lamMoiBoLoc = () => { 
  searchKeyword.value = ''; 
  statusFilter.value = ''; 
  currentPage.value = 0; 
};

const exportToExcel = () => {
  if (filteredDataList.value.length === 0) return alert(`Không có dữ liệu để xuất file!`);
  if (!confirm(`Bạn có chắc chắn muốn xuất toàn bộ danh sách thuộc tính ${props.title} ra file Excel không?`)) return;

  const dataExport = filteredDataList.value.map((item, idx) => {
    const row = { "STT": idx + 1, "Mã thuộc tính": layMaThuocTinhAnToan(item) };
    row[`Tên ${props.title}`] = item[props.propName];
    row["Trạng thái"] = item.trangThai === 1 ? "Đang hoạt động" : "Ngừng hoạt động";
    return row;
  });

  const worksheet = XLSX.utils.json_to_sheet(dataExport);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, "Danh sách thuộc tính");
  XLSX.writeFile(workbook, `ThuocTinh_${props.apiPath.replace(/-/g, '_')}.xlsx`);
};

const moModalThemMoi = () => {
  isEditMode.value = false;
  editingItemId.value = null;
  formValue.value = '';
  
  const prefix = props.apiPath.split('-').map(word => word[0].toUpperCase()).join('');
  let maxNumber = 0;
  
  if (dataList.value && dataList.value.length > 0) {
    dataList.value.forEach(item => {
      const currentCode = layMaThuocTinhAnToan(item);
      if (currentCode.includes('-')) {
        const partNumber = currentCode.split('-').pop();
        const parsedInt = parseInt(partNumber, 10);
        if (!isNaN(parsedInt) && parsedInt > maxNumber) {
          maxNumber = parsedInt;
        }
      }
    });
  }
  
  const nextNumber = maxNumber + 1;
  generatedCode.value = `${prefix}-${String(nextNumber).padStart(3, '0')}`;
  isModalOpen.value = true;
};

const moModalChinhSua = (item) => {
  isEditMode.value = false; 
  isEditMode.value = true;
  editingItemId.value = item.id;
  editingItemTrangThai.value = item.trangThai ?? 1;
  
  generatedCode.value = layMaThuocTinhAnToan(item); 
  formValue.value = item[props.propName]; 
  
  isModalOpen.value = true;
};

const checkValidThuocTinhText = (text) => {
  const cleanText = text ? text.trim() : '';
  if (!cleanText) {
    alert(`Vui lòng điền tên ${props.title.toLowerCase()}!`);
    return false;
  }
  
  const path = props.apiPath.toLowerCase();

  if (path === 'mau-sac') {
    const regexMauSac = /^[\p{L}\s]+$/u;
    if (!regexMauSac.test(cleanText)) {
      alert("Lỗi định dạng: Tên màu sắc phải là chữ thuần tiếng Việt (Ví dụ: Đỏ, Xanh neon, Đen nhám). Không chứa số hay ký tự lạ!");
      return false;
    }
  } 
  else if (path === 'trong-luong') {
    const regexTrongLuong = /^([1-6]U|\d+g|\d+-\d+g)$/i; 
    if (!regexTrongLuong.test(cleanText)) {
      alert("Lỗi định dạng: Trọng lượng vợt phải đúng tiêu chuẩn cầu lông (Ví dụ: 3U, 4U, 5U) hoặc số kèm chữ g (Ví dụ: 85g)!");
      return false;
    }
  } 
  else if (path === 'do-cung') {
    const regexDoCung = /^[\p{L}\s]+$/u;
    if (!regexDoCung.test(cleanText) || cleanText.match(/\d/)) {
      alert("Lỗi định dạng: Độ cứng thân vợt phải là từ mô tả (Ví dụ: Cứng, Dẻo, Trung bình, Rất cứng)!");
      return false;
    }
  } 
  else if (path === 'diem-can-bang') {
    const regexDiemCanBang = /^([\p{L}\s]+|\d+mm)$/u;
    if (!regexDiemCanBang.test(cleanText)) {
      alert("Lỗi định dạng: Điểm cân bằng phải là từ mô tả (Ví dụ: Nặng đầu, Cân bằng, Nhẹ đầu) hoặc số đo mm (Ví dụ: 295mm, 300mm)!");
      return false;
    }
  }
  else if (path === 'chu-vi-can' || path === 'chu-vi-can-vot') {
    const regexChuViCan = /^G[4-9]$/i;
    if (!regexChuViCan.test(cleanText)) {
      alert("Lỗi định dạng: Chu vi cán vợt phải đúng tiêu chuẩn kỹ thuật (Bắt đầu bằng chữ G:)!");
      return false;
    }
  }

  const lowerInput = cleanText.toLowerCase();
  const isDuplicate = dataList.value.some(item => {
    const currentName = String(item[props.propName] || '').trim().toLowerCase();
    if (isEditMode.value) {
      return currentName === lowerInput && item.id !== editingItemId.value;
    } else {
      return currentName === lowerInput;
    }
  });

  if (isDuplicate) {
    alert(`Tên ${props.title.toLowerCase()} này đã tồn tại trên hệ thống! Vui lòng nhập tên khác.`);
    return false;
  }

  return true;
};

const handleHanhDongLuu = () => {
  if (!checkValidThuocTinhText(formValue.value)) {
    return;
  }

  if (isEditMode.value) {
    submitCapNhatTen();
  } else {
    submitThemMoi();
  }
};

// 🌟 ĐÃ SỬA: Thêm đuôi `/add` khớp 100% endpoint `@PostMapping("/add")` của Backend
const submitThemMoi = async () => {
  try {
    const payload = {};
    payload[props.propName] = formValue.value.trim();
    payload.trangThai = 1; 

    payload.ma = generatedCode.value;
    payload.maMauSac = generatedCode.value;
    payload.maTrongLuong = generatedCode.value;
    payload.maDoCung = generatedCode.value;
    payload.maDiemCanBang = generatedCode.value;
    payload.maChuViCanVot = generatedCode.value;
    payload.maChatLieuThanVot = generatedCode.value;
    payload.maChatLieuKhungVot = generatedCode.value;

    await axios.post(`http://localhost:8080/api/${props.apiPath}/add`, payload);
    alert(`Thêm mới ${props.title.toLowerCase()} thành công! 🎉`);
    isModalOpen.value = false;
    loadData();
  } catch (e) {
    alert("Thao tác thêm mới thất bại! Tên thuộc tính có thể đã tồn tại.");
  }
};

// 🌟 ĐÃ SỬA: Thêm đuôi `/update/{id}` khớp 100% endpoint `@PutMapping("/update/{id}")` của Backend
const submitCapNhatTen = async () => {
  try {
    const payload = {
      id: editingItemId.value,
      trangThai: editingItemTrangThai.value
    };
    payload[props.propName] = formValue.value.trim();

    payload.ma = generatedCode.value;
    payload.maMauSac = generatedCode.value;
    payload.maTrongLuong = generatedCode.value;
    payload.maDoCung = generatedCode.value;
    payload.maDiemCanBang = generatedCode.value;
    payload.maChuViCanVot = generatedCode.value;
    payload.maChatLieuThanVot = generatedCode.value;
    payload.maChatLieuKhungVot = generatedCode.value;

    await axios.put(`http://localhost:8080/api/${props.apiPath}/update/${editingItemId.value}`, payload);
    
    alert(`Cập nhật tên ${props.title.toLowerCase()} thành công! 🚀`);
    isModalOpen.value = false;
    loadData();
  } catch (e) {
    alert("Cập nhật thất bại! Tên thuộc tính mới có thể bị trùng lặp.");
  }
};

const toggleTrangThaiNhanh = async (item) => {
  const trangThaiMoi = item.trangThai === 1 ? 0 : 1;
  if (!confirm(`Bạn có chắc chắn muốn thay đổi trạng thái hoạt động của thuộc tính này không?`)) return;
  
  try {
    await axios.put(`http://localhost:8080/api/${props.apiPath}/${item.id}/trang-thai`, null, {
      params: { trangThai: trangThaiMoi }
    });
    item.trangThai = trangThaiMoi;
  } catch (e) {
    try {
      item.trangThai = trangThaiMoi;
      await axios.put(`http://localhost:8080/api/${props.apiPath}/${item.id}`, item);
    } catch (err) {
      alert("Hệ thống chưa đồng bộ được API cập nhật trạng thái!");
      loadData();
    }
  }
};
onMounted(loadData);
</script>

<style scoped>
/* Giữ nguyên 100% CSS màu cam thương hiệu của bạn */
.breadcrumb-container { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #333; }
.filter-container { background-color: #fff; border: 1px solid #fed7aa; border-radius: 6px; padding: 15px; margin-bottom: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
.filter-grid { display: flex; flex-wrap: wrap; gap: 15px; margin-top: 10px; align-items: flex-end; }
.filter-item { display: flex; flex-direction: column; gap: 6px; text-align: left; flex: 1; min-width: 160px; }
.filter-item label { font-size: 12px; font-weight: 600; color: #475569; }
.filter-item input, .filter-item select { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; outline: none; box-sizing: border-box; width: 100%; }
.filter-item input:focus, .filter-item select:focus { border-color: #f79b66; }
.filter-actions { flex: 0 0 auto; min-width: auto; }
.btn-filter-clear { background-color: #e2e8f0; color: #334155; border: 1px solid #cbd5e1; padding: 8px 16px; border-radius: 4px; font-size: 13px; font-weight: 600; cursor: pointer; }
.data-table-container { background-color: #fff; border: 1px solid #fed7aa; border-radius: 4px; padding: 20px; text-align: left; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
.table-header-row { display: flex; justify-content: space-between; align-items: center; padding-bottom: 10px; border-bottom: 1px solid #f3f4f6; }
.table-summary-title { font-size: 15px; font-weight: bold; color: #1e293b; margin: 0; }
.header-actions { display: flex; align-items: center; gap: 10px; }
.btn-action-excel { background: #79c38a; color: white; border: none; padding: 8px 16px; border-radius: 6px; cursor: pointer; font-size: 13px; font-weight: 600; }
.btn-action-excel:hover { background: #15803d; }
.btn-add-new-orange { background-color: #f79b66; color: #fff; border: none; padding: 8px 16px; border-radius: 6px; font-size: 13px; font-weight: 600; cursor: pointer; transition: background-color 0.2s; }
.btn-add-new-orange:hover { background-color: #ea712b; }
.custom-data-table { width: 100%; border-collapse: collapse; text-align: left; }
.custom-data-table th { background-color: #f79b66; color: #fff; padding: 10px 12px; font-size: 13px; font-weight: 600; border: 1px solid #fed7aa; }
.custom-data-table td { padding: 12px; border-bottom: 1px solid #fed7aa; font-size: 13px; color: #334155; vertical-align: middle; }
.product-code-text { font-weight: 600; color: #f79b66; }
.badge-status-text { padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; display: inline-block; }
.badge-status-text.status-active { background-color: #f0fdf4; color: #16a34a; border: 1px solid #bbf7d0; }
.badge-status-text.status-stopped { background-color: #fef2f2; color: #f79b66; border: 1px solid #fecaca; }
.empty-table-row { text-align: center; padding: 30px !important; color: #94a3b8; }
.custom-pagination-container { display: flex; align-items: center; justify-content: space-between; margin-top: 20px; padding-top: 15px; border-top: 1px solid #f1f5f9; }
.pagination-left-summary { font-size: 13.5px; color: #475569; }
.pagination-right-controls { display: flex; align-items: center; gap: 10px; }
.page-arrow-btn { width: 32px; height: 32px; background: #fff; border: 1px solid #e5e7eb; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 14px; color: #4b5563; cursor: pointer; transition: 0.2s; }
.page-arrow-btn:hover:not(:disabled) { background: #f3f4f6; border-color: #cbd5e1; }
.page-arrow-btn:disabled { background: #f9fafb; color: #d1d5db; cursor: not-allowed; border-color: #f3f4f6; }
.page-text-indicator { font-size: 13px; font-weight: 500; color: #374151; padding: 0 4px; }
.custom-modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.4); display: flex; justify-content: center; align-items: center; z-index: 9999; }
.custom-modal-box { background: white; border-radius: 6px; width: 420px; border-top: 5px solid #f79b66; display: flex; flex-direction: column; }
.modal-box-header { padding: 15px; border-bottom: 1px solid #e5e7eb; display: flex; justify-content: space-between; align-items: center; }
.modal-box-header h3 { margin: 0; font-size: 16px; color: #f79b66; }
.btn-close-x { background: none; border: none; font-size: 24px; cursor: pointer; color: #94a3b8; }
.modal-box-body { padding: 20px; display: flex; flex-direction: column; gap: 15px; }
.form-group-item { display: flex; flex-direction: column; gap: 6px; text-align: left; }
.form-group-item label { font-size: 13px; font-weight: 600; color: #374151; }
.form-group-item input { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; outline: none; }
.form-group-item input:focus { border-color: #f79b66; }
.disabled-input { background: #f1f5f9 !important; cursor: not-allowed; color: #64748b; font-weight: 500; border-color: #e2e8f0 !important; }
.modal-box-footer { padding: 15px; border-top: 1px solid #e5e7eb; display: flex; justify-content: flex-end; gap: 10px; border-radius: 0 0 6px 6px; }
.btn-modal-cancel { background: #f3f4f6; border: 1px solid #d1d5db; padding: 8px 16px; border-radius: 4px; cursor: pointer; font-size: 13px; }
.btn-modal-submit { background: #f79b66; color: white; border: none; padding: 8px 20px; border-radius: 4px; cursor: pointer; font-weight: 600; font-size: 13px; }
.required-star { color: #f79b66; font-weight: bold; }
.section-title { font-size: 13px; font-weight: bold; color: #f79b66; border-left: 3px solid #f79b66; padding-left: 8px; text-align: left; }
.action-buttons-flex-group { display: flex; gap: 8px; justify-content: center; align-items: center; }
.icon-btn-circle { width: 32px; height: 32px; border-radius: 50%; border: none; display: inline-flex; align-items: center; justify-content: center; cursor: pointer; font-size: 14px; transition: all 0.2s ease-in-out; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
.icon-btn-circle.edit-pencil { background-color: #f9f5ff; color: #7c3aed; border: 1px solid #f3e8ff; }
.icon-btn-circle.edit-pencil:hover { background-color: #7c3aed; color: #ffffff; border-color: #7c3aed; transform: scale(1.08); }
.status-toggle-container { display: inline-flex; align-items: center; cursor: pointer; user-select: none; }
.toggle-track { position: relative; display: inline-block; width: 36px; height: 18px; border-radius: 999px; transition: background-color 0.2s ease; }
.track-active { background-color: #22c55e; }
.track-inactive { background-color: #cbd5e1; }
.toggle-handle { position: absolute; top: 3px; left: 3px; width: 12px; height: 12px; background-color: #fff; border-radius: 50%; transition: transform 0.2s ease; }
.track-active .toggle-handle { transform: translateX(18px); }
</style>