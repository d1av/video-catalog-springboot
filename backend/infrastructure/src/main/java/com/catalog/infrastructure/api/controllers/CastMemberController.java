package com.catalog.infrastructure.api.controllers;

import com.catalog.application.castmember.create.CreateCastMemberCommand;
import com.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.catalog.infrastructure.api.CastMemberAPI;
import com.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.catalog.infrastructure.castmember.presenters.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;

    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase,
                                final GetCastMemberByIdUseCase getCastMemberByIdUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) throws URISyntaxException {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(new URI("/cast_members/" + output.id())).body(output);
    }

    @Override
    public CastMemberResponse getById(String id) {
        return CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }
}
