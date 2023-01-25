package com.catalog.application.category.retrieve;

import com.catalog.IntegrationTest;
import com.catalog.application.category.create.CreateCategoryUseCase;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjects() {
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(categoryRepository);
    }

}
