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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DotGiamGiaService {

    private final DotGiamGiaRepository dotGiamGiaRepository;
    private final ChiTietDotGiamGiaRepository chiTietDotGiamGiaRepository;
    private final ChiTietSanPhamRepository chiTietSanPhamRepository;

    // ==================== CRUD đợt giảm giá ====================

    /**
     * Lấy danh sách đợt giảm giá có phân trang và tìm kiếm
     */
    public Page<DotGiamGiaDTO> getDanhSach(String keyword, Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DotGiamGia> pageResult = dotGiamGiaRepository.searchDotGiamGia(keyword, trangThai, pageable);
        return pageResult.map(this::toDTO);
    }

    /**
     * Lấy chi tiết đợt giảm giá theo ID
     */
    public DotGiamGiaDTO getById(Integer id) {
        DotGiamGia entity = dotGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
        DotGiamGiaDTO dto = toDTO(entity);

        // Load chi tiết
        List<ChiTietDotGiamGia> chiTietList = chiTietDotGiamGiaRepository.findActiveByDotGiamGiaId(id);
        dto.setChiTietList(chiTietList.stream().map(this::toChiTietDTO).collect(Collectors.toList()));

        return dto;
    }

    /**
     * Tạo mới đợt giảm giá
     */
    @Transactional
    public DotGiamGiaDTO create(DotGiamGiaDTO dto) {
        // Validate
        validateDotGiamGia(dto, null);

        DotGiamGia entity = new DotGiamGia();
        entity.setMaDotGiamGia(generateMaDotGiamGia());
        entity.setTenDotGiamGia(dto.getTenDotGiamGia());
        entity.setHinhThucGiam(dto.getHinhThucGiam());
        entity.setGiaTriGiam(dto.getGiaTriGiam());
        entity.setNgayBatDau(dto.getNgayBatDau());
        entity.setNgayKetThuc(dto.getNgayKetThuc());
        entity.setMoTa(dto.getMoTa());
        entity.setNgayTao(LocalDateTime.now());
        entity.setNgayCapNhat(LocalDateTime.now());
        entity.setTrangThai(calculateTrangThai(dto.getNgayBatDau(), dto.getNgayKetThuc()));

        DotGiamGia saved = dotGiamGiaRepository.save(entity);

        // Lưu chi tiết sản phẩm áp dụng
        if (dto.getChiTietList() != null && !dto.getChiTietList().isEmpty()) {
            saveChiTietList(saved, dto.getChiTietList());
        }

        return getById(saved.getId());
    }

    /**
     * Cập nhật đợt giảm giá
     */
    @Transactional
    public DotGiamGiaDTO update(Integer id, DotGiamGiaDTO dto) {
        DotGiamGia entity = dotGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));

        // Validate
        validateDotGiamGia(dto, id);

        entity.setTenDotGiamGia(dto.getTenDotGiamGia());
        entity.setHinhThucGiam(dto.getHinhThucGiam());
        entity.setGiaTriGiam(dto.getGiaTriGiam());
        entity.setNgayBatDau(dto.getNgayBatDau());
        entity.setNgayKetThuc(dto.getNgayKetThuc());
        entity.setMoTa(dto.getMoTa());
        entity.setNgayCapNhat(LocalDateTime.now());
        entity.setTrangThai(calculateTrangThai(dto.getNgayBatDau(), dto.getNgayKetThuc()));

        dotGiamGiaRepository.save(entity);

        // Xóa chi tiết cũ và lưu chi tiết mới
        chiTietDotGiamGiaRepository.deleteByDotGiamGiaId(id);
        if (dto.getChiTietList() != null && !dto.getChiTietList().isEmpty()) {
            saveChiTietList(entity, dto.getChiTietList());
        }

        return getById(id);
    }

    /**
     * Xóa mềm đợt giảm giá (chuyển trạng thái = 0)
     */
    @Transactional
    public void delete(Integer id) {
        DotGiamGia entity = dotGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
        entity.setTrangThai(0); // Hủy
        entity.setNgayCapNhat(LocalDateTime.now());
        dotGiamGiaRepository.save(entity);
    }

    /**
     * Cập nhật trạng thái đợt giảm giá
     */
    @Transactional
    public DotGiamGiaDTO updateTrangThai(Integer id, Integer trangThai) {
        DotGiamGia entity = dotGiamGiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đợt giảm giá với ID: " + id));
        entity.setTrangThai(trangThai);
        entity.setNgayCapNhat(LocalDateTime.now());
        dotGiamGiaRepository.save(entity);
        return toDTO(entity);
    }

    // ==================== Sản phẩm cho chọn áp dụng ====================

    /**
     * Lấy danh sách sản phẩm chi tiết để chọn áp dụng giảm giá
     */
    public List<ChiTietDotGiamGiaDTO> getProductsForSelection(String keyword) {
        List<ChiTietSanPham> products = chiTietSanPhamRepository.searchActiveProducts(keyword);
        return products.stream().map(ctsp -> {
            ChiTietDotGiamGiaDTO dto = new ChiTietDotGiamGiaDTO();
            dto.setIdChiTietSanPham(ctsp.getId());
            dto.setMaSanPham(ctsp.getMaCtsp());
            dto.setTenSanPham(ctsp.getSanPham().getTenSanPham());
            dto.setGiaBan(ctsp.getGiaBan());
            return dto;
        }).collect(Collectors.toList());
    }

    // ==================== Helper Methods ====================

    private String generateMaDotGiamGia() {
        Integer maxId = dotGiamGiaRepository.findMaxMaDotGiamGia();
        int nextId = (maxId != null ? maxId : 0) + 1;
        return String.format("DGG%04d", nextId);
    }

    /**
     * Tự động tính trạng thái dựa trên ngày bắt đầu và ngày kết thúc
     * 1: Sắp diễn ra, 2: Đang diễn ra, 3: Đã kết thúc, 0: Đã hủy
     */
    private Integer calculateTrangThai(LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc) {
        LocalDateTime now = LocalDateTime.now();
        if (ngayBatDau != null && now.isBefore(ngayBatDau)) {
            return 1; // Sắp diễn ra
        } else if (ngayKetThuc != null && now.isAfter(ngayKetThuc)) {
            return 3; // Đã kết thúc
        } else {
            return 2; // Đang diễn ra
        }
    }

    private String getTrangThaiText(Integer trangThai) {
        if (trangThai == null)
            return "Không xác định";
        return switch (trangThai) {
            case 0 -> "Đã hủy";
            case 1 -> "Sắp diễn ra";
            case 2 -> "Đang diễn ra";
            case 3 -> "Đã kết thúc";
            default -> "Không xác định";
        };
    }

    private void validateDotGiamGia(DotGiamGiaDTO dto, Integer excludeId) {
        if (dto.getTenDotGiamGia() == null || dto.getTenDotGiamGia().isBlank()) {
            throw new RuntimeException("Tên đợt giảm giá không được để trống");
        }
        if (dto.getHinhThucGiam() == null || dto.getHinhThucGiam().isBlank()) {
            throw new RuntimeException("Hình thức giảm không được để trống");
        }
        if (dto.getGiaTriGiam() == null) {
            throw new RuntimeException("Giá trị giảm không được để trống");
        }
        if (dto.getNgayBatDau() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống");
        }
        if (dto.getNgayKetThuc() == null) {
            throw new RuntimeException("Ngày kết thúc không được để trống");
        }
        if (dto.getNgayBatDau().isAfter(dto.getNgayKetThuc())) {
            throw new RuntimeException("Ngày bắt đầu phải trước ngày kết thúc");
        }
        // Validate phần trăm
        if ("phan_tram".equals(dto.getHinhThucGiam())) {
            if (dto.getGiaTriGiam().intValue() < 1 || dto.getGiaTriGiam().intValue() > 100) {
                throw new RuntimeException("Giá trị giảm theo phần trăm phải từ 1 đến 100");
            }
        }
    }

    private void saveChiTietList(DotGiamGia dotGiamGia, List<ChiTietDotGiamGiaDTO> dtoList) {
        List<ChiTietDotGiamGia> entities = new ArrayList<>();
        for (ChiTietDotGiamGiaDTO ctDto : dtoList) {
            ChiTietSanPham ctsp = chiTietSanPhamRepository.findById(ctDto.getIdChiTietSanPham())
                    .orElseThrow(() -> new RuntimeException(
                            "Không tìm thấy sản phẩm chi tiết với ID: " + ctDto.getIdChiTietSanPham()));

            // Check xem sản phẩm đã được áp dụng ở đợt giảm giá khác đang hoạt động chưa
            boolean exists = chiTietDotGiamGiaRepository.existsByChiTietSanPhamInOtherActiveDot(
                    ctDto.getIdChiTietSanPham(), dotGiamGia.getId());
            if (exists) {
                throw new RuntimeException("Sản phẩm " + ctsp.getMaCtsp() +
                        " đã được áp dụng trong đợt giảm giá khác đang hoạt động");
            }

            ChiTietDotGiamGia ct = new ChiTietDotGiamGia();
            ct.setDotGiamGia(dotGiamGia);
            ct.setChiTietSanPham(ctsp);
            ct.setSoLuongApDung(ctDto.getSoLuongApDung());
            ct.setGiaTriGiamRieng(ctDto.getGiaTriGiamRieng());
            ct.setGhiChu(ctDto.getGhiChu());
            ct.setNgayTao(LocalDateTime.now());
            ct.setNgayCapNhat(LocalDateTime.now());
            ct.setTrangThai(1);
            entities.add(ct);
        }
        chiTietDotGiamGiaRepository.saveAll(entities);
    }

    private DotGiamGiaDTO toDTO(DotGiamGia entity) {
        DotGiamGiaDTO dto = new DotGiamGiaDTO();
        dto.setId(entity.getId());
        dto.setMaDotGiamGia(entity.getMaDotGiamGia());
        dto.setTenDotGiamGia(entity.getTenDotGiamGia());
        dto.setHinhThucGiam(entity.getHinhThucGiam());
        dto.setGiaTriGiam(entity.getGiaTriGiam());
        dto.setNgayBatDau(entity.getNgayBatDau());
        dto.setNgayKetThuc(entity.getNgayKetThuc());
        dto.setMoTa(entity.getMoTa());
        dto.setNgayTao(entity.getNgayTao());
        dto.setNgayCapNhat(entity.getNgayCapNhat());

        // Tính lại trạng thái dựa trên thời gian hiện tại (trừ khi đã bị hủy)
        if (entity.getTrangThai() != null && entity.getTrangThai() == 0) {
            dto.setTrangThai(0);
        } else {
            dto.setTrangThai(calculateTrangThai(entity.getNgayBatDau(), entity.getNgayKetThuc()));
        }
        dto.setTrangThaiText(getTrangThaiText(dto.getTrangThai()));

        return dto;
    }

    private ChiTietDotGiamGiaDTO toChiTietDTO(ChiTietDotGiamGia entity) {
        ChiTietDotGiamGiaDTO dto = new ChiTietDotGiamGiaDTO();
        dto.setId(entity.getId());
        dto.setIdDotGiamGia(entity.getDotGiamGia().getId());
        dto.setIdChiTietSanPham(entity.getChiTietSanPham().getId());
        dto.setMaSanPham(entity.getChiTietSanPham().getMaCtsp());
        dto.setTenSanPham(entity.getChiTietSanPham().getSanPham().getTenSanPham());
        dto.setGiaBan(entity.getChiTietSanPham().getGiaBan());
        dto.setSoLuongApDung(entity.getSoLuongApDung());
        dto.setGiaTriGiamRieng(entity.getGiaTriGiamRieng());
        dto.setTrangThai(entity.getTrangThai());
        dto.setGhiChu(entity.getGhiChu());
        dto.setNgayTao(entity.getNgayTao());
        dto.setNgayCapNhat(entity.getNgayCapNhat());
        return dto;
    }
}
