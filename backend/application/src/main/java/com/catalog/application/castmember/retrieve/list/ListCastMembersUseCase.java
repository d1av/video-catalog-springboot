package com.catalog.application.castmember.retrieve.list;

import com.catalog.application.UseCase;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;

public abstract class ListCastMembersUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>> {
}
