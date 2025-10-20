package com.cancikrikci.receiptprocessor.repository;

import com.cancikrikci.receiptprocessor.dto.response.ReceiptResponse;
import com.cancikrikci.receiptprocessor.entity.Receipt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReceiptRepository {
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    private DynamoDbTable<Receipt> receiptTable() {
        return dynamoDbEnhancedClient.table("Receipts", TableSchema.fromBean(Receipt.class));
    }

    public List<ReceiptResponse> getAllReceipts() {
        log.info("Fetching all receipts from DynamoDB");
        return receiptTable()
                .scan()
                .items()
                .stream()
                .map(ReceiptResponse::from)
                .toList();
    }

    public Optional<Receipt> getReceiptById(String receiptId) {
        log.info("Fetching receipt by ID: {}", receiptId);
        return Optional.ofNullable(receiptTable().getItem(
                b -> b.key(
                        k -> k.partitionValue(receiptId))));
    }

    public void addReceipt(Receipt receipt) {
        log.info("Adding/Updating receipt with ID: {}", receipt.getReceiptId());
        getReceiptById(receipt.getReceiptId())
                .ifPresentOrElse(r -> receiptTable().updateItem(receipt),
                        () -> receiptTable().putItem(receipt));
    }

    public void deleteReceiptById(String receiptId) {
        log.info("Deleting receipt with ID: {}", receiptId);
        receiptTable().deleteItem(r -> r.key(k -> k.partitionValue(receiptId)));
    }

    public List<ReceiptResponse> getReceiptsByUserName(String username) {
        log.info("Fetching receipts for user: {}", username);
        return receiptTable()
                .scan()
                .items()
                .stream()
                .filter(r -> r.getUser() != null && r.getUser().getUsername().equals(username))
                .map(ReceiptResponse::from)
                .toList();
    }
}
