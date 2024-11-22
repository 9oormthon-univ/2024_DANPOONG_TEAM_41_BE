package com.allgoing.domain.reservation.domain;

import com.allgoing.domain.product.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ReservationProduct")
@NoArgsConstructor
@Getter
public class ReservationProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_product_id", updatable = false, nullable = false, unique = true)
    private Long reservationProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    public ReservationProduct(Reservation reservation, Product product, int quantity) {
        this.reservation = reservation;
        this.product = product;
        this.quantity = quantity;
    }
}