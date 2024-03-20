package com.yumu.yumu_be.art.repository;

import com.yumu.yumu_be.art.repository.domain.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtRepository extends JpaRepository<Art, Integer>, ArtRepositoryCustom {

//    @Query()
//    Page<Art> findByPopular(Pageable pageable);

    @Query(value = "select a from Art a where a.status=1")
    Page<Art> findLive(Pageable pageable);

    @Query(value = "select a from Art a where a.status=0")
    Page<Art> findLatest(Pageable pageable);
}
