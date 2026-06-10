import { createRouter, createWebHistory } from 'vue-router'

import TrangChuView from '../views/TrangChuView.vue'
import ThongKeView from '../views/ThongKeView.vue'
import BanHang from '../views/BanHangView.vue'
import HoaDon from '../views/HoaDonView.vue'
import HoaDonDetail from '../views/ChiTietHoaDon.vue'
import SanPham from '../views/SanPhamView.vue'
import PhieuGiamGia from '../views/PhieuGiamGiaView.vue'
import DotGiamGia from '../views/DotGiamGiaView.vue'
import NhanVienView from '../views/NhanVienView.vue'
import KhachHangView from '../views/KhachHangView.vue'
import BienThe from '../views/BienTheView.vue'
import ThuocTinh from '../views/ThuocTinhView.vue'
import ThemPhieuGiamGia from '../views/ThemPhieuGiamGia.vue'
import SuaPhieuGiamGia from '../views/SuaPhieuGiamGia.vue'
import ThemNhanVienView from '../views/ThemNhanVienView.vue'
import ThemKhachHangView from '../views/ThemKhachHangView.vue'
const routes = [
  { path: '/', redirect: '/trang-chu' },

  { path: '/trang-chu', component: TrangChuView },
  { path: '/thong-ke', component: ThongKeView },

  { path: '/ban-hang', component: BanHang },

  { path: '/hoa-don', component: HoaDon },
  { path: '/hoa-don/:id', component: HoaDonDetail },
  { path: '/san-pham', component: SanPham },
{ 
    path: '/san-pham/bien-the', 
    component: BienThe,
    alias: '/bien-the' // <-- Chấp nhận cả đường dẫn này, Router sẽ tự hiểu là một
  },
    { path: '/thuoc-tinh', component: ThuocTinh },
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
    component: () => SuaPhieuGiamGia
  },
  { path: '/dot-giam-gia', component: DotGiamGia },
  { path: '/nhan-vien', name: 'nhan-vien', component: NhanVienView },
  { path: '/nhan-vien/them', name: 'them-nhan-vien', component: ThemNhanVienView }, 
  { path: '/khach-hang', name: 'khach-hang-list', component: KhachHangView },
  { path: '/khach-hang/them', name: 'them-khach-hang', component: ThemKhachHangView }
]

export default createRouter({
  history: createWebHistory(),
  routes
})