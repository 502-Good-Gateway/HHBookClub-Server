package com.goodgateway.hhbookclub.domain.upload.dto;

public record UploadResponseDto(
    String presignedUrl,
    String imageUrl
) {}
