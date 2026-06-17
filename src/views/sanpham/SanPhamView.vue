<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <div class="breadcrumb-text">
        <strong>Quản lý sản phẩm</strong> / Danh sách sản phẩm hệ thống
      </div>
      <button class="btn-toggle-filter" @click="isFilterVisible = !isFilterVisible">
        {{ isFilterVisible ? 'Ẩn bộ lọc' : 'Hiện bộ lọc' }}
      </button>
    </div>

    <div class="filter-container-orange" v-if="isFilterVisible">
      <div class="filter-header-title">
        <i class="fa-solid fa-filter"></i> Bộ lọc tìm kiếm
      </div>
      <div class="filter-body-content">
        <div class="filter-grid">
          <div class="filter-item">
            <label>Tìm kiếm</label>
            <input
              type="text"
              v-model="filterForm.keyword"
              placeholder="Mã hoặc tên sản phẩm..."
              @input="phatSinhTimKiem"
            />
          </div>

          <div class="filter-item slider-item">
            <label>Khoảng giá: 0đ - {{ formatTienTe(filterForm.giaMax) }}</label>
            <div class="price-slider-wrapper">
              <input
                type="range"
                v-model.number="filterForm.giaMax"
                min="0"
                max="50000000"
                step="100000"
                class="custom-slider"
                @input="phatSinhTimKiem"
              />
            </div>
          </div>

          <div class="filter-item" style="max-width: 140px;">
            <label>Số lượng tồn</label>
            <input 
              type="number" 
              v-model.number="filterForm.soLuongMin" 
              placeholder="Ví dụ: 0, 10..." 
              min="0"
              @input="phatSinhTimKiem"
            />
          </div>

          <div class="filter-item">
            <label>Thương hiệu</label>
            <select v-model="filterForm.idThuongHieu" @change="phatSinhTimKiem">
              <option value="">-- Tất cả --</option>
              <option v-for="th in danhSachThuocTinhMaster.thuongHieu" :key="th.id" :value="th.id">
                {{ th.tenThuongHieu }}
              </option>
            </select>
          </div>

          <div class="filter-item">
            <label>Xuất xứ</label>
            <select v-model="filterForm.idXuatXu" @change="phatSinhTimKiem">
              <option value="">-- Tất cả --</option>
              <option v-for="xx in danhSachThuocTinhMaster.xuatXu" :key="xx.id" :value="xx.id">
                {{ xx.tenXuatXu }}
              </option>
            </select>
          </div>

          <div class="filter-item">
            <label>Trạng thái</label>
            <select v-model="filterForm.trangThai" @change="phatSinhTimKiem">
              <option value="">-- Tất cả --</option>
              <option value="1">Đang kinh doanh</option>
              <option value="0">Ngừng kinh doanh</option>
            </select>
          </div>

          <div class="filter-item filter-actions">
            <button class="btn-filter-clear" @click="lamMoiBoLoc">
               Đặt lại
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="data-table-container">
      <div class="table-header-row">
        <div class="table-summary-title">Danh sách sản phẩm hệ thống</div>
        <div class="header-actions">
          <button class="btn-action-excel" @click="exportToExcel">
            Xuất Excel
          </button>
          <button class="btn-action-solid" @click="chuyenSangTrangThemMoi">
            Thêm sản phẩm mới
          </button>
        </div>
      </div>

      <table class="custom-data-table">
        <thead>
          <tr>
            <th style="width: 50px; text-align: center;">STT</th>
            <th>Mã sản phẩm</th>
            <th>Tên sản phẩm</th>
            <th>Thương hiệu</th>
            <th>Xuất xứ</th>
            <th>Khoảng giá biến thể</th>
            <th style="text-align: center; width: 130px;">Tổng lượng tồn</th>
            <th style="text-align: center; width: 160px;">Trạng thái</th>
            <th style="text-align: center; width: 180px;">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="filteredDanhSachSanPham.length === 0">
            <td colspan="9" class="empty-table-row">
              Không tìm thấy sản phẩm nào khớp với bộ lọc dữ liệu!
            </td>
          </tr>
          <tr v-for="(sp, index) in filteredDanhSachSanPham" :key="sp.id || index">
            <td style="text-align: center;">{{ (currentPage * pageSize) + index + 1 }}</td>
            <td><span class="product-code-text">{{ sp.maSanPham }}</span></td>
            <td>{{ sp.tenSanPham }}</td>
            <td>{{ sp.tenThuongHieu }}</td>
            <td>{{ sp.tenXuatXu }}</td>
            <td>
              <span style="font-weight: 600; color: #2563eb;">{{ hienThiKhoangGia(sp) }}</span>
            </td>
            <td style="text-align: center; font-weight: bold; color: #475569;">
              {{ sp.tongSoLuongTon }}
            </td>
            
            <td style="text-align: center;">
              <span :class="['badge-status-text', sp.trangThai === 1 ? 'status-active' : 'status-stopped']">
                {{ sp.trangThai === 1 ? 'Đang kinh doanh' : 'Ngừng kinh doanh' }}
              </span>
            </td>
            
            <td style="text-align: center;">
              <div class="action-buttons-flex-group">
                <button class="icon-btn-circle view-eye" @click="xemChiTietBienThe(sp)" title="Xem danh sách biến thể con">
                  <i class="fa-regular fa-eye"></i>
                </button>
                
                <div class="status-toggle-container" @click="toggleXoaMemSanPham(sp)" title="Thay đổi trạng thái kinh doanh nhanh">
                  <span :class="['toggle-track', sp.trangThai === 1 ? 'track-active' : 'track-inactive']">
                    <span class="toggle-handle"></span>
                  </span>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="custom-pagination-container">
        <div class="pagination-left-summary">
          Tổng số: <strong>{{ totalElements }}</strong> sản phẩm hệ thống
        </div>
        <div class="pagination-right-controls">
          <button class="page-arrow-btn" :disabled="currentPage === 0" @click="chuyenTrang(currentPage - 1)">&lt;</button>
          <span class="page-text-indicator">Trang {{ totalPages > 0 ? currentPage + 1 : 1 }} / {{ totalPages || 1 }}</span>
          <button class="page-arrow-btn" :disabled="currentPage >= totalPages - 1 || totalPages <= 1" @click="chuyenTrang(currentPage + 1)">&gt;</button>
        </div>
      </div>

    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'; 
