<template>
  <MainLayout title="Cập nhật nhân viên">
    <div class="employee-form-container">
      <div class="back-header">
        <button type="button" class="btn-back-link" @click="quayLaiDanhSach">← Quay lại danh sách</button>
        <h3 class="page-form-title">Cập nhật thông tin nhân viên</h3>
      </div>

      <form @submit.prevent="handleSubmitForm">
        <div class="form-section-card edit-form-card profile-grid">
          <div class="avatar-upload-block">
            <div class="section-sub-title fw-bold mb-3">Ảnh đại diện</div>
            <div class="avatar-preview-circle mb-3">
              <img v-if="employeeForm.avatar" :src="employeeForm.avatar" alt="Avatar" />
              <span v-else class="default-avatar-icon">👤</span>
            </div>
            <button type="button" class="btn-upload-avatar mb-2" @click="triggerFormFileInput">📁 Chọn ảnh</button>
            <input type="file" ref="formFileInputRef" style="display: none;" accept="image/jpeg, image/png, image/jpg" @change="handleFormAvatarChange" />
            <small class="upload-hint">JPG, PNG (tối đa 2MB)</small>
          </div>

          <div class="info-fields-block">
            <div class="section-sub-title fw-bold mb-3">Thông tin cá nhân nhân viên</div>
            <div class="form-row">
              <div class="form-cell">
                <label>Họ và tên nhân viên <span class="required">*</span></label>
                <input type="text" v-model="employeeForm.hoTen" placeholder="Nhập họ và tên nhân viên" required />
              </div>
              <div class="form-cell">
                <label>Số điện thoại <span class="required">*</span></label>
                <input type="text" v-model="employeeForm.sdt" placeholder="Nhập số điện thoại" required />
              </div>
            </div>
            <div class="form-row">
              <div class="form-cell">
                <label>Email <span class="required">*</span></label>
                <input type="email" v-model="employeeForm.email" placeholder="Nhập email nhân viên" required />
              </div>
              <div class="form-cell">
                <label>Ngày sinh</label>
                <input type="date" v-model="employeeForm.ngaySinh" />
              </div>
            </div>
            <div class="form-row">
              <div class="form-cell">
                <label>Giới tính</label>
                <div class="radio-flex border-box">
                  <label><input type="radio" v-model="employeeForm.gioiTinh" value="1" /> Nam</label>
                  <label><input type="radio" v-model="employeeForm.gioiTinh" value="0" /> Nữ</label>
                </div>
              </div>
              <div class="form-cell">
                <label>Trạng thái</label>
                <select v-model="employeeForm.trangThai" class="select-box">
                  <option :value="1">Đang làm việc</option>
                  <option :value="0">Đã nghỉ việc</option>
                </select>
              </div>
            </div>
          </div>
        </div>

        <div class="form-section-card address-list-section">
          <div class="address-section-header">
            <div class="section-sub-title fw-bold">Địa chỉ thường trú (Nơi ở hiện tại)</div>
          </div>

          <div v-for="(addr, idx) in employeeForm.addresses" :key="addr.id" class="address-item-card">
            <div class="form-row">
              <div class="form-cell">
                <label>Tỉnh / Thành phố <span class="required">*</span></label>
                <select v-model="addr.tinhThanh" class="select-box" @change="handleThayDoiTinhThanhForm(addr)" required>
                  <option value="">-- Chọn Tỉnh / Thành phố --</option>
                  <option v-for="tinh in listTinhThanhAPI" :key="tinh.code" :value="tinh.name">{{ tinh.name }}</option>
                </select>
              </div>
              <div class="form-cell">
                <label>Quận / Huyện <span class="required">*</span></label>
                <select v-model="addr.quanHuyen" class="select-box" :disabled="!addr.tinhThanh" @change="handleThayDoiQuanHuyenForm(addr)" required>
                  <option value="">-- Chọn Quận / Huyện --</option>
                  <option v-for="quan in addr.listQuanHuyenTmp" :key="quan.code" :value="quan.name">{{ quan.name }}</option>
                </select>
              </div>
            </div>

            <div class="form-row">
              <div class="form-cell">
                <label>Phường / Xã / Thị trấn <span class="required">*</span></label>
                <select v-model="addr.phuongXa" class="select-box" :disabled="!addr.quanHuyen" required>
                  <option value="">-- Chọn Phường / Xã --</option>
                  <option v-for="xa in addr.listPhuongXaTmp" :key="xa.code" :value="xa.name">{{ xa.name }}</option>
                </select>
              </div>
              <div class="form-cell">
                <label>Địa chỉ cụ thể (Số nhà, tên đường...) <span class="required">*</span></label>
                <input type="text" v-model="addr.diaChiChiTiet" placeholder="Ví dụ: Số 15, ngõ 3, đường Nguyễn Trãi" required />
              </div>
            </div>
          </div>
        </div>

        <div class="form-submit-bar">
          <button type="button" class="btn-cancel-action" @click="quayLaiDanhSach">Hủy bỏ</button>
          <button type="submit" class="btn-submit-action">Lưu thông tin nhân viên</button>
        </div>
      </form>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import MainLayout from '../layouts/MainLayout.vue'
