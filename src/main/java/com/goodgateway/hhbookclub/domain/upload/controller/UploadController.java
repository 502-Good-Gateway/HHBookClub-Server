package com.goodgateway.hhbookclub.domain.upload.controller;

import com.goodgateway.hhbookclub.domain.upload.dto.UploadResponseDto;
import com.goodgateway.hhbookclub.domain.upload.service.UploadService;
import com.goodgateway.hhbookclub.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<ApiResponse<UploadResponseDto>> upload(@RequestParam("file") MultipartFile file) {
        String imageUrl = uploadService.uploadImage(file);
        return ResponseEntity.ok(ApiResponse.success(new UploadResponseDto(imageUrl)));
    }
}