import axios from 'axios';
import { useRouter } from 'vue-router'; 
import MainLayout from '@/layouts/MainLayout.vue';
import * as XLSX from 'xlsx';

const router = useRouter();

const isFilterVisible = ref(true);
const currentPage = ref(0);
const pageSize = ref(5); 
const totalPages = ref(1);
const totalElements = ref(0);

const danhSachSanPham = ref([]);
const danhSachThuocTinhMaster = ref({ thuongHieu: [], xuatXu: [] });

const filterForm = ref({
  keyword: '', idThuongHieu: '', idXuatXu: '', trangThai: '', giaMax: 50000000, soLuongMin: ''
});

const formatTienTe = (value) => {
  if (value === null || value === undefined || isNaN(value)) return '0 đ';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const safeExtractArray = (res) => { return res && res.data ? (Array.isArray(res.data) ? res.data : (res.data.content || [])) : []; };

const layKhoangGiaCuaSanPham = (sp) => {
  const variants = sp.chiTietSanPhams || [];
  if (variants.length === 0) return { min: 0, max: 0, text: 'Chưa có giá' };
  const giaBans = variants.map(v => v.giaBan || 0);
  const minGia = Math.min(...giaBans);
  const maxGia = Math.max(...giaBans);
  return minGia === maxGia ? { min: minGia, max: maxGia, text: formatTienTe(minGia) } : { min: minGia, max: maxGia, text: `${formatTienTe(minGia)} - ${formatTienTe(maxGia)}` };
};

const hienThiKhoangGia = (sp) => layKhoangGiaCuaSanPham(sp).text;

const filteredDanhSachSanPham = computed(() => {
  return danhSachSanPham.value.map(sp => {
    const tongTonKhoCon = (sp.chiTietSanPhams || []).reduce((sum, item) => sum + (item.soLuong || 0), 0);
    return {
      ...sp,
      computedThuongHieuId: sp.idThuongHieu,
      computedXuatXuId: sp.idXuatXu,
      tenThuongHieu: sp.tenThuongHieu || 'Chưa rõ',
      tenXuatXu: sp.tenXuatXu || 'Chưa rõ',
      tongSoLuongTon: tongTonKhoCon 
    };
  }).filter(sp => {
    const matchKeyword = !filterForm.value.keyword.trim() || 
      (sp.maSanPham || '').toLowerCase().includes(filterForm.value.keyword.trim().toLowerCase()) ||
      (sp.tenSanPham || '').toLowerCase().includes(filterForm.value.keyword.trim().toLowerCase());
    
    const matchThuongHieu = !filterForm.value.idThuongHieu || String(sp.computedThuongHieuId) === String(filterForm.value.idThuongHieu);
    const matchXuatXu = !filterForm.value.idXuatXu || String(sp.computedXuatXuId) === String(filterForm.value.idXuatXu);
    const matchTrangThai = filterForm.value.trangThai === '' || String(sp.trangThai) === String(filterForm.value.trangThai);

    const khoangGia = layKhoangGiaCuaSanPham(sp);
    const matchPrice = filterForm.value.giaMax === 50000000 || khoangGia.min <= filterForm.value.giaMax;
    const matchSoLuong = filterForm.value.soLuongMin === '' || filterForm.value.soLuongMin === null || sp.tongSoLuongTon >= parseInt(filterForm.value.soLuongMin, 10);

    return matchKeyword && matchThuongHieu && matchXuatXu && matchTrangThai && matchPrice && matchSoLuong;
  });
});

const loadToanBoDuLieu = async () => {
  try {
    const [sp, th, xx] = await Promise.all([
      axios.get('http://localhost:8080/api/san-pham/search', {
        params: {
          page: currentPage.value,
          size: pageSize.value,
          keyword: filterForm.value.keyword || null,
          thuongHieuId: filterForm.value.idThuongHieu || null, 
          idXuatXu: filterForm.value.idXuatXu || null,
          trangThai: filterForm.value.trangThai !== '' ? filterForm.value.trangThai : null
        }
      }), 
      axios.get('http://localhost:8080/api/thuong-hieu/all'),   
      axios.get('http://localhost:8080/api/xuat-xu/all')
    ]);

    if (sp.data && sp.data.content !== undefined) {
      danhSachSanPham.value = sp.data.content;
      totalPages.value = sp.data.totalPages || 1;
      totalElements.value = sp.data.totalElements || 0;
    } else {
      danhSachSanPham.value = safeExtractArray(sp);
      totalPages.value = 1;
      totalElements.value = danhSachSanPham.value.length;
    }

    danhSachThuocTinhMaster.value.thuongHieu = safeExtractArray(th); 
    danhSachThuocTinhMaster.value.xuatXu = safeExtractArray(xx);
  } catch (error) { 
    console.error("Lỗi tải dữ liệu:", error); 
  }
};

const chuyenTrang = (page) => { currentPage.value = page; loadToanBoDuLieu(); };
const phatSinhTimKiem = () => { currentPage.value = 0; loadToanBoDuLieu(); };

const toggleXoaMemSanPham = async (sp) => {
  const productId = sp.id; 
  if (!productId) return alert("Không tìm thấy ID sản phẩm!");
  
  const indexGoc = danhSachSanPham.value.findIndex(item => item.id === productId);
  if (indexGoc === -1) return;

  const trangThaiCu = danhSachSanPham.value[indexGoc].trangThai;
  const trangThaiMoi = trangThaiCu === 1 ? 0 : 1; 

  if (!confirm(trangThaiMoi === 1 ? "Bạn muốn khôi phục kinh doanh sản phẩm này?" : "Bạn muốn tạm ngừng kinh doanh sản phẩm này?")) {
    return;
  }

  danhSachSanPham.value[indexGoc].trangThai = trangThaiMoi;

  try {
    await axios.put(`http://localhost:8080/api/san-pham/${productId}/trang-thai`, null, {
      params: { trangThai: trangThaiMoi }
    });
  } catch (e) {
    danhSachSanPham.value[indexGoc].trangThai = trangThaiCu;
    alert("Hệ thống mất kết nối, không thể thay đổi trạng thái kinh doanh!");
  }
};

const xemChiTietBienThe = (sp) => { 
  router.push({ path: '/san-pham/bien-the', query: { idSP: sp.id, maSP: sp.maSanPham, tenSP: sp.tenSanPham } }); 
};

const chuyenTrangSuaSanPhamCha = (sp) => {
  router.push({ path: '/san-pham/sua', query: { id: sp.id } });
};

const chuyenSangTrangThemMoi = () => {
  router.push('/san-pham/them-moi');
};

const lamMoiBoLoc = () => { filterForm.value = { keyword: '', idThuongHieu: '', idXuatXu: '', trangThai: '', giaMax: 50000000, soLuongMin: '' }; loadToanBoDuLieu(); };

const exportToExcel = () => {
  if (filteredDanhSachSanPham.value.length === 0) return alert("Không có dữ liệu để xuất Excel!");
  if (!confirm("Bạn có chắc chắn muốn xuất danh sách sản phẩm hiện tại ra file Excel không?")) return;

  const mapped = filteredDanhSachSanPham.value.map((sp, i) => ({
    "STT": i + 1, "Mã sản phẩm": sp.maSanPham, "Tên sản phẩm": sp.tenSanPham,
    "Thương hiệu": sp.tenThuongHieu, "Xuất xứ": sp.tenXuatXu, "Giá biến thể": layKhoangGiaCuaSanPham(sp).text,
    "Tổng số lượng tồn": sp.tongSoLuongTon, "Trạng thái": sp.trangThai === 1 ? "Đang kinh doanh" : "Ngừng kinh doanh"
  }));
  const ws = XLSX.utils.json_to_sheet(mapped);
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, "Sản phẩm");
  XLSX.writeFile(wb, "Danh_sach_san_pham.xlsx");
};

