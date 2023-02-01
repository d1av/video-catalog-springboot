package com.catalog.domain.castmember;

import com.catalog.domain.Identifier;
import com.catalog.domain.category.CategoryID;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {
    private final String value;

    private CastMemberID(final String anId) {
        Objects.requireNonNull(anId);
        this.value = anId;
    }
    public static CastMemberID unique() {
        return new CastMemberID(UUID.randomUUID().toString().toLowerCase());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }

    public static CastMemberID from(final UUID anId) {
        return new CastMemberID(anId.toString().toLowerCase());
    }
    @Override
    public String getValue() {
        return null;
    }
}
