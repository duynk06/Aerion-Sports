<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <span class="breadcrumb-text">Quản lý sản phẩm / Quản lý biến thể / <strong style="color: #f97316;">Cập nhật chi tiết biến thể</strong></span>
    </div>

    <div class="box-alert-info" v-if="editingForm.maCtsp">
      Đang chỉnh sửa biến thể: <strong style="color: #f97316;">{{ editingForm.maCtsp }}</strong> thuộc sản phẩm <strong>{{ productInfo.ten }}</strong>
    </div>

    <div class="form-container-box">
      <div class="form-box-body">
        <div class="image-upload-section">
          <div class="image-preview-box">
            <img :src="previewImageSrc" @error="onImageLoadError" alt="Hình ảnh sản phẩm" />
          </div>
          <div class="form-group-item" style="flex: 1;">
            <label>Thay đổi hình ảnh biến thể</label>
            <input type="file" @change="onFileChange" accept="image/*" style="margin-top: 5px; font-size: 13px;" />
          </div>
        </div>

        <div class="form-group-item">
          <label>Mã biến thể (SKU)</label>
          <input type="text" v-model="editingForm.maCtsp" disabled class="disabled-input" />
        </div>

        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Màu sắc sản phẩm <span class="required-star"> *</span></label>
            <select v-model="editingForm.idMauSac">
              <option :value="null">-- Chọn màu sắc --</option>
              <option v-for="item in masterData.mauSac" :key="item.id" :value="item.id">{{ item.tenMauSac }}</option>
            </select>
          </div>
          <div class="form-group-item">
            <label>Trọng lượng vợt <span class="required-star"> *</span></label>
            <select v-model="editingForm.idTrongLuong">
              <option :value="null">-- Chọn trọng lượng --</option>
              <option v-for="item in masterData.trongLuong" :key="item.id" :value="item.id">{{ item.tenTrongLuong }}</option>
            </select>
          </div>
        </div>

        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Độ cứng thân (Cố định - Không được sửa)</label>
            <input type="text" :value="productInfo.tenDoCung || 'Chưa rõ'" disabled class="disabled-input" />
          </div>
          <div class="form-group-item">
            <label>Điểm cân bằng (Cố định - Không được sửa)</label>
            <input type="text" :value="productInfo.tenDiemCanBang || 'Chưa rõ'" disabled class="disabled-input" />
          </div>
        </div>

        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Chu vi cán vợt (Cố định - Không được sửa)</label>
            <input type="text" :value="productInfo.tenChuViCanVot || 'Chưa rõ'" disabled class="disabled-input" />
          </div>
          <div class="form-group-item">
            <label>Danh mục phân loại (Cố định - Không được sửa)</label>
            <input type="text" :value="productInfo.tenDanhMuc || 'Chưa rõ'" disabled class="disabled-input" />
          </div>
        </div>

        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Chất liệu thân vợt (Cố định - Không được sửa)</label>
            <input type="text" :value="productInfo.tenChatLieuThanVot || 'Chưa rõ'" disabled class="disabled-input" />
          </div>
          <div class="form-group-item">
            <label>Chất liệu khung vợt (Cố định - Không được sửa)</label>
            <input type="text" :value="productInfo.tenChatLieuKhungVot || 'Chưa rõ'" disabled class="disabled-input" />
          </div>
        </div>
        
        <div class="form-row-flex">
          <div class="form-group-item">
            <label>Giá nhập kho (VNĐ) <span class="required-star"> *</span></label>
            <input 
              type="text" 
              :value="formatDisplayPrice(editingForm.giaNhap)" 
              @input="updateGiaTien($event, 'giaNhap')"
              placeholder="Nhập giá nhập..."
            />
          </div>
          <div class="form-group-item">
            <label>Giá bán lẻ (VNĐ) <span class="required-star"> *</span></label>
            <input 
              type="text" 
              :value="formatDisplayPrice(editingForm.giaBan)" 
              @input="updateGiaTien($event, 'giaBan')"
              placeholder="Nhập giá bán lẻ..."
            />
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
        <button class="btn-modal-cancel" @click="quayLaiDanhSach">Hủy bỏ</button>
        <button class="btn-modal-submit" @click="submitCapNhat">Cập nhật dữ liệu</button>
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

