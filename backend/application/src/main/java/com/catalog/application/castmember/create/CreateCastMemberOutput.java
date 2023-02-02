package com.catalog.application.castmember.create;

import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberID;

public record CreateCastMemberOutput(
        String id
) {
    public static CreateCastMemberOutput from(final CastMemberID anId) {
        return new CreateCastMemberOutput(anId.getValue());
    }
    public static CreateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.getId());
    }

}
