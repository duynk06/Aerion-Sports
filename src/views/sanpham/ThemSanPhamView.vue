<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <div class="breadcrumb-text">
        <strong>Quản lý sản phẩm</strong> / Thêm sản phẩm & Sinh chuỗi biến thể tự động
      </div>
    </div>

    <div class="form-container-box">
      <div class="form-box-body">
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
                <li v-for="item in filteredThuongHieu" :key="item.id" @mousedown.prevent="selectValueGeneral(item, 'thuongHieu', 'idThuongHieu', 'tenThuongHieu')">
                  {{ item.tenThuongHieu || item.ten }}
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
                <li v-for="item in filteredXuatXu" :key="item.id" @mousedown.prevent="selectValueGeneral(item, 'xuatXu', 'idXuatXu', 'tenXuatXu')">
                  {{ item.tenXuatXu || item.ten }}
                </li>
              </ul>
            </div>
          </div>
        </div>
        
        <div class="form-group-item">
          <label>Mô tả chi tiết</label>
          <textarea v-model="productForm.moTa" rows="3" placeholder="Nhập đặc điểm nổi bật..."></textarea>
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
                  :class="{ 'is-selected': isKiemTraDaChon('mauSac', item.id) }"
                  @mousedown.prevent="themThuocTinhDaChon(item, 'mauSac', item.tenMauSac ? 'tenMauSac' : 'ten')"
                >
                  <span>{{ item.tenMauSac || item.ten }}</span>
                  <i v-if="isKiemTraDaChon('mauSac', item.id)" class="fa-solid fa-check check-icon"></i>
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
                  :class="{ 'is-selected': isKiemTraDaChon('trongLuong', item.id) }"
                  @mousedown.prevent="themThuocTinhDaChon(item, 'trongLuong', item.tenTrongLuong ? 'tenTrongLuong' : 'ten')"
                >
                  <span>{{ item.tenTrongLuong || item.ten }}</span>
                  <i v-if="isKiemTraDaChon('trongLuong', item.id)" class="fa-solid fa-check check-icon"></i>
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
              <input type="text" v-model="comboSearch.chuViCan" @focus="comboOpen.chuViCan = true" @blur="dongDropdownSauDelay('chuViCan')" placeholder="Gõ tìm chu vi cán..."/>
              <ul class="combobox-dropdown" v-if="comboOpen.chuViCan">
                <li v-for="item in danhSachChuViCanDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'chuViCan', 'idChuViCanVot', item.tenChuViCanVot ? 'tenChuViCanVot' : 'ten')">
                   {{ item.tenChuViCanVot || item.ten }}
                </li>
              </ul>
            </div>
          </div>

          <div class="form-group-item">
            <label>Độ cứng thân vợt <span class="required-star">*</span></label>
            <div class="combobox-wrapper">
              <input type="text" v-model="comboSearch.doCung" @focus="comboOpen.doCung = true" @blur="dongDropdownSauDelay('doCung')" placeholder="Gõ tìm độ cứng..."/>
              <ul class="combobox-dropdown" v-if="comboOpen.doCung">
                <li v-for="item in danhSachDoCungDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'doCung', 'idDoCung', item.tenDoCung ? 'tenDoCung' : 'ten')">
                  {{ item.tenDoCung || item.ten }}
                </li>
              </ul>
            </div>
          </div>

          <div class="form-group-item">
            <label>Điểm cân bằng <span class="required-star">*</span></label>
            <div class="combobox-wrapper">
              <input type="text" v-model="comboSearch.diemCanBang" @focus="comboOpen.diemCanBang = true" @blur="dongDropdownSauDelay('diemCanBang')" placeholder="Gõ tìm điểm cân bằng..."/>
              <ul class="combobox-dropdown" v-if="comboOpen.diemCanBang">
                <li v-for="item in danhSachDiemCanBangDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'diemCanBang', 'idDiemCanBang', item.tenDiemCanBang ? 'tenDiemCanBang' : 'ten')">
                  {{ item.tenDiemCanBang || item.ten }}
                </li>
              </ul>
            </div>
          </div>
        </div>
        
        <div class="form-grid-row">
          <div class="form-group-item">
            <label>Chất liệu thân vợt <span class="required-star">*</span></label>
            <div class="combobox-wrapper">
              <input type="text" v-model="comboSearch.chatLieuThan" @focus="comboOpen.chatLieuThan = true" @blur="dongDropdownSauDelay('chatLieuThan')" placeholder="Gõ tìm chất liệu thân..."/>
              <ul class="combobox-dropdown" v-if="comboOpen.chatLieuThan">
                <li v-for="item in danhSachChatLieuThanDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'chatLieuThan', 'idChatLieuThanVot', item.tenChatLieuThanVot ? 'tenChatLieuThanVot' : 'ten')">
                   {{ item.tenChatLieuThanVot || item.ten }}
                </li>
              </ul>
            </div>
          </div>

          <div class="form-group-item">
            <label>Chất liệu khung vợt <span class="required-star">*</span></label>
            <div class="combobox-wrapper">
              <input type="text" v-model="comboSearch.chatLieuKhung" @focus="comboOpen.chatLieuKhung = true" @blur="dongDropdownSauDelay('chatLieuKhung')" placeholder="Gõ tìm chất liệu khung..."/>
              <ul class="combobox-dropdown" v-if="comboOpen.chatLieuKhung">
                <li v-for="item in danhSachChatLieuKhungDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'chatLieuKhung', 'idChatLieuKhungVot', item.tenChatLieuKhungVot ? 'tenChatLieuKhungVot' : 'ten')">
                   {{ item.tenChatLieuKhungVot || item.ten }}
                </li>
              </ul>
            </div>
          </div>

          <div class="form-group-item">
            <label>Danh mục sản phẩm <span class="required-star">*</span></label>
            <div class="combobox-wrapper">
              <input type="text" v-model="comboSearch.danhMuc" @focus="comboOpen.danhMuc = true" @blur="dongDropdownSauDelay('danhMuc')" placeholder="Gõ tìm danh mục..."/>
              <ul class="combobox-dropdown" v-if="comboOpen.danhMuc">
                <li v-for="item in danhSachDanhMucDB" :key="item.id" @mousedown.prevent="selectValueGlobalConfig(item, 'danhMuc', 'idDanhMuc', item.tenDanhMuc ? 'tenDanhMuc' : 'ten')">
                  {{ item.tenDanhMuc || item.ten }}
                </li>
              </ul>
            </div>
          </div>
        </div>
        
        <div style="text-align: right; margin-top: 15px; display: flex; justify-content: flex-end; gap: 10px;">
          <button v-if="isBienTheGenerated && productForm.bienThes.length > 0" class="btn-bulk-toggle" @click="isBulkPanelOpen = !isBulkPanelOpen">
            ⚡ {{ isBulkPanelOpen ? 'Đóng bảng điền nhanh' : 'Mở bảng điền nhanh' }}
          </button>
          <button class="btn-generate" @click="taoDanhSachBienTheDong">Tạo danh sách biến thể</button>
        </div>

        <div v-if="isBienTheGenerated && productForm.bienThes.length > 0 && isBulkPanelOpen" class="bulk-fill-container">
          <div class="bulk-fill-title"> Áp dụng thông số chung </div>
          <div class="bulk-fill-grid">
            <div class="bulk-item">
              <label>Giá nhập chung</label>
              <input type="text" :value="dinhDangSoNgoaiUI(bulkInput.giaNhap)" @input="bulkInput.giaNhap = $event.target.value.replace(/\./g, '')" placeholder="Nhập giá nhập..." />
            </div>
            <div class="bulk-item">
              <label>Giá bán chung</label>
              <input type="text" :value="dinhDangSoNgoaiUI(bulkInput.giaBan)" @input="bulkInput.giaBan = $event.target.value.replace(/\./g, '')" placeholder="Nhập giá bán..." />
            </div>
            <div class="bulk-item">
              <label>Số lượng chung</label>
              <input type="number" v-model.number="bulkInput.soLuong" min="0" placeholder="Nhập số lượng..." />
            </div>
            <button class="btn-bulk-apply" @click="apDungChoBienTheDaChon">Áp dụng cho dòng đã tích</button>
          </div>
        </div>

        <div v-if="isBienTheGenerated && productForm.bienThes.length > 0" style="margin-top: 20px;" class="tables-group-wrapper">
          
          <div v-for="colorGroup in danhSachBienTheTheoMau" :key="colorGroup.idMauSac" class="color-table-section">
            <div class="color-table-header">
              <span class="badge-prop orange" style="font-size: 13.5px; padding: 6px 12px; font-weight: bold;">
                Danh sách biến thể - Màu: {{ colorGroup.tenMauSac }}
              </span>
            </div>

            <table class="variant-dynamic-table">
              <thead>
                <tr>
                  <th style="width: 40px; text-align: center;">
                    <input 
                      type="checkbox" 
                      :checked="isSelectAllByColor(colorGroup.idMauSac)" 
                      @change="toggleSelectAllRowsByColor($event, colorGroup.idMauSac)" 
                      style="cursor: pointer;" 
                    />
                  </th>
                  <th>Màu sắc</th>
                  <th>Trọng lượng</th>
                  <th>Chu vi cán</th>
                  <th style="width: 150px;">Giá nhập (VNĐ)</th>
                  <th style="width: 150px;">Giá bán (VNĐ)</th>
                  <th style="width: 120px;">Số lượng kho</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in colorGroup.items" :key="item.globalIndex" :class="{ 'row-checked': selectedRowIndexes.includes(item.globalIndex) }">
                  <td style="text-align: center;">
                    <input type="checkbox" :value="item.globalIndex" v-model="selectedRowIndexes" style="cursor: pointer;" />
                  </td>
                  <td><span class="badge-prop orange">{{ item.tenMauSac }}</span></td>
                  <td><span class="badge-prop cyan">{{ item.tenTrongLuong }}</span></td>
                  <td><span class="badge-prop cyan">{{ item.tenChuViCanVot }}</span></td>
                  <td><input type="text" class="table-input" :value="dinhDangSoNgoaiUI(item.giaNhap)" @input="capNhatGiaTriSoTrucTiep($event, item.globalIndex, 'giaNhap')"/></td>
                  <td><input type="text" class="table-input" :value="dinhDangSoNgoaiUI(item.giaBan)" @input="capNhatGiaTriSoTrucTiep($event, item.globalIndex, 'giaBan')"/></td>
                  <td><input type="text" class="table-input" :value="dinhDangSoNgoaiUI(item.soLuong)" @input="capNhatGiaTriSoTrucTiep($event, item.globalIndex, 'soLuong')"/></td>
                </tr>
              </tbody>
            </table>
          </div>

        </div>

        <div v-if="isBienTheGenerated && listMauSacDaChonQuetDuoc.length > 0" style="margin-top: 25px;">
          <div class="section-title">4. Quản lý hình ảnh đại diện theo màu sắc</div>
          <div class="color-image-grid">
            <div v-for="color in listMauSacDaChonQuetDuoc" :key="color.id" class="color-image-card">
              <span class="badge-prop orange" style="font-size: 13px; font-weight: 600;">Màu: {{ color.name }}</span>
              <div class="color-preview-box">
                <img v-if="colorMapFiles[color.id]?.previewUrl" :src="colorMapFiles[color.id].previewUrl" alt="preview"/>
                <span v-else style="font-size: 12px; color: #94a3b8;">Chưa có ảnh</span>
              </div>
              <input type="file" @change="handleColorFileChange($event, color.id)" accept="image/*" style="font-size: 12px; width: 100%;" />
            </div>
          </div>
        </div>

      </div>
      <div class="form-box-footer">
        <button class="btn-modal-cancel" @click="quayLaiDanhSach">Hủy bỏ</button>
        <button class="btn-modal-submit" @click="submitLuuToanBoSanPham"> Lưu toàn bộ hệ thống</button>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'; 
