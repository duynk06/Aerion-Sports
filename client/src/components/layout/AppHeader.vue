<template>
  <header class="w-full bg-white shadow-sm sticky top-0 z-50">
    <div class="container mx-auto px-5 h-20 flex justify-between items-center">
      <router-link to="/" class="flex-shrink-0 z-10">
        <img src="../../assets/logo/logo.jpg" alt="Aerion Sports" class="h-12 w-auto object-contain" />
      </router-link>

      <nav class="hidden lg:flex flex-1 justify-center items-center gap-5 xl:gap-8 mx-4">
        <router-link to="/" class="relative font-bold text-primary-color uppercase text-[14px] xl:text-[14px] tracking-wide hover:text-secondary-color transition group py-2" exact-active-class="text-secondary-color active-nav">
          Trang chủ
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </router-link>
        <router-link to="/products" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2" active-class="text-secondary-color active-nav">
          Sản phẩm
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </router-link>
        <a href="#" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2">
          Giới thiệu
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </a>
        <a href="#" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2">
          Liên hệ
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </a>
        <router-link to="/order-tracking" class="relative font-bold text-primary-color uppercase text-[14px] tracking-wide hover:text-secondary-color transition group py-2" active-class="text-secondary-color active-nav">
          Theo dõi đơn hàng
          <span class="absolute bottom-0 left-1/2 -translate-x-1/2 w-0 h-[2px] bg-secondary-color transition-all duration-300 group-hover:w-full"></span>
        </router-link>
      </nav>

      <div class="flex items-center gap-4 flex-shrink-0 z-10">
        <div class="relative hidden xl:block mr-2">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="Tìm vợt Yonex, Victor..."
            class="w-72 pl-5 pr-12 py-2.5 bg-gray-100 border border-transparent rounded-full text-sm outline-none focus:bg-white focus:border-secondary-color focus:ring-4 focus:ring-secondary-color/10 transition-all duration-300"
            @input="handleSearchInput"
            @keydown.enter.prevent="submitSearch"
          />
          <button
            type="button"
            class="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 transition-colors hover:text-secondary-color"
            @click="submitSearch"
          >
            <SearchOutlined class="text-lg" />
          </button>
        </div>

        <router-link to="/cart" class="relative group mt-1">
          <div class="p-2.5 bg-gray-50 rounded-full group-hover:bg-secondary-color transition-all duration-300">
            <ShoppingCartOutlined class="text-xl text-primary-color group-hover:text-white transition-colors" />
          </div>
          <span v-if="cartCount > 0" class="absolute -top-1 -right-1 bg-secondary-color text-white text-[10px] font-bold h-5 w-5 rounded-full flex items-center justify-center border-2 border-white shadow-sm">
            {{ cartCount }}
          </span>
        </router-link>

        <div class="relative group mt-1">
          <router-link
            :to="isLoggedIn ? '/profile' : '/login'"
            :class="[
              'flex items-center bg-gray-50 group-hover:bg-secondary-color transition-all duration-300',
              isLoggedIn ? 'gap-2 rounded-full px-3 py-2.5' : 'rounded-full p-2.5'
            ]"
          >
            <UserOutlined class="text-xl text-primary-color group-hover:text-white transition-colors" />
            <span
              v-if="isLoggedIn"
              class="hidden md:block max-w-[130px] truncate text-sm font-semibold text-primary-color group-hover:text-white transition-colors leading-none pt-0.5"
            >
              {{ displayName }}
            </span>
          </router-link>

          <div class="absolute right-0 top-full mt-2 w-48 bg-white rounded-md shadow-lg py-1 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 border border-gray-100 z-50">
            <router-link v-if="isLoggedIn" to="/profile" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-secondary-color transition-colors">
              Hồ sơ
            </router-link>
            <router-link v-if="isLoggedIn" to="/my-orders" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-secondary-color transition-colors">
              Đơn hàng của tôi
            </router-link>
            <button
              v-if="isLoggedIn"
              type="button"
              class="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-50 transition-colors border-t border-gray-100"
              @click="handleLogout"
            >
              Đăng xuất
            </button>
            <router-link v-if="!isLoggedIn" to="/login" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-secondary-color transition-colors">
              Đăng nhập
            </router-link>
            <router-link v-if="!isLoggedIn" to="/register" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-secondary-color transition-colors">
              Đăng ký
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { computed, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { SearchOutlined, ShoppingCartOutlined, UserOutlined } from '@ant-design/icons-vue'
import { useRoute, useRouter } from 'vue-router'
import { useCart } from '../../composables/useCart'

const { cartCount } = useCart()
const router = useRouter()
const route = useRoute()
const currentUser = ref(null)
const searchKeyword = ref('')

const isLoggedIn = computed(() => Boolean(currentUser.value))
const displayName = computed(() => {
  const user = currentUser.value || {}
  return user.ten || user.hoTen || user.email || 'Tài khoản'
})

const syncCurrentUser = () => {
  if (typeof window === 'undefined') return

  const token = window.localStorage.getItem('aerion_client_token')
  const rawUser = window.localStorage.getItem('aerion_client_user')

  if (!token || !rawUser) {
    currentUser.value = null
    return
  }

  try {
    currentUser.value = JSON.parse(rawUser)
  } catch (error) {
    window.localStorage.removeItem('aerion_client_user')
    currentUser.value = null
  }
}

const handleLogout = () => {
  window.localStorage.removeItem('aerion_client_token')
  window.localStorage.removeItem('aerion_client_user')
  currentUser.value = null
  router.push('/login')
}

const sanitizeSearchKeyword = (value) =>
  String(value || '')
    .replace(/[^\p{L}\p{N}\s]/gu, ' ')
    .replace(/\s+/g, ' ')
    .trim()

const sanitizeSearchKeywordForTyping = (value) =>
  String(value || '')
    .replace(/[^\p{L}\p{N}\s]/gu, ' ')
    .replace(/^\s+/g, '')
    .replace(/\s{2,}/g, ' ')

const handleSearchInput = () => {
  const sanitized = sanitizeSearchKeywordForTyping(searchKeyword.value)
  if (sanitized !== searchKeyword.value) {
    searchKeyword.value = sanitized
  }
}

const submitSearch = () => {
  const keyword = sanitizeSearchKeyword(searchKeyword.value)
  searchKeyword.value = keyword
  router.push({
    path: '/products',
    query: keyword ? { keyword } : {},
  })
}

onMounted(() => {
  syncCurrentUser()
  window.addEventListener('storage', syncCurrentUser)
})

onBeforeUnmount(() => {
  window.removeEventListener('storage', syncCurrentUser)
})

watch(
  () => route.fullPath,
  () => {
    syncCurrentUser()
    searchKeyword.value = sanitizeSearchKeyword(typeof route.query.keyword === 'string' ? route.query.keyword : '')
  },
  { immediate: true }
)

watch(searchKeyword, (keyword) => {
  if (keyword !== '') return
  if (route.path !== '/products') return
  if (typeof route.query.keyword !== 'string') return

  router.replace({
    path: '/products',
    query: {},
  })
})
</script>

<style scoped>
.active-nav span {
  width: 100% !important;
}
</style>
