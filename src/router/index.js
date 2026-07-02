import { createRouter, createWebHistory } from 'vue-router'

import ShopLayout from '../layouts/ShopLayout.vue'

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
import ThuongHieuView from '../views/thuoc-tinh/ThuongHieuView.vue'
import XuatXuView from '../views/thuoc-tinh/XuatXuView.vue'

const routes = [
  // 🔓 PHÂN HỆ KHÁCH HÀNG (CLIENT ONLINE)
  {
    path: '/',
    component: ShopLayout,
    children: [
      { path: '', name: 'ShopHome', component: () => import('../../client/src/pages/Home/HomePage.vue') },
      { path: 'products', name: 'ShopProducts', component: () => import('../../client/src/pages/Products/ProductsPage.vue') },
      { path: 'products/:id', name: 'ShopProductDetail', component: () => import('../../client/src/pages/ProductDetail/ProductDetailPage.vue') },
      { path: 'cart', name: 'ShopCart', component: () => import('../../client/src/pages/Cart/CartPage.vue') },
      { path: 'checkout', name: 'ShopCheckout', component: () => import('../../client/src/pages/Checkout/CheckoutPage.vue') },
      { path: 'login', name: 'ShopLogin', component: () => import('../../client/src/pages/Auth/LoginPage.vue') },
      { path: 'register', name: 'Register', component: () => import('../../client/src/pages/Auth/RegisterPage.vue') },
      { path: 'profile', name: 'Profile', component: () => import('../../client/src/pages/Profile/ProfilePage.vue') },
      { path: 'my-orders', name: 'MyOrders', component: () => import('../../client/src/pages/MyOrders/MyOrdersPage.vue') },
      { path: 'order-tracking', name: 'ShopOrderTracking', component: () => import('../../client/src/pages/OrderTracking/OrderTrackingPage.vue') },
    ],
  },

  // 🔐 PHÂN HỆ QUẢN TRỊ NỘI BỘ (ADMIN / POS QUẦY)
  { path: '/admin/login', name: 'AdminLogin', component: Login, meta: { title: 'Đăng nhập hệ thống quản trị' } },
  { path: '/admin', redirect: '/trang-chu' },
  { path: '/trang-chu', component: TrangChuView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Trang chủ điều hành' } },
  { path: '/thong-ke', component: ThongKeView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Báo cáo Thống kê Doanh nghiệp' } },

  { path: '/ban-hang', component: BanHang, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quầy bán hàng POS' } },
  { path: '/hoa-don', component: HoaDon, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quản lý hóa đơn' } },
  { path: '/hoa-don/:id', component: HoaDonDetail, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Chi tiết hóa đơn giao dịch' } },
  
  { path: '/phieu-giam-gia', name: 'PhieuGiamGia', component: PhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Danh sách phiếu giảm giá' } },
  { path: '/phieu-giam-gia/them', name: 'ThemPhieuGiamGia', component: ThemPhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Thêm phiếu giảm giá mới' } },
  { path: '/phieu-giam-gia/sua/:id', name: 'SuaPhieuGiamGia', component: SuaPhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Cập nhật phiếu giảm giá' } },
  { path: '/phieu-giam-gia/xem/:id', component: () => import('@/views/ViewPhieuGiamGia.vue'), meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Thông tin phiếu giảm giá' } },

  { path: '/giao-ca', name: 'GiaoCa', component: GiaoCaView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quản lý giao ca' } },
  { path: '/lich-lam-viec', name: 'LichLamViec', component: LichLamViec, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quản lý lịch làm việc' } },

  { path: '/san-pham', component: SanPham, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Quản lý sản phẩm' } },
  { path: '/san-pham/them-moi', name: 'ThemMoiSanPham', component: ThemSanPhamView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thêm sản phẩm mới' } },
  { path: '/san-pham/danh-sach-bien-the', name: 'DanhSachBienThe', component: DanhSachBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Quản lý danh sách biến thể' } },
  { path: '/san-pham/bien-the', component: BienThe, alias: '/bien-the', meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Quản lý biến thể' } },
  { path: '/san-pham/bien-the/them-moi', name: 'ThemMoiBienThe', component: ThemBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thêm mới biến thể sản phẩm' } },
  { path: '/san-pham/bien-the/sua', name: 'SuaBienThe', component: SuaBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Sửa biến thể sản phẩm' } },
  { path: '/dot-giam-gia', component: DotGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Quản lý đợt giảm giá' } },

  { path: '/thuoc-tinh/mau-sac', component: MauSacView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Màu sắc' } },
  { path: '/thuoc-tinh/trong-luong', component: TrongLuongView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Trọng lượng' } },
  { path: '/thuoc-tinh/do-cung', component: DoCungView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Độ cứng' } },
  { path: '/thuoc-tinh/diem-can-bang', component: DiemCanBangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Điểm cân bằng' } },
  { path: '/thuoc-tinh/chu-vi-can', component: ChuViCanView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Chu vi cán' } },
  { path: '/thuoc-tinh/chat-lieu-than', component: ChatLieuThanView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Chất liệu thân' } },
  { path: '/thuoc-tinh/chat-lieu-khung', component: ChatLieuKhungView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Chất liệu khung' } },
   { path: '/thuoc-tinh/thuong-hieu', component: ThuongHieuView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Thương hiệu' } },
  { path: '/thuoc-tinh/xuat-xu', component: XuatXuView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thuộc tính: Xuất xứ' } },

  { path: '/nhan-vien', name: 'nhan-vien', component: NhanVienView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Quản lý danh sách nhân viên' } },
  { path: '/nhan-vien/them', name: 'them-nhan-vien', component: ThemNhanVienView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Thêm nhân viên mới' } },
  { path: '/nhan-vien/sua/:id', name: 'sua-nhan-vien', component: SuaNhanVienView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'], title: 'Sửa thông tin nhân viên' } },

  { path: '/khach-hang', name: 'khach-hang-list', component: KhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quản lý khách hàng' } },
  { path: '/khach-hang/them', name: 'them-khach-hang', component: ThemKhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Thêm khách hàng mới' } },
  { path: '/khach-hang/sua/:id', name: 'sua-khach-hang', component: SuaKhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Sửa thông tin khách hàng' } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 🌟 NAVIGATION GUARD TỐI ƯU THEO TIÊU CHUẨN VUE ROUTER 4 (KHÔNG DÙNG CALLBACK NEXT)
router.beforeEach((to, from) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('user_info') || '{}')

  // Cập nhật tiêu đề tab trình duyệt tự động
  if (to.meta.title) {
    document.title = to.meta.title
  }

  // 1. Chặn nếu chưa đăng nhập mà cố tình vào hệ thống quản trị
  if (to.meta.requiresAuth && !token) {
    alert('Hệ thống yêu cầu quyền nội bộ! Vui lòng đăng nhập quyền quản trị.')
    return { name: 'AdminLogin' } 
  }

  // 2. Kiểm tra phân quyền chi tiết dựa trên 'ma_vai_tro' (ADMIN, QL, NV) từ DB cấp phát
  if (to.meta.roles && token) {
    const currentRole = userInfo.ma_vai_tro || '' 

    // Nếu Route hiện tại yêu cầu danh sách quyền cụ thể mà user không đáp ứng
    if (!to.meta.roles.includes(currentRole)) {
      alert(`Tài khoản chức vụ [${currentRole || 'Trống'}] của bạn không có quyền truy cập chức năng này!`)
      
      // Định hướng về trang an toàn dựa theo cấp bậc chức vụ thực tế
      if (['ADMIN', 'QL', 'NV'].includes(currentRole)) {
        return '/ban-hang' // Nếu vẫn là nhân viên nội bộ, trả về quầy bán hàng POS
      }
      return '/' // Khách hàng online đẩy ra ngoài trang chủ client
    }
  }

  return true // Hợp lệ, cho phép điều hướng thông suốt
})

export default router