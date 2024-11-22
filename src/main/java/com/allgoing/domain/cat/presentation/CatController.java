package com.allgoing.domain.cat.presentation;

import com.allgoing.domain.cat.application.CatService;
import com.allgoing.domain.cat.dto.response.ExpResponse;
import com.allgoing.global.config.security.token.CurrentUser;
import com.allgoing.global.config.security.token.UserPrincipal;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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




}