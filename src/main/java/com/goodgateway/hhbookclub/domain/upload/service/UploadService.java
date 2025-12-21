package com.goodgateway.hhbookclub.domain.upload.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * Generate a presigned URL for uploading an image to S3.
     * The client can use this URL to upload the file directly to S3.
     *
     * @param filename Original filename
     * @param contentType MIME type of the file (e.g., "image/png")
     * @return PresignedUrlResponse containing presigned URL and final image URL
     */
    public PresignedUrlResponse generatePresignedUrl(String filename, String contentType) {
        String extension = "";
        if (filename != null && filename.contains(".")) {
            extension = filename.substring(filename.lastIndexOf("."));
        }

        String key = "images/" + UUID.randomUUID().toString() + extension;

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // URL valid for 10 minutes
                .putObjectRequest(objectRequest)
                .build();

        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String presignedUrl = presignedRequest.url().toString();
        String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);

        return new PresignedUrlResponse(presignedUrl, imageUrl);
    }

    public record PresignedUrlResponse(String presignedUrl, String imageUrl) {}
}