onMounted(() => loadToanBoDuLieu());
</script>

<style scoped>
/* ==========================================================================
   1. BREADCRUMB & KHUNG CHUNG
   ========================================================================== */
.breadcrumb-container { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 15px; 
}

.breadcrumb-text { 
  font-size: 14px; 
  color: #4a5568; 
}

.btn-toggle-filter { 
  background-color: #f8fafc; 
  border: 1px solid #cbd5e1; 
  color: #475569; 
  padding: 6px 14px; 
  border-radius: 8px; /* Bo góc đồng bộ */
  cursor: pointer; 
  font-size: 13px; 
  font-weight: 500; 
  transition: all 0.2s;
}

.btn-toggle-filter:hover { 
  background-color: #e2e8f0; 
}

/* ==========================================================================
   2. BỘ LỌC TÌM KIẾM CHUẨN BO GÓC (FILTER ZONE)
   ========================================================================== */
.filter-container-orange { 
  background-color: #fff; 
  border: 1px solid #f79b66; 
  border-radius: 12px; /* Tăng bo góc mềm mại */
  margin-bottom: 20px; 
  box-shadow: 0 1px 4px rgba(0,0,0,0.05); 
  overflow: hidden; 
}

.filter-header-title { 
  background-color: #f79b66; 
  color: white; 
  padding: 12px 18px; 
  font-size: 14px; 
  font-weight: bold; 
  text-align: left; 
}

