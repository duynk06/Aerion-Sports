<template>
  <div class="bg-gray-50 min-h-screen py-10">
    <div class="container mx-auto px-5">
      <div class="mb-10 text-sm text-gray-500 flex items-center gap-2">
        <router-link to="/" class="hover:text-primary-color transition-colors">Trang chủ</router-link>
        <span class="text-xs">&gt;</span>
        <span>Tài khoản</span>
        <span class="text-xs">&gt;</span>
        <span class="text-gray-800 font-medium">Đăng nhập</span>
      </div>

      <div class="max-w-md mx-auto bg-white rounded-lg shadow-sm p-8 md:p-10 border border-gray-100">
        <h1 class="text-3xl font-bold text-gray-900 mb-8">Đăng nhập</h1>

        <form @submit.prevent="handleLogin" class="space-y-5">
          <div>
            <input
              v-model="email"
              type="email"
              inputmode="email"
              autocomplete="email"
              placeholder="Email"
              class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3.5 outline-none transition-colors"
              required
            />
          </div>

          <div>
            <input
              v-model="password"
              type="password"
              placeholder="Mật khẩu"
              class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3.5 outline-none transition-colors"
              required
            />
          </div>

          <button
            type="submit"
            :disabled="isLoading"
            class="w-full text-white bg-black hover:bg-gray-800 disabled:bg-gray-400 disabled:cursor-not-allowed focus:ring-4 focus:outline-none focus:ring-gray-300 font-medium rounded text-sm px-5 py-4 text-center uppercase tracking-widest transition-colors mt-2"
          >
            {{ isLoading ? 'Đang xử lý...' : 'Đăng nhập' }}
          </button>
        </form>

        <div class="mt-6 flex items-center text-sm">
          <a href="#" class="text-gray-600 hover:text-black transition-colors">Quên mật khẩu?</a>
          <span class="mx-2 text-gray-400">hoặc</span>
          <router-link to="/register" class="text-gray-600 hover:text-black transition-colors">Đăng ký</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from '../../composables/useToast'
import { loginOnlineCustomer } from '../../services/api'

const router = useRouter()
const toast = useToast()
const email = ref('')
const password = ref('')
const isLoading = ref(false)
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

const handleLogin = async () => {
  if (isLoading.value) return

  const normalizedEmail = email.value.trim().toLowerCase()
  if (!EMAIL_REGEX.test(normalizedEmail)) {
    toast.error('Lỗi', 'Email không đúng định dạng.')
    return
  }

  isLoading.value = true

  try {
    const { data } = await loginOnlineCustomer({
      tenDangNhap: normalizedEmail,
      matKhau: password.value,
    })

    window.localStorage.setItem('aerion_client_token', data.token)
    window.localStorage.setItem('aerion_client_user', JSON.stringify(data.user))
    toast.success('Thành công', 'Đăng nhập thành công.')
    router.push('/')
  } catch (error) {
    toast.error('Đăng nhập thất bại', error?.response?.data?.message || 'Vui lòng thử lại.')
  } finally {
    isLoading.value = false
  }
}
</script>
