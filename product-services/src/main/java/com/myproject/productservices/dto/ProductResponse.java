package com.myproject.productservices.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    
    private Long id;
    private String name;
    private Float price; 
    private Timestamp created_at;
    private Timestamp updated_at;
}
