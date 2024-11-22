package com.allgoing.domain.cat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExpResponse {
    @Schema( type = "int", example = "5", description="고양이의 레벨입니다.")
    private int level;

    @Schema( type = "long", example = "100", description="현재 레벨의 경험치입니다.")
    private Long catExp;
}
