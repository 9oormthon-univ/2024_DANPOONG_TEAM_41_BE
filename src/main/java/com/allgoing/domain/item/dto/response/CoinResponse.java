package com.allgoing.domain.item.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoinResponse {
    @Schema( type = "Long", example = "1000", description="보유 코인 수입니다.")
    private Long coin;
}
