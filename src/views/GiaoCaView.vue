<template>
    <MainLayout title="Quản lý giao ca">
        <div class="split-layout">

            <div v-if="!hasActiveCa" class="form-container single-panel">
                <div class="list-header">
                    <div class="list-title">
                        <div class="title-icon" style="background: #22c55e;">
                            <i class="fa-solid fa-door-open"></i>
                        </div>
                        <div>
                            <h3>Mở ca làm việc mới</h3>
                            <span>Vui lòng chọn nhân viên và nhập số tiền mặt ban đầu trong két</span>
                        </div>
                    </div>
                </div>

                <div class="form-grid two-columns">
                    <div class="form-group">
                        <label>Nhân viên mở ca <span style="color: red;">*</span></label>
                        <select v-model="currentUserId" @change="checkTrangThaiCaHienTai" style="height: 40px;">
                            <option :value="null">-- Chọn nhân viên mở ca --</option>
                            <option v-for="nv in danhSachNhanVien" :key="nv.id" :value="nv.id">
                                [{{ nv.maNv || nv.ma_nv }}] - {{ nv.tenNv || nv.ten_nv }}
                            </option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Tiền mặt đầu ca (đ) <span style="color: red;">*</span></label>
                        <input type="text" :value="formatCurrency(formMoCa.tienBanDau)"
                            @input="onInputCurrencyMoCa('tienBanDau', $event)"
                            placeholder="Nhập số tiền mặt có sẵn trong két lẻ..." />
                    </div>
                    
                    <div class="form-group full-width">
                        <label>Ghi chú mở ca</label>
                        <textarea rows="3" v-model="formMoCa.ghiChu"
                            placeholder="Nhập ghi chú đầu ca nếu có..."></textarea>
                    </div>
                </div>

                <div class="button-group mt-20">
                    <button class="btn-save" @click="handleMoCa" style="background: #22c55e; width: 200px;">Kích hoạt mở ca</button>
                </div>
            </div>

            <div v-else class="form-container single-panel">
                <div class="list-header">
                    <div class="list-title">
                        <div class="title-icon" style="background: #ef4444;">
                            <i></i>
                        </div>
                        <div>
                            <h3>Kết thúc ca làm việc</h3>
                            <span>Mã ca hiện tại: <strong class="product-code-text">{{ caHienTai.maCa }}</strong></span>
                        </div>
                    </div>
                </div>

                <div class="form-grid two-columns">
                    <div class="form-group">
                        <label>Nhân viên hiện tại</label>
                        <input type="text" :value="caHienTai.tenNhanVien" disabled class="disabled-input" />
                    </div>

                    <div class="form-group">
                        <label>Thời gian vào ca</label>
                        <input type="text" :value="formatDateTime(caHienTai.thoiGianVao)" disabled
                            class="disabled-input" />
                    </div>

                    <div class="form-group">
                        <label>Tiền mặt đầu ca (đ)</label>
                        <input type="text" :value="formatCurrency(caHienTai.tienBanDau)" disabled
                            class="disabled-input" />
                    </div>

                    <div class="form-group">
                        <label>Doanh thu tiền mặt trong ca (đ)</label>
                        <input type="text" :value="formatCurrency(caHienTai.tienMatDoanhThu)"
                            @input="onInputCurrencyAdmin('tienMatDoanhThu', $event)"
                            placeholder="Nhập doanh thu tiền mặt..." />
                    </div>

                    <div class="form-group">
                        <label>Doanh thu chuyển khoản / Ví (đ)</label>
                        <input type="text" :value="formatCurrency(caHienTai.tienChuyenKhoan)"
                            @input="onInputCurrencyAdmin('tienChuyenKhoan', $event)"
                            placeholder="Nhập doanh thu chuyển khoản..." />
                    </div>

                    <div class="form-group">
                        <label>Tổng doanh thu của ca (đ)</label>
                        <input type="text" :style="{ fontWeight: 'bold', color: '#2563eb' }"
                            :value="formatCurrency(tongDoanhThuCa)" disabled class="disabled-input" />
                    </div>

                    <div class="form-group full-width">
                        <label>Ghi chú kết thúc ca</label>
                        <textarea rows="3" v-model="formGiaoCa.ghiChu"
                            placeholder="Nhập ghi chú chốt két ca làm việc..."></textarea>
                    </div>
                </div>

                <div class="divider"></div>

                <div class="button-group single-column">
                    <button class="btn-save" @click="handleXacNhanGiaoCa" style="width: 100%; background: #ef4444;">
                        <i style="margin-right: 5px;"></i> Kết thúc ca làm việc
                    </button>
                </div>
            </div>

        </div>
    </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import MainLayout from '@/layouts/MainLayout.vue'