import axios from 'axios';
import { useRouter } from 'vue-router'; 
import MainLayout from '@/layouts/MainLayout.vue';

const router = useRouter();

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
const comboSearch = ref({ thuongHieu: '', xuatXu: '', mauSac: '', trongLuong: '', doCung: '', diemCanBang: '', danhMuc: '', chatLieuThan: '', chatLieuKhung: '', chuViCan: '' });
const comboOpen = ref({ thuongHieu: false, xuatXu: false, mauSac: false, trongLuong: false, doCung: false, diemCanBang: false, danhMuc: false, chatLieuThan: false, chatLieuKhung: false, chuViCan: false });
const globalVariantConfig = ref({ idChuViCanVot: '', idDoCung: '', idDiemCanBang: '', idDanhMuc: '', idChatLieuThanVot: '', idChatLieuKhungVot: '' });
const productForm = ref({ maSanPham: '', tenSanPham: '', idThuongHieu: '', idXuatXu: '', moTa: '', baoHanh: '12 tháng', trangThai: 1, bienThes: [] });
const colorMapFiles = ref({});

const isBienTheGenerated = ref(false);
const bulkInput = ref({ giaNhap: '', giaBan: '', soLuong: '' });
const selectedRowIndexes = ref([]);
const isBulkPanelOpen = ref(false);

