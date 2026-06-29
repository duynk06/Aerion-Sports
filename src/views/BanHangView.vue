<template>
  <MainLayout title="Bán hàng (tại cửa hàng)">
  <!-- Toast thông báo -->
<transition name="toast">
    <div
        v-if="thongBao.show"
        :style="{
            position: 'fixed',
            top: '20px',
            right: '20px',
            zIndex: 9999,
            padding: '12px 20px',
            borderRadius: '8px',
            color: '#fff',
            fontWeight: '600',
            fontSize: '14px',
            boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
            background: thongBao.type === 'success' ? '#f79b66' : '#ff4d4f',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            minWidth: '250px'
        }"
    >
        
        {{ thongBao.message }}
    </div>
</transition>
    <div class="pos-container">
      
      <div class="top-outside-header">
        <span class="order-counter">
          {{ hoaDonCho.length }}/5 đơn
        </span>
        <button
          class="btn-create-order"
          @click="taoHoaDon"
        >
          + Tạo đơn hàng
        </button>
      </div>

      <div class="section order-tabs-section">
        <div class="tabs-wrapper">

          <div
            v-for="hd in hoaDonCho"
            :key="hd.id"
            class="tab"
            :class="{active: activeHoaDon===hd.id}"
            @click="activeHoaDon=hd.id"
          >
          <span class="tab-title">
  Hóa Đơn - {{ hd.maHoaDon }} <br> 
  <small style="font-weight: normal;">
    ({{ (hd.khachHang && hd.khachHang.hoTen) ? hd.khachHang.hoTen : 'Khách vãng lai' }})
  </small>
</span>

            <span class="tab-badge">
              {{ hd.soLuong }}
            </span>

            <button
              class="tab-close"
              @click.stop="dongHoaDon(hd.id)"
            >
              ×
            </button>

          </div>

        </div>
      </div>

      <div class="pos-content">
        
        <div class="section product-section">
          <div class="section-header">
            <h3>Sản phẩm</h3>
            <div class="actions">
    <button class="btn-outline" @click="showProductModal = true">+ Thêm sản phẩm</button>
    
    <!-- ✅ Nút quét QR mới -->
    <button class="btn-outline" @click="moModalQuetQR"
        style="display: flex; align-items: center; gap: 6px;">
        <svg viewBox="0 0 24 24" width="16" height="16" fill="none"
             stroke="currentColor" stroke-width="2">
            <rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/>
            <rect x="3" y="14" width="7" height="7"/>
            <path d="M14 14h3v3m0 4h4m-4 0v-4m-3 4h-1m1-4h-1v-1"/>
        </svg>
        Quét QR
    </button>
</div>
          </div>

          <table class="product-table">
            <thead>
              <tr>
                <th width="80" class="text-center">Ảnh</th>
                <th>Sản phẩm</th>
                <th width="150" class="text-right">Đơn giá</th>
                <th width="140" class="text-center">Số lượng</th>
                <th width="150" class="text-right">Thành tiền</th>
                <th width="60" class="text-center">Xóa</th>
              </tr>
            </thead>
            <tbody>
  <tr v-if="danhSachSanPhamHienTai.length === 0">
    <td colspan="6" class="text-center text-muted" style="padding: 20px;">
      Chưa có sản phẩm nào trong đơn hàng
    </td>
  </tr>

  <tr v-else v-for="(sp, index) in danhSachSanPhamHienTai" :key="sp.id">
    <td class="text-center">
        <div class="img-placeholder">
            <img :src="sp.anh" alt="Ảnh SP" style="width: 100%; height: auto;" />
        </div>
    </td>
    <td>
        <div class="prod-info">
            <h4>{{ sp.tenSanPham }}</h4>
            <p class="text-muted">Màu: {{ sp.mauSac }} - TL: {{ sp.trongLuong }}</p>
            <!-- ✅ Bỏ dòng Mã — chỉ hiện nếu có giá thay đổi -->
            <p v-if="sanPhamGiaThayDoi[sp.id]" style="color: #cf1322; font-size: 12px; margin-top: 4px;">
                Giá sản phẩm đã thay đổi
                <span style="text-decoration: line-through; color: #999;">
                    {{ Number(sanPhamGiaThayDoi[sp.id].giaCu).toLocaleString('vi-VN') }} đ
                </span>
                →
                <strong>{{ Number(sanPhamGiaThayDoi[sp.id].giaMoi).toLocaleString('vi-VN') }} đ</strong>
            </p>
        </div>
    </td>
    <td class="text-right font-bold"
        :style="sanPhamGiaThayDoi[sp.id] ? { color: '#cf1322' } : {}">
        {{ Number(sp.donGia).toLocaleString('vi-VN') }} đ
    </td>
    <td class="text-center">
        <div class="qty-control">
            <button @click="giamSoLuong(sp)">-</button>
            <input
                type="number"
                :value="sp.soLuong"
                min="1"
                style="width: 50px; text-align: center;"
                @change="capNhatSoLuongSanPham(sp, $event.target.value)"
                :disabled="!!sanPhamGiaThayDoi[sp.id]"
            />
            <!-- ✅ Khóa nút + nếu giá đã thay đổi -->
            <button
                @click="tangSoLuong(sp)"
                :disabled="!!sanPhamGiaThayDoi[sp.id]"
                :style="sanPhamGiaThayDoi[sp.id]
                    ? { opacity: 0.4, cursor: 'not-allowed' }
                    : {}"
            >+</button>
        </div>
    </td>
    <td class="text-right font-bold">
        {{ Number(sp.donGia * sp.soLuong).toLocaleString('vi-VN') }} đ
    </td>
    <td class="text-center">
        <button class="btn-delete" @click="xoaChiTiet(sp.id)">
            <svg viewBox="0 0 24 24" width="18" height="18" stroke="currentColor"
                 stroke-width="2" fill="none">
                <path d="M3 6h18M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2M10 11v6M14 11v6"/>
            </svg>
        </button>
    </td>
</tr>
</tbody>
          </table>

          <div class="product-summary">
    <span class="text-muted font-bold">Tổng tiền</span>
    <span class="font-bold">
        {{ Number(tongTienHienTai).toLocaleString('vi-VN') }} đ
    </span>
</div>

        </div>

        <div class="bottom-grid">
          <div class="section customer-section">
            <div class="section-header">
              <div class="header-titles">
                <br>
                <h3>Thông tin khách hàng</h3>
                
              </div>
              <div class="actions">
  <button 
    class="btn-outline-modal" 
    style="border-color: #666;" 
    @click="chonKhachVangLai"
  >
    Khách vãng lai
  </button>
  <button class="btn-primary text-sm" @click="showCustomerModal = true">Chọn khách hàng</button>
</div>
            </div>
            
            <div class="customer-card">
  <template v-if="!khachHangDuocChon || khachHangDuocChon.id === 999">
    <div style="margin-bottom: 12px;">
      <span class="text-muted text-sm" style="display: block;">Tên khách hàng</span>
      <h4 class="mt-1">Khách hàng vãng lai</h4>
    </div>
  </template>

  <template v-else>
    <div style="margin-bottom: 12px;">
        <span class="text-muted text-sm" style="display: block;">Tên khách hàng</span>
        <h4 class="mt-1">{{ khachHangDuocChon.hoTen }}</h4>
    </div>
    <div style="margin-bottom: 12px;" v-if="khachHangDuocChon.sdt">
        <span class="text-muted text-sm" style="display: block;">Số điện thoại</span>
        <div class="mt-1 font-bold">{{ khachHangDuocChon.sdt }}</div>
    </div>
    <div style="margin-bottom: 12px;" v-if="khachHangDuocChon.email">
        <span class="text-muted text-sm" style="display: block;">Email</span>
        <div class="mt-1">{{ khachHangDuocChon.email }}</div>
    </div>

    <!-- Địa chỉ luôn hiện -->
    <!-- Thay đoạn hiển thị địa chỉ hiện tại -->
<div v-if="khachHangDuocChon.diaChi" style="margin-bottom: 12px;">
    <span class="text-muted text-sm" style="display: block;">Địa chỉ</span>
    <div class="mt-1" style="display: flex; justify-content: space-between; align-items: flex-start; gap: 8px;">
        <span>{{ khachHangDuocChon.diaChi }}</span>
        <button
            v-if="loaiHoaDonHienTai === 1"
            @click="moModalDiaChi"
            style="background: none; border: 1px solid #1890ff; color: #1890ff;
                   padding: 2px 8px; border-radius: 4px; cursor: pointer;
                   font-size: 12px; white-space: nowrap; flex-shrink: 0;"
        >
            Thay đổi
        </button>
    </div>
</div>

    <!-- ✅ Phí vận chuyển chỉ hiện khi giao hàng -->
    <div v-if="loaiHoaDonHienTai === 1" style="margin-top: 12px; padding: 8px; background: #f6ffed; border: 1px solid #b7eb8f; border-radius: 6px;">
        <span class="text-muted text-sm" style="display: block;">Phí vận chuyển</span>
        <div class="mt-1 font-bold" style="color: #52c41a;">
            {{ Number(phiVanChuyen).toLocaleString('vi-VN') }} đ
            <small class="text-muted" style="font-weight: normal;">
                ({{ (khachHangDuocChon.diaChi || "").toLowerCase().includes("hà nội") ? "Hà Nội" : "Tỉnh khác" }})
            </small>
        </div>
    </div>