.filter-body-content { 
  padding: 20px; 
  background-color: #fff; 
}

.filter-grid { 
  display: flex; 
  flex-wrap: wrap; 
  gap: 15px; 
  align-items: flex-end; 
}

.filter-item { 
  display: flex; 
  flex-direction: column; 
  gap: 8px; 
  text-align: left; 
  flex: 1; 
  min-width: 160px; 
}

.filter-item label { 
  font-size: 13px; 
  font-weight: 600; 
  color: #4a5568; 
}

/* Ô nhập liệu và select bo góc tròn trịa */
.filter-item input, 
.filter-item select { 
  padding: 8px 12px; 
  border: 1px solid #cbd5e1; 
  border-radius: 8px; /* Bo góc 8px hiện đại */
  font-size: 13px; 
  outline: none; 
  box-sizing: border-box; 
  width: 100%; 
  height: 38px;
  background-color: #fff; 
  transition: border-color 0.2s;
}

.filter-item input:focus, 
.filter-item select:focus { 
  border-color: #f79b66; 
}

/* Thanh trượt khoảng giá */
.slider-item { 
  min-width: 230px; 
  color:#22c55e;
}

.price-slider-wrapper { 
  display: flex; 
  align-items: center; 
  width: 100%; 
  height: 38px; 
}

