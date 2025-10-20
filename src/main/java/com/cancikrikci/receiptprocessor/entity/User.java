package com.cancikrikci.receiptprocessor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDbBean
public class User {

    private String userId;
    private String username;
    private String email;
    private String passwordHash;
    private String role;
    private String createdAt;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }

}