import axios from 'axios'

const currentUserId = ref(null)
const hasActiveCa = ref(false)

const caHienTai = ref({
    id: null, maCa: '', tenNhanVien: '', thoiGianVao: '', tienBanDau: 0, tienMatDoanhThu: 0, tienChuyenKhoan: 0
})

const danhSachNhanVien = ref([])
const formMoCa = ref({ tienBanDau: 0, ghiChu: '' })
const formGiaoCa = ref({ ghiChu: '' })

const formatCurrency = (value) => {
    if (value === null || value === undefined || isNaN(value)) return '0'
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
}

const formatDateTime = (str) => {
    if (!str) return ''
    return str.replace('T', ' ').slice(0, 19)
}

const onInputCurrencyMoCa = (field, event) => {
    let val = event.target.value.replace(/[^0-9]/g, '')
    formMoCa.value[field] = val ? Number(val) : 0
    event.target.value = formatCurrency(formMoCa.value[field])
}

const onInputCurrencyAdmin = (field, event) => {
    let val = event.target.value.replace(/[^0-9]/g, '')
    caHienTai.value[field] = val ? Number(val) : 0
    event.target.value = formatCurrency(caHienTai.value[field])
}

const tongDoanhThuCa = computed(() => {
    return (caHienTai.value.tienMatDoanhThu || 0) + (caHienTai.value.tienChuyenKhoan || 0)
})

const checkTrangThaiCaHienTai = async () => {
    if (!currentUserId.value) {
        hasActiveCa.value = false
        return
    }
    try {
        const res = await axios.get(`http://localhost:8080/api/giao-ca/ca-hien-tai/${currentUserId.value}`)
        if (res.data && res.data.maCa) {
            caHienTai.value = res.data
            hasActiveCa.value = true
        } else {
            hasActiveCa.value = false
        }
    } catch (error) {
        hasActiveCa.value = false
    }
}

const loadDanhSachNhanVien = async () => {
    try {
        const res = await axios.get('http://localhost:8080/nhan-vien/hien-thi')
        danhSachNhanVien.value = res.data || []
    } catch (e) {
        console.error('Lỗi tải nhân viên:', e)
    }
}

const handleMoCa = async () => {
    if (!currentUserId.value) {
        return alert("Vui lòng chọn nhân viên thực hiện mở ca trước!")
    }
    if (formMoCa.value.tienBanDau === null || formMoCa.value.tienBanDau < 0) {
        return alert("Vui lòng nhập số tiền mặt đầu ca hợp lệ!")
    }

    try {
        const dataPost = {
            idNhanVien: currentUserId.value,
            tienBanDau: formMoCa.value.tienBanDau
        }

        await axios.post('http://localhost:8080/api/giao-ca/mo-ca', dataPost)
        alert("Mở ca làm việc thành công!")
        await checkTrangThaiCaHienTai()
    } catch (error) {
        if (error.response && error.response.data) {
            alert(error.response.data.message || "Có lỗi xảy ra khi mở ca!")
        } else {
            alert("Lỗi kết nối đến hệ thống Backend!");
        }
    }
}