const listMauSacDaChonQuetDuoc = computed(() => selectedAttributes.value.mauSac);
const isKiemTraDaChon = (key, id) => selectedAttributes.value[key].some(x => x.id === id);

// ⚡ THUẬT TOÁN MỚI: Tự động gom mảng phẳng "productForm.bienThes" thành nhóm theo ID màu sắc
const danhSachBienTheTheoMau = computed(() => {
  if (!productForm.value.bienThes || productForm.value.bienThes.length === 0) return [];
  
  const groups = {};
  productForm.value.bienThes.forEach((item, index) => {
    if (!groups[item.idMauSac]) {
      groups[item.idMauSac] = {
        idMauSac: item.idMauSac,
        tenMauSac: item.tenMauSac,
        items: []
      };
    }
    // Đính kèm globalIndex để phục vụ việc checkbox và sửa giá trị chuẩn xác của mảng gốc
    groups[item.idMauSac].items.push({
      ...item,
      globalIndex: index
    });
  });
  return Object.values(groups);
});

// Tính toán Checkbox tổng của riêng từng bảng màu
const isSelectAllByColor = (colorId) => {
  const group = danhSachBienTheTheoMau.value.find(g => g.idMauSac === colorId);
  if (!group) return false;
  return group.items.every(item => selectedRowIndexes.value.includes(item.globalIndex));
};

