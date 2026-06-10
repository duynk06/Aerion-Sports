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
          <span :class="['status-badge', customerDetail.trangThai === 1 ? 'active' : 'inactive']"
            style="margin-top: 15px; display: inline-block;">
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
              <input type="text" :value="Number(customerDetail.gioiTinh) === 1 ? 'Nam' : 'Nữ'" disabled
                class="disabled-input" />
            </div>
            <div class="form-cell">
            </div>
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
              <tr v-for="(addr, idx) in customerDetail.addresses" :key="addr.id || idx">
                <td>{{ idx + 1 }}</td>
                <td>
                  <div class="address-detail-text">
                    <span>🏠</span> {{ addr.diaChi || 'Chưa cập nhật' }}
                  </div>
                </td>
                <td class="text-center">
                  <span v-if="addr.isDefault" class="badge-active"
                    style="background-color: #e6f7ff; color: #1890ff; border: 1px solid #91d5ff; padding: 3px 8px; border-radius: 4px; font-size: 12px;">
                    ⭐ Mặc định
                  </span>
                  <span v-else style="color: #999; font-size: 12px;">Phụ</span>
                </td>
              </tr>
              <tr v-if="customerDetail.addresses.length === 0">
                <td colspan="3" class="no-data" style="padding: 20px;">Khách hàng này chưa cập nhật địa chỉ nào.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="form-submit-bar">
        <button class="btn-cancel-action" @click="cancelDetailMode" style="background: #6c757d; color: white;">Đóng
          lại</button>
        <button class="btn-submit-action" @click="switchToEditFromDetail(customerDetail.rawOrigin)">Chỉnh sửa thông
          tin</button>
      </div>
    </div>

    <div v-else-if="isFormMode" class="customer-form-container">
      <div class="back-header">
        <button class="btn-back-link" @click="cancelFormMode">← Quay lại danh sách</button>
        <h3 class="page-form-title">
          {{ customerForm.id ? 'Cập nhật thông tin khách hàng' : 'Nhập thông tin khách hàng mới' }}
        </h3>
      </div>

      <div class="form-section-card profile-grid">
        <div class="avatar-upload-block">
          <div class="section-sub-title fw-bold mb-3">Ảnh đại diện</div>
          <div class="avatar-preview-circle">
            <img v-if="customerForm.avatar" :src="customerForm.avatar" alt="Avatar" />
            <span v-else class="default-avatar-icon">👤</span>
          </div>
          
          <button type="button" class="btn-select-image" @click="triggerFileInput">📷 Chọn ảnh</button>
          
          <input 
            type="file" 
            ref="fileInput" 
            style="display: none;" 
            accept="image/jpeg, image/png" 
            @change="handleAvatarChange" 
          />
          
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
          <div class="section-sub-title fw-bold">Danh sách địa chỉ</div>
          <button class="btn-add-address-item" @click="addNewAddressRow">+ Thêm địa chỉ</button>
        </div>

        <div v-for="(addr, idx) in customerForm.addresses" :key="addr.id || idx" class="address-item-card">
          <div class="address-item-title">
            <span>📍 Địa chỉ {{ idx + 1 }}</span>
            <button v-if="customerForm.addresses.length > 1" class="btn-delete-address" @click="removeAddressRow(idx)">
              Xóa dòng này
            </button>
          </div>

          <div class="form-row">
            <div class="form-cell">
              <label>Tỉnh / Thành phố <span class="required">*</span></label>
              <select v-model="addr.tinhThanh" class="select-box">
                <option value="">-- Chọn Tỉnh / Thành phố --</option>
                <option value="Hà Nội">Hà Nội</option>
                <option value="Cao Bằng">Cao Bằng</option>
                <option value="Hồ Chí Minh">Hồ Chí Minh</option>
              </select>
            </div>
            <div class="form-cell">
              <label>Phường / Xã <span class="required">*</span></label>
              <select v-model="addr.phuongXa" class="select-box" :disabled="!addr.tinhThanh">
                <option value="">-- Chọn Phường / Xã --</option>
                <option value="Phường Văn Quán">Phường Văn Quán</option>
                <option value="Phường Sông Hiến">Phường Sông Hiến</option>
                <option value="Phường Hàng Bông">Phường Hàng Bông</option>
              </select>
            </div>
          </div>

          <div class="form-group full-width mb-2">
            <label>Địa chỉ chi tiết <span class="required">*</span></label>
            <input type="text" v-model="addr.chiTiet" placeholder="Nhập số nhà, tên đường, ngõ ngách..." />
          </div>

          <div class="checkbox-default-wrapper">
            <label class="toggle-control">
              <input type="checkbox" :checked="addr.isDefault" @change="handleSetDefaultInForm(idx)" />
              <span class="control-label">Đặt làm địa chỉ mặc định</span>
            </label>
          </div>
        </div>

        <div v-if="customerForm.addresses.length === 0" class="no-data" style="padding: 20px 0;">
          Chưa có thông tin địa chỉ nào. Vui lòng bấm nút "+ Thêm địa chỉ".
        </div>
      </div>

      <div class="form-submit-bar">
        <button class="btn-cancel-action" @click="cancelFormMode">Hủy bỏ</button>
        <button class="btn-submit-action" @click="handleSubmitForm">Lưu thông tin</button>
      </div>
    </div>

    <div v-else>
      <div class="search-filter-box no-print"
        style="border: 1px solid #e0e0e0; border-radius: 6px; background: #fff; margin-bottom: 25px; box-shadow: 0 1px 4px rgba(0,0,0,0.03); padding: 15px;">

        <div class="filter-header"
          style="background-color: #f17236; color: #fff; padding: 10px 15px; font-weight: bold; font-size: 15px; display: flex; align-items: center; gap: 8px; border-radius: 4px;">
          <span class="filter-icon"><i class="fas fa-filter"></i></span>
          <span> Bộ lọc tìm kiếm</span>
        </div>

        <div class="filter-row"
          style="padding: 15px 0 0 0; display: flex; align-items: flex-end; flex-wrap: nowrap; gap: 15px; background-color: #fff;">
          <div class="filter-item search-input" style="flex: 1; min-width: 200px;">
            <label style="display: block; margin-bottom: 6px; color: #555; font-size: 13px;">Họ và tên, sđt,
              email</label>
            <input type="text" v-model="filterParams.keyword" placeholder="Nhập họ và tên, sđt, email..."
              @input="handleFilter"
              style="width: 100%; padding: 6px 12px; border: 1px solid #ccc; border-radius: 4px; height: 36px; box-sizing: border-box; font-size: 13px;" />
          </div>

          <div class="filter-item gender-radio" style="display: flex; flex-direction: column; gap: 6px;">
            <label style="font-size: 13px; color: #4b5563; font-weight: 500;">Giới tính</label>
            <div class="radio-group"
              style="display: flex; gap: 12px; height: 40px; align-items: center; white-space: nowrap;">
              <label style="display: flex; align-items: center; gap: 4px; cursor: pointer; font-size: 14px;"><input
                  type="radio" v-model="filterParams.gioiTinh" value="" @change="handleFilter" /> Tất cả</label>
              <label style="display: flex; align-items: center; gap: 4px; cursor: pointer; font-size: 14px;"><input
                  type="radio" v-model="filterParams.gioiTinh" value="1" @change="handleFilter" /> Nam</label>
              <label style="display: flex; align-items: center; gap: 4px; cursor: pointer; font-size: 14px;"><input
                  type="radio" v-model="filterParams.gioiTinh" value="0" @change="handleFilter" /> Nữ</label>
            </div>
          </div>

          <div class="filter-item date-input" style="display: flex; flex-direction: column; gap: 6px;">
            <label style="font-size: 13px; color: #4b5563; font-weight: 500;">Ngày sinh</label>
            <input type="date" v-model="filterParams.ngaySinh" @change="handleFilter"
              style="width: 100%; height: 40px; border: 1px solid #cbd5e1; border-radius: 6px; padding: 0 12px; font-size: 14px; box-sizing: border-box;" />
          </div>

          <div class="filter-item status-radio" style="display: flex; flex-direction: column; gap: 6px;">
            <label style="font-size: 13px; color: #4b5563; font-weight: 500;">Trạng thái</label>
            <div class="radio-group"
              style="display: flex; gap: 12px; height: 40px; align-items: center; white-space: nowrap;">
              <label style="display: flex; align-items: center; gap: 4px; cursor: pointer; font-size: 14px;"><input
                  type="radio" v-model="filterParams.trangThai" value="" @change="handleFilter" /> Tất cả</label>
              <label style="display: flex; align-items: center; gap: 4px; cursor: pointer; font-size: 14px;"><input
                  type="radio" v-model="filterParams.trangThai" value="true" @change="handleFilter" /> Hoạt động</label>
              <label
                style="display: flex; align-items: center; gap: 4px; cursor: pointer; font-size: 14px; white-space: nowrap;"><input
                  type="radio" v-model="filterParams.trangThai" value="false" @change="handleFilter" /> Ngừng hoạt
                động</label>
            </div>
          </div>

          <button class="refresh-btn" title="Làm mới bộ lọc" @click="resetFilters"
            style="height: 36px; width: 36px; padding: 0; display: flex; align-items: center; justify-content: center; border: 1px solid #ccc; background-color: #fff; border-radius: 4px; cursor: pointer; color: #f17236; box-sizing: border-box;">
            <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5"
              stroke-linecap="round" stroke-linejoin="round">
              <path d="M21.5 2v6h-6M21.34 15.57a10 10 0 1 1-.57-8.38l5.67-5.67" />
            </svg>
          </button>
        </div>
      </div>

      <div class="table-box"
        style="background: white; border-radius: 10px; padding: 24px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); border: 1px solid #eee; margin-top: 15px; width: 100%; max-width: 100%; display: block; clear: both;">

        <div
          style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 18px; width: 100%;">
          <div>
            <div style="font-size: 24px; font-weight: 700; color: #1f2937;">
              Danh sách khách hàng
            </div>
            <div style="font-size: 13px; color: #9ca3af; margin-top: 2px;">
              Quản lý thông tin khách hàng
            </div>
          </div>

          <div style="display: flex; gap: 10px;">
            <button class="export-btn" title="Xuất file Excel" @click="handleExport"
              style="background: #fff; border: 1px solid #ccc; width: 36px; height: 36px; padding: 0; border-radius: 4px; cursor: pointer; font-size: 14px; display: flex; align-items: center; justify-content: center; box-sizing: border-box;">📑</button>

            <button class="export-btn" title="In danh sách" @click="handlePrint"
              style="background: #fff; border: 1px solid #ccc; width: 36px; height: 36px; padding: 0; border-radius: 4px; cursor: pointer; font-size: 14px; display: flex; align-items: center; justify-content: center; box-sizing: border-box;">🖨️</button>

            <button @click="openAddForm"
              style="background-color: #f17236; color: white; border: none; padding: 0 16px; height: 36px; border-radius: 4px; font-weight: bold; cursor: pointer; font-size: 14px; display: flex; align-items: center; justify-content: center; gap: 4px; box-sizing: border-box;">
              + Thêm mới
            </button>
          </div>
        </div>

        <div class="table-responsive"
          style="border-radius: 8px; overflow-x: auto; border: 1px solid #eee; width: 100%; max-width: 100%; display: block; box-sizing: border-box;">
          <table class="customer-table"
            style="width: 100%; min-width: 1050px; border-collapse: collapse; table-layout: fixed; margin: 0;">
            <thead>
              <tr>
                <th scope="col"
                  style="width: 5%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: center; font-size: 13.5px; border: none;">
                  STT</th>
                <th scope="col"
                  style="width: 7%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: center; font-size: 13.5px; border: none;">
                  Ảnh</th>
                <th scope="col"
                  style="width: 15%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: left; font-size: 13.5px; border: none;">
                  Họ và tên</th>
                <th scope="col"
                  style="width: 8%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: left; font-size: 13.5px; border: none;">
                  Giới tính</th>
                <th scope="col"
                  style="width: 10%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: left; font-size: 13.5px; border: none;">
                  Ngày sinh</th>
                <th scope="col"
                  style="width: 12%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: left; font-size: 13.5px; border: none;">
                  Số điện thoại</th>
                <th scope="col"
                  style="width: 14%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: left; font-size: 13.5px; border: none;">
                  Email</th>
                <th scope="col"
                  style="width: 17%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: left; font-size: 13.5px; border: none;">
                  Địa chỉ</th>
                <th scope="col"
                  style="width: 11%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: center; font-size: 13.5px; border: none;">
                  Trạng thái</th>
                <th scope="col"
                  style="width: 11%; background: #f37d4b; color: white; font-weight: 600; padding: 14px 10px; text-align: center; font-size: 13.5px; border: none;">
                  Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(khachHang, index) in pagedKhachHang" :key="khachHang.id">
                <td
                  style="text-align: center; padding: 12px 10px; border-bottom: 1px solid #eee; font-size: 13.5px; vertical-align: middle;">
                  {{ (currentPage - 1) * pageSize + index + 1 }}
                </td>
                <td
                  style="padding: 12px 10px; border-bottom: 1px solid #eee; text-align: center; vertical-align: middle;">
                  <div class="avatar-placeholder"
                    style="width: 40px; height: 40px; background: #e0e0e0; border-radius: 50%; display: flex; align-items: center; justify-content: center; overflow: hidden; margin: 0 auto;">
                    <img v-if="khachHang.avatar" :src="khachHang.avatar" alt="avatar"
                      style="width: 100%; height: 100%; object-fit: cover;" />
                    <i v-else class="fas fa-user" style="font-size: 18px; color: #a0a0a0;"></i>
                  </div>
                </td>
                <td style="padding: 12px 10px; border-bottom: 1px solid #eee; vertical-align: middle;">
                  <div class="fw-bold"
                    style="font-weight: bold; color: #1a2942; font-size: 13.5px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                    {{ khachHang.hoTen || 'Chưa cập nhật' }}
                  </div>
                </td>
                <td
                  style="padding: 12px 10px; border-bottom: 1px solid #eee; font-size: 13.5px; vertical-align: middle;">
                  {{ formatGioiTinh(khachHang.gioiTinh) }}
                </td>
                <td
                  style="padding: 12px 10px; border-bottom: 1px solid #eee; font-size: 13.5px; vertical-align: middle;">
                  {{ khachHang.ngaySinh || '-' }}
                </td>
                <td class="phone-text"
                  style="padding: 12px 10px; border-bottom: 1px solid #eee; font-weight: 500; color: #222; font-size: 13.5px; vertical-align: middle;">
                  {{ khachHang.sdt }}
                </td>
                <td class="email-text"
                  style="padding: 12px 10px; border-bottom: 1px solid #eee; color: #555; font-size: 13.5px; vertical-align: middle; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                  {{ khachHang.email || '-' }}
                </td>
                <td class="address-text"
                  style="padding: 12px 10px; border-bottom: 1px solid #eee; color: #555; font-size: 13px; line-height: 1.4; vertical-align: middle; word-break: break-word;">
                  {{ getDefaultAddressText(khachHang) }}
                </td>

                <td
                  style="text-align: center; padding: 12px 10px; border-bottom: 1px solid #eee; vertical-align: middle;">
                  <span
                    :class="['status-badge', (khachHang.trangThai === true || Number(khachHang.trangThai) === 1) ? 'active' : 'inactive']"
                    style="cursor: pointer; display: inline-block;" @click="toggleStatus(khachHang)"
                    title="Bấm để đổi trạng thái">
                    {{ formatTrangThai(khachHang.trangThai) }}
                  </span>
                </td>

                <td
                  style="padding: 12px 10px; border-bottom: 1px solid #eee; text-align: center; vertical-align: middle;">
                  <div class="table-actions"
                    style="display: flex; gap: 6px; align-items: center; justify-content: center;">
                    <button class="btn-view" title="Xem chi tiết" @click="viewDetail(khachHang)"
                      style="background: #e6f7ff; border: 1px solid #91d5ff; color: #1890ff; padding: 5px 8px; border-radius: 4px; cursor: pointer; display: inline-flex; align-items: center; justify-content: center;">
                      <i class="far fa-eye"></i>
                    </button>
                    <button class="btn-history" title="Địa chỉ" @click="openAddressModal(khachHang)"
                      style="background: #f6ffed; border: 1px solid #b7eb8f; color: #52c41a; padding: 5px 8px; border-radius: 4px; cursor: pointer; display: inline-flex; align-items: center; justify-content: center;">
                      <i class="fas fa-map-marker-alt"></i>
                    </button>
                    <button class="btn-edit" title="Sửa thông tin" @click="openEditForm(khachHang)"
                      style="background: #fff7e6; border: 1px solid #ffd591; color: #fa8c16; padding: 5px 8px; border-radius: 4px; cursor: pointer; display: inline-flex; align-items: center; justify-content: center;">
                      <i class="far fa-edit"></i>
                    </button>
                  </div>
                </td>
              </tr>

              <tr v-if="filteredKhachHang.length === 0">
                <td colspan="10" class="no-data"
                  style="text-align: center; color: #999; font-style: italic; padding: 30px;">Không tìm thấy khách hàng
                  phù hợp</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="pagination" v-if="filteredKhachHang.length > 0"
        style="display: flex; justify-content: space-between; align-items: center; margin-top: 15px; font-size: 13px; color: #555; padding-top: 10px;">
        <span>Tổng số <strong>{{ filteredKhachHang.length }}</strong> khách hàng</span>
        <div class="page-controls" style="display: flex; align-items: center; gap: 5px;">
          <button :disabled="currentPage === 1" @click="currentPage--"
            style="background: white; border: 1px solid #ddd; padding: 4px 10px; border-radius: 4px; cursor: pointer; display: inline-flex; align-items: center; justify-content: center;"><i
              class="fas fa-chevron-left"></i></button>

          <button v-for="page in totalPages" :key="page" :class="['page-num', { active: currentPage === page }]"
            @click="currentPage = page">
            {{ page }}
          </button>

          <button :disabled="currentPage === totalPages" @click="currentPage++"
            style="background: white; border: 1px solid #ddd; padding: 4px 10px; border-radius: 4px; cursor: pointer; display: inline-flex; align-items: center; justify-content: center;"><i
              class="fas fa-chevron-right"></i></button>

          <select class="page-size-select" v-model="pageSize" @change="currentPage = 1">
            <option :value="5">5 / trang</option>
            <option :value="10">10 / trang</option>
            <option :value="20">20 / trang</option>
          </select>
        </div>
      </div>
    </div>

    <div v-if="isAddressModalOpen" class="modal-overlay" @click.self="closeAddressModal">
      <div class="modal-container">
        <div class="modal-header">
          <div class="modal-title">
            <span class="icon" style="color: #f56c6c;">📍</span>
            <div>
              <h3>Sổ địa chỉ khách hàng</h3>
              <small>{{ selectedCustomer?.hoTen }} - {{ selectedCustomer?.sdt }}</small>
            </div>
          </div>
          <button class="close-btn" @click="closeAddressModal">&times;</button>
        </div>

        <div class="modal-body-layout">
          <div class="left-section">
            <h4 class="section-title"><span class="icon">📍</span> Danh sách địa chỉ hiện có</h4>

            <div class="address-table-wrapper">
              <table class="sub-table">
                <thead>
                  <tr>
                    <th style="width: 10%">STT</th>
                    <th style="width: 55%">Địa chỉ</th>
                    <th style="width: 15%; text-align: center;">Mặc định</th>
                    <th style="width: 20%; text-align: center;">Trạng thái</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(addr, idx) in pagedAddresses" :key="addr.id">
                    <td>{{ (subCurrentPage - 1) * subPageSize + idx + 1 }}</td>
                    <td>
                      <div class="address-detail-text">
                        <span>🏠</span> {{ addr.diaChi }}
                      </div>
                    </td>
                    <td class="text-center">
                      <span class="star-icon" :class="{ active: addr.isDefault }" @click="setAsDefaultAddress(addr.id)"
                        :title="addr.isDefault ? 'Địa chỉ mặc định' : 'Đặt làm mặc định'">
                        {{ addr.isDefault ? '⭐' : '☆' }}
                      </span>
                    </td>
                    <td class="text-center">
                      <span class="badge-active">Hoạt động</span>
                    </td>
                  </tr>
                  <tr v-if="!selectedCustomer?.addresses || selectedCustomer.addresses.length === 0">
                    <td colspan="4" class="no-data" style="padding: 20px;">Chưa có địa chỉ nào.</td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div class="sub-pagination" v-if="selectedCustomer?.addresses?.length > 0">
              <span>Tổng {{ selectedCustomer.addresses.length }} địa chỉ</span>
              <div class="sub-page-controls">
                <button :disabled="subCurrentPage === 1" @click="subCurrentPage--">&lt;</button>
                <span class="sub-page-display">{{ subCurrentPage }} / {{ subTotalPages }}</span>
                <button :disabled="subTotalPages === 0 || subCurrentPage === subTotalPages"
                  @click="subCurrentPage++">&gt;</button>

                <select class="mini-select" v-model="subPageSize" @change="subCurrentPage = 1">
                  <option :value="2">2 / page</option>
                  <option :value="5">5 / page</option>
                  <option :value="10">10 / page</option>
                </select>
              </div>
            </div>
          </div>

          <div class="right-section">
            <h4 class="section-title"><span class="icon">📝</span> Thêm nhanh địa chỉ</h4>

            <div class="form-grid">
              <div class="form-group">
                <label>Tỉnh / Thành phố <span class="required">*</span></label>
                <select v-model="newAddress.tinhThanh">
                  <option value="">-- Chọn hoặc tìm kiếm Tỉnh / TP --</option>
                  <option value="Hà Nội">Hà Nội</option>
                  <option value="Cao Bằng">Cao Bằng</option>
                  <option value="Hồ Chí Minh">Hồ Chí Minh</option>
                </select>
              </div>

              <div class="form-group">
                <label>Phường / Xã <span class="required">*</span></label>
                <select v-model="newAddress.phuongXa" :disabled="!newAddress.tinhThanh">
                  <option value="">-- Chọn Phường / Xã --</option>
                  <option value="Phường Văn Quán">Phường Văn Quán</option>
                  <option value="Phường Sông Hiến">Phường Sông Hiến</option>
                  <option value="Phường Hàng Bông">Phường Hàng Bông</option>
                </select>
              </div>
            </div>

            <div class="form-group full-width">
              <label>Địa chỉ chi tiết <span class="required">*</span></label>
              <input type="text" v-model="newAddress.chiTiet"
                placeholder="Nhập địa chỉ chi tiết (VD: Số nhà, số ngõ, ngách...)" />
            </div>

            <div class="form-group checkbox-group">
              <label class="toggle-control">
                <input type="checkbox" v-model="newAddress.isDefault" />
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
// 🛠️ Đã import thêm hàm addKhachHang và updateKhachHang từ Service của bạn
import { fetchAllKhachHang, addKhachHang, updateKhachHang } from '@/service/KhachHangService'
// 💡 Khuyến khích cài đặt thư viện xlsx để xuất file: npm install xlsx
import * as XLSX from 'xlsx'

