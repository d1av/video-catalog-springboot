package com.catalog.infrastructure.castmember.models;

import com.catalog.domain.castmember.CastMemberType;

public record CastMemberListResponse(
        String id,
        String name,
        String type,
        String createdAt
) {
}
