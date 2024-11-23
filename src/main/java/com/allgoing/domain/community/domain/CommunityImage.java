package com.allgoing.domain.community.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Community_Image")
@NoArgsConstructor
@Getter
public class CommunityImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="community_image_id", updatable = false, nullable = false, unique = true)
    private Long communityImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Column(name="community_image_url")
    private String communityImageUrl;

    @Builder
    public CommunityImage(Community community, String communityImageUrl){
        this.community = community;
        this.communityImageUrl = communityImageUrl;
    }
}
