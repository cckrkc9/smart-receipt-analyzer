package com.cancikrikci.receiptprocessor.exception;

public class ReceiptNotFoundException extends ResourceNotFoundException{
    public ReceiptNotFoundException(String receiptId) {
        super("Receipt", receiptId);
    }
}