const listKhachHang = ref([])

const currentPage = ref(1)
const pageSize = ref(5)

const filterParams = ref({
  keyword: '',
  gioiTinh: '',
  ngaySinh: '',
  trangThai: ''
})

// =================================================================
// 📸 CHỨC NĂNG CHỌN VÀ CHUYỂN ĐỔI ẢNH ĐẠI DIỆN (ĐÃ SỬA LỖI)
// =================================================================
const formFileInputRef = ref(null)
const fileInputRef = ref(null) // Đã bổ sung ref này để tránh lỗi crash form

const triggerFormFileInput = () => {
  if (formFileInputRef.value) {
    formFileInputRef.value.click()
  }
}

const handleFormAvatarChange = (event) => {
  const file = event.target.files[0]
  if (!file) return

  // Kiểm tra định dạng đuôi file hợp lệ
  const validTypes = ['image/jpeg', 'image/jpg', 'image/png']
  if (!validTypes.includes(file.type)) {
    alert('Định dạng tệp không hợp lệ! Vui lòng chọn ảnh có đuôi .jpg, .jpeg hoặc .png.')
    return
  }

  // Kiểm tra dung lượng file (Giới hạn tối đa là 2MB)
  if (file.size > 2 * 1024 * 1024) {
    alert('Dung lượng ảnh quá lớn! Vui lòng chọn file nhỏ hơn 2MB.')
    return
  }

  // Đọc file sang chuỗi Base64 để hiển thị trực tiếp lên khung preview đại diện
  const reader = new FileReader()
  reader.onload = (e) => {
    // 🔥 SỬA LỖI: Thay nhanVienForm thành customerForm để đồng bộ dữ liệu
    customerForm.value.avatar = e.target.result
  }
  reader.readAsDataURL(file)
}


