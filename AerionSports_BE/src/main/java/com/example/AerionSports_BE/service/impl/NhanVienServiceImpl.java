package com.example.AerionSports_BE.service.impl;

import com.example.AerionSports_BE.entity.NhanVien;
import com.example.AerionSports_BE.entity.VaiTro;
import com.example.AerionSports_BE.repository.NhanVienRepository;
import com.example.AerionSports_BE.repository.VaiTroRepository;
import com.example.AerionSports_BE.service.NhanVienService;
import com.example.AerionSports_BE.service.EmailService; // ⚡ THÊM IMPORT
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID; // ⚡ THÊM IMPORT ĐỂ SINH CHUỖI NGẪU NHIÊN

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {
    private final NhanVienRepository nhanVienRepository;
    private final VaiTroRepository vaiTroRepository;
    private final EmailService emailService; // ⚡ TIÊM EMAIL SERVICE VÀO HỆ THỐNG

    @Override
    public List<NhanVien> findAll() {
        return nhanVienRepository.findAll();
    }

    @Override
    public NhanVien findById(Integer id) {
        return nhanVienRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhân viên"));
    }

    @Override
    public void changeStatus(Integer id, Integer trangThai) {
        // 1. Tìm kiếm nhân viên
        NhanVien nv = nhanVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + id));

        // 2. Cập nhật trạng thái
        nv.setTrangThai(trangThai);

        // 3. Lưu vào cơ sở dữ liệu
        nhanVienRepository.save(nv);
    }

    @Override
    public NhanVien add(NhanVien nhanVien) {

        // --- LOGIC TỰ TĂNG MÃ NHÂN VIÊN TUẦN TỰ ---
        if (nhanVien.getMaNv() == null || nhanVien.getMaNv().trim().isEmpty()) {
            Optional<NhanVien> maxIdEmployee = nhanVienRepository.findAll()
                    .stream()
                    .max((nv1, nv2) -> nv1.getId().compareTo(nv2.getId()));

            int nextId = 1;
            if (maxIdEmployee.isPresent()) {
                nextId = maxIdEmployee.get().getId() + 1;
            }

            String maTuTang = String.format("NV%03d", nextId);
            nhanVien.setMaNv(maTuTang);
        }
        // ------------------------------------------

        if (nhanVien.getSdt() != null &&
                nhanVienRepository.existsBySdt(nhanVien.getSdt())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        if (nhanVien.getEmail() != null
                && !nhanVien.getEmail().trim().isEmpty()
                && nhanVienRepository.existsByEmail(nhanVien.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Integer vaiTroId = nhanVien.getVaiTro().getId();

        VaiTro vaiTro = vaiTroRepository.findById(vaiTroId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy vai trò"));

        nhanVien.setVaiTro(vaiTro);
        nhanVien.setNgayTao(LocalDateTime.now());
        nhanVien.setNgaySua(LocalDateTime.now());

        // ==========================================================================
        // ⚡ LOGIC TỰ CẤP TÀI KHOẢN & MẬT KHẨU GỬI MAIL
        // ==========================================================================

        // 1. Tự động sinh chuỗi mật khẩu tạm thời ngẫu nhiên gồm 8 ký tự viết thường/số
        String matKhauTamThoi = UUID.randomUUID().toString().substring(0, 8);

        // 2. Thiết lập mật khẩu cho nhân viên.
        // Lưu ý: Nếu hệ thống của bạn đã có Spring Security, bạn nên bọc mã hóa: passwordEncoder.encode(matKhauTamThoi)
        // Hiện tại mình đặt lưu text phẳng hoặc dùng cơ chế lưu mặc định của bạn
        // nhanVien.setMatKhau(matKhauTamThoi);

        // Thực hiện lưu thông tin nhân viên mới xuống Database SQL Server trước
        NhanVien savedEmployee = nhanVienRepository.save(nhanVien);

        // 3. Kích hoạt luồng gửi mail ngầm trả tài khoản về hòm thư Gmail của nhân viên
        if (savedEmployee.getEmail() != null && !savedEmployee.getEmail().trim().isEmpty()) {
            emailService.sendAccountCreationEmail(
                    savedEmployee.getEmail(),
                    savedEmployee.getTenNv(),
                    matKhauTamThoi
            );
        }

        return savedEmployee;
    }

    @Override
    public NhanVien update(Integer id, NhanVien nhanVien) {

        NhanVien nv = nhanVienRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhân viên"));

        // Check trùng SĐT
        if (nhanVien.getSdt() != null
                && !nhanVien.getSdt().equals(nv.getSdt())
                && nhanVienRepository.existsBySdt(nhanVien.getSdt())) {

            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        // Check trùng Email
        if (nhanVien.getEmail() != null
                && !nhanVien.getEmail().trim().isEmpty()
                && !nhanVien.getEmail().equalsIgnoreCase(nv.getEmail())
                && nhanVienRepository.existsByEmail(nhanVien.getEmail())) {

            throw new RuntimeException("Email đã tồn tại");
        }

        VaiTro vaiTro = vaiTroRepository.findById(
                        nhanVien.getVaiTro().getId())
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy vai trò"));

        nv.setVaiTro(vaiTro);

        if (nhanVien.getMaNv() != null && !nhanVien.getMaNv().trim().isEmpty()) {
            nv.setMaNv(nhanVien.getMaNv());
        }

        nv.setTenNv(nhanVien.getTenNv());
        nv.setSdt(nhanVien.getSdt());
        nv.setEmail(nhanVien.getEmail());
        nv.setGioiTinh(nhanVien.getGioiTinh());
        nv.setAvatar(nhanVien.getAvatar());
        nv.setNgaySinh(nhanVien.getNgaySinh());
        nv.setDiaChi(nhanVien.getDiaChi());
        nv.setTrangThai(nhanVien.getTrangThai());
        nv.setNgaySua(LocalDateTime.now());

        return nhanVienRepository.save(nv);
    }

    @Override
    public void delete(Integer id) {

        NhanVien nv = nhanVienRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy nhân viên"));

        nhanVienRepository.delete(nv);
    }

    @Override
    public List<NhanVien> search(String tenNv) {
        return nhanVienRepository.findByTenNvContaining(tenNv);
    }
}