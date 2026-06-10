import { createRouter, createWebHistory } from 'vue-router'

import TrangChuView from '../views/TrangChuView.vue'
import ThongKeView from '../views/ThongKeView.vue'
import BanHang from '../views/BanHangView.vue'
import HoaDon from '../views/HoaDonView.vue'
import HoaDonDetail from '../views/ChiTietHoaDon.vue'
import SanPham from '../views/SanPhamView.vue'
import BienThe from '../views/BienTheView.vue' 
import PhieuGiamGia from '../views/PhieuGiamGiaView.vue'
import DotGiamGia from '../views/DotGiamGiaView.vue'
import NhanVien from '../views/NhanVienView.vue'
import KhachHang from '../views/KhachHangView.vue'
import ThuocTinh from '../views/ThuocTinhView.vue'

const routes = [
  { path: '/trang-chu', component: TrangChuView },
  { path: '/thong-ke', component: ThongKeView },
  { path: '/ban-hang', component: BanHang },
  { path: '/hoa-don', component: HoaDon },
  { path: '/hoa-don/:id', component: HoaDonDetail },
  { path: '/', redirect: '/ban-hang' },
  { path: '/san-pham', component: SanPham },
  { 
    path: '/san-pham/bien-the', 
    component: BienThe,
    alias: '/bien-the' // <-- Chấp nhận cả đường dẫn này, Router sẽ tự hiểu là một
  },
  { path: '/thuoc-tinh', component: ThuocTinh },
  { path: '/phieu-giam-gia', component: PhieuGiamGia },
  { path: '/dot-giam-gia', component: DotGiamGia },
  { path: '/nhan-vien', component: NhanVien },
  { path: '/khach-hang', component: KhachHang },
]

export default createRouter({
  history: createWebHistory(),
  routes
})