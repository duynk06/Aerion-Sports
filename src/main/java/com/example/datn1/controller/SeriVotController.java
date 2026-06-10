package com.example.datn1.controller;

import com.example.datn1.dto.Request.SeriVotFilter;
import com.example.datn1.dto.Request.SeriVotRequest;
import com.example.datn1.dto.Response.SeriVotResponse;
import com.example.datn1.service.SeriVotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seri-vot")
@CrossOrigin("*")
public class SeriVotController {
    @Autowired
    private SeriVotService service;

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "trangThai", required = false) Integer trangThai
    ) {
        return ResponseEntity.ok(service.search(page, size, trangThai));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody SeriVotRequest r) {
        return ResponseEntity.ok(service.save(r));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody SeriVotRequest r) {
        return ResponseEntity.ok(service.update(id, r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa số mã seri thành công!");
    }
}
