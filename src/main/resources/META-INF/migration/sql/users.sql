CREATE TABLE users
(
   id BIGSERIAL NOT NULL,
   username VARCHAR(45) NOT NULL UNIQUE, 
   password CHAR(60) NOT NULL, 
   enabled BOOLEAN NOT NULL DEFAULT TRUE, 
   CONSTRAINT pk_users PRIMARY KEY (id)
);

INSERT INTO users(username, password) VALUES
('user', '$2a$10$rK3xyny8S/1rF6bAAdQw1OEQ2JG9sflmtTp6E3gW6dBpEtw.aENC2'),
('admin', '$2a$10$rK3xyny8S/1rF6bAAdQw1OEQ2JG9sflmtTp6E3gW6dBpEtw.aENC2');

CREATE TABLE user_roles
(
   id BIGSERIAL NOT NULL, 
   user_id BIGINT NOT NULL, 
   role VARCHAR(45) NOT NULL,
   CONSTRAINT pk_user_roles PRIMARY KEY (id),
   CONSTRAINT fk_user_roles_users_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO user_roles(user_id, role) VALUES
((SELECT id FROM users WHERE username = 'user'), 'ROLE_USER'),
((SELECT id FROM users WHERE username = 'admin'), 'ROLE_USER'),
((SELECT id FROM users WHERE username = 'admin'), 'ROLE_ADMIN');