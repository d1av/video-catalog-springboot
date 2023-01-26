package com.catalog.application.category.retrieve.list;

import com.catalog.IntegrationTest;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategorySearchQuery;
import com.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

@IntegrationTest
public class ListCategoriesUseCaseIT {
    @Autowired
    private ListCategoriesUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Netflix Originals", "Titulos de autoria da netflix", true),
                Category.newCategory("Amazon Originals", "Titulos de autoria da Amazon", true),
                Category.newCategory("Documentários", null, true),
                Category.newCategory("Sports", null, true),
                Category.newCategory("Kids", "Categoria para crianças", true),
                Category.newCategory("Séries", null, true)
        ).map(CategoryJpaEntity::from).toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerm_whenTermDoesntMatchsPrePersisted_shouldReturnEmptyPage() {
        final var expectedPage = 8;
        final var expectedPerPage = 10;
        final var expectedTerms = "jasiajj 1jijsiajdija";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());


    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix Originals",
            "zon,0,10,1,1,Amazon Originals",
            "ki,0,10,1,1,Kids",
            "crianças,0,10,1,1,Kids",
            "da Amazon,0,10,1,1,Amazon Originals",
    })
    public void givenAValidTerm_whenCallsListCategories_shouldReturnCategories(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }


    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc,0,10,7,7,Séries",
            "description,asc,0,10,7,7,Filmes",
            "description,desc,0,10,7,7,Netflix Originals",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategories(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedTerms = "";
        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentários",
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,Netflix Originals;Sports",
            "3,2,1,7,Séries",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedTerms = "";
        final var expectedDirection = "asc";
        final var expectedSort = "name";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (String expectedName : expectedCategoryName.split(";")) {
            final String actualName = actualResult.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }

    }
}
