<template>
  <div class="bg-white min-h-screen py-10">
    <div class="container mx-auto px-5 max-w-6xl">
      <div class="mb-8 text-sm text-gray-500 flex items-center gap-2">
        <router-link to="/" class="hover:text-primary-color transition-colors">Trang chủ</router-link>
        <span class="text-xs">&gt;</span>
        <span class="text-gray-800 font-medium">Hồ sơ</span>
      </div>

      <div v-if="isLoading" class="rounded-lg border border-gray-100 bg-white p-8 shadow-sm text-gray-600">
        Đang tải hồ sơ...
      </div>

      <div v-else-if="errorMessage" class="rounded-lg border border-red-100 bg-red-50 p-6 text-red-700">
        {{ errorMessage }}
      </div>

      <template v-else>
        <h1 class="text-2xl font-bold text-gray-900 mb-6">Hồ sơ của tôi</h1>

        <div class="flex flex-col md:flex-row gap-8">
          <div class="w-full md:w-1/3 lg:w-1/4">
            <div class="bg-white border border-gray-100 text-gray-900 rounded-lg p-6 sticky top-24 shadow-sm">
              <div class="flex flex-col items-center mb-6">
                <div class="w-20 h-20 bg-gray-100 text-gray-700 rounded-full flex items-center justify-center text-3xl font-bold mb-3 shadow-inner">
                  {{ avatarText }}
                </div>
                <h2 class="text-xl font-bold text-center">{{ displayName || 'Tài khoản' }}</h2>
                <p class="text-sm text-gray-500 mt-1 text-center">{{ profile.email || '-' }}</p>
              </div>

              <div class="border-t border-gray-100 pt-5 space-y-4 text-sm">
                <div>
                  <p class="text-gray-500 mb-1 text-xs font-semibold uppercase tracking-wider">Mã khách hàng</p>
                  <p class="font-bold text-[15px]">{{ profile.maKhachHang || '-' }}</p>
                </div>
                <div>
                  <p class="text-gray-500 mb-1 text-xs font-semibold uppercase tracking-wider">Tài khoản</p>
                  <p class="font-bold text-[15px]">{{ profile.email || '-' }}</p>
                </div>
                <div>
                  <p class="text-gray-500 mb-1 text-xs font-semibold uppercase tracking-wider">Điện thoại</p>
                  <p class="font-bold text-[15px]">{{ profile.sdt || '-' }}</p>
                </div>
                <div>
                  <p class="text-gray-500 mb-1 text-xs font-semibold uppercase tracking-wider">Địa chỉ mặc định</p>
                  <p class="font-bold text-[13px] leading-relaxed">
                    {{ defaultAddress.diaChiDayDu || 'Chưa có địa chỉ mặc định' }}
                  </p>
                </div>
              </div>
            </div>
          </div>

          <div class="w-full md:w-2/3 lg:w-3/4">
            <div class="bg-white rounded-lg shadow-sm border border-gray-100 p-8">
              <div class="mb-10">
                <h3 class="text-lg font-bold text-gray-900 mb-6">Thông tin cá nhân</h3>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Họ và tên</label>
                    <input
                      v-model="form.hoTen"
                      type="text"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Mã khách hàng</label>
                    <input
                      type="text"
                      :value="profile.maKhachHang || ''"
                      disabled
                      class="w-full bg-gray-50 border border-gray-200 text-gray-500 text-sm rounded block p-3 outline-none cursor-not-allowed"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Email</label>
                    <input
                      type="email"
                      :value="profile.email || ''"
                      disabled
                      class="w-full bg-gray-50 border border-gray-200 text-gray-500 text-sm rounded block p-3 outline-none cursor-not-allowed"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Số điện thoại</label>
                    <input
                      v-model="form.sdt"
                      type="text"
                      inputmode="numeric"
                      autocomplete="tel"
                      pattern="[0-9]*"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                      @input="handlePhoneInput('sdt', $event)"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Giới tính</label>
                    <select
                      v-model="form.gioiTinh"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                    >
                      <option :value="null">Chưa cập nhật</option>
                      <option :value="1">Nam</option>
                      <option :value="0">Nữ</option>
                      <option :value="2">Khác</option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Ngày sinh</label>
                    <input
                      v-model="form.ngaySinh"
                      type="date"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                    />
                  </div>
                </div>
              </div>

              <hr class="border-gray-100 mb-10" />

              <div class="mb-10">
                <h3 class="text-lg font-bold text-gray-900 mb-6">Địa chỉ nhận hàng mặc định</h3>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Tên người nhận</label>
                    <input
                      v-model="form.nguoiNhan"
                      type="text"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">SĐT người nhận</label>
                    <input
                      v-model="form.sdtNguoiNhan"
                      type="text"
                      inputmode="numeric"
                      autocomplete="tel"
                      pattern="[0-9]*"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                      @input="handlePhoneInput('sdtNguoiNhan', $event)"
                    />
                  </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-6">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Quốc gia</label>
                    <input
                      type="text"
                      value="Việt Nam"
                      disabled
                      class="w-full bg-gray-50 border border-gray-200 text-gray-500 text-sm rounded block p-3 outline-none cursor-not-allowed"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Tỉnh / Thành phố</label>
                    <select
                      v-model="form.provinceCode"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                      :disabled="provinceOptions.length === 0"
                      @change="handleProvinceChange"
                    >
                      <option value="">Chọn tỉnh / thành phố</option>
                      <option v-for="province in provinceOptions" :key="province.code" :value="String(province.code)">
                        {{ province.name }}
                      </option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Quận / Huyện</label>
                    <select
                      v-model="form.districtCode"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                      :disabled="districtOptions.length === 0"
                      @change="handleDistrictChange"
                    >
                      <option value="">Chọn quận / huyện</option>
                      <option v-for="district in districtOptions" :key="district.code" :value="String(district.code)">
                        {{ district.name }}
                      </option>
                    </select>
                  </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Phường / Xã</label>
                    <select
                      v-model="form.wardCode"
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                      :disabled="wardOptions.length === 0"
                      @change="handleWardChange"
                    >
                      <option value="">Chọn phường / xã</option>
                      <option v-for="ward in wardOptions" :key="ward.code" :value="String(ward.code)">
                        {{ ward.name }}
                      </option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">Địa chỉ chi tiết</label>
                    <input
                      v-model="form.diaChiChiTiet"
                      type="text"
                      placeholder="Số nhà, tên đường, thôn/xóm..."
                      class="w-full bg-white border border-gray-200 text-gray-900 text-sm rounded focus:ring-secondary-color focus:border-secondary-color block p-3 outline-none"
                    />
                  </div>
                </div>
              </div>

              <div class="flex items-center gap-4">
                <button
                  type="button"
                  class="bg-secondary-color hover:bg-red-600 text-white font-medium rounded text-sm px-8 py-3 transition-colors disabled:opacity-60 disabled:cursor-not-allowed"
                  :disabled="saving"
                  @click="saveProfile"
                >
                  {{ saving ? 'Đang lưu...' : 'Lưu hồ sơ' }}
                </button>
                <button
                  type="button"
                  class="bg-white border border-gray-300 hover:bg-gray-50 text-gray-700 font-medium rounded text-sm px-8 py-3 transition-colors"
                  @click="reloadAll"
                >
                  Tải lại
                </button>
              </div>

              <p v-if="saveMessage" class="mt-4 text-sm text-green-700 bg-green-50 border border-green-100 rounded p-3">
                {{ saveMessage }}
              </p>
              <p v-if="saveError" class="mt-4 text-sm text-red-700 bg-red-50 border border-red-100 rounded p-3">
                {{ saveError }}
              </p>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import {
  getDistrictsByProvinceCode,
  getOnlineCustomerProfile,
  getProvinces,
  getWardsByDistrictCode,
  updateOnlineCustomerProfile,
} from '../../services/api'

