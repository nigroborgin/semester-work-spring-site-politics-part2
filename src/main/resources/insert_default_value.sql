INSERT INTO role_of_user (id, name)
VALUES (DEFAULT, 'ROLE_ADMIN');

INSERT INTO role_of_user (id, name)
VALUES (DEFAULT, 'ROLE_USER');

INSERT INTO account (id, role_id, username, password, email, picture_url)
VALUES (DEFAULT, 1, 'admin', '$2a$08$R66Hw/MnMs/DA9RncjAjZOoUilTpJe8tI2Ad9rdpsQckihitLFzyO', 'admin@springpolitics.com', '/static/picture/main.jpg');