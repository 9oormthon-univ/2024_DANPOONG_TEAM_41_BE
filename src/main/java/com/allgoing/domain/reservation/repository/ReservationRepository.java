package com.allgoing.domain.reservation.repository;

import com.allgoing.domain.reservation.domain.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUserUserId(Long userId);
    List<Reservation> findAllByStoreStoreId(Long storeId);
}
