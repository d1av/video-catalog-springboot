package com.catalog.domain;

import com.catalog.domain.validation.ValidationHandler;

import java.util.Objects;

public abstract class Entity<ID extends Identifier> {
    protected final ID id;

    protected Entity(final ID id) {
        Objects.requireNonNull(id, "Id should not be null");
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