</template>
</div>
          </div>

          <div class="section payment-section">
            <div class="section-header">
              <h3>Thanh toán</h3>
              <span class="text-muted text-sm">Chỉ tại cửa hàng</span>
            </div>

            <div class="payment-body">
              <div class="form-group row-flex">
    <label class="font-bold">Hình thức</label>
    <div class="toggle-wrap mt-1">
        <div
            class="toggle-switch"
            :class="{ active: loaiHoaDonHienTai === 1 }"
            @click="toggleLoaiHoaDon"
            style="cursor: pointer;"
        ></div>
        <span>{{ loaiHoaDonHienTai === 1 ? 'Giao hàng' : 'Bán tại quầy' }}</span>
    </div>
</div>

<!-- Thay toàn bộ phần form-group mã giảm giá -->
<div class="form-group">
    <div class="flex-between mb-2">
        <label class="font-bold">Mã giảm giá</label>
        <button
            v-if="tienGiamHienTai > 0"
            class="btn-outline text-sm"
            @click="khongDungMa"
        >
            Không dùng mã
        </button>
    </div>

    <!-- Phiếu đang áp dụng -->
    <div v-if="phieuGiamGiaHienTai && phieuGiamGiaHienTai.coTheApDung"
         class="coupon-card">
        <div class="coupon-header">
            <div class="coupon-tags">
                <span class="tag blue">{{ phieuGiamGiaHienTai.loaiPhieuGiamGia }}</span>
                <span class="tag gray">Mã tốt nhất</span>
                <span class="tag dark">{{ phieuGiamGiaHienTai.maPhieuGiamGia }}</span>
            </div>
            <span class="badge-green">Đang áp dụng</span>
        </div>
        <h4 class="mt-2">{{ phieuGiamGiaHienTai.tenPhieuGiamGia }}</h4>
        <p class="mt-1 text-sm">
            <span v-if="phieuGiamGiaHienTai.loaiPhieuGiamGia === 'PHAN_TRAM'
                       || phieuGiamGiaHienTai.loaiPhieuGiamGia?.includes('%')">
                Giảm {{ phieuGiamGiaHienTai.giaTriGiam }}%
                (tối đa {{ Number(phieuGiamGiaHienTai.giaTriGiamToiDa).toLocaleString('vi-VN') }} đ)
            </span>
            <span v-else-if="phieuGiamGiaHienTai.loaiPhieuGiamGia === 'VAN_CHUYEN'">
                Miễn phí vận chuyển tối đa {{ Number(phieuGiamGiaHienTai.giaTriGiam).toLocaleString('vi-VN') }} đ
            </span>
            <span v-else>
                Giảm {{ Number(phieuGiamGiaHienTai.giaTriGiam).toLocaleString('vi-VN') }} đ
            </span>
            →
            <span class="text-danger font-bold">
                - {{ Number(phieuGiamGiaHienTai.soTienGiamThucTe).toLocaleString('vi-VN') }} đ
            </span>
        </p>
        <p class="text-muted text-sm mt-1"
           v-if="phieuGiamGiaHienTai.giaTriDonToiThieu > 0">
            Đơn tối thiểu: {{ Number(phieuGiamGiaHienTai.giaTriDonToiThieu).toLocaleString('vi-VN') }} đ
        </p>
        <p class="text-muted text-sm mt-2">
            Hết hạn: {{ new Date(phieuGiamGiaHienTai.ngayKetThuc).toLocaleDateString('vi-VN') }}
        </p>
    </div>

    <!-- Không có phiếu nào áp dụng được -->
    <div v-else-if="!phieuGiamGiaHienTai || tongTienHienTai === 0"
         style="color: #888; font-size: 13px; padding: 8px 0;">
        Chưa có mã giảm giá phù hợp
    </div>

    <!-- ✅ Gợi ý phiếu tốt hơn — hiển thị KÈM dù đang áp dụng hay không -->
    <div v-if="phieuGiamGiaHienTai?.phieuGoiY"
         style="background: #fffbe6; border: 1px solid #ffe58f; border-radius: 6px;
                padding: 10px 12px; margin-top: 10px;">
        <div style="font-weight: 600; color: #d48806; font-size: 13px; margin-bottom: 6px;">
            💡 Có mã tốt hơn — mua thêm để được ưu đãi hơn
        </div>
        <div style="font-size: 13px; color: #333; margin-bottom: 4px;">
            <strong>{{ phieuGiamGiaHienTai.phieuGoiY.tenPhieuGiamGia }}</strong>
            ({{ phieuGiamGiaHienTai.phieuGoiY.maPhieuGiamGia }})
        </div>
        <div style="font-size: 13px; color: #555; margin-bottom: 4px;">
            Mua thêm
            <span style="color: #cf1322; font-weight: 600;">
                {{ Number(phieuGiamGiaHienTai.phieuGoiY.soTienCanMuaThem).toLocaleString('vi-VN') }} đ
            </span>
            để được giảm
            <span style="color: #389e0d; font-weight: 600;">
                {{ Number(phieuGiamGiaHienTai.phieuGoiY.soTienGiamNeuDat).toLocaleString('vi-VN') }} đ
            </span>
        </div>
        <div style="font-size: 12px; color: #888;">
            Đơn tối thiểu: {{ Number(phieuGiamGiaHienTai.phieuGoiY.giaTriDonToiThieu).toLocaleString('vi-VN') }} đ
        </div>
    </div>
</div>

<hr class="divider" />

<div class="summary-list">
    <div class="summary-row">
        <span class="text-muted">Tiền hàng</span>
        <span class="font-bold">
            {{ Number(tongTienHienTai).toLocaleString('vi-VN') }} đ
        </span>
    </div>

    <!-- ✅ Thêm dòng giảm giá -->
    <div class="summary-row mt-1" v-if="tienGiamHienTai > 0">
        <span class="text-muted">Giảm giá</span>
        <span class="text-danger font-bold">
            - {{ Number(tienGiamHienTai).toLocaleString('vi-VN') }} đ
        </span>
    </div>

    <!-- Phí vận chuyển -->
    <div class="summary-row mt-1" v-if="loaiHoaDonHienTai === 1">
        <span class="text-muted">Phí vận chuyển</span>
        <span class="font-bold" style="color: #52c41a;">
            + {{ Number(phiVanChuyen).toLocaleString('vi-VN') }} đ
        </span>
    </div>

    <div class="summary-row total-row mt-2">
        <span class="font-bold">Tổng phải trả</span>
        <span class="text-danger font-bold text-lg">
            {{ Number(tongThanhToan).toLocaleString('vi-VN') }} đ
        </span>
    </div>

    <div class="summary-row align-center mt-3">
    <span class="text-muted">Khách thanh toán</span>
    <span class="input-wrapper-pay">
        <input
            type="text"
            class="input-right"
            :value="soTienKhachDuaFormatted"
            @input="soTienKhachDuaFormatted = $event.target.value"
            placeholder="0 đ"
            style="width: 150px; text-align: right;"
        />
    </span>
</div>

    <div class="summary-row mt-3">
        <span class="text-muted">Tiền thừa trả khách</span>
        <span class="font-bold" :style="{ color: tienThua > 0 ? '#16a34a' : '#333' }">
            {{ Number(tienThua).toLocaleString('vi-VN') }} đ
        </span>
    </div>
</div>
<!-- Ghi chú -->
<div class="form-group mt-3">
    <label class="font-bold text-sm">Ghi chú</label>
    <textarea
        v-model="ghiChu"
        class="input-full"
        placeholder="Nhập ghi chú cho đơn hàng..."
        rows="2"
        style="resize: none; margin-top: 4px; font-size: 13px;"
    ></textarea>
</div>
<div class="payment-actions mt-3">
    <button class="btn-pay-cash" @click="xuLyThanhToan(1)">
        Thanh toán (tiền mặt)
    </button>
    <button class="btn-pay-qr" @click="xuLyThanhToan(2)">
        Thanh toán QR
    </button>
</div>

            </div>
          </div>

        </div>
      </div>
    </div>

    <div class="modal-overlay" v-if="showProductModal" @click.self="showProductModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h2>Chọn biến thể để thêm vào đơn</h2>
          <button class="btn-close" @click="showProductModal = false">
            <svg viewBox="0 0 24 24" width="20" height="20" stroke="currentColor" stroke-width="2" fill="none"><path d="M18 6L6 18M6 6l12 12"></path></svg>
          </button>
        </div>

        <div class="modal-body">
          <div class="filter-area">
            <div class="filter-row">
              <div class="filter-group flex-2">
                <label>Tìm kiếm</label>
                <input
v-model="keyword"
type="text"
placeholder="Tìm theo mã / tên sản phẩm..."
class="modal-input"
/>
              </div>
              <div class="filter-group flex-1">
                <label>Màu sắc</label>
                <select
    v-model="idMauSac"
    class="modal-select"
>
    <option :value="null">
        -- Chọn màu sắc --
    </option>

    <option
        v-for="mau in dsMauSac"
        :key="mau.id"
        :value="mau.id"
    >
        {{ mau.tenMauSac }}
    </option>
</select>
              </div>
              <div class="filter-group flex-1">
                <label>Trọng lượng</label>
                <select
    v-model="idTrongLuong"
    class="modal-select"
>
    <option :value="null">
        -- Chọn trọng lượng --
    </option>

    <option
        v-for="tl in dsTrongLuong"
        :key="tl.id"
        :value="tl.id"
    >
        {{ tl.tenTrongLuong }}
    </option>
</select>
              </div>
            </div>

            <div class="filter-row align-end">
              <div class="filter-group flex-2">
    <label>Khoảng giá</label>

    <div class="price-value">
        0 đ -
        {{ Number(giaMax).toLocaleString('vi-VN') }} đ
    </div>

    <div class="single-slider">

        <div
            class="slider-progress"
            :style="{
                width:(giaMax/maxPrice)*100+'%'
            }"
        ></div>

        <input
            type="range"
            v-model="giaMax"
            :min="0"
            :max="maxPrice"
            class="range-input"
        />

    </div>
