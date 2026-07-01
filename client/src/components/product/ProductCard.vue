<template>
  <div class="bg-white group relative flex flex-col h-full border border-transparent hover:border-gray-100 transition-colors p-2">
    <div v-if="badge" class="absolute top-2 left-2 z-10">
      <span class="bg-secondary-color text-white text-[10px] font-bold w-9 h-9 flex items-center justify-center rounded-full border-2 border-white shadow-sm">
        {{ badge }}
      </span>
    </div>

    <div class="relative w-full aspect-[3/4] overflow-hidden flex items-center justify-center mb-4 bg-white">
      <img
        :src="image"
        :alt="name"
        class="w-[85%] h-full object-contain transition-transform duration-500 group-hover:scale-105 mix-blend-multiply contrast-[1.15] brightness-[1.05]"
        loading="lazy"
      />

      <div class="quick-add-wrap absolute inset-x-0 bottom-0 opacity-0 translate-y-4 group-hover:opacity-100 group-hover:translate-y-0 transition-all duration-300">
        <button
          type="button"
          class="w-full bg-secondary-color text-white py-2 text-[11px] font-bold uppercase tracking-wider hover:brightness-110 transition-all flex items-center justify-center gap-1.5"
          @click.stop="handleQuickAdd"
        >
          <ShoppingCartOutlined /> Thêm Giỏ Hàng
        </button>
      </div>
    </div>

    <div class="text-center flex-1 flex flex-col justify-between mt-1">
      <div>
        <h3 class="text-[13px] font-medium text-[#333] mb-1.5 line-clamp-2 leading-snug group-hover:text-secondary-color transition-colors min-h-[38px]">
          <router-link :to="'/products/' + id" class="before:absolute before:inset-0">{{ name }}</router-link>
        </h3>
      </div>
      <div class="flex items-center justify-center gap-1.5 mt-auto">
        <span class="text-[#d32323] font-bold text-[14px]">{{ formatPrice(price) }}</span>
        <span v-if="oldPrice" class="text-[11px] text-gray-400 line-through">{{ formatPrice(oldPrice) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ShoppingCartOutlined } from '@ant-design/icons-vue'
import { useToast } from '../../composables/useToast'

defineProps({
  id: [Number, String],
  name: String,
  price: [Number, String],
  oldPrice: [Number, String],
  brand: String,
  badge: String,
  image: String,
})

const toast = useToast()

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`

const handleQuickAdd = () => {
  toast.info('Chọn sản phẩm', 'Vui lòng mở trang chi tiết để chọn biến thể rồi thêm vào giỏ hàng.')
}
</script>

<style scoped>
@media (hover: none) {
  .quick-add-wrap {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
