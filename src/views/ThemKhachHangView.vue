<template>
  <MainLayout title="Khách hàng">
    <div class="customer-card">

      <div class="page-header">
        <button type="button" class="back-link" @click="router.push('/khach-hang')">
          ← Quay lại danh sách
        </button>
        <p>Nhập thông tin khách hàng mới</p>
      </div>

      <div class="qr-control-section" style="margin-bottom: 25px; padding: 0 5px;">
        <button v-if="!isScanning" type="button" class="btn-trigger-camera" @click="startScan">
          📷 Bật Camera quét QR thông tin nhanh
        </button>
        <button v-else type="button" class="btn-close-camera" @click="stopScan">
          ✕ Tắt Camera
        </button>

        <div v-if="isScanning" class="camera-display-box" style="max-width: 400px; margin-top: 15px;">
          <div id="qr-reader-kh"></div>
        </div>
      </div>

      <form @submit.prevent="saveKhachHang">
        <div class="info-section">

          <div class="avatar-box">
            <h3>Ảnh đại diện</h3>
            <div class="avatar-preview">
              <img v-if="khachHang.avatar" :src="khachHang.avatar" alt="avatar" />
              <span v-else>👤</span>
            </div>

            <label class="upload-btn" style="cursor: pointer; display: inline-block;">
              📷 Chọn ảnh
              <input id="avatar-file-input" type="file" accept="image/*" style="display: none;"
                @change="handleImageUpload" />
            </label>

            <small>JPG, PNG (tối đa 2MB)</small>
          </div>

          <div class="form-content">
            <h3>Thông tin khách hàng</h3>

            <div class="form-grid">
              <div class="form-group">
                <label>Họ và tên <span>*</span></label>
                <input type="text" v-model="khachHang.hoTen" placeholder="Nhập họ và tên" />
              </div>

              <div class="address-grid">
                <div class="form-group">
                  <label>Người nhận <span>*</span></label>
                  <input type="text" v-model="itemAddress.nguoiNhan" placeholder="Nhập tên người nhận" required />
                </div>

                <div class="form-group">
                  <label>SĐT người nhận <span>*</span></label>
                  <input type="text" v-model="itemAddress.sdt" placeholder="Nhập SĐT người nhận" required />
                </div>
              </div>

              <div class="form-group" :class="{ 'has-error': errors.sdt }">
                <label>Số điện thoại <span>*</span></label>
                <input type="text" v-model="khachHang.sdt" placeholder="Nhập số điện thoại" @blur="validateSdt"
                  @input="errors.sdt = ''" />
                <span v-if="errors.sdt" class="error-text">{{ errors.sdt }}</span>
              </div>

              <div class="form-group" :class="{ 'has-error': errors.email }">
                <label>Email</label>
                <input type="email" v-model="khachHang.email" placeholder="Nhập email" @blur="validateEmail"
                  @input="errors.email = ''" />
                <span v-if="errors.email" class="error-text">{{ errors.email }}</span>
              </div>

              <div class="form-group">
                <label>Ngày sinh</label>
                <input type="date" v-model="khachHang.ngaySinh" />
              </div>

              <div class="form-group">
                <label>Giới tính</label>
                <div style="display: flex; gap: 15px; margin-top: 8px;">
                  <label style="display: inline-flex; align-items: center; gap: 6px; cursor: pointer;">
                    <input type="radio" value="1" v-model="khachHang.gioiTinh"
                      style="width: 16px; height: 16px; accent-color: #007bff; cursor: pointer;" />
                    Nam
                  </label>
                  <label style="display: inline-flex; align-items: center; gap: 6px; cursor: pointer;">
                    <input type="radio" value="0" v-model="khachHang.gioiTinh"
                      style="width: 16px; height: 16px; accent-color: #007bff; cursor: pointer;" />
                    Nữ
                  </label>
                </div>
              </div>

              <div class="form-group">
                <label>Trạng thái</label>
                <div style="display: flex; gap: 20px; margin-top: 8px;">
                  <label style="display: inline-flex; align-items: center; gap: 6px; cursor: pointer; color: #333;">
                    <input type="radio" :value="1" v-model="khachHang.trangThai"
                      style="width: 18px; height: 18px; accent-color: #007bff; cursor: pointer;" />
                    Hoạt động
                  </label>
                  <label style="display: inline-flex; align-items: center; gap: 6px; cursor: pointer; color: #333;">
                    <input type="radio" :value="0" v-model="khachHang.trangThai"
                      style="width: 18px; height: 18px; accent-color: #007bff; cursor: pointer;" />
                    Ngừng hoạt động
                  </label>
                </div>
              </div>

            </div>
          </div>

        </div>

        <div class="address-card">
          <div class="address-header">
            <h3>Danh sách địa chỉ</h3>
            <button type="button" class="btn-add-address" @click="addAddress">
              + Thêm địa chỉ
            </button>
          </div>

          <div v-for="(itemAddress, index) in khachHang.addresses" :key="itemAddress.id || index" class="address-item">
            <div class="address-title">
              📍 Địa chỉ {{ index + 1 }}
            </div>

            <div class="address-grid">
              <div class="form-group">
                <label>Tỉnh / Thành phố</label>
                <select v-model="itemAddress.tinhThanh">
                  <option value="">Chọn Tỉnh / Thành phố</option>
                  <option value="Hà Nội">Hà Nội</option>
                  <option value="Hồ Chí Minh">Hồ Chí Minh</option>
                  <option value="Đà Nẵng">Đà Nẵng</option>
                  <option value="Hải Phòng">Hải Phòng</option>
                </select>
              </div>

              <div class="form-group">
                <label>Phường / Xã</label>
                <input type="text" v-model="itemAddress.phuongXa" placeholder="Nhập phường/xã" />
              </div>
            </div>

            <div class="form-group">
              <label>Địa chỉ chi tiết</label>
              <input type="text" v-model="itemAddress.chiTiet" placeholder="Nhập số nhà, tên đường..." />
            </div>

            <div class="address-footer">
              <label class="default-address">
                <input type="checkbox" :checked="itemAddress.isDefault" @change="setMainAddress(index)" />
                Đặt làm địa chỉ mặc định
              </label>

              <button v-if="khachHang.addresses.length > 1" type="button" class="btn-delete"
                @click="removeAddress(index)">
                🗑 Xóa
              </button>
            </div>
          </div>
        </div>

        <div class="button-group">
          <button type="submit" class="btn-save">
            💾 Lưu khách hàng
          </button>
          <button type="button" class="btn-cancel" @click="router.push('/khach-hang')">
            ✖ Hủy bỏ
          </button>
        </div>

      </form>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, reactive, onBeforeUnmount, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { Html5QrcodeScanner } from 'html5-qrcode'
