package com.cancikrikci.receiptprocessor.service;

import com.cancikrikci.receiptprocessor.exception.S3UploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final S3Client s3Client;
    private final TextractService textractService;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    public String upload(MultipartFile file, String username) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf("."))
            : "";
        String key = UUID.randomUUID().toString() + fileExtension;
        
        log.info("Uploading file to S3: {} (original: {}) for user: {}", key, originalFilename, username);
        
        var request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        
        try {
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("File successfully uploaded to S3: {}", key);

            processFileAsync(key, username);
            
        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage(), e);
            throw new S3UploadException("Failed to upload file to S3", e);
        }

        return key;
    }

    @Async
    public CompletableFuture<Void> processFileAsync(String key, String username) {
        textractService.processFileByKey(key, username);
        return CompletableFuture.completedFuture(null);
    }
}
