package com.allgoing.domain.cat.application;

import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.cat.domain.CatItem;
import com.allgoing.domain.cat.domain.repository.CatItemRepository;
import com.allgoing.domain.cat.domain.repository.CatRepository;
import com.allgoing.domain.cat.dto.request.PatchCatItemRequest;
import com.allgoing.domain.cat.dto.response.CatItemListResponse;
import com.allgoing.domain.cat.dto.response.CatItemResponse;
import com.allgoing.domain.cat.dto.response.ExpResponse;
import com.allgoing.domain.user.domain.User;
import com.allgoing.domain.user.domain.repository.UserRepository;
import com.allgoing.global.config.security.token.UserPrincipal;
import com.allgoing.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatService {

    private final CatRepository catRepository;
    private final CatItemRepository catItemRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> getExp(UserPrincipal userPrincipal) {
        Cat cat = getCatbyUser(userPrincipal);

        ExpResponse expResponse = ExpResponse.builder()
                .level(cat.getLevel())
                .catExp(cat.getCatExp())
                .build();

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(expResponse)
                .build();

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> getCatItems(UserPrincipal userPrincipal) {
        Cat cat = getCatbyUser(userPrincipal);
        ArrayList<CatItem> catItems = catItemRepository.findByCat(cat);

        ArrayList<CatItemResponse> catItemResponses = new ArrayList<>();
        for (CatItem catItem : catItems) {
            CatItemResponse catItemResponse = CatItemResponse.builder()
                    .itemId(catItem.getItem().getItemId())
                    .itemCategory(catItem.getItem().getItemCategory())
                    .itemName(catItem.getItem().getItemName())
                    .itemPrice(catItem.getItem().getItemPrice())
                    .build();
            catItemResponses.add(catItemResponse);
        }

        CatItemListResponse catItemListResponse = CatItemListResponse.builder()
                .coin(cat.getCoin())
                .catItems(catItemResponses)
                .build();

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information(catItemListResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> patchCatItem(UserPrincipal userPrincipal, PatchCatItemRequest patchCatItemRequest) {
        Cat cat = getCatbyUser(userPrincipal);
        // 현재 Cat이 착용하고 있는 아이템 목록을 모두 착용 해제
        ArrayList<CatItem> catItems = catItemRepository.findByCat(cat);

        for (CatItem catItem : catItems) {
            catItem.updateIsEquipped(false);
        }

        ArrayList<Long> equippedItemIds = patchCatItemRequest.getItemIds();

        for(Long itemId : equippedItemIds) {
            CatItem catItem = catItemRepository.findByItem_ItemId(itemId);
            catItem.updateIsEquipped(true);
        }

        ApiResponse response = ApiResponse.builder()
                .check(true)
                .information("아이템 착용 정보 수정 성공")
                .build();

        return ResponseEntity.ok(response);

    }

    @Transactional
    public void plusExp(Cat cat, Long exp) {
        cat.plusExp(exp); // 현재 경험치 추가
        checkLevelUp(cat); // 레벨업 확인 및 처리
        catRepository.save(cat); // 변경사항 저장
    }

    private void checkLevelUp(Cat cat) {
        long currentExp = cat.getCatExp();
        int currentLevel = cat.getLevel();

        while (currentExp >= getRequiredExp(currentLevel)) {
            currentExp -= getRequiredExp(currentLevel); // 경험치 소모
            currentLevel++; // 레벨 상승
        }

        // 최종 레벨과 남은 경험치 업데이트
        cat.updateLevel(currentLevel);
        cat.updateExp(currentExp);
    }

    private long getRequiredExp(int level) {
        return level + 4; // 레벨업 기준 경험치 계산
    }

    private Cat getCatbyUser(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return catRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

    }
}
