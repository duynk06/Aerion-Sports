import { createRouter, createWebHistory } from 'vue-router'

import BanHang from '../views/BanHangView.vue'
import HoaDon from '../views/HoaDonView.vue'
import SanPham from '../views/SanPhamView.vue'
import PhieuGiamGia from '../views/PhieuGiamGiaView.vue'
import DotGiamGia from '../views/DotGiamGiaView.vue'
import NhanVien from '../views/NhanVienView.vue'
import KhachHang from '../views/KhachHangView.vue'

const routes = [
  { path: '/', component: BanHang },
  { path: '/hoa-don', component: HoaDon },
  { path: '/san-pham', component: SanPham },
  { path: '/phieu-giam-gia', component: PhieuGiamGia },
  { path: '/dot-giam-gia', component: DotGiamGia },
  { path: '/nhan-vien', component: NhanVien },
  { path: '/khach-hang', component: KhachHang },
]

export default createRouter({
  history: createWebHistory(),
  routes
})