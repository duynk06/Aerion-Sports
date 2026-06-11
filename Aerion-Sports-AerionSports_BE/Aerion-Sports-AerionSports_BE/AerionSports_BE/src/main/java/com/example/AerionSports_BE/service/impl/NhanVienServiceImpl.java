package com.example.AerionSports_BE.service.impl;

import com.example.AerionSports_BE.entity.NhanVien;
import com.example.AerionSports_BE.entity.VaiTro;
import com.example.AerionSports_BE.repository.NhanVienRepository;
import com.example.AerionSports_BE.repository.VaiTroRepository;
import com.example.AerionSports_BE.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {
    private final NhanVienRepository nhanVienRepository;
    private final VaiTroRepository vaiTroRepository;

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
    public NhanVien create(NhanVien nhanVien) {

        // --- LOGIC TỰ TĂNG MÃ NHÂN VIÊN TUẦN TỰ ---
        if (nhanVien.getMaNv() == null || nhanVien.getMaNv().trim().isEmpty()) {
            // Tìm nhân viên có ID lớn nhất vừa được thêm vào hệ thống
            Optional<NhanVien> maxIdEmployee = nhanVienRepository.findAll()
                    .stream()
                    .max((nv1, nv2) -> nv1.getId().compareTo(nv2.getId()));

            int nextId = 1; // Nếu chưa có nhân viên nào, mặc định bắt đầu từ 1
            if (maxIdEmployee.isPresent()) {
                nextId = maxIdEmployee.get().getId() + 1; // Nếu có rồi thì lấy ID đó cộng thêm 1
            }

            // Định dạng chuỗi: NV + số thứ tự (ví dụ số 6 thành "NV006", số 12 thành "NV012")
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

        return nhanVienRepository.save(nhanVien);
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

        // Không cho mất mã nhân viên khi cập nhật thông tin
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