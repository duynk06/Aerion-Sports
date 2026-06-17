<template>
  <MainLayout>
    <BaseThuocTinhView title="Trọng lượng" apiPath="trong-luong" propName="tenTrongLuong" />
  </MainLayout>
</template>
<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
import BaseThuocTinhView from './BaseThuocTinhView.vue';
</script>