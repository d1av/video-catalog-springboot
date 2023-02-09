package com.catalog.application.castmember.retrieve.list;

import com.catalog.Fixture;
import com.catalog.Fixture.CastMembers;
import com.catalog.IntegrationTest;
import com.catalog.application.castmember.retrieve.list.CastMemberListOutput;
import com.catalog.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.catalog.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.pagination.Pagination;
import com.catalog.domain.pagination.SearchQuery;
import com.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import com.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static com.catalog.Fixture.CastMembers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class ListCastMemberUseCaseIT {
    @Autowired
    private ListCastMembersUseCase useCase;
    @Autowired
    private CastMemberRepository castMemberRepository;
    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidQuery_whenCallsListCastMember_shouldReturnAll() {
        // given
        final var members = List.of(
                CastMember.newMember(Fixture.name(), type()),
                CastMember.newMember(Fixture.name(), type())
        );

        this.castMemberRepository.saveAllAndFlush(
                members.stream()
                        .map(CastMemberJpaEntity::from)
                        .toList()
        );

        Assertions.assertEquals(2, castMemberRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream().toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                expectedItems
        );

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(actualOutput.items(), savedMembersMap(expectedItems));
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.items().size()
                        && actualOutput.items().containsAll(savedMembersMap(expectedItems))
        );

        verify(castMemberGateway).findAll(eq(aQuery));
    }

    private List<CastMemberListOutput> savedMembersMap(List<CastMember> fromDbList) {
        return fromDbList.stream()
                .map(CastMemberListOutput::from)
                .toList();
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMemberAndIsEmpty_shouldReturn() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<CastMember>of();

        Assertions.assertEquals(0, castMemberRepository.count());

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(savedMembersMap(expectedItems), actualOutput.items());

        verify(castMemberGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).findAll(any());

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findAll(eq(aQuery));
    }
}
