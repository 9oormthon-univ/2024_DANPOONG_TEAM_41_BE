package com.allgoing.domain.item.dto.response;

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
public class CategoryItemListResponse {
    @Schema( type = "list", description="악세서리 카테고리의 아이템 목록입니다")
    private List<CategoryItemResponse> accessories;

    @Schema( type = "list", description="신발 카테고리의 아이템 목록입니다")
    private List<CategoryItemResponse> shoes;
}
