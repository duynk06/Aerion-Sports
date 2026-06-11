<template>
  <Transition name="dot-giam-gia-toast">
    <div v-if="open" class="toast" :class="`toast--${type}`" role="status" aria-live="polite">
      <div class="toast__icon" aria-hidden="true">
        <i :class="iconClass"></i>
      </div>

      <div class="toast__content">
        <h4 class="toast__title">{{ title }}</h4>
        <p class="toast__message">{{ message }}</p>
      </div>

      <button
        type="button"
        class="toast__close"
        @click="$emit('close')"
        aria-label="Đóng thông báo"
      >
        <i class="fa-solid fa-xmark"></i>
      </button>
    </div>
  </Transition>
</template>

<script setup>
import { computed } from 'vue'

defineEmits(['close'])

const props = defineProps({
  open: {
    type: Boolean,
    default: false,
  },
  type: {
    type: String,
    default: 'success',
    validator: (value) => ['success', 'error'].includes(value),
  },
  title: {
    type: String,
    default: '',
  },
  message: {
    type: String,
    default: '',
  },
})

const iconClass = computed(() =>
  props.type === 'error' ? 'fa-solid fa-triangle-exclamation' : 'fa-solid fa-circle-check',
)
</script>

<style scoped>
.toast {
  width: min(380px, calc(100vw - 32px));
  border-radius: 14px;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  box-shadow: 0 12px 28px rgba(15, 23, 42, 0.08);
  padding: 14px 14px 14px 12px;
  display: flex;
  align-items: flex-start;
  gap: 10px;
  pointer-events: auto;
}

.toast--success {
  border-left: 3px solid #f79b66;
}

.toast--error {
  border-left: 3px solid #ef4444;
}

.toast__icon {
  width: 26px;
  height: 26px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
  font-size: 0.92rem;
  margin-top: 1px;
  color: #64748b;
}

.toast__content {
  flex: 1;
  min-width: 0;
}

.toast__title {
  margin: 0;
  font-size: 0.94rem;
  line-height: 1.4;
  font-weight: 700;
  color: #0f172a;
}

.toast__message {
  margin: 4px 0 0;
  font-size: 0.86rem;
  line-height: 1.55;
  color: #475569;
  word-break: break-word;
  white-space: pre-line;
}

.toast__close {
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 999px;
  background: transparent;
  color: #94a3b8;
  cursor: pointer;
  flex: 0 0 auto;
}

.toast__close:hover {
  background: #f8fafc;
  color: #0f172a;
}

.toast--success .toast__icon {
  color: #b45309;
}

.toast--error .toast__icon {
  color: #b91c1c;
}

.dot-giam-gia-toast-enter-active,
.dot-giam-gia-toast-leave-active {
  transition:
    opacity 0.22s ease,
    transform 0.22s ease;
}

.dot-giam-gia-toast-enter-from,
.dot-giam-gia-toast-leave-to {
  opacity: 0;
  transform: translateY(-8px) translateX(8px);
}
</style>
