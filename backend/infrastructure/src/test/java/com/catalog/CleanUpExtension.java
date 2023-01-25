package com.catalog;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

public class CleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        final var repositories = SpringExtension.getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values();
        cleanUp(repositories);
    }

    private void cleanUp(Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}

