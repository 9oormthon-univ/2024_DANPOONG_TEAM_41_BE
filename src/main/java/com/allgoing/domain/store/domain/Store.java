package com.allgoing.domain.store.domain;

import com.allgoing.domain.product.domain.Product;
import com.allgoing.domain.reservation.domain.Reservation;
import com.allgoing.domain.review.domain.Review;
import com.allgoing.domain.traditional.domain.Traditional;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

@Entity
@Table(name="Store")
@NoArgsConstructor
@Getter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="store_id", updatable = false, nullable = false, unique = true)
    private Long storeId;

    @Column(name="store_name")
    private String storeName;

    @Column(name="store_intro")
    private String storeIntro;

    // 가게 주소 (ex. 도로명 주소)
    @Column(name="store_address")
    private String storeAddress;

    // 위도
    @Column(name="store_latitude")
    private double storeLatitude;

    // 경도
    @Column(name="store_longitude")
    private double storeLongitude;

    @Column(name="store_phone")
    private String storePhone;

    @Column(name="store_image_url")
    private String storeImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traditional_id")
    private Traditional traditional;

    @Setter
    @Column(name = "star", nullable = false)
    private Double star = 0.0; // 별점 기본값을 0으로 설정

    // 가게 소식
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<StoreNotice> storeNotices = new ArrayList<>();

    // 가게 이미지
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<StoreImage> storeImages = new ArrayList<>();

    // 가게 영업 정보
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<StoreInfo> storeInfos = new ArrayList<>();

    // 가게 리뷰
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Review> storeReviews = new ArrayList<>();

    // 가게 예약
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Reservation> storeCategories = new ArrayList<>();

    // 상품
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();


}
