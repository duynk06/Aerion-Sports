<template>
  <MainLayout title="Chi Tiết Phiếu Giảm Giá">
    <div class="split-layout">

      <div class="form-container left-panel">
        <div class="list-header">
          <div class="list-title">
            <div class="title-icon" style="background: #3b82f6;">
              <i class="fa-solid fa-eye"></i>
            </div>
            <div>
              <h3>Chi tiết phiếu giảm giá</h3>
              <span>Xem thông tin mã khuyến mãi</span>
            </div>
          </div>
        </div>

        <div class="flex-form">
          <div class="form-grid">
            <div class="form-group">
              <label>Mã phiếu giảm giá</label>
              <input type="text" v-model="form.maPhieuGiamGia" disabled />
            </div>

            <div class="form-group">
              <label>Tên phiếu giảm giá</label>
              <input type="text" v-model="form.tenPhieuGiamGia" disabled />
            </div>

            <div class="form-group">
              <label>Loại phiếu</label>
              <select v-model="form.loaiPhieuGiamGia" disabled>
                <option value="Sale %">Sale %</option>
                <option value="Free Ship">Free Ship</option>
              </select>
            </div>

            <div class="form-group">
              <label>Giá trị giảm {{ form.loaiPhieuGiamGia === 'Sale %' ? '(%)' : '(đ)' }}</label>
              <input v-if="form.loaiPhieuGiamGia === 'Sale %'" type="text" :value="form.giaTriGiam + '%'" disabled />
              <input v-else type="text" :value="formatCurrency(form.giaTriGiam) + ' đ'" disabled />
            </div>

            <div class="form-group">
              <label>Đơn tối thiểu (đ)</label>
              <input type="text" :value="formatCurrency(form.giaTriDonToiThieu)" disabled />
            </div>

            <div class="form-group">
              <label>Giảm tối đa (đ)</label>
              <input type="text" :value="formatCurrency(form.giaTriGiamToiDa)" disabled />
            </div>

            <div class="form-group">
              <label>Số lượng</label>
              <input type="number" v-model="form.soLuong" disabled />
            </div>

            <div class="form-group">
              <label>Ngày bắt đầu</label>
              <input type="datetime-local" v-model="form.ngayBatDau" disabled />
            </div>

            <div class="form-group">
              <label>Ngày kết thúc</label>
              <input type="datetime-local" v-model="form.ngayKetThuc" disabled />
            </div>

            <div class="form-group hidden-placeholder"></div>

            <div class="form-group full-width">
              <label>Mô tả</label>
              <textarea rows="4" v-model="form.moTa" disabled></textarea>
            </div>
          </div>

          <div class="button-group mt-auto">
            <button type="button" class="btn-cancel" @click="$router.back()">Quay lại</button>
            <button type="button" class="btn-save" style="background: #f59e0b;" @click="goToEdit">
              Chuyển sang Sửa
            </button>
          </div>
        </div>
      </div>

      <div class="form-container right-panel">
        <div class="target-group">
          <label class="target-label">Đối tượng áp dụng</label>
          <div class="radio-group">
            <label class="radio-label">
              <input type="radio" v-model="form.doiTuongApDung" value="ALL" disabled />
              Tất cả khách hàng
            </label>
            <label class="radio-label">
              <input type="radio" v-model="form.doiTuongApDung" value="PERSONAL" disabled />
              Khách hàng cụ thể
            </label>
          </div>
        </div>

        <div class="divider"></div>

        <div class="table-area">
          <div :class="{ 'disabled-area': form.doiTuongApDung === 'ALL' }" class="table-content">
            <div class="list-header" style="padding-bottom: 15px; margin-bottom: 15px;">
              <div class="list-title">
                <div class="title-icon" style="background: #10b981; width: 32px; height: 32px; font-size: 14px;">
                  <i class="fa-solid fa-user-check"></i>
                </div>
                <div>
                  <h3>Danh sách khách hàng áp dụng</h3>
                  <span>Có tổng cộng <strong style="color: #ea9154;">{{ selectedKhachHang.length }}</strong> khách hàng được chọn</span>
                </div>
              </div>
            </div>

            <div class="customer-table-container">
              <table class="customer-table">
                <thead>
                  <tr>
                    <th>Mã KH</th>
                    <th>Tên KH</th>
                    <th>Email</th>
                    <th>SĐT</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="selectedKhachHang.length === 0">
                    <td colspan="4" class="no-data">Không có khách hàng cụ thể nào được chọn</td>
                  </tr>
                  <tr v-for="kh in selectedKhachHang" :key="kh.id">
                    <td>{{ kh.maKhachHang }}</td>
                    <td class="font-medium">{{ kh.tenKhachHang }}</td>
                    <td>{{ kh.email }}</td>
                    <td>{{ kh.soDienThoai }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <div class="overlay-msg" v-if="form.doiTuongApDung === 'ALL'">
            Phiếu này áp dụng cho toàn bộ khách hàng.
          </div>
        </div>
      </div>

    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import MainLayout from '../layouts/MainLayout.vue'
import { fetchAllPhieuGiamGia } from '@/service/PhieuGiamGiaService'
import { useRouter, useRoute } from 'vue-router'

// ĐÃ KẾT NỐI API KHÁCH HÀNG THẬT
import { fetchAllKhachHang } from '@/service/KhachHangService'

const router = useRouter()
const route = useRoute()

const form = ref({
  maPhieuGiamGia: '',
  tenPhieuGiamGia: '',
  loaiPhieuGiamGia: 'Sale %',
  giaTriGiam: 0,
  giaTriDonToiThieu: 0,
  giaTriGiamToiDa: 0,
  soLuong: 0,
  ngayBatDau: '',
  ngayKetThuc: '',
  moTa: '',
  trangThai: 1,
  doiTuongApDung: 'ALL',
  khachHangIds: []
})

const khachHangList = ref([])

const formatCurrency = (value) => {
  if (!value && value !== 0) return '0'
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
}

const goToEdit = () => {
  router.push(`/phieu-giam-gia/sua/${route.params.id}`)
}

// LẤY DỮ LIỆU TỪ API THẬT
const loadKhachHang = async () => {
  try {
    const data = await fetchAllKhachHang()
    khachHangList.value = data || []
  } catch (error) {
    console.error('Lỗi tải danh sách khách hàng:', error)
    khachHangList.value = []
  }
}

const loadPhieuGiamGia = async () => {
  try {
    const data = await fetchAllPhieuGiamGia()
    const phieu = data.find(x => x.id == route.params.id)

    if (phieu) {
      const isPersonal = phieu.khachHangIds && phieu.khachHangIds.length > 0;

      form.value = {
        ...phieu,
        ngayBatDau: phieu.ngayBatDau?.slice(0, 16),
        ngayKetThuc: phieu.ngayKetThuc?.slice(0, 16),
        khachHangIds: phieu.khachHangIds || [],
        doiTuongApDung: isPersonal ? 'PERSONAL' : 'ALL'
      }
    }
  } catch (error) {
    console.error('Lỗi tải dữ liệu phiếu giảm giá:', error)
    alert('Không thể tải dữ liệu phiếu giảm giá!')
  }
}

const selectedKhachHang = computed(() => {
  if (form.value.doiTuongApDung === 'ALL') return []
  return khachHangList.value.filter(kh => form.value.khachHangIds.includes(kh.id))
})

onMounted(async () => {
  await loadKhachHang()
  await loadPhieuGiamGia()
})
</script>

<style scoped>
.split-layout {
  display: flex;
  flex-direction: row;
  align-items: stretch;
  gap: 20px;
  width: 100%;
}

.left-panel {
  width: 55%;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.right-panel {
  width: calc(45% - 20px);
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.form-container {
  background: #ffffff;
  padding: 24px;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f3f4f6;
}

.list-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  width: 38px;
  height: 38px;
  color: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.list-title h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #111827;
}

.list-title span {
  color: #6b7280;
  font-size: 13px;
  margin-top: 2px;
  display: block;
}

.flex-form {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 8px;
  font-size: 13px;
  font-weight: 600;
  color: #374151;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  box-sizing: border-box;
  height: 42px;
  padding: 0 14px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 14px;
  color: #374151;
  background-color: #f9fafb;
  cursor: not-allowed;
}

.form-group textarea {
  height: auto;
  padding: 12px 14px;
  resize: none;
}

.full-width {
  grid-column: 1 / -1;
}

.hidden-placeholder {
  display: none;
}

@media (min-width: 768px) {
  .hidden-placeholder {
    display: block;
  }
}

.target-group {
  margin-bottom: 20px;
}

.target-label {
  font-size: 15px;
  font-weight: 700;
  color: #111827;
  margin-bottom: 12px;
  display: block;
}

.radio-group {
  display: flex;
  gap: 30px;
  margin-top: 4px;
}

.radio-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #6b7280;
  cursor: not-allowed;
}

.radio-label input[type="radio"] {
  width: 16px;
  height: 16px;
  accent-color: #9ca3af;
  cursor: not-allowed;
}

.divider {
  border-top: 1px solid #f3f4f6;
  margin-bottom: 20px;
}

.mt-auto {
  margin-top: auto;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid #f3f4f6;
}

.btn-cancel,
.btn-save {
  height: 40px;
  padding: 0 24px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  transition: background-color 0.2s ease;
}

.btn-cancel {
  background: #f3f4f6;
  color: #4b5563;
}

.btn-cancel:hover {
  background: #e5e7eb;
}

.btn-save {
  color: white;
}

.btn-save:hover {
  filter: brightness(0.9);
}

.table-area {
  position: relative;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.table-content {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.disabled-area {
  opacity: 0.5;
  pointer-events: none;
  user-select: none;
}

.overlay-msg {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 12px 20px;
  border-radius: 8px;
  font-size: 13px;
  text-align: center;
  line-height: 1.5;
  z-index: 10;
}

.customer-table-container {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  overflow-y: auto;
  flex: 1;
  min-height: 250px;
}

.customer-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

.customer-table thead {
  position: sticky;
  top: 0;
  background: #f9fafb;
  z-index: 1;
}

.customer-table th {
  padding: 12px;
  font-size: 13px;
  font-weight: 600;
  color: #4b5563;
  border-bottom: 1px solid #e5e7eb;
  white-space: nowrap;
}

.customer-table td {
  padding: 12px;
  font-size: 13px;
  color: #4b5563;
  border-bottom: 1px solid #f3f4f6;
}

.font-medium {
  font-weight: 600;
  color: #111827 !important;
}

.no-data {
  text-align: center;
  color: #9ca3af;
  padding: 20px !important;
  font-style: italic;
}

@media (max-width: 1024px) {
  .split-layout {
    flex-direction: column;
  }

  .left-panel,
  .right-panel {
    width: 100%;
  }
}
</style>