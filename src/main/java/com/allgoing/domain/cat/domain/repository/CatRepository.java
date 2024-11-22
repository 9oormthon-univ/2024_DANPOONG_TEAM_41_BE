package com.allgoing.domain.cat.domain.repository;

import com.allgoing.domain.cat.domain.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatRepository extends JpaRepository<Cat, Long> {
    Optional<Cat> findByUserId(Long id);
}
