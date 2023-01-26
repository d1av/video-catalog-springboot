package com.catalog.application.category.retrieve.list;

import com.catalog.IntegrationTest;
import com.catalog.domain.category.Category;
import com.catalog.domain.category.CategorySearchQuery;
import com.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
                Category.newCategory("Documentario", null, true),
                Category.newCategory("Sports", null, true),
                Category.newCategory("Kids", null, true),
                Category.newCategory("Series", null, true)
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


}
