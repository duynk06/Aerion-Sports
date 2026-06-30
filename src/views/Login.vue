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
          <input 
            type="text" 
            v-model="username" 
            placeholder="Nhập email của bạn..." 
            required
          />
        </div>

        <div class="form-group">
          <label>Mật khẩu</label>
          <input 
            type="password" 
            v-model="password" 
            placeholder="Nhập mật khẩu..." 
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
import myAxios from '../api/axios'; // Import file axios vừa tạo ở Bước 1

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

    // LƯU TRỮ TOKEN VÀ THÔNG TIN USER VÀO LOCALSTORAGE
    localStorage.setItem('token', response.data.token);
    localStorage.setItem('user_info', JSON.stringify(response.data.user));

    alert('Đăng nhập thành công!');
    
    // Đăng nhập xong điều hướng thẳng vào trang bán hàng POS quầy của bạn
    router.push('/ban-hang'); 
  } catch (error) {
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
  border-top: 5px solid #f79b66; /* Màu cam Aerion */
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