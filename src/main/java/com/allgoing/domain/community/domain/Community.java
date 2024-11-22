package com.allgoing.domain.community.domain;

import com.allgoing.domain.common.BaseEntity;
import com.allgoing.domain.review.domain.ReviewLike;
import com.allgoing.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Community")
@NoArgsConstructor
@Getter
public class Community extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="community_id", updatable = false, nullable = false, unique = true)
    private Long communityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="post_title")
    private String postTitle;

    @Column(name="post_content")
    private String postContent;

    @Column(name="like_count")
    private int likeCount;

    @Column(name="writer_name")
    private String writerName;

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    private List<CommunityImage> communityImages = new ArrayList<>();

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY)
    private List<CommunityComment> communityComments = new ArrayList<>();

    @OneToMany(mappedBy = "community", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityLike> communityLikes = new ArrayList<>();


    @Builder
    public Community(User user, String postTitle, String postContent){
        this.user = user;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.likeCount = 0;
        this.writerName = user.getName();
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() { this.likeCount--; }

}
