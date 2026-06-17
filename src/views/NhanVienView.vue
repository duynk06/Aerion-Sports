  <template>
    <MainLayout title="Nhân viên">

      <div v-if="!isDetailMode && !isFormMode">
        <div class="search-filter-box no-print"
          style="border: 1px solid #f0f0f0; border-radius: 4px; overflow: hidden; background: #fff; margin-bottom: 25px; box-shadow: 0 1px 3px rgba(0,0,0,0.05);">
          <div class="filter-header"
            style="background-color: #f79b66; color: #fff; padding: 10px 15px; font-weight: bold; font-size: 15px; display: flex; align-items: center; gap: 8px;">
            <span class="filter-icon"><i class="fas fa-filter"></i></span>
            <span> Bộ lọc tìm kiếm</span>
          </div>

          <div class="filter-row"
            style="padding: 15px; display: flex; align-items: flex-end; flex-wrap: nowrap; gap: 15px; background-color: #fff;">
            <div class="filter-item search-input" style="flex: 1; min-width: 200px;">
              <label style="display: block; margin-bottom: 6px; color: #555; font-size: 13px;">Họ và tên, sđt, email</label>
              <input type="text" v-model="filterParams.keyword" placeholder="Nhập họ và tên, sđt, email..."
                @input="handleFilter"
                style="width: 100%; padding: 6px 12px; border: 1px solid #ccc; border-radius: 4px; height: 36px; box-sizing: border-box; font-size: 13px;" />
            </div>

            <div class="filter-item gender-radio" style="min-width: 180px;">
              <label style="display: block; margin-bottom: 6px; color: #555; font-size: 13px;">Giới tính</label>
              <div class="radio-group"
                style="display: flex; gap: 12px; height: 36px; align-items: center; font-size: 13px;">
                <label style="cursor: pointer; display: flex; align-items: center; gap: 4px;">
                  <input type="radio" v-model="filterParams.gioiTinh" value="" @change="handleFilter" style="accent-color: #f17236; width: 16px; height: 16px;" /> Tất cả
                </label>
                <label style="cursor: pointer; display: flex; align-items: center; gap: 4px;">
                  <input type="radio" v-model="filterParams.gioiTinh" value="1" @change="handleFilter" style="accent-color: #f17236; width: 16px; height: 16px;" /> Nam
                </label>
                <label style="cursor: pointer; display: flex; align-items: center; gap: 4px;">
                  <input type="radio" v-model="filterParams.gioiTinh" value="0" @change="handleFilter" style="accent-color: #f17236; width: 16px; height: 16px;" /> Nữ
                </label>
              </div>
            </div>

            <div class="filter-item date-input" style="min-width: 150px;">
              <label style="display: block; margin-bottom: 6px; color: #555; font-size: 13px;">Ngày sinh</label>
              <input type="date" v-model="filterParams.ngaySinh" @change="handleFilter"
                style="width: 100%; padding: 6px 12px; border: 1px solid #ccc; border-radius: 4px; height: 36px; box-sizing: border-box; font-size: 13px;" />
            </div>

            <div class="filter-item status-radio" style="min-width: 240px;">
              <label style="display: block; margin-bottom: 6px; color: #555; font-size: 13px;">Trạng thái</label>
              <div class="radio-group"
                style="display: flex; gap: 12px; height: 36px; align-items: center; font-size: 13px;">
                <label style="cursor: pointer; display: flex; align-items: center; gap: 4px;">
                  <input type="radio" v-model="filterParams.trangThai" value="" @change="handleFilter" style="accent-color: #f17236; width: 16px; height: 16px;" /> Tất cả
                </label>
                <label style="cursor: pointer; display: flex; align-items: center; gap: 4px;">
                  <input type="radio" v-model="filterParams.trangThai" value="1" @change="handleFilter" style="accent-color: #f17236; width: 16px; height: 16px;" /> Hoạt động
                </label>
                <label style="cursor: pointer; display: flex; align-items: center; gap: 4px;">
                  <input type="radio" v-model="filterParams.trangThai" value="0" @change="handleFilter" style="accent-color: #f17236; width: 16px; height: 16px;" /> Ngừng hoạt động
                </label>
              </div>
            </div>

            <button class="refresh-btn" title="Làm mới bộ lọc" @click="resetFilters"
              style="height: 36px; width: 36px; padding: 0; display: flex; align-items: center; justify-content: center; border: 1px solid #ccc; background-color: #fff; border-radius: 4px; cursor: pointer; color: #f17236; box-sizing: border-box;">
              <svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M21.5 2v6h-6M21.34 15.57a10 10 0 1 1-.57-8.38l5.67-5.67" />
              </svg>
            </button>
          </div>
        </div>

        <div class="table-box" style="border: 1px solid #e0e0e0; border-radius: 4px; overflow: hidden; background: #fff; box-shadow: 0 1px 3px rgba(0,0,0,0.02);">
          <div class="table-header-container" style="display: flex; justify-content: space-between; align-items: center; padding: 15px; border-bottom: 1px solid #e0e0e0; background-color: #fff;">
            <div class="list-title" style="display: flex; flex-direction: column;">
              <span style="font-size: 18px; font-weight: bold; color: #2c3e50;">Danh sách nhân viên</span>
              <span style="font-size: 12px; color: #888; margin-top: 2px;">Quản lý thông tin nhân viên</span>
            </div>
            <div class="btn-group no-print" style="display: flex; gap: 8px; align-items: center;">
  
  <button class="export-btn" @click="handleExport" 
    style="background: #75c589; border: 1px solid #66b079; color: #ffffff; height: 36px; padding: 0 12px; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 500; display: flex; align-items: center; justify-content: center; box-sizing: border-box; transition: background 0.2s;">
    Xuất file
  </button>
  
  <button class="export-btn" @click="handlePrint" 
    style="background: #75c589; border: 1px solid #66b079; color: #ffffff; height: 36px; padding: 0 12px; border-radius: 4px; cursor: pointer; font-size: 13px; font-weight: 500; display: flex; align-items: center; justify-content: center; box-sizing: border-box; transition: background 0.2s;">
    In danh sách
  </button>

  <button class="add-btn" @click="router.push('/nhan-vien/them')" 
    style="background-color: #f79b66; color: white; border: none; padding: 0 16px; height: 36px; border-radius: 4px; font-weight: 500; cursor: pointer; font-size: 13px; display: flex; align-items: center; justify-content: center; gap: 4px; box-sizing: border-box;">
    + Thêm mới
  </button>
