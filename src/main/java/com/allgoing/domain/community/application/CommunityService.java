package com.allgoing.domain.community.application;

import com.allgoing.domain.community.domain.Community;
import com.allgoing.domain.community.domain.CommunityComment;
import com.allgoing.domain.community.domain.CommunityImage;
import com.allgoing.domain.community.domain.CommunityLike;
import com.allgoing.domain.community.domain.repository.CommunityCommentRepository;
import com.allgoing.domain.community.domain.repository.CommunityImageRepository;
import com.allgoing.domain.community.domain.repository.CommunityLikeRepository;
import com.allgoing.domain.community.domain.repository.CommunityRepository;
import com.allgoing.domain.community.dto.request.NewCommentRequest;
import com.allgoing.domain.community.dto.request.NewPostRequest;
import com.allgoing.domain.community.dto.response.CommentResponse;
import com.allgoing.domain.community.dto.response.PostDetailResponse;
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
    private final CommunityCommentRepository communityCommentRepository;

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
                    .createdAt(community.getCreatedAt())
                    .thumbnailUrl(getFirstImage(community).getCommunityImageUrl())
                    .likeCount(community.getLikeCount())
                    .commentCount(getCommentCount(community))
                    .isLiked(isLiked(user, community))
                    .build();

            postListResponse.add(postList);
        }

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(sortPostList(postListResponse))
                .build();

        return ResponseEntity.ok(response);
    }


    private ArrayList<PostListResponse> sortPostList(ArrayList<PostListResponse> postListResponse) {
        // 게시글 생성일 기준으로 내림차순 정렬
        postListResponse.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
        return new ArrayList<>(postListResponse);
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

    public ResponseEntity<?> getPostDetail(UserPrincipal userPrincipal, Long postId) {
        User user = getUser(userPrincipal);
        Community community = getCommunity(postId);

        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .postId(community.getCommunityId())
                .writer(community.getWriterName())
                .title(community.getPostTitle())
                .content(community.getPostContent())
                .createdAt(community.getCreatedAt())
                .imageUrlList(getImageUrlList(community))
                .likeCount(community.getLikeCount())
                .commentCount(getCommentCount(community))
                .isLiked(isLiked(user, community))
                .commentList(getCommentList(community))
                .build();

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(postDetailResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    private ArrayList<String> getImageUrlList(Community community) {
        ArrayList<String> imageUrlList = new ArrayList<>();
        for(CommunityImage communityImage : community.getCommunityImages()) {
            imageUrlList.add(communityImage.getCommunityImageUrl());
        }
        return imageUrlList;
    }

    private ArrayList<CommentResponse> getCommentList(Community community) {
        List<CommunityComment> communityComments = communityCommentRepository.findAllByCommunity(community);
        ArrayList<CommentResponse> commentList = new ArrayList<>();

        for(CommunityComment communityComment : communityComments) {
            CommentResponse comment = CommentResponse.builder()
                    .commentId(communityComment.getCommunityCommentId())
                    .createdAt(communityComment.getCreatedAt())
                    .writer(communityComment.getUser().getName())
                    .isWriter(communityComment.getUser().equals(community.getUser()))
                    .content(communityComment.getCommunityCommentContent())
                    .build();
            commentList.add(comment);
        }

        return sortCommentList(commentList);
    }

    private ArrayList<CommentResponse> sortCommentList(List<CommentResponse> commentList) {
        // 댓글 생성일 기준으로 오름차순 정렬
        commentList.sort((o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
        return new ArrayList<>(commentList);
    }

    @Transactional
    public ResponseEntity<?> createComment(UserPrincipal userPrincipal, Long postId, NewCommentRequest newCommentRequest) {
        User user = getUser(userPrincipal);
        Community community = getCommunity(postId);

        CommunityComment communityComment = CommunityComment.builder()
                .user(user)
                .community(community)
                .communityCommentContent(newCommentRequest.getContent())
                .build();

        communityCommentRepository.save(communityComment);

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information("댓글 작성 완료")
                .build();

        return ResponseEntity.ok(response);

    }

    // 좋아요 수정 기능
    @Transactional
    public ResponseEntity<?> thumsUp(UserPrincipal userPrincipal, Long postId) {
        User user = getUser(userPrincipal);
        Community community = getCommunity(postId);
        String message;
        if(isLiked(user, community)) {
            removeThumsUp(user, community);
            message = "좋아요 취소 성공";
        }
        else {
            addThumsUp(user, community);
            message = "좋아요 등록 성공";
        }

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(message)
                .build();

        return ResponseEntity.ok(response);

    }

    // 좋아요 추가
    private void addThumsUp(User user, Community community) {
        CommunityLike communityLike = CommunityLike.builder()
                .user(user)
                .community(community)
                .build();
        communityLikeRepository.save(communityLike);
        community.incrementLikeCount();
    }

    // 좋아요 취소
    private void removeThumsUp(User user, Community community) {
        CommunityLike communityLike = communityLikeRepository.findByUserAndCommunity(user, community);
        communityLikeRepository.delete(communityLike);
        community.decrementLikeCount();
    }

    public ResponseEntity<?> getMyPost(UserPrincipal userPrincipal) {
        User user = getUser(userPrincipal);
        List<Community> communityList = communityRepository.findAllByUser(user);
        ArrayList<PostListResponse> postListResponse = new ArrayList<>();

        for(Community community : communityList) {
            PostListResponse postList = PostListResponse.builder()
                    .postId(community.getCommunityId())
                    .title(community.getPostTitle())
                    .content(community.getPostContent())
                    .createdAt(community.getCreatedAt())
                    .thumbnailUrl(getFirstImage(community).getCommunityImageUrl())
                    .likeCount(community.getLikeCount())
                    .commentCount(getCommentCount(community))
                    .isLiked(isLiked(user, community))
                    .build();

            postListResponse.add(postList);
        }

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(sortPostList(postListResponse))
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getMyLikedPost(UserPrincipal userPrincipal) {
        User user = getUser(userPrincipal);
        List<CommunityLike> communityLikeList = communityLikeRepository.findAllByUser(user);
        List<Community> communityList = new ArrayList<>();

        for(CommunityLike communityLike : communityLikeList) {
            communityList.add(communityLike.getCommunity());
        }

        ArrayList<PostListResponse> postListResponse = new ArrayList<>();

        for(Community community : communityList) {
            PostListResponse postList = PostListResponse.builder()
                    .postId(community.getCommunityId())
                    .title(community.getPostTitle())
                    .content(community.getPostContent())
                    .createdAt(community.getCreatedAt())
                    .thumbnailUrl(getFirstImage(community).getCommunityImageUrl())
                    .likeCount(community.getLikeCount())
                    .commentCount(getCommentCount(community))
                    .isLiked(isLiked(user, community))
                    .build();

            postListResponse.add(postList);
        }

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(sortPostList(postListResponse))
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getMyCommentPost(UserPrincipal userPrincipal) {
        User user = getUser(userPrincipal);
        List<CommunityComment> communityCommentList = communityCommentRepository.findAllByUser(user);
        List<Community> communityList = new ArrayList<>();

        for(CommunityComment communityComment : communityCommentList) {
            if(!communityList.contains(communityComment.getCommunity())) {
                communityList.add(communityComment.getCommunity());
            }
        }

        ArrayList<PostListResponse> postListResponse = new ArrayList<>();

        for(Community community : communityList) {
            PostListResponse postList = PostListResponse.builder()
                    .postId(community.getCommunityId())
                    .title(community.getPostTitle())
                    .content(community.getPostContent())
                    .createdAt(community.getCreatedAt())
                    .thumbnailUrl(getFirstImage(community).getCommunityImageUrl())
                    .likeCount(community.getLikeCount())
                    .commentCount(getCommentCount(community))
                    .isLiked(isLiked(user, community))
                    .build();

            postListResponse.add(postList);
        }

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(sortPostList(postListResponse))
                .build();

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> deletePost(UserPrincipal userPrincipal, Long postId) {
        User user = getUser(userPrincipal);
        Community community = getCommunity(postId);

        if(!community.getUser().equals(user)) {
            ApiResponse response = ApiResponse.builder()
                    .check(false)
                    .information("게시글 삭제 권한이 없습니다.")
                    .build();

            return ResponseEntity.badRequest().body(response);
        }

        communityRepository.delete(community);

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information("게시글 삭제 성공")
                .build();

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> deletePostComment(UserPrincipal userPrincipal, Long postId, Long commentId) {
        User user = getUser(userPrincipal);
        Community community = getCommunity(postId);
        CommunityComment communityComment = getComment(commentId);

        if(!communityComment.getUser().equals(user)) {
            ApiResponse response = ApiResponse.builder()
                    .check(false)
                    .information("댓글 삭제 권한이 없습니다.")
                    .build();

            return ResponseEntity.badRequest().body(response);
        }

        communityCommentRepository.delete(communityComment);

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information("댓글 삭제 성공")
                .build();

        return ResponseEntity.ok(response);

    }

    private User getUser(UserPrincipal userPrincipal) {
//      return userRepository.findById(userPrincipal.getId())
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return userRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    }

    private Community getCommunity(Long postId) {
        return communityRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    private CommunityComment getComment(Long commentId) {
        return communityCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

}
