INSERT INTO tb_user (username, login, password) VALUES ('admin', 'admin', '$2a$10$niTy0wkb2ngpV3/TNULkjelElc6m29m9awmyfpb9YlM4P62nz3DTG');

INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_TEACHER');
INSERT INTO tb_role (authority) VALUES ('ROLE_MANAGER');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1,1);

