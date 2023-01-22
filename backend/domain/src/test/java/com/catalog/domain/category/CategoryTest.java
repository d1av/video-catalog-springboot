package com.catalog.domain.category;

import com.catalog.domain.exceptions.DomainException;
import com.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        final var expectedErrorMessage = "message='name' should not be null";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0));
    }

    @Test
    public void givenAVInvalidEmptyName_WhenCallNewCategorieAndValidate_thenShouldThrow() {
        final String expectedName = "  ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0));
    }

    @Test
    public void givenAVInvalidNameLengthThan3_WhenCallNewCategorieAndValidate_thenShouldThrow() {
        final String expectedName = "F1  ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0));
    }

    @Test
    public void givenAVInvalidNameLengthMoreThan255() {
        final String expectedName = "O cuidado em identificar pontos críticos na " +
                "execução dos pontos do programa acarreta um processo de " +
                "reformulação e modernização dos métodos utilizados na avaliação " +
                "de resultados. Pensando mais a longo prazo, a estrutura atual da o" +
                "rganização facilita a criação dos modos de operação convencionais.";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0));
    }

    @Test
    public void givenAValidEmptyDescription_whenCallNewCategoryAndvalidate_thenShouldReturn() {
        final String expectedName = "Filmes";
        final var expectedIsActive = true;
        final var expectedDescription = "   ";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidFalseActive_whenCallNewCategoryAndvalidate_thenShouldReturn() {
        final String expectedName = "Filmes";
        final var expectedIsActive = false;
        final var expectedDescription = "   ";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenDeactivate_thenReturnInactivate() {
        final String expectedName = "Filmes";
        final var expectedIsActive = false;
        final var expectedDescription = "A categoria mais assistida";

        final var aCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.deactivate();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt().isAfter(aCategory.getCreatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenAnInactiveCategory_whenCallActivate_thenReturnCategoryActivate() {
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(
                        expectedName,
                        expectedDescription,
                        false);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

        final var updatedAt = aCategory.getUpdatedAt();
        final var createdAt = aCategory.getUpdatedAt();

        Assertions.assertFalse(aCategory.isActive());
        Assertions.assertNotNull(aCategory.getDeletedAt());

        final var actualCategory = aCategory.activate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt().isAfter(aCategory.getCreatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());

    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0));
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    }

    @Test
    public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory("Film", "A categoria", false);


        final var createdAt = aCategory.getCreatedAt();
        final var updatedAt = aCategory.getUpdatedAt();

        final Category actualCategory = aCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}
