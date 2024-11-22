package com.allgoing.domain.review.repository;

import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewLike;
import com.allgoing.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Boolean existsByReviewAndUser(Review review, User user);
    Optional<ReviewLike> findByReviewAndUser(Review review, User user);
}

