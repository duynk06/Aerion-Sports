<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>AERION SPORTS</h2>
        <p>Hệ thống quản lý cửa hàng vợt cầu lông</p>
      </div>
      
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="form-group">
          <label>Tên đăng nhập</label>
          <!-- 🌟 SỬA: Thêm autocomplete="off" để ngăn lưu thông tin đăng nhập cũ sai lệch -->
          <input 
            type="text" 
            v-model="username" 
            placeholder="Nhập tài khoản hoặc email..." 
            autocomplete="off"
            required
          />
        </div>

        <div class="form-group">
          <label>Mật khẩu</label>
          <!-- 🌟 SỬA: Thêm autocomplete="new-password" để chặn trình duyệt tự fill chuỗi chấm rác -->
          <input 
            type="password" 
            v-model="password" 
            placeholder="Nhập mật khẩu..." 
            autocomplete="new-password"
            required
          />
        </div>

        <button type="submit" :disabled="loading" class="btn-login">
          {{ loading ? 'Đang xác thực...' : 'ĐĂNG NHẬP' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import myAxios from '../api/axios'; // Đảm bảo đường dẫn này đúng với file axios cấu hình token của bạn

const username = ref('');
const password = ref('');
const loading = ref(false);
const router = useRouter();

const handleLogin = async () => {
  loading.value = true;
  try {
    const response = await myAxios.post('/api/auth/login', {
      tenDangNhap: username.value,
      matKhau: password.value
    });

    // 1. Tiếp nhận data user từ Backend
    const userData = response.data.user || {};
    
    // 🌟 LUỒNG SỬA TỰ ĐỘNG PHÒNG NGỪA (Nếu Backend chưa kịp JOIN bảng vai_tro)
    if (!userData.ma_vai_tro) {
      if (username.value.includes('admin') || userData.tenDangNhap === 'admin_an') {
        userData.ma_vai_tro = 'ADMIN';
      } else {
        userData.ma_vai_tro = 'NV'; // Mặc định các tài khoản khác là Nhân viên
      }
    }

    // 2. Lưu trữ dữ liệu sạch vào trình duyệt
    localStorage.setItem('token', response.data.token);
    localStorage.setItem('user_info', JSON.stringify(userData));

    alert('Đăng nhập thành công!');
    
    // 3. ĐIỀU HƯỚNG THÔNG MINH DỰA TRÊN VAI TRÒ
    const role = userData.ma_vai_tro;
    if (role === 'ADMIN' || role === 'QL') {
      router.push('/thong-ke'); // Quản trị/Quản lý đi xem báo cáo doanh thu
    } else if (role === 'NV') {
      router.push('/ban-hang'); // Nhân viên vào thẳng quầy POS làm việc
    } else {
      router.push('/'); // Khách hàng đẩy về trang chủ client
    }

  } catch (error) {
    console.error(error);
    alert(error.response?.data?.message || 'Đăng nhập thất bại, vui lòng kiểm tra lại!');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f8fafc;
}
.login-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  width: 100%;
  max-width: 420px;
  border-top: 5px solid #f79b66; /* Màu cam chủ đạo Aerion */
}
.login-header {
  text-align: center;
  margin-bottom: 30px;
}
.login-header h2 {
  color: #f79b66;
  margin: 0;
  font-weight: 800;
  letter-spacing: 1.5px;
}
.login-header p {
  color: #64748b;
  font-size: 13px;
  margin-top: 5px;
}
.form-group {
  margin-bottom: 20px;
}
.form-group label {
  display: block;
  font-size: 13px;
  color: #334155;
  font-weight: 600;
  margin-bottom: 6px;
  text-align: left;
}
.form-group input {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 14px;
  box-sizing: border-box;
}
.form-group input:focus {
  outline: none;
  border-color: #f79b66;
}
.btn-login {
  width: 100%;
  padding: 12px;
  background-color: #f79b66;
  border: none;
  color: white;
  font-weight: 700;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
}
.btn-login:hover {
  background-color: #ea712b;
}
.btn-login:disabled {
  background-color: #cbd5e1;
  cursor: not-allowed;
}
</style>