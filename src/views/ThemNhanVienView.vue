<template>
  <MainLayout title="Quản lý nhân viên">
    <div class="customer-card">
      <div class="page-header">
        <button type="button" class="back-link" @click="router.push('/nhan-vien')">
          &larr; Quay lại danh sách
        </button>
        <p>Nhập thông tin nhân viên mới</p>
      </div>

      <div class="qr-scan-section" style="margin-bottom: 25px; border-bottom: 1px dashed #e2e8f0; padding-bottom: 20px;">
        <button type="button" @click="startScan" class="btn-save" style="background-color: #1890ff; display: inline-flex; align-items: center; gap: 8px;">
        Quét QR CCCD lấy thông tin nhanh
        </button>
        <div v-if="isScanning" id="qr-reader-nv" style="max-width: 350px; margin-top: 15px; border: 1px solid #ddd; border-radius: 6px;"></div>
        <button v-if="isScanning" type="button" @click="stopScan" class="btn-cancel" style="margin-top: 10px; padding: 6px 16px;">Tắt Camera</button>
      </div>

      <form @submit.prevent="saveNhanVien">
        <div class="info-section">
          <div class="form-content">
            <h3>Thông tin cá nhân</h3>
            <div class="form-grid">
              <div class="form-group" :class="{ 'has-error': errors.tenNv }">
                <label>Họ và tên nhân viên <span>*</span></label>
                <input type="text" v-model="nhanVienForm.tenNv" placeholder="Nhập tên nhân viên" @input="errors.tenNv = ''"/>
                <span v-if="errors.tenNv" class="error-text">{{ errors.tenNv }}</span>
              </div>

              <div class="form-group" :class="{ 'has-error': errors.sdt }">
                <label>Số điện thoại liên hệ <span>*</span></label>
                <input type="text" v-model="nhanVienForm.sdt" placeholder="Nhập số điện thoại" @input="errors.sdt = ''" @blur="validateSdtChuDong"/>
                <span v-if="errors.sdt" class="error-text">{{ errors.sdt }}</span>
              </div>

              <div class="form-group" :class="{ 'has-error': errors.email }">
                <label>Địa chỉ Email <span>*</span></label>
                <input type="text" v-model="nhanVienForm.email" placeholder="Nhập địa chỉ email" @input="errors.email = ''" @blur="validateEmailChuDong"/>
                <span v-if="errors.email" class="error-text">{{ errors.email }}</span>
              </div>

              <div class="form-group">
                <label>Ngày sinh</label>
                <input type="date" v-model="nhanVienForm.ngaySinh"/>
              </div>

              <div class="form-group">
                <label>Giới tính</label>
                <div class="radio-group">
                  <label class="radio-label"><input type="radio" :value="1" v-model="nhanVienForm.gioiTinh" style="accent-color: #f79b66;"/> Nam</label>
                  <label class="radio-label"><input type="radio" :value="0" v-model="nhanVienForm.gioiTinh" style="accent-color: #f79b66;"/> Nữ</label>
                </div>
              </div>

              <div class="form-group">
                <label>Vai trò hệ thống</label>
                <select v-model="nhanVienForm.vaiTro">
                  <option :value="1">Quản trị viên</option>
                  <option :value="2">Quản lý</option>
                  <option :value="3">Nhân viên</option>
                </select>
              </div>
            </div>
          </div>
        </div>

        <div class="address-card">
          <div v-for="(item, index) in nhanVienForm.listDiaChi" :key="index" class="address-item">
            <div class="address-title" style="font-weight: bold; color: #f79b66; margin-bottom: 15px;">Địa chỉ nhân viên liên hệ</div>

            <div class="form-grid" style="margin-bottom: 15px;">
              <div class="form-group">
                <label>Tỉnh / Thành phố <span class="required">*</span></label>
                <select v-model="item.tinhThanh" class="form-select-control" @change="handleThayDoiTinhThanh(item)" required>
                  <option value="">-- Chọn Tỉnh / Thành phố --</option>
                  <option v-for="tinh in listTinhThanh" :key="tinh.code" :value="tinh.name">{{ tinh.name }}</option>
                </select>
              </div>

              <div class="form-group">
                <label>Quận / Huyện <span class="required">*</span></label>
                <select v-model="item.quanHuyen" class="form-select-control" :disabled="!item.tinhThanh" @change="handleThayDoiQuanHuyen(item)" required>
                  <option value="">-- Chọn Quận / Huyện --</option>
                  <option v-for="quan in item.listQuanHuyenTmp" :key="quan.code" :value="quan.name">{{ quan.name }}</option>
                </select>
              </div>
            </div>

            <div class="form-grid" style="margin-bottom: 15px;">
              <div class="form-group">
                <label>Phường / Xã / Thị trấn <span class="required">*</span></label>
                <select v-model="item.phuongXa" class="form-select-control" :disabled="!item.quanHuyen" required>
                  <option value="">-- Chọn Phường / Xã --</option>
                  <option v-for="xa in item.listPhuongXaTmp" :key="xa.code" :value="xa.name">{{ xa.name }}</option>
                </select>
              </div>

              <div class="form-cell w-100">
                <label style="font-size: 13px; font-weight: 500; color: #475569; display: block; margin-bottom: 6px;">Địa chỉ chi tiết (Số nhà, tên đường...) <span class="required">*</span></label>
                <input type="text" v-model="item.chiTietCuThe" placeholder="Nhập số nhà, tên đường, ngõ ngách..." class="form-input-text" style="height: 38px; padding: 0 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 14px; width: 100%; box-sizing: border-box;" required />
              </div>
            </div>
          </div>
        </div>

        <div class="button-group" style="margin-top: 30px;">
          <button type="submit" class="btn-save">💾 Lưu thông tin nhân viên</button>
          <button type="button" class="btn-cancel" @click="router.push('/nhan-vien')">✖ Hủy bỏ</button>
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
import { Html5QrcodeScanner } from 'html5-qrcode'
import { addNhanVien, checkDuplicate} from '@/service/NhanVienService'

