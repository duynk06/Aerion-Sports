<template>
  <MainLayout title="Quản lý đợt giảm giá">
    <div class="dot-giam-gia-page">
      <div class="page-shell">
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
        </section>

        <section class="card">
          <div class="list-header">
            <div class="list-title">
              <div class="title-icon">
                <i class="fa-solid fa-percent"></i>
              </div>

              <div>
                <h3>Danh sách đợt giảm giá</h3>
                <span>Lọc nhanh theo trạng thái</span>
              </div>
            </div>

            <button class="btn-add" type="button" @click="handleAdd">
              <i class="fa-solid fa-plus"></i>
              Thêm mới
            </button>
          </div>

          <div class="status-tabs">
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
                <th scope="col">Giá trị giảm</th>
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
                    <span
                      class="status-badge"
                      :class="getStatusClass(item.trangThai)"
                    >
                      {{ item.trangThaiText || formatTrangThaiText(item.trangThai) }}
                    </span>
                  </td>
                  <td>
                    <div class="row-actions">
                      <button class="action-btn" type="button" @click="handleEdit(item)">
                        <i class="fa-solid fa-pen-to-square"></i>
                      </button>
                      <button class="action-btn" type="button" @click="handleDelete(item)">
                        <i class="fa-solid fa-trash-can"></i>
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

              <button
                type="button"
                @click="nextPage"
                :disabled="page + 1 >= totalPages"
              >
                <i class="fa-solid fa-chevron-right"></i>
              </button>
            </div>
          </div>
        </section>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import MainLayout from '../layouts/MainLayout.vue'
import { deleteDotGiamGia, fetchDotGiamGiaPage } from '../service/DotGiamGiaService'

const loading = ref(false)
const errorMessage = ref('')
const rows = ref([])

const page = ref(0)
const size = ref(5)
const totalPages = ref(0)

const filters = reactive({
  keyword: '',
  trangThai: '',
  tuNgay: '',
  denNgay: ''
})

const totalPagesDisplay = ref(1)

const normalizePageContent = (response) => {
  if (Array.isArray(response)) {
    return response
  }

  if (Array.isArray(response?.content)) {
    return response.content
  }

  return []
}

const loadData = async () => {
  loading.value = true
  errorMessage.value = ''

  try {
    const response = await fetchDotGiamGiaPage({
      keyword: filters.keyword,
      trangThai: filters.trangThai === '' ? '' : Number(filters.trangThai),
      tuNgay: filters.tuNgay,
      denNgay: filters.denNgay,
      page: page.value,
      size: size.value
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

const setStatusFilter = async (status) => {
  filters.trangThai = status
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

  return item.hinhThucGiam === 'phan_tram'
    ? `${item.giaTriGiam}%`
    : `${Number(item.giaTriGiam).toLocaleString('vi-VN')} đ`
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

const handleEdit = (item) => {
  console.log('Edit dot giam gia:', item)
}

const handleAdd = () => {
  console.log('Add dot giam gia')
}

const handleDelete = async (item) => {
  if (!item?.id) return

  const confirmed = window.confirm(
    `Bạn có chắc muốn hủy đợt giảm giá ${item.maDotGiamGia || ''}?`
  )

  if (!confirmed) return

  try {
    await deleteDotGiamGia(item.id)
    await loadData()
  } catch (error) {
    errorMessage.value = error?.message || 'Không thể hủy đợt giảm giá'
  }
}

onMounted(() => {
  loadData()
})
</script>
