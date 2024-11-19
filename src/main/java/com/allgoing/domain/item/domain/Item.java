package com.allgoing.domain.item.domain;

import com.allgoing.domain.cat.domain.CatItem;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="Item")
@NoArgsConstructor
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id", updatable = false, nullable = false, unique = true)
    private Long itemId;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

    @Column(name="item_name")
    private String itemName;

    @Column(name="item_price")
    private Long itemPrice;

    @OneToMany(mappedBy = "item")
    private List<CatItem> catItems;

}
