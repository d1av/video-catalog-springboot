package com.catalog.infrastructure.castmember.models;

import com.catalog.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {
}
