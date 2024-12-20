package com.allgoing.domain.cat.domain;

import com.allgoing.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Cat")
@NoArgsConstructor
@Getter
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cat_id", updatable = false, nullable = false, unique = true)
    private Long catId;

    @Column(name="level")
    private int level;

    @Column(name="cat_exp")
    private Long catExp;

    @Column(name="coin")
    private Long coin;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @OneToMany(mappedBy = "cat", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatItem> catItems = new ArrayList<>();

    @Builder
    public Cat(User user){
        this.level = 0;
        this.catExp = 0L;
        this.coin = 0L;
        this.user = user;
    }

    public void useCoin(long l) {
        this.coin -= l;
    }


    public void updateLevel(int newLevel) {
        this.level = newLevel;
    }

    public void updateExp(Long exp) {
        this.catExp = exp;
    }

    public void plusExp(Long exp) {
        this.catExp += exp;
    }

    public void addCoins(int coins) {
        this.coin += coins;
    }
}
