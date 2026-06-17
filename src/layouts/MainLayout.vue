<template>
  <div class="layout">
    <div class="sidebar">
      <div class="logo">
        <img
          src="../assets/logo/Logo_Da.jpg"
          alt="logo"
          class="logo-full"
        />
      </div>

      <div class="menu-list">
        <router-link to="/trang-chu" class="item">
          <i class="fa-solid fa-house"></i>
          <span>Trang chủ</span>
        </router-link>

        <router-link to="/thong-ke" class="item">
          <i class="fa-solid fa-chart-line"></i>
          <span>Thống kê</span>
        </router-link>

        <router-link to="/ban-hang" class="item">
          <i class="fa-solid fa-cart-shopping"></i>
          <span>Bán hàng</span>
        </router-link>

        <router-link to="/hoa-don" class="item">
          <i class="fa-solid fa-receipt"></i>
          <span>Hóa đơn</span>
        </router-link>

        <div class="dropdown-menu-group">
          <div :class="['item-toggle-header', { 'active-parent': isSanPhamOpen }]" @click="isSanPhamOpen = !isSanPhamOpen">
            <i class="fa-solid fa-box-open"></i>
            <span>Sản phẩm</span>
            <i :class="['fa-solid', isSanPhamOpen ? 'fa-chevron-down' : 'fa-chevron-right', 'arrow-nested']"></i>
          </div>
          <div class="submenu-container" v-if="isSanPhamOpen">
            <router-link to="/san-pham" class="sub-item">Danh sách sản phẩm</router-link>
            <router-link to="/san-pham/danh-sach-bien-the" class="sub-item">Danh sách biến thể tổng</router-link>
          </div>
        </div>

        <div class="dropdown-menu-group">
          <div :class="['item-toggle-header', { 'active-parent': isThuocTinhOpen }]" @click="isThuocTinhOpen = !isThuocTinhOpen">
            <i class="fa-solid fa-list-check"></i>
            <span>Thuộc tính</span>
            <i :class="['fa-solid', isThuocTinhOpen ? 'fa-chevron-down' : 'fa-chevron-right', 'arrow-nested']"></i>
          </div>
          <div class="submenu-container" v-if="isThuocTinhOpen">
            <router-link to="/thuoc-tinh/mau-sac" class="sub-item">Màu sắc</router-link>
            <router-link to="/thuoc-tinh/trong-luong" class="sub-item">Trọng lượng</router-link>
            <router-link to="/thuoc-tinh/do-cung" class="sub-item">Độ cứng thân</router-link>
            <router-link to="/thuoc-tinh/diem-can-bang" class="sub-item">Điểm cân bằng</router-link>
            <router-link to="/thuoc-tinh/chu-vi-can" class="sub-item">Chu vi cán</router-link>
            <router-link to="/thuoc-tinh/chat-lieu-than" class="sub-item">Chất liệu thân</router-link>
            <router-link to="/thuoc-tinh/chat-lieu-khung" class="sub-item">Chất liệu khung</router-link>
          </div>
        </div>

        <router-link to="/phieu-giam-gia" class="item">
          <i class="fa-solid fa-ticket"></i>
          <span>Phiếu giảm giá</span>
        </router-link>

        <router-link to="/dot-giam-gia" class="item">
          <i class="fa-solid fa-percent"></i>
          <span>Đợt giảm giá</span>
        </router-link>

        <router-link to="/nhan-vien" class="item">
          <i class="fa-solid fa-user-tie"></i>
          <span>Nhân viên</span>
        </router-link>

        <router-link to="/khach-hang" class="item">
          <i class="fa-solid fa-users"></i>
          <span>Khách hàng</span>
        </router-link>
      </div>

      <div class="bottom-menu">
        <div class="bottom-item logout">
          <i class="fa-solid fa-right-from-bracket"></i>
          <span>Đăng xuất</span>
        </div>
      </div>
    </div>

    <div class="main">
      <div class="header">
        <div class="page-title">
          <h2>{{ title }}</h2>
        </div>

        <div class="user-wrapper">
          <div class="user" @click="showMenu = !showMenu">
            <div class="avatar">
              <i class="fa-solid fa-user"></i>
            </div>
            <div class="user-info">
              <h4>Admin</h4>
              <p>Quản trị viên</p>
            </div>
            <i class="fa-solid fa-chevron-down arrow"></i>
          </div>

          <div v-if="showMenu" class="dropdown">
            <div class="dropdown-item">
              <i class="fa-solid fa-user"></i>
              <span>Thông tin</span>
            </div>
            <div class="dropdown-item">
              <i class="fa-solid fa-key"></i>
              <span>Đổi mật khẩu</span>
            </div>
            <div class="dropdown-item logout-item">
              <i class="fa-solid fa-right-from-bracket"></i>
              <span>Đăng xuất</span>
            </div>
          </div>
        </div>
      </div>

      <div class="content">
        <slot />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  title: String
})

