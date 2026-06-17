<template>
  <MainLayout>
    <BaseThuocTinhView title="Chất liệu thân" apiPath="chat-lieu-than-vot" propName="tenChatLieuThanVot" />
  </MainLayout>
</template>
<script setup>
import MainLayout from '@/layouts/MainLayout.vue';
import BaseThuocTinhView from './BaseThuocTinhView.vue';
</script>