<template>
  <div class="page-shell create-shell">
    <div class="create-header">
      <div class="list-title">
        <div class="title-icon">
          <i class="fa-solid fa-plus"></i>
        </div>
        <div>
          <h3>Thêm đợt giảm giá</h3>
          <span>Chọn sản phẩm áp dụng ở khung bên phải</span>
        </div>
      </div>

    </div>

    <section class="card create-card">
      <div class="create-grid">
        <div class="create-form-panel">
          <form class="create-form" @submit.prevent="$emit('save')">
            <div class="create-field">
              <label for="create-code">Mã đợt giảm giá</label>
              <input id="create-code" type="text" :value="'Hệ thống tự động sinh'" readonly />
            </div>

            <div class="create-field">
              <label for="create-name">Tên đợt giảm giá</label>
              <input
                id="create-name"
                v-model.trim="form.tenDotGiamGia"
                type="text"
                placeholder="Nhập tên đợt giảm giá"
              />
            </div>

            <div class="create-field">
              <label for="create-value">Giá trị (%)</label>
              <input
                id="create-value"
                v-model.number="form.giaTriGiam"
                type="number"
                min="1"
                step="1"
                placeholder="Nhập giá trị giảm"
              />
            </div>

            <div class="create-field">
              <label for="create-start">Ngày bắt đầu</label>
              <input id="create-start" v-model="form.ngayBatDau" type="datetime-local" />
            </div>

            <div class="create-field">
              <label for="create-end">Ngày kết thúc</label>
              <input id="create-end" v-model="form.ngayKetThuc" type="datetime-local" />
            </div>

            <div class="create-field">
              <label for="create-note">Mô tả</label>
              <textarea
                id="create-note"
                v-model.trim="form.moTa"
                rows="5"
                placeholder="Nhập mô tả"
              ></textarea>
            </div>

            <div class="create-summary">
              <strong>Sản phẩm đã chọn:</strong>
              <span>{{ selectedProductsCount }} sản phẩm</span>
            </div>

            <p v-if="errorMessage" class="create-error">{{ errorMessage }}</p>

            <div class="create-actions">
              <button type="button" class="btn-cancel" @click="$emit('back')">Quay lại</button>
              <button type="submit" class="btn-save" :disabled="submitting">
                {{ submitting ? 'Đang lưu...' : 'Thêm mới' }}
              </button>
            </div>
          </form>
        </div>

        <div class="create-product-panel">
          <div class="product-search-row">
            <div class="product-search">
              <input
                :value="productKeyword"
                type="text"
                placeholder="Tìm kiếm theo tên sản phẩm"
                @input="$emit('update:productKeyword', $event.target.value)"
                @keyup.enter="$emit('search-products')"
              />
              <button type="button" @click="$emit('search-products')">
                <i class="fa-solid fa-magnifying-glass"></i>
              </button>
            </div>
          </div>

          <div class="product-meta">
            <span>Đã chọn {{ selectedProductsCount }} sản phẩm</span>
          </div>

          <div class="product-table-wrap">
            <table class="product-table">
              <thead>
                <tr>
                  <th scope="col">
                    <input
                      type="checkbox"
                      :checked="isAllVisibleSelected"
                      :disabled="!visibleProducts.length"
                      @change="$emit('toggle-select-all-visible')"
                    />
                  </th>
                  <th scope="col">STT</th>
                  <th scope="col">Mã SP</th>
                  <th scope="col">Tên sản phẩm</th>
                  <th scope="col">Giá bán</th>
                  <th scope="col">Số lượng</th>
                </tr>
              </thead>

              <tbody>
                <tr v-if="productLoading">
                  <td colspan="6" class="state-cell">Đang tải sản phẩm...</td>
                </tr>

                <tr v-else-if="productError">
                  <td colspan="6" class="state-cell error-cell">{{ productError }}</td>
                </tr>

                <template v-else-if="visibleProducts.length">
                  <tr v-for="(product, index) in visibleProducts" :key="product.idChiTietSanPham">
                    <td>
                      <input
                        type="checkbox"
                        :checked="selectedProductIds.includes(product.idChiTietSanPham)"
                        @change="$emit('toggle-product-selection', product)"
                      />
                    </td>
                    <td>{{ productPage * productPageSize + index + 1 }}</td>
                    <td>{{ product.maSanPham || '-' }}</td>
                    <td>{{ product.tenSanPham || '-' }}</td>
                    <td>{{ formatMoney(product.giaBan) }}</td>
                    <td>{{ product.soLuongTon ?? '-' }}</td>
                  </tr>
                </template>

                <tr v-else>
                  <td colspan="6" class="state-cell">Không có sản phẩm phù hợp</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="pagination">
            <div class="page-center">
              <button
                type="button"
                @click="$emit('prev-product-page')"
                :disabled="productPage === 0"
              >
                <i class="fa-solid fa-chevron-left"></i>
              </button>

              <span>Trang {{ productPage + 1 }} / {{ productTotalPagesDisplay }}</span>

              <button
                type="button"
                @click="$emit('next-product-page')"
                :disabled="productPage + 1 >= productTotalPages"
              >
                <i class="fa-solid fa-chevron-right"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

      <section v-if="groupedSelectedProducts.length" class="selected-details">
        <div class="selected-details-header">
          <h4>Chi tiết sản phẩm đã chọn</h4>
        </div>

        <div v-for="group in groupedSelectedProducts" :key="group.key" class="selected-group">
          <div class="selected-group-header">
            <div>
              <h5>{{ group.name }}</h5>
              <p>Tổng số lượng tồn: {{ group.totalStock }}</p>
            </div>
          </div>

          <div class="selected-group-table-wrap">
            <table class="selected-group-table">
              <thead>
                <tr>
                  <th scope="col">Chọn</th>
                  <th scope="col">STT</th>
                  <th scope="col">Mã CTSP</th>
                  <th scope="col">Tên SP</th>
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
                <tr v-for="(product, index) in group.items" :key="product.idChiTietSanPham">
                  <td>
                    <input
                      type="checkbox"
                      :checked="selectedProductIds.includes(product.idChiTietSanPham)"
                      @change="$emit('toggle-product-selection', product)"
                    />
                  </td>
                  <td>{{ index + 1 }}</td>
                  <td>{{ product.maSanPham || '-' }}</td>
                  <td>{{ product.tenSanPham || '-' }}</td>
                  <td>{{ formatText(product.tenMauSac) }}</td>
                  <td>{{ formatText(product.tenTrongLuong) }}</td>
                  <td>{{ formatText(product.tenDoCung) }}</td>
                  <td>{{ formatText(product.tenDiemCanBang) }}</td>
                  <td>{{ formatText(product.tenChatLieuThanVot) }}</td>
                  <td>{{ formatText(product.tenChatLieuKhungVot) }}</td>
                  <td>{{ formatText(product.chuViCanVot) }}</td>
                  <td>{{ formatText(product.xuatXuChiTiet) }}</td>
                  <td>{{ formatText(product.soLuongTon) }}</td>
                  <td>{{ formatMoney(product.giaBan) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </section>
    </section>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  errorMessage: {
    type: String,
    default: '',
  },
  form: {
    type: Object,
    required: true,
  },
  isAllVisibleSelected: {
    type: Boolean,
    default: false,
  },
  productError: {
    type: String,
    default: '',
  },
  productKeyword: {
    type: String,
    default: '',
  },
  productLoading: {
    type: Boolean,
    default: false,
  },
  productPage: {
    type: Number,
    default: 0,
  },
  productPageSize: {
    type: Number,
    default: 5,
  },
  productTotalPages: {
    type: Number,
    default: 1,
  },
  productTotalPagesDisplay: {
    type: Number,
    default: 1,
  },
  selectedProductDetails: {
    type: Array,
    default: () => [],
  },
  selectedProductIds: {
    type: Array,
    default: () => [],
  },
  selectedProductsCount: {
    type: Number,
    default: 0,
  },
  submitting: {
    type: Boolean,
    default: false,
  },
  visibleProducts: {
    type: Array,
    default: () => [],
  },
})

