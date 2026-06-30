<template>
  <div class="bg-gray-50 min-h-screen py-10">
    <div class="container mx-auto px-5">
      <div class="mb-10 text-sm text-gray-500 flex items-center gap-2">
        <router-link to="/" class="hover:text-primary-color transition-colors">Trang chủ</router-link>
        <span class="text-xs">&gt;</span>
        <span>Tài khoản</span>
        <span class="text-xs">&gt;</span>
        <span class="text-gray-800 font-medium">Đăng ký</span>
      </div>

      <div class="max-w-md mx-auto bg-white rounded-lg shadow-sm p-8 md:p-10 border border-gray-100">
        <h1 class="text-3xl font-bold text-gray-900 mb-8">Tạo tài khoản</h1>

        <form @submit.prevent="handleRegister" class="space-y-5">
          <div>
            <input
              v-model="lastName"
              type="text"
              placeholder="Họ"
              class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3.5 outline-none transition-colors"
              required
            />
          </div>

          <div>
            <input
              v-model="firstName"
              type="text"
              placeholder="Tên"
              class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3.5 outline-none transition-colors"
              required
            />
          </div>

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
              minlength="6"
              required
            />
          </div>

          <p v-if="errorMessage" class="text-sm text-red-600 bg-red-50 border border-red-100 rounded p-3">
            {{ errorMessage }}
          </p>

          <p v-if="successMessage" class="text-sm text-green-700 bg-green-50 border border-green-100 rounded p-3">
            {{ successMessage }}
          </p>

          <button
            type="submit"
            :disabled="isLoading"
            class="w-full text-white bg-black hover:bg-gray-800 disabled:bg-gray-400 disabled:cursor-not-allowed focus:ring-4 focus:outline-none focus:ring-gray-300 font-medium rounded text-sm px-5 py-4 text-center uppercase tracking-widest transition-colors mt-2"
          >
            {{ isLoading ? 'Đang xử lý...' : 'Đăng ký' }}
          </button>
        </form>

        <div class="mt-6 flex items-center text-sm">
          <router-link to="/login" class="text-gray-600 hover:text-black transition-colors flex items-center gap-2">
            Đã có tài khoản? Đăng nhập
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { registerOnlineCustomer } from '../../services/api'

const router = useRouter()
const firstName = ref('')
const lastName = ref('')
const email = ref('')
const password = ref('')
const errorMessage = ref('')
const successMessage = ref('')
const isLoading = ref(false)
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

const handleRegister = async () => {
  if (isLoading.value) return

  errorMessage.value = ''
  successMessage.value = ''

  const normalizedEmail = email.value.trim().toLowerCase()
  if (!EMAIL_REGEX.test(normalizedEmail)) {
    errorMessage.value = 'Email không đúng định dạng.'
    return
  }

  isLoading.value = true

  try {
    const hoTen = `${lastName.value} ${firstName.value}`.trim()

    const { data } = await registerOnlineCustomer({
      hoTen,
      email: normalizedEmail,
      matKhau: password.value,
    })

    successMessage.value = data?.message || 'Đăng ký thành công.'
    setTimeout(() => {
      router.push('/login')
    }, 700)
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || 'Đăng ký thất bại. Vui lòng thử lại.'
  } finally {
    isLoading.value = false
  }
}
</script>
