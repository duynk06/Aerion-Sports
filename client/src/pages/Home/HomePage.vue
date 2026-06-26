<template>
  <div class="home">
    <section class="relative w-full aspect-[21/9] max-h-[600px] min-h-[300px] overflow-hidden bg-gray-100 group">
      <div
        v-for="(banner, index) in banners"
        :key="index"
        class="absolute inset-0 transition-opacity duration-1000 ease-in-out"
        :class="{ 'opacity-100 z-10': currentSlide === index, 'opacity-0 z-0': currentSlide !== index }"
      >
        <img :src="banner" alt="Promotion Banner" class="w-full h-full object-cover" />
      </div>

      <button @click="prevSlide" class="absolute left-4 top-1/2 -translate-y-1/2 z-20 w-12 h-12 rounded-full bg-white/30 backdrop-blur-md hover:bg-secondary-color text-white flex items-center justify-center opacity-0 group-hover:opacity-100 transition-all duration-300">
        <LeftOutlined />
      </button>
      <button @click="nextSlide" class="absolute right-4 top-1/2 -translate-y-1/2 z-20 w-12 h-12 rounded-full bg-white/30 backdrop-blur-md hover:bg-secondary-color text-white flex items-center justify-center opacity-0 group-hover:opacity-100 transition-all duration-300">
        <RightOutlined />
      </button>

      <div class="absolute bottom-5 left-1/2 -translate-x-1/2 z-20 flex gap-2">
        <button
          v-for="(_, index) in banners"
          :key="'dot' + index"
          @click="setSlide(index)"
          class="w-8 h-1.5 transition-all duration-300 rounded-full"
          :class="currentSlide === index ? 'bg-secondary-color' : 'bg-white/50 hover:bg-white'"
        ></button>
      </div>
    </section>

    <section class="container mx-auto px-5 py-16">
      <div class="flex justify-between items-end mb-10">
        <div>
          <h2 class="text-3xl font-bold font-montserrat text-primary-color uppercase">Vợt Bán Chạy</h2>
          <div class="h-1 w-20 bg-secondary-color mt-3"></div>
        </div>
        <router-link to="/products" class="hidden sm:flex items-center gap-2 text-sm font-bold uppercase text-gray-500 hover:text-secondary-color transition">
          Xem tất cả <ArrowRightOutlined />
        </router-link>
      </div>

      <div v-if="loadingFeatured" class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-2 md:gap-4 border-t border-l border-gray-100">
        <div v-for="n in 5" :key="n" class="min-h-[320px] bg-white border-r border-b border-gray-100 animate-pulse"></div>
      </div>

      <div v-else-if="featuredProducts.length" class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-2 md:gap-4 border-t border-l border-gray-100">
        <ProductCard
          v-for="product in featuredProducts"
          :key="product.id"
          class="border-r border-b border-gray-100"
          :id="product.id"
          :name="product.name"
          :price="product.price"
          :oldPrice="product.oldPrice"
          :brand="product.brand"
          :badge="product.badge"
          :image="product.image"
        />
      </div>

      <div v-else class="border border-dashed border-gray-200 rounded-xl bg-white p-8 text-center text-gray-500">
        Chưa có dữ liệu sản phẩm bán chạy từ API.
      </div>
    </section>

    <section class="container mx-auto px-5 pb-16">
      <div class="bg-gray-100 rounded-2xl overflow-hidden flex flex-col md:flex-row">
        <div class="p-10 md:p-16 flex-1 flex flex-col justify-center">
          <h3 class="text-3xl font-bold font-montserrat text-primary-color uppercase mb-4">
            {{ featuredProducts[0]?.name || 'Vợt cầu lông nổi bật' }}
          </h3>
          <p class="text-gray-600 mb-8 leading-relaxed">
            {{ featuredProducts[0]?.description || 'Bộ sưu tập sản phẩm nổi bật được đồng bộ từ backend để bạn test giao diện ngay trên local.' }}
          </p>
          <div>
            <router-link :to="featuredProducts[0] ? `/products/${featuredProducts[0].id}` : '/products'" class="inline-block bg-primary-color hover:bg-secondary-color text-white font-bold py-3 px-8 transition-colors uppercase tracking-widest text-sm">
              Khám Phá Ngay
            </router-link>
          </div>
        </div>
        <div class="flex-1 bg-white flex items-center justify-center p-6 min-h-[300px]">
          <img
            :src="featuredProducts[0]?.image || fallbackImage"
            :alt="featuredProducts[0]?.name || 'Vợt bán chạy'"
            class="max-h-[420px] object-contain drop-shadow-2xl"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import ProductCard from '../../components/product/ProductCard.vue'
