<template>
  <div class="layout">
    <div class="sidebar">
      <div class="logo">
        <img
          src="../assets/logo/Logo_Da.jpg"
          alt="logo"
          class="logo-full"
        />
      </div>

      <div class="menu-list">
        <!-- Tất cả nhân sự nội bộ đều được vào Trang chủ -->
        <router-link to="/trang-chu" class="item">
          <i class="fa-solid fa-house"></i>
          <span>Trang chủ</span>
        </router-link>

        <!-- 🌟 ẨN/HIỆN THÔNG MINH: Chỉ ADMIN và Quản lý (QL) mới nhìn thấy Menu thống kê -->
        <router-link v-if="['ADMIN', 'QL'].includes(currentUser.ma_vai_tro)" to="/thong-ke" class="item">
          <i class="fa-solid fa-chart-line"></i>
          <span>Thống kê</span>
        </router-link>

        <!-- Tất cả nhân sự nội bộ đều được vào Bán hàng, Hóa đơn -->
        <router-link to="/ban-hang" class="item">
          <i class="fa-solid fa-cart-shopping"></i>
          <span>Bán hàng</span>
        </router-link>

        <router-link to="/hoa-don" class="item">
          <i class="fa-solid fa-receipt"></i>
          <span>Hóa đơn</span>
        </router-link>

        <!-- Chỉ ADMIN và Quản lý (QL) mới có quyền quản lý sản phẩm -->
        <div v-if="['ADMIN', 'QL'].includes(currentUser.ma_vai_tro)" class="dropdown-menu-group">
          <div :class="['item-toggle-header', { 'active-parent': isSanPhamOpen }]" @click="isSanPhamOpen = !isSanPhamOpen">
            <i class="fa-solid fa-box-open"></i>
            <span>Sản phẩm</span>
            <i :class="['fa-solid', isSanPhamOpen ? 'fa-chevron-down' : 'fa-chevron-right', 'arrow-nested']"></i>
          </div>
          <div class="submenu-container" v-if="isSanPhamOpen">
            <router-link to="/san-pham" class="sub-item">Danh sách sản phẩm</router-link>
            <router-link to="/san-pham/danh-sach-bien-the" class="sub-item">Danh sách biến thể tổng</router-link>
          </div>
        </div>

        <!-- Chỉ ADMIN và Quản lý (QL) mới được cấu hình danh mục thuộc tính -->
        <div v-if="['ADMIN', 'QL'].includes(currentUser.ma_vai_tro)" class="dropdown-menu-group">
          <div :class="['item-toggle-header', { 'active-parent': isThuocTinhOpen }]" @click="isThuocTinhOpen = !isThuocTinhOpen">
            <i class="fa-solid fa-list-check"></i>
            <span>Thuộc tính</span>
            <i :class="['fa-solid', isThuocTinhOpen ? 'fa-chevron-down' : 'fa-chevron-right', 'arrow-nested']"></i>
          </div>
          <div class="submenu-container" v-if="isThuocTinhOpen">
            <router-link to="/thuoc-tinh/thuong-hieu" class="sub-item">Thương hiệu</router-link>
            <router-link to="/thuoc-tinh/xuat-xu" class="sub-item">Xuất xứ</router-link>
            <router-link to="/thuoc-tinh/mau-sac" class="sub-item">Màu sắc</router-link>
            <router-link to="/thuoc-tinh/trong-luong" class="sub-item">Trọng lượng</router-link>
            <router-link to="/thuoc-tinh/do-cung" class="sub-item">Độ cứng thân</router-link>
            <router-link to="/thuoc-tinh/diem-can-bang" class="sub-item">Điểm cân bằng</router-link>
            <router-link to="/thuoc-tinh/chu-vi-can" class="sub-item">Chu vi cán</router-link>
            <router-link to="/thuoc-tinh/chat-lieu-than" class="sub-item">Chất liệu thân</router-link>
            <router-link to="/thuoc-tinh/chat-lieu-khung" class="sub-item">Chất liệu khung</router-link>
          </div>
        </div>

        <router-link to="/phieu-giam-gia" class="item">
          <i class="fa-solid fa-ticket"></i>
          <span>Phiếu giảm giá</span>
        </router-link>

        <!-- Chỉ ADMIN và Quản lý (QL) mới được lên chiến dịch đợt giảm giá -->
        <router-link v-if="['ADMIN', 'QL'].includes(currentUser.ma_vai_tro)" to="/dot-giam-gia" class="item">
          <i class="fa-solid fa-percent"></i>
          <span>Đợt giảm giá</span>
        </router-link>

        <!-- Chỉ duy nhất ADMIN mới nhìn thấy và quản trị danh sách nhân viên -->
        <router-link v-if="currentUser.ma_vai_tro === 'ADMIN'" to="/nhan-vien" class="item">
          <i class="fa-solid fa-user-tie"></i>
          <span>Nhân viên</span>
        </router-link>

        <router-link to="/giao-ca" class="item">
          <i class="fa-solid fa-business-time"></i>
          <span>Giao ca</span>
        </router-link>

        <router-link to="/lich-lam-viec" class="item">
          <i class="fa-solid fa-calendar-days"></i>
          <span>Lịch làm việc</span>
        </router-link>

        <router-link to="/khach-hang" class="item">
          <i class="fa-solid fa-users"></i>
          <span>Khách hàng</span>
        </router-link>
      </div>

      <div class="bottom-menu">
        <div class="bottom-item logout" @click="handleLogout">
          <i class="fa-solid fa-right-from-bracket"></i>
          <span>Đăng xuất</span>
        </div>
      </div>
    </div>

    <div class="main">
      <div class="header">
        <div class="page-title">
          <h2>{{ title }}</h2>
        </div>

        <div class="user-wrapper">
          <div class="user" @click="showMenu = !showMenu">
            <div class="avatar">
              <i class="fa-solid fa-user"></i>
            </div>
            <div class="user-info">
              <!-- 🌟 ĐÃ SỬA: Hiển thị chuẩn xác trường dữ liệu ten_nv nhận từ AuthController -->
              <h4>{{ currentUser.ten_nv || currentUser.tenDangNhap || 'Nhân viên' }}</h4>
              <p>{{ renderRoleName(currentUser.ma_vai_tro) }}</p>
            </div>
            <i class="fa-solid fa-chevron-down arrow"></i>
          </div>

          <div v-if="showMenu" class="dropdown">
            <div class="dropdown-item">
              <i class="fa-solid fa-user"></i>
              <span>Thông tin</span>
            </div>
            <div class="dropdown-item" @click="moModalDoiMatKhau">
              <i class="fa-solid fa-key"></i>
              <span>Đổi mật khẩu</span>
            </div>
            <div class="dropdown-item logout-item" @click="handleLogout">
              <i class="fa-solid fa-right-from-bracket"></i>
              <span>Đăng xuất</span>
            </div>
          </div>
        </div>
      </div>

      <div class="content">
        <slot />
      </div>
    </div>
  </div>

  <!-- Bảng Popup đổi mật khẩu -->
  <div class="custom-modal-overlay" v-if="isDoiMatKhauOpen">
    <div class="custom-modal-box">
      <div class="modal-box-header">
        <h3>Thay đổi mật khẩu tài khoản</h3>
        <button class="btn-close-x" @click="isDoiMatKhauOpen = false">&times;</button>
      </div>
      <div class="modal-box-body">
        <div class="form-group-item">
          <label>Mật khẩu hiện tại <span class="required-star"> *</span></label>
          <input type="password" v-model="formDoiMatKhau.matKhauCu" placeholder="Nhập mật khẩu đang sử dụng..." />
        </div>
        <div class="form-group-item">
          <label>Mật khẩu mới <span class="required-star"> *</span></label>
          <input type="password" v-model="formDoiMatKhau.matKhauMoi" placeholder="Nhập mật khẩu mới..." />
        </div>
        <div class="form-group-item">
          <label>Xác nhận mật khẩu mới <span class="required-star"> *</span></label>
          <input type="password" v-model="formDoiMatKhau.xacNhanMatKhau" placeholder="Nhập lại mật khẩu mới..." @keyup.enter="handleDoiMatKhau" />
        </div>
      </div>
      <div class="modal-box-footer">
        <button class="btn-modal-cancel" @click="isDoiMatKhauOpen = false">Hủy bỏ</button>
        <button class="btn-modal-submit" @click="handleDoiMatKhau">Cập nhật mật khẩu</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps } from 'vue'