import MainLayout from '../layouts/MainLayout.vue'

const router = useRouter()
const totalCustomers = ref(0)

// Quản lý thông báo lỗi hiển thị trên giao diện
const errors = reactive({
  sdt: '',
  email: ''
})

// 🌟 SỬA LỖI: Đổi cấu trúc diaChi thành addresses để đồng bộ 100% với file Template
const khachHang = reactive({
  hoTen: '',
  sdt: '',
  email: '',
  ngaySinh: '',
  gioiTinh: '1',
  trangThai: 1,
  avatar: '',
  addresses: [
    {
      id: Date.now(),
      tinhThanh: '',
      phuongXa: '',
      chiTiet: '', // Đổi diaChiChiTiet thành chiTiet chuẩn dữ liệu
      isDefault: true // Đổi macDinh thành isDefault
    }
  ]
})

onMounted(async () => {
  try {
    const response = await axios.get('http://localhost:8080/public/khach-hang/list', { headers: getAuthHeaders() })
    const list = response.data.content || response.data || []
    totalCustomers.value = list.length
  } catch (error) {
    console.error('Không thể lấy danh sách số lượng khách hàng:', error)
  }
})

// --- TIẾN TRÌNH QUÉT QR CCCD BẰNG CAMERA ---
const isScanning = ref(false)
let qrScanner = null

const startScan = () => {
  isScanning.value = true
  setTimeout(() => {
    qrScanner = new Html5QrcodeScanner("qr-reader-kh", {
      fps: 10,
      qrbox: { width: 250, height: 250 }
    }, false)
    qrScanner.render(onScanSuccess, onScanFailure)
  }, 300)
}

const stopScan = () => {
  if (qrScanner) {
    qrScanner.clear().then(() => {
      isScanning.value = false
    }).catch(err => console.error("Lỗi tắt quét QR:", err))
  } else {
    isScanning.value = false
  }
}