const router = useRouter()
const isLoading = ref(true)
const saving = ref(false)
const errorMessage = ref('')
const saveMessage = ref('')
const saveError = ref('')
const profile = ref({})
const defaultAddress = ref({})
const provinceOptions = ref([])
const districtOptions = ref([])
const wardOptions = ref([])

const form = reactive({
  hoTen: '',
  sdt: '',
  gioiTinh: null,
  ngaySinh: '',
  nguoiNhan: '',
  sdtNguoiNhan: '',
  tinhThanh: '',
  phuongXa: '',
  provinceCode: '',
  districtCode: '',
  wardCode: '',
  diaChiChiTiet: '',
})
const PHONE_REGEX = /^0\d{9}$/

const displayName = computed(() => String(profile.value.ten || profile.value.hoTen || '').trim())

const avatarText = computed(() => {
  const name = displayName.value
  return name ? name.charAt(0).toUpperCase() : 'T'
})

const handlePhoneInput = (field, event) => {
  const rawValue = String(event?.target?.value || '')
  const digitsOnly = rawValue.replace(/\D/g, '').slice(0, 10)
  if (field === 'sdt' || field === 'sdtNguoiNhan') {
    form[field] = digitsOnly
  }
}

const normalizeText = (value) =>
  String(value || '')
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '')
    .toLowerCase()
    .replace(/[^\p{L}\p{N}\s]/gu, ' ')
    .replace(/\s+/g, ' ')
    .trim()