const idBienThe = route.query.id; 
// 🌟 ĐÃ SỬA: Lưu trữ thông tin tên thuộc tính cố định của sản phẩm cha
const productInfo = ref({ 
  id: route.query.idSP || '', ma: route.query.maSP || '', ten: route.query.tenSP || '',
  tenDoCung: '', tenDiemCanBang: '', tenChuViCanVot: '', tenDanhMuc: '', tenChatLieuThanVot: '', tenChatLieuKhungVot: ''
});

const masterData = ref({ mauSac: [], trongLuong: [] });
const fileUploadData = ref(null);
const previewImageSrc = ref('https://placehold.co/100x100?text=No+Image');

// 🌟 ĐÃ SỬA: editingForm tối giản chỉ chứa idSanPham, idMauSac và idTrongLuong đúng cấu trúc DTO mới
const editingForm = ref({
  id: null, maCtsp: '', soLuong: 0, giaBan: 0, giaNhap: 0, trangThai: 1, hinhAnh: '',
  idSanPham: null, idMauSac: null, idTrongLuong: null
});

const formatDisplayPrice = (value) => {
  if (value === null || value === undefined || value === '') return '';
  return new Intl.NumberFormat('vi-VN').format(value);
};

const updateGiaTien = (event, fieldName) => {
  const rawValue = event.target.value.replace(/\./g, '').replace(/[^\d]/g, '');
  editingForm.value[fieldName] = rawValue ? parseInt(rawValue, 10) : 0;
};

const onImageLoadError = (e) => {
  e.target.src = 'https://placehold.co/100x100?text=No+Image';
};

const loadChiTietBienTheHienTai = async () => {
  try {
    // 🚀 1. GỌI API lấy thông tin chi tiết từ cây sản phẩm cha (Chứa toàn bộ 6 thuộc tính nền)
    const resProduct = await axios.get(`http://localhost:8080/api/san-pham/search`, {
      params: { keyword: productInfo.value.ma, page: 0, size: 10 }
    });
    
    const listProducts = resProduct.data.content || [];
    const productGoc = listProducts.find(p => Number(p.id) === Number(productInfo.value.id));
    
    if (productGoc) {
      productInfo.value.tenDoCung = productGoc.tenDoCung;
      productInfo.value.tenDiemCanBang = productGoc.tenDiemCanBang;
      productInfo.value.tenChuViCanVot = productGoc.tenChuViCanVot;
      productInfo.value.tenDanhMuc = productGoc.tenDanhMuc;
      productInfo.value.tenChatLieuThanVot = productGoc.tenChatLieuThanVot;
      productInfo.value.tenChatLieuKhungVot = productGoc.tenChatLieuKhungVot;
      
      // 🚀 2. Tìm kiếm đúng dòng biến thể con dựa vào maCtsp hoặc id
      const variantsList = productGoc.chiTietSanPhams || [];
      const data = variantsList.find(item => Number(item.id) === Number(idBienThe));
      
      if (data) {
        console.log("Dữ liệu biến thể bóc tách từ cây sản phẩm:", data);

        editingForm.value = {
          id: data.id,
          maCtsp: data.maCtsp,
          soLuong: data.soLuong,
          giaBan: data.giaBan,
          giaNhap: data.giaNhap,
          trangThai: data.trangThai,
          hinhAnh: data.hinhAnh || '',
          idSanPham: parseInt(productInfo.value.id, 10),
          idMauSac: data.idMauSac,
          idTrongLuong: data.idTrongLuong
        };

        const pathAnh = data.hinhAnh;
        if (pathAnh) {
          previewImageSrc.value = pathAnh.startsWith('http') ? pathAnh : `http://localhost:8080${pathAnh}?t=${new Date().getTime()}`;
        }
      }
    }
  } catch (error) {
    console.error("Lỗi xử lý nạp chi tiết form cập nhật:", error);
  }
};

const safeExtractArray = (res) => {
  if (!res || !res.data) return [];
  if (Array.isArray(res.data)) return res.data;
  return res.data.content || [];
};

const loadMasterData = async () => {
  try {
    const [ms, tl] = await Promise.all([
      axios.get('http://localhost:8080/api/mau-sac/all'),
      axios.get('http://localhost:8080/api/trong-luong/all')
    ]);
    masterData.value.mauSac = safeExtractArray(ms);
    masterData.value.trongLuong = safeExtractArray(tl);
  } catch (e) { console.error(e); }
};