// =================================================================
// 🌟 CHỨC NĂNG IN VÀ XUẤT FILE (ĐÃ SỬA LỖI)
// =================================================================

// 1. Hàm xử lý In ấn danh sách khách hàng
const handlePrint = () => {
  window.print()
}

// 2. Hàm xử lý xuất danh sách khách hàng ra file Excel
const handleExport = () => {
  if (filteredKhachHang.value.length === 0) {
    alert('Không có dữ liệu khách hàng nào để xuất file!')
    return
  }

  const dataToExport = filteredKhachHang.value.map((item, index) => {
    return {
      'STT': index + 1,
      'Mã Khách Hàng': item.id || 'N/A',
      'Họ và Tên': item.hoTen || '',
      'Số Điện Thoại': item.sdt || '',
      'Email': item.email || '',
      'Ngày Sinh': item.ngaySinh || '',
      'Giới Tính': formatGioiTinh(item.gioiTinh),
      'Địa Chỉ Chính': getDefaultAddressText(item),
      'Trạng Thái': formatTrangThai(item.trangThai)
    }
  })

  const worksheet = XLSX.utils.json_to_sheet(dataToExport)
  const workbook = XLSX.utils.book_new()
  
  // 🔥 SỬA LỖI: Đổi XXLSX thành XLSX chính xác theo thư viện import
  XXLSX.utils.book_append_sheet(workbook, worksheet, 'Danh Sách Khách Hàng')
  XXLSX.writeFile(workbook, 'Danh_Sach_Khach_Hang.xlsx')
}


