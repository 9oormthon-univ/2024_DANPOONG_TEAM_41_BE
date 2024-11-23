package com.allgoing.domain.store.dto.request;

import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StoreCreatRequest {
    private String storeName;
    private String storeIntro;
    private String storeAddress;
    private double storeLatitude;
    private double storeLongitude;
    private String storePhone;

    private Long traditionalId;

    // 영업일을 받기 위한 내부 클래스
    @Data
    public static class StoreInfoDto {
        private String day;
        private boolean isOpen;
        private LocalTime openTime;
        private LocalTime closeTime;
    }

    private List<StoreInfoDto> storeInfos;
}
