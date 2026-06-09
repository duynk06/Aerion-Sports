<template>
  <MainLayout
    :title="isCreateMode || isEditMode ? 'Thêm/Sửa đợt giảm giá' : 'Quản lý đợt giảm giá'"
  >
    <div class="dot-giam-gia-page">
      <div v-if="!isCreateMode && !isEditMode" class="page-shell">
        <section class="card filter-card">
          <div class="card-header">
            <i class="fa-solid fa-filter"></i>
            Bộ lọc tìm kiếm
          </div>

          <div class="filter-grid">
            <div>
              <label for="discount-search">Tìm kiếm</label>
              <input
                id="discount-search"
                v-model.trim="filters.keyword"
                type="text"
                placeholder="Nhập mã / tên đợt giảm giá..."
                @keyup.enter="applyFilter"
              />
            </div>

            <div>
              <label for="discount-status">Trạng thái</label>
              <select id="discount-status" v-model="filters.trangThai">
                <option value="">Tất cả</option>
                <option value="0">Đã hủy</option>
                <option value="1">Sắp diễn ra</option>
                <option value="2">Đang diễn ra</option>
                <option value="3">Đã kết thúc</option>
              </select>
            </div>

            <div>
              <label for="from-date">Từ ngày</label>
              <input id="from-date" v-model.trim="filters.tuNgay" type="date" />
            </div>

            <div>
              <label for="to-date">Đến ngày</label>
              <input id="to-date" v-model.trim="filters.denNgay" type="date" />
            </div>
          </div>

          <div class="filter-action">
            <button class="btn-reset" type="button" @click="resetFilter">
              <i class="fa-solid fa-rotate-right"></i>
              Đặt lại
            </button>

            <button class="btn-search" type="button" @click="applyFilter">
              <i class="fa-solid fa-magnifying-glass"></i>
              Lọc dữ liệu
            </button>
          </div>

          <p v-if="filterError" class="filter-error">{{ filterError }}</p>
        </section>

        <section class="card">
          <div class="list-header">
            <div class="list-title">
              <div class="title-icon">
                <i class="fa-solid fa-percent"></i>
              </div>

              <div>
                <h3>Danh sách đợt giảm giá</h3>
              </div>
            </div>

            <button class="btn-add" type="button" @click="handleAdd">
              <i class="fa-solid fa-plus"></i>
              Thêm mới
            </button>
          </div>

          <!--
            <button
              type="button"
              :class="{ active: filters.trangThai === '' }"
              @click="setStatusFilter('')"
            >
              Tất cả
            </button>
            <button
              type="button"
              :class="{ active: filters.trangThai === '1' }"
              @click="setStatusFilter('1')"
            >
              Sắp diễn ra
            </button>
            <button
              type="button"
              :class="{ active: filters.trangThai === '2' }"
              @click="setStatusFilter('2')"
            >
              Đang diễn ra
            </button>
            <button
              type="button"
              :class="{ active: filters.trangThai === '3' }"
              @click="setStatusFilter('3')"
            >
              Đã kết thúc
            </button>
            <button
              type="button"
              :class="{ active: filters.trangThai === '0' }"
              @click="setStatusFilter('0')"
            >
              Đã hủy
            </button>
          </div>
          -->

          <table class="discount-table">
            <colgroup>
              <col style="width: 5%" />
              <col style="width: 14%" />
              <col style="width: 20%" />
              <col style="width: 12%" />
              <col style="width: 20%" />
              <col style="width: 14%" />
              <col style="width: 15%" />
            </colgroup>

            <thead>
              <tr>
                <th scope="col">STT</th>
                <th scope="col">Mã DGG</th>
                <th scope="col">Tên DGG</th>
                <th scope="col">Giá trị</th>
                <th scope="col">Thời gian</th>
                <th scope="col">Trạng thái</th>
                <th scope="col">Hành động</th>
              </tr>
            </thead>

            <tbody>
              <tr v-if="loading">
                <td class="state-cell" colspan="7">Đang tải dữ liệu...</td>
              </tr>

              <tr v-else-if="errorMessage">
                <td class="state-cell error-cell" colspan="7">{{ errorMessage }}</td>
              </tr>

              <template v-else-if="rows.length">
                <tr v-for="(item, index) in rows" :key="item.id">
                  <td>{{ page * size + index + 1 }}</td>
                  <td>{{ item.maDotGiamGia || '-' }}</td>
                  <td>{{ item.tenDotGiamGia || '-' }}</td>
                  <td>{{ formatDiscountValue(item) }}</td>
                  <td>{{ formatRange(item.ngayBatDau, item.ngayKetThuc) }}</td>
                  <td>
                    <span class="status-badge" :class="getStatusClass(item.trangThai)">
                      {{ item.trangThaiText || formatTrangThaiText(item.trangThai) }}
                    </span>
                  </td>
                  <td>
                    <div class="row-actions">
                      <button class="action-btn" type="button" @click="handleEdit(item)">
                        <i class="fa-solid fa-pen-to-square"></i>
                      </button>
                      <button class="action-btn" type="button" @click="handleView(item)">
                        <i class="fa-solid fa-eye"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              </template>

              <tr v-else>
                <td class="state-cell" colspan="7">Không có dữ liệu</td>
              </tr>
            </tbody>
          </table>

          <div class="pagination">
            <div class="page-center">
              <button type="button" @click="prevPage" :disabled="page === 0">
                <i class="fa-solid fa-chevron-left"></i>
              </button>

              <span>Trang {{ page + 1 }} / {{ totalPagesDisplay }}</span>

              <button type="button" @click="nextPage" :disabled="page + 1 >= totalPages">
                <i class="fa-solid fa-chevron-right"></i>
              </button>
            </div>
          </div>
        </section>
      </div>

      <DotGiamGiaCreateModal
        v-else-if="isCreateMode"
        :error-message="createError"
        :form="createForm"
        :is-all-visible-selected="isAllVisibleSelected"
        :product-error="productError"
        :product-keyword="productKeyword"
        :product-loading="productLoading"
        :product-page="productPage"
        :product-page-size="productPageSize"
        :product-total-pages="productTotalPages"
        :product-total-pages-display="productTotalPagesDisplay"
        :selected-product-ids="selectedProductIds"
        :selected-products-count="selectedProductsCount"
        :selected-product-details="selectedProductDetails"
        :is-all-selected="isAllSelected"
        :submitting="isSubmittingCreate"
        :visible-products="visibleProducts"
        @back="handleBackToList"
        @next-product-page="nextProductPage"
        @prev-product-page="prevProductPage"
        @save="saveCreate"
        @search-products="searchProducts"
        @toggle-product-selection="toggleProductSelection"
        @toggle-select-all-visible="toggleSelectAllVisible"
        @update:productKeyword="productKeyword = $event"
      />

      <DotGiamGiaEditModal
        v-else
        :error-message="editError"
        :form="editForm"
        :is-all-visible-selected="isAllVisibleSelected"
        :product-error="productError"
        :product-keyword="productKeyword"
        :product-loading="productLoading"
        :product-page="productPage"
        :product-page-size="productPageSize"
        :product-total-pages="productTotalPages"
        :product-total-pages-display="productTotalPagesDisplay"
        :selected-product-ids="selectedProductIds"
        :selected-products-count="selectedProductsCount"
        :selected-product-details="selectedProductDetails"
        :is-all-selected="isAllSelected"
        :submitting="isSubmittingEdit"
        :visible-products="visibleProducts"
        @back="handleBackToList"
        @next-product-page="nextProductPage"
        @prev-product-page="prevProductPage"
        @save="saveEdit"
        @search-products="searchProducts"
        @toggle-product-selection="toggleProductSelection"
        @toggle-select-all-visible="toggleSelectAllVisible"
        @update:productKeyword="productKeyword = $event"
      />

      <DotGiamGiaDetailModal
        v-if="isDetailOpen"
        :detail="detailData"
        :error-message="detailError"
        :loading="detailLoading"
        @close="closeDetail"
      />
    </div>
  </MainLayout>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import MainLayout from '../layouts/MainLayout.vue'
