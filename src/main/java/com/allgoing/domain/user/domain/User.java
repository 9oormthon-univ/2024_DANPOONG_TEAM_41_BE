package com.allgoing.domain.user.domain;

import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.common.BaseEntity;
import com.allgoing.domain.community.domain.Community;
import com.allgoing.domain.community.domain.CommunityComment;
import com.allgoing.domain.reservation.domain.Reservation;
import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewComment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="User")
@NoArgsConstructor
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", updatable = false, nullable = false, unique = true)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name="profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ReviewComment> reviewComments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Community> communities = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<CommunityComment> communityComments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();

    @Builder
    public User(String name, String email, String password, Provider provider, String profileImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.profileImage = profileImage;
        this.role = Role.USER;
    }

}
