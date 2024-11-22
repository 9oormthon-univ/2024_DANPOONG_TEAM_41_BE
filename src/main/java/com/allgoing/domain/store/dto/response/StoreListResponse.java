package com.allgoing.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StoreListResponse {
    private Long storeId;
    private String storeName;
    private String storeIntro;
    private String storeAddress;
    private double storeLatitude;
    private double storeLongitude;
}
