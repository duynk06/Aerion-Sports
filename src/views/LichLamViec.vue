<template>
    <MainLayout title="Lịch làm việc">
        <div class="calendar-dashboard">

            <!-- 1. THANH BỘ LỌC & ACTION TOP BAR -->
            <div class="filter-card">
                <div class="filter-left">
                    <div class="filter-item">
                        <label>Nhân viên</label>
                        <input type="text" v-model="filter.tenNhanVien" @input="filterLich" placeholder="Tìm kiếm nhân viên..." />
                    </div>
                    <div class="filter-item">
                        <label>Ca làm</label>
                        <select v-model="filter.maCa" @change="filterLich">
                            <option value="ALL">Tất cả ca</option>
                            <option value="CA_SANG">Ca sáng</option>
                            <option value="CA_CHIEU">Ca chiều</option>
                        </select>
                    </div>
                    <div class="filter-item">
                        <label>Ngày làm</label>
                        <input type="date" v-model="filter.ngay" @change="onDateFilterChange" />
                    </div>
                </div>
                <div class="filter-right">
                    <button class="btn btn-dark"><i class="fa-solid fa-file-import"></i> Import Excel</button>
                    <button class="btn btn-primary" @click="openAddModal"><i class="fa-solid fa-plus"></i> Thêm mới lịch làm việc</button>
                </div>
            </div>

            <!-- 2. KHU VỰC HIỂN THỊ LỊCH CHI TIẾT -->
            <div class="calendar-container">
                <div class="calendar-header">
                    <div class="calendar-navigation">
                        <button @click="prevPeriod" class="nav-btn"><i class="fa-solid fa-chevron-left"></i></button>
                        <span class="current-period-text">{{ viewTitle }}</span>
                        <button @click="nextPeriod" class="nav-btn"><i class="fa-solid fa-chevron-right"></i></button>
                        <button @click="goToday" class="btn-today">Hôm nay</button>
                    </div>

                    <div class="view-switcher">
                        <button :class="{ active: currentView === 'day' }" @click="switchView('day')">Ngày</button>
                        <button :class="{ active: currentView === 'month' }" @click="switchView('month')">Tháng</button>
                    </div>
                </div>

                <!-- CHẾ ĐỘ XEM THEO NGÀY -->
                <div v-if="currentView === 'day'" class="day-view-grid">
                    <div class="grid-header-col">CA LÀM VIỆC</div>
                    <div class="grid-header-col text-center">
                        {{ formatDayOfWeek(selectedDate) }} <br> <span class="date-sub">{{ formatShortDate(selectedDate) }}</span>
                    </div>

                    <!-- Hàng Ca Sáng -->
                    <div class="ca-info-cell ca-sang-text">
                        <strong>Ca sáng</strong>
                        <span>08:00 - 12:00</span>
                    </div>
                    <div class="ca-content-cell ca-sang-bg">
                        <div v-if="lichCaSangHienTai.length > 0" class="shift-card">
                            <span class="shift-title">Ca sáng</span>
                            <span class="shift-time">08:00 - 12:00</span>
                            <span class="shift-count">{{ lichCaSangHienTai.length }} Nhân viên</span>
                            <button class="btn-view-more" @click="openModalDetail(filter.ngay, 'CA_SANG', lichCaSangHienTai)">Xem thêm</button>
                        </div>
                        <div v-else class="no-shift-text">Chưa xếp lịch ca sáng</div>
                    </div>

                    <!-- Hàng Ca Chiều -->
                    <div class="ca-info-cell ca-chieu-text">
                        <strong>Ca chiều</strong>
                        <span>12:00 - 17:00</span>
                    </div>
                    <div class="ca-content-cell ca-chieu-bg">
                        <div v-if="lichCaChieuHienTai.length > 0" class="shift-card">
                            <span class="shift-title">Ca chiều</span>
                            <span class="shift-time">12:00 - 17:00</span>
                            <span class="shift-count">{{ lichCaChieuHienTai.length }} Nhân viên</span>
                            <button class="btn-view-more" @click="openModalDetail(filter.ngay, 'CA_CHIEU', lichCaChieuHienTai)">Xem thêm</button>
                        </div>
                        <div v-else class="no-shift-text">Chưa xếp lịch ca chiều</div>
                    </div>
                </div>

                <!-- CHẾ ĐỘ XEM THEO THÁNG -->
                <div v-if="currentView === 'month'" class="month-view-grid">
                    <div v-for="dayName in ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN']" :key="dayName" class="month-grid-header">
                        {{ dayName }}
                    </div>
                    
                    <div v-for="cell in daysInMonthCells" :key="cell.dateStr" class="month-day-cell" 
                        :class="{ 'is-today': cell.isToday, 'disabled-cell': !cell.isCurrentMonth }">
                        <span class="day-number">{{ cell.day }}</span>
                        
                        <div v-if="cell.shifts.length > 0" class="avatar-dots-row" @click="openModalMonthDetail(cell)">
                            <span v-for="(nv, idx) in cell.shifts.slice(0, 3)" :key="idx" 
                                class="dot-avatar" :class="nv.caLamViec?.maCa === 'CA_SANG' ? 'bg-green' : 'bg-orange'">
                                {{ getShortName(nv.tenNhanVien) }}
                            </span>
                            <span v-if="cell.shifts.length > 3" class="dot-more">+{{ cell.shifts.length - 3 }}</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 3. MODAL POPUP CHI TIẾT NHÂN VIÊN XEM LỊCH -->
            <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
                <div class="modal-box">
                    <div class="modal-header">
                        <div>
                          <h3>Chi tiết lịch làm ngày {{ modalData.dateDisplay }}</h3>
                          <span class="modal-subtitle">Dữ liệu chấm công thời gian thực</span>
                        </div>
                        <button class="close-modal-btn" @click="showModal = false">&times;</button>
                    </div>

                    <div class="modal-body">
                        <div v-if="modalData.caSangList.length > 0" class="modal-shift-section border-green">
                            <div class="shift-section-header text-green">
                                <span>🟢 Ca sáng <small>08:00 - 12:00</small></span>
                                <span class="count-nv">{{ modalData.caSangList.length }} nhân viên</span>
                            </div>
                            <div v-for="nv in modalData.caSangList" :key="nv.id" class="nv-list-item">
                                <div class="nv-avatar-mock text-green">{{ getShortName(nv.tenNhanVien) }}</div>
                                <div class="nv-info">
                                    <h4>{{ nv.tenNhanVien }}</h4>
                                    <span>{{ nv.maNhanVien || 'N/A' }}</span>
                                </div>
                            </div>
                        </div>

                        <div v-if="modalData.caChieuList.length > 0" class="modal-shift-section border-orange">
                            <div class="shift-section-header text-orange">
                                <span>🟠 Ca chiều <small>12:00 - 17:00</small></span>
                                <span class="count-nv">{{ modalData.caChieuList.length }} nhân viên</span>
                            </div>
                            <div v-for="nv in modalData.caChieuList" :key="nv.id" class="nv-list-item">
                                <div class="nv-avatar-mock text-orange">{{ getShortName(nv.tenNhanVien) }}</div>
                                <div class="nv-info">
                                    <h4>{{ nv.tenNhanVien }}</h4>
                                    <span>{{ nv.maNhanVien || 'N/A' }}</span>
                                </div>
                            </div>
                        </div>
                        
                        <div v-if="modalData.caSangList.length === 0 && modalData.caChieuList.length === 0" style="text-align: center; color: #94a3b8; padding: 20px;">
                            Không có nhân viên nào được xếp lịch trực.
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button class="btn-close-modal" @click="showModal = false">Đóng</button>
                    </div>
                </div>
            </div>

            <!-- 4. POPUP THÊM MỚI LỊCH LÀM VIỆC -->
            <div v-if="showAddModal" class="modal-overlay" @click.self="showAddModal = false">
                <div class="modal-box" style="max-width: 500px;">
                    <div class="modal-header">
                        <div>
                            <h3>Thêm mới lịch làm việc</h3>
                            <span class="modal-subtitle">Xếp lịch trực tiếp cho nhân viên cửa hàng</span>
                        </div>
                        <button class="close-modal-btn" @click="showAddModal = false">&times;</button>
                    </div>

                    <div class="modal-body" style="gap: 15px; text-align: left;">
                        <div style="display: flex; flex-direction: column; gap: 6px;">
                            <label style="font-size: 13px; font-weight: 600; color: #4a5568;">Nhân viên *</label>
                            <select v-model="formXepLich.idNhanVien" style="height: 40px; padding: 0 14px; border: 1px solid #cbd5e1; border-radius: 8px; outline: none;">
                                <option :value="null">-- Chọn nhân viên --</option>
                                <option v-for="nv in danhSachNhanVienAll" :key="nv.id" :value="nv.id">
                                    [{{ nv.maNv || nv.ma_nv }}] - {{ nv.tenNv || nv.ten_nv }}
                                </option>
                            </select>
                        </div>

                        <div style="display: flex; flex-direction: column; gap: 6px;">
                            <label style="font-size: 13px; font-weight: 600; color: #4a5568;">Ca làm việc *</label>
                            <select v-model="formXepLich.caLamViec.id" style="height: 40px; padding: 0 14px; border: 1px solid #cbd5e1; border-radius: 8px; outline: none;">
                                <option :value="null">-- Chọn ca làm việc --</option>
                                <option v-for="ca in danhSachCaAll" :key="ca.id" :value="ca.id">
                                    {{ ca.tenCa }}
                                </option>
                            </select>
                        </div>

                        <div style="display: flex; flex-direction: column; gap: 6px;">
                            <label style="font-size: 13px; font-weight: 600; color: #4a5568;">Ngày làm việc *</label>
                            <input type="date" v-model="formXepLich.ngayLamViec" style="height: 40px; padding: 0 14px; border: 1px solid #cbd5e1; border-radius: 8px; outline: none;" />
                        </div>
                    </div>

                    <div class="modal-footer" style="gap: 10px;">
                        <button @click="showAddModal = false" style="flex: 1; height: 40px; border: none; background: #e2e8f0; border-radius: 8px; font-weight: 600; cursor: pointer;">Hủy</button>
                        <button @click="handleSaveNewLich" style="flex: 1; height: 40px; border: none; background: #da123a; color: white; border-radius: 8px; font-weight: 600; cursor: pointer;">Lưu lại</button>
                    </div>
                </div>
            </div>

        </div>
    </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import MainLayout from '@/layouts/MainLayout.vue'