// Bật/Tắt Checkbox theo nhóm màu
const toggleSelectAllRowsByColor = (event, colorId) => {
  const group = danhSachBienTheTheoMau.value.find(g => g.idMauSac === colorId);
  if (!group) return;
  
  const globalIndexesOfGroup = group.items.map(item => item.globalIndex);
  if (event.target.checked) {
    globalIndexesOfGroup.forEach(idx => {
      if (!selectedRowIndexes.value.includes(idx)) selectedRowIndexes.value.push(idx);
    });
  } else {
    selectedRowIndexes.value = selectedRowIndexes.value.filter(idx => !globalIndexesOfGroup.includes(idx));
  }
};

// Checkbox tổng của toàn bộ các bảng (giữ để không lỗi logic cũ)
const isSelectAll = computed(() => {
  return productForm.value.bienThes.length > 0 && selectedRowIndexes.value.length === productForm.value.bienThes.length;
});

const toggleSelectAllRows = (event) => {
  if (event.target.checked) {
    selectedRowIndexes.value = productForm.value.bienThes.map((_, index) => index);
  } else {
    selectedRowIndexes.value = [];
  }
};

const dinhDangSoNgoaiUI = (val) => {
  if (val === null || val === undefined || val === '') return '';
  const clean = String(val).replace(/\D/g, '');
  return clean ? clean.replace(/\B(?=(\d{3})+(?!\d))/g, '.') : '';
};