// Hãy điều chỉnh các hàm tương tác API nhân viên theo đúng tên Service thực tế của dự án bạn nhé
import { fetchAllNhanVien, updateNhanVien, checkDuplicate} from '@/service/NhanVienService'


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
const route = useRoute()
const router = useRouter()
const employeeId = route.params.id // Hứng ID động từ thanh URL

const listTinhThanhAPI = ref([])
const formFileInputRef = ref(null)

const employeeForm = ref({
  id: null, hoTen: '', sdt: '', email: '', ngaySinh: '', gioiTinh: '1', trangThai: 1, avatar: '',
  addresses: [{ id: 'ADDR_INIT_' + Date.now(), tinhThanh: '', quanHuyen: '', phuongXa: '', diaChiChiTiet: '', listQuanHuyenTmp: [], listPhuongXaTmp: [] }]
})

const load63TinhThanh = async () => {
  try {
    const res = await axios.get('https://provinces.open-api.vn/api/p/')
    listTinhThanhAPI.value = res.data || []
  } catch (e) { console.error("Lỗi tải API 63 tỉnh thành: ", e) }
}

const loadDuLieuNhanVienCu = async () => {
  try {
    const allData = await fetchAllNhanVien()
    
    // Tìm kiếm thông minh chấp nhận cả id hoặc idNhanVien
    const khopNhanVien = allData.find(item => {
      const currentId = item.id || item.idNhanVien || item.id_nhan_vien;
      return String(currentId) === String(employeeId);
    })
    
    if (khopNhanVien) {
      // Ép bản sao sạch để tránh đột biến dữ liệu gốc
      const nv = JSON.parse(JSON.stringify(khopNhanVien));
      
      let pXa = nv.phuongXa || ''
      let qHuyen = ''
      if (pXa.includes(', ')) {
        const parts = pXa.split(', ')
        pXa = parts[0]?.trim() || ''
        qHuyen = parts[1]?.trim() || ''
      }

      let huyenList = []
      let xaList = []

      // Bóc tách địa chính 3 cấp
      const tinhGoc = nv.tinhThanh || ''
      if (tinhGoc && listTinhThanhAPI.value.length > 0) {
        const tinh = listTinhThanhAPI.value.find(t => t.name.trim() === tinhGoc.trim())
        if (tinh) {
          const resHuyen = await axios.get(`https://provinces.open-api.vn/api/p/${tinh.code}?depth=2`)
          huyenList = resHuyen.data.districts || []
          
          if (qHuyen && huyenList.length > 0) {
            const quan = huyenList.find(q => q.name.trim() === qHuyen.trim())
            if (quan) {
              const resXa = await axios.get(`https://provinces.open-api.vn/api/d/${quan.code}?depth=2`)
              xaList = resXa.data.wards || []
            }
          }
        }
      }

      // Xử lý vai trò (Spring Boot mapping object)
      let role = 3;
      if (nv.vaiTro && typeof nv.vaiTro === 'object' && nv.vaiTro.id !== undefined) {
        role = Number(nv.vaiTro.id);
      } else if (nv.id_vai_tro !== undefined) {
        role = Number(nv.id_vai_tro);
      } else if (nv.vaiTro !== undefined) {
        role = Number(nv.vaiTro);
      }

      // Đổ và fill chuẩn xác lên Form
      employeeForm.value = {
        id: nv.id || nv.idNhanVien,
        hoTen: nv.tenNv || nv.ten_nv || '',
        sdt: nv.sdt || nv.sđt || '',
        email: nv.email || '',
        ngaySinh: nv.ngaySinh || '',
        gioiTinh: nv.gioiTinh !== undefined ? String(nv.gioiTinh) : '1',
        trangThai: (nv.trangThai === 1 || nv.trangThai === true) ? 1 : 0,
        avatar: nv.avatar || nv.anh || '',
        addresses: [{
          // Dùng ID động phá cache giúp select-box render fill chữ ngay lập tức
          id: 'ADDR_NV_' + Date.now(),
          tinhThanh: tinhGoc,
          quanHuyen: qHuyen,
          phuongXa: pXa,
          diaChiChiTiet: nv.diaChiChiTiet || nv.diaChi || '',
          listQuanHuyenTmp: huyenList,
          listPhuongXaTmp: xaList
        }]
      }
    }
  } catch (error) { 
    console.error("Lỗi nạp hồ sơ nhân viên cũ lên form: ", error) 
  }
}

