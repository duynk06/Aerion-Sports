<template>
  <MainLayout>
    <div class="breadcrumb-container">
      <div class="breadcrumb-text">
        <i class="fa-solid fa-chart-line" style="color: #f79b66; margin-right: 6px;"></i>
        <strong>Hệ thống quản trị</strong> / Màn hình thống kê tổng quan doanh nghiệp
      </div>
    </div>

    <div class="statistics-cards-grid-row">
      <div class="stat-custom-card">
        <div class="card-inner-top">
          <div class="card-left-labels">
            <span class="card-time-title">Hôm nay</span>
            <h2 class="card-amount-money">{{ formatTienTe(cardsData.today.doanhThu) }}</h2>
          </div>
          <div class="card-icon-box-orange"><i class="fa-solid fa-calendar-day"></i></div>
        </div>
        <div class="card-middle-summary">
          Sản phẩm đã bán <span class="txt-bold">{{ cardsData.today.soSanPhamDaBan }}</span> • Đơn hàng <span class="txt-bold">{{ cardsData.today.tongDonHang }}</span>
        </div>
        <div class="card-footer-badges-group">
          <div class="badge-item-pastel green">Hoàn thành <span class="num-bold">{{ cardsData.today.donHoanThanh }}</span></div>
          <div class="badge-item-pastel pink">Hủy <span class="num-bold">{{ cardsData.today.donHuy }}</span></div>
          <div class="badge-item-pastel blue">Xử lý <span class="num-bold">{{ cardsData.today.donDangXuLy }}</span></div>
        </div>
      </div>

      <div class="stat-custom-card">
        <div class="card-inner-top">
          <div class="card-left-labels">
            <span class="card-time-title">Tuần này</span>
            <h2 class="card-amount-money">{{ formatTienTe(cardsData.week.doanhThu) }}</h2>
          </div>
          <div class="card-icon-box-orange"><i class="fa-solid fa-calendar-week"></i></div>
        </div>
        <div class="card-middle-summary">
          Sản phẩm đã bán <span class="txt-bold">{{ cardsData.week.soSanPhamDaBan }}</span> • Đơn hàng <span class="txt-bold">{{ cardsData.week.tongDonHang }}</span>
        </div>
        <div class="card-footer-badges-group">
          <div class="badge-item-pastel green">Hoàn thành <span class="num-bold">{{ cardsData.week.donHoanThanh }}</span></div>
          <div class="badge-item-pastel pink">Hủy <span class="num-bold">{{ cardsData.week.donHuy }}</span></div>
          <div class="badge-item-pastel blue">Xử lý <span class="num-bold">{{ cardsData.week.donDangXuLy }}</span></div>
        </div>
      </div>

      <div class="stat-custom-card">
        <div class="card-inner-top">
          <div class="card-left-labels">
            <span class="card-time-title">Tháng này</span>
            <h2 class="card-amount-money">{{ formatTienTe(cardsData.month.doanhThu) }}</h2>
          </div>
          <div class="card-icon-box-orange"><i class="fa-solid fa-calendar-days"></i></div>
        </div>
        <div class="card-middle-summary">
          Sản phẩm đã bán <span class="txt-bold">{{ cardsData.month.soSanPhamDaBan }}</span> • Đơn hàng <span class="txt-bold">{{ cardsData.month.tongDonHang }}</span>
        </div>
        <div class="card-footer-badges-group">
          <div class="badge-item-pastel green">Hoàn thành <span class="num-bold">{{ cardsData.month.donHoanThanh }}</span></div>
          <div class="badge-item-pastel pink">Hủy <span class="num-bold">{{ cardsData.month.donHuy }}</span></div>
          <div class="badge-item-pastel blue">Xử lý <span class="num-bold">{{ cardsData.month.donDangXuLy }}</span></div>
        </div>
      </div>

      <div class="stat-custom-card">
        <div class="card-inner-top">
          <div class="card-left-labels">
            <span class="card-time-title">Năm nay</span>
            <h2 class="card-amount-money">{{ formatTienTe(cardsData.year.doanhThu) }}</h2>
          </div>
          <div class="card-icon-box-orange"><i class="fa-solid fa-chart-pie"></i></div>
        </div>
        <div class="card-middle-summary">
          Sản phẩm đã bán <span class="txt-bold">{{ cardsData.year.soSanPhamDaBan }}</span> • Đơn hàng <span class="txt-bold">{{ cardsData.year.tongDonHang }}</span>
        </div>
        <div class="card-footer-badges-group">
          <div class="badge-item-pastel green">Hoàn thành <span class="num-bold">{{ cardsData.year.donHoanThanh }}</span></div>
          <div class="badge-item-pastel pink">Hủy <span class="num-bold">{{ cardsData.year.donHuy }}</span></div>
          <div class="badge-item-pastel blue">Xử lý <span class="num-bold">{{ cardsData.year.donDangXuLy }}</span></div>
        </div>
      </div>
    </div>

    <div class="chart-main-wrapper-box">
      <div class="chart-header-row">
        <div class="chart-title-left"><i class="fa-solid fa-wallet"></i> Doanh thu cửa hàng</div>
        <div class="chart-filter-right-inputs">

          <template v-if="!isCompareMode">
            <select v-model="selectedPeriodType" @change="loadBieuDoDataFromServer" class="minimal-select">
              <option value="ngay">Theo ngày</option>
            </select>
            <input type="month" v-model="selectedMonthYear" @change="loadBieuDoDataFromServer" class="minimal-date-picker" />
          </template>

          <template v-else>
            <select v-model="compareType" @change="loadBieuDoDataFromServer" class="minimal-select">
              <option value="ngay">So sánh Tháng (Các Ngày)</option>
              <option value="quy">So sánh Quý (Trong Năm)</option>
              <option value="thang">So sánh Năm (Các Tháng)</option>
            </select>

            <div style="display: flex; gap: 4px; align-items: center;">
              <template v-if="compareType === 'ngay'">
                <select v-model="filterCompare.thangGoc" @change="loadBieuDoDataFromServer" class="minimal-select">
                  <option v-for="m in 12" :key="m" :value="m">Tháng {{ m }}</option>
                </select>
                <span style="font-size: 11px; color: #94a3b8;">với</span>
                <select v-model="filterCompare.thangSoSanh" @change="loadBieuDoDataFromServer" class="minimal-select">
                  <option v-for="m in 12" :key="m" :value="m">Tháng {{ m }}</option>
                </select>
              </template>

              <template v-else>
                <select v-model="filterCompare.namGoc" @change="loadBieuDoDataFromServer" class="minimal-select">
                  <option v-for="n in danhSachNam" :key="n" :value="n">Năm {{ n }}</option>
                </select>
                <span style="font-size: 11px; color: #94a3b8;">với</span>
                <select v-model="filterCompare.namSoSanh" @change="loadBieuDoDataFromServer" class="minimal-select">
                  <option v-for="n in danhSachNam" :key="n" :value="n">Năm {{ n }}</option>
                </select>
              </template>
            </div>
          </template>

          <button
            :class="['btn-compare', { 'active-compare-mode': isCompareMode }]"
            @click="toggleCompareMode"
          >
            <i class="fa-solid fa-scale-balanced"></i>
            {{ isCompareMode ? 'Tắt so sánh' : 'So sánh' }}
          </button>
        </div>
      </div>

      <div class="chart-canvas-container">
        <canvas ref="revenueChartRef"></canvas>
      </div>
      <div class="chart-footer-summary">
        <span v-if="!isCompareMode">Tổng doanh thu ghi nhận trong tháng: <strong style="color: #2563eb;">{{ formatTienTe(tongDoanhThuThangComputed) }}</strong></span>
        <span v-else>* Đường màu <b style="color: #1d4ed8;">Xanh dương</b>: Kỳ gốc | Đường màu <b style="color: #f97316;">Cam</b>: Kỳ đối chiếu.</span>
        <span class="unit-text-indicator"><i class="fa-solid fa-circle-info"></i> Đơn vị: VND</span>
      </div>
    </div>

    <div class="date-filter-horizontal-bar">
      <div class="filter-date-item">
        <label>Từ ngày</label>
        <input type="date" v-model="dateFilter.tuNgay" />
      </div>
      <div class="filter-date-item">
        <label>Đến ngày</label>
        <input type="date" v-model="dateFilter.denNgay" />
      </div>
      <div class="filter-buttons-action-flex">
        <button class="btn-submit-filter" @click="load4TablesDataWithFilter"><i class="fa-solid fa-filter"></i> Lọc dữ liệu</button>
        <button class="btn-reset-filter" @click="resetDateFilter">Đặt lại</button>

        <button class="btn-download-excel" @click="handleXuatExcelTaiCho">
          <i class="fa-solid fa-file-excel"></i> Xuất file Excel
        </button>

        <button class="btn-send-mail-excel" @click="handleGuiEmailThuCong" :disabled="isSendingMail">
          <i class="fa-solid fa-paper-plane"></i>
          {{ isSendingMail ? 'Đang gửi...' : 'Gửi báo cáo về Mail' }}
        </button>
      </div>
    </div>

    <div class="dashboard-tables-layout-grid">
      <div class="dashboard-table-box">
        <div class="box-header-row font-cam">
          <i class="fa-solid fa-fire"></i> Top sản phẩm bán chạy
          <span class="header-tag-count">Top 10</span>
        </div>
        <div class="box-body-content">
          <table class="minimal-dashboard-table">
            <thead>
              <tr>
                <th style="width: 50px; text-align: center;">STT</th>
                <th>Sản phẩm</th>
                <th style="text-align: center; width: 90px;">Đã bán</th>
                <th style="text-align: center; width: 90px;">Tồn</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, idx) in tableData.topBanChay" :key="idx">
                <td style="text-align: center; color: #94a3b8;">#{{ idx + 1 }}</td>
                <td style="font-weight: 500; text-align: left;">{{ item.tenSanPham }}</td>
                <td style="text-align: center; font-weight: bold; color: #16a34a;">
                  <span class="cell-count-badge green">{{ item.daBan }}</span>
                </td>
                <td style="text-align: center; color: #64748b;">{{ item.tonKho }}</td>
              </tr>
              <tr v-if="tableData.topBanChay.length === 0">
                <td colspan="4" class="empty-text">Chưa ghi nhận số liệu bán hàng trong khoảng ngày chọn.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="dashboard-table-box">
        <div class="box-header-row font-cam">
          <i class="fa-solid fa-list-check"></i> Trạng thái & Tiến độ đơn hàng
          <span class="header-tag-count gray">Tổng: {{ tongDonHangHienTaiComputed }} đơn</span>
        </div>
        <div class="box-body-content">
          <div class="completion-rate-wrapper">
            <div class="completion-rate-text-row">
              <span>Tỷ lệ hoàn thành</span>
              <strong>{{ tyLeHoanThanhComputed }}%</strong>
            </div>
            <div class="master-progress-track">
              <div class="master-progress-fill" :style="{ width: tyLeHoanThanhComputed + '%' }"></div>
            </div>
          </div>

          <div class="order-status-progress-list">
            <div v-for="(status, idx) in tableData.trangThaiDonHang" :key="idx" class="status-progress-item">
              <div class="status-meta-labels">
                <span class="status-name-text"><i class="fa-regular fa-circle-dot dot-orange"></i> {{ status.tenTrangThai }}</span>
                <span class="status-count-number">{{ status.soLuong }}</span>
              </div>
              <div class="progress-bar-track">
                <div class="progress-fill-active" :style="{ width: tinhPhanTramTienDoRow(status.soLuong) + '%' }"></div>
              </div>
            </div>
            <div v-if="tableData.trangThaiDonHang.length === 0" class="empty-text">Chưa phát sinh giao dịch đơn hàng nào.</div>
          </div>
        </div>
      </div>

      <div class="dashboard-table-box">
        <div class="box-header-row font-cam">
          <i class="fa-solid fa-user-star"></i> Khách hàng tiềm năng chi tiêu lớn
        </div>
        <div class="box-body-content">
          <table class="minimal-dashboard-table">
            <thead>
              <tr>
                <th>Khách hàng / SDT</th>
                <th style="text-align: center; width: 80px;">Số đơn</th>
                <th style="text-align: right; width: 140px;">Tổng chi tiêu</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(kh, idx) in tableData.khachHangTiemNang" :key="idx">
                <td style="text-align: left;">
                  <div style="font-weight: 600; color: #1e293b;">{{ kh.hoTen }}</div>
                  <small style="color: #94a3b8; font-size: 11px;">{{ kh.sdt }}</small>
                </td>
                <td style="text-align: center; font-weight: bold; color: #f79b66;">{{ kh.soDon }}</td>
                <td style="text-align: right; font-weight: 600; color: #2563eb;">{{ formatPriceThuan(kh.tongChiTieu) }} đ</td>
              </tr>
              <tr v-if="tableData.khachHangTiemNang.length === 0">
                <td colspan="3" class="empty-text">Hệ thống chưa có dữ liệu khách mua.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="dashboard-table-box">
        <div class="box-header-row font-cam">
          <i class="fa-solid fa-boxes-stacked"></i> Sản phẩm bán chậm & Tồn kho nhiều
          <span class="header-tag-count danger">Chưa bán được</span>
        </div>
        <div class="box-body-content">
          <table class="minimal-dashboard-table">
            <thead>
              <tr>
                <th>Sản phẩm</th>
                <th style="text-align: center; width: 80px;">Đã bán</th>
                <th style="text-align: center; width: 90px;">Tồn kho</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(tk, idx) in tableData.sanPhamTonKho" :key="idx">
                <td style="font-weight: 500; text-align: left; color: #334155;">{{ tk.tenSanPham }}</td>
                <td style="text-align: center; color: #94a3b8;">{{ tk.daBan }}</td>
                <td style="text-align: center; font-weight: bold; color: #ef4444;">
                  <span class="cell-count-badge red">{{ tk.tonKho }}</span>
                </td>
              </tr>
              <tr v-if="tableData.sanPhamTonKho.length === 0">
                <td colspan="3" class="empty-text">Không có hàng tồn kho cảnh báo.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import myAxios from '../api/axios';