</div>
              <div class="filter-group flex-1">
                <label>Trạng thái</label>
                <div class="radio-group">
  <label class="radio-label">
    <input type="radio" v-model="trangThai" :value="null" /> Tất cả
  </label>

  <label class="radio-label">
    <input type="radio" v-model="trangThai" :value="1" /> Còn hàng
  </label>

  <label class="radio-label">
    <input type="radio" v-model="trangThai" :value="0" /> Hết hàng
  </label>
</div>
              </div>

              <div class="filter-actions flex-1 text-right">
                <button
class="btn-outline-modal"
@click="resetFilter"
>
Đặt lại
</button>
                
              </div>
            </div>
            
            <div class="modal-subtitle">
              
            </div>
          </div>

          <div class="modal-table-container">
            <table class="modal-table">
              <thead>
                <tr>
                  <th width="40" class="text-center">STT</th>
                  <th width="120">Mã</th>
                  <th width="60">Ảnh</th>
                  <th>Tên sản phẩm</th>
                  <th width="100">Màu</th>
                  <th width="110">Trọng lượng</th>
                  <th width="80" class="text-right">Tồn</th>
                  <th width="120" class="text-right">Giá</th>
                  <th width="80" class="text-center">Chọn</th>
                </tr>
              </thead>
              <tbody>
                <tr
  v-for="(item,index) in danhSach"
  :key="item.id"
>
<td class="text-center">
  {{ index + 1 + (page * size) }}
</td>

<td>{{ item.ma }}</td>

<td>
  <div class="modal-img-wrapper">
    <img
      :src="item.anh"
      alt="product"
    />
  </div>
</td>

<td>{{ item.ten }}</td>

<td>{{ item.mauSac }}</td>

<td>{{ item.trongLuong }}</td>

<td class="text-right">
  {{ item.soLuongTon }}
</td>

<td class="text-right font-bold">
  {{ Number(item.gia).toLocaleString('vi-VN') }} đ
</td>

<td class="text-center">
  <button class="btn-select-product" @click="themSanPhamVaoHoaDon(item)">
  Chọn
</button>
</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="modal-footer">
            
            <div class="pagination">

<button
  class="page-btn"
  @click="prevPage"
  :disabled="page===0"
>
  <
</button>

<span class="page-info">
  Trang {{ page+1 }}/{{ totalPages }}
</span>

<button
  class="page-btn"
  @click="nextPage"
  :disabled="page+1>=totalPages"
>
  >
</button>

</div>
<span class="text-muted text-sm">
  <span class="text-muted text-sm">Hiển thị {{ danhSach.length }}
/ tổng {{ totalElements }}
bản ghi</span>
</span>
          </div>

        </div>
      </div>
    </div>
    <div class="modal-overlay" v-if="showCustomerModal" @click.self="showCustomerModal = false">
  <div class="modal-content" style="max-width: 900px;">
    <div class="modal-header">
      <h2>Chọn khách hàng</h2>
      <button class="btn-close" @click="showCustomerModal = false">
        <svg viewBox="0 0 24 24" width="20" height="20" stroke="currentColor" stroke-width="2" fill="none"><path d="M18 6L6 18M6 6l12 12"></path></svg>
      </button>
    </div>

    <div class="modal-body">
      <div style="margin-bottom: 15px;">
        <input 
          type="text" 
          v-model="searchKhachHang" 
          placeholder="Tìm theo tên/SĐT/email/địa chỉ..." 
          class="modal-input" 
          style="width: 100%; margin-bottom: 10px;"
        />
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <div style="display: flex; gap: 8px;">
            <button 
  class="btn-outline-modal" 
  style="border-color: #666;" 
  @click="chonKhachVangLai"
>
  Khách vãng lai
</button>
          </div>
          <span class="text-muted text-sm">Tổng: {{ totalElementsKh }}</span>
        </div>
      </div>

      <div class="modal-table-container">
        <table class="modal-table">
          <thead>
            <tr>
              <th width="50" class="text-center">STT</th>
              <th width="150">Tên khách</th>
              <th width="120">SĐT</th>
              <th>Địa chỉ</th>
              <th width="80" class="text-center">Chọn</th>
            </tr>
          </thead>
          <tbody>
  <tr v-for="(kh, index) in dsKhachHang" :key="kh.id">
    <td class="text-center">{{ index + 1 + (pageKh * sizeKh) }}</td>
    <td>{{ kh.hoTen }}</td>
    <td>{{ kh.sdt }}</td>
    <td>{{ kh.diaChi }}</td>
    <td class="text-center">
      <button 
        style="background-color: #222; color: #fff; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; font-size: 13px;"
        @click="chonKhachHang(kh)"
      >
        Chọn
      </button>
    </td>
  </tr>
</tbody>
        </table>
      </div>

      <div class="modal-footer" style="display: flex; justify-content: space-between; align-items: center; margin-top: 15px;">
  <span class="text-muted text-sm">Hiển thị {{ dsKhachHang.length }} / tổng {{ totalElementsKh }} bản ghi</span>
  
  <div class="pagination" style="display: flex; align-items: center; gap: 10px;">
    <button class="page-btn" :disabled="pageKh === 0" @click="prevPageKh"><</button>
    <span class="page-info">Trang {{ pageKh + 1 }} / {{ totalPagesKh === 0 ? 1 : totalPagesKh }}</span>
    <button class="page-btn" :disabled="pageKh + 1 >= totalPagesKh" @click="nextPageKh">></button>
  </div>

  
</div>

    </div>
  </div>
</div>
<!-- Modal chọn địa chỉ giao hàng -->
<div class="modal-overlay" v-if="showDiaChiModal" @click.self="showDiaChiModal = false">
    <div class="modal-content" style="max-width: 650px;">
        <div class="modal-header">
            <h2>Chọn địa chỉ giao hàng</h2>
            <button class="btn-close" @click="showDiaChiModal = false">
                <svg viewBox="0 0 24 24" width="20" height="20" stroke="currentColor"
                     stroke-width="2" fill="none">
                    <path d="M18 6L6 18M6 6l12 12"></path>
                </svg>
            </button>
        </div>

        <div class="modal-body">
            <!-- Không có địa chỉ -->
            <div v-if="dsDiaChi.length === 0"
                 class="text-center text-muted"
                 style="padding: 30px;">
                Khách hàng chưa có địa chỉ nào
            </div>

            <!-- Danh sách địa chỉ -->
            <div
                v-for="dc in dsDiaChi"
                :key="dc.id"
                style="border: 1px solid #e8e8e8; border-radius: 8px;
                       padding: 14px 16px; margin-bottom: 12px;"
                :style="dc.macDinh
                    ? { borderColor: '#52c41a', background: '#f6ffed' }
                    : {}"
            >
                <div style="display: flex; justify-content: space-between;
                            align-items: flex-start; gap: 12px;">
                    <div style="flex: 1;">
                        <div style="font-weight: 600; font-size: 14px; margin-bottom: 4px;">
                            {{ dc.nguoiNhan }}
                            <span style="font-weight: normal; color: #888; margin-left: 8px;">
                                {{ dc.sdt }}
                            </span>
                            <span v-if="dc.macDinh"
                                  style="background: #52c41a; color: #fff; font-size: 11px;
                                         padding: 1px 7px; border-radius: 10px;
                                         margin-left: 8px; font-weight: normal;">
                                Mặc định
                            </span>
                        </div>
                        <div style="color: #555; font-size: 13px;">
                            {{ dc.diaChiDayDu }}
                        </div>
                    </div>
                    <button
                        @click="chonDiaChi(dc)"
                        style="background: #222; color: #fff; border: none;
                               padding: 6px 14px; border-radius: 4px;
                               cursor: pointer; font-size: 13px;
                               white-space: nowrap; flex-shrink: 0;"
                    >
                        Chọn
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Modal Thanh toán QR -->
<div class="modal-overlay" v-if="showQRModal" @click.self="showQRModal = false">
    <div class="modal-content" style="max-width: 480px;">
        <div class="modal-header">
            <h2>Thanh toán bằng QR</h2>
            <button class="btn-close" @click="showQRModal = false">
                <svg viewBox="0 0 24 24" width="20" height="20" stroke="currentColor"
                     stroke-width="2" fill="none">
                    <path d="M18 6L6 18M6 6l12 12"></path>
                </svg>
            </button>
        </div>

        <div class="modal-body" style="text-align: center;">

            <!-- Thông tin hóa đơn -->
            <div style="display: flex; justify-content: space-between;
                        margin-bottom: 20px; padding: 12px 16px;
                        background: #f5f5f5; border-radius: 8px;">
                <div style="text-align: left;">
                    <span style="font-size: 12px; color: #888; display: block;">Mã hóa đơn</span>
                    <span style="font-weight: 600; font-size: 14px;">{{ qrThongTin.maHoaDon }}</span>
                </div>
                <div style="text-align: right;">
                    <span style="font-size: 12px; color: #888; display: block;">Số tiền thanh toán</span>
                    <span style="font-weight: 700; font-size: 18px; color: #cf1322;">
                        {{ Number(qrThongTin.soTien).toLocaleString('vi-VN') }} đ
                    </span>
                </div>
            </div>

            <!-- Ảnh QR (placeholder — bạn thay ảnh thật sau) -->
            <div style="margin-bottom: 16px; display: inline-block;">
    <div style="width: 240px; height: 320px; background: transparent;
                display: flex; align-items: center; justify-content: center;
                border-radius: 8px;">
                
        <img src="/src/assets/logo/anhqr.png.jpg" 
             alt="QR Code" 
             style="width: 100%; height: 100%; object-fit: contain; border-radius: 8px;">
             
    </div>
