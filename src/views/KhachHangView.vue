<template>

  <MainLayout title="Khách hàng">

    <div class="top">

      <button class="add-btn">
        + Thêm khách hàng
      </button>

    </div>

    <div class="table-box">

      <table>

        <thead>

          <tr>
            <th>Mã</th>
            <th>Họ tên</th>
            <th>SĐT</th>
            <th>Email</th>
            <th>Điểm tích lũy</th>
          </tr>

        </thead>

        <tbody>

          <tr>

           
          </tr>

        </tbody>

      </table>

    </div>

  </MainLayout>

</template>

<script setup>

import MainLayout from '../layouts/MainLayout.vue'

</script>

<style scoped>

.top{
  margin-bottom:20px;
}

.add-btn{
  background:#e6925e;
  border:none;
  padding:12px 20px;
  border-radius:10px;
}

.table-box{
  background:#ececec;
  border-radius:10px;
  padding:20px;
}

table{
  width:100%;
  border-collapse:collapse;
  background:white;
}

th{
  background:#e6925e;
  color:white;
  padding:14px;
}

td{
  padding:14px;
  border-bottom:1px solid #ddd;
}

</style>