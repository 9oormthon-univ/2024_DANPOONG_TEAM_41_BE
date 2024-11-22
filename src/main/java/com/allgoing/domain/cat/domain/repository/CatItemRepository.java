package com.allgoing.domain.cat.domain.repository;

import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.cat.domain.CatItem;
import com.allgoing.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CatItemRepository extends JpaRepository<CatItem, Long> {
    ArrayList<CatItem> findByCat(Cat cat);

    // 정확한 경로를 지정
    CatItem findByItem_ItemId(Long itemId);

    Optional<CatItem> findByCatAndItem(Cat cat, Item item);
}
