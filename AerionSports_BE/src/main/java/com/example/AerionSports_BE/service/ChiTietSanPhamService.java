package com.example.AerionSports_BE.service;

import com.example.AerionSports_BE.dto.request.ChiTietSanPhamFilter;
import com.example.AerionSports_BE.dto.request.ChiTietSanPhamRequest;
import com.example.AerionSports_BE.dto.response.ChiTietSanPhamResponse;
import com.example.AerionSports_BE.dto.response.SanPhamResponse;
import com.example.AerionSports_BE.entity.*;
import com.example.AerionSports_BE.repository.ChiTietSanPhamRepository;
import com.example.AerionSports_BE.repository.ChiTietDotGiamGiaRepository; // ⚡ THÊM IMPORT
import com.example.AerionSports_BE.service.impl.IChiTietSanPhamService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
public class ChiTietSanPhamService implements IChiTietSanPhamService {

    @Autowired
    private ChiTietSanPhamRepository repo;

    // ⚡ TIÊM REPOSITORY ĐỂ TÍNH ĐỢT GIẢM GIÁ
    @Autowired
    private ChiTietDotGiamGiaRepository chiTietDotGiamGiaRepository;

    private ChiTietSanPhamResponse toRes(ChiTietSanPham e) {
        if (e == null) return null;

        String duongDanAnhThucTe = null;
        if (e.getHinhAnhs() != null && !e.getHinhAnhs().isEmpty()) {
            duongDanAnhThucTe = e.getHinhAnhs().stream()
                    .filter(anh -> anh.getLaAnhChinh() != null && anh.getLaAnhChinh())
                    .map(HinhAnhSp::getDuongDanAnh)
                    .findFirst()
                    .orElse(e.getHinhAnhs().get(0).getDuongDanAnh());
        }

        Integer idSanPhamCha = null;
        String maSanPhamCha = null;
        if (e.getIdSanPham() != null) {
            idSanPhamCha = e.getIdSanPham().getId();
            maSanPhamCha = e.getIdSanPham().getMaSanPham();
        }

        // Tạo DTO rỗng gán Setter an toàn
        ChiTietSanPhamResponse dto = new ChiTietSanPhamResponse();
        dto.setId(e.getId());
        dto.setIdSanPham(idSanPhamCha);
        dto.setMaSanPham(maSanPhamCha);
        dto.setMaCtsp(e.getMaCtsp());
        dto.setTenMauSac(e.getIdMauSac() != null ? e.getIdMauSac().getTenMauSac() : null);
        dto.setTenTrongLuong(e.getIdTrongLuong() != null ? e.getIdTrongLuong().getTenTrongLuong() : null);
        dto.setTenChuViCanVot(e.getIdChuViCanVot() != null ? e.getIdChuViCanVot().getTenChuViCanVot() : null);
        dto.setTenDoCung(e.getIdDoCung() != null ? e.getIdDoCung().getTenDoCung() : null);
        dto.setTenDiemCanBang(e.getIdDiemCanBang() != null ? e.getIdDiemCanBang().getTenDiemCanBang() : null);
        dto.setTenChatLieuThanVot(e.getIdChatLieuThanVot() != null ? e.getIdChatLieuThanVot().getTenChatLieuThanVot() : null);
        dto.setTenChatLieuKhungVot(e.getIdChatLieuKhungVot() != null ? e.getIdChatLieuKhungVot().getTenChatLieuKhungVot() : null);
        dto.setTenDanhMuc(e.getIdDanhMuc() != null ? e.getIdDanhMuc().getTenDanhMuc() : null);
        dto.setGiaNhap(e.getGiaNhap());
        dto.setGiaBan(e.getGiaBan());
        dto.setSoLuong(e.getSoLuong());
        dto.setTrangThai(e.getTrangThai());
        dto.setNgayTao(e.getNgayTao());
        dto.setNgayCapNhat(e.getNgayCapNhat());
        dto.setHinhAnh(duongDanAnhThucTe);

        // ⚡ THUẬT TOÁN TỰ ĐỘNG BỐC % GIẢM GIÁ VÀ TÍNH TOÁN GIÁ THỰC TẾ TRẢ VỀ CHO VUE
        java.math.BigDecimal phanTramGiam = java.math.BigDecimal.ZERO;
        java.math.BigDecimal giaDaGiam = e.getGiaBan();

        // Lấy giờ hiện tại chuẩn theo múi giờ Việt Nam
        LocalDateTime gioHienTaiVN = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));

        // Gọi câu lệnh Query tinh gọn từ Repository vừa sửa của bạn
        List<ChiTietDotGiamGia> discountLinks = chiTietDotGiamGiaRepository.findBestActiveByChiTietSanPhamId(e.getId(), gioHienTaiVN);

        if (discountLinks != null && !discountLinks.isEmpty()) {
            DotGiamGia dgg = discountLinks.get(0).getDotGiamGia();
            if (dgg != null && dgg.getGiaTriGiam() != null) {
                phanTramGiam = dgg.getGiaTriGiam();
                // Công thức: Giá sau giảm = Giá gốc * (100 - %Giảm) / 100
                java.math.BigDecimal heSo = java.math.BigDecimal.valueOf(100).subtract(phanTramGiam);
                giaDaGiam = e.getGiaBan().multiply(heSo).divide(java.math.BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
            }
        }

        // Gán 2 trường giá trị tính toán thực tế ra ngoài giao diện
        dto.setGiaDaGiam(giaDaGiam);
        dto.setPhanTramGiam(phanTramGiam);

        return dto;
    }

    @Override
    public List<ChiTietSanPhamResponse> getAll() {
        return repo.findByTrangThai(1).stream()
                .map(this::toRes)
                .toList();
    }

    @Override
    public Page<ChiTietSanPhamResponse> search(ChiTietSanPhamFilter f) {
        return repo.search(f.getKeyword(), f.getIdSanPham(), f.getIdDanhMuc(), f.getIdMauSac(),
                f.getIdTrongLuong(), f.getIdChuViCanVot(), f.getIdDoCung(), f.getIdDiemCanBang(), f.getTrangThai(),
                f.getGiaTu(), f.getGiaDen(), PageRequest.of(f.getPage(), f.getSize())).map(this::toRes);
    }

    @Override
    public ChiTietSanPhamResponse save(ChiTietSanPhamRequest r) {
        if (repo.existsByMaCtsp(r.getMaCtsp())) throw new RuntimeException("Mã CTSP này đã tồn tại!");
        ChiTietSanPham e = new ChiTietSanPham();
        mapFields(e, r);
        e.setNgayTao(Instant.now());
        e.setNgayCapNhat(Instant.now());
        return toRes(repo.save(e));
    }

    @Override
    public ChiTietSanPhamResponse update(Integer id, ChiTietSanPhamRequest r) {
        ChiTietSanPham e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể sản phẩm!"));
        mapFields(e, r);
        e.setNgayCapNhat(Instant.now());
        return toRes(repo.save(e));
    }

    private void mapFields(ChiTietSanPham e, ChiTietSanPhamRequest r) {
        SanPham sp = new SanPham(); sp.setId(r.getIdSanPham()); e.setIdSanPham(sp);

        if (r.getIdMauSac() != null) { MauSac m = new MauSac(); m.setId(r.getIdMauSac()); e.setIdMauSac(m); }
        if (r.getIdTrongLuong() != null) { TrongLuong t = new TrongLuong(); t.setId(r.getIdTrongLuong()); e.setIdTrongLuong(t); }
        if (r.getIdChuViCanVot() != null) { ChuViCanVot cv = new ChuViCanVot(); cv.setId(r.getIdChuViCanVot()); e.setIdChuViCanVot(cv); }
        if (r.getIdDoCung() != null) { DoCung d = new DoCung(); d.setId(r.getIdDoCung()); e.setIdDoCung(d); }
        if (r.getIdDiemCanBang() != null) { DiemCanBang dcb = new DiemCanBang(); dcb.setId(r.getIdDiemCanBang()); e.setIdDiemCanBang(dcb); }
        if (r.getIdChatLieuThanVot() != null) { ChatLieuThanVot tv = new ChatLieuThanVot(); tv.setId(r.getIdChatLieuThanVot()); e.setIdChatLieuThanVot(tv); }
        if (r.getIdChatLieuKhungVot() != null) { ChatLieuKhungVot kv = new ChatLieuKhungVot(); kv.setId(r.getIdChatLieuKhungVot()); e.setIdChatLieuKhungVot(kv); }
        if (r.getIdDanhMuc() != null) { DanhMuc dm = new DanhMuc(); dm.setId(r.getIdDanhMuc()); e.setIdDanhMuc(dm); }

        e.setMaCtsp(r.getMaCtsp());
        e.setGiaNhap(r.getGiaNhap());
        e.setGiaBan(r.getGiaBan());
        e.setSoLuong(r.getSoLuong());
        e.setTrangThai(r.getTrangThai());
    }

    @Override
    public void delete(Integer id) {
        ChiTietSanPham e = repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy!"));
        e.setTrangThai(0);
        repo.save(e);
    }

    @Override
    public void updateTrangThai(Integer id, Integer trangThai) {
        ChiTietSanPham e = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể sản phẩm!"));
        e.setTrangThai(trangThai);
        repo.save(e);
    }

    @Override
    public List<SanPhamResponse> getAllProductsWithVariantsForCheck() {
        // 1. Lấy mảng phẳng toàn bộ CTSP hoạt động từ Repository (Đã có JOIN FETCH sang Sản phẩm cha)
        List<ChiTietSanPham> listFlat = repo.searchActiveProducts(null);

        // 2. Gom nhóm các chi tiết sản phẩm dựa theo ID của Sản phẩm cha bằng Map
        java.util.Map<SanPham, List<ChiTietSanPham>> groupMap = listFlat.stream()
                .filter(ctsp -> ctsp.getIdSanPham() != null)
                .collect(java.util.stream.Collectors.groupingBy(ChiTietSanPham::getIdSanPham));

        // 3. Ánh xạ trực tiếp từ Thực thể Sản phẩm cha (SanPham) sang DTO SanPhamResponse của bạn
        return groupMap.entrySet().stream().map(entry -> {
            SanPham spChaEntity = entry.getKey(); // Đây chính là thực thể Sản phẩm cha chứa Thương hiệu & Xuất xứ
            List<ChiTietSanPham> ctspList = entry.getValue();

            // Chuyển đổi list thực thể con sang list DTO ChiTietSanPhamResponse bằng hàm toRes gốc của bạn
            List<ChiTietSanPhamResponse> listVariantsDto = ctspList.stream().map(this::toRes).toList();

            // Khởi tạo DTO SanPhamResponse chuẩn chỉnh của bạn
            SanPhamResponse parentDto = new SanPhamResponse();

            // 🌟 ĐÚNG CHUẨN: Bốc trực tiếp từ bảng Sản phẩm cha giống hệt logic database của bạn
            parentDto.setId(spChaEntity.getId());
            parentDto.setMaSanPham(spChaEntity.getMaSanPham());
            parentDto.setTenSanPham(spChaEntity.getTenSanPham());
            parentDto.setMoTa(spChaEntity.getMoTa());
            parentDto.setBaoHanh(spChaEntity.getBaoHanh());
            parentDto.setTrangThai(spChaEntity.getTrangThai());
            parentDto.setNgayTao(spChaEntity.getNgayTao());
            parentDto.setNgaySua(spChaEntity.getNgaySua());

            // Gán ID và Tên Thương hiệu từ liên kết của bảng Sản phẩm
            if (spChaEntity.getIdThuongHieu() != null) {
                parentDto.setIdThuongHieu(spChaEntity.getIdThuongHieu().getId());
                parentDto.setTenThuongHieu(spChaEntity.getIdThuongHieu().getTenThuongHieu());
            }

            // Gán ID và Tên Xuất xứ từ liên kết của bảng Sản phẩm
            if (spChaEntity.getIdXuatXu() != null) {
                parentDto.setIdXuatXu(spChaEntity.getIdXuatXu().getId());
                parentDto.setTenXuatXu(spChaEntity.getIdXuatXu().getTenXuatXu());
            }

            // Đổ mảng biến thể con vào tập hợp Set của SanPhamResponse
            parentDto.setChiTietSanPhams(new java.util.HashSet<>(listVariantsDto));

            return parentDto;
        }).toList();
    }
}