.custom-slider { 
  -webkit-appearance: none; 
  width: 100%; 
  height: 6px; 
  background: #e2e8f0; 
  border-radius: 5px; 
  outline: none; 
  padding: 0 !important; 
  border: none !important; 
}

.custom-slider::-webkit-slider-thumb { 
  -webkit-appearance: none; 
  appearance: none; 
  width: 18px; 
  height: 18px; 
  border-radius: 50%; 
  background: #f79b66; 
  cursor: pointer; 
}

.filter-actions { 
  flex: 0 0 auto; 
  min-width: auto; 
}

/* Nút đặt lại bo tròn */
.btn-filter-clear { 
  background-color: #f3f4f6; 
  color: #4b5563; 
  border: none; 
  padding: 10px 20px; 
  border-radius: 8px; 
  font-size: 13px; 
  font-weight: 600; 
  cursor: pointer; 
  height: 38px;
  transition: background-color 0.2s; 
}

.btn-filter-clear:hover { 
  background-color: #e5e7eb; 
}

/* ==========================================================================
   3. KHUNG BẢNG DỮ LIỆU
   ========================================================================== */
.data-table-container { 
  background-color: #fff; 
  border: 1px solid #edf2f7; 
  border-radius: 12px; 
  padding: 20px; 
  box-shadow: 0 1px 3px rgba(0,0,0,0.05); 
}

.table-header-row { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 20px; 
  padding-bottom: 12px; 
  border-bottom: 1px solid #edf2f7; 
}

.table-summary-title { 
  font-size: 16px; 
  font-weight: 600; 
  color: #1a202c; 
  margin: 0; 
}

.header-actions { 
  display: flex; 
  align-items: center; 
  gap: 10px; 
}

/* Nút thêm sản phẩm mới */
.btn-action-solid { 
  background-color: #f79b66; 
  border: none; 
  color: #fff; 
  padding: 9px 18px; 
  border-radius: 8px; 
  font-weight: 600; 
  cursor: pointer; 
  font-size: 13px; 
  transition: all 0.2s;
}

.btn-action-solid:hover { 
  background-color: #e08553; 
  transform: translateY(-1px);
}

/* Nút xuất Excel màu xanh Mint dịu mắt */
.btn-action-excel { 
  background: #e6f7f0; 
  color: #0aa06e; 
  border: none; 
  padding: 9px 18px; 
  border-radius: 8px; 
  cursor: pointer; 
  font-size: 13px; 
  font-weight: 600; 
  transition: all 0.2s;
}

.btn-action-excel:hover { 
  background: #ccefe3; 
  transform: translateY(-1px);
}

/* ==========================================================================
   4. CẤU TRÚC BẢNG (TABLE)
   ========================================================================== */
.custom-data-table { 
  width: 100%; 
  border-collapse: collapse; 
  text-align: left; 
}

.custom-data-table th { 
  background-color: #f79b66; 
  color: white; 
  padding: 14px 10px; 
  font-size: 14px; 
  font-weight: 600;
  text-align: center;
}

.custom-data-table td { 
  padding: 14px 10px; 
  border-bottom: 1px solid #edf2f7; 
  font-size: 14px; 
  vertical-align: middle; 
}

.custom-data-table tbody tr:hover {
  background-color: #f8fafc;
}

.product-code-text { 
  font-weight: bold; 
  color: #f79b66; 
}

.empty-table-row { 
  text-align: center; 
  color: #94a3b8; 
  padding: 40px; 
  font-style: italic;
}

/* ==========================================================================
   5. NHÃN TRẠNG THÁI PASTEL (BADGE STATUS)
   ========================================================================== */
