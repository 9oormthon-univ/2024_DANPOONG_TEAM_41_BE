package com.allgoing.domain.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReservationStatus {
    DONE("DONE"),
    CANCEL("CANCEL"),
    YET("YET");

    private String value;
}