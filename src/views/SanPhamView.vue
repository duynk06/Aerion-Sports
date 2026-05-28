<template>
  <MainLayout>

    <h1 class="page-title">Sản Phẩm</h1>

    <div class="box">

      <button class="orange-btn">
        Bộ lọc
      </button>

      <br /><br />

      <div style="display:flex; gap:20px">

        <input placeholder="Tìm kiếm" />
        <input placeholder="Thương hiệu" />
        <input placeholder="Trạng thái" />

      </div>

    </div>

    <div class="box">

      <h2>Danh Sách Sản Phẩm</h2>

      <br />

      <table>

        <thead>
          <tr>
            <th>STT</th>
            <th>Mã</th>
            <th>Tên</th>
            <th>Thương hiệu</th>
            <th>Số lượng tồn</th>
            <th>Trạng thái</th>
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