package com.allgoing.domain.store.domain;

import com.allgoing.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="StoreNotice")
@NoArgsConstructor
@Getter
public class StoreNotice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="store_notice_id", updatable = false, nullable = false, unique = true)
    private Long storeNoticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name="store_notice_content")
    private String storeNoticeContent;

    @OneToMany(mappedBy = "storeNotice", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<StoreImage> storeNoticeImages = new ArrayList<>();

}
