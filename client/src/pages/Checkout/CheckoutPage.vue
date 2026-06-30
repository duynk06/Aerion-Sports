<template>
  <div class="container mx-auto px-5 py-10">
    <h1 class="text-3xl text-primary-color mb-10">THÔNG TIN THANH TOÁN</h1>
    <div class="flex flex-col lg:flex-row gap-12">
      <div class="flex-[2]">
        <div class="mb-10">
          <h3 class="text-xl font-bold mb-6 pb-2 border-b border-gray-200 text-primary-color flex items-center gap-2"><UserOutlined /> Thông tin giao hàng</h3>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-5 mb-5">
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Họ và tên <span class="text-red-500">*</span></label>
              <input
                v-model="guestCustomer.fullName"
                type="text"
                placeholder="Nhập họ tên"
                class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all"
              />
            </div>
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Số điện thoại <span class="text-red-500">*</span></label>
              <input
                v-model="guestCustomer.phone"
                type="text"
                inputmode="numeric"
                autocomplete="tel"
                pattern="[0-9]*"
                placeholder="Nhập số điện thoại"
                class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all"
                @input="handlePhoneInput"
              />
            </div>
          </div>
          <div class="mb-5">
            <label class="block mb-2 font-medium text-sm text-gray-700">Email <span class="text-red-500">*</span></label>
            <input
              v-model="guestCustomer.email"
              type="email"
              placeholder="Nhập email"
              class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all"
            />
          </div>
          <div class="mb-5">
            <label class="block mb-2 font-medium text-sm text-gray-700">Địa chỉ cụ thể <span class="text-red-500">*</span></label>
            <input
              v-model="guestCustomer.address"
              type="text"
              placeholder="Nhập địa chỉ cụ thể"
              class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all"
            />
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-5 mb-5">
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Tỉnh/Thành phố <span class="text-red-500">*</span></label>
              <select
                v-model="guestCustomer.city"
                class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all bg-white"
                @change="handleCityChange"
              >
                <option disabled value="">Chọn tỉnh/thành phố</option>
                <option v-for="city in cities" :key="city.code" :value="city.name">{{ city.name }}</option>
              </select>
            </div>
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Quận/Huyện <span class="text-red-500">*</span></label>
              <select
                v-model="guestCustomer.district"
                class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all bg-white"
                :disabled="!guestCustomer.city || districts.length === 0"
                @change="handleDistrictChange"
              >
                <option disabled value="">Chọn quận/huyện</option>
                <option v-for="district in districts" :key="district.code" :value="district.name">{{ district.name }}</option>
              </select>
            </div>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 gap-5 mb-5">
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Phường/Xã <span class="text-red-500">*</span></label>
              <select
                v-model="guestCustomer.ward"
                class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all bg-white"
                :disabled="!guestCustomer.district || wards.length === 0"
              >
                <option disabled value="">Chọn phường/xã</option>
                <option v-for="ward in wards" :key="ward.code" :value="ward.name">{{ ward.name }}</option>
              </select>
            </div>
            <div>
              <label class="block mb-2 font-medium text-sm text-gray-700">Ghi chú đơn hàng (Tùy chọn)</label>
              <textarea
                v-model="guestCustomer.note"
                rows="3"
                placeholder="Ghi chú về mức căng dây, thời gian giao hàng..."
                class="w-full px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all"
              ></textarea>
            </div>
          </div>
        </div>

        <div>
          <h3 class="text-xl font-bold mb-6 pb-2 border-b border-gray-200 text-primary-color flex items-center gap-2"><CreditCardOutlined /> Phương thức thanh toán</h3>
          <div class="space-y-4">
            <div
              class="flex items-center gap-4 p-5 border rounded cursor-pointer transition-all"
              :class="paymentMethod === 'cod' ? 'border-2 border-secondary-color bg-secondary-color/5' : 'border-gray-200 hover:border-gray-300'"
              @click="paymentMethod = 'cod'"
            >
              <div class="w-5 h-5 rounded-full border-2 flex items-center justify-center transition-all shrink-0"
                   :class="paymentMethod === 'cod' ? 'border-secondary-color' : 'border-gray-300'">
                <div v-if="paymentMethod === 'cod'" class="w-2.5 h-2.5 rounded-full bg-secondary-color"></div>
              </div>
              <div>
                <h4 class="text-base font-bold text-gray-800 mb-1">Thanh toán khi nhận hàng (COD)</h4>
                <p class="text-sm text-gray-500">Khách hàng trả tiền mặt khi giao hàng</p>
              </div>
            </div>
            <div
              class="flex items-center gap-4 p-5 border rounded cursor-pointer transition-all"
              :class="paymentMethod === 'bank' ? 'border-2 border-secondary-color bg-secondary-color/5' : 'border-gray-200 hover:border-gray-300'"
              @click="paymentMethod = 'bank'"
            >
              <div class="w-5 h-5 rounded-full border-2 flex items-center justify-center transition-all shrink-0"
                   :class="paymentMethod === 'bank' ? 'border-secondary-color' : 'border-gray-300'">
                <div v-if="paymentMethod === 'bank'" class="w-2.5 h-2.5 rounded-full bg-secondary-color"></div>
              </div>
              <div>
                <h4 class="text-base font-bold text-gray-800 mb-1">Chuyển khoản qua ngân hàng</h4>
                <p class="text-sm text-gray-500">Thanh toán an toàn qua VNPAY / Momo</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="flex-1">
        <div class="bg-white p-8 rounded-xl border border-gray-200 sticky top-24">
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-xl font-bold text-primary-color flex items-center gap-2"><ShoppingCartOutlined /> Đơn Hàng Của Bạn</h3>
            <div v-if="loading" class="text-xs text-gray-500">Đang cập nhật giá...</div>
          </div>

          <div v-if="!loading && hydratedItems.length === 0" class="py-8 text-center text-gray-500">
            Giỏ hàng đang trống.
          </div>

          <template v-else>
            <div class="border-b border-gray-200 pb-5 mb-5 space-y-4">
              <div v-for="item in hydratedItems" :key="item.id" class="flex justify-between items-center text-sm gap-4">
                <span class="font-medium text-gray-800">{{ item.name }} x {{ item.quantity }}</span>
                <span class="font-bold text-right">{{ formatPrice(item.price * item.quantity) }}</span>
              </div>
            </div>

            <div class="mb-5">
              <label class="block mb-2 font-medium text-sm text-gray-700">Phiếu giảm giá</label>
              <div class="relative">
                <div class="flex gap-3">
                  <input
                    v-model="voucherInput"
                    type="text"
                    placeholder="Nhập hoặc chọn mã giảm giá"
                    class="flex-1 px-4 py-3 border border-gray-300 rounded focus:border-secondary-color focus:ring-1 focus:ring-secondary-color outline-none transition-all uppercase"
                    @focus="openVoucherPanel"
                    @click="openVoucherPanel"
                    @keyup.enter="applyVoucher"
                  />
                  <button
                    type="button"
                    class="px-5 py-3 rounded bg-primary-color text-white font-semibold hover:opacity-90 disabled:opacity-60 disabled:cursor-not-allowed"
                    :disabled="voucherLoading"
                    @click="applyVoucher"
                  >
                    {{ voucherLoading ? 'ĐANG TÌM...' : 'Áp dụng' }}
                  </button>
                </div>

                <div
                  v-if="voucherPanelOpen"
                  class="absolute z-20 mt-2 w-full rounded-xl border border-gray-200 bg-white shadow-lg overflow-hidden"
                >
                  <div class="flex items-center justify-between px-4 py-3 border-b border-gray-100 bg-gray-50">
                    <div class="font-semibold text-gray-700">Voucher có thể dùng</div>
                    <button type="button" class="text-sm text-gray-500 hover:text-gray-700" @click="closeVoucherPanel">Đóng</button>
                  </div>

                  <div v-if="voucherLoading" class="px-4 py-5 text-sm text-gray-500">
                    Đang tải phiếu giảm giá...
                  </div>

                  <div v-else-if="voucherOptions.length === 0" class="px-4 py-5 text-sm text-gray-500">
                    Không có voucher hợp lệ cho đơn hàng này.
                  </div>

                  <button
                    v-for="voucher in voucherOptions"
                    :key="voucher.id"
                    type="button"
                    class="w-full text-left px-4 py-4 border-t border-gray-100 hover:bg-orange-50 transition-colors"
                    @click="selectVoucher(voucher)"
                  >
                    <div class="flex items-start justify-between gap-4">
                      <div>
                        <div class="font-bold text-gray-900">
                          {{ voucher.displayTitle }}
                        </div>
                        <div class="mt-1 text-sm text-gray-600">Mã: {{ voucher.maPhieuGiamGia }}</div>
                        <div class="mt-1 text-xs text-gray-400">
                          Đơn tối thiểu {{ formatPrice(voucher.giaTriDonToiThieu) }}
                        </div>
                      </div>
                      <div class="text-right">
                        <div class="font-semibold text-green-600">-{{ formatPrice(voucher.previewDiscount) }}</div>
                      </div>
                    </div>
                  </button>
                </div>
              </div>
              <p v-if="voucherMessage" class="mt-2 text-sm" :class="voucherMessageType === 'error' ? 'text-red-600' : 'text-green-600'">
                {{ voucherMessage }}
              </p>
              <div v-if="appliedVoucher" class="mt-3 rounded-lg border border-green-200 bg-green-50 px-4 py-3 text-sm text-green-700">
                <div class="flex items-start justify-between gap-4">
                  <div>
                    <div class="font-semibold">
                      Đã áp dụng mã <strong>{{ appliedVoucher.maPhieuGiamGia }}</strong>
                    </div>
                    <span class="block mt-1">
                      Giảm {{ appliedVoucher.isPercent ? `${appliedVoucher.giaTriGiam}%` : formatPrice(appliedVoucher.giaTriGiam) }}
                      <template v-if="appliedVoucher.giaTriGiamToiDa > 0">
                        , tối đa {{ formatPrice(appliedVoucher.giaTriGiamToiDa) }}
                      </template>
                    </span>
                  </div>
                  <button
                    type="button"
                    class="text-red-500 font-medium hover:text-red-600"
                    @click="clearVoucher"
                  >
                    Bỏ
                  </button>
                </div>
              </div>
            </div>

            <div class="flex justify-between mb-4 text-gray-700">
              <span>Tạm tính</span>
              <span class="font-semibold">{{ formatPrice(summary.subtotal) }}</span>
            </div>
            <div class="flex justify-between mb-4 text-gray-700">
              <span>Đợt giảm giá</span>
              <span class="font-semibold text-green-600">- {{ formatPrice(summary.productDiscount) }}</span>
            </div>
            <div class="flex justify-between mb-4 text-gray-700">
              <span>Phiếu giảm giá</span>
              <span class="font-semibold text-green-600">- {{ formatPrice(summary.voucherDiscount) }}</span>
            </div>
            <div class="flex justify-between mb-4 text-gray-700">
              <span class="flex items-center gap-2">
                Phí vận chuyển
                <img :src="logoShip" alt="Shipping logo" class="h-4 w-auto object-contain" />
              </span>
              <span class="font-semibold text-green-600">{{ formatPrice(summary.shipping) }}</span>
            </div>
            <div class="flex justify-between mt-6 pt-6 border-t-2 border-gray-200 text-lg font-bold">
              <span class="text-primary-color">Tổng cộng</span>
              <span class="text-secondary-color text-2xl">{{ formatPrice(summary.total) }}</span>
            </div>
          </template>

          <button
            class="btn-primary w-full flex items-center justify-center gap-2 mt-8 py-4 text-lg disabled:opacity-60 disabled:cursor-not-allowed"
            :disabled="submitting"
            @click="handlePlaceOrder"
            type="button"
          >
            <CheckCircleOutlined />
            {{ submitting ? 'ĐANG XỬ LÝ...' : 'ĐẶT HÀNG NGAY' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { UserOutlined, CreditCardOutlined, ShoppingCartOutlined, CheckCircleOutlined } from '@ant-design/icons-vue'
import { getProductDetail, resolveMediaUrl, getProvinces, getDistrictsByProvinceCode, getWardsByDistrictCode, getVoucherList, createOnlineOrder, getOnlineCustomerProfile } from '../../services/api'
import fallbackImage from '../../assets/mock_racket.png'
import logoShip from '../../assets/logo/image.png'
import { useCart } from '../../composables/useCart'
import { useCatalogRealtime } from '../../composables/useCatalogRealtime'

const router = useRouter()
const { cartItems, clearCart } = useCart()

const loading = ref(false)
const submitting = ref(false)
const voucherLoading = ref(false)
const voucherPanelOpen = ref(false)
const shippingFee = ref(31000)
const hydratedItems = ref([])
const cities = ref([])
const districts = ref([])
const wards = ref([])
const voucherInput = ref('')
const availableVouchers = ref([])
const appliedVoucher = ref(null)
const voucherMessage = ref('')
const voucherMessageType = ref('success')
const paymentMethod = ref('cod')
const guestCustomer = ref({
  fullName: '',
  phone: '',
  email: '',
  address: '',
  city: '',
  district: '',
  ward: '',
  note: '',
})
const PHONE_REGEX = /^0\d{9}$/
const EMAIL_REGEX = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

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
    const haystack = normalizeText(item?.name || '')
    return haystack === needle || haystack.includes(needle) || needle.includes(haystack)
  }) || null
}

