<template>
    <MainLayout title="Thêm Phiếu Giảm Giá">

      <div class="form-container">

        <div class="form-header">
          <h2>Sửa Phiếu Giảm Giá</h2>
        </div>

        <form @submit.prevent="handleUpdate">

          <div class="form-grid">

            <div class="form-group">
              <label>Mã phiếu giảm giá</label>
              <input
                type="text"
                v-model="form.maPhieuGiamGia"
                required
              />
            </div>

            <div class="form-group">
              <label>Tên phiếu giảm giá</label>
              <input
                type="text"
                v-model="form.tenPhieuGiamGia"
                required
              />
            </div>

            <div class="form-group">
              <label>Loại phiếu</label>
              <select v-model="form.loaiPhieuGiamGia">
                <option value="Sale %">Sale %</option>
                <option value="Free Ship">Free Ship</option>
              </select>
            </div>

            <div class="form-group">
              <label>Giá trị giảm</label>
              <input
                type="number"
                v-model="form.giaTriGiam"
              />
            </div>

            <div class="form-group">
              <label>Đơn tối thiểu</label>
              <input
                type="number"
                v-model="form.giaTriDonToiThieu"
              />
            </div>

            <div class="form-group">
              <label>Giảm tối đa</label>
              <input
                type="number"
                v-model="form.giaTriGiamToiDa"
              />
            </div>

            <div class="form-group">
              <label>Số lượng</label>
              <input
                type="number"
                v-model="form.soLuong"
              />
            </div>

            <div class="form-group">
              <label>Ngày bắt đầu</label>
              <input
                type="datetime-local"
                v-model="form.ngayBatDau"
              />
            </div>

            <div class="form-group">
              <label>Ngày kết thúc</label>
              <input
                type="datetime-local"
                v-model="form.ngayKetThuc"
              />
            </div>

            <div class="form-group full-width">
              <label>Mô tả</label>
              <textarea
                rows="4"
                v-model="form.moTa"
              ></textarea>
            </div>

          </div>

          <div class="button-group">
            <button
              type="button"
              class="btn-cancel"
              @click="$router.back()"
            >
              Hủy
            </button>

            <button
              type="submit"
              class="btn-save"
            >
              Lưu
            </button>
          </div>

        </form>

      </div>

    </MainLayout>
  </template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import {
  updatePhieuGiamGia,
  fetchAllPhieuGiamGia
} from '@/service/PhieuGiamGiaService'

const route = useRoute()
const router = useRouter()

const form = ref({})

const loadData = async () => {
  const data = await fetchAllPhieuGiamGia()

  const phieu = data.find(
    x => x.id == route.params.id
  )

  if (phieu) {
    form.value = {
      ...phieu,
      ngayBatDau: phieu.ngayBatDau?.slice(0, 16),
      ngayKetThuc: phieu.ngayKetThuc?.slice(0, 16)
    }
  }
}

const handleUpdate = async () => {
  try {
    await updatePhieuGiamGia(
      route.params.id,
      form.value
    )

    alert('Cập nhật thành công')

    router.push('/phieu-giam-gia')
  }catch (error) {
  console.error('Lỗi update:', error)
  alert(error.message)
}
}

onMounted(loadData)
</script>
<style scoped>
  .form-container {
    background: white;
    padding: 25px;
    border-radius: 15px;
  }

  .form-header {
    margin-bottom: 25px;
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
  }

  .form-group input,
  .form-group select,
  .form-group textarea {
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 10px;
  }

  .full-width {
    grid-column: 1 / -1;
  }

  .button-group {
    margin-top: 25px;
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  .btn-save {
    background: #ea9154;
    color: white;
    border: none;
    padding: 12px 24px;
    border-radius: 10px;
    cursor: pointer;
  }

  .btn-cancel {
    background: gray;
    color: white;
    border: none;
    padding: 12px 24px;
    border-radius: 10px;
    cursor: pointer;
  }
  </style>
