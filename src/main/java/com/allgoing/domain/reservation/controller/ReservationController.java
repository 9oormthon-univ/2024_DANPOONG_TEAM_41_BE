package com.allgoing.domain.reservation.controller;

import com.allgoing.domain.reservation.domain.Reservation;
import com.allgoing.domain.reservation.domain.request.ReservationRequest;
import com.allgoing.domain.reservation.service.ReservationService;
import com.allgoing.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "reservation", description = "예약 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    //예약하기
    @PostMapping("/{storeId}")
    public ResponseEntity<?> makeReservation(@RequestBody ReservationRequest reservationRequest, @PathVariable Long storeId) {
        try {
//            reservationService.makeReservation(storeId, userId);
            reservationService.makeReservation(reservationRequest, storeId, 1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information("Reservation made successfully")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }

    //예약 취소
    @PatchMapping("/{reservationId}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long reservationId) {
        try {
//            reservationService.makeReservation(storeId, userId);
            reservationService.cancelReservation(reservationId, 1L);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information("Reservation canceled successfully")
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .check(false)
                            .information(e.getMessage())
                            .build()
            );
        }
    }
}