import axios from 'axios'

const currentView = ref('day') 
const showModal = ref(false)
const showAddModal = ref(false) 
const selectedDate = ref(new Date())

// Bộ lọc ngày mặc định lấy hôm nay
const filter = ref({ 
    tenNhanVien: '', 
    maCa: 'ALL', 
    ngay: new Date().toISOString().split('T')[0] 
})

const totalLichData = ref([]) 
const modalData = ref({ dateDisplay: '', caSangList: [], caChieuList: [] })
const formXepLich = ref({
    idNhanVien: null,
    caLamViec: { id: null },
    ngayLamViec: new Date().toISOString().split('T')[0]
})

const danhSachNhanVienAll = ref([])
const danhSachCaAll = ref([
    { id: 1, maCa: 'CA_SANG', tenCa: 'Ca Sáng' },
    { id: 2, maCa: 'CA_CHIEU', tenCa: 'Ca Chiều' }
])

// Tiêu đề hiển thị động theo tháng/ngày
const viewTitle = computed(() => {
    const months = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12']
    const year = selectedDate.value.getFullYear()
    if (currentView.value === 'day') {
        return `${selectedDate.value.getDate()} ${months[selectedDate.value.getMonth()]} năm ${year}`
    }
    return `${months[selectedDate.value.getMonth()]} Năm ${year}`
})