const formatPrice = (value) => `${new Intl.NumberFormat('vi-VN').format(Number(value || 0))} đ`

const handlePhoneInput = (event) => {
  const rawValue = String(event?.target?.value || '')
  const digitsOnly = rawValue.replace(/\D/g, '').slice(0, 10)
  guestCustomer.value.phone = digitsOnly
}

const normalizeProduct = (payload, cartItem) => {
  const variants = Array.isArray(payload?.chiTietSanPhams) ? payload.chiTietSanPhams : Array.from(payload?.chiTietSanPhams || [])
  const mappedVariants = variants
    .filter(Boolean)
    .map((variant) => ({
      key: variant?.id ?? `${variant?.maCtsp ?? 'variant'}-${variant?.tenMauSac ?? ''}-${variant?.tenTrongLuong ?? ''}`,
      variantId: variant?.id ?? null,
      image: resolveMediaUrl(variant?.hinhAnh) || fallbackImage,
      price: Number(variant?.giaDaGiam ?? variant?.giaBan ?? 0),
      oldPrice: Number(variant?.giaBan ?? 0),
      stock: Number(variant?.soLuong || 0),
      color: variant?.tenMauSac || '',
      weight: variant?.tenTrongLuong || '',
    }))

  const matchedVariant =
    mappedVariants.find((variant) => String(variant.key) === String(cartItem.variantKey)) ||
    mappedVariants[0] ||
    null

  if (!matchedVariant) return null

  return {
    id: cartItem.id,
    productId: payload?.id,
    variantId: matchedVariant.variantId,
    name: payload?.tenSanPham || `Sản phẩm ${payload?.id ?? ''}`,
    variantLabel: [matchedVariant.color, matchedVariant.weight].filter(Boolean).join(' · ') || matchedVariant.key,
    image: matchedVariant.image,
    price: matchedVariant.price,
    oldPrice: matchedVariant.oldPrice > matchedVariant.price ? matchedVariant.oldPrice : null,
    quantity: Number(cartItem.quantity || 1),
    stock: matchedVariant.stock,
  }
}

