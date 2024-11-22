package com.allgoing.domain.store.repository;

import com.allgoing.domain.store.domain.StoreNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreNoticeRepository extends JpaRepository<StoreNotice, Long> {
}
