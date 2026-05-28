<template>

    <MainLayout title="Trang chủ">
  
      <div class="dashboard">
  
        <div class="card">
          <h3>Doanh thu hôm nay</h3>
          <h1>12.500.000 ₫</h1>
        </div>
  
        <div class="card">
          <h3>Đơn hàng</h3>
          <h1>35</h1>
        </div>
  
        <div class="card">
          <h3>Khách hàng</h3>
          <h1>120</h1>
        </div>
  
        <div class="card">
          <h3>Sản phẩm</h3>
          <h1>240</h1>
        </div>
  
      </div>
  
    </MainLayout>
  
  </template>
  
  <script setup>
  
  import MainLayout from '../layouts/MainLayout.vue'
  
  </script>
  
  <style scoped>
  
  .dashboard{
    display:grid;
    grid-template-columns:repeat(4,1fr);
    gap:20px;
  }
  
  .card{
    background:white;
    border-radius:14px;
    padding:20px;
    box-shadow:0 2px 10px rgba(0,0,0,0.05);
  }
  
  .card h3{
    color:#777;
    margin-bottom:10px;
  }
  
  .card h1{
    color:#e6925e;
  }
  
  </style>