<template>

  <MainLayout title="Hóa đơn">

    <!-- FILTER -->
    <div class="filter-box">

      <div class="filters">

        <input placeholder="Tìm kiếm hóa đơn" />

        <select>
          <option>Trạng thái</option>
        </select>

        <input type="date" />

      </div>

    </div>

    <!-- TABLE -->
    <div class="table-box">

      <h3>Danh sách hóa đơn</h3>

      <table>

        <thead>

          <tr>
            <th>Mã HD</th>
            <th>Khách hàng</th>
            <th>Nhân viên</th>
            <th>Ngày tạo</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
          </tr>

        </thead>

        <tbody>

          <tr>

          

            <td>
             
            </td>

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

.filter-box,
.table-box{
  background:#ececec;
  padding:20px;
  border-radius:10px;
  margin-bottom:20px;
}

.filters{
  display:flex;
  gap:15px;
}

.filters input,
.filters select{
  flex:1;
  height:40px;
  border:none;
  border-radius:10px;
  padding:0 10px;
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

.success{
  background:#4caf50;
  color:white;
  padding:6px 12px;
  border-radius:8px;
}

</style>