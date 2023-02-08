package com.catalog.domain.video;

import java.time.Instant;

public record VideoPreview(
        VideoID id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
        ) {
}
