package com.allgoing.domain.cat.domain.repository;

import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.cat.domain.CatItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CatItemRepository extends JpaRepository<CatItem, Long> {
    ArrayList<CatItem> findByCat(Cat cat);
}
