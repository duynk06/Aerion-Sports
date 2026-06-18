<template>
  <MainLayout title="Cập nhật khách hàng">
    <div class="customer-form-container">
      <div class="back-header">
        <button type="button" class="btn-back-link" @click="quayLaiDanhSach">← Quay lại danh sách</button>
        <h3 class="page-form-title">Cập nhật thông tin khách hàng</h3>
      </div>

      <form @submit.prevent="handleSubmitForm">
        <div class="form-section-card edit-form-card profile-grid">
          <div class="avatar-upload-block">
            <div class="section-sub-title fw-bold mb-3">Ảnh đại diện</div>
            <div class="avatar-preview-circle mb-3">
              <img v-if="customerForm.avatar" :src="customerForm.avatar" alt="Avatar" />
              <span v-else class="default-avatar-icon">👤</span>
            </div>
            <button type="button" class="btn-upload-avatar mb-2" @click="triggerFormFileInput">📁 Chọn ảnh</button>
            <input type="file" ref="formFileInputRef" style="display: none;" accept="image/jpeg, image/png, image/jpg" @change="handleFormAvatarChange" />
            <small class="upload-hint">JPG, PNG (tối đa 2MB)</small>
          </div>

          <div class="info-fields-block">
            <div class="section-sub-title fw-bold mb-3">Thông tin khách hàng</div>
            <div class="form-row">
              <div class="form-cell">
                <label>Họ và tên <span class="required">*</span></label>
                <input type="text" v-model="customerForm.hoTen" placeholder="Nhập họ và tên" required />
              </div>
              <div class="form-cell">
                <label>Số điện thoại <span class="required">*</span></label>
                <input type="text" v-model="customerForm.sdt" placeholder="Nhập số điện thoại" required />
              </div>
            </div>
            <div class="form-row">
              <div class="form-cell">
                <label>Email</label>
                <input type="email" v-model="customerForm.email" placeholder="Nhập email" />
              </div>
              <div class="form-cell">
                <label>Ngày sinh</label>
                <input type="date" v-model="customerForm.ngaySinh" />
              </div>
            </div>
            <div class="form-row">
              <div class="form-cell">
                <label>Giới tính</label>
                <div class="radio-flex border-box">
                  <label><input type="radio" v-model="customerForm.gioiTinh" value="1" /> Nam</label>
                  <label><input type="radio" v-model="customerForm.gioiTinh" value="0" /> Nữ</label>
                </div>
              </div>
              <div class="form-cell">
                <label>Trạng thái</label>
                <select v-model="customerForm.trangThai" class="select-box">
                  <option :value="1">Hoạt động</option>
                  <option :value="0">Ngừng hoạt động</option>
                </select>
              </div>
            </div>
          </div>
        </div>

        <div class="form-section-card address-list-section">
          <div class="address-section-header">
            <div class="section-sub-title fw-bold">Danh sách địa chỉ nhận hàng</div>
            <button type="button" class="btn-add-address-item" @click="addNewAddressRow">+ Thêm địa chỉ mới</button>
          </div>

          <div v-for="(addr, idx) in customerForm.addresses" :key="addr.id" class="address-item-card">
            <div class="address-item-title">
              <span>Khối địa chỉ {{ idx + 1 }}</span>
              <button v-if="customerForm.addresses.length > 1" type="button" class="btn-delete-address" @click="removeAddressRow(idx)">Xóa dòng này</button>
            </div>

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
                <label>Tên người nhận <span class="required">*</span></label>
                <input type="text" v-model="addr.nguoiNhan" placeholder="Nhập tên người nhận" required />
              </div>
            </div>

            <div class="form-row">
              <div class="form-cell">
                <label>SĐT người nhận <span class="required">*</span></label>
                <input type="text" v-model="addr.sdt" placeholder="Nhập số điện thoại nhận" required />
              </div>
              <div class="form-cell">
                <label>Địa chỉ chi tiết <span class="required">*</span></label>
                <input type="text" v-model="addr.diaChiChiTiet" placeholder="Nhập số nhà, tên đường..." required />
              </div>
            </div>

            <div class="checkbox-default-wrapper">
              <label class="toggle-control">
                <input type="checkbox" :checked="addr.macDinh" @change="setFormDefaultAddress(idx)" />
                <span class="control-label">Đặt làm địa chỉ mặc định</span>
              </label>
            </div>
          </div>
        </div>

        <div class="form-submit-bar">
          <button type="button" class="btn-cancel-action" @click="quayLaiDanhSach">Hủy bỏ</button>
          <button type="submit" class="btn-submit-action">Lưu thông tin cập nhật</button>
        </div>
      </form>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import MainLayout from '../layouts/MainLayout.vue'
