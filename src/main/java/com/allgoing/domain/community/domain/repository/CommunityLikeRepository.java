package com.allgoing.domain.community.domain.repository;

import com.allgoing.domain.community.domain.Community;
import com.allgoing.domain.community.domain.CommunityLike;
import com.allgoing.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    Boolean existsByUserAndCommunity(User user, Community community);
}