import MainLayout from '../layouts/MainLayout.vue';
import { Chart, registerables } from 'chart.js';
import * as XLSX from 'xlsx';

Chart.register(...registerables);

// 🌟 ĐÃ SỬA: Không còn hardcode ngày/tháng/năm, luôn lấy theo thời điểm hiện tại của hệ thống
const getNgayDauThangMoi = () => {
  const now = new Date();
  const nam = now.getFullYear();
  const thang = String(now.getMonth() + 1).padStart(2, '0');
  return `${nam}-${thang}-01`;
};

const getNgayCuoiThangMoi = () => {
  const now = new Date();
  const nam = now.getFullYear();
  const thang = now.getMonth() + 1;
  // Ngày 0 của tháng sau = ngày cuối cùng của tháng hiện tại (tự đúng cho tháng 28/29/30/31 ngày)
  const ngayCuoi = new Date(nam, thang, 0).getDate();
  return `${nam}-${String(thang).padStart(2, '0')}-${String(ngayCuoi).padStart(2, '0')}`;
};

const getThangNamMoi = () => {
  const now = new Date();
  const nam = now.getFullYear();
  const thang = String(now.getMonth() + 1).padStart(2, '0');
  return `${nam}-${thang}`;
};

// 🌟 BIẾN ĐIỀU KHIỂN CÔNG TẮC BẬT/TẮT CHẾ ĐỘ SO SÁNH
const isCompareMode = ref(false);

