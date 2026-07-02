<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <div class="breadcrumb-text">
        <strong>Quản lý sản phẩm</strong> / Danh sách biến thể tổng kho
      </div>
      <button class="btn-toggle-filter" @click="isFilterVisible = !isFilterVisible">
        {{ isFilterVisible ? 'Ẩn bộ lọc' : 'Hiện bộ lọc' }}
      </button>
    </div>

    <div class="filter-container-orange" v-if="isFilterVisible">
      <div class="filter-header-title">
        <i class="fa-solid fa-filter"></i> Bộ lọc tìm kiếm
      </div>
      <div class="filter-body-content">
        <div class="filter-grid">
          <div class="filter-item">
            <label>Tìm kiếm</label>
            <input
              type="text"
              v-model="filterForm.keyword"
              placeholder="Mã SKU hoặc tên sản phẩm..."
              @input="phatSinhTimKiem"
            />
          </div>

          <div class="filter-item slider-item">
            <label>Khoảng giá bán: 0đ - {{ formatTienTe(filterForm.giaMax) }}</label>
            <div class="price-slider-wrapper">
              <input
                type="range"
                v-model.number="filterForm.giaMax"
                min="0"
                max="50000000"
                step="100000"
                class="custom-slider"
                @input="phatSinhTimKiem"
              />
            </div>
          </div>

          <div class="filter-item">
            <label>Màu sắc</label>
            <select v-model="filterForm.idMauSac" @change="phatSinhTimKiem">
              <option value="">-- Tất cả --</option>
              <option v-for="ms in masterData.mauSec" :key="ms.id" :value="ms.id">
                {{ ms.tenMauSac || ms.tenMauSec || ms.ten }}
              </option>
            </select>
          </div>

          <div class="filter-item">
            <label>Trọng lượng</label>
            <select v-model="filterForm.idTrongLuong" @change="phatSinhTimKiem">
              <option value="">-- Tất cả --</option>
              <option v-for="tl in masterData.trongLuong" :key="tl.id" :value="tl.id">
                {{ tl.tenTrongLuong || tl.ten }}
              </option>
            </select>
          </div>

          <div class="filter-item">
            <label>Trạng thái</label>
            <select v-model="filterForm.trangThai" @change="phatSinhTimKiem">
              <option value="">-- Tất cả --</option>
              <option value="1">Đang hoạt động</option>
              <option value="0">Ngừng hoạt động</option>
            </select>
          </div>

          <div class="filter-item filter-actions">
            <button class="btn-filter-clear" @click="lamMoiBoLoc">
               Đặt lại
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="data-table-container">
      <div class="table-header-row">
        <div class="table-summary-title">Danh sách biến thể</div>
        <div class="header-actions">
          <button class="btn-action-excel" @click="exportToExcel">
             Xuất Excel biến thể
          </button>
        </div>
      </div>

      <table class="custom-data-table">
        <thead>
          <tr>
            <th style="width: 50px; text-align: center;">STT</th>
            <th style="width: 80px; text-align: center;">Hình ảnh</th>
            <th>Mã SP cha</th>
            <th>Mã SKU (Biến thể)</th>
            <th>Màu sắc</th>
            <th>Trọng lượng</th>
            <th style="text-align: center; width: 90px;">% Giảm</th>
            <th style="width: 140px;">Giá bán lẻ</th>
            <th style="text-align: center; width: 110px;">Số lượng</th>
            <th style="text-align: center; width: 160px;">Trạng thái</th>
            <th style="text-align: center; width: 150px;">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="filteredDanhSachBienTe.length === 0">
            <td colspan="11" class="empty-table-row">
              Không tìm thấy biến thể nào khớp với bộ lọc dữ liệu!
            </td>
          </tr>
          <tr v-for="(item, index) in filteredDanhSachBienTe" :key="item.id || index">
            <td style="text-align: center;">{{ (currentPage * pageSize) + index + 1 }}</td>
            
            <td style="text-align: center; vertical-align: middle;">
              <div class="variant-image-wrapper">
                <img 
                  :src="item.duongDanAnh ? ('http://localhost:8080' + item.duongDanAnh) : 'https://placehold.co/50x50?text=No+Image'" 
                  alt="Ảnh" 
                  class="variant-img-thumb-table"
                />
                <span v-if="item.phanTramGiam && item.phanTramGiam > 0" class="badge-corner-discount">
                  -{{ item.phanTramGiam }}%
                </span>
              </div>
            </td>

            <td style="font-weight: bold; color: #475569;">{{ item.maSanPham || '---' }}</td>
            <td class="product-code-text">{{ item.maCtsp }}</td>
            <td><span class="badge-prop orange">{{ item.tenMauSac || 'Mặc định' }}</span></td>
            <td><span class="badge-prop cyan">{{ item.tenTrongLuong || 'Mặc định' }}</span></td>
            
            <td style="text-align: center;">
              <span v-if="item.phanTramGiam && item.phanTramGiam > 0" class="badge-discount-percent">
                -{{ item.phanTramGiam }}%
              </span>
              <span v-else class="text-muted-dash">-</span>
            </td>

            <td>
              <div v-if="item.phanTramGiam && item.phanTramGiam > 0">
                <del class="original-price-strike">{{ formatTienTe(item.giaBan) }}</del>
                <div class="discounted-price-text">{{ formatTienTe(item.giaDaGiam) }}</div>
              </div>
              <div v-else class="normal-price-text">
                {{ formatTienTe(item.giaBan) }}
              </div>
            </td>
            
            <td style="text-align: center; font-weight: bold; color: #1e293b;">{{ item.soLuong ?? 0 }}</td>
            
            <td style="text-align: center;">
              <span :class="['badge-status-text', item.trangThai === 1 ? 'status-active' : 'status-stopped']">
                {{ item.trangThai === 1 ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
              </span>
            </td>
            
            <td style="text-align: center;">
              <div class="action-buttons-flex-group">
                <button class="icon-btn-circle view-eye" @click="moModalXemQr(item)" title="Xem thông tin & Mã QR">
                  <i class="fa-regular fa-eye"></i>
                </button>

                <button class="icon-btn-circle edit-pencil-black" @click="editItem(item)" title="Sửa biến thể">
                  <i class="fa-regular fa-pen-to-square"></i>
                </button>

                <div class="status-toggle-container" @click="toggleTrangThaiBienTheNhanh(item)" title="Đổi trạng thái">
                  <span :class="['toggle-track', item.trangThai === 1 ? 'track-active' : 'track-inactive']">
                    <span class="toggle-handle"></span>
                  </span>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <div class="custom-pagination-wrapper">
        <button class="page-arrow-btn" :disabled="currentPage === 0" @click="chuyenTrang(currentPage - 1)">&lt;</button>
        <span class="page-text-indicator">Trang {{ totalPages > 0 ? currentPage + 1 : 1 }} / {{ totalPages || 1 }}</span>
        <button class="page-arrow-btn" :disabled="currentPage >= totalPages - 1 || totalPages <= 1" @click="chuyenTrang(currentPage + 1)">&gt;</button>
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
            <div class="spec-row-item"><span>Mã sản phẩm cha:</span> <strong>{{ selectedVariant.maSanPham || '---' }}</strong></div>
            <div class="spec-row-item"><span>Mã SKU biến thể:</span> <strong>{{ selectedVariant.maCtsp }}</strong></div>
            <div class="spec-row-item"><span>Màu sắc:</span> <strong>{{ selectedVariant.tenMauSac || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Trọng lượng:</span> <strong>{{ selectedVariant.tenTrongLuong || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Độ cứng thân vợt:</span> <strong>{{ selectedVariant.tenDoCung || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Điểm cân bằng:</span> <strong>{{ selectedVariant.tenDiemCanBang || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Chu vi cán vợt:</span> <strong>{{ selectedVariant.tenChuViCanVot || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Chất liệu khung:</span> <strong>{{ selectedVariant.tenChatLieuKhungVot || 'Mặc định' }}</strong></div>
            <div class="spec-row-item"><span>Số kho lượng tồn:</span> <strong>{{ selectedVariant.soLuong ?? 0 }} chiếc</strong></div>
            <div class="spec-row-item"><span>Giá gốc niêm yết:</span> <strong>{{ formatTienTe(selectedVariant.giaBan) }}</strong></div>
            <div class="spec-row-item" v-if="selectedVariant.phanTramGiam > 0">
              <span>Giá ưu đãi hiện tại:</span> <strong style="color: #ef4444;">{{ formatTienTe(selectedVariant.giaDaGiam) }} (-{{ selectedVariant.phanTramGiam }}%)</strong>
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
import { ref, onMounted, computed, nextTick } from 'vue';
import myAxios from '../../api/axios';
import { useRouter } from 'vue-router';
import MainLayout from '@/layouts/MainLayout.vue';
import * as XLSX from 'xlsx';
import QRCode from 'qrcode';

const router = useRouter();
const isFilterVisible = ref(true);
const currentPage = ref(0);
const pageSize = ref(5);
const totalPages = ref(1);

const listBienTheRaw = ref([]);
const masterData = ref({ mauSec: [], trongLuong: [] });

const isQrModalOpen = ref(false);
const selectedVariant = ref({});
const qrCanvasRef = ref(null);

const filterForm = ref({
  keyword: '', idMauSac: '', idTrongLuong: '', trangThai: '', giaMax: 50000000
});

const formatTienTe = (value) => {
  if (value === null || value === undefined || isNaN(value)) return '0 đ';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const safeExtractArray = (res) => { return res && res.data ? (Array.isArray(res.data) ? res.data : (res.data.content || [])) : []; };

const moModalXemQr = async (item) => {
  selectedVariant.value = item;
  isQrModalOpen.value = true;
  
  await nextTick();
  if (qrCanvasRef.value) {
    const duLieuMaHoa = item.maCtsp || 'CTSP';
    QRCode.toCanvas(qrCanvasRef.value, duLieuMaHoa, {
      width: 165,
      margin: 1,
      color: { dark: '#1e293b', white: '#ffffff' }
    }, (error) => {
      if (error) console.error("Lỗi vẽ QR Code canvas:", error);
    });
  }
};

const inMaQRCode = () => {
  const canvas = qrCanvasRef.value;
  if (!canvas) return;
  
  const urlAnhQr = canvas.toDataURL("image/png");
  const cuaSoIn = window.open('', '_blank');
  cuaSoIn.document.write(`
    <html>
      <head>
        <title>In tem QR - ${selectedVariant.value.maCtsp}</title>
        <style>
          body { text-align: center; font-family: Arial, sans-serif; padding: 12px; }
          .sku-title { font-size: 14px; font-weight: bold; margin-top: 4px; text-transform: uppercase; }
        </style>
      </head>
      <body onload="window.print(); window.close();">
        <img src="${urlAnhQr}" width="140" height="140"/>
        <div class="sku-title">${selectedVariant.value.maCtsp}</div>
        <div style="font-size: 11px; color: #475569;">Màu: ${selectedVariant.value.tenMauSac || 'Mặc định'} - Trọng lượng: ${selectedVariant.value.tenTrongLuong || 'Mặc định'}</div>
      </body>
    </html>
  `);
  cuaSoIn.document.close();
};

// 🌟 ĐÃ SỬA: Đọc phẳng mảng dữ liệu đã phân trang Server-side được trả về từ DB sạch sẽ
const filteredDanhSachBienTe = computed(() => {
  return listBienTheRaw.value;
});

// 🌟 ĐÃ SỬA: Đính kèm mảng ID gửi trực tiếp lên SQL Server để thực thi tìm kiếm nâng cao ở Database
const loadToanBoDuLieu = async () => {
  try {
    const paramsPayload = {
      page: currentPage.value,
      size: pageSize.value
    };

    if (filterForm.value.keyword?.trim()) paramsPayload.keyword = filterForm.value.keyword.trim();
    if (filterForm.value.trangThai !== '') paramsPayload.trangThai = parseInt(filterForm.value.trangThai, 10);
    
    // Đẩy trực tiếp ID lên Server-side Pagination
    if (filterForm.value.idMauSac !== '') paramsPayload.idMauSac = parseInt(filterForm.value.idMauSac, 10);
    if (filterForm.value.idTrongLuong !== '') paramsPayload.idTrongLuong = parseInt(filterForm.value.idTrongLuong, 10);

    const [ctsp, ms, tl] = await Promise.all([
      myAxios.get('/api/chi-tiet-san-pham/search', {
        params: paramsPayload
      }),
      myAxios.get('/api/mau-sac/all'),   
      myAxios.get('/api/trong-luong/all')
    ]);

    let rawData = [];
    if (ctsp.data && ctsp.data.content !== undefined) {
      rawData = ctsp.data.content;
      totalPages.value = ctsp.data.totalPages || 1;
    } else {
      rawData = safeExtractArray(ctsp);
      totalPages.value = 1;
    }

    // Cơ chế phòng vệ tự động gán trạng thái con ăn theo cha khi sản phẩm cha ngừng bán
    listBienTheRaw.value = rawData.map(item => {
      let finalStatus = item.trangThai;
      if (item.sanPham && (item.sanPham.trangThai === 0 || item.sanPham.trangThai === false)) {
        finalStatus = 0;
      } else if (item.trangThaiSanPham === 0 || item.trangThaiSanPham === false) {
        finalStatus = 0;
      }
      return {
        ...item,
        trangThai: finalStatus
      };
    });
    
    masterData.value.mauSec = safeExtractArray(ms);
    masterData.value.trongLuong = safeExtractArray(tl);
  } catch (error) { console.error("Lỗi nạp dữ liệu tổng kho:", error); }
};

const chuyenTrang = (page) => { currentPage.value = page; loadToanBoDuLieu(); };
const phatSinhTimKiem = () => { currentPage.value = 0; loadToanBoDuLieu(); };

const lamMoiBoLoc = () => { 
  filterForm.value = { keyword: '', idMauSac: '', idTrongLuong: '', trangThai: '', giaMax: 50000000 }; 
  loadToanBoDuLieu(); 
};

const toggleTrangThaiBienTheNhanh = async (item) => {
  const trangThaiMoi = item.trangThai === 1 ? 0 : 1;
  if (!confirm("Thay đổi trạng thái hoạt động biến thể?")) return;
  try {
    await myAxios.put(`/api/chi-tiet-san-pham/${item.id}/trang-thai`, null, { params: { trangThai: trangThaiMoi } });
    loadToanBoDuLieu();
  } catch (error) { alert("Lỗi cập nhật!"); }
};

const editItem = (item) => {
  router.push({ 
    path: '/san-pham/bien-the/sua', 
    query: { 
      id: item.id, 
      idSP: item.idSanPham,
      maSP: item.maSanPham || '',
      tenSP: item.tenSanPham || '',
      from: 'tong-kho' 
    } 
  });
};

const exportToExcel = () => {
  const mapped = filteredDanhSachBienTe.value.map((item, i) => ({ "STT": i + 1, "SKU": item.maCtsp, "Giá thực": item.giaDaGiam || item.giaBan, "Tồn": item.soLuong, "Trạng thái": item.trangThai === 1 ? "Đang hoạt động" : "Ngừng hoạt động" }));
  const ws = XLSX.utils.json_to_sheet(mapped);
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, "Variants");
  XLSX.writeFile(wb, "Bien_the.xlsx");
};

onMounted(loadToanBoDuLieu);
</script>

<style scoped>
.breadcrumb-container { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #4a5568; }
.btn-toggle-filter { background-color: #f8fafc; border: 1px solid #cbd5e1; color: #475569; padding: 6px 14px; border-radius: 8px; cursor: pointer; font-size: 13px; font-weight: 500; transition: all 0.2s; }
.btn-toggle-filter:hover { background-color: #e2e8f0; }
.filter-container-orange { background-color: #fff; border: 1px solid #f79b66; border-radius: 12px; margin-bottom: 20px; box-shadow: 0 1px 4px rgba(0,0,0,0.05); overflow: hidden; }
.filter-header-title { background-color: #f79b66; color: white; padding: 12px 18px; font-size: 14px; font-weight: bold; text-align: left; }
.filter-body-content { padding: 20px; background-color: #fff; }
.filter-grid { display: flex; flex-wrap: wrap; gap: 15px; align-items: flex-end; }
.filter-item { display: flex; flex-direction: column; gap: 8px; text-align: left; flex: 1; min-width: 160px; }
.filter-item label { font-size: 13px; font-weight: 600; color: #4a5568; }
.filter-item input, .filter-item select { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 13px; outline: none; box-sizing: border-box; width: 100%; height: 38px; background-color: #fff; transition: border-color 0.2s; }
.filter-item input:focus, .filter-item select:focus { border-color: #f79b66; }
.slider-item { min-width: 230px; }
.price-slider-wrapper { display: flex; align-items: center; width: 100%; height: 38px; }
.custom-slider { -webkit-appearance: none; width: 100%; height: 6px; background: #e2e8f0; border-radius: 5px; outline: none; }
.custom-slider::-webkit-slider-thumb { -webkit-appearance: none; width: 18px; height: 18px; border-radius: 50%; background: #f79b66; cursor: pointer; }
.btn-filter-clear { background-color: #f3f4f6; color: #4b5563; border: none; padding: 10px 20px; border-radius: 8px; font-size: 13px; font-weight: 600; cursor: pointer; height: 38px; transition: background-color 0.2s; }
.btn-filter-clear:hover { background-color: #e5e7eb; }
.data-table-container { background-color: #fff; border: 1px solid #edf2f7; border-radius: 12px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
.table-header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; padding-bottom: 12px; border-bottom: 1px solid #edf2f7; }
.table-summary-title { font-size: 16px; font-weight: 600; color: #1a202c; }
.btn-action-excel { background: #e6f7f0; color: #0aa06e; border: none; padding: 9px 18px; border-radius: 8px; font-weight: 600; cursor: pointer; font-size: 13px; transition: all 0.2s; }
.btn-action-excel:hover { background: #ccefe3; transform: translateY(-1px); }
.custom-data-table { width: 100%; border-collapse: collapse; }
.custom-data-table th { background-color: #f79b66; color: white; padding: 14px 10px; font-size: 14px; font-weight: 600; text-align: center; }
.custom-data-table td { padding: 14px 10px; border-bottom: 1px solid #edf2f7; font-size: 14px; color: #2d3748; vertical-align: middle; }
.custom-data-table tbody tr:hover { background-color: #f8fafc; }
.product-code-text { font-weight: bold; color: #f79b66; }
.variant-image-wrapper { position: relative; width: 50px; height: 50px; margin: 0 auto; }
.variant-img-thumb-table { width: 50px; height: 50px; object-fit: cover; border-radius: 6px; border: 1px solid #e2e8f0; }
.badge-corner-discount { position: absolute; top: -4px; left: -4px; background-color: #ef4444; color: white; font-size: 10px; font-weight: 800; padding: 1px 4px; border-radius: 4px; box-shadow: 0 2px 4px rgba(239, 68, 68, 0.35); z-index: 2; }
.badge-prop { padding: 3px 8px; border-radius: 4px; font-weight: 500; font-size: 12px; }
.badge-prop.cyan { background: #e6f7ff; color: #096dd9; border: 1px solid #bae7ff; }
.badge-prop.orange { background: #fff7ed; color: #f79b66; border: 1px solid #ffedd5; }
.badge-discount-percent { background-color: #fee2e2; color: #ef4444; padding: 3px 8px; border-radius: 4px; font-weight: 700; font-size: 12px; }
.text-muted-dash { color: #a0aec0; }
.discounted-price-text { color: #ef4444; font-weight: 700; }
.original-price-strike { color: #a0aec0; text-decoration: line-through; font-size: 12px; }
.normal-price-text { color: #2563eb; font-weight: 600; }
.empty-table-row { text-align: center; color: #a0aec0; padding: 40px !important; font-style: italic; }
.badge-status-text { padding: 5px 14px; border-radius: 30px; font-size: 13px; font-weight: 500; display: inline-block; min-width: 130px; text-align: center; }
.badge-status-text.status-active { background-color: #e6f4ea; color: #137333; }
.badge-status-text.status-stopped { background-color: #e8f0fe; color: #1a73e8; }
.action-buttons-flex-group { display: flex; gap: 6px; justify-content: center; align-items: center; }
.icon-btn-circle { width: 32px; height: 32px; border-radius: 50%; border: none; display: inline-flex; align-items: center; justify-content: center; cursor: pointer; font-size: 15px; background-color: transparent; color: #4a5568; transition: all 0.2s ease; }
.icon-btn-circle.view-eye { color: #3b82f6; }
.icon-btn-circle.view-eye:hover { background-color: #eff6ff; color: #2563eb; }
.icon-btn-circle.edit-pencil-black:hover { background-color: #f1f5f9; color: #000000; }
.status-toggle-container { display: inline-flex; align-items: center; cursor: pointer; user-select: none; padding: 0 4px; }
.toggle-track { width: 34px; height: 18px; border-radius: 999px; position: relative; background-color: #cbd5e1; transition: background-color 0.2s ease; }
.track-active { background-color: #22c55e; }
.toggle-handle { width: 12px; height: 12px; background-color: white; border-radius: 50%; position: absolute; top: 3px; left: 3px; transition: transform 0.2s ease; }
.track-active .toggle-handle { transform: translateX(16px); }
.custom-pagination-wrapper { display: flex; align-items: center; gap: 10px; margin-top: 25px; justify-content: flex-start; }
.page-arrow-btn { width: 32px; height: 32px; background: #fff; border: 1px solid #e2e8f0; border-radius: 6px; display: flex; align-items: center; justify-content: center; color: #4a5568; cursor: pointer; transition: 0.2s; }
.page-arrow-btn:hover:not(:disabled) { background: #f8fafc; border-color: #cbd5e1; }
.page-arrow-btn:disabled { background: white; color: #cbd5e1; cursor: not-allowed; border-color: #f1f5f9; opacity: 0.5; }
.page-text-indicator { font-size: 13.5px; font-weight: 500; color: #4a5568; padding: 0 4px; }
.qr-modal-backdrop { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: rgba(15, 23, 42, 0.6); display: flex; align-items: center; justify-content: center; z-index: 9999; }
.qr-modal-content { background-color: #ffffff; width: 580px; border-radius: 8px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.15); animation: popUpModal 0.2s ease-out; }
.modal-header-orange { background-color: #f79b66; color: white; padding: 12px 20px; display: flex; justify-content: space-between; align-items: center; }
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
@keyframes popUpModal { from { transform: scale(0.96); opacity: 0; } to { transform: scale(1); opacity: 1; } }
</style>