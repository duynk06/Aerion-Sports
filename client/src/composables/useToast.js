import { ref } from 'vue'

const toasts = ref([])
let toastId = 0

export const useToast = () => {
  const showToast = ({ title, message, type = 'success', duration = 3000 }) => {
    const id = toastId++
    toasts.value.push({ id, title, message, type })
    
    setTimeout(() => {
      removeToast(id)
    }, duration)
  }

  const removeToast = (id) => {
    const index = toasts.value.findIndex(t => t.id === id)
    if (index > -1) {
      toasts.value.splice(index, 1)
    }
  }

  const success = (title, message) => showToast({ title, message, type: 'success' })
  const error = (title, message) => showToast({ title, message, type: 'error' })
  const info = (title, message) => showToast({ title, message, type: 'info' })

  return {
    toasts,
    showToast,
    removeToast,
    success,
    error,
    info
  }
}
