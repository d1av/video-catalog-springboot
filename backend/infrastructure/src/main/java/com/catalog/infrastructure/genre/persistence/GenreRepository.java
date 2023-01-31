package com.catalog.infrastructure.genre.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreJpaEntity, String> {
    Page<GenreJpaEntity> findAll(Specification<GenreJpaEntity> whereClause, Pageable page);
}