defineEmits([
  'back',
  'next-product-page',
  'prev-product-page',
  'save',
  'search-products',
  'toggle-product-selection',
  'toggle-select-all-visible',
  'update:productKeyword',
])

const formatText = (value) => {
  if (value === null || value === undefined || value === '') {
    return '-'
  }

  return String(value)
}

const formatMoney = (value) => {
  if (value === null || value === undefined || value === '') {
    return '-'
  }

  return `${Number(value).toLocaleString('vi-VN')} đ`
}

const groupedSelectedProducts = computed(() => {
  const groups = new Map()

  for (const product of props.selectedProductDetails) {
    const key = product.tenSanPham || product.maSanPham || String(product.idChiTietSanPham)

    if (!groups.has(key)) {
      groups.set(key, {
        key,
        name: product.tenSanPham || product.maSanPham || 'Sản phẩm',
        totalStock: 0,
        items: [],
      })
    }

    const group = groups.get(key)
    group.items.push(product)
    group.totalStock += Number(product.soLuongTon || 0)
  }

  return Array.from(groups.values())
})
</script>

<style scoped>
.selected-details {
  margin-top: 20px;
  border-top: 1px solid #e5e7eb;
  padding-top: 16px;
}

.selected-details-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 12px;
}

.selected-details-header h4 {
  margin: 0;
  font-size: 1rem;
  font-weight: 700;
  color: #111827;
}

