package com.allgoing.domain.cat.application;

import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.cat.domain.CatItem;
import com.allgoing.domain.cat.domain.repository.CatItemRepository;
import com.allgoing.domain.cat.domain.repository.CatRepository;
import com.allgoing.domain.cat.dto.request.PatchCatItemReq;
import com.allgoing.domain.cat.dto.response.CatItemListResponse;
import com.allgoing.domain.cat.dto.response.CatItemResponse;
import com.allgoing.domain.cat.dto.response.ExpResponse;
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
    public ResponseEntity<?> patchCatItem(UserPrincipal userPrincipal, PatchCatItemReq patchCatItemReq) {
        Cat cat = getCatbyUser(userPrincipal);
        // 현재 Cat이 착용하고 있는 아이템 목록을 모두 착용 해제
        ArrayList<CatItem> catItems = catItemRepository.findByCat(cat);

        for (CatItem catItem : catItems) {
            catItem.updateIsEquipped(false);
        }

        ArrayList<Long> equippedItemIds = patchCatItemReq.getItemIds();

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


    private Cat getCatbyUser(UserPrincipal userPrincipal) {
        // User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // return catRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        return catRepository.findByUserId(1L).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
    }
}
