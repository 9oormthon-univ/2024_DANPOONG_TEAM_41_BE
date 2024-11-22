package com.allgoing.domain.review.dto.response;

import com.allgoing.domain.review.dto.ReviewImageDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StoreReviewResponse {
    private Long reviewId;
    private Long storeId;
    private Long userId;
    private String reviewTitle;
    private String reviewContent;
    private int likeCount;
    private String writerName;
    private int commentCount;
    private LocalDateTime createdAt;
    private int star;

    private List<ReviewImageDto> reviewImages;
}