const handleXacNhanGiaoCa = async () => {
    if (!confirm('Bạn chắc chắn muốn kết thúc ca làm việc hiện tại và chốt két chứ?')) return

    try {
        await axios.post('http://localhost:8080/api/giao-ca/ket-thuc', {
            idNhanVien: currentUserId.value,
            idNhanVienCaSau: currentUserId.value, 
            ghiChu: formGiaoCa.value.ghiChu,
            tienMatDoanhThu: caHienTai.value.tienMatDoanhThu,
            tienChuyenKhoan: caHienTai.value.tienChuyenKhoan
        })

        alert('Kết thúc phiên làm việc thành công!')
        
        hasActiveCa.value = false
        currentUserId.value = null
        formMoCa.value = { tienBanDau: 0, ghiChu: '' }
        formGiaoCa.value = { ghiChu: '' }
        caHienTai.value = { id: null, maCa: '', tenNhanVien: '', thoiGianVao: '', tienBanDau: 0, tienMatDoanhThu: 0, tienChuyenKhoan: 0 }

    } catch (error) {
        alert(error.response?.data?.message || 'Kết thúc ca thất bại!');
    }
}

onMounted(async () => {
    await loadDanhSachNhanVien()
})
</script>

<style scoped>
.split-layout { 
    display: flex; 
    justify-content: center; 
    width: 100%; 
}

/* 🌟 ĐÃ SỬA: Tăng chiều rộng panel từ 600px lên tối đa 1000px */
.single-panel { 
    width: 100%; 
    max-width: 1000px; 
    margin: 20px auto; 
}

.form-container { 
    background: #ffffff; 
    padding: 24px; 
    border-radius: 12px; 
    border: 1px solid #edf2f7; 
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05); 
}

.list-header { 
    display: flex; 
    justify-content: space-between; 
    align-items: center; 
    margin-bottom: 25px; 
    padding-bottom: 20px; 
    border-bottom: 1px solid #edf2f7; 
}

.list-title { 
    display: flex; 
    align-items: center; 
    gap: 12px; 
}

.title-icon { 
    width: 38px; 
    height: 38px; 
    color: white; 
    border-radius: 8px; 
    display: flex; 
    align-items: center; 
    justify-content: center; 
    font-size: 16px; 
}

.list-title h3 { 
    margin: 0; 
    font-size: 18px; 
    font-weight: 700; 
    color: #1a202c; 
    text-align: left; 
}

.list-title span { 
    color: #718096; 
    font-size: 13px; 
    margin-top: 2px; 
    display: block; 
    text-align: left; 
}

/* 🌟 ĐÃ SỬA: Chuyển cấu trúc lưới từ 1 cột sang 2 cột song song */
.form-grid.two-columns { 
    display: grid; 
    grid-template-columns: repeat(2, 1fr); 
    gap: 20px; 
}

.form-group { 
    display: flex; 
    flex-direction: column; 
}

.form-group label { 
    margin-bottom: 8px; 
    font-size: 13px; 
    font-weight: 600; 
    color: #4a5568; 
    text-align: left; 
}

.form-group input, 
.form-group select, 
.form-group textarea { 
    width: 100%; 
    box-sizing: border-box; 
    height: 40px; 
    padding: 0 14px; 
    border: 1px solid #cbd5e1; 
    border-radius: 8px; 
    font-size: 14px; 
    color: #2d3748; 
    outline: none; 
    background-color: #fff; 
    transition: all 0.2s; 
}

.form-group textarea { 
    height: auto; 
    padding: 12px 14px; 
    resize: vertical; 
}

.form-group input:focus, 
.form-group select:focus { 
    border-color: #f79b66; 
}

/* 🌟 ĐÃ THÊM: Ô ghi chú sẽ tự động trải dài hết hàng ngang */
.full-width {
    grid-column: 1 / -1;
}

.disabled-input { 
    background-color: #f8fafc !important; 
    color: #4a5568 !important; 
    cursor: not-allowed; 
    border-color: #e2e8f0 !important; 
    font-weight: 600; 
}

.divider { 
    border-top: 1px solid #edf2f7; 
    margin-bottom: 20px; 
    margin-top: 20px; 
}

.button-group { 
    display: flex; 
    justify-content: center; 
}

.single-column {
    width: 100%;
}

.mt-20 { 
    margin-top: 20px; 
}

.btn-save { 
    height: 44px; 
    padding: 0 24px; 
    border-radius: 8px; 
    font-size: 15px; 
    font-weight: 600; 
    cursor: pointer; 
    border: none; 
    transition: all 0.2s ease; 
    color: white; 
}

.btn-save:hover { 
    opacity: 0.9; 
}

.product-code-text { 
    color: #ef4444; 
    font-weight: bold; 
}
</style>