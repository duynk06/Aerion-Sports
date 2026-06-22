<template>
  <div class="home">
    <section class="relative overflow-hidden bg-gradient-to-r from-[#0b162c] via-[#0f2140] to-[#162c56]">
      <div class="absolute inset-0 opacity-20 bg-[radial-gradient(circle_at_top_right,_rgba(255,107,0,0.5),_transparent_35%),radial-gradient(circle_at_bottom_left,_rgba(59,130,246,0.35),_transparent_30%)]"></div>
      <div class="container mx-auto px-5 relative z-10 py-20 lg:py-32 flex items-center">
        <div class="max-w-2xl">
          <div class="inline-block bg-secondary-color text-white px-3 py-1 font-bold uppercase text-xs tracking-widest mb-6 skew-x-[-10deg]">
            <span class="skew-x-[10deg] block">Bộ Sưu Tập 2026</span>
          </div>
          <h1 class="text-5xl lg:text-7xl font-bold text-white uppercase leading-[1.1] tracking-tight mb-6 font-montserrat">
            Nâng Cấp <br />
            <span class="text-transparent bg-clip-text bg-gradient-to-r from-secondary-color to-yellow-300">Từng Pha Cầu</span>
          </h1>
          <p class="text-gray-300 text-lg mb-10 max-w-lg">
            Bộ mock vợt cầu lông với dữ liệu và hình ảnh có sẵn để bạn test giao diện, lọc sản phẩm và chi tiết sản phẩm ngay trên local.
          </p>
          <div class="flex gap-4">
            <router-link to="/products" class="bg-secondary-color hover:bg-[#e66000] text-white font-bold py-4 px-8 uppercase tracking-wider transition-all skew-x-[-10deg] hover:scale-105">
              <span class="skew-x-[10deg] block">Mua Ngay</span>
            </router-link>
            <router-link to="/products" class="bg-transparent border-2 border-white hover:bg-white hover:text-primary-color text-white font-bold py-4 px-8 uppercase tracking-wider transition-all skew-x-[-10deg]">
              <span class="skew-x-[10deg] block">Xem Mẫu</span>
            </router-link>
          </div>
        </div>
      </div>
    </section>

    <section class="bg-white border-b border-gray-100">
      <div class="container mx-auto px-5 py-6">
        <div class="flex flex-wrap justify-between items-center gap-6">
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 rounded-full bg-secondary-color/10 flex items-center justify-center text-secondary-color text-xl"><SafetyCertificateOutlined /></div>
            <div>
              <h4 class="font-bold text-sm text-primary-color">100% Chính Hãng</h4>
              <p class="text-xs text-gray-500">Mock data để test luồng bán hàng</p>
            </div>
          </div>
          <div class="w-px h-10 bg-gray-200 hidden md:block"></div>
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 rounded-full bg-secondary-color/10 flex items-center justify-center text-secondary-color text-xl"><ToolOutlined /></div>
            <div>
              <h4 class="font-bold text-sm text-primary-color">Bảo Hành 90 Ngày</h4>
              <p class="text-xs text-gray-500">Mô phỏng chính sách cửa hàng</p>
            </div>
          </div>
          <div class="w-px h-10 bg-gray-200 hidden lg:block"></div>
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 rounded-full bg-secondary-color/10 flex items-center justify-center text-secondary-color text-xl"><CarOutlined /></div>
            <div>
              <h4 class="font-bold text-sm text-primary-color">Giao Hàng Siêu Tốc</h4>
              <p class="text-xs text-gray-500">Mẫu giao diện hoàn chỉnh để demo</p>
            </div>
          </div>
          <div class="w-px h-10 bg-gray-200 hidden lg:block"></div>
          <div class="flex items-center gap-3">
            <div class="w-12 h-12 rounded-full bg-secondary-color/10 flex items-center justify-center text-secondary-color text-xl"><GiftOutlined /></div>
            <div>
              <h4 class="font-bold text-sm text-primary-color">Tặng Kèm Cước</h4>
              <p class="text-xs text-gray-500">Ảnh mock local, không phụ thuộc mạng</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="container mx-auto px-5 py-16">
      <div class="flex justify-between items-end mb-10">
        <div>
          <h2 class="text-3xl font-bold font-montserrat text-primary-color uppercase">Vợt Bán Chạy</h2>
          <div class="h-1 w-20 bg-secondary-color mt-3"></div>
        </div>
        <router-link to="/products" class="hidden sm:flex items-center gap-2 text-sm font-bold uppercase text-gray-500 hover:text-secondary-color transition">
          Xem tất cả <ArrowRightOutlined />
        </router-link>
      </div>

      <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-2 md:gap-4 border-t border-l border-gray-100">
        <ProductCard
          v-for="product in featuredProducts"
          :key="product.id"
          class="border-r border-b border-gray-100"
          :id="product.id"
          :name="product.name"
          :price="product.price"
          :oldPrice="product.oldPrice"
          :brand="product.brand"
          :badge="product.badge"
          :image="product.image"
        />
      </div>
    </section>

    <section class="container mx-auto px-5 pb-16">
      <div class="bg-gray-100 rounded-2xl overflow-hidden flex flex-col md:flex-row">
        <div class="p-10 md:p-16 flex-1 flex flex-col justify-center">
          <h3 class="text-3xl font-bold font-montserrat text-primary-color uppercase mb-4">{{ featuredProducts[0].name }}</h3>
          <p class="text-gray-600 mb-8 leading-relaxed">
            {{ featuredProducts[0].description }}
          </p>
          <div>
            <router-link :to="`/products/${featuredProducts[0].id}`" class="inline-block bg-primary-color hover:bg-secondary-color text-white font-bold py-3 px-8 transition-colors uppercase tracking-widest text-sm">
              Khám Phá Ngay
            </router-link>
          </div>
        </div>
        <div class="flex-1 bg-white flex items-center justify-center p-6 min-h-[300px]">
          <img :src="featuredProducts[0].image" :alt="featuredProducts[0].name" class="max-h-[420px] object-contain drop-shadow-2xl" />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import ProductCard from '../../components/product/ProductCard.vue'
import { SafetyCertificateOutlined, ToolOutlined, CarOutlined, GiftOutlined, ArrowRightOutlined } from '@ant-design/icons-vue'
import { featuredProducts } from '../../data/mockShopData'
</script>
