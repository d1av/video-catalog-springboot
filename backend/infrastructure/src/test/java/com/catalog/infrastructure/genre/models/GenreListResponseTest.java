package com.catalog.infrastructure.genre.models;

import com.catalog.JacksonTest;
import com.catalog.infrastructure.category.models.CategoryListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class GenreListResponseTest {
    @Autowired
    private JacksonTester<GenreListResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new GenreListResponse(
                expectedId,
                expectedName,
                expectedIsActive,
                expectedCreatedAt,
                expectedDeletedAt
        );

        final var actualJson = this.json.write(response);

        assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt);
    }

}