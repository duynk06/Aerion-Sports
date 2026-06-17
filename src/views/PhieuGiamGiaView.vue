<template>
  <MainLayout title="Phiếu giảm giá">
    <div class="table-box">
      <div class="filter-box">
        <div class="card-header">
          <i class="fa-solid fa-filter"></i>
          Bộ lọc tìm kiếm
        </div>
        
        <div class="filter-grid">
          <div class="form-group">
            <label>Mã/ tên phiếu giảm giá:</label>
            <input type="text" v-model="keyword" placeholder="Nhập mã hoặc tên phiếu">
          </div>

          <div class="form-group">
            <label>Trạng thái:</label>
            <select v-model="trangThai">
              <option value="">Tất cả</option>
              <option value="1">Hoạt động</option>
              <option value="0">Ngừng hoạt động</option>
            </select>
          </div>

          <div class="form-group">
            <label>Từ ngày:</label>
            <input type="date" v-model="tuNgay">
          </div>

          <div class="form-group">
            <label>Đến ngày:</label>
            <input type="date" v-model="denNgay">
          </div>
        </div>

        <div class="filter-action">
          <button class="btn-reset" type="button" @click="handleResetFilter">
            Đặt lại
          </button>
          <button class="btn-search" type="button">
            Lọc dữ liệu
          </button>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="list-header">
        <div class="list-title">
          <div class="title-icon">
            <i class="fa-solid fa-ticket"></i>
          </div>
          <div>
            <h3>Danh sách phiếu giảm giá</h3>
            <span>Quản lý phiếu giảm giá</span>
          </div>
        </div>
        <button class="btn-add" @click="router.push('/phieu-giam-gia/them')">
          <i class="fa-solid fa-plus"></i>
          Thêm mới
        </button>
      </div>

      <table>
        <thead>
          <tr>
            <th>Mã phiếu</th>
            <th>Tên phiếu</th>
            <th>Giá trị giảm</th>
            <th>Số lượng</th>
            <th>Đã dùng</th>
            <th>Thời gian áp dụng</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="phieu in paginatedPhieuGiamGia" :key="phieu.id">
            <td>{{ phieu.maPhieuGiamGia }}</td>
            <td>{{ phieu.tenPhieuGiamGia }}</td>
            <td>
              <span v-if="phieu.loaiPhieuGiamGia === 'Sale %'">
                {{ phieu.giaTriGiam }} %
              </span>
              <span v-else>
                {{ Number(phieu.giaTriGiam).toLocaleString('vi-VN') }} đ
              </span>
            </td>
            <td>{{ phieu.soLuong }}</td>
            <td>{{ phieu.soLuongDaSuDung || 0 }}</td>
            <td>
              {{ formatDate(phieu.ngayBatDau) }}
              <br>
              {{ formatDate(phieu.ngayKetThuc) }}
            </td>
            <td>
              <span class="status" :class="{
                inactive: phieu.trangThai === 0,
                active: phieu.trangThai === 1
              }">
                {{ phieu.trangThai === 1 ? 'Hoạt động' : 'Ngừng hoạt động' }}
              </span>
            </td>
            <td>
              <button class="action-btn view-btn" @click="router.push(`/phieu-giam-gia/xem/${phieu.id}`)" title="Xem chi tiết">
                <i class="fa-solid fa-eye"></i>
              </button>
              
              <button class="action-btn edit-btn" @click="router.push(`/phieu-giam-gia/sua/${phieu.id}`)" title="Sửa phiếu">
                <i class="fa-solid fa-pen"></i>
              </button>
              
              <button class="action-btn" 
                      :class="phieu.trangThai === 1 ? 'toggle-on-btn' : 'toggle-off-btn'" 
                      @click="handleToggle(phieu)"
                      :title="phieu.trangThai === 1 ? 'Ngừng hoạt động' : 'Kích hoạt'">
                <i :class="phieu.trangThai === 1 ? 'fa-solid fa-toggle-on' : 'fa-solid fa-toggle-off'"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div class="pagination">
        <button @click="currentPage--" :disabled="currentPage === 1">◀</button>
        <span>Trang {{ currentPage }} / {{ totalPages || 1 }}</span>
        <button @click="currentPage++" :disabled="currentPage >= totalPages">▶</button>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import MainLayout from '../layouts/MainLayout.vue'
