package com.allgoing.domain.review.service;

import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewImage;
import com.allgoing.domain.review.dto.request.ReviewRequestDto;
import com.allgoing.domain.review.repository.ReviewImageRepository;
import com.allgoing.domain.review.repository.ReviewRepository;
import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.repository.StoreRepository;
import com.allgoing.domain.user.domain.User;
import com.allgoing.global.error.DefaultException;
import com.allgoing.global.payload.ErrorCode;
import com.allgoing.global.util.S3Util;
import jakarta.persistence.EntityNotFoundException;
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
    private final ReviewImageRepository reviewImageRepository;
    private final S3Util s3Util;

    public void createReview(ReviewRequestDto.Review review, User user, Long storeId, List<MultipartFile> files) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 가게 정보 없음 id: " + storeId));

        //Review 엔티티 생성 및 저장
        Review newReview = Review.builder()
                .user(user)
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .store(store)
                .likeCount(0)
                .writerName(user.getName())
                .build();

        reviewRepository.save(newReview);

        //이미지 파일 처리 및 S3 업로드
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String imageUrl = s3Util.upload(file);

                ReviewImage reviewImage = ReviewImage.builder()
                        .review(newReview)
                        .reviewImageUrl(imageUrl)
                        .build();

                reviewImageRepository.save(reviewImage);
            }
        }

        reviewRepository.save(newReview);
    }

    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 리뷰 없음 id: " + reviewId));

        if(!review.getUser().equals(user)) {
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

//    public List<Revie> allReiews() {
//    }
}

