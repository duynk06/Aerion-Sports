<template>
  <div class="container mx-auto px-5 py-10">
    <div class="flex flex-col lg:flex-row gap-10">
      <div class="flex-[2]">
        <div class="flex items-end justify-between mb-10">
          <h1 class="text-3xl md:text-4xl font-extrabold text-primary-color uppercase tracking-tight">Giỏ Hàng Của Bạn</h1>
          <div v-if="loading" class="text-sm text-gray-500">Đang tải dữ liệu giỏ hàng...</div>
        </div>

        <div class="flex pb-4 border-b-2 border-gray-200 font-semibold uppercase text-sm text-gray-700">
          <div class="flex-[2]">Sản phẩm</div>
          <div class="flex-1 text-center">Đơn giá</div>
          <div class="flex-1 text-center">Số lượng</div>
          <div class="flex-1 text-right">Thành tiền</div>
        </div>

        <div v-if="!loading && hydratedItems.length === 0" class="py-16 text-center text-gray-500">
          Chưa có sản phẩm nào trong giỏ hàng.
          <div class="mt-4">
            <router-link to="/products" class="text-secondary-color font-semibold hover:underline">Quay lại mua sắm</router-link>
          </div>
        </div>

        <div
          v-for="item in hydratedItems"
          :key="item.id"
          class="flex items-center py-6 border-b border-gray-200 gap-4"
        >
          <div class="flex-[2] flex gap-5 min-w-0">
            <img :src="item.image" :alt="item.name" class="w-24 h-24 object-cover rounded border border-gray-200 bg-white shrink-0" />
            <div class="min-w-0">
              <h3 class="text-lg font-bold mb-1 truncate">{{ item.name }}</h3>
              <p class="text-gray-500 text-sm mb-3 truncate">{{ item.variantLabel }}</p>
              <button class="text-red-500 underline text-sm hover:text-red-700 flex items-center gap-1" @click="removeItem(item.id)">
                <DeleteOutlined /> Xóa
              </button>
            </div>
          </div>

          <div class="flex-1 text-center font-semibold text-gray-800">{{ formatPrice(item.price) }}</div>

          <div class="flex-1 flex justify-center">
            <div class="flex border border-gray-300 rounded overflow-hidden">
              <button class="w-8 h-9 bg-white hover:bg-gray-50" @click="decreaseQty(item)">-</button>
              <input
                :value="item.quantity"
                type="text"
                class="w-10 h-9 text-center border-x border-gray-300 outline-none"
                @blur="updateQtyFromInput(item, $event.target.value)"
                @keydown.enter.prevent="updateQtyFromInput(item, $event.target.value)"
              />
              <button class="w-8 h-9 bg-white hover:bg-gray-50" @click="increaseQty(item)">+</button>
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
            <span class="font-semibold">{{ formatPrice(summary.subtotal) }}</span>
          </div>
          <div class="flex justify-between mb-4 text-gray-700">
            <span>Khuyến mãi</span>
            <span class="font-semibold text-green-600">- {{ formatPrice(summary.discount) }}</span>
          </div>
          <div class="flex justify-between mt-6 pt-6 border-t-2 border-gray-200 text-lg font-bold">
            <span class="text-primary-color">Tổng cộng</span>
            <span class="text-secondary-color text-2xl">{{ formatPrice(summary.total) }}</span>
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
import { computed, onMounted, ref, watch } from 'vue'
import { DeleteOutlined, ArrowLeftOutlined } from '@ant-design/icons-vue'
import { getProductDetail, resolveMediaUrl } from '../../services/api'
import fallbackImage from '../../assets/mock_racket.png'
import { useCart } from '../../composables/useCart'
import { useCatalogRealtime } from '../../composables/useCatalogRealtime'

const { cartItems, updateQuantity, removeItem } = useCart()

const loading = ref(false)
const hydratedItems = ref([])

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`

const normalizeProduct = (payload, cartItem) => {
  const variants = Array.isArray(payload?.chiTietSanPhams) ? payload.chiTietSanPhams : Array.from(payload?.chiTietSanPhams || [])
  const mappedVariants = variants
    .filter(Boolean)
    .map((variant) => ({
      key: variant?.id ?? `${variant?.maCtsp ?? 'variant'}-${variant?.tenMauSac ?? ''}-${variant?.tenTrongLuong ?? ''}`,
      name: payload?.tenSanPham || `Sản phẩm ${payload?.id ?? ''}`,
      brand: payload?.tenThuongHieu || '',
      image: resolveMediaUrl(variant?.hinhAnh) || fallbackImage,
      price: Number(variant?.giaDaGiam ?? variant?.giaBan ?? 0),
      oldPrice: Number(variant?.giaBan ?? 0),
      stock: Number(variant?.soLuong || 0),
      color: variant?.tenMauSac || '',
      weight: variant?.tenTrongLuong || '',
    }))

  const matchedVariant =
    mappedVariants.find((variant) => String(variant.key) === String(cartItem.variantKey)) ||
    mappedVariants.find((variant) => variant.color === cartItem.selectedColor && variant.weight === cartItem.selectedWeight) ||
    mappedVariants[0] ||
    null

  if (!matchedVariant) {
    return null
  }

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
  const discount = hydratedItems.value.reduce((total, item) => {
    const base = Number(item.oldPrice || 0)
    const sale = Number(item.price || 0)
    if (base > sale) return total + (base - sale) * Number(item.quantity || 0)
    return total
  }, 0)

  return {
    subtotal,
    discount,
    total: Math.max(0, subtotal - discount),
  }
})

const clampQty = (value, stock) => {
  const max = Math.max(0, Number(stock || 0))
  const parsed = Number.parseInt(String(value ?? '').replace(/[^\d]/g, ''), 10)
  if (!Number.isFinite(parsed)) return max > 0 ? 1 : 0
  return Math.min(max, Math.max(max > 0 ? 1 : 0, parsed))
}

const increaseQty = (item) => {
  const next = clampQty(Number(item.quantity || 0) + 1, item.stock)
  updateQuantity(item.id, next)
  hydrateCart()
}

const decreaseQty = (item) => {
  const next = clampQty(Number(item.quantity || 0) - 1, item.stock)
  updateQuantity(item.id, next)
  hydrateCart()
}

const updateQtyFromInput = (item, value) => {
  const next = clampQty(value, item.stock)
  updateQuantity(item.id, next)
  hydrateCart()
}

watch(cartItems, hydrateCart, { deep: true })

onMounted(hydrateCart)

useCatalogRealtime(hydrateCart)
</script>
