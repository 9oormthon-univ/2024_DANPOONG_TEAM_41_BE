package com.allgoing.domain.store.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StoreImageType {
    MAIN("MAIN"),
    SUB("SUB");

    private String value;
}
