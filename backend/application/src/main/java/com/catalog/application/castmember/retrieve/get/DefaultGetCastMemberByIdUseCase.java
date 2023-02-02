package com.catalog.application.castmember.retrieve.get;

import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.exceptions.NotFoundException;

public final class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Override
    public CastMemberOutput execute(String anId) {
        final var aMemberId = CastMemberID.from(anId);
        return this.castMemberGateway.findById(CastMemberID.from(anId))
                .map(CastMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, aMemberId))
                ;
    }
}
