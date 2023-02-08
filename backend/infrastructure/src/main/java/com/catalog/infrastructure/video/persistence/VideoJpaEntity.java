package com.catalog.infrastructure.video.persistence;

import com.catalog.domain.video.Rating;
import com.catalog.domain.video.Video;
import com.catalog.domain.video.VideoID;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.Year;
import java.util.UUID;

@Table(name = "videos")
@Entity(name = "Video")
public class VideoJpaEntity {
    @Id
    private UUID id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", length = 4000)
    private String description;
    @Column(name = "year_launched", nullable = false)
    private int yearLauched;
    @Column(name = "opened", nullable = false)
    private boolean opened;
    @Column(name = "published", nullable = false)
    private boolean published;
    @Column(name = "rating")
    @Enumerated(EnumType.STRING)
    private Rating rating;
    @Column(name = "duration", precision = 2, nullable = false)
    private double duration;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public VideoJpaEntity() {
    }

    public VideoJpaEntity(final UUID id,
                          final String title,
                          final String description,
                          final int yearLauched,
                          final boolean opened,
                          final boolean published,
                          final Rating rating,
                          final double duration,
                          final Instant createdAt,
                          final Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.yearLauched = yearLauched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static VideoJpaEntity from(final Video aVideo) {
        return new VideoJpaEntity(
                UUID.fromString(aVideo.getId().getValue()),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getRating(),
                aVideo.getDuration(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
    }

    public Video toAggregate() {
        return Video.with(
                VideoID.from(getId()),
                getTitle(),
                getDescription(),
                Year.of(getYearLauched()),
                getDuration(),
                isOpened(),
                isPublished(),
                getRating(),
                getCreatedAt(),
                getUpdatedAt(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYearLauched() {
        return yearLauched;
    }

    public void setYearLauched(int yearLauched) {
        this.yearLauched = yearLauched;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