const selectedPeriodType = ref('ngay');
const compareType = ref('ngay'); // Phân loại so sánh: ngay, quy, thang
const selectedMonthYear = ref(getThangNamMoi());
const revenueChartRef = ref(null);
let lineChartInstance = null;
const isSendingMail = ref(false);

// 🌟 ĐÃ SỬA: Tính mặc định so sánh động theo thời gian thực (tháng hiện tại vs tháng trước, năm hiện tại vs năm trước)
const namHienTai = new Date().getFullYear();
const thangHienTai = new Date().getMonth() + 1; // getMonth() trả 0-11 nên +1

// Danh sách 5 năm gần nhất tính từ năm hiện tại, dùng để render dropdown chọn năm động trong template
const danhSachNam = Array.from({ length: 5 }, (_, i) => namHienTai - i);

// Xử lý luôn trường hợp tháng hiện tại là tháng 1 -> tháng trước phải lùi về tháng 12 năm trước
const thangTruoc = thangHienTai === 1 ? 12 : thangHienTai - 1;

const filterCompare = ref({
  thangGoc: thangHienTai,
  thangSoSanh: thangTruoc,
  namGoc: namHienTai,
  namSoSanh: namHienTai - 1
});

const dateFilter = ref({ tuNgay: getNgayDauThangMoi(), denNgay: getNgayCuoiThangMoi() });