// =================================================================
// STATE QUẢN LÝ GIAO DIỆN XEM CHI TIẾT KHÁCH HÀNG
// =================================================================
const isDetailMode = ref(false)
const customerDetail = ref({
  hoTen: '',
  sdt: '',
  email: '',
  ngaySinh: '',
  gioiTinh: '1',
  trangThai: 1,
  avatar: '',
  addresses: [],
  rawOrigin: null
})

const viewDetail = (khachHang) => {
  customerDetail.value = {
    hoTen: khachHang.hoTen || '',
    sdt: khachHang.sdt || '',
    email: khachHang.email || '',
    ngaySinh: khachHang.ngaySinh || '',
    gioiTinh: String(khachHang.gioiTinh),
    trangThai: (khachHang.trangThai === true || Number(khachHang.trangThai) === 1) ? 1 : 0,
    avatar: khachHang.avatar || '',
    addresses: khachHang.addresses || [],
    rawOrigin: khachHang
  }
  isDetailMode.value = true
}

const cancelDetailMode = () => {
  isDetailMode.value = false
}

const switchToEditFromDetail = (rawCustomer) => {
  isDetailMode.value = false
  openEditForm(rawCustomer)
}


// =================================================================
// STATE QUẢN LÝ GIAO DIỆN FORM TO LỚN (THÊM / SỬA)
// =================================================================
const isFormMode = ref(false)
const customerForm = ref({
  id: null,
  hoTen: '',
  sdt: '',
  email: '',
  ngaySinh: '',
  gioiTinh: '1',
  trangThai: 1,
  avatar: '',
  addresses: []
})

const openAddForm = () => {
  customerForm.value = {
    id: null,
    hoTen: '',
    sdt: '',
    email: '',
    ngaySinh: '',
    gioiTinh: '1',
    trangThai: 1,
    avatar: '', 
    addresses: [
      { id: Date.now(), tinhThanh: '', phuongXa: '', chiTiet: '', isDefault: true, diaChi: '' }
    ]
  }
  if (fileInputRef.value) fileInputRef.value.value = '' 
  if (formFileInputRef.value) formFileInputRef.value.value = '' // Reset luôn input chọn avatar
  isFormMode.value = true
}

