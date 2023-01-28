package com.catalog.domain;

import com.catalog.domain.genre.GenreID;
import com.catalog.domain.validation.ValidationHandler;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {
    protected AggregateRoot(final ID id) {
        super(id);
    }

}