</div>
          </div>

          <table style="width: 100%; border-collapse: separate; border-spacing: 0; text-align: left; font-size: 13px;">
            <thead>
              <tr>
                <th scope="col" style="padding: 12px 10px; width: 4%; font-weight: 600; background-color: #f79b66; color: white; border-top-left-radius: 4px; border-bottom-left-radius: 4px;">STT</th>
                <th scope="col" style="padding: 12px 10px; width: 6%; font-weight: 600; background-color: #f79b66; color: white;" class="no-print">Ảnh</th>
                <th scope="col" style="padding: 12px 10px; width: 13%; font-weight: 600; background-color: #f79b66; color: white;">Họ và tên</th>
                <th scope="col" style="padding: 12px 10px; width: 9%; font-weight: 600; background-color: #f79b66; color: white;">Vai trò</th>
                <th scope="col" style="padding: 12px 10px; width: 7%; font-weight: 600; background-color: #f79b66; color: white;">Giới tính</th>
                <th scope="col" style="padding: 12px 10px; width: 9%; font-weight: 600; background-color: #f79b66; color: white;">Ngày sinh</th>
                <th scope="col" style="padding: 12px 10px; width: 10%; font-weight: 600; background-color: #f79b66; color: white;">Số điện thoại</th>
                <th scope="col" style="padding: 12px 10px; width: 14%; font-weight: 600; background-color: #f79b66; color: white;">Email</th>
                <th scope="col" style="padding: 12px 10px; width: 15%; font-weight: 600; background-color: #f79b66; color: white;">Địa chỉ</th>
                <th scope="col" style="padding: 12px 10px; width: 10%; font-weight: 600; background-color: #f79b66; color: white;">Trạng thái</th>
                <th scope="col" style="padding: 12px 10px; width: 13%; font-weight: 600; text-align: center; background-color: #f79b66; color: white; border-top-right-radius: 4px; border-bottom-right-radius: 4px;" class="no-print">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(nhanVien, index) in pagedNhanVien" :key="nhanVien.id"
                style="border-bottom: 1px solid #eee; transition: background 0.2s;"
                onmouseover="this.style.backgroundColor='#fdf5f0'" onmouseout="this.style.backgroundColor='transparent'">
                <td style="padding: 12px 10px; color: #555;">{{ (currentPage - 1) * pageSize + index + 1 }}</td>
                <td style="padding: 12px 10px;" class="no-print">
                  <div class="avatar-placeholder" style="width: 36px; height: 36px; border-radius: 50%; overflow: hidden; background: #e2e8f0; display: flex; align-items: center; justify-content: center;">
                    <img v-if="nhanVien.avatar" :src="nhanVien.avatar" alt="avatar" style="width: 100%; height: 100%; object-fit: cover;" />
                    <span v-else class="avatar-default-img" style="font-size: 18px;">👤</span>
                  </div>
                </td>
                <td style="padding: 12px 10px;">
                  <div class="fw-bold text-name" style="font-weight: bold; color: #2c3e50;">{{ nhanVien.tenNv || 'Chưa cập nhật' }}</div>
                </td>
                <td style="padding: 12px 10px; color: #444;">{{ formatVaiTro(nhanVien.vaiTro) }}</td>
                <td style="padding: 12px 10px; color: #444;">{{ formatGioiTinh(nhanVien.gioiTinh) }}</td>
                <td style="padding: 12px 10px; color: #444;">{{ nhanVien.ngaySinh || '-' }}</td>
                <td class="phone-text" style="padding: 12px 10px; color: #444;">{{ nhanVien.sdt }}</td>
                <td class="email-text" style="padding: 12px 10px; color: #444; word-break: break-all;">{{ nhanVien.email }}</td>
                <td class="address-text-col" style="padding: 12px 10px; color: #555; max-width: 180px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" :title="getDefaultAddressText(nhanVien)">
                  {{ getDefaultAddressText(nhanVien) }}
                </td>
                <td style="padding: 12px 10px;">
                  <span
                    style="cursor: pointer; user-select: none; padding: 4px 8px; border-radius: 12px; font-size: 11px; font-weight: 500; display: inline-block;"
                    :style="nhanVien.trangThai === 1 ? { backgroundColor: '#e6f7ed', color: '#25a75b' } : { backgroundColor: '#feebee', color: '#ea3a3a' }"
                    title="Click để thay đổi trạng thái" @click="toggleTrangThai(nhanVien)">
                    {{ nhanVien.trangThai === 1 ? 'Hoạt động' : 'Ngừng hoạt động' }}
                  </span>
                </td>
                <td style="padding: 12px 10px; text-align: center;" class="no-print">
                  <div class="table-actions" style="display: flex; gap: 4px; justify-content: center; align-items: center;">
                    <button class="btn-view" title="Xem chi tiết" @click="viewDetail(nhanVien)" 
  style="border: none; background: #ffffff; color: #333; padding: 6px 10px; cursor: pointer; display: flex; align-items: center; transition: background 0.2s;">
  <i class="far fa-eye"></i>
</button>

<button class="btn-action-location" title="Quản lý sổ địa chỉ" @click="openAddressModal(nhanVien)" 
  style="border: none; background: #ffffff; color: #333; padding: 6px 10px; cursor: pointer; display: flex; align-items: center; transition: background 0.2s;">
  <i class="fas fa-map-marker-alt"></i>
</button>

<button class="btn-action-edit" title="Sửa thông tin" @click="openEditForm(nhanVien)" 
  style="border: none; background: #ffffff; color: #333; padding: 6px 10px; cursor: pointer; display: flex; align-items: center; transition: background 0.2s;">
  <i class="far fa-edit"></i>
