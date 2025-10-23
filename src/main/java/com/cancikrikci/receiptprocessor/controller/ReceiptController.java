package com.cancikrikci.receiptprocessor.controller;

import com.cancikrikci.receiptprocessor.dto.response.ReceiptResponse;
import com.cancikrikci.receiptprocessor.dto.response.UserExpenseReportResponse;
import com.cancikrikci.receiptprocessor.entity.Receipt;
import com.cancikrikci.receiptprocessor.exception.ReceiptNotFoundException;
import com.cancikrikci.receiptprocessor.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/receipt")
public class ReceiptController {
    private final ReceiptService receiptService;

    @GetMapping
    public ResponseEntity<List<ReceiptResponse>> getAllReceipts() {
        var receipts = receiptService.getAllReceipts();

        return receipts.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(receipts);
    }

    @GetMapping("{id}")
    public ResponseEntity<Receipt> getReceiptById(@PathVariable("id") String receiptId) {
        var receipt =  receiptService.getReceiptById(receiptId)
                    .orElseThrow(() -> new ReceiptNotFoundException(receiptId));

        return ResponseEntity.ok(receipt);
    }

    @PostMapping("add")
    public ResponseEntity<String> createReceipt(@RequestBody Receipt receipt) {
        receiptService.addReceipt(receipt);

        return ResponseEntity.ok("Receipt saved successfully!");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteReceipt(@PathVariable("id") String receiptId) {
        receiptService.deleteReceiptById(receiptId);

        return ResponseEntity.ok("Receipt deleted successfully!");
    }

    @GetMapping("user/{name}")
    public ResponseEntity<List<ReceiptResponse>> getUserReceiptByUserId(@PathVariable("name") String userName) {
        var receipts = receiptService.getReceiptsByUserName(userName);

        return receipts.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(receipts);
    }

    @GetMapping("user/report")
    public ResponseEntity<UserExpenseReportResponse> getUserExpenseReport() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserExpenseReportResponse report = receiptService.getUserExpenseReport(username);

        return ResponseEntity.ok(report);
    }
}
