package com.catalog.infrastructure.category;

import com.catalog.infrastructure.category.persistence.CategoryMySQLGateway;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {
    @Autowired
    private CategoryMySQLGateway categoryGateway;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testedInjectedDependencies() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(categoryRepository);
    }


}
