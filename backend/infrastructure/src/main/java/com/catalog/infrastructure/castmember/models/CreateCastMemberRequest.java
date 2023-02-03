package com.catalog.infrastructure.castmember.models;

import com.catalog.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
