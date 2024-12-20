package com.allgoing.domain.reservation.service;

import com.allgoing.domain.cat.application.CatService;
import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.cat.domain.repository.CatRepository;
import com.allgoing.domain.cat.dto.response.ExpResponse;
import com.allgoing.domain.product.domain.Product;
import com.allgoing.domain.product.dto.ProductDto;
import com.allgoing.domain.product.repository.ProductRepository;
import com.allgoing.domain.reservation.domain.Reservation;
import com.allgoing.domain.reservation.domain.ReservationStatus;
import com.allgoing.domain.reservation.dto.request.ReservationRequest;
import com.allgoing.domain.reservation.dto.response.ReservationResponse;
import com.allgoing.domain.reservation.repository.ReservationRepository;
//import com.allgoing.domain.store.domain.repository.StoreRepository;
import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.store.repository.StoreRepository;
import com.allgoing.domain.user.domain.User;
import com.allgoing.domain.user.domain.repository.UserRepository;
import com.allgoing.global.config.security.token.UserPrincipal;
import com.allgoing.global.error.DefaultException;
import com.allgoing.global.payload.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CatService catService;
    private final CatRepository catRepository;

    @Transactional
    public void makeReservation(ReservationRequest reservationRequest, Long storeId, UserPrincipal userPrincipal) {
        // Store 가져오기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 가게 없음: " + storeId));

        User user = getUser(userPrincipal);

//        // User 가져오기
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 유저 없음: " + userId));

        // Reservation 생성 및 설정
        Reservation reservation = Reservation.builder()
                .user(user)
                .store(store)
                .reservationDate(reservationRequest.getReservationDate())
                .reservationTime(reservationRequest.getReservationTime())
                .reservationStatus(ReservationStatus.YET)
                .reservationUser(user)
                .build();

        // 상품 추가
        for (ReservationRequest.ProductRequest productRequest : reservationRequest.getProducts()) {
            Product product = productRepository.findById(productRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 상품 없음: " + productRequest.getProductId()));
            reservation.addProduct(product, productRequest.getQuantity());
        }

        // Reservation 저장
        reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId, UserPrincipal userPrincipal) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 예약 없음 id: " + reservationId));

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 유저 없음: " + userId));

        User user = getUser(userPrincipal);

        if (!reservation.getUser().equals(user)) {
            throw new DefaultException(ErrorCode.INVALID_AUTHENTICATION, "예약 당사자만 취소 가능합니다");
        }

        reservationRepository.delete(reservation);
    }

    @Transactional
    public List<ReservationResponse> getMyReservations(UserPrincipal userPrincipal) {
        // User 가져오기
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 유저 없음: " + userId));

        User user = getUser(userPrincipal);

        List<Reservation> reservations = reservationRepository.findAllByUserId(user.getId());
        List<ReservationResponse> responses = new ArrayList<>();

        for (Reservation reservation : reservations) {
            responses.add(makeReservationResponse(reservation));
        }

        return responses;
    }

    @Transactional
    public List<ReservationResponse> getStoreReservations(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 가게 없음: " + storeId));

        List<Reservation> reservations = reservationRepository.findAllByStoreStoreId(storeId);
        List<ReservationResponse> responses = new ArrayList<>();

        for (Reservation reservation : reservations) {
            responses.add(makeReservationResponse(reservation));
        }

        return responses;
    }

    @Transactional
    public List<ProductDto> getStoreProduct(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 가게 없음: " + storeId));

        List<ProductDto> productDtoList = store.getProducts().stream()
                .map(product -> ProductDto.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .productPrice(product.getProductPrice())
                        .productImageUrl(product.getProductImageUrl())
                        .build()).toList();

        return productDtoList;
    }

    @Transactional
    public ExpResponse visitReservation(Long reservationId, UserPrincipal userPrincipal) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 예약 없음 id: " + reservationId));

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 유저 없음: " + userId));

        User user = getUser(userPrincipal);

        reservation.setReservationStatus(ReservationStatus.DONE);
        reservationRepository.save(reservation);

        //고양이 경험치 증가 로직
        Cat cat = catRepository.findByUserId(user.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

        catService.plusExp(cat, 3L);

        ExpResponse catExp = ExpResponse.builder()
                .catExp(cat.getCatExp())
                .level(cat.getLevel())
                .build();

        return catExp;
    }


    private ReservationResponse makeReservationResponse(Reservation reservation) {
        List<ReservationResponse.ProductResponse> products = reservation.getReservationProducts().stream()
                .map(rp -> ReservationResponse.ProductResponse.builder()
                        .productId(rp.getProduct().getProductId())
                        .productName(rp.getProduct().getProductName())
                        .quantity(rp.getQuantity())
                        .build())
                .toList();

        return ReservationResponse.builder()
                .reservationId(reservation.getReservationId())
                .reservationStatus(reservation.getReservationStatus())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .storeId(reservation.getStore().getStoreId())
                .storeName(reservation.getStore().getStoreName())
                .products(products)
                .build();
    }


    private User getUser(UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }


}
