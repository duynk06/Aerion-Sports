<script setup>
import logoShip from '@/assets/logo/image.png'
import { printInvoice } from '@/utils/PrintHoaDon'
import {
  ref,
  onMounted,
  computed,
  watch
} from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  getLichSuThanhToan
} from '@/service/LichSuThanhToanService'
import MainLayout from '@/layouts/MainLayout.vue'
import {
  getChiTietHoaDon
} from '@/service/ChiTietHoaDonDetailService'
import {
  getHoaDonById
} from '@/service/HoaDonService'

import {
  getLichSuHoaDon
} from '@/service/ChiTietHoaDonService'

const formatCurrency = (value) => {
  if (!value) return '0 đ'

  return Number(value).toLocaleString('vi-VN') + ' đ'
}

const historyKeyword = ref('')

const historyAction = ref('')
const danhSachSanPham = ref([])
const lichSuThanhToan = ref([])
const searchKeyword = ref('')
const lichSuList = ref([])
const selectedMauSac = ref('')

const selectedTrongLuong = ref('')

const currentPage = ref(1)

const pageSize = 5
const cancelHistory = computed(() => {
  return lichSuList.value.find(
    item => item.trangThaiMoi === 7
  )
})
const mauSacOptions = computed(() => {
  
return [
  ...new Set(
    danhSachSanPham.value.map(
      item => item.mauSac
    )
  )
]

})
const filteredHistory = computed(() => {

return lichSuList.value.filter(item => {

  const matchName =

    !historyKeyword.value ||

    item.tenNhanVien
      ?.toLowerCase()
      .includes(
        historyKeyword.value.toLowerCase()
      )

  const matchAction =

    !historyAction.value ||

    item.trangThaiMoi ==
    historyAction.value

  return (
    matchName &&
    matchAction
  )

})
})
const openChangeStatusModal = () => {
  showChangeStatusModal.value = true
}
const resetHistoryFilter = () => {

historyKeyword.value = ''

historyAction.value = ''


}
const trongLuongOptions = computed(() => {

return [
  ...new Set(
    danhSachSanPham.value.map(
      item => item.trongLuong
    )
  )
]

})
const filteredProducts = computed(() => {

return danhSachSanPham.value.filter(item => {

  const keyword =
    searchKeyword.value.toLowerCase()

  const matchKeyword =

    item.maSanPham
      ?.toLowerCase()
      .includes(keyword)

    ||

    item.tenSanPham
      ?.toLowerCase()
      .includes(keyword)

  const matchMauSac =

    !selectedMauSac.value ||

    item.mauSac ===
    selectedMauSac.value

  const matchTrongLuong =

    !selectedTrongLuong.value ||

    item.trongLuong ===
    selectedTrongLuong.value

  return (
    matchKeyword &&
    matchMauSac &&
    matchTrongLuong
  )

})

})
const totalProducts = computed(() => {
  return filteredProducts.value.length
})
const totalPages = computed(() => {

return Math.ceil(
  filteredProducts.value.length /
  pageSize
)

})
const paginatedProducts = computed(() => {

const start =
  (currentPage.value - 1) *
  pageSize

const end =
  start + pageSize

return filteredProducts.value.slice(
  start,
  end
)

})
const resetFilter = () => {

searchKeyword.value = ''

selectedMauSac.value = ''

selectedTrongLuong.value = ''

currentPage.value = 1

}
watch(
  [
    searchKeyword,
    selectedMauSac,
    selectedTrongLuong
  ],
  () => {

    currentPage.value = 1

  }
)
const loadChiTietHoaDon = async () => {

try {

  danhSachSanPham.value =
    await getChiTietHoaDon(idHoaDon)

  console.log(
    "Chi tiết hóa đơn:",
    danhSachSanPham.value
  )

} catch (error) {

  console.error(
    "Lỗi lấy chi tiết hóa đơn:",
    error
  )

}
}
const loadLichSuThanhToan = async () => {
  try {

    lichSuThanhToan.value =
      await getLichSuThanhToan(idHoaDon)

    console.log(
      "Lịch sử thanh toán:",
      lichSuThanhToan.value
    )

  } catch (error) {

    console.error(error)

  }
}

