package com.yumu.yumu_be.art.repository;

import com.yumu.yumu_be.art.entity.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtRepositoryCustom {

    Page<Art> findByKeyWord(String keyWord, Pageable pageable);
    Page<Art> findByKeyWordSortByWishCnt(String keyWord, Pageable pageable, String sort);
}
