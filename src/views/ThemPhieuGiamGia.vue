<template>
  <MainLayout title="Thêm Phiếu Giảm Giá">
    <div class="split-layout">

      <div class="form-container left-panel">
        <div class="list-header">
          <div class="list-title">
            <div class="title-icon">
              <i class="fa-solid fa-plus"></i>
            </div>
            <div>
              <h3>Thêm phiếu giảm giá</h3>
              <span>Tạo mã khuyến mãi mới cho khách hàng</span>
            </div>
          </div>
        </div>

        <form @submit.prevent="handleCreate" class="flex-form">
          <div class="form-grid">
            <div class="form-group">
              <label>Mã phiếu giảm giá (Hệ thống tự tăng)</label>
              <input type="text" v-model="form.maPhieuGiamGia" disabled class="disabled-input" title="Mã phiếu tăng tự động" />
            </div>

            <div class="form-group">
              <label>Tên phiếu giảm giá <span class="required-star">*</span></label>
              <input type="text" v-model="form.tenPhieuGiamGia" placeholder="Nhập tên chương trình..." required />
            </div>

            <div class="form-group">
              <label>Loại phiếu</label>
              <select v-model="form.loaiPhieuGiamGia">
                <option value="Sale %">Sale %</option>
                <option value="Free Ship">Free Ship</option>
              </select>
            </div>

            <div class="form-group">
              <label>Giá trị giảm {{ form.loaiPhieuGiamGia === 'Sale %' ? '(%)' : '(đ)' }} <span class="required-star">*</span></label>
              <input v-if="form.loaiPhieuGiamGia === 'Sale %'" type="number" v-model.number="form.giaTriGiam" min="1" max="100" placeholder="Nhập số từ 1 - 100" />
              <input v-else type="text" :value="formatCurrency(form.giaTriGiam)" @input="onInputCurrency('giaTriGiam', $event)" placeholder="Nhập số tiền giảm" />
            </div>

            <div class="form-group">
              <label>Đơn tối thiểu (đ) <span class="required-star">*</span></label>
              <input type="text" :value="formatCurrency(form.giaTriDonToiThieu)" @input="onInputCurrency('giaTriDonToiThieu', $event)" placeholder="Ví dụ: 200.000" />
            </div>

            <div class="form-group">
              <label>Giảm tối đa (đ) <span class="required-star">*</span></label>
              <input type="text" :value="formatCurrency(form.giaTriGiamToiDa)" @input="onInputCurrency('giaTriGiamToiDa', $event)" placeholder="Ví dụ: 50.000" />
            </div>

            <div class="form-group">
              <label>Số lượng phát hành <span class="required-star">*</span></label>
              <input type="number" v-model.number="form.soLuong" min="1" placeholder="Nhập số lượng..." />
            </div>

            <div class="form-group">
              <label>Ngày bắt đầu <span class="required-star">*</span></label>
              <input type="datetime-local" v-model="form.ngayBatDau" required />
            </div>

            <div class="form-group">
              <label>Ngày kết thúc <span class="required-star">*</span></label>
              <input type="datetime-local" v-model="form.ngayKetThuc" required />
            </div>

            <div class="form-group hidden-placeholder"></div>

            <div class="form-group full-width">
              <label>Mô tả chiến dịch</label>
              <textarea rows="4" v-model="form.moTa" placeholder="Nhập ghi chú chi tiết..."></textarea>
            </div>
          </div>

          <div class="button-group mt-auto">
            <button type="button" class="btn-cancel" @click="$router.back()">Hủy</button>
            <button type="submit" class="btn-save">Lưu Khuyến Mãi</button>
          </div>
        </form>
      </div>

      <div class="form-container right-panel">
        <div class="target-group">
          <label class="target-label">Đối tượng áp dụng</label>
          <div class="radio-group">
            <label class="radio-label">
              <input type="radio" v-model="form.doiTuongApDung" value="ALL" />
              Tất cả khách hàng
            </label>
            <label class="radio-label">
              <input type="radio" v-model="form.doiTuongApDung" value="PERSONAL" />
              Khách hàng cụ thể
            </label>
          </div>
        </div>

        <div v-if="form.doiTuongApDung === 'PERSONAL'" class="table-animated-wrapper">
          <div class="divider"></div>

          <div class="table-area">
            <div class="table-content">
              <div class="list-header" style="padding-bottom: 15px; margin-bottom: 15px;">
                <div class="list-title">
                  <div class="title-icon" style="background: #3b82f6; width: 32px; height: 32px; font-size: 14px;">
                    <i class="fa-solid fa-users"></i>
                  </div>
                  <div>
                    <h3 style="font-size: 16px;">Chọn khách hàng áp dụng</h3>
                    <span>Đã chọn: <strong style="color: #f79b66;">{{ form.khachHangIds.length }}</strong> khách hàng</span>
                  </div>
                </div>
              </div>

              <div class="search-box mb-15">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input type="text" v-model="searchKhachHang" placeholder="Tìm tên, email, SĐT hoặc mã KH...">
              </div>

              <div class="customer-table-container">
                <table class="customer-table">
                  <thead>
                    <tr>
                      <th class="col-checkbox">
                        <input type="checkbox" :checked="isAllSelected" @change="toggleSelectAll" />
                      </th>
                      <th>Mã KH</th>
                      <th>Tên KH</th>
                      <th>Email</th>
                      <th>SĐT</th>
                      <th>Ngày sinh</th>
                      <th style="text-align: center;">Tổng đơn</th>
                      <th>Đơn gần nhất</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-if="filteredKhachHang.length === 0">
                      <td colspan="8" class="no-data">Không tìm thấy khách hàng nào hợp lệ</td>
                    </tr>
                    <tr v-for="kh in filteredKhachHang" :key="kh.id" @click="toggleSelectRow(kh.id)"
                      :class="{ 'selected-row': form.khachHangIds.includes(kh.id) }">
                      <td class="col-checkbox" @click.stop>
                        <input type="checkbox" :value="kh.id" v-model="form.khachHangIds" />
                      </td>
                      <td>{{ kh.maKhachHang }}</td>
                      <td class="font-medium">{{ kh.hoTen }}</td>
                      <td>{{ kh.email }}</td>
                      <td>{{ kh.sdt }}</td>
                      <td>{{ dinhDangNgaySinhUI(kh.ngaySinh) }}</td>
                      <td style="text-align: center;">
                        <span class="badge-count">{{ kh.tongSoDonHang || 0 }} đơn</span>
                      </td>
                      <td class="date-time-text">{{ dinhDangNgayGioUI(kh.donHangGanNhat) }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

      </div>

    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import MainLayout from '../layouts/MainLayout.vue'
import { createPhieuGiamGia, fetchAllPhieuGiamGia } from '@/service/PhieuGiamGiaService'
import { useRouter } from 'vue-router'
import { fetchAllKhachHang } from '@/service/KhachHangService'

const router = useRouter()

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
const searchKhachHang = ref('')

const sinhMaPhieuTuDongTang = async () => {
  try {
    const data = await fetchAllPhieuGiamGia()
    let maxNumber = 0

    if (data && data.length > 0) {
      data.forEach(phieu => {
        if (phieu.maPhieuGiamGia && phieu.maPhieuGiamGia.startsWith('PGG')) {
          const laySo = phieu.maPhieuGiamGia.replace('PGG', '')
          const num = parseInt(laySo, 10)
          if (!isNaN(num) && num > maxNumber) {
            maxNumber = num
          }
        }
      })
    }
    
    const nextNumber = maxNumber + 1
    const formatSo = nextNumber < 10 ? '0' + nextNumber : nextNumber
    form.value.maPhieuGiamGia = 'PGG' + formatSo
  } catch (error) {
    console.error('Lỗi tính toán sinh mã tự tăng:', error)
    form.value.maPhieuGiamGia = 'PGG01'
  }
}

// 🌟 ĐÃ THÊM: Định dạng Ngày/Tháng/Năm hiển thị lên bảng
const dinhDangNgaySinhUI = (dateStr) => {
  if (!dateStr) return '-';
  const date = new Date(dateStr);
  if (isNaN(date.getTime())) return dateStr;
  const d = String(date.getDate()).padStart(2, '0');
  const m = String(date.getMonth() + 1).padStart(2, '0');
  const y = date.getFullYear();
  return `${d}/${m}/${y}`;
}

// 🌟 ĐÃ THÊM: Định dạng Ngày Giờ mua đơn gần nhất trực quan
const dinhDangNgayGioUI = (dateTimeStr) => {
  if (!dateTimeStr) return 'Chưa mua hàng 🛒';
  const date = new Date(dateTimeStr);
  if (isNaN(date.getTime())) return dateTimeStr;
  
  const h = String(date.getHours()).padStart(2, '0');
  const m = String(date.getMinutes()).padStart(2, '0');
  const d = String(date.getDate()).padStart(2, '0');
  const mo = String(date.getMonth() + 1).padStart(2, '0');
  const y = date.getFullYear();
  
  return `${h}:${m} - ${d}/${mo}/${y}`;
}

const formatCurrency = (value) => {
  if (!value && value !== 0) return ''
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
}

const onInputCurrency = (field, event) => {
  let val = event.target.value
  val = val.replace(/[^0-9]/g, '')
  form.value[field] = val ? Number(val) : 0
  event.target.value = formatCurrency(form.value[field])
}

watch(() => form.value.loaiPhieuGiamGia, () => {
  form.value.giaTriGiam = 0
})

watch(() => form.value.doiTuongApDung, (newVal) => {
  if (newVal === 'ALL') {
    form.value.khachHangIds = []
    searchKhachHang.value = ''
  }
})

const loadKhachHang = async () => {
  try {
    const data = await fetchAllKhachHang()
    khachHangList.value = data || []
  } catch (error) {
    console.error('Lỗi tải danh sách khách hàng:', error)
    khachHangList.value = []
  }
}

const filteredKhachHang = computed(() => {
  if (!searchKhachHang.value) return khachHangList.value
  const keyword = searchKhachHang.value.toLowerCase()
  return khachHangList.value.filter(kh => {
    return (
      kh.hoTen?.toLowerCase().includes(keyword) ||
      kh.email?.toLowerCase().includes(keyword) ||
      kh.sdt?.includes(keyword) ||
      kh.maKhachHang?.toLowerCase().includes(keyword)
    )
  })
})

const isAllSelected = computed(() => {
  return filteredKhachHang.value.length > 0 &&
    filteredKhachHang.value.every(kh => form.value.khachHangIds.includes(kh.id))
})

const toggleSelectAll = (event) => {
  if (event.target.checked) {
    const newIds = filteredKhachHang.value.map(kh => kh.id)
    form.value.khachHangIds = [...new Set([...form.value.khachHangIds, ...newIds])]
  } else {
    const visibleIds = filteredKhachHang.value.map(kh => kh.id)
    form.value.khachHangIds = form.value.khachHangIds.filter(id => !visibleIds.includes(id))
  }
}

const toggleSelectRow = (id) => {
  const index = form.value.khachHangIds.indexOf(id)
  if (index === -1) {
    form.value.khachHangIds.push(id)
  } else {
    form.value.khachHangIds.splice(index, 1)
  }
}

// 🌟 HÀM VALIDATE CHUYÊN SÂU CHẶN ĐỨNG MỌI NGOẠI LỆ NGHIỆP VỤ
const handleCreate = async () => {
  if (!form.value.tenPhieuGiamGia.trim()) {
    return alert('Vui lòng nhập Tên phiếu giảm giá!')
  }
  if (form.value.tenPhieuGiamGia.trim().length < 4) {
    return alert('Tên phiếu giảm giá quá ngắn! Phải chứa từ 4 ký tự trở lên.')
  }
  if (form.value.soLuong <= 0) {
    return alert('Số lượng phiếu phát hành bắt buộc phải lớn hơn 0!')
  }

  // Khối validate giá trị dựa theo phân loại coupon
  if (form.value.loaiPhieuGiamGia === 'Sale %') {
    if (form.value.giaTriGiam <= 0 || form.value.giaTriGiam > 100) {
      return alert('Tỷ lệ phần trăm giảm giá phải nằm trong khoảng từ 1% đến 100%!')
    }
  } else {
    if (form.value.giaTriGiam <= 0) {
      return alert('Số tiền giảm của Free Ship phải lớn hơn 0đ!')
    }
  }

  if (form.value.giaTriDonToiThieu < 0 || form.value.giaTriGiamToiDa < 0) {
    return alert('Cấu hình mệnh giá tiền mặt không được đặt số âm!')
  }

  // Kiểm tra tính logic thời gian thực
  if (!form.value.ngayBatDau || !form.value.ngayKetThuc) {
    return alert('Vui lòng chọn thời gian hiệu lực cho chiến dịch khuyến mãi!')
  }

  const now = new Date()
  const start = new Date(form.value.ngayBatDau)
  const end = new Date(form.value.ngayKetThuc)
  now.setSeconds(0, 0)

  if (start < now) {
    return alert('Hệ thống từ chối! Ngày bắt đầu không được đặt mốc thời gian trong quá khứ.')
  }
  if (end <= start) {
    return alert('Hệ thống từ chối! Ngày kết thúc phải diễn ra sau ngày bắt đầu chương trình.')
  }

  // Khóa validate đối tượng khách hàng mục tiêu cụ thể
  if (form.value.doiTuongApDung === 'PERSONAL' && form.value.khachHangIds.length === 0) {
    return alert('Yêu cầu chọn cấu hình! Vui lòng tích chọn tối thiểu 1 khách hàng từ danh sách bên phải.')
  }

  const payload = {
    ...form.value,
    khachHangIds: form.value.doiTuongApDung === 'ALL' ? [] : form.value.khachHangIds
  }

  try {
    await createPhieuGiamGia(payload)
    alert('Thêm phiếu giảm giá thành công! Mã ưu đãi đang được tự động bắn hàng loạt về Mail của khách hàng. 🎉')
    router.push('/phieu-giam-gia')
  } catch (error) {
    const errorMsg = error.response?.data?.message?.toLowerCase() || ''
    if (errorMsg.includes('trùng') || errorMsg.includes('tồn tại') || errorMsg.includes('duplicate') || error.response?.status === 409) {
      alert('Mã phiếu giảm giá bị trùng lặp ngầm! Hệ thống đang tự cập nhật mã kế tiếp, vui lòng ấn Lưu lại.');
      await sinhMaPhieuTuDongTang();
    } else {
      alert(error.response?.data?.message || 'Có lỗi xảy ra khi xử lý dữ liệu phiếu!')
    }
  }
}

onMounted(async () => {
  loadKhachHang()
  await sinhMaPhieuTuDongTang()
})
</script>

<style scoped>
/* ==========================================================================
   CSS ĐỊNH DẠNG TỶ LỆ KHUNG GIAO DIỆN BẢNG RỘNG
   ========================================================================== */
.split-layout {
  display: flex;
  flex-direction: row;
  align-items: stretch;
  gap: 20px;
  width: 100%;
}

.left-panel {
  width: 42%; /* Co nhỏ form nhập để nhường không gian bề ngang cho bảng dữ liệu */
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.right-panel {
  width: calc(58% - 20px); /* Nâng diện tích bảng rộng 58% giúp hiển thị 8 cột nằm ngang */
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
}

.form-container {
  background: #ffffff;
  padding: 24px;
  border-radius: 12px;
  border: 1px solid #edf2f7;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 1px solid #edf2f7;
}

.list-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  width: 38px;
  height: 38px;
  background: #f79b66;
  color: white;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  box-shadow: 0 2px 4px rgba(247, 155, 102, 0.2);
}

.list-title h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #1a202c;
}