import DotGiamGiaCreateModal from '../components/modals/DotGiamGiaCreateModal.vue'
import DotGiamGiaDetailModal from '../components/modals/DotGiamGiaDetailModal.vue'
import DotGiamGiaEditModal from '../components/modals/DotGiamGiaEditModal.vue'
import {
  createDotGiamGia,
  fetchDotGiamGiaById,
  fetchDotGiamGiaPage,
  fetchDotGiamGiaProducts,
  updateDotGiamGia,
} from '../service/DotGiamGiaService'

const loading = ref(false)
const errorMessage = ref('')
const rows = ref([])

const page = ref(0)
const size = ref(5)
const totalPages = ref(0)
const totalPagesDisplay = ref(1)

const filters = reactive({
  keyword: '',
  trangThai: '',
  tuNgay: '',
  denNgay: '',
})

const filterError = ref('')

const isCreateMode = ref(false)
const isEditMode = ref(false)
const isDetailOpen = ref(false)
const detailLoading = ref(false)
const detailError = ref('')
const detailData = ref(null)

const createError = ref('')
const isSubmittingCreate = ref(false)
const createForm = reactive({
  tenDotGiamGia: '',
  giaTriGiam: null,
  ngayBatDau: '',
  ngayKetThuc: '',
  moTa: '',
})

