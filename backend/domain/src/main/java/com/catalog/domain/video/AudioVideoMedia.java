package com.catalog.domain.video;

import com.catalog.domain.ValueObject;

import java.util.Objects;

public class AudioVideoMedia extends ValueObject {
    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String createdLocation;
    private final MediaStatus status;

    public AudioVideoMedia(final String checksum,
                           final String name,
                           final String rawLocation,
                           final String createdLocation,
                           final MediaStatus status) {
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.createdLocation = Objects.requireNonNull(createdLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioVideoMedia with(
            final String checksum,
            final String name,
            final String rawLocation,
            final String cratedLocation,
            final MediaStatus status
    ){
        return new AudioVideoMedia(checksum, name, rawLocation, cratedLocation, status);
    }

    public String checksum() {
        return checksum;
    }

    public String name() {
        return name;
    }

    public String rawLocation() {
        return rawLocation;
    }

    public String createdLocation() {
        return createdLocation;
    }

    public MediaStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AudioVideoMedia that)) return false;
        return Objects.equals(checksum, that.checksum) && Objects.equals(rawLocation, that.rawLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, rawLocation);
    }
}