const hydrateCart = async () => {
  const shouldShowLoading = hydratedItems.value.length === 0
  if (shouldShowLoading) {
    loading.value = true
  }

  try {
    const items = cartItems.value
    if (!items.length) {
      hydratedItems.value = []
      return
    }

    const uniqueProductIds = [...new Set(items.map((item) => item.productId))]
    const productResponses = await Promise.all(
      uniqueProductIds.map(async (productId) => {
        const response = await getProductDetail(productId)
        return { productId, data: response.data }
      })
    )

    const responseMap = new Map(productResponses.map((entry) => [String(entry.productId), entry.data]))
    hydratedItems.value = items
      .map((cartItem) => normalizeProduct(responseMap.get(String(cartItem.productId)), cartItem))
      .filter(Boolean)
  } catch {
    if (hydratedItems.value.length === 0) {
      hydratedItems.value = []
    }
  } finally {
    loading.value = false
  }
}

const loadCities = async () => {
  try {
    const response = await getProvinces()
    cities.value = response.data || []
  } catch (error) {
    console.error('Không tải được danh sách tỉnh/thành:', error)
    cities.value = []
  }
}

const prefillLoggedInCustomer = async () => {
  const token = window.localStorage.getItem('aerion_client_token')
  if (!token) return

  try {
    const { data } = await getOnlineCustomerProfile()
    const user = data?.user || {}
    const address = data?.diaChiMacDinh || {}
    const savedDetail = splitSavedDetail(address.diaChiChiTiet || '')

    guestCustomer.value.fullName = user.ten || user.hoTen || guestCustomer.value.fullName
    guestCustomer.value.phone = user.sdt || guestCustomer.value.phone
    guestCustomer.value.email = user.email || guestCustomer.value.email
    guestCustomer.value.note = guestCustomer.value.note || ''

    const provinceMatch = findBestMatch(cities.value, address.tinhThanh)
    if (provinceMatch) {
      guestCustomer.value.city = provinceMatch.name
      await loadDistrictsByCityName(provinceMatch.name)
    }

    const districtMatch = findBestMatch(districts.value, savedDetail.districtName)
    if (districtMatch) {
      guestCustomer.value.district = districtMatch.name
      await loadWardsByDistrictName(districtMatch.name)
    }

    const wardMatch = findBestMatch(wards.value, address.phuongXa)
    if (wardMatch) {
      guestCustomer.value.ward = wardMatch.name
    }

    if (savedDetail.detail) {
      guestCustomer.value.address = savedDetail.detail
    } else if (address.diaChiDayDu) {
      guestCustomer.value.address = address.diaChiDayDu
    }
  } catch (error) {
    console.error('Khong tai duoc ho so khach hang da dang nhap:', error)
  }
}

