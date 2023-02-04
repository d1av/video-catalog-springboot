package com.catalog.domain.video;

import com.catalog.domain.Identifier;
import com.catalog.domain.genre.GenreID;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {
    private final String value;

    private VideoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
    }

    public static VideoID from(final UUID anId) {
        return new VideoID(anId.toString());
    }

    public static VideoID unique() {
        return new VideoID(UUID.randomUUID().toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenreID that)) return false;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
