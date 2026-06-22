<template>
  <header class="w-full bg-white shadow-sm sticky top-0 z-50">

    
    <!-- Main header -->
    <div class="container mx-auto px-5 h-20 flex justify-between items-center">
      <!-- Logo -->
      <router-link to="/" class="flex-shrink-0">
        <img src="../../assets/logo/logo.jpg" alt="Aerion Sports" class="h-12 w-auto object-contain" />
      </router-link>

      <!-- Nav -->
      <nav class="hidden lg:flex gap-8 ml-10">
        <router-link to="/" class="font-bold text-primary-color uppercase text-[14px] hover:text-secondary-color transition" active-class="text-secondary-color">Trang chủ</router-link>
        <router-link to="/products" class="font-bold text-primary-color uppercase text-[14px] hover:text-secondary-color transition" active-class="text-secondary-color">Vợt Cầu Lông</router-link>
        <a href="#" class="font-bold text-primary-color uppercase text-[14px] hover:text-secondary-color transition">Thương Hiệu</a>
        <a href="#" class="font-bold text-primary-color uppercase text-[14px] hover:text-secondary-color transition">Phụ Kiện</a>
        <a href="#" class="font-bold text-primary-color uppercase text-[14px] hover:text-secondary-color transition">Tin Tức</a>
      </nav>

      <!-- Actions -->
      <div class="flex items-center gap-4 ml-auto">
        <div class="relative hidden md:block mr-2">
          <input type="text" placeholder="Tìm vợt Yonex, Victor..." class="w-64 pl-4 pr-10 py-2 border-2 border-gray-100 bg-gray-50 rounded-full text-sm outline-none focus:border-secondary-color transition-colors" />
          <button class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-secondary-color">
            <SearchOutlined class="text-lg" />
          </button>
        </div>
        
        <router-link to="/cart" class="relative group">
          <div class="p-2 bg-gray-50 rounded-full group-hover:bg-secondary-color/10 transition">
            <ShoppingCartOutlined class="text-2xl text-primary-color group-hover:text-secondary-color transition" />
          </div>
          <span class="absolute -top-1 -right-1 bg-secondary-color text-white text-[10px] font-bold h-5 w-5 rounded-full flex items-center justify-center border-2 border-white">2</span>
        </router-link>

        <a href="#" class="p-2 bg-gray-50 rounded-full hover:bg-secondary-color/10 transition group" title="Đăng nhập / Đăng ký">
          <UserOutlined class="text-2xl text-primary-color group-hover:text-secondary-color transition" />
        </a>
      </div>
    </div>
  </header>
</template>
<script setup>
import { PhoneOutlined, EnvironmentOutlined, SearchOutlined, ShoppingCartOutlined, UserOutlined } from '@ant-design/icons-vue';
</script>
