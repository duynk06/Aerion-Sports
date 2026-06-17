<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <span class="breadcrumb-text">Quản lý sản phẩm / Quản lý biến thể / <strong style="color: #f97316;">Thêm mới chi tiết biến thể</strong></span>
    </div>

    <div class="box-alert-info" v-if="productInfo.ma">
      Đang thêm mới biến thể cho sản phẩm: <strong>[{{ productInfo.ma }}] - {{ productInfo.ten }}</strong>
      <br />
      <small v-if="isLockedMode" style="color: #16a34a; font-weight: bold;">
        Hệ thống đã tự động khóa và kế thừa thông số cố định từ biến thể gốc trước đó.
      </small>
      <small v-else style="color: #b45309; font-weight: bold;">
        Đây là biến thể đầu tiên của sản phẩm này. Vui lòng cấu hình đầy đủ thông số kỹ thuật làm gốc!
      </small>
    </div>

    <div class="form-container-box">
      <div class="form-box-body">
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
          <input type="text" v-model="editingForm.maCtsp" disabled class="disabled-input"/>
        </div>

        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Màu sắc sản phẩm <span class="required-star"> *</span></label>
            <select v-model="editingForm.idMauSac">
              <option :value="null">-- Chọn màu sắc --</option>
              <option v-for="item in masterData.mauSac" :key="item.id" :value="item.id">
                {{ item.tenMauSac || item.ten || item.ten_mau_sac }}
              </option>
            </select>
          </div>
          <div class="form-group-item">
            <label>Trọng lượng vợt <span class="required-star"> *</span></label>
            <select v-model="editingForm.idTrongLuong">
              <option :value="null">-- Chọn trọng lượng --</option>
              <option v-for="item in masterData.trongLuong" :key="item.id" :value="item.id">
                {{ item.tenTrongLuong || item.ten || item.ten_trong_luong }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Độ cứng thân</label>
            <select v-model="editingForm.idDoCung" :disabled="isLockedMode" :class="{ 'disabled-input': isLockedMode }">
              <option :value="null">-- Chọn độ cứng --</option>
              <option v-for="item in masterData.doCung" :key="item.id" :value="item.id">
                {{ item.tenDoCung || item.ten || item.tenDoCungThanVot || item.doCung }}
              </option>
            </select>
          </div>
          <div class="form-group-item">
            <label>Điểm cân bằng</label>
            <select v-model="editingForm.idDiemCanBang" :disabled="isLockedMode" :class="{ 'disabled-input': isLockedMode }">
              <option :value="null">-- Chọn điểm cân bằng --</option>
              <option v-for="item in masterData.diemCanBang" :key="item.id" :value="item.id">
                {{ item.tenDiemCanBang || item.ten || item.diemCanBang }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Chu vi cán vợt</label>
            <select v-model="editingForm.idChuViCanVot" :disabled="isLockedMode" :class="{ 'disabled-input': isLockedMode }">
              <option :value="null">-- Chọn chu vi cán --</option>
              <option v-for="item in masterData.chuViCan" :key="item.id" :value="item.id">
                {{ item.tenChuViCanVot || item.ten || item.chuViCanVot }}
              </option>
            </select>
          </div>
          <div class="form-group-item">
            <label>Danh mục phân loại</label>
            <select v-model="editingForm.idDanhMuc" :disabled="isLockedMode" :class="{ 'disabled-input': isLockedMode }">
              <option :value="null">-- Chọn danh mục --</option>
              <option v-for="item in masterData.danhMuc" :key="item.id" :value="item.id">
                {{ item.tenDanhMuc || item.ten || item.danhMuc }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Chất liệu thân vợt</label>
            <select v-model="editingForm.idChatLieuThanVot" :disabled="isLockedMode" :class="{ 'disabled-input': isLockedMode }">
              <option :value="null">-- Chọn chất liệu thân --</option>
              <option v-for="item in masterData.chatLieuThan" :key="item.id" :value="item.id">
                {{ item.tenChatLieuThanVot || item.ten || item.chatLieuThan }}
              </option>
            </select>
          </div>
          <div class="form-group-item">
            <label>Chất liệu khung vợt</label>
            <select v-model="editingForm.idChatLieuKhungVot" :disabled="isLockedMode" :class="{ 'disabled-input': isLockedMode }">
              <option :value="null">-- Chọn chất liệu khung --</option>
              <option v-for="item in masterData.chatLieuKhung" :key="item.id" :value="item.id">
                {{ item.tenChatLieuKhungVot || item.ten || item.chatLieuKhung }}
              </option>
            </select>
          </div>
        </div>
        
        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Giá nhập kho (VNĐ) <span class="required-star"> *</span></label>
            <input type="text" :value="formatDisplayPrice(editingForm.giaNhap)" @input="updateGiaTien($event, 'giaNhap')" />
          </div>
          <div class="form-group-item">
            <label>Giá bán lẻ (VNĐ) <span class="required-star"> *</span></label>
            <input type="text" :value="formatDisplayPrice(editingForm.giaBan)" @input="updateGiaTien($event, 'giaBan')" />
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

      <div class="form-box-footer">
        <button class="btn-modal-cancel" @click="quayLaiDanhSachBienThe">Hủy bỏ</button>
        <button class="btn-modal-submit" @click="submitLuuDuLieuConfirm">Lưu dữ liệu</button>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import MainLayout from '@/layouts/MainLayout.vue';

const route = useRoute();
const router = useRouter();

const productInfo = ref({ id: route.query.idSP || '', ma: route.query.maSP || '', ten: route.query.tenSP || '' });
const masterData = ref({ mauSac: [], trongLuong: [], doCung: [], diemCanBang: [], chuViCan: [], danhMuc: [], chatLieuThan: [], chatLieuKhung: [] });

const isLockedMode = ref(false); 
const fileUploadData = ref(null);
const previewImageSrc = ref('https://placehold.co/100x100?text=Chọn+Ảnh');

const editingForm = ref({
  id: null, maCtsp: '', soLuong: 10, giaBan: 1200000, giaNhap: 800000, trangThai: 1, hinhAnh: '',
  idSanPham: parseInt(productInfo.value.id, 10), idMauSac: null, idTrongLuong: null, 
  idDoCung: null, idDiemCanBang: null, idChatLieuThanVot: null, idChatLieuKhungVot: null, idDanhMuc: null, idChuViCanVot: null
});

const formatDisplayPrice = (value) => value === null || value === undefined || value === '' ? '' : new Intl.NumberFormat('vi-VN').format(value);
const updateGiaTien = (event, fieldName) => {
  const rawValue = event.target.value.replace(/\./g, '').replace(/[^\d]/g, '');
  editingForm.value[fieldName] = rawValue ? parseInt(rawValue, 10) : 0;
};

const safeExtractArray = (res) => {
  if (!res || !res.data) return [];
  if (Array.isArray(res.data)) return res.data;
  return res.data.content || [];
};

const tuDongNapThongSoTuDatabase = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/chi-tiet-san-pham/search', {
      params: { idSanPham: parseInt(productInfo.value.id, 10), page: 0, size: 200 }
    });
    const danhSach = response.data.content || response.data || [];
    
    const g = danhSach.find(item => {
      return (item.idDoCung || item.doCung?.id || item.id_do_cung) != null;
    });

    if (g) {
      editingForm.value.idDoCung = g.idDoCung || g.doCung?.id || g.id_do_cung || null;
      editingForm.value.idDiemCanBang = g.idDiemCanBang || g.diemCanBang?.id || g.id_diem_can_bang || null;
      editingForm.value.idChuViCanVot = g.idChuViCanVot || g.chuViCanVot?.id || g.id_chu_vi_can_vot || null;
      editingForm.value.idDanhMuc = g.idDanhMuc || g.danhMuc?.id || g.id_danh_muc || null;
      editingForm.value.idChatLieuThanVot = g.idChatLieuThanVot || g.chatLieuThanVot?.id || g.id_chat_lieu_than_vot || null;
      editingForm.value.idChatLieuKhungVot = g.idChatLieuKhungVot || g.chatLieuKhungVot?.id || g.id_chat_lieu_khung_vot || null;
      isLockedMode.value = true;
    } else {
      isLockedMode.value = false;
    }
  } catch (error) {
    console.error("Lỗi tự động nạp thuộc tính kỹ thuật:", error);
  }
};

