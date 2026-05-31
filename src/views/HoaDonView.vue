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

<button class="btn-export"  @click="exportExcel">
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
  <button
  :class="{ active: trangThaiFilter === '' }"
  @click="trangThaiFilter = ''"
>
  Tất cả
</button>

<button
  :class="{ active: trangThaiFilter === 0 }"
  @click="trangThaiFilter = 0"
>
  Chờ xác nhận
</button>

<button
  :class="{ active: trangThaiFilter === 1 }"
  @click="trangThaiFilter = 1"
>
  Đã xác nhận
</button>

<button
  :class="{ active: trangThaiFilter === 2 }"
  @click="trangThaiFilter = 2"
>
  Chờ giao hàng
</button>

<button
  :class="{ active: trangThaiFilter === 3 }"
  @click="trangThaiFilter = 3"
>
  Đang giao hàng
</button>

<button
  :class="{ active: trangThaiFilter === 4 }"
  @click="trangThaiFilter = 4"
>
  Đã giao hàng
</button>

<button
  :class="{ active: trangThaiFilter === 5 }"
  @click="trangThaiFilter = 5"
>
  Đã hoàn thành
</button>

<button
  :class="{ active: trangThaiFilter === 6 }"
  @click="trangThaiFilter = 6"
>
  Đã hủy
</button>
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

    <tr v-for="hoaDon in listHoaDon" :key="hoaDon.id">
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

<div class="page-center">

  <button
    @click="prevPage"
    :disabled="page === 0"
  >
    <i class="fa-solid fa-chevron-left"></i>
  </button>

  <span>
    Trang {{ page + 1 }} / {{ totalPages }}
  </span>

  <button
    @click="nextPage"
    :disabled="page + 1 >= totalPages"
  >
    <i class="fa-solid fa-chevron-right"></i>
  </button>

</div>

</div>

</div>

  </MainLayout>
</template>
<script setup>
import MainLayout from '../layouts/MainLayout.vue'
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { filterHoaDon } from '@/service/HoaDonService'
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'
const router = useRouter()

// =======================
// DATA
// =======================

const listHoaDon = ref([])

const keyword = ref('')
const loaiHoaDonFilter = ref('')
const trangThaiFilter = ref('')

const tuNgay = ref('')
const denNgay = ref('')

const page = ref(0)
const size = ref(5)
const totalPages = ref(0)

// =======================
// LOAD DATA
// =======================

const loadData = async () => {
  try {
    const response = await filterHoaDon(
      keyword.value,
      loaiHoaDonFilter.value,
      trangThaiFilter.value === ''
        ? null
        : trangThaiFilter.value,
      tuNgay.value,
      denNgay.value,
      page.value,
      size.value
    )

    listHoaDon.value = response.content
    totalPages.value = response.totalPages

  } catch (error) {
    console.error(error)
  }
}
const exportExcel = () => {

const data = listHoaDon.value.map((hd, index) => ({
  STT: index + 1,
  'Mã hóa đơn': hd.maHoaDon,
  'Nhân viên': hd.tenNv,
  'Khách hàng': hd.hoTen,
  'Số điện thoại': hd.sdt,
  'Loại hóa đơn': hd.loaiHoaDon,
  'Tổng tiền': hd.tongTienThanhToan,
  'Ngày tạo': formatDate(hd.ngayTao),
  'Trạng thái': getTrangThaiText(hd.trangThai)
}))

const worksheet = XLSX.utils.json_to_sheet(data)

const workbook = XLSX.utils.book_new()

XLSX.utils.book_append_sheet(
  workbook,
  worksheet,
  'HoaDon'
)

const excelBuffer = XLSX.write(
  workbook,
  {
    bookType: 'xlsx',
    type: 'array'
  }
)

const file = new Blob(
  [excelBuffer],
  {
    type:
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  }
)

saveAs(
  file,
  `DanhSachHoaDon_${Date.now()}.xlsx`
)
}
// =======================
// PAGINATION
// =======================

const prevPage = async () => {
  if (page.value > 0) {
    page.value--
    await loadData()
  }
}

const nextPage = async () => {
  if (page.value < totalPages.value - 1) {
    page.value++
    await loadData()
  }
}

// =======================
// SEARCH
// =======================

const handleSearch = async () => {
  page.value = 0
  await loadData()
}

// =======================
// FILTER
// =======================

watch(
  [
    loaiHoaDonFilter,
    trangThaiFilter,
    tuNgay,
    denNgay
  ],
  async () => {
    page.value = 0
    await loadData()
  }
)

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
    Number(value).toLocaleString('vi-VN')
    + ' đ'
  )
}

// =======================
// TRẠNG THÁI
// =======================

const getTrangThaiText = (status) => {
  switch (status) {
    case 0: return 'Chờ xác nhận'
    case 1: return 'Đã xác nhận'
    case 2: return 'Chờ giao hàng'
    case 3: return 'Đang giao hàng'
    case 4: return 'Đã giao hàng'
    case 5: return 'Đã hoàn thành'
    case 6: return 'Đã hủy'
    default: return 'Không xác định'
  }
}

const getStatusClass = (status) => {
  switch (status) {
    case 0: return 'status-wait-confirm'
    case 1: return 'status-confirmed'
    case 2: return 'status-wait-delivery'
    case 3: return 'status-delivering'
    case 4: return 'status-delivered'
    case 5: return 'status-completed'
    case 6: return 'status-cancel'
    default: return ''
  }
}

// =======================
// LOẠI HÓA ĐƠN
// =======================

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

// =======================
// CHI TIẾT
// =======================

const viewDetail = (id) => {
  router.push(`/hoa-don/${id}`)
}

// =======================
// RESET FILTER
// =======================

const resetFilter = async () => {
  keyword.value = ''
  loaiHoaDonFilter.value = ''
  trangThaiFilter.value = ''

  const today =
    new Date()
      .toISOString()
      .split('T')[0]

  tuNgay.value = today
  denNgay.value = today

  page.value = 0

  await loadData()
}

// =======================
// MOUNT
// =======================

onMounted(async () => {
  const today =
    new Date()
      .toISOString()
      .split('T')[0]

  tuNgay.value = today
  denNgay.value = today

  await loadData()
})
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
.status-wait-confirm{
  background:#fef3c7;
  color:#b45309;
}

.status-confirmed{
  background:#dbeafe;
  color:#1d4ed8;
}

.status-wait-delivery{
  background:#ede9fe;
  color:#6d28d9;
}

.status-delivering{
  background:#cffafe;
  color:#0e7490;
}

.status-delivered{
  background:#dcfce7;
  color:#15803d;
}

.status-completed{
  background:#bbf7d0;
  color:#166534;
}

.status-cancel{
  background:#fee2e2;
  color:#dc2626;
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