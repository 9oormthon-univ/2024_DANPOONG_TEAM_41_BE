package com.allgoing.domain.store.dto.response;
import com.allgoing.domain.store.domain.StoreInfo;
import com.allgoing.domain.store.dto.StoreInfoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
public class StoreSummaryResponse {
    private Long storeId;
    private String storeName;
    private String storeIntro;
    private String storeAddress;
    private String storeImageUrl;
    private List<StoreInfoDto> storeInfos;
    private long reviewCount;
}
