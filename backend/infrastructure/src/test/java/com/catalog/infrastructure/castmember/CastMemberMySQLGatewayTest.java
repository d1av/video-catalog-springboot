package com.catalog.infrastructure.castmember;

import com.catalog.Fixture;
import com.catalog.MySQLGatewayTest;
import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.castmember.CastMemberType;
import com.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {
    @Autowired
    private CastMemberMySQLGateway castMemberMySQLGateway;
    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testDependencies() {
        Assertions.assertNotNull(castMemberMySQLGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    public void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        // when
        final var actualMember = castMemberMySQLGateway.create(CastMember.with(aMember));

        // then
        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals(expectedId.getValue(), actualMember.getId().getValue());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldRefreshIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember("Stallone", expectedType);
        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        // when
        final var currentMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals(expectedId.getValue(), currentMember.getId());
        Assertions.assertEquals("Stallone", currentMember.getName());
        Assertions.assertEquals(CastMemberType.ACTOR, currentMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), currentMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), currentMember.getUpdatedAt());

        final var updatedMember = castMemberMySQLGateway.update(
                CastMember.with(aMember).update(expectedName, expectedType)
        );
        // then
        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals(expectedId.getValue(), updatedMember.getId().getValue());
        Assertions.assertEquals(expectedName, updatedMember.getName());
        Assertions.assertEquals(expectedType, updatedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), updatedMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(updatedMember.getUpdatedAt()));

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        castMemberMySQLGateway.deleteById(aMember.getId());
        // then
        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void givenAInvalidId_whenCallsDeleteById_shouldBeIgnore() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        castMemberMySQLGateway.deleteById(CastMemberID.from("123"));

        // then
        Assertions.assertEquals(1, castMemberRepository.count());
    }
}