package com.allgoing.domain.store.dto;

import com.allgoing.domain.store.domain.StoreImageType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StoreImageDto {
    private String storeImageUrl;

    private StoreImageType storeImageType;
}
