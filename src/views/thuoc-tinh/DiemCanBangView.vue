<template>
  <MainLayout>
    <BaseThuocTinhView title="Điểm cân bằng" apiPath="diem-can-bang" propName="tenDiemCanBang" />
  </MainLayout>
</template>
<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
import BaseThuocTinhView from './BaseThuocTinhView.vue';
</script>