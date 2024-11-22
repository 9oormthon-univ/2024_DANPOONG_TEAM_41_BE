package com.allgoing.domain.review.controller;

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

    //임시
    private final UserRepository userRepository;

    //리뷰 작성
    @Operation(summary = "리뷰 작성", description = "리뷰 작성 요청(로그인 기능 적용 전이므로 1번유저 고정)")
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
    @Operation(summary = "리뷰 삭제", description = "리뷰 삭제 요청(로그인 기능 적용 전이므로 1번유저 고정)")
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

    //리뷰 상세보기
    @Operation(summary = "리뷰 상세보기", description = "리뷰 상세보기 요청")
    @GetMapping("/detail/{reviewId}")
    public ResponseEntity<?> detailReview(@PathVariable Long reviewId) {
        try {
            ReviewDto reviewDto = reviewService.detailReview(reviewId);
            return ResponseEntity.ok(reviewDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //시장에 따른 리뷰보기
    @Operation(summary = "시장 전체 가게의 리뷰 보기", description = "시장 전체 가게의 리뷰 보기 요청")
    @GetMapping("/traditional/{traditionalId}")
    public ResponseEntity<?> traditionalReview(@PathVariable Long traditionalId) {
        try {
            List<ReviewDto> reviewDtoList = reviewService.traditionalReview(traditionalId);
            return ResponseEntity.ok(reviewDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //내가 쓴 리뷰 보기
    //로그인 미적용 관계로 1번 유저의 리뷰를 가져옴
    @Operation(summary = "내가 쓴 리뷰 보기", description = "내가 쓴 리뷰 보기(로그인 기능 적용 전이므로 1번유저 고정)")
    @GetMapping("/myreview")
    public ResponseEntity<?> myReview() {
        try {
//            User user = userRepository.getById(1L);
            List<ReviewDto> reviewDtoList = reviewService.myReview(1L);
            return ResponseEntity.ok(reviewDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 리뷰 좋아요
    @Operation(summary = "리뷰 좋아요 하기", description = "리뷰 좋아요 요청(로그인 기능 적용 전이므로 1번유저 고정)")
    @PostMapping("/like/{reviewId}")
    public ResponseEntity<?> likeReview(@PathVariable Long reviewId) {
        try {
            // 로그인 사용자 임시 ID 사용 (1L)
            reviewService.likeReview(reviewId, 1L);
            return ResponseEntity.ok("Review liked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // 리뷰 좋아요 취소
    @Operation(summary = "리뷰 좋아요 취소", description = "리뷰 좋아요 취소(로그인 기능 적용 전이므로 1번유저 고정)")
    @DeleteMapping("/like/{reviewId}")
    public ResponseEntity<?> unlikeReview(@PathVariable Long reviewId) {
        try {
            // 로그인 사용자 임시 ID 사용 (1L)
            reviewService.unlikeReview(reviewId, 1L);
            return ResponseEntity.ok("Review unliked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //좋아요 한 리뷰 보기
    @Operation(summary = "좋아요 한 리뷰 보기", description = "좋아요 한 리뷰 보기(로그인 기능 적용 전이므로 1번유저 고정)")
    @GetMapping("/like")
    public ResponseEntity<?> likeReview(){
        try {
            // 로그인 사용자 임시 ID 사용 (1L)
            List<ReviewDto> reviewDtos = reviewService.likeReviewList(1L);
            return ResponseEntity.ok(reviewDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



//    //모든 리뷰 보기
//    @Operation(summary = "모든 리뷰 보기", description = "모든 리뷰 보기")
//    @GetMapping("/all")
//    public ApiResponse getAllReviews() {
//        List<Review> reviews = reviewService.allReiews();
//        return ApiResponse.builder().check(true).information(reviews).build();
//    }
}