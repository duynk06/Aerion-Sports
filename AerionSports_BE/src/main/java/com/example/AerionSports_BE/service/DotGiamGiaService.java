//package com.example.AerionSports_BE.service;
//
//import com.example.AerionSports_BE.dto.ChiTietDotGiamGiaDTO;
//import com.example.AerionSports_BE.dto.DotGiamGiaDTO;
//import com.example.AerionSports_BE.entity.ChiTietDotGiamGia;
//import com.example.AerionSports_BE.entity.ChiTietSanPham;
//import com.example.AerionSports_BE.entity.DotGiamGia;
//import com.example.AerionSports_BE.repository.ChiTietDotGiamGiaRepository;
//import com.example.AerionSports_BE.repository.ChiTietSanPhamRepository;
//import com.example.AerionSports_BE.repository.DotGiamGiaRepository;
//import com.example.AerionSports_BE.repository.HinhAnhSpRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class DotGiamGiaService {
//
//    private final DotGiamGiaRepository dotGiamGiaRepository;
//    private final ChiTietDotGiamGiaRepository chiTietDotGiamGiaRepository;
//    private final ChiTietSanPhamRepository chiTietSanPhamRepository;
//    private final HinhAnhSpRepository hinhAnhSpRepository;
//
//    // ==================== CRUD đợt giảm giá ====================
//
//    /**
//     * Lấy danh sách đợt giảm giá có phân trang và tìm kiếm
//     */
//    public Page<DotGiamGiaDTO> getDanhSach(
//            String keyword,
//            Integer trangThai,
//            String tuNgay,
//            String denNgay,
//            int page,
//            int size) {
//        validateFilterDateRange(tuNgay, denNgay);
//
//        Pageable pageable = PageRequest.of(page, size);
//        Page<DotGiamGia> pageResult = dotGiamGiaRepository.searchDotGiamGia(
//                keyword,
//                trangThai,
//                parseStartOfDay(tuNgay),
//                parseEndOfDay(denNgay),
//                pageable);
//        return pageResult.map(this::toDTO);
//    }
//
//    /**
//     * Lấy chi tiết đợt giảm giá theo ID
//     */
//    public DotGiamGiaDTO getById(Integer id) {
//        DotGiamGia entity = dotGiamGiaRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
//        DotGiamGiaDTO dto = toDTO(entity);
//
//        // Lấy danh sách ID trước, rồi fetch sản phẩm chi tiết bằng query riêng để tránh lazy proxy
//        List<Integer> chiTietSanPhamIds = chiTietDotGiamGiaRepository.findChiTietSanPhamIdsByDotGiamGiaId(id);
//        List<ChiTietSanPham> chiTietSanPhamList = chiTietSanPhamIds.isEmpty()
//                ? List.of()
//                : chiTietSanPhamRepository.findProductsWithDetailsByIds(chiTietSanPhamIds);
//        dto.setChiTietList(chiTietSanPhamList.stream()
//                .map(ctsp -> toChiTietDTO(ctsp, null, null))
//                .collect(Collectors.toList()));
//
//        return dto;
//    }
//
//    /**
//     * Tạo mới đợt giảm giá
//     */
//    @Transactional
//    public DotGiamGiaDTO create(DotGiamGiaDTO dto) {
//        // Validate
//        validateDotGiamGia(dto, null);
//
//        DotGiamGia entity = new DotGiamGia();
//        entity.setMaDotGiamGia(generateMaDotGiamGia());
//        entity.setTenDotGiamGia(dto.getTenDotGiamGia());
//        entity.setGiaTriGiam(dto.getGiaTriGiam());
//        entity.setNgayBatDau(dto.getNgayBatDau());
//        entity.setNgayKetThuc(dto.getNgayKetThuc());
//        entity.setMoTa(dto.getMoTa());
//        entity.setTrangThai(calculateTrangThai(dto.getNgayBatDau(), dto.getNgayKetThuc()));
//
//        DotGiamGia saved = dotGiamGiaRepository.save(entity);
//
//        // Lưu chi tiết sản phẩm áp dụng
//        if (dto.getChiTietList() != null && !dto.getChiTietList().isEmpty()) {
//            saveChiTietList(saved, dto.getChiTietList());
//        }
//
//        return getById(saved.getId());
//    }
//
//    /**
//     * Cập nhật đợt giảm giá
//     */
//    @Transactional
//    public DotGiamGiaDTO update(Integer id, DotGiamGiaDTO dto) {
//        DotGiamGia entity = dotGiamGiaRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
//
//        // Validate
//        validateDotGiamGia(dto, id);
//
//        entity.setTenDotGiamGia(dto.getTenDotGiamGia());
//        entity.setGiaTriGiam(dto.getGiaTriGiam());
//        entity.setNgayBatDau(dto.getNgayBatDau());
//        entity.setNgayKetThuc(dto.getNgayKetThuc());
//        entity.setMoTa(dto.getMoTa());
//        entity.setTrangThai(calculateTrangThai(dto.getNgayBatDau(), dto.getNgayKetThuc()));
//
//        dotGiamGiaRepository.save(entity);
//
//        // Xóa chi tiết cũ và lưu chi tiết mới
//        chiTietDotGiamGiaRepository.deleteByDotGiamGiaId(id);
//        if (dto.getChiTietList() != null && !dto.getChiTietList().isEmpty()) {
//            saveChiTietList(entity, dto.getChiTietList());
//        }
//
//        return getById(id);
//    }
//
//    /**
//     * Xóa mềm đợt giảm giá (chuyển trạng thái = 0)
//     */
//    @Transactional
//    public void delete(Integer id) {
//        DotGiamGia entity = dotGiamGiaRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
//        entity.setTrangThai(0); // Hủy
//        dotGiamGiaRepository.save(entity);
//    }
//
//    /**
//     * Cập nhật trạng thái đợt giảm giá
//     */
//    @Transactional
//    public DotGiamGiaDTO updateTrangThai(Integer id, Integer trangThai) {
//        DotGiamGia entity = dotGiamGiaRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
//        entity.setTrangThai(trangThai);
//        dotGiamGiaRepository.save(entity);
//        return toDTO(entity);
//    }
//
//    // ==================== Sản phẩm cho chọn áp dụng ====================
//
//    /**
//     * Lấy danh sách sản phẩm chi tiết để chọn áp dụng giảm giá
//     */
//    public List<ChiTietDotGiamGiaDTO> getProductsForSelection(String keyword) {
//        List<ChiTietSanPham> products = chiTietSanPhamRepository.searchActiveProducts(keyword);
//        Map<Integer, String> mainImagesByProductId = loadMainImages(products);
//        LocalDateTime now = LocalDateTime.now();
//        Map<Integer, BigDecimal> bestDiscountByProductId = loadBestActiveDiscounts(products, now);
//
//        return products.stream()
//                .map(ctsp -> toChiTietDTO(
//                        ctsp,
//                        mainImagesByProductId.get(ctsp.getId()),
//                        bestDiscountByProductId.get(ctsp.getId())))
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * Lấy đợt giảm giá hiệu lực cao nhất cho một sản phẩm tại thời điểm hiện tại.
//     * Nếu sản phẩm nằm trong nhiều đợt giảm giá đang giao nhau, đợt có giá trị giảm lớn hơn sẽ được ưu tiên.
//     */
//    public DotGiamGiaDTO getDotGiamGiaHieuLucCaoNhat(Integer chiTietSanPhamId) {
//        if (chiTietSanPhamId == null) {
//            throw new IllegalArgumentException("ID sản phẩm chi tiết không được để trống");
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//        List<ChiTietDotGiamGia> activeDiscounts =
//                chiTietDotGiamGiaRepository.findBestActiveByChiTietSanPhamId(chiTietSanPhamId, now);
//
//        if (activeDiscounts.isEmpty()) {
//            return null;
//        }
//
//        return toDTO(activeDiscounts.get(0).getDotGiamGia());
//    }
//
//    // ==================== Helper Methods ====================
//
//    private String generateMaDotGiamGia() {
//        Integer maxId = dotGiamGiaRepository.findMaxMaDotGiamGia();
//        int nextId = (maxId != null ? maxId : 0) + 1;
//        return String.format("DGG%04d", nextId);
//    }
//
//    /**
//     * Tự động tính trạng thái dựa trên ngày bắt đầu và ngày kết thúc
//     * 1: Sắp diễn ra, 2: Đang diễn ra, 3: Đã kết thúc, 0: Đã hủy
//     */
//    private Integer calculateTrangThai(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
//        LocalDateTime now = LocalDateTime.now();
//        if (ngayBatDau != null && now.isBefore(ngayBatDau)) {
//            return 1; // Sắp diễn ra
//        } else if (ngayKetThuc != null && now.isAfter(ngayKetThuc)) {
//            return 3; // Đã kết thúc
//        } else {
//            return 2; // Đang diễn ra
//        }
//    }
//
//    private String getTrangThaiText(Integer trangThai) {
//        if (trangThai == null)
//            return "Không xác định";
//        return switch (trangThai) {
//            case 0 -> "Đã hủy";
//            case 1 -> "Sắp diễn ra";
//            case 2 -> "Đang diễn ra";
//            case 3 -> "Đã kết thúc";
//            default -> "Không xác định";
//        };
//    }
//
//    private LocalDateTime parseStartOfDay(String value) {
//        if (value == null || value.isBlank()) {
//            return null;
//        }
//
//        return LocalDate.parse(value).atStartOfDay();
//    }
//
//    private LocalDateTime parseEndOfDay(String value) {
//        if (value == null || value.isBlank()) {
//            return null;
//        }
//
//        return LocalDate.parse(value).atTime(LocalTime.MAX);
//    }
//
//    private Map<Integer, String> loadMainImages(List<ChiTietSanPham> products) {
//        if (products == null || products.isEmpty()) {
//            return Map.of();
//        }
//
//        List<Integer> productIds = products.stream()
//                .map(ChiTietSanPham::getId)
//                .filter(id -> id != null)
//                .collect(Collectors.toList());
//
//        if (productIds.isEmpty()) {
//            return Map.of();
//        }
//
//        List<Object[]> imageRows = hinhAnhSpRepository.findMainImagePathsByChiTietSanPhamIds(productIds);
//        Map<Integer, String> images = new HashMap<>();
//
//        for (Object[] row : imageRows) {
//            Integer productId = (Integer) row[0];
//            String imagePath = (String) row[1];
//
//            if (productId != null && imagePath != null && !images.containsKey(productId)) {
//                images.put(productId, imagePath);
//            }
//        }
//
//        return images;
//    }
//
//    private Map<Integer, BigDecimal> loadBestActiveDiscounts(List<ChiTietSanPham> products, LocalDateTime now) {
//        if (products == null || products.isEmpty()) {
//            return Map.of();
//        }
//
//        List<Integer> productIds = products.stream()
//                .map(ChiTietSanPham::getId)
//                .filter(id -> id != null)
//                .collect(Collectors.toList());
//
//        if (productIds.isEmpty()) {
//            return Map.of();
//        }
//
//        List<Object[]> rows = chiTietDotGiamGiaRepository
//                .findBestActiveDiscountRows(productIds, now);
//
//        Map<Integer, BigDecimal> bestDiscounts = new HashMap<>();
//        for (Object[] row : rows) {
//            if (row == null || row.length < 2) {
//                continue;
//            }
//
//            Integer productId = row[0] instanceof Integer ? (Integer) row[0] : null;
//            BigDecimal discountValue = row[1] instanceof BigDecimal ? (BigDecimal) row[1] : null;
//            if (productId == null || bestDiscounts.containsKey(productId)) {
//                continue;
//            }
//
//            bestDiscounts.put(productId, discountValue);
//        }
//
//        return bestDiscounts;
//    }
//
//    private void validateFilterDateRange(String tuNgay, String denNgay) {
//        boolean hasFrom = tuNgay != null && !tuNgay.isBlank();
//        boolean hasTo = denNgay != null && !denNgay.isBlank();
//
//        if (hasFrom ^ hasTo) {
//            throw new IllegalArgumentException("Vui lòng chọn đủ từ ngày và đến ngày");
//        }
//
//        if (hasFrom && hasTo && LocalDate.parse(tuNgay).isAfter(LocalDate.parse(denNgay))) {
//            throw new IllegalArgumentException("Ngày bắt đầu không được lớn hơn ngày kết thúc");
//        }
//    }
//
//    private void validateDotGiamGia(DotGiamGiaDTO dto, Integer excludeId) {
//        if (dto.getTenDotGiamGia() == null || dto.getTenDotGiamGia().isBlank()) {
//            throw new RuntimeException("Tên đợt giảm giá không được để trống");
//        }
//        if (dto.getGiaTriGiam() == null) {
//            throw new RuntimeException("Giá trị giảm không được để trống");
//        }
//        if (dto.getNgayBatDau() == null) {
//            throw new RuntimeException("Ngày bắt đầu không được để trống");
//        }
//        if (dto.getNgayKetThuc() == null) {
//            throw new RuntimeException("Ngày kết thúc không được để trống");
//        }
//        if (dto.getNgayBatDau().isAfter(dto.getNgayKetThuc())) {
//            throw new RuntimeException("Ngày bắt đầu phải trước ngày kết thúc");
//        }
//    }
//
//    private void saveChiTietList(DotGiamGia dotGiamGia, List<ChiTietDotGiamGiaDTO> dtoList) {
//        List<ChiTietDotGiamGia> entities = new ArrayList<>();
//        for (ChiTietDotGiamGiaDTO ctDto : dtoList) {
//            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(ctDto.getIdChiTietSanPham())
//                    .orElseThrow(() -> new RuntimeException(
//                            "Không tìm thấy sản phẩm chi tiết với ID: " + ctDto.getIdChiTietSanPham()));
//
//            ChiTietDotGiamGia ct = new ChiTietDotGiamGia();
//            ct.setDotGiamGia(dotGiamGia);
//            ct.setChiTietSanPham(ctsp);
//            entities.add(ct);
//        }
//        chiTietDotGiamGiaRepository.saveAll(entities);
//    }
//
//    private DotGiamGiaDTO toDTO(DotGiamGia entity) {
//        DotGiamGiaDTO dto = new DotGiamGiaDTO();
//        dto.setId(entity.getId());
//        dto.setMaDotGiamGia(entity.getMaDotGiamGia());
//        dto.setTenDotGiamGia(entity.getTenDotGiamGia());
//        dto.setGiaTriGiam(entity.getGiaTriGiam());
//        dto.setNgayBatDau(entity.getNgayBatDau());
//        dto.setNgayKetThuc(entity.getNgayKetThuc());
//        dto.setMoTa(entity.getMoTa());
//
//        // Tính lại trạng thái dựa trên thời gian hiện tại (trừ khi đã bị hủy)
//        if (entity.getTrangThai() != null && entity.getTrangThai() == 0) {
//            dto.setTrangThai(0);
//        } else {
//            dto.setTrangThai(calculateTrangThai(entity.getNgayBatDau(), entity.getNgayKetThuc()));
//        }
//        dto.setTrangThaiText(getTrangThaiText(dto.getTrangThai()));
//
//        return dto;
//    }
//
//    private ChiTietDotGiamGiaDTO toChiTietDTO(ChiTietSanPham ctsp, String anhDaiDien, BigDecimal giaTriGiamRieng) {
//        ChiTietDotGiamGiaDTO dto = new ChiTietDotGiamGiaDTO();
//        dto.setIdChiTietSanPham(ctsp.getId());
//        dto.setAnhDaiDien(anhDaiDien);
//        dto.setMaSanPham(ctsp.getMaCtsp());
//        dto.setTenSanPham(ctsp.getSanPham() != null ? ctsp.getSanPham().getTenSanPham() : null);
//        dto.setGiaBan(ctsp.getGiaBan());
//        dto.setSoLuongTon(ctsp.getSoLuong());
//        dto.setTenMauSac(ctsp.getMauSac() != null ? ctsp.getMauSac().getTenMauSac() : null);
//        dto.setTenTrongLuong(ctsp.getTrongLuong() != null ? ctsp.getTrongLuong().getTenTrongLuong() : null);
//        dto.setTenDoCung(ctsp.getDoCung() != null ? ctsp.getDoCung().getTenDoCung() : null);
//        dto.setTenDiemCanBang(ctsp.getDiemCanBang() != null ? ctsp.getDiemCanBang().getTenDiemCanBang() : null);
//        dto.setTenChatLieuThanVot(
//                ctsp.getChatLieuThanVot() != null ? ctsp.getChatLieuThanVot().getTenChatLieuThanVot() : null);
//        dto.setTenChatLieuKhungVot(
//                ctsp.getChatLieuKhungVot() != null ? ctsp.getChatLieuKhungVot().getTenChatLieuKhungVot() : null);
//        dto.setTenDanhMuc(ctsp.getDanhMuc() != null ? ctsp.getDanhMuc().getTenDanhMuc() : null);
//        dto.setTenThuongHieu(
//                ctsp.getSanPham() != null && ctsp.getSanPham().getThuongHieu() != null
//                        ? ctsp.getSanPham().getThuongHieu().getTenThuongHieu()
//                        : null);
//        dto.setXuatXuChiTiet(ctsp.getXuatXuChiTiet());
//        dto.setChuViCanVot(ctsp.getChuViCanVot());
//        dto.setGiaTriGiamRieng(giaTriGiamRieng);
//        return dto;
//    }
//
//    private ChiTietDotGiamGiaDTO toChiTietDTO(ChiTietDotGiamGia entity) {
//        ChiTietSanPham ctsp = entity.getChiTietSanPham();
//        ChiTietDotGiamGiaDTO dto = toChiTietDTO(ctsp, null, null);
//        dto.setId(entity.getId());
//        return dto;
//    }
//}
