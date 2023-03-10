package com.catalog.e2e.castmember;

import com.catalog.E2ETest;
import com.catalog.Fixture;
import com.catalog.Fixture.CastMembers;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.castmember.CastMemberType;
import com.catalog.e2e.MockDsl;
import com.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CastMemberRepository castMemberRepository;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withPassword("root")
                    .withUsername("root")
                    .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MY_SQL_CONTAINER.getMappedPort(3306);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = CastMembers.type();

        final var actualMemberId = givenACastMember(expectedName, expectedType);

        final var actualMember = castMemberRepository.findById(actualMemberId.getValue()).get();

        // then
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingANewCastMemberWithInvalidValues() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "'name' should not be null";


        givenACastMemberResult(expectedName, expectedType)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void asACalogAdminIShouldBeAbleToNavigateToAllMembers() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Monoa", CastMemberType.ACTOR);

        listCastMembers(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Jason Monoa")))
        ;

        listCastMembers(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(1)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Quentin Tarantino")))
        ;

        listCastMembers(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(2)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")))
        ;

        listCastMembers(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(3)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)))
        ;
    }

    @Test
    public void asACalogAdminIShouldBeAbleToSearchToAllMembers() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Monoa", CastMemberType.ACTOR);

        listCastMembers(0, 1, "vin")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.perPage", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")))
        ;
    }

    @Test
    public void asACalogAdminIShouldBeAbleToSortAllMembersDesc() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember("Vin Diesel", CastMemberType.ACTOR);
        givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
        givenACastMember("Jason Monoa", CastMemberType.ACTOR);

        listCastMembers(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(0)))
                .andExpect(jsonPath("$.perPage", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Quentin Tarantino")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Jason Monoa")))
        ;
    }

    @Test
    public void asACalogAdminIShouldBeAbleToGetACastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember(expectedName, expectedType);

        final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType.name(), actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    public void asACalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCastMember() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        retrieveACastMemberResult(CastMemberID.from("123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("CastMember with ID 123 was not found")
                ));
    }

    @Test
    public void asACalogAdminIShouldBeAbleToUpdateACastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember("vin d", CastMemberType.DIRECTOR);

        updateACastMember(actualId, expectedName, expectedType).
                andExpect(status().isOk());

        final var actualMember = retrieveACastMember(actualId);

        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType.name(), actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertEquals(actualId.getValue(), actualMember.id());
    }

    @Test
    public void asACalogAdminIShouldBeAbleToSeeATreatedErrorByUpdatingACastMemberWithInvalidValue() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        final var expectedName = "";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be empty";

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember("vin d", CastMemberType.DIRECTOR);

        updateACastMember(actualId, expectedName, expectedType).
                andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)))
        ;
    }

    @Test
    public void asACalogAdminIShouldBeAbleToDeleteACastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        final var actualId = givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        deleteACastMember(actualId).andExpect(status().isNoContent());

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertFalse(castMemberRepository.existsById(actualId.getValue()));
    }

    @Test
    public void asACalogAdminIShouldBeAbleToNotSeeAErrorByDeletingAInvalidCastMemberByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, castMemberRepository.count());

        givenACastMember(Fixture.name(), Fixture.CastMembers.type());
        givenACastMember(Fixture.name(), Fixture.CastMembers.type());

        Assertions.assertEquals(2, castMemberRepository.count());

        deleteACastMember(CastMemberID.from("123")).andExpect(status().isNoContent());

        Assertions.assertEquals(2, castMemberRepository.count());
    }
}
