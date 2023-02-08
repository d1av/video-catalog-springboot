package com.catalog.domain.video;

import com.catalog.domain.AggregateRoot;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.genre.GenreID;
import com.catalog.domain.utils.InstantUtils;
import com.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
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
    private ImageMedia thumbnailHalf;
    private AudioVideoMedia trailer;
    private AudioVideoMedia video;
    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    protected Video(
            final VideoID anId,
            final String aTitle,
            final String aDescription,
            final Year aLauchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumb,
            final ImageMedia aThumbHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        super(anId);
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLauchYear;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.banner = aBanner;
        this.thumbnail = aThumb;
        this.thumbnailHalf = aThumbHalf;
        this.trailer = aTrailer;
        this.video = aVideo;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = castMembers;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLauchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        final var now = InstantUtils.now();
        final var anId = VideoID.unique();
        return new Video(
                anId,
                aTitle,
                aDescription,
                aLauchYear,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                castMembers
        );
    }

    public static Video with(final Video aVideo) {
        final var now = InstantUtils.now();
        final var anId = VideoID.unique();
        return new Video(
                aVideo.getId(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt(),
                aVideo.getDuration(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getRating(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getVideo().orElse(null),
                new HashSet<>(aVideo.getCategories()),
                new HashSet<>(aVideo.getGenres()),
                new HashSet<>(aVideo.getCastMembers())
        );
    }

    public Video update(
            final String aTitle,
            final String aDescription,
            final Year aLauchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLauchYear;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(castMembers);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public static Video with(
            final VideoID anId,
            final String aTitle,
            final String aDescription,
            final Year aLauchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumb,
            final ImageMedia aThumbHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> castMembers
    ) {
        return new Video(
                anId,
                aTitle,
                aDescription,
                aLauchYear,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                aCreationDate,
                aUpdateDate,
                aBanner,
                aThumb,
                aThumbHalf,
                aTrailer,
                aVideo,
                categories,
                genres,
                castMembers
        );
    }


    public Set<CastMemberID> getCastMembers() {
        return castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public String getTitle() {
        return title;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public VideoID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Video setBanner(final ImageMedia banner) {
        this.updatedAt = InstantUtils.now();
        this.banner = banner;
        return this;
    }


    public Video setThumbnail(final ImageMedia thumbnail) {
        this.updatedAt = InstantUtils.now();
        this.thumbnail = thumbnail;
        return this;
    }


    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.updatedAt = InstantUtils.now();
        this.thumbnailHalf = thumbnailHalf;
        return this;
    }


    public Video setTrailer(final AudioVideoMedia trailer) {
        this.updatedAt = InstantUtils.now();
        this.trailer = trailer;
        return this;
    }


    public Video setVideo(AudioVideoMedia video) {
        this.updatedAt = InstantUtils.now();
        this.video = video;
        return this;
    }

    private void setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
    }

    private void setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }


    private void setCastMembers(Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }
}