const processedLichList = computed(() => {
    return totalLichData.value.filter(item => {
        const matchName = !filter.value.tenNhanVien || item.tenNhanVien.toLowerCase().includes(filter.value.tenNhanVien.toLowerCase());
        const matchCa = filter.value.maCa === 'ALL' || item.caLamViec?.maCa === filter.value.maCa;
        return matchName && matchCa;
    })
})

const lichCaSangHienTai = computed(() => {
    return processedLichList.value.filter(item => item.caLamViec?.maCa === 'CA_SANG')
})

const lichCaChieuHienTai = computed(() => {
    return processedLichList.value.filter(item => item.caLamViec?.maCa === 'CA_CHIEU')
})
// 🌟 SỬA LẠI ĐOẠN ĐANG BỊ LỆCH MÚI GIỜ:
const daysInMonthCells = computed(() => {
    const cells = []
    const year = selectedDate.value.getFullYear()
    const month = selectedDate.value.getMonth()
    
    const firstDayOfMonth = new Date(year, month, 1)
    const lastDayOfMonth = new Date(year, month + 1, 0)
    
    let startDayIdx = firstDayOfMonth.getDay() - 1
    if (startDayIdx === -1) startDayIdx = 6

    const prevMonthLastDay = new Date(year, month, 0).getDate()
    for (let i = startDayIdx - 1; i >= 0; i--) {
        cells.push({ day: prevMonthLastDay - i, isCurrentMonth: false, shifts: [] })
    }

    const todayStr = new Date().toISOString().split('T')[0]
    for (let day = 1; day <= lastDayOfMonth.getDate(); day++) {
        // 🌟 ĐÃ SỬA: Tạo chuỗi ngày yyyy-MM-dd thủ công bằng cách cộng chuỗi để không bị dính múi giờ UTC/ISO
        const txtMonth = (month + 1) < 10 ? '0' + (month + 1) : (month + 1);
        const txtDay = day < 10 ? '0' + day : day;
        const dateStr = `${year}-${txtMonth}-${txtDay}`;
        
        // So khớp chuẩn xác với trường ngayLamViec (chuỗi yyyy-MM-dd) từ API Backend
        const dayShifts = processedLichList.value.filter(item => item.ngayLamViec === dateStr)

        cells.push({
            day,
            dateStr,
            isCurrentMonth: true,
            isToday: dateStr === todayStr,
            shifts: dayShifts
        })
    }
    return cells
})

