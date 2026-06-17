<template>
  <MainLayout title="Quản lý hóa đơn">

  
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
 
/>
        </div>

        <div>
  <label>Loại hóa đơn</label>
  <select v-model="loaiHoaDonFilter">
    <option :value="null">Tất cả</option>
    <option :value="0">Tại quầy</option>
    <option :value="1">Online</option>
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
<button
  :class="{ active: trangThaiFilter === 7 }"
  @click="trangThaiFilter = 7"
>
  Yêu cầu huỷ
</button>
<button
  :class="{ active: trangThaiFilter === 8 }"
  @click="trangThaiFilter = 8"
>
  Đã hoàn tiền
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

    <tr v-for="(hoaDon, index) in listHoaDon" :key="hoaDon.id">
      <td>{{ (page * size) + index + 1 }}</td>
    <td>{{ hoaDon.maHoaDon }}</td>
    <td>{{ hoaDon.tenNv }}</td>
    <td>{{ hoaDon.tenNguoiNhan }}</td>
    <td>{{ hoaDon.sdtNguoiNhan }}</td>
    <td>
  <span
    class="invoice-badge"
    :class="getLoaiHoaDonClass(hoaDon.loaiHoaDon)"
  >
    {{ hoaDon.loaiHoaDon === 1 ? 'Online' : 'Tại quầy' }}
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
<div class="table-footer">

<div class="table-info">
  Tổng:
  <strong>{{ totalInvoices }}</strong>
  hóa đơn
</div>

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
import { computed } from 'vue'
import { getHoaDonById } from '@/service/HoaDonService'
import { getChiTietHoaDon } from '@/service/ChiTietHoaDonDetailService'
import { printInvoice } from '@/utils/PrintHoaDon'
import { getLichSuThanhToan } from '@/service/LichSuThanhToanService'
const router = useRouter()



const listHoaDon = ref([])

const keyword = ref('')
const loaiHoaDonFilter = ref(null)
const trangThaiFilter = ref('')

const tuNgay = ref('')
const denNgay = ref('')

const page = ref(0)
const size = ref(5)
const totalPages = ref(0)

const printHoaDon = async (id) => {
  try {

    const hoaDon =
      await getHoaDonById(id)

    const danhSachSanPham =
      await getChiTietHoaDon(id)

    const lichSuThanhToan =
      await getLichSuThanhToan(id)

    await printInvoice(
      hoaDon,
      danhSachSanPham,
      lichSuThanhToan
    )

  } catch (error) {

    console.error(
      'Lỗi in hóa đơn:',
      error
    )

  }
}
const totalInvoices = computed(() => {
  return listHoaDon.value.length
})
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
    'Người nhận': hd.tenNguoiNhan,
    'Số điện thoại': hd.sdtNguoiNhan,
    // Sửa logic ở đây
    'Loại hóa đơn': hd.loaiHoaDon === 1 ? 'Online' : 'Tại quầy', 
    'Tổng tiền': formatCurrency(hd.tongTienThanhToan),
    'Ngày tạo': formatDate(hd.ngayTao),
    'Trạng thái': getTrangThaiText(hd.trangThai)
  }));
  // ... phần còn lại giữ nguyên


const worksheet = XLSX.utils.aoa_to_sheet([
  ['DANH SÁCH HÓA ĐƠN'],
  [
    'Ngày xuất: ' +
    new Date().toLocaleString('vi-VN')
  ],
  []
])

XLSX.utils.sheet_add_json(
  worksheet,
  data,
  {
    origin: 'A4',
    skipHeader: false
  }
)

// Gộp ô tiêu đề
worksheet['!merges'] = [
  {
    s: { r: 0, c: 0 },
    e: { r: 0, c: 8 }
  },
  {
    s: { r: 1, c: 0 },
    e: { r: 1, c: 8 }
  }
]

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


watch(
  [
  keyword,
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


const getTrangThaiText = (status) => {
  switch (status) {
    case 0: return 'Chờ xác nhận';
    case 1: return 'Đã xác nhận';
    case 2: return 'Chờ giao hàng';
    case 3: return 'Đang giao hàng';
    case 4: return 'Đã giao hàng';
    case 5: return 'Đã hoàn thành';
    case 6: return 'Đã hủy';
    case 7: return 'Yêu cầu hủy';
    case 8: return 'Đã hoàn tiền';
  }
}

const getStatusClass = (status) => {
  switch (status) {
    case 0: return 'status-wait-confirm';
    case 1: return 'status-confirmed';
    case 2: return 'status-wait-delivery';
    case 3: return 'status-delivering';
    case 4: return 'status-delivered';
    case 5: return 'status-completed';
    case 6: return 'status-cancel';
    case 7: return 'status-request-cancel'; // Bạn cần thêm style cho class này trong CSS
    case 8: return 'status-refunded';       // Thêm style cho class này
    default: return 'status-badge';
  }
}


const getLoaiHoaDonClass = (loai) => {
  // loai bây giờ là Integer (0 hoặc 1)
  switch (loai) {
    case 0:
      return 'invoice-offline'; // Class cho 'Tại quầy'
    case 1:
      return 'invoice-online';  // Class cho 'Online'
    default:
      return 'invoice-badge';
  }
}


const viewDetail = (id) => {
  router.push(`/hoa-don/${id}`)
}


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
<style scoped src="./HoaDon.css"></style>