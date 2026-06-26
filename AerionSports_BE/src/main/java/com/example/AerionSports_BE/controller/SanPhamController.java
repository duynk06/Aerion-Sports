package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.dto.request.ChiTietSanPhamRequest;
import com.example.AerionSports_BE.dto.request.SanPhamFilter;
import com.example.AerionSports_BE.dto.request.SanPhamRequest;
import com.example.AerionSports_BE.dto.response.SanPhamResponse;
import com.example.AerionSports_BE.service.SanPhamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/san-pham")
@CrossOrigin("*")
public class SanPhamController {

    @Autowired
    private SanPhamService svc;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "thuongHieuId", required = false) Integer thuongHieuId,
            @RequestParam(value = "idXuatXu", required = false) Integer idXuatXu,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        SanPhamFilter filter = new SanPhamFilter();
        filter.setKeyword(keyword);
        filter.setIdThuongHieu(thuongHieuId);
        filter.setIdXuatXu(idXuatXu);
        filter.setTrangThai(trangThai);
        filter.setPage(page);
        filter.setSize(size);

        Page<SanPhamResponse> result = svc.search(filter);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @PostMapping(value = "/save-simple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            @RequestParam("data") String data,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SanPhamRequest request = objectMapper.readValue(data, SanPhamRequest.class);
        return ResponseEntity.ok(svc.save(request, files));
    }

    @PostMapping(value = "/create-variants", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProductWithVariants(
            @RequestParam("data") String dataJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            return ResponseEntity.ok(svc.createProductWithVariants(dataJson, files));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Loi xu ly luu he thong san pham to hop: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/trang-thai")
    public ResponseEntity<?> updateTrangThai(
            @PathVariable Integer id,
            @RequestParam("trangThai") Integer trangThai
    ) {
        svc.updateTrangThai(id, trangThai);
        return ResponseEntity.ok("Cap nhat trang thai thanh cong!");
    }

    @PutMapping(value = "/bien-the/update/{idCtsp}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateSingleVariant(
            @PathVariable Integer idCtsp,
            @RequestParam("data") String dataJson,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ChiTietSanPhamRequest req = mapper.readValue(dataJson, ChiTietSanPhamRequest.class);

            svc.updateSingleVariantWithImage(idCtsp, req, file);
            return ResponseEntity.ok("Cap nhat thong tin va anh bien the thanh cong!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Loi cap nhat bien the: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody SanPhamRequest r) {
        return ResponseEntity.ok(svc.update(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        svc.delete(id);
        return ResponseEntity.ok("Xoa san pham thanh cong!");
    }
}
