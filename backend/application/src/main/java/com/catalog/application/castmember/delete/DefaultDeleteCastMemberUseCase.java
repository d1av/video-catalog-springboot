package com.catalog.application.castmember.delete;

import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;

public final class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultDeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = castMemberGateway;
    }

    @Override
    public void execute(String onIn) {
        this.castMemberGateway.deleteById(CastMemberID.from(onIn));
    }
}
