package com.myproject.chartservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCartRequest {
    private Long userId;
    private Long productId;
    private Integer quantity;
}