const khoiTaoMaSKUBienTheTuDong = async () => {
  try {
    const maCha = productInfo.value.ma || 'SP';
    const response = await axios.get('http://localhost:8080/api/chi-tiet-san-pham/search', {
      params: { idSanPham: parseInt(productInfo.value.id, 10), page: 0, size: 9999 }
    });
    const danhSach = response.data.content || [];
    let soTiepTheo = 1;
    if (danhSach.length > 0) {
      const danhSachSoCon = danhSach.map(item => {
        const maSkuCon = item.maCtsp || '';
        const phanDuoi = maSkuCon.split('-').pop(); 
        const phanSo = phanDuoi ? phanDuoi.replace(/[^\d]/g, '') : '';
        return phanSo ? parseInt(phanSo, 10) : 0;
      });
      const soLonNhat = Math.max(...danhSachSoCon, 0);
      soTiepTheo = soLonNhat + 1;
    }
    editingForm.value.maCtsp = `${maCha}-CT-${String(soTiepTheo).padStart(3, '0')}`;
  } catch (error) {
    editingForm.value.maCtsp = `${productInfo.value.ma || 'SP'}-CT-001`;
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
  } catch (e) { console.error(e); }
};

const onFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    fileUploadData.value = file;
    previewImageSrc.value = URL.createObjectURL(file);
    editingForm.value.hinhAnh = "/uploads/" + file.name;
  }
};

