<template>
  <MainLayout title="Quản Lý Khách Hàng">
    <div class="customer-card">
      <div class="page-header">
        <button type="button" class="back-link" @click="router.push('/khach-hang')">
          &larr; Quay lại danh sách
        </button>
        <p>Nhập thông tin khách hàng mới</p>
      </div>

      <div class="qr-scan-section mb-4" style="border-bottom: 1px solid #eee; padding-bottom: 20px;">
        <button type="button" @click="startScan" class="btn-scan-qr">
          <i class="fas fa-qrcode"></i> Quét QR lấy thông tin nhanh
        </button>
        <div v-if="isScanning" id="qr-reader-them-kh" style="max-width: 330px; margin: 15px 0 10px 0; border: 1px solid #ddd; border-radius: 6px; overflow: hidden;"></div>
        <button v-if="isScanning" type="button" class="btn-cancel" @click="stopScan" style="margin-top: 5px; height: 34px; padding: 0 15px;">Tắt Camera</button>
      </div>

      <form @submit.prevent="saveKhachHang">
        <!-- 🌟 THÊM MỚI: Khối upload ảnh đại diện -->
        <div class="avatar-upload-section">
          <div class="avatar-preview-box">
            <img :src="avatarPreviewUrl" alt="Ảnh đại diện khách hàng" @error="onAvatarLoadError" />
          </div>
          <div class="avatar-upload-controls">
            <label class="avatar-label">Ảnh đại diện khách hàng</label>
            <input type="file" accept="image/*" @change="onAvatarFileChange" />
            <span class="avatar-hint">Chọn ảnh JPG/PNG, tối đa 5MB (không bắt buộc)</span>
            <button v-if="avatarPreviewUrl !== defaultAvatarUrl" type="button" class="btn-remove-avatar" @click="clearAvatar">
              ✕ Bỏ ảnh đã chọn
            </button>
          </div>
        </div>

        <div class="info-section">
          <div class="form-content">
            <h3>Thông tin cá nhân</h3>
            <div class="form-grid">
              <div class="form-group" :class="{ 'has-error': errors.tenKh }">
                <label>Họ và tên khách hàng <span>*</span></label>
                <input type="text" v-model="khachHang.tenKh" placeholder="Nhập tên khách hàng" @input="errors.tenKh = ''" @blur="validateHoTenChuDong"/>
                <span v-if="errors.tenKh" class="error-text">{{ errors.tenKh }}</span>
              </div>

              <div class="form-group" :class="{ 'has-error': errors.sdt }">
                <label>Số điện thoại liên hệ <span>*</span></label>
                <input type="text" v-model="khachHang.sdt" placeholder="Nhập số điện thoại" @input="errors.sdt = ''" @blur="validateSdtChuDong"/>
                <span v-if="errors.sdt" class="error-text">{{ errors.sdt }}</span>
              </div>

              <div class="form-group" :class="{ 'has-error': errors.email }">
                <label>Địa chỉ Email <span>*</span></label>
                <input type="text" v-model="khachHang.email" placeholder="Nhập địa chỉ email" @input="errors.email = ''" @blur="validateEmailChuDong"/>
                <span v-if="errors.email" class="error-text">{{ errors.email }}</span>
              </div>

              <div class="form-group" :class="{ 'has-error': errors.ngaySinh }">
                <label>Ngày sinh <span>*</span></label>
                <input type="date" v-model="khachHang.ngaySinh" :max="ngayMaxChoPhep" @change="validateNgaySinhChuDong"/>
                <span v-if="errors.ngaySinh" class="error-text">{{ errors.ngaySinh }}</span>
              </div>

              <div class="form-group">
                <label>Giới tính</label>
                <div class="radio-group">
                  <label class="radio-label"><input type="radio" :value="1" v-model="khachHang.gioiTinh" style="accent-color: #f79b66;"/> Nam</label>
                  <label class="radio-label"><input type="radio" :value="0" v-model="khachHang.gioiTinh" style="accent-color: #f79b66;"/> Nữ</label>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="address-section-header">
          <h3>Sổ địa chỉ giao nhận (Tối đa nhiều nơi)</h3>
          <button type="button" class="btn-add-address" @click="themKhoiDiaChiMoi">➕ Thêm địa chỉ mới</button>
        </div>

        <div class="address-list-container">
          <div v-for="(item, index) in khachHang.listDiaChi" :key="index" class="address-box-item">
            <div class="address-box-header">
              <span class="badge-title">📍 Khối địa chỉ #{{ index + 1 }}</span>
              <button v-if="khachHang.listDiaChi.length > 1" type="button" class="btn-remove-address" @click="xoaKhoiDiaChi(index)">✕ Xóa khối</button>
            </div>

            <div class="form-grid" style="margin-top: 15px; margin-bottom: 15px;">
              <div class="form-group">
                <label>Tên người nhận hàng <span>*</span></label>
                <input type="text" v-model="item.tenNguoiNhan" placeholder="Nhập tên người nhận" required/>
              </div>
              <div class="form-group">
                <label>SĐT người nhận <span>*</span></label>
                <input type="text" v-model="item.sdtNguoiNhan" placeholder="Nhập SĐT nhận hàng" required/>
              </div>
            </div>

            <div class="form-grid" style="margin-bottom: 15px;">
              <div class="form-group">
                <label>Tỉnh / Thành phố <span>*</span></label>
                <select v-model="item.tinhThanh" class="form-select-control" @change="handleThayDoiTinhThanh(item)" required>
                  <option value="">-- Chọn Tỉnh / Thành phố --</option>
                  <option v-for="tinh in listTinhThanh" :key="tinh.code" :value="tinh.name">{{ tinh.name }}</option>
                </select>
              </div>

              <div class="form-group">
                <label>Quận / Huyện <span>*</span></label>
                <select v-model="item.quanHuyen" class="form-select-control" :disabled="!item.tinhThanh" @change="handleThayDoiQuanHuyen(item)" required>
                  <option value="">-- Chọn Quận / Huyện --</option>
                  <option v-for="quan in item.listQuanHuyenTmp" :key="quan.code" :value="quan.name">{{ quan.name }}</option>
                </select>
              </div>
            </div>

            <div class="form-grid" style="margin-bottom: 15px;">
              <div class="form-group">
                <label>Phường / Xã / Thị trấn <span>*</span></label>
                <select v-model="item.phuongXa" class="form-select-control" :disabled="!item.quanHuyen" required>
                  <option value="">-- Chọn Phường / Xã / Thị trấn --</option>
                  <option v-for="xa in item.listPhuongXaTmp" :key="xa.code" :value="xa.name">{{ xa.name }}</option>
                </select>
              </div>

              <div class="form-group">
                <label>Địa chỉ chi tiết (Số nhà, tên đường...) <span>*</span></label>
                <input type="text" v-model="item.chiTietCuThe" placeholder="Ví dụ: Số 20, ngõ 4, đường Trần Phú" required/>
              </div>
            </div>

            <div class="default-toggle">
              <label style="cursor: pointer; font-size: 13px; display: flex; align-items: center; gap: 6px;">
                <input type="checkbox" :checked="item.isDefault" @change="datDiaChiMacDinh(index)" style="accent-color: #f79b66;"/> Đặt làm địa chỉ mặc định khi đặt hàng
              </label>
            </div>
          </div>
        </div>

        <div class="button-group" style="margin-top: 30px;">
          <button type="submit" class="btn-save">💾 Lưu thông tin khách hàng</button>
          <button type="button" class="btn-cancel" @click="router.push('/khach-hang')">✖ Hủy bỏ</button>
        </div>
      </form>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import MainLayout from '../layouts/MainLayout.vue'
