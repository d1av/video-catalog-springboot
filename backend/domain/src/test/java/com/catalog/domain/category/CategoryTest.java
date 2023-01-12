package com.catalog.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.regex.Grapheme.T;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCategory_thenInstantiateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAVInvalidNullName_WhenCallNewCategorieAndValidate_thenShouldThrow() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate());

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().length());
        Assertions.assertEquals(expectedErrorMessage, actualException.getError().get(0));
    }
}
