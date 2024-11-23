package com.allgoing.domain.community.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewPostRequest {
    @Schema( type = "String", example = "판교 맛집 추천해주세요", description="게시글 제목입니다.")
    private String title;

    @Schema( type = "String", example = "맛있는 떡볶이집 없나요?", description="게시글 내용입니다.")
    private String content;

}
