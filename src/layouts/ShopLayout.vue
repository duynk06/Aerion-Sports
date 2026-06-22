<template>
  <div class="shop-shell">
    <AppHeader />
    <main class="shop-shell__content">
      <router-view />
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import AppHeader from '../../client/src/components/layout/AppHeader.vue'
import AppFooter from '../../client/src/components/layout/AppFooter.vue'
import '../../client/src/styles/global.css'
</script>

<style scoped>
.shop-shell {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f8fafc;
}

.shop-shell__content {
  flex: 1;
}
</style>
