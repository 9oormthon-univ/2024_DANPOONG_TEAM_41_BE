package com.allgoing.domain.store.repository;

import com.allgoing.domain.store.domain.StoreInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreInfoReposiory extends JpaRepository<StoreInfo, Integer> {
}
