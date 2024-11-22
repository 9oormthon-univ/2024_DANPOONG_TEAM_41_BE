package com.allgoing.domain.community.domain.repository;

import com.allgoing.domain.community.domain.Community;
import com.allgoing.domain.community.domain.CommunityImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityImageRepository extends JpaRepository<CommunityImage, Long> {
    CommunityImage findFirstByCommunity(Community community);
}
