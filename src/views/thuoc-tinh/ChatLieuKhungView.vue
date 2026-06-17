<template>
  <MainLayout>
    <BaseThuocTinhView title="Chất liệu khung" apiPath="chat-lieu-khung-vot" propName="tenChatLieuKhungVot" />
  </MainLayout>
</template>
<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
import BaseThuocTinhView from './BaseThuocTinhView.vue';
</script>