package com.example.AerionSports_BE.service.impl;

import com.example.AerionSports_BE.dto.response.KhachHangResponse;
import com.example.AerionSports_BE.entity.KhachHang;
import com.example.AerionSports_BE.entity.DiaChiKhachHang;
import com.example.AerionSports_BE.repository.KhachHangRepository;
import com.example.AerionSports_BE.service.KhachHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Transactional // Đảm bảo an toàn dữ liệu khi lưu nhiều bảng cùng lúc
    public KhachHang add(KhachHang khachHang) {

        // --- LOGIC TỰ TĂNG MÃ KHÁCH HÀNG TUẦN TỰ ---
        if (khachHang.getMaKhachHang() == null || khachHang.getMaKhachHang().trim().isEmpty()) {
            Optional<KhachHang> maxIdCustomer = khachHangRepository.findAll()
                    .stream()
                    .max((kh1, kh2) -> kh1.getId().compareTo(kh2.getId()));

            int nextId = 1;
            if (maxIdCustomer.isPresent()) {
                nextId = maxIdCustomer.get().getId() + 1;
            }

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

        // --- ĐOẠN MỚI CẬP NHẬT: Thiết lập mối quan hệ Khóa Ngoại cho danh sách địa chỉ ---
        if (khachHang.getAddresses() != null) {
            for (DiaChiKhachHang addr : khachHang.getAddresses()) {
                addr.setKhachHang(khachHang); // Gắn thực thể khách hàng vào từng địa chỉ con
                addr.setNgayTao(LocalDateTime.now());
                addr.setNgayCapNhat(LocalDateTime.now());
            }
        }

        return khachHangRepository.save(khachHang);
    }

    @Override
    @Transactional // Đảm bảo dọn dẹp và cập nhật địa chỉ cũ/mới đồng bộ dưới DB
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

        if (khachHang.getTrangThai() != null) {
            oldKhachHang.setTrangThai(khachHang.getTrangThai());
        }

        // --- ĐOẠN MỚI CẬP NHẬT: Đồng bộ mảng danh sách địa chỉ ---
        oldKhachHang.getAddresses().clear();

        if (khachHang.getAddresses() != null) {
            for (DiaChiKhachHang addr : khachHang.getAddresses()) {
                addr.setKhachHang(oldKhachHang);

                if (addr.getId() == null || String.valueOf(addr.getId()).startsWith("NEW_")) {
                    addr.setId(null);
                    addr.setNgayTao(LocalDateTime.now());
                }

                addr.setNgayCapNhat(LocalDateTime.now());
                oldKhachHang.getAddresses().add(addr);
            }
        }

        oldKhachHang.setNgayCapNhat(LocalDateTime.now());

        return khachHangRepository.save(oldKhachHang);
    }

    @Override
    public void delete(Integer id) {
        KhachHang khachHang = getById(id);
        khachHangRepository.delete(khachHang);
    }

    // 🌟 ĐÃ THÊM MỚI: Hàm xử lý bóc tách mảng Object thô từ SQL đưa về cấu trúc DTO
    @Override
    public List<KhachHangResponse> getAllSummary() {
        List<Object[]> rawData = khachHangRepository.findAllKhachHangWithOrderSummary();
        List<KhachHangResponse> resultList = new ArrayList<>();

        for (Object[] row : rawData) {
            KhachHangResponse dto = new KhachHangResponse();
            dto.setId((Integer) row[0]);
            dto.setMaKhachHang((String) row[1]);
            dto.setHoTen((String) row[2]);
            dto.setEmail((String) row[3]);
            dto.setSdt((String) row[4]);

            // Ép kiểu Date an toàn tránh crash hệ thống
            if (row[5] != null) {
                dto.setNgaySinh(((java.sql.Date) row[5]).toLocalDate());
            }

            // Đếm tổng số đơn hàng
            dto.setTongSoDonHang(row[6] != null ? ((Number) row[6]).longValue() : 0L);

            // Tìm mốc thời gian đơn hàng mới nhất
            if (row[7] != null) {
                dto.setDonHangGanNhat(((Timestamp) row[7]).toLocalDateTime());
            }

            resultList.add(dto);
        }
        return resultList;
    }
}