const loadHoaDon = async () => {
  try {
    const response = await getHoaDonById(idHoaDon)
    hoaDon.value = response

    // Sửa lại logic so sánh số 0 (Tại quầy)
    if (response.loaiHoaDon === 0) { 
      currentStatus.value = 5
    } else {
      currentStatus.value = response.trangThai
    }
  } catch (error) {
    console.error(error)
  }
}
const hoaDon = ref({})
// Router
const router = useRouter()
const route = useRoute()
const isCancelled = () => currentStatus.value === 6
// Id hóa đơn
const idHoaDon = route.params.id

// Modal lịch sử
const showHistoryModal = ref(false)
const showChangeStatusModal = ref(false)
// Danh sách lịch sử
const lastHistory = computed(() => {
  if (!lichSuList.value.length) return null

  return lichSuList.value[0]
})
// Trạng thái hiện tại của hóa đơn
const currentStatus = ref(0)

// Danh sách các bước
const onlineSteps = [
  { value: 0, label: 'Chờ xác nhận', icon: 'fa-hourglass-start' },
  { value: 1, label: 'Đã xác nhận', icon: 'fa-circle-check' },
  { value: 2, label: 'Chờ giao hàng', icon: 'fa-box' },
  { value: 3, label: 'Đang giao hàng', icon: 'fa-truck' },
  { value: 4, label: 'Đã giao hàng', icon: 'fa-truck-ramp-box' },
  { value: 5, label: 'Đã hoàn thành', icon: 'fa-flag-checkered' }
]
const orderSteps = computed(() => {

// Đơn tại quầy
if (isTaiQuay.value) {
  return [
    {
      value: 5,
      label: 'Đã hoàn thành',
      icon: 'fa-flag-checkered'
    }
  ]
}

// Trạng thái hiện tại
const status = currentStatus.value

// Trạng thái trước khi huỷ
const previousStatus =
  lichSuList.value.find(
    x => x.trangThaiMoi === 7
  )?.trangThaiCu ?? 0

// Đơn online bình thường
if (![6, 7, 8].includes(status)) {
  return onlineSteps.filter(
    step => step.value >= 0 && step.value <= 5
  )
}

// Yêu cầu huỷ
if (status === 7) {
  return [
    ...onlineSteps.filter(
      step =>
        step.value >= 0 &&
        step.value <= previousStatus
    ),
    {
      value: 7,
      label: 'Yêu cầu huỷ',
      icon: 'fa-triangle-exclamation'
    }
  ]
}

// Đã huỷ
if (status === 6) {
  return [
    ...onlineSteps.filter(
      step =>
        step.value >= 0 &&
        step.value <= previousStatus
    ),
    {
      value: 7,
      label: 'Yêu cầu huỷ',
      icon: 'fa-triangle-exclamation'
    },
    {
      value: 6,
      label: 'Đã huỷ',
      icon: 'fa-ban'
    }
  ]
}

// Đã hoàn tiền
if (status === 8) {
  return [
    ...onlineSteps.filter(
      step =>
        step.value >= 0 &&
        step.value <= previousStatus
    ),
    {
      value: 7,
      label: 'Yêu cầu huỷ',
      icon: 'fa-triangle-exclamation'
    },
    {
      value: 8,
      label: 'Đã hoàn tiền',
      icon: 'fa-money-bill-transfer'
    }
  ]
}

return []
})
const isStepActive = (stepValue) => {

const status = currentStatus.value

// Đơn tại quầy
if (isTaiQuay.value) {
  return true
}

// Luồng bình thường
if (![6, 7, 8].includes(status)) {
  return stepValue <= status
}

// Yêu cầu huỷ
if (status === 7) {
  const cancelRecord =
    lichSuList.value.find(
      x => x.trangThaiMoi === 7
    )

  const previous =
    cancelRecord?.trangThaiCu ?? 0

  console.log(
    "step:",
    stepValue,
    "previous:",
    previous
  )

  return (
    stepValue <= previous ||
    stepValue === 7
  )
}

// Đã huỷ
if (status === 6) {

const cancelRecord =
  lichSuList.value.find(
    x => x.trangThaiMoi === 7
  )

const previous =
  cancelRecord?.trangThaiCu ?? 0

return (
  stepValue <= previous ||
  stepValue === 7 ||
  stepValue === 6
)
}

// Đã hoàn tiền
if (status === 8) {

const cancelRecord =
  lichSuList.value.find(
    x => x.trangThaiMoi === 7
  )

const previous =
  cancelRecord?.trangThaiCu ?? 0

return (
  stepValue <= previous ||
  stepValue === 7 ||
  stepValue === 8
)
}

return false
}
const isTaiQuay = computed(() => {
  return hoaDon.value?.loaiHoaDon === 0
})
// Quay lại
const goBack = () => {
  router.push('/hoa-don')
}


