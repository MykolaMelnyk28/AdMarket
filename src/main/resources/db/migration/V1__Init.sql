
#CREATE DATABASE admarket_db;

USE admarket_db;
CREATE TABLE user_addresses
(
    id              BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    country         VARCHAR(50) NOT NULL,
    city            VARCHAR(50) NOT NULL,
    street_address  VARCHAR(60) NOT NULL,
    postal_code     VARCHAR(10) NOT NULL,
    state           VARCHAR(50),
    additional_info TEXT
);

CREATE TABLE users
(
    id              BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(20)  NOT NULL UNIQUE,
    email           VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    phone_number    VARCHAR(15) UNIQUE,
    first_name      VARCHAR(20),
    last_name       VARCHAR(20),
    sur_name        VARCHAR(20),
    full_name       VARCHAR(40),
    address_id      BIGINT,
    date_registered TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
    date_updated    TIMESTAMP                                DEFAULT CURRENT_TIMESTAMP,
    account_status  ENUM ('NOACTIVATE', 'ACTIVE', 'BLOCKED') DEFAULT 'NOACTIVATE',
    FOREIGN KEY (address_id) REFERENCES user_addresses (id)
);

CREATE TABLE categories
(
    name      VARCHAR(255) NOT NULL PRIMARY KEY,
    parent_name VARCHAR(255),
    FOREIGN KEY (parent_name) REFERENCES categories (name) ON DELETE CASCADE
);

CREATE TABLE ads
(
    id             BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(100)   NOT NULL,
    description    TEXT,
    price          DECIMAL(10, 2) NOT NULL,
    currency       VARCHAR(3)     NOT NULL,
    ad_status      ENUM ('ACTIVE', 'INACTIVE', 'SOLD') DEFAULT 'ACTIVE',
    item_condition ENUM ('NONE', 'NEW', 'USED')        DEFAULT 'NONE',
    category_name  VARCHAR(255),
    user_id        BIGINT,
    views_count    INT                                 DEFAULT 0,
    date_created   TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,
    date_updated   TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (title, category_name),
    FOREIGN KEY (category_name) REFERENCES categories (name) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_images
(
    user_id   BIGINT,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, image_url),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE ad_images
(
    ad_id     BIGINT,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (ad_id, image_url),
    FOREIGN KEY (ad_id) REFERENCES ads (id)
);

CREATE TABLE seller_reviews
(
    id          BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    rating      INT    NOT NULL,
    reviewer_id BIGINT,
    seller_id   BIGINT,
    comment     TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY idx_review (reviewer_id, seller_id, comment(255)),
    CHECK (rating >= 1 AND rating <= 5),
    FOREIGN KEY (reviewer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE saved_ads
(
    id         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ad_id      BIGINT,
    user_id    BIGINT,
    saved_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (ad_id, user_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (ad_id) REFERENCES ads (id) ON DELETE CASCADE
);

CREATE TABLE chat_messages
(
    id             BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sender_id      BIGINT,
    receiver_id    BIGINT,
    message_text   TEXT,
    send_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read        BOOLEAN   DEFAULT FALSE,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE
);