const findBestMatch = (items, label) => {
  const needle = normalizeText(label)
  if (!needle) return null

  return items.find((item) => {
    const haystack = normalizeText(item?.name || item?.name_with_type || '')
    return haystack === needle || haystack.includes(needle) || needle.includes(haystack)
  }) || null
}

const splitSavedDetail = (value) => {
  const raw = String(value || '').trim()
  if (!raw.includes(' | ')) {
    return { districtName: '', detail: raw }
  }

  const [districtName, ...rest] = raw.split(' | ')
  return {
    districtName: districtName.trim(),
    detail: rest.join(' | ').trim(),
  }
}

const buildSavedDetail = () => {
  const parts = []
  if (form.districtCode) {
    const district = districtOptions.value.find((item) => String(item.code) === String(form.districtCode))
    if (district) {
      parts.push(district.name)
    }
  }
  if (form.diaChiChiTiet.trim()) {
    parts.push(form.diaChiChiTiet.trim())
  }
  return parts.join(' | ')
}

const syncFormFromProfile = () => {
  const savedDetail = splitSavedDetail(defaultAddress.value.diaChiChiTiet || '')

  form.hoTen = profile.value.ten || profile.value.hoTen || ''
  form.sdt = profile.value.sdt || ''
  form.gioiTinh = profile.value.gioiTinh ?? null
  form.ngaySinh = profile.value.ngaySinh || ''
  form.nguoiNhan = defaultAddress.value.nguoiNhan || displayName.value || ''
  form.sdtNguoiNhan = defaultAddress.value.sdt || profile.value.sdt || ''
  form.tinhThanh = defaultAddress.value.tinhThanh || ''
  form.phuongXa = defaultAddress.value.phuongXa || ''
  form.diaChiChiTiet = savedDetail.detail || ''

  if (!form.nguoiNhan) {
    form.nguoiNhan = displayName.value || ''
  }
  if (!form.sdtNguoiNhan) {
    form.sdtNguoiNhan = profile.value.sdt || ''
  }

  return savedDetail
}

const loadProvinces = async () => {
  const { data } = await getProvinces()
  provinceOptions.value = Array.isArray(data) ? data : []
}

const loadDistricts = async (provinceCode) => {
  districtOptions.value = []
  wardOptions.value = []
  form.districtCode = ''
  form.wardCode = ''

  if (!provinceCode) {
    return
  }

  const { data } = await getDistrictsByProvinceCode(provinceCode)
  districtOptions.value = Array.isArray(data?.districts) ? data.districts : []
}

const loadWards = async (districtCode) => {
  wardOptions.value = []
  form.wardCode = ''

  if (!districtCode) {
    return
  }

  const { data } = await getWardsByDistrictCode(districtCode)
  wardOptions.value = Array.isArray(data?.wards) ? data.wards : []
}

const applySavedAddressSelections = async () => {
  const savedDetail = splitSavedDetail(defaultAddress.value.diaChiChiTiet || '')
  const provinceMatch = findBestMatch(provinceOptions.value, defaultAddress.value.tinhThanh)

  if (provinceMatch) {
    form.provinceCode = String(provinceMatch.code)
    form.tinhThanh = provinceMatch.name || provinceMatch.name_with_type || ''
    await loadDistricts(form.provinceCode)
  }

  const districtMatch = findBestMatch(districtOptions.value, savedDetail.districtName)
  if (districtMatch) {
    form.districtCode = String(districtMatch.code)
    await loadWards(form.districtCode)
  }

  const wardMatch = findBestMatch(wardOptions.value, defaultAddress.value.phuongXa)
  if (wardMatch) {
    form.wardCode = String(wardMatch.code)
    form.phuongXa = wardMatch.name || wardMatch.name_with_type || ''
  } else {
    form.phuongXa = defaultAddress.value.phuongXa || ''
  }

  if (!provinceMatch) {
    form.tinhThanh = defaultAddress.value.tinhThanh || ''
  }
  if (!districtMatch) {
    form.diaChiChiTiet = savedDetail.detail || form.diaChiChiTiet || ''
  } else {
    form.districtCode = String(districtMatch.code)
  }

  if (!wardMatch) {
    form.phuongXa = defaultAddress.value.phuongXa || ''
  }
}

