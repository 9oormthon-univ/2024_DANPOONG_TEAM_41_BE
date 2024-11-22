package com.allgoing.domain.community.application;

import com.allgoing.domain.community.domain.Community;
import com.allgoing.domain.community.domain.CommunityImage;
import com.allgoing.domain.community.domain.CommunityLike;
import com.allgoing.domain.community.domain.repository.CommunityImageRepository;
import com.allgoing.domain.community.domain.repository.CommunityLikeRepository;
import com.allgoing.domain.community.domain.repository.CommunityRepository;
import com.allgoing.domain.community.dto.request.NewPostRequest;
import com.allgoing.domain.community.dto.response.PostListResponse;
import com.allgoing.domain.user.domain.User;
import com.allgoing.domain.user.domain.repository.UserRepository;
import com.allgoing.global.config.security.token.UserPrincipal;
import com.allgoing.global.payload.ApiResponse;
import com.allgoing.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommunityImageRepository communityImageRepository;
    private final CommunityLikeRepository communityLikeRepository;

    private final S3Util s3Util;

    @Transactional
    public ResponseEntity<?> createNewPost(UserPrincipal userPrincipal, NewPostRequest newPostRequest, List<MultipartFile> files) {
        User user = getUser(userPrincipal);

        // 게시글 생성
        Community community = Community.builder()
                .user(user)
                .postTitle(newPostRequest.getTitle())
                .postContent(newPostRequest.getContent())
                .build();

        // 게시글 저장
        communityRepository.save(community);

        // 생성된 게시글에 이미지 목록 업로드 및 저장
        uploadFiles(community, files);

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information("게시글 작성 성공")
                .build();

        return ResponseEntity.ok(response);
    }


    private void uploadFiles(Community community, List<MultipartFile> files) {
        if(files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String imageUrl = s3Util.upload(file);

                CommunityImage communityImage = CommunityImage.builder()
                        .community(community)
                        .communityImageUrl(imageUrl)
                        .build();

                communityImageRepository.save(communityImage);

            }
        }
    }

    public ResponseEntity<?> getPostList(UserPrincipal userPrincipal) {
        User user = getUser(userPrincipal);

        List<Community> communityList = communityRepository.findAll();
        ArrayList<PostListResponse> postListResponse = new ArrayList<>();

        for(Community community : communityList) {
            PostListResponse postList = PostListResponse.builder()
                    .postId(community.getCommunityId())
                    .title(community.getPostTitle())
                    .content(community.getPostContent())
                    .createdAt(community.getCreatedAt().toString())
                    .thumbnailUrl(getFirstImage(community).getCommunityImageUrl())
                    .likeCount(community.getLikeCount())
                    .commentCount(getCommentCount(community))
                    .isLiked(isLiked(user, community))
                    .build();

            postListResponse.add(postList);
        }

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(postListResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    private Boolean isLiked(User user, Community community) {
        return communityLikeRepository.existsByUserAndCommunity(user, community);

    }

    private int getCommentCount(Community community) {
        return community.getCommunityComments().size();
    }

    private CommunityImage getFirstImage(Community community) {
        return communityImageRepository.findFirstByCommunity(community);
    }



    private User getUser(UserPrincipal userPrincipal) {
//      return userRepository.findById(userPrincipal.getId())
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    }

}
