package com.allgoing.domain.review.domain;

import com.allgoing.domain.common.BaseEntity;
import com.allgoing.domain.common.Status;
import com.allgoing.domain.store.domain.Store;
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

    @Column(name="star")
    private int star;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false) // Store와 연결
    private Store store; // Store 필드 추가

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewComment> reviewComments = new ArrayList<>();

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    @Builder
    public Review(User user, String reviewTitle, String reviewContent, int likeCount, Store store, int star) {
        this.user = user;
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.likeCount = likeCount;
        this.store = store;
        this.star = star;
    }
    
    public String getWriterName(){
        if (this.getUser().getStatus() == Status.ACTIVE){
            return user.getName();
        }else{
            return "탈퇴한 유저";
        }
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }
}
