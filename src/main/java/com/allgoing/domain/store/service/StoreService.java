package com.allgoing.domain.store.service;

<<<<<<< HEAD
import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewImage;
import com.allgoing.domain.store.controller.response.StoreListResponse;
import com.allgoing.domain.store.controller.response.StoreSummaryResponse;
import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.domain.StoreImage;
=======
import com.allgoing.domain.store.controller.response.StoreListResponse;
import com.allgoing.domain.store.controller.response.StoreSummaryResponse;
import com.allgoing.domain.store.domain.Store;
>>>>>>> 1dcdc9e3a1bb7513b40e94d923e40772c1a2c0cb
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

<<<<<<< HEAD
        StoreSummaryResponse storeSummaryResponse = StoreSummaryResponse.builder()
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeInfos(store.getStoreInfos())
                .storeIntro(store.getStoreIntro())
                .reviewCount(store.getStoreReviews().size())
                .storeImageUrl(getImageUrl(store))
                .build();

        return storeSummaryResponse;
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

=======
        return new StoreSummaryResponse(
                store.getStoreName(),
                store.getStoreIntro(),
                store.getStoreAddress(),
                store.getStoreInfos(),
                store.getStoreReviews().size()
        );
    }
>>>>>>> 1dcdc9e3a1bb7513b40e94d923e40772c1a2c0cb
}
