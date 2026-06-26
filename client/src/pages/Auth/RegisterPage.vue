<template>
  <div class="bg-gray-50 min-h-screen py-10">
    <div class="container mx-auto px-5">
      <!-- Breadcrumbs -->
      <div class="mb-10 text-sm text-gray-500 flex items-center gap-2">
        <router-link to="/" class="hover:text-primary-color transition-colors">Trang chủ</router-link>
        <span class="text-xs">&gt;</span>
        <span>Tài khoản</span>
        <span class="text-xs">&gt;</span>
        <span class="text-gray-800 font-medium">Đăng ký</span>
      </div>

      <!-- Register Form Container -->
      <div class="max-w-md mx-auto bg-white rounded-lg shadow-sm p-8 md:p-10 border border-gray-100">
        <h1 class="text-3xl font-bold text-gray-900 mb-8">Tạo tài khoản</h1>
        
        <form @submit.prevent="handleRegister" class="space-y-5">
          <div>
            <input 
              type="text" 
              placeholder="Họ" 
              v-model="lastName"
              class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3.5 outline-none transition-colors"
              required
            />
          </div>
          <div>
            <input 
              type="text" 
              placeholder="Tên" 
              v-model="firstName"
              class="w-full bg-gray-50 border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3.5 outline-none transition-colors"
              required
            />
          </div>
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
            Đăng ký
          </button>
        </form>

        <div class="mt-6 flex items-center text-sm">
          <router-link to="/login" class="text-gray-600 hover:text-black transition-colors flex items-center gap-2">
            Quay về
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const firstName = ref('')
const lastName = ref('')
const email = ref('')
const password = ref('')

const handleRegister = () => {
  // Mock register logic
  console.log('Registering with:', firstName.value, lastName.value, email.value, password.value)
  // Redirect to login after 'registering'
  router.push('/login')
}
</script>

<style scoped>
</style>