const loadLichFromApi = async () => {
    let tuNgay = filter.value.ngay
    let denNgay = filter.value.ngay

    if (currentView.value === 'month') {
        const year = selectedDate.value.getFullYear()
        const month = selectedDate.value.getMonth() + 1
        const txtMonth = month < 10 ? '0' + month : month
        tuNgay = `${year}-${txtMonth}-01`
        denNgay = `${year}-${txtMonth}-31`
    }

    try {
        const res = await axios.get('http://localhost:8080/api/lich-lam-viec/danh-sach', {
            params: { tuNgay, denNgay }
        })
        totalLichData.value = res.data || []
    } catch (e) {
        console.error("Lỗi đồng bộ dữ liệu lịch:", e)
    }
}
const openAddModal = async () => {
    showAddModal.value = true
    formXepLich.value.ngayLamViec = filter.value.ngay;

    try {
        const res = await axios.get('http://localhost:8080/nhan-vien/hien-thi')
        danhSachNhanVienAll.value = res.data || []
    } catch (e) {
        console.error("Lỗi tải danh sách nhân viên:", e)
    }
}
const handleSaveNewLich = async () => {
    if (
        !formXepLich.value.idNhanVien ||
        !formXepLich.value.caLamViec.id ||
        !formXepLich.value.ngayLamViec
    ) {
        return alert("Vui lòng nhập đầy đủ thông tin xếp lịch!");
    }
    const ngayChon = new Date(formXepLich.value.ngayLamViec);
    const homNay = new Date();
    ngayChon.setHours(0, 0, 0, 0);
    homNay.setHours(0, 0, 0, 0);

    if (ngayChon < homNay) {
        return alert("Không được xếp lịch làm việc trong quá khứ!");
    }
    try {
        const ngayChuan = new Date(formXepLich.value.ngayLamViec)
            .toISOString()
            .split("T")[0];

        const idNv = Number(formXepLich.value.idNhanVien);
        const idCa = Number(formXepLich.value.caLamViec.id);
        const dataPost = {
            idNhanVien: idNv,
            caLamViec: {
                id: idCa
            },
            ngayLamViec: ngayChuan
        };

        console.log("Data gửi lên:", dataPost);

        const res = await axios.post(
            "http://localhost:8080/api/lich-lam-viec/xep-lich",
            dataPost
        );

        alert("Xếp lịch làm việc thành công!");

        showAddModal.value = false;

        formXepLich.value = {
            idNhanVien: null,
            caLamViec: {
                id: null
            },
            ngayLamViec: new Date().toISOString().split("T")[0]
        };

        await loadLichFromApi();

    } catch (error) {
        console.error("Lỗi:", error);

        if (error.response) {
            console.log(error.response.data);

            alert(
                error.response.data.detail ||
                error.response.data.message ||
                "Có lỗi xảy ra!"
            );
        } else {
            alert("Không kết nối được Backend!");
        }
    }
};
const switchView = (view) => {
    currentView.value = view
    loadLichFromApi()
}

