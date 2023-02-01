package com.catalog.domain.castmember;

import com.catalog.domain.genre.Genre;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;

import java.util.Optional;

public interface CastMemberGateway {
    CastMember create(CastMember aCastMember);

    void deleteById(CastMemberID anId);

    Optional<CastMember> findById(CastMemberID anId);

    Genre update(CastMember aCastMember);

    Pagination<CastMember> findAll(SearchQuery aQuery);

}
