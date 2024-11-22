package com.allgoing.domain.traditional.controller;

import com.allgoing.domain.store.dto.response.StoreListResponse;
import com.allgoing.domain.traditional.dto.TraditionalDto;
import com.allgoing.domain.traditional.service.TraditionalService;
import com.allgoing.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "traditional", description = "전통시장 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/traditional")
public class TraditionalController {
    private final TraditionalService traditionalService;

    @Operation(summary = "모든 전통시장 정보 조회", description = "어떤 전통시장이 있는지 전체 조회")
    @GetMapping
    public ResponseEntity<ApiResponse> getTraditional() {
        try {
            List<TraditionalDto> traditionalDtos = traditionalService.allTraditional();
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(traditionalDtos)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "전통시장 포함되는 가게 전체 조회", description = "전통시장의 ID를 받아 포함되는 가게 전체 조회")
    @GetMapping("/{traditionalId}")
    public ResponseEntity<ApiResponse> getTraditionalById(@PathVariable Long traditionalId) {
        try {
            List<StoreListResponse> allStores = traditionalService.allTraditionalStores(traditionalId);
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
                            .information(e.getMessage())
                            .build()
            );
        }
    }
}