const editError = ref('')
const isSubmittingEdit = ref(false)
const editForm = reactive({
  id: null,
  maDotGiamGia: '',
  tenDotGiamGia: '',
  giaTriGiam: null,
  ngayBatDau: '',
  ngayKetThuc: '',
  moTa: '',
})

const productKeyword = ref('')
const productLoading = ref(false)
const productError = ref('')
const productRows = ref([])
const productPage = ref(0)
const productPageSize = ref(5)
const selectedProductIds = ref([])

const productTotalPages = computed(() =>
  Math.max(1, Math.ceil(productRows.value.length / productPageSize.value)),
)

const productTotalPagesDisplay = computed(() => {
  return productRows.value.length ? productTotalPages.value : 1
})

const visibleProducts = computed(() => {
  const start = productPage.value * productPageSize.value
  return productRows.value.slice(start, start + productPageSize.value)
})

const selectedProductsCount = computed(() => selectedProductIds.value.length)

const selectedProductDetails = computed(() => {
  const selectedIds = new Set(selectedProductIds.value)
  return productRows.value.filter((product) => selectedIds.has(product.idChiTietSanPham))
})

const isAllSelected = computed(() => {
  if (!selectedProductDetails.value.length) return false
  return selectedProductDetails.value.every((product) =>
    selectedProductIds.value.includes(product.idChiTietSanPham),
  )
})

const isAllVisibleSelected = computed(() => {
  if (!visibleProducts.value.length) return false
  return visibleProducts.value.every((product) =>
    selectedProductIds.value.includes(product.idChiTietSanPham),
  )
})

const normalizePageContent = (response) => {
  if (Array.isArray(response)) return response
  if (Array.isArray(response?.content)) return response.content
  return []
}

const normalizeUiErrorMessage = (error, fallback) => {
  const message = error?.message || fallback

  if (!message) {
    return fallback
  }

  if (message.includes('JDBC exception executing SQL') || message.includes('Invalid column name')) {
    return 'Không tải được dữ liệu. Vui lòng kiểm tra lại mapping database.'
  }

  return message
}

