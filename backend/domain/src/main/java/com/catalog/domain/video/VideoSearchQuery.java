package com.catalog.domain.video;

import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.genre.GenreID;

import java.util.Set;

public record VideoSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        Set<CastMemberID> castMembers,
        Set<CategoryID> categories,
        Set<GenreID> genres
        ) {

}
