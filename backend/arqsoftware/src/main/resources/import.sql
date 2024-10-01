INSERT INTO tb_user (username, login, password) VALUES ('Joao', 'Joaoorg', '$2a$10$/x3U57tUoAoWNkWTO5YLEON5tq1S0QAhi8o8X7K2YHe8zH3jWfnB.');
INSERT INTO tb_user (username, login, password) VALUES ('Maria', 'Mariaorg', '$2a$10$/x3U57tUoAoWNkWTO5YLEON5tq1S0QAhi8o8X7K2YHe8zH3jWfnB.');

INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_TEACHER');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1,1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2,1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2,2);





