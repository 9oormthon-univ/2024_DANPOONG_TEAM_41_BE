package com.allgoing.domain.product.domain;

import com.allgoing.domain.store.domain.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Product")
@NoArgsConstructor
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id", updatable = false, nullable = false, unique = true)
    private Long productId;

    @Column(name="product_name")
    private String productName;

    @Column(name="product_price")
    private int productPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name="product_image_url")
    private String productImageUrl;

}
