package com.allgoing.domain.product.repository;

import com.allgoing.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
