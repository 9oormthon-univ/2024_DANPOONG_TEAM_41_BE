package com.allgoing.domain.review.domain;

import com.allgoing.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Review_Comment")
@NoArgsConstructor
@Getter
public class ReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_comment_id", updatable = false, nullable = false, unique = true)
    private Long reviewCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @Column(name="review_comment_content")
    private String reviewCommentContent;

    @Column(name="writer_name")
    private String writerName;

    @Builder
    public ReviewComment(User user, Review review, String reviewCommentContent){
        this.user = user;
        this.review = review;
        this.reviewCommentContent = reviewCommentContent;
        this.writerName = user.getName();
    }
}
