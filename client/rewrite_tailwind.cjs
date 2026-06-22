const fs = require('fs');
const path = require('path');

const baseDir = "d:/A1/fe/Aerion-Sports/client";

const files = {
    "src/styles/global.css": `
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;500;600;700;800&family=Inter:wght@300;400;500;600&display=swap');

@tailwind base;
@tailwind components;
@tailwind utilities;

@layer base {
  body {
    @apply font-inter bg-gray-50 text-gray-800;
  }
  h1, h2, h3, h4, h5, h6 {
    @apply font-montserrat font-bold text-primary-color;
  }
}

@layer components {
  .btn-primary {
    @apply bg-secondary-color text-white px-6 py-3 rounded font-semibold transition-all duration-300 inline-flex items-center justify-center gap-2 hover:bg-secondary-hover hover:-translate-y-0.5 hover:shadow-lg;
  }
  .btn-outline {
    @apply bg-transparent text-primary-color border-2 border-primary-color px-6 py-2.5 rounded font-semibold transition-all duration-300 hover:bg-primary-color hover:text-white;
  }
}
`,
    "src/components/layout/AppHeader.vue": `
<template>
  <header class="fixed top-0 left-0 right-0 h-20 bg-white shadow-sm z-50 flex items-center">
    <div class="container mx-auto px-5 flex justify-between items-center w-full">
      <router-link to="/" class="flex items-center">
        <img src="../../assets/logo/logo.jpg" alt="Aerion Sports" class="h-12 w-auto object-contain block" />
      </router-link>
      
      <nav class="flex gap-8">
        <router-link to="/" class="font-montserrat font-semibold text-primary-color text-[15px] uppercase transition-colors hover:text-secondary-color" active-class="text-secondary-color">Trang chủ</router-link>
        <router-link to="/products" class="font-montserrat font-semibold text-primary-color text-[15px] uppercase transition-colors hover:text-secondary-color" active-class="text-secondary-color">Vợt Cầu Lông</router-link>
        <router-link to="#" class="font-montserrat font-semibold text-primary-color text-[15px] uppercase transition-colors hover:text-secondary-color">Thương hiệu</router-link>
        <router-link to="#" class="font-montserrat font-semibold text-primary-color text-[15px] uppercase transition-colors hover:text-secondary-color">Kiến thức</router-link>
        <router-link to="#" class="font-montserrat font-semibold text-primary-color text-[15px] uppercase transition-colors hover:text-secondary-color">Liên hệ</router-link>
      </nav>

      <div class="flex items-center gap-5">
        <div class="relative">
          <input type="text" placeholder="Tìm kiếm vợt..." class="px-4 py-2 rounded-full border border-gray-200 bg-gray-50 w-48 outline-none transition-all focus:border-secondary-color focus:bg-white focus:ring-2 focus:ring-secondary-color/20" />
          <SearchOutlined class="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400" />
        </div>
        <router-link to="/cart" class="relative text-2xl text-primary-color">
          <ShoppingCartOutlined />
          <span class="absolute -top-2 -right-2 bg-secondary-color text-white text-[10px] font-bold h-5 w-5 rounded-full flex items-center justify-center">2</span>
        </router-link>
      </div>
    </div>
  </header>
</template>
<script setup>
import { SearchOutlined, ShoppingCartOutlined } from '@ant-design/icons-vue';
</script>
`,
    "src/components/layout/AppFooter.vue": `
<template>
  <footer class="bg-primary-color text-white pt-16 mt-16">
    <div class="container mx-auto px-5 grid grid-cols-1 md:grid-cols-12 gap-10 mb-10">
      <div class="md:col-span-4">
        <img src="../../assets/logo/logo.jpg" alt="Aerion Sports" class="h-16 w-auto object-contain mb-5 block bg-white p-2 rounded" />
        <p class="text-gray-400 text-sm">Chuyên cung cấp vợt cầu lông chính hãng. Nơi hội tụ đam mê của các lông thủ với chất lượng dịch vụ hàng đầu.</p>
      </div>
      <div class="md:col-span-2">
        <h4 class="text-lg mb-5 relative pb-2 after:content-[''] after:absolute after:left-0 after:bottom-0 after:w-10 after:h-0.5 after:bg-secondary-color">Về Chúng Tôi</h4>
        <ul class="text-gray-400 text-sm space-y-3">
          <li><a href="#" class="hover:text-secondary-color transition-colors">Giới thiệu</a></li>
          <li><a href="#" class="hover:text-secondary-color transition-colors">Hệ thống cửa hàng</a></li>
          <li><a href="#" class="hover:text-secondary-color transition-colors">Tin tức & Sự kiện</a></li>
        </ul>
      </div>
      <div class="md:col-span-3">
        <h4 class="text-lg mb-5 relative pb-2 after:content-[''] after:absolute after:left-0 after:bottom-0 after:w-10 after:h-0.5 after:bg-secondary-color">Chính Sách</h4>
        <ul class="text-gray-400 text-sm space-y-3">
          <li><a href="#" class="hover:text-secondary-color transition-colors">Chính sách đổi trả</a></li>
          <li><a href="#" class="hover:text-secondary-color transition-colors">Chính sách bảo hành</a></li>
          <li><a href="#" class="hover:text-secondary-color transition-colors">Chính sách vận chuyển</a></li>
        </ul>
      </div>
      <div class="md:col-span-3">
        <h4 class="text-lg mb-5 relative pb-2 after:content-[''] after:absolute after:left-0 after:bottom-0 after:w-10 after:h-0.5 after:bg-secondary-color">Liên Hệ</h4>
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
`,
    "src/pages/Home/HomePage.vue": `
<template>
  <div class="home">
    <section class="bg-gradient-to-br from-primary-color to-primary-light py-20 text-white overflow-hidden">
      <div class="container mx-auto px-5 flex flex-col md:flex-row items-center justify-between">
        <div class="flex-1 max-w-2xl">
          <h1 class="text-white text-5xl leading-tight mb-5">SỨC MẠNH VƯỢT TRỘI<br/><span class="text-secondary-color">ĐAM MÊ BẤT TẬN</span></h1>
          <p class="text-lg text-gray-300 mb-10">Khám phá bộ sưu tập vợt cầu lông chuyên nghiệp từ các thương hiệu hàng đầu thế giới. Nâng tầm lối chơi của bạn ngay hôm nay.</p>
          <div class="flex gap-5">
            <router-link to="/products" class="btn-primary">Xem Tất Cả Vợt <ArrowRightOutlined /></router-link>
            <router-link to="/products" class="btn-outline border-white text-white hover:bg-white hover:text-primary-color">Tư Vấn Chọn Vợt</router-link>
          </div>
        </div>
        <div class="flex-1 flex justify-end relative mt-10 md:mt-0">
          <div class="w-[400px] h-[500px] bg-[url('https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80')] bg-center bg-cover rounded-2xl rotate-12 shadow-[0_20px_50px_rgba(0,0,0,0.5)] border-4 border-secondary-color"></div>
        </div>
      </div>
    </section>

    <section class="container mx-auto px-5 grid grid-cols-2 md:grid-cols-4 gap-5 -mt-10 relative z-10 mb-16">
      <div class="bg-white p-6 rounded-lg shadow-md text-center transition-transform hover:-translate-y-1">
        <SafetyCertificateOutlined class="text-4xl text-secondary-color mb-4" />
        <h3 class="text-base mb-2 text-primary-color">100% Chính Hãng</h3>
        <p class="text-sm text-gray-500">Cam kết chất lượng tuyệt đối</p>
      </div>
      <div class="bg-white p-6 rounded-lg shadow-md text-center transition-transform hover:-translate-y-1">
        <ToolOutlined class="text-4xl text-secondary-color mb-4" />
        <h3 class="text-base mb-2 text-primary-color">Bảo Hành Uy Tín</h3>
        <p class="text-sm text-gray-500">Lỗi 1 đổi 1 trong 30 ngày</p>
      </div>
      <div class="bg-white p-6 rounded-lg shadow-md text-center transition-transform hover:-translate-y-1">
        <RocketOutlined class="text-4xl text-secondary-color mb-4" />
        <h3 class="text-base mb-2 text-primary-color">Giao Hàng Siêu Tốc</h3>
        <p class="text-sm text-gray-500">Giao hàng toàn quốc 2-3 ngày</p>
      </div>
      <div class="bg-white p-6 rounded-lg shadow-md text-center transition-transform hover:-translate-y-1">
        <GiftOutlined class="text-4xl text-secondary-color mb-4" />
        <h3 class="text-base mb-2 text-primary-color">Quà Tặng Hấp Dẫn</h3>
        <p class="text-sm text-gray-500">Tặng kèm cước, quấn cán</p>
      </div>
    </section>

    <section class="container mx-auto px-5 mb-20">
      <div class="flex justify-between items-end mb-8 mt-16">
        <h2 class="text-3xl m-0 text-primary-color">VỢT BÁN CHẠY NHẤT</h2>
        <router-link to="/products" class="text-secondary-color font-semibold hover:underline">Xem thêm <ArrowRightOutlined /></router-link>
      </div>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <ProductCard v-for="i in 4" :key="'hot-'+i" :id="i" :name="'Yonex Astrox 99 Pro'" :price="3450000" :brand="'Yonex'" :badge="i === 1 ? 'Best Seller' : ''" />
      </div>
    </section>

    <section class="container mx-auto px-5 mb-20">
      <div class="flex justify-between items-end mb-8 mt-16">
        <h2 class="text-3xl m-0 text-primary-color">SẢN PHẨM MỚI</h2>
        <router-link to="/products" class="text-secondary-color font-semibold hover:underline">Xem thêm <ArrowRightOutlined /></router-link>
      </div>
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <ProductCard v-for="i in 4" :key="'new-'+i" :id="i+4" :name="'Victor Thruster Ryuga II'" :price="3200000" :brand="'Victor'" :badge="'New'" />
      </div>
    </section>
  </div>
</template>

<script setup>
import ProductCard from '../../components/product/ProductCard.vue'
import { SafetyCertificateOutlined, ToolOutlined, RocketOutlined, GiftOutlined, ArrowRightOutlined } from '@ant-design/icons-vue'
</script>
`,
    "src/components/product/ProductCard.vue": `
<template>
  <div class="bg-white rounded-lg overflow-hidden shadow-sm transition-all duration-300 border border-gray-200 relative hover:shadow-xl hover:-translate-y-1 group">
    <div v-if="badge" class="absolute top-2 left-2 bg-secondary-color text-white text-xs font-bold px-2 py-1 rounded-full z-10 uppercase">{{ badge }}</div>
    <div class="relative pt-[100%] bg-gray-100 overflow-hidden">
      <img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60" :alt="name" class="absolute top-0 left-0 w-full h-full object-cover transition-transform duration-500 group-hover:scale-105" />
      <div class="absolute inset-0 bg-primary-color/40 flex items-center justify-center opacity-0 transition-opacity duration-300 group-hover:opacity-100">
        <router-link :to="'/products/' + id" class="btn-primary py-2 px-4 text-sm"><EyeOutlined /> Xem Chi Tiết</router-link>
      </div>
    </div>
    <div class="p-4">
      <span class="text-xs text-gray-500 uppercase tracking-widest font-semibold">{{ brand }}</span>
      <h3 class="text-base my-2 leading-snug font-semibold"><router-link :to="'/products/' + id" class="text-gray-800 transition-colors hover:text-secondary-color">{{ name }}</router-link></h3>
      <div>
        <span class="text-lg font-bold text-secondary-color font-montserrat">{{ formatPrice(price) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { EyeOutlined } from '@ant-design/icons-vue'

const props = defineProps({
  id: Number,
  name: String,
  price: Number,
  brand: String,
  badge: String
})

const formatPrice = (value) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
}
</script>
`,
    "src/pages/Products/ProductsPage.vue": `
<template>
  <div class="container mx-auto px-5 py-10">
    <div class="mb-5 text-sm text-gray-500 flex items-center gap-2">
      <router-link to="/" class="hover:text-primary-color"><HomeOutlined /></router-link>
      <span>/</span>
      <span class="text-gray-800">Vợt Cầu Lông</span>
    </div>
    
    <div class="flex flex-col md:flex-row justify-between md:items-center mb-10 pb-5 border-b-2 border-gray-200">
      <h1 class="text-3xl m-0 text-primary-color mb-4 md:mb-0">VỢT CẦU LÔNG CHUYÊN NGHIỆP</h1>
      <div>
        <select class="px-4 py-2.5 rounded border border-gray-300 outline-none focus:border-secondary-color bg-white">
          <option>Mới nhất</option>
          <option>Giá tăng dần</option>
          <option>Giá giảm dần</option>
          <option>Bán chạy</option>
        </select>
      </div>
    </div>

    <div class="flex flex-col lg:flex-row gap-10">
      <aside class="w-full lg:w-64 shrink-0">
        <div class="mb-8">
          <h3 class="text-base mb-4 uppercase tracking-wider text-primary-color font-bold">Thương Hiệu</h3>
          <div class="space-y-2">
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> Yonex (45)</label>
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> Victor (32)</label>
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> Lining (28)</label>
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> Mizuno (15)</label>
          </div>
        </div>
        
        <div class="mb-8">
          <h3 class="text-base mb-4 uppercase tracking-wider text-primary-color font-bold">Mức Giá</h3>
          <div class="space-y-2">
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> Dưới 1 triệu</label>
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> 1 triệu - 2 triệu</label>
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> 2 triệu - 3 triệu</label>
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> Trên 3 triệu</label>
          </div>
        </div>

        <div class="mb-8">
          <h3 class="text-base mb-4 uppercase tracking-wider text-primary-color font-bold">Trọng Lượng</h3>
          <div class="space-y-2">
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> 3U (85-89g)</label>
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> 4U (80-84g)</label>
            <label class="flex items-center gap-2 cursor-pointer text-[15px]"><input type="checkbox" class="accent-secondary-color w-4 h-4" /> 5U (75-79g)</label>
          </div>
        </div>
      </aside>
      
      <main class="flex-1">
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          <ProductCard v-for="i in 12" :key="i" :id="i" :name="'Vợt Cầu Lông Cao Cấp ' + i" :price="1500000 + (i * 100000)" :brand="i % 2 === 0 ? 'Yonex' : 'Victor'" />
        </div>
        <div class="flex justify-center gap-2 mt-12">
          <button class="w-10 h-10 border border-secondary-color bg-secondary-color text-white rounded flex items-center justify-center transition-colors">1</button>
          <button class="w-10 h-10 border border-gray-300 bg-white hover:border-secondary-color hover:text-secondary-color rounded flex items-center justify-center transition-colors">2</button>
          <button class="w-10 h-10 border border-gray-300 bg-white hover:border-secondary-color hover:text-secondary-color rounded flex items-center justify-center transition-colors">3</button>
          <button class="px-4 h-10 border border-gray-300 bg-white hover:border-secondary-color hover:text-secondary-color rounded flex items-center justify-center transition-colors gap-1">Tiếp <RightOutlined class="text-xs" /></button>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import ProductCard from '../../components/product/ProductCard.vue'
import { HomeOutlined, RightOutlined } from '@ant-design/icons-vue'
</script>
`,
    "src/pages/ProductDetail/ProductDetailPage.vue": `
<template>
  <div class="container mx-auto px-5 py-10">
    <div class="mb-8 text-sm text-gray-500 flex items-center gap-2">
      <router-link to="/" class="hover:text-primary-color"><HomeOutlined /></router-link>
      <span>/</span>
      <router-link to="/products" class="hover:text-primary-color">Vợt Cầu Lông</router-link>
      <span>/</span>
      <span class="text-gray-800 font-medium">Yonex Astrox 99 Pro</span>
    </div>

    <div class="flex flex-col lg:flex-row gap-16 mb-16">
      <div class="flex-1">
        <div class="bg-white rounded-lg border border-gray-200 p-5 mb-4 aspect-square">
          <img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80" alt="Vợt" class="w-full h-full object-contain" />
        </div>
        <div class="flex gap-4">
          <div class="w-20 h-20 border-2 border-secondary-color rounded p-1 cursor-pointer bg-white"><img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80" class="w-full h-full object-cover" /></div>
          <div class="w-20 h-20 border border-gray-200 rounded p-1 cursor-pointer bg-white hover:border-secondary-color"><img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80" class="w-full h-full object-cover" /></div>
          <div class="w-20 h-20 border border-gray-200 rounded p-1 cursor-pointer bg-white hover:border-secondary-color"><img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=200&q=80" class="w-full h-full object-cover" /></div>
        </div>
      </div>

      <div class="flex-1">
        <div class="text-sm text-gray-500 uppercase tracking-widest mb-2 font-semibold">Yonex</div>
        <h1 class="text-3xl lg:text-4xl leading-tight mb-5 text-primary-color">Vợt Cầu Lông Yonex Astrox 99 Pro Chính Hãng</h1>
        <div class="text-4xl font-bold text-secondary-color font-montserrat mb-8">3.450.000 ₫</div>
        
        <div class="mb-6">
          <h4 class="text-sm text-gray-800 mb-3 font-semibold">Trọng lượng / Kích thước cán:</h4>
          <div class="flex gap-3">
            <button class="px-5 py-2.5 border-2 border-secondary-color text-secondary-color bg-secondary-color/5 rounded font-medium transition-colors">3U / G5</button>
            <button class="px-5 py-2.5 border border-gray-300 rounded font-medium hover:border-secondary-color hover:text-secondary-color transition-colors">4U / G5</button>
          </div>
        </div>
        
        <div class="mb-8">
          <h4 class="text-sm text-gray-800 mb-3 font-semibold">Màu sắc:</h4>
          <div class="flex gap-3">
            <button class="px-5 py-2.5 border-2 border-secondary-color text-secondary-color bg-secondary-color/5 rounded font-medium transition-colors">Trắng Hổ Phách (White Tiger)</button>
            <button class="px-5 py-2.5 border border-gray-300 rounded font-medium hover:border-secondary-color hover:text-secondary-color transition-colors">Đỏ Đậm (Cherry Sunburst)</button>
          </div>
        </div>

        <div class="flex flex-col sm:flex-row gap-5 my-10 pt-8 border-t border-gray-200">
          <div class="flex border border-gray-300 rounded overflow-hidden">
            <button class="w-12 bg-white text-xl hover:bg-gray-50">-</button>
            <input type="text" value="1" class="w-14 border-x border-gray-300 text-center font-bold outline-none" />
            <button class="w-12 bg-white text-xl hover:bg-gray-50">+</button>
          </div>
          <button class="btn-primary flex-1 text-lg h-[50px]"><ShoppingCartOutlined /> Thêm Vào Giỏ Hàng</button>
        </div>

        <div class="bg-gray-50 p-6 rounded-lg border-l-4 border-secondary-color space-y-2">
          <p class="font-medium flex items-center gap-2"><CheckCircleOutlined class="text-secondary-color" /> Tặng cước Yonex BG65 Ti</p>
          <p class="font-medium flex items-center gap-2"><CheckCircleOutlined class="text-secondary-color" /> Miễn phí công căng</p>
          <p class="font-medium flex items-center gap-2"><CheckCircleOutlined class="text-secondary-color" /> Tặng quấn cán Yonex xịn</p>
          <p class="font-medium flex items-center gap-2"><CheckCircleOutlined class="text-secondary-color" /> Bảo hành 3 tháng lỗi nhà sản xuất</p>
        </div>
      </div>
    </div>

    <div class="mt-16">
      <div class="flex gap-10 border-b border-gray-200 mb-8">
        <button class="pb-4 text-xl font-semibold text-primary-color relative after:content-[''] after:absolute after:-bottom-[1.5px] after:left-0 after:right-0 after:h-[3px] after:bg-secondary-color">Thông Số Kỹ Thuật</button>
        <button class="pb-4 text-xl text-gray-500 hover:text-primary-color transition-colors">Mô Tả Sản Phẩm</button>
      </div>
      <div>
        <table class="w-full max-w-4xl border-collapse">
          <tbody>
            <tr class="border-b border-gray-200"><td class="p-4 font-semibold w-1/3 bg-gray-50">Độ cứng</td><td class="p-4">Siêu Cứng (Extra Stiff)</td></tr>
            <tr class="border-b border-gray-200"><td class="p-4 font-semibold bg-gray-50">Khung vợt</td><td class="p-4">H.M. GRAPHITE + Namd + VOLUME CUT RESIN + Tungsten</td></tr>
            <tr class="border-b border-gray-200"><td class="p-4 font-semibold bg-gray-50">Thân vợt</td><td class="p-4">H.M. Graphite + Namd</td></tr>
            <tr class="border-b border-gray-200"><td class="p-4 font-semibold bg-gray-50">Điểm cân bằng</td><td class="p-4">Nặng đầu</td></tr>
            <tr class="border-b border-gray-200"><td class="p-4 font-semibold bg-gray-50">Mức căng tối đa</td><td class="p-4">3U: 21 - 29 lbs, 4U: 20 - 28 lbs</td></tr>
            <tr class="border-b border-gray-200"><td class="p-4 font-semibold bg-gray-50">Trình độ phù hợp</td><td class="p-4">Người chơi chuyên nghiệp, có lực cổ tay khỏe, lối đánh thiên công.</td></tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
<script setup>
import { HomeOutlined, ShoppingCartOutlined, CheckCircleOutlined } from '@ant-design/icons-vue'
</script>
`,
    "src/pages/Cart/CartPage.vue": `
<template>
  <div class="container mx-auto px-5 py-10">
    <h1 class="text-3xl text-primary-color mb-10">GIỎ HÀNG CỦA BẠN</h1>
    <div class="flex flex-col lg:flex-row gap-10">
      <div class="flex-[2]">
        <div class="flex pb-4 border-b-2 border-gray-200 font-semibold uppercase text-sm text-gray-700">
          <div class="flex-[2]">Sản phẩm</div>
          <div class="flex-1 text-center">Đơn giá</div>
          <div class="flex-1 text-center">Số lượng</div>
          <div class="flex-1 text-right">Thành tiền</div>
        </div>
        
        <div class="flex items-center py-6 border-b border-gray-200" v-for="i in 2" :key="i">
          <div class="flex-[2] flex gap-5">
            <img src="https://images.unsplash.com/photo-1622279457486-62dcc4a431d6?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=60" alt="Vợt" class="w-24 h-24 object-cover rounded border border-gray-200" />
            <div>
              <h3 class="text-lg font-bold mb-1">Yonex Astrox 99 Pro</h3>
              <p class="text-gray-500 text-sm mb-3">Màu: Trắng Hổ Phách, Bản: 4U</p>
              <button class="text-red-500 underline text-sm hover:text-red-700 flex items-center gap-1"><DeleteOutlined /> Xóa</button>
            </div>
          </div>
          <div class="flex-1 text-center font-semibold text-gray-800">3.450.000 ₫</div>
          <div class="flex-1 flex justify-center">
            <div class="flex border border-gray-300 rounded overflow-hidden">
              <button class="w-8 h-9 bg-white hover:bg-gray-50">-</button>
              <input type="text" value="1" class="w-10 h-9 text-center border-x border-gray-300 outline-none" />
              <button class="w-8 h-9 bg-white hover:bg-gray-50">+</button>
            </div>
          </div>
          <div class="flex-1 text-right font-bold text-secondary-color">3.450.000 ₫</div>
        </div>
      </div>
      
      <div class="flex-1">
        <div class="bg-gray-50 p-8 rounded-xl sticky top-24">
          <h3 class="text-xl font-bold mb-6 text-primary-color">Tóm Tắt Đơn Hàng</h3>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Tạm tính</span>
            <span class="font-semibold">6.900.000 ₫</span>
          </div>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Khuyến mãi</span>
            <span class="font-semibold text-green-600">- 0 ₫</span>
          </div>
          <div class="flex justify-between mt-6 pt-6 border-t-2 border-gray-200 text-lg font-bold">
            <span class="text-primary-color">Tổng cộng</span>
            <span class="text-secondary-color text-2xl">6.900.000 ₫</span>
          </div>
          <p class="text-right text-xs text-gray-500 mt-2 mb-8">(Đã bao gồm VAT nếu có)</p>
          <router-link to="/checkout" class="btn-primary w-full text-center block mb-4 py-4">Tiến Hành Đặt Hàng</router-link>
          <router-link to="/products" class="text-center block text-primary-color font-medium hover:underline"><ArrowLeftOutlined /> Tiếp tục mua hàng</router-link>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { DeleteOutlined, ArrowLeftOutlined } from '@ant-design/icons-vue'
</script>
`,
    "src/pages/Checkout/CheckoutPage.vue": `
<template>
  <div class="container mx-auto px-5 py-10">
    <h1 class="text-3xl text-primary-color mb-10">THÔNG TIN THANH TOÁN</h1>
    <div class="flex flex-col lg:flex-row gap-12">
      <div class="flex-[2]">
        <div class="mb-10">
          <h3 class="text-xl font-bold mb-6 pb-2 border-b border-gray-200 text-primary-color"><UserOutlined /> Thông tin giao hàng</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-5 mb-5">
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Họ và tên</label>
              <input type="text" placeholder="Nhập họ tên" class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all" />
            </div>
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Số điện thoại</label>
              <input type="text" placeholder="Nhập số điện thoại" class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all" />
            </div>
          </div>
          <div class="mb-5">
            <label class="block mb-2 font-medium text-sm text-gray-700">Email</label>
            <input type="email" placeholder="Nhập email" class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all" />
          </div>
          <div class="mb-5">
            <label class="block mb-2 font-medium text-sm text-gray-700">Địa chỉ</label>
            <input type="text" placeholder="Nhập địa chỉ nhận hàng" class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all" />
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-5 mb-5">
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Tỉnh/Thành phố</label>
              <select class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all bg-white"><option>Hà Nội</option></select>
            </div>
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Quận/Huyện</label>
              <select class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all bg-white"><option>Cầu Giấy</option></select>
            </div>
          </div>
          <div>
            <label class="block mb-2 font-medium text-sm text-gray-700">Ghi chú đơn hàng (Tùy chọn)</label>
            <textarea rows="3" placeholder="Ghi chú về mức căng dây, thời gian giao hàng..." class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all"></textarea>
          </div>
        </div>

        <div>
          <h3 class="text-xl font-bold mb-6 pb-2 border-b border-gray-200 text-primary-color"><CreditCardOutlined /> Phương thức thanh toán</h3>
          <div class="space-y-4">
            <label class="flex items-start gap-4 p-5 border-2 border-secondary-color bg-secondary-color/5 rounded cursor-pointer transition-all">
              <input type="radio" name="payment" checked class="mt-1 accent-secondary-color w-4 h-4" />
              <div>
                <h4 class="text-base font-bold text-gray-800 mb-1">Thanh toán khi nhận hàng (COD)</h4>
                <p class="text-sm text-gray-500">Khách hàng trả tiền mặt khi giao hàng</p>
              </div>
            </label>
            <label class="flex items-start gap-4 p-5 border border-gray-200 hover:border-gray-300 rounded cursor-pointer transition-all">
              <input type="radio" name="payment" class="mt-1 accent-secondary-color w-4 h-4" />
              <div>
                <h4 class="text-base font-bold text-gray-800 mb-1">Chuyển khoản qua ngân hàng</h4>
                <p class="text-sm text-gray-500">Thanh toán an toàn qua VNPAY / Momo</p>
              </div>
            </label>
          </div>
        </div>
      </div>
      
      <div class="flex-1">
        <div class="bg-white p-8 rounded-xl border border-gray-200 sticky top-24">
          <h3 class="text-xl font-bold mb-6 text-primary-color"><ShoppingCartOutlined /> Đơn Hàng Của Bạn</h3>
          <div class="border-b border-gray-200 pb-5 mb-5 space-y-4">
            <div class="flex justify-between items-center text-sm">
              <span class="font-medium text-gray-800">Yonex Astrox 99 Pro x 2</span>
              <span class="font-bold">6.900.000 ₫</span>
            </div>
          </div>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Tạm tính</span>
            <span class="font-semibold">6.900.000 ₫</span>
          </div>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Phí vận chuyển</span>
            <span class="font-semibold text-green-600">Miễn phí</span>
          </div>
          <div class="flex justify-between mt-6 pt-6 border-t-2 border-gray-200 text-lg font-bold">
            <span class="text-primary-color">Tổng cộng</span>
            <span class="text-secondary-color text-2xl">6.900.000 ₫</span>
          </div>
          <button class="btn-primary w-full text-center block mt-8 py-4 text-lg"><CheckCircleOutlined /> ĐẶT HÀNG NGAY</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { UserOutlined, CreditCardOutlined, ShoppingCartOutlined, CheckCircleOutlined } from '@ant-design/icons-vue'
</script>
`
};

for (const [filepath, content] of Object.entries(files)) {
    const fullPath = path.join(baseDir, filepath);
    fs.mkdirSync(path.dirname(fullPath), { recursive: true });
    fs.writeFileSync(fullPath, content.trim(), 'utf8');
}
console.log('Vue files rewritten with Tailwind and Ant Design Icons!');
