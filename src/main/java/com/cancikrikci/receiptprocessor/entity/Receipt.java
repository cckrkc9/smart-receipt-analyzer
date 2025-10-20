package com.cancikrikci.receiptprocessor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDbBean
public class Receipt {
    private String receiptId = UUID.randomUUID().toString();
    private String userId;
    private String originalFileName;
    private String s3Url;
    private String textContent;
    private Double totalAmount;
    private String currency;
    private List<String> categories;
    private String createdAt;
    private User user;

    @DynamoDbPartitionKey
    public String getReceiptId(){
        return receiptId;
    }
}