// Load lịch sử hóa đơn
const loadLichSuHoaDon = async () => {
  try {

    lichSuList.value =
      await getLichSuHoaDon(idHoaDon)

  } catch (error) {

    console.error(
      'Lỗi load lịch sử hóa đơn:',
      error
    )

  }
}

const getTrangThaiText = (status) => {
  switch (status) {
    case 0:
      return 'Chờ xác nhận'

    case 1:
      return 'Đã xác nhận'

    case 2:
      return 'Chờ giao hàng'

    case 3:
      return 'Đang giao hàng'

    case 4:
      return 'Đã giao hàng'

    case 5:
      return 'Đã hoàn thành'

    case 6:
      return 'Đã hủy'

    case 7:
      return 'Yêu cầu hủy'

    case 8:
      return 'Đã hoàn tiền'

    default:
      return 'Khởi tạo'
  }
}

// Mounted
onMounted(async () => {

await loadHoaDon()

await loadLichSuHoaDon()
console.log("currentStatus", currentStatus.value)
console.log("lastHistory", lastHistory.value)
console.log("lichSuList", lichSuList.value)
await loadLichSuThanhToan()

await loadChiTietHoaDon()

})
</script>
<template>
  <MainLayout title="Chi tiết hóa đơn">

    <!-- Header -->
    <div class="page-header">

<div class="header-left">
  <button class="back-btn" @click="goBack">
    <i class="fa-solid fa-arrow-left"></i>
  </button>

  <div>
    <h2 class="page-title">
      Chi tiết hóa đơn
    </h2>

    <span class="invoice-code">
      {{ hoaDon.maHoaDon }}
    </span>
  </div>
</div>

<button
  class="btn-print-invoice"
  @click="async () => await printInvoice(
  hoaDon,
  danhSachSanPham,
  lichSuThanhToan
)"
>
  <i class="fa-solid fa-print"></i>
  In hóa đơn
</button>

</div>

    <!-- HÀNG 1 -->
    <!-- HÀNG 1 -->
    <div class="card">

<div class="card-title">
  <i class="fa-solid fa-truck-fast"></i>
  <span>Trạng thái đơn hàng</span>
</div>

<div class="order-steps">
  <div
  v-for="step in orderSteps"
  :key="step.value"
  class="step"
  :class="{
    active: isStepActive(step.value),
    'active-line': isStepActive(step.value)
  }"
>
<div
  class="step-icon"
  :class="{ active: isStepActive(step.value) }"
>
  <i :class="['fa-solid', step.icon]"></i>
</div>

<div
  class="step-label"
  :class="{ active: isStepActive(step.value) }"
>
  {{ step.label }}
</div>
  </div>
</div>

<!-- Bọc nút vào div -->
<div class="action-group">

<button
  class="change-status-btn"
  @click="openChangeStatusModal"
>
  <i class="fa-solid fa-arrows-rotate"></i>
  Chuyển trạng thái
</button>

<button
  class="history-btn"
  @click="showHistoryModal = true"
>
  <i class="fa-solid fa-clock-rotate-left"></i>
  Lịch sử thao tác
</button>

</div>

</div>
    <!-- HÀNG 2 -->
   <!-- HÀNG 2 -->
<div class="middle-row">

<!-- Tổng kết thanh toán -->
<div class="card">

  <div class="card-title">
    <i class="fa-solid fa-money-bill-wave"></i>
    <span>Tổng kết thanh toán</span>
  </div>

  <div class="summary-item">
  <span>Tổng tiền hàng</span>
  <span>{{ formatCurrency(hoaDon.tongTienHang) }}</span>
</div>

<div class="summary-item">
  <span>
    Giảm giá
    <small v-if="hoaDon.maPhieuGiamGia">
      ({{ hoaDon.maPhieuGiamGia }})
    </small>
  </span>

  <span>
    {{ formatCurrency(hoaDon.tienGiam) }}
  </span>
</div>

<div class="summary-item">
  <span class="shipping-label">
    
    Phí vận chuyển
    <img
      :src="logoShip"
      alt="Shipping"
      class="shipping-logo"
    >
  </span>

  <span>
    {{ formatCurrency(hoaDon.tienVanChuyen) }}
  </span>
</div>

<hr>

