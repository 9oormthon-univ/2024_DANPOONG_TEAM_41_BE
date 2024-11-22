package com.allgoing.domain.community.presentation;

import com.allgoing.domain.community.application.CommunityService;
import com.allgoing.domain.community.dto.request.NewCommentRequest;
import com.allgoing.domain.community.dto.request.NewPostRequest;
import com.allgoing.domain.community.dto.response.PostDetailResponse;
import com.allgoing.global.config.security.token.CurrentUser;
import com.allgoing.global.config.security.token.UserPrincipal;
import com.allgoing.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Tag(name = "Post", description = "정보/질문 게시판 관련 API입니다.")
public class CommunityController {
    private final CommunityService communityService;

    @Operation(summary = "게시글 작성 API", description = "정보/질문 게시판에 게시글을 작성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 작성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "게시글 작성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/new")
    public ResponseEntity<?> createNewPost(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "게시글 제목과 내용을 입력해주세요.", required = true) @RequestPart NewPostRequest newPostRequest,
            @Parameter(description = "게시글에 첨부할 이미지 파일을 입력해주세요.", required = false) @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        return communityService.createNewPost(userPrincipal, newPostRequest, files);
    }


    @Operation(summary = "게시글 목록 조회 API", description = "정보/질문 게시판에서 게시글 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArrayList.class) ) } ),
            @ApiResponse(responseCode = "400", description = "게시글 목록 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/list")
    public ResponseEntity<?> getPostList(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return communityService.getPostList(userPrincipal);
    }

    @Operation(summary = "게시글 상세 조회 API", description = "정보/질문 게시판에서 특정 게시글의 내용을 조회하는 API입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PostDetailResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "게시글 상세 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(
        @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
        @Parameter(description = "게시글 아이디를 입력해주세요.", required = true) @PathVariable Long postId
    ) {
        return communityService.getPostDetail(userPrincipal, postId);
    }


    @Operation(summary = "게시글 댓글 작성 API", description = "정보/질문 게시판에서 특정 게시글에 댓글을 작성하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 댓글 작성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "게시글 댓글 작성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/{postId}/comment")
    public ResponseEntity<?> createComment(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "게시글 아이디를 입력해주세요.", required = true) @PathVariable Long postId,
            @Parameter(description = "게시글에 작성할 댓글을 입력해주세요.", required = true) @RequestBody NewCommentRequest newCommentRequest
    ) {
        return communityService.createComment(userPrincipal, postId, newCommentRequest);
    }

    @Operation(summary = "게시글 좋아요 수정 API", description = "정보/질문 게시판에서 특정 게시글에 좋아요를 등록/취소하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "좋아요 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> like(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "게시글 아이디를 입력해주세요.", required = true) @PathVariable Long postId
    ) {
        return communityService.thumsUp(userPrincipal, postId);
    }

    @Operation(summary = "내가 작성한 게시글 조회 API", description = "정보/질문 게시판에서 내가 작성한 게시글을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성한 게시글 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArrayList.class) ) } ),
            @ApiResponse(responseCode = "400", description = "작성한 게시글 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/my/list")
    public ResponseEntity<?> getMyPost(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return communityService.getMyPost(userPrincipal);
    }


    @Operation(summary = "내가 좋아요를 등록한 게시글 조회 API", description = "정보/질문 게시판에서 내가 좋아요를 등록한 게시글을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 등록 게시글 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArrayList.class) ) } ),
            @ApiResponse(responseCode = "400", description = "좋아요 등록 게시글 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/my/like")
    public ResponseEntity<?> getMyLikedPost(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return communityService.getMyLikedPost(userPrincipal);
    }



}