import {
  fetchAllPhieuGiamGia,
  updatePhieuGiamGia,
  deletePhieuGiamGia
} from '@/service/PhieuGiamGiaService'
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const listPhieuGiamGia = ref([])
const currentPage = ref(1)

const pageSize = ref(5)

const keyword = ref('')
const trangThai = ref('')
const tuNgay = ref('')
const denNgay = ref('')

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('vi-VN')
}

const handleFetchAllData = async () => {
  try {
    const data = await fetchAllPhieuGiamGia()
    listPhieuGiamGia.value = data || []
  } catch (error) {
    console.error('Lỗi tải dữ liệu:', error)
    listPhieuGiamGia.value = []
  }
}

const handleToggle = async (phieu) => {
  const data = {
    ...phieu,
    trangThai: phieu.trangThai === 1 ? 0 : 1
  }
  await updatePhieuGiamGia(phieu.id, data)
  await handleFetchAllData()
}

const handleResetFilter = () => {
  keyword.value = ''
  trangThai.value = ''
  tuNgay.value = ''
  denNgay.value = ''
  currentPage.value = 1
}

const handleDelete = async (id) => {
  if (!confirm('Bạn có chắc muốn xóa không?')) return
  try {
    await deletePhieuGiamGia(id)
    alert('Xóa thành công')
    await handleFetchAllData()
  } catch (error) {
    console.error('Lỗi xóa:', error)
    alert(error.message)
  }
}

const filteredPhieuGiamGia = computed(() => {
  return listPhieuGiamGia.value.filter(phieu => {

    const matchKeyword = !keyword.value ||
      phieu.maPhieuGiamGia?.toLowerCase().includes(keyword.value.toLowerCase()) ||
      phieu.tenPhieuGiamGia?.toLowerCase().includes(keyword.value.toLowerCase())

    const matchTrangThai = trangThai.value === '' || phieu.trangThai === Number(trangThai.value)

    const matchTuNgay = !tuNgay.value || new Date(phieu.ngayBatDau) >= new Date(tuNgay.value)
    const matchDenNgay = !denNgay.value || new Date(phieu.ngayKetThuc) <= new Date(denNgay.value)

    return matchKeyword && matchTrangThai && matchTuNgay && matchDenNgay
  })
})

watch([keyword, trangThai, tuNgay, denNgay], () => {
  currentPage.value = 1
})

const paginatedPhieuGiamGia = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredPhieuGiamGia.value.slice(start, end)
})

const totalPages = computed(() =>
  Math.ceil(filteredPhieuGiamGia.value.length / pageSize.value) || 1
)

onMounted(() => {
  handleFetchAllData()
})
</script>

<style scoped>
/* ==========================================================================
   1. KHUNG CHUNG & THẺ CARD
   ========================================================================== */
.card {
  background: white;
  border: 1px solid #edf2f7;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.table-box {
  margin-bottom: 20px;
}

/* Header của bộ lọc tìm kiếm */
.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #f79b66;
  color: white;
  padding: 12px 18px;
  border-radius: 8px;
  margin-bottom: 18px;
  font-weight: 600;
  font-size: 15px;
}

/* ==========================================================================
   2. BỘ LỌC TÌM KIẾM (FILTER ZONE)
   ========================================================================== */
.filter-box {
  background: #ffffff;
  padding: 25px;
  border-radius: 12px;
  border: 1px solid #edf2f7;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.filter-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.form-group label {
  margin-bottom: 8px;
  font-size: 13px;
  font-weight: 600;
  color: #4a5568;
}

/* Ô nhập liệu và select đã được bo tròn góc mềm mại */
.form-group input,
.form-group select {
  width: 100%;
  height: 40px;
  box-sizing: border-box;
  border: 1px solid #cbd5e1;
  border-radius: 8px; /* Bo góc tròn nhẹ nhàng */
  padding: 0 12px;
  color: #333;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}

.form-group input:focus,
.form-group select:focus {
  border-color: #f79b66;
}

/* Khu vực nút bấm của bộ lọc */
.filter-action {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #f1f5f9;
}

/* Custom các nút bấm Lọc dữ liệu / Đặt lại bo tròn góc đẹp hơn */
.btn-reset,
.btn-search {
  border: none;
  border-radius: 8px; /* Tăng độ bo góc tròn trịa hơn hình chữ nhật cũ */
  padding: 10px 22px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.2s ease;
}

.btn-reset {
  background: #f3f4f6;
  color: #4b5563;
}

.btn-reset:hover {
  background: #e5e7eb;
}

/* Nút lọc dữ liệu màu xanh Mint dịu giống ảnh mẫu */
.btn-search {
  background: #e6f7f0;
  color: #0aa06e;
}

.btn-search:hover {
  background: #ccefe3;
  transform: translateY(-1px);
}

/* ==========================================================================
   3. DANH SÁCH & BẢNG DỮ LIỆU (TABLE ZONE)
   ========================================================================== */
.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 5px 20px 5px;
}

