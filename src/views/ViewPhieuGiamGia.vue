<template>
  <MainLayout title="Chi tiết phiếu giảm giá">

    <div class="form-container">

      <div class="form-header">
        <h2>Chi tiết phiếu giảm giá</h2>
      </div>

      <div class="form-grid">

        <div class="form-group">
          <label>Mã phiếu</label>
          <input :value="form.maPhieuGiamGia" disabled />
        </div>

        <div class="form-group">
          <label>Tên phiếu</label>
          <input :value="form.tenPhieuGiamGia" disabled />
        </div>

        <div class="form-group">
          <label>Loại phiếu</label>
          <input :value="form.loaiPhieuGiamGia" disabled />
        </div>

        <div class="form-group">
          <label>Giá trị giảm</label>
          <input :value="form.giaTriGiam" disabled />
        </div>

        <div class="form-group">
          <label>Đơn tối thiểu</label>
          <input :value="form.giaTriDonToiThieu" disabled />
        </div>

        <div class="form-group">
          <label>Giảm tối đa</label>
          <input :value="form.giaTriGiamToiDa" disabled />
        </div>

        <div class="form-group">
          <label>Số lượng</label>
          <input :value="form.soLuong" disabled />
        </div>

        <div class="form-group">
          <label>Đã sử dụng</label>
          <input :value="form.soLuongDaSuDung" disabled />
        </div>

        <div class="form-group">
          <label>Ngày bắt đầu</label>
          <input :value="formatDate(form.ngayBatDau)" disabled />
        </div>

        <div class="form-group">
          <label>Ngày kết thúc</label>
          <input :value="formatDate(form.ngayKetThuc)" disabled />
        </div>

        <div class="form-group full-width">
          <label>Mô tả</label>
          <textarea :value="form.moTa" disabled></textarea>
        </div>

      </div>

      <div class="button-group">
        <button
          class="btn-back"
          @click="router.push('/phieu-giam-gia')"
        >
          Quay lại
        </button>
      </div>

    </div>

  </MainLayout>
</template>

<script setup>
import MainLayout from '../layouts/MainLayout.vue'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const form = ref({})

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('vi-VN')
}

const fetchDetail = async () => {
  const response = await fetch(
    `http://localhost:8080/phieu-giam-gia/${route.params.id}`
  )

  form.value = await response.json()
}

onMounted(() => {
  fetchDetail()
})
</script>
<style>
.form-container {
  background: white;
  padding: 25px;
  border-radius: 15px;
}

.form-header {
  margin-bottom: 25px;
}

.form-header h2 {
  margin: 0;
  color: #333;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 8px;
  font-weight: 600;
  color: #555;
}

.form-group input,
.form-group textarea {
  width: 100%;
  box-sizing: border-box;
  word-break: break-word;
}
.form-group input:disabled,
.form-group textarea:disabled {
  background: #f3f4f6;
  color: #333;
  cursor: not-allowed;
  opacity: 1;
}

.full-width {
  grid-column: 1 / -1;
}

textarea {
  resize: none;
}

.button-group {
  margin-top: 25px;
  display: flex;
  justify-content: flex-end;
}

.btn-back {
  background: #ea9154;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 10px;
  cursor: pointer;
  transition: 0.3s;
}

.btn-back:hover {
  background: #d97f42;
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>