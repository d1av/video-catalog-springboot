package com.catalog.infrastructure.api.controllers;

import com.catalog.application.castmember.create.CreateCastMemberCommand;
import com.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.catalog.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.catalog.application.castmember.update.UpdateCastMemberCommand;
import com.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.catalog.infrastructure.api.CastMemberAPI;
import com.catalog.infrastructure.castmember.models.CastMemberResponse;
import com.catalog.infrastructure.castmember.models.CreateCastMemberRequest;
import com.catalog.infrastructure.castmember.models.UpdateCastMemberRequest;
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
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;

    public CastMemberController(final CreateCastMemberUseCase createCastMemberUseCase,
                                final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
                                final UpdateCastMemberUseCase updateCastMemberUseCase,
                                final DeleteCastMemberUseCase deleteCastMemberUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
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

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest aBody) {
        final var aCommand = UpdateCastMemberCommand.with(id, aBody.name(), aBody.type());

        final var output = this.updateCastMemberUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}
