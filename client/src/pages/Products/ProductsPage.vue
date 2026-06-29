<template>
  <div class="container mx-auto px-5 py-10">
    <div class="mb-5 text-sm text-gray-500 flex items-center gap-2">
      <router-link to="/" class="hover:text-primary-color"><HomeOutlined /></router-link>
      <span>/</span>
      <span class="text-gray-800">Vợt Cầu Lông</span>
    </div>

    <div class="flex flex-col md:flex-row justify-between md:items-center mb-10 pb-5 border-b-2 border-gray-200 gap-4">
      <div>
        <h1 class="text-3xl m-0 text-primary-color mb-0 font-bold uppercase tracking-wide">Vợt Cầu Lông</h1>
        <p class="text-sm text-gray-500 mt-2">{{ filteredProducts.length }} sản phẩm phù hợp</p>
      </div>
      <div>
        <select
          v-model="sortKey"
          class="px-4 py-2.5 rounded border border-gray-300 outline-none focus:border-secondary-color bg-white"
        >
          <option value="newest">Mới nhất</option>
          <option value="price-asc">Giá tăng dần</option>
          <option value="price-desc">Giá giảm dần</option>
        </select>
      </div>
    </div>

    <div class="flex flex-col lg:flex-row gap-10">
      <aside class="w-full lg:w-64 shrink-0">
        <div class="mb-8">
          <h3 class="text-base mb-4 uppercase tracking-wider text-primary-color font-bold">Thương Hiệu</h3>
          <div class="space-y-2">
            <label v-for="brand in brandOptions" :key="brand.label" class="flex items-center gap-2 cursor-pointer text-[15px]">
              <input v-model="selectedBrands" :value="brand.label" type="checkbox" class="accent-secondary-color w-4 h-4" />
              {{ brand.label }} ({{ brand.count }})
            </label>
          </div>
        </div>

        <div class="mb-8">
          <h3 class="text-base mb-4 uppercase tracking-wider text-primary-color font-bold">Mức Giá</h3>
          <div class="space-y-2">
            <label v-for="price in priceRanges" :key="price.label" class="flex items-center gap-2 cursor-pointer text-[15px]">
              <input v-model="selectedPriceRanges" :value="price.label" type="checkbox" class="accent-secondary-color w-4 h-4" />
              {{ price.label }}
            </label>
          </div>
        </div>

        <div class="mb-8">
          <h3 class="text-base mb-4 uppercase tracking-wider text-primary-color font-bold">Trọng Lượng</h3>
          <div class="space-y-2">
            <label v-for="weight in weightRanges" :key="weight.label" class="flex items-center gap-2 cursor-pointer text-[15px]">
              <input v-model="selectedWeightRanges" :value="weight.label" type="checkbox" class="accent-secondary-color w-4 h-4" />
              {{ weight.label }}
            </label>
          </div>
        </div>
      </aside>

      <main class="flex-1">
        <div v-if="loading" class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
          <div v-for="n in 8" :key="n" class="h-[360px] rounded-lg border border-gray-100 bg-white animate-pulse"></div>
        </div>

        <div v-else-if="error" class="rounded-lg border border-red-200 bg-red-50 text-red-700 px-4 py-3">
          {{ error }}
        </div>

        <template v-else>
          <div v-if="displayProducts.length" class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
            <ProductCard
              v-for="product in displayProducts"
              :key="product.id"
              :id="product.id"
              :name="product.name"
              :price="product.price"
              :oldPrice="product.oldPrice"
              :brand="product.brand"
              :badge="product.badge"
              :image="product.image"
            />
          </div>

          <div v-else class="rounded-lg border border-gray-200 bg-white p-10 text-center text-gray-500">
            Không có sản phẩm phù hợp với bộ lọc hiện tại.
          </div>
        </template>

        <div v-if="totalPages > 1" class="flex justify-center gap-2 mt-12 flex-wrap">
          <button
            class="px-4 h-10 border border-gray-300 bg-white hover:border-secondary-color hover:text-secondary-color rounded flex items-center justify-center transition-colors disabled:opacity-50"
            :disabled="currentPage === 0"
            @click="goToPage(currentPage - 1)"
          >
            <LeftOutlined class="text-xs mr-1" />
            Trước
          </button>

          <button
            v-for="pageNumber in visiblePages"
            :key="pageNumber"
            class="w-10 h-10 border rounded flex items-center justify-center transition-colors"
            :class="pageNumber === currentPage ? 'border-secondary-color bg-secondary-color text-white' : 'border-gray-300 bg-white hover:border-secondary-color hover:text-secondary-color'"
            @click="goToPage(pageNumber)"
          >
            {{ pageNumber + 1 }}
          </button>

          <button
            class="px-4 h-10 border border-gray-300 bg-white hover:border-secondary-color hover:text-secondary-color rounded flex items-center justify-center transition-colors disabled:opacity-50"
            :disabled="currentPage >= totalPages - 1"
            @click="goToPage(currentPage + 1)"
          >
            Tiếp
            <RightOutlined class="text-xs ml-1" />
          </button>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import ProductCard from '../../components/product/ProductCard.vue'
import { HomeOutlined, LeftOutlined, RightOutlined } from '@ant-design/icons-vue'
import { useRoute } from 'vue-router'
import { getProductsPage, resolveMediaUrl } from '../../services/api'
import fallbackImage from '../../assets/mock_racket.png'
import { useCatalogRealtime } from '../../composables/useCatalogRealtime'

