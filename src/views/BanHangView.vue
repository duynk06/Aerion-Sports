<template>

  <MainLayout title="Bán hàng">

    <div class="wrapper">

      <!-- LEFT -->
      <div class="left">

        <div class="tabs">

          <button class="active">Giỏ hàng</button>

          <button>Hóa đơn</button>

          <button class="plus">+</button>

        </div>

        <div class="search">

          <input
            type="text"
            placeholder="Tìm kiếm sản phẩm"
          />

          <button>Chọn sản phẩm</button>

        </div>

        <table>

          <thead>

            <tr>

              <th>Sản phẩm</th>

              <th>Số lượng</th>

              <th>Đơn giá</th>

              <th>Thành tiền</th>

            </tr>

          </thead>

          <tbody>

            <tr>

              <td class="product">

                

                <div>
                  
                </div>

              </td>

              <td>
                
              </td>


            </tr>

          </tbody>

        </table>

      </div>

      <!-- RIGHT -->
      <div class="right">

        <div class="type">

          <button class="active">Bán tại quầy</button>

          <button>Giao hàng</button>

        </div>

        <div class="card">

          <h4>Khách Hàng</h4>

          <p>Họ và tên: </p>

          <p>SDT: </p>

        </div>

        <div class="card">

          <h4>Thông tin hóa đơn</h4>

          <div class="row">
            <span>Mã hóa đơn</span>
            
          </div>

          <div class="row">
            <span>Ngày tạo</span>
            
          </div>

          <div class="row">
            <span>Nhân viên</span>
            
          </div>

          <div class="row">
            <span>Tổng tiền</span>
            
          </div>

          <div class="row">

            <span>Phiếu giảm giá</span>

            <select>
              <option>PP giảm thường</option>
            </select>

          </div>

          <div class="row">

            <span>Hình thức thanh toán</span>

            <select>
              <option>Tiền mặt</option>
            </select>

          </div>

          <div class="row">

            <span>Tiền khách đưa</span>

            <input type="text" value="" />

          </div>

          <div class="total">

            <span>TỔNG TIỀN PHẢI TRẢ</span>

            <h2></h2>

          </div>

        </div>

        <div class="actions">

          <button class="cancel">
            Hủy hóa đơn
          </button>

          <button class="pay">
            Thanh toán
          </button>

        </div>

      </div>

    </div>

  </MainLayout>

</template>

<script setup>

import MainLayout from '../layouts/MainLayout.vue'

</script>

<style scoped>

.wrapper{
  display: flex;
  gap: 18px;
}

.left{
  flex: 1;
  background: #ececec;
  border-radius: 10px;
  padding: 14px;
}

.tabs{
  display: flex;
  gap: 10px;
  margin-bottom: 14px;
}

.tabs button{
  border: none;
  padding: 10px 18px;
  border-radius: 8px;
  background: white;
}

.tabs .active{
  background: #e38b55;
}

.plus{
  width: 40px;
}

.search{
  display: flex;
  gap: 10px;
  margin-bottom: 14px;
}

.search input{
  flex: 1;
  height: 38px;
  border-radius: 8px;
  border: none;
  padding: 0 12px;
}

.search button{
  width: 140px;
  border: none;
  border-radius: 8px;
  background: #e38b55;
}

table{
  width: 100%;
  border-collapse: collapse;
  background: white;
}

th{
  background: #f5f5f5;
  padding: 12px;
  text-align: left;
}

td{
  padding: 12px;
}

.product{
  display: flex;
  gap: 10px;
  align-items: center;
}

.product img{
  width: 50px;
  height: 50px;
}

.right{
  width: 300px;
  background: #ececec;
  border-radius: 10px;
  padding: 14px;
}

.type{
  display: flex;
  gap: 10px;
  margin-bottom: 14px;
}

.type button{
  flex: 1;
  height: 40px;
  border: none;
  border-radius: 8px;
}

.type .active{
  background: #e38b55;
}

.card{
  background: white;
  border-radius: 10px;
  padding: 12px;
  margin-bottom: 14px;
}

.row{
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.row select,
.row input{
  width: 120px;
}

.total{
  margin-top: 10px;
}

.total h2{
  color: red;
}

.actions{
  display: flex;
  gap: 10px;
}

.actions button{
  flex: 1;
  height: 40px;
  border: none;
  border-radius: 8px;
}

.cancel{
  background: #e38b55;
}

.pay{
  background: orange;
}

</style>