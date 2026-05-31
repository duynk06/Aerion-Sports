<script setup>
import MainLayout from '@/layouts/MainLayout.vue'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

const showHistoryModal = ref(false)
const router = useRouter()

const goBack = () => {
  router.push('/hoa-don')
  // hoặc router.back()
}
</script>
<template>
  <MainLayout title="Chi tiết hóa đơn">

    <!-- Header -->
    <div class="page-header">
      <button class="back-btn" @click="goBack">
        <i class="fa-solid fa-arrow-left"></i>
      </button>

      <p>Chi tiết hóa đơn</p>
    </div>

    <!-- HÀNG 1 -->
    <!-- HÀNG 1 -->
    <div class="card">

<div class="card-title">
  <i class="fa-solid fa-truck-fast"></i>
  <span>Trạng thái đơn hàng</span>
</div>

<div class="order-steps">
  ...
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
    <span>0 đ</span>
  </div>

  <div class="summary-item">
    <span>Giảm giá</span>
    <span>0 đ</span>
  </div>

  <div class="summary-item">
    <span>Phí vận chuyển</span>
    <span>0 đ</span>
  </div>

  <hr>

  <div class="summary-total">
    <span>Tổng thanh toán</span>
    <span>0 đ</span>
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
    <span></span>
  </div>

  <div class="info-row">
    <label>SĐT:</label>
    <span></span>
  </div>

  <div class="info-row">
    <label>Email:</label>
    <span></span>
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
    <span></span>
  </div>

  <div class="info-row">
    <label>Loại đơn:</label>
    <span></span>
  </div>

  <div class="info-row">
    <label>Ghi chú:</label>
    <span></span>
  </div>

</div>

</div>

<!-- HÀNG 3 -->
<div class="card">

<div class="card-title">
  <i class="fa-solid fa-clock-rotate-left"></i>
  <span>Lịch sử thanh toán</span>
</div>

<div class="history-item">
  <div class="history-title">
    Thanh toán thành công
  </div>

  <div class="history-time">
    20/06/2026 14:30
  </div>
</div>

</div>

    <!-- HÀNG 3 -->
    <div class="card">

      <div class="card-title">
        <i class="fa-solid fa-cart-shopping"></i>
        <span>Danh sách sản phẩm</span>
      </div>

      <table>

        <thead>
          <tr>
            <th>STT</th>
            <th>Mã sản phẩm</th>
            <th>Tên sản phẩm</th>
            <th>Size</th>
            <th>Màu sắc</th>
            <th>Số lượng</th>
            <th>Đơn giá</th>
            <th>Thành tiền</th>
          </tr>
        </thead>

        <tbody>

          <tr>
          
          </tr>

        </tbody>

      </table>

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
    type="text"
    placeholder="Nhập tên người thao tác"
  >
</div>

<div class="filter-group">
  <label>Hành động</label>
  <select>
    <option value="">Tất cả</option>
    <option value="0">Chờ xác nhận</option>
    <option value="1">Đã xác nhận</option>
    <option value="2">Chờ giao hàng</option>
    <option value="3">Đang giao hàng</option>
    <option value="4">Đã giao hàng</option>
    <option value="5">Đã hoàn thành</option>
  </select>
</div>

<button class="search-history-btn">
  <i class="fa-solid fa-magnifying-glass"></i>
</button>

</div>

<table class="history-table">

<thead>
  <tr>
    <th>STT</th>
    <th>Người thao tác</th>
    <th>Hành động</th>
    <th>Ghi chú</th>
    <th>Thời gian</th>
  </tr>
</thead>

<tbody>

  <!-- Để trống -->

  <!--
  <tr>
    <td colspan="5" class="empty-data">
      Chưa có dữ liệu
    </td>
  </tr>
  -->

</tbody>

</table>

  </div>

</div>
</template>

<style scoped>
.card-title-wrapper{
  display:flex;
  justify-content:space-between;
  align-items:center;
}

.history-btn{
  border:none;
  background:#f79b66;
  color:white;
  padding:8px 14px;
  border-radius:8px;
  cursor:pointer;
  display:flex;
  align-items:center;
  gap:6px;
  font-size:13px;
}

.history-btn:hover{
  opacity:.9;
}

/* Overlay */

.modal-overlay{
  position:fixed;
  inset:0;
  background:rgba(0,0,0,.4);
  display:flex;
  justify-content:center;
  align-items:center;
  z-index:9999;
}

/* Popup */

.history-modal{
  width:500px;
  max-width:90%;
  background:white;
  border-radius:12px;
  overflow:hidden;
  box-shadow:0 10px 30px rgba(0,0,0,.2);
}

.modal-header{
  display:flex;
  justify-content:space-between;
  align-items:center;
  padding:16px 20px;
  border-bottom:1px solid #eee;
}

.modal-header h3{
  margin:0;
  display:flex;
  align-items:center;
  gap:8px;
}

.close-btn{
  border:none;
  background:none;
  cursor:pointer;
  font-size:18px;
}

.modal-body{
  padding:20px;
}

.history-item{
  display:flex;
  gap:12px;
  padding:12px 0;
  border-bottom:1px solid #eee;
}

.history-item:last-child{
  border-bottom:none;
}

.history-item i{
  color:#f79b66;
  margin-top:3px;
}

