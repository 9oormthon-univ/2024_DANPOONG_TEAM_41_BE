package com.allgoing.domain.store.service;

import com.allgoing.domain.product.dto.ProductDto;
import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewImage;
import com.allgoing.domain.store.controller.response.StoreHomeResponse;
import com.allgoing.domain.store.controller.response.StoreListResponse;
import com.allgoing.domain.store.controller.response.StoreNoticeResponse;
import com.allgoing.domain.store.controller.response.StoreSummaryResponse;
import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.domain.StoreImage;
import com.allgoing.domain.store.dto.StoreImageDto;
import com.allgoing.domain.store.dto.StoreInfoDto;
import com.allgoing.domain.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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

        StoreSummaryResponse storeSummaryResponse = StoreSummaryResponse.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeInfos(store.getStoreInfos())
                .storeIntro(store.getStoreIntro())
                .reviewCount(store.getStoreReviews().size())
                .storeImageUrl(getImageUrl(store))
                .build();

        return storeSummaryResponse;
    }

    public StoreHomeResponse getStoreHome(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 가게 정보 없음 id: " + storeId));

        List<ProductDto> productDtos = store.getProducts().stream()
                .map(product -> ProductDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .productPrice(product.getProductPrice())
                        .productImageUrl(product.getProductImageUrl())
                        .build())
                .toList();

        List<StoreImageDto> storeImageDtos = store.getStoreImages().stream()
                .map(storeImage -> StoreImageDto.builder()
                        .storeImageUrl(storeImage.getStoreImageUrl())
                        .storeImageType(storeImage.getStoreImageType()) // StoreImageType 추가
                        .build())
                .toList();

        List<StoreInfoDto> storeInfoDtos = store.getStoreInfos().stream()
                .map(storeInfo -> StoreInfoDto.builder()
                        .day(storeInfo.getDay())
                        .isOpen(storeInfo.isOpen())
                        .openTime(storeInfo.getOpenTime())
                        .closeTime(storeInfo.getCloseTime())
                        .build())
                .toList();

        return StoreHomeResponse.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeIntro(store.getStoreIntro())
                .storeAddress(store.getStoreAddress())
                .storePhone(store.getStorePhone())
                .products(productDtos)
                .storeImages(storeImageDtos)
                .storeInfos(storeInfoDtos)
                .build();
    }

    public List<StoreNoticeResponse> getStoreNotice(Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 가게 정보 없음 id: " + storeId));
        List<StoreNoticeResponse> storeNoticeList = store.getStoreNotices().stream()
                .map(notice -> StoreNoticeResponse.builder()
                        .storeNoticeContent(notice.getStoreNoticeContent())
                        .storeId(notice.getStore().getStoreId())
                        .createdAt(notice.getCreatedAt())
                        .build())
                .toList();
        return storeNoticeList;
    }






    private String getImageUrl(Store store) {
        // 1. 가게 이미지가 있는 경우
        List<StoreImage> storeImages = store.getStoreImages();
        if (!storeImages.isEmpty()) {
            return storeImages.get(0).getStoreImageUrl(); // 첫 번째 가게 이미지 반환
        }

        // 2. 가게 이미지가 없고 리뷰 이미지가 있는 경우
        List<Review> storeReviews = store.getStoreReviews();
        if (!storeReviews.isEmpty()) {
            for (Review review : storeReviews) {
                List<ReviewImage> reviewImages = review.getReviewImages();
                if (!reviewImages.isEmpty()) {
                    return reviewImages.get(0).getReviewImageUrl(); // 첫 번째 리뷰 이미지 반환
                }
            }
        }

        // 3. 가게 이미지도, 리뷰 이미지도 없는 경우
        return null;
    }
}
