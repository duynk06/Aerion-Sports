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
              <select class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all bg-white">
                <option>{{ checkoutShipping.city }}</option>
              </select>
            </div>
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Quận/Huyện</label>
              <select class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all bg-white">
                <option>{{ checkoutShipping.district }}</option>
              </select>
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
            <div v-for="item in orderItems" :key="item.id" class="flex justify-between items-center text-sm">
              <span class="font-medium text-gray-800">{{ item.name }} x {{ item.quantity }}</span>
              <span class="font-bold">{{ formatPrice(item.price * item.quantity) }}</span>
            </div>
          </div>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Tạm tính</span>
            <span class="font-semibold">{{ formatPrice(orderTotals.subtotal) }}</span>
          </div>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Phí vận chuyển</span>
            <span class="font-semibold text-green-600">Miễn phí</span>
          </div>
          <div class="flex justify-between mt-6 pt-6 border-t-2 border-gray-200 text-lg font-bold">
            <span class="text-primary-color">Tổng cộng</span>
            <span class="text-secondary-color text-2xl">{{ formatPrice(orderTotals.total) }}</span>
          </div>
          <button class="btn-primary w-full text-center block mt-8 py-4 text-lg"><CheckCircleOutlined /> ĐẶT HÀNG NGAY</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { UserOutlined, CreditCardOutlined, ShoppingCartOutlined, CheckCircleOutlined } from '@ant-design/icons-vue'
import { cartItems as orderItems, cartSummary as orderTotals, checkoutShipping } from '../../data/mockShopData'

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`
</script>
