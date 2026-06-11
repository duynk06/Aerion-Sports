<template>
  <MainLayout title="Quản Lý Nhân Viên">
    <div class="customer-card">

      <div class="page-header">
        <button
          type="button"
          class="back-link"
          @click="router.push('/nhan-vien')"
        >
          &larr; Quay lại danh sách
        </button>
        <p>Nhập thông tin nhân viên mới</p>
      </div>

      <div class="qr-control-section" style="margin-bottom: 25px;">
        <button v-if="!isScanning" type="button" class="btn-trigger-camera" @click="startScan">
          📷 Bật Camera quét QR thông tin nhanh (CCCD)
        </button>
        <button v-else type="button" class="btn-close-camera" @click="stopScan">
          ✕ Tắt Camera
        </button>
        
        <div v-if="isScanning" class="camera-display-box" style="max-width: 400px; margin-top: 15px;">
          <div id="qr-reader-nv"></div>
        </div>
      </div>

      <form @submit.prevent="saveNhanVien">
        <div class="info-section">

          <div class="avatar-box">
            <h3>Ảnh đại diện</h3>
            <div class="avatar-preview">
              <img v-if="nhanVien.avatar" :src="nhanVien.avatar" alt="avatar" />
              <span v-else>👤</span>
            </div>
            
            <label for="employee-avatar-input" class="upload-btn">
              Chọn ảnh
            </label>
            <input 
              id="employee-avatar-input"
              type="file" 
              style="display: none;" 
              accept="image/jpeg, image/jpg, image/png" 
              @change="handleImageUpload" 
            />
            
            <small style="display: block; margin-top: 8px; color: #94a3b8; font-size: 12px;">JPG, PNG (tối đa 2MB)</small>
          </div>

          <div class="form-content">
            <h3>Thông tin nhân viên</h3> <div class="form-grid">
              
              <div class="form-group">
                <label>Họ và tên <span>*</span></label>
                <input 
                  type="text" 
                  v-model="nhanVien.tenNv" 
                  placeholder="Nhập họ và tên" 
                  :class="{ 'input-error': errors.tenNv }"
                  @input="errors.tenNv = ''"
                />
                <span v-if="errors.tenNv" class="error-text">{{ errors.tenNv }}</span>
              </div>

              <div class="form-group">
                <label>Số điện thoại <span>*</span></label>
                <input 
                  type="text" 
                  v-model="nhanVien.sdt" 
                  placeholder="Nhập số điện thoại" 
                  :class="{ 'input-error': errors.sdt }"
                  @input="errors.sdt = ''"
                />
                <span v-if="errors.sdt" class="error-text">{{ errors.sdt }}</span>
              </div>

              <div class="form-group">
                <label>Email <span>*</span></label> <input 
                  type="type" 
                  v-model="nhanVien.email" 
                  placeholder="Nhập email" 
                  :class="{ 'input-error': errors.email }"
                  @input="errors.email = ''"
                />
                <span v-if="errors.email" class="error-text">{{ errors.email }}</span>
              </div>

              <div class="form-group">
                <label>Ngày sinh</label>
                <input type="date" v-model="nhanVien.ngaySinh" />
              </div>

              <div class="form-group">
                <label>Giới tính</label>
                <div class="radio-group">
                  <label class="radio-label">
                    <input type="radio" :value="1" v-model="nhanVien.gioiTinh" /> Nam
                  </label>
                  <label class="radio-label">
                    <input type="radio" :value="0" v-model="nhanVien.gioiTinh" /> Nữ
                  </label>
                </div>
              </div>

              <div class="form-group">
                <label>Vai trò <span>*</span></label>
                <select v-model="nhanVien.vaiTro">
                  <option :value="3">Nhân viên</option>
                  <option :value="2">Quản lý</option>
                  <option :value="1">Quản trị viên (Admin)</option>
                </select>
              </div>

              <div class="form-group-full">
                <label>Trạng thái</label>
                <select v-model="nhanVien.trangThai">
                  <option :value="1">Hoạt động</option>
                  <option :value="0">Ngừng hoạt động</option>
                </select>
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

          <div 
            v-for="(item, index) in nhanVien.listDiaChi" 
            :key="item.id || index" 
            class="address-item"
          >
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
              <div class="address-title">📍 Địa chỉ {{ index + 1 }}</div>
              <button 
                v-if="nhanVien.listDiaChi.length > 1" 
                type="button" 
                class="btn-delete-addr"
                @click="removeAddress(index)"
              >
                🗑 Xóa địa chỉ
              </button>
            </div>

            <div class="form-grid" style="margin-bottom: 15px;">
              <div class="form-group">
                <label>Tỉnh / Thành phố <span>*</span></label>
                <select v-model="item.tinhThanh">
                  <option value="">-- Chọn Tỉnh / Thành phố --</option>
                  <option value="Hà Nội">Hà Nội</option>
                  <option value="Hồ Chí Minh">Hồ Chí Minh</option>
                  <option value="Đà Nẵng">Đà Nẵng</option>
                </select>
              </div>

              <div class="form-group">
                <label>Phường / Xã <span>*</span></label>
                <input type="text" v-model="item.phuongXa" placeholder="Nhập phường / xã" />
              </div>
            </div>

            <div class="form-group" style="margin-bottom: 15px;">
              <label>Địa chỉ chi tiết <span>*</span></label>
              <input 
                type="text" 
                v-model="item.chiTietCuThe" 
                placeholder="Nhập số nhà, tên đường, ngõ ngách..." 
              />
            </div>

            <div>
              <label class="checkbox-label">
                <input type="checkbox" :checked="item.isDefault" @change="setMainAddress(index)" />
                Đặt làm địa chỉ mặc định
              </label>
            </div>
          </div>
        </div>

        <div class="button-group">
          <button type="submit" class="btn-save">Lưu nhân viên</button>
          <button type="button" class="btn-cancel" @click="router.push('/nhan-vien')">
            Hủy bỏ
          </button>
        </div>

      </form>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, reactive, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { Html5QrcodeScanner } from 'html5-qrcode'