const loadProfile = async () => {
  const token = window.localStorage.getItem('aerion_client_token')
  if (!token) {
    router.push('/login')
    return false
  }

  try {
    const { data } = await getOnlineCustomerProfile()
    profile.value = data?.user || {}
    defaultAddress.value = data?.diaChiMacDinh || {}
    syncFormFromProfile()
    return true
  } catch (error) {
    const status = error?.response?.status
    if (status === 401 || status === 403) {
      window.localStorage.removeItem('aerion_client_token')
      window.localStorage.removeItem('aerion_client_user')
      router.push('/login')
      return false
    }

    errorMessage.value = error?.response?.data?.message || 'Khong the tai ho so. Vui long thu lai.'
    return false
  }
}

const reloadAll = async () => {
  await initPage()
}

const handleProvinceChange = async () => {
  const province = provinceOptions.value.find((item) => String(item.code) === String(form.provinceCode))
  form.tinhThanh = province ? province.name || province.name_with_type || '' : ''
  form.districtCode = ''
  form.wardCode = ''
  form.phuongXa = ''
  districtOptions.value = []
  wardOptions.value = []

  if (form.provinceCode) {
    await loadDistricts(form.provinceCode)
  }
}

const handleDistrictChange = async () => {
  const district = districtOptions.value.find((item) => String(item.code) === String(form.districtCode))
  wardOptions.value = []
  form.wardCode = ''
  form.phuongXa = ''

  if (!district) {
    return
  }

  await loadWards(form.districtCode)
}

const handleWardChange = () => {
  const ward = wardOptions.value.find((item) => String(item.code) === String(form.wardCode))
  form.phuongXa = ward ? ward.name || ward.name_with_type || '' : ''
}

const saveProfile = async () => {
  if (saving.value) return

  saving.value = true
  saveMessage.value = ''
  saveError.value = ''

  try {
    if (!PHONE_REGEX.test(String(form.sdt || '').trim())) {
      saveError.value = 'Số điện thoại phải bắt đầu bằng số 0 và gồm đúng 10 chữ số.'
      return
    }

    if (!PHONE_REGEX.test(String(form.sdtNguoiNhan || '').trim())) {
      saveError.value = 'SĐT người nhận phải bắt đầu bằng số 0 và gồm đúng 10 chữ số.'
      return
    }

    const { data } = await updateOnlineCustomerProfile({
      hoTen: form.hoTen,
      sdt: form.sdt,
      gioiTinh: form.gioiTinh,
      ngaySinh: form.ngaySinh || null,
      nguoiNhan: form.nguoiNhan,
      sdtNguoiNhan: form.sdtNguoiNhan,
      tinhThanh: form.tinhThanh,
      phuongXa: form.phuongXa,
      diaChiChiTiet: buildSavedDetail(),
    })

    profile.value = data?.user || profile.value
    defaultAddress.value = data?.diaChiMacDinh || defaultAddress.value
    saveMessage.value = data?.message || 'Cap nhat ho so thanh cong.'

    const token = window.localStorage.getItem('aerion_client_token')
    if (token) {
      window.localStorage.setItem('aerion_client_user', JSON.stringify(profile.value))
      window.dispatchEvent(new Event('storage'))
    }
  } catch (error) {
    saveError.value = error?.response?.data?.message || 'Khong the luu ho so. Vui long thu lai.'
  } finally {
    saving.value = false
  }
}

const initPage = async () => {
  isLoading.value = true
  errorMessage.value = ''
  saveMessage.value = ''
  saveError.value = ''

  try {
    const ok = await loadProfile()
    if (!ok) return

    await loadProvinces()
    await applySavedAddressSelections()
  } finally {
    isLoading.value = false
  }
}

onMounted(initPage)
</script>

<style scoped>
</style>
