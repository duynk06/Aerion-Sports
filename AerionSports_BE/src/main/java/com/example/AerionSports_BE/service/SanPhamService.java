package com.example.AerionSports_BE.service;


import com.example.AerionSports_BE.dto.request.ChiTietSanPhamRequest;
import com.example.AerionSports_BE.dto.request.SanPhamFilter;
import com.example.AerionSports_BE.dto.request.SanPhamRequest;
import com.example.AerionSports_BE.dto.response.ChiTietSanPhamResponse;
import com.example.AerionSports_BE.dto.response.SanPhamResponse;
import com.example.AerionSports_BE.entity.ChiTietSanPham;
import com.example.AerionSports_BE.entity.HinhAnhSp;
import com.example.AerionSports_BE.entity.SanPham;
import com.example.AerionSports_BE.repository.*;
import com.example.AerionSports_BE.service.impl.ISanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SanPhamService implements ISanPhamService {
    @Autowired
    private SanPhamRepository repo;

    @Autowired
    private ThuongHieuRepository thuongHieuRepo;

    @Autowired
    private XuatXuRepository xuatXuRepo;

    @Autowired
    private ChiTietSanPhamRepository chiTietRepo;

    @Autowired
    private HinhAnhSpRepository hinhAnhRepo;

    @Autowired
    private MauSacRepository mauSacRepo;

    @Autowired
    private ChatLieuThanVotRepository chatLieuThanRepo;

    @Autowired
    private ChatLieuKhungVotRepository chatLieuKhungRepo;

    @Autowired
    private TrongLuongRepository trongLuongRepo;

    @Autowired
    private DoCungRepository doCungRepo;

    @Autowired
    private DanhMucRepository danhMucRepo;

    @Autowired
    private DiemCanBangRepository diemCanBangRepo;

    @Autowired
    private ChuViCanVotRepository chuViCanVotRepo;

    private ChiTietSanPhamResponse toChiTietRes(ChiTietSanPham ct) {
        if (ct == null) return null;
        return new ChiTietSanPhamResponse(
                ct.getId(),
                null, // <-- ĐÃ SỬA: Đổi từ ct.getIdSanPham().getId() thành null để ngắt vòng lặp vô hạn JSON
                null, // <-- ĐÃ SỬA: Đổi từ ct.getIdSanPham().getTenSanPham() thành null để ngắt vòng lặp
                ct.getMaCtsp(),
                ct.getIdMauSac() != null ? ct.getIdMauSac().getTenMauSac() : null,
                ct.getIdTrongLuong() != null ? ct.getIdTrongLuong().getTenTrongLuong() : null,
                ct.getIdChuViCanVot() != null ? ct.getIdChuViCanVot().getTenChuViCanVot() : null,
                ct.getIdDoCung() != null ? ct.getIdDoCung().getTenDoCung() : null,
                ct.getIdDiemCanBang() != null ? ct.getIdDiemCanBang().getTenDiemCanBang() : null,
                ct.getIdChatLieuThanVot() != null ? ct.getIdChatLieuThanVot().getTenChatLieuThanVot() : null,
                ct.getIdChatLieuKhungVot() != null ? ct.getIdChatLieuKhungVot().getTenChatLieuKhungVot() : null,
                ct.getIdDanhMuc() != null ? ct.getIdDanhMuc().getTenDanhMuc() : null,
                ct.getGiaNhap(),
                ct.getGiaBan(),
                ct.getSoLuong(),
                ct.getTrangThai(),
                ct.getNgayTao(),
                ct.getNgayCapNhat()
        );
    }

    // Đã nạp mảng con chiTietDTOs vào tham số thứ 13 để Frontend tính khoảng giá
    private SanPhamResponse toRes(SanPham e) {
        Set<ChiTietSanPhamResponse> chiTietDTOs = new LinkedHashSet<>();
        if (e.getChiTietSanPhams() != null) {
            for (ChiTietSanPham ct : e.getChiTietSanPhams()) {
                chiTietDTOs.add(this.toChiTietRes(ct));
            }
        }

        return new SanPhamResponse(
                e.getId(),
                e.getIdThuongHieu() != null ? e.getIdThuongHieu().getId() : null,
                e.getIdThuongHieu() != null ? e.getIdThuongHieu().getTenThuongHieu() : null,
                e.getIdXuatXu() != null ? e.getIdXuatXu().getId() : null,
                e.getIdXuatXu() != null ? e.getIdXuatXu().getTenXuatXu() : null,
                e.getMaSanPham(), e.getTenSanPham(), e.getMoTa(), e.getBaoHanh(),
                e.getTrangThai(), e.getNgayTao(), e.getNgaySua(),
                chiTietDTOs
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SanPhamResponse> search(SanPhamFilter f) {
        return repo.search(f.getKeyword(), f.getIdThuongHieu(), f.getIdXuatXu(), f.getTrangThai(),
                PageRequest.of(f.getPage(), f.getSize(), Sort.by("ngayTao").descending())).map(this::toRes);
    }

    @Override
    public void updateTrangThai(Integer id, Integer trangThai) {
        SanPham sanPham = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm!"));
        sanPham.setTrangThai(trangThai);
        repo.save(sanPham);
    }

    @Transactional
    @Override
    public SanPhamResponse save(SanPhamRequest r, List<MultipartFile> files) {
        if (repo.existsByMaSanPham(r.getMaSanPham())) {
            throw new RuntimeException("Mã sản phẩm đã tồn tại!");
        }

        SanPham e = new SanPham();
        mapFields(e, r);
        e.setNgayTao(Instant.now());
        e.setNgaySua(Instant.now());
        SanPham sanPhamDaLuu = repo.save(e);

        if (r.getChiTietSanPhams() != null && !r.getChiTietSanPhams().isEmpty()) {
            int fileIndex = 0;

            for (ChiTietSanPhamRequest ctReq : r.getChiTietSanPhams()) {
                if (files != null && fileIndex < files.size()) {
                    MultipartFile file = files.get(fileIndex);
                    try {
                        String fileName = saveFileToDisk(file);
                        ctReq.setHinhAnh("/uploads/" + fileName);
                    } catch (IOException ex) {
                        throw new RuntimeException("Lỗi lưu file ảnh");
                    }
                    fileIndex++;
                }

                ChiTietSanPham ctEntity = new ChiTietSanPham();
                ctEntity.setIdSanPham(sanPhamDaLuu);
                ctEntity.setMaCtsp(ctReq.getMaCtsp());

                if (ctReq.getIdMauSac() != null) ctEntity.setIdMauSac(mauSacRepo.findById(ctReq.getIdMauSac()).orElseThrow());
                if (ctReq.getIdTrongLuong() != null) ctEntity.setIdTrongLuong(trongLuongRepo.findById(ctReq.getIdTrongLuong()).orElseThrow());
                if (ctReq.getIdDoCung() != null) ctEntity.setIdDoCung(doCungRepo.findById(ctReq.getIdDoCung()).orElseThrow());
                if (ctReq.getIdChatLieuThanVot() != null) ctEntity.setIdChatLieuThanVot(chatLieuThanRepo.findById(ctReq.getIdChatLieuThanVot()).orElseThrow());
                if (ctReq.getIdChatLieuKhungVot() != null) ctEntity.setIdChatLieuKhungVot(chatLieuKhungRepo.findById(ctReq.getIdChatLieuKhungVot()).orElseThrow());
                if (ctReq.getIdDanhMuc() != null) ctEntity.setIdDanhMuc(danhMucRepo.findById(ctReq.getIdDanhMuc()).orElseThrow());
                if (ctReq.getIdDiemCanBang() != null) ctEntity.setIdDiemCanBang(diemCanBangRepo.findById(ctReq.getIdDiemCanBang()).orElseThrow());
                if (ctReq.getIdChuViCanVot() != null) ctEntity.setIdChuViCanVot(chuViCanVotRepo.findById(ctReq.getIdChuViCanVot()).orElseThrow());

                // ĐÃ SỬA: Gán đầy đủ cả giá nhập và giá bán lên DB SQL Server
                ctEntity.setGiaNhap(ctReq.getGiaNhap());
                ctEntity.setGiaBan(ctReq.getGiaBan());
                ctEntity.setSoLuong(ctReq.getSoLuong());
                ctEntity.setTrangThai(1);
                ctEntity.setNgayTao(Instant.now());
                ctEntity.setNgayCapNhat(Instant.now());

                ChiTietSanPham chiTietDaLuu = chiTietRepo.save(ctEntity);

                if (ctReq.getHinhAnh() != null && !ctReq.getHinhAnh().isEmpty()) {
                    HinhAnhSp anhEntity = new HinhAnhSp();
                    anhEntity.setIdSanPhamChiTiet(chiTietDaLuu);
                    anhEntity.setDuongDanAnh(ctReq.getHinhAnh());
                    anhEntity.setLaAnhChinh(true);
                    anhEntity.setTrangThai(1);
                    hinhAnhRepo.save(anhEntity);
                }
            }
        }
        return toRes(sanPhamDaLuu);
    }

    @Override
    public SanPhamResponse update(Integer id, SanPhamRequest r) {
        SanPham e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm!"));
        if (repo.existsByMaSanPhamAndIdNot(r.getMaSanPham(), id)) {
            throw new RuntimeException("Mã sản phẩm đã được sử dụng!");
        }
        mapFields(e, r);
        e.setNgaySua(Instant.now());
        return toRes(repo.save(e));
    }

    private void mapFields(SanPham e, SanPhamRequest r) {
        if (r.getIdXuatXu() != null) {
            e.setIdXuatXu(xuatXuRepo.findById(r.getIdXuatXu()).orElseThrow());
        } else {
            e.setIdXuatXu(null);
        }
        if (r.getIdThuongHieu() != null) {
            e.setIdThuongHieu(thuongHieuRepo.findById(r.getIdThuongHieu()).orElseThrow());
        } else {
            e.setIdThuongHieu(null);
        }
        e.setMaSanPham(r.getMaSanPham());
        e.setTenSanPham(r.getTenSanPham());
        e.setMoTa(r.getMoTa());
        e.setBaoHanh(r.getBaoHanh());
        e.setTrangThai(r.getTrangThai());
    }

    @Override
    public void delete(Integer id) {
        if (!repo.existsById(id)) throw new RuntimeException("Không tìm thấy sản phẩm cần xóa");
        repo.deleteById(id);
    }

    private String saveFileToDisk(MultipartFile file) throws IOException {
        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}