package com.catalog.application.castmember.create;

import com.catalog.domain.castmember.CastMember;
import com.catalog.domain.castmember.CastMemberGateway;
import com.catalog.domain.exceptions.NotificationException;
import com.catalog.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(CreateCastMemberCommand aCommand) {
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        final var notification = Notification.create();
        notification.validate(() -> CastMember.newMember(aName, aType));
        final var aMember = CastMember.newMember(aName, aType);

        if (notification.hasError()) {
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }

    private static void notify(Notification notification) {
        throw new NotificationException("Could not create Aggregate Cast Member", notification);
    }
}
