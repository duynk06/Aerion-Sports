<template>
  <MainLayout>
    <BaseThuocTinhView title="Độ cứng thân" apiPath="do-cung" propName="tenDoCung" />
  </MainLayout>
</template>
<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
import BaseThuocTinhView from './BaseThuocTinhView.vue';
</script>