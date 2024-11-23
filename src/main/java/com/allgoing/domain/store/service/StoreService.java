package com.allgoing.domain.store.service;

import com.allgoing.domain.product.dto.ProductDto;
import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.review.domain.ReviewImage;
import com.allgoing.domain.review.dto.ReviewImageDto;
import com.allgoing.domain.review.dto.response.StoreReviewResponse;
import com.allgoing.domain.store.domain.StoreInfo;
import com.allgoing.domain.store.dto.request.StoreCreatRequest;
import com.allgoing.domain.store.dto.response.StoreHomeResponse;
import com.allgoing.domain.store.dto.response.StoreListResponse;
import com.allgoing.domain.store.dto.response.StoreNoticeResponse;
import com.allgoing.domain.store.dto.response.StoreSummaryResponse;
import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.domain.StoreImage;
import com.allgoing.domain.store.dto.StoreImageDto;
import com.allgoing.domain.store.dto.StoreInfoDto;
import com.allgoing.domain.store.repository.StoreImageRepository;
import com.allgoing.domain.store.repository.StoreInfoReposiory;
import com.allgoing.domain.store.repository.StoreRepository;
import com.allgoing.domain.traditional.repository.TraditionalRepository;
import com.allgoing.global.util.S3Util;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Builder
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;
    private final TraditionalRepository traditionalRepository;
    private final S3Util s3Util;
    private final StoreImageRepository storeImageRepository;
    private final StoreInfoReposiory storeInfoReposiory;

    public List<StoreListResponse> getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return stores.stream()
                .map(store -> new StoreListResponse(
                        store.getStoreId(),
                        store.getStoreName(),
                        store.getStoreIntro(),
                        store.getStoreAddress(),
                        store.getStar(),
                        store.getStoreLatitude(),
                        store.getStoreLongitude()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public StoreSummaryResponse getStoreSummary(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 가게 정보 없음 id: " + storeId));

        StoreSummaryResponse storeSummaryResponse = StoreSummaryResponse.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeInfos(store.getStoreInfos().stream()
                        .map(storeInfo -> StoreInfoDto.builder()
                                .day(storeInfo.getDay())
                                .isOpen(storeInfo.isOpen())
                                .openTime(storeInfo.getOpenTime())
                                .closeTime(storeInfo.getCloseTime())
                                .build()).toList())
                .storeIntro(store.getStoreIntro())
                .reviewCount(store.getStoreReviews().size())
                .storeImageUrl(getImageUrl(store))
                .star(store.getStar())
                .build();

        return storeSummaryResponse;
    }

    @Transactional
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
                .star(store.getStar())
                .build();
    }

    @Transactional
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

    @Transactional
    public List<StoreReviewResponse> getStoreReview(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 가게 정보 없음 id: " + storeId));
        List<StoreReviewResponse> storeReviewList = store.getStoreReviews().stream()
                .map(review -> StoreReviewResponse.builder()
                        .reviewId(review.getReviewId())
                        .storeId(review.getStore().getStoreId())
                        .userId(review.getUser().getId())
                        .reviewTitle(review.getReviewTitle())
                        .reviewContent(review.getReviewContent())
                        .likeCount(review.getLikeCount())
                        .writerName(review.getWriterName())
                        .commentCount(review.getReviewComments().size())
                        .reviewImages(review.getReviewImages().stream()
                                .map(image -> ReviewImageDto.builder()
                                        .reviewImageId(image.getReviewImageId())
                                        .reviewId(review.getReviewId())
                                        .reviewImageUrl(image.getReviewImageUrl())
                                        .build())
                                .toList())
                        .createdAt(review.getCreatedAt())
                        .star(review.getStar())
                        .build())
                .toList();
        return storeReviewList;
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


    public void creatStore(StoreCreatRequest store, List<MultipartFile> files) {

        // Store 엔티티 생성
        Store savingStore = Store.builder()
                .storeName(store.getStoreName())
                .storeIntro(store.getStoreIntro())
                .storeAddress(store.getStoreAddress())
                .storeLatitude(store.getStoreLatitude())
                .storeLongitude(store.getStoreLongitude())
                .storePhone(store.getStorePhone())
                .traditional(traditionalRepository.findById(store.getTraditionalId())
                        .orElseThrow(() -> new IllegalArgumentException("해당하는 전통시장이 없습니다! ID: " + store.getTraditionalId())))
                .star(0.0)
                .build();
        Store savedStore = storeRepository.save(savingStore);

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String imageUrl = s3Util.upload(file);
                    System.out.println(imageUrl);
                    StoreImage storeImage = StoreImage.builder()
                            .storeImageUrl(imageUrl)
                            .store(savedStore)
                            .build();
                    System.out.println(storeImage.getStoreImageUrl());
                    storeImageRepository.save(storeImage);
                }
            }
        }

        List<StoreInfo> list = store.getStoreInfos().stream()
                .map(storeInfoDto -> StoreInfo.builder()
                        .day(storeInfoDto.getDay())
                        .isOpen(storeInfoDto.isOpen())
                        .openTime(storeInfoDto.getOpenTime())
                        .closeTime(storeInfoDto.getCloseTime())
                        .store(savedStore).build()).toList();

        storeInfoReposiory.saveAll(list);

    }
}