// Hàm cập nhật số lượng/giá tiền dựa trên globalIndex của hàng
const capNhatGiaTriSoTrucTiep = (event, globalIndex, truongDuLieu) => {
  const chuoiSoThuanTuy = event.target.value.replace(/\./g, '');
  const giaTriSo = chuoiSoThuanTuy ? parseInt(chuoiSoThuanTuy, 10) : 0;
  productForm.value.bienThes[globalIndex][truongDuLieu] = isNaN(giaTriSo) ? 0 : giaTriSo;
  event.target.value = dinhDangSoNgoaiUI(productForm.value.bienThes[globalIndex][truongDuLieu]);
};

const apDungChoBienTheDaChon = () => {
  if (productForm.value.bienThes.length === 0) return;
  if (selectedRowIndexes.value.length === 0) return alert("Vui lòng tích chọn ít nhất một dòng biến thể!");

  const gNhap = bulkInput.value.giaNhap ? parseInt(bulkInput.value.giaNhap, 10) : null;
  const gBan = bulkInput.value.giaBan ? parseInt(bulkInput.value.giaBan, 10) : null;
  const sLuong = bulkInput.value.soLuong !== '' ? parseInt(bulkInput.value.soLuong, 10) : null;

  if (gNhap === null && gBan === null && sLuong === null) {
    return alert("Vui lòng điền thông số chung vào bảng trước khi nhấn áp dụng!");
  }

  selectedRowIndexes.value.forEach(idx => {
    if (gNhap !== null) productForm.value.bienThes[idx].giaNhap = gNhap;
    if (gBan !== null) productForm.value.bienThes[idx].giaBan = gBan;
    if (sLuong !== null) productForm.value.bienThes[idx].soLuong = sLuong;
  });

  alert(`Đã áp dụng thông số chung cho ${selectedRowIndexes.value.length} dòng biến thể! ⚡`);
};

const dongDropdownSauDelay = (field) => { setTimeout(() => { comboOpen.value[field] = false; }, 200); };
const safeExtractArray = (res) => { return res && res.data ? (Array.isArray(res.data) ? res.data : (res.data.content || [])) : []; };

const filteredThuongHieu = computed(() => danhSachThuocTinhMaster.value.thuongHieu.filter(i => (i.tenThuongHieu || i.ten || '').toLowerCase().includes(comboSearch.value.thuongHieu.toLowerCase())));
const filteredXuatXu = computed(() => danhSachThuocTinhMaster.value.xuatXu.filter(i => (i.tenXuatXu || i.ten || '').toLowerCase().includes(comboSearch.value.xuatXu.toLowerCase())));
const filteredMauSac = computed(() => danhSachMauSacDB.value.filter(i => (i.tenMauSac || i.ten || '').toLowerCase().includes(comboSearch.value.mauSac.toLowerCase())));
const filteredTrongLuong = computed(() => danhSachTrongLuongDB.value.filter(i => (i.tenTrongLuong || i.ten || '').toLowerCase().includes(comboSearch.value.trongLuong.toLowerCase())));

const selectValueGeneral = (item, searchKey, targetField, displayField) => { productForm.value[targetField] = item.id; comboSearch.value[searchKey] = item[displayField] || item.ten; comboOpen.value[searchKey] = false; };
const selectValueGlobalConfig = (item, searchKey, targetField, displayField) => { globalVariantConfig.value[targetField] = item.id; comboSearch.value[searchKey] = item[displayField] || item.ten; comboOpen.value[searchKey] = false; };

const themThuocTinhDaChon = (item, key, displayField) => {
  const index = selectedAttributes.value[key].findIndex(x => x.id === item.id);
  if (index > -1) {
    selectedAttributes.value[key].splice(index, 1);
    isBienTheGenerated.value = false; 
  } else {
    selectedAttributes.value[key].push({ id: item.id, name: item[displayField] || item.ten });
    isBienTheGenerated.value = false;
  }
  comboSearch.value[key] = ''; 
};

