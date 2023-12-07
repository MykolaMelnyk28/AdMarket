INSERT INTO categories(name, parent_id, is_leaf)
VALUES
('all', NULL, FALSE),
('other', 1, TRUE);

INSERT INTO users(username, password_hash, email, phone_number, account_status)
VALUES
('admin0', '$2a$10$w3tc566f2ji2WVVIowkyhe2ddSpEP2YZQnRpRQtzQrV3PrHskMEu2', 'admin0@mail.com', '0000000000', 'ACTIVE');

INSERT INTO user_roles(user_id, role)
VALUES
(1, 'ROLE_ADMIN');