const cardsData = ref({
  today: { doanhThu: 0, soSanPhamDaBan: 0, tongDonHang: 0, donHoanThanh: 0, donHuy: 0, donDangXuLy: 0 },
  week: { doanhThu: 0, soSanPhamDaBan: 0, tongDonHang: 0, donHoanThanh: 0, donHuy: 0, donDangXuLy: 0 },
  month: { doanhThu: 0, soSanPhamDaBan: 0, tongDonHang: 0, donHoanThanh: 0, donHuy: 0, donDangXuLy: 0 },
  year: { doanhThu: 0, soSanPhamDaBan: 0, tongDonHang: 0, donHoanThanh: 0, donHuy: 0, donDangXuLy: 0 }
});

const tableData = ref({ topBanChay: [], trangThaiDonHang: [], khachHangTiemNang: [], sanPhamTonKho: [] });

// Data đơn lẻ phục vụ biểu đồ thường
const mapDoanhThuBieuDoFromServer = ref({});
// Data phức hợp phục vụ biểu đồ so sánh đối chiếu
const bieuDoCompositeData = ref({ gocLabel: '', gocData: {}, ssLabel: '', ssData: {} });

const formatPriceThuan = (value) => new Intl.NumberFormat('vi-VN').format(value || 0);
const formatTienTe = (value) => new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value || 0);