const onScanSuccess = (decodedText) => {
  alert('Quét thông tin thành công!')
  stopScan()

  if (decodedText.includes('|')) {
    const parts = decodedText.split('|')
    if (parts.length >= 6) {
      khachHang.hoTen = parts[2]
      const rawDate = parts[3]
      if (rawDate && rawDate.length === 8) {
        khachHang.ngaySinh = `${rawDate.substring(4, 8)}-${rawDate.substring(2, 4)}-${rawDate.substring(0, 2)}`
      }
      khachHang.gioiTinh = parts[4] === 'Nam' ? '1' : '0'

      if (khachHang.addresses.length > 0) {
        khachHang.addresses[0].chiTiet = parts[5]
      }
    }
  } else {
    if (khachHang.addresses.length > 0) {
      khachHang.addresses[0].chiTiet = decodedText
    }
  }
}

const onScanFailure = (error) => { }

onBeforeUnmount(() => {
  if (qrScanner) qrScanner.clear()
})

// --- LOGIC XỬ LÝ ĐỊA CHỈ & HÌNH ẢNH ---
// 🌟 SỬA LỖI: Cập nhật hàm thêm địa chỉ theo mảng cấu trúc mới
const addAddress = () => {
  khachHang.addresses.push({
    id: Date.now() + Math.random(),
    tinhThanh: '',
    phuongXa: '',
    chiTiet: '',
    isDefault: false
  })
}

// 🌟 SỬA LỖI: Cập nhật hàm xóa địa chỉ, tự động đẩy địa chỉ đầu lên làm mặc định nếu xóa mất ô mặc định

const removeAddress = (index) => {
  const wasDefault = khachHang.addresses[index].isDefault
  khachHang.addresses.splice(index, 1)
  
  if (wasDefault && khachHang.addresses.length > 0) {
    khachHang.addresses[0].isDefault = true
  }
}

const setMainAddress = (selectedIndex) => {
  khachHang.addresses.forEach((item, index) => {
    item.isDefault = (index === selectedIndex)
  })
}

// 🌟 BỔ SUNG: Hàm quản lý click chọn duy nhất một địa chỉ mặc định trong mảng
const handleSetDefaultAddress = (index) => {
  // Cập nhật mảng bằng cách tạo mới để trigger Vue reactivity
  khachHang.addresses = khachHang.addresses.map((addr, idx) => ({
    ...addr,
    isDefault: idx === index // Cái được click là true, còn lại là false
  }));
};

// 🌟 SỬA LỖI: Đổi từ tạo ObjectURL sang chuỗi Base64 hoàn chỉnh để lưu được xuống DB
const handleImageUpload = (event) => {
  const file = event.target.files[0]
  if (!file) return

  // Kiểm tra dung lượng
  if (file.size > 2 * 1024 * 1024) {
    alert('Kích thước ảnh không được vượt quá 2MB!')
    event.target.value = ''
    return
  }

  // Kiểm tra định dạng
  if (!file.type.startsWith('image/')) {
    alert('Vui lòng chọn file ảnh hợp lệ!')
    event.target.value = ''
    return
  }

  const reader = new FileReader()
  reader.onload = (e) => {
    khachHang.avatar = e.target.result
    console.log('✅ Chuyển đổi Base64 ảnh khách hàng thành công!')
  }
  reader.readAsDataURL(file)
}

// --- LOGIC HEADERS & CHECK TRÙNG LẶP ---
const getAuthHeaders = () => {
  const token = localStorage.getItem('token')
    || localStorage.getItem('accessToken')
    || sessionStorage.getItem('token')
  return {
    'Content-Type': 'application/json',
    'Authorization': token ? `Bearer ${token}` : ''
  }
}

const checkTrungLapKhachHang = async (sdt, email) => {
  try {
    const response = await axios.post('http://localhost:8080/public/khach-hang/check-trung', {
      sdt: sdt.trim(),
      email: email ? email.trim() : null
    }, { headers: getAuthHeaders() })

    return response.data
  } catch (error) {
    console.error('Lỗi hệ thống khi check trùng khách hàng:', error)
    return { sdtTrung: false, emailTrung: false }
  }
}

