#CREATE DATABASE IF NOT EXISTS admarket_db;
#USE admarket_db;

CREATE TABLE IF NOT EXISTS user_info
(
    id              BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(20) NOT NULL,
    last_name       VARCHAR(20) NOT NULL,
    sur_name        VARCHAR(20),
    country         VARCHAR(50) NOT NULL,
    city            VARCHAR(50) NOT NULL,
    street_address  VARCHAR(60) NOT NULL,
    postal_code     VARCHAR(10) NOT NULL,
    state           VARCHAR(50),
    additional_info TEXT
);

CREATE TABLE IF NOT EXISTS users
(
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(20)  NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    email           VARCHAR(50)  NOT NULL UNIQUE,
    phone_number    VARCHAR(15)  NOT NULL UNIQUE,
    date_registered TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
    date_updated    TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
    account_status  ENUM ('NOACTIVATE', 'ACTIVE', 'BLOCKED') DEFAULT 'NOACTIVATE',
    user_info_id    BIGINT,
    FOREIGN KEY (user_info_id) REFERENCES user_info (id)
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT,
    role    ENUM ('USER', 'ADMIN') NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS user_images
(
    user_id   BIGINT,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, image_url),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS user_reviews
(
    id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    rating      INT    NOT NULL,
    reviewer_id BIGINT,
    seller_id   BIGINT,
    comment     TEXT NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY idx_review (reviewer_id, seller_id, comment(255)),
    CHECK (rating >= 1 AND rating <= 5),
    FOREIGN KEY (reviewer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chat_messages
(
    id             BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sender_id      BIGINT,
    receiver_id    BIGINT,
    text   TEXT NOT NULL,
    is_read        BOOLEAN   DEFAULT FALSE,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categories
(
    name        VARCHAR(255) NOT NULL PRIMARY KEY,
    parent_name VARCHAR(255),
    FOREIGN KEY (parent_name) REFERENCES categories (name) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ads
(
    id             BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(100)   NOT NULL,
    category_name  VARCHAR(255),
    price          DECIMAL(10, 2) NOT NULL,
    currency       VARCHAR(3)     NOT NULL,
    description    TEXT,
    ad_status      ENUM ('ACTIVE', 'INACTIVE', 'SOLD') DEFAULT 'ACTIVE',
    item_condition ENUM ('NONE', 'NEW', 'USED')        DEFAULT 'NONE',
    user_id        BIGINT,
    views_count    INT                                 DEFAULT 0,
    date_created   TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,
    date_updated   TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (title, category_name),
    FOREIGN KEY (category_name) REFERENCES categories (name) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ad_images
(
    ad_id     BIGINT,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (ad_id, image_url),
    FOREIGN KEY (ad_id) REFERENCES ads (id)
);

CREATE TABLE IF NOT EXISTS saved_ads
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ad_id      BIGINT,
    user_id    BIGINT,
    saved_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (ad_id, user_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (ad_id) REFERENCES ads (id) ON DELETE CASCADE
);

