package com.allgoing.domain.cat.domain;

import com.allgoing.domain.item.domain.Item;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="CatItem")
@NoArgsConstructor
@Getter
public class CatItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cat_item_id", updatable = false, nullable = false, unique = true)
    private Long catItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name="is_equipped")
    private boolean isEquipped;

    @Builder
    public CatItem(Cat cat, Item item, boolean isEquipped){
        this.cat = cat;
        this.item = item;
        this.isEquipped = isEquipped;
    }

}
