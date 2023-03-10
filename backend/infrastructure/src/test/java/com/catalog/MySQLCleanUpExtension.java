package com.catalog;

import com.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import com.catalog.infrastructure.category.persistence.CategoryRepository;
import com.catalog.infrastructure.genre.persistence.GenreRepository;
import com.catalog.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {

        final var repositories = SpringExtension.getApplicationContext(context)
                .getBeansOfType(CrudRepository.class)
                .values();

        cleanUp(repositories);
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                appContext.getBean(VideoRepository.class),
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CastMemberRepository.class),
                appContext.getBean(CategoryRepository.class)
        ));


    }

    private void cleanUp(Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}

