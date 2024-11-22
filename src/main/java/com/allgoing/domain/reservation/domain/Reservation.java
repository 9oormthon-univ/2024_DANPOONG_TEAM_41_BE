package com.allgoing.domain.reservation.domain;

import com.allgoing.domain.store.domain.Store;
import com.allgoing.domain.common.BaseEntity;
import com.allgoing.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="Reservation")
@NoArgsConstructor
@Getter
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_id", updatable = false, nullable = false, unique = true)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="reservation_status")
    private ReservationStatus reservationStatus;

    @Column(name="reservation_date")
    private LocalDate reservationDate;

    @Column(name="reservation_time")
    private LocalTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 예약자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_user_id")
    private User reservationUser;

}