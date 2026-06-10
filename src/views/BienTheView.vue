<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <span class="breadcrumb-text">Quản lý sản phẩm / Quản lý biến thể / <strong style="color: #f97316;">Chi tiết biến thể</strong></span>
      
      <div style="display: flex; gap: 10px;">
        <button class="btn-toggle-filter" @click="isFilterVisible = !isFilterVisible">
          {{ isFilterVisible ? '🙈 Ẩn bộ lọc' : '🔍 Hiện bộ lọc' }}
        </button>
        <button class="btn-back-to-product" @click="quayLaiDanhSachSanPham">
          ⬅ Quay lại trang Sản phẩm
        </button>
      </div>
    </div>

    <div class="box-alert-info" v-if="productInfo.ma">
      💡 Đang hiển thị danh sách biến thể của: <strong>[{{ productInfo.ma }}] - {{ productInfo.ten }}</strong>
    </div>

    <div class="filter-section-container" v-if="isFilterVisible">
      <div class="filter-title">🔍 Bộ lọc tìm kiếm biến thể</div>
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
          <button class="btn-reset-filter" @click="resetFilter">Làm mới</button>
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
          <button class="btn-add-new" @click="moModalThemMoi">
            ➕ Thêm mới biến thể
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
              <th style="text-align: center;">Số lượng tồn</th>
              <th>Giá bán lẻ</th>
              <th style="width: 160px; text-align: center;">Trạng thái</th>
              <th style="text-align: center; width: 180px;">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(bt, index) in danhSachBienThe" :key="bt.id">
              <td style="text-align: center;">{{ (filter.page * filter.size) + index + 1 }}</td>
              
              <td style="text-align: center;">
  <img 
    :src="(() => {
      if (!bt.duongDanAnh) return 'https://placehold.co/50x50?text=No+Image';
      
      if (bt.duongDanAnh.startsWith('http') || bt.duongDanAnh.startsWith('data:')) return bt.duongDanAnh;
      
      const fileSach = bt.duongDanAnh.replace(/^\/?uploads\//, '').replace(/^\//, '');
      
      return `http://localhost:8080/uploads/${fileSach}`;
    })()" 
    alt="Ảnh biến thể" 
    class="variant-img-thumb"
    @error="(e) => { e.target.src = 'https://placehold.co/50x50?text=Lỗi+Ảnh' }"
  />