const onDateFilterChange = () => {
    selectedDate.value = new Date(filter.value.ngay)
    loadLichFromApi()
}

const filterLich = () => {}
const prevPeriod = () => {
    const newDate = new Date(selectedDate.value)
    if (currentView.value === 'day') {
        newDate.setDate(newDate.getDate() - 1)
    } else {
        newDate.setMonth(newDate.getMonth() - 1)
    }
    selectedDate.value = newDate
    syncFilterDate()
}

const nextPeriod = () => {
    const newDate = new Date(selectedDate.value)
    if (currentView.value === 'day') {
        newDate.setDate(newDate.getDate() + 1)
    } else {
        newDate.setMonth(newDate.getMonth() + 1)
    }
    selectedDate.value = newDate
    syncFilterDate()
}

const goToday = () => {
    selectedDate.value = new Date()
    syncFilterDate()
}

const syncFilterDate = () => {
    filter.value.ngay = selectedDate.value.toISOString().split('T')[0]
    loadLichFromApi()
}

const formatShortDate = (date) => {
    return date.toLocaleDateString('vi-VN')
}

const formatDayOfWeek = (date) => {
    const days = ['Chủ Nhật', 'Thứ Hai', 'Thứ Ba', 'Thứ Tư', 'Thứ Năm', 'Thứ Sáu', 'Thứ Bảy']
    return days[date.getDay()]
}

const getShortName = (fullName) => {
    if (!fullName) return 'NV'
    const words = fullName.trim().split(' ')
    if (words.length >= 2) {
        return (words[words.length - 2][0] + words[words.length - 1][0]).toUpperCase()
    }
    return fullName.slice(0, 2).toUpperCase()
}

const openModalDetail = (dateStr, shiftStr, listData) => {
    modalData.value = {
        dateDisplay: dateStr.split('-').reverse().join('/'),
        caSangList: shiftStr === 'CA_SANG' ? listData : [],
        caChieuList: shiftStr === 'CA_CHIEU' ? listData : []
    }
    showModal.value = true
}

const openModalMonthDetail = (cell) => {
    modalData.value = {
        dateDisplay: cell.dateStr.split('-').reverse().join('/'),
        caSangList: cell.shifts.filter(item => item.caLamViec?.maCa === 'CA_SANG'),
        caChieuList: cell.shifts.filter(item => item.caLamViec?.maCa === 'CA_CHIEU')
    }
    showModal.value = true
}

