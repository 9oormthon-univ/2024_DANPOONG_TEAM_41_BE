package com.allgoing.domain.cat.application;

import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.cat.domain.CatItem;
import com.allgoing.domain.cat.domain.repository.CatItemRepository;
import com.allgoing.domain.cat.domain.repository.CatRepository;
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
        // User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // Cat cat = catRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        Cat cat = catRepository.findByUserId(1L).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));

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
        // User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // Cat cat = catRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        Cat cat = catRepository.findByUserId(1L).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
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
}
