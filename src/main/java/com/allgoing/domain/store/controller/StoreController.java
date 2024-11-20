package com.allgoing.domain.store.controller;

import com.allgoing.domain.store.controller.response.StoreSummaryResponse;
import com.allgoing.domain.store.controller.response.StoreListResponse;
import com.allgoing.domain.store.service.StoreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "store", description = "가게 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreService storeService;

    //가게 전체 조회(지도 위의 핀을 만들 때 사용)
    @GetMapping("/allsummaries")
    public ResponseEntity<List<StoreListResponse>> getAllStoreSummaries() {
        List<StoreListResponse> allStores = storeService.getAllStores();
        return ResponseEntity.ok(allStores);
    }

    //가게 정보 간단 조회(지도 위의 핀 클릭시)
    @GetMapping("/summary/{storeId}")
    public ResponseEntity<StoreSummaryResponse> getStoreSummary(@PathVariable Long storeId) {
        StoreSummaryResponse storeSummaryResponse = storeService.getStoreSummary(storeId);
        return ResponseEntity.ok(storeSummaryResponse);
    }
    
    //가게 정보 전체 상세 조회(핀 클릭 후 가게명 클릭시)

}