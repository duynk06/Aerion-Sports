<template>
  <div class="fixed top-5 right-5 z-[9999] flex flex-col gap-3 pointer-events-none">
    <TransitionGroup name="toast">
      <div 
        v-for="toast in toasts" 
        :key="toast.id"
        class="bg-white rounded-xl shadow-[0_8px_30px_rgb(0,0,0,0.12)] border border-gray-100 p-4 w-80 sm:w-[350px] flex gap-4 pointer-events-auto items-start transform transition-all duration-300"
      >
        <div v-if="toast.type === 'success'" class="w-7 h-7 rounded-full border-[2.5px] border-[#c92a2a] flex items-center justify-center shrink-0 mt-0.5">
          <CheckOutlined class="text-[#c92a2a] font-bold text-sm" />
        </div>
        <div v-else-if="toast.type === 'error'" class="w-7 h-7 rounded-full border-[2.5px] border-red-500 flex items-center justify-center shrink-0 mt-0.5">
          <CloseOutlined class="text-red-500 font-bold text-sm" />
        </div>
        <div v-else class="w-7 h-7 rounded-full border-[2.5px] border-blue-500 flex items-center justify-center shrink-0 mt-0.5">
          <InfoOutlined class="text-blue-500 font-bold text-sm" />
        </div>

        <div class="flex-1 pt-0.5">
          <h4 class="text-gray-800 font-bold text-[15px] mb-0.5">{{ toast.title }}</h4>
          <p class="text-gray-500 text-sm leading-relaxed">{{ toast.message }}</p>
        </div>

        <button @click="removeToast(toast.id)" class="text-gray-400 hover:text-gray-600 transition-colors p-1 -mr-2 -mt-1">
          <CloseOutlined class="text-xs" />
        </button>
      </div>
    </TransitionGroup>
  </div>
</template>

<script setup>
import { useToast } from '../../composables/useToast'
import { CheckOutlined, CloseOutlined, InfoOutlined } from '@ant-design/icons-vue'

const { toasts, removeToast } = useToast()
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
  transition: all 0.3s ease;
}
.toast-enter-from {
  opacity: 0;
  transform: translateX(100%);
}
.toast-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
</style>
