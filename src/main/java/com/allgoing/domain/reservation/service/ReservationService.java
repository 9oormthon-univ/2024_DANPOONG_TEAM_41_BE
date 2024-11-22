package com.allgoing.domain.reservation.service;

import com.allgoing.domain.reservation.domain.Reservation;
import com.allgoing.domain.reservation.domain.ReservationStatus;
import com.allgoing.domain.reservation.domain.request.ReservationRequest;
import com.allgoing.domain.reservation.repository.ReservationRepository;
//import com.allgoing.domain.store.domain.repository.StoreRepository;
import com.allgoing.domain.user.domain.User;
import com.allgoing.domain.user.domain.repository.UserRepository;
import com.allgoing.global.error.DefaultException;
import com.allgoing.global.payload.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
//    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void makeReservation(ReservationRequest reservationRequest, Long storeId, Long userId) {
        // Store 가져오기
//        Store store = storeRepository.findById(reservationRequest.getStoreId())
//                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 가게 없음: " + reservationRequest.getStoreId()));

        // User 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID에 맞는 유저 없음: " + userId));

        // Reservation 생성 및 설정
        Reservation reservation = Reservation.builder()
                .user(user)
//                .store(store)
                .reservationDate(reservationRequest.getReservationDate())
                .reservationTime(reservationRequest.getReservationTime())
                .reservationStatus(ReservationStatus.YET)
                .reservationUser(user)
                .build();

        // Reservation 저장
        reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("해당 id에 맞는 리뷰 없음 id: " + reservationId));

        if (!reservation.getUser().equals(userRepository.getById(userId))) {
            throw new DefaultException(ErrorCode.INVALID_AUTHENTICATION, "예약 당사자만 취소 가능합니다");
        }

        reservationRepository.delete(reservation);
    }
}
