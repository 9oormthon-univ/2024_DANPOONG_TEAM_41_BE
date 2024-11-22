package com.allgoing.domain.item.domain.repository;

import com.allgoing.domain.item.domain.Item;
import com.allgoing.domain.item.domain.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    ArrayList<Item> findByItemCategory(ItemCategory itemCategory);
}
