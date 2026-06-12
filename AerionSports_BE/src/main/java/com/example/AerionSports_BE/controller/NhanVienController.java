package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.entity.NhanVien;
import com.example.AerionSports_BE.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nhan-vien")
@CrossOrigin(origins = {"http://localhost:5173"})
public class NhanVienController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/hien-thi")
    public ResponseEntity<List<NhanVien>> getAll() {
        List<NhanVien> list = nhanVienService.findAll();

        System.out.println("======> Số lượng nhân viên lấy được từ DB: "
                + (list != null ? list.size() : 0));

        return ResponseEntity.ok(list);
    }

    @PutMapping("/doi-trang-thai/{id}")
    public ResponseEntity<?> changeStatus(
            @PathVariable Integer id,
            @RequestParam Integer trangThai) {

        // 1. Lấy thông tin nhân viên hiện tại ra từ DB
        NhanVien nv = nhanVienService.findById(id);
        if (nv == null) {
            return ResponseEntity.notFound().build();
        }

        // 2. Cập nhật duy nhất trường trạng thái
        nv.setTrangThai(trangThai);

        // 3. Lưu ngược trở lại database thông qua hàm update có sẵn của bạn
        nhanVienService.update(id, nv);

        return ResponseEntity.ok().body("{\"message\": \"Đổi trạng thái thành công!\"}");
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhanVien> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(nhanVienService.findById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<NhanVien> add(@RequestBody NhanVien nhanVien) {
        return ResponseEntity.ok(nhanVienService.create(nhanVien));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<NhanVien> update(
            @PathVariable Integer id,
            @RequestBody NhanVien nhanVien) {

        return ResponseEntity.ok(
                nhanVienService.update(id, nhanVien)
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {

        nhanVienService.delete(id);

        return ResponseEntity.ok("Xóa nhân viên thành công");
    }

    @GetMapping("/search")
    public ResponseEntity<List<NhanVien>> search(
            @RequestParam String tenNv) {

        return ResponseEntity.ok(
                nhanVienService.search(tenNv)
        );
    }
}
