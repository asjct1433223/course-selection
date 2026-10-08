-- =============================================
-- 学生选课管理系统 - 测试数据
-- 密码统一为: 123456 (BCrypt加密)
-- data.sql 支持重复执行：先清空旧数据再插入，避免重启冲突
-- 外键统一使用子查询按业务编号动态定位，避免自增主键随重启漂移导致关联失效
-- =============================================

-- 清空旧数据（按外键依赖倒序）
DELETE FROM sc;
DELETE FROM course;
DELETE FROM teacher;
DELETE FROM student;
DELETE FROM sys_user;

-- 用户数据 (密码123456的BCrypt密文)
INSERT INTO sys_user (username, password, real_name, role, phone, email) VALUES
('admin',    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'ADMIN',   '13800000001', 'admin@school.edu.cn'),
('t001',     '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张教授',    'TEACHER', '13800000002', 'zhang@school.edu.cn'),
('t002',     '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李副教授',  'TEACHER', '13800000003', 'li@school.edu.cn'),
('t003',     '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王讲师',    'TEACHER', '13800000004', 'wang@school.edu.cn'),
('t004',     '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵教授',    'TEACHER', '13800000005', 'zhao@school.edu.cn'),
('s2024001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三',      'STUDENT', '13900001001', 'zhangsan@stu.school.edu.cn'),
('s2024002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李四',      'STUDENT', '13900001002', 'lisi@stu.school.edu.cn'),
('s2024003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王五',      'STUDENT', '13900001003', 'wangwu@stu.school.edu.cn'),
('s2024004', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵六',      'STUDENT', '13900001004', 'zhaoliu@stu.school.edu.cn'),
('s2024005', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '孙七',      'STUDENT', '13900001005', 'sunqi@stu.school.edu.cn'),
('s2024006', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '周八',      'STUDENT', '13900001006', 'zhouba@stu.school.edu.cn'),
('s2024007', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '吴九',      'STUDENT', '13900001007', 'wujiu@stu.school.edu.cn'),
('s2024008', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '郑十',      'STUDENT', '13900001008', 'zhengshi@stu.school.edu.cn'),
('s2024009', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '陈一一',    'STUDENT', '13900001009', 'chenyiyi@stu.school.edu.cn'),
('s2024010', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '刘一二',    'STUDENT', '13900001010', 'liuyier@stu.school.edu.cn');

-- 教师数据
INSERT INTO teacher (user_id, teacher_no, name, gender, title, department, phone) VALUES
((SELECT id FROM sys_user WHERE username='t001'), 'T2020001', '张教授',   '男', '教授',   '计算机科学与技术学院', '13800000002'),
((SELECT id FROM sys_user WHERE username='t002'), 'T2020002', '李副教授', '女', '副教授', '软件学院',             '13800000003'),
((SELECT id FROM sys_user WHERE username='t003'), 'T2021001', '王讲师',   '男', '讲师',   '计算机科学与技术学院', '13800000004'),
((SELECT id FROM sys_user WHERE username='t004'), 'T2019001', '赵教授',   '女', '教授',   '软件学院',             '13800000005');

-- 学生数据
INSERT INTO student (user_id, student_no, name, gender, class_name, grade, phone) VALUES
((SELECT id FROM sys_user WHERE username='s2024001'), '2024001', '张三',   '男', '软件工程2401班', '2024', '13900001001'),
((SELECT id FROM sys_user WHERE username='s2024002'), '2024002', '李四',   '女', '软件工程2401班', '2024', '13900001002'),
((SELECT id FROM sys_user WHERE username='s2024003'), '2024003', '王五',   '男', '软件工程2402班', '2024', '13900001003'),
((SELECT id FROM sys_user WHERE username='s2024004'), '2024004', '赵六',   '女', '计算机科学2401班', '2024', '13900001004'),
((SELECT id FROM sys_user WHERE username='s2024005'), '2024005', '孙七',   '男', '计算机科学2401班', '2024', '13900001005'),
((SELECT id FROM sys_user WHERE username='s2024006'), '2024006', '周八',   '女', '软件工程2402班', '2024', '13900001006'),
((SELECT id FROM sys_user WHERE username='s2024007'), '2024007', '吴九',   '男', '软件工程2401班', '2024', '13900001007'),
((SELECT id FROM sys_user WHERE username='s2024008'), '2024008', '郑十',   '女', '计算机科学2402班', '2024', '13900001008'),
((SELECT id FROM sys_user WHERE username='s2024009'), '2024009', '陈一一', '女', '软件工程2402班', '2024', '13900001009'),
((SELECT id FROM sys_user WHERE username='s2024010'), '2024010', '刘一二', '男', '计算机科学2402班', '2024', '13900001010');

-- 课程数据
INSERT INTO course (course_no, name, credit, teacher_id, capacity, selected_num, semester, class_hours, description) VALUES
('CS101', 'Java程序设计',        4.0, (SELECT id FROM teacher WHERE teacher_no='T2020001'), 60, 5, '2025-2026-2', 64, '学习Java语言基础、面向对象编程、集合框架、多线程及网络编程等内容。'),
('CS102', '数据结构与算法',      3.5, (SELECT id FROM teacher WHERE teacher_no='T2020002'), 50, 4, '2025-2026-2', 56, '系统地学习线性表、树、图等数据结构，以及排序、查找等经典算法。'),
('CS103', '数据库原理与应用',    3.0, (SELECT id FROM teacher WHERE teacher_no='T2021001'), 60, 3, '2025-2026-2', 48, '介绍关系数据库基本理论、SQL语言、数据库设计范式及事务管理。'),
('CS104', '软件工程导论',        2.5, (SELECT id FROM teacher WHERE teacher_no='T2019001'), 45, 5, '2025-2026-2', 40, '学习软件生命周期、需求分析、系统设计、软件测试及项目管理等知识。'),
('CS105', '操作系统原理',        3.5, (SELECT id FROM teacher WHERE teacher_no='T2020001'), 50, 4, '2025-2026-2', 56, '学习进程管理、内存管理、文件系统、设备管理等操作系统核心概念。'),
('CS106', '计算机网络',          3.0, (SELECT id FROM teacher WHERE teacher_no='T2020002'), 55, 3, '2025-2026-2', 48, '学习TCP/IP协议栈、网络层协议、传输层协议及应用层协议等。'),
('CS107', 'Web前端开发技术',     2.5, (SELECT id FROM teacher WHERE teacher_no='T2021001'), 50, 6, '2025-2026-2', 40, '学习HTML5、CSS3、JavaScript及Vue.js框架，掌握前端开发技能。'),
('CS108', '人工智能导论',        2.0, (SELECT id FROM teacher WHERE teacher_no='T2019001'), 60, 2, '2025-2026-2', 32, '介绍人工智能基本概念、搜索算法、机器学习基础及深度学习入门。');

-- 选课记录数据
INSERT INTO sc (student_id, course_id, score) VALUES
((SELECT id FROM student WHERE student_no='2024001'), (SELECT id FROM course WHERE course_no='CS101'), 85.5),
((SELECT id FROM student WHERE student_no='2024001'), (SELECT id FROM course WHERE course_no='CS102'), 78.0),
((SELECT id FROM student WHERE student_no='2024001'), (SELECT id FROM course WHERE course_no='CS105'), NULL),
((SELECT id FROM student WHERE student_no='2024002'), (SELECT id FROM course WHERE course_no='CS101'), 92.0),
((SELECT id FROM student WHERE student_no='2024002'), (SELECT id FROM course WHERE course_no='CS102'), 88.5),
((SELECT id FROM student WHERE student_no='2024002'), (SELECT id FROM course WHERE course_no='CS106'), NULL),
((SELECT id FROM student WHERE student_no='2024003'), (SELECT id FROM course WHERE course_no='CS101'), 75.0),
((SELECT id FROM student WHERE student_no='2024003'), (SELECT id FROM course WHERE course_no='CS107'), NULL),
((SELECT id FROM student WHERE student_no='2024004'), (SELECT id FROM course WHERE course_no='CS102'), 90.0),
((SELECT id FROM student WHERE student_no='2024004'), (SELECT id FROM course WHERE course_no='CS103'), 82.0),
((SELECT id FROM student WHERE student_no='2024005'), (SELECT id FROM course WHERE course_no='CS101'), NULL),
((SELECT id FROM student WHERE student_no='2024005'), (SELECT id FROM course WHERE course_no='CS103'), NULL),
((SELECT id FROM student WHERE student_no='2024006'), (SELECT id FROM course WHERE course_no='CS104'), NULL),
((SELECT id FROM student WHERE student_no='2024006'), (SELECT id FROM course WHERE course_no='CS107'), NULL),
((SELECT id FROM student WHERE student_no='2024007'), (SELECT id FROM course WHERE course_no='CS104'), NULL),
((SELECT id FROM student WHERE student_no='2024007'), (SELECT id FROM course WHERE course_no='CS107'), NULL),
((SELECT id FROM student WHERE student_no='2024007'), (SELECT id FROM course WHERE course_no='CS108'), NULL),
((SELECT id FROM student WHERE student_no='2024008'), (SELECT id FROM course WHERE course_no='CS105'), NULL),
((SELECT id FROM student WHERE student_no='2024008'), (SELECT id FROM course WHERE course_no='CS106'), NULL),
((SELECT id FROM student WHERE student_no='2024009'), (SELECT id FROM course WHERE course_no='CS104'), NULL),
((SELECT id FROM student WHERE student_no='2024009'), (SELECT id FROM course WHERE course_no='CS107'), NULL),
((SELECT id FROM student WHERE student_no='2024010'), (SELECT id FROM course WHERE course_no='CS103'), NULL),
((SELECT id FROM student WHERE student_no='2024010'), (SELECT id FROM course WHERE course_no='CS108'), NULL);