</div>

            <p style="color: #888; font-size: 13px; margin-bottom: 20px;">
                Quét mã hoặc chuyển sang trang thanh toán
            </p>

            <!-- Thông tin thêm -->
            <div style="background: #f5f5f5; border-radius: 8px; padding: 12px 16px;
                        text-align: left; margin-bottom: 20px;">
                <div style="display: flex; justify-content: space-between; margin-bottom: 6px;">
                    <span style="font-size: 13px; color: #666;">Mã đơn</span>
                    <span style="font-size: 13px; font-weight: 600;">{{ qrThongTin.maHoaDon }}</span>
                </div>
                <div style="display: flex; justify-content: space-between;">
                    <span style="font-size: 13px; color: #666;">Ghi chú</span>
                    <span style="font-size: 13px; color: #555;">
            <!-- ✅ Dùng ghi chú thực tế, fallback về mặc định nếu trống -->
            {{ ghiChu || ('Thanh toán QR - ' + qrThongTin.maHoaDon) }}
        </span>
                </div>
            </div>

            <!-- Nút hành động -->
            <div style="display: flex; gap: 12px;">
                <button
                    @click="thucHienThanhToan(2)"
                    style="flex: 1; background: #f79b66; color: #fff; border: none;
                           padding: 12px; border-radius: 8px; font-size: 14px;
                           font-weight: 600; cursor: pointer;"
                >
                    Xác nhận thanh toán
                </button>
                <button
                    @click="showQRModal = false"
                    style="flex: 1; background: #fff; color: #555; border: 1px solid #d9d9d9;
                           padding: 12px; border-radius: 8px; font-size: 14px;
                           cursor: pointer;"
                >
                    Đóng
                </button>
            </div>

        </div>
    </div>
</div>
<!-- Modal Quét QR sản phẩm -->
<div class="modal-overlay" v-if="showQrScanModal" @click.self="dongModalQuetQR">
    <div class="modal-content" style="max-width: 420px;">
        <div class="modal-header">
            <h2>Quét mã QR sản phẩm</h2>
            <button class="btn-close" @click="dongModalQuetQR">
                <svg viewBox="0 0 24 24" width="20" height="20" stroke="currentColor"
                     stroke-width="2" fill="none">
                    <path d="M18 6L6 18M6 6l12 12"></path>
                </svg>
            </button>
        </div>
        <div class="modal-body" style="text-align: center; padding: 24px;">
            <!-- Vùng camera quét QR -->
            <div id="qr-reader"
                 style="width: 100%; border-radius: 8px; overflow: hidden; border: 2px solid #f79b66;">
            </div>
            <p style="margin-top: 12px; color: #888; font-size: 13px;">
                Hướng camera vào mã QR trên sản phẩm
            </p>
            <!-- Kết quả quét -->
            <div v-if="qrScanKetQua"
                 style="margin-top: 12px; padding: 10px; background: #f6ffed;
                        border: 1px solid #b7eb8f; border-radius: 6px; font-size: 13px;">
                ✅ Đã quét: <strong>{{ qrScanKetQua }}</strong>
            </div>
            <div v-if="qrScanLoi"
                 style="margin-top: 12px; padding: 10px; background: #fff2f0;
                        border: 1px solid #ffccc7; border-radius: 6px; font-size: 13px; color: #cf1322;">
                ❌ {{ qrScanLoi }}
            </div>
        </div>
    </div>
</div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted,nextTick, onUnmounted, watch, computed } from "vue"
import MainLayout from "../layouts/MainLayout.vue"
import {
taoHoaDonCho,
getSanPham,
getMauSac,
getTrongLuong,
getKhoangGia,
getKhachHangPos,
updateKhachHangHoaDon,
themChiTietHoaDon,
capNhatSoLuong,
getHoaDonCho,
huyHoaDon,
xoaChiTietHoaDon,
thanhToanHoaDon,
capNhatLoaiHoaDon,
capNhatPhiVanChuyen,
getDiaChiKhachHang,
getPhieuGiamGiaTotNhat,
apDungPhieuGiamGia,
boPhieuGiamGia,
kiemTraGiaSanPham,
timSanPhamTheoMa
} from "../service/BanHangService"
import { Html5Qrcode } from "html5-qrcode"

const showQrScanModal = ref(false)
let html5QrCode = null
const KHACH_HANG_VANG_LAI = {
  id: 999, // Đổi sang 999 theo DB mới
  hoTen: "Khách hàng vãng lai",
  sdt: null,
  email: null,
  diaChi: null
};
const hoaDonCho = ref([])
const activeHoaDon = ref(null)
const dsMauSac = ref([])
const dsTrongLuong = ref([])

// --- MODAL STATE ---
const showProductModal = ref(false)
const chiTietHoaDonHienTai = ref([])
const idMauSac = ref(null)
const idTrongLuong = ref(null)
const sanPhamGiaThayDoi = ref({})
const minPrice = ref(0)
const maxPrice = ref(0)
const ghiChu = ref('')
const giaMin = ref(0)
const giaMax = ref(0)
const trangThai = ref(null)
const phieuGiamGiaHienTai = ref(null)
const totalElements = ref(0)
let dangCapNhatSanPham = false
const qrScanKetQua = ref('')
const qrScanLoi = ref('')
const moModalQuetQR = async () => {
    if (!activeHoaDon.value) {
        showThongBao("Vui lòng chọn hoặc tạo hóa đơn trước!", 'error');
        return;
    }
    qrScanKetQua.value = '';
    qrScanLoi.value = '';
    showQrScanModal.value = true;

    // Đợi DOM render xong mới khởi động camera
    await nextTick();

    html5QrCode = new Html5Qrcode("qr-reader");

    try {
        await html5QrCode.start(
            { facingMode: "environment" },  // dùng camera sau
            { fps: 10, qrbox: { width: 250, height: 250 } },
            async (decodedText) => {
                // ✅ Quét thành công — dừng camera ngay
                await html5QrCode.stop();
                qrScanKetQua.value = decodedText;
                await xuLyQrQuetDuoc(decodedText);
            },
            () => {} // bỏ qua lỗi frame không đọc được
        );
    } catch (err) {
        qrScanLoi.value = "Không thể truy cập camera. Vui lòng kiểm tra quyền!";
        console.error(err);
    }
}

const dongModalQuetQR = async () => {
    if (html5QrCode) {
        try {
            const state = html5QrCode.getState();
            // 2 = SCANNING, dừng nếu đang chạy
            if (state === 2) await html5QrCode.stop();
        } catch (e) {
            // ignore
        }
        html5QrCode = null;
    }
    showQrScanModal.value = false;
    qrScanKetQua.value = '';
    qrScanLoi.value = '';
}

const xuLyQrQuetDuoc = async (maCtsp) => {
    try {
        // Tìm sản phẩm theo mã CTSP
        const sanPham = await timSanPhamTheoMa(maCtsp);

        if (!sanPham) {
            qrScanLoi.value = `Không tìm thấy sản phẩm với mã: ${maCtsp}`;
            return;
        }

        if (sanPham.soLuongTon <= 0) {
            qrScanLoi.value = `Sản phẩm ${maCtsp} đã hết hàng!`;
            return;
        }

        // Thêm vào hóa đơn
        const payload = {
            idHoaDon: activeHoaDon.value,
            idSanPhamChiTiet: sanPham.id,
            soLuong: 1,
            donGia: sanPham.gia
        };

        const response = await themChiTietHoaDon(payload);

        dangCapNhatSanPham = true;
        chiTietHoaDonHienTai.value = [...(response.sanPham || [])];

        const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
        if (index !== -1) {
            hoaDonCho.value[index].chiTietHoaDon = [...(response.sanPham || [])];
            hoaDonCho.value[index].tongTienHang = response.tongTienHang;
        }

        await nextTick();
        dangCapNhatSanPham = false;

        await loadData();
        await kiemTraGia();

        // Đóng modal sau khi thêm thành công
        showQrScanModal.value = false;
        qrScanKetQua.value = '';
        qrScanLoi.value = '';
        showThongBao(`Đã thêm ${sanPham.ten || maCtsp} vào hóa đơn!`, 'success');

    } catch (error) {
        dangCapNhatSanPham = false;
        qrScanLoi.value = error.message || `Không tìm thấy sản phẩm: ${maCtsp}`;
        // Khởi động lại camera để quét tiếp nếu có lỗi
        if (html5QrCode) {
            try {
                await html5QrCode.start(
                    { facingMode: "environment" },
                    { fps: 10, qrbox: { width: 250, height: 250 } },
                    async (decodedText) => {
                        await html5QrCode.stop();
                        qrScanKetQua.value = decodedText;
                        qrScanLoi.value = '';
                        await xuLyQrQuetDuoc(decodedText);
                    },
                    () => {}
                );
            } catch (e) {
                console.error(e);
            }
        }
    }
}
const taoHoaDon = async () => {
    if (hoaDonCho.value.length >= 5) {
        alert("Đã đạt tối đa 5 hóa đơn chờ. Vui lòng hoàn tất hoặc hủy bớt hóa đơn!");
        return;
    }

    try {
        const hoaDonMoi = await taoHoaDonCho();

        // ✅ Thêm vào đầu mảng thay vì cuối
        hoaDonCho.value.unshift({
            ...hoaDonMoi,
            chiTietHoaDon: hoaDonMoi.sanPham || [],
            khachHang: KHACH_HANG_VANG_LAI
        });

        activeHoaDon.value = hoaDonMoi.id;

        // ✅ Thông báo nhỏ thay vì alert cứng
        showThongBao(`Đã tạo hóa đơn ${hoaDonMoi.maHoaDon}`, 'success');

    } catch(error) {
        showThongBao(error.message || "Tạo hóa đơn thất bại", 'error');
    }
}

