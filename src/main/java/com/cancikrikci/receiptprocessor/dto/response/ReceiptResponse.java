package com.cancikrikci.receiptprocessor.dto.response;

import com.cancikrikci.receiptprocessor.entity.Receipt;

import java.util.List;

public record ReceiptResponse(
        String receiptId,
        List<String> categories,
        String currency,
        Double totalAmount,
        String userName,
        String userEmail
) {
    public static ReceiptResponse from(Receipt receipt) {
        String userName = receipt.getUser() != null ? receipt.getUser().getUsername() : null;
        String userEmail = receipt.getUser() != null ? receipt.getUser().getEmail() : null;
        
        return new ReceiptResponse(
                receipt.getReceiptId(),
                receipt.getCategories(),
                receipt.getCurrency(),
                receipt.getTotalAmount(),
                userName,
                userEmail
        );
    }
}