.history-item p{
  margin:4px 0 0;
  color:#888;
  font-size:12px;
}

/* =========================
HEADER
========================= */

.page-header{
display:flex;
align-items:center;
gap:12px;
margin-bottom:20px;
}

.back-btn{
width:36px;
height:36px;
border:none;
border-radius:8px;
background:#f79b66;
color:white;
cursor:pointer;
display:flex;
align-items:center;
justify-content:center;
}

.page-title{
font-size:20px;
font-weight:600;
color:#333;
}

/* =========================
CARD CHUNG
========================= */
.history-filter{
  display:flex;
  gap:16px;
  align-items:flex-end;
  margin-bottom:20px;
}

.filter-group{
  flex:1;
}

.filter-group label{
  display:block;
  margin-bottom:6px;
  font-size:13px;
  font-weight:600;
  color:#333;
}

.filter-group input,
.filter-group select{
  width:100%;
  height:40px;
  border:1px solid #ddd;
  border-radius:8px;
  padding:0 12px;
  outline:none;
}

.filter-group input:focus,
.filter-group select:focus{
  border-color:#f79b66;
}

.search-history-btn{
  width:40px;
  height:40px;
  border:none;
  border-radius:8px;
  background:#f79b66;
  color:white;
  cursor:pointer;
}

.history-table{
  width:100%;
  border-collapse:collapse;
}

.history-table th{
  background:#f79b66;
  color:white;
  padding:12px;
  text-align:center;
}

.history-table td{
  padding:12px;
  text-align:center;
  border-bottom:1px solid #eee;
}

.empty-data{
  text-align:center;
  color:#999;
  padding:20px;
}
.card{
background:#fff;
border:1px solid #e5e7eb;
border-radius:12px;
padding:20px;
margin-bottom:20px;
box-shadow:0 2px 6px rgba(0,0,0,.04);
}

.card-title{
display:flex;
align-items:center;
gap:8px;
font-size:15px;
font-weight:600;
margin-bottom:18px;
color:#333;
}

.card-title i{
color:#f79b66;
}

/* =========================
HÀNG 1
========================= */

.top-row{
display:grid;
grid-template-columns: 2fr 1fr;
gap:20px;
}

/* =========================
TIMELINE
========================= */

.order-steps{
display:flex;
justify-content:space-between;
align-items:flex-start;
margin-top:25px;
}

.step{
flex:1;
text-align:center;
position:relative;
}

.step:not(:last-child)::after{
content:'';
position:absolute;
top:18px;
left:55%;
width:90%;
height:2px;
background:#d6b16d;
}

.step-icon{
width:36px;
height:36px;
border-radius:50%;
background:#c79b47;
color:white;
margin:auto;
display:flex;
align-items:center;
justify-content:center;
z-index:2;
position:relative;
}

.step-label{
margin-top:8px;
font-size:13px;
font-weight:500;
}

.step-date{
font-size:12px;
color:#888;
}

/* =========================
THANH TOÁN
========================= */

.summary-item{
display:flex;
justify-content:space-between;
margin-bottom:12px;
font-size:14px;
}

.summary-item.total{
margin-top:15px;
padding-top:15px;
border-top:1px dashed #ddd;
font-size:18px;
font-weight:700;
color:#111;
}

/* =========================
HÀNG 2
========================= */

.middle-row{
display:grid;
grid-template-columns:1fr 1fr 1fr;
gap:20px;
}

/* =========================
THÔNG TIN
========================= */

.info-item{
display:flex;
justify-content:space-between;
margin-bottom:12px;
font-size:14px;
}

.info-label{
color:#777;
}

.info-value{
font-weight:500;
text-align:right;
}

/* =========================
LỊCH SỬ
========================= */

.history-item{
padding:10px 0;
border-bottom:1px solid #eee;
}

.history-item:last-child{
border-bottom:none;
}

.history-title{
font-size:14px;
font-weight:500;
}

.history-time{
font-size:12px;
color:#888;
margin-top:4px;
}

/* =========================
BẢNG SẢN PHẨM
========================= */

.product-table{
width:100%;
border-collapse:collapse;
}

.product-table thead{
background:#f79b66;
color:white;
}

.product-table th,
.product-table td{
padding:12px;
border-bottom:1px solid #eee;
text-align:center;
font-size:13px;
}

.product-table tbody tr:hover{
background:#fafafa;
}

.money{
color:#dc2626;
font-weight:600;
}

/* =========================
RESPONSIVE
========================= */

@media(max-width:1200px){

.top-row{
 grid-template-columns:1fr;
}

.middle-row{
 grid-template-columns:1fr;
}

.order-steps{
 flex-wrap:wrap;
 gap:20px;
}

.step:not(:last-child)::after{
 display:none;
}
}

th{
  background:#f79b66;
  color:white;
}

th,
td{
  padding:12px;
  text-align:center;
  border-bottom:1px solid #eee;
}
.history-action{
  display:flex;
  justify-content:flex-end;
  margin-top:20px;
}

.history-btn{
  border:none;
  background:#f79b66;
  color:white;
  padding:10px 16px;
  border-radius:8px;
  cursor:pointer;
  display:flex;
  align-items:center;
  gap:8px;
  font-size:13px;
  font-weight:500;
}

.history-btn:hover{
  opacity:.9;
}
</style>