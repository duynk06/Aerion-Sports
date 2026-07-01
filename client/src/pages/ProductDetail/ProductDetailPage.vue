<template>
  <div class="bg-[#f7f8fb] min-h-screen">
    <div class="container mx-auto px-5 py-8 lg:py-10">
      <div class="mb-6 text-sm text-gray-500 flex items-center gap-2">
        <router-link to="/" class="hover:text-primary-color"><HomeOutlined /></router-link>
        <span>/</span>
        <router-link to="/products" class="hover:text-primary-color">Vợt Cầu Lông</router-link>
        <span>/</span>
        <span class="text-gray-800 font-medium truncate">{{ product?.name || 'Chi tiết sản phẩm' }}</span>
      </div>

      <div v-if="loading" class="grid lg:grid-cols-2 gap-10">
        <div class="bg-white rounded-2xl border border-gray-100 p-5 shadow-sm">
          <div class="aspect-square rounded-xl bg-gray-100 animate-pulse"></div>
          <div class="mt-4 flex gap-3">
            <div v-for="n in 3" :key="n" class="w-20 h-20 rounded-lg bg-gray-100 animate-pulse"></div>
          </div>
        </div>
        <div class="pt-2 space-y-4">
          <div class="h-5 w-28 bg-gray-200 rounded animate-pulse"></div>
          <div class="h-12 w-4/5 bg-gray-200 rounded animate-pulse"></div>
          <div class="h-10 w-48 bg-gray-200 rounded animate-pulse"></div>
          <div class="h-6 w-full bg-gray-200 rounded animate-pulse"></div>
          <div class="h-24 w-full bg-gray-200 rounded-2xl animate-pulse"></div>
        </div>
      </div>

      <div v-else-if="error && !hasProduct" class="rounded-2xl border border-red-200 bg-red-50 px-5 py-4 text-red-700">
        {{ error }}
      </div>

      <template v-else>
        <div v-if="error" class="mb-5 rounded-xl border border-amber-200 bg-amber-50 px-4 py-3 text-amber-800">
          {{ error }}
        </div>

        <div class="grid lg:grid-cols-2 gap-10 xl:gap-14 items-start">
          <section class="bg-white rounded-3xl border border-gray-100 shadow-sm p-4 sm:p-6">
            <div class="rounded-2xl bg-[#fafafa] border border-gray-100 p-4 sm:p-6">
              <div class="aspect-square overflow-hidden flex items-center justify-center">
                <img
                  :src="currentImage"
                  :alt="product.name"
                  class="w-full h-full object-contain drop-shadow-xl transition-transform duration-300"
                />
              </div>
            </div>

            <div class="mt-4 flex gap-3 overflow-x-auto pb-1">
              <button
                v-for="(image, index) in gallery"
                :key="`${image}-${index}`"
                class="w-20 h-20 sm:w-24 sm:h-24 rounded-xl border-2 bg-white flex-shrink-0 p-1 transition-colors"
                :class="selectedImageIndex === index ? 'border-secondary-color' : 'border-gray-200 hover:border-gray-300'"
                @click="selectedImageIndex = index"
              >
                <img :src="image" :alt="`${product.name} ${index + 1}`" class="w-full h-full object-contain" />
              </button>
            </div>
          </section>

          <section class="pt-1 lg:pt-4">
            <div class="text-sm font-semibold uppercase tracking-[0.18em] text-gray-500 mb-3">
              {{ product.brand || 'AERION' }}
            </div>
            <h1 class="text-3xl md:text-4xl font-bold text-primary-color leading-tight mb-4">
              {{ product.name }}
            </h1>

            <div class="flex items-end gap-4 flex-wrap mb-5">
              <div class="text-4xl md:text-5xl font-extrabold text-secondary-color font-montserrat">
                {{ formatPrice(displayPrice) }}
              </div>
              <div v-if="hasDiscount" class="text-lg text-gray-400 line-through pb-1">
                {{ formatPrice(displayOldPrice) }}
              </div>
            </div>

            <!-- Màu sắc -->
            <div v-if="availableColors.length > 0 && availableColors[0] !== ''" class="mb-5">
              <div class="text-sm font-semibold text-gray-800 mb-3">
                Màu sắc: <span class="font-normal text-gray-500">{{ selectedColor }}</span>
              </div>
              <div class="flex flex-wrap gap-3">
                <button
                  v-for="color in availableColors"
                  :key="color"
                  class="px-4 py-2 rounded-xl border transition-colors flex items-center gap-2"
                  :class="selectedColor === color
                    ? 'border-secondary-color bg-secondary-color/5 text-secondary-color'
                    : 'border-gray-200 bg-white hover:border-secondary-color/60'"
                  @click="selectedColor = color"
                >
                  <span class="w-3 h-3 rounded-full bg-gray-800 inline-block" :style="getColorStyle(color)"></span>
                  <span class="font-medium">{{ color }}</span>
                </button>
              </div>
            </div>

            <!-- Kích cỡ / Trọng lượng -->
            <div v-if="availableWeights.length > 0 && availableWeights[0] !== ''" class="mb-5">
              <div class="text-sm font-semibold text-gray-800 mb-3">
                Độ cứng: <span class="font-normal text-gray-500">{{ selectedWeight }}</span>
              </div>
              <div class="flex flex-wrap gap-3">
                <button
                  v-for="weight in availableWeights"
                  :key="weight"
                  class="min-w-[60px] px-4 py-2 rounded-xl border transition-colors text-center"
                  :class="selectedWeight === weight
                    ? 'border-secondary-color bg-secondary-color/5 text-secondary-color'
                    : 'border-gray-200 bg-white hover:border-secondary-color/60'"
                  @click="selectedWeight = weight"
                >
                  <span class="font-medium">{{ weight }}</span>
                </button>
              </div>
            </div>



            <div class="flex flex-col gap-4 mb-6">
              <div class="flex items-center gap-4">
                <div class="text-sm font-semibold text-gray-800 w-20">Số lượng</div>
                <div class="flex items-center gap-4">
                  <div class="flex border border-gray-200 rounded-full overflow-hidden bg-white h-10 w-[130px] shrink-0 shadow-sm">
                    <button class="w-10 h-full text-lg hover:bg-gray-50 text-gray-500 flex items-center justify-center shrink-0 focus:outline-none" @click="decreaseQuantity">-</button>
                    <input
                      v-model="quantity"
                      type="text"
                      @blur="normalizeQuantity"
                      class="flex-1 w-full border-none text-center font-semibold outline-none text-gray-800 bg-transparent min-w-0"
                    />
                    <button class="w-10 h-full text-lg hover:bg-gray-50 text-gray-500 flex items-center justify-center shrink-0 focus:outline-none" @click="increaseQuantity">+</button>
                  </div>
                  <div v-if="currentStock > 0" class="text-sm text-gray-400 font-medium">
                    Còn {{ currentStock }} sản phẩm
                  </div>
                  <div v-else class="text-sm text-red-500 font-medium">
                    Hết hàng
                  </div>
                </div>
              </div>

              <div class="flex flex-row sm:items-center mt-3 gap-3">
                <button 
                  class="h-[48px] flex-1 rounded-xl border border-secondary-color text-secondary-color font-bold px-2 sm:px-4 flex items-center justify-center gap-2 hover:bg-secondary-color/5 transition-colors"
                  @click="addToCart"
                >
                  <ShoppingCartOutlined class="text-[1.1rem]" />
                  Thêm vào giỏ
                </button>
                <button 
                  class="h-[48px] flex-1 rounded-xl bg-gradient-to-r from-secondary-color to-secondary-hover text-white font-bold px-2 sm:px-4 flex items-center justify-center transition-all hover:brightness-110 shadow-[0_8px_16px_rgba(255,107,0,0.3)]"
                  @click="buyNow"
                >
                  Mua ngay
                </button>
              </div>
            </div>


          </section>
        </div>

        <section class="mt-12 lg:mt-16">
          <div class="flex gap-8 border-b border-gray-200">
            <button
              class="pb-4 font-semibold text-lg relative"
              :class="activeTab === 'specs' ? 'text-primary-color' : 'text-gray-500 hover:text-primary-color'"
              :style="activeTab === 'specs' ? { boxShadow: 'inset 0 -3px 0 #ff7a00' } : {}"
              @click="activeTab = 'specs'"
            >
              Thông Số Kỹ Thuật
            </button>
            <button
              class="pb-4 font-semibold text-lg relative"
              :class="activeTab === 'description' ? 'text-primary-color' : 'text-gray-500 hover:text-primary-color'"
              :style="activeTab === 'description' ? { boxShadow: 'inset 0 -3px 0 #ff7a00' } : {}"
              @click="activeTab = 'description'"
            >
              Mô Tả Sản Phẩm
            </button>
          </div>

          <div class="mt-8 bg-white rounded-2xl border border-gray-100 shadow-sm overflow-hidden">
            <table v-if="activeTab === 'specs'" class="w-full border-collapse">
              <tbody>
                <tr v-for="spec in product.specs" :key="spec.label" class="border-b border-gray-100 last:border-b-0">
                  <td class="w-1/3 sm:w-1/4 p-4 sm:p-5 bg-gray-50 font-semibold text-gray-800">
                    {{ spec.label }}
                  </td>
                  <td class="p-4 sm:p-5 text-gray-700">
                    {{ spec.value }}
                  </td>
                </tr>
              </tbody>
            </table>

            <div v-else class="p-6 sm:p-8 text-gray-700 leading-8">
              <p class="whitespace-pre-line">{{ product.description }}</p>
            </div>
          </div>
        </section>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { CheckCircleOutlined, HomeOutlined, ShoppingCartOutlined } from '@ant-design/icons-vue'
