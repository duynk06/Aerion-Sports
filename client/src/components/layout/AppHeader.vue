<template>
  <header class="w-full bg-white shadow-sm sticky top-0 z-50">

    
    <!-- Main header -->
    <div class="container mx-auto px-5 h-20 flex justify-between items-center">
      <!-- Logo -->
      <router-link to="/" class="flex-shrink-0 z-10">
        <img src="../../assets/logo/logo.jpg" alt="Aerion Sports" class="h-12 w-auto object-contain" />
      </router-link>

      <nav class="hidden lg:flex items-center gap-10 absolute left-1/2 -translate-x-1/2">
        <router-link to="/" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2" exact-active-class="text-secondary-color active-nav">
          Trang Chủ
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </router-link>
        <router-link to="/products" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2" active-class="text-secondary-color active-nav">
          Sản Phẩm
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </router-link>
        <a href="#" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2">
          Giới Thiệu
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </a>
        <a href="#" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2">
          Tin Tức
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </a>
        <a href="#" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2">
          Liên Hệ
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </a>
      </nav>

      <!-- Actions -->
      <div class="flex items-center gap-4 ml-auto z-10">
        <div class="relative hidden xl:block mr-2 group">
          <input type="text" placeholder="Tìm vợt Yonex, Victor..." class="w-72 pl-5 pr-12 py-2.5 bg-gray-100 border border-transparent rounded-full text-sm outline-none focus:bg-white focus:border-secondary-color focus:ring-4 focus:ring-secondary-color/10 transition-all duration-300" />
          <button class="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 group-focus-within:text-secondary-color transition-colors hover:text-secondary-color">
            <SearchOutlined class="text-lg" />
          </button>
        </div>
        
        <router-link to="/cart" class="relative group mt-1">
          <div class="p-2.5 bg-gray-50 rounded-full group-hover:bg-secondary-color transition-all duration-300">
            <ShoppingCartOutlined class="text-xl text-primary-color group-hover:text-white transition-colors" />
          </div>
          <span v-if="cartCount > 0" class="absolute -top-1 -right-1 bg-secondary-color text-white text-[10px] font-bold h-5 w-5 rounded-full flex items-center justify-center border-2 border-white shadow-sm">
            {{ cartCount }}
          </span>
        </router-link>

        <div class="relative group mt-1">
          <router-link to="/profile" class="block p-2.5 bg-gray-50 rounded-full group-hover:bg-secondary-color transition-all duration-300">
            <UserOutlined class="text-xl text-primary-color group-hover:text-white transition-colors" />
          </router-link>
          
          <!-- Dropdown Menu -->
          <div class="absolute right-0 top-full mt-2 w-48 bg-white rounded-md shadow-lg py-1 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 border border-gray-100 z-50">
            <router-link to="/profile" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-secondary-color transition-colors">
              Hồ sơ
            </router-link>
            <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-secondary-color transition-colors">
              Đơn hàng của tôi
            </a>
            <router-link to="/login" class="block px-4 py-2 text-sm text-red-600 hover:bg-gray-50 transition-colors border-t border-gray-100">
              Đăng xuất
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>
<script setup>
import { PhoneOutlined, EnvironmentOutlined, SearchOutlined, ShoppingCartOutlined, UserOutlined } from '@ant-design/icons-vue';
import { useCart } from '../../composables/useCart'

const { cartCount } = useCart()
</script>
<style scoped>
/* Animated Underline for Active Nav */
.active-nav span {
  width: 100% !important;
}
</style>
