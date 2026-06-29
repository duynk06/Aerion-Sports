import { createRouter, createWebHistory } from 'vue-router'

import TrangChuView from '../views/TrangChuView.vue'
import ThongKeView from '../views/ThongKeView.vue'
import BanHang from '../views/BanHangView.vue'
import HoaDon from '../views/HoaDonView.vue'
import HoaDonDetail from '../views/ChiTietHoaDon.vue'
import SanPham from '../views/sanpham/SanPhamView.vue'
import PhieuGiamGia from '../views/PhieuGiamGiaView.vue'
import DotGiamGia from '../views/DotGiamGiaView.vue'
import NhanVienView from '../views/NhanVienView.vue'
import KhachHangView from '../views/KhachHangView.vue'
import BienThe from '../views/bienthe/BienTheView.vue'
import ThemPhieuGiamGia from '../views/ThemPhieuGiamGia.vue'
import SuaPhieuGiamGia from '../views/SuaPhieuGiamGia.vue'
import ThemNhanVienView from '../views/ThemNhanVienView.vue'
import ThemKhachHangView from '../views/ThemKhachHangView.vue'
import ThemSanPhamView from '../views/sanpham/ThemSanPhamView.vue'
import ThemBienTheView from '../views/bienthe/ThemBienTheView.vue'
import SuaBienTheView from '../views/bienthe/SuaBienTheView.vue'
import MauSacView from '../views/thuoc-tinh/MauSacView.vue'
import TrongLuongView from '../views/thuoc-tinh/TrongLuongView.vue'
import DoCungView from '../views/thuoc-tinh/DoCungView.vue'
import DiemCanBangView from '../views/thuoc-tinh/DiemCanBangView.vue'
import ChuViCanView from '../views/thuoc-tinh/ChuViCanView.vue'
import ChatLieuThanView from '../views/thuoc-tinh/ChatLieuThanView.vue'
import ChatLieuKhungView from '../views/thuoc-tinh/ChatLieuKhungView.vue'
import DanhSachBienTheView from '../views/bienthe/DanhSachBienTheView.vue'
import SuaKhachHangView from '../views/SuaKhachHangView.vue'
import SuaNhanVienView from '../views/SuaNhanVienView.vue'
import GiaoCaView from '../views/GiaoCaView.vue'
import LichLamViec from '../views/LichLamViec.vue'


import Login from '../views/Login.vue'

const routes = [
  // 🔓 Tuyến công khai: Ai cũng vào được
  { path: '/login', name: 'Login', component: Login },
  { path: '/', redirect: '/trang-chu' },
  { path: '/trang-chu', component: TrangChuView },

  // 🛍️ Module Bán Hàng & Hoá Đơn tại quầy (Yêu cầu đăng nhập, ADMIN, QUẢN LÝ hay NHÂN VIÊN đều vào được)
  { path: '/ban-hang', component: BanHang, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/hoa-don', component: HoaDon, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/hoa-don/:id', component: HoaDonDetail, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/phieu-giam-gia', name: 'PhieuGiamGia', component: PhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/phieu-giam-gia/them', name: 'ThemPhieuGiamGia', component: ThemPhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/phieu-giam-gia/sua/:id', name: 'SuaPhieuGiamGia', component: SuaPhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/phieu-giam-gia/xem/:id', component: () => import('@/views/ViewPhieuGiamGia.vue'), meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },

  // ⚙️ Module Nội Bộ Làm Việc (Yêu cầu đăng nhập, ADMIN, QL, NV vào được)
  { path: '/giao-ca', name: 'GiaoCa', component: GiaoCaView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quản lý giao ca' } },
  { path: '/lich-lam-viec', name: 'LichLamViec', component: LichLamViec, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quản lý lịch làm việc' } },

  // 🏸 Module Quản Lý Kho Sản Phẩm & Thuộc Tính (Chỉ cho phép ADMIN hoặc QUẢN LÝ cấu hình)
  { path: '/san-pham', component: SanPham, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/them-moi', name: 'ThemMoiSanPham', component: ThemSanPhamView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/danh-sach-bien-the', name: 'DanhSachBienThe', component: DanhSachBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/bien-the', component: BienThe, alias: '/bien-the', meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/bien-the/them-moi', name: 'ThemMoiBienThe', component: ThemBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/bien-the/sua', name: 'SuaBienThe', component: SuaBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/dot-giam-gia', component: DotGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },

  // Thuộc tính chi tiết vợt
  { path: '/thuoc-tinh/mau-sac', component: MauSacView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/trong-luong', component: TrongLuongView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/do-cung', component: DoCungView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/diem-can-bang', component: DiemCanBangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/chu-vi-can', component: ChuViCanView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/chat-lieu-than', component: ChatLieuThanView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/chat-lieu-khung', component: ChatLieuKhungView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },

  // 👥 Module Quản Lý Đối Tượng Con Người
  { path: '/nhan-vien', name: 'nhan-vien', component: NhanVienView, meta: { requiresAuth: true, roles: ['ADMIN'] } }, // Khóa chặt duy nhất ADMIN
  { path: '/nhan-vien/them', name: 'them-nhan-vien', component: ThemNhanVienView, meta: { requiresAuth: true, roles: ['ADMIN'] } }, 
  { path: '/nhan-vien/sua/:id', name: 'sua-nhan-vien', component: SuaNhanVienView, meta: { requiresAuth: true, roles: ['ADMIN'], title: 'Sửa thông tin nhân viên' } },
  
  { path: '/khach-hang', name: 'khach-hang-list', component: KhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/khach-hang/them', name: 'them-khach-hang', component: ThemKhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/khach-hang/sua/:id', name: 'sua-khach-hang', component: SuaKhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Sửa thông tin khách hàng' } },

  // 📊 Phân tích doanh số tối cao (ADMIN hoặc QL mới được xem)
  { path: '/thong-ke', component: ThongKeView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// =========================================================================
// 🛡️ BỘ LỌC ĐIỀU HƯỚNG BẢO MẬT (Navigation Guard)
// =========================================================================
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  const userInfo = JSON.parse(localStorage.getItem('user_info') || '{}');

  if (to.meta.title) {
    document.title = to.meta.title;
  }

  // 1. Nếu trang bắt buộc đăng nhập mà Client trống rỗng Token
  if (to.meta.requiresAuth && !token) {
    alert('Hệ thống yêu cầu quyền truy cập! Vui lòng đăng nhập.');
    return next({ name: 'Login' });
  }

  // 2. Nếu đã đăng nhập nhưng cố tình truy cập vào phân hệ vượt cấp
  if (to.meta.roles && !to.meta.roles.includes(userInfo.vai_tro)) {
    alert('Bạn không được cấp quyền điều hành chức năng này!');
    return next('/ban-hang'); // Đẩy trả về phân hệ bán hàng cơ bản công khai tại quầy
  }

  next(); // Thỏa mãn điều kiện, cho phép thông quan chuyển trang
});

export default router;