package com.allgoing.domain.store.controller.response;
import com.allgoing.domain.store.domain.StoreInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreSummaryResponse {
    private String storeName;
    private String storeIntro;
    private String storeAddress;
    private List<StoreInfo> storeInfos;
    private long reviewCount;
}
