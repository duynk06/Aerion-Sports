import axios from 'axios';

// Tạo một instance riêng cho dự án Aerion Sports
const myAxios = axios.create({
    baseURL: 'http://localhost:8080', // URL mặc định của Backend Spring Boot
    timeout: 10000,
});

// 🌟 BỘ LỌC REQUEST: Tự động đính kèm Token JWT vào mọi API gửi đi
myAxios.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            // Đính kèm đúng chuẩn cấu hình Bearer của Spring Security
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// 🌟 BỘ LỌC RESPONSE: Tự động xử lý nếu Token hết hạn hoặc không hợp lệ
myAxios.interceptors.response.use(
    (response) => response,
    (error) => {
        // Nếu Backend trả về 401 (Chưa đăng nhập) hoặc 403 (Bị chặn quyền)
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            // Xóa sạch dữ liệu đăng nhập cũ
            localStorage.removeItem('token');
            localStorage.removeItem('user_info');
            
            // Đá người dùng về trang login của Vue
            window.location.href = '/admin/login'; 
        }
        return Promise.reject(error);
    }
);

export default myAxios;
