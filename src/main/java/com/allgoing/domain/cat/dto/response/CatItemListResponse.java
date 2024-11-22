package com.allgoing.domain.cat.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CatItemListResponse {
    @Schema( type = "long", example = "13900", description="보유 코인 수량입니다.")
    private Long coin;

    @Schema( type = "list", description="보유한 아이템 목록입니다.")
    private List<CatItemResponse> catItems;
}
