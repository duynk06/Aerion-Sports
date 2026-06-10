  <template>
    <MainLayout title="Thêm Phiếu Giảm Giá">

      <div class="form-container">

        <div class="form-header">
          <h2>Thêm Phiếu Giảm Giá</h2>
        </div>

        <form @submit.prevent="handleCreate">

          <div class="form-grid">

            <div class="form-group">
              <label>Mã phiếu giảm giá</label>
              <input type="text" v-model="form.maPhieuGiamGia" required />
            </div>

            <div class="form-group">
              <label>Tên phiếu giảm giá</label>
              <input type="text" v-model="form.tenPhieuGiamGia" required />
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
              <input type="number" v-model="form.giaTriGiam" />
            </div>

            <div class="form-group">
              <label>Đơn tối thiểu</label>
              <input type="number" v-model="form.giaTriDonToiThieu" />
            </div>

            <div class="form-group">
              <label>Giảm tối đa</label>
              <input type="number" v-model="form.giaTriGiamToiDa" />
            </div>

            <div class="form-group">
              <label>Số lượng</label>
              <input type="number" v-model="form.soLuong" />
            </div>

            <div class="form-group">
              <label>Ngày bắt đầu</label>
              <input type="datetime-local" v-model="form.ngayBatDau" />
            </div>

            <div class="form-group">
              <label>Ngày kết thúc</label>
              <input type="datetime-local" v-model="form.ngayKetThuc" />
            </div>

            <div class="form-group full-width">
              <label>Mô tả</label>
              <textarea rows="4" v-model="form.moTa"></textarea>
            </div>

          </div>

          <div class="button-group">
            <button type="button" class="btn-cancel" @click="$router.back()">
              Hủy
            </button>

            <button type="submit" class="btn-save">
              Lưu
            </button>
          </div>

        </form>

      </div>

    </MainLayout>
  </template>

<script setup>
import { ref } from 'vue'
import MainLayout from '../layouts/MainLayout.vue'
import { createPhieuGiamGia } from '@/service/PhieuGiamGiaService'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  maPhieuGiamGia: '',
  tenPhieuGiamGia: '',
  loaiPhieuGiamGia: '',
  giaTriGiam: 0,
  giaTriDonToiThieu: 0,
  giaTriGiamToiDa: 0,
  soLuong: 0,
  ngayBatDau: '',
  ngayKetThuc: '',
  moTa: '',
  trangThai: 1
})

const handleCreate = async () => {
  if (!form.value.maPhieuGiamGia.trim()) {
    alert('Vui lòng nhập Mã phiếu giảm giá!')
    return
  }
  if (!form.value.moTa.trim()) {
    alert('Vui lòng nhập Mô tả!')
    return
  }

   try {
    await createPhieuGiamGia(form.value)

    alert('Thêm phiếu giảm giá thành công!')
    router.push('/phieu-giam-gia')

  } catch (error) {
    console.log(error.response)
    if (
      error.response?.data?.message?.includes('trùng') ||
      error.response?.data?.message?.includes('đã tồn tại')
    ) {
      alert('Mã phiếu giảm giá đã tồn tại!')
      return
    }

    alert(
      error.response?.data?.message ||
      'Thêm phiếu giảm giá thất bại!'
    )
  }
}
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