.badge-status-text { 
  padding: 5px 14px; 
  border-radius: 30px; /* Định dạng viên thuốc mềm mại */
  font-size: 13px; 
  font-weight: 500; 
  display: inline-block; 
  min-width: 130px;
  text-align: center;
}

/* Đang kinh doanh (Xanh lá pastel nhạt) */
.badge-status-text.status-active { 
  background-color: #e6f4ea; 
  color: #137333; 
}

/* Ngừng kinh doanh (Xanh dương pastel nhạt đồng bộ) */
.badge-status-text.status-stopped { 
  background-color: #e8f0fe; 
  color: #1a73e8; 
}

/* ==========================================================================
   6. NÚT THAO TÁC MINIMALIST BLACK (HÀNH ĐỘNG TỐI GIẢN)
   ========================================================================== */
.action-buttons-flex-group { 
  display: flex; 
  gap: 6px; 
  justify-content: center; 
  align-items: center; 
}

/* Đồng bộ nút tròn tối giản, màu đen xám thanh lịch */
.icon-btn-circle { 
  width: 32px; 
  height: 32px; 
  border-radius: 50%; 
  border: none; 
  display: inline-flex; 
  align-items: center; 
  justify-content: center; 
  cursor: pointer; 
  font-size: 15px; 
  background-color: transparent;
  color: #4a5568; /* Icon màu tối giản */
  transition: all 0.2s ease; 
}

.icon-btn-circle:hover { 
  background-color: #f1f5f9;
  color: #000000;
}

/* Loại bỏ class màu cũ */
.icon-btn-circle.view-eye,
.icon-btn-circle.edit-pencil-black {
  background-color: transparent !important;
  border: none !important;
  color: #4a5568 !important;
}

.icon-btn-circle.view-eye:hover,
.icon-btn-circle.edit-pencil-black:hover {
  background-color: #f1f5f9 !important;
  color: #000000 !important;
}

/* Công tắc gạt Toggle Switch mượt mà */
.status-toggle-container { 
  display: inline-flex; 
  align-items: center; 
  cursor: pointer; 
  user-select: none; 
  padding: 0 4px;
}

.toggle-track { 
  position: relative; 
  display: inline-block; 
  width: 34px; 
  height: 18px; 
  border-radius: 999px; 
  transition: background-color 0.2s ease; 
}

.track-active { 
  background-color: #22c55e; /* Màu xanh gạt khi active */
}

.track-inactive { 
  background-color: #cbd5e1; 
}

.toggle-handle { 
  position: absolute; 
  top: 3px; 
  left: 3px; 
  width: 12px; 
  height: 12px; 
  background-color: #fff; 
  border-radius: 50%; 
  transition: transform 0.2s ease; 
}

.track-active .toggle-handle { 
  transform: translateX(16px); 
}

/* ==========================================================================
   7. THANH PHÂN TRANG (PAGINATION)
   ========================================================================== */
.custom-pagination-container { 
  display: flex; 
  align-items: center; 
  justify-content: space-between; 
  margin-top: 25px; 
  padding-top: 15px; 
  border-top: 1px solid #edf2f7; 
}

.pagination-left-summary { 
  font-size: 13.5px; 
  color: #4a5568; 
}

.pagination-right-controls { 
  display: flex; 
  align-items: center; 
  gap: 10px; 
}

.page-arrow-btn { 
  width: 32px; 
  height: 32px; 
  background: #fff; 
  border: 1px solid #e2e8f0; 
  border-radius: 6px; 
  display: flex; 
  align-items: center; 
  justify-content: center; 
  font-size: 14px; 
  color: #4a5568; 
  cursor: pointer; 
  transition: 0.2s; 
}

.page-arrow-btn:hover:not(:disabled) { 
  background: #f8fafc; 
  border-color: #cbd5e1; 
}

.page-arrow-btn:disabled { 
  background: white; 
  color: #cbd5e1; 
  cursor: not-allowed; 
  border-color: #f1f5f9; 
  opacity: 0.5;
}

.page-text-indicator { 
  font-size: 13.5px; 
  font-weight: 500; 
  color: #4a5568; 
  padding: 0 4px; 
}
</style>