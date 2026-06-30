<template>
  <MainLayout>
    <BaseThuocTinhView title="Thương hiệu" apiPath="thuong-hieu" propName="tenThuongHieu" />
  </MainLayout>
</template>

<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
import BaseThuocTinhView from './BaseThuocTinhView.vue';
</script>