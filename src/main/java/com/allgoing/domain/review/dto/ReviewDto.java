package com.allgoing.domain.review.dto;

import com.allgoing.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReviewDto {
    private Long reviewId;

    private String reviewTitle;

    private String reviewContent;

    private int likeCount;

    private String writerName;

    private List<ReviewImageDto> reviewImages;

    private Boolean liked;

    private LocalDateTime createdAt;

    private int star;

    private Long userId;

    private Long storeId;


}
