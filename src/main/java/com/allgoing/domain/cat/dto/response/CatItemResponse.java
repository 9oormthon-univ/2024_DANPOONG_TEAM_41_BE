package com.allgoing.domain.cat.dto.response;

import com.allgoing.domain.item.domain.ItemCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CatItemResponse {
    @Schema( type = "long", example = "1", description="아이템 아이디입니다.")
    private Long itemId;

    @Schema( type = "enum", example = "ACCESSORIES", description="아이템 카테고리 정보입니다.")
    private ItemCategory itemCategory;

    @Schema( type = "string", example = "리본", description="아이템 이름입니다.")
    private String itemName;

    @Schema( type = "long", example = "1000", description="아이템 가격입니다.")
    private Long itemPrice;
}