const openEditForm = (khachHang) => {
  const parsedAddresses = khachHang.addresses ? khachHang.addresses.map(addr => {
    let tinh = addr.tinhThanh || '';
    let phuong = addr.phuongXa || '';
    let chiTietText = addr.chiTiet || addr.diaChi || '';

    if (addr.diaChi && !addr.tinhThanh) {
      const parts = addr.diaChi.split(',').map(p => p.trim());
      if (parts.length >= 3) {
        tinh = parts[parts.length - 1];
        phuong = parts[parts.length - 2];
        chiTietText = parts.slice(0, parts.length - 2).join(', ');
      }
    }

    return {
      id: addr.id || Date.now() + Math.random(),
      tinhThanh: tinh,
      phuongXa: phuong,
      chiTiet: chiTietText,
      isDefault: addr.isDefault || false,
      diaChi: addr.diaChi || chiTietText
    }
  }) : [];

  customerForm.value = {
    id: khachHang.id,
    hoTen: khachHang.hoTen || '',
    sdt: khachHang.sdt || '',
    email: khachHang.email || '',
    ngaySinh: khachHang.ngaySinh || '',
    gioiTinh: String(khachHang.gioiTinh),
    trangThai: (khachHang.trangThai === true || Number(khachHang.trangThai) === 1) ? 1 : 0,
    avatar: khachHang.avatar || '', 
    addresses: parsedAddresses
  }
  if (fileInputRef.value) fileInputRef.value.value = '' 
  if (formFileInputRef.value) formFileInputRef.value.value = ''
  isFormMode.value = true
}

const cancelFormMode = () => {
  isFormMode.value = false
}

const addNewAddressRow = () => {
  const isFirst = customerForm.value.addresses.length === 0
  customerForm.value.addresses.push({
    id: Date.now(),
    tinhThanh: '',
    phuongXa: '',
    chiTiet: '',
    isDefault: isFirst,
    diaChi: ''
  })
}

const removeAddressRow = (index) => {
  const deletedWasDefault = customerForm.value.addresses[index].isDefault
  customerForm.value.addresses.splice(index, 1)
  if (deletedWasDefault && customerForm.value.addresses.length > 0) {
    customerForm.value.addresses[0].isDefault = true
  }
}

const handleSetDefaultInForm = (index) => {
  customerForm.value.addresses.forEach((addr, idx) => {
    addr.isDefault = (idx === index)
  })
}

const handleSubmitForm = async () => {
  if (!customerForm.value.hoTen || !customerForm.value.sdt) {
    alert('Vui lòng nhập đầy đủ thông tin bắt buộc (*)')
    return
  }

  const cleanSdt = customerForm.value.sdt.trim()
  const cleanEmail = customerForm.value.email ? customerForm.value.email.trim() : ''

  const isEditing = !!customerForm.value.id

  const isSdtDuplicate = listKhachHang.value.some(kh => {
    if (isEditing && kh.id === customerForm.value.id) return false
    return kh.sdt && kh.sdt.trim() === cleanSdt
  })

  if (isSdtDuplicate) {
    alert(`Thao tác thất bại: Số điện thoại đã tồn tại trong hệ thống!`)
    return
  }

  if (cleanEmail) {
    const isEmailDuplicate = listKhachHang.value.some(kh => {
      if (isEditing && kh.id === customerForm.value.id) return false
      return kh.email && kh.email.trim().toLowerCase() === cleanEmail.toLowerCase()
    })

    if (isEmailDuplicate) {
      alert(`Thao tác thất bại: Địa chỉ Email đã tồn tại trong hệ thống!`)
      return
    }
  }

  const processedAddresses = customerForm.value.addresses.map(addr => {
    let combinedString = addr.chiTiet || ''
    if (addr.phuongXa) combinedString += (combinedString ? `, ${addr.phuongXa}` : addr.phuongXa)
    if (addr.tinhThanh) combinedString += (combinedString ? `, ${addr.tinhThanh}` : addr.tinhThanh)

    return {
      ...addr,
      diaChi: combinedString || 'Chưa cập nhật chi tiết'
    }
  })

  const diaChiMacDinh =
    processedAddresses.find(a => a.isDefault)?.diaChi ||
    processedAddresses[0]?.diaChi ||
    null

  const customerPayload = {
    hoTen: customerForm.value.hoTen.trim(),
    sdt: cleanSdt,
    email: cleanEmail || null,
    ngaySinh: customerForm.value.ngaySinh,
    gioiTinh: Number(customerForm.value.gioiTinh),
    trangThai: Number(customerForm.value.trangThai),
    avatar: customerForm.value.avatar || null, 
    diaChi: diaChiMacDinh
  }

  try {
    if (customerForm.value.id) {
      await updateKhachHang(customerForm.value.id, customerPayload)
      alert('Cập nhật thông tin khách hàng thành công!')
    } else {
      await addKhachHang(customerPayload)
      alert('Thêm mới khách hàng thành công!')
    }

    await handleFetchAllData()

    if (!customerForm.value.id) {
      currentPage.value = totalPages.value
    }

    isFormMode.value = false
  } catch (error) {
    console.error('Lỗi khi lưu dữ liệu xuống cơ sở dữ liệu:', error)

    let errorMsg = 'Thao tác thất bại! Vui lòng kiểm tra lại kết nối hệ thống.'
    let errorText = ''

    if (typeof error === 'string') {
      errorText = error
    } else if (error && error.message) {
      errorText = error.message
    } else if (error && error.response && error.response.data) {
      errorText = typeof error.response.data === 'object'
        ? JSON.stringify(error.response.data)
        : String(error.response.data)
    }

    if (errorText.includes('Số điện thoại đã tồn tại')) {
      errorMsg = 'Thêm thất bại: Số điện thoại này đã tồn tại trên cơ sở dữ liệu server!'
    } else if (errorText.includes('Email đã tồn tại')) {
      errorMsg = 'Thêm thất bại: Địa chỉ Email này đã tồn tại trên cơ sở dữ liệu server!'
    }

    alert(errorMsg)
  }
}

// ======================
// STATE QUẢN LÝ ĐỊA CHỈ (MODAL BẢNG DANH SÁCH CHÍNH)
// ======================
const isAddressModalOpen = ref(false)
const selectedCustomer = ref(null)

const subCurrentPage = ref(1)
const subPageSize = ref(2)

const newAddress = ref({
  tinhThanh: '',
  phuongXa: '',
  chiTiet: '',
  isDefault: false
})

// ======================
// LOAD DATA FROM API
// ======================
const handleFetchAllData = async () => {
  try {
    const data = await fetchAllKhachHang()
    const rawList = Array.isArray(data) ? data : []
    console.log("DATA API:", rawList)

    listKhachHang.value = rawList.map((kh, index) => {
      let activeAddresses = []

      if (Array.isArray(kh.addresses) && kh.addresses.length > 0) {
        activeAddresses = kh.addresses.map(addr => {
          const fullAddress =
            addr.diaChi ||
            [
              addr.chiTiet,
              addr.phuongXa,
              addr.tinhThanh
            ]
              .filter(Boolean)
              .join(', ')

          return {
            ...addr,
            diaChi: fullAddress,
            isDefault: addr.isDefault ?? true
          }
        })
      }
      else if (kh.diaChi) {
        activeAddresses = [
          {
            id: Date.now() + index,
            diaChi: kh.diaChi,
            isDefault: true
          }
        ]
      }

      return {
        ...kh,
        addresses: activeAddresses
      }
    })
  } catch (error) {
    console.error('Lỗi khi tải danh sách khách hàng:', error)
    listKhachHang.value = []
  }
}

