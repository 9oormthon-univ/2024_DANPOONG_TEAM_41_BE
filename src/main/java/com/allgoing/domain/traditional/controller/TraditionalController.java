package com.allgoing.domain.traditional.controller;

import com.allgoing.domain.store.dto.response.StoreListResponse;
import com.allgoing.domain.traditional.dto.TraditionalDto;
import com.allgoing.domain.traditional.service.TraditionalService;
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

    //모든 전통시장 정보 조회
    @Operation(summary = "모든 전통시장 정보 조회", description = "어떤 전통시장이 있는지 전체 조회")
    @GetMapping
    public ResponseEntity<?> getTraditional() {
        try {
            List<TraditionalDto> traditionalDtos = traditionalService.allTraditional();
            return ResponseEntity.ok(traditionalDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //해당 전통시장 ID에 해당하는 가게 모두 조회
    @Operation(summary = "전통시장 포함되는 가게 전체 조회", description = "전통시장의 ID를 받아 포함되는 가게 전체 조회")
    @GetMapping("/{traditionalId}")
    public ResponseEntity<?> getTraditionalById(@PathVariable Long traditionalId) {
        try {
            List<StoreListResponse> allStores = traditionalService.allTraditionalStores(traditionalId);
            return ResponseEntity.ok(allStores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