// đóng hóa đơn
const dongHoaDon = async (id) => {
    const hd = hoaDonCho.value.find(h => h.id === id);
    const coSanPham = hd?.chiTietHoaDon?.length > 0;

    const message = coSanPham
        ? "Hóa đơn có sản phẩm. Xác nhận hủy sẽ hoàn lại tồn kho và lưu trạng thái HỦY?"
        : "Xác nhận xóa hóa đơn trống này?";

    const confirmed = confirm(message);
    if (!confirmed) return;

    try {
        await huyHoaDon(id);

        hoaDonCho.value = hoaDonCho.value.filter(hd => hd.id !== id);
        if (activeHoaDon.value === id) {
            activeHoaDon.value = hoaDonCho.value[0]?.id || null;
        }
        loadData();
    } catch (error) {
        alert(error.message || "Lỗi: Không thể hủy hóa đơn!");
    }
}
const danhSach = ref([])

const page = ref(0)
const size = ref(5)

const totalPages = ref(0)

const keyword = ref("")
const resetFilter = () => {
  // 1. Đặt lại các trường nhập liệu và dropdown về mặc định
  keyword.value = ""
  idMauSac.value = null
  idTrongLuong.value = null
  trangThai.value = null

  // 2. Đặt lại khoảng giá về từ 0 đến mức giá cao nhất
  giaMin.value = 0
  giaMax.value = maxPrice.value 

  // 3. Đưa về trang đầu tiên
  page.value = 0

  // 4. Gọi lại API để tải danh sách sản phẩm chưa bị lọc
  loadData()
}
// --- MODAL KHÁCH HÀNG STATE ---
const showCustomerModal = ref(false)
const searchKhachHang = ref('')

// XÓA MẢNG DỮ LIỆU GIẢ VÀ THAY BẰNG MẢNG RỖNG
const dsKhachHang = ref([])

// Thêm các biến quản lý phân trang cho khách hàng
const pageKh = ref(0)
const sizeKh = ref(5) // Số bản ghi trên 1 trang
const totalPagesKh = ref(0)
const totalElementsKh = ref(0)
const showQRModal = ref(false)
const qrThongTin = ref({ maHoaDon: '', soTien: 0 })
// Biến lưu trữ khách hàng đang được chọn
const khachHangDuocChon = computed(() => {
  if (!activeHoaDon.value) return null;
  // Tìm hóa đơn đang được chọn hiện tại
  const hdHienTai = hoaDonCho.value.find(hd => hd.id === activeHoaDon.value);
  // Trả về thông tin khách hàng của hóa đơn đó (nếu có)
  return hdHienTai ? hdHienTai.khachHang : null;
});

// --- HÀM LOAD DỮ LIỆU KHÁCH HÀNG TỪ API ---
const loadKhachHang = async () => {
  try {
    const response = await getKhachHangPos(
      searchKhachHang.value,
      pageKh.value,
      sizeKh.value
    )
    dsKhachHang.value = response.content
    totalPagesKh.value = response.totalPages
    totalElementsKh.value = response.totalElements
  } catch (error) {
    console.log("Lỗi tải khách hàng:", error)
  }
}

// --- HÀM XỬ LÝ CHỌN KHÁCH HÀNG ---
const chonKhachHang = async (kh) => {
    if (!activeHoaDon.value) {
        alert("Vui lòng tạo hoặc chọn một hóa đơn trước!");
        return;
    }

    try {
        await updateKhachHangHoaDon(activeHoaDon.value, kh.id);

        const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
        if (index !== -1) {
            hoaDonCho.value[index].khachHang = {
                id: kh.id,
                hoTen: kh.hoTen,
                sdt: kh.sdt,
                email: kh.email,
                diaChi: kh.diaChi,        // ✅ địa chỉ đầy đủ để hiển thị
                tinhThanh: kh.tinhThanh   // ✅ tỉnh/thành để tính phí ship
            };
        }

        showCustomerModal.value = false;

        // Nếu đang giao hàng → tự cập nhật phí vận chuyển
        if (loaiHoaDonHienTai.value === 1) {
            await capNhatPhiVanChuyenHoaDon();
        }

    } catch (error) {
        console.error(error);
        alert("Lỗi: Không thể gán khách hàng vào hóa đơn này!");
    }
}

const chonKhachVangLai = async () => {
  if (!activeHoaDon.value) return;
  
  try {
    // Gọi backend update với ID = 999
    await updateKhachHangHoaDon(activeHoaDon.value, 999); 

    const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
    if (index !== -1) {
      hoaDonCho.value[index].khachHang = KHACH_HANG_VANG_LAI; // Gán object cố định
    }
    showCustomerModal.value = false;
  } catch (error) {
    alert("Lỗi: Không thể gán khách vãng lai!");
  }
}
// --- HÀM PHÂN TRANG KHÁCH HÀNG ---
const prevPageKh = () => {
  if (pageKh.value > 0) {
    pageKh.value--
    loadKhachHang()
  }
}

const nextPageKh = () => {
  if (pageKh.value + 1 < totalPagesKh.value) {
    pageKh.value++
    loadKhachHang()
  }
}

// --- WATCHERS CHO KHÁCH HÀNG ---
// Tự động load dữ liệu khi mở Modal nếu danh sách còn trống
watch(showCustomerModal, (newVal) => {
  if (newVal && dsKhachHang.value.length === 0) {
    loadKhachHang()
  }
})

// Xử lý tìm kiếm delay 300ms (debounce)
let searchKhTimeout = null
watch(searchKhachHang, () => {
  pageKh.value = 0
  clearTimeout(searchKhTimeout)
  searchKhTimeout = setTimeout(() => {
    loadKhachHang()
  }, 300)
})
const loadData = async () => {

try {

    const response = await getSanPham(
        keyword.value,
        idMauSac.value,
        idTrongLuong.value,
        giaMin.value,
        giaMax.value,
        trangThai.value,
        page.value,
        size.value
    )

    danhSach.value = response.content
    totalPages.value = response.totalPages
    totalElements.value = response.totalElements

} catch(error){
    console.log(error)
}
}

const nextPage=()=>{

    if(page.value+1<totalPages.value){

        page.value++
        loadData()

    }
}

const prevPage=()=>{

    if(page.value>0){

        page.value--
        loadData()

    }
}

const search=()=>{

    page.value=0
    loadData()
}

// Thêm vào onMounted
onMounted(async () => {
    loadData()
    loadFilterData()

    try {
        const dsHoaDon = await getHoaDonCho();
        hoaDonCho.value = dsHoaDon.map(hd => ({
            ...hd,
            chiTietHoaDon: hd.sanPham || [],
            khachHang: hd.idKhachHang ? {
                id: hd.idKhachHang,
                hoTen: hd.tenKhachHang,
                sdt: hd.sdt,
                diaChi: hd.diaChiKhachHang || "",
                tinhThanh: hd.tinhThanhKhachHang || ""
            } : KHACH_HANG_VANG_LAI
        }));
// Trong onMounted, sau khi set activeHoaDon

        if (hoaDonCho.value.length > 0) {
            activeHoaDon.value = hoaDonCho.value[0].id;
        }
        if (hoaDonCho.value.length > 0) {
    activeHoaDon.value = hoaDonCho.value[0].id;
    // ✅ Đồng bộ ngay
    dongBoChiTietHienTai();
}
    } catch (error) {
        console.log("Lỗi load hóa đơn chờ:", error);
    }

    // ✅ Khi user quay lại tab này → tự kiểm tra giá
    document.addEventListener('visibilitychange', handleVisibilityChange);
})

// ✅ Cleanup khi component unmount
onUnmounted(() => {
    document.removeEventListener('visibilitychange', handleVisibilityChange);
})

const handleVisibilityChange = async () => {
    if (document.visibilityState === 'visible' && activeHoaDon.value) {
        await kiemTraGia();
        await lamMoiPhieuGiamGia(); // ✅ Thêm dòng này
    }
}
watch(keyword,()=>{

page.value=0

clearTimeout(window.searchTimeout)

window.searchTimeout=setTimeout(()=>{
    loadData()
},300)

})
watch(
[
idMauSac,
idTrongLuong,
giaMin,
giaMax,
trangThai
],
()=>{

    page.value=0
    loadData()

}
)
watch([giaMin, giaMax], () => {

if(Number(giaMin.value) > Number(giaMax.value)){
    giaMin.value = giaMax.value
}

})

const loadFilterData = async()=>{

try{

const mauSacData = await getMauSac()
dsMauSac.value = mauSacData

const trongLuongData = await getTrongLuong()
dsTrongLuong.value = trongLuongData

const giaData = await getKhoangGia()

minPrice.value = giaData.giaMin
maxPrice.value = giaData.giaMax

giaMin.value = 0
giaMax.value = giaData.giaMax

}
catch(error){

console.log(error)

}

}
// Hàm helper — gọi sau mọi thao tác thêm/xóa/cập nhật SP
const dongBoChiTietHienTai = () => {
    if (!activeHoaDon.value) {
        chiTietHoaDonHienTai.value = [];
        return;
    }
    const hd = hoaDonCho.value.find(h => h.id === activeHoaDon.value);
    chiTietHoaDonHienTai.value = hd ? [...(hd.chiTietHoaDon || [])] : [];
}


// Computed property để lấy danh sách sản phẩm của hóa đơn đang active
const danhSachSanPhamHienTai = computed(() => chiTietHoaDonHienTai.value)

