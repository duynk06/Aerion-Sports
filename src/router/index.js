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
      { path: 'login', name: 'Login', component: () => import('../../client/src/pages/Auth/LoginPage.vue') },
      { path: 'register', name: 'Register', component: () => import('../../client/src/pages/Auth/RegisterPage.vue') },
      { path: 'profile', name: 'Profile', component: () => import('../../client/src/pages/Profile/ProfilePage.vue') },
    ],
  },

  { path: '/admin', redirect: '/trang-chu' },
  { path: '/trang-chu', component: TrangChuView },
  { path: '/thong-ke', component: ThongKeView },

  { path: '/ban-hang', component: BanHang },

  { path: '/hoa-don', component: HoaDon },
  { path: '/hoa-don/:id', component: HoaDonDetail },
  { path: '/san-pham', component: SanPham },
  { 
    path: '/san-pham/them-moi', 
    name: 'ThemMoiSanPham', 
    component: ThemSanPhamView 
  },
  { path: '/san-pham/danh-sach-bien-the', name: 'DanhSachBienThe', component: DanhSachBienTheView },
{ 
    path: '/san-pham/bien-the', 
    component: BienThe,
    alias: '/bien-the' 
  },
  {
  path: '/san-pham/bien-the/them-moi',
  name: 'ThemMoiBienThe',
  component: ThemBienTheView
},
{
  path: '/san-pham/bien-the/sua',
  name: 'SuaBienThe',
  component: SuaBienTheView
},

    { path: '/thuoc-tinh/mau-sac', component: MauSacView },
  { path: '/thuoc-tinh/trong-luong', component: TrongLuongView },
  { path: '/thuoc-tinh/do-cung', component: DoCungView },
  { path: '/thuoc-tinh/diem-can-bang', component: DiemCanBangView },
  { path: '/thuoc-tinh/chu-vi-can', component: ChuViCanView },
  { path: '/thuoc-tinh/chat-lieu-than', component: ChatLieuThanView },
  { path: '/thuoc-tinh/chat-lieu-khung', component: ChatLieuKhungView },
 {
    path: '/phieu-giam-gia',
    name: 'PhieuGiamGia',
    component: PhieuGiamGia
  },

  {
    path: '/phieu-giam-gia/them',
    name: 'ThemPhieuGiamGia',
    component: ThemPhieuGiamGia
  },
  {
    path: '/phieu-giam-gia/sua/:id',
    name: 'SuaPhieuGiamGia',
    component: SuaPhieuGiamGia
  },
  {
    path: '/phieu-giam-gia/xem/:id',
    component: () => import('@/views/ViewPhieuGiamGia.vue')
  },
  { path: '/dot-giam-gia', component: DotGiamGia },
  { path: '/nhan-vien', name: 'nhan-vien', component: NhanVienView },
  { path: '/nhan-vien/them', name: 'them-nhan-vien', component: ThemNhanVienView }, 
  {
  path: '/nhan-vien/sua/:id',
  name: 'sua-nhan-vien',
  component: SuaNhanVienView,
  meta: { title: 'Sửa thông tin nhân viên' }
},
  { path: '/khach-hang', name: 'khach-hang-list', component: KhachHangView },
  { path: '/khach-hang/them', name: 'them-khach-hang', component: ThemKhachHangView },
  {
  path: '/khach-hang/sua/:id',
  name: 'sua-khach-hang',
  component: SuaKhachHangView,
  meta: { title: 'Sửa thông tin khách hàng' } // Nếu dự án của bạn có dùng meta title
}
]

export default createRouter({
  history: createWebHistory(),
  routes
})