const xoaTagThuocTinh = (key, index) => {
  selectedAttributes.value[key].splice(index, 1);
  isBienTheGenerated.value = false;
};

const loadToanBoDuLieuThuocTinh = async () => {
  try {
    const [th, xx, ms, tl, dc, clt, clk, dm, dcb, cvc] = await Promise.all([
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
  } catch (error) { console.error(error); }
};

const khoiTaoMaSanPhamTuDong = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/san-pham/search', { params: { page: 0, size: 9999 } });
    const danhSach = safeExtractArray(res);
    let soTiepTheo = 1;
    if (danhSach.length > 0) {
      const danhSachSo = danhSach.map(sp => {
        const phanSo = (sp.maSanPham || '').replace(/[^\d]/g, '');
        return phanSo ? parseInt(phanSo, 10) : 0;
      });
      const soLonNhat = Math.max(...danhSachSo, 0);
      soTiepTheo = soLonNhat + 1;
    }
    productForm.value.maSanPham = 'SP' + String(soTiepTheo).padStart(3, '0');
  } catch (e) { productForm.value.maSanPham = 'SP001'; }
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
  const textChuVi = chuViObj ? (chuViObj.tenChuViCanVot || chuViObj.ten) : 'Chưa rõ';

  attrs.mauSac.forEach((ms, indexMs) => {
    if (!colorMapFiles.value[ms.id]) {
      colorMapFiles.value[ms.id] = { file: null, previewUrl: '', hinhAnhName: '' };
    }
    
    attrs.trongLuong.forEach((tl, indexTl) => {
      const codeAuto = `${productForm.value.maSanPham || 'SP'}-CT-${indexMs}${indexTl}`;
      matrix.push({
        idMauSac: ms.id, tenMauSac: ms.name,
        idTrongLuong: tl.id, tenTrongLuong: tl.name,
        idChuViCanVot: cfg.idChuViCanVot, tenChuViCanVot: textChuVi,
        idDoCung: cfg.idDoCung, idDiemCanBang: cfg.idDiemCanBang, idDanhMuc: cfg.idDanhMuc,
        idChatLieuThanVot: cfg.idChatLieuThanVot, idChatLieuKhungVot: cfg.idChatLieuKhungVot,
        maCtsp: codeAuto, giaNhap: 800000, giaBan: 1200000, soLuong: 20, trangThai: 1
      });
    });
  });
  
  productForm.value.bienThes = matrix;
  isBienTheGenerated.value = true;
  selectedRowIndexes.value = matrix.map((_, index) => index);
};

const handleColorFileChange = (event, colorId) => {
  const file = event.target.files[0];
  if (!file) return;
  colorMapFiles.value[colorId] = { file: file, previewUrl: URL.createObjectURL(file), hinhAnhName: file.name };
};

