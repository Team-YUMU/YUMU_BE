package com.yumu.yumu_be.art.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yumu.yumu_be.art.entity.Art;
import com.yumu.yumu_be.art.entity.QArt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
}
