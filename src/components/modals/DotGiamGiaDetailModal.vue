<template>
  <div class="detail-backdrop" @click.self="$emit('close')">
    <div class="detail-modal">
      <div class="detail-header">
        <div class="list-title">
          <div class="title-icon">
            <i class="fa-solid fa-eye"></i>
          </div>
          <div>
            <h3>Chi tiết đợt giảm giá</h3>
            <span>Xem thông tin đợt giảm giá và sản phẩm áp dụng</span>
          </div>
        </div>

        <button type="button" class="btn-close" @click="$emit('close')">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <div v-if="loading" class="state-box">Đang tải chi tiết...</div>

      <div v-else-if="errorMessage" class="state-box error-box">
        {{ errorMessage }}
      </div>

      <template v-else>
        <section class="detail-summary">
          <div class="summary-item">
            <span>Mã đợt giảm giá</span>
            <strong>{{ detail?.maDotGiamGia || '-' }}</strong>
          </div>
          <div class="summary-item">
            <span>Tên đợt giảm giá</span>
            <strong>{{ detail?.tenDotGiamGia || '-' }}</strong>
          </div>
          <div class="summary-item">
            <span>Giá trị giảm</span>
            <strong>{{ formatDiscountValue(detail?.giaTriGiam) }}</strong>
          </div>
          <div class="summary-item">
            <span>Trạng thái</span>
            <strong>
              <span class="status-badge" :class="getStatusClass(detail?.trangThai)">
                {{ formatTrangThaiText(detail?.trangThai) }}
              </span>
            </strong>
          </div>
          <div class="summary-item">
            <span>Ngày bắt đầu</span>
            <strong>{{ formatDateTime(detail?.ngayBatDau) }}</strong>
          </div>
          <div class="summary-item">
            <span>Ngày kết thúc</span>
            <strong>{{ formatDateTime(detail?.ngayKetThuc) }}</strong>
          </div>
          <div class="summary-item summary-note">
            <span>Mô tả</span>
            <strong>{{ detail?.moTa || '-' }}</strong>
          </div>
        </section>

        <section class="product-section">
          <div class="section-title">
            <h4>Sản phẩm áp dụng</h4>
            <span>{{ detailProducts.length }} sản phẩm</span>
          </div>

          <div v-if="detailProducts.length" class="table-wrap">
            <table class="detail-table">
              <thead>
                <tr>
                  <th scope="col">STT</th>
                  <th scope="col">Mã CTSP</th>
                  <th scope="col">Tên SP</th>
                  <th scope="col">Hãng</th>
                  <th scope="col">Màu sắc</th>
                  <th scope="col">Trọng lượng</th>
                  <th scope="col">Độ cứng</th>
                  <th scope="col">Điểm cân bằng</th>
                  <th scope="col">Chất liệu than</th>
                  <th scope="col">Chất liệu khung</th>
                  <th scope="col">Chu vi</th>
                  <th scope="col">Xuất xứ</th>
                  <th scope="col">Tồn kho</th>
                  <th scope="col">Giá bán</th>
                </tr>
              </thead>

              <tbody>
                <tr v-for="(product, index) in detailProducts" :key="product.idChiTietSanPham">
                  <td>{{ index + 1 }}</td>
                  <td>{{ product.maCtsp || product.maSanPham || '-' }}</td>
                  <td>{{ product.tenSanPham || '-' }}</td>
                  <td>{{ formatText(product.tenThuongHieu) }}</td>
                  <td>{{ formatText(product.tenMauSac) }}</td>
                  <td>{{ formatText(product.tenTrongLuong) }}</td>
                  <td>{{ formatText(product.tenDoCung) }}</td>
                  <td>{{ formatText(product.tenDiemCanBang) }}</td>
                  <td>{{ formatText(product.tenChatLieuThanVot) }}</td>
                  <td>{{ formatText(product.tenChatLieuKhungVot) }}</td>
                  <td>{{ formatText(product.tenChuViCanVot || product.chuViCanVot) }}</td>
                  <td>{{ formatText(product.tenXuatXu || product.xuatXuChiTiet) }}</td>
                  <td>{{ formatText(product.soLuong ?? product.soLuongTon) }}</td>
                  <td>{{ formatMoney(product.giaBan) }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div v-else class="state-box">Không có sản phẩm áp dụng</div>
        </section>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  detail: {
    type: Object,
    default: null,
  },
  errorMessage: {
    type: String,
    default: '',
  },
  loading: {
    type: Boolean,
    default: false,
  },
})

