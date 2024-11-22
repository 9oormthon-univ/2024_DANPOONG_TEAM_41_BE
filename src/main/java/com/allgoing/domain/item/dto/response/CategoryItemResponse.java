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
public class CategoryItemResponse {
    @Schema( type = "Long", example = "1", description="아이템 아이디입니다.")
    private Long itemId;

    @Schema( type = "String", example = "리본", description="아이템 이름입니다.")
    private String itemName;

    @Schema( type = "Long", example = "1000", description="아이템 가격입니다.")
    private Long itemPrice;

    @Schema( type = "Boolean", example = "true", description="아이템 보유 여부입니다.")
    private Boolean isOwned;

    @Schema( type = "Boolean", example = "true", description="아이템 착용 여부입니다.")
    private Boolean isEquipped;
}
