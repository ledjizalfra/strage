--INSERT IGNORE INTO natural_key(id, ) VALUES (1, null);

INSERT IGNORE INTO role VALUE (1, 'ADMIN');
INSERT IGNORE INTO role VALUE (2, 'PROFESSOR');
INSERT IGNORE INTO role VALUE (3, 'STUDENT');

--THE DEFAULT PERMISSION
INSERT IGNORE INTO permission VALUE (1, 'ROLE_ADMIN', 'ADMIN');
INSERT IGNORE INTO permission VALUE (2, 'ROLE_PROFESSOR', 'PROFESSOR');
INSERT IGNORE INTO permission VALUE (3, 'ROLE_STUDENT', 'STUDENT');

--ROLE
INSERT IGNORE INTO permission VALUE (4, 'ROLE_READ', 'role:read');

--PERMISSION
INSERT IGNORE INTO permission VALUE (5, 'PERMISSION_READ', 'permission:read');

--USER
INSERT IGNORE INTO permission VALUE (6, 'USER_CREATE', 'user:create');
INSERT IGNORE INTO permission VALUE (7, 'USER_READ', 'user:read');
INSERT IGNORE INTO permission VALUE (8, 'USER_UPDATE', 'user:update');
INSERT IGNORE INTO permission VALUE (9, 'USER_DELETE', 'user:delete');

--ADMIN
INSERT IGNORE INTO permission VALUE (10, 'ADMIN_CREATE', 'admin:create');
INSERT IGNORE INTO permission VALUE (11, 'ADMIN_READ', 'admin:read');
INSERT IGNORE INTO permission VALUE (12, 'ADMIN_UPDATE', 'admin:update');
INSERT IGNORE INTO permission VALUE (13, 'ADMIN_DELETE', 'admin:delete');

--PROFESSOR
INSERT IGNORE INTO permission VALUE (14, 'PROFESSOR_CREATE', 'professor:create');
INSERT IGNORE INTO permission VALUE (15, 'PROFESSOR_READ', 'professor:read');
INSERT IGNORE INTO permission VALUE (16, 'PROFESSOR_UPDATE', 'professor:update');
INSERT IGNORE INTO permission VALUE (17, 'PROFESSOR_DELETE', 'professor:delete');

--STUDENT
INSERT IGNORE INTO permission VALUE (18, 'STUDENT_CREATE', 'student:create');
INSERT IGNORE INTO permission VALUE (19, 'STUDENT_READ', 'student:read');
INSERT IGNORE INTO permission VALUE (20, 'STUDENT_UPDATE', 'student:update');
INSERT IGNORE INTO permission VALUE (21, 'STUDENT_DELETE', 'student:delete');

--CLASSROOM
INSERT IGNORE INTO permission VALUE (22, 'CLASSROOM_CREATE', 'classroom:create');
INSERT IGNORE INTO permission VALUE (23, 'CLASSROOM_READ', 'classroom:read');
INSERT IGNORE INTO permission VALUE (24, 'CLASSROOM_UPDATE', 'classroom:update');
INSERT IGNORE INTO permission VALUE (25, 'CLASSROOM_DELETE', 'classroom:delete');

--SUBJECT
INSERT IGNORE INTO permission VALUE (26, 'SUBJECT_CREATE', 'subject:create');
INSERT IGNORE INTO permission VALUE (27, 'SUBJECT_READ', 'subject:read');
INSERT IGNORE INTO permission VALUE (28, 'SUBJECT_UPDATE', 'subject:update');
INSERT IGNORE INTO permission VALUE (29, 'SUBJECT_DELETE', 'subject:delete');

--ARGUMENT
INSERT IGNORE INTO permission VALUE (30, 'ARGUMENT_CREATE', 'argument:create');
INSERT IGNORE INTO permission VALUE (31, 'ARGUMENT_READ', 'argument:read');
INSERT IGNORE INTO permission VALUE (32, 'ARGUMENT_UPDATE', 'argument:update');
INSERT IGNORE INTO permission VALUE (33, 'ARGUMENT_DELETE', 'argument:delete');

--QUESTION
INSERT IGNORE INTO permission VALUE (34, 'QUESTION_CREATE', 'question:create');
INSERT IGNORE INTO permission VALUE (35, 'QUESTION_READ', 'question:read');
INSERT IGNORE INTO permission VALUE (36, 'QUESTION_UPDATE', 'question:update');
INSERT IGNORE INTO permission VALUE (37, 'QUESTION_DELETE', 'question:delete');

--ANSWER
INSERT IGNORE INTO permission VALUE (38, 'ANSWER_CREATE', 'answer:create');
INSERT IGNORE INTO permission VALUE (39, 'ANSWER_READ', 'answer:read');
INSERT IGNORE INTO permission VALUE (40, 'ANSWER_UPDATE', 'answer:update');
INSERT IGNORE INTO permission VALUE (41, 'ANSWER_DELETE', 'answer:delete');