import { useRouter } from 'vue-router'
import myAxios from '../api/axios' // 🌟 SỬA: Chuyển sang dùng myAxios để tự đính kèm Bearer Token chống 403

defineProps({
  title: String
})

const router = useRouter()
const showMenu = ref(false)
const isSanPhamOpen = ref(false)
const isThuocTinhOpen = ref(false)
const isDoiMatKhauOpen = ref(false)
const formDoiMatKhau = ref({ matKhauCu: '', matKhauMoi: '', xacNhanMatKhau: '' })
const currentUser = ref({ tenDangNhap: '', ma_vai_tro: '', ten_nv: '' })

onMounted(() => {
  const savedUser = localStorage.getItem('user_info')
  if (savedUser) {
    currentUser.value = JSON.parse(savedUser)
    console.log("Dữ liệu nhân sự đang đăng nhập:", currentUser.value)
  }
})

const renderRoleName = (role) => {
  if (role === 'ADMIN') return 'Quản trị viên tối cao';
  if (role === 'QL') return 'Quản lý cửa hàng';
  if (role === 'NV') return 'Nhân viên hệ thống';
  return 'Nhân sự Aerion Sports';
}

const handleLogout = () => {
  if (confirm('Bạn có chắc chắn muốn đăng xuất khỏi hệ thống Aerion Sports không?')) {
    localStorage.removeItem('token')
    localStorage.removeItem('user_info')
    router.push('/admin/login')
  }
}

