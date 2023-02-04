package com.catalog.domain.video;

import com.catalog.domain.AggregateRoot;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.genre.GenreID;
import com.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Set;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailOf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> categories;
    private Set<CastMemberID> categories;

    protected Video(final VideoID videoID) {
        super(videoID);
    }

    @Override
    public void validate(final ValidationHandler handler) {

    }
}