<div class="summary-total">
  <span>Tổng thanh toán</span>
  <span>{{ formatCurrency(hoaDon.tongTienThanhToan) }}</span>
</div>
</div>

<!-- Khách hàng -->
<div class="card">

  <div class="card-title">
    <i class="fa-solid fa-user"></i>
    <span>Thông tin khách hàng</span>
  </div>

  <div class="info-row">
  <label>Họ tên:</label>
  <span>{{ hoaDon.tenNguoiNhan }}</span>
</div>

<div class="info-row">
  <label>SĐT:</label>
  <span>{{ hoaDon.sdtNguoiNhan }}</span>
</div>

<div class="info-row">
  <label>Email:</label>
  <span>{{ hoaDon.email }}</span>
</div>
</div>

<!-- Giao hàng -->
<div class="card">

  <div class="card-title">
    <i class="fa-solid fa-location-dot"></i>
    <span>Thông tin giao hàng</span>
  </div>
  <div class="info-row">
  <label>Địa chỉ:</label>
  <span>{{ hoaDon.diaChiNhan }}</span>
</div>

<div class="info-row">
  <label>Loại đơn:</label>
  <span>
    {{ hoaDon.loaiHoaDon === 0 ? 'Tại quầy' : 'Online' }}
  </span>
</div>

<div class="info-row">
  <label>Ghi chú:</label>
  <span>{{ hoaDon.ghiChu }}</span>
</div>
</div>

</div>

<!-- HÀNG 3 -->
<!-- HÀNG 3 -->
<div class="card">

<div class="card-title">
  <i class="fa-solid fa-clock-rotate-left"></i>
  <span>Lịch sử thanh toán</span>
</div>

<table class="payment-table">
  <thead>
    <tr>
      <th>STT</th>
      <th>Số tiền</th>
      <th>Thời gian</th>
      <th>Phương thức</th>
      <th>Ghi chú</th>
    </tr>
  </thead>

  <tbody>

    <tr
  v-for="(item,index) in lichSuThanhToan"
  :key="item.id"
>
  <td>{{ index + 1 }}</td>

  <td>
    {{ formatCurrency(item.soTien) }}
  </td>

  <td>
    {{
      new Date(item.ngayThanhToan)
      .toLocaleString('vi-VN')
    }}
  </td>

  <td>
    {{ item.phuongThucThanhToan }}
  </td>

  <td>
    {{ item.ghiChu }}
  </td>
</tr>

  </tbody>
</table>

</div>

    <!-- HÀNG 3 -->
    <div class="card">

      <div class="card-title">
        <i class="fa-solid fa-cart-shopping"></i>
        <span>Danh sách sản phẩm</span>
      </div>

      <div class="table-toolbar">

<input
  v-model="searchKeyword"
  type="text"
  placeholder="Tìm mã hoặc tên sản phẩm..."
>

<select v-model="selectedMauSac">

  <option value="">
    Tất cả màu sắc
  </option>

  <option
    v-for="item in mauSacOptions"
    :key="item"
    :value="item"
  >
    {{ item }}
  </option>

</select>

<select v-model="selectedTrongLuong">

  <option value="">
    Tất cả trọng lượng
  </option>

  <option
    v-for="item in trongLuongOptions"
    :key="item"
    :value="item"
  >
    {{ item }}
  </option>

</select>

<button
  class="reset-btn"
  @click="resetFilter"
>
  <i class="fa-solid fa-rotate-right"></i>
</button>

</div>
      <table>

        <thead>
          <tr>
            <th>STT</th>
            <th>Mã sản phẩm</th>
            <th>Tên sản phẩm</th>
            <th>Trọng Lượng1</th>
            <th>Màu sắc</th>
            <th>Số lượng</th>
            <th>Đơn giá</th>
            <th>Thành tiền</th>
          </tr>
        </thead>

        <tbody>

<tr
  v-for="(item,index) in paginatedProducts"
  :key="item.id"
>

  <td>{{ index + 1 }}</td>

  <td>{{ item.maSanPham }}</td>

  <td>{{ item.tenSanPham }}</td>

  <td>{{ item.trongLuong }}</td>

  <td>{{ item.mauSac }}</td>

  <td>{{ item.soLuong }}</td>

  <td>{{ formatCurrency(item.donGia) }}</td>

  <td>{{ formatCurrency(item.thanhTien) }}</td>

</tr>

</tbody>

      </table>
      <div class="table-footer">