// --- HÀM VALIDATE CHỦ ĐỘNG KHI BLUR (RỜI CHUỘT) ---
const validateSdt = async () => {
  if (!khachHang.sdt.trim()) {
    errors.sdt = 'Số điện thoại không được để trống!'
    return
  }
  const phoneRegex = /(0[3|5|7|8|9])+([0-9]{8})\b/
  if (!phoneRegex.test(khachHang.sdt.trim())) {
    errors.sdt = 'Số điện thoại không đúng định dạng!'
    return
  }

  const res = await checkTrungLapKhachHang(khachHang.sdt, '')
  if (res.sdtTrung) {
    errors.sdt = 'Số điện thoại này đã tồn tại trong hệ thống!'
  } else {
    errors.sdt = ''
  }
}

const validateEmail = async () => {
  if (!khachHang.email || !khachHang.email.trim()) {
    errors.email = ''
    return
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(khachHang.email.trim())) {
    errors.email = 'Email không đúng định dạng!'
    return
  }

  const res = await checkTrungLapKhachHang('', khachHang.email)
  if (res.emailTrung) {
    errors.email = 'Địa chỉ Email này đã tồn tại trong hệ thống!'
  } else {
    errors.email = ''
  }
}

// --- CALL API LƯU THÔNG TIN KHÁCH HÀNG ---
// --- CALL API LƯU THÔNG TIN KHÁCH HÀNG ---
const saveKhachHang = async () => {
  // 1. Kiểm tra validate cơ bản
  if (!khachHang.hoTen.trim() || !khachHang.sdt.trim()) {
    alert('Vui lòng nhập đầy đủ các trường bắt buộc (Họ tên, Số điện thoại)!');
    return;
  }

  if (errors.sdt || errors.email) {
    alert('Vui lòng sửa các lỗi định dạng/trùng lặp trước khi lưu!');
    return;
  }

  // 2. Xử lý mảng địa chỉ - BỔ SUNG CÁC TRƯỜNG BẮT BUỘC ĐỂ KHÔNG BỊ LỖI NULL
  const now = new Date().toISOString();
  const processedAddresses = khachHang.addresses.map(addr => ({
    // Đảm bảo tên các key này khớp chính xác với Entity Address bên Java
    diaChiChiTiet: addr.chiTiet || '',
    phuongXa: addr.phuongXa || '',
    tinhThanh: addr.tinhThanh || '',
    nguoiNhan: khachHang.hoTen || 'Khách hàng', // Trường bị thiếu gây lỗi 400
    sdt: khachHang.sdt || '',                  // Trường bị thiếu gây lỗi 400
    macDinh: addr.isDefault ? 1 : 0,           // Chuyển boolean sang số (1/0)
    ngayTao: now,
    ngayCapNhat: now
  }));

  const dataPost = {
    hoTen: khachHang.hoTen.trim(),
    sdt: khachHang.sdt.trim(),
    email: khachHang.email || null,
    // ... các trường khác của khách hàng
    addresses: processedAddresses // Mảng chứa các object đã đủ 9 trường
  };

  // 4. Gửi API
  try {
    const response = await axios.post('http://localhost:8080/public/khach-hang/add', dataPost, {
      headers: getAuthHeaders()
    });

    if (response.status === 200 || response.status === 201) {
      alert('Thêm khách hàng thành công!');
      router.push('/khach-hang');
    }
  } catch (error) {
    console.error('Lỗi API:', error);
    // Hiển thị chi tiết lỗi từ backend để dễ debug
    const errorMsg = error.response?.data?.message || error.message || 'Thêm thất bại';
    alert('Thêm khách hàng thất bại: ' + errorMsg);
  }
};
</script>

<style scoped>
/* 1. CHUẨN HÓA PHÔNG CHỮ & RESET BOX-SIZING TOÀN BỘ COMPONENT */
* {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
  box-sizing: border-box;
}

/* 2. ĐỒNG BỘ CÁC Ô NHẬP THÔNG TIN (INPUT, SELECT, TEXTAREA) BẰNG NHAU */
input,
select,
textarea {
  width: 100%;
  height: 40px;
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 8px 12px;
  font-size: 14px;
  color: #333;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
  background-color: #fff;
}

textarea {
  height: 40px;
  min-height: 40px;
  padding-top: 10px;
}

input:focus,
select:focus,
textarea:focus {
  border-color: #e6925e;
  box-shadow: 0 0 0 3px rgba(230, 146, 94, 0.15);
}