import { Html5QrcodeScanner } from 'html5-qrcode'

const router = useRouter()
const listTinhThanh = ref([])

const isScanning = ref(false)
let qrScanner = null

// 🌟 THÊM MỚI: State quản lý ảnh đại diện
const defaultAvatarUrl = 'https://placehold.co/120x120?text=Avatar'
const avatarFile = ref(null)
const avatarPreviewUrl = ref(defaultAvatarUrl)

const onAvatarFileChange = (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (!file.type.startsWith('image/')) {
    alert('Vui lòng chọn đúng định dạng file ảnh!')
    event.target.value = ''
    return
  }
  if (file.size > 5 * 1024 * 1024) {
    alert('Kích thước ảnh không được vượt quá 5MB!')
    event.target.value = ''
    return
  }

  avatarFile.value = file
  avatarPreviewUrl.value = URL.createObjectURL(file)
}

const clearAvatar = () => {
  avatarFile.value = null
  avatarPreviewUrl.value = defaultAvatarUrl
}

const onAvatarLoadError = (e) => {
  e.target.src = defaultAvatarUrl
}

const khachHang = reactive({
  tenKh: '', sdt: '', email: '', ngaySinh: '', gioiTinh: 1,
  listDiaChi: [{ tenNguoiNhan: '', sdtNguoiNhan: '', tinhThanh: '', quanHuyen: '', phuongXa: '', chiTietCuThe: '', isDefault: true, listQuanHuyenTmp: [], listPhuongXaTmp: [] }]
})

