package com.allgoing.domain.review.service;

import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewImage;
import com.allgoing.domain.review.domain.ReviewLike;
import com.allgoing.domain.review.dto.ReviewDto;
import com.allgoing.domain.review.dto.ReviewImageDto;
import com.allgoing.domain.review.dto.request.ReviewRequestDto;
import com.allgoing.domain.review.repository.ReviewImageRepository;
import com.allgoing.domain.review.repository.ReviewLikeRepository;
import com.allgoing.domain.review.repository.ReviewRepository;
import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.repository.StoreRepository;
import com.allgoing.domain.traditional.domain.Traditional;
import com.allgoing.domain.traditional.repository.TraditionalRepository;
import com.allgoing.domain.user.domain.User;
import com.allgoing.domain.user.domain.repository.UserRepository;
import com.allgoing.global.error.DefaultException;
import com.allgoing.global.payload.ErrorCode;
import com.allgoing.global.util.S3Util;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Builder
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final TraditionalRepository traditionalRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final S3Util s3Util;

    //리뷰 등록
    @Transactional
    public void createReview(ReviewRequestDto.Review review, Long userId, Long storeId, List<MultipartFile> files) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 가게 정보 없음 id: " + storeId));

        synchronized (store) {
            int reviewNum = store.getStoreReviews().size();
            double starAvg = store.getStar() != null ? store.getStar() : 0.0;

            // 별점 평균 계산
            double newAvg = (starAvg * reviewNum + review.getStar()) / (reviewNum + 1);
            store.setStar(newAvg);
        }

        Review newReview = Review.builder()
                .user(userRepository.getById(userId))
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .store(store)
                .likeCount(0)
                .star(review.getStar())
                .writerName(userRepository.getById(userId).getName())
                .build();

        reviewRepository.save(newReview);

        // 이미지 파일 처리 및 S3 업로드
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String imageUrl = s3Util.upload(file);

                    ReviewImage reviewImage = ReviewImage.builder()
                            .review(newReview)
                            .reviewImageUrl(imageUrl)
                            .build();

                    reviewImageRepository.save(reviewImage);
                }
            }
        }
    }


    //리뷰삭제
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 리뷰 없음 id: " + reviewId));

        if (!review.getUser().equals(userRepository.getById(userId))) {
            throw new DefaultException(ErrorCode.INVALID_AUTHENTICATION, "리뷰 작성자만 삭제 가능합니다");
        }

        Store store = review.getStore();

        //별점 업데이트
        synchronized (store) {
            int reviewCount = store.getStoreReviews().size();
            double currentStarAvg = store.getStar() != null ? store.getStar() : 0.0;

            //새로운 평균 계산
            if (reviewCount > 1) {
                double newStarAvg = (currentStarAvg * reviewCount - review.getStar()) / (reviewCount - 1);
                store.setStar(newStarAvg);
            } else {
                // 마지막 리뷰 삭제 시 별점 초기화
                store.setStar(0.0);
            }
        }

        // 리뷰 이미지 삭제
        List<ReviewImage> reviewImages = review.getReviewImages();
        if (!reviewImages.isEmpty()) {
            for (ReviewImage reviewImage : reviewImages) {
                s3Util.deleteFile(reviewImage.getReviewImageUrl());
            }
        }

        reviewRepository.delete(review);
    }


    @Transactional
    public ReviewDto detailReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 리뷰 없음 id: " + reviewId));

        // User 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 유저 없음 id: " + userId));

        return ReviewDto.builder()
                .reviewId(review.getReviewId())
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .reviewImages(review.getReviewImages().stream()
                        .map(image -> ReviewImageDto.builder()
                                .reviewImageId(image.getReviewImageId())
                                .reviewId(review.getReviewId())
                                .reviewImageUrl(image.getReviewImageUrl())
                                .build())
                        .toList())
                .storeId(review.getStore().getStoreId())
                .likeCount(review.getLikeCount())
                .userId(review.getUser().getId())
                .writerName(review.getWriterName())
                .liked(reviewLikeRepository.existsByReviewAndUser(review, user))
                .createdAt(review.getCreatedAt())
                .star(review.getStar())
                .build();
    }

    @Transactional
    public List<ReviewDto> traditionalReview(Long traditionalId, Long userId) {
        Traditional traditional = traditionalRepository.findById(traditionalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 전통시장 없음 id: " + traditionalId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 유저 없음 id: " + userId));

        List<Store> stores = traditional.getStore();
        List<ReviewDto> reviewDtos = new ArrayList<>();
        for (Store store : stores) {
            List<ReviewDto> list = store.getStoreReviews().stream()
                    .map(review -> ReviewDto.builder()
                            .reviewTitle(review.getReviewTitle())
                            .reviewContent(review.getReviewContent())
                            .reviewId(review.getReviewId())
                            .storeId(store.getStoreId())
                            .likeCount(review.getLikeCount())
                            .writerName(review.getWriterName())
                            .userId(review.getUser().getId())
                            .liked(reviewLikeRepository.existsByReviewAndUser(review, user))
                            .createdAt(review.getCreatedAt())
                            .star(review.getStar())
                            .reviewImages(review.getReviewImages().stream()
                                    .map(reviewImage -> ReviewImageDto.builder()
                                            .reviewId(review.getReviewId())
                                            .reviewImageId(reviewImage.getReviewImageId())
                                            .reviewImageUrl(reviewImage.getReviewImageUrl())
                                            .build()).toList())
                            .build()).toList();
            reviewDtos.addAll(list);
        }
        return reviewDtos;
    }

    @Transactional
    public List<ReviewDto> myReview(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 유저 없음 id: " + userId));

        List<ReviewDto> ReviewDtoList = reviewRepository.findAllByUserId(userId).stream()
                .map(review -> ReviewDto.builder()
                        .reviewTitle(review.getReviewTitle())
                        .reviewContent(review.getReviewContent())
                        .reviewId(review.getReviewId())
                        .storeId(review.getStore().getStoreId())
                        .likeCount(review.getLikeCount())
                        .writerName(review.getWriterName())
                        .userId(review.getUser().getId())
                        .liked(reviewLikeRepository.existsByReviewAndUser(review, user))
                        .createdAt(review.getCreatedAt())
                        .star(review.getStar())
                        .reviewImages(review.getReviewImages().stream()
                                .map(reviewImage -> ReviewImageDto.builder()
                                        .reviewId(review.getReviewId())
                                        .reviewImageId(reviewImage.getReviewImageId())
                                        .reviewImageUrl(reviewImage.getReviewImageUrl())
                                        .build()).toList())
                        .build())
                .toList();
        return ReviewDtoList;
    }


    @Transactional
    public void likeReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 리뷰 없음 id: " + reviewId));

        // User 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 유저 없음 id: " + userId));

        // 이미 좋아요한 경우 예외 처리
        if (reviewLikeRepository.existsByReviewAndUser(review, user)) {
            throw new DefaultException(ErrorCode.INVALID_PARAMETER, "이미 좋아요를 누른 상태입니다.");
        }

        // ReviewLike 생성 및 저장
        ReviewLike reviewLike = new ReviewLike(user, review);
        reviewLikeRepository.save(reviewLike);

        // 좋아요 카운트 증가
        review.incrementLikeCount();
        reviewRepository.save(review);
    }

    @Transactional
    public void unlikeReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 리뷰 없음 id: " + reviewId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 유저 없음 id: " + userId));

        // 좋아요 기록 찾기
        ReviewLike reviewLike = reviewLikeRepository.findByReviewAndUser(review, user)
                .orElseThrow(() -> new EntityNotFoundException("좋아요 기록이 존재하지 않습니다."));

        // 좋아요 삭제
        reviewLikeRepository.delete(reviewLike);

        // 좋아요 카운트 감소
        review.decrementLikeCount();
        reviewRepository.save(review);
    }

    @Transactional
    public List<ReviewDto> likeReviewList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 유저 없음 id: " + userId));

        List<ReviewDto> allLikeReview = reviewLikeRepository.findAllByUserId(userId).stream()
                .map(reviewLike -> ReviewDto.builder()
                        .reviewTitle(reviewLike.getReview().getReviewTitle())
                        .reviewContent(reviewLike.getReview().getReviewContent())
                        .reviewId(reviewLike.getReview().getReviewId())
                        .likeCount(reviewLike.getReview().getLikeCount())
                        .writerName(reviewLike.getReview().getWriterName())
                        .storeId(reviewLike.getReview().getStore().getStoreId())
                        .userId(reviewLike.getReview().getUser().getId())
                        .liked(true)
                        .createdAt(reviewLike.getReview().getCreatedAt())
                        .star(reviewLike.getReview().getStar())
                        .reviewImages(reviewLike.getReview().getReviewImages().stream()
                                .map(reviewImage -> ReviewImageDto.builder()
                                        .reviewImageUrl(reviewImage.getReviewImageUrl())
                                        .reviewImageId(reviewImage.getReviewImageId())
                                        .reviewId(reviewImage.getReviewImageId())
                                        .build()).toList())
                        .build()).toList();
        return allLikeReview;
    }

//    public List<Review> allReiews() {
//        return reviewRepository.findAll();
//    }
}