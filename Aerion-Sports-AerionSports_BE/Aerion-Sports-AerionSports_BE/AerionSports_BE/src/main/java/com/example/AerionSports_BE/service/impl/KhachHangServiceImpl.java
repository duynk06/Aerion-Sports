package com.example.AerionSports_BE.service.impl;

import com.example.AerionSports_BE.entity.KhachHang;
import com.example.AerionSports_BE.repository.KhachHangRepository;
import com.example.AerionSports_BE.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KhachHangServiceImpl implements KhachHangService {
    private final KhachHangRepository khachHangRepository;

    @Override
    public List<KhachHang> getAll() {
        return khachHangRepository.findAll();
    }

    @Override
    public KhachHang getById(Integer id) {
        return khachHangRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy khách hàng với id: " + id));
    }

    @Override
    public KhachHang add(KhachHang khachHang) {

        // --- ĐOẠN ĐƯỢC SỬA: LOGIC TỰ TĂNG MÃ KHÁCH HÀNG TUẦN TỰ ---
        if (khachHang.getMaKhachHang() == null || khachHang.getMaKhachHang().trim().isEmpty()) {
            // Tìm khách hàng có ID lớn nhất vừa được thêm vào hệ thống
            Optional<KhachHang> maxIdCustomer = khachHangRepository.findAll()
                    .stream()
                    .max((kh1, kh2) -> kh1.getId().compareTo(kh2.getId()));

            int nextId = 1; // Nếu chưa có khách hàng nào, mặc định bắt đầu từ 1
            if (maxIdCustomer.isPresent()) {
                nextId = maxIdCustomer.get().getId() + 1; // Nếu có rồi thì lấy ID đó cộng thêm 1
            }

            // Định dạng chuỗi: KH + số thứ tự (ví dụ số 6 thành "KH006", số 15 thành "KH015")
            // Số 3 ở "%03d" nghĩa là mã sẽ hiển thị tối thiểu 3 chữ số
            String maTuTang = String.format("KH%03d", nextId);
            khachHang.setMaKhachHang(maTuTang);
        }
        // ---------------------------------------------------------

        if (khachHangRepository.existsByMaKhachHang(khachHang.getMaKhachHang())) {
            throw new RuntimeException("Mã khách hàng đã tồn tại");
        }

        if (khachHang.getSdt() != null
                && khachHangRepository.existsBySdt(khachHang.getSdt())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        if (khachHang.getEmail() != null
                && !khachHang.getEmail().trim().isEmpty()
                && khachHangRepository.existsByEmail(khachHang.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        khachHang.setNgayTao(LocalDateTime.now());
        khachHang.setNgayCapNhat(LocalDateTime.now());

        return khachHangRepository.save(khachHang);
    }

    @Override
    public KhachHang update(Integer id, KhachHang khachHang) {

        KhachHang oldKhachHang = getById(id);

        // Check trùng SĐT
        if (khachHang.getSdt() != null
                && !khachHang.getSdt().equals(oldKhachHang.getSdt())
                && khachHangRepository.existsBySdt(khachHang.getSdt())) {

            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        // Check trùng Email
        if (khachHang.getEmail() != null
                && !khachHang.getEmail().trim().isEmpty()
                && !khachHang.getEmail().equalsIgnoreCase(oldKhachHang.getEmail())
                && khachHangRepository.existsByEmail(khachHang.getEmail())) {

            throw new RuntimeException("Email đã tồn tại");
        }

        // Không cho mất mã khách hàng
        if (khachHang.getMaKhachHang() != null &&
                !khachHang.getMaKhachHang().trim().isEmpty()) {

            oldKhachHang.setMaKhachHang(khachHang.getMaKhachHang());
        }

        if (khachHang.getHoTen() != null) {
            oldKhachHang.setHoTen(khachHang.getHoTen());
        }

        if (khachHang.getNgaySinh() != null) {
            oldKhachHang.setNgaySinh(khachHang.getNgaySinh());
        }

        if (khachHang.getGioiTinh() != null) {
            oldKhachHang.setGioiTinh(khachHang.getGioiTinh());
        }

        if (khachHang.getSdt() != null) {
            oldKhachHang.setSdt(khachHang.getSdt());
        }

        if (khachHang.getEmail() != null) {
            oldKhachHang.setEmail(khachHang.getEmail());
        }

        if (khachHang.getAvatar() != null) {
            oldKhachHang.setAvatar(khachHang.getAvatar());
        }

        if (khachHang.getDiaChi() != null) {
            oldKhachHang.setDiaChi(khachHang.getDiaChi());
        }

        if (khachHang.getTrangThai() != null) {
            oldKhachHang.setTrangThai(khachHang.getTrangThai());
        }

        oldKhachHang.setNgayCapNhat(LocalDateTime.now());

        return khachHangRepository.save(oldKhachHang);
    }

    @Override
    public void delete(Integer id) {
        KhachHang khachHang = getById(id);
        khachHangRepository.delete(khachHang);
    }
}