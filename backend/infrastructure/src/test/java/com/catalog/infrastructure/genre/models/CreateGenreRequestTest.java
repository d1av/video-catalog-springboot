package com.catalog.infrastructure.genre.models;

import com.catalog.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class CreateGenreRequestTest {
    @Autowired
    private JacksonTester<CreateGenreRequest> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Filmes";
        final var expectedCategories = List.of("123","456");
        final var expectedIsActive = true;

        final var request =
                new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        final var actualJson = this.json.write(request);

        assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshal() throws Exception {
        final var expectedName = "Filmes";
        final var expectedCategories = "123";
        final var expectedIsActive = false;

        final var json = """
                {
                "name": "%s",
                "categories_id": ["%s"],
                "is_active": "%s"
                }
                """.formatted(
                expectedName,
                expectedCategories,
                expectedIsActive
        );

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategories))
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }

}