.list-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  width: 34px;
  height: 34px;
  background: #f79b66;
  color: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
}

.list-title h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1a202c;
}

.list-title span {
  color: #718096;
  font-size: 12px;
}

/* Nút thêm mới */
.btn-add {
  background: #f79b66;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 18px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  transition: all 0.2s;
}

.btn-add:hover {
  background: #e08553;
  transform: translateY(-1px);
}

/* Cấu trúc Bảng */
table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

thead {
  background: #f79b66;
}

thead th {
  color: white;
  padding: 14px;
  text-align: center;
  font-weight: 600;
  font-size: 14px;
}

tbody td {
  padding: 15px 14px;
  text-align: center;
  border-bottom: 1px solid #edf2f7;
  color: #2d3748;
  font-size: 14px;
}

tbody tr:hover {
  background-color: #f8fafc;
}

/* ==========================================================================
   4. NHÃN TRẠNG THÁI (STATUS LABELS - PASTEL STYLE)
   ========================================================================== */
/* Định dạng nhãn trạng thái bo tròn viên thuốc nhẹ nhàng, không bị chói */
.status {
  display: inline-block;
  min-width: 120px;
  padding: 5px 14px;
  border-radius: 30px; /* Bo tròn hoàn toàn kiểu capsule */
  font-size: 13px;
  font-weight: 500;
  text-align: center;
  white-space: nowrap;
}

/* Trạng thái Hoạt động / Đang diễn ra (Xanh lá nhạt) */
.status.active {
  background: #e6f4ea;
  color: #137333;
}

/* Trạng thái Ngừng hoạt động / Đã kết thúc (Xanh dương nhạt như ảnh mẫu) */
.status.inactive {
  background: #e8f0fe;
  color: #1a73e8;
}

/* ==========================================================================
   5. NÚT THAO TÁC HÀNH ĐỘNG (ACTION BUTTONS - MINIMALIST BLACK)
   ========================================================================== */
/* Chuyển các nút Xem, Sửa về màu đen/xám tối giản, không màu cam thô */
.action-btn {
  border: none;
  background: transparent;
  color: #4a5568; /* Màu xám đen tối giản chủ đạo */
  padding: 6px 8px;
  border-radius: 6px;
  cursor: pointer;
  margin: 0 2px;
  font-size: 15px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
}

/* Hiệu ứng hover tạo nền xám nhẹ hiện đại */
.action-btn:hover {
  background: #f1f5f9;
  color: #000000;
}

/* Ghi đè loại bỏ màu cam cũ của nút xem và sửa */
.view-btn, 
.edit-btn {
  background: transparent !important;
  color: #4a5568 !important;
}

.view-btn:hover, 
.edit-btn:hover {
  background: #f1f5f9 !important;
  color: #000000 !important;
}

/* Style cho các nút gạt Toggle Switch trạng thái Bật/Tắt */
.toggle-on-btn {
  color: #f79b66 !important; /* Giữ màu cam thương hiệu khi đang bật active */
}

.toggle-off-btn {
  color: #cbd5e1 !important; /* Màu xám khi tắt hẳn */
}

/* ==========================================================================
   6. PHÂN TRANG (PAGINATION)
   ========================================================================== */
.pagination {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 12px;
  margin: 25px 0 10px 5px;
}

.pagination button {
  width: 32px;
  height: 32px;
  border: 1px solid #e2e8f0;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #4a5568;
  transition: all 0.2s;
}

.pagination button:not(:disabled):hover {
  background: #f8fafc;
  border-color: #cbd5e1;
}

.pagination button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.pagination span {
  font-size: 14px;
  color: #4a5568;
  font-weight: 500;
}

.no-data {
  text-align: center;
  padding: 40px 20px;
  color: #a0aec0;
  font-style: italic;
}
</style>