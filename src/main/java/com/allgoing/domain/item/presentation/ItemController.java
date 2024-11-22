package com.allgoing.domain.item.presentation;

import com.allgoing.domain.cat.dto.response.ExpResponse;
import com.allgoing.domain.item.application.ItemService;
import com.allgoing.domain.item.dto.response.CategoryItemListResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
@Tag(name = "Item", description = "아이템 관련 API입니다.")
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "카테고리별 아이템 목록 조회 API", description = "카테고리별 아이템 목록을 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 목록 조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryItemListResponse.class) ) } ),
            @ApiResponse(responseCode = "400", description = "아이템 목록 조회 조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/list")
    public ResponseEntity<?> getCategoryItems(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return itemService.getCategoryItems(userPrincipal);
    }


}
