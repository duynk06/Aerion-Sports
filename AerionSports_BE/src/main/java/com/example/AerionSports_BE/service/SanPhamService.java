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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
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

    @Autowired
    private ChiTietDotGiamGiaRepository chiTietDotGiamGiaRepository;


    @Transactional
    public SanPhamResponse createProductWithVariants(String dataJson, List<MultipartFile> files) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SanPhamRequest request = mapper.readValue(dataJson, SanPhamRequest.class);

        if (repo.existsByMaSanPham(request.getMaSanPham())) {
            throw new RuntimeException("Mã sản phẩm cha đã tồn tại trên hệ thống!");
        }

        SanPham sanPhamEntity = new SanPham();
        mapFields(sanPhamEntity, request);
        sanPhamEntity.setNgayTao(Instant.now());
        sanPhamEntity.setNgaySua(Instant.now());
        SanPham savedSanPham = repo.save(sanPhamEntity);

        int fileIndex = 0;
        for (ChiTietSanPhamRequest ctRequest : request.getChiTietSanPhams()) {
            ChiTietSanPham ctspEntity = new ChiTietSanPham();
            ctspEntity.setIdSanPham(savedSanPham);
            ctspEntity.setMaCtsp(ctRequest.getMaCtsp());

            if (ctRequest.getIdMauSac() != null) ctspEntity.setIdMauSac(mauSacRepo.findById(ctRequest.getIdMauSac()).orElse(null));
            if (ctRequest.getIdTrongLuong() != null) ctspEntity.setIdTrongLuong(trongLuongRepo.findById(ctRequest.getIdTrongLuong()).orElse(null));
            if (ctRequest.getIdDoCung() != null) ctspEntity.setIdDoCung(doCungRepo.findById(ctRequest.getIdDoCung()).orElse(null));
            if (ctRequest.getIdChatLieuThanVot() != null) ctspEntity.setIdChatLieuThanVot(chatLieuThanRepo.findById(ctRequest.getIdChatLieuThanVot()).orElse(null));
            if (ctRequest.getIdChatLieuKhungVot() != null) ctspEntity.setIdChatLieuKhungVot(chatLieuKhungRepo.findById(ctRequest.getIdChatLieuKhungVot()).orElse(null));
            if (ctRequest.getIdDanhMuc() != null) ctspEntity.setIdDanhMuc(danhMucRepo.findById(ctRequest.getIdDanhMuc()).orElse(null));
            if (ctRequest.getIdDiemCanBang() != null) ctspEntity.setIdDiemCanBang(diemCanBangRepo.findById(ctRequest.getIdDiemCanBang()).orElse(null));
            if (ctRequest.getIdChuViCanVot() != null) ctspEntity.setIdChuViCanVot(chuViCanVotRepo.findById(ctRequest.getIdChuViCanVot()).orElse(null));

            ctspEntity.setGiaNhap(ctRequest.getGiaNhap());
            ctspEntity.setGiaBan(ctRequest.getGiaBan());
            ctspEntity.setSoLuong(ctRequest.getSoLuong());
            ctspEntity.setTrangThai(1);
            ctspEntity.setNgayTao(Instant.now());
            ctspEntity.setNgayCapNhat(Instant.now());

            ChiTietSanPham savedCtsp = chiTietRepo.save(ctspEntity);

            if (files != null && fileIndex < files.size()) {
                MultipartFile currentFile = files.get(fileIndex);
                if (currentFile != null && !currentFile.isEmpty()) {
                    String savedFileName = saveFileToDisk(currentFile);
                    String dbImagePath = "/uploads/" + savedFileName;

                    HinhAnhSp hinhAnhEntity = new HinhAnhSp();
                    hinhAnhEntity.setIdSanPhamChiTiet(savedCtsp);
                    hinhAnhEntity.setLaAnhChinh(true);
                    hinhAnhEntity.setDuongDanAnh(dbImagePath);
                    hinhAnhEntity.setTrangThai(1);

                    hinhAnhRepo.save(hinhAnhEntity);
                }
                fileIndex++;
            }
        }

        return toRes(savedSanPham);
    }

    private ChiTietSanPhamResponse toChiTietRes(ChiTietSanPham ct) {
        if (ct == null) return null;

        // 1. Logic bốc ảnh đại diện chứa UUID từ bảng hinh_anh_sp
        String duongDanAnhThucTe = null;
        if (ct.getHinhAnhs() != null && !ct.getHinhAnhs().isEmpty()) {
            duongDanAnhThucTe = ct.getHinhAnhs().stream()
                    .filter(HinhAnhSp::getLaAnhChinh)
                    .map(HinhAnhSp::getDuongDanAnh)
                    .findFirst()
                    .orElse(ct.getHinhAnhs().iterator().next().getDuongDanAnh());
        }

        // 2. Logic bốc mã và ID từ thực thể cha tránh Lazy Loading
        Integer idSanPhamCha = null;
        String maSanPhamCha = null;
        if (ct.getIdSanPham() != null) {
            idSanPhamCha = ct.getIdSanPham().getId();
            maSanPhamCha = ct.getIdSanPham().getMaSanPham();
        }

        // ⚡ 3. THUẬT TOÁN TỰ ĐỘNG TÍNH GIÁ GIẢM ĐỒNG BỘ ĐỢT GIẢM GIÁ
        java.math.BigDecimal phanTramGiam = java.math.BigDecimal.ZERO;
        java.math.BigDecimal giaDaGiam = ct.getGiaBan();

        // 🔥 ĐÃ SỬA: Ép chuẩn múi giờ Việt Nam (Asia/Ho_Chi_Minh) để tránh lệch múi giờ với Database
        java.time.LocalDateTime gioHienTaiVietNam = java.time.LocalDateTime.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));

        // Log nhanh để bạn check giờ chạy thực tế của Backend trong Console IntelliJ
        System.out.println("⏱️ Giờ hệ thống Backend đang quét discount: " + gioHienTaiVietNam + " | Kiểm tra CTSP ID: " + ct.getId());

        // Truyền biến 'gioHienTaiVietNam' thay vì LocalDateTime.now() thô sơ cũ
        List<com.example.AerionSports_BE.entity.ChiTietDotGiamGia> discountLinks =
                chiTietDotGiamGiaRepository.findBestActiveByChiTietSanPhamId(ct.getId(), gioHienTaiVietNam);

        if (discountLinks != null && !discountLinks.isEmpty()) {
            com.example.AerionSports_BE.entity.DotGiamGia dgg = discountLinks.get(0).getDotGiamGia();
            if (dgg != null && dgg.getGiaTriGiam() != null) {
                phanTramGiam = dgg.getGiaTriGiam();
                System.out.println("🎉 Khớp đợt giảm giá thành công! Mã: " + dgg.getMaDotGiamGia() + " | Giảm: " + phanTramGiam + "%");

                java.math.BigDecimal heSo = java.math.BigDecimal.valueOf(100).subtract(phanTramGiam);
                giaDaGiam = ct.getGiaBan().multiply(heSo).divide(java.math.BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
            }
        } else {
            System.out.println("❌ Không tìm thấy đợt giảm giá nào có hiệu lực cho CTSP ID: " + ct.getId() + " vào lúc này.");
        }

        // ⚡ 4. GIẢI PHÁP AN TOÀN: Khởi tạo DTO rỗng và gán Setter để loại bỏ hoàn toàn lỗi Constructor
        ChiTietSanPhamResponse res = new ChiTietSanPhamResponse();

        res.setId(ct.getId());
        res.setIdSanPham(idSanPhamCha);
        res.setMaSanPham(maSanPhamCha);
        res.setMaCtsp(ct.getMaCtsp());
        res.setTenMauSac(ct.getIdMauSac() != null ? ct.getIdMauSac().getTenMauSac() : null);
        res.setTenTrongLuong(ct.getIdTrongLuong() != null ? ct.getIdTrongLuong().getTenTrongLuong() : null);
        res.setTenChuViCanVot(ct.getIdChuViCanVot() != null ? ct.getIdChuViCanVot().getTenChuViCanVot() : null);
        res.setTenDoCung(ct.getIdDoCung() != null ? ct.getIdDoCung().getTenDoCung() : null);
        res.setTenDiemCanBang(ct.getIdDiemCanBang() != null ? ct.getIdDiemCanBang().getTenDiemCanBang() : null);
        res.setTenChatLieuThanVot(ct.getIdChatLieuThanVot() != null ? ct.getIdChatLieuThanVot().getTenChatLieuThanVot() : null);
        res.setTenChatLieuKhungVot(ct.getIdChatLieuKhungVot() != null ? ct.getIdChatLieuKhungVot().getTenChatLieuKhungVot() : null);
        res.setTenDanhMuc(ct.getIdDanhMuc() != null ? ct.getIdDanhMuc().getTenDanhMuc() : null);
        res.setGiaNhap(ct.getGiaNhap());
        res.setGiaBan(ct.getGiaBan());
        res.setSoLuong(ct.getSoLuong());
        res.setTrangThai(ct.getTrangThai());
        res.setNgayTao(ct.getNgayTao());
        res.setNgayCapNhat(ct.getNgayCapNhat());
        res.setHinhAnh(duongDanAnhThucTe);

        res.setGiaDaGiam(giaDaGiam);
        res.setPhanTramGiam(phanTramGiam);

        return res;
    }

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

    @Transactional
    @Override
    public SanPhamResponse update(Integer id, SanPhamRequest r) {
        SanPham e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm!"));
        if (repo.existsByMaSanPhamAndIdNot(r.getMaSanPham(), id)) {
            throw new RuntimeException("Mã sản phẩm đã được sử dụng!");
        }
        mapFields(e, r);
        SanPham savedSanPham = repo.save(e);

        if (r.getChiTietSanPhams() != null) {
            for (ChiTietSanPhamRequest ctReq : r.getChiTietSanPhams()) {
                ChiTietSanPham ctEntity = chiTietRepo.findByMaCtsp(ctReq.getMaCtsp()).orElse(new ChiTietSanPham());

                ctEntity.setIdSanPham(savedSanPham);
                ctEntity.setMaCtsp(ctReq.getMaCtsp());
                ctEntity.setGiaNhap(ctReq.getGiaNhap());
                ctEntity.setGiaBan(ctReq.getGiaBan());
                ctEntity.setSoLuong(ctReq.getSoLuong());
                ctEntity.setNgayCapNhat(Instant.now());

                chiTietRepo.save(ctEntity);
            }
        }

        return toRes(savedSanPham);
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
        String uploadDir = "C:/Users/ADMIN/OneDrive/Desktop/Tong-hop-fe/Aerion-Sports/public/uploads/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @Transactional
    public void updateSingleVariantWithImage(Integer idCtsp, ChiTietSanPhamRequest req, MultipartFile file) throws IOException {
        ChiTietSanPham ct = chiTietRepo.findById(idCtsp)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể con!"));

        ct.setGiaNhap(req.getGiaNhap());
        ct.setGiaBan(req.getGiaBan());
        ct.setSoLuong(req.getSoLuong());
        ct.setNgayCapNhat(Instant.now());
        ChiTietSanPham savedCt = chiTietRepo.save(ct);

        if (file != null && !file.isEmpty()) {
            String fileName = saveFileToDisk(file);
            String dbImagePath = "/uploads/" + fileName;

            HinhAnhSp anhEntity = hinhAnhRepo.findAll().stream()
                    .filter(anh -> anh.getIdSanPhamChiTiet() != null && anh.getIdSanPhamChiTiet().getId().equals(idCtsp))
                    .findFirst()
                    .orElse(new HinhAnhSp());

            anhEntity.setIdSanPhamChiTiet(savedCt);
            anhEntity.setDuongDanAnh(dbImagePath);
            anhEntity.setLaAnhChinh(true);
            anhEntity.setTrangThai(1);

            hinhAnhRepo.save(anhEntity);
        }
    }
}