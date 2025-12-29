package com.goodgateway.hhbookclub.domain.upload.scheduler;

import com.goodgateway.hhbookclub.domain.upload.entity.S3Image;
import com.goodgateway.hhbookclub.domain.upload.repository.S3ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ImageCleanupScheduler {

    private final S3ImageRepository s3ImageRepository;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * Clean up orphaned images from S3 and database.
     * Runs every day at 3 AM.
     */
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupOrphanedImages() {
        log.info("Starting S3 orphaned image cleanup...");

        // Find images that are not linked to any post and are older than 24 hours
        LocalDateTime threshold = LocalDateTime.now().minusHours(24);
        List<S3Image> orphanedImages = s3ImageRepository.findAllByPostIsNullAndCreatedAtBefore(threshold);

        log.info("Found {} orphaned images to delete.", orphanedImages.size());

        for (S3Image image : orphanedImages) {
            try {
                // Delete from S3
                deleteFromS3(image.getS3Key());

                // Delete from DB
                s3ImageRepository.delete(image);

                log.info("Successfully deleted orphaned image: {}", image.getS3Key());
            } catch (Exception e) {
                log.error("Failed to delete orphaned image: {}", image.getS3Key(), e);
            }
        }

        log.info("S3 orphaned image cleanup finished.");
    }

    private void deleteFromS3(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }
}