const errors = reactive({ tenKh: '', sdt: '', email: '', ngaySinh: '' })

const ngayMaxChoPhep = computed(() => {
  const today = new Date()
  const yearLimit = today.getFullYear() - 15
  const month = String(today.getMonth() + 1).padStart(2, '0')
  const date = String(today.getDate()).padStart(2, '0')
  return `${yearLimit}-${month}-${date}`
})

const startScan = () => {
  isScanning.value = true
  setTimeout(() => {
    qrScanner = new Html5QrcodeScanner("qr-reader-them-kh", { fps: 10, qrbox: { width: 250, height: 250 } }, false)
    qrScanner.render(onScanSuccess, () => {})
  }, 300)
}

const stopScan = () => {
  if (qrScanner) {
    qrScanner.clear().then(() => { isScanning.value = false }).catch(err => console.error(err))
  }
}

const onScanSuccess = (decodedText) => {
  alert('Quét thông tin căn cước công dân thành công! 🎉')
  stopScan()
  
  if (decodedText.includes('|')) {
    const parts = decodedText.split('|')
    if (parts.length >= 6) {
      khachHang.tenKh = parts[2]
      
      if (parts[3]?.length === 8) {
        khachHang.ngaySinh = `${parts[3].substring(4, 8)}-${parts[3].substring(2, 4)}-${parts[3].substring(0, 2)}`
      }
      
      khachHang.gioiTinh = parts[4] === 'Nam' ? 1 : 0
      
      if (khachHang.listDiaChi.length > 0) {
        khachHang.listDiaChi[0].tenNguoiNhan = parts[2]
        khachHang.listDiaChi[0].chiTietCuThe = parts[5]
      }
    }
  }
}

const load63TinhThanhTuAPI = async () => {
  try {
    const res = await axios.get('https://provinces.open-api.vn/api/p/')
    listTinhThanh.value = res.data || []
  } catch (e) { console.error(e) }
}

const handleThayDoiTinhThanh = async (item) => {
  item.quanHuyen = ''; item.phuongXa = ''; item.listQuanHuyenTmp = []; item.listPhuongXaTmp = [];
  if (!item.tinhThanh) return
  const tinh = listTinhThanh.value.find(t => t.name === item.tinhThanh)
  if (tinh) {
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${tinh.code}?depth=2`)
    item.listQuanHuyenTmp = res.data.districts || []
  }
}

const handleThayDoiQuanHuyen = async (item) => {
  item.phuongXa = ''; item.listPhuongXaTmp = [];
  if (!item.quanHuyen) return
  const quan = item.listQuanHuyenTmp.find(q => q.name === item.quanHuyen)
  if (quan) {
    const res = await axios.get(`https://provinces.open-api.vn/api/d/${quan.code}?depth=2`)
    item.listPhuongXaTmp = res.data.wards || []
  }
}

onMounted(async () => { await load63TinhThanhTuAPI() })

const themKhoiDiaChiMoi = () => {
  khachHang.listDiaChi.push({
    tenNguoiNhan: khachHang.tenKh || '', sdtNguoiNhan: khachHang.sdt || '', tinhThanh: '', quanHuyen: '', phuongXa: '', chiTietCuThe: '', isDefault: khachHang.listDiaChi.length === 0, listQuanHuyenTmp: [], listPhuongXaTmp: []
  })
}
const xoaKhoiDiaChi = (index) => { const wasDef = khachHang.listDiaChi[index].isDefault; khachHang.listDiaChi.splice(index, 1); if (wasDef && khachHang.listDiaChi.length > 0) khachHang.listDiaChi[0].isDefault = true }
const datDiaChiMacDinh = (index) => { khachHang.listDiaChi.forEach((addr, idx) => addr.isDefault = idx === index) }

const validateSdtChuDong = () => { if (!/^(0[3|5|7|8|9])([0-9]{8})$/.test(khachHang.sdt.trim())) { errors.sdt = 'SĐT không đúng định dạng nhà mạng VN!'; return false } errors.sdt = ''; return true }
const validateEmailChuDong = () => { if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(khachHang.email.trim())) { errors.email = 'Email sai định dạng!'; return false } errors.email = ''; return true }