/* 3. QUẢN LÝ LỖI VALIDATION (Đã đồng bộ lại class .input-error giống Template) */
input.input-error,
select.input-error,
.form-group.has-error input {
  border-color: #dc3545 !important;
  background-color: #fff8f8;
}

input.input-error:focus,
select.input-error:focus {
  box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.15);
}

.error-text {
  color: #dc3545;
  font-size: 12px;
  margin-top: 5px;
  display: block;
  font-weight: 500;
}

/* 4. CAMERA & QUÉT MÃ */
.btn-trigger-camera {
  background: #e4925d;
  color: white;
  border: none;
  padding: 0 16px;
  height: 40px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 13px;
  transition: background 0.2s;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.btn-trigger-camera:hover {
  background: #d0814f;
}

.btn-close-camera {
  background: #ff4d4f;
  color: white;
  border: none;
  padding: 0 16px;
  height: 40px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 13px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.camera-display-box {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

/* 5. GIAO DIỆN FORM CHÍNH */
.customer-card {
  background: #fff;
  border-radius: 14px;
  padding: 24px;
}

.page-header {
  margin-bottom: 25px;
}

.back-link {
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  margin-bottom: 10px;
  font-size: 14px;
}

.page-header h1 {
  font-size: 34px;
  margin: 0;
  font-weight: 700;
  color: #2c3e50;
}

.page-header p {
  color: #777;
  margin-top: 6px;
}

.info-section {
  display: flex;
  gap: 30px;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 20px;
}

.avatar-box {
  width: 220px;
  text-align: center;
  border-right: 1px solid #eee;
  padding-right: 20px;
}

.avatar-box h3 {
  text-align: left;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 600;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: #f3f3f3;
  margin: auto;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 45px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-btn {
  background-color: #3b82f6;
  color: white;
  padding: 10px 16px;
  border-radius: 6px;
  font-weight: 500;
  margin-top: 10px;
  display: inline-block;
  transition: 0.2s;
}

.upload-btn:hover {
  background-color: #2563eb;
}

.avatar-box small {
  display: block;
  color: #888;
  margin-top: 8px;
}

.form-content {
  flex: 1;
}

.form-content h3 {
  color: #000;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 600;
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

.form-group-full {
  display: flex;
  flex-direction: column;
  grid-column: span 2;
  /* 💡 BỔ SUNG: Giúp ô trạng thái co giãn hết 2 cột */
}

.form-group label,
.form-group-full label {
  font-weight: 600;
  margin-bottom: 7px;
  font-size: 14px;
  color: #444;
}

.form-group label span,
.form-group-full label span {
  color: red;
}

.radio-group {
  display: flex;
  gap: 25px;
  height: 40px;
  align-items: center;
}

.radio-label {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 14px;
}

.radio-label input[type="radio"] {
  width: 17px;
  height: 17px;
  accent-color: #e6925e;
  cursor: pointer;
}

/* 6. QUẢN LÝ DANH SÁCH ĐỊA CHỈ (Bổ sung layout đồng bộ với Template mới) */
.address-card {
  margin-top: 25px;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 20px;
}

.address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.address-header h3 {
  color: #000;
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.btn-add-address {
  background: #e6925e;
  color: white;
  border: none;
  padding: 0 15px;
  height: 36px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 13px;
}

.address-item {
  border: 1px solid #f1f5f9;
  background-color: #f8fafc;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 15px;
}

.address-title {
  font-weight: 600;
  color: #334155;
  font-size: 14px;
}

.address-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 15px;
}

.checkbox-label {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 13px;
  color: #475569;
}

.checkbox-label input[type="checkbox"] {
  width: 16px;
  height: 16px;
  accent-color: #e6925e;
  cursor: pointer;
}

.btn-delete-addr {
  background: #fff0f0;
  border: 1px solid #ffd2d2;
  color: #ff4d4f;
  padding: 6px 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.2s;
}

.btn-delete-addr:hover {
  background: #ff4d4f;
  color: white;
}

/* 7. NHÓM NÚT ĐIỀU HƯỚNG CUỐI PAGE */
.button-group {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-top: 25px;
}

.btn-save {
  background: #e6925e;
  color: white;
  border: none;
  padding: 0 24px;
  height: 44px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
}

.btn-cancel {
  background: #6c757d;
  color: white;
  border: none;
  padding: 0 24px;
  height: 44px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
}
</style>