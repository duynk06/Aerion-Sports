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
              <span>{{ selectedProductsCount }} biến thể</span>
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
            <span>Hiển thị {{ visibleProducts.length }} dòng sản phẩm cha</span>
          </div>

          <div class="product-table-wrap">
            <table class="product-table">
              <thead>
                <tr>
                  <th style="width: 50px; text-align: center;">#</th>
                  <th style="width: 40px; text-align: center;">
                    <input
                      type="checkbox"
                      :checked="isAllVisibleGroupSelected"
                      :disabled="!visibleProducts.length"
                      @change="toggleSelectAllVisibleGroups"
                    />
                  </th>
                  <th style="width: 140px; text-align: left;">Mã Sản Phẩm</th>
                  <th style="text-align: left;">Tên sản phẩm / Biến thể thuộc tính</th>
                </tr>
              </thead>

              <tbody>
                <tr v-if="productLoading">
                  <td colspan="4" class="state-cell">Đang tải cấu trúc sản phẩm...</td>
                </tr>
                <tr v-else-if="productError">
                  <td colspan="4" class="state-cell error-cell">{{ productError }}</td>
                </tr>
                <tr v-else-if="!visibleProducts.length">
                  <td colspan="4" class="state-cell">Không có sản phẩm phù hợp</td>
                </tr>

                <template v-else v-for="(spCha, index) in visibleProducts" :key="spCha.idSanPhamCha">
                  <tr class="parent-row-tr">
                    <td style="text-align: center;">
                      <button type="button" class="btn-toggle-accordion" @click="toggleGroupExpand(spCha.idSanPhamCha)">
                        <i :class="['fa-solid', expandedGroupIds.includes(spCha.idSanPhamCha) ? 'fa-minus' : 'fa-plus']"></i>
                      </button>
                    </td>
                    <td style="text-align: center;">
                      <input
                        type="checkbox"
                        :checked="isAllChildrenSelected(spCha)"
                        @change="toggleSelectAllChildren($event, spCha)"
                      />
                    </td>
                    <td style="text-align: left; font-weight: bold; color: #475569;">{{ spCha.maSanPham || '-' }}</td>
                    <td style="text-align: left; font-weight: 600;">{{ spCha.tenSanPham || '-' }}</td>
                  </tr>

                  <template v-if="expandedGroupIds.includes(spCha.idSanPhamCha)">
                    <tr v-for="btCon in spCha.mangBienTheCon" :key="btCon.idChiTietSanPham" class="child-row-tr">
                      <td></td>
                      <td style="text-align: center;">
                        <input
                          type="checkbox"
                          :checked="selectedProductIds.includes(btCon.idChiTietSanPham)"
                          @change="$emit('toggle-product-selection', btCon)"
                        />
                      </td>
                      <td style="text-align: left; padding-left: 20px;" class="product-code-text">{{ btCon.maCtsp }}</td>
                      <td style="text-align: left;">
                        <div class="child-variant-details-flex">
                          <span class="variant-spec-label">Màu: {{ btCon.tenMauSac || 'Mặc định' }}</span>
                          <span class="variant-spec-label">Size: {{ btCon.tenTrongLuong || 'Mặc định' }}</span>
                          <span class="variant-spec-label" v-if="btCon.tenDoCung">Độ cứng: {{ btCon.tenDoCung }}</span>
                          <span class="variant-spec-price">{{ formatMoney(btCon.giaBan) }}</span>
                        </div>
                      </td>
                    </tr>
                  </template>
                </template>
              </tbody>
            </table>
          </div>

          <div class="pagination">
            <div class="page-center">
              <button type="button" @click="$emit('prev-product-page')" :disabled="productPage === 0">
                <i class="fa-solid fa-chevron-left"></i>
              </button>
              <span>Trang {{ productPage + 1 }} / {{ productTotalPagesDisplay }}</span>
              <button type="button" @click="$emit('next-product-page')" :disabled="productPage + 1 >= productTotalPages">
                <i class="fa-solid fa-chevron-right"></i>
              </button>
            </div>
          </div>
        </div>
      </div>

      <section v-if="selectedProductDetails.length" class="selected-details">
        <div class="selected-details-header">
          <h4>Chi tiết sản phẩm đã chọn áp dụng ({{ filteredSelectedProducts.length }} biến thể)</h4>
          <span>{{ filteredSelectedProducts.length }} / {{ selectedProductDetails.length }} sản phẩm</span>
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
                <th scope="col">Chất liệu thân</th>
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

              <tr v-for="(product, index) in filteredSelectedProducts" :key="product.idChiTietSanPham">
                <td>
                  <input
                    type="checkbox"
                    :checked="selectedProductIds.includes(product.idChiTietSanPham)"
                    @change="$emit('toggle-product-selection', product)"
                  />
                </td>
                <td>{{ index + 1 }}</td>
                <td>{{ product.maCtsp || product.maSanPham || '-' }}</td>
                <td style="text-align: left; font-weight: 500;">{{ product.tenSanPham || '-' }}</td>
                <td>{{ formatText(product.tenThuongHieu) }}</td>
                <td>{{ formatText(product.tenMauSac) }}</td>
                <td>{{ formatText(product.tenTrongLuong) }}</td>
                <td>{{ formatText(product.tenDoCung) }}</td>
                <td>{{ formatText(product.tenDiemCanBang) }}</td>
                <td>{{ formatText(product.tenChatLieuThanVot) }}</td>
                <td>{{ formatText(product.tenChatLieuKhungVot) }}</td>
                <td>{{ formatText(product.tenChuViCanVot || product.chuViCanVot) }}</td>
                <td>{{ formatText(product.tenXuatXu || product.xuatXuChiTiet) }}</td>
                <td style="font-weight: bold;">{{ formatText(product.soLuong ?? product.soLuongTon) }}</td>
                <td style="font-weight: 700; color: #2563eb;">{{ formatMoney(product.giaBan) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </section>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'

const props = defineProps({
  errorMessage: { type: String, default: '' },
  form: { type: Object, required: true },
  isAllVisibleSelected: { type: Boolean, default: false },
  minDateTime: { type: String, default: '' },
  productError: { type: String, default: '' },
  productKeyword: { type: String, default: '' },
  productLoading: { type: Boolean, default: false },
  productPage: { type: Number, default: 0 },
  productPageSize: { type: Number, default: 5 },
  productTotalPages: { type: Number, default: 1 },
  productTotalPagesDisplay: { type: Number, default: 1 },
  selectedProductDetails: { type: Array, default: () => [], },
  selectedProductIds: { type: Array, default: () => [], },
  selectedProductsCount: { type: Number, default: 0 },
  isAllSelected: { type: Boolean, default: false },
  submitting: { type: Boolean, default: false },
  visibleProducts: { type: Array, default: () => [], },
})

const emit = defineEmits([
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

// Mảng theo dõi ID các sản phẩm cha đang được bấm mở rộng (+)
const expandedGroupIds = ref([])

// Hàm hỗ trợ Đóng/Mở danh mục con khi nhấn vào nút cộng/trừ
const toggleGroupExpand = (idCha) => {
  const index = expandedGroupIds.value.indexOf(idCha)
  if (index > -1) {
    expandedGroupIds.value.splice(index, 1) // Đóng lại
  } else {
    expandedGroupIds.value.push(idCha) // Mở rộng ra
  }
}

// Kiểm tra xem toán bộ biến thể con của 1 nhóm sản phẩm cha cụ thể đã được chọn chưa
const isAllChildrenSelected = (spCha) => {
  if (!spCha.mangBienTheCon || !spCha.mangBienTheCon.length) return false
  return spCha.mangBienTheCon.every(bt => props.selectedProductIds.includes(bt.idChiTietSanPham))
}

// Logic Tích/Bỏ tích dòng sản phẩm cha tổng quát -> Tự động kích hoạt hàng loạt con
const toggleSelectAllChildren = (event, spCha) => {
  const isChecked = event.target.checked
  if (!spCha.mangBienTheCon) return
  
  spCha.mangBienTheCon.forEach(bt => {
    const isCurrentlySelected = props.selectedProductIds.includes(bt.idChiTietSanPham)
    if ((isChecked && !isCurrentlySelected) || (!isChecked && isCurrentlySelected)) {
      emit('toggle-product-selection', bt)
    }
  })
}

// Kiểm tra xem tất cả các nhóm sản phẩm đang hiển thị trên trang hiện tại đã được chọn sạch chưa
const isAllVisibleGroupSelected = computed(() => {
  if (!props.visibleProducts.length) return false
  return props.visibleProducts.every(group => isAllChildrenSelected(group))
})

// Tích chọn hàng loạt tất cả các nhóm và biến thể đang xuất hiện trên trang hiện hành
const toggleSelectAllVisibleGroups = (event) => {
  const isChecked = event.target.checked
  props.visibleProducts.forEach(group => {
    if (group.mangBienTheCon) {
      group.mangBienTheCon.forEach(bt => {
        const isCurrentlySelected = props.selectedProductIds.includes(bt.idChiTietSanPham)
        if ((isChecked && !isCurrentlySelected) || (!isChecked && isCurrentlySelected)) {
          emit('toggle-product-selection', bt)
        }
      })
    }
  })
}

const formatText = (value) => {
  if (value === null || value === undefined || value === '') return '-'
  return String(value)
}

const formatMoney = (value) => {
  if (value === null || value === undefined || value === '') return '-'
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

const brandOptions = computed(() => uniqueOptions(props.selectedProductDetails.map((product) => product.tenThuongHieu)))
const colorOptions = computed(() => uniqueOptions(props.selectedProductDetails.map((product) => product.tenMauSac)))
const frameMaterialOptions = computed(() => uniqueOptions(props.selectedProductDetails.map((product) => product.tenChatLieuKhungVot)))
const hardnessOptions = computed(() => uniqueOptions(props.selectedProductDetails.map((product) => product.tenDoCung)))

const matchesPriceFilter = (price, filter) => {
  if (!filter) return true
  const value = Number(price ?? 0)
  switch (filter) {
    case 'under-2000000': return value < 2000000
    case '2000000-3000000': return value >= 2000000 && value <= 3000000
    case 'over-3000000': return value > 3000000
    default: return true
  }
}

const filteredSelectedProducts = computed(() => {
  const keyword = normalizeText(selectedFilters.keyword)
  return props.selectedProductDetails.filter((product) => {
    if (keyword) {
      const searchable = normalizeText(`${product.maCtsp || product.maSanPham || ''} ${product.tenSanPham || ''} ${product.tenThuongHieu || ''}`)
      if (!searchable.includes(keyword)) return false
    }
    if (selectedFilters.thuongHieu && product.tenThuongHieu !== selectedFilters.thuongHieu) return false
    if (selectedFilters.mauSac && product.tenMauSac !== selectedFilters.mauSac) return false
    if (selectedFilters.chatLieuKhung && product.tenChatLieuKhungVot !== selectedFilters.chatLieuKhung) return false
    if (selectedFilters.doCung && product.tenDoCung !== selectedFilters.doCung) return false
    if (!matchesPriceFilter(product.giaBan, selectedFilters.giaBan)) return false
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
/* ==========================================================================
   ⚡ CSS ĐỒNG BỘ: Thiết lập giao diện Accordion đóng mở lồng bảng con chuẩn SevenStrike
   ========================================================================== */
.btn-toggle-accordion {
  background: none;
  border: 1px solid #cbd5e1;
  color: #64748b;
  width: 24px;
  height: 24px;
  border-radius: 4px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  transition: all 0.2s;
}

.btn-toggle-accordion:hover {
  background-color: #f1f5f9;
  color: #1e293b;
  border-color: #94a3b8;
}

/* Định hình giao diện hàng cha (Dòng Sản phẩm) */
.parent-row-tr {
  background-color: #fff7ed !important; /* Phủ cam nhạt nền */
}
.parent-row-tr td {
  border-bottom: 1px solid #fed7aa !important;
  font-size: 12.5px !important;
}

/* Định hình giao diện hàng con (Dòng biến thể cấu hình) */
.child-row-tr {
  background-color: #ffffff !important;
}
.child-row-tr:hover {
  background-color: #f8fafc !important;
}
.child-row-tr td {
  border-bottom: 1px solid #f1f5f9 !important;
  padding: 10px 8px !important;
}

.child-variant-details-flex {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.variant-spec-label {
  background-color: #f1f5f9;
  color: #475569;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
}

.variant-spec-price {
  font-weight: 600;
  color: #2563eb;
  margin-left: auto; /* Đẩy giá bán dạt sang góc phải cho gọn */
}

/* Các thuộc tính cấu trúc bảng và bộ lọc gốc của bạn */
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
  max-height: 480px;
}

.create-product-panel .product-table,
.selected-details .selected-group-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 12px;
}

.create-product-panel .product-table {
  min-width: 500px;
}

.create-product-panel .product-table countries th,
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

.selected-details .selected-group-table-wrap {
  overflow-x: auto;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
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
</style>