const validateNgaySinhChuDong = () => {
  if (!khachHang.ngaySinh) { errors.ngaySinh = 'Vui lòng chọn ngày sinh!'; return false }
  const birthDate = new Date(khachHang.ngaySinh); const today = new Date()
  let age = today.getFullYear() - birthDate.getFullYear(); const m = today.getMonth() - birthDate.getMonth()
  if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) age--
  if (age < 15) { errors.ngaySinh = `Khách hàng chưa đủ điều kiện (Yêu cầu phải từ 15 tuổi trở lên, hiện tại: ${age} tuổi)!`; return false }
  errors.ngaySinh = ''; return true
}

const validateHoTenChuDong = () => {
  let name = khachHang.tenKh.trim()
  if (!name) { errors.tenKh = 'Họ tên khách hàng không được để trống!'; return false }
  
  name = name.replace(/\s+/g, ' ')
  khachHang.tenKh = name 

  if (name.length < 3 || name.length > 100) { errors.tenKh = `Độ dài họ tên phải nằm trong khoảng từ 3 đến 100 ký tự (Hiện tại: ${name.length} ký tự)!`; return false }
  if (!/^[\p{L}\s]+$/u.test(name)) { errors.tenKh = 'Họ tên không được chứa số hoặc ký tự đặc biệt!'; return false }
  
  errors.tenKh = ''; 
  return true;
}

const saveKhachHang = async () => {
  const hoTenHopLe = validateHoTenChuDong()
  const sdtHopLe = validateSdtChuDong()
  const emailHopLe = validateEmailChuDong()
  const tuoiHopLe = validateNgaySinhChuDong()
  
  if (!hoTenHopLe || !sdtHopLe || !emailHopLe || !tuoiHopLe) {
    return alert('Vui lòng hoàn thiện đúng các trường hiển thị lỗi đỏ!')
  }

  let trong = khachHang.listDiaChi.some(a => !a.tenNguoiNhan.trim() || !a.sdtNguoiNhan.trim() || !a.tinhThanh || !a.quanHuyen || !a.phuongXa || !a.chiTietCuThe.trim())
  if (trong) return alert('Vui lòng không để trống bất kì thông tin nào trong khối sổ địa chỉ!')

  try {
    const checkTrungRes = await axios.get('http://localhost:8080/public/khach-hang/check-trung', {
      params: { 
        sdt: khachHang.sdt.trim(), 
        email: khachHang.email.trim() 
      }
    })
    
    const { trungSdt, trungEmail } = checkTrungRes.data

    if (trungSdt) {
      errors.sdt = 'Số điện thoại này đã tồn tại trên hệ thống!'
      return alert('Thất bại: Số điện thoại này đã được đăng ký bởi khách hàng khác!')
    }

    if (trungEmail) {
      errors.email = 'Địa chỉ Email này đã tồn tại trên hệ thống!'
      return alert('Thất bại: Địa chỉ email này đã được sử dụng bởi khách hàng khác!')
    }

    // 🌟 ĐÃ SỬA LỖI: Đồng bộ chính xác thuộc tính `addr.tenNguoiNhan` và `addr.sdtNguoiNhan` để tránh lỗi trim() của undefined
    const addressesPayload = khachHang.listDiaChi.map(addr => ({
      nguoiNhan: addr.tenNguoiNhan ? addr.tenNguoiNhan.trim() : '',
      sdt: addr.sdtNguoiNhan ? addr.sdtNguoiNhan.trim() : '',
      tinhThanh: addr.tinhThanh,
      phuongXa: `${addr.phuongXa}, ${addr.quanHuyen}`, 
      diaChiChiTiet: addr.chiTietCuThe ? addr.chiTietCuThe.trim() : '',
      macDinh: addr.isDefault ? 1 : 0
    }))

    const payload = {
      hoTen: khachHang.tenKh.trim(), 
      sdt: khachHang.sdt.trim(), 
      email: khachHang.email.trim(), 
      gioiTinh: parseInt(khachHang.gioiTinh, 10), 
      ngaySinh: khachHang.ngaySinh, 
      trangThai: 1, 
      addresses: addressesPayload
    }

    // 🌟 ĐÃ SỬA: Đóng gói FormData thay vì gửi JSON thuần, để đính kèm được file ảnh nhị phân thật
    const formData = new FormData()
    formData.append('data', JSON.stringify(payload))
    if (avatarFile.value) {
      formData.append('avatar', avatarFile.value)
    }

    const response = await axios.post('http://localhost:8080/public/khach-hang/add', formData)
    if (response.status === 200 || response.status === 201) { 
      alert('🎉 Thêm khách hàng và sổ địa chỉ thành công!')
      router.push('/khach-hang') 
    }
  } catch (error) { 
    alert('Thất bại: ' + (error.response?.data?.message || error.response?.data || error.message)) 
  }
}
</script>

