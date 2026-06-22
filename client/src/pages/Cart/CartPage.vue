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

        <div v-for="item in cartItems" :key="item.id" class="flex items-center py-6 border-b border-gray-200">
          <div class="flex-[2] flex gap-5">
            <img :src="item.image" :alt="item.name" class="w-24 h-24 object-cover rounded border border-gray-200" />
            <div>
              <h3 class="text-lg font-bold mb-1">{{ item.name }}</h3>
              <p class="text-gray-500 text-sm mb-3">{{ item.variant }}</p>
              <button class="text-red-500 underline text-sm hover:text-red-700 flex items-center gap-1"><DeleteOutlined /> Xóa</button>
            </div>
          </div>
          <div class="flex-1 text-center font-semibold text-gray-800">{{ formatPrice(item.price) }}</div>
          <div class="flex-1 flex justify-center">
            <div class="flex border border-gray-300 rounded overflow-hidden">
              <button class="w-8 h-9 bg-white hover:bg-gray-50">-</button>
              <input type="text" :value="item.quantity" class="w-10 h-9 text-center border-x border-gray-300 outline-none" />
              <button class="w-8 h-9 bg-white hover:bg-gray-50">+</button>
            </div>
          </div>
          <div class="flex-1 text-right font-bold text-secondary-color">{{ formatPrice(item.price * item.quantity) }}</div>
        </div>
      </div>

      <div class="flex-1">
        <div class="bg-gray-50 p-8 rounded-xl sticky top-24">
          <h3 class="text-xl font-bold mb-6 text-primary-color">Tóm Tắt Đơn Hàng</h3>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Tạm tính</span>
            <span class="font-semibold">{{ formatPrice(cartSummary.subtotal) }}</span>
          </div>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Khuyến mãi</span>
            <span class="font-semibold text-green-600">- {{ formatPrice(cartSummary.discount) }}</span>
          </div>
          <div class="flex justify-between mt-6 pt-6 border-t-2 border-gray-200 text-lg font-bold">
            <span class="text-primary-color">Tổng cộng</span>
            <span class="text-secondary-color text-2xl">{{ formatPrice(cartSummary.total) }}</span>
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
import { cartItems, cartSummary } from '../../data/mockShopData'

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`
</script>