// Hàm xử lý khi bấm nút "Chọn"
const themSanPhamVaoHoaDon = async (sanPham) => {
    if (!activeHoaDon.value) { alert("Vui lòng chọn hoặc tạo hóa đơn trước!"); return; }
    if (sanPham.soLuongTon <= 0) { alert("Sản phẩm này đã hết hàng!"); return; }

    try {
        const payload = {
            idHoaDon: activeHoaDon.value,
            idSanPhamChiTiet: sanPham.id,
            soLuong: 1,
            donGia: sanPham.gia
        };

        const response = await themChiTietHoaDon(payload);

        // ✅ Set flag TRƯỚC khi thay đổi bất kỳ thứ gì
        dangCapNhatSanPham = true;

        // Cập nhật ref hiển thị ngay lập tức
        chiTietHoaDonHienTai.value = [...(response.sanPham || [])];

        // Cập nhật hoaDonCho
        const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
        if (index !== -1) {
            hoaDonCho.value[index].chiTietHoaDon = [...(response.sanPham || [])];
            hoaDonCho.value[index].tongTienHang = response.tongTienHang;
        }

        // Tắt flag sau nextTick để watch đã chạy xong
        await nextTick();
        dangCapNhatSanPham = false;

        await loadData();
        await kiemTraGia();
        showThongBao("Đã thêm vào hóa đơn thành công!", 'success');

    } catch (error) {
        dangCapNhatSanPham = false;
        console.error(error);
        showThongBao("Lỗi: Không thể thêm sản phẩm vào hóa đơn", 'error');
    }
}

const tongTienHienTai = computed(() => {
    return danhSachSanPhamHienTai.value.reduce((sum, sp) => {
        return sum + (sp.donGia * sp.soLuong);
    }, 0);
});


// Hàm xử lý thay đổi số lượng (dùng chung cho cả +/- và nhập tay)
const capNhatSoLuongSanPham = async (sp, soLuongMoi) => {
    const sl = parseInt(soLuongMoi);
    if (isNaN(sl) || sl < 1) return;

    try {
        const response = await capNhatSoLuong(sp.id, sl);

        dangCapNhatSanPham = true;
        chiTietHoaDonHienTai.value = [...(response.sanPham || [])];

        const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
        if (index !== -1) {
            hoaDonCho.value[index].chiTietHoaDon = [...(response.sanPham || [])];
            hoaDonCho.value[index].tongTienHang = response.tongTienHang;
        }

        await nextTick();
        dangCapNhatSanPham = false;

        await loadData();
    } catch (error) {
        dangCapNhatSanPham = false;
        showThongBao(error.response?.data || "Lỗi cập nhật số lượng", 'error');
    }
}

const tangSoLuong = (sp) => capNhatSoLuongSanPham(sp, sp.soLuong + 1)
const giamSoLuong = (sp) => { if (sp.soLuong > 1) capNhatSoLuongSanPham(sp, sp.soLuong - 1) }
const xoaChiTiet = async (idChiTiet) => {
    const confirmed = confirm("Xóa sản phẩm này khỏi hóa đơn?");
    if (!confirmed) return;

    try {
        await xoaChiTietHoaDon(idChiTiet);

        // ✅ Cập nhật trực tiếp TRƯỚC
        chiTietHoaDonHienTai.value = chiTietHoaDonHienTai.value.filter(sp => sp.id !== idChiTiet);

        // ✅ Cập nhật hoaDonCho bằng index
        const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
        if (index !== -1) {
            hoaDonCho.value[index].chiTietHoaDon =
                hoaDonCho.value[index].chiTietHoaDon.filter(sp => sp.id !== idChiTiet);
        }

        // Xóa khỏi map giá thay đổi
        const mapMoi = { ...sanPhamGiaThayDoi.value };
        delete mapMoi[idChiTiet];
        sanPhamGiaThayDoi.value = mapMoi;

        await loadData();
        await kiemTraGia();
    } catch (error) {
        showThongBao(error.message || "Lỗi xóa sản phẩm!", 'error');
    }
}
// Thêm vào script setup
const soTienKhachDua = ref(0)

const tienThua = computed(() => {
    const thua = soTienKhachDua.value - tongThanhToan.value;
    return thua > 0 ? thua : 0;
})
// Thêm vào script
const soTienKhachDuaFormatted = computed({
    get() {
        if (!soTienKhachDua.value) return '';
        return Number(soTienKhachDua.value).toLocaleString('vi-VN');
    },
    set(val) {
        // Xóa dấu chấm/phẩy rồi parse thành số
        const raw = val.replace(/\./g, '').replace(/,/g, '');
        const num = parseInt(raw);
        soTienKhachDua.value = isNaN(num) ? 0 : num;
    }
})
const xuLyThanhToan = async (idHinhThuc) => {
    if (!activeHoaDon.value) { alert("Vui lòng chọn hóa đơn!"); return; }
    if (danhSachSanPhamHienTai.value.length === 0) { alert("Hóa đơn chưa có sản phẩm!"); return; }

    // ✅ Kiểm tra giá thay đổi
    const spGiaThayDoi = Object.values(sanPhamGiaThayDoi.value);
    if (spGiaThayDoi.length > 0) {
        const dsMa = spGiaThayDoi.map(sp => sp.maCtsp).join(", ");
        alert(`Sản phẩm (${dsMa}) đã bị thay đổi giá, vui lòng xóa sản phẩm giá cũ và chọn lại sản phẩm giá mới!`);
        return;
    }

    if (loaiHoaDonHienTai.value === 1 && (!khachHangDuocChon.value || khachHangDuocChon.value.id === 999)) {
        alert("Vui lòng chọn khách hàng để giao hàng!"); return;
    }
    if (idHinhThuc === 1 && soTienKhachDua.value < tongThanhToan.value) {
        alert("Số tiền khách đưa không đủ!"); return;
    }

    if (idHinhThuc === 2) {
        const hd = hoaDonCho.value.find(h => h.id === activeHoaDon.value);
        qrThongTin.value = { maHoaDon: hd?.maHoaDon || '', soTien: tongThanhToan.value };
        showQRModal.value = true;
        return;
    }

    const confirmed = confirm(
        `Xác nhận thanh toán ${Number(tongThanhToan.value).toLocaleString('vi-VN')} đ bằng tiền mặt?`
    );
    if (!confirmed) return;
    await thucHienThanhToan(1);
}

const loaiHoaDonHienTai = computed(() => {
    if (!activeHoaDon.value) return 0;
    const hd = hoaDonCho.value.find(h => h.id === activeHoaDon.value);
    return hd?.loaiHoaDon ?? 0;
})
const thucHienThanhToan = async (idHinhThuc) => {
    try {
        await thanhToanHoaDon({
            idHoaDon: activeHoaDon.value,
            idHinhThucThanhToan: idHinhThuc,
            soTienKhachDua: idHinhThuc === 1 ? soTienKhachDua.value : tongThanhToan.value,
            ghiChu: ghiChu.value
        });

        const thongBaoMsg = loaiHoaDonHienTai.value === 1
            ? "Đã xác nhận đơn hàng giao hàng!"
            : "Thanh toán thành công!";
        showThongBao(thongBaoMsg, 'success');

        const id = activeHoaDon.value;
        hoaDonCho.value = hoaDonCho.value.filter(hd => hd.id !== id);
        activeHoaDon.value = hoaDonCho.value[0]?.id || null;
        soTienKhachDua.value = 0;
        ghiChu.value = '';
        showQRModal.value = false;
        loadData();

    } catch (error) {
        showThongBao(error.message || "Lỗi thanh toán!", 'error');
    }
}
// Computed phí vận chuyển dựa vào địa chỉ khách hàng
const PHI_HN = 20000;
const PHI_TINH_KHAC = 45000;

const phiVanChuyen = computed(() => {
    if (loaiHoaDonHienTai.value !== 1) return 0;
    if (!khachHangDuocChon.value || khachHangDuocChon.value.id === 999) return 0;

    const tinhThanh = (khachHangDuocChon.value.tinhThanh || "").toLowerCase();
    const laHaNoi = tinhThanh.includes("hà nội") || tinhThanh.includes("ha noi");
    return laHaNoi ? 20000 : 45000;
})
// Tổng thanh toán = tiền hàng + phí vận chuyển
const tongThanhToan = computed(() => {
    return tongTienHienTai.value - tienGiamHienTai.value + phiVanChuyen.value;
})

const toggleLoaiHoaDon = async () => {
    if (!activeHoaDon.value) return;
    const loaiMoi = loaiHoaDonHienTai.value === 0 ? 1 : 0;

    // ✅ Nếu chuyển sang giao hàng mà là khách vãng lai → chặn
    if (loaiMoi === 1 && (!khachHangDuocChon.value || khachHangDuocChon.value.id === 999)) {
        showThongBao("Vui lòng chọn khách hàng trước khi chuyển sang giao hàng!", 'error');
        return;
    }

    try {
        const response = await capNhatLoaiHoaDon(activeHoaDon.value, loaiMoi);
        const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
        if (index !== -1) {
            hoaDonCho.value[index] = {
                ...hoaDonCho.value[index],
                loaiHoaDon: loaiMoi,
                tienVanChuyen: response.tienVanChuyen
            };
        }

        if (loaiMoi === 1 && khachHangDuocChon.value?.id !== 999) {
            await capNhatPhiVanChuyenHoaDon();
        }
    } catch (error) {
        showThongBao(error.message || "Lỗi cập nhật hình thức!", 'error');
    }
}

