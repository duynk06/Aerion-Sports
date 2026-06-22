<template>
  <footer class="bg-primary-color text-white pt-16 mt-16">
    <div class="container mx-auto px-5 grid grid-cols-1 md:grid-cols-12 gap-10 mb-10">
      <div class="md:col-span-4">
        <!-- Logo Badge -->
        <router-link to="/" class="inline-block bg-white px-4 py-2.5 rounded-xl mb-6 shadow-[0_4px_20px_rgba(255,255,255,0.1)] hover:shadow-[0_4px_25px_rgba(255,107,0,0.2)] transition-all duration-300 border border-gray-100/10">
          <img src="../../assets/logo/logo.jpg" alt="Aerion Sports" class="h-10 w-auto object-contain" />
        </router-link>
        <p class="text-gray-400 text-sm leading-relaxed pr-6">Chuyên cung cấp vợt cầu lông chính hãng. Nơi hội tụ đam mê của các lông thủ với chất lượng dịch vụ hàng đầu.</p>
      </div>
      <div class="md:col-span-2">
        <h4 class="text-white text-lg font-bold mb-5 relative pb-2 after:content-[''] after:absolute after:left-0 after:bottom-0 after:w-10 after:h-0.5 after:bg-secondary-color">Về Chúng Tôi</h4>
        <ul class="text-gray-400 text-sm space-y-3">
          <li><a href="#" class="hover:text-secondary-color transition-colors">Giới thiệu</a></li>
          <li><a href="#" class="hover:text-secondary-color transition-colors">Hệ thống cửa hàng</a></li>
          <li><a href="#" class="hover:text-secondary-color transition-colors">Tin tức & Sự kiện</a></li>
        </ul>
      </div>
      <div class="md:col-span-3">
        <h4 class="text-white text-lg font-bold mb-5 relative pb-2 after:content-[''] after:absolute after:left-0 after:bottom-0 after:w-10 after:h-0.5 after:bg-secondary-color">Chính Sách</h4>
        <ul class="text-gray-400 text-sm space-y-3">
          <li><a href="#" class="hover:text-secondary-color transition-colors">Chính sách đổi trả</a></li>
          <li><a href="#" class="hover:text-secondary-color transition-colors">Chính sách bảo hành</a></li>
          <li><a href="#" class="hover:text-secondary-color transition-colors">Chính sách vận chuyển</a></li>
        </ul>
      </div>
      <div class="md:col-span-3">
        <h4 class="text-white text-lg font-bold mb-5 relative pb-2 after:content-[''] after:absolute after:left-0 after:bottom-0 after:w-10 after:h-0.5 after:bg-secondary-color">Liên Hệ</h4>
        <div class="text-gray-400 text-sm space-y-3">
          <p class="flex items-center gap-2"><PhoneOutlined /> 1900 6868</p>
          <p class="flex items-center gap-2"><MailOutlined /> support@aerionsports.vn</p>
          <p class="flex items-center gap-2"><EnvironmentOutlined /> 123 Đường Vợt Cầu Lông, Hà Nội</p>
        </div>
      </div>
    </div>
    <div class="border-t border-white/10 py-5 text-center text-sm text-gray-400">
      <p>&copy; 2026 Aerion Sports. All rights reserved.</p>
    </div>
  </footer>
</template>
<script setup>
import { PhoneOutlined, MailOutlined, EnvironmentOutlined } from '@ant-design/icons-vue';
</script>