defineEmits(['close'])

const detailProducts = computed(() => {
  return Array.isArray(props.detail?.chiTietList) ? props.detail.chiTietList : []
})

const formatText = (value) => {
  if (value === null || value === undefined || value === '') return '-'
  return String(value)
}

const formatMoney = (value) => {
  if (value === null || value === undefined || value === '') return '-'
  return `${Number(value).toLocaleString('vi-VN')} đ`
}

const formatDiscountValue = (value) => {
  if (value === null || value === undefined || value === '') return '-'
  return `${Number(value).toLocaleString('vi-VN')}%`
}

const formatDateTime = (value) => {
  if (!value) return '-'
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return String(value).replace('T', ' ').slice(0, 16)
  }
  return new Intl.DateTimeFormat('vi-VN', {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(date)
}

const formatTrangThaiText = (status) => {
  switch (Number(status)) {
    case 0:
      return 'Đã hủy'
    case 1:
      return 'Sắp diễn ra'
    case 2:
      return 'Đang diễn ra'
    case 3:
      return 'Đã kết thúc'
    default:
      return 'Không xác định'
  }
}

const getStatusClass = (status) => {
  switch (Number(status)) {
    case 0:
      return 'status-cancel'
    case 1:
      return 'status-wait'
    case 2:
      return 'status-running'
    case 3:
      return 'status-end'
    default:
      return ''
  }
}
</script>

<style scoped>
.detail-backdrop {
  position: fixed;
  inset: 0;
  z-index: 1000;
  background: rgba(17, 24, 39, 0.55);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.detail-modal {
  width: min(1180px, 100%);
  max-height: 92vh;
  overflow: auto;
  background: #ffffff;
  border-radius: 18px;
  padding: 20px;
  box-shadow: 0 22px 60px rgba(0, 0, 0, 0.2);
}

.detail-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 18px;
}

.list-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title-icon {
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: #f79b66;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.list-title h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 700;
}

.list-title span {
  color: #6b7280;
}

.btn-close {
  width: 40px;
  height: 40px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  background: #ffffff;
  cursor: pointer;
}

.detail-summary {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  margin-bottom: 20px;
}

.summary-item {
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  padding: 14px 16px;
  background: #fafafa;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.summary-item span {
  color: #6b7280;
  font-size: 0.92rem;
}

.summary-item strong {
  color: #111827;
  font-size: 1rem;
  font-weight: 700;
}

.summary-note {
  grid-column: 1 / -1;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  border-radius: 999px;
  font-size: 0.82rem;
}

.status-wait {
  background: #fef3c7;
  color: #b45309;
}

.status-running {
  background: #dcfce7;
  color: #15803d;
}

.status-end {
  background: #dbeafe;
  color: #1d4ed8;
}

.status-cancel {
  background: #fee2e2;
  color: #dc2626;
}

.product-section {
  border-top: 1px solid #e5e7eb;
  padding-top: 18px;
}

.section-title {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
}

.section-title h4 {
  margin: 0;
  font-size: 1rem;
  font-weight: 700;
}

.section-title span {
  color: #6b7280;
  font-size: 0.92rem;
}

.table-wrap {
  overflow: auto;
}

.detail-table {
  width: 100%;
  min-width: 1280px;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 12px;
}

.detail-table thead th {
  background: #f79b66;
  color: #ffffff;
}

.detail-table th,
.detail-table td {
  padding: 12px 10px;
  text-align: center;
  border-bottom: 1px solid #e5e7eb;
  white-space: nowrap;
}

.state-box {
  border: 1px dashed #d1d5db;
  border-radius: 12px;
  padding: 16px;
  color: #374151;
  background: #fafafa;
}

.error-box {
  color: #b91c1c;
}

@media (max-width: 900px) {
  .detail-summary {
    grid-template-columns: 1fr;
  }
}
</style>
