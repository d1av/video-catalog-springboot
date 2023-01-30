CREATE TABLE genres (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        active BOOLEAN NOT NULL DEFAULT TRUE,
        created_At DATETIME(6) NOT NULL,
        updated_At DATETIME(6) NOT NULL,
        deleted_At DATETIME(6) NOT NULL,
);

CREATE TABLE genres_categories (
        genres_id VARCHAR(36) NOT NULL,
        category_id VARCHAR(36) NOT NULL,
        CONSTRAINT idx_genre_category UNIQUE(genres_id, category_id),
        CONSTRAINT fk_genre_id UNIQUE(genres_id) REFERENCES genres (id) ON DELETE CASCADE,
        CONSTRAINT fk_category_id UNIQUE(category_id) REFERENCES category (id) ON DELETE CASCADE,

);