const tongDoanhThuThangComputed = computed(() => {
  return Object.values(mapDoanhThuBieuDoFromServer.value).reduce((sum, val) => sum + Number(val), 0);
});

const tongDonHangHienTaiComputed = computed(() => {
  return tableData.value.trangThaiDonHang.reduce((sum, item) => sum + item.soLuong, 0);
});

const tyLeHoanThanhComputed = computed(() => {
  const tong = tongDonHangHienTaiComputed.value;
  if (tong === 0) return 0;
  const hoanThanhObj = tableData.value.trangThaiDonHang.find(i => i.tenTrangThai === 'Hoàn thành');
  const soLuongHoanThanh = hoanThanhObj ? hoanThanhObj.soLuong : 0;
  return Math.round((soLuongHoanThanh / tong) * 100);
});

const tinhPhanTramTienDoRow = (soLuong) => {
  const total = tongDonHangHienTaiComputed.value;
  return total > 0 ? Math.round((soLuong / total) * 100) : 0;
};

// 🌟 HÀM XỬ LÝ BẬT/TẮT CHẾ ĐỘ SO SÁNH
const toggleCompareMode = () => {
  isCompareMode.value = !isCompareMode.value;
  loadBieuDoDataFromServer(); // Tải lại data tương ứng với chế độ mới
};

// 🌟 HÀM RENDER ĐỒ THỊ THÔNG MINH (TỰ ĐỘNG VẼ 1 ĐƯỜNG HOẶC 2 ĐƯỜNG)
const renderWaveLineChartGraphic = () => {
  if (lineChartInstance) lineChartInstance.destroy();
  const ctx = revenueChartRef.value.getContext('2d');

  const gradientFill = ctx.createLinearGradient(0, 0, 0, 260);
  gradientFill.addColorStop(0, 'rgba(37, 99, 235, 0.22)');
  gradientFill.addColorStop(1, 'rgba(255, 255, 255, 0.01)');

  // 1. Xác định trục X dựa theo chế độ
  let labelsX = [];
  const currentType = isCompareMode.value ? compareType.value : selectedPeriodType.value;

  if (currentType === 'ngay') {
    labelsX = Array.from({ length: 30 }, (_, i) => i + 1);
  } else if (currentType === 'quy') {
    labelsX = [1, 2, 3, 4];
  } else if (currentType === 'thang') {
    labelsX = Array.from({ length: 12 }, (_, i) => i + 1);
  }

  // 2. Tạo datasets dựa vào việc có đang bật nút so sánh hay không
  let datasetsConfig = [];

  if (!isCompareMode.value) {
    // Biểu đồ đơn (1 đường màu xanh mặc định)
    const dataSingle = labelsX.map(day => Number(mapDoanhThuBieuDoFromServer.value[day] || 0));
    datasetsConfig.push({
      label: 'Doanh thu',
      data: dataSingle,
      borderColor: '#1d4ed8',
      borderWidth: 2,
      backgroundColor: gradientFill,
      fill: true,
      tension: 0.4,
      pointRadius: (context) => dataSingle[context.dataIndex] > 0 ? 5 : 0,
      pointBackgroundColor: '#1d4ed8',
      pointBorderColor: '#fff',
      pointBorderWidth: 1.5
    });
  } else {
    // Biểu đồ so sánh đối chiếu (2 đường Xanh và Cam chạy song song)
    const dataGoc = labelsX.map(k => Number(bieuDoCompositeData.value.gocData[k] || 0));
    const dataSoSanh = labelsX.map(k => Number(bieuDoCompositeData.value.ssData[k] || 0));

    datasetsConfig.push(
      {
        label: bieuDoCompositeData.value.gocLabel || 'Kỳ gốc',
        data: dataGoc,
        borderColor: '#1d4ed8',
        borderWidth: 2.5,
        backgroundColor: 'rgba(37, 99, 235, 0.02)',
        fill: true,
        tension: 0.38
      },
      {
        label: bieuDoCompositeData.value.ssLabel || 'Kỳ đối chiếu',
        data: dataSoSanh,
        borderColor: '#f97316',
        borderWidth: 2.5,
        backgroundColor: 'rgba(249, 115, 22, 0.02)',
        fill: true,
        tension: 0.38
      }
    );
  }

  lineChartInstance = new Chart(ctx, {
    type: 'line',
    data: {
      labels: labelsX.map(lbl => {
        if (currentType === 'quy') return 'Quý ' + lbl;
        if (currentType === 'thang') return 'Tháng ' + lbl;
        return lbl;
      }),
      datasets: datasetsConfig
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: { legend: { display: isCompareMode.value } }, // Hiện chú thích khi so sánh
      scales: {
        y: {
          min: 0,
          suggestedMax: 25000000,
          ticks: { callback: (val) => val === 0 ? '0' : (val / 1000000) + '.0M' },
          grid: { color: '#f1f5f9' }
        },
        x: { grid: { display: false } }
      }
    }
  });
};

