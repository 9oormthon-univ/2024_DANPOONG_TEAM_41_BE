package com.allgoing.domain.item.application;

import com.allgoing.domain.cat.domain.Cat;
import com.allgoing.domain.cat.domain.repository.CatRepository;
import com.allgoing.domain.item.domain.Item;
import com.allgoing.domain.item.domain.ItemCategory;
import com.allgoing.domain.item.domain.repository.ItemRepository;
import com.allgoing.domain.item.dto.response.CategoryItemListResponse;
import com.allgoing.domain.item.dto.response.CategoryItemResponse;
import com.allgoing.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final CatRepository catRepository;
    private final ItemRepository itemRepository;

    public ResponseEntity<?> getCategoryItems(UserPrincipal userPrincipal) {
        Cat cat = getCatbyUser(userPrincipal);

        ArrayList<CategoryItemResponse> accessories = getCategoryItems(cat, ItemCategory.ACCESSORIES);
        ArrayList<CategoryItemResponse> shoes = getCategoryItems(cat, ItemCategory.SHOES);

        CategoryItemListResponse categoryItemListResponse = CategoryItemListResponse.builder()
                .accessories(accessories)
                .shoes(shoes)
                .build();

        return ResponseEntity.ok(categoryItemListResponse);
    }

    private ArrayList<CategoryItemResponse> getCategoryItems(Cat cat, ItemCategory itemCategory) {

        ArrayList<CategoryItemResponse> accessories = new ArrayList<>();
        ArrayList<Item> items = itemRepository.findByItemCategory(itemCategory);
        for (Item item : items) {
            CategoryItemResponse categoryItemResponse = CategoryItemResponse.builder()
                    .itemId(item.getItemId())
                    .itemName(item.getItemName())
                    .itemPrice(item.getItemPrice())
                    .isOwned(isOwned(cat, item))
                    .isEquipped(isEquipped(cat, item))
                    .build();
            accessories.add(categoryItemResponse);
        }
        return accessories;
    }

    private boolean isOwned(Cat cat, Item item) {
        return cat.getCatItems().stream().anyMatch(catItem -> catItem.getItem().equals(item));
    }

    private boolean isEquipped(Cat cat, Item item) {
        return cat.getCatItems().stream().anyMatch(catItem -> catItem.getItem().equals(item) && catItem.isEquipped());
    }


    private Cat getCatbyUser(UserPrincipal userPrincipal) {
        // User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // return catRepository.findByUserId(userPrincipal.getId()).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
        return catRepository.findByUserId(1L).orElseThrow(() -> new IllegalArgumentException("캐릭터를 찾을 수 없습니다."));
    }
}
