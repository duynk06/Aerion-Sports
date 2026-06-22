<template>
  <div class="bg-white group relative flex flex-col h-full border border-transparent hover:border-gray-100 transition-colors p-2">
    <!-- Badge (Circle) -->
    <div v-if="badge" class="absolute top-2 left-2 z-10">
      <span class="bg-[#d32323] text-white text-[11px] font-bold w-10 h-10 flex items-center justify-center rounded-full border-2 border-white shadow-sm">
        {{ badge }}
      </span>
    </div>

    <!-- Image -->
    <div class="relative w-full aspect-[3/4] overflow-hidden flex items-center justify-center mb-4 bg-white">
      <!-- Use CSS filters to blow out the light-grey studio background to pure white -->
      <img :src="image" :alt="name" class="w-[85%] h-full object-contain transition-transform duration-500 group-hover:scale-105 mix-blend-multiply contrast-[1.15] brightness-[1.05]" loading="lazy" />
      
      <!-- Hover Actions (Optional, kept subtle) -->
      <div class="absolute inset-x-0 bottom-0 opacity-0 translate-y-4 group-hover:opacity-100 group-hover:translate-y-0 transition-all duration-300">
        <button class="w-full bg-[#d32323] text-white py-2 text-xs font-bold uppercase tracking-wider hover:bg-red-700 transition-colors flex items-center justify-center gap-2">
          <ShoppingCartOutlined /> Thêm Giỏ Hàng
        </button>
      </div>
    </div>

    <!-- Content -->
    <div class="text-center flex-1 flex flex-col justify-between">
      <div>
        <h3 class="text-[14px] font-medium text-[#333] mb-2 line-clamp-2 leading-snug group-hover:text-[#d32323] transition-colors min-h-[42px]">
          <router-link :to="'/products/' + id" class="before:absolute before:inset-0">{{ name }}</router-link>
        </h3>
      </div>
      <div class="flex items-center justify-center gap-2 mt-auto">
        <span class="text-[#d32323] font-bold text-[15px]">{{ formatPrice(price) }}</span>
        <span v-if="oldPrice" class="text-[12px] text-gray-400 line-through">{{ formatPrice(oldPrice) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ShoppingCartOutlined } from '@ant-design/icons-vue'

const props = defineProps({
  id: [Number, String],
  name: String,
  price: [Number, String],
  oldPrice: [Number, String],
  brand: String, // Kept for logic, but hidden in UI to match target
  badge: String,
  image: String
})

const formatPrice = (value) => {
  return `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`
}
</script>
