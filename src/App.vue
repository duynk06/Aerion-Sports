<template>
  <router-view />
  <ToastProvider />
</template>

<script setup>
import ToastProvider from '../client/src/components/ui/ToastProvider.vue'
</script>
