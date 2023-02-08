package com.catalog.domain.castmember;

import com.catalog.domain.Identifier;
import com.catalog.domain.utils.IdUtils;

import java.util.Objects;

public class CastMemberID extends Identifier {
    private final String value;

    private CastMemberID(final String anId) {
        Objects.requireNonNull(anId);
        this.value = anId;
    }

    public static CastMemberID unique() {
        return new CastMemberID(IdUtils.uuid());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }


    @Override
    public String getValue() {
        return value;
    }
}