const router = useRouter()
const listTinhThanh = ref([])
const isScanning = ref(false)
let qrScanner = null

// Kiểm tra tên: trên 3 ký tự, dưới 100 ký tự, không chứa ký tự đặc biệt
const validateTenNv = (name) => {
  const nameRegex = /^[\p{L}\s]+$/u; // Chỉ cho phép chữ cái và khoảng trắng
  if (name.length < 3 || name.length > 100) return 'Tên phải từ 3 đến 100 ký tự!';
  if (!nameRegex.test(name)) return 'Tên không được chứa ký tự đặc biệt hoặc số!';
  return '';
};

// Kiểm tra đủ 18 tuổi
const validateTuoi = (ngaySinh) => {
  if (!ngaySinh) return 'Vui lòng chọn ngày sinh!';
  const birthDate = new Date(ngaySinh);
  const today = new Date();
  let age = today.getFullYear() - birthDate.getFullYear();
  const m = today.getMonth() - birthDate.getMonth();
  if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
    age--;
  }
  return age >= 18 ? '' : 'Nhân viên phải đủ 18 tuổi!';
};
// Đã quy hoạch lại tên biến chuẩn dữ liệu Nhân viên (tenNv, sdt, vaiTro, trangThai)
const nhanVienForm = ref({
  tenNv: '', sdt: '', email: '', ngaySinh: '', gioiTinh: 1, vaiTro: 3, trangThai: 1, avatar: null,
  listDiaChi: [{ tinhThanh: '', quanHuyen: '', phuongXa: '', chiTietCuThe: '', isDefault: true, listQuanHuyenTmp: [], listPhuongXaTmp: [] }]
})

const errors = reactive({ tenNv: '', sdt: '', email: '' })

// --- LOGIC QUÉT VÀ BÓC TÁCH CHUỖI QR CCCD ---
const startScan = () => {
  isScanning.value = true
  setTimeout(() => {
    qrScanner = new Html5QrcodeScanner("qr-reader-nv", { fps: 10, qrbox: { width: 250, height: 250 } }, false)
    qrScanner.render(onScanSuccess, () => {})
  }, 300)
}

const stopScan = () => {
  if (qrScanner) {
    qrScanner.clear().then(() => { isScanning.value = false })
  }
}

