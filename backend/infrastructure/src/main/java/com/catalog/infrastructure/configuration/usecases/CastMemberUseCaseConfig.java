package com.catalog.infrastructure.configuration.usecases;

import com.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.catalog.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.catalog.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import com.catalog.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.catalog.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }
}