// ⚡ ĐÃ CẢI TIẾN: Thêm luồng hỏi xác nhận và popup báo thêm mới thành công giống Form sản phẩm cha
const submitLuuDuLieuConfirm = async () => {
  if (!editingForm.value.idMauSac) return alert("Vui lòng chọn Màu sắc biến thể!");
  if (!editingForm.value.idTrongLuong) return alert("Vui lòng chọn Trọng lượng biến thể!");
  if (!fileUploadData.value) return alert("Vui lòng chọn hình ảnh file đính kèm cho biến thể!");

  // 1. ⚡ BẢNG HỎI XÁC NHẬN CHUẨN ĐỒNG BỘ
  if (!confirm(`Bạn có muốn thêm biến thể [ ${editingForm.value.maCtsp} ] này không?`)) {
    return; // Người dùng nhấn Hủy -> Dừng luồng xử lý
  }
  
  try {
    // Lưu thông tin chi tiết cấu hình biến thể trước
    const resCTSP = await axios.post('http://localhost:8080/api/chi-tiet-san-pham', editingForm.value);
    const idChiTiet = resCTSP.data.id; 

    // Đẩy tệp tin ảnh liên kết vào bảng hinh_anh_sp
    if (fileUploadData.value && idChiTiet) {
      const hinhAnhPayload = { 
        idSanPhamChiTiet: idChiTiet, 
        laAnhChinh: true, 
        duongDanAnh: "/uploads/" + fileUploadData.value.name, 
        trangThai: 1 
      };
      await axios.post('http://localhost:8080/api/hinh-anh-sp', hinhAnhPayload);
    }

    // 2. ⚡ POPUP BÁO THÀNH CÔNG 🎉
    alert("Thêm mới biến thể thành công! 🎉");
    quayLaiDanhSachBienThe();
  } catch (error) { 
    alert("Thao tác thất bại! Vui lòng kiểm tra lại kết nối hệ thống."); 
  }
};

const quayLaiDanhSachBienThe = () => {
  if (!productInfo.value.ma || productInfo.value.ma === 'SP') {
    router.push('/san-pham/danh-sach-bien-the');
  } else {
    router.push({ path: '/san-pham/bien-the', query: { idSP: productInfo.value.id, maSP: productInfo.value.ma, tenSP: productInfo.value.ten } });
  }
};

onMounted(async () => {
  if (!productInfo.value.id) return router.push('/san-pham');
  await loadMasterDataCombobox();
  await tuDongNapThongSoTuDatabase(); 
  await khoiTaoMaSKUBienTheTuDong();
});
</script>

<style scoped>
.breadcrumb-container { display: flex; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #333; }
.box-alert-info { background-color: #fff7ed; border-left: 4px solid #f79b66; padding: 12px 15px; font-size: 14px; margin-bottom: 20px; color: #7c2d12; border-radius: 0 4px 4px 0; text-align: left; line-height: 1.5; }
.form-container-box { background: white; border: 1px solid #fed7aa; border-radius: 6px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
.form-box-body { display: flex; flex-direction: column; gap: 15px; }
.form-group-item { display: flex; flex-direction: column; gap: 6px; text-align: left; }
.form-group-item label { font-size: 13px; font-weight: 600; color: #374151; }
.form-group-item input, .form-group-item select { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; width: 100%; box-sizing: border-box; outline: none; }
.form-group-item input:focus, .form-group-item select:focus { border-color: #f79b66; }
.disabled-input { background: #f1f5f9 !important; cursor: not-allowed; color: #64748b; font-weight: 500; border-color: #e2e8f0 !important; }
.form-row-flex { display: flex; gap: 15px; }
.form-row-flex .form-group-item { flex: 1; }
.image-upload-section { display: flex; gap: 15px; align-items: center; background-color: #fff7ed; padding: 12px; border-radius: 6px; border: 1px dashed #f79b66; }
.image-preview-box { width: 90px; height: 90px; border-radius: 4px; background-color: #fff; border: 1px solid #cbd5e1; overflow: hidden; display: flex; align-items: center; justify-content: center; }
.image-preview-box img { width: 100%; height: 100%; object-fit: cover; }
.status-toggle-container { display: inline-flex; align-items: center; cursor: pointer; user-select: none; gap: 8px; }
.toggle-track { position: relative; display: inline-block; width: 36px; height: 18px; border-radius: 999px; transition: background-color 0.2s ease; }
.track-active { background-color: #22c55e; }
.track-inactive { background-color: #cbd5e1; }
.toggle-handle { position: absolute; top: 3px; left: 3px; width: 12px; height: 12px; background-color: #fff; border-radius: 50%; transition: transform 0.2s ease; }
.track-active .toggle-handle { transform: translateX(18px); }
.toggle-text { font-size: 13px; font-weight: 600; }
.form-box-footer { padding: 15px 0 0 0; border-top: 1px solid #e5e7eb; display: flex; justify-content: flex-end; gap: 10px; margin-top: 15px; }
.btn-modal-cancel { background: #f3f4f6; border: 1px solid #d1d5db; padding: 8px 16px; border-radius: 4px; cursor: pointer; font-size: 13px; color: #4b5563; }
.btn-modal-submit { background: #f79b66; color: white; border: none; padding: 8px 20px; border-radius: 4px; cursor: pointer; font-weight: 600; font-size: 13px; }
.required-star { color: #f79b66; font-weight: bold; }
</style>