const onScanSuccess = (decodedText) => {
  alert('Quét thông tin CCCD thành công! 🎉')
  stopScan()
  if (decodedText.includes('|')) {
    const parts = decodedText.split('|')
    if (parts.length >= 6) {
      nhanVienForm.value.tenNv = parts[2] // Lấy họ tên
      if (parts[3]?.length === 8) { // Bóc ngày sinh ddmmyyyy -> yyyy-mm-dd
        nhanVienForm.value.ngaySinh = `${parts[3].substring(4, 8)}-${parts[3].substring(2, 4)}-${parts[3].substring(0, 2)}`
      }
      nhanVienForm.value.gioiTinh = parts[4] === 'Nam' ? 1 : 0 // Lấy giới tính
      if (nhanVienForm.value.listDiaChi.length > 0) {
        nhanVienForm.value.listDiaChi[0].chiTietCuThe = parts[5] // Lấy địa chỉ thường trú gốc điền vào ô chi tiết
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

const validateSdtChuDong = () => { 
  if (!/^(0[3|5|7|8|9])([0-9]{8})$/.test(nhanVienForm.value.sdt.trim())) { 
    errors.sdt = 'SĐT không hợp lệ!'; return false 
  } 
  errors.sdt = ''; return true 
}

const validateEmailChuDong = () => { 
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(nhanVienForm.value.email.trim())) { 
    errors.email = 'Email sai định dạng!'; return false 
  } 
  errors.email = ''; return true 
}

// --- SUBMIT LƯU DỮ LIỆU NHÂN VIÊN ---
const saveNhanVien = async () => {
  const name = nhanVienForm.value.tenNv.trim();
  const sdt = nhanVienForm.value.sdt.trim();
  const email = nhanVienForm.value.email.trim();

  // 1. Validate Tên
  const errorName = validateTenNv(name);
  if (errorName) {
    errors.tenNv = errorName;
    return alert(errorName);
  }

  // 2. Validate Tuổi
  const errorAge = validateTuoi(nhanVienForm.value.ngaySinh);
  if (errorAge) return alert(errorAge);

  // 3. Validate Định dạng SĐT & Email
  if (!validateSdtChuDong() || !validateEmailChuDong()) {
    return alert('SĐT hoặc Email sai định dạng!');
  }

  // 4. Validate Địa chỉ
  const trong = nhanVienForm.value.listDiaChi.some(a => 
    !a.tinhThanh || !a.quanHuyen || !a.phuongXa || !a.chiTietCuThe.trim()
  );
  if (trong) return alert('Vui lòng điền đầy đủ thông tin địa chỉ!');

  // 5. Kiểm tra trùng lặp (Server Side)
  try {
    const isDuplicate = await checkDuplicate(sdt, email);
    if (isDuplicate) {
      return alert('⚠️ Cảnh báo: Số điện thoại hoặc Email đã tồn tại trong hệ thống!');
    }
  } catch (err) {
    console.error("Lỗi kiểm tra trùng:", err);
    return alert('Không thể kiểm tra dữ liệu, vui lòng thử lại sau.');
  }

  // 6. Gửi Payload
  try {
    const active = nhanVienForm.value.listDiaChi[0];
    const payload = {
      tenNv: name,
      sdt: sdt,
      email: email,
      gioiTinh: Number(nhanVienForm.value.gioiTinh),
      ngaySinh: nhanVienForm.value.ngaySinh || null,
      trangThai: Number(nhanVienForm.value.trangThai),
      diaChi: `${active.chiTietCuThe.trim()}, ${active.phuongXa}, ${active.tinhThanh}`,
      tinhThanh: active.tinhThanh,
      phuongXa: `${active.phuongXa}, ${active.quanHuyen}`,
      diaChiChiTiet: active.chiTietCuThe.trim(),
      avatar: nhanVienForm.value.avatar,
      vaiTro: { id: Number(nhanVienForm.value.vaiTro) }
    };

    await addNhanVien(payload);
    alert('🎉 Thêm mới nhân viên thành công!');
    router.push('/nhan-vien');
  } catch (error) {
    alert('Thất bại: Có lỗi xảy ra trong quá trình lưu dữ liệu!');
  }
};
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
.form-content h3 { font-size: 15px; font-weight: 600; color: #1e293b; margin: 0 0 16px 0; }
.form-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 16px; }
.form-group { display: flex; flex-direction: column; gap: 6px; }
.form-group label { font-size: 13px; font-weight: 500; color: #475569; }
.form-group label span { color: #ef4444; }
.form-group input, .form-group select { height: 38px; padding: 0 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 14px; color: #334155; outline: none; }
.radio-group { display: flex; gap: 16px; height: 38px; align-items: center; }
.radio-label { display: flex; align-items: center; gap: 6px; font-size: 13px; color: #334155; cursor: pointer; }
.address-card { border: 1px solid #e2e8f0; border-radius: 8px; padding: 20px; margin-bottom: 24px; margin-top: 25px;}
.address-item { border: 1px solid #e2e8f0; padding: 16px; border-radius: 6px; }
.address-title { font-size: 13px; font-weight: 600; }
.button-group { display: flex; justify-content: center; gap: 12px; }
.btn-save { background: #f79b66; color: white; border: none; padding: 10px 24px; border-radius: 4px; cursor: pointer; font-weight: 500; font-size: 14px; }
.btn-cancel { background: #cbd5e1; color: #475569; border: none; padding: 10px 24px; border-radius: 4px; cursor: pointer; font-size: 14px; }
.required { color: #ff4d4f; }
</style>