// Luồng Async gối đầu tuần tự bắt buộc fill chuẩn dữ liệu hành chính
onMounted(async () => {
  await load63TinhThanh()
  await loadDuLieuNhanVienCu()
})

const handleThayDoiTinhThanhForm = async (addr) => {
  addr.quanHuyen = ''; addr.phuongXa = ''; addr.listQuanHuyenTmp = []; addr.listPhuongXaTmp = [];
  if (!addr.tinhThanh) return
  const tinh = listTinhThanhAPI.value.find(t => t.name === addr.tinhThanh)
  if (tinh) {
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${tinh.code}?depth=2`)
    addr.listQuanHuyenTmp = res.data.districts || []
  }
}

const handleThayDoiQuanHuyenForm = async (addr) => {
  addr.phuongXa = ''; addr.listPhuongXaTmp = [];
  if (!addr.quanHuyen) return
  const quan = addr.listQuanHuyenTmp.find(q => q.name === addr.quanHuyen)
  if (quan) {
    const res = await axios.get(`https://provinces.open-api.vn/api/d/${quan.code}?depth=2`)
    addr.listPhuongXaTmp = res.data.wards || []
  }
}

const triggerFormFileInput = () => { if (formFileInputRef.value) formFileInputRef.value.click() }
const handleFormAvatarChange = (e) => {
  const f = e.target.files[0]; if (!f) return
  const r = new FileReader(); r.onload = (ev) => employeeForm.value.avatar = ev.target.result; r.readAsDataURL(f)
}
const quayLaiDanhSach = () => router.push('/nhan-vien')

const handleSubmitForm = async () => {
  // Validate cơ bản
  const name = employeeForm.value.hoTen.trim();
  if (validateTenNv(name) || validateTuoi(employeeForm.value.ngaySinh)) {
    return alert('Thông tin cá nhân không hợp lệ!');
  }

  // Check trùng lặp
  try {
    const isDuplicate = await checkDuplicateUpdate(
      employeeForm.value.sdt.trim(), 
      employeeForm.value.email.trim(), 
      employeeForm.value.id
    );
    if (isDuplicate) {
      return alert('⚠️ Cảnh báo: SĐT hoặc Email đã được sử dụng bởi nhân viên khác!');
    }
  } catch (err) {
    return alert('Lỗi kiểm tra trùng lặp!');
  }

  // Tiếp tục xử lý Payload
  const activeAddr = employeeForm.value.addresses[0];
  const payload = {
    hoTen: name,
    sdt: employeeForm.value.sdt.trim(),
    email: employeeForm.value.email.trim(),
    ngaySinh: employeeForm.value.ngaySinh || null,
    gioiTinh: Number(employeeForm.value.gioiTinh),
    trangThai: Number(employeeForm.value.trangThai),
    avatar: employeeForm.value.avatar || null,
    tinhThanh: activeAddr.tinhThanh,
    phuongXa: `${activeAddr.phuongXa}, ${activeAddr.quanHuyen}`,
    diaChiChiTiet: activeAddr.diaChiChiTiet.trim(),
    vaiTro: { id: Number(employeeForm.value.vaiTro) } // Đừng quên gửi kèm vai trò
  };

  try {
    await updateNhanVien(employeeForm.value.id, payload);
    alert('🎉 Cập nhật thông tin nhân viên thành công!');
    quayLaiDanhSach();
  } catch (e) { 
    alert('Thao tác cập nhật thất bại! Vui lòng kiểm tra lại kết nối.'); 
  }
}
</script>

