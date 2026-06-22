<template>
<div class="menu-list">

  <router-link to="/" class="item">
    <i class="fa-solid fa-store"></i> <span>Giao diện bán hàng</span>
  </router-link>

  <router-link to="/trang-chu" class="item">
    <i class="fa-solid fa-house"></i> <span>Trang chủ</span>
  </router-link>

  <router-link to="/thong-ke" class="item">
    <i class="fa-solid fa-chart-column"></i> <span>Thống kê</span>
  </router-link>

  <router-link to="/ban-hang" class="item">
    <i class="fa-solid fa-cart-shopping"></i> <span>Bán hàng</span>
  </router-link>

  <router-link to="/hoa-don" class="item">
    <i class="fa-solid fa-file-invoice"></i> <span>Hóa đơn</span>
  </router-link>

  <div class="dropdown-menu">
    <div class="item" @click="isSanPhamOpen = !isSanPhamOpen">
      <i class="fa-solid fa-box"></i>
      <span>Sản phẩm</span>
      <i :class="['fa-solid', isSanPhamOpen ? 'fa-chevron-down' : 'fa-chevron-right']" style="margin-left: auto; font-size: 10px;"></i>
    </div>
    <div class="submenu" v-if="isSanPhamOpen">
      <router-link to="/san-pham" class="sub-item">Danh sách sản phẩm</router-link>
      <router-link to="/san-pham/danh-sach-bien-the" class="sub-item">Danh sách biến thể</router-link>
    </div>
  </div>

  <div class="dropdown-menu">
    <div class="item" @click="isThuocTinhOpen = !isThuocTinhOpen">
      <i class="fa-solid fa-layer-group"></i>
      <span>Thuộc tính</span>
      <i :class="['fa-solid', isThuocTinhOpen ? 'fa-chevron-down' : 'fa-chevron-right']" style="margin-left: auto; font-size: 10px;"></i>
    </div>
    <div class="submenu" v-if="isThuocTinhOpen">
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
    <i class="fa-solid fa-ticket"></i> <span>Phiếu giảm giá</span>
  </router-link>
  
  <router-link to="/nhan-vien" class="item">
    <i class="fa-solid fa-user-tie"></i> <span>Nhân viên</span>
  </router-link>
</div>
</template>
<script setup>
import { ref } from 'vue';

// Các biến trạng thái để đóng/mở menu
const isSanPhamOpen = ref(false);
const isThuocTinhOpen = ref(false);
</script>
