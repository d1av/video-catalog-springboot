package com.catalog.domain.genre;

import com.catalog.domain.category.CategoryID;
import com.catalog.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallsNewGenre_shouldInstantiateAGente() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;


        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallsNewGenre_shouldReceiceAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = 0;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallsNewGenre_shouldReceiceAError() {
        final String expectedName = "     ";
        final var expectedIsActive = true;
        final var expectedCategories = 0;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenre_shouldReceiceAError() {
        final String expectedName = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of " +
                "classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney " +
                "College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through " +
                "the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of" +
                " \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, " +
                "very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.";
        final var expectedIsActive = true;
        final var expectedCategories = 0;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAActiveGenre_whenCallDeactivate_shouldReceivedOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);

        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var acualCreatedAt = actualGenre.getCreatedAt();
        final var acualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(acualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(acualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInactiveGenre_whenCallActivate_shouldReceivedOK() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, false);

        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());

        final var acualCreatedAt = actualGenre.getCreatedAt();
        final var acualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(50);

        actualGenre.activate();

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(acualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(acualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateActivate_shouldReceiveGenreUpdated() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("Ronaldo", false);

        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
        Assertions.assertNotNull(actualGenre.getCreatedAt());

        final var acualCreatedAt = actualGenre.getCreatedAt();
        final var acualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(50);

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(acualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(acualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() throws InterruptedException {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenre("Ronaldo", true);

        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre);
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var acualCreatedAt = actualGenre.getCreatedAt();
        final var acualUpdatedAt = actualGenre.getUpdatedAt();

        Thread.sleep(50);

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(acualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(acualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException() throws InterruptedException {
        final String expectedName = null;
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualGenre = Genre.newGenre("Ronaldo", true);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK() throws InterruptedException {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = new ArrayList<CategoryID>();

        final var actualGenre = Genre.newGenre("Ronaldo", false);

        final var acualCreatedAt = actualGenre.getCreatedAt();
        final var acualUpdatedAt = actualGenre.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, null);
        });

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(acualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(acualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGente_whenCallAddCategory_shouldReceiveOK() throws InterruptedException {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");
        final List<CategoryID> expectedCategories = List.of(seriesID, moviesID);

        final var actualGenre = Genre.newGenre("Ronaldo", false);

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();
        Thread.sleep(100);
        actualGenre.update(expectedName, expectedIsActive, Collections.emptyList());

        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK() throws InterruptedException {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("456");

        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of(moviesID); // only remain movie id

        final var actualGenre = Genre.newGenre("Ronaldo", expectedIsActive);
        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();
        Thread.sleep(100);

        actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID)); // go 2 elements
        Assertions.assertEquals(2, actualGenre.getCategories().size());

        actualGenre.removeCategory(seriesID);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullAsCategoriesGente_whenCallRemoveCategory_shouldReceiveOK() throws InterruptedException {
        final var seriesID = CategoryID.from("123");
        final var moviesID = CategoryID.from("123");

        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = List.of(moviesID, seriesID);

        final var actualGenre = Genre.newGenre("Ronaldo", false);
        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();
        Thread.sleep(100);

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);


        actualGenre.removeCategory(null);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }
}
