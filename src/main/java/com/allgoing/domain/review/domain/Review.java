package com.allgoing.domain.review.domain;

import com.allgoing.domain.common.BaseEntity;
import com.allgoing.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Review")
@NoArgsConstructor
@Getter
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id", updatable = false, nullable = false, unique = true)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="review_title")
    private String reviewTitle;

    @Column(name="review_content")
    private String reviewContent;

    @Column(name="like_count")
    private int likeCount;

    @Column(name="writer_name")
    private String writerName;

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewComment> reviewComments = new ArrayList<>();

    @Builder
    public Review(User user, String reviewTitle, String reviewContent){
        this.user = user;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.likeCount = 0;
        this.writerName = user.getName();
    }


}