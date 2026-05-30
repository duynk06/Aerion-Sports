<template>
  <MainLayout title="Quản lý hóa đơn">

    <!-- FILTER -->
    <div class="card">

      <div class="card-header">
        <i class="fa-solid fa-filter"></i>
        Bộ lọc tìm kiếm
      </div>

      <div class="filter-grid">

        <div>
          <label>Tìm kiếm</label>
          <input
  v-model="keyword"
  type="text"
  placeholder="Nhập mã hóa đơn / tên khách / SDT..."
  @keyup.enter="handleSearch"
/>
        </div>

        <div>
          <label>Loại hóa đơn</label>
          <select v-model="loaiHoaDonFilter">
  <option value="">Tất cả</option>
  <option value="Tại quầy">Tại quầy</option>
  <option value="Online">Online</option>
</select>
        </div>

        <div>
          <label>Từ ngày</label>
          <input
  type="date"
  v-model="tuNgay"
/>
        </div>

        <div>
          <label>Đến ngày</label>
          <input
  type="date"
  v-model="denNgay"
/>
        </div>

      </div>

      <div class="filter-action">
        <button
  class="btn-reset"
  @click="resetFilter"
>
  <i class="fa-solid fa-rotate-right"></i>
  Đặt lại
</button>

<button class="btn-export">
  <i class="fa-solid fa-file-excel"></i>
  Xuất Excel
</button>
      </div>

    </div>

    <!-- LIST -->
    <!-- DANH SÁCH HÓA ĐƠN -->
<div class="card">

<div class="list-title">

  <div class="title-icon">
    <i class="fa-solid fa-file-invoice"></i>
  </div>

  <div>

    <h3>Danh sách hóa đơn</h3>

    <span>Lọc nhanh theo trạng thái</span>

  </div>

</div>

<div class="status-tabs">

  <button class="active">Tất cả</button>

  <button>Chờ xác nhận</button>

  <button>Đang giao</button>

  <button>Đã giao</button>

  <button>Hoàn thành</button>

  <button>Đã hủy</button>

  <button>Yêu cầu hủy</button>

  <button>Đã hoàn tiền</button>

</div>

<table>
  <colgroup>
    <col style="width:5%">
    <col style="width:10%">
    <col style="width:14%">
    <col style="width:12%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:10%">
    <col style="width:12%">
    <col style="width:9%">
    <col style="width:8%">
  </colgroup>

  <thead>

    <tr>

      <th scope="col">STT</th>
      <th scope="col">Mã hóa đơn</th>
      <th scope="col">Tên nhân viên</th>
      <th scope="col">Khách hàng</th>
      <th scope="col">Số điện thoại</th>
      <th scope="col">Loại hóa đơn</th>
      <th scope="col">Tổng tiền</th>
      <th scope="col">Ngày tạo</th>
      <th scope="col">Trạng thái</th>
      <th scope="col">Hành động</th>

    </tr>

  </thead>

  <tbody>

    <tr v-for="hoaDon in filteredHoaDon" :key="hoaDon.id">
    <td>{{ hoaDon.id }}</td>
    <td>{{ hoaDon.maHoaDon }}</td>
    <td>{{ hoaDon.tenNv }}</td>
    <td>{{ hoaDon.hoTen }}</td>
    <td>{{ hoaDon.sdt }}</td>
    <td>
  <span
    class="invoice-badge"
    :class="getLoaiHoaDonClass(hoaDon.loaiHoaDon)"
  >
    {{ hoaDon.loaiHoaDon }}
  </span>
</td>
<td class="money">
  {{ formatCurrency(hoaDon.tongTienThanhToan) }}
</td>
    <td>{{ formatDate(hoaDon.ngayTao) }}</td>
    <td>
  <span
    class="status-badge"
    :class="getStatusClass(hoaDon.trangThai)"
  >
    {{ getTrangThaiText(hoaDon.trangThai) }}
  </span>
</td>
    <td>
  <button
    class="action-btn"
    @click="viewDetail(hoaDon.id)"
  >
    <i class="fa-solid fa-eye"></i>
  </button>

  <button
    class="action-btn"
    @click="printHoaDon(hoaDon.id)"
  >
    <i class="fa-solid fa-print"></i>
  </button>
</td>
    
   </tr>

  </tbody>

</table>

<div class="pagination">

  <span>
    Hiển thị 1 / 4 bản ghi
  </span>

  <div class="page-center">

    <button>
      <i class="fa-solid fa-chevron-left"></i>
    </button>

    <span>Trang 1</span>

    <button>
      <i class="fa-solid fa-chevron-right"></i>
    </button>

  </div>

  <select>

    <option>10 bản ghi / trang</option>

  </select>

</div>

</div>

  </MainLayout>
</template>
<script setup>
import MainLayout from '../layouts/MainLayout.vue'
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'

