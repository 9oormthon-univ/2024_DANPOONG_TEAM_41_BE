package com.allgoing.domain.store.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StoreInfoDto {
    // 요일
    private String day;

    // 영업 여부
    private boolean isOpen;

    // 영업 시작 시간
    private LocalTime openTime;

    // 영업 종료 시간
    private LocalTime closeTime;
}
