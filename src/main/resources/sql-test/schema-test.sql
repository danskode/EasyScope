CREATE TABLE IF NOT EXISTS accounts (
                          account_id INT NOT NULL AUTO_INCREMENT,
                          account_name VARCHAR(25) NOT NULL,
                          account_password VARCHAR(25) NOT NULL,
                          account_type VARCHAR(25) NOT NULL,
                          PRIMARY KEY(account_id)
);

CREATE TABLE IF NOT EXISTS project (
                         project_id INT NOT NULL AUTO_INCREMENT,
                         project_name VARCHAR(100) NOT NULL,
                         project_description VARCHAR(1000),
                         project_is_finished TINYINT,
                         account_id_fk INT,
                         PRIMARY KEY (project_id),
                         FOREIGN KEY (account_id_fk) REFERENCES accounts(account_id)
);


CREATE TABLE IF NOT EXISTS sub_project (
                             sub_project_id INT NOT NULL AUTO_INCREMENT,
                             sub_project_name VARCHAR(100),
                             sub_project_description VARCHAR(1000),
                             sub_project_is_finished TINYINT,
                             sub_project_deadline DATE,
                             project_id_fk INT,
                             PRIMARY KEY (sub_project_id),
                             FOREIGN KEY (project_id_fk) REFERENCES project (project_id)
);

CREATE TABLE IF NOT EXISTS task (
                      task_id INT NOT NULL AUTO_INCREMENT,
                      task_name VARCHAR(100),
                      task_description VARCHAR(1000),
                      task_is_finished TINYINT,
                      task_start_date DATE,
                      sub_project_id_fk INT,
                      PRIMARY KEY (task_id),
                      FOREIGN KEY (sub_project_id_fk) REFERENCES sub_project (sub_project_id)
);

CREATE TABLE IF NOT EXISTS task_hours_realized (
                                     task_hours_realized_id INT NOT NULL AUTO_INCREMENT,
                                     task_hours_realized FLOAT,
                                     task_id_fk INT,
                                     account_id_fk INT,
                                     PRIMARY KEY (task_hours_realized_id),
                                     FOREIGN KEY (task_id_fk) REFERENCES task (task_id) ON DELETE CASCADE,
                                     FOREIGN KEY (account_id_fk) REFERENCES accounts (account_id)
);

CREATE TABLE IF NOT EXISTS task_hours_estimated (
                                      task_hours_estimated_id INT NOT NULL AUTO_INCREMENT,
                                      task_hours_estimated FLOAT,
                                      task_id_fk INT,
                                      account_id_fk INT,
                                      PRIMARY KEY (task_hours_estimated_id),
                                      FOREIGN KEY (task_id_fk) REFERENCES task (task_id) ON DELETE CASCADE,
                                      FOREIGN KEY (account_id_fk) REFERENCES accounts (account_id)
);

CREATE TABLE IF NOT EXISTS task_member (
                             task_member_id INT NOT NULL AUTO_INCREMENT,
                             account_id_fk INT,
                             task_id_fk INT,
                             PRIMARY KEY (task_member_id),
                             FOREIGN KEY (account_id_fk) REFERENCES accounts (account_id),
                             FOREIGN KEY (task_id_fk) REFERENCES task (task_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sub_project_member (
                                    sub_project_member_id INT NOT NULL AUTO_INCREMENT,
                                    account_id_fk INT,
                                    sub_project_id_fk INT,
                                    PRIMARY KEY (sub_project_member_id),
                                    FOREIGN KEY (account_id_fk) REFERENCES accounts (account_id),
                                    FOREIGN KEY (sub_project_id_fk) REFERENCES sub_project (sub_project_id)
);