import {
  fetchAllHoaDon,
  searchHoaDon
} from '@/service/HoaDonService.js'

const router = useRouter()

// =======================
// DATA
// =======================

const listHoaDon = ref([])

const keyword = ref('')

const loaiHoaDonFilter = ref('')

const tuNgay = ref('')
const denNgay = ref('')

// =======================
// LOAD DATA
// =======================

const handleFetchAllData = async () => {
  try {
    listHoaDon.value = await fetchAllHoaDon()
  } catch (error) {
    console.log(error)
  }
}

// =======================
// SEARCH
// =======================

const handleSearch = async () => {
  try {
    if (!keyword.value.trim()) {
      await handleFetchAllData()
      return
    }

    listHoaDon.value = await searchHoaDon(
      keyword.value
    )
  } catch (error) {
    console.log(error)
  }
}

// tìm kiếm realtime
watch(keyword, async (newValue) => {
  try {
    if (!newValue.trim()) {
      await handleFetchAllData()
      return
    }

    listHoaDon.value = await searchHoaDon(newValue)
  } catch (error) {
    console.log(error)
  }
})

// =======================
// FILTER
// =======================

const filteredHoaDon = computed(() => {
  return listHoaDon.value.filter((hd) => {
    // lọc loại hóa đơn
    const matchLoaiHoaDon =
      !loaiHoaDonFilter.value ||
      hd.loaiHoaDon === loaiHoaDonFilter.value

    // xử lý ngày
    const ngayHoaDon = hd.ngayTao
      ? hd.ngayTao.substring(0, 10)
      : ''

    const matchTuNgay =
      !tuNgay.value ||
      ngayHoaDon >= tuNgay.value

    const matchDenNgay =
      !denNgay.value ||
      ngayHoaDon <= denNgay.value

    return (
      matchLoaiHoaDon &&
      matchTuNgay &&
      matchDenNgay
    )
  })
})

// =======================
// FORMAT
// =======================

const formatDate = (date) => {
  if (!date) return ''

  return new Date(date)
    .toLocaleDateString('vi-VN')
}

const formatCurrency = (value) => {
  if (!value) return '0 đ'

  return (
    Number(value).toLocaleString('vi-VN') +
    ' đ'
  )
}

// =======================
// STATUS
// =======================

const getTrangThaiText = (status) => {
  switch (status) {
    case 0:
      return 'Đã hủy'

    case 1:
      return 'Đã thanh toán'

    case 2:
      return 'Chờ xác nhận'

    case 3:
      return 'Đang giao'

    case 4:
      return 'Hoàn thành'

    default:
      return 'Không xác định'
  }
}

const getStatusClass = (status) => {
  switch (status) {
    case 0:
      return 'status-cancel'

    case 1:
      return 'status-paid'

    default:
      return ''
  }
}

const getLoaiHoaDonClass = (loai) => {
  switch (loai) {
    case 'Tại quầy':
      return 'invoice-offline'

    case 'Online':
      return 'invoice-online'

    default:
      return ''
  }
}


const viewDetail = (id) => {
  router.push(`/hoa-don/${id}`)
}

onMounted(async () => {
  await handleFetchAllData()

  // mặc định ngày hôm nay
  const today =
    new Date().toISOString().split('T')[0]

  tuNgay.value = today
  denNgay.value = today
})
const resetFilter = async () => {
  // Xóa tìm kiếm
  keyword.value = ''

  // Loại hóa đơn về tất cả
  loaiHoaDonFilter.value = ''

  // Ngày về hôm nay
  const today = new Date()
    .toISOString()
    .split('T')[0]

  tuNgay.value = today
  denNgay.value = today

  // Load lại dữ liệu
  await handleFetchAllData()
}
</script>

<style scoped>
  .invoice-badge{
  display:inline-block;
  padding:4px 12px;
  border-radius:999px;
  font-size:11px;
  font-weight:600;
}

.invoice-offline{
  background:#dbeafe;
  color:#1d4ed8;
}

.invoice-online{
  background:#f3e8ff;
  color:#7e22ce;
}
  .status-badge{
  display:inline-block;
  padding:4px 12px;
  border-radius:999px;
  font-size:11px;
  font-weight:600;
}

.status-paid{
  background:#dcfce7;
  color:#15803d;
}

.status-cancel{
  background:#fee2e2;
  color:#dc2626;
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

.invoice-type{
  background:#f4f4f4;
  padding:4px 10px;
  border-radius:20px;
  font-size:11px;
}

.status-success{
  background:#28a745;
  color:white;
  padding:4px 10px;
  border-radius:20px;
  font-size:11px;
}

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
  margin-top:16px;
  display:flex;
  justify-content:space-between;
  align-items:center;
  font-size:12px;
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

</style>