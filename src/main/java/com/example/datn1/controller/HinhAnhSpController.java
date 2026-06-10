package com.example.datn1.controller;

import com.example.datn1.dto.Request.HinhAnhSpFilter;
import com.example.datn1.dto.Request.HinhAnhSpRequest;
import com.example.datn1.dto.Response.HinhAnhSpResponse;
import com.example.datn1.service.HinhAnhSpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hinh-anh-sp")
@CrossOrigin("*")
public class HinhAnhSpController {
    @Autowired
    private HinhAnhSpService service;

    @PostMapping("/list")
    public ResponseEntity<List<HinhAnhSpResponse>> getImages(@RequestBody HinhAnhSpFilter f) {
        return ResponseEntity.ok(service.getImages(f));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody HinhAnhSpRequest r) {
        return ResponseEntity.ok(service.save(r));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Xóa ảnh thành công!");
    }
}
