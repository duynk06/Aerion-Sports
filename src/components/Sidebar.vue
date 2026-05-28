<template>
  <div class="sidebar">

    <div class="logo">
      <img src="../assets/logo.png" />
    </div>

    <router-link to="/" class="menu-item">
      <i class="fas fa-store"></i>
      Bán hàng
    </router-link>

    <router-link to="/hoa-don" class="menu-item">
      <i class="fas fa-file-invoice"></i>
      Hóa đơn
    </router-link>

    <router-link to="/san-pham" class="menu-item">
      <i class="fas fa-box"></i>
      Sản phẩm
    </router-link>

    <router-link to="/phieu-giam-gia" class="menu-item">
      <i class="fas fa-ticket-alt"></i>
      Phiếu giảm giá
    </router-link>

    <router-link to="/dot-giam-gia" class="menu-item">
      <i class="fas fa-percent"></i>
      Đợt giảm giá
    </router-link>

    <router-link to="/nhan-vien" class="menu-item">
      <i class="fas fa-user"></i>
      Nhân viên
    </router-link>

    <router-link to="/khach-hang" class="menu-item">
      <i class="fas fa-users"></i>
      Khách hàng
    </router-link>

  </div>
</template>