package com.allgoing.domain.reservation.controller;

import com.allgoing.domain.cat.application.CatService;
import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.cat.dto.response.ExpResponse;
import com.allgoing.domain.product.dto.ProductDto;
import com.allgoing.domain.reservation.dto.request.ReservationRequest;
import com.allgoing.domain.reservation.dto.response.ReservationResponse;
import com.allgoing.domain.reservation.service.ReservationService;
import com.allgoing.global.config.security.token.CurrentUser;
import com.allgoing.global.config.security.token.UserPrincipal;
import com.allgoing.global.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
    private final CatService catService;

    //가게 예약내역 보기
    @Operation(summary = "가게 예약내역 보기", description = "이미 예약된 시간은 선택 못하도록")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getStoreReservations(@PathVariable Long storeId) {
        try {
            List<ReservationResponse> myReservations = reservationService.getStoreReservations(storeId);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(myReservations)
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

    //예약 과정중 해당 가게 상품 조회
    @Operation(summary = "해당 가게 상품 조회", description = "예약 과정중 해당 가게 상품을 선택 가능하게")
    @GetMapping("/product/{storeId}")
    public ResponseEntity<?> getStoreProduct(@PathVariable Long storeId) {
        try {
            List<ProductDto> storeProduct = reservationService.getStoreProduct(storeId);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(storeProduct)
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

    //예약하기
    @Operation(summary = "예약하기", description = "예약하기")
    @PostMapping("/{storeId}")
    public ResponseEntity<?> makeReservation(@Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
                                             @RequestBody ReservationRequest reservationRequest,
                                             @PathVariable Long storeId) {
        try {
//            reservationService.makeReservation(storeId, userId);
            reservationService.makeReservation(reservationRequest, storeId, userPrincipal);
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
    @Operation(summary = "예약 취소", description = "예약 취소")
    @PatchMapping("/{reservationId}")
    public ResponseEntity<?> cancelReservation(@Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
                                               @PathVariable Long reservationId) {
        try {
            //임시로 1번 유저 사용
//            reservationService.makeReservation(storeId, userId);
            reservationService.cancelReservation(reservationId, userPrincipal);
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

    //예약 후 방문완료 처리
    @Operation(summary = "예약 후 방문완료 처리", description = "예약 후 방문완료 처리(로그인 기능 적용 전이므로 1번유저 고정)")
    @PostMapping("/visited/{reservationId}")
    public ResponseEntity<?> visit(@Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
                                   @PathVariable Long reservationId) {
        try {
            //임시로 1번 유저 사용
//            reservationService.makeReservation(storeId, userId);
            ExpResponse expResponse = reservationService.visitReservation(reservationId, userPrincipal);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(expResponse)
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

    //내 예약 내역 보기
    @Operation(summary = "내 예약 내역 보기", description = "내 예약 내역 보기")
    @GetMapping("/my")
    public ResponseEntity<?> getMyReservations(@Parameter(description = "Access Token을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal) {
        try {
            //임시로 1번 유저 사용
//            reservationService.makeReservation(storeId, userId);
            List<ReservationResponse> myReservations = reservationService.getMyReservations(userPrincipal);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .check(true)
                            .information(myReservations)
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
