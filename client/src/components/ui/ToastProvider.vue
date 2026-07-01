<template>
  <Teleport to="body">
    <div class="toast-provider">
      <TransitionGroup name="toast-stack" tag="div" class="toast-stack">
        <div
          v-for="toast in toasts"
          :key="toast.id"
          class="toast-item"
          :class="`toast-item--${toast.type}`"
          role="status"
          aria-live="polite"
        >
          <span class="toast-item__status" aria-hidden="true"></span>

          <div class="toast-item__content">
            <div v-if="toast.title" class="toast-item__title">{{ toast.title }}</div>
            <div class="toast-item__message">{{ toast.message }}</div>
          </div>

          <button type="button" class="toast-item__close" @click="removeToast(toast.id)" aria-label="Đóng thông báo">
            ×
          </button>
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<script setup>
import { useToast } from '../../composables/useToast'

const { toasts, removeToast } = useToast()
</script>

<style scoped>
.toast-provider {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 10000;
}

.toast-stack {
  position: fixed;
  top: 84px;
  right: 24px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: min(92vw, 380px);
}

.toast-item {
  pointer-events: auto;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 14px 16px 14px 14px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.96);
  color: #0f172a;
  box-shadow: 0 16px 40px rgba(15, 23, 42, 0.08);
  border: 1px solid rgba(148, 163, 184, 0.14);
  backdrop-filter: blur(10px);
  position: relative;
  overflow: hidden;
}

.toast-item::before {
  content: '';
  position: absolute;
  inset: auto 0 0 0;
  height: 2px;
  background: rgba(148, 163, 184, 0.18);
}

.toast-item--success::before {
  background: linear-gradient(90deg, #10b981, rgba(16, 185, 129, 0.12));
}

.toast-item--error::before {
  background: linear-gradient(90deg, #ef4444, rgba(239, 68, 68, 0.12));
}

.toast-item--info::before {
  background: linear-gradient(90deg, #2563eb, rgba(37, 99, 235, 0.12));
}

.toast-item__status {
  width: 10px;
  height: 10px;
  flex: 0 0 10px;
  border-radius: 999px;
  margin-top: 5px;
  background: #94a3b8;
}

.toast-item--success .toast-item__status {
  background: #10b981;
}

.toast-item--error .toast-item__status {
  background: #ef4444;
}

.toast-item--info .toast-item__status {
  background: #2563eb;
}

.toast-item__content {
  flex: 1;
  min-width: 0;
}

.toast-item__title {
  font-weight: 600;
  font-size: 0.93rem;
  color: #0f172a;
  margin-bottom: 2px;
}

.toast-item__message {
  font-size: 0.9rem;
  line-height: 1.45;
  color: #475569;
  word-break: break-word;
}

.toast-item__close {
  flex: 0 0 auto;
  border: 0;
  background: transparent;
  color: #94a3b8;
  font-size: 1.1rem;
  line-height: 1;
  cursor: pointer;
  padding: 2px 0 0;
}

.toast-item__close:hover {
  color: #0f172a;
}

.toast-stack-enter-active,
.toast-stack-leave-active {
  transition: all 0.22s ease;
}

.toast-stack-enter-from,
.toast-stack-leave-to {
  opacity: 0;
  transform: translateY(-6px) scale(0.99);
}

@media (max-width: 640px) {
  .toast-stack {
    top: 76px;
    right: 12px;
    left: 12px;
    width: auto;
  }

  .toast-item {
    padding: 13px 14px 13px 12px;
  }
}
</style>
