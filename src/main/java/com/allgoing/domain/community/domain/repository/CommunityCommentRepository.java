package com.allgoing.domain.community.domain.repository;

import com.allgoing.domain.community.domain.Community;
import com.allgoing.domain.community.domain.CommunityComment;
import com.allgoing.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
    List<CommunityComment> findAllByCommunity(Community community);

    List<CommunityComment> findAllByUser(User user);
}