import { ArrowRightOutlined, LeftOutlined, RightOutlined } from '@ant-design/icons-vue'
import { getProductsPage, resolveMediaUrl } from '../../services/api'
import fallbackImage from '../../assets/mock_racket.png'
import { useCatalogRealtime } from '../../composables/useCatalogRealtime'

import banner1 from '../../assets/banner/banner_1.png'
import banner2 from '../../assets/banner/banner_2.png'
import banner3 from '../../assets/banner/banner_3.png'
import banner4 from '../../assets/banner/banner_4.png'
import banner5 from '../../assets/banner/banner_5.png'
import banner6 from '../../assets/banner/banner_6.png'

const banners = [banner1, banner2, banner3, banner4, banner5, banner6]

const currentSlide = ref(0)
const loadingFeatured = ref(false)
const featuredProducts = ref([])
let slideInterval = null

const normalizeProduct = (product) => {
  const variants = Array.isArray(product?.chiTietSanPhams) ? product.chiTietSanPhams : Array.from(product?.chiTietSanPhams || [])
  const variant = variants[0] || {}
  const originalPrice = Number(variant.giaBan || 0)
  const salePrice = Number(variant.giaDaGiam || originalPrice || 0)
  const hasDiscount = originalPrice > 0 && salePrice > 0 && salePrice < originalPrice
  const image = resolveMediaUrl(variant.hinhAnh) || fallbackImage

  return {
    id: product?.id,
    name: product?.tenSanPham || product?.maSanPham || `Sản phẩm ${product?.id ?? ''}`,
    description: product?.moTa || 'Sản phẩm nổi bật được đồng bộ từ backend.',
    price: hasDiscount ? salePrice : originalPrice,
    oldPrice: hasDiscount ? originalPrice : null,
    brand: product?.tenThuongHieu || '',
    badge: variant?.phanTramGiam && Number(variant.phanTramGiam) > 0 ? `-${Math.round(Number(variant.phanTramGiam))}%` : null,
    image,
  }
}

const fetchFeaturedProducts = async () => {
  const shouldShowLoading = featuredProducts.value.length === 0
  if (shouldShowLoading) {
    loadingFeatured.value = true
  }

  try {
    const response = await getProductsPage({
      page: 0,
      size: 5,
      trangThai: 1,
    })

    const payload = response.data
    const content = Array.isArray(payload) ? payload : payload?.content || []
    featuredProducts.value = content.map(normalizeProduct).slice(0, 5)
  } catch (error) {
    if (featuredProducts.value.length === 0) {
      featuredProducts.value = []
    }
  } finally {
    loadingFeatured.value = false
  }
}

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % banners.length
}

const prevSlide = () => {
  currentSlide.value = currentSlide.value === 0 ? banners.length - 1 : currentSlide.value - 1
}

const setSlide = (index) => {
  currentSlide.value = index
  resetInterval()
}

const resetInterval = () => {
  if (slideInterval) clearInterval(slideInterval)
  slideInterval = setInterval(nextSlide, 6000)
}

onMounted(() => {
  fetchFeaturedProducts()
  slideInterval = setInterval(nextSlide, 6000)
})

onUnmounted(() => {
  if (slideInterval) clearInterval(slideInterval)
})

useCatalogRealtime(fetchFeaturedProducts)
</script>

<style scoped>
</style>
