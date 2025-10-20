package com.cancikrikci.receiptprocessor.service;

import com.cancikrikci.receiptprocessor.dto.response.ReceiptResponse;
import com.cancikrikci.receiptprocessor.dto.response.UserExpenseReportResponse;
import com.cancikrikci.receiptprocessor.entity.Receipt;
import com.cancikrikci.receiptprocessor.repository.ReceiptRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReceiptService {
    private final ReceiptRepository receiptRepository;

    public List<ReceiptResponse> getAllReceipts() {
        return receiptRepository.getAllReceipts();
    }

    public Optional<Receipt> getReceiptById(String receiptId) {
        return receiptRepository.getReceiptById(receiptId);
    }

    public void addReceipt(Receipt receipt) {
       receiptRepository.addReceipt(receipt);
    }

    public void deleteReceiptById(String receiptId) {
      receiptRepository.deleteReceiptById(receiptId);
    }

    public List<ReceiptResponse> getReceiptsByUserName(String username) {
        return receiptRepository.getReceiptsByUserName(username);
    }

    public UserExpenseReportResponse getUserExpenseReport(String username) {
        var receipts = receiptRepository.getReceiptsByUserName(username);
        Map<String, Double> categoryTotals = new HashMap<>();

        receipts.forEach(receipt -> {
            if (receipt.categories() != null && !receipt.categories().isEmpty()) {
                double amountPerCategory = receipt.totalAmount() / receipt.categories().size();
                for (String category : receipt.categories()) {
                    categoryTotals.merge(category, amountPerCategory, Double::sum);
                }
            }
        });

        return new UserExpenseReportResponse(categoryTotals);
    }
}
