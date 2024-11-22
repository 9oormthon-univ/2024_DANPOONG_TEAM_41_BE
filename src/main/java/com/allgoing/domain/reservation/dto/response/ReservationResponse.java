package com.allgoing.domain.reservation.dto.response;

import com.allgoing.domain.reservation.domain.ReservationStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ReservationResponse {
    private Long reservationId;
    private ReservationStatus reservationStatus;
    private LocalDate reservationDate;
    private LocalTime reservationTime;

    private Long storeId;
    private String storeName;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ProductResponse {
        private Long productId;
        private String productName;
        private int quantity;
    }

    private List<ProductResponse> products;
}
