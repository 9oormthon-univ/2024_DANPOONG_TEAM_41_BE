package com.allgoing.domain.store.controller.response;
import com.allgoing.domain.store.domain.StoreInfo;
import java.util.List;
import lombok.AllArgsConstructor;
<<<<<<< HEAD
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Builder
=======
import lombok.Getter;

@Getter
@AllArgsConstructor
>>>>>>> 1dcdc9e3a1bb7513b40e94d923e40772c1a2c0cb
public class StoreSummaryResponse {
    private String storeName;
    private String storeIntro;
    private String storeAddress;
<<<<<<< HEAD
    @Setter
    private String storeImageUrl;
    private List<StoreInfo> storeInfos;
    private long reviewCount;

=======
    private List<StoreInfo> storeInfos;
    private long reviewCount;
>>>>>>> 1dcdc9e3a1bb7513b40e94d923e40772c1a2c0cb
}