<style scoped>
/* Đồng bộ giao diện phẳng màu cam Aerion Sports */
.employee-form-container { background-color: #f9fbfd; padding: 20px; }
.back-header { margin-bottom: 20px; text-align: left; }
.btn-back-link { background: none; border: none; color: #909399; cursor: pointer; font-size: 14px; padding: 0; }
.page-form-title { margin: 10px 0 0 0; font-size: 18px; color: #303133; font-weight: 500; }
.form-section-card { background: #ffffff; border: 1px solid #e4e7ed; border-radius: 8px; padding: 24px; margin-bottom: 24px; }
.profile-grid { display: flex; gap: 40px; }
.avatar-upload-block { width: 200px; display: flex; flex-direction: column; align-items: center; border-right: 1px solid #f2f6fc; padding-right: 30px; }
.avatar-preview-circle { width: 110px; height: 110px; border-radius: 50%; background-color: #f5f7fa; display: flex; justify-content: center; align-items: center; overflow: hidden; border: 1px solid #e4e7ed; margin-bottom: 15px; }
.avatar-preview-circle img { width: 100%; height: 100%; object-fit: cover; }
.default-avatar-icon { font-size: 50px; color: #c0c4cc; }
.btn-upload-avatar { background: #ffffff; border: 1px dashed #dcdfe6; padding: 6px 16px; border-radius: 4px; font-size: 13px; cursor: pointer; }
.upload-hint { color: #909399; font-size: 11px; margin-top: 8px; }
.info-fields-block { flex: 1; }
.section-sub-title { font-size: 16px; color: #303133; font-weight: 600; text-align: left; }
.form-row { display: flex; gap: 20px; margin-bottom: 16px; }
.form-cell { flex: 1; display: flex; flex-direction: column; gap: 6px; text-align: left; }
.form-cell label { font-size: 13px; color: #606266; font-weight: 500; }
.select-box, .form-cell input { padding: 9px 12px; border: 1px solid #dcdfe6; border-radius: 4px; font-size: 13px; outline: none; width: 100%; box-sizing: border-box; height: 38px; }
.radio-flex { display: flex; gap: 20px; padding: 8px 12px; background: #ffffff; height: 38px; align-items: center; }
.radio-flex label { display: flex; align-items: center; gap: 6px; cursor: pointer; font-size: 13px; }
.border-box { border: 1px solid #dcdfe6; border-radius: 4px; }
.address-section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.address-item-card { border: 1px solid #ebeef5; border-radius: 6px; padding: 16px; background-color: #fafafa; margin-bottom: 16px; }
.btn-cancel-action { background-color: #ffffff; border: 1px solid #dcdfe6; color: #606266; padding: 10px 24px; border-radius: 4px; cursor: pointer; height: 38px; }
.btn-submit-action { background-color: #1a2942; border: none; color: #ffffff; padding: 10px 24px; border-radius: 4px; cursor: pointer; height: 38px; }
.required { color: #f79b66; }
.mb-3 { margin-bottom: 1rem; }
.mb-2 { margin-bottom: 0.5rem; }
.fw-bold { font-weight: bold; }
</style>