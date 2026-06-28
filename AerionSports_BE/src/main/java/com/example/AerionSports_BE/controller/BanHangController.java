package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.dto.request.ThanhToanRequest;
import com.example.AerionSports_BE.dto.request.ThemSanPhamRequest;
import com.example.AerionSports_BE.dto.response.BanHangResponse;
import com.example.AerionSports_BE.dto.response.KhachHangPosResponse;
import com.example.AerionSports_BE.dto.response.SanPhamPosResponse;
import com.example.AerionSports_BE.service.BanHangService;
import com.example.AerionSports_BE.service.KhachHangPosService;
import com.example.AerionSports_BE.service.SanPhamPosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ban-hang")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BanHangController {

    private final BanHangService banHangService;
    private final
    SanPhamPosService sanPhamPosService;
    private final KhachHangPosService khachHangPosService;
    @PostMapping("/tao-hoa-don")
    public ResponseEntity<BanHangResponse> taoHoaDon(){

        return ResponseEntity.ok(
                banHangService.taoHoaDonCho()
        );
    }
    @GetMapping("/san-pham")
    public ResponseEntity<Page<SanPhamPosResponse>> locSanPham(

            @RequestParam(required = false)
            String keyword,

            @RequestParam(required = false)
            Integer idMauSac,

            @RequestParam(required = false)
            Integer idTrongLuong,

            @RequestParam(required = false)
            BigDecimal giaMin,

            @RequestParam(required = false)
            BigDecimal giaMax,

            @RequestParam(required = false)
            Integer trangThai,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size
    ){
        return ResponseEntity.ok(
                sanPhamPosService.locSanPham(
                        keyword,
                        idMauSac,
                        idTrongLuong,
                        giaMin,
                        giaMax,
                        trangThai,
                        page,
                        size
                )
        );
    }
    @GetMapping("/khoang-gia")
    public ResponseEntity<?> getKhoangGia(){

        return ResponseEntity.ok(
                sanPhamPosService.getKhoangGia()
        );
    }

    // ============== API MỚI CHO POPUP KHÁCH HÀNG ==============
    @GetMapping("/khach-hang")
    public ResponseEntity<Page<KhachHangPosResponse>> timKhachHangPos(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        return ResponseEntity.ok(khachHangPosService.locKhachHangPos(keyword, page, size));
    }

    @PutMapping("/{id}/khach-hang")
    public ResponseEntity<BanHangResponse> capNhatKhachHang(
            @PathVariable("id") Integer id, // Bổ sung ("id") vào đây
            @RequestParam(required = false) Integer idKhachHang
    ) {
        return ResponseEntity.ok(
                banHangService.capNhatKhachHangVaoHoaDon(id, idKhachHang)
        );
    }

    @PostMapping("/them-san-pham")
    public ResponseEntity<?> themSanPham(@RequestBody ThemSanPhamRequest request) {
        try {
            BanHangResponse response = banHangService.themSanPhamVaoHoaDon(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Trả về lỗi 400 Bad Request nếu hết hàng hoặc không tìm thấy ID
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // BanHangController.java — thêm endpoint
    @PutMapping("/chi-tiet/{id}/so-luong")
    public ResponseEntity<?> capNhatSoLuong(
            @PathVariable Integer id,
            @RequestParam Integer soLuong
    ) {
        try {
            BanHangResponse response = banHangService.capNhatSoLuong(id, soLuong);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // BanHangController.java — thêm 2 endpoint
    @GetMapping("/hoa-don-cho")
    public ResponseEntity<List<BanHangResponse>> getHoaDonCho() {
        return ResponseEntity.ok(banHangService.getHoaDonCho());
    }


    // BanHangController.java — thêm endpoint
    @DeleteMapping("/chi-tiet/{id}")
    public ResponseEntity<?> xoaChiTiet(@PathVariable Integer id) {
        try {
            banHangService.xoaChiTietHoaDon(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/thanh-toan")
    public ResponseEntity<?> thanhToan(@RequestBody ThanhToanRequest request) {
        try {
            BanHangResponse response = banHangService.thanhToan(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // BanHangController.java — sửa lại endpoint
    @DeleteMapping("/hoa-don/{id}")
    public ResponseEntity<?> huyHoaDon(@PathVariable Integer id) {
        try {
            banHangService.huyHoaDon(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/loai-hoa-don")
    public ResponseEntity<?> capNhatLoaiHoaDon(
            @PathVariable Integer id,
            @RequestParam Integer loaiHoaDon
    ) {
        try {
            return ResponseEntity.ok(banHangService.capNhatLoaiHoaDon(id, loaiHoaDon));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/phi-van-chuyen")
    public ResponseEntity<?> capNhatPhiVanChuyen(
            @PathVariable Integer id,
            @RequestParam BigDecimal phiVanChuyen
    ) {
        try {
            return ResponseEntity.ok(banHangService.capNhatPhiVanChuyen(id, phiVanChuyen));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // BanHangController.java
    @GetMapping("/khach-hang/{id}/dia-chi")
    public ResponseEntity<?> getDiaChiKhachHang(@PathVariable Integer id) {
        return ResponseEntity.ok(banHangService.getDiaChiKhachHang(id));
    }

    @GetMapping("/{id}/phieu-giam-gia-tot-nhat")
    public ResponseEntity<?> getPhieuTotNhat(@PathVariable Integer id) {
        return ResponseEntity.ok(banHangService.timPhieuGiamGiaTotNhat(id));
    }

    @PutMapping("/{id}/ap-dung-phieu")
    public ResponseEntity<?> apDungPhieu(
            @PathVariable Integer id,
            @RequestParam Integer idPhieu
    ) {
        try {
            return ResponseEntity.ok(banHangService.apDungPhieuGiamGia(id, idPhieu));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/bo-phieu")
    public ResponseEntity<?> boPhieu(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(banHangService.boPhieuGiamGia(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // BanHangController.java — thêm endpoint
    @GetMapping("/hoa-don/{id}/kiem-tra-gia")
    public ResponseEntity<?> kiemTraGia(@PathVariable Integer id) {
        return ResponseEntity.ok(banHangService.kiemTraGiaThayDoi(id));
    }
}