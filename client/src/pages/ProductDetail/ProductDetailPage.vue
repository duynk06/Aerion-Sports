<template>
  <div class="container mx-auto px-5 py-10">
    <div class="mb-8 text-sm text-gray-500 flex items-center gap-2">
      <router-link to="/" class="hover:text-primary-color"><HomeOutlined /></router-link>
      <span>/</span>
      <router-link to="/products" class="hover:text-primary-color">Vợt Cầu Lông</router-link>
      <span>/</span>
      <span class="text-gray-800 font-medium">{{ product.name }}</span>
    </div>

    <div class="flex flex-col lg:flex-row gap-16 mb-16">
      <div class="flex-1">
        <div class="bg-white rounded-lg border border-gray-200 p-5 mb-4 aspect-square flex items-center justify-center">
          <img :src="product.image" :alt="product.name" class="w-full h-full object-contain" />
        </div>
        <div class="flex gap-4 overflow-x-auto pb-1">
          <button
            v-for="(thumb, idx) in gallery"
            :key="idx"
            class="w-20 h-20 border rounded p-1 cursor-pointer bg-white hover:border-secondary-color shrink-0"
            :class="idx === 0 ? 'border-secondary-color border-2' : 'border-gray-200'"
          >
            <img :src="thumb" :alt="product.name" class="w-full h-full object-contain" />
          </button>
        </div>
      </div>

      <div class="flex-1">
        <div class="text-sm text-gray-500 uppercase tracking-widest mb-2 font-semibold">{{ product.brand }}</div>
        <h1 class="text-3xl lg:text-4xl leading-tight mb-5 text-primary-color">{{ product.name }}</h1>
        <div class="text-4xl font-bold text-secondary-color font-montserrat mb-3">{{ formatPrice(product.price) }}</div>
        <div v-if="product.oldPrice" class="text-gray-400 line-through mb-8">{{ formatPrice(product.oldPrice) }}</div>
        
        <div class="mb-6">
          <h4 class="text-sm text-gray-800 mb-3 font-semibold">Trọng lượng / Kích thước cán:</h4>
          <div class="flex flex-wrap gap-3">
            <button class="px-5 py-2.5 border-2 border-secondary-color text-secondary-color bg-secondary-color/5 rounded font-medium transition-colors">
              {{ product.specs['Trọng lượng'] }}
            </button>
            <button class="px-5 py-2.5 border border-gray-300 rounded font-medium hover:border-secondary-color hover:text-secondary-color transition-colors">
              {{ product.category }}
            </button>
          </div>
        </div>
        
        <div class="mb-8">
          <h4 class="text-sm text-gray-800 mb-3 font-semibold">Mô tả nhanh:</h4>
          <p class="text-gray-600 leading-relaxed">{{ product.description }}</p>
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
          <p class="font-medium flex items-center gap-2"><CheckCircleOutlined class="text-secondary-color" /> Hàng mock để test UI</p>
          <p class="font-medium flex items-center gap-2"><CheckCircleOutlined class="text-secondary-color" /> Ảnh local, không phụ thuộc mạng</p>
          <p class="font-medium flex items-center gap-2"><CheckCircleOutlined class="text-secondary-color" /> Dữ liệu đồng bộ với trang danh sách</p>
          <p class="font-medium flex items-center gap-2"><CheckCircleOutlined class="text-secondary-color" /> Có thể thay bằng API thật sau này</p>
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
            <tr v-for="(value, key) in product.specs" :key="key" class="border-b border-gray-200">
              <td class="p-4 font-semibold w-1/3 bg-gray-50">{{ key }}</td>
              <td class="p-4">{{ value }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { HomeOutlined, ShoppingCartOutlined, CheckCircleOutlined } from '@ant-design/icons-vue'
import { getProductById, mockProducts } from '../../data/mockShopData'

const route = useRoute()

const product = computed(() => {
  return getProductById(route.params.id) || mockProducts[0]
})

const gallery = computed(() => [product.value.image, product.value.image, product.value.image])

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`
</script>
