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
INSERT IGNORE INTO permission VALUE (24, 'CLASSROOM_DELETE', 'classroom:delete');

