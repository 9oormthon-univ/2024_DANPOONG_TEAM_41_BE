package com.allgoing.domain.review.controller;

import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.dto.request.ReviewRequestDto;
import com.allgoing.domain.review.service.ReviewService;
import com.allgoing.domain.user.domain.User;
import com.allgoing.domain.user.domain.repository.UserRepository;
import com.allgoing.global.payload.ApiResponse;
import com.allgoing.global.util.S3Util;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "review", description = "리뷰 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final S3Util s3Util;

    //임시
    private final UserRepository userRepository;

    //리뷰 작성
    @PostMapping("/create/{storeId}")
    public ResponseEntity<?> createReview(@PathVariable Long storeId,
                                          @Validated @RequestPart(value = "review") ReviewRequestDto.Review review,
                                          @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                          Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors());
        }
//        User user = userRepository.getById(1L);

        try {
            // 리뷰 생성 서비스 호출
            reviewService.createReview(review, 1L, storeId, files);

            // 성공 응답 반환
            return ResponseEntity.ok("Review created successfully");
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //리뷰 삭제
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
//        User user = userRepository.getById(1L);
        try {
            reviewService.deleteReview(reviewId, 1L);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //모든 리뷰 보기
    @GetMapping("/all")
    public ApiResponse getAllReviews() {
        List<Review> reviews = reviewService.allReiews();
        return ApiResponse.builder().check(true).information(reviews).build();
    }
}