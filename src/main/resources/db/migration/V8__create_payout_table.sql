-- =========================
-- PAYOUTS
-- =========================
CREATE TABLE payouts
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    booking_id BIGINT NOT NULL,
    photographer_id BIGINT NOT NULL,

    total_amount DOUBLE,
    commission DOUBLE,
    payout_amount DOUBLE,

    status VARCHAR(50),

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME,

    CONSTRAINT uk_payout_booking
        UNIQUE (booking_id),

    CONSTRAINT fk_payout_booking
        FOREIGN KEY (booking_id)
            REFERENCES bookings (id),

    CONSTRAINT fk_payout_photographer
        FOREIGN KEY (photographer_id)
            REFERENCES photographers (id)
);