package com.catalog.application.castmember.retrieve.list;

import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListCastMembersUseCase extends ListCastMembersUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultListCastMembersUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(SearchQuery pagination) {
        return this.castMemberGateway.findAll(pagination)
                .map(CastMemberListOutput::from);
    }
}
