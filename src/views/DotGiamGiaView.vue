<template>

  <MainLayout title="Đợt giảm giá">

    <div class="top">

      <button class="add-btn">
        + Thêm đợt giảm giá
      </button>

    </div>

    <div class="table-box">

      <table>

        <thead>

          <tr>
            <th>Mã</th>
            <th>Tên đợt</th>
            <th>Ngày bắt đầu</th>
            <th>Ngày kết thúc</th>
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

.status{
  background:#4caf50;
  color:white;
  padding:6px 12px;
  border-radius:8px;
}

</style>