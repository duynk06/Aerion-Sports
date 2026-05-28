```vue
<template>

  <div class="layout">

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

        <router-link to="/" class="item">

          <i class="fa-solid fa-cart-shopping"></i>

          <span>Bán hàng</span>

        </router-link>

        <router-link to="/hoa-don" class="item">

          <i class="fa-solid fa-file-invoice"></i>

          <span>Hóa đơn</span>

        </router-link>

        <router-link to="/san-pham" class="item">

          <i class="fa-solid fa-box"></i>

          <span>Sản phẩm</span>

        </router-link>

        <router-link to="/phieu-giam-gia" class="item">

          <i class="fa-solid fa-ticket"></i>

          <span>Phiếu giảm giá</span>

        </router-link>

        <router-link to="/dot-giam-gia" class="item">

          <i class="fa-solid fa-tags"></i>

          <span>Đợt giảm giá</span>

        </router-link>

        <router-link to="/nhan-vien" class="item">

          <i class="fa-solid fa-user"></i>

          <span>Nhân viên</span>

        </router-link>

        <router-link to="/khach-hang" class="item">

          <i class="fa-solid fa-users"></i>

          <span>Khách hàng</span>

        </router-link>

      </div>

      <!-- BOTTOM -->
      <div class="bottom-menu">

        <div class="bottom-item">

          <i class="fa-solid fa-gear"></i>

          <span>Cài đặt</span>

        </div>

        <div class="bottom-item logout">

          <i class="fa-solid fa-right-from-bracket"></i>

          <span>Đăng xuất</span>

        </div>

      </div>

    </div>

    <!-- MAIN -->
    <div class="main">

      <!-- HEADER -->
      <div class="header">

        <div class="page-title">

          <h2>{{ title }}</h2>

        </div>

        <!-- USER -->
        <div class="user-wrapper">

          <div
            class="user"
            @click="showMenu = !showMenu"
          >

            <div class="avatar">
              <i class="fa-solid fa-user"></i>
            </div>

            <div class="user-info">

              <h4>Admin</h4>

              <p>Quản trị viên</p>

            </div>

            <i class="fa-solid fa-chevron-down arrow"></i>

          </div>

          <!-- DROPDOWN -->
          <div
            v-if="showMenu"
            class="dropdown"
          >

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

      <!-- CONTENT -->
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

</script>

<style scoped>

*{
  box-sizing:border-box;
}

/* LAYOUT */

.layout{
  display:flex;
  min-height:100vh;
  background:#f5f5f5;
  font-family:Arial, Helvetica, sans-serif;
}

/* SIDEBAR */

.sidebar{
  width:250px;
  background:white;
  padding:20px 14px;
  border-right:1px solid #e5e5e5;
  display:flex;
  flex-direction:column;
}

/* LOGO */

.logo{
  display:flex;
  align-items:center;
  gap:14px;
  margin-bottom:35px;
  padding:0 10px;
}

.logo-circle{
  width:48px;
  height:48px;
  border-radius:50%;
  background:#e6925e;
  color:white;
  display:flex;
  align-items:center;
  justify-content:center;
  font-size:22px;
  font-weight:bold;
}

.logo-text h2{
  color:#d87d4a;
  font-size:24px;
  margin:0;
}

.logo-text p{
  margin:0;
  color:#777;
  font-size:12px;
  letter-spacing:2px;
}

/* MENU */

.menu-list{
  display:flex;
  flex-direction:column;
  gap:8px;
}

.item{
  display:flex;
  align-items:center;
  gap:14px;
  padding:14px;
  border-radius:12px;
  text-decoration:none;
  color:#333;
  transition:0.2s;
  font-size:15px;
}

.item i{
  width:20px;
  font-size:18px;
}

.item:hover{
  background:#f3f3f3;
}

.item.router-link-active{
  background:#e6925e;
  color:white;
}

/* BOTTOM */

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
}

.bottom-item:hover{
  background:#f3f3f3;
}

.bottom-item i{
  width:20px;
}

.logout{
  color:red;
}

/* MAIN */

.main{
  flex:1;
  display:flex;
  flex-direction:column;
}

/* HEADER */

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
  margin:0;
  font-size:24px;
  color:#333;
}

/* USER */

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
  margin:0;
  font-size:14px;
}

.user-info p{
  margin:0;
  color:#888;
  font-size:12px;
}

.arrow{
  font-size:12px;
  color:#777;
}

/* DROPDOWN */

.dropdown{
  position:absolute;
  top:65px;
  right:0;
  width:200px;
  background:white;
  border-radius:12px;
  box-shadow:0 4px 20px rgba(0,0,0,0.1);
  overflow:hidden;
  z-index:999;
}

.dropdown-item{
  display:flex;
  align-items:center;
  gap:12px;
  padding:14px 16px;
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

/* CONTENT */

.content{
  flex:1;
  padding:20px;
}

</style>
```
