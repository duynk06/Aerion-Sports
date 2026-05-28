<template>

    <MainLayout title="Thống kê">
  
      <div class="box">
  
        <h2>Biểu đồ doanh thu</h2>
  
        <div class="chart">
  
          <div class="bar h1"></div>
          <div class="bar h2"></div>
          <div class="bar h3"></div>
          <div class="bar h4"></div>
          <div class="bar h5"></div>
  
        </div>
  
      </div>
  
    </MainLayout>
  
  </template>
  
  <script setup>
  
  import MainLayout from '../layouts/MainLayout.vue'
  
  </script>
  
  <style scoped>
  
  .box{
    background:white;
    border-radius:14px;
    padding:20px;
  }
  
  .chart{
    height:300px;
    display:flex;
    align-items:flex-end;
    gap:20px;
    margin-top:30px;
  }
  
  .bar{
    width:70px;
    background:#e6925e;
    border-radius:10px 10px 0 0;
  }
  
  .h1{height:120px;}
  .h2{height:180px;}
  .h3{height:250px;}
  .h4{height:150px;}
  .h5{height:220px;}
  
  </style>