const loadDistrictsByCityName = async (cityName) => {
  try {
    const selectedCity = cities.value.find((city) => city.name === cityName)
    if (!selectedCity) {
      districts.value = []
      wards.value = []
      return
    }

    const response = await getDistrictsByProvinceCode(selectedCity.code)
    districts.value = response.data?.districts || []
    wards.value = []
  } catch (error) {
    console.error('Không tải được danh sách quận/huyện:', error)
    districts.value = []
    wards.value = []
  }
}

const loadWardsByDistrictName = async (districtName) => {
  try {
    const selectedDistrict = districts.value.find((district) => district.name === districtName)
    if (!selectedDistrict) {
      wards.value = []
      return
    }

    const response = await getWardsByDistrictCode(selectedDistrict.code)
    wards.value = response.data?.wards || []
  } catch (error) {
    console.error('Không tải được danh sách phường/xã:', error)
    wards.value = []
  }
}

const handleCityChange = async () => {
  guestCustomer.value.district = ''
  guestCustomer.value.ward = ''
  wards.value = []
  await loadDistrictsByCityName(guestCustomer.value.city)
}

const handleDistrictChange = async () => {
  guestCustomer.value.ward = ''
  await loadWardsByDistrictName(guestCustomer.value.district)
}

const summary = computed(() => {
  const subtotal = hydratedItems.value.reduce((total, item) => total + Number(item.price || 0) * Number(item.quantity || 0), 0)
  const productDiscount = hydratedItems.value.reduce((total, item) => {
    const base = Number(item.oldPrice || 0)
    const sale = Number(item.price || 0)
    if (base > sale) return total + (base - sale) * Number(item.quantity || 0)
    return total
  }, 0)
  const voucherDiscount = getVoucherDiscountAmount(appliedVoucher.value, subtotal)
  const discount = productDiscount + voucherDiscount
  const shipping = Number(shippingFee.value || 0)
  return {
    subtotal,
    productDiscount,
    voucherDiscount,
    discount,
    shipping,
    total: Math.max(0, subtotal - discount + shipping),
  }
})

