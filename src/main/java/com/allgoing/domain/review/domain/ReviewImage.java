package com.allgoing.domain.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Review_Image")
@NoArgsConstructor
@Getter
public class ReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_image_id", updatable = false, nullable = false, unique = true)
    private Long reviewImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name="review_image_url")
    private String reviewImageUrl;

    @Builder
    public ReviewImage(Review review, String reviewImageUrl){
        this.review = review;
        this.reviewImageUrl = reviewImageUrl;
    }
}