.list-title span {
  color: #718096;
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
  color: #4a5568;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  box-sizing: border-box;
  height: 40px;
  padding: 0 14px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 14px;
  color: #2d3748;
  outline: none;
  background-color: #fff;
  transition: all 0.2s;
}

.disabled-input {
  background-color: #f8fafc !important;
  color: #4a5568 !important;
  cursor: not-allowed;
  border-color: #e2e8f0 !important;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.form-group textarea {
  height: auto;
  padding: 12px 14px;
  resize: vertical;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: #f79b66;
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
  margin-bottom: 5px;
}

.target-label {
  font-size: 15px;
  font-weight: 700;
  color: #1a202c;
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
  cursor: pointer;
  color: #2d3748;
}

.radio-label input[type="radio"] {
  width: 16px;
  height: 16px;
  accent-color: #f79b66;
  cursor: pointer;
}

.divider {
  border-top: 1px solid #edf2f7;
  margin-bottom: 20px;
  margin-top: 20px;
}

.mt-auto {
  margin-top: auto;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid #edf2f7;
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
  background: #f79b66;
  color: white;
}

.btn-save:hover {
  background: #e08553;
}

.table-animated-wrapper {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.table-area {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.table-content {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.mb-15 {
  margin-bottom: 15px;
}

.search-box {
  position: relative;
  width: 100%;
}

.search-box input {
  width: 100%;
  height: 40px;
  box-sizing: border-box;
  padding: 0 14px 0 36px;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  font-size: 13.5px;
  outline: none;
  color: #2d3748;
  transition: border-color 0.2s;
}

.search-box input:focus {
  border-color: #f79b66;
}

.search-box i {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
  font-size: 14px;
}

.customer-table-container {
  border: 1px solid #edf2f7;
  border-radius: 8px;
  overflow-x: auto; /* Tự kích hoạt thanh thanh cuộn ngang nếu hiển thị trên màn hình nhỏ */
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
  background: #f8fafc;
  z-index: 1;
}

.customer-table th {
  padding: 12px;
  font-size: 12.5px;
  font-weight: 600;
  color: #4a5568;
  border-bottom: 1px solid #edf2f7;
  white-space: nowrap;
}

.customer-table td {
  padding: 12px;
  font-size: 12.5px;
  color: #2d3748;
  border-bottom: 1px solid #f1f5f9;
  white-space: nowrap; /* Khóa không cho dữ liệu ngày giờ và email bị bẻ gãy dòng */
}

.font-medium {
  font-weight: 600;
  color: #1a202c !important;
}

.badge-count {
  background-color: #e6fffa;
  color: #319795;
  padding: 2px 8px;
  border-radius: 12px;
  font-weight: bold;
  font-size: 12px;
  border: 1px solid #b2f5ea;
}

.date-time-text {
  color: #718096;
  font-size: 12px;
}

.customer-table tbody tr {
  cursor: pointer;
  transition: background 0.2s;
}

.customer-table tbody tr:hover {
  background: #fff8f5;
}

.customer-table tbody tr.selected-row {
  background: #fff1e7;
}

.col-checkbox {
  width: 40px;
  text-align: center !important;
}

.col-checkbox input[type="checkbox"] {
  accent-color: #f79b66;
  width: 16px;
  height: 16px;
  cursor: pointer;
}

.no-data {
  text-align: center;
  color: #a0aec0;
  padding: 24px !important;
  font-style: italic;
}

.required-star {
  color: #e53e3e;
  font-weight: bold;
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