import { fetchAllKhachHang, updateKhachHang } from '@/service/KhachHangService'

const route = useRoute()
const router = useRouter()
const customerId = route.params.id 

const listTinhThanhAPI = ref([])
const formFileInputRef = ref(null)

const customerForm = ref({
  id: null, hoTen: '', sdt: '', email: '', ngaySinh: '', gioiTinh: '1', trangThai: 1, avatar: '', addresses: []
})

const load63TinhThanh = async () => {
  try {
    const res = await axios.get('https://provinces.open-api.vn/api/p/')
    listTinhThanhAPI.value = res.data || []
  } catch (e) { console.error("Lỗi tải 63 tỉnh thành: ", e) }
}

const loadDuLieuKhachHangCu = async () => {
  try {
    const allData = await fetchAllKhachHang()
    const kh = allData.find(item => String(item.id) === String(customerId))
    
    if (kh) {
      let processedAddresses = []
      if (Array.isArray(kh.addresses) && kh.addresses.length > 0) {
        processedAddresses = await Promise.all(kh.addresses.map(async (addr) => {
          let pXa = addr.phuongXa || ''
          let qHuyen = ''
          
          if (pXa.includes(', ')) {
            const parts = pXa.split(', ')
            pXa = parts[0]?.trim() || ''
            qHuyen = parts[1]?.trim() || ''
          }

          let huyenList = []
          let xaList = []

          // Kiểm tra nghiêm ngặt mảng tỉnh thành gốc phải có dữ liệu mới bóc tách
          if (addr.tinhThanh && listTinhThanhAPI.value.length > 0) {
            const tinhChuan = addr.tinhThanh.trim()
            const tinh = listTinhThanhAPI.value.find(t => t.name.trim() === tinhChuan)
            
            if (tinh) {
              const resHuyen = await axios.get(`https://provinces.open-api.vn/api/p/${tinh.code}?depth=2`)
              huyenList = resHuyen.data.districts || []
              
              if (qHuyen && huyenList.length > 0) {
                const quanChuan = qHuyen.trim()
                const quan = huyenList.find(q => q.name.trim() === quanChuan)
                if (quan) {
                  const resXa = await axios.get(`https://provinces.open-api.vn/api/d/${quan.code}?depth=2`)
                  xaList = resXa.data.wards || []
                }
              }
            }
          }

          return {
            // Ép sinh id động kèm theo Date.now() để bắt buộc tầng DOM của Vue phải re-render fill chữ lên select
            id: addr.id ? `${addr.id}_${Date.now()}` : 'ADDR_' + Date.now() + Math.random(),
            tinhThanh: addr.tinhThanh || '',
            quanHuyen: qHuyen,
            phuongXa: pXa,
            diaChiChiTiet: addr.diaChiChiTiet || addr.chiTietCuThe || '',
            nguoiNhan: addr.nguoiNhan || '',
            sdt: addr.sdt || '',
            macDinh: addr.macDinh === true || addr.macDinh === 1,
            listQuanHuyenTmp: huyenList, 
            listPhuongXaTmp: xaList       
          }
        }))
      } else {
        processedAddresses = [{
          id: 'ADDR_NEW_' + Date.now(), tinhThanh: '', quanHuyen: '', phuongXa: '', diaChiChiTiet: '', nguoiNhan: '', sdt: '', macDinh: true, listQuanHuyenTmp: [], listPhuongXaTmp: []
        }]
      }

      customerForm.value = {
        id: kh.id,
        hoTen: kh.hoTen || '',
        sdt: kh.sdt || '',
        email: kh.email || '',
        ngaySinh: kh.ngaySinh || '',
        gioiTinh: String(kh.gioiTinh),
        trangThai: (kh.trangThai === true || Number(kh.trangThai) === 1) ? 1 : 0,
        avatar: kh.avatar || '',
        addresses: processedAddresses
      }
    }
  } catch (error) { console.error("Lỗi nạp hồ sơ khách hàng cũ: ", error) }
}