</td>

              <td class="product-code-text">{{ bt.maCtsp }}</td>
              <td><span class="badge-prop orange">{{ bt.tenMauSac || 'Mặc định' }}</span></td>
              <td><span class="badge-prop cyan">{{ bt.tenTrongLuong || 'Mặc định' }}</span></td>
              <td>{{ bt.tenDoCung || 'Mặc định' }}</td>
              <td>{{ bt.tenDiemCanBang || 'Mặc định' }}</td>
              <td>{{ bt.tenChuViCanVot || 'Mặc định' }}</td>
              
              <td style="font-weight: bold; text-align: center;">{{ bt.soLuong ?? 0 }}</td>
              <td style="color: #2563eb; font-weight: 600;">{{ formatPrice(bt.giaBan) }} đ</td>
              
              <td style="text-align: center;">
                <span :class="['badge-status-text', bt.trangThai === 1 ? 'status-active' : 'status-stopped']">
                  {{ bt.trangThai === 1 ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
                </span>
              </td>

              <td style="text-align: center;">
                <div class="action-buttons-cell">
                  <button class="btn-action-edit" @click="moModalCapNhat(bt)">✏️ Sửa</button>
                  
                  <div class="status-toggle-container" @click="toggleTrangThaiNhanhCucBo(bt)" title="Gạt để thay đổi trạng thái hoạt động">
                    <span :class="['toggle-track', bt.trangThai === 1 ? 'track-active' : 'track-inactive']">
                      <span class="toggle-handle"></span>
                    </span>
                  </div>
                </div>
              </td>
            </tr>
            <tr v-if="danhSachBienThe.length === 0">
              <td colspan="13" class="empty-table-row">Không tìm thấy chi tiết biến thể nào khớp với bộ lọc!</td>
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

    <div class="custom-modal-overlay" v-if="isModalOpen">
      <div class="custom-modal-box">
        <div class="modal-box-header">
          <h3>{{ isEditMode ? '✏️ Cập nhật thông tin chi tiết biến thể' : '➕ Thêm mới chi tiết biến thể' }}</h3>
          <button class="btn-close-x" @click="isModalOpen = false">&times;</button>
        </div>
        
        <div class="modal-box-body">
          <div class="image-upload-section">
            <div class="image-preview-box">
              <img :src="previewImageSrc" alt="Xem trước ảnh" />
            </div>
            <div class="form-group-item" style="flex: 1;">
              <label>Hình ảnh file đính kèm <span class="required-star"> *</span></label>
              <input type="file" @change="onFileChange" accept="image/*" style="margin-top: 5px; font-size: 13px;" />
            </div>
          </div>

          <div class="form-group-item">
            <label>Mã biến thể (Hệ thống tự sinh) <span class="required-star"> *</span></label>
            <input 
              type="text" 
              v-model="editingForm.maCtsp" 
              disabled
              placeholder="Hệ thống đang tự tính toán mã..."
              class="disabled-input"
            />
          </div>

          <div class="form-row-flex">
            <div class="form-group-item">
              <label>Màu sắc sản phẩm</label>
              <select v-model="editingForm.idMauSac">
                <option :value="null">-- Chọn màu sắc --</option>
                <option v-for="item in masterData.mauSac" :key="item.id" :value="item.id">{{ item.tenMauSac }}</option>
              </select>
            </div>
            <div class="form-group-item">
              <label>Trọng lượng vợt</label>
              <select v-model="editingForm.idTrongLuong">
                <option :value="null">-- Chọn trọng lượng --</option>
                <option v-for="item in masterData.trongLuong" :key="item.id" :value="item.id">{{ item.tenTrongLuong }}</option>
              </select>
            </div>
          </div>

          <div class="form-row-flex">
            <div class="form-group-item">
              <label>Độ cứng thân</label>
              <select v-model="editingForm.idDoCung">
                <option :value="null">-- Chọn độ cứng --</option>
                <option v-for="item in masterData.doCung" :key="item.id" :value="item.id">{{ item.tenDoCung }}</option>
              </select>
            </div>
            <div class="form-group-item">
              <label>Điểm cân bằng</label>
              <select v-model="editingForm.idDiemCanBang">
                <option :value="null">-- Chọn điểm cân bằng --</option>
                <option v-for="item in masterData.diemCanBang" :key="item.id" :value="item.id">{{ item.tenDiemCanBang }}</option>
              </select>
            </div>
          </div>

          <div class="form-row-flex">
            <div class="form-group-item">
              <label>Chu vi cán vợt (Cấu hình size)</label>
              <select v-model="editingForm.idChuViCanVot">
                <option :value="null">-- Chọn chu vi cán --</option>
                <option v-for="item in masterData.chuViCan" :key="item.id" :value="item.id">{{ item.tenChuViCanVot }}</option>
              </select>
            </div>
            <div class="form-group-item">
              <label>Danh mục phân loại</label>
              <select v-model="editingForm.idDanhMuc">
                <option :value="null">-- Chọn danh mục --</option>
                <option v-for="item in masterData.danhMuc" :key="item.id" :value="item.id">{{ item.tenDanhMuc }}</option>
              </select>
            </div>
          </div>

          <div class="form-row-flex">
            <div class="form-group-item">
              <label>Chất liệu thân vợt</label>
              <select v-model="editingForm.idChatLieuThanVot">
                <option :value="null">-- Chọn chất liệu thân --</option>
                <option v-for="item in masterData.chatLieuThan" :key="item.id" :value="item.id">{{ item.tenChatLieuThanVot }}</option>
              </select>
            </div>
            <div class="form-group-item">
              <label>Chất liệu khung vợt</label>
              <select v-model="editingForm.idChatLieuKhungVot">
                <option :value="null">-- Chọn chất liệu khung --</option>
                <option v-for="item in masterData.chatLieuKhung" :key="item.id" :value="item.id">{{ item.tenChatLieuKhungVot }}</option>
              </select>
            </div>
          </div>
          
          <div class="form-row-flex">
            <div class="form-group-item">
              <label>Giá nhập kho (VNĐ) <span class="required-star"> *</span></label>
              <input type="number" v-model.number="editingForm.giaNhap" min="0"/>
            </div>
            <div class="form-group-item">
              <label>Giá bán lẻ (VNĐ) <span class="required-star"> *</span></label>
              <input type="number" v-model.number="editingForm.giaBan" min="0"/>
            </div>
          </div>

          <div class="form-row-flex">
            <div class="form-group-item">
              <label>Số lượng kho <span class="required-star"> *</span></label>
              <input type="number" v-model.number="editingForm.soLuong" min="0"/>
            </div>
            <div class="form-group-item">
              <label>Trạng thái hoạt động</label>
              <div class="status-toggle-container" style="margin-top: 8px;" @click="editingForm.trangThai = editingForm.trangThai === 1 ? 0 : 1">
                <span :class="['toggle-track', editingForm.trangThai === 1 ? 'track-active' : 'track-inactive']">
                  <span class="toggle-handle"></span>
                </span>
                <span class="toggle-text" :style="{ color: editingForm.trangThai === 1 ? '#16a34a' : '#64748b' }">
                  {{ editingForm.trangThai === 1 ? 'Đang hoạt động' : 'Ngừng hoạt động' }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-box-footer">
          <button class="btn-modal-cancel" @click="isModalOpen = false">Hủy bỏ</button>
          <button class="btn-modal-submit" @click="submitLuuDuLieuConfirm">Lưu dữ liệu</button>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import MainLayout from '../layouts/MainLayout.vue';
import * as XLSX from 'xlsx';

const route = useRoute();
const router = useRouter();

const isFilterVisible = ref(true); 
const isModalOpen = ref(false);
const isEditMode = ref(false);

const danhSachBienThe = ref([]);
const totalElements = ref(0);
const totalPages = ref(1); 

// Đồng bộ cấu trúc dữ liệu filter tránh bị chết bộ lọc tìm kiếm
const filter = ref({ page: 0, size: 5, keyword: '', trangThai: '' });
const productInfo = ref({ id: route.query.idSP || '', ma: route.query.maSP || '', ten: route.query.tenSP || '' });

const masterData = ref({
  mauSac: [], trongLuong: [], doCung: [], diemCanBang: [], chuViCan: [], danhMuc: [], chatLieuThan: [], chatLieuKhung: []
});

const fileUploadData = ref(null);
const previewImageSrc = ref('https://placehold.co/100x100?text=Chọn+Ảnh');

const editingForm = ref({
  id: null, maCtsp: '', soLuong: 0, giaBan: 0, giaNhap: 0, trangThai: 1, hinhAnh: '', idSanPham: null,
  idMauSac: null, idTrongLuong: null, idDoCung: null, idDiemCanBang: null, idChatLieuThanVot: null, idChatLieuKhungVot: null, idDanhMuc: null, idChuViCanVot: null
});

const formatPrice = (value) => new Intl.NumberFormat('vi-VN').format(value || 0);

const safeExtractArray = (res) => {
  if (!res || !res.data) return [];
  if (Array.isArray(res.data)) return res.data;
  if (res.data.content && Array.isArray(res.data.content)) return res.data.content;
  return [];
};

// 🤖 TỰ SINH MÃ SKU BIẾN THỂ TỊNH TIẾN TỰ ĐỘNG
const taoTuDongMaSKUBienThe = () => {
  const maCha = productInfo.value.ma || 'SP';
  if (danhSachBienThe.value.length === 0) {
    return `${maCha}-CT-001`;
  }
  
  const danhSachSoCon = danhSachBienThe.value.map(item => {
    const maSkuCon = item.maCtsp || '';
    const phanDuoi = maSkuCon.split('-').pop(); 
    const phanSo = phanDuoi ? phanDuoi.replace(/[^\d]/g, '') : '';
    return phanSo ? parseInt(phanSo, 10) : 0;
  });

  const soLonNhat = Math.max(...danhSachSoCon, 0);
  const soTiepTheo = soLonNhat + 1;
  return `${maCha}-CT-${String(soTiepTheo).padStart(3, '0')}`;
};

// 🔍 HỒI SINH BỘ LỌC: Truyền đầy đủ keyword và trangThai lên API tìm kiếm nâng cao
const fetchDataBienThe = async () => {
  try {
    if (!productInfo.value.id) return;
    
    const paramsPayload = {
      idSanPham: parseInt(productInfo.value.id, 10),
      page: filter.value.page,
      size: filter.value.size
    };
    
    // Gắn thêm điều kiện lọc nếu người dùng điền/chọn ngoài giao diện
    if (filter.value.keyword.trim() !== '') paramsPayload.keyword = filter.value.keyword.trim();
    if (filter.value.trangThai !== '') paramsPayload.trangThai = parseInt(filter.value.trangThai, 10);

    const response = await axios.get('http://localhost:8080/api/chi-tiet-san-pham/search', {
      params: paramsPayload
    });
    
    const content = response.data.content || [];
    
    // Quét ngầm lấy ảnh từ bảng hinh_anh_sp đắp vào mảng hiển thị
    await Promise.all(content.map(async (bt) => {
      try {
        const resAnh = await axios.post('http://localhost:8080/api/hinh-anh-sp/list', {
          idSanPhamChiTiet: bt.id,
          trangThai: 1
        });
        bt.duongDanAnh = resAnh.data && resAnh.data.length > 0 ? resAnh.data[0].duongDanAnh : null;
      } catch (err) {
        bt.duongDanAnh = null;
      }
    }));

    danhSachBienThe.value = content;
    totalPages.value = response.data.totalPages || 1;
    totalElements.value = response.data.totalElements || 0;
  } catch (error) { 
    console.error("Lỗi lấy danh sách biến thể:", error); 
  }
};

const loadMasterDataCombobox = async () => {
  try {
    const [ms, tl, dc, dcb, cvc, dm, clt, clk] = await Promise.all([
      axios.get('http://localhost:8080/api/mau-sac/all'),
      axios.get('http://localhost:8080/api/trong-luong/all'),
      axios.get('http://localhost:8080/api/do-cung/all'),
      axios.get('http://localhost:8080/api/diem-can-bang/all'),
      axios.get('http://localhost:8080/api/chu-vi-can-vot/active'),
      axios.get('http://localhost:8080/api/danh-muc/all'),
      axios.get('http://localhost:8080/api/chat-lieu-than-vot/all'), 
      axios.get('http://localhost:8080/api/chat-lieu-khung-vot/all')
    ]);
    masterData.value.mauSac = safeExtractArray(ms);
    masterData.value.trongLuong = safeExtractArray(tl);
    masterData.value.doCung = safeExtractArray(dc);
    masterData.value.diemCanBang = safeExtractArray(dcb);
    masterData.value.chuViCan = safeExtractArray(cvc);
    masterData.value.danhMuc = safeExtractArray(dm);
    masterData.value.chatLieuThan = safeExtractArray(clt); 
    masterData.value.chatLieuKhung = safeExtractArray(clk);
  } catch (e) {
    console.error("Lỗi nạp danh mục Master:", e);
  }
};

const thayDoiTrang = (pageNo) => { filter.value.page = pageNo; fetchDataBienThe(); };
const phatSinhTimKiem = () => { filter.value.page = 0; fetchDataBienThe(); };
const resetFilter = () => { filter.value.keyword = ''; filter.value.trangThai = ''; filter.value.page = 0; fetchDataBienThe(); };

// ⚡ ĐÃ SỬA TÊN HÀM KHỚP VỚI TEMPLATE VÀ TỐI ƯU GẠT CÔNG TẮC SIÊU TỐC KHÔNG LAG
const toggleTrangThaiNhanhCucBo = async (bt) => {
  const productId = bt.id;
  // Khóa mục tiêu chỉnh sửa thẳng vào mảng gốc của Vue (.value) để kích hoạt re-render lập tức
  const indexGoc = danhSachBienThe.value.findIndex(item => item.id === productId);
  if (indexGoc === -1) return;

  const trangThaiCu = danhSachBienThe.value[indexGoc].trangThai;
  const trangThaiMoi = trangThaiCu === 1 ? 0 : 1;
  
  if (!confirm(trangThaiMoi === 1 ? "Bạn có chắc chắn muốn MỞ kích hoạt hoạt động lại biến thể này?" : "Bạn có chắc chắn muốn TẠM NGỪNG hoạt động biến thể này?")) {
    return;
  }

  // Thay đổi cục bộ trên giao diện trong 0.01 giây (Cột trạng thái và công tắc đổi màu ngay lập tức)
  danhSachBienThe.value[indexGoc].trangThai = trangThaiMoi;

  try {
    // Truyền chuẩn Query Parameters bằng params theo đặc tả @RequestParam của Backend bạn vừa sửa
    await axios.put(`http://localhost:8080/api/chi-tiet-san-pham/${productId}/trang-thai`, null, {
      params: { trangThai: trangThaiMoi }
    });
  } catch (error) {
    // Hoàn tác (Rollback) dữ liệu cũ lên màn hình nếu đường truyền mạng gặp sự cố
    danhSachBienThe.value[indexGoc].trangThai = trangThaiCu;
    console.error("Lỗi gạt công tắc:", error);
    alert("Hệ thống mất kết nối, không thể cập nhật trạng thái kinh doanh biến thể!");
  }
};

const onFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    fileUploadData.value = file;
    previewImageSrc.value = URL.createObjectURL(file);
    editingForm.value.hinhAnh = "/uploads/" + file.name;
  }
};

const moModalThemMoi = () => {
  isEditMode.value = false;
  fileUploadData.value = null;
  previewImageSrc.value = 'https://placehold.co/100x100?text=Chọn+Ảnh';
  
  const maSkuTuSinh = taoTuDongMaSKUBienThe();

  editingForm.value = {
    id: null, maCtsp: maSkuTuSinh, soLuong: 10, giaBan: 1200000, giaNhap: 800000, trangThai: 1, hinhAnh: '',
    idSanPham: parseInt(productInfo.value.id, 10), idMauSac: null, idTrongLuong: null, idDoCung: null, idDiemCanBang: null, idChatLieuThanVot: null, idChatLieuKhungVot: null, idDanhMuc: null, idChuViCanVot: null
  };
  isModalOpen.value = true;
};

const moModalCapNhat = (bt) => {
  isEditMode.value = true;
  fileUploadData.value = null;
  
  previewImageSrc.value = bt.duongDanAnh 
    ? (bt.duongDanAnh.startsWith('http') || bt.duongDanAnh.startsWith('data:') 
        ? bt.duongDanAnh 
        : 'http://localhost:8080/uploads/' + bt.duongDanAnh.replace(/^\/?uploads\//, '')) 
    : 'https://placehold.co/100x100?text=No+Image';

  const findIdByText = (masterList, fieldName, targetText) => {
    if (!targetText) return null;
    const found = masterList.find(item => String(item[fieldName]).trim().toLowerCase() === String(targetText).trim().toLowerCase());
    return found ? found.id : null;
  };

  editingForm.value = {
    id: Number(bt.id),
    maCtsp: String(bt.maCtsp || ''),
    soLuong: bt.soLuong ? Number(bt.soLuong) : 0,
    giaBan: bt.giaBan ? Number(bt.giaBan) : 0,
    giaNhap: bt.giaNhap ? Number(bt.giaNhap) : 0,
    trangThai: bt.trangThai ? Number(bt.trangThai) : 1,
    hinhAnh: String(bt.duongDanAnh || ''), 
    idSanPham: parseInt(productInfo.value.id, 10),
    
    idMauSac: findIdByText(masterData.value.mauSac, 'tenMauSac', bt.tenMauSac),
    idTrongLuong: findIdByText(masterData.value.trongLuong, 'tenTrongLuong', bt.tenTrongLuong),
    idDoCung: findIdByText(masterData.value.doCung, 'tenDoCung', bt.tenDoCung),
    idDiemCanBang: findIdByText(masterData.value.diemCanBang, 'tenDiemCanBang', bt.tenDiemCanBang),
    idChatLieuThanVot: findIdByText(masterData.value.chatLieuThan, 'tenChatLieuThanVot', bt.tenChatLieuThanVot), 
    idChatLieuKhungVot: findIdByText(masterData.value.chatLieuKhung, 'tenChatLieuKhungVot', bt.tenChatLieuKhungVot),
    idDanhMuc: findIdByText(masterData.value.danhMuc, 'tenDanhMuc', bt.tenDanhMuc),
    idChuViCanVot: findIdByText(masterData.value.chuViCan, 'tenChuViCanVot', bt.tenChuViCanVot)
  };
  isModalOpen.value = true;
};

const submitLuuDuLieuConfirm = async () => {
  if (!editingForm.value.maCtsp?.trim()) return alert("Vui lòng điền mã biến thể (SKU)!");
  
  const thongBaoHoi = isEditMode.value 
    ? `Bạn có chắc chắn muốn CẬP NHẬT thông tin biến thể [ ${editingForm.value.maCtsp} ] không?`
    : `Bạn có chắc chắn muốn THÊM MỚI biến thể [ ${editingForm.value.maCtsp} ] không?`;

  if (!confirm(thongBaoHoi)) return;

  try {
    let idChiTiet = editingForm.value.id;
    let resCTSP;

    if (isEditMode.value) {
      resCTSP = await axios.put(`http://localhost:8080/api/chi-tiet-san-pham/${idChiTiet}`, editingForm.value);
    } else {
      resCTSP = await axios.post('http://localhost:8080/api/chi-tiet-san-pham', editingForm.value);
      idChiTiet = resCTSP.data.id; 
    }

    // Đẩy dữ liệu lưu sang bảng hình ảnh độc lập đồng bộ khóa ngoại
    if (fileUploadData.value && idChiTiet) {
      const hinhAnhPayload = {
        idSanPhamChiTiet: idChiTiet,          
        laAnhChinh: true,                     
        duongDanAnh: fileUploadData.value.name, 
        trangThai: 1
      };
      await axios.post('http://localhost:8080/api/hinh-anh-sp', hinhAnhPayload);
    }

    isModalOpen.value = false;
    fileUploadData.value = null;
    alert("Lưu thông tin biến thể và hình ảnh thành công! 🎉");
    fetchDataBienThe(); 

  } catch (error) {
    console.error("Lỗi lưu dữ liệu tổ hợp:", error);
    alert("Thao tác thất bại! Vui lòng kiểm tra lại kết nối dữ liệu.");
  }
};

const exportToExcelConfirm = () => {
  if (danhSachBienThe.value.length === 0) return alert("Không có dữ liệu biến thể để xuất Excel!");
  
  if (!confirm("📊 Bạn có chắc chắn muốn xuất toàn bộ dữ liệu biến thể của sản phẩm này ra file Excel không?")) {
    return;
  }

  const dataExport = danhSachBienThe.value.map((item, idx) => ({
    "STT": idx + 1,
    "Mã sản phẩm cha": productInfo.value.ma,
    "Tên sản phẩm cha": productInfo.value.ten,
    "Mã SKU biến thể": item.maCtsp,
    "Màu sắc": item.tenMauSac || 'Mặc định',
    "Trọng lượng": item.tenTrongLuong || 'Mặc định',
    "Độ cứng thân": item.tenDoCung || 'Mặc định',
    "Điểm cân bằng": item.tenDiemCanBang || 'Mặc định',
    "Chu vi cán": item.tenChuViCanVot || 'Mặc định',
    "Số lượng tồn": item.soLuong || 0,
    "Giá bán lẻ (VNĐ)": item.giaBan || 0,
    "Trạng thái": item.trangThai === 1 ? "Đang hoạt động" : "Ngừng hoạt động"
  }));

  const worksheet = XLSX.utils.json_to_sheet(dataExport);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, "Danh sách biến thể");
  XLSX.writeFile(workbook, `BienThe_${productInfo.value.ma || 'SP'}.xlsx`);
};

const quayLaiDanhSachSanPham = () => { router.push('/san-pham'); };

onMounted(() => {
  fetchDataBienThe();
  loadMasterDataCombobox();
});
</script>

<style scoped>
.breadcrumb-container { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #333; }
.btn-toggle-filter { background-color: #f8fafc; border: 1px solid #cbd5e1; color: #475569; padding: 6px 12px; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 500; transition: all 0.2s; }
.btn-toggle-filter:hover { background-color: #e2e8f0; }
.btn-back-to-product { background-color: #fff; border: 1px solid #f97316; color: #f97316; padding: 6px 14px; border-radius: 4px; font-size: 13px; font-weight: 600; cursor: pointer; transition: all 0.2s ease; }
.btn-back-to-product:hover { background-color: #f97316; color: #fff; }
.box-alert-info { background-color: #fff7ed; border-left: 4px solid #f97316; padding: 12px 15px; font-size: 14px; margin-bottom: 20px; color: #7c2d12; border-radius: 0 4px 4px 0; }
.filter-section-container { background: #fff; border: 1px solid #fed7aa; border-radius: 6px; padding: 15px; margin-bottom: 20px; }
.filter-title { font-size: 14px; font-weight: bold; color: #c2410c; margin-bottom: 12px; text-transform: uppercase; }
.filter-grid { display: flex; gap: 15px; align-items: flex-end; flex-wrap: wrap; }
.filter-item { display: flex; flex-direction: column; gap: 6px; text-align: left; flex: 1; min-width: 200px; }
.filter-item label { font-size: 12px; font-weight: 600; color: #4a5568; }
.filter-item input, .filter-item select { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; outline: none; box-sizing: border-box; width: 100%; }
.filter-buttons-group { display: flex; gap: 10px; }
.btn-trigger-filter { background: #f97316; color: white; border: none; padding: 8px 18px; border-radius: 4px; font-size: 13px; font-weight: 600; cursor: pointer; }
.btn-trigger-filter:hover { background: #ea580c; }
.btn-reset-filter { background: #f3f4f6; color: #4b5563; border: 1px solid #d1d5db; padding: 8px 16px; border-radius: 4px; font-size: 13px; cursor: pointer; }
.data-table-container { background-color: #fff; border: 1px solid #fed7aa; border-radius: 4px; padding: 20px; }
.table-header-flex { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #f3f4f6; }
.table-summary-title { font-size: 16px; font-weight: bold; color: #1e293b; margin: 0; }
.btn-add-new { background-color: #1e293b; color: #fff; border: none; padding: 8px 16px; border-radius: 6px; font-size: 13px; font-weight: 600; cursor: pointer; }
.btn-add-new:hover { background-color: #0f172a; }
.btn-action-excel { background: #16a34a; color: white; border: none; padding: 8px 16px; border-radius: 6px; cursor: pointer; font-size: 13px; font-weight: 600; }
.btn-action-excel:hover { background: #15803d; }
.custom-data-table { width: 100%; border-collapse: collapse; text-align: left; }
.custom-data-table th { background-color: #f97316; color: #fff; padding: 10px 12px; font-size: 13px; font-weight: 600; border: 1px solid #fed7aa; }
.custom-data-table td { padding: 12px; border-bottom: 1px solid #fed7aa; font-size: 13px; color: #334155; }
.product-code-text { font-weight: 600; color: #c2410c; }

/* Style Text Badge Tĩnh ở cột Trạng thái */
.badge-status-text { padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; display: inline-block; }
.badge-status-text.status-active { background-color: #f0fdf4; color: #16a34a; border: 1px solid #bbf7d0; }
.badge-status-text.status-stopped { background-color: #fef2f2; color: #ef4444; border: 1px solid #fecaca; }

/* Công tắc gạt xóa mềm nằm trong cột hành động */
.status-toggle-container { display: inline-flex; align-items: center; cursor: pointer; user-select: none; }
.toggle-track { position: relative; display: inline-block; width: 36px; height: 18px; border-radius: 999px; transition: background-color 0.2s ease; }
.track-active { background-color: #22c55e; }
.track-inactive { background-color: #cbd5e1; }
.toggle-handle { position: absolute; top: 3px; left: 3px; width: 12px; height: 12px; background-color: #fff; border-radius: 50%; transition: transform 0.2s ease; }
.track-active .toggle-handle { transform: translateX(18px); }

.action-buttons-cell { display: flex; gap: 12px; justify-content: center; align-items: center; }
.btn-action-edit { background: #fff7ed; border: 1px solid #f97316; color: #f97316; padding: 4px 10px; border-radius: 4px; font-size: 12px; font-weight: 600; cursor: pointer; }
.empty-table-row { text-align: center; padding: 30px !important; color: #94a3b8; }

.custom-pagination-wrapper { display: flex; align-items: center; gap: 10px; margin-top: 15px; justify-content: flex-start; }
.page-arrow-btn { width: 32px; height: 32px; background: #fff; border: 1px solid #e5e7eb; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 14px; color: #4b5563; cursor: pointer; transition: 0.2s; }
.page-arrow-btn:hover:not(:disabled) { background: #f3f4f6; border-color: #cbd5e1; }
.page-arrow-btn:disabled { background: #f9fafb; color: #d1d5db; cursor: not-allowed; border-color: #f3f4f6; }
.page-text-indicator { font-size: 13px; font-weight: 500; color: #374151; padding: 0 4px; }

.custom-modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.4); display: flex; justify-content: center; align-items: center; z-index: 9999; }
.custom-modal-box { background: white; border-radius: 6px; width: 580px; max-width: 95%; border-top: 5px solid #f97316; display: flex; flex-direction: column; }
.modal-box-header { padding: 15px; border-bottom: 1px solid #e5e7eb; display: flex; justify-content: space-between; align-items: center; }
.modal-box-header h3 { margin: 0; font-size: 16px; color: #c2410c; }
.btn-close-x { background: none; border: none; font-size: 24px; cursor: pointer; color: #94a3b8; }
.modal-box-body { padding: 20px; display: flex; flex-direction: column; gap: 15px; max-height: 70vh; overflow-y: auto; }
.form-group-item { display: flex; flex-direction: column; gap: 6px; text-align: left; }
.form-group-item label { font-size: 13px; font-weight: 600; color: #374151; }
.form-group-item input, .form-group-item select { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; width: 100%; box-sizing: border-box; outline: none; }
.form-group-item input:focus, .form-group-item select:focus { border-color: #f97316; }
.disabled-input { background: #f3f4f6; cursor: not-allowed; color: #9ca3af; font-weight: bold; }
.form-row-flex { display: flex; gap: 15px; }
.form-row-flex .form-group-item { flex: 1; }
.modal-box-footer { padding: 15px; border-top: 1px solid #e5e7eb; display: flex; justify-content: flex-end; gap: 10px; }
.btn-modal-cancel { background: #f3f4f6; border: 1px solid #d1d5db; padding: 8px 16px; border-radius: 4px; cursor: pointer; font-size: 13px; }
.btn-modal-submit { background: #f97316; color: white; border: none; padding: 8px 20px; border-radius: 4px; cursor: pointer; font-weight: 600; font-size: 13px; }
.btn-modal-submit:hover { background: #ea580c; }
.variant-img-thumb { width: 45px; height: 45px; object-fit: cover; border-radius: 4px; border: 1px solid #fed7aa; }
.image-upload-section { display: flex; gap: 15px; align-items: center; background-color: #fff7ed; padding: 12px; border-radius: 6px; border: 1px dashed #f97316; }
.image-preview-box { width: 90px; height: 90px; border-radius: 4px; background-color: #fff; border: 1px solid #cbd5e1; overflow: hidden; display: flex; align-items: center; justify-content: center; }
.image-preview-box img { width: 100%; height: 100%; object-fit: cover; }
.badge-prop { padding: 2px 6px; border-radius: 4px; font-weight: 500; font-size: 11px; }
.badge-prop.cyan { background: #ecfeff; color: #0891b2; border: 1px solid #c5f6fa; }
.badge-prop.orange { background: #fff7ed; color: #ea580c; border: 1px solid #ffedd5; }
.required-star { color: #ef4444; font-weight: bold; }
</style>