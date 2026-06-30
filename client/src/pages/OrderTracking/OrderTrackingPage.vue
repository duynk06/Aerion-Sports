<template>
  <div class="container mx-auto px-5 py-10 max-w-6xl">
    <h1 class="text-3xl font-extrabold text-primary-color mb-8 text-center uppercase tracking-tight">
      Theo dõi đơn hàng
    </h1>

    <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8 flex flex-col md:flex-row gap-4 items-end">
      <div class="flex-1 w-full">
        <label class="block text-sm font-medium text-gray-700 mb-1">Mã hóa đơn</label>
        <input
          v-model="searchForm.orderId"
          type="text"
          placeholder="VD: HD260630123456789"
          class="w-full px-4 py-2.5 bg-gray-50 border border-gray-200 rounded-lg outline-none focus:bg-white focus:border-secondary-color focus:ring-2 focus:ring-secondary-color/20 transition-all duration-300"
          @keydown.enter.prevent="handleSearch"
        />
      </div>
      <div class="flex-1 w-full">
        <label class="block text-sm font-medium text-gray-700 mb-1">Số điện thoại</label>
        <input
          v-model="searchForm.phone"
          type="text"
          placeholder="Không bắt buộc"
          class="w-full px-4 py-2.5 bg-gray-50 border border-gray-200 rounded-lg outline-none focus:bg-white focus:border-secondary-color focus:ring-2 focus:ring-secondary-color/20 transition-all duration-300"
          @keydown.enter.prevent="handleSearch"
        />
      </div>
      <button
        @click="handleSearch"
        class="w-full md:w-32 bg-primary-color text-white px-6 py-2.5 rounded-lg font-semibold hover:bg-opacity-90 transition-all duration-300 h-[46px] flex items-center justify-center"
      >
        <span v-if="loading" class="animate-spin mr-2">
          <LoadingOutlined />
        </span>
        Tra cứu
      </button>
    </div>

    <div v-if="searched && orderData" class="space-y-6">
      <div class="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
        <div class="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
          <div>
            <h2 class="text-xl font-bold text-primary-color mb-1">Mã đơn: {{ orderData.maHoaDon }}</h2>
            <p class="text-sm text-gray-500">Đặt lúc: {{ formatDateTime(orderData.createdAt) }}</p>
          </div>
          <div class="px-4 py-1.5 bg-blue-50 text-blue-600 rounded-full text-sm font-semibold border border-blue-100">
            {{ orderData.statusName }}
          </div>
        </div>

        <div class="relative max-w-4xl mx-auto mb-4">
          <div class="absolute top-6 left-0 w-full h-1 bg-gray-200 -translate-y-1/2 rounded-full"></div>
          <div
            class="absolute top-6 left-0 h-1 bg-[#f7945d] -translate-y-1/2 rounded-full transition-all duration-500"
            :style="{ width: progressWidth }"
          ></div>

          <div class="relative flex justify-between gap-2">
            <div v-for="(step, index) in orderSteps" :key="step.label" class="flex flex-col items-center flex-1">
              <div
                class="w-12 h-12 rounded-full flex items-center justify-center z-10 transition-colors duration-300 shadow-sm"
                :class="getStepIconClasses(index)"
              >
                <component :is="step.icon" class="text-xl" />
              </div>
              <span
                class="text-sm font-semibold mt-3 text-center"
                :class="index <= progressIndex ? 'text-orange-500' : 'text-gray-400'"
              >
                {{ step.label }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div class="lg:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-gray-100">
          <h3 class="text-lg font-bold text-primary-color mb-6 border-b pb-3">Thông tin đơn hàng</h3>

          <div class="grid grid-cols-2 gap-y-6 gap-x-4 text-sm">
            <div>
              <p class="text-gray-500 mb-1 text-xs uppercase tracking-wider">Khách hàng</p>
              <p class="font-semibold text-gray-800">{{ orderData.customer?.name || 'Khách lẻ' }}</p>
            </div>
            <div>
              <p class="text-gray-500 mb-1 text-xs uppercase tracking-wider">SĐT đặt hàng</p>
              <p class="font-semibold text-gray-800">{{ orderData.customer?.phone || '---' }}</p>
            </div>
            <div>
              <p class="text-gray-500 mb-1 text-xs uppercase tracking-wider">Người nhận</p>
              <p class="font-semibold text-gray-800">{{ orderData.receiver?.name || '---' }}</p>
            </div>
            <div>
              <p class="text-gray-500 mb-1 text-xs uppercase tracking-wider">SĐT người nhận</p>
              <p class="font-semibold text-gray-800">{{ orderData.receiver?.phone || '---' }}</p>
            </div>
            <div class="col-span-2">
              <p class="text-gray-500 mb-1 text-xs uppercase tracking-wider">Địa chỉ giao hàng</p>
              <p class="font-semibold text-gray-800">{{ orderData.receiver?.address || '---' }}</p>
            </div>
            <div>
              <p class="text-gray-500 mb-1 text-xs uppercase tracking-wider">Thanh toán</p>
              <div class="flex items-center gap-2">
                <span class="font-semibold text-gray-800">{{ orderData.paymentMethod }}</span>
                <span
                  class="px-2 py-0.5 rounded text-[10px] font-bold"
                  :class="orderData.isPaid ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700'"
                >
                  {{ orderData.isPaid ? 'Đã thanh toán' : 'Chưa thanh toán' }}
                </span>
              </div>
            </div>
            <div>
              <p class="text-gray-500 mb-1 text-xs uppercase tracking-wider">Ghi chú</p>
              <p class="font-semibold text-gray-800">{{ orderData.note || 'Không có ghi chú' }}</p>
            </div>
          </div>
        </div>

        <div class="bg-gray-50 p-6 rounded-xl shadow-sm border border-gray-100 flex flex-col">
          <h3 class="text-lg font-bold text-primary-color mb-6 border-b border-gray-200 pb-3">Tóm tắt thanh toán</h3>

          <div class="flex justify-between mb-3 text-sm text-gray-600">
            <span>Tiền hàng</span>
            <span class="font-semibold text-gray-800">{{ formatPrice(orderData.summary?.subtotal) }}</span>
          </div>
          <div class="flex justify-between mb-3 text-sm text-gray-600">
            <span>Giảm giá</span>
            <span class="font-semibold text-green-600">- {{ formatPrice(orderData.summary?.discount) }}</span>
          </div>
          <div class="flex justify-between mb-4 text-sm text-gray-600">
            <span>Phí vận chuyển</span>
            <span class="font-semibold text-gray-800">{{ formatPrice(orderData.summary?.shipping) }}</span>
          </div>

          <div class="flex justify-between mt-auto pt-4 border-t border-gray-200 text-base font-bold">
            <span class="text-primary-color">Tổng thanh toán</span>
            <span class="text-secondary-color text-xl">{{ formatPrice(orderData.summary?.total) }}</span>
          </div>
        </div>

        <div class="lg:col-span-2 bg-white p-6 rounded-xl shadow-sm border border-gray-100 mt-2">
          <h3 class="text-lg font-bold text-primary-color mb-4 border-b pb-3">Sản phẩm trong đơn</h3>

          <div class="space-y-4">
            <div
              v-for="item in orderData.items"
              :key="item.id"
              class="flex gap-4 p-3 bg-gray-50 rounded-lg border border-gray-100 items-center"
            >
              <img
                :src="resolveItemImage(item.image)"
                :alt="item.name"
                class="w-16 h-16 object-cover rounded bg-white border border-gray-200 shrink-0"
              />
              <div class="flex-1 min-w-0">
                <h4 class="font-bold text-sm text-gray-800 truncate mb-1">{{ item.name }}</h4>
                <p class="text-xs text-gray-500 mb-1">Phân loại: {{ item.variant || '---' }}</p>
                <p class="text-xs text-gray-500">SL: {{ item.quantity }}</p>
              </div>
              <div class="text-right">
                <p v-if="item.oldPrice" class="text-xs text-gray-400 line-through mb-0.5">{{ formatPrice(item.oldPrice) }}</p>
                <p class="font-bold text-sm text-primary-color">{{ formatPrice(item.price) }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else-if="searched && !loading && !orderData" class="text-center py-12 bg-white rounded-xl border border-gray-100 shadow-sm">
      <p class="text-gray-500 mb-2">Không tìm thấy đơn hàng nào khớp với thông tin đã nhập.</p>
      <p class="text-sm text-gray-400">Vui lòng kiểm tra lại mã đơn hàng hoặc số điện thoại.</p>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { LoadingOutlined, HourglassOutlined, CheckCircleOutlined, InboxOutlined, CarOutlined, HomeOutlined, FlagOutlined } from '@ant-design/icons-vue'
import mockImage from '../../assets/mock_racket.png'
import { getOnlineOrderByCode, resolveMediaUrl } from '../../services/api'
import { useOrderRealtime } from '../../composables/useOrderRealtime'
import { normalizeOnlineOrder } from '../../utils/order'

const route = useRoute()
const searchForm = ref({
  orderId: typeof route.query.code === 'string' ? route.query.code : '',
  phone: '',
})

const loading = ref(false)
const searched = ref(false)
const orderData = ref(null)

const orderSteps = [
  { label: 'Chờ xác nhận', icon: HourglassOutlined },
  { label: 'Đã xác nhận', icon: CheckCircleOutlined },
  { label: 'Chờ giao hàng', icon: InboxOutlined },
  { label: 'Đang giao hàng', icon: CarOutlined },
  { label: 'Đã giao hàng', icon: HomeOutlined },
  { label: 'Đã hoàn thành', icon: FlagOutlined },
]

const progressIndex = computed(() => {
  if (!orderData.value) return 0
  return Math.max(0, Math.min(Number(orderData.value.statusIndex || 0), orderSteps.length - 1))
})

const progressWidth = computed(() => {
  const maxIndex = orderSteps.length - 1
  return `${(progressIndex.value / maxIndex) * 100}%`
})

const getStepIconClasses = (index) => {
  if (index <= progressIndex.value) {
    return 'bg-[#f7945d] text-white shadow-md shadow-orange-200'
  }
  return 'bg-gray-300 text-white'
}

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`

const formatDateTime = (value) => {
  if (!value) return '---'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return String(value)
  return new Intl.DateTimeFormat('vi-VN', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  }).format(date)
}

const resolveItemImage = (path) => resolveMediaUrl(path) || mockImage

const refreshCurrentOrder = async () => {
  const code = searchForm.value.orderId.trim()
  if (!code) return

  try {
    const response = await getOnlineOrderByCode(code)
    orderData.value = normalizeOnlineOrder(response.data)
  } catch {
    // Giữ dữ liệu hiện tại nếu refresh realtime tạm thời lỗi.
  }
}

const handleSearch = async () => {
  const code = searchForm.value.orderId.trim()
  if (!code) return

  loading.value = true
  searched.value = false

  try {
    const response = await getOnlineOrderByCode(code)
    orderData.value = normalizeOnlineOrder(response.data)
  } catch {
    orderData.value = null
  } finally {
    loading.value = false
    searched.value = true
  }
}

useOrderRealtime((payload) => {
  const payloadCode = String(payload?.maHoaDon || '').trim().toUpperCase()
  const currentCode = String(searchForm.value.orderId || '').trim().toUpperCase()

  if (!payloadCode || !currentCode || payloadCode !== currentCode) {
    return
  }

  refreshCurrentOrder()
})

onMounted(() => {
  if (searchForm.value.orderId) {
    handleSearch()
  }
})
</script>
