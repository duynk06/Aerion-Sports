<script setup>
import MainLayout from '@/layouts/MainLayout.vue'
import { useRouter } from 'vue-router'

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
    <div class="top-row">

      <!-- Trạng thái đơn hàng -->
      <div class="card order-status-card">

        <div class="card-title">
          <i class="fa-solid fa-truck-fast"></i>
          <span>Trạng thái đơn hàng</span>
        </div>

      </div>

      <!-- Tổng kết thanh toán -->
      <div class="card payment-summary">

        <div class="card-title">
          <i class="fa-solid fa-money-bill-wave"></i>
          <span>Tổng kết thanh toán</span>
        </div>

        <div class="summary-item">
          <span>Tổng tiền hàng: </span>
          
        </div>

        <div class="summary-item">
          <span>Giảm giá: </span>
        </div>

        <div class="summary-item">
          <span>Phí vận chuyển: </span>
        </div>

        <hr>
        <br>

        <div class="summary-total">
          <span>Tổng tiền: </span>
        </div>

      </div>

    </div>

    <!-- HÀNG 2 -->
    <div class="middle-row">

      <!-- Khách hàng -->
      <div class="card info-card">

        <div class="card-title">
          <i class="fa-solid fa-user"></i>
          <span>Thông tin khách hàng</span>
        </div>

        <div class="info-row">
          <label>Họ tên:</label>
         
        </div>

        <div class="info-row">
          <label>SĐT: </label>
        </div>

        <div class="info-row">
          <label>Email: </label>
        </div>

      </div>

      <!-- Giao hàng -->
      <div class="card info-card">

        <div class="card-title">
          <i class="fa-solid fa-location-dot"></i>
          <span>Thông tin giao hàng</span>
        </div>

        <div class="info-row">
          <label>Địa chỉ: </label>
        </div>

        <div class="info-row">
          <label>Loại đơn: </label>
        </div>

        <div class="info-row">
          <label>Ghi chú: </label>
        </div>

      </div>

      <!-- Thanh toán -->
      <div class="card info-card">

        <div class="card-title">
          <i class="fa-solid fa-clock-rotate-left"></i>
          <span>Lịch sử thanh toán</span>
        </div>

        <div class="payment-history">

          <div class="history-item">
            <div>
              <strong>Chuyển khoản</strong>
            </div>

         
          </div>

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
</template>

<style scoped>/* =========================
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
</style>