import { getProductDetail, resolveMediaUrl } from '../../services/api'
import fallbackImage from '../../assets/mock_racket.png'
import { useToast } from '../../composables/useToast'
import { useCart } from '../../composables/useCart'
import { useCatalogRealtime } from '../../composables/useCatalogRealtime'

const route = useRoute()
const toast = useToast()
const { addItem } = useCart()

const loading = ref(false)
const error = ref('')
const detail = ref(null)
const lastLoadedProductId = ref(null)
const activeTab = ref('specs')
const selectedVariantIndex = ref(0)
const selectedColor = ref('')
const selectedWeight = ref('')
const selectedImageIndex = ref(0)
const quantity = ref('1')

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`

const getColorStyle = (colorName) => {
  const c = String(colorName).toLowerCase().trim();
  if (c.includes('đen') || c.includes('black')) return { backgroundColor: '#000000' }
  if (c.includes('trắng') || c.includes('white')) return { backgroundColor: '#ffffff', border: '1px solid #e5e7eb' }
  if (c.includes('đỏ') || c.includes('red')) return { backgroundColor: '#ef4444' }
  if (c.includes('xanh dương') || c.includes('blue')) return { backgroundColor: '#3b82f6' }
  if (c.includes('xanh lá') || c.includes('green') || c === 'xanh') return { backgroundColor: '#22c55e' }
  if (c.includes('vàng') || c.includes('yellow')) return { backgroundColor: '#eab308' }
  if (c.includes('hồng') || c.includes('pink')) return { backgroundColor: '#ec4899' }
  if (c.includes('tím') || c.includes('purple')) return { backgroundColor: '#a855f7' }
  if (c.includes('cam') || c.includes('orange')) return { backgroundColor: '#f97316' }
  if (c.includes('xám') || c.includes('grey') || c.includes('gray')) return { backgroundColor: '#6b7280' }
  return { backgroundColor: '#9ca3af' }
}

const normalizeApiDetail = (payload) => {
  const variants = Array.isArray(payload?.chiTietSanPhams) ? payload.chiTietSanPhams : Array.from(payload?.chiTietSanPhams || [])
  const mappedVariants = variants
    .filter(Boolean)
    .map((variant) => {
      const salePrice = Number(variant?.giaDaGiam ?? variant?.giaBan ?? 0)
      const basePrice = Number(variant?.giaBan ?? salePrice ?? 0)
      const discount = Number(variant?.phanTramGiam ?? 0)

      return {
        key: variant?.id ?? `${variant?.maCtsp ?? 'variant'}-${variant?.tenMauSac ?? ''}-${variant?.tenTrongLuong ?? ''}`,
        label: variant?.tenTrongLuong || variant?.tenMauSac || variant?.maCtsp || 'Biến thể',
        subLabel: [variant?.tenMauSac, variant?.soLuong ? `${variant.soLuong} sp` : null].filter(Boolean).join(' · ') || 'Biến thể từ API',
        image: resolveMediaUrl(variant?.hinhAnh) || fallbackImage,
        price: salePrice,
        oldPrice: discount > 0 && basePrice > salePrice ? basePrice : null,
        weight: variant?.tenTrongLuong || '',
        color: variant?.tenMauSac || '',
        stock: Number(variant?.soLuong || 0),
      }
    })

  const gallery = [...new Set(mappedVariants.map((variant) => variant.image).filter(Boolean))]
  const primaryVariant = mappedVariants[0] || {
    key: 'default',
    label: 'Mặc định',
    subLabel: 'Chưa có biến thể',
    image: fallbackImage,
    price: 0,
    oldPrice: null,
    weight: '',
    color: '',
  }
  const currentPrice = primaryVariant.oldPrice && primaryVariant.price < primaryVariant.oldPrice
    ? primaryVariant.price
    : Number(primaryVariant.price || 0)
  const currentOldPrice = primaryVariant.oldPrice && primaryVariant.price < primaryVariant.oldPrice
    ? primaryVariant.oldPrice
    : null

  return {
    id: payload?.id,
    brand: payload?.tenThuongHieu || payload?.maSanPham || 'AERION',
    name: payload?.tenSanPham || `Sản phẩm ${payload?.id ?? ''}`,
    description: payload?.moTa || 'Chưa có mô tả sản phẩm từ API.',
    price: currentPrice,
    oldPrice: currentOldPrice,
    category: payload?.tenDanhMuc || 'Vợt Cầu Lông',
    gallery: gallery.length ? gallery : [primaryVariant.image || fallbackImage],
    variants: mappedVariants.length ? mappedVariants : [primaryVariant],
    specs: [
      { label: 'Độ cứng', value: payload?.tenDoCung || 'Chưa cập nhật' },
      { label: 'Cân bằng', value: payload?.tenDiemCanBang || 'Chưa cập nhật' },
      { label: 'Chất liệu thân', value: payload?.tenChatLieuThanVot || 'Chưa cập nhật' },
      { label: 'Chất liệu khung', value: payload?.tenChatLieuKhungVot || 'Chưa cập nhật' },
      { label: 'Danh mục', value: payload?.tenDanhMuc || 'Chưa cập nhật' },
      { label: 'Chu vi cán', value: payload?.tenChuViCanVot || 'Chưa cập nhật' },
      { label: 'Xuất xứ', value: payload?.tenXuatXu || 'Chưa cập nhật' },
    ],
  }
}

const product = computed(() => detail.value || {})
const hasProduct = computed(() => Boolean(detail.value))

const gallery = computed(() => product.value.gallery || [fallbackImage])

const availableColors = computed(() => {
  if (!product.value.variants) return []
  const colors = product.value.variants.map(v => v.color).filter(Boolean)
  return [...new Set(colors)]
})

const availableWeights = computed(() => {
  if (!product.value.variants) return []
  // Only show weights available for the currently selected color if applicable, or just all unique weights
  let filteredVariants = product.value.variants
  if (selectedColor.value) {
    const hasColorMatch = filteredVariants.some(v => v.color === selectedColor.value)
    if (hasColorMatch) {
      filteredVariants = filteredVariants.filter(v => v.color === selectedColor.value)
    }
  }
  const weights = filteredVariants.map(v => v.weight).filter(Boolean)
  return [...new Set(weights)]
})

const currentVariant = computed(() => {
  if (!product.value.variants || product.value.variants.length === 0) return null
  const match = product.value.variants.find(v => v.color === selectedColor.value && v.weight === selectedWeight.value)
  if (match) return match
  const colorMatch = product.value.variants.find(v => v.color === selectedColor.value)
  if (colorMatch) return colorMatch
  return product.value.variants[0]
})

const currentStock = computed(() => {
  return currentVariant.value?.stock || 0
})

const clampQuantity = (value) => {
  const stock = Math.max(0, Number(currentStock.value || 0))
  const maxQuantity = stock
  const minQuantity = stock > 0 ? 1 : 0
  const raw = String(value ?? '').replace(/[^\d]/g, '')

  if (raw === '') {
    return minQuantity
  }

  const parsed = Number.parseInt(raw, 10)

  if (!Number.isFinite(parsed)) {
    return minQuantity
  }

  return Math.min(maxQuantity, Math.max(minQuantity, parsed))
}

const normalizeQuantity = () => {
  quantity.value = String(clampQuantity(quantity.value))
}

watch(currentVariant, (newVal) => {
  if (newVal) {
    if (!selectedColor.value) selectedColor.value = newVal.color
    if (!selectedWeight.value && availableWeights.value.includes(newVal.weight)) selectedWeight.value = newVal.weight
    
    // Update image index based on new variant
    const idx = product.value.gallery?.findIndex(img => img === newVal.image)
    if (idx !== -1 && idx !== undefined) {
      selectedImageIndex.value = idx
    }
  }
})

const currentImage = computed(() => {
  return product.value.gallery?.[selectedImageIndex.value] || currentVariant.value?.image || fallbackImage
})

const displayPrice = computed(() => {
  const variantPrice = Number(currentVariant.value?.price ?? product.value.price ?? 0)
  return variantPrice
})

const displayOldPrice = computed(() => {
  const value = currentVariant.value?.oldPrice ?? product.value.oldPrice
  return value ? Number(value) : null
})

const hasDiscount = computed(() => {
  return Boolean(displayOldPrice.value && Number(displayOldPrice.value) > Number(displayPrice.value))
})

const loadProduct = async (id) => {
  const productId = String(id ?? '')
  const isSameProduct = String(lastLoadedProductId.value ?? '') === productId && Boolean(detail.value)
  const previousColor = selectedColor.value
  const previousWeight = selectedWeight.value
  const shouldShowLoading = !isSameProduct
  if (shouldShowLoading) {
    loading.value = true
  }
  error.value = ''
  const hadDetail = Boolean(detail.value)

  try {
    const response = await getProductDetail(id)
    detail.value = normalizeApiDetail(response.data)
    lastLoadedProductId.value = productId
  } catch (err) {
    if (!hadDetail) {
      detail.value = null
    }
    error.value = err?.response?.data?.message || 'Không tải được chi tiết sản phẩm từ API.'
  } finally {
    const variants = product.value.variants || []

    if (variants.length > 0) {
      const matchingVariant = variants.find((variant) => variant.color === previousColor && variant.weight === previousWeight)
      const nextVariant = matchingVariant || variants[0]

      if (!isSameProduct || !previousColor || !previousWeight || !matchingVariant) {
        selectedColor.value = nextVariant.color || ''
        selectedWeight.value = nextVariant.weight || ''
      }
    }

    if (!isSameProduct) {
      selectedVariantIndex.value = 0
      selectedImageIndex.value = 0
      activeTab.value = 'specs'
    }
    loading.value = false
  }
}

const addToCart = () => {
  if (currentStock.value <= 0) {
    toast.error('Thất bại', 'Sản phẩm này hiện đang hết hàng!')
    return
  }
  const normalizedQuantity = clampQuantity(quantity.value)
  if (!normalizedQuantity || Number(normalizedQuantity) < 1) {
    toast.error('Lỗi', 'Vui lòng chọn số lượng hợp lệ!')
    return
  }

  addItem({
    productId: product.value.id,
    variantKey: currentVariant.value?.key || '',
    quantity: normalizedQuantity,
    selectedColor: selectedColor.value,
    selectedWeight: selectedWeight.value,
  })

  toast.success('Đã thêm vào giỏ', 'Sản phẩm đã được thêm vào giỏ hàng.')
}

const buyNow = () => {
  if (currentStock.value <= 0) {
    toast.error('Thất bại', 'Sản phẩm này hiện đang hết hàng!')
    return
  }
  const normalizedQuantity = clampQuantity(quantity.value)
  if (!normalizedQuantity || Number(normalizedQuantity) < 1) {
    toast.error('Lỗi', 'Vui lòng chọn số lượng hợp lệ!')
    return
  }

  addItem({
    productId: product.value.id,
    variantKey: currentVariant.value?.key || '',
    quantity: normalizedQuantity,
    selectedColor: selectedColor.value,
    selectedWeight: selectedWeight.value,
  })

  toast.success('Đã thêm vào giỏ', 'Sản phẩm đã được thêm vào giỏ hàng.')
  // Ở đây bạn có thể thêm logic router.push('/checkout') để qua trang thanh toán
}

const increaseQuantity = () => {
  quantity.value = String(clampQuantity(Number(quantity.value || 0) + 1))
}

const decreaseQuantity = () => {
  quantity.value = String(clampQuantity(Number(quantity.value || 0) - 1))
}

watch(selectedVariantIndex, () => {
  selectedImageIndex.value = 0
})

watch(currentStock, () => {
  if (String(quantity.value).trim() === '') return
  const nextQuantity = String(clampQuantity(quantity.value))
  if (nextQuantity !== String(quantity.value)) {
    quantity.value = nextQuantity
  }
})

watch(
  () => route.params.id,
  (id) => {
    if (id != null) loadProduct(id)
  }
)

onMounted(() => {
  const id = route.params.id
  if (id != null) {
    loadProduct(id)
  }
})

useCatalogRealtime(() => {
  const id = route.params.id
  if (id != null) {
    loadProduct(id)
  }
})
</script>

