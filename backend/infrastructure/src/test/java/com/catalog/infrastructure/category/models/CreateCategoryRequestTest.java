package com.catalog.infrastructure.category.models;

import com.catalog.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
class CreateCategoryRequestTest {
    @Autowired
    private JacksonTester<CreateCategoryRequest> json;


    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var request =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var actualJson = this.json.write(request);

        assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshal() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var json = """
                {
                "name": "%s",
                "description": "%s",
                "is_active": "%s"
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualJson = this.json.parse(json);

        assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }

}