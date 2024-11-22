package com.allgoing.domain.community.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostDetailResponse {
    @Schema( type = "Long", example = "1", description="게시글 아이디입니다.")
    private Long postId;

    @Schema( type = "String", example = "작성자", description="게시글 작성자입니다.")
    private String writer;

    @Schema( type = "String", example = "제목", description="게시글 제목입니다.")
    private String title;

    @Schema( type = "String", example = "내용", description="게시글 내용입니다.")
    private String content;

    @Schema( type = "String", example = "2021-08-01", description="게시글 작성일입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Schema( type = "list", description = "게시글 이미지 url 리스트입니다.")
    private List<String> imageUrlList;

    @Schema( type = "int", example = "10", description="게시글 좋아요 수입니다.")
    private int likeCount;

    @Schema( type = "int", example = "25", description="게시글 댓글 수입니다.")
    private int commentCount;

    @Schema( type = "Boolean", example = "true", description="게시글 좋아요 여부입니다.")
    private Boolean isLiked;

    @Schema( type = "list", description = "게시글 댓글 리스트입니다.")
    private List<CommentResponse> commentList = new ArrayList<>();
}
