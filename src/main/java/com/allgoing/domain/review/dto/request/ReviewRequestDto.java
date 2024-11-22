package com.allgoing.domain.review.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReviewRequestDto {

    @Getter
    @Setter
    public static class Review {

        @NotEmpty(message = "제목은 필수 입력값입니다.")
        private String reviewTitle;

        @NotEmpty(message = "내용은 필수 입력값입니다.")
        private String reviewContent;

        @NotEmpty(message = "별점은 필수 입력값입니다.")
        private int star;
    }
}
