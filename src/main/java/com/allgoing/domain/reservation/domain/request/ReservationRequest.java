package com.allgoing.domain.reservation.domain.request;


import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class ReservationRequest {
    private LocalDate reservationDate;
    private LocalTime reservationTime;
}
