package com.catalog;


import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@ComponentScan(
        basePackages = "com.catalog",
        includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")
})
@DataJpaTest
@ExtendWith(MySQLCleanUpExtension.class)
public @interface MySQLGatewayTest {
}
