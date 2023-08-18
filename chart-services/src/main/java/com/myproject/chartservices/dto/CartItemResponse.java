package com.myproject.chartservices.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {
    private Long productId;
    private String productName;
    private Float productPrice;
    private Integer quantity;
    private Timestamp created_at;
    private Timestamp updated_at;
}
