package com.allgoing.domain.store.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name="StoreInfo")
@NoArgsConstructor
@Getter
public class StoreInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="store_info_id", updatable = false, nullable = false, unique = true)
    private Long storeInfoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 요일
    @Column(name="day")
    private String day;

    // 영업 여부
    @Column(name="is_open")
    private boolean isOpen;

    // 영업 시작 시간
    @Column(name="open_time")
    private LocalTime openTime;

    // 영업 종료 시간
    @Column(name="close_time")
    private LocalTime closeTime;



}
