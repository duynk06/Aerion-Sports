<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <span class="breadcrumb-text">Quản lý sản phẩm / Quản lý biến thể / <strong style="color: #f97316;">Chi tiết biến thể</strong></span>
      
      <div style="display: flex; gap: 10px;">
        <button class="btn-toggle-filter" @click="isFilterVisible = !isFilterVisible">
          {{ isFilterVisible ? 'Ẩn bộ lọc' : 'Hiện bộ lọc' }}
        </button>
        <button class="btn-back-to-product" @click="quayLaiDanhSachSanPham">
          Quản lý sản phẩm
        </button>
      </div>
    </div>

    <div class="box-alert-info" v-if="productInfo.ma">
      Đang hiển thị danh sách biến thể của: <strong>[{{ productInfo.ma }}] - {{ productInfo.ten }}</strong>
    </div>

    <div class="filter-container-orange" v-if="isFilterVisible">
      <div class="filter-header-title">
        <i class="fa-solid fa-filter"></i> Bộ lọc tìm kiếm biến thể
      </div>
      <div class="filter-body-content">
        <div class="filter-grid">
          <div class="filter-item">
            <label>Từ khóa tìm kiếm</label>
            <input 
              type="text" 
              v-model="filter.keyword" 
              placeholder="Nhập mã SKU biến thể..." 
              @keyup.enter="phatSinhTimKiem"
            />
          </div>
          <div class="filter-item">
            <label>Trạng thái</label>
            <select v-model="filter.trangThai" @change="phatSinhTimKiem">
              <option value="">Tất cả trạng thái</option>
              <option value="1">Đang hoạt động</option>
              <option value="0">Ngừng hoạt động</option>
            </select>
          </div>
          <div class="filter-buttons-group">
            <button class="btn-trigger-filter" @click="phatSinhTimKiem">Tìm kiếm</button>
            <button class="btn-reset-filter" @click="resetFilter">Đặt lại</button>
          </div>
        </div>
      </div>
    </div>

    <div class="data-table-container">
      <div class="table-header-flex">
        <div class="table-summary-title">
          Danh Sách Biến Thể Chi Tiết (Thông số Vợt, Số lượng, Giá bán)
        </div>
        <div style="display: flex; gap: 10px;">
          <button class="btn-action-excel" @click="exportToExcelConfirm">
             Xuất Excel biến thể
          </button>
          <button class="btn-add-new-orange" @click="chuyenSangTrangThemMoi">
             Thêm mới biến thể
          </button>
        </div>
      </div>

      <div class="table-responsive">
        <table class="custom-data-table">
          <thead>
            <tr>
              <th style="width: 50px; text-align: center;">STT</th>
              <th style="width: 80px; text-align: center;">Hình ảnh</th>
              <th>Mã biến thể (SKU)</th>
              <th>Màu sắc</th>
              <th>Trọng lượng</th>
              <th>Độ cứng</th>
              <th>Điểm cân bằng</th>
              <th>Chu vi cán</th>
              <th>Chất liệu thân</th>
              <th>Chất liệu khung</th>
              <th style="text-align: center; width: 75px;">Tồn kho</th>
              <th style="text-align: center; width: 90px;">% Giảm</th>
              <th style="width: 130px;">Giá bán lẻ</th>
              <th style="width: 120px; text-align: center;">Trạng thái</th>
              <th style="text-align: center; width: 140px;">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(bt, index) in danhSachBienThe" :key="bt.id">
              <td style="text-align: center;">{{ (filter.page * filter.size) + index + 1 }}</td>
              
              <td style="text-align: center; vertical-align: middle;">
                <div class="variant-image-wrapper">
                  <img 
                    :src="bt.hinhAnh ? 'http://localhost:8080/images/' + bt.hinhAnh : 'https://placehold.co/50x50?text=No+Image'" 
                    alt="Ảnh" 
                    class="variant-img-thumb-table"
                  />
                  <span v-if="bt.phanTramGiam && bt.phanTramGiam > 0" class="badge-corner-discount">
                    -{{ bt.phanTramGiam }}%
                  </span>
                </div>
              </td>

              <td class="product-code-text">{{ bt.maCtsp }}</td>
              <td><span class="badge-prop orange">{{ bt.tenMauSac || 'Mặc định' }}</span></td>
              <td><span class="badge-prop cyan">{{ bt.tenTrongLuong || 'Mặc định' }}</span></td>
              <td>{{ bt.tenDoCung || 'Mặc định' }}</td>
              <td>{{ bt.tenDiemCanBang || 'Mặc định' }}</td>
              <td>{{ bt.tenChuViCanVot || 'Mặc định' }}</td>
              <td>{{ bt.tenChatLieuThanVot || 'Mặc định' }}</td>
              <td>{{ bt.tenChatLieuKhungVot || 'Mặc định' }}</td>
              
              <td style="font-weight: bold; text-align: center;">{{ bt.soLuong ?? 0 }}</td>
              
              <td style="text-align: center;">
                <span v-if="bt.phanTramGiam && bt.phanTramGiam > 0" class="badge-discount-percent">
                  -{{ bt.phanTramGiam }}%
                </span>
                <span v-else class="text-muted-dash">-</span>
              </td>

              <td>
                <div v-if="bt.phanTramGiam && bt.phanTramGiam > 0">
                  <del class="original-price-strike">{{ formatPrice(bt.giaBan) }} đ</del>
                  <div class="discounted-price-text">{{ formatPrice(bt.giaDaGiam) }} đ</div>
                </div>
                <div v-else class="normal-price-text">
                  {{ formatPrice(bt.giaBan) }} đ
                </div>
              </td>
              
              <td style="text-align: center;">
                <span :class="['badge-status-text', bt.trangThai === 1 ? 'status-active' : 'status-stopped']">
                  {{ bt.trangThai === 1 ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
                </span>
              </td>

              <td style="text-align: center;">
                <div class="action-buttons-flex-group">
                  <button class="icon-btn-circle view-eye" @click="moModalXemQr(bt)" title="Xem thông tin & Mã QR">
                    <i class="fa-regular fa-eye"></i>
                  </button>

                  <button class="icon-btn-circle edit-pencil-black" @click="chuyenSangTrangChinhSua(bt)" title="Sửa chi tiết biến thể">
                    <i class="fa-regular fa-pen-to-square"></i>
                  </button>
                  
                  <div class="status-toggle-container" @click="toggleTrangThaiNhanhCucBo(bt)" title="Thay đổi trạng thái hoạt động">
                    <span :class="['toggle-track', bt.trangThai === 1 ? 'track-active' : 'track-inactive']">
                      <span class="toggle-handle"></span>
                    </span>
                  </div>
                </div>
              </td>
            </tr>
            <tr v-if="danhSachBienThe.length === 0">
              <td colspan="15" class="empty-table-row">Không tìm thấy chi tiết biến thể nào khớp với bộ lọc!</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="custom-pagination-wrapper">
        <button class="page-arrow-btn" :disabled="filter.page === 0" @click="thayDoiTrang(filter.page - 1)">&lt;</button>
        <span class="page-text-indicator">Trang {{ totalPages > 0 ? filter.page + 1 : 1 }} / {{ totalPages || 1 }}</span>
        <button class="page-arrow-btn" :disabled="filter.page >= totalPages - 1 || totalPages <= 1" @click="thayDoiTrang(filter.page + 1)">&gt;</button>
      </div>
    </div>

    <div class="qr-modal-backdrop" v-if="isQrModalOpen" @click.self="isQrModalOpen = false">
      <div class="qr-modal-content">
        <div class="modal-header-orange">
          <h4><i class="fa-solid fa-qrcode"></i> Thông tin chi tiết & Mã QR Biến thể</h4>
          <button class="btn-close-modal" @click="isQrModalOpen = false">&times;</button>
        </div>
        
        <div class="modal-body-layout">
          <div class="info-specs-column">
            <div class="spec-row-item"><span>Mã CTSP (SKU):</span> <strong>{{ selectedVariant.maCtsp }}</strong></div>
            <div class="spec-row-item"><span>Màu sắc:</span> <strong>{{ selectedVariant.tenMauSac || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Trọng lượng:</span> <strong>{{ selectedVariant.tenTrongLuong || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Độ cứng thân:</span> <strong>{{ selectedVariant.tenDoCung || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Điểm cân bằng:</span> <strong>{{ selectedVariant.tenDiemCanBang || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Chu vi cán:</span> <strong>{{ selectedVariant.tenChuViCanVot || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Chất liệu thân:</span> <strong>{{ selectedVariant.tenChatLieuThanVot || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Giá bán gốc:</span> <strong>{{ formatPrice(selectedVariant.giaBan) }} đ</strong></div>
            <div class="spec-row-item" v-if="selectedVariant.phanTramGiam > 0">
              <span>Giá sau giảm:</span> <strong style="color: #dc2626;">{{ formatPrice(selectedVariant.giaDaGiam) }} đ (-{{ selectedVariant.phanTramGiam }}%)</strong>
            </div>
          </div>
          
          <div class="qr-code-column">
            <div class="qr-canvas-holder">
              <canvas ref="qrCanvasRef"></canvas>
            </div>
            <div class="qr-action-footer">
              <button class="btn-print-qr-code" @click="inMaQRCode">
                <i class="fa-solid fa-print"></i> In mã sản phẩm
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import MainLayout from '@/layouts/MainLayout.vue';
import * as XLSX from 'xlsx';
import QRCode from 'qrcode'; // ⚡ Nhớ chạy lệnh 'npm install qrcode' tại Terminal nhé!

const route = useRoute();
const router = useRouter();

const isFilterVisible = ref(true); 
const danhSachBienThe = ref([]);
const totalElements = ref(0);
const totalPages = ref(1); 

// Các biến phục vụ Modal thông tin QR Code
const isQrModalOpen = ref(false);
const selectedVariant = ref({});
const qrCanvasRef = ref(null);

const filter = ref({ page: 0, size: 5, keyword: '', trangThai: '' });
const productInfo = ref({ id: route.query.idSP || '', ma: route.query.maSP || '', ten: route.query.tenSP || '' });

const formatPrice = (value) => new Intl.NumberFormat('vi-VN').format(value || 0);

const quayLaiDanhSachSanPham = () => {
  router.push('/san-pham');
};

// ⚡ Hàm bốc thông tin mở Modal vẽ QR tự động
const moModalXemQr = async (bt) => {
  selectedVariant.value = bt;
  isQrModalOpen.value = true;
  
  await nextTick();
  if (qrCanvasRef.value) {
    const duLieuMaHoa = bt.maCtsp || 'CTSP'; 
    QRCode.toCanvas(qrCanvasRef.value, duLieuMaHoa, {
      width: 170,
      margin: 1,
      color: {
        dark: '#1e293b',
        white: '#ffffff'
      }
    }, (error) => {
      if (error) console.error("Lỗi tạo mã QR:", error);
    });
  }
};

// Hàm kích hoạt bản in tem dán sản phẩm
const inMaQRCode = () => {
  const canvas = qrCanvasRef.value;
  if (!canvas) return;
  
  const urlAnhQr = canvas.toDataURL("image/png");
  const cuaSoIn = window.open('', '_blank');
  cuaSoIn.document.write(`
    <html>
      <head>
        <title>In mã QR - ${selectedVariant.value.maCtsp}</title>
        <style>
          body { text-align: center; font-family: Arial, sans-serif; padding: 10px; }
          .sku-title { font-size: 14px; font-weight: bold; margin-top: 5px; text-transform: uppercase; }
        </style>
      </head>
      <body onload="window.print(); window.close();">
        <img src="${urlAnhQr}" width="140" height="140"/>
        <div class="sku-title">${selectedVariant.value.maCtsp}</div>
        <div style="font-size: 11px; color: #475569;">Màu: ${selectedVariant.value.tenMauSac || 'Mặc định'} - Lượng: ${selectedVariant.value.tenTrongLuong || 'Mặc định'}</div>
      </body>
    </html>
  `);
  cuaSoIn.document.close();
};

const fetchDataBienThe = async () => {
  try {
    if (!productInfo.value.id) return;
    
    const paramsPayload = {
      idSanPham: parseInt(productInfo.value.id, 10),
      page: filter.value.page,
      size: filter.value.size
    };
    
    if (filter.value.keyword.trim() !== '') paramsPayload.keyword = filter.value.keyword.trim();
    if (filter.value.trangThai !== '') paramsPayload.trangThai = parseInt(filter.value.trangThai, 10);

    const response = await axios.get('http://localhost:8080/api/chi-tiet-san-pham/search', {
      params: paramsPayload
    });
    
    danhSachBienThe.value = response.data.content || response.data || [];
    totalPages.value = response.data.totalPages || 1;
    totalElements.value = response.data.totalElements || 0;
  } catch (error) { 
    console.error("Lỗi lấy danh sách biến thể:", error); 
  }
};

const thayDoiTrang = (pageNo) => { filter.value.page = pageNo; fetchDataBienThe(); };
const phatSinhTimKiem = () => { filter.value.page = 0; fetchDataBienThe(); };
const resetFilter = () => { filter.value.keyword = ''; filter.value.trangThai = ''; filter.value.page = 0; fetchDataBienThe(); };

const toggleTrangThaiNhanhCucBo = async (bt) => {
  const productId = bt.id;
  const indexGoc = danhSachBienThe.value.findIndex(item => item.id === productId);
  if (indexGoc === -1) return;

  const trangThaiCu = danhSachBienThe.value[indexGoc].trangThai;
  const trangThaiMoi = trangThaiCu === 1 ? 0 : 1;
  
  if (!confirm(trangThaiMoi === 1 ? "Bạn có chắc chắn muốn kích hoạt lại biến thể này?" : "Bạn có chắc chắn muốn tạm ngừng hoạt động biến thể này?")) {
    return;
  }

  danhSachBienThe.value[indexGoc].trangThai = trangThaiMoi;

  try {
    await axios.put(`http://localhost:8080/api/chi-tiet-san-pham/${productId}/trang-thai`, null, {
      params: { trangThai: trangThaiMoi }
    });
  } catch (error) {
    danhSachBienThe.value[indexGoc].trangThai = trangThaiCu;
    alert("Hệ thống mất kết nối, không thể cập nhật trạng thái kinh doanh biến thể!");
  }
};

const chuyenSangTrangThemMoi = () => {
  const targetId = productInfo.value.id || route.query.idSP;
  const targetMa = productInfo.value.ma || route.query.maSP;
  const targetTen = productInfo.value.ten || route.query.tenSP;

  if (!targetId) {
    return alert("Lỗi hệ thống: Không tìm thấy ID sản phẩm gốc!");
  }

  let doCungGoc = '', canBangGoc = '', chuViGoc = '', danhMucGoc = '', chatLieuThanGoc = '', chatLieuKhungGoc = '';

  if (danhSachBienThe.value && danhSachBienThe.value.length > 0) {
    const btGoc = danhSachBienThe.value[0];
    doCungGoc = btGoc.idDoCung || '';
    canBangGoc = btGoc.idDiemCanBang || '';
    chuViGoc = btGoc.idChuViCanVot || '';
    danhMucGoc = btGoc.idDanhMuc || '';
    chatLieuThanGoc = btGoc.idChatLieuThanVot || '';
    chatLieuKhungGoc = btGoc.idChatLieuKhungVot || '';
  }

  router.push({
    path: '/san-pham/bien-the/them-moi',
    query: { 
      idSP: targetId, maSP: targetMa, tenSP: targetTen,
      dc: doCungGoc, cb: canBangGoc, cv: chuViGoc, dm: danhMucGoc, clt: chatLieuThanGoc, clk: chatLieuKhungGoc
    }
  });
};

const chuyenSangTrangChinhSua = (bt) => {
  const targetId = productInfo.value.id || route.query.idSP;
  const targetMa = productInfo.value.ma || route.query.maSP;
  const targetTen = productInfo.value.ten || route.query.tenSP;

  router.push({
    path: '/san-pham/bien-the/sua',
    query: { id: bt.id, idSP: targetId, maSP: targetMa, tenSP: targetTen }
  });
};

const exportToExcelConfirm = () => {
  if (danhSachBienThe.value.length === 0) return alert("Không có dữ liệu biến thể để xuất Excel!");
  if (!confirm("Bạn có chắc chắn muốn xuất toàn bộ dữ liệu biến thể của sản phẩm này ra file Excel không?")) return;

  const dataExport = danhSachBienThe.value.map((item, idx) => ({
    "STT": idx + 1,
    "Mã sản phẩm cha": productInfo.value.ma,
    "Tên sản phẩm cha": productInfo.value.ten,
    "Mã SKU biến thể": item.maCtsp,
    "Màu sắc": item.tenMauSec || 'Mặc định',
    "Trọng lượng": item.tenTrongLuong || 'Mặc định',
    "Độ cứng thân": item.tenDoCung || 'Mặc định',
    "Điểm cân bằng": item.tenDiemCanBang || 'Mặc định',
    "Chu vi cán": item.tenChuViCanVot || 'Mặc định',
    "Chất liệu thân": item.tenChatLieuThanVot || 'Mặc định',
    "Chất liệu khung": item.tenChatLieuKhungVot || 'Mặc định',
    "Số lượng tồn": item.soLuong || 0,
    "Giá gốc (VNĐ)": item.giaBan || 0,
    "Phần trăm giảm": item.phanTramGiam ? item.phanTramGiam + '%' : '0%',
    "Giá bán thực tế (VNĐ)": item.giaDaGiam || item.giaBan || 0,
    "Trạng thái": item.trangThai === 1 ? "Đang hoạt động" : "Ngừng hoạt động"
  }));

  const worksheet = XLSX.utils.json_to_sheet(dataExport);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, "Danh sách biến thể");
  XLSX.writeFile(workbook, `BienThe_${productInfo.value.ma || 'SP'}.xlsx`);
};

onMounted(() => {
  fetchDataBienThe();
});
</script>

<style scoped>
/* ==========================================================================
   1. BREADCRUMB & THÔNG BÁO GIAO DIỆN
   ========================================================================== */
.breadcrumb-container { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 15px; 
}

.breadcrumb-text { 
  font-size: 14px; 
  color: #4a5568; 
}

.btn-toggle-filter { 
  background-color: #f8fafc; 
  border: 1px solid #cbd5e1; 
  color: #475569; 
  padding: 6px 14px; 
  border-radius: 8px; 
  cursor: pointer; 
  font-size: 13px; 
  font-weight: 500; 
  transition: all 0.2s; 
}

.btn-toggle-filter:hover { 
  background-color: #e2e8f0; 
}

.btn-back-to-product { 
  background-color: #fff; 
  border: 1px solid #f79b66; 
  color: #f79b66; 
  padding: 6px 14px; 
  border-radius: 8px; 
  font-size: 13px; 
  font-weight: 600; 
  cursor: pointer; 
  transition: all 0.2s ease; 
}

.btn-back-to-product:hover { 
  background-color: #f79b66; 
  color: #fff; 
}

.box-alert-info { 
  background-color: #fff7ed; 
  border-left: 4px solid #f79b66; 
  padding: 12px 15px; 
  font-size: 14px; 
  margin-bottom: 20px; 
  color: #7c2d12; 
  border-radius: 0 8px 8px 0; 
  text-align: left; 
}

/* ==========================================================================
   2. BỘ LỌC TÌM KIẾM CHI TIẾT (FILTER ZONE)
   ========================================================================== */
.filter-container-orange { 
  background-color: #fff; 
  border: 1px solid #f79b66; 
  border-radius: 12px; 
  margin-bottom: 20px; 
  box-shadow: 0 1px 4px rgba(0,0,0,0.05); 
  overflow: hidden; 
}

.filter-header-title { 
  background-color: #f79b66; 
  color: white; 
  padding: 12px 18px; 
  font-size: 14px; 
  font-weight: bold; 
  text-align: left; 
}

.filter-body-content { 
  padding: 20px; 
  background-color: #fff; 
}

.filter-grid { 
  display: flex; 
  gap: 15px; 
  align-items: flex-end; 
  flex-wrap: wrap; 
}

.filter-item { 
  display: flex; 
  flex-direction: column; 
  gap: 8px; 
  text-align: left; 
  flex: 1; 
  min-width: 200px; 
}

.filter-item label { 
  font-size: 13px; 
  font-weight: 600; 
  color: #4a5568; 
}

.filter-item input, 
.filter-item select { 
  padding: 8px 12px; 
  border: 1px solid #cbd5e1; 
  border-radius: 8px; 
  font-size: 13px; 
  outline: none; 
  box-sizing: border-box; 
  width: 100%; 
  height: 38px;
  transition: border-color 0.2s;
}

.filter-item input:focus, 
.filter-item select:focus { 
  border-color: #f79b66; 
}

.filter-buttons-group { 
  display: flex; 
  gap: 10px; 
}

.btn-trigger-filter { 
  background: #e6f7f0; 
  color: #0aa06e; 
  border: none; 
  padding: 10px 20px; 
  border-radius: 8px; 
  font-size: 13px; 
  font-weight: 600; 
  cursor: pointer; 
  height: 38px;
  transition: all 0.2s;
}

.btn-trigger-filter:hover {
  background: #ccefe3;
}

.btn-reset-filter { 
  background: #f3f4f6; 
  color: #4b5563; 
  border: none; 
  padding: 10px 20px; 
  border-radius: 8px; 
  font-size: 13px; 
  font-weight: 600; 
  cursor: pointer; 
  height: 38px;
  transition: all 0.2s;
}

.btn-reset-filter:hover {
  background: #e5e7eb;
}

/* ==========================================================================
   3. KHUNG BẢNG DỮ LIỆU & TIÊU ĐỀ THAO TÁC
   ========================================================================== */
.data-table-container { 
  background-color: #fff; 
  border: 1px solid #edf2f7; 
  border-radius: 12px; 
  padding: 20px; 
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.table-header-flex { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 20px; 
  padding-bottom: 12px; 
  border-bottom: 1px solid #edf2f7; 
}

.table-summary-title { 
  font-size: 16px; 
  font-weight: 600; 
  color: #1a202c; 
  margin: 0; 
}

.btn-add-new-orange { 
  background-color: #f79b66; 
  color: #fff; 
  border: none; 
  padding: 9px 18px; 
  border-radius: 8px; 
  font-size: 13px; 
  font-weight: 600; 
  cursor: pointer; 
  transition: all 0.2s ease; 
}

.btn-add-new-orange:hover { 
  background-color: #e08553; 
  transform: translateY(-1px);
}

.btn-action-excel { 
  background: #e6f7f0; 
  color: #0aa06e; 
  border: none; 
  padding: 9px 18px; 
  border-radius: 8px; 
  cursor: pointer; 
  font-size: 13px; 
  font-weight: 600; 
  transition: all 0.2s;
}

.btn-action-excel:hover { 
  background: #ccefe3; 
  transform: translateY(-1px);
}

.table-responsive {
  overflow-x: auto;
}

/* ==========================================================================
   4. CẤU TRÚC BẢNG DỮ LIỆU (TABLE STYLE)
   ========================================================================== */
.custom-data-table { 
  width: 100%; 
  border-collapse: collapse; 
  text-align: left; 
}

.custom-data-table th { 
  background-color: #f79b66; 
  color: #fff; 
  padding: 14px 10px; 
  font-size: 14px; 
  font-weight: 600; 
  text-align: center; 
}

.custom-data-table td { 
  padding: 14px 10px; 
  border-bottom: 1px solid #edf2f7; 
  font-size: 14px; 
  color: #2d3748; 
  vertical-align: middle; 
}

.custom-data-table tbody tr:hover {
  background-color: #f8fafc;
}

.product-code-text { 
  font-weight: 600; 
  color: #f79b66; 
}

/* ⚡ ĐÃ CẬP NHẬT CSS: Định dạng Badge góc ảnh theo chuẩn Nike */
.variant-image-wrapper {
  position: relative;
  width: 50px;
  height: 50px;
  margin: 0 auto;
}

.variant-img-thumb-table {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
}

.badge-corner-discount {
  position: absolute;
  top: -4px;
  left: -4px;
  background-color: #ef4444; 
  color: white;
  font-size: 10px;
  font-weight: 800;
  padding: 1px 4px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(239, 68, 68, 0.35);
  z-index: 2;
}

.badge-prop { 
  padding: 3px 8px; 
  border-radius: 4px; 
  font-weight: 500; 
  font-size: 12px; 
}

.badge-prop.cyan { 
  background: #e6f7ff; 
  color: #096dd9; 
  border: 1px solid #bae7ff; 
}

.badge-prop.orange { 
  background: #fff7ed; 
  color: #f79b66; 
  border: 1px solid #ffedd5; 
}

.badge-discount-percent { 
  background-color: #fee2e2; 
  color: #ef4444; 
  padding: 3px 8px; 
  border-radius: 4px; 
  font-weight: 700; 
  font-size: 12px; 
  display: inline-block; 
}

.text-muted-dash { 
  color: #a0aec0; 
  font-weight: 500; 
}

.original-price-strike { 
  color: #a0aec0; 
  font-size: 12px; 
  text-decoration: line-through; 
  display: block; 
  margin-bottom: 2px; 
}

.discounted-price-text { 
  color: #ef4444; 
  font-weight: 700; 
  font-size: 14px; 
}

.normal-price-text { 
  color: #2563eb; 
  font-weight: 600; 
  font-size: 14px; 
}

.empty-table-row { 
  text-align: center; 
  padding: 40px !important; 
  color: #a0aec0; 
  font-style: italic;
}

.badge-status-text { 
  padding: 5px 14px; 
  border-radius: 30px; 
  font-size: 13px; 
  font-weight: 500; 
  display: inline-block; 
  min-width: 130px;
  text-align: center;
}

.badge-status-text.status-active { 
  background-color: #e6f4ea; 
  color: #137333; 
}

.badge-status-text.status-stopped { 
  background-color: #e8f0fe; 
  color: #1a73e8; 
}

/* ==========================================================================
   5. NÚT THAO TÁC MINIMALIST BLACK & TOGGLE
   ========================================================================== */
.action-buttons-flex-group { 
  display: flex; 
  gap: 6px; 
  justify-content: center; 
  align-items: center; 
}

.icon-btn-circle { 
  width: 32px; 
  height: 32px; 
  border-radius: 50%; 
  border: none; 
  display: inline-flex; 
  align-items: center; 
  justify-content: center; 
  cursor: pointer; 
  font-size: 15px; 
  background-color: transparent;
  color: #4a5568; 
  transition: all 0.2s ease; 
}

.icon-btn-circle.view-eye {
  color: #3b82f6;
}
.icon-btn-circle.view-eye:hover {
  background-color: #eff6ff;
  color: #2563eb;
}

.icon-btn-circle.edit-pencil-black:hover {
  background-color: #f1f5f9;
  color: #000000;
}

.status-toggle-container { 
  display: inline-flex; 
  align-items: center; 
  cursor: pointer; 
  user-select: none; 
  padding: 0 4px;
}

.toggle-track { 
  position: relative; 
  display: inline-block; 
  width: 34px; 
  height: 18px; 
  border-radius: 999px; 
  transition: background-color 0.2s ease; 
  background-color: #cbd5e1; 
}

.track-active { 
  background-color: #22c55e; 
}

.toggle-handle { 
  position: absolute; 
  top: 3px; 
  left: 3px; 
  width: 12px; 
  height: 12px; 
  background-color: #fff; 
  border-radius: 50%; 
  transition: transform 0.2s ease; 
}

.track-active .toggle-handle { 
  transform: translateX(16px); 
}

/* ==========================================================================
   6. THANH PHÂN TRANG & HIỆU ỨNG CHUẨN MODAL QR CODE
   ========================================================================== */
.custom-pagination-wrapper { 
  display: flex; 
  align-items: center; 
  gap: 10px; 
  margin-top: 25px; 
  justify-content: flex-start; 
}

.page-arrow-btn { 
  width: 32px; 
  height: 32px; 
  background: #fff; 
  border: 1px solid #e2e8f0; 
  border-radius: 6px; 
  display: flex; 
  align-items: center; 
  justify-content: center; 
  font-size: 14px; 
  color: #4a5568; 
  cursor: pointer; 
  transition: 0.2s; 
}

.page-arrow-btn:hover:not(:disabled) { 
  background: #f8fafc; 
  border-color: #cbd5e1; 
}

.page-arrow-btn:disabled { 
  background: white; 
  color: #cbd5e1; 
  cursor: not-allowed; 
  border-color: #f1f5f9; 
  opacity: 0.5;
}

.page-text-indicator { 
  font-size: 13.5px; 
  font-weight: 500; 
  color: #4a5568; 
  padding: 0 4px; 
}

/* ⚡ HIỆU ỨNG PHẲNG CHO KHỐI MODAL QR CODE MỚI */
.qr-modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(15, 23, 42, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
.qr-modal-content {
  background-color: #ffffff;
  width: 580px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(0,0,0,0.15);
  animation: popUpModal 0.2s ease-out;
}
.modal-header-orange {
  background-color: #f79b66;
  color: white;
  padding: 12px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.modal-header-orange h4 { margin: 0; font-size: 14px; font-weight: bold; }
.btn-close-modal { background: none; border: none; color: white; font-size: 24px; cursor: pointer; }

.modal-body-layout { padding: 20px; display: flex; gap: 20px; }
.info-specs-column { flex: 1; display: flex; flex-direction: column; gap: 10px; }
.spec-row-item { display: flex; justify-content: space-between; font-size: 13px; border-bottom: 1px dashed #f1f5f9; padding-bottom: 6px; text-align: left; }
.spec-row-item span { color: #64748b; }

.qr-code-column { width: 190px; display: flex; flex-direction: column; align-items: center; justify-content: center; border-left: 1px solid #f1f5f9; padding-left: 20px; }
.qr-canvas-holder { border: 1px solid #cbd5e1; padding: 4px; border-radius: 6px; background: #fff; }
.btn-print-qr-code { background-color: #1e293b; color: white; border: none; padding: 9px 16px; border-radius: 6px; font-size: 12px; font-weight: 600; margin-top: 15px; cursor: pointer; width: 100%; }
.btn-print-qr-code:hover { background-color: #0f172a; }

@keyframes popUpModal {
  from { transform: scale(0.96); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}
</style>