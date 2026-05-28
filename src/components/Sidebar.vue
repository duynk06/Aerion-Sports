<template>
<!-- SIDEBAR -->
<div class="sidebar">

<!-- LOGO -->
<div class="logo">

  <div class="logo-circle">
    A
  </div>

  <div class="logo-text">
    <h2>AERION</h2>
    <p>SPORTS</p>
  </div>

</div>

<!-- MENU -->
<div class="menu-list">

  <!-- TRANG CHỦ -->
  <router-link to="/trang-chu" class="item">

    <i class="fa-solid fa-house"></i>

    <span>Trang chủ</span>

  </router-link>

  <!-- BÁN HÀNG -->
  <router-link to="/" class="item">

    <i class="fa-solid fa-cart-shopping"></i>

    <span>Bán hàng</span>

  </router-link>

  <!-- HÓA ĐƠN -->
  <router-link to="/hoa-don" class="item">

    <i class="fa-solid fa-file-invoice"></i>

    <span>Hóa đơn</span>

  </router-link>

  <!-- SẢN PHẨM -->
  <router-link to="/san-pham" class="item">

    <i class="fa-solid fa-box"></i>

    <span>Sản phẩm</span>

  </router-link>

  <!-- PHIẾU GIẢM GIÁ -->
  <router-link to="/phieu-giam-gia" class="item">

    <i class="fa-solid fa-ticket"></i>

    <span>Phiếu giảm giá</span>

  </router-link>

  <!-- ĐỢT GIẢM GIÁ -->
  <router-link to="/dot-giam-gia" class="item">

    <i class="fa-solid fa-tags"></i>

    <span>Đợt giảm giá</span>

  </router-link>

  <!-- NHÂN VIÊN -->
  <router-link to="/nhan-vien" class="item">

    <i class="fa-solid fa-user-tie"></i>

    <span>Nhân viên</span>

  </router-link>

  <!-- KHÁCH HÀNG -->
  <router-link to="/khach-hang" class="item">

    <i class="fa-solid fa-users"></i>

    <span>Khách hàng</span>

  </router-link>

  <!-- THỐNG KÊ -->
  <router-link to="/thong-ke" class="item">

    <i class="fa-solid fa-chart-column"></i>

    <span>Thống kê</span>

  </router-link>

</div>

<!-- BOTTOM -->
<div class="bottom-menu">

  <!-- CÀI ĐẶT -->
  <div class="bottom-item">

    <i class="fa-solid fa-gear"></i>

    <span>Cài đặt</span>

  </div>

  <!-- ĐĂNG XUẤT -->
  <div class="bottom-item logout">

    <i class="fa-solid fa-right-from-bracket"></i>

    <span>Đăng xuất</span>

  </div>

</div>

</div>
</template>