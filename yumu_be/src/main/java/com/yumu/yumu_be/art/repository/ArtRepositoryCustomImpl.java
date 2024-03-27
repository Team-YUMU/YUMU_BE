package com.yumu.yumu_be.art.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumu.yumu_be.art.entity.Art;
import com.yumu.yumu_be.art.entity.QArt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ArtRepositoryCustomImpl implements ArtRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Art> findByKeyWord(String keyWord, Pageable pageable) {
        QArt art  = QArt.art;

         List<Art> arts = jpaQueryFactory.select(art)
                .from(art)
                .where(art.artName.contains(keyWord).or(art.artist.contains(keyWord)))
                .orderBy(art.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

         long count = jpaQueryFactory.select(art)
                .from(art)
                .where(art.artName.contains(keyWord).or(art.artist.contains(keyWord)))
                 .fetch().size();

        return new PageImpl<>(arts, pageable, count);
    }

    @Override
    public Page<Art> findByKeyWordSortByWishCnt(String keyWord, Pageable pageable, String sort) {
        QArt art  = QArt.art;
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(sort);
        List<Art> arts = jpaQueryFactory.select(art)
                .from(art)
                .where(art.artName.contains(keyWord).or(art.artist.contains(keyWord)))
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = jpaQueryFactory.select(art)
                .from(art)
                .where(art.artName.contains(keyWord).or(art.artist.contains(keyWord)))
                .fetch().size();

        return new PageImpl<>(arts, pageable, count);
    }

    private OrderSpecifier[] createOrderSpecifier(String sortType) {
        QArt art  = QArt.art;
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if(sortType.equals("popular")){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, art.wishCnt));
        }else if(sortType.equals("latest")){
            orderSpecifiers.add(new OrderSpecifier(Order.DESC, art.createdAt));
        }
        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
