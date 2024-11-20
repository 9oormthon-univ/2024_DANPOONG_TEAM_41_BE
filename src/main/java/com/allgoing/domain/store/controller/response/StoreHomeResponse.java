package com.allgoing.domain.store.controller.response;

import com.allgoing.domain.product.dto.ProductDto;
import com.allgoing.domain.store.dto.StoreImageDto;
import com.allgoing.domain.store.dto.StoreInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class StoreHomeResponse {
    private Long storeId;
    private String storeName;
    private String storeIntro;
    private String storeAddress;
    private String storePhone;

    private List<ProductDto> products;

    private List<StoreImageDto> storeImages;
    private List<StoreInfoDto> storeInfos;
}
