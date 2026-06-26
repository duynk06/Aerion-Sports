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
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-xl font-bold text-primary-color"><ShoppingCartOutlined /> Đơn Hàng Của Bạn</h3>
            <div v-if="loading" class="text-xs text-gray-500">Đang cập nhật giá...</div>
          </div>

          <div v-if="!loading && hydratedItems.length === 0" class="py-8 text-center text-gray-500">
            Giỏ hàng đang trống.
          </div>

          <template v-else>
            <div class="border-b border-gray-200 pb-5 mb-5 space-y-4">
              <div v-for="item in hydratedItems" :key="item.id" class="flex justify-between items-center text-sm gap-4">
                <span class="font-medium text-gray-800">{{ item.name }} x {{ item.quantity }}</span>
                <span class="font-bold text-right">{{ formatPrice(item.price * item.quantity) }}</span>
              </div>
            </div>

            <div class="flex justify-between mb-4 text-gray-700">
              <span>Tạm tính</span>
              <span class="font-semibold">{{ formatPrice(summary.subtotal) }}</span>
            </div>
            <div class="flex justify-between mb-4 text-gray-700">
              <span>Phí vận chuyển</span>
              <span class="font-semibold text-green-600">Miễn phí</span>
            </div>
            <div class="flex justify-between mt-6 pt-6 border-t-2 border-gray-200 text-lg font-bold">
              <span class="text-primary-color">Tổng cộng</span>
              <span class="text-secondary-color text-2xl">{{ formatPrice(summary.total) }}</span>
            </div>
          </template>

          <button class="btn-primary w-full text-center block mt-8 py-4 text-lg"><CheckCircleOutlined /> ĐẶT HÀNG NGAY</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { UserOutlined, CreditCardOutlined, ShoppingCartOutlined, CheckCircleOutlined } from '@ant-design/icons-vue'
import { getProductDetail, resolveMediaUrl } from '../../services/api'
import fallbackImage from '../../assets/mock_racket.png'
import { useCart } from '../../composables/useCart'
import { useCatalogRealtime } from '../../composables/useCatalogRealtime'

const { cartItems } = useCart()

const checkoutShipping = {
  city: 'Hà Nội',
  district: 'Cầu Giấy',
}

const loading = ref(false)
const hydratedItems = ref([])

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`

const normalizeProduct = (payload, cartItem) => {
  const variants = Array.isArray(payload?.chiTietSanPhams) ? payload.chiTietSanPhams : Array.from(payload?.chiTietSanPhams || [])
  const mappedVariants = variants
    .filter(Boolean)
    .map((variant) => ({
      key: variant?.id ?? `${variant?.maCtsp ?? 'variant'}-${variant?.tenMauSac ?? ''}-${variant?.tenTrongLuong ?? ''}`,
      image: resolveMediaUrl(variant?.hinhAnh) || fallbackImage,
      price: Number(variant?.giaDaGiam ?? variant?.giaBan ?? 0),
      oldPrice: Number(variant?.giaBan ?? 0),
      stock: Number(variant?.soLuong || 0),
      color: variant?.tenMauSac || '',
      weight: variant?.tenTrongLuong || '',
    }))

  const matchedVariant =
    mappedVariants.find((variant) => String(variant.key) === String(cartItem.variantKey)) ||
    mappedVariants[0] ||
    null

  if (!matchedVariant) return null

  return {
    id: cartItem.id,
    productId: payload?.id,
    name: payload?.tenSanPham || `Sản phẩm ${payload?.id ?? ''}`,
    variantLabel: [matchedVariant.color, matchedVariant.weight].filter(Boolean).join(' · ') || matchedVariant.key,
    image: matchedVariant.image,
    price: matchedVariant.price,
    oldPrice: matchedVariant.oldPrice > matchedVariant.price ? matchedVariant.oldPrice : null,
    quantity: Number(cartItem.quantity || 1),
    stock: matchedVariant.stock,
  }
}

const hydrateCart = async () => {
  const shouldShowLoading = hydratedItems.value.length === 0
  if (shouldShowLoading) {
    loading.value = true
  }

  try {
    const items = cartItems.value
    if (!items.length) {
      hydratedItems.value = []
      return
    }

    const uniqueProductIds = [...new Set(items.map((item) => item.productId))]
    const productResponses = await Promise.all(
      uniqueProductIds.map(async (productId) => {
        const response = await getProductDetail(productId)
        return { productId, data: response.data }
      })
    )

    const responseMap = new Map(productResponses.map((entry) => [String(entry.productId), entry.data]))
    hydratedItems.value = items
      .map((cartItem) => normalizeProduct(responseMap.get(String(cartItem.productId)), cartItem))
      .filter(Boolean)
  } catch {
    if (hydratedItems.value.length === 0) {
      hydratedItems.value = []
    }
  } finally {
    loading.value = false
  }
}

const summary = computed(() => {
  const subtotal = hydratedItems.value.reduce((total, item) => total + Number(item.price || 0) * Number(item.quantity || 0), 0)
  return {
    subtotal,
    total: subtotal,
  }
})

watch(cartItems, hydrateCart, { deep: true })
onMounted(hydrateCart)

useCatalogRealtime(hydrateCart)
</script>
