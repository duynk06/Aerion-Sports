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
                max="100"
                step="1"
                placeholder="Nhập giá trị giảm"
              />
            </div>

            <div class="create-field">
              <label for="create-start">Ngày bắt đầu</label>
              <input
                id="create-start"
                v-model="form.ngayBatDau"
                type="datetime-local"
                :min="minDateTime"
              />
            </div>

            <div class="create-field">
              <label for="create-end">Ngày kết thúc</label>
              <input
                id="create-end"
                v-model="form.ngayKetThuc"
                type="datetime-local"
                :min="endDateMin"
              />
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
                  <th scope="col">Mã CTSP</th>
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
                    <td>{{ product.maCtsp || product.maSanPham || '-' }}</td>
                    <td>{{ product.tenSanPham || '-' }}</td>
                    <td>{{ formatMoney(product.giaBan) }}</td>
                    <td>{{ product.soLuong ?? product.soLuongTon ?? '-' }}</td>
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

      <section v-if="selectedProductDetails.length" class="selected-details">
        <div class="selected-details-header">
          <h4>Chi tiết sản phẩm đã chọn</h4>
          <span
            >{{ filteredSelectedProducts.length }} / {{ selectedProductDetails.length }} sản
            phẩm</span
          >
        </div>

        <div class="selected-filter-bar">
          <div class="selected-search">
            <input
              v-model.trim="selectedFilters.keyword"
              type="text"
              placeholder="Tìm theo tên hoặc mã..."
            />
            <button type="button" aria-label="Tìm kiếm sản phẩm đã chọn">
              <i class="fa-solid fa-magnifying-glass"></i>
            </button>
          </div>

          <div class="selected-filter">
            <label>Hãng</label>
            <select v-model="selectedFilters.thuongHieu">
              <option value="">Tất cả</option>
              <option v-for="option in brandOptions" :key="option" :value="option">
                {{ option }}
              </option>
            </select>
          </div>

          <div class="selected-filter">
            <label>Màu sắc</label>
            <select v-model="selectedFilters.mauSac">
              <option value="">Tất cả</option>
              <option v-for="option in colorOptions" :key="option" :value="option">
                {{ option }}
              </option>
            </select>
          </div>

          <div class="selected-filter">
            <label>Chất liệu khung</label>
            <select v-model="selectedFilters.chatLieuKhung">
              <option value="">Tất cả</option>
              <option v-for="option in frameMaterialOptions" :key="option" :value="option">
                {{ option }}
              </option>
            </select>
          </div>

          <div class="selected-filter">
            <label>Độ cứng</label>
            <select v-model="selectedFilters.doCung">
              <option value="">Tất cả</option>
              <option v-for="option in hardnessOptions" :key="option" :value="option">
                {{ option }}
              </option>
            </select>
          </div>

          <div class="selected-filter">
            <label>Giá bán</label>
            <select v-model="selectedFilters.giaBan">
              <option value="">Tất cả</option>
              <option value="under-2000000">Dưới 2 triệu</option>
              <option value="2000000-3000000">2 - 3 triệu</option>
              <option value="over-3000000">Trên 3 triệu</option>
            </select>
          </div>
        </div>

        <div class="selected-group-table-wrap">
          <table class="selected-group-table">
            <thead>
              <tr>
                <th scope="col">
                  <input
                    type="checkbox"
                    :checked="isAllFilteredSelected"
                    :disabled="!filteredSelectedProducts.length"
                    @change="$emit('toggle-select-all-selected', filteredSelectedIds)"
                  />
                </th>
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
              <tr v-if="filteredSelectedProducts.length === 0">
                <td class="state-cell" colspan="15">Không có sản phẩm phù hợp bộ lọc</td>
              </tr>

              <tr
                v-for="(product, index) in filteredSelectedProducts"
                :key="product.idChiTietSanPham"
              >
                <td>
                  <input
                    type="checkbox"
                    :checked="selectedProductIds.includes(product.idChiTietSanPham)"
                    @change="$emit('toggle-product-selection', product)"
                  />
                </td>
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
      </section>
    </section>
  </div>