onMounted(() => {
  handleFetchAllData()
})

const getDefaultAddressText = (khachHang) => {
  if (khachHang.addresses?.length) {
    const def = khachHang.addresses.find(a => a.isDefault)
    return def?.diaChi || khachHang.addresses[0]?.diaChi || '-'
  }
  return khachHang.diaChi || '-'
}

// ======================
// TÌM KIẾM & LỌC DỮ LIỆU
// ======================
const handleFilter = () => {
  currentPage.value = 1
}

const filteredKhachHang = computed(() => {
  return listKhachHang.value.filter(khachHang => {
    const kw = filterParams.value.keyword.trim().toLowerCase()
    const matchKeyword = !kw ||
      (khachHang.hoTen && khachHang.hoTen.toLowerCase().includes(kw)) ||
      (khachHang.sdt && khachHang.sdt.includes(kw)) ||
      (khachHang.email && khachHang.email.toLowerCase().includes(kw))

    const gt = filterParams.value.gioiTinh
    const matchGioiTinh = gt === '' || String(khachHang.gioiTinh) === String(gt)

    const ns = filterParams.value.ngaySinh
    const matchNgaySinh = !ns || khachHang.ngaySinh === ns

    const tt = filterParams.value.trangThai
    let matchTrangThai = true
    if (tt !== '') {
      const isFilterActive = tt === 'true'
      const isUserActive = khachHang.trangThai === true || Number(khachHang.trangThai) === 1
      matchTrangThai = isUserActive === isFilterActive
    }

    return matchKeyword && matchGioiTinh && matchNgaySinh && matchTrangThai
  })
})

// ======================
// LOGIC PHÂN TRANG KHÁCH HÀNG CHÍNH
// ======================
const totalPages = computed(() => {
  return Math.ceil(filteredKhachHang.value.length / pageSize.value) || 1
})

const pagedKhachHang = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredKhachHang.value.slice(start, end)
})

// ======================
// LOGIC PHÂN TRANG ĐỊA CHỈ TRONG MODAL
// ======================
const subTotalPages = computed(() => {
  if (!selectedCustomer.value || !selectedCustomer.value.addresses) return 1
  return Math.ceil(selectedCustomer.value.addresses.length / subPageSize.value) || 1
})

const pagedAddresses = computed(() => {
  if (!selectedCustomer.value || !selectedCustomer.value.addresses) return []
  const start = (subCurrentPage.value - 1) * subPageSize.value
  const end = start + subPageSize.value
  return selectedCustomer.value.addresses.slice(start, end)
})

// ======================
// THAO TÁC MODAL ĐỊA CHỈ & LOGIC XỬ LÝ
// ======================
const openAddressModal = (khachHang) => {
  selectedCustomer.value = khachHang
  subCurrentPage.value = 1
  isAddressModalOpen.value = true
}

const closeAddressModal = () => {
  isAddressModalOpen.value = false
  selectedCustomer.value = null
  newAddress.value = { tinhThanh: '', phuongXa: '', chiTiet: '', isDefault: false }
}

const setAsDefaultAddress = async (addressId) => {
  if (!selectedCustomer.value || !selectedCustomer.value.addresses) return

  let primaryAddressText = ''

  const updatedAddresses = selectedCustomer.value.addresses.map(addr => {
    if (addr.id === addressId) {
      primaryAddressText = addr.diaChi || `${addr.chiTiet}, ${addr.phuongXa}, ${addr.tinhThanh}`
      return { ...addr, isDefault: true }
    }
    return { ...addr, isDefault: false }
  })

  const updatePayload = {
    ...selectedCustomer.value,
    diaChi: primaryAddressText,
    addresses: updatedAddresses,
    trangThai: (selectedCustomer.value.trangThai === true || Number(selectedCustomer.value.trangThai) === 1) ? 1 : 0,
    gioiTinh: Number(selectedCustomer.value.gioiTinh)
  }

  try {
    await updateKhachHang(selectedCustomer.value.id, updatePayload)
    selectedCustomer.value.addresses = updatedAddresses
    selectedCustomer.value.diaChi = primaryAddressText
    alert('Thay đổi địa chỉ mặc định thành công!')
    await handleFetchAllData()
  } catch (error) {
    console.error('Lỗi khi cập nhật địa chỉ mặc định:', error)
    alert('Không thể cập nhật địa chỉ mặc định xuống cơ sở dữ liệu!')
  }
}

const handleSaveAddress = async () => {
  if (!newAddress.value.tinhThanh || !newAddress.value.phuongXa || !newAddress.value.chiTiet.trim()) {
    alert('Vui lòng điền đầy đủ thông tin bắt buộc (*)')
    return
  }

  const fullString = `${newAddress.value.chiTiet.trim()}, ${newAddress.value.phuongXa}, ${newAddress.value.tinhThanh}`
  const targetAddressId = Date.now()

  let currentAddresses = selectedCustomer.value.addresses ? [...selectedCustomer.value.addresses] : []

  if (newAddress.value.isDefault) {
    currentAddresses = currentAddresses.map(a => ({ ...a, isDefault: false }))
  }

  const createdAddressItem = {
    id: targetAddressId,
    diaChi: fullString,
    tinhThanh: newAddress.value.tinhThanh,
    phuongXa: newAddress.value.phuongXa,
    chiTiet: newAddress.value.chiTiet.trim(),
    isDefault: newAddress.value.isDefault || currentAddresses.length === 0
  }

  currentAddresses.push(createdAddressItem)

  const customerMainAddress = createdAddressItem.isDefault
    ? fullString
    : (selectedCustomer.value.diaChi || fullString)

  const updatePayload = {
    ...selectedCustomer.value,
    diaChi: customerMainAddress,
    addresses: currentAddresses,
    trangThai: (selectedCustomer.value.trangThai === true || Number(selectedCustomer.value.trangThai) === 1) ? 1 : 0,
    gioiTinh: Number(selectedCustomer.value.gioiTinh)
  }

  try {
    await updateKhachHang(selectedCustomer.value.id, updatePayload)
    selectedCustomer.value.addresses = currentAddresses
    selectedCustomer.value.diaChi = customerMainAddress
    newAddress.value = { tinhThanh: '', phuongXa: '', chiTiet: '', isDefault: false }
    subCurrentPage.value = Math.ceil(currentAddresses.length / subPageSize.value)
    alert('Thêm nhanh địa chỉ mới và lưu SQL thành công!')
    await handleFetchAllData()
  } catch (error) {
    console.error('Lỗi lưu địa chỉ mới:', error)
    alert('Không thể lưu địa chỉ vào hệ thống!')
  }
}

// ======================
// THAO TÁC KHÁC
// ======================
const resetFilters = () => {
  filterParams.value = { keyword: '', gioiTinh: '', ngaySinh: '', trangThai: '' }
  currentPage.value = 1
}

const toggleStatus = async (khachHang) => {
  const currentStatus = Number(khachHang.trangThai) === 1
  const newStatus = currentStatus ? 0 : 1

  try {
    await updateKhachHang(
      khachHang.id,
      {
        ...khachHang,
        trangThai: newStatus
      }
    )
    khachHang.trangThai = newStatus
    alert('Thay đổi trạng thái khách hàng thành công!')
  } catch (error) {
    console.error('Lỗi khi đổi trạng thái:', error)
    alert('Không thể cập nhật trạng thái xuống SQL!')
  }
}

