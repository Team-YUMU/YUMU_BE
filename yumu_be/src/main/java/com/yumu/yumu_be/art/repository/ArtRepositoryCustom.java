package com.yumu.yumu_be.art.repository;

import com.yumu.yumu_be.art.repository.domain.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtRepositoryCustom {

    Page<Art> findByKeyWord(String keyWord, Pageable pageable);
}
