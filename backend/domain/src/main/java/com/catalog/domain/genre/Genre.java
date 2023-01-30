package com.catalog.domain.genre;

import com.catalog.domain.AggregateRoot;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.NotificationException;
import com.catalog.domain.utils.InstantUtils;
import com.catalog.domain.validation.ValidationHandler;
import com.catalog.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {
    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.categories = categories;
        this.active = isActive;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.deletedAt = aDeletedAt;

        selfValidate();
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final var anId = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(
                anId,
                aName,
                isActive,
                new ArrayList<>(),
                now,
                now,
                deletedAt
        );
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new Genre(
                anId,
                aName,
                isActive,
                categories,
                aCreatedAt,
                aUpdatedAt,
                aDeletedAt
        );
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    public Genre update(String aName, boolean isActive, List<CategoryID> categories) {
        if (isActive) activate();
        else deactivate();

        this.name = aName;
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public void deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
    }

    public void activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
    }


    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public GenreID getId() {
        return super.id;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to validate Aggregate Genre", notification);
        }
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) return this;

        this.categories.add(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }


    public Genre removeCategory(CategoryID aCategoryID) {
        if (aCategoryID == null) return this;

        this.categories.remove(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategories(List<CategoryID> aCategories) {
        if (aCategories == null) return this;
        if (aCategories.size() == 0) return this;

        this.categories.addAll(aCategories);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCategories(List<CategoryID> categories) {
        this.categories = categories;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
