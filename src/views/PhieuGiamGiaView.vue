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
            <label>Đến:</label>
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
            <th>Loại</th>
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

            <td>{{ formatLoaiPhieu(phieu.loaiPhieuGiamGia) }}</td>

            <td>
              {{ Number(phieu.giaTriGiam).toLocaleString('vi-VN') }} đ
            </td>

            <td>{{ phieu.soLuong }}</td>

            <td>{{ phieu.soLuongDaSuDung }}</td>

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
              <button
  class="action-btn view-btn"
  @click="router.push(`/phieu-giam-gia/xem/${phieu.id}`)"
>
  👁️
</button>

              <button class="action-btn edit-btn" @click="router.push(`/phieu-giam-gia/sua/${phieu.id}`)">
                ✏️
              </button>

              <button class="action-btn toggle-btn" @click="handleToggle(phieu)">
                <i :class="phieu.trangThai
                  ? 'fa-solid fa-toggle-on'
                  : 'fa-solid fa-toggle-off'"></i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div class="pagination">
        <button @click="currentPage--" :disabled="currentPage === 1">
          ◀
        </button>

        <span>
          Trang {{ currentPage }} / {{ totalPages || 1 }}
        </span>

        <button @click="currentPage++" :disabled="currentPage >= totalPages">
          ▶
        </button>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import MainLayout from '../layouts/MainLayout.vue'
import {
  fetchAllPhieuGiamGia,
  updatePhieuGiamGia
} from '@/service/PhieuGiamGiaService'
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'

const listPhieuGiamGia = ref([])
const currentPage = ref(1)
const pageSize = ref(5)
const keyword = ref('')
const trangThai = ref('')
const loaiGiam = ref('')
const tuNgay = ref('')
const denNgay = ref('')

const formatDate = (date) => {
  if (!date) return ''

  return new Date(date).toLocaleDateString('vi-VN')
}

const handleFetchAllData = async () => {
  try {
    const data = await fetchAllPhieuGiamGia()

    console.log('Dữ liệu API:', data)

    listPhieuGiamGia.value = data || []
  } catch (error) {
    console.error('Lỗi tải dữ liệu:', error)
    listPhieuGiamGia.value = []
  }
}

const getTrangThai = (phieu) => {
  return phieu.trangThai === 1
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
  loaiGiam.value = ''
  tuNgay.value = ''
  denNgay.value = ''
}
const router = useRouter()
const formatLoaiPhieu = (loai) => {
  switch (loai) {
    case 'Sale %':
      return 'Sale %'
    case 'Free Ship':
      return 'Free Ship'
    default:
      return loai
  }
}
const handleDelete = async (id) => {
  if (!confirm('Bạn có chắc muốn xóa không?')) {
    return
  }

  try {
    await deletePhieuGiamGia(id)

    alert('Xóa thành công')
    await handleFetchAllData()

  } catch (error) {
    console.error('Lỗi xóa:', error)
    alert(error.message)
  }
}
let matchTrangThai = true

if (trangThai.value === '1') {
  matchTrangThai = getTrangThai(phieu)
} else if (trangThai.value === '0') {
  matchTrangThai = !getTrangThai(phieu)
}
const filteredPhieuGiamGia = computed(() => {
  return listPhieuGiamGia.value.filter(phieu => {

    const matchKeyword =
      !keyword.value ||
      phieu.maPhieuGiamGia?.toLowerCase().includes(keyword.value.toLowerCase()) ||
      phieu.tenPhieuGiamGia?.toLowerCase().includes(keyword.value.toLowerCase())

    const matchLoai =
      !loaiGiam.value ||
      phieu.loaiPhieuGiamGia === loaiGiam.value

    const matchTrangThai =
      trangThai.value === '' ||
      phieu.trangThai === Number(trangThai.value)

    const matchTuNgay =
      !tuNgay.value ||
      new Date(phieu.ngayBatDau) >= new Date(tuNgay.value)

    const matchDenNgay =
      !denNgay.value ||
      new Date(phieu.ngayKetThuc) <= new Date(denNgay.value)

    return (
      matchKeyword &&
      matchLoai &&
      matchTrangThai &&
      matchTuNgay &&
      matchDenNgay
    )
  })
})
const paginatedPhieuGiamGia = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value

  return filteredPhieuGiamGia.value.slice(start, end)
})
const totalPages = computed(() =>
  Math.ceil(filteredPhieuGiamGia.value.length / pageSize.value)
)
onMounted(() => {
  handleFetchAllData()
})
</script>

<style scoped>
.top {
  margin-bottom: 20px;
  text-align: right;
}

.add-btn {
  background: #f79b66;
  border: none;
  padding: 12px 20px;
  border-radius: 10px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.add-btn:hover {
  background: #f79b66;
  transform: translateY(-2px);
}

table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

th {
  background: #f79b66;
  color: white;
  padding: 14px;
  text-align: left;
  font-weight: 600;
}

td {
  padding: 14px;
  border-bottom: 1px solid #eee;
}

.status {
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 500;
}

.status.active {
  background: #2bae1f;
  color: white;
}

.status.inactive {
  background: #e84444;
  color: white;
}

.no-data {
  text-align: center;
  padding: 60px 20px;
  color: #777;
  font-style: italic;
}

.filter-box {
  background: #ffffff;
  padding: 25px;
  border-radius: 12px;
  margin-bottom: 20px;
}

.form-group input,
.form-group select {
  width: 100%;
  height: 48px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.card {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
}

.filter-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #f79b66;
  color: white;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 18px;
  font-weight: 600;
}

.filter-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.form-group label {
  margin-bottom: 6px;
  font-size: 12px;
  font-weight: 600;
}

.form-group input,
.form-group select {
  height: 38px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  padding: 0 12px;
}

.filter-action {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn-reset,
.btn-search {
  border: none;
  border-radius: 6px;
  padding: 10px 18px;
  cursor: pointer;
}

.btn-reset {
  background: #f3f4f6;
}

.btn-search {
  background: #d8f5eb;
  color: #00875a;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 20px;
}

.list-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  width: 32px;
  height: 32px;
  background: #f79b66;
  color: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.list-title h3 {
  margin: 0;
  font-size: 15px;
}

.list-title span {
  color: #888;
  font-size: 12px;
}

.btn-add {
  background: #f79b66;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 16px;
  cursor: pointer;
}

thead {
  background: #f79b66;
}

thead th {
  padding: 15px;
  text-align: center;
}

tbody td {
  padding: 15px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

.no-data {
  text-align: center;
  padding: 20px;
}

.status-tabs {
  display: flex;
  gap: 8px;
  margin: 10px 20px 15px;
}

.status-tabs button {
  height: 32px;
  padding: 0 16px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: white;
  cursor: pointer;
}

.status-tabs button.active {
  background: #f79b66;
  color: white;
  border-color: #f79b66;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin: 20px 0;
}

.pagination button {
  width: 32px;
  height: 32px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  cursor: pointer;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.action-btn {
  border: none;
  padding: 8px 10px;
  border-radius: 6px;
  cursor: pointer;
  margin: 0 3px;
}

.view-btn {
  background: #3b82f6;
  color: white;
}

.edit-btn {
  background: #f59e0b;
  color: white;
}

.toggle-btn {
  height: 33px;
  background: #10b981;
  color: white;
}
</style>