const voucherOptions = computed(() => {
  return availableVouchers.value
    .map((voucher) => ({
      ...voucher,
      previewDiscount: getVoucherDiscountAmount(voucher, summary.value.subtotal),
    }))
    .filter((voucher) => voucher.previewDiscount > 0)
    .sort((a, b) => b.previewDiscount - a.previewDiscount)
})

const normalizeVoucher = (voucher) => {
  const isPercent = String(voucher?.loaiPhieuGiamGia || '').trim().toLowerCase() === 'sale %'
  return {
    ...voucher,
    isPercent,
    giaTriGiam: Number(voucher?.giaTriGiam || 0),
    giaTriDonToiThieu: Number(voucher?.giaTriDonToiThieu || 0),
    giaTriGiamToiDa: Number(voucher?.giaTriGiamToiDa || 0),
    soLuong: Number(voucher?.soLuong ?? 0),
    soLuongDaSuDung: Number(voucher?.soLuongDaSuDung ?? 0),
    displayTitle: isPercent
      ? `Giảm ${Number(voucher?.giaTriGiam || 0)}%${Number(voucher?.giaTriGiamToiDa || 0) > 0 ? ` (tối đa ${formatPrice(voucher?.giaTriGiamToiDa)})` : ''}`
      : `Giảm ${formatPrice(voucher?.giaTriGiam || 0)}`,
  }
}

