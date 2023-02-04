package com.catalog.infrastructure.castmember.models;

import com.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberResponse(
        String id,
        String name,
        String type,
        String createdAt,
        String updatedAt
) {
}
