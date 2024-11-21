package com.allgoing.domain.review.service;

import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewImage;
import com.allgoing.domain.review.dto.request.ReviewRequestDto;
import com.allgoing.domain.review.repository.ReviewImageRepository;
import com.allgoing.domain.review.repository.ReviewRepository;
import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.repository.StoreRepository;
import com.allgoing.domain.user.domain.User;
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

        // 1. Review 엔티티 생성 및 저장
        Review newReview = Review.builder()
                .user(user)
                .reviewTitle(review.getReviewTitle())
                .reviewContent(review.getReviewContent())
                .store(store)
                .build();

        reviewRepository.save(newReview);

        // 2. 이미지 파일 처리 및 S3 업로드
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String imageUrl = s3Util.upload(file);
                // ReviewImage 엔티티 생성
                ReviewImage reviewImage = ReviewImage.builder()
                        .review(newReview)
                        .reviewImageUrl(imageUrl)
                        .build();

                // Review 엔티티에 이미지 추가
                //newReview.getReviewImages().add(reviewImage);
                reviewImageRepository.save(reviewImage);
            }
        }else {
            System.out.println("sadfsadfsadf");
        }

        // 저장된 Review 엔티티에 이미지가 추가된 상태로 다시 저장
        reviewRepository.save(newReview);
    }
}

