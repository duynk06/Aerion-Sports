<script setup>
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

const selectedMauSac = ref('')

const selectedTrongLuong = ref('')

const currentPage = ref(1)

const pageSize = 5
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

    const response =
      await getHoaDonById(idHoaDon)

    hoaDon.value = response

    currentStatus.value =
      response.trangThai

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

// Danh sách lịch sử
const lichSuList = ref([])

// Trạng thái hiện tại của hóa đơn
const currentStatus = ref(0)

// Danh sách các bước
const orderSteps = [
  {
    value: 0,
    label: 'Chờ xác nhận',
    icon: 'fa-hourglass-start'
  },
  {
    value: 1,
    label: 'Đã xác nhận',
    icon: 'fa-circle-check'
  },
  {
    value: 2,
    label: 'Chờ giao hàng',
    icon: 'fa-box'
  },
  {
    value: 3,
    label: 'Đang giao hàng',
    icon: 'fa-truck'
  },
  {
    value: 4,
    label: 'Đã giao hàng',
    icon: 'fa-truck-ramp-box'
  },
  {
    value: 5,
    label: 'Đã hoàn thành',
    icon: 'fa-flag-checkered'
  }
]

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

  default:
    return 'Khởi tạo'
}
}

// Mounted
onMounted(async () => {

await loadHoaDon()

await loadLichSuHoaDon()

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
  @click="printInvoice(hoaDon, danhSachSanPham)"
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
    active: !isCancelled() && step.value <= currentStatus,
    'active-line': !isCancelled() && step.value < currentStatus
  }"
>
<div
  class="step-icon"
  :class="{
    active: !isCancelled() && step.value <= currentStatus
  }"
>
      <i :class="['fa-solid', step.icon]"></i>
    </div>

    <div
  class="step-label"
  :class="{
    active:
      !isCancelled() &&
      step.value <= currentStatus
  }"
>
      {{ step.label }}
    </div>
  </div>
</div>

<!-- Bọc nút vào div -->
<div class="history-action">
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
  <span>Giảm giá</span>
  <span>{{ formatCurrency(hoaDon.tienGiam) }}</span>
</div>

<div class="summary-item">
  <span>Phí vận chuyển</span>
  <span>{{ formatCurrency(hoaDon.tienVanChuyen) }}</span>
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
  <span>{{ hoaDon.hoTen }}</span>
</div>

<div class="info-row">
  <label>SĐT:</label>
  <span>{{ hoaDon.sdt }}</span>
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
  <span>{{ hoaDon.loaiHoaDon }}</span>
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
<th>Người thao tác</th>
<th>Trạng thái cũ</th>
<th>Trạng thái mới</th>
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

    <td>{{ item.tenNhanVien }}</td>

<td>
  {{ getTrangThaiText(item.trangThaiCu) }}
</td>

<td>
  {{ getTrangThaiText(item.trangThaiMoi) }}
</td>

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
</template>

<style scoped src="./ChiTietHoaDon.css"></style>