</button>
                  </div>
                </td>
              </tr>
              <tr v-if="filteredNhanVien.length === 0">
                <td colspan="11" class="no-data" style="text-align: center; padding: 30px; color: #888;">Không tìm thấy nhân viên phù hợp</td>
              </tr>
            </tbody>
          </table>
        </div>

        <div class="main-pagination-container mt-3 no-print" v-if="filteredNhanVien.length > 0" style="display: flex; justify-content: space-between; align-items: center; font-size: 13px; margin-top: 15px;">
          <div class="pagination-info" style="color: #666;">
            Tổng số <strong>{{ filteredNhanVien.length }}</strong> nhân viên
          </div>
          <div class="pagination-controls" style="display: flex; gap: 5px; align-items: center;">
            <button class="page-btn" :disabled="currentPage === 1" @click="currentPage--" style="padding: 4px 8px; border: 1px solid #ccc; background: #fff; border-radius: 4px; cursor: pointer; font-size: 12px;">‹</button>
            <button v-for="page in totalPages" :key="page" @click="currentPage = page" style="padding: 4px 10px; border: 1px solid #ccc; border-radius: 4px; cursor: pointer; font-size: 12px; font-weight: 500;"
              :style="currentPage === page ? { backgroundColor: '#f79b66', color: '#fff', borderColor: '#f17236' } : { backgroundColor: '#fff', color: '#333' }">
              {{ page }}
            </button>
            <button class="page-btn" :disabled="currentPage === totalPages" @click="currentPage++" style="padding: 4px 8px; border: 1px solid #ccc; background: #fff; border-radius: 4px; cursor: pointer; font-size: 12px;">›</button>
            
          </div>
        </div>
      </div>

      <div v-else-if="isDetailMode" class="customer-form-container customer-detail-container mb-4 no-print"
        style="border: 1px solid #3b82f6; border-radius: 6px; padding: 20px; background: #fff; box-shadow: 0 2px 8px rgba(59,130,246,0.1);">
        <div class="back-to-list mb-3" @click="cancelDetailMode" style="cursor: pointer; color: #555; font-size: 14px;">
          ← Đóng vùng xem chi tiết
          <div style="font-size: 18px; font-weight: bold; color: #2c3e50; margin-top: 4px;">Chi tiết thông tin nhân viên</div>
        </div>

        <div class="profile-grid">
          <div class="avatar-upload-block">
            <div class="section-sub-title fw-bold mb-3">Ảnh đại diện</div>
            <div class="avatar-preview-circle">
              <img v-if="nhanVienDetail.avatar" :src="nhanVienDetail.avatar" alt="Avatar" />
              <span v-else class="default-avatar-icon">👤</span>
            </div>
            <div :class="['status-badge-detail mt-3', nhanVienDetail.trangThai === 1 ? 'badge-active-blue' : 'badge-inactive-red']">
              {{ nhanVienDetail.trangThai === 1 ? 'Hoạt động' : 'Ngừng hoạt động' }}
            </div>
          </div>

          <div class="info-fields-block">
            <div class="section-sub-title fw-bold mb-3">Thông tin cá nhân</div>
            <div class="form-row">
              <div class="form-cell">
                <label>Họ và tên</label>
                <input type="text" :value="nhanVienDetail.tenNv" disabled class="disabled-input" />
              </div>
              <div class="form-cell">
                <label>Số điện thoại</label>
                <input type="text" :value="nhanVienDetail.sdt" disabled class="disabled-input" />
                <small v-if="nhanVienForm.sdt && !validateSdt(nhanVienForm.sdt)" style="color: red;">
      Số điện thoại không đúng định dạng.
    </small>
              </div>
            </div>
            <div class="form-row">
              <div class="form-cell">
                <label>Email</label>
                <input type="email" :value="nhanVienDetail.email" disabled class="disabled-input" />
              </div>
              <div class="form-cell">
                <label>Ngày sinh</label>
                <input type="text" :value="nhanVienDetail.ngaySinh || '---- / -- / --'" disabled class="disabled-input" />
              </div>
            </div>
            <div class="form-row">
              <div class="form-cell">
                <label>Giới tính</label>
                <input type="text" :value="formatGioiTinh(nhanVienDetail.gioiTinh)" disabled class="disabled-input" />
              </div>
              <div class="form-cell">
                <label>Vai trò</label>
                <input type="text" :value="formatVaiTro(nhanVienDetail.vaiTro)" disabled class="disabled-input" />
              </div>
            </div>
          </div>
        </div>

        <div class="table-responsive" v-if="nhanVienDetail.addresses && nhanVienDetail.addresses.length > 0">
          <table class="table table-bordered table-hover align-middle bg-white text-center">
            <thead class="table-light">
              <tr>
                <th style="width: 80px;">STT</th>
                <th>Địa chỉ chi tiết</th>
                <th style="width: 200px;">Loại địa chỉ</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(addr, index) in nhanVienDetail.addresses" :key="addr.id || index">
                <td>{{ index + 1 }}</td>
                <td class="text-start">
                  {{ addr.chiTiet || `Số ${addr.chiTietCuThe || ''}, Phường ${addr.phuongXa || ''}, ${addr.tinhThanh || ''}` }}
                </td>
                <td class="text-center">
                  <span v-if="addr.isDefault" class="badge-active" style="background-color: #e6f7ff; color: #1890ff; border: 1px solid #91d5ff; padding: 3px 8px; border-radius: 4px; font-size: 12px;">
                    ⭐ Mặc định
                  </span>
                  <span v-else style="color: #999; font-size: 12px;">Phụ</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-else class="alert alert-light border text-muted py-3 ps-3">Chưa có thông tin địa chỉ.</div>

        <div class="form-submit-bar mt-4" style="display: flex; justify-content: flex-end; gap: 10px;">
          <button class="btn-cancel-action" @click="cancelDetailMode">Đóng lại</button>
          <button class="btn-submit-action" @click="openEditFormFromDetail(nhanVienDetail)">Chỉnh sửa thông tin</button>
        </div>
      </div>

      <div v-if="isFormMode" class="customer-form-container mb-4"
        style="border: 1px solid #25a75b; border-radius: 6px; padding: 20px; background: #fff; box-shadow: 0 2px 8px rgba(37,167,91,0.1);">
        <div class="back-to-list mb-3" @click="cancelFormMode" style="cursor: pointer; color: #555; font-size: 14px;">
          ← Hủy cập nhật thông tin
          <div style="font-size: 18px; font-weight: bold; color: #2c3e50; margin-top: 4px;">Cập nhật thông tin nhân viên</div>
        </div>

        <div class="form-section-card edit-form-card">
          <form @submit.prevent="submitEditForm">
            <div class="profile-grid">
              <div class="avatar-upload-block">
                <div class="section-sub-title fw-bold mb-3">Ảnh đại diện</div>
                <div class="avatar-preview-circle mb-3">
                  <img v-if="nhanVienForm.avatar" :src="nhanVienForm.avatar" alt="Avatar" />
                  <span v-else class="default-avatar-icon">👤</span>
                </div>
                <button type="button" class="btn-upload-avatar mb-2" @click="triggerFormFileInput">📁 Chọn ảnh</button>
                <input type="file" ref="formFileInputRef" style="display: none;" accept="image/jpeg, image/png, image/jpg" @change="handleFormAvatarChange" />
                <div style="font-size: 11px; color: #888; margin-top: 4px;">JPG, PNG (tối đa 2MB)</div>
              </div>

              <div class="info-fields-block">
                <div class="section-sub-title fw-bold mb-3">Thông tin nhân viên</div>

                <div class="form-row">
                  <div class="form-cell">
                    <label>Họ và tên <span class="required">*</span></label>
                    <input type="text" v-model="nhanVienForm.tenNv" required class="form-input-text" />
                  </div>
                  <div class="form-cell">
                    <label>Số điện thoại <span class="required">*</span></label>
                    <input type="text" v-model="nhanVienForm.sdt" required class="form-input-text" />
                    <small v-if="nhanVienForm.sdt && !validateSdt(nhanVienForm.sdt)" style="color: red;">
      Số điện thoại không đúng định dạng.
    </small>
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-cell">
                    <label>Email</label>
                    <input type="email" v-model="nhanVienForm.email" class="form-input-text" />
                  </div>
                  <div class="form-cell">
                    <label>Ngày sinh</label>
                    <input type="date" v-model="nhanVienForm.ngaySinh" class="form-input-text" />
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-cell">
                    <label>Giới tính</label>
                    <div class="radio-group-horizontal mt-2">
                      <label><input type="radio" v-model="nhanVienForm.gioiTinh" :value="1" style="accent-color: #f17236;" /> Nam</label>
                      <label class="ms-3"><input type="radio" v-model="nhanVienForm.gioiTinh" :value="0" style="accent-color: #f17236;" /> Nữ</label>
                    </div>
                  </div>
                  <div class="form-cell">
                    <label>Trạng thái</label>
                    <select v-model="nhanVienForm.trangThai" class="form-select-control">
                      <option :value="1">Hoạt động</option>
                      <option :value="0">Ngừng hoạt động</option>
                    </select>
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-cell">
                    <label>Vai trò</label>
                    <select v-model="nhanVienForm.vaiTro" class="form-select-control">
                      <option :value="1">Quản trị viên</option>
                      <option :value="2">Quản lý</option>
                      <option :value="3">Nhân viên</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>

            <div class="address-management-block mt-4 pt-4" style="border-top: 1px dashed #e9e9e9;">
              

              <div class="address-management-block mt-4 pt-4" style="border-top: 1px dashed #e9e9e9;">

    <div class="address-item-box mb-3 p-3" style="border: 1px solid #f0f0f0; border-radius: 6px; background: #fbfbfb;">
      <div class="form-row">
        <div class="form-cell">
          <label>Tỉnh / Thành phố <span class="required">*</span></label>
          <select v-model="nhanVienForm.address.tinhThanh" class="form-select-control" required>
            <option value="Hà Nội">Thành phố Hà Nội</option>
            <option value="Hồ Chí Minh">Thành phố Hồ Chí Minh</option>
            <option value="Đà Nẵng">Thành phố Đà Nẵng</option>
          </select>
        </div>
        <div class="form-cell">
          <label>Phường / Xã <span class="required">*</span></label>
          <select v-model="nhanVienForm.address.phuongXa" class="form-select-control" required>
            <option value="Văn Quán">Phường Văn Quán</option>
            <option value="Mộ Lao">Phường Mộ Lao</option>
            <option value="Dịch Vọng">Phường Dịch Vọng</option>
          </select>
        </div>
      </div>
      <div class="form-row mt-2">
        <div class="form-cell w-100">
          <label>Địa chỉ chi tiết <span class="required">*</span></label>
          <input type="text" v-model="nhanVienForm.address.chiTietCuThe" class="form-input-text" placeholder="Số nhà, ngõ, ngách..." required />
        </div>
      </div>
    </div>
  </div>
            </div>

            <div class="form-submit-bar mt-4" style="display: flex; justify-content: flex-end; gap: 10px;">
              <button type="button" class="btn-cancel-action" @click="cancelFormMode">Hủy bỏ</button>
              <button type="submit" class="btn-submit-action bg-success">Lưu thay đổi</button>
            </div>
          </form>
        </div>
      </div>

      <div v-if="showAddressModal" class="custom-modal-overlay">
        <div class="custom-modal-container">
          <div class="custom-modal-header">
            <div class="modal-title-block">
              <span class="modal-icon-marker"></span>
              <div>
                <div class="modal-main-title">Sổ địa chỉ nhân viên</div>
                <div class="modal-sub-title">{{ selectedNhanVienForAddress?.tenNv }} - {{ selectedNhanVienForAddress?.sdt }}</div>
              </div>
            </div>
            <button class="modal-close-btn" @click="closeAddressModal">✕</button>
          </div>

          <div class="custom-modal-body">
            <div class="modal-body-left">
              <div class="section-badge-title">
                <span class="icon-small"></span> Danh sách địa chỉ hiện có
              </div>

              <table class="modal-address-table">
                <thead>
                  <tr>
                    <th style="width: 12%">STT</th>
                    <th style="width: 53%">Địa chỉ</th>
                    <th style="width: 15%">Mặc định</th>
                    <th style="width: 20%">Trạng thái</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(addr, index) in currentAddressList" :key="addr.id || index">
                    <td>{{ index + 1 }}</td>
                    <td>
                      <div class="address-cell-content">
                        <span>🏠 {{ addr.chiTiet || `Số ${addr.chiTietCuThe}, Phường ${addr.phuongXa}, ${addr.tinhThanh}` }}</span>
                      </div>
                    </td>
                    <td>
                      <span v-if="addr.isDefault" class="star-active" style="cursor: default; color: #f17236;">⭐</span>
                      <span v-else class="star-inactive" style="cursor: pointer;" @click="setAsDefaultAddress(addr)" title="Đặt làm mặc định">☆</span>
                    </td>
                    <td>
                      <span style="background-color: #e6f7ed; color: #25a75b; padding: 3px 8px; border-radius: 12px; font-size: 11px; font-weight: 500; display: inline-block;">Hoạt động</span>
                    </td>
                  </tr>
                  <tr v-if="currentAddressList.length === 0">
                    <td colspan="4" class="text-center text-muted" style="padding: 15px;">Chưa có địa chỉ nào.</td>
                  </tr>
                </tbody>
              </table>
              <div class="modal-pagination-footer">
                <span>Tổng {{ currentAddressList.length }} địa chỉ</span>
              </div>
            </div>

            <div class="modal-body-right">
              <div class="section-badge-title">
                Thêm nhanh địa chỉ
              </div>

              <div class="modal-form-group">
                <label>Tỉnh / Thành phố <span class="required">*</span></label>
                <select v-model="newAddress.tinhThanh" class="form-select-control">
                  <option value="">-- Chọn Tỉnh / TP --</option>
                  <option value="Hà Nội">Thành phố Hà Nội</option>
                  <option value="Hồ Chí Minh">Thành phố Hồ Chí Minh</option>
                  <option value="Đà Nẵng">Thành phố Đà Nẵng</option>
                </select>
              </div>

              <div class="modal-form-group">
                <label>Phường / Xã <span class="required">*</span></label>
                <select v-model="newAddress.phuongXa" class="form-select-control">
                  <option value="">-- Chọn Phường / Xã --</option>
                  <option value="Văn Quán">Phường Văn Quán</option>
                  <option value="Mộ Lao">Phường Mộ Lao</option>
                  <option value="Dịch Vọng">Phường Dịch Vọng</option>
                </select>
              </div>

              <div class="modal-form-group">
                <label>Địa chỉ chi tiết <span class="required">*</span></label>
                <textarea v-model="newAddress.chiTietCuThe" placeholder="Nhập địa chỉ chi tiết (VD: Số nhà, số ngõ, ngách...)"></textarea>
              </div>

              <div class="modal-form-actions">
                <button class="btn-modal-cancel" @click="resetAddressForm">Hủy</button>
                <button class="btn-modal-submit" @click="submitQuickAddress">Thêm nhanh</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </MainLayout>
  </template>

  <style scoped>
  @media print {

    /* Ẩn bộ lọc tìm kiếm, phân trang và các nút điều hướng không liên quan */
    .no-print,
    .search-filter-box,
    .btn-group,
    .main-pagination-container,
    th.no-print,
    td.no-print {
      display: none !important;
    }

    /* Giữ định dạng bảng dàn trang rộng toàn màn hình giấy */
    .table-box {
      border: none !important;
      box-shadow: none !important;
    }

    table {
      width: 100% !important;
      border: 1px solid #333 !important;
    }

    th {
      background-color: #f17236 !important;
      color: #000 !important;
      /* Đảm bảo mực in rõ ràng */
      -webkit-print-color-adjust: exact;
      print-color-adjust: exact;
    }
  }
  </style>

  <script setup>
  import MainLayout from '../layouts/MainLayout.vue'
  import { onMounted, ref, computed, watch } from 'vue' // Thêm watch ở đây
  import { useRouter } from 'vue-router'
  import * as XLSX from 'xlsx'

  // Import các hàm giao tiếp API từ NhanVienService
  import {
    fetchAllNhanVien,
    updateNhanVien,
    changeStatusNhanVien // Hãy dùng hàm này nếu backend hỗ trợ đổi trạng thái nhanh
  } from '@/service/NhanVienService'

  const isValidPhoneNumber = (phone) => {
    const vnf_regex = /((09|03|07|08|05)+([0-9]{8})\b)/g;
    return vnf_regex.test(phone);
  };
  const validateSdt = (sdt) => {
    if (!sdt) return false;
    // Ví dụ regex kiểm tra số điện thoại (10 chữ số)
    const regex = /^[0-9]{10}$/;
    return regex.test(sdt);
  };
  const router = useRouter()

  const listNhanVien = ref([])
  const isDetailMode = ref(false)
  const isFormMode = ref(false)

  const filterParams = ref({ keyword: '', gioiTinh: '', ngaySinh: '', trangThai: '' })
  const currentPage = ref(1)
  const pageSize = ref(5)

  const nhanVienDetail = ref({
    id: '', tenNv: '', sdt: '', email: '', ngaySinh: '', gioiTinh: '', avatar: '', vaiTro: '', trangThai: '', diaChi: '', addresses: []
  })

  // Đồng bộ kiểu dữ liệu số (Number) giống màn hình thêm mới
  // Xóa hoặc thay thế nhanVienForm hiện tại bằng:
  const nhanVienForm = ref({
    id: '', tenNv: '', sdt: '', email: '', ngaySinh: '', gioiTinh: 1, 
    avatar: '', vaiTro: 3, trangThai: 1, diaChi: '', 
    address: { tinhThanh: '', phuongXa: '', chiTietCuThe: '' } // Chỉ 1 object
  })

  // Trạng thái quản lý Modal địa chỉ khách hàng/nhân viên
  const showAddressModal = ref(false)
  const selectedNhanVienForAddress = ref(null)
  const currentAddressList = ref([])
  const newAddress = ref({ tinhThanh: '', phuongXa: '', chiTietCuThe: '', isDefault: false })

  // 🌟 LOGIC CHỌN ẢNH ĐẠI DIỆN CHO FORM CẬP NHẬT (ĐÃ CHUYỂN .anh SANG .avatar)
  const formFileInputRef = ref(null)

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
      nhanVienForm.value.avatar = e.target.result
    }
    reader.readAsDataURL(file)
  }

  // --- 1. TẢI TOÀN BỘ DANH SÁCH TỪ DATABASE SQL ---
  const handleFetchAllData = async () => {
    try {
      const data = await fetchAllNhanVien();
      if (Array.isArray(data)) {
        listNhanVien.value = data.map(item => {
          // Giải quyết object lồng nhau vaiTro gửi về từ Spring Boot
          let role = 3;
          if (item.vaiTro && typeof item.vaiTro === 'object' && item.vaiTro.id !== undefined) {
            role = Number(item.vaiTro.id);
          } else if (item.id_vai_tro !== undefined && item.id_vai_tro !== null) {
            role = Number(item.id_vai_tro);
          } else if (item.vaiTro !== undefined && item.vaiTro !== null && !isNaN(item.vaiTro)) {
            role = Number(item.vaiTro);
          }

          // 🌟 ĐỒNG BỘ ĐỊA CHỈ
          let processedAddresses = [];
          if (Array.isArray(item.addresses) && item.addresses.length > 0) {
            processedAddresses = item.addresses.map(addr => ({
              ...addr,
              tinhThanh: addr.tinhThanh || '',
              phuongXa: addr.phuongXa || '',
              chiTietCuThe: addr.chiTietCuThe || addr.chiTiet || '',
              chiTiet: addr.chiTiet || `Số ${addr.chiTietCuThe || ''}, Phường ${addr.phuongXa || ''}, ${addr.tinhThanh || ''}`
            }));
          } else if (item.diaChi && item.diaChi.trim() !== '') {
            processedAddresses = [{
              id: 'ADDR_' + (item.id || item.idNhanVien),
              tinhThanh: '',
              phuongXa: '',
              chiTietCuThe: item.diaChi,
              chiTiet: item.diaChi,
              isDefault: true
            }];
          }

          return {
            ...item,
            id: item.id || item.idNhanVien,
            tenNv: item.tenNv || item.ten_nv,
            sdt: item.sdt || item.sđt,
            gioiTinh: item.gioiTinh !== undefined ? Number(item.gioiTinh) : 1,
            trangThai: (item.trangThai === 1 || item.trangThai === true) ? 1 : 0,
            vaiTro: role,
            diaChi: item.diaChi || '',
            avatar: item.avatar || item.anh || '', // Nhận linh hoạt cả avatar hoặc anh từ API
            addresses: processedAddresses
          };
        });

        // Giữ đồng bộ dữ liệu nếu người dùng đang đứng ở màn hình xem chi tiết
        if (isDetailMode.value && nhanVienDetail.value.id) {
          const updatedDetail = listNhanVien.value.find(nv => nv.id === nhanVienDetail.value.id);
          if (updatedDetail) nhanVienDetail.value = JSON.parse(JSON.stringify(updatedDetail));
        }
      }
    } catch (error) {
      console.error('Lỗi khi tải danh sách nhân viên:', error);
      alert('Không thể kết nối dữ liệu từ SQL Server!');
    }
  };

  onMounted(handleFetchAllData)

  // --- 2. IN ẤN & XUẤT EXCEL ---
  const handlePrint = () => {
    window.print()
  }

  const handleExport = () => {
    if (filteredNhanVien.value.length === 0) {
      alert('Không có dữ liệu nhân viên nào để xuất file!')
      return
    }

    const dataToExport = filteredNhanVien.value.map((item, index) => {
      return {
        'STT': index + 1,
        'Mã Nhân Viên': item.id || 'N/A',
        'Họ và Tên': item.tenNv || '',
        'Số Điện Thoại': item.sdt || '',
        'Email': item.email || '',
        'Ngày Sinh': item.ngaySinh || '',
        'Giới Tính': formatGioiTinh(item.gioiTinh),
        'Chức Vụ': formatVaiTro(item.vaiTro),
        'Địa Chỉ Chính': item.diaChi || 'Chưa có địa chỉ',
        'Trạng thái': item.trangThai === 1 ? 'Đang hoạt động' : 'Ngưng hoạt động'
      }
    })

    const worksheet = XLSX.utils.json_to_sheet(dataToExport)
    const workbook = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Danh Sách Nhân Viên')
    XLSX.writeFile(workbook, 'Danh_Sach_Nhan_Vien.xlsx')
  }

  // --- 3. QUẢN LÝ SỔ ĐỊA CHỈ QUA MODAL ---
  const openAddressModal = (nhanVien) => {
    selectedNhanVienForAddress.value = nhanVien
    currentAddressList.value = JSON.parse(JSON.stringify(nhanVien.addresses || []))
    resetAddressForm()
    showAddressModal.value = true
  }

  const closeAddressModal = () => { showAddressModal.value = false }
  const resetAddressForm = () => { newAddress.value = { tinhThanh: '', phuongXa: '', chiTietCuThe: '', isDefault: false } }

  const submitQuickAddress = async () => {
    // Tạo chuỗi mới sạch
    const fullString = `${newAddress.value.chiTietCuThe}, ${newAddress.value.phuongXa}, ${newAddress.value.tinhThanh}`;

    const newAddrObj = {
      id: 'ADDR_' + Date.now(),
      ...newAddress.value,
      chiTiet: fullString
    };

    try {
      const payload = {
        ...selectedNhanVienForAddress.value,
        diaChi: fullString, // Ghi đè, không lấy giá trị cũ
        addresses: [newAddrObj],
        vaiTro: { id: Number(selectedNhanVienForAddress.value.vaiTro) }
      };

      await updateNhanVien(selectedNhanVienForAddress.value.id, payload);
      alert('Thêm địa chỉ thành công!');
      await handleFetchAllData();
      showAddressModal.value = false;
    } catch (error) {
      alert('Thêm địa chỉ thất bại!');
    }
  }

  const setAsDefaultAddress = async (addrItem) => {
    currentAddressList.value.forEach(addr => {
      addr.isDefault = (addr.id === addrItem.id)
    })

    try {
      const payload = {
        ...selectedNhanVienForAddress.value,
        diaChi: addrItem.chiTiet,
        addresses: currentAddressList.value,
        vaiTro: { id: Number(selectedNhanVienForAddress.value.vaiTro) }
      }

      await updateNhanVien(selectedNhanVienForAddress.value.id, payload)
      alert('Thay đổi địa chỉ mặc định thành công!')
      await handleFetchAllData()
    } catch (error) {
      console.error('Lỗi khi đổi địa chỉ mặc định:', error)
      alert('Không thể cập nhật địa chỉ mặc định!')
    }
  }

  const removeAddressInModal = async (addrId, index) => {
    if (!confirm('Bạn có chắc chắn muốn xóa địa chỉ này không?')) return

    const wasDefault = currentAddressList.value[index].isDefault
    currentAddressList.value.splice(index, 1)

    if (wasDefault && currentAddressList.value.length > 0) {
      currentAddressList.value[0].isDefault = true
    }

    const newDefaultAddress = currentAddressList.value.find(addr => addr.isDefault)

    try {
      const payload = {
        ...selectedNhanVienForAddress.value,
        diaChi: newDefaultAddress ? newDefaultAddress.chiTiet : '',
        addresses: currentAddressList.value,
        vaiTro: { id: Number(selectedNhanVienForAddress.value.vaiTro) }
      }

      await updateNhanVien(selectedNhanVienForAddress.value.id, payload)
      alert('Xóa địa chỉ thành công!')
      await handleFetchAllData()
    } catch (error) {
      console.error('Lỗi khi xóa địa chỉ:', error)
      alert('Không thể thực hiện thao tác xóa!')
    }
  }

  // --- 4. TẠO ĐỊA CHỈ TRONG FORM CHỈNH SỬA ---
  const addNewAddressRow = () => {
    nhanVienForm.value.addresses.push({
      id: 'ADDR_NEW_' + Date.now(),
      tinhThanh: '',
      phuongXa: '',
      chiTietCuThe: '',
      chiTiet: '',
      isDefault: nhanVienForm.value.addresses.length === 0
    })
  }

  const removeAddressRow = (index) => {
    const wasDefault = nhanVienForm.value.addresses[index].isDefault
    nhanVienForm.value.addresses.splice(index, 1)
    if (wasDefault && nhanVienForm.value.addresses.length > 0) {
      nhanVienForm.value.addresses[0].isDefault = true
    }
  }

  const setFormDefaultAddress = (index) => {
    nhanVienForm.value.addresses.forEach((addr, idx) => {
      addr.isDefault = idx === index
    })
  }

  // --- 5. XEM CHI TIẾT ---
  const viewDetail = (nhanVien) => {
    nhanVienDetail.value = JSON.parse(JSON.stringify(nhanVien))
    isDetailMode.value = true
    isFormMode.value = false
  }
  const cancelDetailMode = () => { isDetailMode.value = false }

  // --- 6. CHỈNH SỬA TOÀN BỘ THÔNG TIN ---
  // 🌟 ĐÃ SỬA: Chuẩn hóa map mảng địa chỉ tương thích 100% với form input khi bấm sửa thông tin
  const openEditForm = (nhanVien) => {
    let addrData = {
      tinhThanh: '',
      phuongXa: '',
      chiTietCuThe: ''
    }

    // Nếu có addresses
    if (nhanVien.addresses && nhanVien.addresses.length > 0) {
      addrData = {
        tinhThanh: nhanVien.addresses[0].tinhThanh || '',
        phuongXa: nhanVien.addresses[0].phuongXa || '',
        chiTietCuThe: nhanVien.addresses[0].chiTietCuThe || ''
      }
    }

    // Nếu vẫn chưa có dữ liệu thì tách từ chuỗi diaChi
    if ((!addrData.tinhThanh || !addrData.phuongXa) && nhanVien.diaChi) {
      const parts = nhanVien.diaChi.split(',')

      if (parts.length >= 3) {
        addrData = {
          chiTietCuThe: parts[0].trim(),
          phuongXa: parts[1].trim(),
          tinhThanh: parts[2].trim()
        }
      }
    }

    nhanVienForm.value = {
      ...nhanVien,
      vaiTro: Number(nhanVien.vaiTro),
      gioiTinh: Number(nhanVien.gioiTinh),
      trangThai: Number(nhanVien.trangThai),
      address: addrData
    }

    console.log('Address edit:', addrData)

    isDetailMode.value = false
    isFormMode.value = true
    watch(
    () => nhanVienForm.value.address.tinhThanh,
    async (newTinh) => {
      if (!newTinh) return

      await loadPhuongXa(newTinh)

      console.log('Đã load xã/phường của:', newTinh)
    }
  )
  }

  const openEditFormFromDetail = (detailData) => {
    isDetailMode.value = false
    openEditForm(detailData)
  }

  const cancelFormMode = () => { isFormMode.value = false }