const formatGioiTinh = (gioiTinh) => {
  return Number(gioiTinh) === 1 ? 'Nam' : 'Nữ'
}

const formatTrangThai = (trangThai) => {
  return (trangThai === true || Number(trangThai) === 1) ? 'Hoạt động' : 'Ngừng hoạt động'
}
</script>

<style scoped>
/* ================================================================= */
/* 1. KHUNG BỘ LỌC TÌM KIẾM PHẲNG - CHỐNG LỆCH HÀNG CHUẨN ĐẸP          */
/* ================================================================= */
.table-responsive {
  width: 100% !important;
  max-width: 100% !important;
  min-width: 100% !important;
  box-sizing: border-box !important;
  display: block !important;
}

/* Ép bảng luôn bung rộng, khi màn hình nhỏ sẽ xuất hiện cuộn ngang thay vì co chữ */
.customer-table {
  width: 100% !important;
  min-width: 1100px !important;
  table-layout: fixed !important;
  border-collapse: collapse !important;
}

/* Đảm bảo ô thông tin quá dài không bóp méo layout dòng */
.customer-table td {
  padding: 12px 10px !important;
  vertical-align: middle;
}

/* Khung bọc ngoài bộ lọc */
.search-filter-box.aerion-filter {
  background: #ffffff;
  border: 1px solid #e8e8e8;
  border-radius: 10px;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
  /* Bo tròn góc dải màu cam theo khung */
  padding: 0 !important;
  /* Xóa bỏ padding thừa để dải cam tràn viền */
}

