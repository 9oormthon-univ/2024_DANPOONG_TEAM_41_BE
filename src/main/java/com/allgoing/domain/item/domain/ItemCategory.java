package com.allgoing.domain.item.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemCategory {
    CLOTHES("CLOTHES"),
    ACCESSORIES("ACCESSORIES"),
    SHOES("SHOES");

    private String value;

}
