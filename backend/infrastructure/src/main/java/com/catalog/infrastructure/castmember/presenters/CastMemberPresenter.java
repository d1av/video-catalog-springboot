package com.catalog.infrastructure.castmember.presenters;

import com.catalog.application.castmember.retrieve.get.CastMemberOutput;
import com.catalog.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {
    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().toString(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }
}
