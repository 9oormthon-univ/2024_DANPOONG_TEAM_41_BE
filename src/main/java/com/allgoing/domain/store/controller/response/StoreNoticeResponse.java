package com.allgoing.domain.store.controller.response;

import com.allgoing.domain.store.dto.StoreImageDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StoreNoticeResponse {
    private Long storeId;
    private String storeNoticeContent;
    private List<StoreImageDto> storeNoticeImages;
    private LocalDateTime createdAt;
}