const validateDateFilters = () => {
  const from = filters.tuNgay || ''
  const to = filters.denNgay || ''

  if (!from && !to) {
    filterError.value = ''
    return true
  }

  if (!from || !to) {
    filterError.value = 'Vui lòng chọn đủ từ ngày và đến ngày'
    return false
  }

  if (from > to) {
    filterError.value = 'Ngày bắt đầu không được lớn hơn ngày kết thúc'
    return false
  }

  filterError.value = ''
  return true
}

const toLocalDateTimeValue = (value) => {
  if (!value) return ''

  if (typeof value === 'string') {
    const normalized = value.includes('T') ? value : value.replace(' ', 'T')
    return normalized.slice(0, 16)
  }

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return ''

  const pad = (num) => String(num).padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(
    date.getHours(),
  )}:${pad(date.getMinutes())}`
}

const toPayloadDateTime = (value) => {
  if (!value) return null
  return `${value}:00`
}

const resetCreateForm = () => {
  createForm.tenDotGiamGia = ''
  createForm.giaTriGiam = null
  createForm.ngayBatDau = ''
  createForm.ngayKetThuc = ''
  createForm.moTa = ''
  createError.value = ''
  selectedProductIds.value = []
}

const resetEditForm = () => {
  editForm.id = null
  editForm.maDotGiamGia = ''
  editForm.tenDotGiamGia = ''
  editForm.giaTriGiam = null
  editForm.ngayBatDau = ''
  editForm.ngayKetThuc = ''
  editForm.moTa = ''
  editError.value = ''
  selectedProductIds.value = []
}

const loadData = async () => {
  if (!validateDateFilters()) {
    return
  }

  loading.value = true
  errorMessage.value = ''

  try {
    const response = await fetchDotGiamGiaPage({
      keyword: filters.keyword,
      trangThai: filters.trangThai === '' ? '' : Number(filters.trangThai),
      tuNgay: filters.tuNgay,
      denNgay: filters.denNgay,
      page: page.value,
      size: size.value,
    })

    rows.value = normalizePageContent(response)
    totalPages.value = response?.totalPages ?? 0
    totalPagesDisplay.value = totalPages.value || 1
    page.value = response?.currentPage ?? page.value
  } catch (error) {
    rows.value = []
    totalPages.value = 0
    totalPagesDisplay.value = 1
    errorMessage.value = error?.message || 'Không thể tải dữ liệu đợt giảm giá'
  } finally {
    loading.value = false
  }
}

const loadProducts = async () => {
  productLoading.value = true
  productError.value = ''

  try {
    const response = await fetchDotGiamGiaProducts(productKeyword.value)
    productRows.value = Array.isArray(response) ? response : []
    productPage.value = 0
  } catch (error) {
    productRows.value = []
    productError.value = error?.message || 'Không thể tải danh sách sản phẩm'
  } finally {
    productLoading.value = false
  }
}

const applyFilter = async () => {
  page.value = 0
  await loadData()
}

const resetFilter = async () => {
  filters.keyword = ''
  filters.trangThai = ''
  filters.tuNgay = ''
  filters.denNgay = ''
  page.value = 0
  await loadData()
}

const prevPage = async () => {
  if (page.value <= 0) return
  page.value--
  await loadData()
}

const nextPage = async () => {
  if (page.value + 1 >= totalPages.value) return
  page.value++
  await loadData()
}

const prevProductPage = () => {
  if (productPage.value <= 0) return
  productPage.value--
}

const nextProductPage = () => {
  if (productPage.value + 1 >= productTotalPages.value) return
  productPage.value++
}

const toggleSelectAllSelected = (ids = []) => {
  const selectedIds = (Array.isArray(ids) && ids.length
    ? ids
    : selectedProductDetails.value.map((product) => product.idChiTietSanPham)
  ).filter((id) => id !== null && id !== undefined)

  if (!selectedIds.length) return

  if (isAllSelected.value) {
    selectedProductIds.value = selectedProductIds.value.filter((id) => !selectedIds.includes(id))
    return
  }

  const merged = new Set([...selectedProductIds.value, ...selectedIds])
  selectedProductIds.value = Array.from(merged)
}

const formatDate = (value) => {
  if (!value) return '-'

  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return String(value).replace('T', ' ').slice(0, 16)
  }

  return new Intl.DateTimeFormat('vi-VN').format(date)
}

const formatRange = (start, end) => {
  const from = formatDate(start)
  const to = formatDate(end)

  if (from === '-' && to === '-') return '-'
  return `${from} - ${to}`
}

const formatDiscountValue = (item) => {
  if (item?.giaTriGiam === null || item?.giaTriGiam === undefined) {
    return '-'
  }

  return `${Number(item.giaTriGiam).toLocaleString('vi-VN')}%`
}

const formatTrangThaiText = (trangThai) => {
  switch (Number(trangThai)) {
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

const toggleProductSelection = (product) => {
  const productId = product.idChiTietSanPham
  const index = selectedProductIds.value.indexOf(productId)

  if (index >= 0) {
    selectedProductIds.value = selectedProductIds.value.filter((id) => id !== productId)
    return
  }

  selectedProductIds.value = [...selectedProductIds.value, productId]
}

const toggleSelectAllVisible = () => {
  const currentIds = visibleProducts.value.map((product) => product.idChiTietSanPham)

  if (isAllVisibleSelected.value) {
    selectedProductIds.value = selectedProductIds.value.filter((id) => !currentIds.includes(id))
    return
  }

  const merged = new Set([...selectedProductIds.value, ...currentIds])
  selectedProductIds.value = Array.from(merged)
}

const handleAdd = async () => {
  isEditMode.value = false
  isCreateMode.value = true
  productKeyword.value = ''
  productRows.value = []
  productPage.value = 0
  productError.value = ''
  createError.value = ''
  resetCreateForm()
  await loadProducts()
}

const fillEditForm = (detail) => {
  editForm.id = detail.id
  editForm.maDotGiamGia = detail.maDotGiamGia || ''
  editForm.tenDotGiamGia = detail.tenDotGiamGia || ''
  editForm.giaTriGiam = detail.giaTriGiam ?? null
  editForm.ngayBatDau = toLocalDateTimeValue(detail.ngayBatDau)
  editForm.ngayKetThuc = toLocalDateTimeValue(detail.ngayKetThuc)
  editForm.moTa = detail.moTa || ''
  selectedProductIds.value = Array.isArray(detail.chiTietList)
    ? detail.chiTietList
        .filter((item) => item?.idChiTietSanPham)
        .map((item) => item.idChiTietSanPham)
    : []
}

const handleEdit = async (item) => {
  if (!item?.id) return

  isCreateMode.value = false
  isEditMode.value = true
  productKeyword.value = ''
  productRows.value = []
  productPage.value = 0
  productError.value = ''
  editError.value = ''
  resetEditForm()

  try {
    const [detail] = await Promise.all([fetchDotGiamGiaById(item.id), loadProducts()])
    fillEditForm(detail)
  } catch (error) {
    editError.value = error?.message || 'Không thể tải dữ liệu đợt giảm giá'
  }
}

const handleView = async (item) => {
  if (!item?.id) return

  isDetailOpen.value = true
  detailLoading.value = true
  detailError.value = ''
  detailData.value = null

  try {
    detailData.value = await fetchDotGiamGiaById(item.id)
  } catch (error) {
    detailError.value = error?.message || 'KhÃ´ng thá»ƒ táº£i chi tiáº¿t Ä‘á»£t giáº£m giÃ¡'
  } finally {
    detailLoading.value = false
  }
}

const closeDetail = () => {
  isDetailOpen.value = false
  detailLoading.value = false
  detailError.value = ''
  detailData.value = null
}

const handleBackToList = () => {
  closeDetail()
  isCreateMode.value = false
  isEditMode.value = false
  productKeyword.value = ''
  productRows.value = []
  productPage.value = 0
  productError.value = ''
  createError.value = ''
  editError.value = ''
  selectedProductIds.value = []
  resetCreateForm()
  resetEditForm()
}

const searchProducts = async () => {
  await loadProducts()
}

const buildPayload = (form) => ({
  tenDotGiamGia: form.tenDotGiamGia,
  giaTriGiam: form.giaTriGiam,
  ngayBatDau: toPayloadDateTime(form.ngayBatDau),
  ngayKetThuc: toPayloadDateTime(form.ngayKetThuc),
  moTa: form.moTa,
  chiTietList: selectedProductIds.value.map((idChiTietSanPham) => ({
    idChiTietSanPham,
  })),
})

const saveCreate = async () => {
  createError.value = ''

  if (!createForm.tenDotGiamGia.trim()) {
    createError.value = 'Vui lòng nhập tên đợt giảm giá'
    return
  }

  if (createForm.giaTriGiam === null || createForm.giaTriGiam === undefined) {
    createError.value = 'Vui lòng nhập giá trị giảm'
    return
  }

  if (!createForm.ngayBatDau) {
    createError.value = 'Vui lòng chọn ngày bắt đầu'
    return
  }

  if (!createForm.ngayKetThuc) {
    createError.value = 'Vui lòng chọn ngày kết thúc'
    return
  }

  if (selectedProductIds.value.length === 0) {
    createError.value = 'Vui lòng chọn ít nhất một sản phẩm'
    return
  }

  isSubmittingCreate.value = true

  try {
    await createDotGiamGia(buildPayload(createForm))
    isCreateMode.value = false
    resetCreateForm()
    productRows.value = []
    await loadData()
  } catch (error) {
    createError.value = error?.message || 'Không thể tạo đợt giảm giá'
  } finally {
    isSubmittingCreate.value = false
  }
}

const saveEdit = async () => {
  editError.value = ''

  if (!editForm.id) {
    editError.value = 'Không tìm thấy đợt giảm giá cần sửa'
    return
  }

  if (!editForm.tenDotGiamGia.trim()) {
    editError.value = 'Vui lòng nhập tên đợt giảm giá'
    return
  }

  if (editForm.giaTriGiam === null || editForm.giaTriGiam === undefined) {
    editError.value = 'Vui lòng nhập giá trị giảm'
    return
  }

  if (!editForm.ngayBatDau) {
    editError.value = 'Vui lòng chọn ngày bắt đầu'
    return
  }

  if (!editForm.ngayKetThuc) {
    editError.value = 'Vui lòng chọn ngày kết thúc'
    return
  }

  if (selectedProductIds.value.length === 0) {
    editError.value = 'Vui lòng chọn ít nhất một sản phẩm'
    return
  }

  isSubmittingEdit.value = true

  try {
    await updateDotGiamGia(editForm.id, buildPayload(editForm))
    handleBackToList()
    await loadData()
  } catch (error) {
    editError.value = error?.message || 'Không thể cập nhật đợt giảm giá'
  } finally {
    isSubmittingEdit.value = false
  }
}

/*
const handleDelete = async (item) => {
  if (!item?.id) return

  const confirmed = window.confirm(`Bạn có chắc muốn hủy đợt giảm giá ${item.maDotGiamGia || ''}?`)

  if (!confirmed) return

  try {
    await deleteDotGiamGia(item.id)
    await loadData()
  } catch (error) {
    errorMessage.value = error?.message || 'Không thể hủy đợt giảm giá'
  }
}
*/

onMounted(() => {
  loadData()
})

watch(
  () => [filters.tuNgay, filters.denNgay],
  () => {
    validateDateFilters()
  },
)
</script>

<style scoped>
.filter-error {
  margin-top: 10px;
  color: #dc2626;
  font-size: 0.95rem;
  font-weight: 600;
}
</style>
