<template>
  <MainLayout title="Quản Lý Khách Hàng">
    <div class="customer-card">
      <div class="page-header">
        <button type="button" class="back-link" @click="router.push('/khach-hang')">
          &larr; Quay lại danh sách
        </button>
        <p>Nhập thông tin khách hàng mới</p>
      </div>

      <form @submit.prevent="saveKhachHang">
        <div class="info-section">
          <div class="form-content">
            <h3>Thông tin cá nhân</h3>
            <div class="form-grid">
              <div class="form-group" :class="{ 'has-error': errors.tenKh }">
                <label>Họ và tên khách hàng <span>*</span></label>
                <input type="text" v-model="khachHang.tenKh" placeholder="Nhập tên khách hàng" @input="errors.tenKh = ''"/>
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

              <div class="form-group">
                <label>Ngày sinh</label>
                <input type="date" v-model="khachHang.ngaySinh"/>
              </div>

              <div class="form-group">
                <label>Giới tính</label>
                <div class="radio-group">
                  <label class="radio-label"><input type="radio" :value="1" v-model="khachHang.gioiTinh" style="accent-color: #f79b66;"/> Nam</label>
                  <label class="radio-label"><input type="radio" :value="0" v-model="khachHang.gioiTinh" style="accent-color: #f79b66;"/> Nữ</label>
                </div>
              </div>

              <div class="form-group">
                <label>Trạng thái tài khoản</label>
                <select v-model="khachHang.trangThai">
                  <option :value="1">Đang hoạt động</option>
                  <option :value="0">Ngừng hoạt động</option>
                </select>
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
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import MainLayout from '../layouts/MainLayout.vue'

const router = useRouter()
const listTinhThanh = ref([])

const khachHang = reactive({
  tenKh: '', sdt: '', email: '', ngaySinh: '', gioiTinh: 1, trangThai: 1,
  listDiaChi: [{ tenNguoiNhan: '', sdtNguoiNhan: '', tinhThanh: '', quanHuyen: '', phuongXa: '', chiTietCuThe: '', isDefault: true, listQuanHuyenTmp: [], listPhuongXaTmp: [] }]
})

const errors = reactive({ tenKh: '', sdt: '', email: '' })

// 1. Tải danh sách 63 tỉnh thành gốc khi vừa vào trang
const load63TinhThanhTuAPI = async () => {
  try {
    const res = await axios.get('https://provinces.open-api.vn/api/p/')
    listTinhThanh.value = res.data || []
  } catch (e) { console.error(e) }
}

// 2. Khi đổi Tỉnh thành -> Gọi lấy danh sách Quận/Huyện trực thuộc
const handleThayDoiTinhThanh = async (item) => {
  item.quanHuyen = ''; item.phuongXa = ''; item.listQuanHuyenTmp = []; item.listPhuongXaTmp = [];
  if (!item.tinhThanh) return
  const tinh = listTinhThanh.value.find(t => t.name === item.tinhThanh)
  if (tinh) {
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${tinh.code}?depth=2`)
    item.listQuanHuyenTmp = res.data.districts || []
  }
}

// 3. Khi đổi Quận/Huyện -> Gọi lấy danh sách Phường/Xã/Thị trấn trực thuộc
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

// --- QUẢN LÝ SỔ ĐỊA CHỈ HÀNG LOẠT ---
const themKhoiDiaChiMoi = () => {
  khachHang.listDiaChi.push({
    tenNguoiNhan: khachHang.tenKh || '', sdtNguoiNhan: khachHang.sdt || '', tinhThanh: '', quanHuyen: '', phuongXa: '', chiTietCuThe: '', isDefault: khachHang.listDiaChi.length === 0, listQuanHuyenTmp: [], listPhuongXaTmp: []
  })
}
const xoaKhoiDiaChi = (index) => { const wasDef = khachHang.listDiaChi[index].isDefault; khachHang.listDiaChi.splice(index, 1); if (wasDef && khachHang.listDiaChi.length > 0) khachHang.listDiaChi[0].isDefault = true }
const datDiaChiMacDinh = (index) => { khachHang.listDiaChi.forEach((addr, idx) => addr.isDefault = idx === index) }

const validateSdtChuDong = () => { if (!/^(0[3|5|7|8|9])([0-9]{8})$/.test(khachHang.sdt.trim())) { errors.sdt = 'SĐT không hợp lệ!'; return false } errors.sdt = ''; return true }
const validateEmailChuDong = () => { if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(khachHang.email.trim())) { errors.email = 'Email sai định dạng!'; return false } errors.email = ''; return true }

const saveKhachHang = async () => {
  const name = khachHang.tenKh.trim()
  if (!name || !/^[\p{L}\s]+$/u.test(name)) return alert('Họ tên sai ký tự hoặc rỗng!')
  if (!validateSdtChuDong() || !validateEmailChuDong()) return alert('Vui lòng sửa các trường lỗi đỏ!')

  let trong = khachHang.listDiaChi.some(a => !a.tenNguoiNhan.trim() || !a.sdtNguoiNhan.trim() || !a.tinhThanh || !a.quanHuyen || !a.phuongXa || !a.chiTietCuThe.trim())
  if (trong) return alert('Vui lòng điền đầy đủ mọi ô trống trong khối địa chỉ!')

  try {
    const addressesPayload = khachHang.listDiaChi.map(addr => ({
      tenNguoiNhan: addr.tenNguoiNhan.trim(),
      sdtNguoiNhan: addr.sdtNguoiNhan.trim(),
      tinhThanh: addr.tinhThanh,
      // Ghép nối chuỗi: "Phường/Xã, Quận/Huyện" để tương thích với trường phuongXa duy nhất ở Backend DTO của bạn
      phuongXa: `${addr.phuongXa}, ${addr.quanHuyen}`, 
      diaChiChiTiet: addr.chiTietCuThe.trim(),
      macDinh: addr.isDefault ? 1 : 0
    }))

    const payload = {
      hoTen: name, 
      sdt: khachHang.sdt.trim(), 
      email: khachHang.email.trim(), 
      gioiTinh: parseInt(khachHang.gioiTinh, 10), 
      ngaySinh: khachHang.ngaySinh || null, 
      trangThai: khachHang.trangThai, 
      addresses: addressesPayload
    }

    const response = await axios.post('http://localhost:8080/public/khach-hang/add', payload)
    if (response.status === 200 || response.status === 201) { alert('🎉 Thêm khách hàng và sổ địa chỉ 3 cấp thành công!'); router.push('/khach-hang') }
  } catch (error) { alert('Thất bại: ' + (error.response?.data?.message || error.message)) }
}
</script>

<style scoped>
.form-group.has-error input { border-color: #ef4444 !important; background-color: #fef2f2; }
.form-select-control { width: 100%; padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; height: 38px; background-color: #fff; font-size: 14px; color: #334155; outline: none; box-sizing: border-box; }
.form-select-control:focus { border-color: #f79b66; }
.error-text { color: #ef4444; font-size: 13px; margin-top: 5px; display: block; text-align: left;}
.customer-card { background: #ffffff; padding: 24px; border-radius: 8px; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; text-align: left; }
.page-header { margin-bottom: 20px; }
.back-link { background: none; border: none; color: #94a3b8; cursor: pointer; font-size: 14px; padding: 0; margin-bottom: 6px; }
.page-header p { font-size: 18px; font-weight: 600; color: #1e293b; margin: 0; }
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