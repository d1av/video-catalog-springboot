package com.catalog.application.video.retrieve.list;

import com.catalog.domain.video.Video;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record VideoListOutput(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers
) {
    public static VideoListOutput from(final Video aVideo) {
        return new VideoListOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>()
        );
    }

    public static VideoListOutput newListWithNames(
            String id,
            String title,
            String description,
            Instant createdAt,
            Instant updatedAt,
            Set<String> categories,
            Set<String> genres,
            Set<String> castMembers
    ) {
        return new VideoListOutput(
                id,
                title,
                description,
                createdAt,
                updatedAt,
                categories,
                genres,
                castMembers
        );
    }
}