onMounted(() => {
    loadLichFromApi()
})
</script>

<style scoped>
.calendar-dashboard { padding: 20px; background: #f8fafc; min-height: 100vh; font-family: sans-serif; }
.filter-card { background: white; padding: 16px; border-radius: 12px; display: flex; justify-content: space-between; align-items: center; gap: 20px; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02); margin-bottom: 20px; }
.filter-left { display: flex; gap: 16px; flex-grow: 1; }
.filter-item { display: flex; flex-direction: column; gap: 6px; text-align: left; }
.filter-item label { font-size: 12px; font-weight: 600; color: #64748b; }
.filter-item input, .filter-item select { height: 36px; padding: 0 12px; border: 1px solid #e2e8f0; border-radius: 6px; outline: none; min-width: 150px; font-size: 13px; }
.filter-right { display: flex; gap: 8px; align-items: flex-end; }

.btn { height: 38px; padding: 0 14px; border-radius: 6px; font-size: 13px; font-weight: 600; cursor: pointer; border: none; display: flex; align-items: center; gap: 6px; }
.btn-outline { background: #f1f5f9; color: #475569; }
.btn-download { background: #fff0f3; color: #e45d31; }
.btn-dark { background: #1e293b; color: white; }
.btn-primary { background: #e2780d; color: white; }

.calendar-container { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02); }
.calendar-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.calendar-navigation { display: flex; align-items: center; gap: 12px; }
.nav-btn { background: none; border: 1px solid #e2e8f0; width: 32px; height: 32px; border-radius: 6px; cursor: pointer; }
.current-period-text { font-size: 16px; font-weight: 700; color: #1e293b; min-width: 180px; text-align: center; }
.btn-today { background: white; border: 1px solid #e2e8f0; padding: 6px 12px; border-radius: 6px; font-size: 13px; cursor: pointer; }

.view-switcher { display: flex; background: #f1f5f9; padding: 4px; border-radius: 8px; }
.view-switcher button { border: none; background: none; padding: 6px 16px; font-size: 13px; font-weight: 600; color: #64748b; cursor: pointer; border-radius: 6px; }
.view-switcher button.active { background: #1e293b; color: white; }
.day-view-grid { display: grid; grid-template-columns: 180px 1fr; border-top: 1px solid #e2e8f0; border-left: 1px solid #e2e8f0; }
.grid-header-col { background: #f8fafc; padding: 12px; font-weight: 700; font-size: 13px; color: #475569; border-bottom: 1px solid #e2e8f0; border-right: 1px solid #e2e8f0; text-align: left; }
.date-sub { font-weight: 500; color: #64748b; font-size: 12px; }
.text-center { text-align: center; }

.ca-info-cell { padding: 20px 12px; border-bottom: 1px solid #e2e8f0; border-right: 1px solid #e2e8f0; display: flex; flex-direction: column; gap: 4px; text-align: left; }
.ca-info-cell strong { font-size: 14px; }
.ca-info-cell span { font-size: 12px; color: #64748b; }
.ca-content-cell { padding: 12px; border-bottom: 1px solid #e2e8f0; border-right: 1px solid #e2e8f0; display: flex; align-items: center; min-height: 80px; }
.no-shift-text { font-size: 13px; color: #94a3b8; font-style: italic; }
.ca-sang-text { color: #10b981; } .ca-sang-bg { background: #f0fdf4; }
.ca-chieu-text { color: #f59e0b; } .ca-chieu-bg { background: #fffbeb; }

.shift-card { background: white; border: 1px solid #e2e8f0; border-radius: 8px; padding: 12px 20px; display: flex; align-items: center; gap: 30px; width: 100%; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02); }
.shift-title { font-weight: 700; font-size: 14px; color: #1e293b; width: 100px; text-align: left; }
.shift-time { color: #64748b; font-size: 13px; }
.shift-count { background: #f1f5f9; padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: 600; color: #475569; }
.btn-view-more { margin-left: auto; background: none; border: 1px solid #da123a; color: #da123a; padding: 6px 14px; border-radius: 20px; font-size: 12px; font-weight: 600; cursor: pointer; }
.btn-view-more:hover { background: #da123a; color: white; }
.month-view-grid { display: grid; grid-template-columns: repeat(7, 1fr); border-top: 1px solid #e2e8f0; border-left: 1px solid #e2e8f0; }
.month-grid-header { background: #f8fafc; padding: 10px; font-weight: 700; font-size: 13px; border-bottom: 1px solid #e2e8f0; border-right: 1px solid #e2e8f0; color: #475569; }
.month-day-cell { height: 110px; padding: 8px; border-bottom: 1px solid #e2e8f0; border-right: 1px solid #e2e8f0; position: relative; display: flex; flex-direction: column; justify-content: space-between; align-items: flex-end; }
.disabled-cell { background-color: #f1f5f9; opacity: 0.5; pointer-events: none; }
.day-number { font-size: 13px; font-weight: 600; color: #64748b; }
.is-today { background: #f0fdfa; }
.is-today .day-number { background: #0ea5e9; color: white; width: 22px; height: 22px; display: flex; align-items: center; justify-content: center; border-radius: 50%; }
.avatar-dots-row { display: flex; align-items: center; gap: 2px; width: 100%; justify-content: flex-start; cursor: pointer; margin-bottom: 4px; }
.dot-avatar { width: 24px; height: 24px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 9px; font-weight: 700; color: white; }
.bg-green { background: #10b981; } .bg-orange { background: #f59e0b; }
.dot-more { font-size: 11px; font-weight: 700; color: #64748b; margin-left: 2px; }

/* MODAL BOX POPUP SYSTEM */
.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.4); display: flex; justify-content: center; align-items: center; z-index: 9999; }
.modal-box { background: white; width: 100%; max-width: 480px; border-radius: 16px; display: flex; flex-direction: column; overflow: hidden; box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1); }
.modal-header { padding: 18px 24px; display: flex; justify-content: space-between; align-items: flex-start; border-bottom: 1px solid #f1f5f9; text-align: left; }
.modal-header h3 { margin: 0; font-size: 18px; color: #1e293b; font-weight: 700; }
.modal-subtitle { font-size: 12px; color: #94a3b8; display: block; margin-top: 4px; }
.close-modal-btn { background: none; border: none; font-size: 24px; color: #94a3b8; cursor: pointer; line-height: 1; }

.modal-body { padding: 20px 24px; display: flex; flex-direction: column; gap: 16px; max-height: 450px; overflow-y: auto; }
.modal-shift-section { border: 1px solid #e2e8f0; border-radius: 12px; padding: 14px; background: #fff; text-align: left; }
.border-green { border-color: #bbf7d0; } .border-orange { border-color: #fef3c7; }
.shift-section-header { display: flex; justify-content: space-between; font-weight: 700; font-size: 13px; margin-bottom: 12px; }
.shift-section-header small { font-weight: 500; color: #64748b; margin-left: 6px; }
.count-nv { font-size: 12px; font-weight: 600; color: #64748b; }

.nv-list-item { display: flex; align-items: center; gap: 12px; padding: 8px 0; border-bottom: 1px solid #f1f5f9; }
.nv-list-item:last-child { border-bottom: none; }
.nv-avatar-mock { width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 11px; font-weight: 700; background: #f1f5f9; }
.nv-info h4 { margin: 0; font-size: 13px; color: #1e293b; font-weight: 600; }
.nv-info span { font-size: 11px; color: #94a3b8; }

.modal-footer { padding: 16px 24px; border-top: 1px solid #f1f5f9; display: flex; justify-content: center; }
.btn-close-modal { width: 100%; height: 40px; background: #f1f5f9; color: #475569; font-weight: 600; border: none; border-radius: 8px; cursor: pointer; font-size: 14px; }
.btn-close-modal:hover { background: #e2e8f0; }
</style>    