const submitEditForm = async () => {
  try {
    if (!nhanVienForm.value.tenNv || !nhanVienForm.value.sdt || !nhanVienForm.value.email) {
      alert('Vui lòng điền đầy đủ các thông tin bắt buộc!')
      return
    }

    if (!isValidPhoneNumber(nhanVienForm.value.sdt)) {
      alert('Số điện thoại không đúng định dạng!')
      return
    }

    // Ghép chuỗi địa chỉ gọn gàng
    const addr = nhanVienForm.value.address;
    if (addr && addr.tinhThanh && addr.phuongXa && addr.chiTietCuThe) {
      nhanVienForm.value.diaChi = `${addr.chiTietCuThe}, ${addr.phuongXa}, ${addr.tinhThanh}`;
    }

    // Đóng gói data đồng bộ cấu hình với hàm update của NhanVienServiceImpl
    const payload = {
      id: nhanVienForm.value.id,
      maNv: nhanVienForm.value.maNv,
      tenNv: nhanVienForm.value.tenNv,
      sdt: nhanVienForm.value.sdt,
      email: nhanVienForm.value.email.trim(),
      gioiTinh: Number(nhanVienForm.value.gioiTinh),
      ngaySinh: nhanVienForm.value.ngaySinh,
      diaChi: nhanVienForm.value.diaChi,
      avatar: nhanVienForm.value.avatar,
      trangThai: Number(nhanVienForm.value.trangThai),
      vaiTro: { 
        id: Number(nhanVienForm.value.vaiTro) // ⚡ Ép về Object chứa ID vai trò để Backend nhận diện mapping JPA
      }
    }

    await updateNhanVien(payload.id, payload)
    alert('Cập nhật thông tin nhân viên thành công!')
    isFormMode.value = false
    await handleFetchAllData()
  } catch (error) {
    alert('Cập nhật thất bại do trùng email hoặc số điện thoại!');
  }
}

  // --- 7. ĐỔI TRẠNG THÁI NHANH TRÊN DANH SÁCH ---
  const toggleTrangThai = async (nhanVien) => {
    const targetStatus = nhanVien.trangThai === 1 ? 0 : 1;
    try {
      if (typeof changeStatusNhanVien === 'function') {
        await changeStatusNhanVien(nhanVien.id, targetStatus);
      } else {
        const updatedNhanVienData = {
          ...nhanVien,
          trangThai: targetStatus,
          vaiTro: { id: Number(nhanVien.vaiTro) }
        };
        await updateNhanVien(nhanVien.id, updatedNhanVienData);
      }
      
      nhanVien.trangThai = targetStatus;
      alert('Thay đổi trạng thái nhân viên thành công!');
      await handleFetchAllData();
    } catch (error) {
      console.error('Lỗi cập nhật trạng thái:', error);
      alert('Không thể lưu trạng thái nhân viên mới vào SQL Server!');
    }
  }

  // --- 8. ĐỊNH DẠNG TEXT HIỂN THỊ (FORMATTERS) ---
  const formatGioiTinh = (val) => Number(val) === 1 ? 'Nam' : 'Nữ'

  const formatVaiTro = (vaiTro) => {
    if (vaiTro === undefined || vaiTro === null || vaiTro === '') return 'Nhân viên';
    const roleId = String(vaiTro).trim();
    const map = { '1': 'Quản trị viên', '2': 'Quản lý', '3': 'Nhân viên' };
    return map[roleId] || 'Nhân viên';
  };

  const getDefaultAddressText = (nv) => {
    return nv?.diaChi || 'Chưa có địa chỉ'
  }

  // --- 9. BỘ LỌC TÌM KIẾM & PHÂN TRANG ---
  const handleFilter = () => { currentPage.value = 1 }
  const resetFilters = () => {
    filterParams.value = { keyword: '', gioiTinh: '', ngaySinh: '', trangThai: '' }
    currentPage.value = 1
  }

  watch(filterParams, () => {
    handleFilter()
  }, { deep: true })

  const totalPages = computed(() => {
    return Math.ceil(filteredNhanVien.value.length / pageSize.value) || 1
  })

  const filteredNhanVien = computed(() => {
    return listNhanVien.value.filter(item => {
      if (filterParams.value.keyword) {
        const kw = filterParams.value.keyword.toLowerCase()
        const tenHienThi = item.tenNv || ''
        const matchKw = tenHienThi.toLowerCase().includes(kw) || item.sdt?.includes(kw) || item.email?.toLowerCase().includes(kw)
        if (!matchKw) return false
      }
      if (filterParams.value.gioiTinh !== '') {
        if (Number(item.gioiTinh) !== Number(filterParams.value.gioiTinh)) return false
      }
      if (filterParams.value.ngaySinh) {
        if (item.ngaySinh !== filterParams.value.ngaySinh) return false
      }
      if (filterParams.value.trangThai !== '') {
        if (Number(item.trangThai) !== Number(filterParams.value.trangThai)) return false
      }
      return true
    })
  })

  const pagedNhanVien = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value
    return filteredNhanVien.value.slice(start, start + pageSize.value)
  })
  </script>

  <style scoped>
  /* Đồng bộ phân trang và các Input control */
  .main-pagination-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 16px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }

  .pagination-info {
    font-size: 13px;
    color: #666;
  }

  .pagination-controls {
    display: flex;
    align-items: center;
    gap: 5px;
  }

  .page-btn {
    padding: 4px 10px;
    font-size: 13px;
    border: 1px solid #d9d9d9;
    background-color: #fff;
    border-radius: 4px;
    cursor: pointer;
  }

  .page-btn:disabled {
    background-color: #f5f5f5;
    color: #ccc;
    cursor: not-allowed;
  }

  .page-btn-number {
    min-width: 28px;
    height: 28px;
    border: 1px solid #d9d9d9;
    background-color: #fff;
    border-radius: 4px;
    cursor: pointer;
  }

  .page-btn-number.active {
    background-color: #f79b66;
    color: #fff;
    border-color: #f79b66;
  }

  .page-size-select {
    height: 28px;
    font-size: 13px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    padding: 0 4px;
  }

  .form-input-text,
  .form-select-control {
    width: 100%;
    height: 38px;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    padding: 0 12px;
    font-size: 14px;
    outline: none;
  }

  .form-input-text:focus,
  .form-select-control:focus {
    border-color: #e4925d;
  }

  .btn-upload-avatar {
    background: #fff;
    border: 1px solid #d9d9d9;
    padding: 5px 12px;
    border-radius: 4px;
    font-size: 13px;
    cursor: pointer;
  }

  .btn-add-address-orange {
    background-color: #f79b66;
    color: white;
    border: none;
    padding: 6px 14px;
    border-radius: 4px;
    font-size: 13px;
    cursor: pointer;
  }

  /* Kiểu dáng Modal gốc của Khách hàng */
  .custom-modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
  }

  .custom-modal-container {
    background: #fff;
    width: 950px;
    max-width: 95%;
    border-radius: 8px;
    overflow: hidden;
  }

  .custom-modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 14px 20px;
    border-bottom: 1px solid #f0f0f0;
  }

  .modal-title-block {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .modal-main-title {
    font-size: 15px;
    font-weight: bold;
    color: #333;
  }

  .modal-sub-title {
    font-size: 13px;
    color: #777;
  }

  .modal-close-btn {
    background: none;
    border: none;
    font-size: 16px;
    cursor: pointer;
  }

  .custom-modal-body {
    display: grid;
    grid-template-columns: 1.2fr 1fr;
    gap: 24px;
    padding: 20px;
  }

  .modal-body-left {
    border-right: 1px solid #f0f0f0;
    padding-right: 20px;
  }

  .modal-address-table {
    width: 100%;
    border-collapse: collapse;
  }

  .modal-address-table th {
    background: #fafafa;
    padding: 10px;
    font-size: 13px;
    border-bottom: 1px solid #f0f0f0;
  }

  .modal-address-table td {
    padding: 12px 10px;
    font-size: 13px;
    border-bottom: 1px solid #f0f0f0;
  }

  .star-active {
    color: #faad14;
    font-size: 16px;
    cursor: pointer;
  }

  .star-inactive {
    color: #ccc;
    font-size: 16px;
    cursor: pointer;
  }

  .badge-status-active {
    background: #f6ffed;
    color: #52c41a;
    border: 1px solid #b7eb8f;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 12px;
  }

  .modal-form-group {
    margin-bottom: 14px;
    display: flex;
    flex-direction: column;
  }

  .modal-form-group label {
    font-size: 13px;
    margin-bottom: 6px;
    font-weight: 500;
  }

  .modal-form-group textarea {
    width: 100%;
    height: 70px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    padding: 8px 10px;
    resize: none;
    font-size: 13px;
  }

  .btn-modal-cancel {
    background: #fff;
    border: 1px solid #d9d9d9;
    padding: 6px 16px;
    border-radius: 4px;
    cursor: pointer;
  }

  .btn-modal-submit {
    background: #1f2937;
    border: none;
    color: #fff;
    padding: 6px 16px;
    border-radius: 4px;
    cursor: pointer;
  }

  .bg-success {
    background-color: #2e7d32 !important;
    color: #fff;
  }

  .required {
    color: #ff4d4f;
  }

  /* ==========================================================================
    CSS MỚI: PHÂN TRANG DANH SÁCH CHÍNH & COMPONENT PHỤ TRỢ FORM SỬA
    ========================================================================== */
  .main-pagination-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 16px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }

  .pagination-info {
    font-size: 13px;
    color: #666;
  }

  .pagination-controls {
    display: flex;
    align-items: center;
    gap: 5px;
  }

  .page-btn {
    padding: 4px 10px;
    font-size: 13px;
    border: 1px solid #d9d9d9;
    background-color: #fff;
    border-radius: 4px;
    cursor: pointer;
    color: #333;
  }

  .page-btn:disabled {
    background-color: #f5f5f5;
    color: #ccc;
    cursor: not-allowed;
  }

  .page-btn-number {
    min-width: 28px;
    height: 28px;
    padding: 0 6px;
    font-size: 13px;
    border: 1px solid #d9d9d9;
    background-color: #fff;
    color: #333;
    border-radius: 4px;
    cursor: pointer;
  }

  .page-btn-number.active {
    background-color: #f79b66;
    color: #fff;
    border-color: #f79b66;
  }

  .page-size-select {
    height: 28px;
    font-size: 13px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    padding: 0 4px;
    margin-left: 5px;
    outline: none;
  }

  /* Kiểu css cho ô Form điền thông tin */
  .form-input-text {
    width: 100%;
    height: 38px;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    padding: 0 12px;
    font-size: 14px;
    color: #333;
    outline: none;
    background: #fff;
  }

  .form-input-text:focus {
    border-color: #f79b66;
  }

  .form-select-control {
    width: 100%;
    height: 38px;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    padding: 0 12px;
    font-size: 14px;
    color: #333;
    background: #fff;
    outline: none;
  }

  .bg-success {
    background-color: #2e7d32 !important;
  }

  /* ==========================================================================
    CSS SẴN CÓ ĐƯỢC GIỮ NGUYÊN GỐC
    ========================================================================== */
  .custom-modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
  }

  .custom-modal-container {
    background: #fff;
    width: 950px;
    max-width: 95%;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
    overflow: hidden;
    animation: fadeIn 0.25s ease-out;
  }

  .custom-modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 14px 20px;
    border-bottom: 1px solid #f0f0f0;
  }

  .modal-title-block {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .modal-icon-marker {
    font-size: 20px;
    color: #e74c3c;
  }

  .modal-main-title {
    font-size: 15px;
    font-weight: bold;
    color: #333;
  }

  .modal-sub-title {
    font-size: 13px;
    color: #777;
    margin-top: 2px;
  }

  .modal-close-btn {
    background: none;
    border: none;
    font-size: 16px;
    color: #aaa;
    cursor: pointer;
  }

  .custom-modal-body {
    display: grid;
    grid-template-columns: 1.2fr 1fr;
    gap: 24px;
    padding: 20px;
    background: #fff;
  }

  .modal-body-left {
    border-right: 1px solid #f0f0f0;
    padding-right: 20px;
  }

  .section-badge-title {
    font-size: 14px;
    font-weight: bold;
    color: #333;
    margin-bottom: 14px;
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .modal-address-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 12px;
  }

  .modal-address-table th {
    background: #fafafa;
    padding: 10px;
    font-size: 13px;
    font-weight: bold;
    color: #444;
    border-bottom: 1px solid #f0f0f0;
    text-align: left;
  }

  .modal-address-table td {
    padding: 12px 10px;
    font-size: 13px;
    border-bottom: 1px solid #f0f0f0;
    color: #555;
    vertical-align: middle;
  }

  .star-active {
    color: #f79b66;
    font-size: 16px;
    cursor: pointer;
  }

  .star-inactive {
    color: #ccc;
    font-size: 16px;
    cursor: pointer;
  }

  .badge-status-active {
    background: #f6ffed;
    color: #52c41a;
    border: 1px solid #b7eb8f;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 12px;
  }

  .modal-pagination-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 13px;
    color: #666;
    margin-top: 10px;
  }

  .modal-form-group {
    margin-bottom: 14px;
    display: flex;
    flex-direction: column;
  }

  .modal-form-group label {
    font-size: 13px;
    font-weight: 500;
    color: #444;
    margin-bottom: 6px;
  }

  .modal-form-group select,
  .modal-form-group textarea {
    width: 100%;
    height: 34px;
    border: 1px solid #d9d9d9;
    border-radius: 4px;
    padding: 0 10px;
    font-size: 13px;
    color: #333;
    outline: none;
  }

  .modal-form-group textarea {
    height: 70px;
    padding: 8px 10px;
    resize: none;
  }

  .required {
    color: #ff4d4f;
  }

  .modal-form-checkbox {
    margin-bottom: 18px;
  }

  .modal-form-checkbox label {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 13px;
    cursor: pointer;
    color: #444;
  }

  .modal-form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }

  .btn-modal-cancel {
    background: #fff;
    border: 1px solid #d9d9d9;
    color: #555;
    padding: 6px 16px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 13px;
  }

  .btn-modal-submit {
    background: #1f2937;
    border: none;
    color: #fff;
    padding: 6px 16px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 13px;
  }

  .add-address-btn {
    background: #fff;
    border: 1px solid #d9d9d9;
    padding: 4px 12px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 13px;
    color: #444;
  }

  

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: scale(0.97);
    }

    to {
      opacity: 1;
      transform: scale(1);
    }
  }

  .customer-detail-container {
    background: #fdfdfd;
    padding: 24px;
    border-radius: 12px;
  }

  .profile-grid {
    display: grid;
    grid-template-columns: 240px 1fr;
    gap: 30px;
    background: #fff;
    border: 1px solid #eef2f5;
    border-radius: 12px;
    padding: 24px;
  }

  .avatar-upload-block {
    display: flex;
    flex-direction: column;
    align-items: center;
    border-right: 1px solid #f0f0f0;
    padding-right: 20px;
  }

  .avatar-preview-circle {
    width: 140px;
    height: 140px;
    border-radius: 50%;
    background: #f5f5f5;
    border: 1px dashed #d9d9d9;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
  }

  .avatar-preview-circle img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .default-avatar-icon {
    font-size: 50px;
  }

  .status-badge-detail {
    padding: 4px 16px;
    border-radius: 4px;
    font-size: 13px;
    font-weight: bold;
  }

  .section-sub-title {
    font-size: 16px;
    color: #2c3e50;
    border-left: 4px solid #f79b66;
    padding-left: 8px;
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
  }

  .form-cell label {
    font-size: 13px;
    font-weight: 600;
    color: #666;
    margin-bottom: 6px;
  }

  .disabled-input {
    background-color: #f5f7fa !important;
    color: #444 !important;
    border: 1px solid #e4e7ed !important;
    height: 38px;
    padding: 0 12px;
    border-radius: 6px;
    font-size: 14px;
    cursor: not-allowed;
    width: 100%;
  }

  .address-detail-table {
    width: 100%;
    border-collapse: collapse;
    background: white;
    border-radius: 8px;
    overflow: hidden;
  }

  .address-detail-table th {
    background: #f7f9fa;
    padding: 12px;
    color: #333;
    font-weight: bold;
    font-size: 13px;
    border-bottom: 1px solid #eef0f2;
    text-align: left;
  }

  .address-detail-table td {
    padding: 14px 12px;
    border-bottom: 1px solid #f1f3f4;
    font-size: 14px;
  }

  .badge-default-address {
    background-color: #e6f7ff;
    color: #1890ff;
    padding: 4px 10px;
    border-radius: 20px;
    font-size: 12px;
    font-weight: 500;
  }

  .btn-cancel-action {
    background: #6c757d;
    color: white;
    border: none;
    padding: 9px 20px;
    border-radius: 6px;
    cursor: pointer;
    font-weight: 500;
  }

  .btn-submit-action {
    background: #1a252f;
    color: white;
    border: none;
    padding: 9px 20px;
    border-radius: 6px;
    cursor: pointer;
    font-weight: 500;
  }

  .search-filter-box {
    background: #fff;
    border-radius: 12px;
    padding: 16px 20px;
    margin-bottom: 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }

  .filter-title {
    font-size: 15px;
    font-weight: 700;
    color: #2c3e50;
    margin-bottom: 12px;
  }

  .filter-row {
    display: flex;
    align-items: flex-end;
    gap: 12px;
    flex-wrap: wrap;
  }

  .filter-item {
    display: flex;
    flex-direction: column;
  }

  .filter-item label {
    font-size: 13px;
    font-weight: 500;
    margin-bottom: 6px;
    color: #555;
  }

  .search-input {
    width: 320px;
  }

  .date-input {
    width: 180px;
  }

  .search-input input,
  .date-input input {
    width: 100%;
    height: 34px;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    padding: 0 12px;
    font-size: 14px;
    color: #333;
  }

  .radio-group {
    display: flex;
    gap: 14px;
    align-items: center;
    height: 34px;
  }

  .radio-group label {
    margin: 0;
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;
    cursor: pointer;
  }

  .refresh-btn {
    height: 34px;
    padding: 0 12px;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    background: #f5f5f5;
    color: #555;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .top-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 16px 0 12px 0;
  }

  .list-title {
    font-size: 16px;
    font-weight: 700;
    color: #2c3e50;
  }

  .add-btn {
    height: 34px;
    padding: 0 14px;
    border: none;
    border-radius: 6px;
    background: #f79b66;
    color: white;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 4px;
  }

  .table-box {
    background: white;
    border-radius: 12px;
    padding: 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }

  table {
    width: 100%;
    border-collapse: collapse;
  }

  thead th {
    background: #fff;
    padding: 12px 10px;
    font-size: 14px;
    font-weight: 700;
    color: #444;
    text-align: left;
    border-bottom: 2px solid #f0f0f0;
  }

  tbody td {
    padding: 10px 10px;
    vertical-align: middle;
    border-bottom: 1px solid #f0f0f0;
    font-size: 14px;
  }

  tbody tr:hover {
    background: #fafafa;
  }

  .avatar-placeholder {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    overflow: hidden;
    background: #e5e7eb;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .avatar-placeholder img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .avatar-default-img {
    display: inline-block;
    width: 34px;
    height: 34px;
    background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23ccc"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>');
    background-size: cover;
    border-radius: 50%;
  }

  .text-name {
    font-weight: 600;
    color: #333;
  }

  .status-badge-preview {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 4px 10px;
    border-radius: 4px;
    font-size: 13px;
    font-weight: 500;
  }

  .table-actions {
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .table-actions button {
    background: none;
    border: none;
    padding: 6px 10px;
    cursor: pointer;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 28px;
  }

  .btn-action-view {
    background-color: #e6f7ff !important;
    color: #1890ff;
  }

   

  .switch-control {
    position: relative;
    display: inline-block;
    width: 36px;
    height: 20px;
    margin-left: 4px;
  }

  .slider-round {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    transition: .3s;
    border-radius: 20px;
  }

  .slider-round:before {
    position: absolute;
    content: "";
    height: 14px;
    width: 14px;
    left: 3px;
    bottom: 3px;
    background-color: white;
    transition: .3s;
    border-radius: 50%;
  }

  input:checked+.slider-round {
    background-color: #52c41a;
  }

  input:checked+.slider-round:before {
    transform: translateX(16px);
  }
  </style>

  <style scoped>
  /* ==========================================================================
    1. KHUNG BỘ LỌC (SEARCH & FILTER BOX)
    ========================================================================== */
  .search-filter-box {
    background: #fff;
    border-radius: 12px;
    padding: 16px 20px;
    margin-bottom: 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }

  .filter-title {
    font-size: 15px;
    font-weight: 700;
    color: #2c3e50;
    margin-bottom: 12px;
  }

  .filter-row {
    display: flex;
    align-items: flex-end;
    gap: 12px;
    flex-wrap: wrap;
  }

  .filter-item {
    display: flex;
    flex-direction: column;
  }

  .filter-item label {
    font-size: 13px;
    font-weight: 500;
    margin-bottom: 6px;
    color: #555;
  }

  /* Input độ rộng thu nhỏ vừa phải */
  .search-input {
    width: 320px;
  }

  .date-input {
    width: 180px;
  }

  .search-input input,
  .date-input input {
    width: 100%;
    height: 34px;
    /* Giảm từ 46px xuống 34px chuẩn compact */
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    padding: 0 12px;
    font-size: 14px;
    color: #333;
    transition: all 0.3s;
  }

  .search-input input:focus,
  .date-input input:focus {
    border-color: #f79b66;
    outline: none;
  }

  .radio-group {
    display: flex;
    gap: 14px;
    align-items: center;
    height: 34px;
  }

  .radio-group label {
    margin: 0;
    display: flex;
    align-items: center;
    gap: 4px;
    font-size: 14px;
    cursor: pointer;
  }

  .refresh-btn {
    height: 34px;
    padding: 0 12px;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    background: #f5f5f5;
    color: #555;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    transition: background 0.2s;
  }

  .refresh-btn:hover {
    background: #e8e8e8;
  }

  /* ==========================================================================
    2. TIÊU ĐỀ DANH SÁCH & BUTTON GÓC PHẢI (TOP ACTIONS)
    ========================================================================== */
  .top-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 16px 0 12px 0;
  }

  .list-title {
    font-size: 16px;
    font-weight: 700;
    color: #2c3e50;
  }

  .btn-group {
    display: flex;
    gap: 8px;
    align-items: center;
  }

  .action-icon-btn {
    height: 34px;
    padding: 0 12px;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    background: white;
    color: #1890ff;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
  }

  .action-icon-btn:hover {
    background: #f5f5f5;
  }

  .add-btn {
    height: 34px;
    padding: 0 14px;
    border: none;
    border-radius: 6px;
    background: #f79b66;
    color: white;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 4px;
  }

  .add-btn:hover {
    background: #f79b66;
  }

  /* ==========================================================================
    3. BẢNG DỮ LIỆU CHUẨN COMPACT (DATA TABLE)
    ========================================================================== */
  .table-box {
    background: white;
    border-radius: 12px;
    padding: 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  }

  table {
    width: 100%;
    border-collapse: collapse;
  }

  thead th {
    background: #fff;
    padding: 12px 10px;
    /* Thu nhỏ padding header */
    font-size: 14px;
    font-weight: 700;
    color: #444;
    text-align: left;
    border-bottom: 2px solid #f0f0f0;
  }

  tbody td {
    padding: 10px 10px;
    /* Thu nhỏ chiều cao hàng */
    vertical-align: middle;
    border-bottom: 1px solid #f0f0f0;
    font-size: 14px;
  }

  tbody tr:hover {
    background: #fafafa;
  }

  /* Avatar nhỏ gọn */
  .avatar-placeholder {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    overflow: hidden;
    background: #e5e7eb;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .avatar-placeholder img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .avatar-default-img {
    display: inline-block;
    width: 34px;
    height: 34px;
    background-image: url('data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="%23ccc"><path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>');
    background-size: cover;
    border-radius: 50%;
  }

  .text-name {
    font-weight: 600;
    color: #333;
  }

  /* Badge trạng thái */
  .status-badge-preview {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    padding: 4px 10px;
    border-radius: 4px;
    font-size: 13px;
    font-weight: 500;
  }

  .badge-active-blue {
    background: #e6f7ff;
    color: #1890ff;
    border: 1px solid #91d5ff;
  }

  .badge-inactive-red {
    background: #fff1f0;
    color: #f5222d;
    border: 1px solid #f79b66;
  }

  /* Thao tác nút trong bảng */
  .table-actions {
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .table-actions button {
    background: none;
    border: none;
    padding: 6px 10px;
    cursor: pointer;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 13px;
    height: 28px;
  }

  .btn-action-view {
    background-color: #e6f7ff !important;
    color: #1890ff;
  }

  

 

  /* ==========================================================================
    4. SWITCH CONTROL MINI (NÚT BẬT TẮT)
    ========================================================================== */
  .switch-control {
    position: relative;
    display: inline-block;
    width: 36px;
    height: 20px;
  }

  .switch-control input {
    opacity: 0;
    width: 0;
    height: 0;
  }

  .slider-round {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    transition: .3s;
    border-radius: 20px;
  }

  .slider-round:before {
    position: absolute;
    content: "";
    height: 14px;
    width: 14px;
    left: 3px;
    bottom: 3px;
    background-color: white;
    transition: .3s;
    border-radius: 50%;
  }

  input:checked+.slider-round {
    background-color: #f79b66;
  }

  input:checked+.slider-round:before {
    transform: translateX(16px);
  }

  /* ==========================================================================
    5. PHÂN TRANG (PAGINATION)
    ========================================================================== */
  .pagination {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 16px;
    font-size: 14px;
  }

  .page-controls {
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .page-num {
    min-width: 28px;
    height: 28px;
    border: 1px solid #ddd;
    background: white;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 13px;
  }

  .page-num.active {
    background: #f79b66;
    color: white;
    border-color: #f79b66;
  }
  </style>