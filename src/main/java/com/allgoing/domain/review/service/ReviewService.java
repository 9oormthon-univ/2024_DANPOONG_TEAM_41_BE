package com.allgoing.domain.review.service;

import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewImage;
import com.allgoing.domain.review.dto.ReviewDto;
import com.allgoing.domain.review.dto.ReviewImageDto;
import com.allgoing.domain.review.dto.request.ReviewRequestDto;
import com.allgoing.domain.review.repository.ReviewImageRepository;
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
    private final S3Util s3Util;

    //리뷰 등록
    @Transactional
    public void createReview(ReviewRequestDto.Review review, Long userId, Long storeId, List<MultipartFile> files) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 가게 정보 없음 id: " + storeId));

        //Review 엔티티 생성 및 저장
        Review newReview = Review.builder()
                .user(userRepository.getById(userId))
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .store(store)
                .likeCount(0)
                .writerName(userRepository.getById(userId).getName())
                .build();

        reviewRepository.save(newReview);

        //이미지 파일 처리 및 S3 업로드
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

        reviewRepository.save(newReview);
    }

    //리뷰삭제
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 리뷰 없음 id: " + reviewId));

        if(!review.getUser().equals(userRepository.getById(userId))) {
            throw new DefaultException(ErrorCode.INVALID_AUTHENTICATION, "리뷰 작성자만 삭제 가능합니다");
        }
        List<ReviewImage> reviewImages = review.getReviewImages();
        if(!reviewImages.isEmpty()) {
            for (ReviewImage reviewImage : reviewImages) {
                System.out.println(reviewImage.getReviewImageUrl());
                s3Util.deleteFile(reviewImage.getReviewImageUrl());
            }
        }
        reviewRepository.delete(review);
    }

    @Transactional
    public ReviewDto detailReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 리뷰 없음 id: " + reviewId));

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
                .userId(review.getUser().getUserId())
                .writerName(review.getWriterName())
                .build();
    }

    @Transactional
    public List<ReviewDto> traditionalReview(Long traditionalId) {
        Traditional traditional = traditionalRepository.findById(traditionalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 전통시장 없음 id: " + traditionalId));

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
                            .userId(review.getUser().getUserId())
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

        List<ReviewDto> ReviewDtoList = reviewRepository.findAllByUserUserId(userId).stream()
                .map(review -> ReviewDto.builder()
                        .reviewTitle(review.getReviewTitle())
                        .reviewContent(review.getReviewContent())
                        .reviewId(review.getReviewId())
                        .storeId(review.getStore().getStoreId())
                        .likeCount(review.getLikeCount())
                        .writerName(review.getWriterName())
                        .userId(review.getUser().getUserId())
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

    public List<Review> allReiews() {
        return reviewRepository.findAll();
    }


}