</template>

<script setup>
import { computed, reactive } from 'vue'

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
  minDateTime: {
    type: String,
    default: '',
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
  isAllSelected: {
    type: Boolean,
    default: false,
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
  'toggle-select-all-selected',
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

const endDateMin = computed(() => props.form.ngayBatDau || props.minDateTime)

const selectedFilters = reactive({
  keyword: '',
  thuongHieu: '',
  mauSac: '',
  chatLieuKhung: '',
  doCung: '',
  giaBan: '',
})

const normalizeText = (value) =>
  String(value ?? '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .trim()

const uniqueOptions = (values) =>
  Array.from(new Set(values.filter(Boolean).map((value) => String(value).trim()))).sort((a, b) =>
    a.localeCompare(b, 'vi'),
  )

const brandOptions = computed(() =>
  uniqueOptions(props.selectedProductDetails.map((product) => product.tenThuongHieu)),
)

const colorOptions = computed(() =>
  uniqueOptions(props.selectedProductDetails.map((product) => product.tenMauSac)),
)

const frameMaterialOptions = computed(() =>
  uniqueOptions(props.selectedProductDetails.map((product) => product.tenChatLieuKhungVot)),
)

const hardnessOptions = computed(() =>
  uniqueOptions(props.selectedProductDetails.map((product) => product.tenDoCung)),
)

const matchesPriceFilter = (price, filter) => {
  if (!filter) return true

  const value = Number(price ?? 0)

  switch (filter) {
    case 'under-2000000':
      return value < 2000000
    case '2000000-3000000':
      return value >= 2000000 && value <= 3000000
    case 'over-3000000':
      return value > 3000000
    default:
      return true
  }
}

const filteredSelectedProducts = computed(() => {
  const keyword = normalizeText(selectedFilters.keyword)

  return props.selectedProductDetails.filter((product) => {
    if (keyword) {
      const searchable = normalizeText(
        `${product.maCtsp || product.maSanPham || ''} ${product.tenSanPham || ''} ${product.tenThuongHieu || ''}`,
      )
      if (!searchable.includes(keyword)) {
        return false
      }
    }

    if (selectedFilters.thuongHieu && product.tenThuongHieu !== selectedFilters.thuongHieu) {
      return false
    }

    if (selectedFilters.mauSac && product.tenMauSac !== selectedFilters.mauSac) {
      return false
    }

    if (
      selectedFilters.chatLieuKhung &&
      product.tenChatLieuKhungVot !== selectedFilters.chatLieuKhung
    ) {
      return false
    }

    if (selectedFilters.doCung && product.tenDoCung !== selectedFilters.doCung) {
      return false
    }

    if (!matchesPriceFilter(product.giaBan, selectedFilters.giaBan)) {
      return false
    }

    return true
  })
})

const filteredSelectedIds = computed(() =>
  filteredSelectedProducts.value
    .map((product) => product.idChiTietSanPham)
    .filter((id) => id !== null && id !== undefined),
)

const isAllFilteredSelected = computed(
  () =>
    filteredSelectedIds.value.length > 0 &&
    filteredSelectedIds.value.every((id) => props.selectedProductIds.includes(id)),
)
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

.selected-filter-bar {
  display: grid;
  grid-template-columns: 1.6fr repeat(5, minmax(0, 1fr));
  gap: 12px;
  align-items: end;
  margin-bottom: 14px;
}

.selected-search {
  display: flex;
  gap: 8px;
}

.selected-search input,
.selected-filter select {
  width: 100%;
  height: 38px;
  border: 1px solid #d1d5db;
  border-radius: 10px;
  padding: 0 12px;
  font-size: 13px;
  background: #ffffff;
  outline: none;
}

.selected-search button {
  width: 44px;
  height: 38px;
  border: none;
  border-radius: 10px;
  background: #f79b66;
  color: #ffffff;
  cursor: pointer;
}

.selected-filter {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.selected-filter label {
  font-size: 12px;
  font-weight: 600;
  color: #111827;
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
  min-width: 1400px;
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
