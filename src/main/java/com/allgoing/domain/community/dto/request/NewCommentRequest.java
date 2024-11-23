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
public class NewCommentRequest {

    @Schema( type = "String", example = "내용", description="댓글 내용입니다.")
    private String content;

}
