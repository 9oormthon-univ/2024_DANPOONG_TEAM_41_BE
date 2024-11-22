package com.allgoing.domain.review.controller;

import com.allgoing.domain.cat.dto.response.ExpResponse;
import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.dto.ReviewDto;
import com.allgoing.domain.review.dto.request.ReviewRequestDto;
import com.allgoing.domain.review.service.ReviewService;
import com.allgoing.domain.user.domain.repository.UserRepository;
import com.allgoing.global.payload.ApiResponse;
import com.allgoing.global.util.S3Util;
import io.swagger.v3.oas.annotations.Operation;
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
    private final UserRepository userRepository;

    @Operation(summary = "리뷰 작성", description = "리뷰 작성 요청(로그인 기능 적용 전이므로 1번유저 고정)")
    @PostMapping("/create/{storeId}")
    public ResponseEntity<ApiResponse> createReview(
            @PathVariable Long storeId,
            @Validated @RequestPart(value = "review") ReviewRequestDto.Review review,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(errors.getAllErrors())
                            .build()
            );
        }
        try {
            ExpResponse catExp = reviewService.createReview(review, 1L, storeId, files);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(catExp)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제 요청(로그인 기능 적용 전이므로 1번유저 고정)")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId, 1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information("Review deleted successfully")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "리뷰 상세보기", description = "리뷰 상세보기 요청")
    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<ApiResponse> detailReview(@PathVariable Long reviewId) {
        try {
            //임시로 1번 유저를 받음
            ReviewDto reviewDto = reviewService.detailReview(reviewId, 1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(reviewDto)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "시장 전체 가게의 리뷰 보기", description = "시장 전체 가게의 리뷰 보기 요청")
    @GetMapping("/traditional/{traditionalId}")
    public ResponseEntity<ApiResponse> traditionalReview(@PathVariable Long traditionalId) {
        try {
            //임시로 1번 유저를 받음
            List<ReviewDto> reviewDtoList = reviewService.traditionalReview(traditionalId, 1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(reviewDtoList)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "내가 쓴 리뷰 보기", description = "내가 쓴 리뷰 보기(로그인 기능 적용 전이므로 1번유저 고정)")
    @GetMapping("/myreview")
    public ResponseEntity<ApiResponse> myReview() {
        try {
            List<ReviewDto> reviewDtoList = reviewService.myReview(1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(reviewDtoList)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "리뷰 좋아요 하기", description = "리뷰 좋아요 요청(로그인 기능 적용 전이므로 1번유저 고정)")
    @PostMapping("/like/{reviewId}")
    public ResponseEntity<ApiResponse> likeReview(@PathVariable Long reviewId) {
        try {
            reviewService.likeReview(reviewId, 1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information("Review liked successfully")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰 좋아요 취소(로그인 기능 적용 전이므로 1번유저 고정)")
    @DeleteMapping("/like/{reviewId}")
    public ResponseEntity<ApiResponse> unlikeReview(@PathVariable Long reviewId) {
        try {
            reviewService.unlikeReview(reviewId, 1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information("Review unliked successfully")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "좋아요 한 리뷰 보기", description = "좋아요 한 리뷰 보기(로그인 기능 적용 전이므로 1번유저 고정)")
    @GetMapping("/like")
    public ResponseEntity<ApiResponse> likeReviewList() {
        try {
            List<ReviewDto> reviewDtos = reviewService.likeReviewList(1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(reviewDtos)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }
}
