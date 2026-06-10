
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

        <router-link to="/san-pham" class="item">

          <i class="fa-solid fa-box-open"></i>

          <span>Sản phẩm</span>

        </router-link>
       <router-link to="/thuoc-tinh" class="item">
          <i class="fa-solid fa-list-check"></i>
          <span>Thuộc tính</span> 
        </router-link>

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

.logo-circle{
  width:50px;
  height:50px;
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
}

.logo-text p{
  color:#888;
  font-size:12px;
  letter-spacing:2px;
}

.menu-list{
  display:flex;
  flex-direction:column;
  gap:6px;
}

.item{
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
}
.item i{
  width:20px;
  font-size:15px;
}

.item:hover{
  background:#fff8f3;
}

.item.router-link-active{
  background:#fff4ed;
  color:#d87d4a;
  box-shadow:none;
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
```
