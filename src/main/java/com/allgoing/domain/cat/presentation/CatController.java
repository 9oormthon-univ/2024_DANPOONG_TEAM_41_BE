package com.allgoing.domain.cat.presentation;

import com.allgoing.domain.cat.application.CatService;
import com.allgoing.domain.cat.dto.request.PatchCatItemReq;
import com.allgoing.domain.cat.dto.response.CatItemListResponse;
import com.allgoing.domain.cat.dto.response.ExpResponse;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cat")
@Tag(name = "Cat", description = "고양이 관련 API입니다.")
public class CatController {
    private final CatService catService;

    @Operation(summary = "경험치 및 레벨 정보 조회 API", description = "경험치 및 레벨 정보를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경험치/레벨 정보 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExpResponse.class) ) } ),
            @ApiResponse(responseCode = "400", description = "경험치/레벨 정보 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/exp")
    public ResponseEntity<?> getExp(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return catService.getExp(userPrincipal);
    }

    @Operation(summary = "착용 아이템 조회 API", description = "고양이가 현재 착용한 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "착용 아이템 정보 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CatItemListResponse.class) ) } ),
            @ApiResponse(responseCode = "400", description = "착용 아이템 정보 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/my")
    public ResponseEntity<?> catItem(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return catService.getCatItems(userPrincipal);
    }

    @Operation(summary = "착용 아이템 수정 API", description = "고양이 착용 아이템을 수정하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "착용 아이템 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "착용 아이템 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping("/item")
    public ResponseEntity<?> catItem(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @RequestBody PatchCatItemReq patchCatItemReq
    ) {
        return catService.patchCatItem(userPrincipal, patchCatItemReq);
    }




}