const priceRanges = [
  { label: 'Dưới 1 triệu', min: 0, max: 999999 },
  { label: '1 triệu - 2 triệu', min: 1000000, max: 2000000 },
  { label: '2 triệu - 3 triệu', min: 2000001, max: 3000000 },
  { label: 'Trên 3 triệu', min: 3000001, max: Infinity },
]

const weightRanges = [
  { label: '3U (85-89g)', match: ['3u'] },
  { label: '4U (80-84g)', match: ['4u'] },
  { label: '5U (75-79g)', match: ['5u'] },
]

const loading = ref(false)
const error = ref('')
const allProducts = ref([])
const currentPage = ref(0)
const sortKey = ref('newest')
const pageSize = 200
const route = useRoute()
const sanitizeSearchKeyword = (value) =>
  String(value || '')
    .replace(/[^\p{L}\p{N}\s]/gu, ' ')
    .replace(/\s+/g, ' ')
    .trim()

const selectedBrands = ref([])
const selectedPriceRanges = ref([])
const selectedWeightRanges = ref([])

const normalizeProduct = (product) => {
  const variants = Array.isArray(product?.chiTietSanPhams) ? product.chiTietSanPhams : Array.from(product?.chiTietSanPhams || [])
  const variant = variants[0] || {}
  const originalPrice = Number(variant.giaBan || 0)
  const salePrice = Number(variant.giaDaGiam || originalPrice || 0)
  const hasDiscount = originalPrice > 0 && salePrice > 0 && salePrice < originalPrice
  const image = resolveMediaUrl(variant.hinhAnh) || fallbackImage
  const weight = String(variant?.tenTrongLuong || '').trim()

  return {
    id: product?.id,
    name: product?.tenSanPham || product?.maSanPham || `Sản phẩm ${product?.id ?? ''}`,
    price: hasDiscount ? salePrice : originalPrice,
    oldPrice: hasDiscount ? originalPrice : null,
    brand: product?.tenThuongHieu || '',
    badge: variant?.phanTramGiam && Number(variant.phanTramGiam) > 0 ? `-${Math.round(Number(variant.phanTramGiam))}%` : null,
    image,
    weight,
  }
}

const fetchProducts = async () => {
  const shouldShowLoading = allProducts.value.length === 0
  if (shouldShowLoading) {
    loading.value = true
  }
  error.value = ''

  try {
    const response = await getProductsPage({
      page: 0,
      size: pageSize,
      trangThai: 1,
      keyword: sanitizeSearchKeyword(typeof route.query.keyword === 'string' ? route.query.keyword : ''),
    })

    const payload = response.data
    const content = Array.isArray(payload) ? payload : payload?.content || []

    allProducts.value = content.map(normalizeProduct)
  } catch (err) {
    error.value = err?.response?.data?.message || 'Không tải được danh sách sản phẩm từ API.'
    if (allProducts.value.length === 0) {
      allProducts.value = []
    }
  } finally {
    loading.value = false
  }
}

const brandOptions = computed(() => {
  const map = new Map()
  allProducts.value.forEach((product) => {
    const key = product.brand || 'Khác'
    map.set(key, (map.get(key) || 0) + 1)
  })

  return Array.from(map.entries()).map(([label, count]) => ({ label, count }))
})

const matchesPriceRange = (product) => {
  if (!selectedPriceRanges.value.length) return true
  return selectedPriceRanges.value.some((label) => {
    const range = priceRanges.find((item) => item.label === label)
    if (!range) return false
    return Number(product.price || 0) >= range.min && Number(product.price || 0) <= range.max
  })
}

const matchesWeightRange = (product) => {
  if (!selectedWeightRanges.value.length) return true
  const weight = String(product.weight || '').toLowerCase()
  return selectedWeightRanges.value.some((label) => {
    const range = weightRanges.find((item) => item.label === label)
    if (!range) return false
    return range.match.some((token) => weight.includes(token))
  })
}

const filteredProducts = computed(() => {
  let list = [...allProducts.value]

  if (selectedBrands.value.length) {
    list = list.filter((product) => selectedBrands.value.includes(product.brand))
  }

  list = list.filter(matchesPriceRange).filter(matchesWeightRange)

  if (sortKey.value === 'price-asc') {
    list.sort((a, b) => Number(a.price || 0) - Number(b.price || 0))
  } else if (sortKey.value === 'price-desc') {
    list.sort((a, b) => Number(b.price || 0) - Number(a.price || 0))
  }

  return list
})

const pageCount = computed(() => Math.max(1, Math.ceil(filteredProducts.value.length / pageSize)))

const displayProducts = computed(() => {
  const start = currentPage.value * pageSize
  return filteredProducts.value.slice(start, start + pageSize)
})

const totalPages = computed(() => pageCount.value)

const visiblePages = computed(() => {
  const total = totalPages.value
  if (total <= 1) return [0]

  const pages = []
  const start = Math.max(0, currentPage.value - 2)
  const end = Math.min(total - 1, start + 4)
  const adjustedStart = Math.max(0, end - 4)

  for (let index = adjustedStart; index <= end; index += 1) {
    pages.push(index)
  }

  return pages
})

const goToPage = (page) => {
  if (page < 0 || page >= totalPages.value || page === currentPage.value) return
  currentPage.value = page
}

watch([selectedBrands, selectedPriceRanges, selectedWeightRanges, sortKey], () => {
  currentPage.value = 0
})

watch(
  () => route.query.keyword,
  () => {
    currentPage.value = 0
    fetchProducts()
  },
  { immediate: true }
)

useCatalogRealtime(fetchProducts)
</script>
