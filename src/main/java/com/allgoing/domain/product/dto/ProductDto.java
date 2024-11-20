package com.allgoing.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductDto {
    private Long productId;

    private String productName;

    private int productPrice;

    private String productImageUrl;
}
