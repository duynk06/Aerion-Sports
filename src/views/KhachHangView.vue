<template>
  <MainLayout title="Khách hàng">
    <div v-if="isDetailMode" class="customer-form-container customer-detail-container">
      <div class="back-header">
        <button class="btn-back-link" @click="cancelDetailMode">← Quay lại danh sách</button>
        <h3 class="page-form-title">Chi tiết thông tin khách hàng</h3>
      </div>

      <div class="form-section-card profile-grid">
        <div class="avatar-upload-block">
          <div class="section-sub-title fw-bold mb-3">Ảnh đại diện</div>
          <div class="avatar-preview-circle">
            <img v-if="customerDetail.avatar" :src="customerDetail.avatar" alt="Avatar" />
            <span v-else class="default-avatar-icon">👤</span>
          </div>
          <span :class="['status-badge', customerDetail.trangThai === 1 ? 'active' : 'inactive']" style="margin-top: 15px; display: inline-block;">
            {{ customerDetail.trangThai === 1 ? 'Hoạt động' : 'Ngừng hoạt động' }}
          </span>
        </div>

        <div class="info-fields-block">
          <div class="section-sub-title fw-bold mb-3">Thông tin cá nhân</div>
          <div class="form-row">
            <div class="form-cell">
              <label>Họ và tên</label>
              <input type="text" :value="customerDetail.hoTen || 'Chưa cập nhật'" disabled class="disabled-input" />
            </div>
            <div class="form-cell">
              <label>Số điện thoại</label>
              <input type="text" :value="customerDetail.sdt" disabled class="disabled-input" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-cell">
              <label>Email</label>
              <input type="text" :value="customerDetail.email || '-'" disabled class="disabled-input" />
            </div>
            <div class="form-cell">
              <label>Ngày sinh</label>
              <input type="text" :value="customerDetail.ngaySinh || '-'" disabled class="disabled-input" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-cell">
              <label>Giới tính</label>
              <input type="text" :value="Number(customerDetail.gioiTinh) === 1 ? 'Nam' : 'Nữ'" disabled class="disabled-input" />
            </div>
            <div class="form-cell"></div>
          </div>
        </div>
      </div>

      <div class="form-section-card address-list-section">
        <div class="address-section-header">
          <div class="section-sub-title fw-bold">Danh sách địa chỉ của khách hàng</div>
        </div>
        <div class="table-box" style="box-shadow: none; padding: 0; margin-top: 10px;">
          <table class="sub-table">
            <thead>
              <tr>
                <th style="width: 10%">STT</th>
                <th style="width: 65%">Địa chỉ chi tiết</th>
                <th style="width: 25%; text-align: center;">Loại địa chỉ</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(addr, idx) in customerDetail.addresses" :key="idx">
                <td>{{ idx + 1 }}</td>
                <td>
                  <div class="address-detail-text">
                    {{ addr.diaChiChiTiet }}<span v-if="addr.phuongXa">, {{ addr.phuongXa }}</span><span v-if="addr.tinhThanh">, {{ addr.tinhThanh }}</span>
                  </div>
                </td>
                <td class="text-center">
                  <span v-if="addr.macDinh" class="badge-active" style="background-color: #e6f7ff; color: #1890ff; border: 1px solid #91d5ff; padding: 3px 8px; border-radius: 4px; font-size: 12px;">
                    ⭐ Mặc định
                  </span>
                  <span v-else style="color: #999; font-size: 12px;">Phụ</span>
                </td>
              </tr>
              <tr v-if="!customerDetail.addresses || customerDetail.addresses.length === 0">
                <td colspan="3" class="no-data" style="padding: 20px;">Khách hàng này chưa cập nhật địa chỉ nào.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="form-submit-bar">
        <button class="btn-cancel-action" @click="cancelDetailMode" style="background: #6c757d; color: white;">Đóng lại</button>
        <button class="btn-submit-action" @click="switchToEditFromDetail(customerDetail.rawOrigin)">Chỉnh sửa thông tin</button>
      </div>
    </div>

    <div v-else-if="isFormMode" class="customer-form-container">
      <div class="back-header">
        <button class="btn-back-link" @click="cancelFormMode">← Quay lại danh sách</button>
        <h3 class="page-form-title">{{ customerForm.id ? 'Cập nhật thông tin khách hàng' : 'Nhập thông tin khách hàng mới' }}</h3>
      </div>

      <div class="qr-scan-section mt-4" style="border-top: 1px solid #eee; padding-top: 15px;">
        <button type="button" @click="startScan" class="btn-add-address-item" style="background-color: #1890ff; color: white;">
          <i class="fas fa-qrcode"></i> Quét QR lấy thông tin nhanh
        </button>
        <div v-if="isScanning" id="qr-reader-kh" style="max-width: 300px; margin-top: 10px;"></div>
        <button v-if="isScanning" type="button" @click="stopScan" class="btn-cancel-action" style="margin-top: 5px;">Tắt Camera</button>
      </div>

      <div class="form-section-card edit-form-card profile-grid">
        <div class="avatar-upload-block">
          <div class="section-sub-title fw-bold mb-3">Ảnh đại diện</div>
          <div class="avatar-preview-circle mb-3">
            <img v-if="customerForm.avatar" :src="customerForm.avatar" alt="Avatar" />
            <span v-else class="default-avatar-icon">👤</span>
          </div>
          <button type="button" class="btn-upload-avatar mb-2" @click="triggerFormFileInput">📁 Chọn ảnh</button>
          <input type="file" ref="formFileInputRef" style="display: none;" accept="image/jpeg, image/png, image/jpg" @change="handleFormAvatarChange" />
          <small class="upload-hint">JPG, PNG (tối đa 2MB)</small>
        </div>

        <div class="info-fields-block">
          <div class="section-sub-title fw-bold mb-3">Thông tin khách hàng</div>
          <div class="form-row">
            <div class="form-cell">
              <label>Họ và tên <span class="required">*</span></label>
              <input type="text" v-model="customerForm.hoTen" placeholder="Nhập họ và tên" />
            </div>
            <div class="form-cell">
              <label>Số điện thoại <span class="required">*</span></label>
              <input type="text" v-model="customerForm.sdt" placeholder="Nhập số điện thoại" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-cell">
              <label>Email</label>
              <input type="email" v-model="customerForm.email" placeholder="Nhập email" />
            </div>
            <div class="form-cell">
              <label>Ngày sinh</label>
              <input type="date" v-model="customerForm.ngaySinh" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-cell">
              <label>Giới tính</label>
              <div class="radio-flex border-box">
                <label><input type="radio" v-model="customerForm.gioiTinh" value="1" /> Nam</label>
                <label><input type="radio" v-model="customerForm.gioiTinh" value="0" /> Nữ</label>
              </div>
            </div>
            <div class="form-cell">
              <label>Trạng thái</label>
              <select v-model="customerForm.trangThai" class="select-box">
                <option :value="1">Hoạt động</option>
                <option :value="0">Ngừng hoạt động</option>
              </select>
            </div>
          </div>
        </div>
      </div>

      <div class="form-section-card address-list-section">
        <div class="address-section-header">
          <div class="section-sub-title fw-bold">Danh sách địa chỉ nhận hàng</div>
          <button class="btn-add-address-item" @click="addNewAddressRow">+ Thêm địa chỉ mới</button>
        </div>

        <div v-for="(addr, idx) in customerForm.addresses" :key="addr.id" class="address-item-card">
          <div class="address-item-title">
            <span>Khối địa chỉ {{ idx + 1 }}</span>
            <button v-if="customerForm.addresses.length > 1" class="btn-delete-address" @click="removeAddressRow(idx)">Xóa dòng này</button>
          </div>

          <div class="form-row">
            <div class="form-cell">
              <label>Tỉnh / Thành phố <span class="required">*</span></label>
              <select v-model="addr.tinhThanh" class="select-box" @change="handleThayDoiTinhThanhForm(addr)">
                <option value="">-- Chọn Tỉnh / Thành phố --</option>
                <option v-for="tinh in listTinhThanhAPI" :key="tinh.code" :value="tinh.name">{{ tinh.name }}</option>
              </select>
            </div>
            <div class="form-cell">
              <label>Quận / Huyện <span class="required">*</span></label>
              <select v-model="addr.quanHuyen" class="select-box" :disabled="!addr.tinhThanh" @change="handleThayDoiQuanHuyenForm(addr)">
                <option value="">-- Chọn Quận / Huyện --</option>
                <option v-for="quan in addr.listQuanHuyenTmp" :key="quan.code" :value="quan.name">{{ quan.name }}</option>
              </select>
            </div>
          </div>

          <div class="form-row">
            <div class="form-cell">
              <label>Phường / Xã / Thị trấn <span class="required">*</span></label>
              <select v-model="addr.phuongXa" class="select-box" :disabled="!addr.quanHuyen">
                <option value="">-- Chọn Phường / Xã --</option>
                <option v-for="xa in addr.listPhuongXaTmp" :key="xa.code" :value="xa.name">{{ xa.name }}</option>
              </select>
            </div>
            <div class="form-cell">
              <label>Tên người nhận <span class="required">*</span></label>
              <input type="text" v-model="addr.nguoiNhan" placeholder="Nhập tên người nhận" />
            </div>
          </div>

          <div class="form-row">
            <div class="form-cell">
              <label>SĐT người nhận <span class="required">*</span></label>
              <input type="text" v-model="addr.sdt" placeholder="Nhập số điện thoại nhận" />
            </div>
            <div class="form-cell">
              <label>Địa chỉ chi tiết <span class="required">*</span></label>
              <input type="text" v-model="addr.diaChiChiTiet" placeholder="Nhập số nhà, tên đường..." />
            </div>
          </div>

          <div class="checkbox-default-wrapper">
            <label class="toggle-control">
              <input type="checkbox" :checked="addr.macDinh" @change="setFormDefaultAddress(idx)" />
              <span class="control-label">Đặt làm địa chỉ mặc định</span>
            </label>
          </div>
        </div>
      </div>

      <div class="form-submit-bar">
        <button class="btn-cancel-action" @click="cancelFormMode">Hủy bỏ</button>
        <button class="btn-submit-action" @click="handleSubmitForm">Lưu thông tin</button>
      </div>
    </div>

    <div v-else>
      <div class="search-filter-box aerion-filter no-print" style="border: 1px solid #e0e0e0; border-radius: 6px; background: #fff; margin-bottom: 25px; box-shadow: 0 1px 4px rgba(0,0,0,0.03);">
        <div class="filter-header">
          <span class="filter-icon"><i class="fas fa-filter"></i></span>
          <h4 class="filter-title">Bộ lọc tìm kiếm nhanh</h4>
        </div>

        <div class="filter-row">
          <div class="filter-item search-input">
            <label>Họ và tên, sđt, email</label>
            <input type="text" v-model="filterParams.keyword" placeholder="Nhập từ khóa cần tìm..." @input="handleFilter" />
          </div>

          <div class="filter-item gender-radio">
            <label>Giới tính</label>
            <div class="radio-group">
              <label><input type="radio" v-model="filterParams.gioiTinh" value="" @change="handleFilter" /> Tất cả</label>
              <label><input type="radio" v-model="filterParams.gioiTinh" value="1" @change="handleFilter" /> Nam</label>
              <label><input type="radio" v-model="filterParams.gioiTinh" value="0" @change="handleFilter" /> Nữ</label>
            </div>
          </div>

          <div class="filter-item date-input">
            <label>Ngày sinh</label>
            <input type="date" v-model="filterParams.ngaySinh" @change="handleFilter" />
          </div>

          <div class="filter-item status-radio">
            <label>Trạng thái</label>
            <div class="radio-group">
              <label><input type="radio" v-model="filterParams.trangThai" value="" @change="handleFilter" /> Tất cả</label>
              <label><input type="radio" v-model="filterParams.trangThai" value="1" @change="handleFilter" /> Hoạt động</label>
              <label><input type="radio" v-model="filterParams.trangThai" value="0" @change="handleFilter" /> Ngừng</label>
            </div>
          </div>

          <button class="refresh-btn" title="Làm mới bộ lọc" @click="resetFilters">✕</button>
        </div>
      </div>

      <div class="table-box" style="background: white; border-radius: 10px; padding: 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); border: 1px solid #eee;">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 18px;">
          <div>
            <div style="font-size: 24px; font-weight: 700; color: #1f2937;">Danh sách khách hàng</div>
            <div style="font-size: 13px; color: #9ca3af; margin-top: 2px;">Quản lý thông tin khách hàng trên hệ thống</div>
          </div>

          <div style="display: flex; gap: 10px;">
            <button class="btn-cancel-action" style="background: #75c589; color: #fff; border: 1px solid #66b079; padding: 0 12px;" @click="handleExport">Xuất file</button>
            <button class="btn-cancel-action" style="background: #75c589; color: #fff; border: 1px solid #66b079; padding: 0 12px;" @click="handlePrint">In danh sách</button>
            <button @click="openAddForm" style="background-color: #f79b66; color: white; border: none; padding: 0 16px; height: 36px; border-radius: 4px; font-weight: bold; cursor: pointer;">
              + Thêm mới
            </button>
          </div>
        </div>

        <div class="table-responsive">
          <table class="customer-table">
            <thead>
              <tr>
                <th style="width: 5%">STT</th>
                <th style="width: 8%">Ảnh</th>
                <th style="width: 15%">Họ và tên</th>
                <th style="width: 8%">Giới tính</th>
                <th style="width: 10%">Ngày sinh</th>
                <th style="width: 12%">Số điện thoại</th>
                <th style="width: 14%">Email</th>
                <th style="width: 17%">Địa chỉ mặc định</th>
                <th style="width: 11%">Trạng thái</th>
                <th style="width: 11%">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(khachHang, index) in pagedKhachHang" :key="khachHang.id">
                <td class="text-center">{{ (currentPage - 1) * pageSize + index + 1 }}</td>
                <td class="text-center">
                  <div class="avatar-placeholder">
                    <img v-if="khachHang.avatar" :src="khachHang.avatar" alt="avatar" />
                    <span v-else>👤</span>
                  </div>
                </td>
                <td class="fw-bold">{{ khachHang.hoTen || 'Chưa cập nhật' }}</td>
                <td>{{ formatGioiTinh(khachHang.gioiTinh) }}</td>
                <td>{{ khachHang.ngaySinh || '-' }}</td>
                <td>{{ khachHang.sdt }}</td>
                <td>{{ khachHang.email || '-' }}</td>
                <td>{{ getDefaultAddressText(khachHang) }}</td>
                <td class="text-center">
                  <span :class="['status-badge', (khachHang.trangThai === true || khachHang.trangThai === 1) ? 'active' : 'inactive']" @click="toggleStatus(khachHang)">
                    {{ formatTrangThai(khachHang.trangThai) }}
                  </span>
                </td>
                <td class="text-center">
                  <div class="table-actions">
                    <button class="btn-view" @click="viewDetail(khachHang)" title="Xem chi tiết"><i class="far fa-eye"></i></button>
                    <button class="btn-action-location" @click="openAddressModal(khachHang)" title="Quản lý sổ địa chỉ"><i class="fas fa-map-marker-alt"></i></button>
                    <button class="btn-edit" @click="openEditForm(khachHang)" title="Sửa thông tin"><i class="far fa-edit"></i></button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="pagination" v-if="filteredKhachHang.length > 0">
          <span>Tổng số <strong>{{ filteredKhachHang.length }}</strong> khách hàng</span>
          <div class="page-controls">
            <button :disabled="currentPage === 1" @click="currentPage--">&lt;</button>
            <button v-for="page in totalPages" :key="page" :class="['page-num', { active: currentPage === page }]" @click="currentPage = page">{{ page }}</button>
            <button :disabled="currentPage === totalPages" @click="currentPage++">&gt;</button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isAddressModalOpen" class="modal-overlay" @click.self="closeAddressModal">
      <div class="modal-container">
        <div class="modal-header">
          <div class="modal-title">
            <h3>Sổ địa chỉ khách hàng</h3>
            <small>{{ selectedCustomer?.hoTen }} - {{ selectedCustomer?.sdt }}</small>
          </div>
          <button class="close-btn" @click="closeAddressModal">&times;</button>
        </div>

        <div class="modal-body-layout">
          <div class="left-section">
            <h4 class="section-title">Danh sách địa chỉ hiện có</h4>
            <div class="address-table-wrapper">
              <table class="sub-table">
                <thead>
                  <tr>
                    <th>STT</th>
                    <th>Người nhận</th>
                    <th>SĐT</th>
                    <th>Địa chỉ</th>
                    <th>Mặc định</th>
                    <th>Xóa</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(addr, idx) in pagedAddresses" :key="idx">
                    <td>{{ (subCurrentPage - 1) * subPageSize + idx + 1 }}</td>
                    <td>{{ addr.nguoiNhan }}</td>
                    <td>{{ addr.sdt }}</td>
                    <td>{{ addr.diaChiChiTiet }}<span v-if="addr.phuongXa">, {{ addr.phuongXa }}</span><span v-if="addr.tinhThanh">, {{ addr.tinhThanh }}</span></td>
                    <td class="text-center">
                      <span class="star-icon" :class="{ active: addr.macDinh }" @click="setAsDefaultAddress(addr.id)">
                        {{ addr.macDinh ? '⭐' : '☆' }}
                      </span>
                    </td>
                    <td class="text-center">
                      <button class="btn-delete-address" @click="deleteAddress(idx)">Xóa</button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="sub-pagination" v-if="selectedCustomer?.addresses?.length > 0">
              <span>Tổng {{ selectedCustomer.addresses.length }} địa chỉ</span>
              <div class="sub-page-controls">
                <button :disabled="subCurrentPage === 1" @click="subCurrentPage--">&lt;</button>
                <span class="sub-page-display">{{ subCurrentPage }} / {{ subTotalPages }}</span>
                <button :disabled="subCurrentPage === subTotalPages" @click="subCurrentPage++">&gt;</button>
              </div>
            </div>
          </div>

          <div class="right-section">
            <h4 class="section-title">Thêm nhanh địa chỉ mới</h4>
            <div class="form-grid">
              <div class="form-group">
                <label>Tên người nhận <span class="required">*</span></label>
                <input type="text" v-model="newAddress.nguoiNhan" placeholder="Nhập tên..." />
              </div>
              <div class="form-group">
                <label>SĐT người nhận <span class="required">*</span></label>
                <input type="text" v-model="newAddress.sdt" placeholder="Nhập SĐT..." />
              </div>
            </div>
            <div class="form-grid">
              <div class="form-group">
                <label>Tỉnh / Thành phố <span class="required">*</span></label>
                <select v-model="newAddress.tinhThanh" @change="handleThayDoiTinhThanhModal">
                  <option value="">-- Chọn Tỉnh / Thành phố --</option>
                  <option v-for="tinh in listTinhThanhAPI" :key="tinh.code" :value="tinh.name">{{ tinh.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>Quận / Huyện <span class="required">*</span></label>
                <select v-model="newAddress.quanHuyen" :disabled="!newAddress.tinhThanh" @change="handleThayDoiQuanHuyenModal">
                  <option value="">-- Chọn Quận / Huyện --</option>
                  <option v-for="quan in listQuanHuyenModalTmp" :key="quan.code" :value="quan.name">{{ quan.name }}</option>
                </select>
              </div>
            </div>
            <div class="form-grid">
              <div class="form-group">
                <label>Phường / Xã <span class="required">*</span></label>
                <select v-model="newAddress.phuongXa" :disabled="!newAddress.quanHuyen">
                  <option value="">-- Chọn Phường / Xã --</option>
                  <option v-for="xa in listPhuongXaModalTmp" :key="xa.code" :value="xa.name">{{ xa.name }}</option>
                </select>
              </div>
              <div class="form-group">
                <label>Địa chỉ chi tiết <span class="required">*</span></label>
                <input type="text" v-model="newAddress.diaChiChiTiet" placeholder="Nhập số nhà, tên đường..." />
              </div>
            </div>
            <div class="form-group checkbox-group">
              <label class="toggle-control">
                <input type="checkbox" v-model="newAddress.macDinh" />
                <span class="control-label">Đặt làm địa chỉ mặc định</span>
              </label>
            </div>
            <div class="form-actions-right">
              <button class="btn-cancel-sub" @click="closeAddressModal">Hủy</button>
              <button class="btn-submit-sub" @click="handleSaveAddress">Thêm nhanh</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import MainLayout from '../layouts/MainLayout.vue'
import { onMounted, ref, computed, reactive } from 'vue'
import { fetchAllKhachHang, addKhachHang, updateKhachHang } from '@/service/KhachHangService'
import * as XLSX from 'xlsx'
import { Html5QrcodeScanner } from 'html5-qrcode'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const isScanning = ref(false)
let qrScanner = null
const listTinhThanhAPI = ref([])

const listQuanHuyenModalTmp = ref([])
const listPhuongXaModalTmp = ref([])

const listKhachHang = ref([])
const currentPage = ref(1)
const pageSize = ref(5)

const filterParams = ref({ keyword: '', gioiTinh: '', ngaySinh: '', trangThai: '' })
const errors = reactive({ tenKh: '', sdt: '', email: '' })

const customerForm = ref({ id: null, hoTen: '', sdt: '', email: '', ngaySinh: '', gioiTinh: '1', trangThai: 1, avatar: '', addresses: [] })

const isDetailMode = ref(false)
const customerDetail = ref({ hoTen: '', sdt: '', email: '', ngaySinh: '', gioiTinh: '1', trangThai: 1, avatar: '', addresses: [], rawOrigin: null })

const isFormMode = ref(false)
const formFileInputRef = ref(null)

const isAddressModalOpen = ref(false)
const selectedCustomer = ref(null)
const subCurrentPage = ref(1)
const subPageSize = ref(2)

const newAddress = ref({ nguoiNhan: '', sdt: '', tinhThanh: '', quanHuyen: '', phuongXa: '', diaChiChiTiet: '', macDinh: false })

const load63TinhThanhGlobal = async () => {
  try {
    const res = await axios.get('https://provinces.open-api.vn/api/p/')
    listTinhThanhAPI.value = res.data || []
  } catch (e) { 
    console.error("Lỗi khi gọi API tỉnh thành: ", e) 
  }
}

const handleThayDoiTinhThanhForm = async (addr) => {
  addr.quanHuyen = ''; addr.phuongXa = ''; addr.listQuanHuyenTmp = []; addr.listPhuongXaTmp = [];
  if (!addr.tinhThanh) return
  const tinh = listTinhThanhAPI.value.find(t => t.name === addr.tinhThanh)
  if (tinh) {
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${tinh.code}?depth=2`)
    addr.listQuanHuyenTmp = res.data.districts || []
  }
}

const handleThayDoiQuanHuyenForm = async (addr) => {
  addr.phuongXa = ''; addr.listPhuongXaTmp = [];
  if (!addr.quanHuyen) return
  const quan = addr.listQuanHuyenTmp.find(q => q.name === addr.quanHuyen)
  if (quan) {
    const res = await axios.get(`https://provinces.open-api.vn/api/d/${quan.code}?depth=2`)
    addr.listPhuongXaTmp = res.data.wards || []
  }
}

const handleThayDoiTinhThanhModal = async () => {
  newAddress.value.quanHuyen = ''; newAddress.value.phuongXa = ''; listQuanHuyenModalTmp.value = []; listPhuongXaModalTmp.value = [];
  if (!newAddress.value.tinhThanh) return
  const tinh = listTinhThanhAPI.value.find(t => t.name === newAddress.value.tinhThanh)
  if (tinh) {
    const res = await axios.get(`https://provinces.open-api.vn/api/p/${tinh.code}?depth=2`)
    listQuanHuyenModalTmp.value = res.data.districts || []
  }
}

const handleThayDoiQuanHuyenModal = async () => {
  newAddress.value.phuongXa = ''; listPhuongXaModalTmp.value = [];
  if (!newAddress.value.quanHuyen) return
  const quan = listQuanHuyenModalTmp.value.find(q => q.name === newAddress.value.quanHuyen)
  if (quan) {
    const res = await axios.get(`https://provinces.open-api.vn/api/d/${quan.code}?depth=2`)
    listPhuongXaModalTmp.value = res.data.wards || []
  }
}

const startScan = () => {
  isScanning.value = true
  setTimeout(() => {
    qrScanner = new Html5QrcodeScanner("qr-reader-kh", { fps: 10, qrbox: { width: 250, height: 250 } }, false)
    qrScanner.render(onScanSuccess, () => {})
  }, 300)
}
const stopScan = () => { if (qrScanner) qrScanner.clear().then(() => isScanning.value = false) }

const onScanSuccess = (decodedText) => {
  alert('Quét thông tin thành công!')
  stopScan()
  if (decodedText.includes('|')) {
    const parts = decodedText.split('|')
    if (parts.length >= 6) {
      customerForm.value.hoTen = parts[2]
      if (parts[3]?.length === 8) {
        customerForm.value.ngaySinh = `${parts[3].substring(4, 8)}-${parts[3].substring(2, 4)}-${parts[3].substring(0, 2)}`
      }
      customerForm.value.gioiTinh = parts[4] === 'Nam' ? '1' : '0'
      if (customerForm.value.addresses.length > 0) {
        customerForm.value.addresses[0].diaChiChiTiet = parts[5]
      }
    }
  }
}

const handleFetchAllData = async () => {
  try {
    const data = await fetchAllKhachHang()
    const rawList = Array.isArray(data) ? data : []
    listKhachHang.value = rawList.map(kh => ({
      ...kh,
      addresses: Array.isArray(kh.addresses) ? kh.addresses.map(addr => ({
        ...addr,
        diaChi: addr.diaChi || [addr.diaChiChiTiet, addr.phuongXa, addr.tinhThanh].filter(Boolean).join(', '),
        macDinh: addr.macDinh ?? false
      })) : []
    }))
  } catch (e) { console.error(e) }
}

onMounted(async () => {
  await load63TinhThanhGlobal() 
  await handleFetchAllData()
})

const triggerFormFileInput = () => { if (formFileInputRef.value) formFileInputRef.value.click() }
const handleFormAvatarChange = (e) => {
  const f = e.target.files[0]; if (!f) return
  if (f.size > 2*1024*1024) return alert('Dung lượng ảnh tối đa 2MB!')
  const r = new FileReader(); r.onload = (ev) => customerForm.value.avatar = ev.target.result; r.readAsDataURL(f)
}

const handlePrint = () => { window.print() }
const handleExport = () => {
  if (filteredKhachHang.value.length === 0) return alert('Không có dữ liệu!')
  const data = filteredKhachHang.value.map((item, idx) => ({
    'STT': idx + 1, 'Họ và Tên': item.hoTen, 'Số Điện Thoại': item.sdt, 'Email': item.email, 'Giới Tính': formatGioiTinh(item.gioiTinh), 'Địa Chi': getDefaultAddressText(item)
  }))
  const ws = XLSX.utils.json_to_sheet(data); const wb = XLSX.utils.book_new(); XLSX.utils.book_append_sheet(wb, ws, 'KhachHang'); XLSX.writeFile(wb, 'Danh_Sach_Khach_Hang.xlsx')
}

const viewDetail = (kh) => {
  customerDetail.value = { ...kh, rawOrigin: kh, addresses: kh.addresses || [] }
  isDetailMode.value = true
}
const cancelDetailMode = () => isDetailMode.value = false
const switchToEditFromDetail = (raw) => { isDetailMode.value = false; openEditForm(raw) }

const openAddForm = () => {
  customerForm.value = { 
    id: null, hoTen: '', sdt: '', email: '', ngaySinh: '', gioiTinh: '1', trangThai: 1, avatar: '', 
    addresses: [{ 
      id: 'ADDR_NEW_' + Date.now(),
      tinhThanh: '', quanHuyen: '', phuongXa: '', diaChiChiTiet: '', nguoiNhan: '', sdt: '', macDinh: true, 
      listQuanHuyenTmp: [], listPhuongXaTmp: [] 
    }] 
  }
  isFormMode.value = true
}

const openEditForm = (khachHang) => {
  router.push({
    name: 'sua-khach-hang',
    params: { id: khachHang.id }
  })
}

const cancelFormMode = () => isFormMode.value = false
const addNewAddressRow = () => {
  customerForm.value.addresses.push({ id: 'ADDR_ADD_' + Date.now() + Math.random(), tinhThanh: '', quanHuyen: '', phuongXa: '', diaChiChiTiet: '', nguoiNhan: '', sdt: '', macDinh: customerForm.value.addresses.length === 0, listQuanHuyenTmp: [], listPhuongXaTmp: [] })
}
const setFormDefaultAddress = (index) => { customerForm.value.addresses.forEach((a, idx) => a.macDinh = idx === index) }
const removeAddressRow = (index) => { const wasDef = customerForm.value.addresses[index].macDinh; customerForm.value.addresses.splice(index, 1); if (wasDef && customerForm.value.addresses.length > 0) customerForm.value.addresses[0].macDinh = true }

const handleSubmitForm = async () => {
  if (!customerForm.value.hoTen?.trim() || !customerForm.value.sdt?.trim()) return alert('Vui lòng điền đủ các trường bắt buộc (*)!')
  if (customerForm.value.addresses.some(a => !a.tinhThanh || !a.quanHuyen || !a.phuongXa || !a.nguoiNhan || !a.sdt || !a.diaChiChiTiet.trim())) return alert('Vui lòng điền đầy đủ mọi ô trống địa chỉ!')

  const processed = customerForm.value.addresses.map(addr => {
    // Tách bỏ đuôi timestamp lúc nãy thêm vào id để gửi dữ liệu payload sạch về Backend
    let cleanId = addr.id;
    if(typeof cleanId === 'string' && cleanId.includes('_')) {
       cleanId = cleanId.split('_')[0];
       if(cleanId.startsWith('ADDR')) cleanId = null; // Nếu là ID tạm thời sinh ra ở FE thì truyền null xuống để DB tự sinh
    }
    return {
      id: cleanId,
      nguoiNhan: addr.nguoiNhan.trim(),
      sdt: addr.sdt.trim(),
      tinhThanh: addr.tinhThanh,
      phuongXa: `${addr.phuongXa}, ${addr.quanHuyen}`,
      diaChiChiTiet: addr.diaChiChiTiet.trim(),
      macDinh: addr.macDinh ? 1 : 0
    }
  })

  const payload = {
    hoTen: customerForm.value.hoTen.trim(), sdt: customerForm.value.sdt.trim(), email: customerForm.value.email || null, ngaySinh: customerForm.value.ngaySinh || null,
    gioiTinh: Number(customerForm.value.gioiTinh), trangThai: Number(customerForm.value.trangThai), avatar: customerForm.value.avatar || null,
    addresses: processed
  }

  try {
    if (customerForm.value.id) {
      await updateKhachHang(customerForm.value.id, payload)
    } else {
      await addKhachHang(payload)
    }
    alert('Lưu thông tin thành công! 🎉')
    await handleFetchAllData()
    isFormMode.value = false
  } catch (e) { alert('Thao tác thất bại!') }
}

const openAddressModal = (kh) => {
  selectedCustomer.value = { ...kh, addresses: kh.addresses || [] }
  subCurrentPage.value = 1
  isAddressModalOpen.value = true
}
const closeAddressModal = () => { isAddressModalOpen.value = false; selectedCustomer.value = null }

const deleteAddress = async (idx) => {
  if (!confirm("Bạn có chắc chắn muốn xóa địa chỉ này?")) return
  selectedCustomer.value.addresses.splice((subCurrentPage.value - 1) * subPageSize.value + idx, 1)
  const payload = { ...selectedCustomer.value, gioiTinh: Number(selectedCustomer.value.gioiTinh), trangThai: selectedCustomer.value.trangThai ? 1 : 0 }
  try { await updateKhachHang(selectedCustomer.value.id, payload); await handleFetchAllData() } catch (e) { alert('Không thể xóa!') }
}

const setAsDefaultAddress = async (id) => {
  selectedCustomer.value.addresses.forEach(a => a.macDinh = a.id === id)
  const payload = { ...selectedCustomer.value, gioiTinh: Number(selectedCustomer.value.gioiTinh), trangThai: selectedCustomer.value.trangThai ? 1 : 0 }
  try { await updateKhachHang(selectedCustomer.value.id, payload); alert('Đổi địa chỉ mặc định thành công!'); await handleFetchAllData() } catch (e) { alert('Thất bại!') }
}

const handleSaveAddress = async () => {
  if (!newAddress.value.tinhThanh || !newAddress.value.quanHuyen || !newAddress.value.phuongXa || !newAddress.value.nguoiNhan || !newAddress.value.sdt || !newAddress.value.diaChiChiTiet.trim()) return alert('Điền thiếu thông tin!')
  
  const fresh = {
    id: 'ADDR_' + Date.now(), tinhThanh: newAddress.value.tinhThanh, phuongXa: `${newAddress.value.phuongXa}, ${newAddress.value.quanHuyen}`,
    diaChiChiTiet: newAddress.value.diaChiChiTiet.trim(), nguoiNhan: newAddress.value.nguoiNhan.trim(), sdt: newAddress.value.sdt.trim(), macDinh: newAddress.value.macDinh
  }
  if (newAddress.value.macDinh) selectedCustomer.value.addresses.forEach(a => a.macDinh = false)
  selectedCustomer.value.addresses.push(fresh)

  const payload = { ...selectedCustomer.value, gioiTinh: Number(selectedCustomer.value.gioiTinh), trangThai: selectedCustomer.value.trangThai ? 1 : 0 }
  try { await updateKhachHang(selectedCustomer.value.id, payload); alert('Thêm nhanh thành công!'); await handleFetchAllData(); closeAddressModal() } catch (e) { alert('Lỗi!') }
}

const resetFilters = () => { filterParams.value = { keyword: '', gioiTinh: '', ngaySinh: '', trangThai: '' }; currentPage.value = 1 }
const toggleStatus = async (kh) => {
  const next = kh.trangThai ? 0 : 1
  const payload = { ...kh, trangThai: next, gioiTinh: Number(kh.gioiTinh) }
  try { await updateKhachHang(kh.id, payload); kh.trangThai = next; alert('Đổi trạng thái thành công!') } catch (e) { alert('Lỗi!') }
}

const getDefaultAddressText = (kh) => kh.addresses?.find(a => a.macDinh)?.diaChi || kh.addresses[0]?.diaChi || kh.diaChi || '-'
const formatGioiTinh = (g) => Number(g) === 1 ? 'Nam' : 'Nữ'
const formatTrangThai = (t) => (t === true || Number(t) === 1) ? 'Hoạt động' : 'Ngừng hoạt động'

const filteredKhachHang = computed(() => {
  return listKhachHang.value.filter(kh => {
    const kw = filterParams.value.keyword.trim().toLowerCase()
    const matchKw = !kw || kh.hoTen?.toLowerCase().includes(kw) || kh.sdt?.includes(kw) || kh.email?.toLowerCase().includes(kw)
    const matchGt = filterParams.value.gioiTinh === '' || String(kh.gioiTinh) === String(filterParams.value.gioiTinh)
    const matchNs = !filterParams.value.ngaySinh || kh.ngaySinh === filterParams.value.ngaySinh
    const matchTt = filterParams.value.trangThai === '' || ((kh.trangThai ? 1 : 0) === Number(filterParams.value.trangThai))
    return matchKw && matchGt && matchNs && matchTt
  })
})

const totalPages = computed(() => Math.ceil(filteredKhachHang.value.length / pageSize.value) || 1)
const pagedKhachHang = computed(() => filteredKhachHang.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value))
const subTotalPages = computed(() => Math.ceil(selectedCustomer.value?.addresses?.length / subPageSize.value) || 1)
const pagedAddresses = computed(() => selectedCustomer.value?.addresses ? selectedCustomer.value.addresses.slice((subCurrentPage.value - 1) * subPageSize.value, subCurrentPage.value * subPageSize.value) : [])
</script>

<style scoped>
/* Giữ nguyên 100% CSS Scoped cá tính nguyên bản của bạn */
.table-responsive { width: 100% !important; max-width: 100% !important; min-width: 100% !important; box-sizing: border-box !important; display: block !important; }
.customer-table { width: 100% !important; min-width: 1100px !important; table-layout: fixed !important; border-collapse: collapse !important; }
.customer-table td { padding: 12px 10px !important; vertical-align: middle; }
.search-filter-box.aerion-filter { background: #ffffff; border: 1px solid #e8e8e8; border-radius: 10px; margin-bottom: 20px; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05); overflow: hidden; padding: 0 !important; }
.filter-header { background-color: #f79b66; padding: 12px 20px; display: flex; align-items: center; gap: 8px; }
.filter-title { color: #ffffff !important; font-weight: 600; font-size: 14.5px; margin: 0 !important; }
.filter-icon { color: #ffffff; font-size: 14px; }
.filter-row { display: flex; align-items: flex-end; gap: 15px; flex-wrap: wrap; padding: 20px; }
.filter-item { display: flex; flex-direction: column; gap: 6px; }
.filter-item label { font-size: 13px; color: #666; font-weight: 500; margin: 0; }
.search-input { flex: 2; min-width: 240px; }
.gender-radio, .status-radio { flex: 1.5; min-width: 210px; }
.date-input { flex: 1; min-width: 140px; }
.filter-item input[type="text"], .filter-item input[type="date"] { width: 100%; height: 38px; padding: 0 12px; border: 1px solid #ddd; border-radius: 6px; outline: none; font-size: 13px; box-sizing: border-box; transition: border-color 0.2s; background-color: #fff; }
.filter-item input[type="text"]:focus, .filter-item input[type="date"]:focus { border-color: #f79b66; }
.radio-group { display: flex; align-items: center; gap: 12px; height: 38px; box-sizing: border-box; }
.radio-group label { display: inline-flex; align-items: center; gap: 5px; font-size: 13.5px; color: #333; cursor: pointer; white-space: nowrap; font-weight: normal !important; margin: 0 !important; }
.radio-group input[type="radio"] { margin: 0; width: 16px; height: 16px; accent-color: #f79b66; cursor: pointer; }
.refresh-btn { background: #f5f5f5; border: 1px solid #ddd; border-radius: 6px; cursor: pointer; height: 38px; width: 38px; min-width: 38px; display: flex; align-items: center; justify-content: center; box-sizing: border-box; transition: all 0.2s; font-size: 14px; }
.refresh-btn:hover { background: #ebebeb; border-color: #ccc; }
.disabled-input { background-color: #f5f7fa !important; color: #555 !important; cursor: not-allowed; border: 1px solid #e4e7ed !important; }
.customer-form-container { background-color: #f9fbfd; padding: 10px 20px 40px 20px; }
.back-header { margin-bottom: 20px; text-align: left; }
.btn-back-link { background: none; border: none; color: #909399; cursor: pointer; font-size: 14px; padding: 0; margin-bottom: 5px; }
.btn-back-link:hover { color: #1a2942; text-decoration: underline; }
.page-form-title { margin: 0; font-size: 18px; color: #303133; font-weight: 500; text-align: left; }
.form-section-card { background: #ffffff; border: 1px solid #e4e7ed; border-radius: 8px; padding: 24px; margin-bottom: 24px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02); }
.profile-grid { display: flex; gap: 40px; }
.avatar-upload-block { width: 200px; display: flex; flex-direction: column; align-items: center; border-right: 1px solid #f2f6fc; padding-right: 30px; }
.avatar-preview-circle { width: 110px; height: 110px; border-radius: 50%; background-color: #f5f7fa; display: flex; justify-content: center; align-items: center; overflow: hidden; border: 1px solid #e4e7ed; margin-bottom: 15px; }
.avatar-preview-circle img { width: 100%; height: 100%; object-fit: cover; }
.default-avatar-icon { font-size: 50px; color: #c0c4cc; }
.btn-upload-avatar { background: #ffffff; border: 1px dashed #dcdfe6; padding: 6px 16px; border-radius: 4px; font-size: 13px; cursor: pointer; }
.upload-hint { color: #909399; font-size: 11px; margin-top: 8px; }
.info-fields-block { flex: 1; }
.section-sub-title { font-size: 16px; color: #303133; font-weight: 600; text-align: left; }
.form-row { display: flex; gap: 20px; margin-bottom: 16px; }
.form-cell { flex: 1; display: flex; flex-direction: column; gap: 6px; text-align: left; }
.form-cell label, .form-group label { font-size: 13px; color: #606266; font-weight: 500; }
.form-cell input[type="text"], .form-cell input[type="email"], .form-cell input[type="date"], .select-box { padding: 9px 12px; border: 1px solid #dcdfe6; border-radius: 4px; font-size: 13px; outline: none; width: 100%; box-sizing: border-box; height: 38px; }
.radio-flex { display: flex; gap: 20px; padding: 8px 12px; background: #ffffff; height: 38px; align-items: center; }
.radio-flex label { display: flex; align-items: center; gap: 6px; cursor: pointer; font-size: 13px; }
.border-box { border: 1px solid #dcdfe6; border-radius: 4px; }
.address-section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.btn-add-address-item { background-color: #f79b66; border: none; color: #ffffff; padding: 6px 14px; border-radius: 4px; font-size: 13px; cursor: pointer; }
.address-item-card { border: 1px solid #ebeef5; border-radius: 6px; padding: 16px; background-color: #fafafa; margin-bottom: 16px; }
.address-item-title { display: flex; justify-content: space-between; font-weight: bold; font-size: 13px; color: #606266; margin-bottom: 12px; }
.btn-delete-address { background: none; border: none; color: #f79b66; cursor: pointer; font-size: 13px; }
.toggle-control { display: inline-flex; align-items: center; gap: 8px; cursor: pointer; font-size: 13px; color: #606266; }
.form-submit-bar { display: flex; justify-content: flex-end; gap: 12px; margin-top: 10px; }
.btn-cancel-action { background-color: #ffffff; border: 1px solid #dcdfe6; color: #606266; padding: 10px 24px; border-radius: 4px; cursor: pointer; height: 38px; display: inline-flex; align-items: center; }
.btn-submit-action { background-color: #1a2942; border: none; color: #ffffff; padding: 10px 24px; border-radius: 4px; cursor: pointer; height: 38px; display: inline-flex; align-items: center; }
.table-box { background: #fff; border-radius: 12px; padding: 0; overflow: hidden; border: 1px solid #ececec; }
.customer-table, table { width: 100%; border-collapse: collapse; }
.customer-table thead th, table thead th { background: #f79b66; color: white; font-weight: 600; font-size: 14px; padding: 14px 10px; text-align: center; border: none; }
.customer-table tbody td, table tbody td { padding: 14px 10px; font-size: 14px; color: #333; vertical-align: middle; border-bottom: 1px solid #f0f0f0; text-align: left; }
.customer-table tbody tr:hover, table tbody tr:hover { background: #fafafa; }
.avatar-placeholder { width: 40px; height: 40px; border-radius: 50%; overflow: hidden; background: #e0e0e0; margin: auto; display: flex; align-items: center; justify-content: center; }
.avatar-placeholder img { width: 100%; height: 100%; object-fit: cover; }
.status-badge { display: inline-block; padding: 5px 12px; border-radius: 16px; font-size: 12px; font-weight: 500; }
.status-badge.active { background: #e8f7ee; color: #16a34a; }
.status-badge.inactive { background: #fee2e2; color: #dc2626; }
.table-actions { display: flex; justify-content: center; align-items: center; gap: 8px; }
.table-actions button { width: 34px; height: 34px; border-radius: 6px; cursor: pointer; padding: 0; }
.btn-view { background: #f0fdf4 !important; border: 1px solid #a3e635 !important; color: #16a34a !important; }
.btn-action-location { background: #eff6ff !important; border: 1px solid #bfdbfe !important; color: #1d4ed8 !important; }
.btn-action-edit { background: #fff7ed !important; border: 1px solid #fed7aa !important; color: #ea580c !important; }
.pagination { display: flex; justify-content: space-between; align-items: center; padding: 15px; background: #fff; }
.page-controls { display: flex; align-items: center; gap: 6px; }
.page-controls button { min-width: 34px; height: 34px; border: 1px solid #ddd; background: #fff; border-radius: 6px; cursor: pointer; }
.page-controls .active { background: #f79b66; border-color: #f79b66; color: #fff; }
.no-data { text-align: center; color: #666; font-style: italic; padding: 25px; }
.modal-overlay { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; background-color: rgba(0, 0, 0, 0.4); display: flex; justify-content: center; align-items: center; z-index: 2000; }
.modal-container { background: #ffffff; border-radius: 8px; width: 1050px; max-width: 95%; box-shadow: 0 4px 24px rgba(0, 0, 0, 0.15); display: flex; flex-direction: column; overflow: hidden; }
.modal-header { display: flex; justify-content: space-between; align-items: center; padding: 14px 20px; border-bottom: 1px solid #ebeef5; text-align: left; }
.modal-title h3 { margin: 0; font-size: 16px; color: #303133; }
.modal-title small { color: #909399; font-size: 13px; }
.close-btn { background: none; border: none; font-size: 24px; color: #909399; cursor: pointer; }
.modal-body-layout { display: flex; flex-direction: row; min-height: 380px; }
.left-section { flex: 1.15; padding: 20px; border-right: 1px solid #ebeef5; background-color: #fafafa; text-align: left; }
.right-section { flex: 0.85; padding: 20px; text-align: left; }
.section-title { font-size: 14px; margin-top: 0; margin-bottom: 16px; color: #303133; font-weight: 600; display: flex; align-items: center; gap: 6px; }
.address-table-wrapper { background: #ffffff; border: 1px solid #ebeef5; border-radius: 4px; min-height: 180px; }
.sub-table { width: 100%; border-collapse: collapse; }
.sub-table th { background-color: #f5f7fa; color: #606266; text-align: left; padding: 10px; font-size: 13px; border-bottom: 1px solid #ebeef5; }
.sub-table td { padding: 12px 10px; font-size: 13px; border-bottom: 1px solid #ebeef5; color: #606266; vertical-align: middle; }
.star-icon { font-size: 18px; cursor: pointer; color: #c0c4cc; user-select: none; transition: transform 0.1s ease; }
.star-icon:hover { transform: scale(1.2); }
.star-icon.active { color: #f79b66; }
.badge-active { background-color: #e1f3d8; color: #67c23a; padding: 2px 8px; border-radius: 4px; font-size: 12px; }
.sub-pagination { display: flex; justify-content: space-between; align-items: center; margin-top: 12px; font-size: 13px; color: #606266; }
.sub-page-controls { display: flex; align-items: center; gap: 8px; }
.sub-page-controls button { background-color: #ffffff; border: 1px solid #dcdfe6; border-radius: 4px; padding: 2px 8px; cursor: pointer; }
.sub-page-controls button:disabled { color: #c0c4cc; cursor: not-allowed; }
.form-grid { display: flex; gap: 16px; margin-bottom: 14px; }
.form-group { flex: 1; display: flex; flex-direction: column; gap: 6px; margin-bottom: 14px; text-align: left; }
.form-actions-right { display: flex; justify-content: flex-end; gap: 10px; margin-top: 35px; }
.btn-cancel-sub { background-color: #ffffff; border: 1px solid #dcdfe6; color: #606266; padding: 8px 18px; border-radius: 4px; cursor: pointer; font-size: 13px; }
.btn-submit-sub { background-color: #1a2942; border: none; color: #ffffff; padding: 8px 18px; border-radius: 4px; cursor: pointer; font-size: 13px; }
.btn-submit-sub:hover { background-color: #2c3e50; }
.checkbox-default-wrapper { text-align: left; margin-top: 10px; }
.required { color: #f79b66; }
.mb-3 { margin-bottom: 1rem; }
.mb-2 { margin-bottom: 0.5rem; }
.fw-bold { font-weight: bold; }
.text-center { text-align: center !important; }
</style>