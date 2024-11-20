package com.allgoing.domain.store.controller.response;
import com.allgoing.domain.store.domain.StoreInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class StoreSummaryResponse {
    private String storeName;
    private String storeIntro;
    private String storeAddress;
    private String storeImageUrl;
    private List<StoreInfo> storeInfos;
    private long reviewCount;
}