const getVoucherDiscountAmount = (voucher, subtotal) => {
  if (!voucher) return 0
  if (subtotal < voucher.giaTriDonToiThieu) return 0
  let discount = voucher.isPercent
    ? (subtotal * voucher.giaTriGiam) / 100
    : voucher.giaTriGiam

  if (voucher.giaTriGiamToiDa > 0) {
    discount = Math.min(discount, voucher.giaTriGiamToiDa)
  }

  return Math.max(0, Math.min(discount, subtotal))
}

const isVoucherActive = (voucher) => {
  const now = new Date()
  const start = voucher?.ngayBatDau ? new Date(voucher.ngayBatDau) : null
  const end = voucher?.ngayKetThuc ? new Date(voucher.ngayKetThuc) : null

  if (voucher?.trangThai !== 1) return false
  if (voucher?.soLuong !== null && voucher?.soLuong !== undefined && voucher.soLuong <= voucher.soLuongDaSuDung) return false
  if (start && now < start) return false
  if (end && now > end) return false
  return true
}

const resetVoucherState = (message = '', type = 'success') => {
  voucherMessage.value = message
  voucherMessageType.value = type
}

const openVoucherPanel = async () => {
  voucherPanelOpen.value = true
  voucherLoading.value = true
  voucherMessage.value = ''
  try {
    const vouchers = await loadAvailableVouchers()
    availableVouchers.value = vouchers.filter((voucher) => isVoucherActive(voucher))
  } catch (error) {
    console.error('Không tải được phiếu giảm giá:', error)
    availableVouchers.value = []
    resetVoucherState('Không thể tải danh sách phiếu giảm giá.', 'error')
  } finally {
    voucherLoading.value = false
  }
}

const closeVoucherPanel = () => {
  voucherPanelOpen.value = false
}

const loadAvailableVouchers = async () => {
  const response = await getVoucherList()
  return Array.isArray(response.data) ? response.data.map(normalizeVoucher) : []
}

const selectVoucher = async (voucher) => {
  voucherInput.value = voucher.maPhieuGiamGia
  voucherPanelOpen.value = false
  availableVouchers.value = [voucher, ...availableVouchers.value.filter((item) => item.maPhieuGiamGia !== voucher.maPhieuGiamGia)]
  await applyVoucher()
}

const clearVoucher = () => {
  voucherInput.value = ''
  appliedVoucher.value = null
  voucherPanelOpen.value = false
  resetVoucherState('Đã bỏ phiếu giảm giá.', 'success')
}

const applyVoucher = async () => {
  const code = voucherInput.value.trim().toUpperCase()
  if (!code) {
    appliedVoucher.value = null
    voucherPanelOpen.value = true
    resetVoucherState('Nhập mã phiếu giảm giá để áp dụng.', 'error')
    return
  }

  voucherLoading.value = true
  try {
    if (availableVouchers.value.length === 0) {
      const vouchers = await loadAvailableVouchers()
      availableVouchers.value = vouchers.filter((voucher) => isVoucherActive(voucher))
    }

    const matched = availableVouchers.value.find((voucher) => String(voucher.maPhieuGiamGia || '').trim().toUpperCase() === code)
    if (!matched) {
      appliedVoucher.value = null
      resetVoucherState('Mã giảm giá không hợp lệ hoặc đã hết hiệu lực.', 'error')
      return
    }

    const discountAmount = getVoucherDiscountAmount(matched, summary.value.subtotal)
    if (discountAmount <= 0) {
      appliedVoucher.value = null
      resetVoucherState('Đơn hàng chưa đạt giá trị tối thiểu của phiếu giảm giá.', 'error')
      return
    }

    appliedVoucher.value = {
      ...matched,
      discountAmount,
    }
    voucherPanelOpen.value = false
    resetVoucherState(`Áp dụng thành công mã ${matched.maPhieuGiamGia}.`, 'success')
  } catch (error) {
    console.error('Không tải được phiếu giảm giá:', error)
    resetVoucherState('Không thể tải danh sách phiếu giảm giá.', 'error')
  } finally {
    voucherLoading.value = false
  }
}