<div class="table-info">
  Tổng:
  <strong>{{ totalProducts }}</strong>
  sản phẩm
</div>

<div class="pagination">

  <div class="page-center">

    <button
      :disabled="currentPage === 1"
      @click="currentPage--"
    >
      <
    </button>

    <span>
      Trang {{ currentPage }} / {{ totalPages || 1 }}
    </span>

    <button
      :disabled="currentPage === totalPages"
      @click="currentPage++"
    >
      >
    </button>

  </div>

</div>



</div>

    </div>

  </MainLayout>
  <div
  v-if="showHistoryModal"
  class="modal-overlay"
  @click.self="showHistoryModal = false"
>

  <div class="history-modal">

    <div class="modal-header">

      <h3>
        <i class="fa-solid fa-clock-rotate-left"></i>
        Lịch sử thao tác
      </h3>

      <button
        class="close-btn"
        @click="showHistoryModal = false"
      >
        <i class="fa-solid fa-xmark"></i>
      </button>

    </div>

    <div class="history-filter">

<div class="filter-group">
  <label>Người thao tác</label>
  <input
  v-model="historyKeyword"
  type="text"
  placeholder="Nhập tên người thao tác"
/>
</div>

<div class="filter-group">
  <label>Hành động</label>
  <select v-model="historyAction">
  <option value="">Tất cả</option>
  <option value="0">Chờ xác nhận</option>
  <option value="1">Đã xác nhận</option>
  <option value="2">Chờ giao hàng</option>
  <option value="3">Đang giao hàng</option>
  <option value="4">Đã giao hàng</option>
  <option value="5">Đã hoàn thành</option>
  <option value="6">Đã hủy</option>
  <option value="7">Yêu cầu hủy</option>
  <option value="8">Đã hoàn tiền</option>
</select>
</div>

<button class="search-history-btn"  @click="resetHistoryFilter">
  <i class="fa-solid fa-rotate-right"></i>
</button>

</div>

<table class="history-table">

<thead>
  <tr>
    <th>STT</th>
    <th>Trạng thái</th>
<th>Người thao tác</th>
<th>Hành động</th>
<th>Thời gian</th>
  </tr>
</thead>

<tbody>
  <tr
  v-for="(item,index) in filteredHistory"
  :key="item.id"
>
    <td>{{ index + 1 }}</td>

    <td>
  {{ getTrangThaiText(item.trangThaiMoi) }}
</td>

    <td>{{ item.tenNhanVien }}</td>


<td>{{ item.hanhDong }}</td>

<td>
  {{
    new Date(item.thoiGianHanhDong)
      .toLocaleString('vi-VN')
  }}
</td>
  </tr>
</tbody>

</table>

  </div>
  
</div>
<div
  v-if="showChangeStatusModal"
  class="modal-overlay"
  @click.self="showChangeStatusModal = false"
>
  <div class="status-modal">

    <div class="modal-header">
      <h3>
        <i class="fa-solid fa-arrows-rotate"></i>
        Chuyển trạng thái đơn hàng
      </h3>

      <button
        class="close-btn"
        @click="showChangeStatusModal = false"
      >
        <i class="fa-solid fa-xmark"></i>
      </button>
    </div>

    <div class="modal-body">

     <div>
  <label>Hóa đơn:</label>&nbsp;
  <span>{{ hoaDon.maHoaDon }}</span>
</div>

      <br>
      <div class="form-group">
        <label>Ghi chú</label>

        <textarea
          v-model="ghiChu"
          rows="3"
          placeholder="Nhập ghi chú..."
        />
      </div>

      <div class="status-preview">

        <span>Chuyển trạng thái:</span>

        <span class="status-old">
          {{ getTrangThaiText(currentStatus) }}
        </span>

        <i class="fa-solid fa-arrow-right"></i>

        <span class="status-new">
          {{ getTrangThaiText(nextStatus) }}
        </span>

      </div>

      <div class="status-flow">

        <span>Trạng thái hiện tại</span>

        <div class="status-box old">
          {{ getTrangThaiText(currentStatus) }}
        </div>

        <i class="fa-solid fa-arrow-right"></i>

        <span>Trạng thái mới</span>

        <div class="status-box new">
          {{ getTrangThaiText(nextStatus) }}
        </div>

      </div>

      <p class="warning-text">
        Hành động này sẽ cập nhật trạng thái đơn hàng và ghi nhận lịch sử thay đổi.
      </p>

    </div>

    <div class="modal-footer">

      <button
        class="btn-cancel"
        @click="showChangeStatusModal = false"
      >
        Hủy
      </button>

      <button
        class="btn-confirm"
        @click="changeStatus"
      >
        Xác nhận
      </button>

    </div>

  </div>