onMounted(async () => {
  await load63TinhThanh()        
  await loadDuLieuKhachHangCu()   
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

const addNewAddressRow = () => {
  customerForm.value.addresses.push({ id: 'ADDR_ADD_' + Date.now() + Math.random(), tinhThanh: '', quanHuyen: '', phuongXa: '', diaChiChiTiet: '', nguoiNhan: '', sdt: '', macDinh: customerForm.value.addresses.length === 0, listQuanHuyenTmp: [], listPhuongXaTmp: [] })
}
const setFormDefaultAddress = (index) => { customerForm.value.addresses.forEach((a, idx) => a.macDinh = idx === index) }
const removeAddressRow = (index) => { const wasDef = customerForm.value.addresses[index].macDinh; customerForm.value.addresses.splice(index, 1); if (wasDef && customerForm.value.addresses.length > 0) customerForm.value.addresses[0].macDinh = true }

const triggerFormFileInput = () => { if (formFileInputRef.value) formFileInputRef.value.click() }
const handleFormAvatarChange = (e) => {
  const f = e.target.files[0]; if (!f) return
  const r = new FileReader(); r.onload = (ev) => customerForm.value.avatar = ev.target.result; r.readAsDataURL(f)
}
const quayLaiDanhSach = () => router.push('/khach-hang')

const handleSubmitForm = async () => {
  const processed = customerForm.value.addresses.map(addr => {
    // Tách bỏ phần timestamp để lấy lại ID nguyên bản gửi về DB sạch sẽ
    let cleanId = addr.id;
    if (typeof cleanId === 'string' && cleanId.includes('_')) {
      cleanId = cleanId.split('_')[0];
    }
    return {
      id: cleanId,
      nguoiNhan: addr.nguoiNhan.trim(),
      sdt: addr.sdt.trim(),
      tinhThanh: addr.tinhThanh,
      phuongXa: `${addr.phuongXa}, ${addr.quanHuyen}`,
      diaChiChiTiet: addr.diaChiChiTiet.trim(),
      macDinh: addr.macDinh ? 1 : 0
    }
  })

  const payload = {
    hoTen: customerForm.value.hoTen.trim(), sdt: customerForm.value.sdt.trim(), email: customerForm.value.email || null, ngaySinh: customerForm.value.ngaySinh || null,
    gioiTinh: Number(customerForm.value.gioiTinh), trangThai: Number(customerForm.value.trangThai), avatar: customerForm.value.avatar || null,
    addresses: processed
  }

  try {
    await updateKhachHang(customerForm.value.id, payload)
    alert('🎉 Cập nhật thông tin khách hàng thành công!')
    quayLaiDanhSach()
  } catch (e) { alert('Thao tác cập nhật thất bại!') }
}
</script>

<style scoped>
/* Giữ nguyên 100% CSS Scoped của bạn */
.customer-form-container { background-color: #f9fbfd; padding: 20px; }
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
.form-cell label, .address-item-title label { font-size: 13px; color: #606266; font-weight: 500; }
.select-box, .form-cell input { padding: 9px 12px; border: 1px solid #dcdfe6; border-radius: 4px; font-size: 13px; outline: none; width: 100%; box-sizing: border-box; height: 38px; }
.radio-flex { display: flex; gap: 20px; padding: 8px 12px; background: #ffffff; height: 38px; align-items: center; }
.radio-flex label { display: flex; align-items: center; gap: 6px; cursor: pointer; font-size: 13px; }
.border-box { border: 1px solid #dcdfe6; border-radius: 4px; }
.address-section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.btn-add-address-item { background-color: #f79b66; border: none; color: #ffffff; padding: 6px 14px; border-radius: 4px; font-size: 13px; cursor: pointer; }
.address-item-card { border: 1px solid #ebeef5; border-radius: 6px; padding: 16px; background-color: #fafafa; margin-bottom: 16px; }
.address-item-title { display: flex; justify-content: space-between; font-weight: bold; font-size: 13px; color: #606266; margin-bottom: 12px; }
.btn-delete-address { background: none; border: none; color: #f79b66; cursor: pointer; font-size: 13px; }
.toggle-control { display: inline-flex; align-items: center; gap: 8px; cursor: pointer; font-size: 13px; color: #606266; }
.form-submit-bar { display: flex; justify-content: flex-end; gap: 12px; margin-top: 10px; }
.btn-cancel-action { background-color: #ffffff; border: 1px solid #dcdfe6; color: #606266; padding: 10px 24px; border-radius: 4px; cursor: pointer; height: 38px; }
.btn-submit-action { background-color: #1a2942; border: none; color: #ffffff; padding: 10px 24px; border-radius: 4px; cursor: pointer; height: 38px; }
.required { color: #f79b66; }
.mb-3 { margin-bottom: 1rem; }
.mb-2 { margin-bottom: 0.5rem; }
.fw-bold { font-weight: bold; }
</style>