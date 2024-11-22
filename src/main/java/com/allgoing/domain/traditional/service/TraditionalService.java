package com.allgoing.domain.traditional.service;

import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.dto.response.StoreListResponse;
import com.allgoing.domain.store.dto.response.StoreListResponse.StoreListResponseBuilder;
import com.allgoing.domain.traditional.domain.Traditional;
import com.allgoing.domain.traditional.dto.TraditionalDto;
import com.allgoing.domain.traditional.repository.TraditionalRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Builder
@RequiredArgsConstructor
public class TraditionalService {
    private final TraditionalRepository traditionalRepository;

    @Transactional
    public List<TraditionalDto> allTraditional() {
        List<TraditionalDto> traditionalDtos = traditionalRepository.findAll().stream()
                .map(traditional -> TraditionalDto.builder()
                        .traditionalId(traditional.getTraditionalId())
                        .traditionalName(traditional.getTraditionalName())
                        .traditionalLatitude(traditional.getTraditionalLatitude())
                        .traditionalLongitude(traditional.getTraditionalLongitude())
                        .build()).toList();
        return traditionalDtos;
    }

    @Transactional
    public List<StoreListResponse> allTraditionalStores(Long traditionalId) {
        Traditional traditional = traditionalRepository.findById(traditionalId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 전통시장 정보 없음 id: " + traditionalId));
        List<StoreListResponse> storeLists = traditional.getStore().stream()
                .map(store -> StoreListResponse.builder()
                        .storeId(store.getStoreId())
                        .storeName(store.getStoreName())
                        .storeIntro(store.getStoreIntro())
                        .star(store.getStar())
                        .storeLatitude(store.getStoreLatitude())
                        .storeLongitude(store.getStoreLongitude())
                        .storeAddress(store.getStoreAddress()).build())
                .toList();
        return storeLists;
    }
}
