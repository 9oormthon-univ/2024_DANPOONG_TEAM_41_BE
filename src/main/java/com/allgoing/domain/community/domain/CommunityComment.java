package com.allgoing.domain.community.domain;

import com.allgoing.domain.common.BaseEntity;
import com.allgoing.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Community_Comment")
@NoArgsConstructor
@Getter
public class CommunityComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="community_comment_id", updatable = false, nullable = false, unique = true)
    private Long communityCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Column(name="community_comment_content")
    private String communityCommentContent;

    @Column(name="writer_name")
    private String writerName;

    @Builder
    public CommunityComment(User user, Community community, String communityCommentContent){
        this.user = user;
        this.community = community;
        this.communityCommentContent = communityCommentContent;
        this.writerName = user.getName();
    }
}