const submitLuuToanBoSanPham = async () => {
  if (!productForm.value.maSanPham?.trim() || !productForm.value.tenSanPham?.trim()) return alert("Vui lòng nhập Mã và Tên sản phẩm!");
  if (!productForm.value.idThuongHieu || !productForm.value.idXuatXu) return alert("Vui lòng nhập Thương hiệu và Xuất xứ!");
  if (!isBienTheGenerated.value || productForm.value.bienThes.length === 0) return alert("Vui lòng ấn nút sinh chuỗi biến thể!");

  let thieuAnh = false;
  listMauSacDaChonQuetDuoc.value.forEach(color => {
    if (!colorMapFiles.value[color.id]?.file) thieuAnh = true;
  });
  if (thieuAnh) return alert("Vui lòng bổ sung đầy đủ hình ảnh đại diện cho tất cả các Màu Sắc ở dưới cùng!");

  if (!confirm(`Bạn có muốn thêm sản phẩm [ ${productForm.value.maSanPham} ] này không?`)) {
    return;
  }

  const formData = new FormData();
  try {
    const listChiTietSanPhamsPayload = productForm.value.bienThes.map(item => {
      const fileConfigOfColor = colorMapFiles.value[item.idMauSac];
      return {
        idMauSac: item.idMauSac, idTrongLuong: item.idTrongLuong, idChuViCanVot: item.idChuViCanVot, 
        idDoCung: item.idDoCung, idDiemCanBang: item.idDiemCanBang, idDanhMuc: item.idDanhMuc,
        idChatLieuThanVot: item.idChatLieuThanVot, idChatLieuKhungVot: item.idChatLieuKhungVot,
        maCtsp: item.maCtsp, giaNhap: item.giaNhap, giaBan: item.giaBan, soLuong: item.soLuong, trangThai: 1, 
        hinhAnh: fileConfigOfColor ? "/uploads/" + fileConfigOfColor.hinhAnhName : 'default.jpg'
      };
    });

    const jsonPayload = JSON.stringify({
      idThuongHieu: productForm.value.idThuongHieu,
      idXuatXu: productForm.value.idXuatXu,
      maSanPham: productForm.value.maSanPham,
      tenSanPham: productForm.value.tenSanPham,
      moTa: productForm.value.moTa,
      baoHanh: productForm.value.baoHanh,
      trangThai: 1,
      chiTietSanPhams: listChiTietSanPhamsPayload
    });
    
    formData.append("data", jsonPayload);
    
    listMauSacDaChonQuetDuoc.value.forEach(color => {
      const targetObj = colorMapFiles.value[color.id];
      if (targetObj && targetObj.file) {
        formData.append("files", targetObj.file);
      }
    });
    
    await axios.post("http://localhost:8080/api/san-pham/create-variants", formData, { 
        headers: { "Content-Type": "multipart/form-data" } 
    });
    
    alert("Thêm sản phẩm thành công! 🎉");
    quayLaiDanhSach();
  } catch (error) { 
    alert("Có lỗi xảy ra khi lưu dữ liệu!"); 
  }
};

const quayLaiDanhSach = () => { router.push('/san-pham'); };

onMounted(() => {
  khoiTaoMaSanPhamTuDong();
  loadToanBoDuLieuThuocTinh();
});
</script>

