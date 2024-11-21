package com.allgoing.domain.traditional.domain;

import com.allgoing.domain.store.domain.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Traditional")
@NoArgsConstructor
@Getter
public class Traditional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="traditional_id", updatable = false, nullable = false, unique = true)
    private Long traditionalId;

    // 전통시장 이름
    @Column(name="traditional_name")
    private String traditionalName;

    // 위도
    @Column(name="traditional_latitude")
    private double traditionalLatitude;

    // 경도
    @Column(name="traditional_longitude")
    private double traditionalLongitude;

    // 포함하는 가게
    @OneToMany(mappedBy = "traditional", fetch = FetchType.LAZY)
    private List<Store> store;
}
