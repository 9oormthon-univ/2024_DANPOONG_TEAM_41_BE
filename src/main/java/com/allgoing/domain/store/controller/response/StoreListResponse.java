package com.allgoing.domain.store.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreListResponse {
    private Long storeId;
    private String storeName;
    private String storeIntro;
    private String storeAddress;
    private double storeLatitude;
    private double storeLongitude;
}
