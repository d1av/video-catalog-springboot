package com.catalog.application.video.retrieve.get;

import com.catalog.domain.Identifier;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.utils.CollectionUtils;
import com.catalog.domain.video.AudioVideoMedia;
import com.catalog.domain.video.ImageMedia;
import com.catalog.domain.video.Resource;
import com.catalog.domain.video.Video;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record VideoOutput(
        String id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String description,
        Integer launchedAt,
        Double duration,
        Boolean opened,
        Boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf,
        AudioVideoMedia video,
        AudioVideoMedia trailer
) {
    public static VideoOutput from(final Video aVideo) {
        return new VideoOutput(
                aVideo.getId().getValue(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getDuration(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getRating().getName(),
                CollectionUtils.mapTo(aVideo.getCategories(), Identifier::getValue),
                CollectionUtils.mapTo(aVideo.getGenres(), Identifier::getValue),
                CollectionUtils.mapTo(aVideo.getCastMembers(), Identifier::getValue),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getVideo().orElse(null),
                aVideo.getTrailer().orElse(null)
        );
    }
}
