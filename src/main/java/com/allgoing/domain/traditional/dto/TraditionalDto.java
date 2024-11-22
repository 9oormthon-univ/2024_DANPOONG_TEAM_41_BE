package com.allgoing.domain.traditional.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TraditionalDto {
    private Long traditionalId;

    private String traditionalName;

    private double traditionalLatitude;

    private double traditionalLongitude;
}