const onFileChange = (event) => {
  const file = event.target.files[0];
  if (file) {
    fileUploadData.value = file;
    previewImageSrc.value = URL.createObjectURL(file);
  }
};

const submitCapNhat = async () => {
  if (!editingForm.value.idMauSac) return alert("Vui lòng chọn Màu sắc sản phẩm!");
  if (!editingForm.value.idTrongLuong) return alert("Vui lòng chọn Trọng lượng vợt!");
  
  if (editingForm.value.giaNhap === null || editingForm.value.giaNhap === undefined || editingForm.value.giaNhap <= 0) {
    return alert("Giá nhập kho phải lớn hơn 0 VNĐ!");
  }
  if (editingForm.value.giaBan === null || editingForm.value.giaBan === undefined || editingForm.value.giaBan <= 0) {
    return alert("Giá bán lẻ phải lớn hơn 0 VNĐ!");
  }

  if (parseInt(editingForm.value.giaBan, 10) < parseInt(editingForm.value.giaNhap, 10)) {
    return alert("Cảnh báo lỗi: Giá bán lẻ không được nhỏ hơn Giá nhập kho!");
  }

  if (editingForm.value.soLuong === null || editingForm.value.soLuong === undefined || editingForm.value.soLuong < 0) {
    return alert("Số lượng tồn kho không được để trống hoặc là số âm!");
  }

  try {
    let formData = new FormData();
    
    // 🌟 ĐÃ SỬA: Đóng gói payload sạch chỉ chứa Màu sắc, Trọng lượng, Giá và Số lượng chuyển đi
    const cleanPayload = {
      idSanPham: editingForm.value.idSanPham,
      idMauSac: editingForm.value.idMauSac,
      idTrongLuong: editingForm.value.idTrongLuong,
      maCtsp: editingForm.value.maCtsp,
      giaNhap: editingForm.value.giaNhap,
      giaBan: editingForm.value.giaBan,
      soLuong: editingForm.value.soLuong,
      trangThai: editingForm.value.trangThai,
      hinhAnh: editingForm.value.hinhAnh
    };

    formData.append("data", JSON.stringify(cleanPayload));
    
    if (fileUploadData.value) {
      formData.append("file", fileUploadData.value);
    }

    // Gửi put cập nhật thông tin đơn lẻ lên Controller
    await axios.put(`http://localhost:8080/api/san-pham/bien-the/update/${editingForm.value.id}`, formData, {
      headers: { "Content-Type": "multipart/form-data" }
    });

    alert("Cập nhật dữ liệu và hình ảnh tự động thành công! 🎉");
    quayLaiDanhSach();
  } catch (error) {
    console.error(error);
    alert("Cập nhật thất bại, vui lòng kiểm tra lại kết nối mạng hoặc dữ liệu!");
  }
};

const quayLaiDanhSach = () => {
  if (!productInfo.value.ma || productInfo.value.ma === 'SP') {
    router.push('/san-pham/danh-sach-bien-the');
  } else {
    router.push({
      path: '/san-pham/bien-the',
      query: { 
        idSP: productInfo.value.id, 
        maSP: productInfo.value.ma, 
        tenSP: productInfo.value.ten 
      }
    });
  }
};

onMounted(async () => {
  await loadMasterData();
  await loadChiTietBienTheHienTai();
});
</script>

<style scoped>
/* Giữ nguyên vẹn 100% CSS ban đầu của bạn */
.breadcrumb-container { display: flex; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #333; }
.box-alert-info { background-color: #fff7ed; border-left: 4px solid #f79b66; padding: 12px 15px; font-size: 14px; margin-bottom: 20px; color: #7c2d12; border-radius: 0 4px 4px 0; text-align: left; }
.form-container-box { background: white; border: 1px solid #fed7aa; border-radius: 6px; padding: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }
.form-box-body { display: flex; flex-direction: column; gap: 15px; }
.form-group-item { display: flex; flex-direction: column; gap: 6px; text-align: left; }
.form-group-item label { font-size: 13px; font-weight: 600; color: #374151; }
.form-group-item input, .form-group-item select { padding: 8px 12px; border: 1px solid #cbd5e1; border-radius: 4px; font-size: 13px; width: 100%; box-sizing: border-box; outline: none; }
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