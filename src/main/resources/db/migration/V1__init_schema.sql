-- =========================
-- USERS
-- =========================
CREATE TABLE users
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(255),
    phone_number  VARCHAR(50),
    gender        VARCHAR(20),
    birth_date    DATE,
    avatar_url    VARCHAR(255),
    status        VARCHAR(50)  NOT NULL,
    created_at    DATETIME     NOT NULL,
    updated_at    DATETIME,
    deleted_at    DATETIME
);

-- =========================
-- ROLES
-- =========================
CREATE TABLE roles
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- =========================
-- USER_ROLES (Many-to-Many)
-- =========================
CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- =========================
-- PHOTOGRAPHERS
-- =========================
CREATE TABLE photographers
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,

    user_id          BIGINT      NOT NULL UNIQUE,

    bio              TEXT,
    experience_years INT,

    approval_status  VARCHAR(50) NOT NULL,
    approved_at      DATETIME,

    created_at       DATETIME    NOT NULL,
    updated_at       DATETIME,
    deleted_at       DATETIME,

    CONSTRAINT fk_photographer_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);


-- =========================
-- PHOTO_CONCEPTS
-- =========================
CREATE TABLE photo_concepts
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description TEXT
);

-- =========================
-- PHOTO_PACKAGES
-- =========================
CREATE TABLE photo_packages
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,

    photographer_id  BIGINT       NOT NULL,

    title            VARCHAR(255) NOT NULL,
    description      TEXT,

    price            INT          NOT NULL,
    duration_minutes INT          NOT NULL,

    active           BOOLEAN      NOT NULL,

    created_at       DATETIME     NOT NULL,
    updated_at       DATETIME,

    CONSTRAINT fk_package_photographer
        FOREIGN KEY (photographer_id)
            REFERENCES photographers (id)
);


-- =========================
-- PHOTO_PACKAGE_CONCEPTS (Many-to-Many)
-- =========================
CREATE TABLE photo_package_concepts
(
    photo_package_id BIGINT NOT NULL,
    photo_concept_id BIGINT NOT NULL,
    PRIMARY KEY (photo_package_id, photo_concept_id),
    CONSTRAINT fk_ppc_package
        FOREIGN KEY (photo_package_id)
            REFERENCES photo_packages (id),
    CONSTRAINT fk_ppc_concept
        FOREIGN KEY (photo_concept_id)
            REFERENCES photo_concepts (id)
);

-- =========================
-- Bookings
-- =========================
CREATE TABLE bookings
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,

    user_id          BIGINT      NOT NULL,
    photographer_id  BIGINT      NOT NULL,
    photo_package_id BIGINT      NOT NULL,

    start_time       DATETIME    NOT NULL,
    end_time         DATETIME    NOT NULL,

    status           VARCHAR(50) NOT NULL,

    customer_status     VARCHAR(50) NOT NULL,
    photographer_status VARCHAR(50) NOT NULL,

    note             TEXT,

    created_at       DATETIME    NOT NULL,
    updated_at       DATETIME,

    CONSTRAINT fk_booking_user
        FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT fk_booking_photographer
        FOREIGN KEY (photographer_id) REFERENCES photographers (id),

    CONSTRAINT fk_booking_package
        FOREIGN KEY (photo_package_id) REFERENCES photo_packages (id)
);

-- =========================
-- Photographer Availability
-- =========================
CREATE TABLE photographer_availability
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    photographer_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME,

    CONSTRAINT fk_availability_photographer
        FOREIGN KEY (photographer_id)
            REFERENCES photographers(id)
);