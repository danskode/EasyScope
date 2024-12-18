use EasyScope;

-- Data in accounts table -- 
INSERT INTO accounts (account_name, account_password, account_type) VALUES ('Anna', 'minkode', 'ADMIN');
INSERT INTO accounts (account_name, account_password, account_type) VALUES ('Jens', '1234', 'PROJECT_MANAGER');
INSERT INTO accounts (account_name, account_password, account_type) VALUES ('Otto', 'password', 'TEAM_MEMBER');

-- Data in project table -- 
INSERT INTO project (project_name, project_description, project_is_finished, account_id_fk) VALUES ('Novo Nordisk', 'The client wants to establish a new digital footprint for its new weightloss medicine', 1, 2);
INSERT INTO project (project_name, project_description, project_is_finished, account_id_fk) VALUES ('Tesla', 'The client wants an app for more republican votes', 0, 2);

-- Data in sub_project table --
INSERT INTO sub_project (sub_project_name, sub_project_description, sub_project_is_finished, sub_project_deadline, project_id_fk) VALUES ('A new website', 'Full-stack web app', 1, '2025-12-12', 1);
INSERT INTO sub_project (sub_project_name, sub_project_description, sub_project_is_finished, sub_project_deadline, project_id_fk) VALUES ('A political app', 'Needs to work in Tesla cars', 0, '2025-12-12', 2);

-- Data in task --
INSERT INTO task (task_name, task_description, task_is_finished, task_start_date, sub_project_id_fk) VALUES ('Frontend', 'Beautiful CSS', 1, '2024-12-12', 1);
INSERT INTO task (task_name, task_description, task_is_finished, task_start_date, sub_project_id_fk) VALUES ('Backend', 'Beautiful Java', 1, '2024-12-12', 1);
INSERT INTO task (task_name, task_description, task_is_finished, task_start_date, sub_project_id_fk) VALUES ('Frontend', 'Make design great again', 0, '2024-12-12', 2);
INSERT INTO task (task_name, task_description, task_is_finished, task_start_date, sub_project_id_fk) VALUES ('Mockup', 'Make wireframes', 0, '2024-12-12', 1);

-- Data in task_member --
INSERT INTO task_member (account_id_fk, task_id_fk) VALUES (3, 1);
INSERT INTO task_member (account_id_fk, task_id_fk) VALUES (3, 2);
INSERT INTO task_member (account_id_fk, task_id_fk) VALUES (3, 3);
INSERT INTO task_member (account_id_fk, task_id_fk) VALUES (3, 4);

-- Data in sub_project_member -- 
INSERT INTO sub_project_member (account_id_fk, sub_project_id_fk) VALUES (2, 1);
INSERT INTO sub_project_member (account_id_fk, sub_project_id_fk) VALUES (2, 2);

-- Data in task_hours_estimated -- 
INSERT INTO task_hours_estimated (task_hours_estimated, task_id_fk, account_id_fk) VALUES (7.5, 1, 3);
INSERT INTO task_hours_estimated (task_hours_estimated, task_id_fk, account_id_fk) VALUES (15.0, 2, 3);
INSERT INTO task_hours_estimated (task_hours_estimated, task_id_fk, account_id_fk) VALUES (2.5, 3, 3);
INSERT INTO task_hours_estimated (task_hours_estimated, task_id_fk, account_id_fk) VALUES (5, 4, 3);


-- Data in task_hours_realized --
INSERT INTO task_hours_realized (task_hours_realized, task_id_fk, account_id_fk) VALUES (4.5, 4, 3);
INSERT INTO task_hours_realized (task_hours_realized, task_id_fk, account_id_fk) VALUES (5, 3, 3);