const showMenu = ref(false)

// Khai báo các trạng thái đóng mở menu phụ lồng nhau
const isSanPhamOpen = ref(false)
const isThuocTinhOpen = ref(false)
</script>

<style scoped>
*{
  box-sizing:border-box;
  margin:0;
  padding:0;
}

.layout{
  display:flex;
  min-height:100vh;
  background:#f5f5f5;
  font-family:Arial, Helvetica, sans-serif;
}

.sidebar{
  width:220px;
  background:white;
  padding:20px 14px;
  border-right:1px solid #e5e5e5;
  display:flex;
  flex-direction:column;
}

.logo{
  display:flex;
  align-items:center;
  gap:14px;
  margin-bottom:35px;
  padding:0 10px;
}

.logo img {
  width: 100%;
  height: auto;
  object-fit: contain;
}

.menu-list{
  display:flex;
  flex-direction:column;
  gap:6px;
}

/* Định dạng CSS item chung cho cả liên kết thẳng và nút bấm dropdown cha */
.item, .item-toggle-header {
  display:flex;
  align-items:center;
  gap:12px;
  padding:10px 14px;
  border-radius:12px;
  text-decoration:none;
  color:#4b5563;
  transition:0.2s;
  font-size:13px;
  font-weight:500;
  cursor: pointer;
  user-select: none;
}

.item i, .item-toggle-header i {
  width:20px;
  font-size:15px;
}

.item:hover, .item-toggle-header:hover {
  background:#fff8f3;
}

/* Đánh dấu màu khi kích hoạt router link chính xác */
.item.router-link-active {
  background:#fff4ed;
  color:#d87d4a;
}

/* Kiểu định dạng bổ sung cho nút cha khi menu đang mở rộng */
.active-parent {
  color: #d87d4a;
  background: #fffbf7;
}

.arrow-nested {
  margin-left: auto;
  font-size: 11px !important;
  color: #9ca3af;
}

/* Khối hộp submenu bọc danh sách liên kết con thụt lề */
.submenu-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding-left: 20px;
  margin: 4px 0;
  border-left: 1.5px dashed #fed7aa;
  margin-left: 23px;
}

.sub-item {
  display: block;
  padding: 8px 12px;
  font-size: 12.5px;
  color: #6b7280;
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.2s;
  text-align: left;
}

.sub-item:hover {
  background-color: #fff8f3;
  color: #d87d4a;
}

/* Giữ trạng thái sáng màu cam khi đang đứng ở trang con */
.sub-item.router-link-active {
  background-color: #fff4ed;
  color: #d87d4a;
  font-weight: 600;
}

.bottom-menu{
  margin-top:auto;
  border-top:1px solid #eee;
  padding-top:15px;
}

.bottom-item{
  display:flex;
  align-items:center;
  gap:14px;
  padding:14px;
  border-radius:12px;
  cursor:pointer;
  transition:0.2s;
  color:#4b5563;
}

.bottom-item:hover{
  background:#f3f4f6;
}

.bottom-item i{
  width:20px;
}

.logout{
  color:red;
}

.main{
  flex:1;
  display:flex;
  flex-direction:column;
}

.header{
  height:75px;
  background:white;
  border-bottom:1px solid #e5e5e5;
  display:flex;
  align-items:center;
  justify-content:space-between;
  padding:0 25px;
}

.page-title h2{
  font-size:24px;
  color:#333;
}

.user-wrapper{
  position:relative;
}

.user{
  display:flex;
  align-items:center;
  gap:12px;
  cursor:pointer;
  padding:8px 12px;
  border-radius:12px;
  transition:0.2s;
}

.user:hover{
  background:#f5f5f5;
}

.avatar{
  width:42px;
  height:42px;
  border-radius:50%;
  background:#e6925e;
  color:white;
  display:flex;
  align-items:center;
  justify-content:center;
}

.user-info h4{
  font-size:14px;
}

.user-info p{
  color:#888;
  font-size:12px;
}

.arrow{
  font-size:12px;
  color:#777;
}

.dropdown{
  position:absolute;
  top:65px;
  right:0;
  width:210px;
  background:white;
  border-radius:14px;
  box-shadow:0 4px 20px rgba(0,0,0,0.1);
  overflow:hidden;
  z-index:999;
}

.dropdown-item{
  display:flex;
  align-items:center;
  gap:12px;
  padding:15px 16px;
  cursor:pointer;
  transition:0.2s;
}

.dropdown-item:hover{
  background:#f5f5f5;
}

.dropdown-item i{
  width:18px;
}

.logout-item{
  color:red;
}

.content{
  flex:1;
  padding:20px;
}
</style>