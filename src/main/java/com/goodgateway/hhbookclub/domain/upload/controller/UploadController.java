package com.goodgateway.hhbookclub.domain.upload.controller;

import com.goodgateway.hhbookclub.domain.upload.dto.UploadResponseDto;
import com.goodgateway.hhbookclub.domain.upload.service.UploadService;
import com.goodgateway.hhbookclub.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    /**
     * Get a presigned URL for uploading an image to S3.
     *
     * @param filename Original filename (for determining extension)
     * @param contentType MIME type of the file (e.g., "image/png", "image/jpeg")
     * @return Presigned URL and final image URL
     */
    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<UploadResponseDto>> getPresignedUrl(
            @RequestParam String filename,
            @RequestParam(defaultValue = "image/png") String contentType) {
        UploadService.PresignedUrlResponse response = uploadService.generatePresignedUrl(filename, contentType);
        return ResponseEntity.ok(ApiResponse.success(new UploadResponseDto(response.presignedUrl(), response.imageUrl())));
    }
}