// Khi chọn khách hàng mới và đang ở chế độ giao hàng → tự cập nhật phí
watch(khachHangDuocChon, async (newKh) => {
    if (loaiHoaDonHienTai.value === 1 && newKh && newKh.id !== 999) {
        await capNhatPhiVanChuyenHoaDon();
    }
})

const capNhatPhiVanChuyenHoaDon = async () => {
    if (!activeHoaDon.value) return;
    try {
        const response = await capNhatPhiVanChuyen(
            activeHoaDon.value,
            phiVanChuyen.value
        );
        const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
        if (index !== -1) {
            hoaDonCho.value[index] = {
                ...hoaDonCho.value[index],
                tienVanChuyen: response.tienVanChuyen,
                tongTienThanhToan: response.tongTienThanhToan
            };
        }
    } catch (error) {
        console.log("Lỗi cập nhật phí vận chuyển:", error);
    }
}

const showDiaChiModal = ref(false)
const dsDiaChi = ref([])

const moModalDiaChi = async () => {
    if (!khachHangDuocChon.value || khachHangDuocChon.value.id === 999) return;
    try {
        dsDiaChi.value = await getDiaChiKhachHang(khachHangDuocChon.value.id);
        showDiaChiModal.value = true;
    } catch (error) {
        alert("Lỗi tải danh sách địa chỉ!");
    }
}

const chonDiaChi = async (dc) => {
    const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
    if (index !== -1) {
        hoaDonCho.value[index].khachHang = {
            ...hoaDonCho.value[index].khachHang,
            diaChi: dc.diaChiDayDu || dc.diaChiChiTiet || "",  // ✅ fallback
            tinhThanh: dc.tinhThanh || ""
        };
    }
    showDiaChiModal.value = false;

    if (loaiHoaDonHienTai.value === 1) {
        await capNhatPhiVanChuyenHoaDon();
    }
}
const tienGiamHienTai = computed(() => {
    if (!activeHoaDon.value) return 0;
    const hd = hoaDonCho.value.find(h => h.id === activeHoaDon.value);
    return Number(hd?.tienGiam) || 0;
})
// Tự động tìm phiếu tốt nhất khi tổng tiền thay đổi
watch(tongTienHienTai, async (newVal) => {
    if (!activeHoaDon.value || newVal <= 0) {
        phieuGiamGiaHienTai.value = null;
        return;
    }
    await lamMoiPhieuGiamGia();
})
watch(activeHoaDon, async (newId, oldId) => {
    soTienKhachDua.value = 0;
    ghiChu.value = '';
    sanPhamGiaThayDoi.value = {};

    if (newId) {
        // ✅ Chỉ đồng bộ nếu KHÔNG đang cập nhật sản phẩm
        if (!dangCapNhatSanPham) {
            dongBoChiTietHienTai();
        }
        await lamMoiPhieuGiamGia();
        await kiemTraGia();
    } else {
        phieuGiamGiaHienTai.value = null;
        chiTietHoaDonHienTai.value = [];
    }
})
const danhSachMaCtspGiaThayDoi = computed(() => {
    return Object.values(sanPhamGiaThayDoi.value).map(v => v.maCtsp);
})
const lamMoiPhieuGiamGia = async () => {
    if (!activeHoaDon.value) return;
    try {
        phieuGiamGiaHienTai.value = await getPhieuGiamGiaTotNhat(activeHoaDon.value);

        if (phieuGiamGiaHienTai.value?.coTheApDung) {
            await xuLyApDungPhieu(phieuGiamGiaHienTai.value.id);
        } else {
            await xuLyBoPhieu();
        }
    } catch (e) {
        console.log("Lỗi load phiếu giảm giá:", e);
    }
}

const xuLyApDungPhieu = async (idPhieu) => {
    try {
        const response = await apDungPhieuGiamGia(activeHoaDon.value, idPhieu);
        capNhatHoaDonTuResponse(response);
    } catch (e) {
        console.log("Lỗi áp dụng phiếu:", e);
    }
}

const xuLyBoPhieu = async () => {
    try {
        const hd = hoaDonCho.value.find(h => h.id === activeHoaDon.value);
        if (!hd?.tienGiam || Number(hd.tienGiam) <= 0) return;
        const response = await boPhieuGiamGia(activeHoaDon.value);
        capNhatHoaDonTuResponse(response);
    } catch (e) {
        console.log("Lỗi bỏ phiếu:", e);
    }
}
const capNhatHoaDonTuResponse = (response) => {
    const index = hoaDonCho.value.findIndex(hd => hd.id === activeHoaDon.value);
    if (index !== -1) {
        hoaDonCho.value[index] = {
            ...hoaDonCho.value[index],
            tienGiam: response.tienGiam,
            maPhieuGiamGia: response.maPhieuGiamGia,
            tenPhieuGiamGia: response.tenPhieuGiamGia,
            tongTienThanhToan: response.tongTienThanhToan
        };
    }
}
const khongDungMa = async () => {
    phieuGiamGiaHienTai.value = null;
    await xuLyBoPhieu();
}
// Thêm vào script
const thongBao = ref({ show: false, message: '', type: 'success' })
let thongBaoTimeout = null

const showThongBao = (message, type = 'success') => {
    if (thongBaoTimeout) clearTimeout(thongBaoTimeout);
    thongBao.value = { show: true, message, type };
    thongBaoTimeout = setTimeout(() => {
        thongBao.value.show = false;
    }, 3000);
}
const kiemTraGia = async () => {
    if (!activeHoaDon.value) return;
    try {
        const ketQua = await kiemTraGiaSanPham(activeHoaDon.value);
        const map = {};
        ketQua.forEach(item => {
            if (item.daThayDoi) {
                map[item.idChiTiet] = {
                    giaCu: item.giaCu,
                    giaMoi: item.giaMoi,
                    maCtsp: item.maCtsp
                };
            }
        });
        sanPhamGiaThayDoi.value = map;
    } catch (e) {
        console.log("Lỗi kiểm tra giá:", e);
    }
}

</script>

