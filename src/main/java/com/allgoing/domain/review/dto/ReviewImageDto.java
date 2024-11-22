package com.allgoing.domain.review.dto;

import com.allgoing.domain.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReviewImageDto {
    private Long reviewImageId;
    private Long reviewId;
    private String reviewImageUrl;
}
