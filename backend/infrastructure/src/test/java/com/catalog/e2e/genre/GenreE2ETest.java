package com.catalog.e2e.genre;

import com.catalog.E2ETest;
import com.catalog.domain.category.CategoryID;
import com.catalog.domain.genre.Genre;
import com.catalog.domain.genre.GenreID;
import com.catalog.e2e.MockDsl;
import com.catalog.infrastructure.category.models.UpdateCategoryRequest;
import com.catalog.infrastructure.genre.models.UpdateGenreRequest;
import com.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withPassword("root")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MY_SQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", "O melhor filme", true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());


        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateToAllGenres() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação", true, List.of());
        givenAGenre("Esportes", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Ação")))
        ;

        listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(1)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")))
        ;

        listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(2)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Esportes")))
        ;

        listGenres(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(3)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)))
        ;
    }


    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação", true, List.of());
        givenAGenre("Esportes", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 1, "dram")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")))
        ;

    }


    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Ação", true, List.of());
        givenAGenre("Esportes", true, List.of());
        givenAGenre("Drama", true, List.of());

        listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.perPage", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Esportes")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Ação")))
        ;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", "O melhor filme", true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                        && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingNotFoundGenre() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var aRequest = MockMvcRequestBuilders.get("/genres/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Genre with ID 123 was not found")))
        ;

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToToUpdateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", "O melhor filme", true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes);

        final var actualId = givenAGenre("acao", expectedIsActive, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(
                expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive);

        updateAGenre(actualId, aRequestBody).andExpect(status().isCreated());

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToToInactivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());


        final var filmes = givenACategory("Filmes", "O melhor filme", true);

        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of(filmes);

        final var actualId = givenAGenre(expectedName, true, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(
                expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive);

        updateAGenre(actualId, aRequestBody).andExpect(status().isCreated());

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();


        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToToActivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", "O melhor filme", true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of(filmes);

        final var actualId = givenAGenre(expectedName, false, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(
                expectedName,
                mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive);

        updateAGenre(actualId, aRequestBody).andExpect(status().isCreated());

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIDs())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", "O melhor filme", true);

        final var actualId = givenAGenre("Ação", true, List.of(filmes));

        deleteAGenre(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.genreRepository.existsById(actualId.getValue()));
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void asACatalogAdminIShouldNotSeeAErrorByDeletingNotFoundGenre() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        deleteAGenre(GenreID.from("123"))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, genreRepository.count());
    }
}