<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <div class="breadcrumb-text">
        <strong>Quản lý sản phẩm</strong> / Danh sách sản phẩm hệ thống
      </div>
      <button class="btn-toggle-filter" @click="isFilterVisible = !isFilterVisible">
        {{ isFilterVisible ? '🙈 Ẩn bộ lọc' : '🔍 Hiện bộ lọc' }}
      </button>
    </div>

    <div class="filter-container" v-if="isFilterVisible">
      <div class="section-title">🔍 Bộ lọc tìm kiếm nâng cao</div>
      <div class="filter-grid">
        <div class="filter-item">
          <label>Tìm kiếm</label>
          <input
            type="text"
            v-model="filterForm.keyword"
            placeholder="Mã hoặc tên sản phẩm..."
            @input="phatSinhTimKiem"
          />
        </div>

        <div class="filter-item slider-item">
          <label>Khoảng giá: 0đ - {{ formatTienTe(filterForm.giaMax) }}</label>
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

        <div class="filter-item" style="max-width: 140px;">
          <label>Số lượng tồn tối thiểu</label>
          <input 
            type="number" 
            v-model.number="filterForm.soLuongMin" 
            placeholder="Ví dụ: 0, 10..." 
            min="0"
            @input="phatSinhTimKiem"
          />
        </div>

        <div class="filter-item">
          <label>Thương hiệu</label>
          <select v-model="filterForm.idThuongHieu" @change="phatSinhTimKiem">
            <option value="">-- Tất cả --</option>
            <option v-for="th in danhSachThuocTinhMaster.thuongHieu" :key="th.id" :value="th.id">
              {{ th.tenThuongHieu }}
            </option>
          </select>
        </div>

        <div class="filter-item">
          <label>Xuất xứ</label>
          <select v-model="filterForm.idXuatXu" @change="phatSinhTimKiem">
            <option value="">-- Tất cả --</option>
            <option v-for="xx in danhSachThuocTinhMaster.xuatXu" :key="xx.id" :value="xx.id">
              {{ xx.tenXuatXu }}
            </option>
          </select>
        </div>

        <div class="filter-item">
          <label>Trạng thái</label>
          <select v-model="filterForm.trangThai" @change="phatSinhTimKiem">
            <option value="">-- Tất cả --</option>
            <option value="1">Đang kinh doanh</option>
            <option value="0">Ngừng kinh doanh</option>
          </select>
        </div>

        <div class="filter-item filter-actions">
          <button class="btn-filter-clear" @click="lamMoiBoLoc">
            🧹 Xóa bộ lọc
          </button>
        </div>
      </div>
    </div>

    <div class="data-table-container">
      <div class="table-header-row">
        <div class="table-summary-title">Danh sách sản phẩm hệ thống</div>
        <div class="header-actions">
          <button class="btn-action-excel" @click="exportToExcel">
            Xuất Excel
          </button>
          <button class="btn-action-solid" @click="moModalThemMoi">
            ➕ Thêm sản phẩm mới
          </button>
        </div>
      </div>

      <table class="custom-data-table">
        <thead>
          <tr>
            <th style="width: 50px; text-align: center;">STT</th>
            <th>Mã sản phẩm</th>
            <th>Tên sản phẩm</th>
            <th>Thương hiệu</th>
            <th>Xuất xứ</th>
            <th>Khoảng giá biến thể</th>
            <th style="text-align: center; width: 160px;">Trạng thái</th>
            <th style="text-align: center; width: 240px;">Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="filteredDanhSachSanPham.length === 0">
            <td colspan="8" class="empty-table-row">
              Không tìm thấy sản phẩm nào khớp với bộ lọc dữ liệu!
            </td>
          </tr>
          <tr v-for="(sp, index) in filteredDanhSachSanPham" :key="sp.id || index">
            <td style="text-align: center;">{{ (currentPage * pageSize) + index + 1 }}</td>
            <td><span class="product-code-text">{{ sp.maSanPham }}</span></td>
            <td>{{ sp.tenSanPham }}</td>
            <td>{{ sp.tenThuongHieu }}</td>
            <td>{{ sp.tenXuatXu }}</td>
            <td>
              <span style="font-weight: 600; color: #2563eb;">{{ hienThiKhoangGia(sp) }}</span>
            </td>
            
            <td style="text-align: center;">
              <span :class="['badge-status-text', sp.trangThai === 1 ? 'status-active' : 'status-stopped']">
                {{ sp.trangThai === 1 ? 'Đang kinh doanh' : 'Ngừng kinh doanh' }}
              </span>
            </td>
            
            <td style="text-align: center;">
              <div style="display: flex; gap: 12px; justify-content: center; align-items: center;">
                <button class="btn-action-view-variant" @click="xemChiTietBienThe(sp)">👁️ Biến thể</button>
                
                <div class="status-toggle-container" @click="toggleXoaMemSanPham(sp)" title="Gạt công tắc để thay đổi trạng thái kinh doanh nhanh">
                  <span :class="['toggle-track', sp.trangThai === 1 ? 'track-active' : 'track-inactive']">
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

    <div class="custom-modal-overlay" v-if="isModalOpen">
      <div class="custom-modal-box large-modal">
        <div class="modal-box-header">
          <h3>📦 Thêm sản phẩm & Sinh chuỗi biến thể tự động</h3>
          <button class="btn-close-x" @click="dongModalThemMoi">&times;</button>
        </div>
        <div class="modal-box-body">
          <div class="section-title">1. Thông tin sản phẩm chung</div>
          <div class="form-grid-row">
            <div class="form-group-item flex-1">
              <label>Mã sản phẩm (Tự động sinh) <span class="required-star">*</span></label>
              <input type="text" v-model="productForm.maSanPham" disabled placeholder="Hệ thống đang tự sinh mã..." class="disabled-input"/>
            </div>
            <div class="form-group-item flex-2">
              <label>Tên sản phẩm <span class="required-star">*</span></label>
              <input type="text" v-model="productForm.tenSanPham" placeholder="Nhập tên sản phẩm..."/>
            </div>
          </div>
          
          <div class="form-grid-row">
            <div class="form-group-item">
              <label>Thương hiệu <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input
                  type="text"
                  v-model="comboSearch.thuongHieu"
                  @focus="comboOpen.thuongHieu = true"
                  @blur="dongDropdownSauDelay('thuongHieu')"
                  placeholder="Gõ tìm thương hiệu..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.thuongHieu">
                  <li 
                    v-for="item in filteredThuongHieu" 
                    :key="item.id" 
                    @mousedown.prevent="selectValueGeneral(item, 'thuongHieu', 'idThuongHieu', 'tenThuongHieu')"
                  >
                    {{ item.tenThuongHieu }}
                  </li>
                </ul>
              </div>
            </div>
            <div class="form-group-item">
              <label>Xuất xứ <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.xuatXu" 
                  @focus="comboOpen.xuatXu = true" 
                  @blur="dongDropdownSauDelay('xuatXu')" 
                  placeholder="Gõ tìm xuất xứ..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.xuatXu">
                  <li 
                    v-for="item in filteredXuatXu" 
                    :key="item.id" 
                    @mousedown.prevent="selectValueGeneral(item, 'xuatXu', 'idXuatXu', 'tenXuatXu')"
                  >
                    {{ item.tenXuatXu }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
          
          <div class="form-group-item">
            <label>Mô tả chi tiết</label>
            <textarea v-model="productForm.moTa" rows="2" placeholder="Nhập đặc điểm nổi bật..."></textarea>
          </div>

          <hr class="form-divider"/>

          <div class="section-title">2. Chọn thuộc tính biến thể tổ hợp sản phẩm (Hỗ trợ chọn nhiều)</div>
          <div class="form-grid-row">
            <div class="form-group-item">
              <label>Màu sắc <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.mauSac" 
                  @focus="comboOpen.mauSac = true" 
                  @blur="dongDropdownSauDelay('mauSac')" 
                  placeholder="Gõ & click chọn nhiều màu..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.mauSac">
                  <li 
                    v-for="item in filteredMauSac" 
                    :key="item.id" 
                    @mousedown.prevent="themThuocTinhDaChon(item, 'mauSac', 'tenMauSac')"
                  >
                    🎨 {{ item.tenMauSac }}
                  </li>
                </ul>
              </div>
              <div class="selected-tags-container">
                <span v-for="(tag, idx) in selectedAttributes.mauSac" :key="tag.id" class="badge-prop orange">
                  {{ tag.name }} <span class="remove-tag-x" @click="xoaTagThuocTinh('mauSac', idx)">&times;</span>
                </span>
              </div>
            </div>

            <div class="form-group-item">
              <label>Trọng lượng <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.trongLuong" 
                  @focus="comboOpen.trongLuong = true" 
                  @blur="dongDropdownSauDelay('trongLuong')" 
                  placeholder="Gõ & click chọn nhiều trọng lượng..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.trongLuong">
                  <li 
                    v-for="item in filteredTrongLuong" 
                    :key="item.id" 
                    @mousedown.prevent="themThuocTinhDaChon(item, 'trongLuong', 'tenTrongLuong')"
                  >
                    ⚖️ {{ item.tenTrongLuong }}
                  </li>
                </ul>
              </div>
              <div class="selected-tags-container">
                <span v-for="(tag, idx) in selectedAttributes.trongLuong" :key="tag.id" class="badge-prop cyan">
                  {{ tag.name }} <span class="remove-tag-x" @click="xoaTagThuocTinh('trongLuong', idx)">&times;</span>
                </span>
              </div>
            </div>
          </div>

          <hr class="form-divider"/>

          <div class="section-title">3. Cấu hình thông số kỹ thuật chung hệ thống</div>
          <div class="form-grid-row">
            <div class="form-group-item">
              <label>Chu vi cán vợt <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.chuViCan" 
                  @focus="comboOpen.chuViCan = true" 
                  @blur="dongDropdownSauDelay('chuViCan')" 
                  placeholder="Gõ tìm chu vi cán..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.chuViCan">
                  <li v-for="item in danhSachChuViCanDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'chuViCan', 'idChuViCanVot', 'tenChuViCanVot')">
                    🏸 {{ item.tenChuViCanVot }}
                  </li>
                </ul>
              </div>
            </div>

            <div class="form-group-item">
              <label>Độ cứng thân vợt <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.doCung" 
                  @focus="comboOpen.doCung = true" 
                  @blur="dongDropdownSauDelay('doCung')" 
                  placeholder="Gõ tìm độ cứng..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.doCung">
                  <li v-for="item in danhSachDoCungDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'doCung', 'idDoCung', 'tenDoCung')">
                    {{ item.tenDoCung }}
                  </li>
                </ul>
              </div>
            </div>

            <div class="form-group-item">
              <label>Điểm cân bằng <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.diemCanBang" 
                  @focus="comboOpen.diemCanBang = true" 
                  @blur="dongDropdownSauDelay('diemCanBang')" 
                  placeholder="Gõ tìm điểm cân bằng..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.diemCanBang">
                  <li v-for="item in danhSachDiemCanBangDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'diemCanBang', 'idDiemCanBang', 'tenDiemCanBang')">
                    {{ item.tenDiemCanBang }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
          
          <div class="form-grid-row">
            <div class="form-group-item">
              <label>Chất liệu thân vợt <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.chatLieuThan" 
                  @focus="comboOpen.chatLieuThan = true" 
                  @blur="dongDropdownSauDelay('chatLieuThan')" 
                  placeholder="Gõ tìm chất liệu thân..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.chatLieuThan">
                  <li v-for="item in danhSachChatLieuThanDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'chatLieuThan', 'idChatLieuThanVot', 'tenChatLieuThanVot')">
                    🧪 {{ item.tenChatLieuThanVot }}
                  </li>
                </ul>
              </div>
            </div>

            <div class="form-group-item">
              <label>Chất liệu khung vợt <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.chatLieuKhung" 
                  @focus="comboOpen.chatLieuKhung = true" 
                  @blur="dongDropdownSauDelay('chatLieuKhung')" 
                  placeholder="Gõ tìm chất liệu khung..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.chatLieuKhung">
                  <li v-for="item in danhSachChatLieuKhungDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'chatLieuKhung', 'idChatLieuKhungVot', 'tenChatLieuKhungVot')">
                    🛡️ {{ item.tenChatLieuKhungVot }}
                  </li>
                </ul>
              </div>
            </div>

            <div class="form-group-item">
              <label>Danh mục sản phẩm <span class="required-star">*</span></label>
              <div class="combobox-wrapper">
                <input 
                  type="text" 
                  v-model="comboSearch.danhMuc" 
                  @focus="comboOpen.danhMuc = true" 
                  @blur="dongDropdownSauDelay('danhMuc')" 
                  placeholder="Gõ tìm danh mục..."
                />
                <ul class="combobox-dropdown" v-if="comboOpen.danhMuc">
                  <li v-for="item in danhSachDanhMucDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'danhMuc', 'idDanhMuc', 'tenDanhMuc')">
                    📦 {{ item.tenDanhMuc }}
                  </li>
                </ul>
              </div>
            </div>
          </div>
          
          <div style="text-align: right; margin-top: 5px;">
            <button class="btn-generate" @click="taoDanhSachBienTheDong">⚙️ Nhân bản chuỗi biến thể tổ hợp</button>
          </div>

          <div v-if="productForm.bienThes.length > 0" style="margin-top: 10px;">
            <table class="variant-dynamic-table">
              <thead>
                <tr>
                  <th>Màu sắc</th>
                  <th>Trọng lượng</th>
                  <th>Chu vi cán</th>
                  <th style="width: 140px;">Giá nhập (VNĐ)</th>
                  <th style="width: 140px;">Giá bán (VNĐ)</th>
                  <th style="width: 100px;">Số lượng kho</th>
                  <th>Hình ảnh file</th>
                  <th style="width: 60px; text-align: center;">Xem ảnh</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, idx) in productForm.bienThes" :key="idx">
                  <td><span class="badge-prop orange">{{ item.tenMauSac }}</span></td>
                  <td><span class="badge-prop cyan">{{ item.tenTrongLuong }}</span></td>
                  <td><span class="badge-prop cyan">{{ item.tenChuViCanVot }}</span></td>
                  <td><input type="text" class="table-input" :value="dinhDangSoNgoaiUI(item.giaNhap)" @input="capNhatGiaTriSo($event, idx, 'giaNhap')"/></td>
                  <td><input type="text" class="table-input" :value="dinhDangSoNgoaiUI(item.giaBan)" @input="capNhatGiaTriSo($event, idx, 'giaBan')"/></td>
                  <td><input type="text" class="table-input" :value="dinhDangSoNgoaiUI(item.soLuong)" @input="capNhatGiaTriSo($event, idx, 'soLuong')"/></td>
                  <td><input type="file" @change="handleVariantFileChange($event, idx)" accept="image/*" /></td>
                  <td>
                    <div class="variant-preview-img-box">
                      <img v-if="item.previewUrl" :src="item.previewUrl" alt="preview"/>
                      <span v-else style="font-size: 10px; color:#94a3b8">Trống</span>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="modal-box-footer">
          <button class="btn-modal-cancel" @click="dongModalThemMoi">Hủy bỏ</button>
          <button class="btn-modal-submit" @click="submitLuuToanBoSanPham">💾 Lưu toàn bộ hệ thống</button>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'; 
import axios from 'axios';
import { useRouter } from 'vue-router'; 
import MainLayout from '../layouts/MainLayout.vue';
import * as XLSX from 'xlsx';

const router = useRouter();

const isFilterVisible = ref(true);
const isModalOpen = ref(false);

const currentPage = ref(0);
const pageSize = ref(5); 
const totalPages = ref(1);

const danhSachSanPham = ref([]);
const danhSachThuocTinhMaster = ref({ thuongHieu: [], xuatXu: [] });

const danhSachMauSacDB = ref([]);
const danhSachTrongLuongDB = ref([]);
const danhSachDoCungDB = ref([]);
const danhSachChatLieuThanDB = ref([]);
const danhSachChatLieuKhungDB = ref([]);
const danhSachDanhMucDB = ref([]);
const danhSachDiemCanBangDB = ref([]);
const danhSachChuViCanDB = ref([]); 

const selectedAttributes = ref({ mauSac: [], trongLuong: [] });

const filterForm = ref({
  keyword: '', idThuongHieu: '', idXuatXu: '', trangThai: '', giaMax: 50000000, soLuongMin: ''
});

const comboSearch = ref({
  thuongHieu: '', xuatXu: '', mauSac: '', trongLuong: '', doCung: '', diemCanBang: '', danhMuc: '', chatLieuThan: '', chatLieuKhung: '', chuViCan: ''
});

const comboOpen = ref({
  thuongHieu: false, xuatXu: false, mauSac: false, trongLuong: false, doCung: false, diemCanBang: false, danhMuc: false, chatLieuThan: false, chatLieuKhung: false, chuViCan: false
});

const globalVariantConfig = ref({ 
  idChuViCanVot: '', idDoCung: '', idDiemCanBang: '', idDanhMuc: '', idChatLieuThanVot: '', idChatLieuKhungVot: ''
});

const productForm = ref({ maSanPham: '', tenSanPham: '', idThuongHieu: '', idXuatXu: '', moTa: '', baoHanh: '12 tháng', trangThai: 1, bienThes: [] });

const formatTienTe = (value) => {
  if (value === null || value === undefined || isNaN(value)) return '0 đ';
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
};

const dinhDangSoNgoaiUI = (val) => {
  if (val === null || val === undefined || val === '') return '';
  const clean = String(val).replace(/\D/g, '');
  return clean ? clean.replace(/\B(?=(\d{3})+(?!\d))/g, '.') : '';
};

const capNhatGiaTriSo = (event, index, truongDuLieu) => {
  const chuoiSoThuanTuy = event.target.value.replace(/\./g, '');
  const giaTriSo = chuoiSoThuanTuy ? parseInt(chuoiSoThuanTuy, 10) : 0;
  productForm.value.bienThes[index][truongDuLieu] = isNaN(giaTriSo) ? 0 : giaTriSo;
  event.target.value = dinhDangSoNgoaiUI(productForm.value.bienThes[index][truongDuLieu]);
};

const dongDropdownSauDelay = (field) => { setTimeout(() => { comboOpen.value[field] = false; }, 200); };
const safeExtractArray = (res) => { return res && res.data ? (Array.isArray(res.data) ? res.data : (res.data.content || [])) : []; };

const layKhoangGiaCuaSanPham = (sp) => {
  const variants = sp.chiTietSanPhams || [];
  if (variants.length === 0) return { min: 0, max: 0, text: 'Chưa có giá' };
  const giaBans = variants.map(v => v.giaBan || 0);
  const minGia = Math.min(...giaBans);
  const maxGia = Math.max(...giaBans);
  return minGia === maxGia ? { min: minGia, max: maxGia, text: formatTienTe(minGia) } : { min: minGia, max: maxGia, text: `${formatTienTe(minGia)} - ${formatTienTe(maxGia)}` };
};

const hienThiKhoangGia = (sp) => layKhoangGiaCuaSanPham(sp).text;

const filteredThuongHieu = computed(() => danhSachThuocTinhMaster.value.thuongHieu.filter(i => (i.tenThuongHieu || '').toLowerCase().includes(comboSearch.value.thuongHieu.toLowerCase())));
const filteredXuatXu = computed(() => danhSachThuocTinhMaster.value.xuatXu.filter(i => (i.tenXuatXu || '').toLowerCase().includes(comboSearch.value.xuatXu.toLowerCase())));
const filteredMauSac = computed(() => danhSachMauSacDB.value.filter(i => (i.tenMauSac || '').toLowerCase().includes(comboSearch.value.mauSac.toLowerCase())));
const filteredTrongLuong = computed(() => danhSachTrongLuongDB.value.filter(i => (i.tenTrongLuong || '').toLowerCase().includes(comboSearch.value.trongLuong.toLowerCase())));

const selectValueGeneral = (item, searchKey, targetField, displayField) => { productForm.value[targetField] = item.id; comboSearch.value[searchKey] = item[displayField]; comboOpen.value[searchKey] = false; };
const selectValueGlobalConfig = (item, searchKey, targetField, displayField) => { globalVariantConfig.value[targetField] = item.id; comboSearch.value[searchKey] = item[displayField]; comboOpen.value[searchKey] = false; };
const themThuocTinhDaChon = (item, key, displayField) => { if (!selectedAttributes.value[key].some(x => x.id === item.id)) { selectedAttributes.value[key].push({ id: item.id, name: item[displayField] }); } comboSearch.value[key] = ''; };
const xoaTagThuocTinh = (key, index) => selectedAttributes.value[key].splice(index, 1);

const filteredDanhSachSanPham = computed(() => {
  return danhSachSanPham.value.map(sp => {
    const tongTonKhoCon = (sp.chiTietSanPhams || []).reduce((sum, item) => sum + (item.soLuong || 0), 0);
    return {
      ...sp,
      computedThuongHieuId: sp.idThuongHieu,
      computedXuatXuId: sp.idXuatXu,
      tenThuongHieu: sp.tenThuongHieu || 'Chưa rõ',
      tenXuatXu: sp.tenXuatXu || 'Chưa rõ',
      tongSoLuongTon: tongTonKhoCon
    };
  }).filter(sp => {
    const matchKeyword = !filterForm.value.keyword.trim() || 
      (sp.maSanPham || '').toLowerCase().includes(filterForm.value.keyword.trim().toLowerCase()) ||
      (sp.tenSanPham || '').toLowerCase().includes(filterForm.value.keyword.trim().toLowerCase());
    
    const matchThuongHieu = !filterForm.value.idThuongHieu || String(sp.computedThuongHieuId) === String(filterForm.value.idThuongHieu);
    const matchXuatXu = !filterForm.value.idXuatXu || String(sp.computedXuatXuId) === String(filterForm.value.idXuatXu);
    const matchTrangThai = filterForm.value.trangThai === '' || String(sp.trangThai) === String(filterForm.value.trangThai);

    const khoangGia = layKhoangGiaCuaSanPham(sp);
    const matchPrice = filterForm.value.giaMax === 50000000 || khoangGia.min <= filterForm.value.giaMax;
    const matchSoLuong = filterForm.value.soLuongMin === '' || sp.tongSoLuongTon >= parseInt(filterForm.value.soLuongMin, 10);

    return matchKeyword && matchThuongHieu && matchXuatXu && matchTrangThai && matchPrice && matchSoLuong;
  });
});

const loadToanBoDuLieu = async () => {
  try {
    const [sp, th, xx, ms, tl, dc, clt, clk, dm, dcb, cvc] = await Promise.all([
      axios.get('http://localhost:8080/api/san-pham/search', {
        params: {
          page: currentPage.value,
          size: pageSize.value,
          keyword: filterForm.value.keyword || null,
          idThuongHieu: filterForm.value.idThuongHieu || null,
          idXuatXu: filterForm.value.idXuatXu || null,
          trangThai: filterForm.value.trangThai !== '' ? filterForm.value.trangThai : null
        }
      }), 
      axios.get('http://localhost:8080/api/thuong-hieu/all'),   
      axios.get('http://localhost:8080/api/xuat-xu/all'), 
      axios.get('http://localhost:8080/api/mau-sac/all'),      
      axios.get('http://localhost:8080/api/trong-luong/all'), 
      axios.get('http://localhost:8080/api/do-cung/all'),      
      axios.get('http://localhost:8080/api/chat-lieu-than-vot/all'), 
      axios.get('http://localhost:8080/api/chat-lieu-khung-vot/all'),    
      axios.get('http://localhost:8080/api/danh-muc/all'), 
      axios.get('http://localhost:8080/api/diem-can-bang/all'),
      axios.get('http://localhost:8080/api/chu-vi-can-vot/active') 
    ]);

    if (sp.data && sp.data.content !== undefined) {
      danhSachSanPham.value = sp.data.content;
      totalPages.value = sp.data.totalPages || 1;
    } else {
      danhSachSanPham.value = safeExtractArray(sp);
      totalPages.value = 1;
    }

    danhSachThuocTinhMaster.value.thuongHieu = safeExtractArray(th); 
    danhSachThuocTinhMaster.value.xuatXu = safeExtractArray(xx);
    danhSachMauSacDB.value = safeExtractArray(ms); 
    danhSachTrongLuongDB.value = safeExtractArray(tl);
    danhSachDoCungDB.value = safeExtractArray(dc); 
    danhSachChatLieuThanDB.value = safeExtractArray(clt); 
    danhSachChatLieuKhungDB.value = safeExtractArray(clk); 
    danhSachDanhMucDB.value = safeExtractArray(dm);
    danhSachDiemCanBangDB.value = safeExtractArray(dcb); 
    danhSachChuViCanDB.value = safeExtractArray(cvc);
  } catch (error) { 
    console.error("Lỗi tải dữ liệu:", error); 
  }
};

const chuyenTrang = (page) => { currentPage.value = page; loadToanBoDuLieu(); };
const phatSinhTimKiem = () => { currentPage.value = 0; loadToanBoDuLieu(); };

// Thay thế chính xác hàm toggleXoaMemSanPham cũ bằng đoạn này:
const toggleXoaMemSanPham = async (sp) => {
  const productId = sp.id; 
  if (!productId) return alert("Không tìm thấy ID sản phẩm!");
  
  // 1. Tìm đúng vị trí của sản phẩm này trong MẢNG GỐC (.value) để ép Vue re-render giao diện
  const indexGoc = danhSachSanPham.value.findIndex(item => item.id === productId);
  if (indexGoc === -1) return;

  const trangThaiCu = danhSachSanPham.value[indexGoc].trangThai;
  const trangThaiMoi = trangThaiCu === 1 ? 0 : 1; 

  // 2. Thay đổi trực tiếp trên mảng gốc -> Công tắc gạt màu ngay lập tức (0.01 giây)
  danhSachSanPham.value[indexGoc].trangThai = trangThaiMoi;

  // 3. Gửi API chạy ngầm dưới database
  try {
    await axios.put(`http://localhost:8080/api/san-pham/${productId}/trang-thai`, null, {
      params: { trangThai: trangThaiMoi }
    });
    // Thành công âm thầm, không cần hiện alert thông báo gây phiền hà cho người dùng
  } catch (e) {
    // 4. Nếu API lỗi, hoàn tác (Rollback) lại trạng thái cũ trên mảng gốc
    danhSachSanPham.value[indexGoc].trangThai = trangThaiCu;
    alert("Hệ thống mất kết nối, không thể thay đổi trạng thái kinh doanh!");
  }
};

const taoDanhSachBienTheDong = () => {
  const attrs = selectedAttributes.value;
  const cfg = globalVariantConfig.value;
  if (attrs.mauSac.length === 0 || attrs.trongLuong.length === 0) return alert("Vui lòng chọn Màu sắc và Trọng lượng!");
  if (!cfg.idChuViCanVot || !cfg.idDoCung || !cfg.idDiemCanBang || !cfg.idDanhMuc || !cfg.idChatLieuThanVot || !cfg.idChatLieuKhungVot) {
    return alert("Vui lòng chọn đầy đủ Thông số kỹ thuật chung!");
  }
  const matrix = [];
  const chuViObj = danhSachChuViCanDB.value.find(c => c.id === cfg.idChuViCanVot);
  const tenChuVi = chuViObj ? chuViObj.tenChuViCanVot : 'Chưa rõ';

  attrs.mauSac.forEach((ms, indexMs) => {
    attrs.trongLuong.forEach((tl, indexTl) => {
      const codeAuto = `${productForm.value.maSanPham || 'SP'}-CT-${indexMs}${indexTl}`;
      matrix.push({
        idMauSac: ms.id, tenMauSac: ms.name,
        idTrongLuong: tl.id, tenTrongLuong: tl.name,
        idChuViCanVot: cfg.idChuViCanVot, tenChuViCanVot: tenChuVi,
        idDoCung: cfg.idDoCung, idDiemCanBang: cfg.idDiemCanBang, idDanhMuc: cfg.idDanhMuc,
        idChatLieuThanVot: cfg.idChatLieuThanVot, idChatLieuKhungVot: cfg.idChatLieuKhungVot,
        maCtsp: codeAuto, giaNhap: 800000, giaBan: 1200000, soLuong: 20, trangThai: 1, hinhAnh: '', previewUrl: ''
      });
    });
  });
  productForm.value.bienThes = matrix;
};

const handleVariantFileChange = (event, index) => {
  const file = event.target.files[0];
  if (!file) return;
  productForm.value.bienThes[index].file = file;
  productForm.value.bienThes[index].previewUrl = URL.createObjectURL(file); 
  productForm.value.bienThes[index].hinhAnh = file.name;
};

// ❓ XÁC NHẬN: Hộp thoại hỏi ý kiến chắc chắn trước khi lưu sản phẩm tổ hợp
const submitLuuToanBoSanPham = async () => {
  if (!productForm.value.maSanPham?.trim() || !productForm.value.tenSanPham?.trim()) return alert("Vui lòng nhập Mã và Tên sản phẩm!");
  if (!productForm.value.idThuongHieu || !productForm.value.idXuatXu) return alert("Vui lòng nhập Thương hiệu và Xuất xứ!");
  if (productForm.value.bienThes.length === 0) return alert("Vui lòng ấn nút sinh chuỗi biến thể!");

  if (!confirm(`Hệ thống ghi nhận hành động thêm mới.\nBạn có chắc chắn muốn lưu sản phẩm [ ${productForm.value.maSanPham} ] này không?`)) {
    return;
  }

  const formData = new FormData();
  try {
    const jsonPayload = JSON.stringify({
      idThuongHieu: productForm.value.idThuongHieu,
      idXuatXu: productForm.value.idXuatXu,
      maSanPham: productForm.value.maSanPham,
      tenSanPham: productForm.value.tenSanPham,
      moTa: productForm.value.moTa,
      baoHanh: productForm.value.baoHanh,
      trangThai: 1,
      chiTietSanPhams: productForm.value.bienThes.map(item => ({
        idMauSac: item.idMauSac, idTrongLuong: item.idTrongLuong, idChuViCanVot: item.idChuViCanVot, 
        idDoCung: item.idDoCung, idDiemCanBang: item.idDiemCanBang, idDanhMuc: item.idDanhMuc,
        idChatLieuThanVot: item.idChatLieuThanVot, idChatLieuKhungVot: item.idChatLieuKhungVot,
        maCtsp: item.maCtsp, giaNhap: item.giaNhap, giaBan: item.giaBan, soLuong: item.soLuong, trangThai: 1, hinhAnh: item.hinhAnh || 'default.jpg'
      }))
    });
    
    formData.append("data", jsonPayload);
    productForm.value.bienThes.forEach((item) => { if (item.file) formData.append("files", item.file); });
    
    await axios.post("http://localhost:8080/api/san-pham", formData, { headers: { "Content-Type": "multipart/form-data" } });
    alert("Thêm sản phẩm thành công!");
    dongModalThemMoi();
    loadToanBoDuLieu();
  } catch (error) {
    alert("Có lỗi xảy ra khi lưu dữ liệu!");
  }
};

const xemChiTietBienThe = (sp) => { 
  router.push({ path: '/bien-the', query: { idSP: sp.id, maSP: sp.maSanPham, tenSP: sp.tenSanPham } }); 
};

// 🤖 TỰ SINH MÃ TỊNH TIẾN: Thuật toán tự sinh mã dạng SP001, SP002... chuẩn chỉ khi mở form
const moModalThemMoi = () => {
  let soTiepTheo = 1;
  if (danhSachSanPham.value.length > 0) {
    const danhSachSo = danhSachSanPham.value.map(sp => {
      const phanSo = (sp.maSanPham || '').replace(/[^\d]/g, '');
      return phanSo ? parseInt(phanSo, 10) : 0;
    });
    const soLonNhat = Math.max(...danhSachSo, 0);
    soTiepTheo = soLonNhat + 1;
  }
  const maSanPhamTuSinh = 'SP' + String(soTiepTheo).padStart(3, '0');

  productForm.value = { maSanPham: maSanPhamTuSinh, tenSanPham: '', idThuongHieu: '', idXuatXu: '', moTa: '', baoHanh: '12 tháng', trangThai: 1, bienThes: [] };
  globalVariantConfig.value = { idChuViCanVot: '', idDoCung: '', idDiemCanBang: '', idDanhMuc: '', idChatLieuThanVot: '', idChatLieuKhungVot: '' };
  comboSearch.value = { thuongHieu: '', xuatXu: '', mauSac: '', trongLuong: '', doCung: '', diemCanBang: '', danhMuc: '', chatLieuThan: '', chatLieuKhung: '', chuViCan: '' };
  selectedAttributes.value = { mauSac: [], trongLuong: [] };
  isModalOpen.value = true;
};

const dongModalThemMoi = () => { isModalOpen.value = false; };
const lamMoiBoLoc = () => { filterForm.value = { keyword: '', idThuongHieu: '', idXuatXu: '', trangThai: '', giaMax: 50000000, soLuongMin: '' }; loadToanBoDuLieu(); };

// ❓ XÁC NHẬN: Hỏi ý kiến chắc chắn trước khi export file Excel dữ liệu sản phẩm
const exportToExcel = () => {
  if (filteredDanhSachSanPham.value.length === 0) return alert("Không có dữ liệu để xuất Excel!");
  
  if (!confirm("📊 Bạn có chắc chắn muốn xuất danh sách sản phẩm hiện tại ra file Excel không?")) {
    return;
  }

  const mapped = filteredDanhSachSanPham.value.map((sp, i) => ({
    "STT": i + 1, "Mã sản phẩm": sp.maSanPham, "Tên sản phẩm": sp.tenSanPham,
    "Thương hiệu": sp.tenThuongHieu, "Xuất xứ": sp.tenXuatXu, "Giá biến thể": layKhoangGiaCuaSanPham(sp).text,
    "Tổng số lượng tồn": sp.tongSoLuongTon, "Trạng thái": sp.trangThai === 1 ? "Đang kinh doanh" : "Ngừng kinh doanh"
  }));
  const ws = XLSX.utils.json_to_sheet(mapped);
  const wb = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(wb, ws, "Sản phẩm");
  XLSX.writeFile(wb, "Danh_sach_san_pham.xlsx");
};

onMounted(() => loadToanBoDuLieu());
</script>

<style scoped>
.breadcrumb-container { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #333; }
.btn-toggle-filter { background-color: #f8fafc; border: 1px solid #cbd5e1; color: #475569; padding: 6px 12px; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 500; }
.btn-toggle-filter:hover { background-color: #e2e8f0; }
.filter-container { background-color: #fff; border: 1px solid #fed7aa; border-radius: 6px; padding: 15px; margin-bottom: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
.filter-grid { display: flex; flex-wrap: wrap; gap: 15px; margin-top: 10px; align-items: flex-end; }
.filter-item { display: flex; flex-direction: column; gap: 6px; text-align: left; flex: 1; min-width: 160px; }
.filter-item label { font-size: 12px; font-weight: 600; color: #475569; }
.filter-item input, .filter-item select { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; outline: none; box-sizing: border-box; width: 100%; }
.filter-item input:focus, .filter-item select:focus { border-color: #f97316; }
.slider-item { min-width: 230px; }
.price-slider-wrapper { display: flex; align-items: center; width: 100%; height: 37px; }
.custom-slider { -webkit-appearance: none; width: 100%; height: 6px; background: #e2e8f0; border-radius: 5px; outline: none; padding: 0 !important; border: none !important; }
.custom-slider::-webkit-slider-thumb { -webkit-appearance: none; appearance: none; width: 18px; height: 18px; border-radius: 50%; background: #f97316; cursor: pointer; }
.filter-actions { flex: 0 0 auto; min-width: auto; }
.btn-filter-clear { background-color: #e2e8f0; color: #334155; border: 1px solid #cbd5e1; padding: 8px 16px; border-radius: 4px; font-size: 13px; font-weight: 600; cursor: pointer; }

.custom-pagination-wrapper { display: flex; align-items: center; gap: 10px; margin-top: 15px; justify-content: flex-start; }
.page-arrow-btn { width: 32px; height: 32px; background: #fff; border: 1px solid #e5e7eb; border-radius: 6px; display: flex; align-items: center; justify-content: center; font-size: 14px; color: #4b5563; cursor: pointer; transition: 0.2s; }
.page-arrow-btn:hover:not(:disabled) { background: #f3f4f6; border-color: #cbd5e1; }
.page-arrow-btn:disabled { background: #f9fafb; color: #d1d5db; cursor: not-allowed; border-color: #f3f4f6; }
.page-text-indicator { font-size: 13px; font-weight: 500; color: #374151; padding: 0 4px; }

.data-table-container { background-color: #fff; border: 1px solid #fed7aa; border-radius: 6px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
.table-header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #f3f4f6; }
.table-summary-title { font-size: 16px; font-weight: bold; color: #1e293b; margin: 0; }
.btn-action-solid { background-color: #1e293b; border: none; color: #fff; padding: 8px 16px; border-radius: 6px; font-weight: 600; cursor: pointer; font-size: 13px; }
.btn-action-solid:hover { background-color: #0f172a; }

.badge-status-text { padding: 4px 10px; border-radius: 6px; font-size: 12px; font-weight: 600; display: inline-block; }
.badge-status-text.status-active { background-color: #f0fdf4; color: #16a34a; border: 1px solid #bbf7d0; }
.badge-status-text.status-stopped { background-color: #fef2f2; color: #ef4444; border: 1px solid #fecaca; }

.combobox-wrapper { position: relative; width: 100%; }
.combobox-dropdown { position: absolute; top: 100%; left: 0; width: 100%; background: #ffffff; border: 1px solid #cbd5e1; border-radius: 4px; max-height: 180px; overflow-y: auto; z-index: 999; padding: 0; margin: 4px 0 0 0; list-style: none; }
.combobox-dropdown li { padding: 8px 12px; font-size: 13px; cursor: pointer; text-align: left; }
.combobox-dropdown li:hover { background-color: #fff7ed; color: #f97316; }
.selected-tags-container { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 6px; min-height: 24px; }
.remove-tag-x { margin-left: 6px; font-weight: bold; cursor: pointer; color: #ef4444; }
.custom-data-table { width: 100%; border-collapse: collapse; text-align: left; }
.custom-data-table th { background-color: #f97316; color: white; padding: 10px; font-size: 13px; border: 1px solid #fed7aa; }
.custom-data-table td { padding: 12px; border-bottom: 1px solid #fed7aa; font-size: 13px; }
.product-code-text { font-weight: bold; color: #c2410c; }
.btn-action-view-variant { background-color: #f0fdf4; color: #16a34a; border: 1px solid #16a34a; border-radius: 4px; padding: 5px 12px; cursor: pointer; font-size: 12px; font-weight: 600; }
.btn-action-view-variant:hover { background-color: #16a34a; color: white; }
.empty-table-row { text-align: center; color: #94a3b8; padding: 30px; }

.status-toggle-container { display: inline-flex; align-items: center; cursor: pointer; user-select: none; }
.toggle-track { width: 36px; height: 18px; border-radius: 999px; position: relative; display: inline-block; transition: background-color 0.2s; }
.track-active { background-color: #22c55e; }
.track-inactive { background-color: #cbd5e1; }
.toggle-handle { width: 12px; height: 12px; background-color: white; border-radius: 50%; position: absolute; top: 3px; left: 3px; transition: transform 0.2s; }
.track-active .toggle-handle { transform: translateX(18px); }

.custom-modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.4); display: flex; justify-content: center; align-items: center; z-index: 9999; }
.custom-modal-box.large-modal { width: 880px; max-width: 95%; border-top: 5px solid #f97316; background: #fff; border-radius: 6px; display: flex; flex-direction: column; }
.modal-box-header { padding: 15px; border-bottom: 1px solid #e5e7eb; display: flex; justify-content: space-between; align-items: center; margin-bottom: 0; }
.modal-box-header h3 { margin: 0; font-size: 15px; color: #c2410c; }
.btn-close-x { background: none; border: none; font-size: 24px; cursor: pointer; color: #9ca3af; }
.modal-box-body { padding: 20px; display: flex; flex-direction: column; gap: 15px; max-height: 75vh; overflow-y: auto; }
.section-title { font-size: 13px; font-weight: bold; color: #f97316; border-left: 3px solid #f97316; padding-left: 8px; text-align: left; }
.form-divider { border: 0; border-top: 1px dashed #cbd5e1; margin: 8px 0; }
.form-grid-row { display: flex; gap: 15px; }
.form-group-item { display: flex; flex-direction: column; gap: 6px; text-align: left; flex: 1; position: relative; }
.form-group-item label { font-size: 12px; font-weight: 600; color: #374151; }
.form-group-item input, .form-group-item select, .form-group-item textarea { padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 4px; font-size: 13px; outline: none; width: 100%; box-sizing: border-box; }
.disabled-input { background: #f3f4f6; color: #9ca3af; cursor: not-allowed; font-weight: bold; }
.btn-generate { background-color: #1e293b; color: white; border: none; padding: 8px 16px; font-size: 12px; border-radius: 4px; cursor: pointer; font-weight: 600; }
.variant-dynamic-table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 12px; text-align: left; }
.variant-dynamic-table th { background-color: #f8fafc; color: #475569; padding: 8px; border: 1px solid #e2e8f0; }
.variant-dynamic-table td { padding: 6px; border: 1px solid #e2e8f0; vertical-align: middle; }
.table-input { padding: 6px !important; font-size: 12px !important; border-radius: 4px; border: 1px solid #cbd5e1; width: 100%; box-sizing: border-box; }
.variant-preview-img-box { width: 45px; height: 45px; border: 1px dashed #cbd5e1; border-radius: 4px; overflow: hidden; background: #f8fafc; display: flex; justify-content: center; align-items: center; margin: 0 auto; }
.variant-preview-img-box img { width: 100%; height: 100%; object-fit: cover; }
.badge-prop { padding: 2px 6px; border-radius: 4px; font-weight: 500; font-size: 11px; }
.badge-prop.cyan { background: #ecfeff; color: #0891b2; border: 1px solid #c5f6fa; }
.badge-prop.orange { background: #fff7ed; color: #ea580c; border: 1px solid #ffedd5; }
.modal-box-footer { padding: 15px; border-top: 1px solid #e5e7eb; display: flex; justify-content: flex-end; gap: 10px; }
.btn-modal-cancel { background: #f3f4f6; color: #4b5563; border: 1px solid #d1d5db; padding: 8px 16px; border-radius: 4px; cursor: pointer; font-size: 13px; }
.btn-modal-submit { background: #f97316; color: white; border: none; padding: 8px 20px; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 600; }
.header-actions { display: flex; align-items: center; gap: 10px; }
.btn-action-excel { background: #16a34a; color: white; border: none; padding: 8px 16px; border-radius: 6px; cursor: pointer; font-size: 13px; font-weight: 600; }
.btn-action-excel:hover { background: #15803d; }
</style>