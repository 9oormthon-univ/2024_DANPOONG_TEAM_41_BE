package com.allgoing.domain.store.controller;

import com.allgoing.domain.review.dto.request.ReviewRequestDto;
import com.allgoing.domain.review.dto.response.StoreReviewResponse;
import com.allgoing.domain.store.dto.request.StoreCreatRequest;
import com.allgoing.domain.store.dto.response.StoreHomeResponse;
import com.allgoing.domain.store.dto.response.StoreNoticeResponse;
import com.allgoing.domain.store.dto.response.StoreSummaryResponse;
import com.allgoing.domain.store.dto.response.StoreListResponse;
import com.allgoing.domain.store.service.StoreService;
import com.allgoing.global.payload.ApiResponse;
import com.allgoing.global.payload.ErrorCode;
import com.allgoing.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "store", description = "가게 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store")
public class StoreController {
    private final StoreService storeService;

    @Operation(summary = "가게 전체 조회", description = "지도 위의 핀을 만들 때 사용")
    @GetMapping("/allsummaries")
    public ResponseEntity<ApiResponse> getAllStoreSummaries() {
        try {
            List<StoreListResponse> allStores = storeService.getAllStores();
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(allStores)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(ErrorResponse.of(ErrorCode.INVALID_PARAMETER, e.getMessage()))
                            .build()
            );
        }
    }

    @Operation(summary = "가게 정보 간단 조회", description = "지도 위의 핀 클릭시")
    @GetMapping("/summary/{storeId}")
    public ResponseEntity<ApiResponse> getStoreSummary(@PathVariable Long storeId) {
        try {
            StoreSummaryResponse storeSummary = storeService.getStoreSummary(storeId);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(storeSummary)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(404).body(
                    ApiResponse.builder()
                            .check(false)
                            .information(ErrorResponse.of(ErrorCode.INVALID_CHECK, e.getMessage()))
                            .build()
            );
        }
    }

    @Operation(summary = "가게 홈 조회", description = "핀 클릭 후 가게명 클릭시")
    @GetMapping("/home/{storeId}")
    public ResponseEntity<ApiResponse> getStoreHome(@PathVariable Long storeId) {
        try {
            StoreHomeResponse storeHomeResponse = storeService.getStoreHome(storeId);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(storeHomeResponse)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(404).body(
                    ApiResponse.builder()
                            .check(false)
                            .information(ErrorResponse.of(ErrorCode.INVALID_CHECK, e.getMessage()))
                            .build()
            );
        }
    }

    @Operation(summary = "가게 소식 조회", description = "소식 버튼 클릭시")
    @GetMapping("/notice/{storeId}")
    public ResponseEntity<ApiResponse> getStoreNotice(@PathVariable Long storeId) {
        try {
            List<StoreNoticeResponse> storeNoticeList = storeService.getStoreNotice(storeId);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(storeNoticeList)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(404).body(
                    ApiResponse.builder()
                            .check(false)
                            .information(ErrorResponse.of(ErrorCode.INVALID_CHECK, e.getMessage()))
                            .build()
            );
        }
    }

    @Operation(summary = "가게 리뷰 조회", description = "리뷰 버튼 클릭시")
    @GetMapping("/review/{storeId}")
    public ResponseEntity<ApiResponse> getStoreReview(@PathVariable Long storeId) {
        try {
            List<StoreReviewResponse> storeReviewList = storeService.getStoreReview(storeId);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(storeReviewList)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(404).body(
                    ApiResponse.builder()
                            .check(false)
                            .information(ErrorResponse.of(ErrorCode.INVALID_CHECK, e.getMessage()))
                            .build()
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> creatStore(
            @RequestPart(value = "store") StoreCreatRequest store,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            storeService.creatStore(store, files);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information("Store created successfully")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(404).body(
                    ApiResponse.builder()
                            .check(false)
                            .information(ErrorResponse.of(ErrorCode.INVALID_CHECK, e.getMessage()))
                            .build()
            );
        }
    }
}

