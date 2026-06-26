<template>
  <div class="bg-gray-50 min-h-screen py-10">
    <div class="container mx-auto px-5">
      <!-- Breadcrumbs -->
      <div class="mb-10 text-sm text-gray-500 flex items-center gap-2">
        <router-link to="/" class="hover:text-primary-color transition-colors">Trang chủ</router-link>
        <span class="text-xs">&gt;</span>
        <span>Tài khoản</span>
        <span class="text-xs">&gt;</span>
        <span class="text-gray-800 font-medium">Đăng nhập</span>
      </div>

      <!-- Login Form Container -->
      <div class="max-w-md mx-auto bg-white rounded-lg shadow-sm p-8 md:p-10 border border-gray-100">
        <h1 class="text-3xl font-bold text-gray-900 mb-8">Đăng nhập</h1>
        
        <form @submit.prevent="handleLogin" class="space-y-5">
          <div>
            <input 
              type="email" 
              placeholder="Email" 
              v-model="email"
              class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3.5 outline-none transition-colors"
              required
            />
          </div>
          <div>
            <input 
              type="password" 
              placeholder="Mật khẩu" 
              v-model="password"
              class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3.5 outline-none transition-colors"
              required
            />
          </div>
          
          <button 
            type="submit" 
            class="w-full text-white bg-black hover:bg-gray-800 focus:ring-4 focus:outline-none focus:ring-gray-300 font-medium rounded text-sm px-5 py-4 text-center uppercase tracking-widest transition-colors mt-2"
          >
            Đăng nhập
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

const router = useRouter()
const email = ref('')
const password = ref('')

const handleLogin = () => {
  // Mock login logic
  console.log('Logging in with:', email.value, password.value)
  // Redirect to home after 'login'
  router.push('/')
}
</script>

<style scoped>
</style>
