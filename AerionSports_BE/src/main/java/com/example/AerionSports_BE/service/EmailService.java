package com.example.AerionSports_BE.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Tính năng 1: Gửi email cấp tài khoản và mật khẩu tạm thời cho Nhân viên mới
     * @param toEmail Email của nhân viên nhận tài khoản
     * @param tenNhanVien Tên hiển thị của nhân viên
     * @param matKhauTamThoi Chuỗi mật khẩu ngẫu nhiên 8 ký tự sinh từ Backend
     */
    @Async // Chạy ngầm đa luồng để không gây nghẽn/đơ giao diện Front-end
    public void sendAccountCreationEmail(String toEmail, String tenNhanVien, String matKhauTamThoi) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🎉 Aerion Sports - Thông báo cấp tài khoản nhân viên hệ thống");

            // Thiết kế giao diện HTML phẳng với tông màu cam chủ đạo của thương hiệu
            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 25px; border: 1px solid #f79b66; border-radius: 12px; max-width: 550px; margin: 0 auto;'>"
                    + "<div style='text-align: center; margin-bottom: 20px;'>"
                    + "  <h2 style='color: #f79b66; margin: 0; font-size: 22px; letter-spacing: 1px;'>CHÀO MỪNG THÀNH VIÊN MỚI</h2>"
                    + "  <p style='color: #475569; font-size: 14px;'>Tài khoản quản trị nội bộ hệ thống Aerion Sports của bạn đã được khởi tạo!</p>"
                    + "</div>"
                    + "<p>Xin chào <strong>" + tenNhanVien + "</strong>,</p>"
                    + "<p>Dưới đây là thông tin đăng nhập cá nhân của bạn trên hệ thống, vui lòng bảo mật thông tin này:</p>"
                    + "<div style='background-color: #f8fafc; padding: 18px; border-radius: 8px; border: 1px solid #e2e8f0; margin: 15px 0; line-height: 1.6;'>"
                    + "  <p style='margin: 5px 0; font-size: 14px;'>🌐 <strong>Trang quản trị:</strong> <a href='http://localhost:5173/login' style='color: #ea712b; text-decoration: none; font-weight: bold;'>Click để đến trang Đăng nhập</a></p>"
                    + "  <p style='margin: 5px 0; font-size: 14px;'>📧 <strong>Tài khoản (Username):</strong> <span style='font-weight: 600; color: #1e293b;'>" + toEmail + "</span></p>"
                    + "  <p style='margin: 5px 0; font-size: 14px;'>🔑 <strong>Mật khẩu tạm thời:</strong> <span style='font-weight: 700; color: #dc2626; font-family: monospace; background: #fee2e2; padding: 2px 6px; border-radius: 4px;'>" + matKhauTamThoi + "</span></p>"
                    + "</div>"
                    + "<p style='color: #ef4444; font-size: 12.5px; font-style: italic; font-weight: 500;'>⚠️ *Lưu ý quan trọng: Vì lý do bảo mật dữ liệu cửa hàng, bạn bắt buộc phải thực hiện thay đổi mật khẩu mới ngay trong lần đầu tiên đăng nhập hệ thống thành công.</p>"
                    + "<hr style='border: none; border-top: 1px solid #f1f5f9; margin: 20px 0;'/>"
                    + "<p style='font-size: 11px; color: #94a3b8; text-align: center; margin: 0;'>Hệ thống vận hành tự động Aerion Sports &copy; 2026</p>"
                    + "</div>";

            helper.setText(htmlContent, true); // Đánh dấu true để hiển thị định dạng HTML thay vì text thuần
            mailSender.send(message);
            System.out.println(">>> [MAIL SUCCESS] Đã gửi thông tin tài khoản thành công tới email: " + toEmail);
        } catch (Exception e) {
            System.err.println(">>> [MAIL ERROR] Thất bại khi gửi tài khoản về mail " + toEmail + ". Lý do: " + e.getMessage());
        }
    }

    /**
     * Tính năng 2: Gửi email chứa mã giảm giá (Voucher) cá nhân dành riêng cho Khách hàng được tri ân
     * @param toEmail Email của khách hàng nhận voucher
     * @param tenKhachHang Tên hiển thị của khách hàng
     * @param maVoucher Mã phiếu giảm giá (Ví dụ: APRILELEMENT, TRIAN2026...)
     * @param giaTriGiam Giá trị phần trăm giảm giá (Ví dụ: 20%)
     * @param ngayKetThuc Hạn sử dụng của voucher
     */
    @Async
    public void sendVoucherEmail(String toEmail, String tenKhachHang, String maVoucher, String giaTriGiam, String ngayKetThuc) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🎁 Quà tặng đặc biệt từ Aerion Sports dành riêng cho bạn!");

            // Thiết kế giao diện HTML phiếu quà tặng bắt mắt
            String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 25px; border: 1px solid #f79b66; border-radius: 12px; max-width: 500px; margin: 0 auto;'>"
                    + "<div style='text-align: center; margin-bottom: 15px;'>"
                    + "  <h2 style='color: #ea712b; margin: 0; font-size: 20px;'>MÓN QUÀ TRI ÂN ĐẶC BIỆT</h2>"
                    + "</div>"
                    + "<p>Thân gửi Quý khách hàng <strong>" + tenKhachHang + "</strong>,</p>"
                    + "<p>Aerion Sports xin gửi tặng riêng bạn mã giảm giá cá nhân siêu ưu đãi áp dụng cho toàn bộ các sản phẩm vợt và phụ kiện cầu lông tại hệ thống:</p>"
                    + "<div style='background: #fff7ed; padding: 20px; text-align: center; border: 2px dashed #f79b66; border-radius: 8px; margin: 20px 0;'>"
                    + "  <span style='font-size: 13px; color: #7c2d12; display: block; margin-bottom: 5px; font-weight: bold;'>MÃ PHIẾU GIẢM GIÁ CỦA BẠN:</span>"
                    + "  <span style='font-size: 24px; font-weight: 800; color: #ea712b; font-family: sans-serif; letter-spacing: 1px;'>" + maVoucher + "</span>"
                    + "  <span style='display: block; margin-top: 8px; font-size: 15px; color: #1e293b; font-weight: bold;'>Ưu đãi giảm ngay: " + giaTriGiam + "</span>"
                    + "</div>"
                    + "<p style='font-size: 14px;'>⏰ Thời hạn áp dụng ưu đãi kéo dài đến hết ngày: <strong style='color: #ea712b;'>" + ngayKetThuc + "</strong></p>"
                    + "<p style='margin-top: 15px;'>Mã số có hạn và chỉ áp dụng duy nhất cho tài khoản cá nhân của bạn. Hãy nhanh chân ghé qua cửa hàng Aerion Sports để săn ngay những siêu phẩm với mức giá tốt nhất nhé!</p>"
                    + "<hr style='border: none; border-top: 1px solid #f1f5f9; margin: 20px 0;'/>"
                    + "<p style='font-size: 11px; color: #94a3b8; text-align: center; margin: 0;'>Cảm ơn bạn đã đồng hành cùng Aerion Sports &copy; 2026</p>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            System.out.println(">>> [MAIL SUCCESS] Đã phát hành và gửi voucher thành công tới khách hàng: " + toEmail);
        } catch (Exception e) {
            System.err.println(">>> [MAIL ERROR] Lỗi khi phát hành voucher tới mail " + toEmail + ". Chi tiết: " + e.getMessage());
        }
    }
}