</div>
</template>

<style scoped src="./ChiTietHoaDon.css">

/* Badge chung: Tất cả về nền xanh lá nhạt, chữ xanh lá đậm */
.invoice-badge, .status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 999px;
  font-size: 11px;
  font-weight: 600;
  
  /* Thiết lập màu sắc đồng nhất */
  background: #dcfce7; /* Nền xanh lá nhạt */
  color: #166534;      /* Chữ xanh lá đậm */
  
  /* Đảm bảo border cũng hài hòa */
  border: 1px solid #bbf7d0;
}
 

.card{
  background:#fff;
  border-radius:12px;
  padding:16px;
  margin-bottom:20px;
  border:1px solid #e5e7eb;
}

.card-header{
  background:#f79b66;
  color:#fff;
  padding:12px 16px;
  border-radius:8px;
  margin-bottom:20px;
  font-weight:600;
  font-size:14px;
}

.filter-grid{
  display:grid;
  grid-template-columns:repeat(4,1fr);
  gap:20px;
}

.filter-grid label{
  display:block;
  margin-bottom:6px;
  font-size:12px;
  font-weight:500;
}

.filter-grid input,
.filter-grid select{
  width:100%;
  height:38px;
  border:1px solid #ddd;
  border-radius:6px;
  padding:0 10px;
}

.filter-action{
  display:flex;
  justify-content:flex-end;
  gap:10px;
  margin-top:16px;
}
.btn-reset{
  border:none;
  background:#f3f4f6;
  padding:10px 18px;
  border-radius:6px;
  cursor:pointer;
}

.btn-export{
  border:none;
  background:#d8f5eb;
  color:#00875a;
  padding:10px 18px;
  border-radius:6px;
  cursor:pointer;
}


.list-title{
  display:flex;
  align-items:center;
  gap:10px;
  margin-bottom:12px;
}

.title-icon{
  width:28px;
  height:28px;
  border-radius:8px;
  background:#f79b66;
  color:#fff;
  display:flex;
  align-items:center;
  justify-content:center;
  font-size:12px;
}

.list-title h3{
  margin:0;
  font-size:14px;
  font-weight:600;
}

.list-title span{
  font-size:11px;
  color:#888;
}

.status-tabs{
  display:flex;
  flex-wrap:wrap;
  gap:6px;
  margin-bottom:15px;
}

.status-tabs button{
  height:28px;
  padding:0 12px;
  border-radius:20px;
  border:1px solid #e5e7eb;
  background:white;
  font-size:11px;
  cursor:pointer;
}

.status-tabs .active{
  background:#f79b66;
  color:white;
  border-color:#f79b66;
}


table{
  width:100%;
  border-collapse:collapse;
  font-size:12px;
  table-layout: fixed;
}

thead{
  background:#f79b66;
  color:white;
}

th,
td {
  padding: 10px;
  text-align: center;
  vertical-align: middle;
  white-space: nowrap;
}

tbody tr:hover{
  background:#fafafa;
}

.money{
  color:#d14343;
  font-weight:600;
}

/* BADGE */




/* ACTION */

.action-btn{
  width:32px;
  height:32px;
  border:1px solid #ddd;
  border-radius:6px;
  background:#fff;
  cursor:pointer;
  margin:0 2px;
}

.action-btn:hover{
  background:#f5f5f5;
}

/* PAGINATION */

.pagination{
  display:flex;
  align-items:center;
  font-size:12px;
  margin-top:0;
}

.page-center{
  display:flex;
  align-items:center;
  gap:10px;
}

.page-center button{
  width:28px;
  height:28px;
  border:1px solid #ddd;
  background:white;
  border-radius:4px;
  cursor:pointer;
}

.pagination select{
  height:32px;
  border:1px solid #ddd;
  border-radius:6px;
  padding:0 8px;
}
.table-footer{
  display:flex;
  justify-content:space-between;
  align-items:center; /* căn giữa theo chiều dọc */
  margin-top:20px;
}

.table-info{
  font-size:13px;
  color:#060606;
  margin:0; /* thêm */
}


.table-info strong{
  color:#060606;
}
</style>