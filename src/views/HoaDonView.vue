<template>
  <MainLayout title="Quản lý hóa đơn">

    <!-- FILTER -->
    <div class="card">

      <div class="card-header">
        <i class="fa-solid fa-filter"></i>
        Bộ lọc tìm kiếm
      </div>

      <div class="filter-grid">

        <div>
          <label>Tìm kiếm</label>
          <input
            type="text"
            placeholder="Nhập mã hóa đơn / tên khách / SDT..."
          >
        </div>

        <div>
          <label>Loại hóa đơn</label>
          <select>
            <option>Tất cả</option>
            <option>Tại quầy</option>
            <option>Online</option>
          </select>
        </div>

        <div>
          <label>Từ ngày</label>
          <input type="date">
        </div>

        <div>
          <label>Đến ngày</label>
          <input type="date">
        </div>

      </div>

      <div class="filter-action">
        <button class="btn-reset">
  <i class="fa-solid fa-rotate-right"></i>
  Đặt lại
</button>

<button class="btn-export">
  <i class="fa-solid fa-file-excel"></i>
  Xuất Excel
</button>
      </div>

    </div>

    <!-- LIST -->
    <!-- DANH SÁCH HÓA ĐƠN -->
<div class="card">

<div class="list-title">

  <div class="title-icon">
    <i class="fa-solid fa-file-invoice"></i>
  </div>

  <div>

    <h3>Danh sách hóa đơn</h3>

    <span>Lọc nhanh theo trạng thái</span>

  </div>

</div>

<div class="status-tabs">

  <button class="active">Tất cả</button>

  <button>Chờ xác nhận</button>

  <button>Đang giao</button>

  <button>Đã giao</button>

  <button>Hoàn thành</button>

  <button>Đã hủy</button>

  <button>Yêu cầu hủy</button>

  <button>Đã hoàn tiền</button>

</div>

<table>

  <thead>

    <tr>

      <th>STT</th>
      <th>Mã hóa đơn</th>
      <th>Tên nhân viên</th>
      <th>Khách hàng</th>
      <th>Số điện thoại</th>
      <th>Loại hóa đơn</th>
      <th>Tổng tiền</th>
      <th>Ngày tạo</th>
      <th>Trạng thái</th>
      <th>Hành động</th>

    </tr>

  </thead>

  <tbody>

    <tr>

      <td>1</td>

      <td>HD2604298918</td>

      <td>
        Admin TBT
        <br>
        <small>NV001</small>
      </td>

      <td>Duy Quyết</td>

      <td>0868219136</td>

      <td>

        <span class="invoice-type">
          Tại quầy
        </span>

      </td>

      <td class="money">
        6.000.000 đ
      </td>

      <td>29/04/2026</td>

      <td>

        <span class="status-success">
          Hoàn thành
        </span>

      </td>

      <td>

        <button class="action-btn">

          <i class="fa-solid fa-eye"></i>

        </button>

        <button class="action-btn">

          <i class="fa-solid fa-print"></i>

        </button>

      </td>

    </tr>

    <tr>

      <td>2</td>

      <td>HD2604295243</td>

      <td>
        Admin TBT
        <br>
        <small>NV001</small>
      </td>

      <td>Duy Quyết</td>

      <td>0868219136</td>

      <td>

        <span class="invoice-type">
          Tại quầy
        </span>

      </td>

      <td class="money">
        2.800.000 đ
      </td>

      <td>29/04/2026</td>

      <td>

        <span class="status-success">
          Hoàn thành
        </span>

      </td>

      <td>

        <button class="action-btn">

          <i class="fa-solid fa-eye"></i>

        </button>

        <button class="action-btn">

          <i class="fa-solid fa-print"></i>

        </button>

      </td>

    </tr>

  </tbody>

</table>

<div class="pagination">

  <span>
    Hiển thị 1 / 4 bản ghi
  </span>

  <div class="page-center">

    <button>
      <i class="fa-solid fa-chevron-left"></i>
    </button>

    <span>Trang 1</span>

    <button>
      <i class="fa-solid fa-chevron-right"></i>
    </button>

  </div>

  <select>

    <option>10 bản ghi / trang</option>

  </select>

