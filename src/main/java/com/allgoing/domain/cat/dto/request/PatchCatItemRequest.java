package com.allgoing.domain.cat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PatchCatItemRequest {
    ArrayList<Long> itemIds;
}