const moModalDoiMatKhau = () => {
  formDoiMatKhau.value = { matKhauCu: '', matKhauMoi: '', xacNhanMatKhau: '' }
  showMenu.value = false
  isDoiMatKhauOpen.value = true
}

const handleDoiMatKhau = async () => {
  const { matKhauCu, matKhauMoi, xacNhanMatKhau } = formDoiMatKhau.value
  const token = localStorage.getItem('token')

  if (!token) {
    return alert('Hệ thống không tìm thấy Token xác thực! Vui lòng Đăng nhập lại.')
  }
  if (!matKhauCu?.trim() || !matKhauMoi?.trim() || !xacNhanMatKhau?.trim()) {
    return alert('Vui lòng điền đầy đủ tất cả các trường dữ liệu bắt buộc!')
  }
  if (matKhauMoi.trim() !== xacNhanMatKhau.trim()) {
    return alert('Mật khẩu mới và xác nhận nhập lại không trùng khớp!')
  }

  try {
    const payload = {
      matKhauCu: matKhauCu.trim(),
      matKhauMoi: matKhauMoi.trim(),
      xacNhanMatKhau: xacNhanMatKhau.trim()
    }

    // 🌟 SỬA: Gọi qua myAxios để gửi kèm Header Authorization lên Backend xử lý bảo mật
    await myAxios.put('/api/auth/doi-mat-khau', payload)

    alert('Đổi mật khẩu tài khoản thành công! 🎉 Hệ thống yêu cầu đăng nhập lại.');
    isDoiMatKhauOpen.value = false
    localStorage.removeItem('token')
    localStorage.removeItem('user_info')
    router.push('/admin/login')
  } catch (error) {
    console.error(error)
    alert(error.response?.data?.message || 'Thao tác thất bại! Vui lòng kiểm tra lại mật khẩu hiện tại.');
  }
}
</script>

<style scoped>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

.layout {
  display: flex;
  min-height: 100vh;
  background: #f5f5f5;
  font-family: Arial, Helvetica, sans-serif;
}

.sidebar {
  width: 220px;
  background: white;
  padding: 20px 14px;
  border-right: 1px solid #e5e5e5;
  display: flex;
  flex-direction: column;
}

.logo {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 35px;
  padding: 0 10px;
}

