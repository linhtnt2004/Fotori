-- =========================
-- USERS
-- =========================
CREATE TABLE users
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    full_name      VARCHAR(255),
    phone_number   VARCHAR(50),
    gender         VARCHAR(20),
    birth_date     DATE,
    address        VARCHAR(255),
    avatar_url     VARCHAR(255),
    email_verified BOOLEAN      NOT NULL DEFAULT FALSE,
    status         VARCHAR(50)  NOT NULL,
    created_at     DATETIME     NOT NULL,
    updated_at     DATETIME,
    deleted_at     DATETIME
);

-- =========================
-- REFRESH TOKENS
-- =========================
CREATE TABLE refresh_tokens
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    token       VARCHAR(255) NOT NULL UNIQUE,
    expiry_date DATETIME     NOT NULL,
    revoked     BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id     BIGINT       NOT NULL,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
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
    city             TEXT,
    experience_years INT,

    average_rating DOUBLE,

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
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,

    user_id             BIGINT      NOT NULL,
    photographer_id     BIGINT      NOT NULL,
    photo_package_id    BIGINT      NOT NULL,

    start_time          DATETIME    NOT NULL,
    end_time            DATETIME    NOT NULL,

    status              VARCHAR(50) NOT NULL,

    customer_status     VARCHAR(50) NOT NULL,
    photographer_status VARCHAR(50) NOT NULL,

    note                TEXT,

    created_at          DATETIME    NOT NULL,
    updated_at          DATETIME,

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
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    photographer_id BIGINT   NOT NULL,
    start_time      DATETIME NOT NULL,
    end_time        DATETIME NOT NULL,
    created_at      DATETIME NOT NULL,
    updated_at      DATETIME,
    deleted_at      DATETIME,

    CONSTRAINT fk_availability_photographer
        FOREIGN KEY (photographer_id)
            REFERENCES photographers (id)
);

-- =========================
-- EMAIL VERIFY TOKENS
-- =========================
CREATE TABLE email_verification_tokens
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,

    user_id     BIGINT       NOT NULL,

    token       VARCHAR(255) NOT NULL UNIQUE,

    expiry_date DATETIME     NOT NULL,

    verified    BOOLEAN DEFAULT FALSE,

    created_at  DATETIME     NOT NULL,

    CONSTRAINT fk_verify_token_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

-- =========================
-- REVIEWS
-- =========================
CREATE TABLE reviews
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,

    customer_id     BIGINT   NOT NULL,
    photographer_id BIGINT   NOT NULL,
    booking_id      BIGINT   NOT NULL UNIQUE,

    rating          INT,
    skills          INT,
    attitude        INT,
    punctuality     INT,
    post_processing INT,

    comment         TEXT,
    response        TEXT,

    created_at      DATETIME NOT NULL,
    updated_at      DATETIME,
    deleted_at      DATETIME,

    CONSTRAINT fk_review_customer
        FOREIGN KEY (customer_id)
            REFERENCES users (id),

    CONSTRAINT fk_review_photographer
        FOREIGN KEY (photographer_id)
            REFERENCES photographers (id),

    CONSTRAINT fk_review_booking
        FOREIGN KEY (booking_id)
            REFERENCES bookings (id)
);

-- =========================
-- PORTFOLIO IMAGES
-- =========================
CREATE TABLE portfolio_images
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    photographer_id BIGINT NOT NULL,

    image_url VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(100),

    description TEXT,

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME,

    CONSTRAINT fk_portfolio_photographer
        FOREIGN KEY (photographer_id)
            REFERENCES photographers (id)
);

-- =========================
-- WISHLISTS
-- =========================
CREATE TABLE wishlists
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    user_id BIGINT NOT NULL,
    photographer_id BIGINT NOT NULL,

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME,

    CONSTRAINT fk_wishlist_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT fk_wishlist_photographer
        FOREIGN KEY (photographer_id)
            REFERENCES photographers (id),

    CONSTRAINT uq_wishlist_user_photographer
        UNIQUE (user_id, photographer_id)
);

-- =========================
-- NOTIFICATIONS
-- =========================
CREATE TABLE notifications
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    user_id BIGINT NOT NULL,

    title VARCHAR(255),

    content TEXT,

    is_read BOOLEAN DEFAULT FALSE,

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME,

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
);

-- =========================
-- BLOG CATEGORIES
-- =========================
CREATE TABLE blog_categories
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) UNIQUE
);

-- =========================
-- BLOGS
-- =========================
CREATE TABLE blogs
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,

    excerpt TEXT,
    content LONGTEXT,

    cover_image VARCHAR(255),

    category_id BIGINT,

    featured BOOLEAN DEFAULT FALSE,
    likes INT DEFAULT 0,

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME,

    CONSTRAINT fk_blog_category
        FOREIGN KEY (category_id)
            REFERENCES blog_categories (id)
);

-- =========================
-- FORUM CATEGORIES
-- =========================
CREATE TABLE forum_categories
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME
);

-- =========================
-- FORUM THREADS
-- =========================
CREATE TABLE forum_threads
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    title VARCHAR(255) NOT NULL,
    content TEXT,

    author_id BIGINT,
    category_id BIGINT,

    likes INT DEFAULT 0,

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME,

    CONSTRAINT fk_thread_author
        FOREIGN KEY (author_id)
            REFERENCES users(id),

    CONSTRAINT fk_thread_category
        FOREIGN KEY (category_id)
            REFERENCES forum_categories(id)
);

-- =========================
-- FORUM REPLIES
-- =========================
CREATE TABLE forum_replies
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    thread_id BIGINT,
    author_id BIGINT,

    content TEXT,

    likes INT DEFAULT 0,
    accepted BOOLEAN DEFAULT FALSE,

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME,

    CONSTRAINT fk_reply_thread
        FOREIGN KEY (thread_id)
            REFERENCES forum_threads(id),

    CONSTRAINT fk_reply_author
        FOREIGN KEY (author_id)
            REFERENCES users(id)
);

-- =========================
-- VOUCHERS
-- =========================
CREATE TABLE vouchers
(
    code VARCHAR(100) PRIMARY KEY,

    type VARCHAR(50) NOT NULL,

    value INT NOT NULL,

    min_order_value INT,
    max_discount INT,

    expires_at DATETIME NOT NULL,

    usage_limit INT,
    used_count INT NOT NULL DEFAULT 0,

    description TEXT,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    deleted_at DATETIME
);