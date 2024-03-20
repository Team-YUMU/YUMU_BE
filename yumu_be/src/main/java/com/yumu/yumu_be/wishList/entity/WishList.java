package com.yumu.yumu_be.wishList.entity;

import com.yumu.yumu_be.art.entity.Art;
import com.yumu.yumu_be.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="wishList_id")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_id", referencedColumnName = "art_id")
    private Art art;

    public WishList() {

    }

    public WishList(Member member, Art art) {
        this.member = member;
        this.art = art;
    }

    public static WishList of(Member member, Art art) {
        return new WishList(member, art);
    }
}