<style scoped>
.btn-scan-qr { background-color: #1890ff; color: white; border: none; padding: 0 16px; height: 38px; border-radius: 4px; font-weight: bold; cursor: pointer; display: inline-flex; align-items: center; gap: 8px; font-size: 13px; }
.btn-scan-qr:hover { background-color: #40a9ff; }
.mb-4 { margin-bottom: 1.5rem; }

.form-group.has-error input { border-color: #ef4444 !important; background-color: #fef2f2; }
.form-select-control { width: 100%; padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; height: 38px; background-color: #fff; font-size: 14px; color: #334155; outline: none; box-sizing: border-box; width: 100%; }
.form-select-control:focus { border-color: #f79b66; }
.error-text { color: #ef4444; font-size: 13px; margin-top: 5px; display: block; text-align: left;}
.customer-card { background: #ffffff; padding: 24px; border-radius: 8px; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; text-align: left; }
.page-header { margin-bottom: 20px; }
.back-link { background: none; border: none; color: #94a3b8; cursor: pointer; font-size: 14px; padding: 0; margin-bottom: 6px; }
.page-header p { font-size: 18px; font-weight: 600; color: #1e293b; margin: 0; }

/* 🌟 THÊM MỚI: Style khối upload avatar */
.avatar-upload-section { display: flex; gap: 20px; align-items: center; background-color: #fff7ed; padding: 16px; border-radius: 8px; border: 1px dashed #f79b66; margin-bottom: 24px; }
.avatar-preview-box { width: 100px; height: 100px; border-radius: 50%; overflow: hidden; border: 2px solid #f79b66; flex-shrink: 0; background: #fff; display: flex; align-items: center; justify-content: center; }
.avatar-preview-box img { width: 100%; height: 100%; object-fit: cover; }
.avatar-upload-controls { display: flex; flex-direction: column; gap: 6px; }
.avatar-label { font-size: 13px; font-weight: 600; color: #374151; }
.avatar-hint { font-size: 12px; color: #94a3b8; }
.btn-remove-avatar { align-self: flex-start; background: #fef2f2; color: #dc2626; border: 1px solid #fee2e2; padding: 4px 10px; border-radius: 4px; cursor: pointer; font-size: 12px; margin-top: 4px; }

.info-section { display: flex; gap: 24px; margin-bottom: 24px; }
.form-content { flex: 1; }
.form-content h3, .address-section-header h3 { font-size: 15px; font-weight: 600; color: #1e293b; margin: 0 0 16px 0; }
.form-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }
.form-group { display: flex; flex-direction: column; gap: 6px; }
.form-group label { font-size: 13px; font-weight: 500; color: #475569; }
.form-group label span { color: #ef4444; }
.form-group input, .form-group select { height: 38px; padding: 0 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 14px; color: #334155; outline: none; }
.radio-group { display: flex; gap: 16px; height: 38px; align-items: center; }
.radio-label { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #334155; cursor: pointer; }
.address-section-header { display: flex; justify-content: space-between; align-items: center; border-top: 1px solid #e2e8f0; padding-top: 20px; margin-bottom: 15px; }
.btn-add-address { background: #eff6ff; color: #1d4ed8; border: 1px dashed #1d4ed8; padding: 6px 14px; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 500; }
.address-list-container { display: flex; flex-direction: column; gap: 16px; }
.address-box-item { border: 1px solid #e2e8f0; padding: 16px; border-radius: 6px; background-color: #fafafa; }
.address-box-header { display: flex; justify-content: space-between; align-items: center; }
.badge-title { font-size: 13px; font-weight: bold; color: #f79b66; }
.btn-remove-address { background: #fef2f2; color: #dc2626; border: 1px solid #fee2e2; padding: 4px 10px; border-radius: 4px; cursor: pointer; font-size: 12px; }
.button-group { display: flex; justify-content: center; gap: 12px; }
.btn-save { background: #f79b66; color: white; border: none; padding: 10px 24px; border-radius: 4px; cursor: pointer; font-weight: 500; font-size: 14px; }
.btn-cancel { background: #cbd5e1; color: #475569; border: none; padding: 10px 24px; border-radius: 4px; cursor: pointer; font-size: 14px; }
</style>