import MainLayout from '../layouts/MainLayout.vue'

const router = useRouter()

// 🌟 ĐỒNG BỘ: Đổi trường 'anh' thành 'avatar' để đồng bộ toàn bộ dự án
const nhanVien = reactive({
  tenNv: '',
  sdt: '',
  email: '',
  ngaySinh: '',
  gioiTinh: 1, 
  vaiTro: 3,   
  trangThai: 1, 
  avatar: '',   // Đã sửa thành avatar
  listDiaChi: [
    {
      tinhThanh: '',
      phuongXa: '',
      chiTietCuThe: '', 
      isDefault: true   
    }
  ]
})

// Quản lý trạng thái thông báo lỗi cục bộ
const errors = reactive({
  tenNv: '',
  sdt: '',
  email: ''
})

// --- CAMERA QR CCCD ---
const isScanning = ref(false)
let qrScanner = null

const startScan = () => {
  isScanning.value = true
  setTimeout(() => {
    qrScanner = new Html5QrcodeScanner("qr-reader-nv", { 
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
    }).catch(err => console.error(err))
  } else {
    isScanning.value = false
  }
}

const onScanSuccess = (decodedText) => {
  alert('Quét thành công QR CCCD!')
  stopScan()
  
  if (decodedText.includes('|')) {
    const parts = decodedText.split('|')
    if (parts.length >= 6) {
      nhanVien.tenNv = parts[2]
      
      const rawDate = parts[3]
      if (rawDate && rawDate.length === 8) {
        nhanVien.ngaySinh = `${rawDate.substring(4, 8)}-${rawDate.substring(2, 4)}-${rawDate.substring(0, 2)}`
      }
      
      nhanVien.gioiTinh = parts[4] === 'Nam' ? 1 : 0
      
      if (nhanVien.listDiaChi.length > 0) {
        nhanVien.listDiaChi[0].chiTietCuThe = parts[5]
      }
    }
  } else {
    if (nhanVien.listDiaChi.length > 0) {
      nhanVien.listDiaChi[0].chiTietCuThe = decodedText
    }
  }
}

const onScanFailure = () => {}

onBeforeUnmount(() => {
  if (qrScanner) qrScanner.clear()
})

// --- LOGIC MẢNG ĐỊA CHỈ ---
const addAddress = () => {
  nhanVien.listDiaChi.push({
    tinhThanh: '',
    phuongXa: '',
    chiTietCuThe: '',
    isDefault: false
  })
}

// Nếu xóa đúng địa chỉ mặc định thì tự động đẩy dòng đầu tiên làm mặc định mới
const removeAddress = (index) => {
  const wasDefault = nhanVien.listDiaChi[index].isDefault
  nhanVien.listDiaChi.splice(index, 1)
  
  if (wasDefault && nhanVien.listDiaChi.length > 0) {
    nhanVien.listDiaChi[0].isDefault = true
  }
}

const setMainAddress = (selectedIndex) => {
  nhanVien.listDiaChi.forEach((item, index) => {
    item.isDefault = (index === selectedIndex)
  })
}

// --- XỬ LÝ ĐỌC FILE ẢNH SANG CHUỖI BASE64 ---
const handleImageUpload = (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (file.size > 2 * 1024 * 1024) {
    alert('Kích thước ảnh không được vượt quá 2MB!')
    return
  }
  
  const reader = new FileReader()
  reader.onload = (e) => {
    nhanVien.avatar = e.target.result // Đã sửa gán vào avatar
  }
  reader.onerror = (err) => {
    console.error("Lỗi khi đọc file ảnh:", err)
  }
  reader.readAsDataURL(file)
}

// --- SUBMIT LƯU DỮ LIỆU CHUẨN HOÁ PHÍA FRONTEND ---
const saveNhanVien = async () => {
  errors.tenNv = ''
  errors.sdt = ''
  errors.email = ''

  let hasError = false
  if (!nhanVien.tenNv.trim()) {
    errors.tenNv = 'Vui lòng không để trống Họ và tên!'
    hasError = true
  }
  if (!nhanVien.sdt.trim()) {
    errors.sdt = 'Vui lòng không để trống Số điện thoại!'
    hasError = true
  }
  
  if (hasError) return

  try {
    const activeAddr = nhanVien.listDiaChi.find(a => a.isDefault) || nhanVien.listDiaChi[0]
    
    let stringDiaChi = ''
    if (activeAddr) {
      stringDiaChi = `Số ${activeAddr.chiTietCuThe || ''}, Phường ${activeAddr.phuongXa || ''}, ${activeAddr.tinhThanh || ''}`
    }

    const processedAddresses = nhanVien.listDiaChi.map(addr => ({
      ...addr,
      chiTiet: `Số ${addr.chiTietCuThe || ''}, Phường ${addr.phuongXa || ''}, ${addr.tinhThanh || ''}`
    }))

    // Cấu trúc Payload đồng bộ trường avatar gửi lên Backend
    const dataPayload = {
      tenNv: nhanVien.tenNv.trim(),
      sdt: nhanVien.sdt.trim(),
      email: nhanVien.email.trim() || null, 
      gioiTinh: Number(nhanVien.gioiTinh),
      avatar: nhanVien.avatar || null, // Đã sửa đổi gửi avatar lên API
      ngaySinh: nhanVien.ngaySinh || null, 
      diaChi: stringDiaChi, 
      trangThai: Number(nhanVien.trangThai),
      addresses: processedAddresses, 
      vaiTro: {
        id: Number(nhanVien.vaiTro)
      }
    }

    console.log("Dữ liệu gửi lên API kiểm tra:", dataPayload)

    const response = await axios.post('http://localhost:8080/nhan-vien/add', dataPayload)

    if (response.status === 200 || response.status === 201) {
      alert('Thêm nhân viên mới thành công!')
      router.push('/nhan-vien')
    }
  } catch (error) {
    console.error("Chi tiết phản hồi lỗi mạng:", error)
    
    if (error.response && (error.response.status === 400 || error.response.status === 422)) {
      const serverMessage = error.response.data?.message || error.response.data
      const messageStr = String(serverMessage).toLowerCase()
      
      if (messageStr.includes('số điện thoại') || messageStr.includes('sdt') || messageStr.includes('phone')) {
        errors.sdt = 'Số điện thoại này đã được sử dụng trong hệ thống!'
        return
      } 
      
      if (messageStr.includes('email')) {
        errors.email = 'Địa chỉ email này đã được sử dụng trong hệ thống!'
        return
      }
    }
    
    const errDetail = error.response?.data?.message || error.response?.data || error.message
    alert('Lưu thất bại: ' + errDetail)
  }
}
</script>

<style scoped>
/* CSS bổ sung để hiển thị giao diện báo lỗi tinh tế */
.error-text {
  color: #ef4444;
  font-size: 13px;
  margin-top: 5px;
  display: block;
}

.input-error {
  border-color: #ef4444 !important;
  background-color: #fef2f2;
}
</style>

<style scoped>
/* Toàn bộ CSS đồng bộ tuyệt đối theo layout mẫu mượt mà của Khách hàng */
.customer-card {
  background: #ffffff;
  padding: 24px;
  border-radius: 8px;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}
.page-header {
  margin-bottom: 20px;
}
.back-link {
  background: none;
  border: none;
  color: #94a3b8;
  cursor: pointer;
  font-size: 14px;
  padding: 0;
  margin-bottom: 6px;
}
.page-header p {
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}
.info-section {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}
.avatar-box {
  width: 200px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}
.avatar-box h3, .form-content h3, .address-header h3 {
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 16px 0;
}
.avatar-preview {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: #f1f5f9;
  margin: 0 auto 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
}
.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.upload-btn {
  display: inline-block;
  padding: 6px 16px;
  background: #ffffff;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
  color: #334155;
}
.form-content {
  flex: 1;
}
.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}
.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.form-group-full {
  grid-column: span 2;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.form-group label, .form-group-full label {
  font-size: 13px;
  font-weight: 500;
  color: #475569;
}
.form-group label span, .form-group-full label span {
  color: #ef4444;
}
.form-group input, .form-group select, .form-group-full select {
  height: 38px;
  padding: 0 12px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  font-size: 14px;
  color: #334155;
  outline: none;
  background-color: #fff;
}
.radio-group {
  display: flex;
  gap: 16px;
  height: 38px;
  align-items: center;
}
.radio-label, .checkbox-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #334155;
  cursor: pointer;
}
.address-card {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 24px;
}
.address-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}
.address-header h3 { margin: 0; }
.btn-add-address {
  background: #f97316;
  color: white;
  border: none;
  padding: 6px 14px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
}
.address-item {
  border: 1px solid #e2e8f0;
  padding: 16px;
  border-radius: 6px;
  margin-bottom: 16px;
}
.address-title {
  font-size: 13px;
  font-weight: 600;
  color: #f97316;
}
.btn-delete-addr {
  background: none;
  border: none;
  color: #ef4444;
  cursor: pointer;
  font-size: 13px;
}
.button-group {
  display: flex;
  gap: 12px;
}
.btn-save {
  background: #f97316;
  color: white;
  border: none;
  padding: 10px 24px;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  font-size: 14px;
}
.btn-cancel {
  background: #f1f5f9;
  color: #475569;
  border: none;
  padding: 10px 24px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}
.btn-trigger-camera {
  background: #3b82f6;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}
.btn-close-camera {
  background: #ef4444;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}
</style>