const isLoadingChart = ref(false); // Biến chặn loop

const loadBieuDoDataFromServer = async () => {
  if (isLoadingChart.value) return; // Nếu đang tải thì không chạy tiếp
  isLoadingChart.value = true;

  try {
    if (!isCompareMode.value) {
      const [nam, thang] = selectedMonthYear.value.split('-');
      const res = await myAxios.get('/api/thong-ke/bieu-do-line', {
        params: { loai: 'ngay', thang: parseInt(thang, 10), nam: parseInt(nam, 10), isCompare: false }
      });
      mapDoanhThuBieuDoFromServer.value = res.data;
    } else {
      // Bắt buộc thêm isCompare: true để backend trả dữ liệu so sánh
      const res = await myAxios.get('/api/thong-ke/bieu-do-line', {
        params: {
          loai: compareType.value,
          isCompare: true,
          thangGoc: filterCompare.value.thangGoc,
          thangSoSanh: filterCompare.value.thangSoSanh,
          namGoc: filterCompare.value.namGoc,
          namSoSanh: filterCompare.value.namSoSanh
        }
      });
      bieuDoCompositeData.value = res.data;
    }
    renderWaveLineChartGraphic();
  } catch (e) {
    console.error(e);
  } finally {
    // Trì hoãn mở khóa một chút để DOM ổn định hẳn
    setTimeout(() => {
      isLoadingChart.value = false;
    }, 200);
  }
};

const handleXuatExcelTaiCho = () => {
  if (tableData.value.topBanChay.length === 0) {
    alert("Không có dữ liệu trong khoảng ngày chọn để xuất!");
    return;
  }
  const flattenData = [];
  flattenData.push(["TOP SẢN PHẨM BÁN CHẠY"]);
  flattenData.push(["STT", "Tên Sản Phẩm", "Số Lượng Đã Bán", "Còn Tồn Kho"]);
  tableData.value.topBanChay.forEach((item, index) => {
    flattenData.push([index + 1, item.tenSanPham, item.daBan, item.tonKho]);
  });
  flattenData.push([]);
  flattenData.push(["TRẠNG THÁI & TIẾN ĐỘ ĐƠN HÀNG"]);
  flattenData.push(["Tên Trạng Thái", "Số Lượng Đơn"]);
  tableData.value.trangThaiDonHang.forEach(item => {
    flattenData.push([item.tenTrangThai, item.soLuong]);
  });

  const worksheet = XLSX.utils.aoa_to_sheet(flattenData);
  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, "Báo Cáo Doanh Thu");
  XLSX.writeFile(workbook, `BaoCao_AerionSports_${dateFilter.value.tuNgay}_to_${dateFilter.value.denNgay}.xlsx`);
};

const handleGuiEmailThuCong = async () => {
  isSendingMail.value = true;
  try {
    const res = await myAxios.post('/api/thong-ke/gui-email-thu-cong');
    if (res.data.status === 'success') {
      alert("Thành công! Hệ thống đã gửi đính kèm file báo cáo Excel về hòm thư Gmail của bạn.");
    }
  } catch (error) {
    console.error(error);
    alert("Thất bại, kiểm tra lại luồng mạng hoặc cấu hình SMTP!");
  } finally {
    isSendingMail.value = false;
  }
};

