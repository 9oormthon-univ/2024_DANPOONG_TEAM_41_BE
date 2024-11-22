package com.allgoing.domain.review.repository;

import com.allgoing.domain.review.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUserUserId(Long userId);
}