<style scoped>
.breadcrumb-container { display: flex; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #333; }
.form-container-box { background: #fff; border: 1px solid #fed7aa; border-radius: 6px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
.form-box-body { display: flex; flex-direction: column; gap: 15px; }
.section-title { font-size: 13px; font-weight: bold; color: #f79b66; border-left: 3px solid #f79b66; padding-left: 8px; text-align: left; margin-bottom: 5px; }
.form-divider { border: 0; border-top: 1px dashed #cbd5e1; margin: 8px 0; }
.form-grid-row { display: flex; gap: 15px; }
.form-group-item { display: flex; flex-direction: column; gap: 6px; text-align: left; flex: 1; position: relative; }
.form-group-item label { font-size: 12px; font-weight: 600; color: #374151; }
.form-group-item input, .form-group-item select, .form-group-item textarea { padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 4px; font-size: 13px; outline: none; width: 100%; box-sizing: border-box; }
.disabled-input { background: #f3f4f6; color: #9ca3af; cursor: not-allowed; font-weight: bold; }
.required-star { color:#f79b66; }

.btn-generate { background-color: #f79b66; color: white; border: none; padding: 8px 16px; font-size: 12px; border-radius: 4px; cursor: pointer; font-weight: 600; }
.btn-bulk-toggle { background-color: #1e293b; color: white; border: none; padding: 8px 16px; font-size: 12px; border-radius: 4px; cursor: pointer; font-weight: 600; transition: 0.2s; }
.btn-bulk-toggle:hover { background-color: #0f172a; }

.combobox-wrapper { position: relative; width: 100%; }
.combobox-dropdown { position: absolute; top: 100%; left: 0; width: 100%; background: #ffffff; border: 1px solid #cbd5e1; border-radius: 4px; max-height: 180px; overflow-y: auto; z-index: 999; padding: 0; margin: 4px 0 0 0; list-style: none; }
.combobox-dropdown li { padding: 8px 12px; font-size: 13px; cursor: pointer; text-align: left; display: flex; justify-content: space-between; align-items: center; transition: 0.15s; }
.combobox-dropdown li:hover { background-color: #fff8f3; color: #f79b66; }
.combobox-dropdown li.is-selected { background-color: #ffedd5; color: #f79b66; font-weight: 600; }
.check-icon { font-size: 12px; color: #f79b66; }
.selected-tags-container { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 6px; min-height: 24px; }
.remove-tag-x { margin-left: 6px; font-weight: bold; cursor: pointer; color: #f79b66; }
.badge-prop { padding: 2px 6px; border-radius: 4px; font-weight: 500; font-size: 11px; display: inline-block; }
.badge-prop.cyan { background: #ecfeff; color: #0891b2; border: 1px solid #c5f6fa; }
.badge-prop.orange { background: #fff7ed; color: #f79b66; border: 1px solid #ffedd5; }

.bulk-fill-container { background: #f8fafc; border: 1px dashed #f79b66; padding: 15px; border-radius: 6px; margin-top: 15px; text-align: left; animation: fadeIn 0.25s ease-out; }
.bulk-fill-title { font-size: 12.5px; font-weight: bold; color: #f79b66; margin-bottom: 10px; text-transform: uppercase; }
.bulk-fill-grid { display: flex; gap: 15px; align-items: flex-end; }
.bulk-item { display: flex; flex-direction: column; gap: 6px; flex: 1; }
.bulk-item label { font-size: 11.5px; font-weight: 600; color: #64748b; }
.bulk-item input { padding: 6px 10px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 12.5px; outline: none; }
.btn-bulk-apply { background-color: #f79b66; color: white; border: none; padding: 7px 20px; font-size: 12.5px; border-radius: 4px; cursor: pointer; font-weight: 600; transition: 0.2s; height: 31px; }
.btn-bulk-apply:hover { background-color: #ea712b; }

.variant-dynamic-table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 12px; text-align: left; margin-bottom: 25px; }
.variant-dynamic-table th { background-color: #f8fafc; color: #475569; padding: 8px; border: 1px solid #e2e8f0; }
.variant-dynamic-table td { padding: 6px; border: 1px solid #e2e8f0; vertical-align: middle; }
.table-input { padding: 6px !important; font-size: 12px !important; border-radius: 4px; border: 1px solid #cbd5e1; width: 100%; box-sizing: border-box; }

.row-checked { background-color: #fffbf7; }

.color-image-grid { display: flex; flex-wrap: wrap; gap: 20px; margin-top: 15px; background: #fff8f3; padding: 15px; border-radius: 8px; border: 1px dashed #f79b66; }
.color-image-card { background: #fff; padding: 12px; border: 1px solid #fed7aa; border-radius: 6px; display: flex; flex-direction: column; gap: 8px; align-items: center; width: calc(25% - 15px); min-width: 160px; box-shadow: 0 2px 4px rgba(0,0,0,0.02); }
.color-preview-box { width: 100px; height: 100px; border: 1px solid #cbd5e1; border-radius: 4px; display: flex; align-items: center; justify-content: center; overflow: hidden; background: #fafafa; }
.color-preview-box img { width: 100%; height: 100%; object-fit: cover; }
.form-box-footer { padding: 15px 0 0 0; border-top: 1px solid #e5e7eb; display: flex; justify-content: flex-end; gap: 10px; margin-top: 15px;}
.btn-modal-cancel { background: #f3f4f6; color: #4b5563; border: 1px solid #d1d5db; padding: 8px 16px; border-radius: 4px; cursor: pointer; font-size: 13px; }
.btn-modal-submit { background: #f79b66; color: white; border: none; padding: 8px 20px; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 600; }

/* ⚡ CSS MỚI: Định dạng tiêu đề cho từng nhóm bảng màu */
.tables-group-wrapper { display: flex; flex-direction: column; gap: 20px; text-align: left; }
.color-table-section { background: #ffffff; border: 1px solid #e2e8f0; padding: 15px; border-radius: 6px; box-shadow: 0 1px 3px rgba(0,0,0,0.02); }
.color-table-header { margin-bottom: 12px; display: flex; align-items: center; }

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-5px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>