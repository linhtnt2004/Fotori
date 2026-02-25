package com.example.fotori.common.resolver;

import com.example.fotori.common.enums.BookingActorStatus;
import com.example.fotori.common.enums.BookingStatus;

import static com.example.fotori.common.enums.BookingActorStatus.*;

public final class BookingStatusResolver {

    public static BookingStatus resolve(
        BookingActorStatus customer,
        BookingActorStatus photographer
    ) {

        if (customer == CANCELLED || photographer == CANCELLED) {
            return BookingStatus.CANCELLED;
        }

        if (customer == DONE && photographer == DONE) {
            return BookingStatus.DONE;
        }

        if (customer == IN_PROGRESS || photographer == IN_PROGRESS) {
            return BookingStatus.IN_PROGRESS;
        }

        if (customer == ACCEPTED && photographer == ACCEPTED) {
            return BookingStatus.ACCEPTED;
        }

        return BookingStatus.PENDING;
    }
}

