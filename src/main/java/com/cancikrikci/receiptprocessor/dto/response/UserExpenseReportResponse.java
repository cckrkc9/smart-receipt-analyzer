package com.cancikrikci.receiptprocessor.dto.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExpenseReportResponse {
    
    @JsonProperty("Toplam")
    private Double totalAmount;
    
    private Map<String, Double> categories;
    
    public UserExpenseReportResponse(Map<String, Double> categoryTotals) {
        this.categories = categoryTotals;
        this.totalAmount = categoryTotals.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }
    
    @JsonAnyGetter
    public Map<String, Double> getCategories() {
        return categories;
    }
}

