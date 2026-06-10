import { createRouter, createWebHistory } from 'vue-router'

import TrangChuView from '../views/TrangChuView.vue'
import ThongKeView from '../views/ThongKeView.vue'
import BanHang from '../views/BanHangView.vue'
import HoaDon from '../views/HoaDonView.vue'
import SanPham from '../views/SanPhamView.vue'
import PhieuGiamGia from '../views/PhieuGiamGiaView.vue'
import DotGiamGia from '../views/DotGiamGiaView.vue'

// Sửa đồng bộ tên component để tránh lỗi ReferenceError
import NhanVienView from '../views/NhanVienView.vue' 
import ThemNhanVienView from '../views/ThemNhanVienView.vue' 
import KhachHang from '../views/KhachHangView.vue'
import ThemKhachHang from '../views/ThemKhachHangView.vue'

const routes = [
  { path: '/', name: 'ban-hang', component: BanHang },
  { path: '/trang-chu', name: 'trang-chu', component: TrangChuView },
  { path: '/thong-ke', name: 'thong-ke', component: ThongKeView },
  { path: '/hoa-don', name: 'hoa-don', component: HoaDon },
  { path: '/san-pham', name: 'san-pham', component: SanPham },
  { path: '/phieu-giam-gia', name: 'phieu-giam-gia', component: PhieuGiamGia },
  { path: '/dot-giam-gia', name: 'dot-giam-gia', component: DotGiamGia },
  
  // Quản lý Nhân Viên (Đã đồng bộ tên biến component ở đây)
  { path: '/nhan-vien', name: 'nhan-vien', component: NhanVienView },
  { path: '/nhan-vien/them', name: 'them-nhan-vien', component: ThemNhanVienView }, 
  
  // Quản lý Khách Hàng
  { path: '/khach-hang', name: 'khach-hang-list', component: KhachHang },
  { path: '/khach-hang/them', name: 'them-khach-hang', component: ThemKhachHang }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router