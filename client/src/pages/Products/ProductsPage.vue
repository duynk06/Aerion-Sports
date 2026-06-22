<template>
  <div class="container mx-auto px-5 py-10">
    <div class="mb-5 text-sm text-gray-500 flex items-center gap-2">
      <router-link to="/" class="hover:text-primary-color"><HomeOutlined /></router-link>
      <span>/</span>
      <span class="text-gray-800">Vợt Cầu Lông</span>
    </div>
    
    <div class="flex flex-col md:flex-row justify-between md:items-center mb-10 pb-5 border-b-2 border-gray-200 gap-4">
      <div>
        <h1 class="text-3xl m-0 text-primary-color mb-2">VỢT CẦU LÔNG CHUYÊN NGHIỆP</h1>
        <p class="text-gray-500 text-sm max-w-2xl">
          Bộ mock dữ liệu gồm nhiều mẫu vợt, ảnh local và giá mẫu để bạn test danh sách, chi tiết và giỏ hàng.
        </p>
      </div>
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
            <label v-for="brand in shopBrands" :key="brand.label" class="flex items-center gap-2 cursor-pointer text-[15px]">
              <input type="checkbox" class="accent-secondary-color w-4 h-4" />
              {{ brand.label }} ({{ brand.count }})
            </label>
          </div>
        </div>
        
        <div class="mb-8">
          <h3 class="text-base mb-4 uppercase tracking-wider text-primary-color font-bold">Mức Giá</h3>
          <div class="space-y-2">
            <label v-for="price in priceRanges" :key="price" class="flex items-center gap-2 cursor-pointer text-[15px]">
              <input type="checkbox" class="accent-secondary-color w-4 h-4" />
              {{ price }}
            </label>
          </div>
        </div>

        <div class="mb-8">
          <h3 class="text-base mb-4 uppercase tracking-wider text-primary-color font-bold">Trọng Lượng</h3>
          <div class="space-y-2">
            <label v-for="weight in weightRanges" :key="weight" class="flex items-center gap-2 cursor-pointer text-[15px]">
              <input type="checkbox" class="accent-secondary-color w-4 h-4" />
              {{ weight }}
            </label>
          </div>
        </div>
      </aside>
      
      <main class="flex-1">
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          <ProductCard
            v-for="product in mockProducts"
            :key="product.id"
            :id="product.id"
            :name="product.name"
            :price="product.price"
            :oldPrice="product.oldPrice"
            :brand="product.brand"
            :badge="product.badge"
            :image="product.image"
          />
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
import { mockProducts, shopBrands, priceRanges, weightRanges } from '../../data/mockShopData'
</script>
