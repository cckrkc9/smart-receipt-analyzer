package com.cancikrikci.receiptprocessor.util;

import com.cancikrikci.receiptprocessor.entity.Receipt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class ExpenseClassifier {
    private final ChatClient chatClient;

    private final String prompt = """
        You are a receipt parser.
        Given the following OCR text, extract fields and return JSON compatible with this model:
        
        {
          "receiptId": String or null,
          "originalFileName": String or null,
          "s3Url": String or null,
          "textContent": String,
          "totalAmount": Double or null,
          "currency": TL,
          "categories": [String], // deduplicate categories, e.g., ["gıda","giyim"]
          "createdAt": String or null // ISO-8601 format if available (e.g., 2025-10-17T15:30:00Z)
        }
        
        Rules:
        - If a field is not found in the text, set it to "null".
        - Do not invent data.
        - Ensure "categories" contains unique lowercase category names only.
        - Detect categories based on item types (e.g., "gıda", "giyim", "kozmetik", "elektronik").
        - Do not include userId or user fields in the response.
        - Return only valid JSON without any extra commentary.
        
        OCR Text:
        """;

    public ExpenseClassifier(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    public Receipt textractModifier(Receipt receipt) {
        var content = receipt.getTextContent();
        return chatClient.prompt()
                .user(prompt + content)
                .call()
                .entity(Receipt.class);
    }
}