</div>

</div>

  </MainLayout>
</template>

<script setup>
import MainLayout from '../layouts/MainLayout.vue'
</script>

<style scoped>
.card{
  background:#fff;
  border-radius:12px;
  padding:16px;
  margin-bottom:20px;
  border:1px solid #e5e7eb;
}

/* FILTER */

.card-header{
  background:#f79b66;
  color:#fff;
  padding:12px 16px;
  border-radius:8px;
  margin-bottom:20px;
  font-weight:600;
  font-size:14px;
}

.filter-grid{
  display:grid;
  grid-template-columns:repeat(4,1fr);
  gap:20px;
}

.filter-grid label{
  display:block;
  margin-bottom:6px;
  font-size:12px;
  font-weight:500;
}

.filter-grid input,
.filter-grid select{
  width:100%;
  height:38px;
  border:1px solid #ddd;
  border-radius:6px;
  padding:0 10px;
}

.filter-action{
  display:flex;
  justify-content:flex-end;
  gap:10px;
  margin-top:16px;
}

.btn-reset{
  border:none;
  background:#f3f4f6;
  padding:10px 18px;
  border-radius:6px;
  cursor:pointer;
}

.btn-export{
  border:none;
  background:#d8f5eb;
  color:#00875a;
  padding:10px 18px;
  border-radius:6px;
  cursor:pointer;
}

/* DANH SÁCH HÓA ĐƠN */

.list-title{
  display:flex;
  align-items:center;
  gap:10px;
  margin-bottom:12px;
}

.title-icon{
  width:28px;
  height:28px;
  border-radius:8px;
  background:#f79b66;
  color:#fff;
  display:flex;
  align-items:center;
  justify-content:center;
  font-size:12px;
}

.list-title h3{
  margin:0;
  font-size:14px;
  font-weight:600;
}

.list-title span{
  font-size:11px;
  color:#888;
}

.status-tabs{
  display:flex;
  flex-wrap:wrap;
  gap:6px;
  margin-bottom:15px;
}

.status-tabs button{
  height:28px;
  padding:0 12px;
  border-radius:20px;
  border:1px solid #e5e7eb;
  background:white;
  font-size:11px;
  cursor:pointer;
}

.status-tabs .active{
  background:#f79b66;
  color:white;
  border-color:#f79b66;
}

/* TABLE */

table{
  width:100%;
  border-collapse:collapse;
  font-size:12px;
}

thead{
  background:#f79b66;
  color:white;
}

th{
  padding:10px;
  font-size:11px;
  font-weight:600;
}

td{
  padding:10px;
  text-align:center;
  border-bottom:1px solid #eee;
}

tbody tr:hover{
  background:#fafafa;
}

.money{
  color:#d14343;
  font-weight:600;
}

/* BADGE */

.invoice-type{
  background:#f4f4f4;
  padding:4px 10px;
  border-radius:20px;
  font-size:11px;
}

.status-success{
  background:#28a745;
  color:white;
  padding:4px 10px;
  border-radius:20px;
  font-size:11px;
}

/* ACTION */

.action-btn{
  width:28px;
  height:28px;
  border-radius:6px;
  border:1px solid #dcdcdc;
  background:white;
  cursor:pointer;
  margin:0 2px;
}

.action-btn:hover{
  background:#f5f5f5;
}

/* PAGINATION */

.pagination{
  margin-top:16px;
  display:flex;
  justify-content:space-between;
  align-items:center;
  font-size:12px;
}

.page-center{
  display:flex;
  align-items:center;
  gap:10px;
}

.page-center button{
  width:28px;
  height:28px;
  border:1px solid #ddd;
  background:white;
  border-radius:4px;
  cursor:pointer;
}

.pagination select{
  height:32px;
  border:1px solid #ddd;
  border-radius:6px;
  padding:0 8px;
}

</style>