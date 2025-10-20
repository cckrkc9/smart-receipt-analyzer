package com.cancikrikci.receiptprocessor.service;

import com.cancikrikci.receiptprocessor.entity.Receipt;
import com.cancikrikci.receiptprocessor.entity.User;
import com.cancikrikci.receiptprocessor.repository.UserRepository;
import com.cancikrikci.receiptprocessor.util.ExpenseClassifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class TextractService {

    private final TextractClient textractClient;
    private final ReceiptService receiptService;
    private final ExpenseClassifier expenseClassifier;
    private final UserRepository userRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;


    public void processFile(String bucket, String key, String username) {
        log.info("Processing file: {} from bucket: {} for user: {}", key, bucket, username);
        
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            DetectDocumentTextRequest request = DetectDocumentTextRequest.builder()
                    .document(Document.builder()
                            .s3Object(S3Object.builder()
                                    .bucket(bucket)
                                    .name(key)
                                    .build())
                            .build())
                    .build();

            DetectDocumentTextResponse response = textractClient.detectDocumentText(request);

            StringBuilder extractedText = new StringBuilder();
            response.blocks().forEach(block -> {
                if (block.blockType().equals(BlockType.LINE)) {
                    extractedText.append(block.text()).append("\n");
                }
            });

            log.info("Extracted text length: {} characters", extractedText.length());

            Receipt receipt = Receipt.builder()
                    .receiptId(key)
                    .originalFileName(key)
                    .s3Url(buildS3Url(bucket, key))
                    .textContent(extractedText.toString())
                    .createdAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();

            Receipt processedReceipt = expenseClassifier.textractModifier(receipt);
            processedReceipt.setUser(user);
            processedReceipt.setUserId(user.getUserId());

            receiptService.addReceipt(processedReceipt);

            log.info("Receipt saved to DynamoDB with ID: {} for user: {}", processedReceipt.getReceiptId(), username);
            
        } catch (Exception e) {
            log.error("Error processing file {} from bucket {}: {}", key, bucket, e.getMessage(), e);
            throw new RuntimeException("Failed to process file with Textract", e);
        }
    }

    public void processFileByKey(String key, String username) {
        processFile(bucketName, key, username);
    }

    private String buildS3Url(String bucket, String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
    }
}
