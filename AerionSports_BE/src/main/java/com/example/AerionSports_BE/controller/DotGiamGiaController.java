package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.dto.ChiTietDotGiamGiaDTO;
import com.example.AerionSports_BE.dto.DotGiamGiaDTO;
import com.example.AerionSports_BE.service.DotGiamGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dot-giam-gia")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DotGiamGiaController {

    private final DotGiamGiaService dotGiamGiaService;

    /**
     * GET /api/dot-giam-gia?keyword=&trangThai=&page=0&size=10
     * Lấy danh sách đợt giảm giá có phân trang
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDanhSach(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<DotGiamGiaDTO> pageResult = dotGiamGiaService.getDanhSach(keyword, trangThai, page, size);

            Map<String, Object> response = new HashMap<>();
            response.put("content", pageResult.getContent());
            response.put("totalElements", pageResult.getTotalElements());
            response.put("totalPages", pageResult.getTotalPages());
            response.put("currentPage", pageResult.getNumber());
            response.put("size", pageResult.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            DotGiamGiaDTO dto = dotGiamGiaService.getById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody DotGiamGiaDTO dto) {
        try {
            DotGiamGiaDTO created = dotGiamGiaService.create(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/dot-giam-gia/{id}
     * Cập nhật đợt giảm giá
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody DotGiamGiaDTO dto) {
        try {
            DotGiamGiaDTO updated = dotGiamGiaService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/dot-giam-gia/{id}
     * Xóa mềm đợt giảm giá (chuyển trạng thái = 0)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            dotGiamGiaService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Đã hủy đợt giảm giá thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/dot-giam-gia/{id}/trang-thai
     * Cập nhật trạng thái đợt giảm giá
     */
    @PutMapping("/{id}/trang-thai")
    public ResponseEntity<?> updateTrangThai(
            @PathVariable Integer id,
            @RequestParam Integer trangThai) {
        try {
            DotGiamGiaDTO updated = dotGiamGiaService.updateTrangThai(id, trangThai);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * GET /api/dot-giam-gia/san-pham?keyword=
     * Lấy danh sách sản phẩm chi tiết để chọn áp dụng giảm giá
     */
    @GetMapping("/san-pham")
    public ResponseEntity<?> getProductsForSelection(
            @RequestParam(required = false) String keyword) {
        try {
            List<ChiTietDotGiamGiaDTO> products = dotGiamGiaService.getProductsForSelection(keyword);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
