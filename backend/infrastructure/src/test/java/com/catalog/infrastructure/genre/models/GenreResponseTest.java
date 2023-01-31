package com.catalog.infrastructure.genre.models;

import com.catalog.JacksonTest;
import com.catalog.infrastructure.category.models.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class GenreResponseTest {
    @Autowired
    private JacksonTester<GenreResponse> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedCategories = List.of("123");
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new GenreResponse(
                expectedId,
                expectedName,
                expectedCategories,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        final var actualJson = this.json.write(response);

        assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt)
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt);
    }

    @Test
    public void testUnMarshall() throws IOException {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedCategory ="123";
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
                {
                "id": "%s",
                "name": "%s",
                "categories_id": ["%s"],
                "is_active": "%s",
                "created_at": "%s",
                "deleted_at": "%s",
                "updated_at": "%s"
                }
                """.formatted(
                expectedId,
                expectedName,
                expectedCategory,
                expectedIsActive,
                expectedCreatedAt.toString(),
                expectedDeletedAt.toString(),
                expectedUpdatedAt.toString()
        );

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt);
    }

}
