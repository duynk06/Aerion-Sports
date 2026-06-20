package com.example.AerionSports_BE.controller;

import com.example.AerionSports_BE.entity.NhanVien;
import com.example.AerionSports_BE.repository.NhanVienRepository;
import com.example.AerionSports_BE.service.NhanVienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nhan-vien")
@CrossOrigin("*")
public class NhanVienController {

    @Autowired
    private NhanVienService nhanVienService;
    @Autowired
    private NhanVienRepository NhanVienRepository;

    @GetMapping("/hien-thi")
    public ResponseEntity<List<NhanVien>> getAll() {
        List<NhanVien> list = nhanVienService.findAll();

        System.out.println("======> Số lượng nhân viên lấy được từ DB: "
                + (list != null ? list.size() : 0));

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhanVien> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(nhanVienService.findById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<NhanVien> add(@RequestBody NhanVien nhanVien) {
        return ResponseEntity.ok(nhanVienService.add(nhanVien));
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
    // Trong NhanVienController.java
    @GetMapping("/check-duplicate")
    public ResponseEntity<Boolean> checkDuplicate(@RequestParam String sdt, @RequestParam String email) {
        // Gọi Service để kiểm tra trong Database
        boolean exists = NhanVienRepository.existsBySdtOrEmail(sdt, email);
        return ResponseEntity.ok(exists);
    }
    @GetMapping("/check-duplicate-update")
    public ResponseEntity<Boolean> checkDuplicateUpdate(@RequestParam String sdt,
                                                        @RequestParam String email,
                                                        @RequestParam Integer id) {
        boolean sdtExists = NhanVienRepository.existsBySdtAndIdNot(sdt, id);
        boolean emailExists = NhanVienRepository.existsByEmailAndIdNot(email, id);
        return ResponseEntity.ok(sdtExists || emailExists);
    }
}
