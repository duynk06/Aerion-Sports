package com.example.AerionSports_BE.Controller;

import com.example.AerionSports_BE.entity.PhieuGiamGia;
import com.example.AerionSports_BE.Service.PhieuGiamGiaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phieu-giam-gia")
@CrossOrigin(origins = {"http://localhost:5173"})
public class PhieuGiamGiaController {

    private final PhieuGiamGiaService service;

    public PhieuGiamGiaController(PhieuGiamGiaService service) {
        this.service = service;
    }

    @GetMapping("/hien-thi")
    public ResponseEntity<List<PhieuGiamGia>> getAll() {
        List<PhieuGiamGia> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGia> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PhieuGiamGia> add(@RequestBody PhieuGiamGia pgg) {
        return ResponseEntity.ok(service.add(pgg));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGia> update(@PathVariable Integer id, @RequestBody PhieuGiamGia pgg) {
        return ResponseEntity.ok(service.update(id, pgg));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}