const validateGuestCustomer = () => {
  const fullName = guestCustomer.value.fullName.trim()
  const phone = guestCustomer.value.phone.trim()
  const email = guestCustomer.value.email.trim()
  const address = guestCustomer.value.address.trim()
  const city = guestCustomer.value.city.trim()
  const district = guestCustomer.value.district.trim()
  const ward = guestCustomer.value.ward.trim()

  if (!fullName || !phone || !address || !city || !district || !ward) {
    alert('Vui lòng nhập đầy đủ họ tên, số điện thoại và địa chỉ nhận hàng.')
    return false
  }

  if (!PHONE_REGEX.test(phone)) {
    alert('Số điện thoại phải bắt đầu bằng số 0 và gồm đúng 10 chữ số.')
    return false
  }

  if (!email || !EMAIL_REGEX.test(email)) {
    alert('Email phải đúng định dạng.')
    return false
  }

  return true
}

const handlePlaceOrder = async () => {
  if (!validateGuestCustomer()) return
  if (hydratedItems.value.length === 0) {
    alert('Giỏ hàng đang trống.')
    return
  }

  submitting.value = true

  try {
    if (voucherInput.value.trim() && !appliedVoucher.value) {
      await applyVoucher()
      if (voucherInput.value.trim() && !appliedVoucher.value) return
    }

    const payload = {
      hoTen: guestCustomer.value.fullName.trim(),
      sdt: guestCustomer.value.phone.trim(),
      email: guestCustomer.value.email.trim() || null,
      diaChiChiTiet: guestCustomer.value.address.trim(),
      phuongXa: guestCustomer.value.ward.trim(),
      quanHuyen: guestCustomer.value.district.trim(),
      tinhThanh: guestCustomer.value.city.trim(),
      ghiChu: guestCustomer.value.note.trim() || null,
      phuongThucThanhToan: paymentMethod.value,
      maPhieuGiamGia: appliedVoucher.value?.maPhieuGiamGia || null,
      phiVanChuyen: summary.value.shipping,
      items: hydratedItems.value.map((item) => {
        if (!item.variantId) {
          throw new Error(`San pham ${item.name} chua co variantId hop le.`)
        }
        return {
          chiTietSanPhamId: Number(item.variantId),
          soLuong: Number(item.quantity || 1),
          donGia: Number(item.price || 0),
        }
      }),
    }

    const response = await createOnlineOrder(payload)
    const order = response?.data || null
    clearCart()

    if (order?.maHoaDon) {
      await router.push({
        path: '/order-tracking',
        query: { code: order.maHoaDon },
      })
      alert(`Da tao don hang thanh cong. Ma hoa don: ${order.maHoaDon}`)
    } else {
      alert('Da tao don hang thanh cong.')
    }
  } catch (error) {
    const message = error?.response?.data || error?.message || 'Khong the tao don hang moi.'
    alert(`Thất bại: ${message}`)
  } finally {
    submitting.value = false
  }
}

watch(cartItems, hydrateCart, { deep: true })
watch(
  () => summary.value.subtotal,
  (subtotal) => {
    if (!appliedVoucher.value) return
    if (getVoucherDiscountAmount(appliedVoucher.value, subtotal) <= 0) {
      appliedVoucher.value = null
      resetVoucherState('Phiếu giảm giá không còn phù hợp với đơn hàng hiện tại.', 'error')
    }
  }
)
onMounted(async () => {
  await hydrateCart()
  await loadCities()
  await prefillLoggedInCustomer()
})

useCatalogRealtime(hydrateCart)
</script>