/* Thanh tiêu đề màu cam phẳng chạy dài */
.filter-header {
  background-color: #f37d4b;
  /* Màu cam phẳng phẳng */
  padding: 12px 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* Chữ tiêu đề bên trong thanh cam */
.filter-title {
  color: #ffffff !important;
  font-weight: 600;
  font-size: 14.5px;
  margin: 0 !important;
}

/* Icon kính lúp hoặc phễu màu trắng */
.filter-icon {
  color: #ffffff;
  font-size: 14px;
}

/* Hàng chứa các ô lọc: Ép tất cả thành phần canh đều theo đáy (flex-end) */
.filter-row {
  display: flex;
  align-items: flex-end;
  /* Quan trọng: Giúp chân của input, radio, button bằng chẹn nhau */
  gap: 15px;
  flex-wrap: wrap;
  padding: 20px;
  /* Padding tạo khoảng cách thông thoáng phía trong */
}

/* Cấu trúc từng ô lọc */
.filter-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.filter-item label {
  font-size: 13px;
  color: #666;
  font-weight: 500;
  margin: 0;
}

/* Thiết lập độ rộng co giãn hợp lý cho từng ô */
.search-input {
  flex: 2;
  min-width: 240px;
}

.gender-radio,
.status-radio {
  flex: 1.5;
  min-width: 210px;
}

.date-input {
  flex: 1;
  min-width: 140px;
}

/* Đồng bộ chiều cao chuẩn 38px cho các thẻ nhập liệu text và date */
.filter-item input[type="text"],
.filter-item input[type="date"] {
  width: 100%;
  height: 38px;
  padding: 0 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  outline: none;
  font-size: 13.5px;
  box-sizing: border-box;
  transition: border-color 0.2s;
  background-color: #fff;
}

.filter-item input[type="text"]:focus,
.filter-item input[type="date"]:focus {
  border-color: #f37d4b;
}

/* Cố định cụm nút Radio - Không cho lệch hoặc rớt dòng text */
.radio-group {
  display: flex;
  align-items: center;
  gap: 12px;
  /* Khoảng cách giữa các nút chọn Tất cả / Nam / Nữ */
  height: 38px;
  /* Khớp chuẩn chiều cao tuyệt đối với ô input */
  box-sizing: border-box;
}

.radio-group label {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  font-size: 13.5px;
  color: #333;
  cursor: pointer;
  white-space: nowrap;
  /* Giữ chữ trên một hàng */
  font-weight: normal !important;
  margin: 0 !important;
}

.radio-group input[type="radio"] {
  margin: 0;
  width: 16px;
  height: 16px;
  accent-color: #f37d4b;
  /* Đổi màu chấm tròn radio sang màu cam đồng bộ */
  cursor: pointer;
}

/* Nút xoá / làm mới bộ lọc (Refresh Button) */
.refresh-btn {
  background: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 6px;
  cursor: pointer;
  height: 38px;
  /* Cao 38px bằng tắp với tất cả các ô bên cạnh */
  width: 38px;
  min-width: 38px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  transition: all 0.2s;
  font-size: 14px;
}

.refresh-btn:hover {
  background: #ebebeb;
  border-color: #ccc;
}

/* Trạng thái input disabled */
.disabled-input {
  background-color: #f5f7fa !important;
  color: #555 !important;
  cursor: not-allowed;
  border: 1px solid #e4e7ed !important;
}

.customer-detail-container input {
  font-weight: 500;
}

/* ================================================================= */
/* 2. STYLE GIAO DIỆN FORM CARD THÊM / SỬA KHÁCH HÀNG                 */
/* ================================================================= */
.customer-form-container {
  background-color: #f9fbfd;
  padding: 10px 20px 40px 20px;
}

.back-header {
  margin-bottom: 20px;
}

.btn-back-link {
  background: none;
  border: none;
  color: #909399;
  cursor: pointer;
  font-size: 14px;
  padding: 0;
  margin-bottom: 5px;
}

.btn-back-link:hover {
  color: #1a2942;
  text-decoration: underline;
}

.page-form-title {
  margin: 0;
  font-size: 18px;
  color: #303133;
  font-weight: 500;
}

.form-section-card {
  background: #ffffff;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.02);
}

.profile-grid {
  display: flex;
  gap: 40px;
}

.avatar-upload-block {
  width: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-right: 1px solid #f2f6fc;
  padding-right: 30px;
}

.avatar-preview-circle {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  border: 1px solid #e4e7ed;
  margin-bottom: 15px;
}

.avatar-preview-circle img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.default-avatar-icon {
  font-size: 50px;
  color: #c0c4cc;
}

.btn-select-image {
  background: #ffffff;
  border: 1px dashed #dcdfe6;
  padding: 6px 16px;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
}

.upload-hint {
  color: #909399;
  font-size: 11px;
  margin-top: 8px;
}

.info-fields-block {
  flex: 1;
}

.section-sub-title {
  font-size: 16px;
  color: #303133;
  font-weight: 600;
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 16px;
}

.form-cell {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-cell label,
.form-group label {
  font-size: 13px;
  color: #606266;
  font-weight: 500;
}

.form-cell input[type="text"],
.form-cell input[type="email"],
.form-cell input[type="date"],
.select-box {
  padding: 9px 12px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 13px;
  outline: none;
  width: 100%;
  box-sizing: border-box;
}

.radio-flex {
  display: flex;
  gap: 20px;
  padding: 8px 12px;
  background: #ffffff;
}

.radio-flex label {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  font-size: 13px;
}

.border-box {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.address-section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.btn-add-address-item {
  background-color: #ff9800;
  border: none;
  color: #ffffff;
  padding: 6px 14px;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
}

.address-item-card {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 16px;
  background-color: #fafafa;
  margin-bottom: 16px;
}

.address-item-title {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  font-size: 13px;
  color: #606266;
  margin-bottom: 12px;
}

.btn-delete-address {
  background: none;
  border: none;
  color: #f56c6c;
  cursor: pointer;
  font-size: 13px;
}

.toggle-control {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  font-size: 13px;
  color: #606266;
}

.form-submit-bar {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 10px;
}

.btn-cancel-action {
  background-color: #ffffff;
  border: 1px solid #dcdfe6;
  color: #606266;
  padding: 10px 24px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-submit-action {
  background-color: #1a2942;
  border: none;
  color: #ffffff;
  padding: 10px 24px;
  border-radius: 4px;
  cursor: pointer;
}

/* ================================================================= */
/* 3. STYLE CHO DANH SÁCH BẢNG DỮ LIỆU & TIÊU ĐỀ THAO TÁC            */
/* ================================================================= */
/* ==========================
   DANH SÁCH KHÁCH HÀNG
========================== */

.top-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 18px 0;
}

.list-title {
  font-size: 22px;
  font-weight: 700;
  color: #222;
}

.btn-group {
  display: flex;
  gap: 10px;
}

.action-icon-btn {
  width: 40px;
  height: 40px;
  border: 1px solid #e5e7eb;
  background: #fff;
  border-radius: 8px;
  cursor: pointer;
  transition: all .2s;
}

.action-icon-btn:hover {
  background: #f8f8f8;
}

.add-btn {
  width: 40px;
  height: 40px;
  border: none;
  background: #f37d4b;
  color: #fff;
  border-radius: 8px;
  cursor: pointer;
  font-size: 18px;
}

/* ==========================
   TABLE
========================== */

.table-box {
  background: #fff;
  border-radius: 12px;
  padding: 0;
  overflow: hidden;
  border: 1px solid #ececec;
}

.customer-table,
table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.customer-table thead th,
table thead th {
  background: #f37d4b;
  color: white;
  font-weight: 600;
  font-size: 14px;
  padding: 14px 10px;
  text-align: center;
  border: none;
}

.customer-table tbody td,
table tbody td {
  padding: 14px 10px;
  font-size: 14px;
  color: #333;
  vertical-align: middle;
  border-bottom: 1px solid #f0f0f0;
}

.customer-table tbody tr:hover,
table tbody tr:hover {
  background: #fafafa;
}

/* ==========================
   AVATAR
========================== */

.avatar-placeholder {
  width: 46px;
  height: 46px;
  border-radius: 50%;
  overflow: hidden;
  background: #f3f4f6;
  margin: auto;
}

.avatar-placeholder img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* ==========================
   EMAIL - ĐỊA CHỈ
========================== */

.email-text {
  word-break: break-word;
  line-height: 1.4;
}

.address-text {
  line-height: 1.5;
  max-width: 220px;
  white-space: normal;
}

/* ==========================
   TRẠNG THÁI
========================== */

.status-badge {
  display: inline-block;
  padding: 5px 12px;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.active {
  background: #e8f7ee;
  color: #16a34a;
  border: none;
}

.status-badge.inactive {
  background: #fee2e2;
  color: #dc2626;
  border: none;
}

/* ==========================
   ACTIONS
========================== */

.table-actions {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.table-actions button {
  width: 34px;
  height: 34px;
  border-radius: 6px;
  cursor: pointer;
  padding: 0;
}

.btn-view {
  background: #eef6ff !important;
  border: 1px solid #93c5fd !important;
  color: #2563eb !important;
}

.btn-address {
  background: #f0fdf4 !important;
  border: 1px solid #a3e635 !important;
  color: #16a34a !important;
}

.btn-edit {
  background: #fff7ed !important;
  border: 1px solid #fdba74 !important;
  color: #ea580c !important;
}

/* ==========================
   PAGINATION
========================== */

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: #fff;
}

.page-controls {
  display: flex;
  align-items: center;
  gap: 6px;
}

.page-controls button {
  min-width: 34px;
  height: 34px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 6px;
  cursor: pointer;
}

.page-controls .active {
  background: #f37d4b;
  border-color: #f37d4b;
  color: #fff;
}

.page-size-select {
  height: 34px;
  border: 1px solid #ddd;
  border-radius: 6px;
  padding: 0 10px;
}

.no-data {
  text-align: center;
  color: #666;
  font-style: italic;
  padding: 25px;
}

/* Modal UI */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

.modal-container {
  background: #ffffff;
  border-radius: 8px;
  width: 1050px;
  max-width: 95%;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 20px;
  border-bottom: 1px solid #ebeef5;
}

.modal-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.modal-title h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}

.modal-title small {
  color: #909399;
  font-size: 13px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #909399;
  cursor: pointer;
}

.modal-body-layout {
  display: flex;
  flex-direction: row;
  min-height: 380px;
}

.left-section {
  flex: 1.15;
  padding: 20px;
  border-right: 1px solid #ebeef5;
  background-color: #fafafa;
}

.right-section {
  flex: 0.85;
  padding: 20px;
}

.section-title {
  font-size: 14px;
  margin-top: 0;
  margin-bottom: 16px;
  color: #303133;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
}

.address-table-wrapper {
  background: #ffffff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  min-height: 180px;
}

.sub-table {
  width: 100%;
  border-collapse: collapse;
}

.sub-table th {
  background-color: #f5f7fa;
  color: #606266;
  text-align: left;
  padding: 10px;
  font-size: 13px;
  border-bottom: 1px solid #ebeef5;
}

.sub-table td {
  padding: 12px 10px;
  font-size: 13px;
  border-bottom: 1px solid #ebeef5;
  color: #606266;
  vertical-align: middle;
}

.star-icon {
  font-size: 18px;
  cursor: pointer;
  color: #c0c4cc;
  user-select: none;
  transition: transform 0.1s ease;
}

.star-icon:hover {
  transform: scale(1.2);
}

.star-icon.active {
  color: #f5a623;
}

.badge-active {
  background-color: #e1f3d8;
  color: #67c23a;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.sub-pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  font-size: 13px;
  color: #606266;
}

.sub-page-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.sub-page-controls button {
  background-color: #ffffff;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 2px 8px;
  cursor: pointer;
}

.sub-page-controls button:disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}

.mini-select {
  padding: 2px 4px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  font-size: 12px;
}

.form-grid {
  display: flex;
  gap: 16px;
  margin-bottom: 14px;
}

.form-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 14px;
}

.form-actions-right {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 35px;
}

.btn-cancel-sub {
  background-color: #ffffff;
  border: 1px solid #dcdfe6;
  color: #606266;
  padding: 8px 18px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

.btn-submit-sub {
  background-color: #1a2942;
  border: none;
  color: #ffffff;
  padding: 8px 18px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

.btn-submit-sub:hover {
  background-color: #2c3e50;
}

.required {
  color: #f56c6c;
}

.mb-3 {
  margin-bottom: 1rem;
}

.mb-2 {
  margin-bottom: 0.5rem;
}

.fw-bold {
  font-weight: bold;
}

.text-center {
  text-align: center;
}
</style>