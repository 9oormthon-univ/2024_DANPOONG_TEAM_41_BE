package com.allgoing.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {
    @Schema( type = "Long", example = "1", description="댓글 아이디입니다.")
    private Long commentId;

    @Schema( type = "string", example = "2021-08-01", description="댓글 작성일입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Schema( type = "String", example = "작성자", description="댓글 작성자입니다.")
    private String writer;

    @Schema( type = "Boolean", example = "true", description="게시글 작성자 여부")
    private Boolean isWriter;

    @Schema( type = "String", example = "내용", description="댓글 내용입니다.")
    private String content;
}
