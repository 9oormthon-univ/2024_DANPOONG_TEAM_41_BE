package com.allgoing.domain.store.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="StoreImage")
@NoArgsConstructor
@Getter
public class StoreImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="store_image_id", updatable = false, nullable = false, unique = true)
    private Long storeImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_notice_id")
    private StoreNotice storeNotice;

    @Column(name="store_image_url")
    private String storeImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name="store_image_type")
    private StoreImageType storeImageType;


}
