package com.catalog.domain.genre;

import com.catalog.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {
    private final String value;

    private GenreID(final String value) {
        this.value = value;
    }

    public static GenreID unique() {
        return new GenreID(UUID.randomUUID().toString().toLowerCase());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId);
    }

    public static GenreID from(final UUID anId) {
        return new GenreID(anId.toString().toLowerCase());
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
