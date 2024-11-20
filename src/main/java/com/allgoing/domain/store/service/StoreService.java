package com.allgoing.domain.store.service;

import com.allgoing.domain.store.controller.response.StoreListResponse;
import com.allgoing.domain.store.controller.response.StoreSummaryResponse;
import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public List<StoreListResponse> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream()
                .map(store -> new StoreListResponse(
                        store.getStoreId(),
                        store.getStoreName(),
                        store.getStoreIntro(),
                        store.getStoreAddress(),
                        store.getStoreLatitude(),
                        store.getStoreLongitude()
                ))
                .collect(Collectors.toList());
    }

    public StoreSummaryResponse getStoreSummary(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 가게 정보 없음 id: " + storeId));

        return new StoreSummaryResponse(
                store.getStoreName(),
                store.getStoreIntro(),
                store.getStoreAddress(),
                store.getStoreInfos(),
                store.getStoreReviews().size()
        );
    }
}
