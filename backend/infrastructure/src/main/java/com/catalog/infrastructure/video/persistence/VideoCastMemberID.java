package com.catalog.infrastructure.video.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class VideoCastMemberID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private UUID videoId;
    @Column(name = "cast_member_id", nullable = false)
    private UUID castMemberId;

    public VideoCastMemberID() {
    }

    private VideoCastMemberID(final UUID videoId, final UUID castMemberId) {
        this.videoId = videoId;
        this.castMemberId = castMemberId;
    }

    public static VideoCastMemberID from(final UUID videoId, final UUID castMemberId) {
        return new VideoCastMemberID(videoId, castMemberId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoCastMemberID that)) return false;
        return Objects.equals(getVideoId(), that.getVideoId()) && Objects.equals(getCastMemberId(), that.getCastMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVideoId(), getCastMemberId());
    }

    public UUID getVideoId() {
        return videoId;
    }

    public void setVideoId(UUID videoId) {
        this.videoId = videoId;
    }

    public UUID getCastMemberId() {
        return castMemberId;
    }

    public void setCastMemberId(UUID castMemberId) {
        this.castMemberId = castMemberId;
    }
}
