package com.example.AerionSports_BE.service;

import com.example.AerionSports_BE.dto.ChiTietDotGiamGiaDTO;
import com.example.AerionSports_BE.dto.DotGiamGiaDTO;
import com.example.AerionSports_BE.entity.ChiTietDotGiamGia;
import com.example.AerionSports_BE.entity.ChiTietSanPham;
import com.example.AerionSports_BE.entity.DotGiamGia;
import com.example.AerionSports_BE.repository.ChiTietDotGiamGiaRepository;
import com.example.AerionSports_BE.repository.ChiTietSanPhamRepository;
import com.example.AerionSports_BE.repository.DotGiamGiaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DotGiamGiaService {

    private final DotGiamGiaRepository dotGiamGiaRepository;
    private final ChiTietDotGiamGiaRepository chiTietDotGiamGiaRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
    private static final int TRANG_THAI_DANG_DIEN_RA = 2;

    public Page<DotGiamGiaDTO> getDanhSach(
            String keyword,
            Integer trangThai,
            String tuNgay,
            String denNgay,
            int page,
            int size) {
        validateFilterDateRange(tuNgay, denNgay);

        Pageable pageable = PageRequest.of(page, size);
        Page<DotGiamGia> pageResult = dotGiamGiaRepository.searchDotGiamGia(
                normalizeKeyword(keyword),
                trangThai,
                parseStartOfDay(tuNgay),
                parseEndOfDay(denNgay),
                pageable);

        return pageResult.map(this::toDTO);
    }

    @Transactional
    public DotGiamGiaDTO getById(Integer id) {
        DotGiamGia entity = dotGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));

        DotGiamGiaDTO dto = toDTO(entity);
        List<ChiTietDotGiamGia> chiTietList = chiTietDotGiamGiaRepository.findActiveByDotGiamGiaId(id);
        dto.setChiTietList(chiTietList.stream()
                .map(this::toChiTietDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    @Transactional
    public DotGiamGiaDTO create(DotGiamGiaDTO dto) {
        validateCreateDotGiamGia(dto);

        DotGiamGia entity = new DotGiamGia();
        entity.setMaDotGiamGia(generateMaDotGiamGia());
        entity.setTenDotGiamGia(dto.getTenDotGiamGia());
        entity.setGiaTriGiam(dto.getGiaTriGiam());
        entity.setNgayBatDau(dto.getNgayBatDau());
        entity.setNgayKetThuc(dto.getNgayKetThuc());
        entity.setMoTa(dto.getMoTa());
        entity.setTrangThai(calculateTrangThai(dto.getNgayBatDau(), dto.getNgayKetThuc()));

        DotGiamGia saved = dotGiamGiaRepository.save(entity);
        saveChiTietList(saved, dto.getChiTietList());
        return getById(saved.getId());
    }

    @Transactional
    public DotGiamGiaDTO update(Integer id, DotGiamGiaDTO dto) {
        DotGiamGia entity = dotGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));

        Integer currentTrangThai = resolveTrangThai(entity);
        validateUpdateDotGiamGia(dto, entity, currentTrangThai);
        LocalDateTime ngayBatDau = currentTrangThai == TRANG_THAI_DANG_DIEN_RA
                ? entity.getNgayBatDau()
                : dto.getNgayBatDau();

        entity.setTenDotGiamGia(dto.getTenDotGiamGia());
        entity.setGiaTriGiam(dto.getGiaTriGiam());
        entity.setNgayBatDau(ngayBatDau);
        entity.setNgayKetThuc(dto.getNgayKetThuc());
        entity.setMoTa(dto.getMoTa());
        entity.setTrangThai(calculateTrangThai(entity.getNgayBatDau(), entity.getNgayKetThuc()));

        dotGiamGiaRepository.save(entity);
        chiTietDotGiamGiaRepository.deleteByDotGiamGia_Id(id);
        saveChiTietList(entity, dto.getChiTietList());
        return getById(id);
    }

    @Transactional
    public void delete(Integer id) {
        DotGiamGia entity = dotGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
        entity.setTrangThai(0);
        dotGiamGiaRepository.save(entity);
    }

    @Transactional
    public DotGiamGiaDTO updateTrangThai(Integer id, Integer trangThai) {
        DotGiamGia entity = dotGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
        validateTrangThai(trangThai);
        entity.setTrangThai(trangThai);
        dotGiamGiaRepository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public List<ChiTietDotGiamGiaDTO> getProductsForSelection(String keyword) {
        List<ChiTietSanPham> products = chiTietSanPhamRepository.searchActiveProducts(normalizeKeyword(keyword));
        return products.stream()
                .map(this::toChiTietDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DotGiamGiaDTO getDotGiamGiaHieuLucCaoNhat(Integer chiTietSanPhamId) {
        if (chiTietSanPhamId == null) {
            throw new IllegalArgumentException("ID sản phẩm chi tiết không được để trống");
        }

        List<ChiTietDotGiamGia> activeDiscounts =
                chiTietDotGiamGiaRepository.findBestActiveByChiTietSanPhamId(chiTietSanPhamId, LocalDateTime.now());

        if (activeDiscounts.isEmpty()) {
            return null;
        }

        return toDTO(activeDiscounts.get(0).getDotGiamGia());
    }

    private String generateMaDotGiamGia() {
        Integer maxId = dotGiamGiaRepository.findMaxMaDotGiamGia();
        int nextId = (maxId != null ? maxId : 0) + 1;
        return String.format("DGG%04d", nextId);
    }

    private Integer calculateTrangThai(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        LocalDateTime now = LocalDateTime.now();
        if (ngayBatDau != null && now.isBefore(ngayBatDau)) {
            return 1;
        }
        if (ngayKetThuc != null && now.isAfter(ngayKetThuc)) {
            return 3;
        }
        return 2;
    }

    private Integer resolveTrangThai(DotGiamGia entity) {
        Integer trangThai = entity.getTrangThai();
        if (trangThai != null && (trangThai == 0 || trangThai == 3)) {
            return trangThai;
        }

        return calculateTrangThai(entity.getNgayBatDau(), entity.getNgayKetThuc());
    }

    private void validateTrangThai(Integer trangThai) {
        if (trangThai == null || trangThai < 0 || trangThai > 3) {
            throw new RuntimeException("Trạng thái đợt giảm giá không hợp lệ");
        }
    }

    private String getTrangThaiText(Integer trangThai) {
        if (trangThai == null) {
            return "Không xác định";
        }
        return switch (trangThai) {
            case 0 -> "Đã hủy";
            case 1 -> "Sắp diễn ra";
            case 2 -> "Đang diễn ra";
            case 3 -> "Đã kết thúc";
            default -> "Không xác định";
        };
    }

    private LocalDateTime parseStartOfDay(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value).atStartOfDay();
    }

    private LocalDateTime parseEndOfDay(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return LocalDate.parse(value).atTime(LocalTime.MAX);
    }

    private void validateFilterDateRange(String tuNgay, String denNgay) {
        boolean hasFrom = tuNgay != null && !tuNgay.isBlank();
        boolean hasTo = denNgay != null && !denNgay.isBlank();

        if (hasFrom ^ hasTo) {
            throw new IllegalArgumentException("Vui lòng chọn đủ từ ngày và đến ngày");
        }

        if (hasFrom && hasTo && LocalDate.parse(tuNgay).isAfter(LocalDate.parse(denNgay))) {
            throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc");
        }
    }

    private void validateCreateDotGiamGia(DotGiamGiaDTO dto) {
        validateDotGiamGiaRequired(dto);
        validateStartBeforeEnd(dto.getNgayBatDau(), dto.getNgayKetThuc());
        validateNotBeforeCurrentMinute(dto.getNgayBatDau(), "Ngày bắt đầu");
        validateNotBeforeCurrentMinute(dto.getNgayKetThuc(), "Ngày kết thúc");
    }

    private void validateUpdateDotGiamGia(DotGiamGiaDTO dto, DotGiamGia entity, Integer currentTrangThai) {
        validateDotGiamGiaRequired(dto);

        LocalDateTime effectiveNgayBatDau = currentTrangThai == TRANG_THAI_DANG_DIEN_RA
                ? entity.getNgayBatDau()
                : dto.getNgayBatDau();

        if (currentTrangThai == TRANG_THAI_DANG_DIEN_RA
                && !isSameMinute(dto.getNgayBatDau(), entity.getNgayBatDau())) {
            throw new RuntimeException("Đợt giảm giá đang diễn ra không được sửa ngày bắt đầu");
        }

        validateStartBeforeEnd(effectiveNgayBatDau, dto.getNgayKetThuc());

        if (currentTrangThai != TRANG_THAI_DANG_DIEN_RA) {
            validateNotBeforeCurrentMinute(dto.getNgayBatDau(), "Ngày bắt đầu");
        }
        validateNotBeforeCurrentMinute(dto.getNgayKetThuc(), "Ngày kết thúc");
    }

    private void validateDotGiamGiaRequired(DotGiamGiaDTO dto) {
        if (dto == null) {
            throw new RuntimeException("Dữ liệu đợt giảm giá không hợp lệ");
        }
        if (dto.getTenDotGiamGia() == null || dto.getTenDotGiamGia().isBlank()) {
            throw new RuntimeException("Tên đợt giảm giá không được để trống");
        }
        if (dto.getGiaTriGiam() == null) {
            throw new RuntimeException("Giá trị giảm không được để trống");
        }
        validateGiaTriGiamPercent(dto.getGiaTriGiam());
        if (dto.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống");
        }
        if (dto.getNgayKetThuc() == null) {
            throw new RuntimeException("Ngày kết thúc không được để trống");
        }
    }

    private void validateStartBeforeEnd(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        if (!ngayBatDau.isBefore(ngayKetThuc)) {
            throw new RuntimeException("Ngày bắt đầu phải trước ngày kết thúc");
        }
    }

    private void validateNotBeforeCurrentMinute(LocalDateTime value, String fieldName) {
        if (value.truncatedTo(ChronoUnit.MINUTES).isBefore(currentMinute())) {
            throw new RuntimeException(fieldName + " không được chọn thời gian trong quá khứ");
        }
    }

    private LocalDateTime currentMinute() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    private boolean isSameMinute(LocalDateTime left, LocalDateTime right) {
        return left != null
                && right != null
                && left.truncatedTo(ChronoUnit.MINUTES).isEqual(right.truncatedTo(ChronoUnit.MINUTES));
    }

    private void validateGiaTriGiamPercent(BigDecimal giaTriGiam) {
        if (giaTriGiam.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Giá trị giảm phải lớn hơn 0%");
        }
        if (giaTriGiam.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new RuntimeException("Giá trị giảm không được vượt quá 100%");
        }
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }

        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void saveChiTietList(DotGiamGia dotGiamGia, List<ChiTietDotGiamGiaDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return;
        }

        List<Integer> ids = dtoList.stream()
                .map(ChiTietDotGiamGiaDTO::getIdChiTietSanPham)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (ids.isEmpty()) {
            return;
        }

        Map<Integer, ChiTietSanPham> productsById = chiTietSanPhamRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(ChiTietSanPham::getId, product -> product, (left, right) -> left, LinkedHashMap::new));

        List<ChiTietDotGiamGia> entities = new ArrayList<>();
        for (Integer productId : ids) {
            ChiTietSanPham ctsp = productsById.get(productId);
            if (ctsp == null) {
                throw new RuntimeException("Không tìm thấy sản phẩm chi tiết với ID: " + productId);
            }

            ChiTietDotGiamGia ct = new ChiTietDotGiamGia();
            ct.setDotGiamGia(dotGiamGia);
            ct.setChiTietSanPham(ctsp);
            entities.add(ct);
        }

        chiTietDotGiamGiaRepository.saveAll(entities);
    }

    private DotGiamGiaDTO toDTO(DotGiamGia entity) {
        DotGiamGiaDTO dto = new DotGiamGiaDTO();
        dto.setId(entity.getId());
        dto.setMaDotGiamGia(entity.getMaDotGiamGia());
        dto.setTenDotGiamGia(entity.getTenDotGiamGia());
        dto.setGiaTriGiam(entity.getGiaTriGiam());
        dto.setNgayBatDau(entity.getNgayBatDau());
        dto.setNgayKetThuc(entity.getNgayKetThuc());
        dto.setMoTa(entity.getMoTa());
        dto.setTrangThai(resolveTrangThai(entity));
        dto.setTrangThaiText(getTrangThaiText(dto.getTrangThai()));
        return dto;
    }

    private ChiTietDotGiamGiaDTO toChiTietDTO(ChiTietDotGiamGia entity) {
        ChiTietDotGiamGiaDTO dto = toChiTietDTO(entity.getChiTietSanPham());
        dto.setId(entity.getId());
        dto.setIdDotGiamGia(entity.getDotGiamGia() != null ? entity.getDotGiamGia().getId() : null);
        return dto;
    }

    private ChiTietDotGiamGiaDTO toChiTietDTO(ChiTietSanPham ctsp) {
        ChiTietDotGiamGiaDTO dto = new ChiTietDotGiamGiaDTO();
        if (ctsp == null) {
            return dto;
        }

        String tenChuVi = ctsp.getIdChuViCanVot() != null ? ctsp.getIdChuViCanVot().getTenChuViCanVot() : null;
        String tenThuongHieu = ctsp.getIdSanPham() != null && ctsp.getIdSanPham().getIdThuongHieu() != null
                ? ctsp.getIdSanPham().getIdThuongHieu().getTenThuongHieu()
                : null;
        String tenXuatXu = ctsp.getIdSanPham() != null && ctsp.getIdSanPham().getIdXuatXu() != null
                ? ctsp.getIdSanPham().getIdXuatXu().getTenXuatXu()
                : null;

        dto.setId(ctsp.getId());
        dto.setIdChiTietSanPham(ctsp.getId());
        dto.setMaCtsp(ctsp.getMaCtsp());
        dto.setMaSanPham(ctsp.getMaCtsp());
        dto.setTenSanPham(ctsp.getIdSanPham() != null ? ctsp.getIdSanPham().getTenSanPham() : null);
        dto.setTenThuongHieu(tenThuongHieu);
        dto.setAnhDaiDien(null);
        dto.setTenMauSac(ctsp.getIdMauSac() != null ? ctsp.getIdMauSac().getTenMauSac() : null);
        dto.setTenTrongLuong(ctsp.getIdTrongLuong() != null ? ctsp.getIdTrongLuong().getTenTrongLuong() : null);
        dto.setTenChuViCanVot(tenChuVi);
        dto.setChuViCanVot(tenChuVi);
        dto.setTenDoCung(ctsp.getIdDoCung() != null ? ctsp.getIdDoCung().getTenDoCung() : null);
        dto.setTenDiemCanBang(ctsp.getIdDiemCanBang() != null ? ctsp.getIdDiemCanBang().getTenDiemCanBang() : null);
        dto.setTenChatLieuThanVot(
                ctsp.getIdChatLieuThanVot() != null ? ctsp.getIdChatLieuThanVot().getTenChatLieuThanVot() : null);
        dto.setTenChatLieuKhungVot(
                ctsp.getIdChatLieuKhungVot() != null ? ctsp.getIdChatLieuKhungVot().getTenChatLieuKhungVot() : null);
        dto.setTenDanhMuc(ctsp.getIdDanhMuc() != null ? ctsp.getIdDanhMuc().getTenDanhMuc() : null);
        dto.setTenXuatXu(tenXuatXu);
        dto.setXuatXuChiTiet(tenXuatXu);
        dto.setGiaNhap(ctsp.getGiaNhap());
        dto.setGiaBan(ctsp.getGiaBan());
        dto.setSoLuong(ctsp.getSoLuong());
        dto.setSoLuongTon(ctsp.getSoLuong());
        dto.setTrangThai(ctsp.getTrangThai());
        dto.setGiaTriGiamRieng(null);
        dto.setGhiChu(null);
        return dto;
    }
}

