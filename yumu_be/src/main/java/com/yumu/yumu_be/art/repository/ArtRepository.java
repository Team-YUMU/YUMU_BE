package com.yumu.yumu_be.art.repository;

import com.yumu.yumu_be.art.repository.domain.Art;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtRepository extends JpaRepository<Art, Integer> {
}
