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

const routes = [
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

  { path: '/admin/login', name: 'AdminLogin', component: Login },
  { path: '/admin', redirect: '/trang-chu' },
  { path: '/trang-chu', component: TrangChuView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/thong-ke', component: ThongKeView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },

  { path: '/ban-hang', component: BanHang, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/hoa-don', component: HoaDon, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/hoa-don/:id', component: HoaDonDetail, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/phieu-giam-gia', name: 'PhieuGiamGia', component: PhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/phieu-giam-gia/them', name: 'ThemPhieuGiamGia', component: ThemPhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/phieu-giam-gia/sua/:id', name: 'SuaPhieuGiamGia', component: SuaPhieuGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/phieu-giam-gia/xem/:id', component: () => import('@/views/ViewPhieuGiamGia.vue'), meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },

  { path: '/giao-ca', name: 'GiaoCa', component: GiaoCaView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quản lý giao ca' } },
  { path: '/lich-lam-viec', name: 'LichLamViec', component: LichLamViec, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Quản lý lịch làm việc' } },

  { path: '/san-pham', component: SanPham, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/them-moi', name: 'ThemMoiSanPham', component: ThemSanPhamView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/danh-sach-bien-the', name: 'DanhSachBienThe', component: DanhSachBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/bien-the', component: BienThe, alias: '/bien-the', meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/bien-the/them-moi', name: 'ThemMoiBienThe', component: ThemBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/san-pham/bien-the/sua', name: 'SuaBienThe', component: SuaBienTheView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/dot-giam-gia', component: DotGiamGia, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },

  { path: '/thuoc-tinh/mau-sac', component: MauSacView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/trong-luong', component: TrongLuongView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/do-cung', component: DoCungView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/diem-can-bang', component: DiemCanBangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/chu-vi-can', component: ChuViCanView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/chat-lieu-than', component: ChatLieuThanView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },
  { path: '/thuoc-tinh/chat-lieu-khung', component: ChatLieuKhungView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL'] } },

  { path: '/nhan-vien', name: 'nhan-vien', component: NhanVienView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
  { path: '/nhan-vien/them', name: 'them-nhan-vien', component: ThemNhanVienView, meta: { requiresAuth: true, roles: ['ADMIN'] } },
  { path: '/nhan-vien/sua/:id', name: 'sua-nhan-vien', component: SuaNhanVienView, meta: { requiresAuth: true, roles: ['ADMIN'], title: 'Sửa thông tin nhân viên' } },

  { path: '/khach-hang', name: 'khach-hang-list', component: KhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/khach-hang/them', name: 'them-khach-hang', component: ThemKhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'] } },
  { path: '/khach-hang/sua/:id', name: 'sua-khach-hang', component: SuaKhachHangView, meta: { requiresAuth: true, roles: ['ADMIN', 'QL', 'NV'], title: 'Sửa thông tin khách hàng' } },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userInfo = JSON.parse(localStorage.getItem('user_info') || '{}')

  if (to.meta.title) {
    document.title = to.meta.title
  }

  if (to.meta.requiresAuth && !token) {
    alert('Hệ thống yêu cầu quyền truy cập! Vui lòng đăng nhập.')
    return next({ name: 'AdminLogin' })
  }

  if (to.meta.roles && !to.meta.roles.includes(userInfo.vai_tro)) {
    alert('Bạn không được cấp quyền điều hành chức năng này!')
    return next('/ban-hang')
  }

  next()
})

export default router