const loadAllCardsData = async () => {
  try {
    const res = await myAxios.get('/api/thong-ke/dashboard-cards');
    cardsData.value = res.data;
  } catch (e) { console.error(e); }
};

const load4TablesDataWithFilter = async () => {
  try {
    const res = await myAxios.get('/api/thong-ke/chi-tiet-tables', {
      params: { tuNgay: dateFilter.value.tuNgay || null, denNgay: dateFilter.value.denNgay || null }
    });
    tableData.value = res.data;
  } catch (e) { console.error(e); }
};

const resetDateFilter = () => {
  dateFilter.value = { tuNgay: getNgayDauThangMoi(), denNgay: getNgayCuoiThangMoi() };
  load4TablesDataWithFilter();
};

onMounted(() => {
  loadAllCardsData();
  load4TablesDataWithFilter();
  loadBieuDoDataFromServer();
});
</script>

<style scoped>
.breadcrumb-container { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.breadcrumb-text { font-size: 14px; color: #475569; }

/* 2. CSS Ô THÈ THỐNG KÊ */
.statistics-cards-grid-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 20px; }
.stat-custom-card { background: #ffffff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 16px; display: flex; flex-direction: column; }
.stat-custom-card:hover { transform: translateY(-1px); box-shadow: 0 4px 6px rgba(0,0,0,0.02); }
.card-inner-top { display: flex; justify-content: space-between; align-items: flex-start; }
.card-left-labels { text-align: left; }
.card-time-title { font-size: 13px; font-weight: 500; color: #64748b; }
.card-amount-money { font-size: 18px; font-weight: 700; color: #0f172a; margin: 4px 0; }
.card-icon-box-orange { width: 34px; height: 34px; border-radius: 8px; background-color: #fff7ed; border: 1px solid #ffedd5; color: #f79b66; display: flex; align-items: center; justify-content: center; font-size: 14px; }
.card-middle-summary { font-size: 11.5px; color: #475569; text-align: left; margin-bottom: 10px; }
.txt-bold { font-weight: 700; color: #1e293b; }
.card-footer-badges-group { display: flex; gap: 4px; width: 100%; }
.badge-item-pastel { flex: 1; font-size: 10px; padding: 4px 1px; border-radius: 6px; font-weight: 500; text-align: center; }
.badge-item-pastel.green { background-color: #e6f4ea; color: #137333; }
.badge-item-pastel.pink { background-color: #fce8e6; color: #c5221f; }
.badge-item-pastel.blue { background-color: #e8f0fe; color: #1a73e8; }
.num-bold { font-weight: 700; display: inline-block; margin-left: 1px; }

/* 3. CSS BIỂU ĐỒ */
.chart-main-wrapper-box { background: #ffffff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 18px; margin-bottom: 20px; }
.chart-header-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 8px; border-bottom: 1px solid #f1f5f9; }
.chart-title-left { font-size: 14px; font-weight: 700; color: #1e293b; }
.chart-filter-right-inputs { display: flex; gap: 8px; align-items: center; }
.minimal-select, .minimal-date-picker { padding: 5px 10px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 12px; outline: none; background: #fff; height: 30px; }
.btn-compare { background: #f8fafc; border: 1px solid #cbd5e1; color: #475569; padding: 0 12px; border-radius: 6px; font-size: 12px; font-weight: 500; cursor: pointer; height: 30px; display: flex; align-items: center; gap: 4px; }

/* Style nổi bật khi kích hoạt nút so sánh */
.active-compare-mode {
  background-color: #f79b66 !important;
  color: white !important;
  border-color: #ea712b !important;
}

.chart-canvas-container { height: 260px; position: relative; width: 100%; }
.chart-footer-summary { text-align: left; font-size: 12.5px; color: #64748b; margin-top: 12px; padding-top: 8px; border-top: 1px dashed #e2e8f0; display: flex; justify-content: space-between; }
.unit-text-indicator { font-size: 11.5px; color: #94a3b8; }

/* 4. CSS BỘ LỌC NGÀY DƯỚI */
.date-filter-horizontal-bar { background-color: #fff; border: 1px solid #fed7aa; border-radius: 10px; padding: 12px 20px; margin-bottom: 20px; display: flex; align-items: flex-end; gap: 20px; }
.filter-date-item { display: flex; flex-direction: column; gap: 6px; text-align: left; }
.filter-date-item label { font-size: 12px; font-weight: 600; color: #475569; }
.filter-date-item input { padding: 6px 12px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 12.5px; outline: none; width: 170px; height: 32px; }
.filter-buttons-action-flex { display: flex; gap: 8px; }
.btn-submit-filter { background-color: #2563eb; color: white; border: none; padding: 0 16px; height: 32px; font-size: 12.5px; font-weight: 600; border-radius: 6px; cursor: pointer; }
.btn-submit-filter:hover { background-color: #1d4ed8; }
.btn-reset-filter { background-color: #f3f4f6; color: #4b5563; border: 1px solid #cbd5e1; padding: 0 16px; height: 32px; font-size: 12.5px; font-weight: 600; border-radius: 6px; cursor: pointer; }

.btn-download-excel { background-color: #2563eb; color: white; border: none; padding: 0 16px; height: 32px; font-size: 12.5px; font-weight: 600; border-radius: 6px; cursor: pointer; display: flex; align-items: center; gap: 6px; }
.btn-send-mail-excel { background-color: #10b981; color: white; border: none; padding: 0 16px; height: 32px; font-size: 12.5px; font-weight: 600; border-radius: 6px; cursor: pointer; display: flex; align-items: center; gap: 6px; }
.btn-send-mail-excel:disabled { background-color: #9ca3af; cursor: not-allowed; }

/* 5. KHỐI BẢNG DƯỚI ĐÁY */
.dashboard-tables-layout-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }
.dashboard-table-box { background: #ffffff; border: 1px solid #edf2f7; border-radius: 12px; display: flex; flex-direction: column; overflow: hidden; }
.box-header-row { background-color: #fff; border-bottom: 1px solid #f1f5f9; padding: 12px 16px; font-size: 13.5px; font-weight: 700; text-align: left; display: flex; align-items: center; gap: 6px; }
.box-header-row.font-cam { color: #f79b66; }
.header-tag-count { background-color: #ffedd5; color: #f79b66; font-size: 10.5px; padding: 1px 6px; border-radius: 4px; margin-left: auto; }
.header-tag-count.gray { background-color: #f1f5f9; color: #475569; }
.header-tag-count.danger { background-color: #fee2e2; color: #ef4444; }

.box-body-content { padding: 12px; max-height: 350px; overflow-y: auto; }
.minimal-dashboard-table { width: 100%; border-collapse: collapse; font-size: 12.5px; }
.minimal-dashboard-table th { color: #64748b; font-weight: 600; padding: 8px; border-bottom: 1px solid #edf2f7; background-color: #fafafa; }
.minimal-dashboard-table td { padding: 10px 8px; border-bottom: 1px solid #f1f5f9; vertical-align: middle; }
.empty-text { padding: 30px; color: #94a3b8; font-style: italic; font-size: 12.5px; text-align: center; }

.completion-rate-wrapper { background: #f8fafc; border: 1px dashed #cbd5e1; padding: 10px 14px; border-radius: 8px; margin-bottom: 15px; text-align: left; }
.completion-rate-text-row { display: flex; justify-content: space-between; font-size: 12.5px; font-weight: 600; color: #475569; margin-bottom: 6px; }
.master-progress-track { width: 100%; height: 10px; background-color: #e2e8f0; border-radius: 999px; overflow: hidden; }
.master-progress-fill { height: 100%; background-color: #3b82f6; border-radius: 999px; }

.order-status-progress-list { display: flex; flex-direction: column; gap: 14px; padding: 2px 0; }
.status-progress-item { display: flex; flex-direction: column; gap: 5px; }
.status-meta-labels { display: flex; justify-content: space-between; font-size: 12.5px; font-weight: 500; }
.status-name-text { color: #475569; }
.dot-orange { color: #f79b66; font-size: 10px; margin-right: 2px; }
.status-count-number { font-weight: bold; color: #0f172a; }
.progress-bar-track { width: 100%; height: 7px; background-color: #f1f5f9; border-radius: 999px; overflow: hidden; }
.progress-fill-active { height: 100%; background-color: #f79b66; border-radius: 999px; }

.cell-count-badge { padding: 2px 8px; border-radius: 20px; font-weight: bold; font-size: 11.5px; }
.cell-count-badge.green { background-color: #e6f4ea; color: #137333; }
.cell-count-badge.red { background-color: #fce8e6; color: #c5221f; }
</style>