.logo img {
  width: 100%;
  height: auto;
  object-fit: contain;
}

.menu-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.item, .item-toggle-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 14px;
  border-radius: 12px;
  text-decoration: none;
  color: #4b5563;
  transition: 0.2s;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  user-select: none;
}

.item i, .item-toggle-header i {
  width: 20px;
  font-size: 15px;
}

.item:hover, .item-toggle-header:hover {
  background: #fff8f3;
}

.item.router-link-active {
  background: #fff4ed;
  color: #d87d4a;
}

.active-parent {
  color: #d87d4a;
  background: #fffbf7;
}

.arrow-nested {
  margin-left: auto;
  font-size: 11px !important;
  color: #9ca3af;
}

.submenu-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding-left: 20px;
  margin: 4px 0;
  border-left: 1.5px dashed #fed7aa;
  margin-left: 23px;
}

.sub-item {
  display: block;
  padding: 8px 12px;
  font-size: 12.5px;
  color: #6b7280;
  text-decoration: none;
  border-radius: 8px;
  transition: all 0.2s;
  text-align: left;
}

.sub-item:hover {
  background-color: #fff8f3;
  color: #d87d4a;
}

.sub-item.router-link-active {
  background-color: #fff4ed;
  color: #d87d4a;
  font-weight: 600;
}

.bottom-menu {
  margin-top: auto;
  border-top: 1px solid #eee;
  padding-top: 15px;
}

.bottom-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px;
  border-radius: 12px;
  cursor: pointer;
  transition: 0.2s;
  color: #4b5563;
}

.bottom-item:hover {
  background: #f3f4f6;
}

.bottom-item i {
  width: 20px;
}

.logout {
  color: red;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.header {
  height: 75px;
  background: white;
  border-bottom: 1px solid #e5e5e5;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 25px;
}

.page-title h2 {
  font-size: 24px;
  color: #333;
}

.user-wrapper {
  position: relative;
}

.user {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 12px;
  transition: 0.2s;
}

.user:hover {
  background: #f5f5f5;
}

.avatar {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  background: #e6925e;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-info h4 {
  font-size: 14px;
}

.user-info p {
  color: #888;
  font-size: 12px;
}

.arrow {
  font-size: 12px;
  color: #777;
}

.dropdown {
  position: absolute;
  top: 65px;
  right: 0;
  width: 210px;
  background: white;
  border-radius: 14px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  z-index: 999;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 15px 16px;
  cursor: pointer;
  transition: 0.2s;
}

.dropdown-item:hover {
  background: #f5f5f5;
}

.dropdown-item i {
  width: 18px;
}

.logout-item {
  color: red;
}

.content {
  flex: 1;
  padding: 20px;
}

/* --- LAYER CSS MODAL CỦA POPUP ĐỔI MẬT KHẨU --- */
.custom-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.custom-modal-box {
  background: #ffffff;
  width: 100%;
  max-width: 440px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(0,0,0,0.15);
  border-top: 4px solid #d87d4a;
}

.modal-box-header {
  padding: 16px 20px;
  border-bottom: 1px solid #f1f5f9;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-box-header h3 {
  font-size: 15px;
  font-weight: 700;
  color: #1e293b;
}

.btn-close-x {
  background: none;
  border: none;
  font-size: 22px;
  cursor: pointer;
  color: #94a3b8;
}

.modal-box-body {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.form-group-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
  text-align: left;
}

.form-group-item label {
  font-size: 13px;
  font-weight: 600;
  color: #475569;
}

.required-star {
  color: #ef4444;
}

.form-group-item input {
  padding: 9px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 13.5px;
  outline: none;
}

.form-group-item input:focus {
  border-color: #d87d4a;
}

.modal-box-footer {
  padding: 14px 20px;
  border-top: 1px solid #f1f5f9;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  background: #f8fafc;
}

.btn-modal-cancel {
  padding: 7px 16px;
  border: 1px solid #cbd5e1;
  background: #ffffff;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
}

.btn-modal-submit {
  padding: 7px 18px;
  border: none;
  background: #d87d4a;
  color: #ffffff;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
}
</style>