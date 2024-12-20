package com.allgoing.domain.item.presentation;


import com.allgoing.domain.item.application.ItemService;
import com.allgoing.domain.item.dto.response.CategoryItemListResponse;
import com.allgoing.domain.item.dto.response.CoinResponse;
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

    @Operation(summary = "아이템 구매 API", description = "아이템샵에서 코인을 통해 아이템을 구매하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이템 구매 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "아이템 구매 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/{itemId}")
    public ResponseEntity<?> getCategoryItems(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "구매할 아이템의 ID를 입력해주세요.", required = true) @PathVariable Long itemId
    ) {
        return itemService.buyItem(userPrincipal, itemId);
    }

    @Operation(summary = "보유 코인 정보 조회 API", description = "보유 코인 정보를 조회하는 API입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "착용 아이템 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CoinResponse.class) ) } ),
            @ApiResponse(responseCode = "400", description = "착용 아이템 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/coin")
    public ResponseEntity<?> getCoinInfo(
            @Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return itemService.getCoinInfo(userPrincipal);
    }



}