<style scoped>
* { box-sizing: border-box; }
.text-center { text-align: center; }
.text-right { text-align: right; }
.text-muted { color: #8c8c8c; }
.text-danger { color: #cf1322; }
.font-bold { font-weight: 600; }
.text-sm { font-size: 13px; }
.text-lg { font-size: 18px; }
.mt-1 { margin-top: 4px; }
.mt-2 { margin-top: 8px; }
.mt-3 { margin-top: 12px; }
.mb-2 { margin-bottom: 8px; }
.block { display: block; }

.pos-container {
  background-color: #f5f5f5;
  padding: 16px;
  min-height: calc(100vh - 60px);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Arial, sans-serif;
  color: #333;
}

/* Các khung nền trắng, bo góc (Dùng chung) */
.section {
  background: #fff;
  border-radius: 8px;
  border: 1px solid #e8e8e8;
  padding: 16px;
}

/* --- DÒNG NGOÀI CÙNG BÊN TRÊN --- */
.top-outside-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.order-counter { font-size: 14px; color: #666; font-weight: 500; }

.btn-create-order {
  background: #fff; border: 1px solid #1890ff; padding: 6px 16px;
  border-radius: 6px; cursor: pointer; color: #1890ff;
  font-weight: 500; font-size: 14px; transition: 0.2s;
}
.btn-create-order:hover { background: #e6f7ff; }

/* --- KHUNG HÓA ĐƠN NẰM NGANG --- */
.order-tabs-section {
  padding: 10px 16px; margin-bottom: 16px; display: flex; align-items: center;
}
.tabs-wrapper { display: flex; gap: 8px; }
.tab {
  display:inline-flex; align-items:center; background:#f5f5f5;
  color:#333; padding:8px 16px; border-radius:6px; cursor:pointer;
}
.tab.active { background:#f79b66; color:white; }
.tab-badge {
  background: #cf1322; color: #fff; font-size: 12px;
  padding: 2px 6px; border-radius: 10px; margin: 0 8px;
}
.tab-close {
  background: none; border: none; color: inherit;
  cursor: pointer; font-size: 16px; opacity: 0.7; margin-left: 4px;
}
.tab-close:hover { opacity: 1; }

/* --- CÁC PHẦN CÒN LẠI --- */
.pos-content { display: flex; flex-direction: column; gap: 16px; }

.section-header {
  display: flex; justify-content: space-between; align-items: flex-start;
  margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px solid #f0f0f0;
}
.section-header h3 { margin: 0; font-size: 16px; font-weight: 600; }
.actions { display: flex; gap: 8px; }
.btn-outline {
  background: #fff; border: 1px solid #d9d9d9; padding: 6px 12px;
  border-radius: 6px; cursor: pointer; color: #333; transition: 0.2s;
}
.btn-outline:hover { border-color: #40a9ff; color: #40a9ff; }
.btn-primary {
  background: #73a8ff; border: 1px solid #73a8ff; padding: 6px 12px;
  border-radius: 6px; cursor: pointer; color: #fff; transition: 0.2s;
}

.product-table { width: 100%; border-collapse: collapse; margin-bottom: 16px; }
.product-table th { padding: 12px 8px; background: #f79b66; border-bottom: 1px solid #f0f0f0; font-weight: 600; font-size: 14px; text-align: left; }
.product-table td { padding: 16px 8px; border-bottom: 1px solid #f0f0f0; vertical-align: top; }

.img-placeholder img { width: 50px; height: 50px; object-fit: cover; border-radius: 4px; border: 1px solid #f0f0f0; }
.prod-info h4 { margin: 0 0 4px 0; font-size: 14px; }
.prod-info p { margin: 2px 0; font-size: 13px; }
.price-warning { color: #cf1322; font-size: 12px; margin-top: 6px !important; }

.qty-control { display: inline-flex; border: 1px solid #d9d9d9; border-radius: 4px; overflow: hidden; }
.qty-control button { background: #fafafa; border: none; padding: 4px 10px; cursor: pointer; }
.qty-control input { width: 40px; text-align: center; border: none; border-left: 1px solid #d9d9d9; border-right: 1px solid #d9d9d9; font-size: 14px; }

.btn-delete { background: none; border: 1px solid #d9d9d9; border-radius: 4px; padding: 6px; cursor: pointer; color: #888; }
.btn-delete:hover { color: #cf1322; border-color: #cf1322; }

.product-summary { display: flex; justify-content: space-between; padding-top: 12px; font-size: 16px; }

.bottom-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; align-items: start; }

.customer-card { border: 1px solid #f0f0f0; border-radius: 8px; padding: 16px; background: #fafafa; }
.customer-card h4 { margin: 0; font-size: 16px; }

.payment-body { display: flex; flex-direction: column; gap: 16px; }
.flex-between { display: flex; justify-content: space-between; align-items: center; }
.row-flex { display: flex; flex-direction: column; gap: 4px; }

.toggle-wrap { display: flex; align-items: center; gap: 8px; font-size: 14px; }
.toggle-switch { width: 32px; height: 18px; background: #d9d9d9; border-radius: 10px; position: relative; }
.toggle-switch::after { content: ''; position: absolute; top: 2px; left: 2px; width: 14px; height: 14px; background: #fff; border-radius: 50%; }

.coupon-tabs { display: flex; gap: 16px; border-bottom: 1px solid #f0f0f0; margin-bottom: 12px; }
.coupon-tabs .tab-link { padding: 8px 0; cursor: pointer; font-size: 14px; color: #8c8c8c; background: none; }
.coupon-tabs .tab-link.active { color: #1890ff; border-bottom: 2px solid #1890ff; font-weight: 500; }

.coupon-card { background: #f6ffed; border: 1px solid #b7eb8f; border-radius: 8px; padding: 12px; }
.coupon-header { display: flex; justify-content: space-between; align-items: center; }
.coupon-tags { display: flex; gap: 6px; }
.tag { font-size: 11px; padding: 2px 6px; border-radius: 4px; font-weight: 600; color: #fff; }
.tag.blue { background: #1890ff; } .tag.gray { background: #8c8c8c; } .tag.dark { background: #333; }
.badge-green { background: #52c41a; color: #fff; font-size: 12px; padding: 2px 8px; border-radius: 10px; }
.coupon-card h4 { margin: 0; font-size: 14px; }

.input-full { width: 100%; padding: 8px 12px; border: 1px solid #d9d9d9; border-radius: 6px; background: #fafafa; margin-top: 4px; }
.divider { border: 0; height: 1px; background: #f0f0f0; margin: 0; }

.summary-list { display: flex; flex-direction: column; gap: 8px; }
.summary-row { display: flex; justify-content: space-between; font-size: 14px; }
.summary-row.align-center { align-items: center; }
.input-right { width: 120px; text-align: right; padding: 6px 12px; border: 1px solid #d9d9d9; border-radius: 6px; }

.payment-actions { display: flex; flex-direction: column; gap: 12px; }
.btn-pay-cash { background: #f79b66; color: white; border: none; border-radius: 6px; padding: 12px; font-size: 14px; font-weight: 600; cursor: pointer; transition: opacity 0.2s; }
.btn-pay-cash:hover { opacity: 0.9; }
.btn-pay-qr { background: #fff; color: #555; border: 1px solid #ccc; border-radius: 6px; padding: 12px; font-size: 14px; font-weight: 600; cursor: pointer; transition: all 0.2s; }
.btn-pay-qr:hover { background: #fafafa; border-color: #999; }

/* ========================================================
   MODAL CSS
   ======================================================== */
.modal-overlay {
  position: fixed; top: 0; left: 0; right: 0; bottom: 0;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex; justify-content: center; align-items: center;
  z-index: 1000;
}
.modal-content {
  background: #fff; width: 900px; max-width: 95vw;
  border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  display: flex; flex-direction: column; overflow: hidden;
}
.modal-header {
  padding: 16px 24px; border-bottom: 1px solid #f0f0f0;
  display: flex; justify-content: space-between; align-items: center;
}
.modal-header h2 { margin: 0; font-size: 18px; font-weight: 600; color: #333; }
.btn-close { background: none; border: none; cursor: pointer; color: #888; display: flex; align-items: center; }
.btn-close:hover { color: #333; }

.modal-body { padding: 24px; }
.filter-area { display: flex; flex-direction: column; gap: 16px; margin-bottom: 24px; }
.filter-row { display: flex; gap: 16px; align-items: center; }
.filter-row.align-end { align-items: flex-end; }
.filter-group { display: flex; flex-direction: column; gap: 6px; }
.filter-group label { font-size: 13px; color: #666; font-weight: 500; }
.flex-1 { flex: 1; }
.flex-2 { flex: 2; }
.modal-input, .modal-select {
  width: 100%; padding: 8px 12px; border: 1px solid #d9d9d9;
  border-radius: 6px; font-size: 14px; outline: none;
}
.modal-input:focus, .modal-select:focus { border-color: #40a9ff; }

/* Fake Range Slider */
.price-label span { color: #1890ff; float: right; font-weight: 600; }
.fake-range-slider { position: relative; height: 36px; display: flex; align-items: center; padding: 0 8px;}
.range-track { width: 100%; height: 4px; background: #f79b66; border-radius: 2px; position: relative;}
.range-thumb {
  width: 14px; height: 14px; background: #fff; border: 2px solid #f79b66;
  border-radius: 50%; position: absolute; top: -5px; cursor: pointer;
}
.range-thumb.left { left: 0; }
.range-thumb.right { right: 0; }

.radio-group { display: flex; gap: 16px; height: 36px; align-items: center; }
.radio-label { font-size: 14px; display: flex; align-items: center; gap: 6px; cursor: pointer; color: #333; }
.radio-label input[type="radio"] { margin: 0; cursor: pointer; }

.filter-actions { display: flex; gap: 8px; justify-content: flex-end; }
.btn-outline-modal {
  background: #fff; border: 1px solid #d9d9d9; padding: 8px 16px;
  border-radius: 6px; cursor: pointer; color: #555; font-size: 13px; font-weight: 500;
}
.btn-outline-modal:hover { border-color: #999; }
.modal-subtitle { border-bottom: 1px solid #f0f0f0; padding-bottom: 8px; }

/* Modal Table */
.modal-table-container { border: 1px solid #f0f0f0; border-radius: 8px; overflow: hidden; margin-bottom: 16px;}
.modal-table { width: 100%; border-collapse: collapse; text-align: left; font-size: 14px; }
.modal-table th { background: #f79b66; padding: 12px; font-weight: 600; border-bottom: 1px solid #f0f0f0; }
.modal-table td { padding: 12px; border-bottom: 1px solid #f0f0f0; vertical-align: middle; }
.modal-img-wrapper img { width: 36px; height: 36px; border-radius: 4px; object-fit: cover; display: block; border: 1px solid #eee; }
.btn-select-product {
  background: #2a2a2a; color: #fff; border: none; padding: 6px 12px;
  border-radius: 6px; font-size: 13px; cursor: pointer; transition: 0.2s;
}
.btn-select-product:hover { background: #444; }
.price-slider{
display:flex;
flex-direction:column;
gap:10px;
}

.price-slider input{
width:100%;
}
/* Modal Footer Pagination */
.modal-footer { display: flex; justify-content: space-between; align-items: center; }
.pagination { display: flex; align-items: center; gap: 12px; }
.page-btn { background: #fff; border: 1px solid #d9d9d9; border-radius: 4px; width: 28px; height: 28px; cursor: pointer; color: #666; display: flex; justify-content: center; align-items: center; }
.page-btn:hover { border-color: #40a9ff; color: #40a9ff; }
.page-info { font-size: 14px; color: #333; }
.modal-select-sm { padding: 6px 10px; border: 1px solid #d9d9d9; border-radius: 6px; font-size: 13px; outline: none; }
.single-slider{
    position:relative;
    width:100%;
    height:30px;
    display:flex;
    align-items:center;
}

.range-input{
    position:absolute;
    width:100%;
    appearance:none;
    background:transparent;
    pointer-events:none;
    z-index:2;
}

.range-input::-webkit-slider-thumb{
    appearance:none;
    width:18px;
    height:18px;
    border-radius:50%;
    background:white;
    border:3px solid #f79b66;
    cursor:pointer;
    pointer-events:auto;
    box-shadow:0 2px 8px rgba(0,0,0,.2);
}

.range-input::-moz-range-thumb{
    width:18px;
    height:18px;
    border-radius:50%;
    background:white;
    border:3px solid #f79b66;
    cursor:pointer;
}

.single-slider::before{
    content:"";
    position:absolute;
    width:100%;
    height:5px;
    background:#ddd;
    border-radius:10px;
}

.slider-progress{
    position:absolute;
    height:5px;
    background:#f79b66;
    border-radius:10px;
    z-index:1;
}

.price-value{
    margin-bottom:8px;
    font-size:13px;
    font-weight:600;
    color:#f79b66;
}
.toggle-switch.active {
    background: #f79b66;
}
.toggle-switch.active::after {
    left: 16px;
}
.toggle-switch {
    transition: background 0.2s;
}
.toggle-switch::after {
    transition: left 0.2s;
}
.toast-enter-active, .toast-leave-active {
    transition: all 0.3s ease;
}
.toast-enter-from, .toast-leave-to {
    opacity: 0;
    transform: translateX(100px);
}
</style>