.selected-details-header span {
  color: #6b7280;
  font-size: 0.92rem;
}

.create-product-panel .product-table-wrap {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  overflow: auto;
}

.create-product-panel .product-table,
.selected-details .selected-group-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 12px;
}

.create-product-panel .product-table thead th,
.selected-details .selected-group-table thead th {
  background: #f79b66 !important;
  color: #ffffff;
}

.create-product-panel .product-table th,
.create-product-panel .product-table td,
.selected-details .selected-group-table th,
.selected-details .selected-group-table td {
  padding: 12px 10px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #e5e7eb;
  white-space: nowrap;
}

.create-product-panel .product-table tbody tr:hover,
.selected-details .selected-group-table tbody tr:hover {
  background: #fafafa;
}

.create-product-panel .product-table th,
.selected-details .selected-group-table th {
  font-size: 11px;
}

.create-product-panel .product-table td,
.selected-details .selected-group-table td {
  font-size: 11px;
}

.selected-group {
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  padding: 14px 16px;
  background: #fafafa;
  margin-bottom: 12px;
}

.selected-group-header {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
  margin-bottom: 12px;
}

.selected-group-header h5 {
  margin: 0;
  font-size: 1rem;
  font-weight: 700;
  color: #111827;
}

.selected-group-header p {
  margin: 4px 0 0;
  color: #6b7280;
  font-size: 0.9rem;
}

.selected-group-header span {
  color: #2563eb;
  font-weight: 700;
  white-space: nowrap;
}

.selected-details .selected-group-table-wrap {
  overflow-x: auto;
}

.selected-details .selected-group-table {
  width: 100%;
  min-width: 1300px;
  border-collapse: collapse;
}

.selected-details .selected-group-table th,
.selected-details .selected-group-table td {
  padding: 12px 10px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #e5e7eb;
  white-space: nowrap;
  font-size: 11px;
}

.selected-details .selected-group-table thead th {
  color: #ffffff;
  font-weight: 700;
  background: #f79b66;
}

.selected-details .selected-group-table tbody tr:last-child td {
  border-bottom: 0;
}

.selected-details .selected-group-table td:last-child,
.selected-details .selected-group-table th:last-child {
  text-align: center;
}

.selected-detail-card {
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  padding: 14px 16px;
  background: #fafafa;
}

.selected-detail-main {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 10px;
}

.selected-detail-badge {
  min-width: 92px;
  padding: 6px 10px;
  border-radius: 999px;
  background: #eff6ff;
  color: #2563eb;
  font-weight: 700;
  text-align: center;
}

.selected-detail-main h5 {
  margin: 0;
  font-size: 0.98rem;
  font-weight: 700;
}

.selected-detail-main p {
  margin: 4px 0 0;
  color: #6b7280;
  font-size: 0.9rem;
}

.selected-detail-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px 16px;
  font-size: 0.92rem;
  color: #374151;
}

.selected-detail-grid strong {
  color: #111827;
}
</style>
