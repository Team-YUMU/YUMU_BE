package com.yumu.yumu_be.art.repository;

import com.yumu.yumu_be.art.entity.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArtRepository extends JpaRepository<Art, Integer>, ArtRepositoryCustom {

    @Query(value = "select a from Art a order by a.wishCnt DESC")
    Page<Art> findByPopular(Pageable pageable);

    @Query(value = "select a from Art a where a.status=1")
    Page<Art> findLive(Pageable pageable);

    @Query(value = "select a from Art a where a.status=0")
    Page<Art> findLatest(Pageable pageable);
}
