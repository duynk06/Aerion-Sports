<template>
  <div class="bg-white min-h-screen py-10">
    <div class="container mx-auto px-5 max-w-6xl">
      <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4">
        <div>
          <h1 class="text-2xl font-bold text-primary-color">Đơn hàng của tôi</h1>
        </div>
        <router-link
          to="/order-tracking"
          class="px-4 py-2 text-sm font-medium border border-gray-300 rounded hover:bg-gray-50 transition-colors text-gray-700"
        >
          Tra cứu đơn hàng công khai
        </router-link>
      </div>

      <div v-if="!isLoggedIn" class="bg-gray-50 border border-gray-200 rounded-xl p-8 text-center">
        <h2 class="text-lg font-semibold text-gray-800 mb-2">Bạn chưa đăng nhập</h2>
        <p class="text-gray-500 mb-4">Đăng nhập để xem các đơn hàng đã đặt bằng tài khoản này.</p>
        <router-link to="/login" class="inline-flex px-5 py-2.5 rounded bg-primary-color text-white font-medium">
          Đi tới đăng nhập
        </router-link>
      </div>

      <div v-else>
        <div class="flex flex-wrap gap-2 mb-8">
          <button
            v-for="tab in tabs"
            :key="tab.id"
            @click="activeTab = tab.id"
            class="px-4 py-1.5 rounded-full text-sm font-medium border transition-colors duration-200"
            :class="activeTab === tab.id ? 'bg-primary-color text-white border-primary-color' : 'bg-white text-gray-700 border-gray-200 hover:border-gray-300'"
          >
            {{ tab.name }}
          </button>
        </div>

        <div v-if="loading" class="bg-gray-50 border border-gray-100 rounded-xl p-8 text-gray-500">
          Đang tải đơn hàng...
        </div>

        <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-xl p-5 text-red-600">
          {{ error }}
        </div>

        <div v-else-if="orders.length === 0" class="bg-gray-50 border border-gray-200 rounded-xl p-8 text-center">
          <h2 class="text-lg font-semibold text-gray-800 mb-2">Chưa có đơn hàng</h2>
          <p class="text-gray-500 mb-4">Khi bạn đặt hàng online, đơn sẽ hiển thị tại đây.</p>
          <router-link to="/products" class="inline-flex px-5 py-2.5 rounded bg-secondary-color text-white font-medium">
            Mua sắm ngay
          </router-link>
        </div>

        <div v-else class="flex flex-col lg:flex-row gap-6">
          <div class="w-full lg:w-1/3 space-y-4 max-h-[800px] overflow-y-auto pr-2 custom-scrollbar">
            <div
              v-for="order in filteredOrders"
              :key="order.id"
              @click="selectedOrderId = order.id"
              class="bg-white p-4 rounded-xl border-2 cursor-pointer transition-all duration-200 relative"
              :class="selectedOrderId === order.id ? 'border-secondary-color shadow-md' : 'border-gray-100 hover:border-gray-200 shadow-sm'"
            >
              <div class="flex justify-between items-start mb-2 gap-3">
                <div>
                  <h3 class="font-bold text-gray-900">{{ order.maHoaDon }}</h3>
                  <p class="text-xs text-gray-500">{{ formatDateTime(order.createdAt) }}</p>
                </div>
                <span class="px-2.5 py-1 rounded-full text-xs font-semibold" :class="getStatusBadgeClass(order.statusIndex)">
                  {{ getStatusLabel(order.statusIndex) }}
                </span>
              </div>

              <div class="space-y-1.5 mt-4 text-sm">
                <div class="flex gap-2">
                  <span class="text-gray-500 w-24">Người nhận:</span>
                  <span class="font-medium text-gray-800 truncate">{{ order.receiver?.name || '-' }}</span>
                </div>
                <div class="flex gap-2">
                  <span class="text-gray-500 w-24">SĐT nhận:</span>
                  <span class="font-medium text-gray-800">{{ order.receiver?.phone || '-' }}</span>
                </div>
                <div class="flex gap-2">
                  <span class="text-gray-500 w-24">Sản phẩm:</span>
                  <span class="font-medium text-gray-800">{{ order.items?.length || 0 }}</span>
                </div>
                <div class="flex gap-2 items-center">
                  <span class="text-gray-500 w-24">Thanh toán:</span>
                  <span class="font-medium text-gray-800">{{ order.paymentMethod || '-' }}</span>
                  <span
                    class="px-2 py-0.5 rounded text-[10px] font-bold ml-1"
                    :class="order.isPaid ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-600'"
                  >
                    {{ order.isPaid ? 'Đã thanh toán' : 'Chưa thanh toán' }}
                  </span>
                </div>
              </div>

              <div class="mt-4 pt-3 border-t border-gray-100 flex justify-between items-center">
                <span class="font-bold text-secondary-color text-base">{{ formatPrice(order.summary?.total) }}</span>
              </div>
            </div>
          </div>

          <div class="w-full lg:w-2/3">
            <div v-if="selectedOrder" class="bg-gray-50 rounded-xl p-6 border border-gray-100">
              <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-6 gap-4 border-b border-gray-200 pb-4">
                <div>
                  <h2 class="text-lg font-bold text-gray-900 mb-1">Mã đơn: {{ selectedOrder.maHoaDon }}</h2>
                  <p class="text-sm text-gray-500">Đặt lúc: {{ formatDateTime(selectedOrder.createdAt) }}</p>
                </div>
                <div class="flex flex-col items-end gap-2">
                  <span class="px-3 py-1 rounded-full text-xs font-semibold" :class="getStatusBadgeClass(selectedOrder.statusIndex)">
                    {{ getStatusLabel(selectedOrder.statusIndex) }}
                  </span>
                </div>
              </div>

              <div class="space-y-6">
                <div class="bg-white p-5 rounded-lg border border-gray-100 shadow-sm">
                  <h3 class="font-bold text-gray-900 mb-4 text-sm">Thông tin giao hàng</h3>
                  <div class="grid grid-cols-1 md:grid-cols-2 gap-y-4 gap-x-4 text-sm">
                    <div class="flex gap-2">
                      <span class="text-gray-500 w-24 shrink-0">Khách hàng:</span>
                      <span class="font-medium text-gray-800">{{ selectedOrder.customer?.name || '-' }}</span>
                    </div>
                    <div class="flex gap-2">
                      <span class="text-gray-500 w-20 shrink-0">SĐT đặt:</span>
                      <span class="font-medium text-gray-800">{{ selectedOrder.customer?.phone || '-' }}</span>
                    </div>
                    <div class="flex gap-2">
                      <span class="text-gray-500 w-24 shrink-0">Người nhận:</span>
                      <span class="font-medium text-gray-800">{{ selectedOrder.receiver?.name || '-' }}</span>
                    </div>
                    <div class="flex gap-2">
                      <span class="text-gray-500 w-20 shrink-0">SĐT nhận:</span>
                      <span class="font-medium text-gray-800">{{ selectedOrder.receiver?.phone || '-' }}</span>
                    </div>
                    <div class="flex gap-2 md:col-span-2">
                      <span class="text-gray-500 w-24 shrink-0">Địa chỉ:</span>
                      <span class="font-medium text-gray-800">{{ selectedOrder.receiver?.address || '-' }}</span>
                    </div>
                    <div class="flex gap-2 md:col-span-2">
                      <span class="text-gray-500 w-24 shrink-0">Ghi chú:</span>
                      <span class="font-medium text-gray-800">{{ selectedOrder.note || 'Không có ghi chú' }}</span>
                    </div>
                  </div>
                </div>

                <div class="bg-white p-5 rounded-lg border border-gray-100 shadow-sm">
                  <h3 class="font-bold text-gray-900 mb-4 text-sm">Sản phẩm</h3>
                  <div class="space-y-3">
                    <div
                      v-for="item in selectedOrder.items"
                      :key="item.id"
                      class="flex gap-4 items-center"
                    >
                      <img
                        :src="item.image || fallbackImage"
                        :alt="item.name"
                        class="w-14 h-14 object-cover rounded bg-gray-50 border border-gray-200 shrink-0"
                      />
                      <div class="flex-1 min-w-0 text-sm">
                        <h4 class="font-bold text-gray-900 truncate">{{ item.name }}</h4>
                        <p class="text-xs text-gray-500 mt-0.5">Phân loại: {{ item.variant || '---' }}</p>
                        <p class="text-xs text-gray-500 mt-0.5">Số lượng: {{ item.quantity }}</p>
                      </div>
                      <div class="text-right">
                        <p class="font-bold text-sm text-gray-900">{{ formatPrice(item.price) }}</p>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="bg-white p-5 rounded-lg border border-gray-100 shadow-sm">
                  <h3 class="font-bold text-gray-900 mb-4 text-sm">Thanh toán</h3>
                  <div class="space-y-3 text-sm">
                    <div class="flex justify-between text-gray-600">
                      <span>Tiền hàng</span>
                      <span class="font-semibold text-gray-900">{{ formatPrice(selectedOrder.summary?.subtotal) }}</span>
                    </div>
                    <div class="flex justify-between text-gray-600">
                      <span>Giảm giá</span>
                      <span class="font-semibold text-gray-900">- {{ formatPrice(selectedOrder.summary?.discount) }}</span>
                    </div>
                    <div class="flex justify-between text-gray-600">
                      <span>Phí vận chuyển</span>
                      <span class="font-semibold text-gray-900">{{ formatPrice(selectedOrder.summary?.shipping) }}</span>
                    </div>
                    <div class="flex justify-between text-gray-600">
                      <span>Phương thức</span>
                      <div class="flex items-center gap-2">
                        <span class="font-semibold text-gray-900">{{ selectedOrder.paymentMethod || '-' }}</span>
                        <span
                          class="px-2 py-0.5 rounded text-[10px] font-bold"
                          :class="selectedOrder.isPaid ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-600'"
                        >
                          {{ selectedOrder.isPaid ? 'Đã thanh toán' : 'Chưa thanh toán' }}
                        </span>
                      </div>
                    </div>
                    <div class="flex justify-between mt-4 pt-4 border-t border-gray-100 text-base font-bold">
                      <span class="text-secondary-color">Tổng thanh toán</span>
                      <span class="text-secondary-color">{{ formatPrice(selectedOrder.summary?.total) }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div v-else class="h-full min-h-[400px] flex items-center justify-center bg-gray-50 rounded-xl border border-gray-100 text-gray-400">
              Chọn một đơn hàng để xem chi tiết
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import mockImage from '../../assets/mock_racket.png'
import { getMyOnlineOrders } from '../../services/api'
import { useOrderRealtime } from '../../composables/useOrderRealtime'
import { normalizeOnlineOrder } from '../../utils/order'

const fallbackImage = mockImage
const loading = ref(false)
const error = ref('')
const orders = ref([])
const selectedOrderId = ref('')
const tabs = [
  { id: 'all', name: 'Tất cả' },
  { id: 0, name: 'Chờ xác nhận' },
  { id: 1, name: 'Đã xác nhận' },
  { id: 2, name: 'Chờ giao hàng' },
  { id: 3, name: 'Đang giao hàng' },
  { id: 4, name: 'Đã giao hàng' },
  { id: 5, name: 'Đã hoàn thành' },
  { id: 6, name: 'Đã hủy' },
]
const activeTab = ref('all')

const isLoggedIn = computed(() => {
  if (typeof window === 'undefined') return false
  return Boolean(window.localStorage.getItem('aerion_client_token'))
})

const statusLabels = {
  0: 'Chờ xác nhận',
  1: 'Đã xác nhận',
  2: 'Chờ giao hàng',
  3: 'Đang giao hàng',
  4: 'Đã giao hàng',
  5: 'Đã hoàn thành',
  6: 'Đã hủy',
  7: 'Yêu cầu hủy',
  8: 'Đã hoàn tiền',
}

const getStatusLabel = (statusIndex) => statusLabels[statusIndex] || 'Khởi tạo'

const getStatusBadgeClass = (statusIndex) => {
  switch (Number(statusIndex)) {
    case 0: return 'bg-blue-50 text-blue-600'
    case 1: return 'bg-indigo-50 text-indigo-600'
    case 2: return 'bg-amber-50 text-amber-600'
    case 3: return 'bg-orange-50 text-orange-600'
    case 4: return 'bg-teal-50 text-teal-600'
    case 5: return 'bg-green-50 text-green-600'
    case 6: return 'bg-red-50 text-red-600'
    default: return 'bg-gray-100 text-gray-600'
  }
}

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`

const formatDateTime = (value) => {
  if (!value) return '-'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return new Intl.DateTimeFormat('vi-VN', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(date).replace(',', '')
}

const normalizeOrders = (responseData) => {
  if (Array.isArray(responseData)) return responseData
  if (Array.isArray(responseData?.data)) return responseData.data
  return []
}

const loadOrders = async () => {
  if (!isLoggedIn.value) return
  loading.value = true
  error.value = ''
  try {
    const response = await getMyOnlineOrders()
    const data = normalizeOrders(response?.data)
    orders.value = data.map(normalizeOnlineOrder)
    selectedOrderId.value = data[0]?.id ?? ''
  } catch (err) {
    const message = err?.response?.data?.message || err?.response?.data || err?.message || 'Không thể tải đơn hàng.'
    error.value = String(message)
    orders.value = []
    selectedOrderId.value = ''
  } finally {
    loading.value = false
  }
}

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') return orders.value
  return orders.value.filter(order => Number(order.statusIndex) === Number(activeTab.value))
})

const selectedOrder = computed(() => {
  return filteredOrders.value.find(order => order.id === selectedOrderId.value) || filteredOrders.value[0] || null
})

onMounted(() => {
  if (isLoggedIn.value) {
    loadOrders()
  }
})

useOrderRealtime((payload) => {
  if (!isLoggedIn.value) return
  if (payload?.type !== 'order-update') return

  loadOrders()
})

watch(activeTab, () => {
  selectedOrderId.value = filteredOrders.value[0]?.id ?? ''
})
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #e5e7eb;
  border-radius: 10px;
}
.custom-scrollbar:hover::-webkit-scrollbar-thumb {
  background-color: #d1d5db;
}
</style>
