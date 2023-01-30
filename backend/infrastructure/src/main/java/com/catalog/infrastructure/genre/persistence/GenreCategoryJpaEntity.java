package com.catalog.infrastructure.genre.persistence;

import com.catalog.domain.category.CategoryID;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "genres_categories")
public class GenreCategoryJpaEntity {
    @EmbeddedId
    private GenreCategoryID id;
    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    public GenreCategoryJpaEntity() {
    }

    private GenreCategoryJpaEntity(
            final GenreJpaEntity aGenre, final CategoryID aCategoryId
    ) {
        this.id = GenreCategoryID.from(aGenre.getId(), aCategoryId.getValue());
        this.genre = aGenre;
    }

    public static GenreCategoryJpaEntity from(final GenreJpaEntity aGenre, final CategoryID aCategoryId) {
        return new GenreCategoryJpaEntity(aGenre, aCategoryId);
    }

    public GenreCategoryID getId() {
        return id;
    }

    public void setId(GenreCategoryID id) {
        this.id = id;
    }

    public GenreJpaEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreJpaEntity genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenreCategoryJpaEntity that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
