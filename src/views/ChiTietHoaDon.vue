<script setup>
import MainLayout from '@/layouts/MainLayout.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.push('/hoa-don')
  // hoặc router.back()
}
</script>
<template>
  <MainLayout title="Chi tiết hóa đơn">

    <div class="page-header">
      <button class="back-btn" @click="goBack">
        <i class="fa-solid fa-arrow-left"></i>
      </button>
    </div>

    <!-- Nội dung chi tiết hóa đơn -->

  </MainLayout>
</template>

<style scoped>
  .page-header{
  display:flex;
  align-items:center;
  gap:12px;
  margin-bottom:20px;
}

.back-btn{
  width:27px;
  height:27px;
  border:none;
  border-radius:6px;
  background:#f79b66;
  color:white;
  cursor:pointer;
  display:flex;
  align-items:center;
  justify-content:center;
}

.back-btn i{
  font-size:12px;
}

.back-btn:hover{
  opacity:.9;
}
</style>