package com.allgoing.domain.reservation.dto.request;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@Data
public class ReservationRequest {
    private LocalDate reservationDate;
    private LocalTime reservationTime;

    // 상품을 받기 위한 내부 클래스
    @Data
    public static class ProductRequest {
        private Long productId;
        private int quantity;
    }

    private List<ProductRequest> products;
}
