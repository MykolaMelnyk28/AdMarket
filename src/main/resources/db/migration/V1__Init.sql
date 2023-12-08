#CREATE DATABASE IF NOT EXISTS admarket_db;
#USE admarket_db;

CREATE TABLE IF NOT EXISTS user_info
(
    id              BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name      VARCHAR(20),
    last_name       VARCHAR(20),
    sur_name        VARCHAR(20),
    country         VARCHAR(50),
    city            VARCHAR(50),
    street_address  VARCHAR(60),
    postal_code     VARCHAR(10),
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
    FOREIGN KEY (user_info_id) REFERENCES user_info (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT,
    role    ENUM ('ROLE_USER', 'ROLE_ADMIN') NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS user_images
(
    user_id   BIGINT,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, image_url),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS user_reviews
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    rating       INT    NOT NULL,
    reviewer_id  BIGINT,
    seller_id    BIGINT,
    comment      TEXT   NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY idx_review (reviewer_id, seller_id, comment(255)),
    CHECK (rating >= 1 AND rating <= 5),
    FOREIGN KEY (reviewer_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS chats
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS messages
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    chat_id      BIGINT NOT NULL,
    sender_id    BIGINT NOT NULL,
    receiver_id  BIGINT NOT NULL,
    text         TEXT   NOT NULL,
    is_read      BOOLEAN   DEFAULT FALSE,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (sender_id <> receiver_id),
    FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE ON UPDATE NO ACTION,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS chats_users
(
    chat_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (chat_id, user_id),
    FOREIGN KEY (chat_id) REFERENCES chats (id) ON DELETE CASCADE ON UPDATE NO ACTION,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS categories
(
    id        BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(80) NOT NULL UNIQUE,
    parent_id BIGINT,
    is_leaf   BOOLEAN      NOT NULL DEFAULT TRUE,
    FOREIGN KEY (parent_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS ads
(
    id             BIGINT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(100)   NOT NULL,
    category_id    BIGINT,
    price          DECIMAL(10, 2) NOT NULL,
    currency       VARCHAR(3)     NOT NULL,
    description    TEXT,
    ad_status      ENUM ('ACTIVE', 'INACTIVE', 'SOLD') DEFAULT 'ACTIVE',
    item_condition ENUM ('NEW', 'NONE', 'USED')        DEFAULT 'NEW',
    views_count    INT                                 DEFAULT 0,
    date_created   TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,
    date_updated   TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (title, category_id),
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS ad_images
(
    ad_id     BIGINT,
    image_url VARCHAR(255) NOT NULL,
    PRIMARY KEY (ad_id, image_url),
    FOREIGN KEY (ad_id) REFERENCES ads (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS users_ads
(
    user_id BIGINT NOT NULL,
    ad_id   BIGINT NOT NULL,
    PRIMARY KEY (user_id, ad_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE NO ACTION,
    FOREIGN KEY (ad_id) REFERENCES ads (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS saved_ads
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    ad_id        BIGINT,
    user_id      BIGINT,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (ad_id, user_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ad_id) REFERENCES ads (id) ON DELETE CASCADE ON UPDATE CASCADE
);

# TRIGGERS

CREATE TRIGGER before_insert_ads
BEFORE INSERT ON ads
FOR EACH ROW
BEGIN
    IF NOT EXISTS (SELECT 1 FROM categories c WHERE c.id = NEW.category_id AND c.is_leaf = true) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Category is not a leaf category';
    END IF;
END;
