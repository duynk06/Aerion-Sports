package com.example.AerionSports_BE.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class GlobalHandleException {
    // Hàm này sẽ tự động kích hoạt khi có bất kỳ lỗi Validation (@Valid) nào xảy ra
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Duyệt qua tất cả các trường bị lỗi và lấy ra Message bạn đã định nghĩa ở DTO
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Trả về cho Vue.js một Object chứa danh sách lỗi sạch đẹp
        return ResponseEntity.badRequest().body(errors);
    }
}
