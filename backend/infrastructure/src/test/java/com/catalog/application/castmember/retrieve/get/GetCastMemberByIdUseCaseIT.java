package com.catalog.application.castmember.retrieve.get;

import com.catalog.Fixture;
import com.catalog.IntegrationTest;
import com.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.castmember.CastMemberID;
import com.catalog.domain.exceptions.NotFoundException;
import com.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class GetCastMemberByIdUseCaseIT {
    @Autowired
    private GetCastMemberByIdUseCase useCase;
    @Autowired
    private CastMemberRepository castMemberRepository;
    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();

        // when
        final var actualOutput = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(aMember.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualOutput.updatedAt());

        verify(castMemberGateway).findById(any());

        Assertions.assertEquals(1, this.castMemberRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCastMemberNotExists_shouldReturnNotFoundException() {
        // given
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedErrorCount = 1;

        // when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.firstError().message());

        verify(castMemberGateway).findById(any());
    }
}
